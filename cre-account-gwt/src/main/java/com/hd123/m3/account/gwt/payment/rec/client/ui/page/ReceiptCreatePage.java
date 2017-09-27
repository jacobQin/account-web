/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： ReceiptCreatePage.java
 * 模块说明：    
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.page;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.payment.commons.client.BPaymentDefrayalValidator2;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLogger;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentRemarkEditGadget;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCollectionLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.BPaymentLineDefrayalValidator2;
import com.hd123.m3.account.gwt.payment.rec.client.BReceiptValidator;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptDefrayalApportionHelper;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptServiceAgent;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.AccountLineEditGrid;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.AccountLineEditGrid2;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.OverdueLineEditGrid;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptGeneralCreateGadget;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptInfoEditGadget;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptInfoViewGadget;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineEditGrid;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineLineEditBox;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection.CollectionLineSingleLineEditGrid;
import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams.Create;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmCreatePage;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.RPCCommand;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RTabDef;
import com.hd123.rumba.gwt.widget2.client.panel.RTabPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author lixiaohong
 * 
 */
public class ReceiptCreatePage extends BaseBpmCreatePage implements Create, RValidatable,
    RActionHandler, HasRActionHandlers, SelectionHandler<Message> {

  private static ReceiptCreatePage instance;

  public static ReceiptCreatePage getInstance() {
    if (instance == null)
      instance = new ReceiptCreatePage();
    return instance;
  }

  private EPReceipt ep = EPReceipt.getInstance();
  private BPayment bill;
  private RAction saveAction;
  private RAction cancelAction;

  private VerticalPanel panel;
  private ReceiptGeneralCreateGadget generalGadget;

  private RTabPanel tabPanel;
  private RTabDef accountTab;
  private RTabDef accountMultiTab;
  private RTabDef overdueTab;
  private RTabDef collectionLineTab;
  private AccountLineEditGrid accountLineEditGrid;
  private OverdueLineEditGrid overdueLineEditGrid;
  private CollectionLineEditGrid collectionLineEditGrid;
  private CollectionLineSingleLineEditGrid collectionLineSingleLineEditGrid;
  private CollectionLineLineEditBox collectionLineLineEditGrid;

  private RSimplePanel collectionLineEditGridWidget;
  private RSimplePanel collectionLineSingleLineEditGridWidget;
  private RSimplePanel collectionLineLineEditWidget;

  private ReceiptInfoEditGadget paymentInfoEditGadget;
  private ReceiptInfoViewGadget paymentInfoViewGadget;
  private PaymentRemarkEditGadget remarkGadget;
  private Widget middle;

  private AccountLineEditGrid2 accountLineMultiEditGrid;

  protected BReceiptValidator validator;

  /** 按科目收款时验证 */
  protected BPaymentLineDefrayalValidator2 lineDefrayalValidator;

  /** 按总额收款时验证 */
  protected BPaymentDefrayalValidator2 defrayalValidator;

  private Handler_click clickHandler = new Handler_click();

  public ReceiptCreatePage() {
    super();
    drawToolbar();
    drawSelf();
    getMessagePanel().addSelectionHandler(this);
  }

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, clickHandler);
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    // BPM出口按钮
    injectBpmActions();

    cancelAction = new RAction(RActionFacade.CANCEL, clickHandler);
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
  }

  private void drawSelf() {
    panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    generalGadget = new ReceiptGeneralCreateGadget();
    panel.add(generalGadget);

    middle = drawMiddle();
    panel.add(middle);

    paymentInfoEditGadget = new ReceiptInfoEditGadget();
    generalGadget.addActionHandler(paymentInfoEditGadget);
    panel.add(paymentInfoEditGadget);

    paymentInfoViewGadget = new ReceiptInfoViewGadget(false);
    paymentInfoViewGadget.setVisible(false);
    panel.add(paymentInfoViewGadget);

    remarkGadget = new PaymentRemarkEditGadget();
    remarkGadget.setEditing(true);
    remarkGadget.getCaptionBar().setShowCollapse(true);
    panel.add(remarkGadget);
    addActionHandler();
  }

  private Widget drawMiddle() {
    tabPanel = new RTabPanel();
    tabPanel.setWidth("100%");

    accountLineEditGrid = new AccountLineEditGrid();
    accountLineEditGrid.setEditing(true);

    overdueLineEditGrid = new OverdueLineEditGrid();
    overdueLineEditGrid.setEditing(true);

    collectionLineEditGrid = new CollectionLineEditGrid();
    collectionLineSingleLineEditGrid = new CollectionLineSingleLineEditGrid();
    collectionLineLineEditGrid = new CollectionLineLineEditBox();

    accountLineMultiEditGrid = new AccountLineEditGrid2();
    accountLineMultiEditGrid.setEditing(true);

    accountTab = new RTabDef(PPaymentAccountLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(accountLineEditGrid));
    accountMultiTab = new RTabDef(PPaymentAccountLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(accountLineMultiEditGrid));
    overdueTab = new RTabDef(PPaymentOverdueLineDef.TABLE_CAPTION,
        RSimplePanel.decorate(overdueLineEditGrid));

    collectionLineEditGridWidget = RSimplePanel.decorate(collectionLineEditGrid);
    collectionLineSingleLineEditGridWidget = RSimplePanel
        .decorate(collectionLineSingleLineEditGrid);
    collectionLineLineEditWidget = RSimplePanel.decorate(collectionLineLineEditGrid);

    collectionLineTab = new RTabDef(PPaymentCollectionLineDef.TABLE_CAPTION,
        collectionLineEditGridWidget);

    tabPanel.addTabDef(accountTab);
    tabPanel.addTabDef(accountMultiTab);
    tabPanel.addTabDef(overdueTab);
    tabPanel.addTabDef(collectionLineTab);

    tabPanel.selectFirstVisibleTab();
    return tabPanel;
  }

  @Override
  protected EPReceipt getEP() {
    return EPReceipt.getInstance();
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected void refreshEntity() {
    if (bill.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      bill.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    bill.setPaymentDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    bill.setDealer(new BUCN(getEP().getCurrentEmployee().getUuid(), getEP().getCurrentEmployee()
        .getCode(), getEP().getCurrentEmployee().getName()));

    if (CPaymentDefrayalType.line.equals(ep.getConfig().getDefalutReceiptPaymentType())) {
      bill.setDefrayalType(CPaymentDefrayalType.line);
    } else if (CPaymentDefrayalType.lineSingle
        .equals(ep.getConfig().getDefalutReceiptPaymentType())) {
      bill.setDefrayalType(CPaymentDefrayalType.lineSingle);
    } else {
      bill.setDefrayalType(CPaymentDefrayalType.bill);
    }

    // 计算滞纳金
    BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(), ep.getOverdueDefault());
    defrayalValidator = new BPaymentDefrayalValidator2(bill, paymentInfoEditGadget);

    init();
    refresh();

  }

  /**
   * 初始化界面
   */
  protected void init() {

    validator = new BReceiptValidator(bill, false);

    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      paymentInfoViewGadget.setVisible(false);
      paymentInfoEditGadget.setVisible(true);
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      paymentInfoViewGadget.setVisible(true);
      paymentInfoEditGadget.setVisible(false);
      lineDefrayalValidator = new BPaymentLineDefrayalValidator2(bill);
    } else if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
      paymentInfoViewGadget.setVisible(true);
      paymentInfoEditGadget.setVisible(false);

      validator = new BReceiptValidator(bill, true);
      lineDefrayalValidator = new BPaymentLineDefrayalValidator2(bill);
    }

  }

  private void refresh() {
    assert bill != null;

    refreshMiddle();

    paymentInfoEditGadget.setBill(bill);
    paymentInfoEditGadget.refresh();

    paymentInfoViewGadget.setBill(bill);
    paymentInfoViewGadget.refresh();

    remarkGadget.setEntity(bill);
    remarkGadget.refresh();

    generalGadget.setBill(bill);
    generalGadget.refresh();
    generalGadget.focusOnFirstField();

    accountLineEditGrid.refreshButton();
    accountLineMultiEditGrid.refreshButton();

    accountLineEditGrid.doApportionInit();
    refreshShowWhichCollection();

    refreshTabVisible();
    clearValidResults();
  }

  private void refreshShowWhichCollection() {
    if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      collectionLineTab.setWidget(collectionLineSingleLineEditGridWidget);
      collectionLineSingleLineEditGrid.refresh();
    } else if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
      collectionLineTab.setWidget(collectionLineLineEditWidget);
      collectionLineLineEditGrid.setBill(bill);
    } else {
      collectionLineTab.setWidget(collectionLineEditGridWidget);
      collectionLineEditGrid.refresh();
    }
  }

  private void refreshMiddle() {

    accountLineEditGrid.setBill(bill);
    accountLineEditGrid.refreshData();
    accountLineEditGrid.refreshGrid();

    overdueLineEditGrid.setBill(bill);
    overdueLineEditGrid.refreshData();
    overdueLineEditGrid.refreshGrid();

    // Multi
    accountLineMultiEditGrid.setBill(bill);
    accountLineMultiEditGrid.refreshGrid();
    accountLineMultiEditGrid.refreshData();

    // 代收明细
    resetContract();
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      collectionLineEditGrid.setValue(bill);
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      collectionLineSingleLineEditGrid.setPayment(bill);
    } else {
      collectionLineLineEditGrid.setBill(bill);
    }
  }

  private void refreshTabVisible() {
    boolean isFirstTabPanel = false;
    if (tabPanel.getCurrentTabDef() == accountTab || tabPanel.getCurrentTabDef() == accountMultiTab) {
      isFirstTabPanel = true;
    }
    accountTab.setVisible(!CPaymentDefrayalType.line.equals(bill.getDefrayalType()));
    accountMultiTab.setVisible(CPaymentDefrayalType.line.equals(bill.getDefrayalType()));
    if (isFirstTabPanel) {
      tabPanel.selectFirstVisibleTab();
    }
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    decodeParams(params, new Command() {
      public void execute() {
        refresh(bill);
        getEP().appendSearchBox();
        generalGadget.refreshRightPanel();
        initDefaultReceipt();
      }
    });
  }

  // 根据配置初始化默认收款金额
  private void initDefaultReceipt() {
    // 按总额或者科目单收款默认设置收款金额等于未收金额
    if (CPaymentDefrayalType.line.equals(bill.getDefrayalType()) == false) {
      accountLineEditGrid.setDefaultReceiptEqualsUnpayed();
    }
  }

  @Override
  public void onHide() {
    generalGadget.clearQueryConditions();
    accountLineEditGrid.onHide();
    accountLineMultiEditGrid.onHide();
    overdueLineEditGrid.onHide();
    collectionLineEditGrid.onHide();
    getEP().clearConditions();
  }

  private void decodeParams(final JumpParameters params, final Command callback) {
    assert callback != null;
    RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.create()));

    if (params.getExtend().get(PN_CREATEBYSTATEMENT) != null) {
      doCreateByStatement((List<String>) params.getExtend().get(PN_CREATEBYSTATEMENT), callback);
    } else if (params.getExtend().get(PN_CREATEBYINVOICEREG) != null) {
      doCreateByInvoiceReg((List<String>) params.getExtend().get(PN_CREATEBYINVOICEREG), callback);
    } else if (params.getExtend().get(PN_CREATEBYPAYMENTNOTICE) != null) {
      doCreateByPaymentNotice((List<String>) params.getExtend().get(PN_CREATEBYPAYMENTNOTICE),
          callback);
    } else if (params.getExtend().get(PN_CREATEBYSOURCEBILL) != null) {
      doCreateBySourceBill((List<String>) params.getExtend().get(PN_CREATEBYSOURCEBILL), callback);
    } else if (params.getExtend().get(PN_CREATEBYACCOUNT) != null) {
      doCreateByAccount((List<String>) params.getExtend().get(PN_CREATEBYACCOUNT), callback);
    } else if (params.getUrlRef().get(PN_CREATEBYSTATEMENT) != null) {
      List<String> statementUuids = new ArrayList<String>();
      statementUuids.add(params.getUrlRef().get(PN_CREATEBYSTATEMENT));
      doCreateByStatement(statementUuids, callback);
    } else if (params.getUrlRef().get(PN_CREATEBYINVOICEREG) != null) {
      List<String> invoiceUuids = new ArrayList<String>();
      invoiceUuids.add(params.getUrlRef().get(PN_CREATEBYINVOICEREG));
      doCreateByInvoiceReg(invoiceUuids, callback);
    } else if (params.getUrlRef().get(PN_CREATEBYPAYMENTNOTICE) != null) {
      List<String> noticeUuids = new ArrayList<String>();
      noticeUuids.add(params.getUrlRef().get(PN_CREATEBYPAYMENTNOTICE));
      doCreateByPaymentNotice(noticeUuids, callback);
    } else if (params.getUrlRef().get(PN_CREATEBYSOURCEBILL) != null) {
      List<String> sourceBillUuids = new ArrayList<String>();
      sourceBillUuids.add(params.getUrlRef().get(PN_CREATEBYSOURCEBILL));
      doCreateBySourceBill(sourceBillUuids, callback);
    } else if (params.getUrlRef().get(PN_CREATEBYACCOUNT) != null) {
      List<String> accountUuids = new ArrayList<String>();
      accountUuids.add(params.getUrlRef().get(PN_CREATEBYACCOUNT));
      doCreateByAccount(accountUuids, callback);
    } else if (params.getExtend().get(PN_CREATEBYACCOUNTIDS) != null) {
      doCreateByAccountIds((List<String>) params.getExtend().get(PN_CREATEBYACCOUNTIDS), callback);
    } else if (params.getUrlRef().get(PN_CREATEBYACCOUNTIDS) != null) {
      List<String> accountIds = new ArrayList<String>();
      String values = params.getUrlRef().get(PN_CREATEBYACCOUNTIDS);
      getEP().log(values);

      String[] valueArr = values.split(";");
      for (String id : valueArr) {
        accountIds.add(id.trim());
      }
      getEP().log("以下打印出accountIds,size="+accountIds.size());
      for(String accId:accountIds){
        getEP().log(accId);
      }
      doCreateByAccountIds(accountIds, callback);
    } else {
      doCreate(callback);
    }
  }

  private void doCreateByStatement(List<String> statementUuids, final Command callback) {
    assert statementUuids != null;
    assert !statementUuids.isEmpty();

    ReceiptService.Locator.getService().createByStatements(statementUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.create(),
                PPaymentDef.TABLE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateByInvoiceReg(List<String> invoiceUuids, final Command callback) {
    assert invoiceUuids != null;
    assert !invoiceUuids.isEmpty();

    ReceiptService.Locator.getService().createByInvoices(invoiceUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.create(),
                PPaymentDef.TABLE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateByPaymentNotice(List<String> noticeUuids, final Command callback) {
    assert noticeUuids != null;
    assert !noticeUuids.isEmpty();

    ReceiptService.Locator.getService().createByPaymentNotices(noticeUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.create(),
                PPaymentDef.TABLE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateBySourceBill(List<String> sourceBillUuids, final Command callback) {
    assert sourceBillUuids != null;
    assert !sourceBillUuids.isEmpty();

    ReceiptService.Locator.getService().createBySourceBills(sourceBillUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.create(),
                PPaymentDef.TABLE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateByAccount(List<String> accountUuids, final Command callback) {
    assert accountUuids != null;
    assert !accountUuids.isEmpty();

    ReceiptService.Locator.getService().createByAccounts(accountUuids,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.create(),
                PPaymentDef.TABLE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreateByAccountIds(List<String> accountIds, final Command callback) {
    assert accountIds != null;
    assert !accountIds.isEmpty();

    ReceiptService.Locator.getService().createByAccountIds(accountIds,
        new RBAsyncCallback2<BPayment>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.create(),
                PPaymentDef.TABLE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            bill = result;
            callback.execute();
          }
        });
  }

  private void doCreate(final Command callback) {

    ReceiptService.Locator.getService().create(new RBAsyncCallback2<BPayment>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.create(),
            PPaymentDef.TABLE_CAPTION);
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BPayment result) {
        RLoadingDialog.hide();
        bill = result;
        callback.execute();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isBpmMode())
      return true;
    if (getEP().isPermitted(ReceiptPermDef.CREATE) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  @Override
  public void clearValidResults() {
    generalGadget.clearValidResults();
    paymentInfoEditGadget.clearValidResults();
    remarkGadget.clearValidResults();
    accountLineEditGrid.clearValidResults();
    accountLineMultiEditGrid.clearValidResults();
    overdueLineEditGrid.clearValidResults();
    validator.clearValidResults();
    if (defrayalValidator != null) {
      defrayalValidator.clearValidResults();
    }
    if (lineDefrayalValidator != null) {
      lineDefrayalValidator.clearValidResults();
    }
  }

  @Override
  public boolean isValid() {
    boolean isValid = generalGadget.isValid()
        && paymentInfoEditGadget.isValid()
        && remarkGadget.isValid()
        && validator.isValid()
        && overdueLineEditGrid.isValid()
        && (CPaymentDefrayalType.bill.equals(bill.getDefrayalType()) ? defrayalValidator.isValid()
            & accountLineEditGrid.isValid() : (CPaymentDefrayalType.lineSingle.equals(bill
            .getDefrayalType()) ? lineDefrayalValidator.isValid() & accountLineEditGrid.isValid()
            : lineDefrayalValidator.isValid() & accountLineMultiEditGrid.isValid()));
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      isValid = isValid && collectionLineEditGrid.isValid();
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      isValid = isValid && collectionLineSingleLineEditGrid.isValid();
    } else {
      isValid = isValid && collectionLineLineEditGrid.isValid();
    }
    return isValid;
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(generalGadget.getInvalidMessages());
    list.addAll(paymentInfoEditGadget.getInvalidMessages());
    list.addAll(remarkGadget.getInvalidMessages());
    list.addAll(validator.getInvalidMessages());
    list.addAll(overdueLineEditGrid.getInvalidMessages());
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      list.addAll(defrayalValidator.getInvalidMessages());
      list.addAll(collectionLineEditGrid.getInvalidMessages());
      list.addAll(accountLineEditGrid.getInvalidMessages());
    } else {
      list.addAll(lineDefrayalValidator.getInvalidMessages());
      if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
        list.addAll(collectionLineSingleLineEditGrid.getInvalidMessages());
        list.addAll(accountLineEditGrid.getInvalidMessages());
      } else {
        list.addAll(accountLineMultiEditGrid.getInvalidMessages());
        list.addAll(collectionLineLineEditGrid.getInvalidMessages());
      }
    }
    return list;
  }

  @Override
  public boolean validate() {
    // 这里验证器的验证放在最后，否则验证器中给控件绑定的错误消息会在控件验证的时候被清空。
    clearValidResults();
    getMessagePanel().clearMessages();
    boolean valid = generalGadget.validate();
    valid &= paymentInfoEditGadget.validate();
    valid &= remarkGadget.validate();
    valid &= overdueLineEditGrid.validate();
    if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
      valid &= collectionLineLineEditGrid.validate();
      valid &= accountLineMultiEditGrid.validate();
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      valid &= collectionLineSingleLineEditGrid.validate();
      valid &= accountLineEditGrid.validate();
    } else {
      valid &= collectionLineEditGrid.validate();
      valid &= accountLineEditGrid.validate();
    }
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      valid &= defrayalValidator.validate();
    } else {
      valid &= lineDefrayalValidator.validate();
    }
    valid &= validator.validate();
    if (valid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return valid;
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(saveAction)) {
        doSaveCounfirm();
      } else if (event.getSource().equals(cancelAction)) {
        doCancel();
      }
    }
  }

  private void doSaveCounfirm() {
    GWTUtil.blurActiveElement();

    if (!validate())
      return;

    if (bill.getDepositTotal().compareTo(BigDecimal.ZERO) > 0) {
      RMsgBox.showConfirm(ReceiptMessages.M.saveRecConfirm(bill.getDepositTotal().doubleValue()),
          new RMsgBox.ConfirmCallback() {
            @Override
            public void onClosed(boolean confirmed) {
              if (!confirmed)
                return;
              else
                doSave();
            }
          });
    } else {
      doSave();
    }
  }

  private void doSave() {
    GWTUtil.blurActiveElement();

    if (!validate())
      return;

    CommandQueue.offer(new RPCCommand() {
      public void onCall(CommandQueue queue, AsyncCallback callback) {
        RLoadingDialog.show(ReceiptMessages.M.actionDoing(ReceiptMessages.M.save()));
        ReceiptService.Locator.getService().save(bill, getEP().getProcessCtx(), callback);
      }

      public void onFailure(CommandQueue queue, Throwable t) {
        RLoadingDialog.hide();
        String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.save(),
            PPaymentDef.TABLE_CAPTION);
        RMsgBox.showError(msg, t);
        queue.abort();
      }

      public void onSuccess(CommandQueue queue, Object result) {
        RLoadingDialog.hide();
        BPaymentLogger.getInstance().log(ReceiptMessages.M.create(), (BPayment) result);

        Message msg = Message.info(ReceiptMessages.M.actionSuccess(ReceiptMessages.M.save(),
            PPaymentDef.TABLE_CAPTION));
        getEP().jumpToViewPage(((BPayment) result).getUuid(), msg);
        queue.goon();
      }
    });
    CommandQueue.awake();
  }

  private void doCancel() {
    RMsgBox.showConfirm(
        ReceiptMessages.M.actionComfirm(ReceiptMessages.M.cancel(), ReceiptMessages.M.create()),
        new RMsgBox.ConfirmCallback() {
          public void onClosed(boolean confirmed) {
            if (confirmed)
              RHistory.back();
          }
        });
  }

  private void doChangeDefrayalType() {
    getMessagePanel().clearMessages();
    generalGadget.clearValidResults();
    refreshTabVisible();
    if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      // 按科目，账款应收金额默认=本次应收
      for (BPaymentAccountLine line : bill.getAccountLines()) {
        line.setTotal(BTotal.zero());
        line.setDefrayalTotal(BigDecimal.ZERO);
        line.setDepositTotal(BigDecimal.ZERO);
        line.getCashs().clear();
        line.getDeposits().clear();
        line.getDeductions().clear();
      }
      for (BPaymentCollectionLine line : bill.getCollectionLines()) {
        line.setTotal(BTotal.zero());
        line.setDefrayalTotal(BigDecimal.ZERO);
        line.setDepositTotal(BigDecimal.ZERO);
        line.getCashs().clear();
      }
      bill.aggregate(ep.getScale(), ep.getRoundingMode());
      // 重算滞纳金
      BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(),
          ep.getOverdueDefault());
      RActionEvent.fire(ReceiptCreatePage.this, ActionName.ACTION_REFRESH_AFFTER_APPORTION);

      if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
        collectionLineTab.setWidget(collectionLineSingleLineEditGridWidget);
        collectionLineSingleLineEditGrid.refresh();
      } else {
        collectionLineTab.setWidget(collectionLineLineEditWidget);
        collectionLineLineEditGrid.setBill(bill);
      }
    } else {
      collectionLineTab.setWidget(collectionLineEditGridWidget);
      collectionLineEditGrid.refresh();
      // 按总额，重新分摊
      RActionEvent.fire(ReceiptCreatePage.this, ActionName.ACTION_ACCOUNTLINE_CHANGE);
    }

    refreshMiddle();

    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      accountLineEditGrid.clearValidResults();
      overdueLineEditGrid.clearValidResults();
      paymentInfoViewGadget.setVisible(false);
      paymentInfoEditGadget.setVisible(true);
      paymentInfoEditGadget.refresh();
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      accountLineEditGrid.clearValidResults();
      overdueLineEditGrid.clearValidResults();

      paymentInfoViewGadget.setVisible(true);
      paymentInfoEditGadget.setVisible(false);
      paymentInfoViewGadget.refresh();
      lineDefrayalValidator = new BPaymentLineDefrayalValidator2(bill);
    } else if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
      accountLineMultiEditGrid.clearValidResults();
      overdueLineEditGrid.clearValidResults();

      paymentInfoViewGadget.setVisible(true);
      paymentInfoEditGadget.setVisible(false);
      paymentInfoViewGadget.refresh();

      validator = new BReceiptValidator(bill, true);
      lineDefrayalValidator = new BPaymentLineDefrayalValidator2(bill);
    }
    validator = new BReceiptValidator(bill, false);
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected boolean doBeforeShowDialog() {
      if (BBizActions.DELETE.equals(outgoingDefinition.getBusinessAction())) {
        return true;
      } else {
        GWTUtil.blurActiveElement();
        return validate();
      }
    }

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      ReceiptServiceAgent.executeTask(operation, bill, processCtx, true, this);
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.ACTION_GENERALCREATE_COUONTERPARTCHANGED) {
      refresh();
    } else if (event.getActionName() == ActionName.ACTION_GENERALCREATE_RECEIVEDATECHANGED) {
      refreshMiddle();
    } else if (event.getActionName() == ActionName.ACTION_GENERALCREATE_DEFRAYALTYPECHANGED) {
      doChangeDefrayalType();
      initDefaultReceipt();
    } else if (ObjectUtil.isEquals(event.getActionName(),
        ActionName.ACTION_REFRESH_AFFTER_APPORTION)) {
      // 分摊重算后刷新滞纳金
      afterShareCalculation();
    } else if (ObjectUtil.isEquals(event.getActionName(), ActionName.ACTION_BILLACCOUNTLINE_CHANGE)
        || ObjectUtil.isEquals(event.getActionName(), ActionName.ACTION_BILLACCOUNTLINE_CHANGE2)) {
      // 刷新统计区域
      reComputeTotal();
    } else if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())
        && (event.getActionName() == ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE || event
            .getActionName() == ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE)) {
      reComputeTotal();
    } else if (event.getActionName() == ActionName.ACTION_ACCOUNTLINE_AGGREGATE) {
      reComputeTotal();
    } else if (event.getActionName() == ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUELINES) {
      overdueLineEditGrid.refreshData();
    } else if (event.getActionName() == ActionName.ACTION_BILLOVERDUELINE_UNPAYEDTOTALCHANGE) {
      reComputeTotal();
    } else if (event.getActionName() == ActionName.ACTION_SHARE_CALCULATION) {
      new BReceiptDefrayalApportionHelper().apportion(bill, ep.getScale(), ep.getRoundingMode());
      doChangeDefrayalTotal();
      afterShareCalculation();
    } else if (event.getActionName() == ActionName.ACTION_RECEIVABLE_CHANGE) {
      reComputeTotal();
    } else if (event.getActionName() == "refreshCash") { // 按总额收款时，设置默认收款金额=应收金额，刷新收款信息
      paymentInfoEditGadget.refresh();
      new BReceiptDefrayalApportionHelper().apportion(bill, ep.getScale(), ep.getRoundingMode());
      doChangeDefrayalTotal();
      afterShareCalculation();
    }
  }

  /** 合计本次应收付金额以及账款应收付金额预存款金额,并刷新 */
  private void reComputeTotal() {
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    generalGadget.refreshRightPanel();
    paymentInfoViewGadget.refresh();
  }

  private void afterShareCalculation() {
    overdueLineEditGrid.refreshData();
    accountLineEditGrid.refreshData();
    collectionLineEditGrid.refresh();
    generalGadget.refreshRightPanel();
  }

  private void addActionHandler() {
    paymentInfoEditGadget.addActionHandler(generalGadget);
    paymentInfoEditGadget.addActionHandler(accountLineEditGrid);

    accountLineEditGrid.addActionHandler(paymentInfoEditGadget);
    accountLineEditGrid.addActionHandler(generalGadget);
    accountLineEditGrid.addActionHandler(accountLineEditGrid);
    accountLineEditGrid.addActionHandler(this);
    accountLineEditGrid.addActionHandler(overdueLineEditGrid);

    collectionLineEditGrid.addActionHandler(this);
    collectionLineSingleLineEditGrid.addActionHandler(this);
    collectionLineLineEditGrid.addActionHandler(this);
    collectionLineLineEditGrid.addActionHandler(generalGadget);

    generalGadget.addActionHandler(collectionLineEditGrid);
    generalGadget.addActionHandler(collectionLineSingleLineEditGrid);
    generalGadget.addActionHandler(collectionLineLineEditGrid);
    generalGadget.addActionHandler(accountLineMultiEditGrid);
    generalGadget.addActionHandler(paymentInfoEditGadget);
    generalGadget.addActionHandler(collectionLineSingleLineEditGrid);
    generalGadget.addActionHandler(this);

    this.addActionHandler(accountLineEditGrid);
    this.addActionHandler(accountLineMultiEditGrid);
    this.addActionHandler(generalGadget);

    overdueLineEditGrid.addActionHandler(generalGadget);
    overdueLineEditGrid.addActionHandler(this);

    // Multi
    accountLineMultiEditGrid.addActionHandler(this);
    accountLineMultiEditGrid.addActionHandler(generalGadget);
    accountLineMultiEditGrid.addActionHandler(overdueLineEditGrid);
    accountLineMultiEditGrid.addActionHandler(paymentInfoEditGadget);

    paymentInfoEditGadget.addActionHandler(accountLineMultiEditGrid);
  }

  @Override
  public void onSelection(SelectionEvent<Message> event) {
    if (event.getSelectedItem().getSource() instanceof PaymentLineLocator) {
      PaymentLineLocator o = (PaymentLineLocator) event.getSelectedItem().getSource();
      if (o != null) {
        if (o.getTabId() != null) {
          tabPanel.selectTab(o.getTabId());
          RActionEvent.fire(ReceiptCreatePage.this, ActionName.CREATEPAGE_CHANGE_LINE, o);
        }
      }
    } else if (event.getSelectedItem().getSource() instanceof PaymentLineDefrayalLineLocator) {
      PaymentLineDefrayalLineLocator o = (PaymentLineDefrayalLineLocator) event.getSelectedItem()
          .getSource();
      if (o != null) {
        if (o.getTabId() != null) {
          tabPanel.selectTab(o.getTabId());
          RActionEvent.fire(ReceiptCreatePage.this,
              ActionName.CREATEPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE, o);
        }
      }
    }
  }

  /** 合计实收总额以及产生的预存款金额 */
  private void doChangeDefrayalTotal() {
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      BigDecimal defrayalTotal = BigDecimal.ZERO;
      defrayalTotal.setScale(2, RoundingMode.HALF_UP);
      // 合计实收总额
      for (BPaymentCashDefrayal cash : bill.getCashs()) {
        if (cash.getTotal() == null)
          continue;
        defrayalTotal = defrayalTotal.add(cash.getTotal());
      }
      // 合计扣预存款总额
      for (BPaymentDepositDefrayal deposit : bill.getDeposits()) {
        if (deposit.getTotal() == null)
          continue;
        defrayalTotal = defrayalTotal.add(deposit.getTotal());
      }
      bill.setDefrayalTotal(defrayalTotal);
      bill.aggregate(ep.getScale(), ep.getRoundingMode());
    }
  }

  private void resetContract() {
    if (bill.getCounterpart() == null || bill.getCounterpart().getUuid() == null
        || bill.getAccountUnit() == null || bill.getAccountUnit().getUuid() == null) {
      resetCollectionContract(null);
      return;
    }
    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(ReceiptMessages.M.loading());
    ReceiptService.Locator.getService().getContract(bill.getAccountUnit().getUuid(),
        bill.getCounterpart().getUuid(), bill.getCounterpart().getCounterpartType(),
        new RBAsyncCallback2<BContract>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                PPaymentCollectionLineDef.constants.contract());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BContract result) {
            RLoadingDialog.hide();
            resetCollectionContract(result);
          }
        });
  }

  private void resetCollectionContract(BContract contract) {
    collectionLineEditGrid.setContract(contract);
    collectionLineLineEditGrid.setContract(contract);
    collectionLineSingleLineEditGrid.setContract(contract);
  }
}
