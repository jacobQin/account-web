/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	EPPayDepositRepayment.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentServiceAsync;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.PayDepositRepaymentCreatePage;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.PayDepositRepaymentEditPage;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.PayDepositRepaymentLogViewPage;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.PayDepositRepaymentSearchPage;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.PayDepositRepaymentViewPage;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.PayDepositRepaymentUrlParams;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.perm.PayDepositRepaymentPermDef;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
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
 * @author zhuhairui
 * 
 */
public class EPPayDepositRepayment extends AccEPBpmModule {
  public static final String OPN_ENTITY = "payDepositRepayment";

  public static EPPayDepositRepayment getInstance() {
    return EPPayDepositRepayment.getInstance(EPPayDepositRepayment.class);
  }

  @Override
  public String getModuleCaption() {
    return PayDepositRepaymentUrlParams.MODULE_CAPTION;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = PayDepositRepaymentSearchPage.START_NODE;

      if (PayDepositRepaymentSearchPage.START_NODE.equals(start)) {
        showContentPage(PayDepositRepaymentSearchPage.getInstance(), urlParams);
      } else if (PayDepositRepaymentCreatePage.START_NODE.equals(start)) {
        showContentPage(PayDepositRepaymentCreatePage.getInstance(), urlParams);
      } else if (PayDepositRepaymentEditPage.START_NODE.equals(start)) {
        showContentPage(PayDepositRepaymentEditPage.getInstance(), urlParams);
      } else if (PayDepositRepaymentViewPage.START_NODE.equals(start)) {
        showContentPage(PayDepositRepaymentViewPage.getInstance(), urlParams);
      } else if (PayDepositRepaymentLogViewPage.START_NODE.equals(start)) {
        showContentPage(PayDepositRepaymentLogViewPage.getInstance(), urlParams);
      }
    } catch (Exception e) {
      String msg = DepositRepaymentMessage.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public PayDepositRepaymentServiceAsync getModuleService() {
    return PayDepositRepaymentService.Locator.getService();
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return PayDepositRepaymentPermDef.PRINT_PATH;
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
        JumpParameters params = PayDepositRepaymentSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }

      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(PayDepositRepaymentSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(DepositRepaymentMessage.M.actionDoing(DepositRepaymentMessage.M.search()));

    PayDepositRepaymentService.Locator.getService().loadByNumber(keyword,
        new RBAsyncCallback2<BDepositRepayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(PayDepositRepaymentSearchPage.START_NODE);
            params.getUrlRef().set(PayDepositRepaymentSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          public void onSuccess(BDepositRepayment result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(PayDepositRepaymentSearchPage.START_NODE);
              params.getUrlRef().set(PayDepositRepaymentSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(PayDepositRepaymentViewPage.START_NODE);
              params.getUrlRef().set(PayDepositRepaymentViewPage.PN_ENTITY_UUID, result.getUuid());
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
      PayDepositRepaymentService.Locator.getService().getPaymentTypes(
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
      PayDepositRepaymentService.Locator.getService().getBanks(new RBAsyncCallback2<List<BBank>>() {

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
      PayDepositRepaymentService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
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
    return PayDepositRepaymentPermDef.RESOURCE_PAYDEPOSITREPAYMENT;
  }
}
