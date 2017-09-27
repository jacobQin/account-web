/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayActionName.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.client;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.payment.client.rpc.PayDepositService;
import com.hd123.m3.account.gwt.deposit.payment.client.rpc.PayDepositServiceAsync;
import com.hd123.m3.account.gwt.deposit.payment.client.ui.page.PayDepositCreatePage;
import com.hd123.m3.account.gwt.deposit.payment.client.ui.page.PayDepositEditPage;
import com.hd123.m3.account.gwt.deposit.payment.client.ui.page.PayDepositLogViewPage;
import com.hd123.m3.account.gwt.deposit.payment.client.ui.page.PayDepositSearchPage;
import com.hd123.m3.account.gwt.deposit.payment.client.ui.page.PayDepositViewPage;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.PayDepositUrlParams;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.perm.PayDepositPermDef;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

public class EPPayDeposit extends AccEPBpmModule {
  public static final String OPN_ENTITY = "payDeposit ";

  public static EPPayDeposit getInstance() {
    return EPPayDeposit.getInstance(EPPayDeposit.class);
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      getTitleBar().rebuild();

      if (start == null)
        start = PayDepositSearchPage.START_NODE;

      if (PayDepositSearchPage.START_NODE.equals(start)) {
        showContentPage(PayDepositSearchPage.getInstance(), urlParams);
      } else if (PayDepositCreatePage.START_NODE.equals(start)) {
        showContentPage(PayDepositCreatePage.getInstance(), urlParams);
      } else if (PayDepositEditPage.START_NODE.equals(start)) {
        showContentPage(PayDepositEditPage.getInstance(), urlParams);
      } else if (PayDepositViewPage.START_NODE.equals(start)) {
        showContentPage(PayDepositViewPage.getInstance(), urlParams);
      } else if (PayDepositLogViewPage.START_NODE.equals(start)) {
        showContentPage(PayDepositLogViewPage.getInstance(), urlParams);
      }
    } catch (Exception e) {
      String msg = DepositMessage.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public String getModuleCaption() {
    return PayDepositUrlParams.MODULE_CAPTION;
  }

  @Override
  public PayDepositServiceAsync getModuleService() {
    return PayDepositService.Locator.getService();
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return PayDepositPermDef.PRINT_PATH;
  }

  private List<BUCN> paymentTypes;

  /** 付款方式 */
  public List<BUCN> getPaymentTypes() {
    GWTUtil.enableSynchronousRPC();
    if (paymentTypes == null) {
      PayDepositService.Locator.getService().getPaymentTypes(new RBAsyncCallback2<List<BUCN>>() {

        @Override
        public void onException(Throwable caught) {
          RLoadingDialog.hide();
          String msg = DepositMessage.M.actionFailed(DepositMessage.M.get(),
              DepositMessage.M.paymentType());
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
      PayDepositService.Locator.getService().getBanks(new RBAsyncCallback2<List<BBank>>() {

        @Override
        public void onException(Throwable caught) {
          RLoadingDialog.hide();
          String msg = DepositMessage.M.actionFailed(DepositMessage.M.get(),
              DepositMessage.M.bank());
          RMsgBox.showError(msg, caught);
        }

        @Override
        public void onSuccess(List<BBank> result) {
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
      RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.load()
          + DepositMessage.M.currentEmployee()));
      PayDepositService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
          new RBAsyncCallback2<BUCN>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = DepositMessage.M.actionFailed(DepositMessage.M.load(),
                  DepositMessage.M.currentEmployee());
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
        JumpParameters params = PayDepositSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(PayDepositSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.search()));
    PayDepositService.Locator.getService().loadByNumber(keyword, new RBAsyncCallback2<BDeposit>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        JumpParameters params = new JumpParameters();
        params.setStart(PayDepositSearchPage.START_NODE);
        params.getUrlRef().set(PayDepositSearchPage.PN_KEYWORD, keyword);
        jump(params);
      }

      public void onSuccess(BDeposit result) {
        RLoadingDialog.hide();
        JumpParameters params = new JumpParameters();
        if (result == null) {
          params.setStart(PayDepositSearchPage.START_NODE);
          params.getUrlRef().set(PayDepositSearchPage.PN_KEYWORD, keyword);
        } else {
          params.setStart(PayDepositViewPage.START_NODE);
          params.getUrlRef().set(PayDepositViewPage.PN_UUID, result.getUuid());
        }
        jump(params);
      }
    });
  }

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(
      PayDepositUrlParams.Flecs.FIELD_COUNTERPART,  GRes.R.counterpart());
  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      PayDepositUrlParams.Flecs.FIELD_COUNTERPARTTYPE, GRes.R.counterpartType());
  public static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(
      PayDepositUrlParams.Flecs.FIELD_PERMGROUP, DepositMessage.M.permGroup());
  public static DateFieldDef depositDate = new DateFieldDef(
      PayDepositUrlParams.Flecs.FIELD_DEPOSITDATE, DepositMessage.M.paymentDate(), true, null,
      true, null, true, GWTFormat.fmt_yMd, false);

  @Override
  public String getPermResource() {
    return PayDepositPermDef.RESOURCE_PAYDEPOSIT;
  }
}
