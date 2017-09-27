/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	EPStatementAdjust.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client;

import java.math.RoundingMode;
import java.util.Map;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustService;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustServiceAsync;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.StatementAdjustCreatePage;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.StatementAdjustEditPage;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.StatementAdjustLogViewPage;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.StatementAdjustSearchPage;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.StatementAdjustViewPage;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class EPStatementAdjust extends AccEPBpmModule {

  public static final String OPN_ENTITY = "statementAdjust";

  public static EPStatementAdjust getInstance() {
    return EPStatementAdjust.getInstance(EPStatementAdjust.class);
  }

  @Override
  public String getModuleCaption() {
    return PStatementAdjustDef.TABLE_CAPTION;
  }

  @Override
  public StatementAdjustServiceAsync getModuleService() {
    return StatementAdjustService.Locator.getService();
  }

  /** 单据类型 */
  public Map<String, String> getBillType() {
    String billTypeString = getModuleContext().get(StatementAdjustService.KEY_BILLTYPES);
    return CollectionUtil.toMap(billTypeString);
  }

  /** 账单调整单单据类型 */
  public BBillType getAdjBillType() {
    GWTUtil.enableSynchronousRPC();
    if (billType != null)
      return billType;

    StatementAdjustService.Locator.getService().getBillType(new RBAsyncCallback2<BBillType>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.get(),
            StatementAdjustMessages.M.billType());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BBillType result) {
        billType = result;
      }
    });
    return billType;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = StatementAdjustSearchPage.START_NODE;

      if (StatementAdjustSearchPage.START_NODE.equals(start))
        showContentPage(StatementAdjustSearchPage.getInstance(), urlParams);
      else if (StatementAdjustCreatePage.START_NODE.equals(start))
        showContentPage(StatementAdjustCreatePage.getInstance(), urlParams);
      else if (StatementAdjustEditPage.START_NODE.equals(start))
        showContentPage(StatementAdjustEditPage.getInstance(), urlParams);
      else if (StatementAdjustViewPage.START_NODE.equals(start))
        showContentPage(StatementAdjustViewPage.getInstance(), urlParams);
      else if (StatementAdjustLogViewPage.START_NODE.equals(start))
        showContentPage(StatementAdjustLogViewPage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = StatementAdjustMessages.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  protected void onCreate() {
    super.onCreate();

    searchBox = new RSearchBox();
    searchBox.addSearchHandler(new SearchBoxHandler());
    appendSearchBox();
  }

  public void appendSearchBox() {
    getTitleBar().add(new RToolbarSeparator(), RToolbar.ALIGN_RIGHT);

    getTitleBar().add(searchBox, RToolbar.ALIGN_RIGHT);

  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return StatementAdjustPermDef.PRINT_PATH;
  }

  private BBillType billType;
  private RSearchBox searchBox;

  private class SearchBoxHandler implements RSearchHandler {

    @Override
    public void onSearch(RSearchEvent event) {

      if (event.isBack()) {
        try {
          JumpParameters params = StatementAdjustSearchPage.getInstance().getLastJumpParams();
          jump(params);
        } catch (ClientBizException e) {
          RMsgBox.showError(e);
        }
        return;
      }

      if (event.isEmpty()) {
        try {
          JumpParameters params = new JumpParameters(StatementAdjustSearchPage.START_NODE);
          jump(params);
        } catch (Exception e) {
          RMsgBox.showError(e);
        }
        return;
      }

      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M.search()));

    StatementAdjustService.Locator.getService().loadByNumber(keyword, null,
        new RBAsyncCallback2<BStatementAdjust>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(StatementAdjustSearchPage.START_NODE);
            params.getUrlRef().set(StatementAdjustSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          @Override
          public void onSuccess(BStatementAdjust result) {
            RLoadingDialog.hide();
            if (result == null) {
              JumpParameters params = new JumpParameters();
              params.setStart(StatementAdjustSearchPage.START_NODE);
              params.getUrlRef().set(StatementAdjustSearchPage.PN_KEYWORD, keyword);
              jump(params);
            } else {
              jumpToViewPage(result.getUuid());
            }
          }
        });
  }

  @Override
  public String getPermResource() {
    return StatementAdjustPermDef.RESOURCE_STATEMENTADJUST;
  }

  /** 获取选项配置小数精度 */
  public int getScale() {
    return Integer.valueOf(getModuleContext().get(StatementAdjustService.SCALE));
  }

  /** 获取选项配置舍入算法 */
  public RoundingMode getRoundingMode() {
    return RoundingMode.valueOf(getModuleContext().get(StatementAdjustService.ROUNDING_MODE));
  }
}
