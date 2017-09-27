/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： EPStatement.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.PayInvoiceRegUrlParams;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegUrlParams;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams;
import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.AccountDefrayalUrlParams;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementServiceAsync;
import com.hd123.m3.account.gwt.statement.client.ui.StatementAccSettleLogPage;
import com.hd123.m3.account.gwt.statement.client.ui.StatementAccViewPage;
import com.hd123.m3.account.gwt.statement.client.ui.StatementCreatePage;
import com.hd123.m3.account.gwt.statement.client.ui.StatementEditPage;
import com.hd123.m3.account.gwt.statement.client.ui.StatementEditPatchPage;
import com.hd123.m3.account.gwt.statement.client.ui.StatementLogPage;
import com.hd123.m3.account.gwt.statement.client.ui.StatementSearchPage;
import com.hd123.m3.account.gwt.statement.client.ui.StatementViewPage;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
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
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author huangjunxian
 * 
 */
public class EPStatement extends AccEPBpmModule {
  private static EPStatement instance = null;
  public static final String OPN_ENTITY = "open_entity";
  public static final String OPN_LINE = "open_line";
  private List<String> coopModes;

  public static EPStatement getInstance() {
    return instance;
  }

  public List<String> getBuildingTypes() {
    String types = getModuleContext().get(StatementService.KEY_BUILDING_TYPE);
    if (types == null) {
      return new ArrayList<String>();
    }
    return CollectionUtil.toList(types);
  }

  public EPStatement() throws ClientBizException {
    super();
    instance = this;
  }

  public native void log(String text)
  /*-{ 
      $wnd.console.log(text); 
  }-*/;

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null) {
        start = StatementSearchPage.START_NODE;
      }

      if (StatementSearchPage.START_NODE.equals(start)) {
        log("进入模块:" + getModuleCaption() + ",时间" + GWTFormat.fmt_yMdHmsS.format(new Date()));
        showContentPage(StatementSearchPage.getInstance(), urlParams);
      } else if (StatementViewPage.START_NODE.equals(start))
        showContentPage(StatementViewPage.getInstance(), urlParams);
      else if (StatementLogPage.START_NODE.equals(start))
        showContentPage(StatementLogPage.getInstance(), urlParams);
      else if (StatementAccViewPage.START_NODE.equals(start))
        showContentPage(StatementAccViewPage.getInstance(), urlParams);
      else if (StatementAccSettleLogPage.START_NODE.equals(start))
        showContentPage(StatementAccSettleLogPage.getInstance(), urlParams);
      else if (StatementCreatePage.START_NODE.equals(start))
        showContentPage(StatementCreatePage.getInstance(), urlParams);
      else if (StatementEditPage.START_NODE.equals(start))
        showContentPage(StatementEditPage.getInstance(), urlParams);
      else if (StatementEditPatchPage.START_NODE.equals(start))
        showContentPage(StatementEditPatchPage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = CommonsMessages.M.processError(CommonsMessages.M.show());
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public String getModuleCaption() {
    return StatementUrlParams.MODULE_CAPTION;
  }

  @Override
  public StatementServiceAsync getModuleService() {
    return StatementService.Locator.getService();
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
    return StatementPermDef.PRINT_PATH;
  }

  private RSearchBox searchBox;

  private class SearchBoxHandler implements RSearchHandler {

    @Override
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        JumpParameters params = StatementSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(StatementSearchPage.START_NODE);
        jump(params);
      } else {
        doSearch(event.getKeyword());
      }
    }
  }

  public List<String> getCoopModes() {
    if (coopModes == null) {
      coopModes = CollectionUtil.toList(getModuleContext().get(StatementService.KEY_COOPMODES));
    }
    return coopModes;
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(StatementMessages.M.actionDoing(StatementMessages.M.search()));
    StatementService.Locator.getService().loadByNumber(keyword, StatementUrlParams.View.START_NODE,
        new RBAsyncCallback2<BStatement>() {

          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(StatementSearchPage.START_NODE);
            params.getUrlRef().set(StatementSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          @Override
          public void onSuccess(BStatement result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(StatementSearchPage.START_NODE);
              params.getUrlRef().set(StatementSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(StatementViewPage.START_NODE);
              params.getUrlRef().set(StatementViewPage.PN_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  private BBillType billType;
  private Map<String, String> billTypeMap;

  /** 账单调整单单据类型 */
  public BBillType getAdjBillType() {
    GWTUtil.enableSynchronousRPC();
    if (billType != null)
      return billType;

    StatementService.Locator.getService().getBillType(new RBAsyncCallback2<BBillType>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = StatementMessages.M.actionFailed(StatementMessages.M.get(),
            StatementMessages.M.billType());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BBillType result) {
        billType = result;
      }
    });
    return billType;
  }

  /** 单据类型 */
  public Map<String, String> getBillType() {
    GWTUtil.enableSynchronousRPC();
    if (billTypeMap != null)
      return billTypeMap;

    StatementService.Locator.getService().getBillTypeMap(
        new RBAsyncCallback2<Map<String, String>>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.get(),
                StatementMessages.M.billType());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Map<String, String> result) {
            if (result == null)
              billTypeMap = new HashMap<String, String>();
            else
              billTypeMap = result;
          }
        });
    return billTypeMap;
  }

  public void doPaymentIvc(BStatement entity) {
    GwtUrl url = PayInvoiceRegUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, PayInvoiceRegUrlParams.Create.START_NODE);
    url.getQuery().set(PayInvoiceRegUrlParams.Create.PN_STATEMENT_UUID, entity.getUuid());
    RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
  }

  public void doReceiptIvc(BStatement entity) {
    GwtUrl url = InvoiceRegUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, InvoiceRegUrlParams.START_NODE_CREATE);
    url.getQuery().set(InvoiceRegUrlParams.KEY_CREATE_BY_STATEMENT, entity.getUuid());
    RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
  }

  public void doPayment(BStatement entity) {
    GwtUrl url = PaymentUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, PaymentUrlParams.Create.START_NODE);
    url.getQuery().set(PaymentUrlParams.Create.PN_CREATEBYSTATEMENT, entity.getUuid());
    RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
  }

  public void doReceipt(BStatement entity) {
    GwtUrl url = ReceiptUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, ReceiptUrlParams.Create.START_NODE);
    url.getQuery().set(ReceiptUrlParams.Create.PN_CREATEBYSTATEMENT, entity.getUuid());
    RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
  }

  public void doAdjust(BStatement entity) {
    GwtUrl url = StatementAdjustUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, StatementAdjustUrlParams.Create.START_NODE);
    url.getQuery().set(StatementAdjustUrlParams.Create.PN_STATEMENT_UUID, entity.getUuid());
    RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
  }

  public void doSubjectInfo(BStatement entity) {
    GwtUrl url = AccountDefrayalUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, AccountDefrayalUrlParams.Search.START_NODE);
    url.getQuery().set(AccountDefrayalUrlParams.Search.KEY_STATEMENTNUMBER, entity.getBillNumber());
    RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
  }

  @Override
  public String getPermResource() {
    return StatementPermDef.RESOURCE_STATEMENT;
  }

  public static String buildDateRangeStr(BDateRange dateRange) {
    if (dateRange == null)
      return null;
    StringBuilder sb = new StringBuilder();
    if (dateRange.getBeginDate() != null)
      sb.append(M3Format.fmt_yMd.format(dateRange.getBeginDate()));
    sb.append("~");
    if (dateRange.getEndDate() != null)
      sb.append(M3Format.fmt_yMd.format(dateRange.getEndDate()));
    return sb.toString();
  }

  /** 获取选项配置小数精度 */
  public int getScale() {
    return Integer.valueOf(getModuleContext().get(StatementService.SCALE));
  }

  /** 获取选项配置舍入算法 */
  public RoundingMode getRoundingMode() {
    return RoundingMode.valueOf(getModuleContext().get(StatementService.ROUNDING_MODE));
  }
}
