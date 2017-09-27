/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	EPReceive.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByBillBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByInvoiceRegBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByPaymentNoticeBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByStatementBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.pay.client.biz.BPaymentConfig;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentService;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentServiceAsync;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentCreatePage;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentEditPage;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentIvcRegPage;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentIvcRegViewPage;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentLogViewPage;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentSearchPage;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentViewPage;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountInvoice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author subinzhu
 * 
 */
public class EPPayment extends AccEPBpmModule {
  public static final String OPN_ENTITY = "enitity";

  private Map<String, BBillType> billTypeMap;
  private BDefaultOption defaultOption;
  private List<BUCN> paymentTypes;
  private List<BBank> banks;
  private BUCN currentEmployee;
  private BPaymentConfig config;
  private Map<String, String> invoiceTypeMap;
  private List<String> coopModes;

  public static EPPayment getInstance() {
    return EPPayment.getInstance(EPPayment.class);
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = PaymentSearchPage.START_NODE;

      if (PaymentSearchPage.START_NODE.equals(start))
        showContentPage(PaymentSearchPage.getInstance(), urlParams);
      else if (PaymentCreatePage.START_NODE.equals(start))
        showContentPage(PaymentCreatePage.getInstance(), urlParams);
      else if (PaymentViewPage.START_NODE.equals(start))
        showContentPage(PaymentViewPage.getInstance(), urlParams);
      else if (PaymentEditPage.START_NODE.equals(start))
        showContentPage(PaymentEditPage.getInstance(), urlParams);
      else if (PaymentLogViewPage.START_NODE.equals(start))
        showContentPage(PaymentLogViewPage.getInstance(), urlParams);
      else if (PaymentIvcRegPage.START_NODE.equals(start))
        showContentPage(PaymentIvcRegPage.getInstance(), urlParams);
      else if (PaymentIvcRegViewPage.START_NODE.equals(start))
        showContentPage(PaymentIvcRegViewPage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = CommonsMessages.M.processError(CommonsMessages.M.show());
      RMsgBox.showError(msg, e);
    }
  }

  public PaymentServiceAsync getModuleService() {
    return PaymentService.Locator.getService();
  }

  @Override
  public String getModuleCaption() {
    return PaymentMessages.M.payment();
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return PaymentPermDef.PRINT_PATH;
  }

  private RSearchBox searchBox;

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
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        JumpParameters params = PaymentSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        jump(PaymentSearchPage.START_NODE);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.search()));

    PaymentService.Locator.getService().loadByNumber(keyword, false,
        new RBAsyncCallback2<BPayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(PaymentSearchPage.START_NODE);
            params.getUrlRef().set(PaymentSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(PaymentSearchPage.START_NODE);
              params.getUrlRef().set(PaymentSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(PaymentViewPage.START_NODE);
              params.getUrlRef().set(PaymentViewPage.PN_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  /** 单据类型map */
  public Map<String, BBillType> getBillTypeMap() {
    if (billTypeMap == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.load()
          + PaymentMessages.M.billType()));
      PaymentCommonsService.Locator.getService().getBillTypes(
          DirectionType.payment.getDirectionValue(), new RBAsyncCallback2<List<BBillType>>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.load(),
                  PaymentMessages.M.billType());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(List<BBillType> result) {
              RLoadingDialog.hide();
              billTypeMap = new HashMap<String, BBillType>();
              for (BBillType type : result) {
                billTypeMap.put(type.getName(), type);
              }
            }
          });
    }
    return billTypeMap;
  }

  public List<String> getCoopModes() {
    if (coopModes == null) {
      coopModes = CollectionUtil.toList(getModuleContext().get(PaymentService.KEY_COOPMODES));
    }
    return coopModes;
  }

  /** 获取默认配置 */
  public BDefaultOption getDefaultOption() {
    if (defaultOption == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.load()
          + PaymentMessages.M.defaultOption()));
      PaymentCommonsService.Locator.getService().getDefaultOption(
          new RBAsyncCallback2<BDefaultOption>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.load(),
                  PaymentMessages.M.defaultOption());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(BDefaultOption result) {
              RLoadingDialog.hide();
              defaultOption = result;
            }
          });
    }
    return defaultOption;
  }

  /** 获取所有可用的付款方式 */
  public List<BUCN> getPaymentTypes() {
    if (paymentTypes == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.load()
          + PaymentMessages.M.paymentType()));
      PaymentCommonsService.Locator.getService().getPaymentTypes(
          new RBAsyncCallback2<List<BUCN>>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.load(),
                  PaymentMessages.M.paymentType());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(List<BUCN> result) {
              RLoadingDialog.hide();
              paymentTypes = result;
            }
          });
    }
    return paymentTypes;
  }

  /** 获取所有银行 */
  public List<BBank> getBanks() {
    if (banks == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.load()
          + PaymentMessages.M.banks()));
      PaymentCommonsService.Locator.getService().getBanks(new RBAsyncCallback2<List<BBank>>() {
        @Override
        public void onException(Throwable caught) {
          RLoadingDialog.hide();
          String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.load(),
              PaymentMessages.M.banks());
          RMsgBox.showError(msg, caught);
        }

        @Override
        public void onSuccess(List<BBank> result) {
          RLoadingDialog.hide();
          banks = result;
        }
      });
    }
    return banks;
  }

  public BUCN getCurrentEmployee() {
    if (currentEmployee == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.load()
          + PaymentMessages.M.currentEmployee()));
      PaymentCommonsService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
          new RBAsyncCallback2<BUCN>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.load(),
                  PaymentMessages.M.currentEmployee());
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
    return PaymentPermDef.RESOURCE_PAYMENT;
  }

  private RAction createAction;
  private RAction statementCreateAction;
  private RAction invoiceRegCreateAction;
  private RAction paymentNoticeCreateAction;
  private RAction sourceBillCreateAction;
  private RAction accountCreateAction;

  @Override
  public List<RAction> getCreateActions() {
    List<RAction> result = new ArrayList<RAction>();
    if (isPermitted(BAction.CREATE.getKey())) {
      if (createAction == null) {
        createAction = new RAction(RActionFacade.CREATE, clickHandler);
      }
      if (statementCreateAction == null) {
        statementCreateAction = new RAction(PaymentMessages.M.statementImport(), clickHandler);
      }
      if (invoiceRegCreateAction == null) {
        invoiceRegCreateAction = new RAction(PaymentMessages.M.invoiceRegImport(), clickHandler);
      }
      if (paymentNoticeCreateAction == null) {
        paymentNoticeCreateAction = new RAction(PaymentMessages.M.paymentNoticeImport(),
            clickHandler);
      }
      if (sourceBillCreateAction == null) {
        sourceBillCreateAction = new RAction(PaymentMessages.M.sourceBillImport(), clickHandler);
      }
      if (accountCreateAction == null) {
        accountCreateAction = new RAction(PaymentMessages.M.accountImport(), clickHandler);
      }
      result.add(createAction);
      result.add(statementCreateAction);
      result.add(invoiceRegCreateAction);
      result.add(paymentNoticeCreateAction);
      result.add(sourceBillCreateAction);
      result.add(accountCreateAction);
    }
    return result;
  }

  private Handler_click clickHandler = new Handler_click();

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(createAction)) {
        doCreate();
      } else if (event.getSource().equals(statementCreateAction)) {
        doStatementCreate();
      } else if (event.getSource().equals(invoiceRegCreateAction)) {
        doInvoiceRegCreate();
      } else if (event.getSource().equals(paymentNoticeCreateAction)) {
        doPaymentNoticeCreate();
      } else if (event.getSource().equals(sourceBillCreateAction)) {
        doSourceBillCreate();
      } else if (event.getSource().equals(accountCreateAction)) {
        doAccountCreate();
      }
    }
  }

  private void doCreate() {
    JumpParameters params = new JumpParameters(PaymentCreatePage.START_NODE);
    jump(params);
  }

  private void doStatementCreate() {
    if (statementDialog == null) {
      statementDialog = new AccByStatementBrowserDialog(true, getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business()), DirectionType.payment,
          new AccByStatementBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountStatement> results) {
              List<String> statementUuids = new ArrayList<String>();
              for (BAccountStatement statement : results)
                statementUuids.add(statement.getUuid());

              JumpParameters jumParams = new JumpParameters(PaymentCreatePage.START_NODE);
              jumParams.getExtend().put(PaymentCreatePage.PN_CREATEBYSTATEMENT, statementUuids);
              jump(jumParams);
            }
          }, getCaptionMap(), getCounterpartTypeMap());
    }
    statementDialog.center();
  }

  private void doInvoiceRegCreate() {
    if (invoiceRegDialog == null) {
      invoiceRegDialog = new AccByInvoiceRegBrowserDialog(true, getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business()), DirectionType.payment,
          new AccByInvoiceRegBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountInvoice> results) {
              List<String> invoiceUuids = new ArrayList<String>();
              for (BAccountInvoice invoice : results)
                invoiceUuids.add(invoice.getUuid());

              JumpParameters jumParams = new JumpParameters(PaymentCreatePage.START_NODE);
              jumParams.getExtend().put(PaymentCreatePage.PN_CREATEBYINVOICEREG, invoiceUuids);
              jump(jumParams);
            }
          }, getCaptionMap(), getCounterpartTypeMap());
    }
    invoiceRegDialog.center();
  }

  private void doPaymentNoticeCreate() {
    if (paymentNoticeDialog == null) {
      paymentNoticeDialog = new AccByPaymentNoticeBrowserDialog(true, getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business()), DirectionType.payment,
          new AccByPaymentNoticeBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountNotice> results) {
              List<String> noticeUuids = new ArrayList<String>();
              for (BAccountNotice notice : results)
                noticeUuids.add(notice.getUuid());

              JumpParameters jumParams = new JumpParameters(PaymentCreatePage.START_NODE);
              jumParams.getExtend().put(PaymentCreatePage.PN_CREATEBYPAYMENTNOTICE, noticeUuids);
              jump(jumParams);
            }
          }, getCaptionMap(), getCounterpartTypeMap());
    }
    paymentNoticeDialog.center();
  }

  // 新建dialog
  private AccByStatementBrowserDialog statementDialog;
  private AccByInvoiceRegBrowserDialog invoiceRegDialog;
  private AccByPaymentNoticeBrowserDialog paymentNoticeDialog;
  private AccByBillBrowserDialog sourceBillDialog;
  private AccBrowserDialog accDialog;

  private void doSourceBillCreate() {
    if (sourceBillDialog == null) {
      sourceBillDialog = new AccByBillBrowserDialog(true, getFieldCaption(GRes.FIELDNAME_BUSINESS,
          GRes.R.business()), DirectionType.payment, new AccByBillBrowserDialog.Callback() {

        @Override
        public void execute(List<BAccountSourceBill> results) {
          List<String> sourceBillUuids = new ArrayList<String>();
          for (BAccountSourceBill sourceBill : results)
            sourceBillUuids.add(sourceBill.getSourceBill().getBillUuid());

          JumpParameters jumParams = new JumpParameters(PaymentCreatePage.START_NODE);
          jumParams.getExtend().put(PaymentCreatePage.PN_CREATEBYSOURCEBILL, sourceBillUuids);
          jump(jumParams);
        }
      }, getCaptionMap(), getCounterpartTypeMap());
    }
    sourceBillDialog.setBillTypeMap(getBillTypeMap());
    sourceBillDialog.center();
  }

  private void doAccountCreate() {
    if (accDialog == null) {
      accDialog = new AccBrowserDialog(true, getFieldCaption(GRes.FIELDNAME_BUSINESS,
          GRes.R.business()), new AccBrowserDialog.Callback() {

        @Override
        public void execute(List<BAccount> results) {
          List<String> accountUuids = new ArrayList<String>();
          for (BAccount account : results)
            accountUuids.add(account.getUuid());

          JumpParameters jumParams = new JumpParameters(PaymentCreatePage.START_NODE);
          jumParams.getExtend().put(PaymentCreatePage.PN_CREATEBYACCOUNT, accountUuids);
          jump(jumParams);
        }
      }, DirectionType.payment, getCaptionMap(), getCounterpartTypeMap());
    }
    accDialog.setBillTypeMap(getBillTypeMap());
    accDialog.center();
  }

  public BPaymentConfig getConfig() {
    if (config == null) {
      config = new BPaymentConfig();
      config.setShowTax(StringUtil.toBoolean(getModuleContext().get(PaymentService.KEY_SHOW_TAX),
          false));
      config.setUseInvoiceStock(StringUtil.toBoolean(
          getModuleContext().get(PaymentService.KEY_USE_INVOICE_STOCK), false));
    }
    return config;
  }

  /** 获取发票类型 */
  public Map<String, String> getInvoiceTypeMap() {
    GWTUtil.enableSynchronousRPC();
    if (invoiceTypeMap == null) {
      PaymentCommonsService.Locator.getService().getInvoiceTypeMap(
          new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
              String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.get(),
                  PaymentMessages.M.ivcType());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(Map<String, String> result) {
              if (result == null) {
                result = new HashMap<String, String>();
              }
              invoiceTypeMap = result;
            }
          });
    }
    return invoiceTypeMap;
  }

  private BInvoiceRegConfig invoiceRegConfig;

  public BInvoiceRegConfig getInvoiceRegConfig() {
    if (invoiceRegConfig == null) {
      invoiceRegConfig = new BInvoiceRegConfig();
      invoiceRegConfig.setRegTotalWritable(StringUtil.toBoolean(
          getModuleContext().get(PaymentService.KEY_PAY_REGTOTAL_WRITABLE), false));
      try {
        invoiceRegConfig.setTaxDiffHi(new BigDecimal(getModuleContext().get(
            PaymentService.KEY_PAY_TAX_DIFF_HI)));
      } catch (Exception e) {
        invoiceRegConfig.setTaxDiffHi(BigDecimal.ZERO);
      }
      try {
        invoiceRegConfig.setTaxDiffLo(new BigDecimal(getModuleContext().get(
            PaymentService.KEY_PAY_TAX_DIFF_LO)));
      } catch (Exception e) {
        invoiceRegConfig.setTaxDiffLo(BigDecimal.ZERO);
      }
      try {
        invoiceRegConfig.setTotalDiffHi(new BigDecimal(getModuleContext().get(
            PaymentService.KEY_PAY_TOTAL_DIFF_HI)));
      } catch (Exception e) {
        invoiceRegConfig.setTotalDiffHi(BigDecimal.ZERO);
      }
      try {
        invoiceRegConfig.setTotalDiffLo(new BigDecimal(getModuleContext().get(
            PaymentService.KEY_PAY_TOTAL_DIFF_LO)));
      } catch (Exception e) {
        invoiceRegConfig.setTotalDiffLo(BigDecimal.ZERO);
      }
    }
    return invoiceRegConfig;
  }

  /** 获取选项配置小数精度 */
  public int getScale() {
    return Integer.valueOf(getModuleContext().get(PaymentService.SCALE));
  }

  /** 获取选项配置舍入算法 */
  public RoundingMode getRoundingMode() {
    return RoundingMode.valueOf(getModuleContext().get(PaymentService.ROUNDING_MODE));
  }

}
