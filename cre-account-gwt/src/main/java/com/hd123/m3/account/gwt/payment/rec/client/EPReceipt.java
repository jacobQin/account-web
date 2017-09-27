/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	EPReceipt.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client;

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
import com.hd123.m3.account.gwt.commons.client.biz.BReceiptOverdueDefault;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByBillBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByInvoiceRegBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByPaymentNoticeBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByStatementBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptConfig;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptServiceAsync;
import com.hd123.m3.account.gwt.payment.rec.client.ui.page.ReceiptCreatePage;
import com.hd123.m3.account.gwt.payment.rec.client.ui.page.ReceiptEditPage;
import com.hd123.m3.account.gwt.payment.rec.client.ui.page.ReceiptIvcRegViewPage;
import com.hd123.m3.account.gwt.payment.rec.client.ui.page.ReceiptLogViewPage;
import com.hd123.m3.account.gwt.payment.rec.client.ui.page.ReceiptSearchPage;
import com.hd123.m3.account.gwt.payment.rec.client.ui.page.ReceiptViewPage;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountInvoice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
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
public class EPReceipt extends AccEPBpmModule {
  public static final String OPN_ENTITY = "enitity";

  private Map<String, BBillType> billTypeMap;
  private BDefaultOption defaultOption;
  private List<BUCN> paymentTypes;
  private List<BBank> banks;
  private BUCN currentEmployee;
  private BReceiptConfig config;
  private Map<String, String> invoiceTypeMap;
  private List<String> coopModes;

  public static EPReceipt getInstance() {
    return EPReceipt.getInstance(EPReceipt.class);
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = ReceiptSearchPage.START_NODE;

      if (ReceiptSearchPage.START_NODE.equals(start))
        showContentPage(ReceiptSearchPage.getInstance(), urlParams);
      else if (ReceiptCreatePage.START_NODE.equals(start))
        showContentPage(ReceiptCreatePage.getInstance(), urlParams);
      else if (ReceiptViewPage.START_NODE.equals(start))
        showContentPage(ReceiptViewPage.getInstance(), urlParams);
      else if (ReceiptEditPage.START_NODE.equals(start))
        showContentPage(ReceiptEditPage.getInstance(), urlParams);
      else if (ReceiptLogViewPage.START_NODE.equals(start))
        showContentPage(ReceiptLogViewPage.getInstance(), urlParams);
      else if (ReceiptIvcRegViewPage.START_NODE.equals(start))
        showContentPage(ReceiptIvcRegViewPage.getInstance(), urlParams);
    } catch (Exception e) {
      e.printStackTrace();
      String msg = CommonsMessages.M.processError(CommonsMessages.M.show());
      RMsgBox.showError(msg, e);
    }
  }

  public ReceiptServiceAsync getModuleService() {
    return ReceiptService.Locator.getService();
  }

  @Override
  public String getModuleCaption() {
    return PPaymentDef.TABLE_CAPTION;
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return ReceiptPermDef.PRINT_PATH;
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
        JumpParameters params = ReceiptSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        jump(ReceiptSearchPage.START_NODE);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.search()));

    ReceiptService.Locator.getService().loadByNumber(keyword, false,
        new RBAsyncCallback2<BPayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(ReceiptSearchPage.START_NODE);
            params.getUrlRef().set(ReceiptSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(ReceiptSearchPage.START_NODE);
              params.getUrlRef().set(ReceiptSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(ReceiptViewPage.START_NODE);
              params.getUrlRef().set(ReceiptViewPage.PN_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  public native void log(String text)
  /*-{ 
      $wnd.console.log(text); 
  }-*/;

  
  /** 单据类型map */
  public Map<String, BBillType> getBillTypeMap() {
    if (billTypeMap == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.load()
          + ReceiptMessages.M.billType()));
      PaymentCommonsService.Locator.getService().getBillTypes(0,
          new RBAsyncCallback2<List<BBillType>>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                  ReceiptMessages.M.billType());
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
      coopModes = CollectionUtil.toList(getModuleContext().get(ReceiptService.KEY_COOPMODES));
    }
    return coopModes;
  }

  /** 获取默认配置 */
  public BDefaultOption getDefaultOption() {
    if (defaultOption == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.load()
          + ReceiptMessages.M.defaultOption()));
      PaymentCommonsService.Locator.getService().getDefaultOption(
          new RBAsyncCallback2<BDefaultOption>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                  ReceiptMessages.M.defaultOption());
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

  /**
   * 获取所有可用的付款方式
   * 
   * @param showLimitPayType
   *          是否显示长短款付款方式
   * @return
   */
  public List<BUCN> getPaymentTypes(boolean showLimitPayType) {
    if (paymentTypes == null) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.load()
          + ReceiptMessages.M.paymentType()));
      PaymentCommonsService.Locator.getService().getPaymentTypes(
          new RBAsyncCallback2<List<BUCN>>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                  ReceiptMessages.M.paymentType());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(List<BUCN> result) {
              RLoadingDialog.hide();
              paymentTypes = result;
            }
          });
    }

    if (!showLimitPayType) {
      List<BUCN> list = new ArrayList<BUCN>();
      list.addAll(paymentTypes);
      list.remove(config == null ? getConfig().getReceiptPaymentType() : config
          .getReceiptPaymentType());
      return list;
    }
    return paymentTypes;
  }

  /** 获取所有银行 */
  public List<BBank> getBanks() {
    if (banks == null || banks.isEmpty()) {
      GWTUtil.enableSynchronousRPC();
      RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.load()
          + ReceiptMessages.M.banks()));
      PaymentCommonsService.Locator.getService().getBanks(new RBAsyncCallback2<List<BBank>>() {
        @Override
        public void onException(Throwable caught) {
          RLoadingDialog.hide();
          String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
              ReceiptMessages.M.banks());
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
      RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.load()
          + ReceiptMessages.M.currentEmployee()));
      PaymentCommonsService.Locator.getService().getCurrentEmployee(getCurrentUser().getId(),
          new RBAsyncCallback2<BUCN>() {
            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                  ReceiptMessages.M.currentEmployee());
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
    return ReceiptPermDef.RESOURCE_RECEIPT;
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
        statementCreateAction = new RAction(ReceiptMessages.M.statementImport(), clickHandler);
      }
      if (invoiceRegCreateAction == null) {
        invoiceRegCreateAction = new RAction(ReceiptMessages.M.invoiceRegImport(), clickHandler);
      }
      if (paymentNoticeCreateAction == null) {
        paymentNoticeCreateAction = new RAction(ReceiptMessages.M.paymentNoticeImport(),
            clickHandler);
      }
      if (sourceBillCreateAction == null) {
        sourceBillCreateAction = new RAction(ReceiptMessages.M.sourceBillImport(), clickHandler);
      }
      if (accountCreateAction == null) {
        accountCreateAction = new RAction(ReceiptMessages.M.accountImport(), clickHandler);
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

  // 新建dialog
  private AccByStatementBrowserDialog statementDialog;
  private AccByInvoiceRegBrowserDialog invoiceRegDialog;
  private AccByPaymentNoticeBrowserDialog paymentNoticeDialog;
  private AccByBillBrowserDialog sourceBillDialog;
  private AccBrowserDialog accDialog;

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
    JumpParameters params = new JumpParameters(ReceiptCreatePage.START_NODE);
    jump(params);
  }

  private void doStatementCreate() {
    if (statementDialog == null) {
      statementDialog = new AccByStatementBrowserDialog(true, getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business()), DirectionType.receipt,
          new AccByStatementBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountStatement> results) {
              List<String> statementUuids = new ArrayList<String>();
              for (BAccountStatement statement : results)
                statementUuids.add(statement.getUuid());

              JumpParameters jumParams = new JumpParameters(ReceiptCreatePage.START_NODE);
              jumParams.getExtend().put(ReceiptCreatePage.PN_CREATEBYSTATEMENT, statementUuids);
              jump(jumParams);
            }
          }, getCaptionMap(), getCounterpartTypeMap());
    }
    statementDialog.center();
  }

  public void clearConditions() {
    if (statementDialog != null) {
      statementDialog.clearConditions();
    }
    if (invoiceRegDialog != null) {
      invoiceRegDialog.clearConditions();
    }
    if (paymentNoticeDialog != null) {
      paymentNoticeDialog.clearConditions();
    }
    if (sourceBillDialog != null) {
      sourceBillDialog.clearConditions();
    }
    if (accDialog != null) {
      accDialog.clearConditions();
    }
  }

  private void doInvoiceRegCreate() {
    if (invoiceRegDialog == null) {
      invoiceRegDialog = new AccByInvoiceRegBrowserDialog(true, getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business()), DirectionType.receipt,
          new AccByInvoiceRegBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountInvoice> results) {
              List<String> invoiceUuids = new ArrayList<String>();
              for (BAccountInvoice invoice : results)
                invoiceUuids.add(invoice.getUuid());

              JumpParameters jumParams = new JumpParameters(ReceiptCreatePage.START_NODE);
              jumParams.getExtend().put(ReceiptCreatePage.PN_CREATEBYINVOICEREG, invoiceUuids);
              jump(jumParams);
            }
          }, getCaptionMap(), getCounterpartTypeMap());
    }
    invoiceRegDialog.center();
  }

  private void doPaymentNoticeCreate() {
    if (paymentNoticeDialog == null) {
      paymentNoticeDialog = new AccByPaymentNoticeBrowserDialog(true, getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business()), DirectionType.receipt,
          new AccByPaymentNoticeBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountNotice> results) {
              List<String> noticeUuids = new ArrayList<String>();
              for (BAccountNotice notice : results)
                noticeUuids.add(notice.getUuid());

              JumpParameters jumParams = new JumpParameters(ReceiptCreatePage.START_NODE);
              jumParams.getExtend().put(ReceiptCreatePage.PN_CREATEBYPAYMENTNOTICE, noticeUuids);
              jump(jumParams);
            }
          }, getCaptionMap(), getCounterpartTypeMap());
    }
    paymentNoticeDialog.center();
  }

  private void doSourceBillCreate() {
    if (sourceBillDialog == null) {
      sourceBillDialog = new AccByBillBrowserDialog(true, getFieldCaption(GRes.FIELDNAME_BUSINESS,
          GRes.R.business()), DirectionType.receipt, new AccByBillBrowserDialog.Callback() {

        @Override
        public void execute(List<BAccountSourceBill> results) {
          List<String> sourceBillUuids = new ArrayList<String>();
          for (BAccountSourceBill sourceBill : results)
            sourceBillUuids.add(sourceBill.getSourceBill().getBillUuid());

          JumpParameters jumParams = new JumpParameters(ReceiptCreatePage.START_NODE);
          jumParams.getExtend().put(ReceiptCreatePage.PN_CREATEBYSOURCEBILL, sourceBillUuids);
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

          JumpParameters jumParams = new JumpParameters(ReceiptCreatePage.START_NODE);
          jumParams.getExtend().put(ReceiptCreatePage.PN_CREATEBYACCOUNT, accountUuids);
          jump(jumParams);
        }
      }, DirectionType.receipt, getCaptionMap(), getCounterpartTypeMap());
    }
    accDialog.setBillTypeMap(getBillTypeMap());
    accDialog.center();
  }

  /** 获取收款配置 */
  public BReceiptConfig getConfig() {
    GWTUtil.enableSynchronousRPC();
    ReceiptService.Locator.getService().loadConfig(new AsyncCallback<BReceiptConfig>() {
      @Override
      public void onSuccess(BReceiptConfig result) {
        config = result;
      }

      @Override
      public void onFailure(Throwable caught) {
      }
    });
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
              String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.get(),
                  ReceiptMessages.M.ivcType());
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

  /** 获取选项配置小数精度 */
  public int getScale() {
    return Integer.valueOf(getModuleContext().get(ReceiptService.SCALE));
  }

  /** 获取选项配置舍入算法 */
  public RoundingMode getRoundingMode() {
    return RoundingMode.valueOf(getModuleContext().get(ReceiptService.ROUNDING_MODE));
  }

  /** 获取收款单滞纳金默认值方式 */
  public BReceiptOverdueDefault getOverdueDefault() {
    return getModuleContext().get(ReceiptService.KEY_RECEIPT_OVERDUE_DEFAULTS) == null ? null
        : BReceiptOverdueDefault.valueOf(getModuleContext().get(
            ReceiptService.KEY_RECEIPT_OVERDUE_DEFAULTS));
  }
}
