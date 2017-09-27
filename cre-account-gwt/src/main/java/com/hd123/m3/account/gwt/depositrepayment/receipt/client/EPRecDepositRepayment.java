/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	EPPayDepositRepayment.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentServiceAsync;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.RecDepositRepaymentCreatePage;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.RecDepositRepaymentEditPage;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.RecDepositRepaymentLogViewPage;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.RecDepositRepaymentSearchPage;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.RecDepositRepaymentViewPage;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.RecDepositRepaymentUrlParams;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.perm.RecDepositRepaymentPermDef;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenpeisi
 * 
 */
public class EPRecDepositRepayment extends AccEPBpmModule {
  public static final String OPN_ENTITY = "recDepositRepayment";

  public static EPRecDepositRepayment getInstance() {
    return EPRecDepositRepayment.getInstance(EPRecDepositRepayment.class);
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = RecDepositRepaymentSearchPage.START_NODE;

      if (RecDepositRepaymentSearchPage.START_NODE.equals(start)) {
        showContentPage(RecDepositRepaymentSearchPage.getInstance(), urlParams);
      } else if (RecDepositRepaymentCreatePage.START_NODE.equals(start)) {
        showContentPage(RecDepositRepaymentCreatePage.getInstance(), urlParams);
      } else if (RecDepositRepaymentEditPage.START_NODE.equals(start)) {
        showContentPage(RecDepositRepaymentEditPage.getInstance(), urlParams);
      } else if (RecDepositRepaymentViewPage.START_NODE.equals(start)) {
        showContentPage(RecDepositRepaymentViewPage.getInstance(), urlParams);
      } else if (RecDepositRepaymentLogViewPage.START_NODE.equals(start)) {
        showContentPage(RecDepositRepaymentLogViewPage.getInstance(), urlParams);
      }
    } catch (Exception e) {
      String msg = DepositRepaymentMessage.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public RecDepositRepaymentServiceAsync getModuleService() {
    return RecDepositRepaymentService.Locator.getService();
  }

  @Override
  public String getModuleCaption() {
    return RecDepositRepaymentUrlParams.MODULE_CAPTION;
  }

  /**
   * 是否启用还款单按单还款
   * 
   * @return
   */
  public boolean isEnableByDeposit() {
    return StringUtil.toBoolean(getModuleContext().get("isEnableByDeposit"));
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return RecDepositRepaymentPermDef.PRINT_PATH;
  }

  private RSearchBox searchBox;

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

  private class SearchBoxHandler implements RSearchHandler {
    @Override
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        JumpParameters params = RecDepositRepaymentSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(RecDepositRepaymentSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(DepositRepaymentMessage.M.actionDoing(DepositRepaymentMessage.M.search()));
    RecDepositRepaymentService.Locator.getService().loadByNumber(keyword,
        new RBAsyncCallback2<BDepositRepayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(RecDepositRepaymentSearchPage.START_NODE);
            params.getUrlRef().set(RecDepositRepaymentSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          public void onSuccess(BDepositRepayment result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(RecDepositRepaymentSearchPage.START_NODE);
              params.getUrlRef().set(RecDepositRepaymentSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(RecDepositRepaymentViewPage.START_NODE);
              params.getUrlRef().set(RecDepositRepaymentViewPage.PN_ENTITY_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  private List<BUCN> paymentTypes;

  /** 付款方式 */
  public List<BUCN> getPaymentTypes() {
    GWTUtil.enableSynchronousRPC();
    if (paymentTypes == null) {
      RecDepositRepaymentService.Locator.getService().getPaymentTypes(
          new RBAsyncCallback2<List<BUCN>>() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = DepositRepaymentMessage.M.processError(DepositRepaymentMessage.M
                  .getPayments());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(List<BUCN> result) {
              if (result == null)
                paymentTypes = new ArrayList<BUCN>();
              else
                paymentTypes = result;
            }
          });
    }
    return paymentTypes;
  }

  private List<BBank> banks;

  /** 银行资料 */
  public List<BBank> getBanks() {
    GWTUtil.enableSynchronousRPC();
    if (banks == null) {
      RecDepositRepaymentService.Locator.getService().getBanks(new RBAsyncCallback2<List<BBank>>() {

        @Override
        public void onException(Throwable caught) {
          RLoadingDialog.hide();
          String msg = DepositRepaymentMessage.M.processError(DepositRepaymentMessage.M
              .getPayments());
          RMsgBox.showError(msg, caught);
        }

        @Override
        public void onSuccess(List<BBank> result) {
          if (result == null)
            banks = new ArrayList<BBank>();
          else
            banks = result;
        }
      });

    }

    return banks;
  }

  /** 当前员工 */
  private BUCN currentEmployee;

  public BUCN getCurrentEmployee() {
    if (currentEmployee == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(DepositRepaymentMessage.M.actionDoing(DepositRepaymentMessage.M.load()
          + DepositRepaymentMessage.M.currentEmployee()));
      RecDepositRepaymentService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
          new RBAsyncCallback2<BUCN>() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = DepositRepaymentMessage.M.actionFailed(DepositRepaymentMessage.M.load(),
                  DepositRepaymentMessage.M.currentEmployee());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(BUCN result) {
              RLoadingDialog.hide();
              currentEmployee = result;
            }
          });
    }
    return currentEmployee;
  }

  @Override
  public String getPermResource() {
    return RecDepositRepaymentPermDef.RESOURCE_RECDEPOSITREPAYMENT;
  }
}
