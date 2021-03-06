/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2015-10-9 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SubjectFilter;
import com.hd123.m3.account.gwt.payment.commons.client.AdvanceSubjectFilter;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
import com.hd123.m3.account.gwt.payment.commons.client.PaymentGridCellStyleUtil;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccOverdue;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueTerm;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByBillBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByInvoiceRegBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByPaymentNoticeBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByStatementBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentTypeComboBox;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.SubjectComboBox;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.SubjectComboBox.WidgetRes;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCashDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDepositDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineCashDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDepositDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptDefrayalApportionHelper;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptServiceAgent;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountInvoice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.SortDef;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.event.SortEvent;
import com.hd123.rumba.gwt.widget2.client.event.SortHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 账款明细编辑表格(按总额、按科目单收款)
 * 
 * @author lixiaohong
 * 
 */
public class AccountLineEditGrid extends RCaptionBox implements RActionHandler, HasRActionHandlers {

  private EPReceipt ep = EPReceipt.getInstance();
  private BPayment bill;
  /** 账款明细行 */
  private List<BPaymentAccountLine> lines;
  private int currentRow = -1;

  private Label accountTotalNumber;
  private RAction statementAction;
  private RAction invoiceRegAction;
  private RAction paymentNoticeAction;
  private RAction sourceBillAction;
  private RToolbarSplitButton addButton;
  private RAction accountAction;
  private RAction deleteAction;

  private AccByStatementBrowserDialog statementDialog;
  private AccByInvoiceRegBrowserDialog invoiceRegDialog;
  private AccByPaymentNoticeBrowserDialog paymentNoticeDialog;
  private AccByBillBrowserDialog sourceBillDialog;
  private AccBrowserDialog accDialog;

  private EditGrid<BPaymentAccountLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef originTotalCol;
  private RGridColumnDef originTaxCol;
  private RGridColumnDef unpayedTotalCol;
  private RGridColumnDef unpayedTaxCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef leftTotalCol;
  private RGridColumnDef defrayalTotalCol;
  private RGridColumnDef overdueTotalCol;
  private RGridColumnDef linePaymentTypeCol;
  private RGridColumnDef lineTotalCol;
  private RGridColumnDef lineBankCol;
  private RGridColumnDef lineGenDepSubjectCol;
  private RGridColumnDef lineGenDepTotalCol;
  private RGridColumnDef lineDepSubjectCol;
  private RGridColumnDef lineDepTotalCol;
  private RGridColumnDef remarkCol;
  private boolean isEdit = false;
  private boolean checkEmpty = false;

  // 事件处理
  private ActionHandler actionHandler = new ActionHandler();
  private CellRendererFactory rendererFactory;

  private static final int BANK_COLUMN = 16;

  /** 扣预存款科目下拉框选项值 */
  private List<BUCN> subjects = new ArrayList<BUCN>();
  /** 产生预存款科目下拉框选项值 */
  private List<BUCN> genDepSubjects = new ArrayList<BUCN>();

  protected EPReceipt getEP() {
    return EPReceipt.getInstance();
  }

  @Override
  public boolean isValid() {
    return validate();
  }

  @Override
  public boolean validate() {
    checkEmpty = true;
    boolean isValid = grid.validate();
    checkEmpty = false;
    return isValid;
  }

  public void setIsEdit(boolean isEdit) {
    this.isEdit = isEdit;
  }

  public void onHide() {
    if (statementDialog != null)
      statementDialog.clearConditions();
    if (invoiceRegDialog != null)
      invoiceRegDialog.clearConditions();
    if (paymentNoticeDialog != null)
      paymentNoticeDialog.clearConditions();
    if (sourceBillDialog != null)
      sourceBillDialog.clearConditions();
    if (accDialog != null)
      accDialog.clearConditions();
    accountTotalNumber.setText(ReceiptMessages.M.resultTotal(0));
  }

  public void refreshButton() {
    if (getEP().isBpmMode() == false) {
      statementAction.setEnabled(getEP().isPermitted(ReceiptPermDef.ADDFROMSTATEMENT));
      invoiceRegAction.setEnabled(getEP().isPermitted(ReceiptPermDef.ADDFROMINVOICE));
      paymentNoticeAction.setEnabled(getEP().isPermitted(ReceiptPermDef.ADDFROMPAYNOTICE));
      sourceBillAction.setEnabled(getEP().isPermitted(ReceiptPermDef.ADDFROMSRCBILL));
      accountAction.setEnabled(getEP().isPermitted(ReceiptPermDef.ADDFROMACCOUNT));
    } else {
      statementAction.setEnabled(true);
      invoiceRegAction.setEnabled(true);
      paymentNoticeAction.setEnabled(true);
      sourceBillAction.setEnabled(true);
      accountAction.setEnabled(true);
    }

    clearValidResults();
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
    resetBankOptions();
  }

  public void refreshGrid() {
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      defrayalTotalCol.setVisible(false);
      totalCol.setVisible(true);
      linePaymentTypeCol.setVisible(false);
      lineTotalCol.setVisible(false);
      lineBankCol.setVisible(false);
      lineDepSubjectCol.setVisible(false);
      lineDepTotalCol.setVisible(false);
      lineGenDepSubjectCol.setVisible(false);
      lineGenDepTotalCol.setVisible(false);
      resetBankOptions();
    } else if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      defrayalTotalCol.setVisible(true);
      totalCol.setVisible(false);
      linePaymentTypeCol.setVisible(true);
      lineTotalCol.setVisible(true);
      lineBankCol.setVisible(true);
      lineDepSubjectCol.setVisible(true);
      lineDepTotalCol.setVisible(true);
      lineGenDepSubjectCol.setVisible(true);
      lineGenDepTotalCol.setVisible(true);
    }

    boolean showTax = ep.getConfig().isShowTax();
    taxRateCol.setVisible(showTax);
    originTaxCol.setVisible(showTax);
    unpayedTaxCol.setVisible(showTax);

    grid.rebuild();
  }

  public void doApportionInit() {
    doApportion(false);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public AccountLineEditGrid() {
    super();
    setContent(drawGrid());
    setEditing(true);
    setCaption(PPaymentLineDef.TABLE_CAPTION);
    setStyleName(RCaptionBox.STYLENAME_STANDARD);
    setBorder(false);
    getCaptionBar().setShowCollapse(true);
    setWidth("100%");

    accountTotalNumber = new Label(ReceiptMessages.M.resultTotal(0));
    getCaptionBar().addButton(accountTotalNumber);

    RPopupMenu addMenu = new RPopupMenu();
    statementAction = new RAction(ReceiptMessages.M.statementImport(), actionHandler);
    statementAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(statementAction));
    invoiceRegAction = new RAction(ReceiptMessages.M.invoiceRegImport(), actionHandler);
    invoiceRegAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(invoiceRegAction));
    paymentNoticeAction = new RAction(ReceiptMessages.M.paymentNoticeImport(), actionHandler);
    paymentNoticeAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(paymentNoticeAction));
    sourceBillAction = new RAction(ReceiptMessages.M.sourceBillImport(), actionHandler);
    sourceBillAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(sourceBillAction));
    accountAction = new RAction(ReceiptMessages.M.accountImport(), actionHandler);
    accountAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(accountAction));

    addButton = new RToolbarSplitButton(RActionFacade.APPEND);
    addButton.setMenu(addMenu);
    addButton.setShowText(false);
    addButton.addClickHandler(actionHandler);
    getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, actionHandler);
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);
  }

  private Widget drawGrid() {
    grid = new EditGrid<BPaymentAccountLine>();
    grid.setCaption(PPaymentLineDef.TABLE_CAPTION);
    grid.setWidth("100%");
    grid.setShowHorizontalLine(true);
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);
    Handler_grid handler = new Handler_grid();
    grid.addHighlightHandler(handler);
    grid.addClickHandler(handler);

    rendererFactory = new CellRendererFactory();
    lineNumberCol = new RGridColumnDef(PPaymentAccountLineDef.lineNumber);
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    lineNumberCol.setResizable(true);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_subject);
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("130px"));
    subjectCol.setOverflowEllipsis(true);
    subjectCol.setSortable(true);
    subjectCol.setResizable(true);
    subjectCol.setWidth("150px");
    grid.addColumnDef(subjectCol);

    sourceBillNumberCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_sourceBill_billNumber);
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    sourceBillNumberCol.setResizable(true);
    sourceBillNumberCol.setSortable(true);
    grid.addColumnDef(sourceBillNumberCol);

    sourceBillTypeCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_sourceBill_billType);
    sourceBillTypeCol.setWidth("100px");
    sourceBillTypeCol.setResizable(true);
    grid.addColumnDef(sourceBillTypeCol);

    dateRangeCol = new RGridColumnDef(CommonMessages.M.dateRange());
    dateRangeCol.setName(BPaymentAccountLine.ORDER_BY_BEGINTIME);
    dateRangeCol.setWidth("130px");
    dateRangeCol.setResizable(true);
    dateRangeCol.setSortable(true);
    grid.addColumnDef(dateRangeCol);

    taxRateCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_taxRate_rate);
    taxRateCol.setWidth("90px");
    taxRateCol.setResizable(true);
    grid.addColumnDef(taxRateCol);

    originTotalCol = new RGridColumnDef(ReceiptMessages.M.originTotal_total());
    originTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTotalCol.setWidth("70px");
    originTotalCol.setResizable(true);
    grid.addColumnDef(originTotalCol);

    originTaxCol = new RGridColumnDef(ReceiptMessages.M.originTotal_tax());
    originTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTaxCol.setWidth("70px");
    originTaxCol.setResizable(true);
    grid.addColumnDef(originTaxCol);

    unpayedTotalCol = new RGridColumnDef(PPaymentDef.unpayedTotal_total);
    unpayedTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTotalCol.setWidth("88px");
    unpayedTotalCol.setResizable(true);
    grid.addColumnDef(unpayedTotalCol);

    unpayedTaxCol = new RGridColumnDef(PPaymentDef.unpayedTotal_tax);
    unpayedTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTaxCol.setWidth("88px");
    unpayedTaxCol.setResizable(true);
    grid.addColumnDef(unpayedTaxCol);

    leftTotalCol = new RGridColumnDef(ReceiptMessages.M.leftTotal_total());
    leftTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    leftTotalCol.setWidth("88px");
    leftTotalCol.setResizable(true);
    grid.addColumnDef(leftTotalCol);

    totalCol = new RGridColumnDef(PPaymentAccountLineDef.total_total);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("88px");
    totalCol.setResizable(true);
    grid.addColumnDef(totalCol);

    defrayalTotalCol = new RGridColumnDef(PPaymentAccountLineDef.defrayalTotal);
    defrayalTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    defrayalTotalCol.setWidth("88px");
    defrayalTotalCol.setResizable(true);
    grid.addColumnDef(defrayalTotalCol);

    overdueTotalCol = new RGridColumnDef(PPaymentAccountLineDef.overdueTotal_total);
    overdueTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTotalCol.setWidth("70px");
    overdueTotalCol.setResizable(true);
    grid.addColumnDef(overdueTotalCol);

    linePaymentTypeCol = new RGridColumnDef(PPaymentLineCashDef.paymentType);
    linePaymentTypeCol.setWidth("70px");
    linePaymentTypeCol.setResizable(true);
    linePaymentTypeCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(linePaymentTypeCol);

    lineTotalCol = new RGridColumnDef(PPaymentLineCashDef.total);
    lineTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    lineTotalCol.setWidth("70px");
    lineTotalCol.setResizable(true);
    lineTotalCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(lineTotalCol);

    lineBankCol = new RGridColumnDef(PPaymentLineCashDef.bankAccount);
    lineBankCol.setWidth("70px");
    lineBankCol.setResizable(true);
    lineBankCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(lineBankCol);

    lineGenDepSubjectCol = new RGridColumnDef("产生预存款科目");
    lineGenDepSubjectCol.setWidth("100px");
    lineGenDepSubjectCol.setResizable(true);
    lineGenDepSubjectCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(lineGenDepSubjectCol);

    lineGenDepTotalCol = new RGridColumnDef(PPaymentLineDepositDef.gendeposit);
    lineGenDepTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    lineGenDepTotalCol.setWidth("70px");
    lineGenDepTotalCol.setResizable(true);
    grid.addColumnDef(lineGenDepTotalCol);

    lineDepSubjectCol = new RGridColumnDef(PPaymentLineDepositDef.subject);
    lineDepSubjectCol.setWidth("70px");
    lineDepSubjectCol.setResizable(true);
    lineDepSubjectCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(lineDepSubjectCol);

    lineDepTotalCol = new RGridColumnDef(PPaymentLineDepositDef.total_deposit);
    lineDepTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    lineDepTotalCol.setWidth("108px");
    lineDepTotalCol.setResizable(true);
    lineDepTotalCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(lineDepTotalCol);

    remarkCol = new RGridColumnDef(PPaymentAccountLineDef.remark);
    remarkCol.setWidth("200px");
    remarkCol.setResizable(true);
    remarkCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    grid.setAllowHorizontalScrollBar(true);
    grid.addSortHandler(new GridColumnSortHandler());
    grid.setSort(dateRangeCol, OrderDir.asc, true);
    grid.addRefreshHandler(new GridRefreshHandler());
    return grid;
  }

  private class GridColumnSortHandler implements SortHandler<RGridColumnDef> {
    @Override
    public void onSort(SortEvent<RGridColumnDef> event) {
      if (event.getSortDefs().isEmpty() == false) {
        List<Order> orders = new ArrayList<Order>();
        SortDef<RGridColumnDef> sort = event.getSortDefs().get(0);
        OrderDir dir = sort.getDir();
        if (subjectCol == sort.getField()) {
          orders.addAll(buildSubjectOrders(dir));
        } else if (sourceBillNumberCol == sort.getField()) {
          orders.addAll(buildSourceBillOrders(dir));
        } else if (dateRangeCol == sort.getField()) {
          orders.addAll(buildBeginTimeOrders(dir));
        }
        sortLines(orders);
      }
    }
  }

  private void sortLines(List<Order> orders) {
    GWTUtil.enableSynchronousRPC();
    ReceiptService.Locator.getService().sortLines(lines, orders,
        new AsyncCallback<List<BPaymentAccountLine>>() {
          @Override
          public void onSuccess(List<BPaymentAccountLine> result) {
            if (lines != null) {
              lines.clear();
              lines.addAll(result);
              refreshGridShow();
            }
          }

          @Override
          public void onFailure(Throwable caught) {
            // Do Nothing
          }
        });
  }

  public List<Order> buildSubjectOrders(OrderDir dir) {
    List<Order> orders = new ArrayList<Order>();
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_SUBJECT, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_SOURCEBILL, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_BEGINTIME, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_ENDTIME, dir));
    return orders;
  }

  public List<Order> buildSourceBillOrders(OrderDir dir) {
    List<Order> orders = new ArrayList<Order>();
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_SOURCEBILL, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_BEGINTIME, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_ENDTIME, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_SUBJECT, dir));
    return orders;
  }

  public List<Order> buildBeginTimeOrders(OrderDir dir) {
    List<Order> orders = new ArrayList<Order>();
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_BEGINTIME, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_ENDTIME, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_SUBJECT, dir));
    orders.add(new Order(BPaymentAccountLine.ORDER_BY_SOURCEBILL, dir));
    return orders;
  }

  private class GridDataProvider implements RGridDataProvider,
      EditGridDataProduce<BPaymentAccountLine> {
    @Override
    public int getRowCount() {
      return lines == null ? 0 : lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (lines.size() == 0)
        return null;

      BPaymentAccountLine line = lines.get(row);
      if (line.getCashs().size() == 0) {
        BPaymentLineCash lineCash = new BPaymentLineCash();
        lineCash.setItemNo(0);
        lineCash.setPaymentType(EPReceipt.getInstance().getDefaultOption().getPaymentType());
        if (lineCash.getPaymentType() == null
            && EPReceipt.getInstance().getPaymentTypes(false).size() > 0) {
          lineCash.setPaymentType(EPReceipt.getInstance().getPaymentTypes(false).get(0));
        }
        line.getCashs().add(lineCash);
      }
      if (line.getDeposits().size() == 0) {
        BPaymentLineDeposit lineDeposit = new BPaymentLineDeposit();
        lineDeposit.setSubject(EPReceipt.getInstance().getDefaultOption().getPrePaySubject());
        lineDeposit.setItemNo(0);
        line.getDeposits().add(lineDeposit);
      }
      if (col == lineNumberCol.getIndex())
        return line.getLineNumber();
      else if (col == subjectCol.getIndex())
        return line.getAcc1().getSubject() == null ? null : line.getAcc1().getSubject()
            .toFriendlyStr();
      else if (col == sourceBillNumberCol.getIndex()) {
        if (line.getAcc1().getSourceBill() == null) {
          return null;
        }

        BDispatch dispatch = new BDispatch(line.getAcc1().getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), line.getAcc1().getSourceBill().getBillNumber());
        return dispatch;
      } else if (col == sourceBillTypeCol.getIndex()) {
        BSourceBill sourceBill = line.getAcc1().getSourceBill();
        if (sourceBill == null || sourceBill.getBillType() == null) {
          return null;
        }

        BBillType type = ep.getBillTypeMap().get(line.getAcc1().getSourceBill().getBillType());
        return type == null ? null : type.getCaption();
      } else if (col == dateRangeCol.getIndex())
        return buildDateRange(line.getAcc1().getBeginTime(), line.getAcc1().getEndTime());
      else if (col == taxRateCol.getIndex())
        return line.getAcc1().getTaxRate() == null ? null : line.getAcc1().getTaxRate().toString();
      else if (col == originTotalCol.getIndex())
        return line.getOriginTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getOriginTotal().getTotal().doubleValue());
      else if (col == originTaxCol.getIndex())
        return line.getOriginTotal().getTax() == null ? null : GWTFormat.fmt_money.format(line
            .getOriginTotal().getTax().doubleValue());
      else if (col == unpayedTotalCol.getIndex())
        return line.getUnpayedTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getUnpayedTotal().getTotal().doubleValue());
      else if (col == unpayedTaxCol.getIndex())
        return line.getUnpayedTotal().getTax() == null ? null : GWTFormat.fmt_money.format(line
            .getUnpayedTotal().getTax().doubleValue());
      else if (col == totalCol.getIndex())
        return line.getTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getTotal().getTotal().doubleValue());
      else if (col == leftTotalCol.getIndex())
        return line.getLeftTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getLeftTotal().getTotal().doubleValue());
      else if (col == defrayalTotalCol.getIndex()
          && CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType()))
        return line.getDefrayalTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getDefrayalTotal().doubleValue());
      else if (col == overdueTotalCol.getIndex())
        return line.getOverdueTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getOverdueTotal().getTotal().doubleValue());
      else if (col == linePaymentTypeCol.getIndex())
        return line.getCashs().get(0).getPaymentType();
      else if (col == lineTotalCol.getIndex()) {
        return line.getCashs().get(0).getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getCashs().get(0).getTotal().doubleValue());
      } else if (col == lineBankCol.getIndex())
        return line.getCashs().get(0).getBank();
      else if (col == lineDepSubjectCol.getIndex())
        return line.getDeposits().get(0).getSubject();
      else if (col == lineDepTotalCol.getIndex())
        return line.getDeposits().get(0).getTotal() == null ? null : GWTFormat.fmt_money
            .format(line.getDeposits().get(0).getTotal().doubleValue());
      else if (col == remarkCol.getIndex())
        return line.getRemark();
      else if (col == lineGenDepSubjectCol.getIndex()) {
        return line.getDepositSubject();
      } else if (col == lineGenDepTotalCol.getIndex())
        return line.getDepositTotal();
      else
        return null;
    }

    @Override
    public BPaymentAccountLine create() {
      BPaymentAccountLine detail = new BPaymentAccountLine();
      return detail;
    }
  }

  private String buildDateRange(Date beginDate, Date endDate) {
    if (beginDate == null && endDate == null)
      return null;
    StringBuffer sb = new StringBuffer();
    if (beginDate != null)
      sb.append(M3Format.fmt_yMd.format(beginDate));
    sb.append("~");
    if (endDate != null)
      sb.append(M3Format.fmt_yMd.format(endDate));
    return sb.toString();
  }

  private class Handler_grid implements ClickHandler, HighlightHandler<Point> {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCol)) {
        BUCN subject = lines.get(cell.getRow()).getAcc1().getSubject();
        if (subject == null || StringUtil.isNullOrBlank(subject.getUuid()))
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subject.getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = ReceiptMessages.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    }

    @Override
    public void onHighlight(HighlightEvent<Point> event) {
      final int row = event.getHighlighted().getY();
      currentRow = row;
    }

  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      String fieldName = colDef.getName();
      if (fieldName.equals(linePaymentTypeCol.getName())) {
        return new LinePaymentTypeRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(lineTotalCol.getName())) {
        return new LineTotalRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(lineBankCol.getName())) {
        return new LineBankRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(lineDepSubjectCol.getName())) {
        return new LineDepSubjectRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(lineDepTotalCol.getName())) {
        return new LineDepTotalRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(remarkCol.getName())) {
        return new RemarkTextRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(lineGenDepSubjectCol.getName())) {
        return new LineGenDepSubjectRenderer(grid, colDef, row, col, data);
      }
      return null;
    }
  }

  /**
   * 刷新滞纳金
   */
  public void refreshOverdue() {
    // 计算账款明细滞纳金
    BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(), ep.getOverdueDefault());
  }

  /** 刷新当前行的实收金额 */
  private void refreshDefrayalTotal(BPaymentAccountLine line) {
    BigDecimal defrayalTotal = BigDecimal.ZERO;
    defrayalTotal.setScale(2, RoundingMode.HALF_UP);
    for (BPaymentLineCash cash : line.getCashs()) {
      if (cash.getTotal() == null || cash.getTotal().compareTo(BigDecimal.ZERO) == 0)
        continue;
      defrayalTotal = defrayalTotal.add(cash.getTotal());
    }
    for (BPaymentLineDeposit deposit : line.getDeposits()) {
      if (deposit.getTotal() == null || deposit.getTotal().compareTo(BigDecimal.ZERO) <= 0)
        continue;
      defrayalTotal = defrayalTotal.add(deposit.getTotal());
    }
    BigDecimal newTotal = BigDecimal.ZERO;
    if (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0) {
      if (line.getUnpayedTotal().getTotal() != null
          && line.getUnpayedTotal().getTotal().compareTo(defrayalTotal) > 0) {
        newTotal = defrayalTotal;
      } else {
        newTotal = line.getUnpayedTotal().getTotal();
      }
    } else {
      if (line.getUnpayedTotal().getTotal() != null
          && line.getUnpayedTotal().getTotal().compareTo(defrayalTotal) > 0) {
        newTotal = line.getUnpayedTotal().getTotal();
      } else {
        newTotal = defrayalTotal;
      }
    }

    line.getTotal().setTotal(newTotal);
    line.getTotal().setTax(
        BTaxCalculator.tax(line.getTotal().getTotal(), line.getAcc1().getTaxRate(), ep.getScale(),
            ep.getRoundingMode()));
    line.setDefrayalTotal(defrayalTotal);
    if (line.getAcc2().getLastPayDate() != null)
      refreshOverdue();
    RActionEvent.fire(AccountLineEditGrid.this,
        ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE);

  }

  /**
   * 获取科目余额
   */
  public void getRemainTotal(final BPaymentLineDeposit value) {
    if (bill.getAccountUnit() == null || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid()) || value.getSubject() == null) {
      value.setRemainTotal(BigDecimal.ZERO);
      return;
    }

    RLoadingDialog.show(ReceiptMessages.M.loading2(ReceiptMessages.M.depositRecSubject()
        + ReceiptMessages.M.remainTotal()));
    GWTUtil.enableSynchronousRPC();
    PaymentCommonsService.Locator
        .getService()
        .getDepositSubjectRemainTotal(
            bill.getAccountUnit().getUuid(),
            bill.getCounterpart().getUuid(),
            (value.getContract() == null || StringUtil.isNullOrBlank(value.getContract().getUuid())) ? "-"
                : value.getContract().getUuid(), value.getSubject().getUuid(),
            new RBAsyncCallback2<BigDecimal>() {
              @Override
              public void onException(Throwable caught) {
                RLoadingDialog.hide();
                String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                    ReceiptMessages.M.depositRecSubject() + ReceiptMessages.M.remainTotal());
                RMsgBox.showErrorAndBack(msg, caught);
              }

              @Override
              public void onSuccess(BigDecimal result) {
                RLoadingDialog.hide();
                value.setRemainTotal(result);
              }
            });
  }

  private class LineTotalRenderer extends EditGridCellWidgetRenderer implements ChangeHandler {

    private RNumberBox field;

    public LineTotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox();
      field.setSelectAllOnFocus(true);
      field.setScale(2);
      field.setFormat(GWTFormat.fmt_money);
      field.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
      field.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
      field.addChangeHandler(this);
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          if (field.isValid() == false) {
            return;
          } else {
            field.clearValidResults();
          }
          BPaymentAccountLine value = bill.getAccountLines().get(getRow());
          BPaymentLineCash cash = value.getCashs().get(0);
          cash.setTotal(field.getValueAsBigDecimal());
          refreshDefrayalTotal(value);
          grid.refreshRow(getRow());
          RActionEvent
              .fire(AccountLineEditGrid.this, ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
        }
      });
      field.setValidator(new RValidator() {
        @Override
        public Message validate(Widget sender, String value) {
          BPaymentAccountLine line = bill.getAccountLines().get(getRow());
          BigDecimal depositValue = line.getDeposits().get(0).getTotal();
          BigDecimal fieldValue = field.getValueAsBigDecimal();
          BigDecimal defrayalTotal = depositValue == null ? fieldValue : depositValue
              .add(fieldValue);
          if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())
              && line.getUnpayedTotal() != null && fieldValue != null
              && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0
              && fieldValue.compareTo(BigDecimal.ZERO) < 0) {
            return Message.error(ReceiptMessages.M.mustMoreThan(ReceiptMessages.M.receiptTotal(),
                BigDecimal.ZERO.toString()));
          }
          if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())
              && line.getUnpayedTotal() != null
              && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0
              && defrayalTotal.compareTo(BigDecimal.ZERO) < 0) {
            return Message.error(ReceiptMessages.M.forbidNegative());
          }
          if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())
              && line.getUnpayedTotal() != null
              && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) < 0
              && (defrayalTotal.compareTo(BigDecimal.ZERO) > 0 || defrayalTotal.compareTo(line
                  .getUnpayedTotal().getTotal()) < 0)) {
            return Message.error(ReceiptMessages.M.totalBetween(line.getUnpayedTotal().getTotal()
                .toString()));
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      field.setValue(detail.getCashs().get(0).getTotal());
    }

    @Override
    public void onChange(ChangeEvent event) {
      if (field.getValueAsBigDecimal() == null) {
        field.setValue(0, true);
      }
    }

  }

  private class LineBankRenderer extends EditGridCellWidgetRenderer implements RValidator {
    private RComboBox<BBank> field;

    public LineBankRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RComboBox(PPaymentCashDefrayalDef.constants.bankCode());
      field.setNullOptionTextToDefault();
      field.setNullOptionText("[空]");
      field.setMaxDropdownRowCount(10);
      field.setValidator(this);
      field.setEditable(false);
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          BPaymentAccountLine value = bill.getAccountLines().get(getRow());
          BPaymentLineCash cash = value.getCashs().get(0);
          cash.setBank(field.getValue());
          RActionEvent
              .fire(AccountLineEditGrid.this, ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      resetOptions();
      field.setValue(detail.getCashs().get(0).getBank());
    }

    public void resetOptions() {
      BBank value = field.getValue();
      List<BBank> results = new ArrayList<BBank>();
      field.clearOptions();
      if (bill.getAccountUnit() == null || bill.getAccountUnit().getUuid() == null) {
        return;
      }
      for (BBank bank : EPReceipt.getInstance().getBanks()) {
        if (bank.getStore() == null) {
          continue;
        }
        if (bill.getAccountUnit().getUuid().equals(bank.getStore().getUuid())) {
          results.add(bank);
        }
      }
      field.addOptionList(results);

      if (value != null) {
        field.setValue(value);
      }
    }

    @Override
    public Message validate(Widget sender, String value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      BPaymentLineCash cash = detail.getCashs().get(0);
      if (ep.getConfig().isBankRequired() == false) {
        return null;
      }
      if (checkEmpty == false || cash.getTotal().compareTo(BigDecimal.ZERO) == 0) {
        return null;
      }
      if (field.getValue() == null) {
        return Message.error(ReceiptMessages.M.notNull(field.getCaption()), field);
      }
      return null;
    }

  }

  private class LineDepSubjectRenderer extends EditGridCellWidgetRenderer {
    private SubjectComboBox field;

    public LineDepSubjectRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col,
        Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new SubjectComboBox();
      field.setEditable(false);
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          BPaymentAccountLine value = bill.getAccountLines().get(getRow());
          BPaymentLineDeposit deposit = value.getDeposits().get(0);
          deposit.setContract(value.getAcc1().getContract());
          deposit.setSubject(field.getValue());
          getRemainTotal(deposit);
          grid.refreshRow(getRow());
          RActionEvent
              .fire(AccountLineEditGrid.this, ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      final BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      ReceiptServiceAgent.filterSubjects(ReceiptServiceAgent.KEY_PREFIX_DECSUB, subjects,
          bill.getAccountUnit(), detail.getAcc1().getSubject(), detail.getAcc1().getContract(),
          new com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptServiceAgent.Callback() {
            @Override
            public void execute(List<BUCN> value) {
              field.refreshOptions(value);
              if (detail.getDeposits().get(0).getSubject() == null) {
                setDefaultSubject(field, detail.getDeposits().get(0));
              }
              field.setValue(detail.getDeposits().get(0).getSubject());
            }
          });

    }

  }

  private class LineGenDepSubjectRenderer extends EditGridCellWidgetRenderer {
    private SubjectComboBox field;

    public LineGenDepSubjectRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col,
        Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new SubjectComboBox();
      field.setEditable(false);
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          BPaymentAccountLine value = bill.getAccountLines().get(getRow());
          value.setDepositSubject(field.getValue());
          grid.refreshRow(getRow());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      final BPaymentAccountLine detail = bill.getAccountLines().get(getRow());

      ReceiptServiceAgent.filterSubjects(ReceiptServiceAgent.KEY_PREFIX_GENSUB, genDepSubjects,
          bill.getAccountUnit(), detail.getAcc1().getSubject(), detail.getAcc1().getContract(),
          new ReceiptServiceAgent.Callback() {
            @Override
            public void execute(List<BUCN> value) {
              field.refreshOptions(value);
              if (detail.getDepositSubject() == null) {
                setDefaultGenDepSubject(field, detail);
              }
              field.setValue(detail.getDepositSubject());
            }
          });

    }

    /**
     * 设置默认科目
     */
    private void setDefaultGenDepSubject(SubjectComboBox subjectField, BPaymentAccountLine value) {
      if (subjectField.getOptions().getByValue(
          EPReceipt.getInstance().getDefaultOption().getPreReceiveSubject()) != null) {
        value.setDepositSubject(EPReceipt.getInstance().getDefaultOption().getPreReceiveSubject());
      } else {
        if (subjectField.getOptions().size() > 0) {
          value.setDepositSubject(subjectField.getOptions().getValue(0));
        } else {
          value.setDepositSubject(null);
        }
      }
      subjectField.setValue(value.getDepositSubject(), true);

    }

  }

  private class LineDepTotalRenderer extends EditGridCellWidgetRenderer implements ChangeHandler {
    private RNumberBox field;

    public LineDepTotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox();
      field.setSelectAllOnFocus(true);
      field.setScale(2);
      field.setFormat(GWTFormat.fmt_money);
      field.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
      field.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
      field.addChangeHandler(this);
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          if (field.isValid() == false) {
            return;
          } else {
            field.clearValidResults();
          }
          BPaymentAccountLine value = bill.getAccountLines().get(getRow());
          BPaymentLineDeposit deposit = value.getDeposits().get(0);
          deposit.setTotal(field.getValueAsBigDecimal() == null ? BigDecimal.ZERO : field
              .getValueAsBigDecimal());
          refreshDefrayalTotal(value);
          grid.refreshRow(getRow());
          RActionEvent
              .fire(AccountLineEditGrid.this, ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
        }
      });
      field.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BPaymentAccountLine line = bill.getAccountLines().get(getRow());
          BPaymentLineDeposit deposit = line.getDeposits().get(0);
          BigDecimal cashValue = line.getCashs().get(0).getTotal();
          BigDecimal defrayalTotal = cashValue == null ? field.getValueAsBigDecimal() : cashValue
              .add(field.getValueAsBigDecimal());
          if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())
              && field.getValueAsBigDecimal() != null) {
            if (field.getValueAsBigDecimal().compareTo(BigDecimal.ZERO) < 0) {
              return Message.error(ReceiptMessages.M.cannotLessThan3(
                  PPaymentLineDepositDef.constants.total_deposit(), 0));
            } else if (field.getValueAsBigDecimal().compareTo(deposit.getRemainTotal()) > 0) {
              return Message.error(ReceiptMessages.M.cannotMoreThan(
                  PPaymentDepositDefrayalDef.constants.total(), ReceiptMessages.M.remainTotal()
                      + deposit.getRemainTotal()));
            }
            if (line.getUnpayedTotal() != null
                && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0
                && defrayalTotal.compareTo(BigDecimal.ZERO) < 0) {
              return Message.error(ReceiptMessages.M.forbidNegative());
            }
            if (line.getUnpayedTotal() != null
                && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) < 0
                && field.getValueAsBigDecimal().compareTo(BigDecimal.ZERO) > 0) {
              return Message.error(ReceiptMessages.M.lineNoDeposit());
            }
            if (line.getUnpayedTotal() != null
                && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) < 0
                && (defrayalTotal.compareTo(BigDecimal.ZERO) > 0 || defrayalTotal.compareTo(line
                    .getUnpayedTotal().getTotal()) < 0)) {
              return Message.error(ReceiptMessages.M.totalBetween(line.getUnpayedTotal().getTotal()
                  .toString()));
            }
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      field.setValue(detail.getDeposits().get(0).getTotal());
    }

    @Override
    public void onChange(ChangeEvent event) {
      if (field.getValueAsBigDecimal() == null) {
        field.setValue(0, true);
      }

    }

  }

  private class LinePaymentTypeRenderer extends EditGridCellWidgetRenderer {
    private PaymentTypeComboBox paymentTypeField;

    public LinePaymentTypeRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col,
        Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      paymentTypeField = new PaymentTypeComboBox(EPReceipt.getInstance().getPaymentTypes(false));
      paymentTypeField.setEditable(false);
      paymentTypeField.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BPaymentAccountLine value = bill.getAccountLines().get(getRow());
          value.getCashs().get(0).setPaymentType(paymentTypeField.getValue());
          RActionEvent
              .fire(AccountLineEditGrid.this, ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
        }
      });
      return paymentTypeField;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      BPaymentLineCash cash = detail.getCashs().get(0);
      paymentTypeField.setValue(cash.getPaymentType());
    }
  }

  private class RemarkTextRenderer extends EditGridCellWidgetRenderer {
    private RTextBox field;

    public RemarkTextRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler(false));
      field.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BPaymentAccountLine value = bill.getAccountLines().get(getRow());
          value.setRemark(field.getValue());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      field.setValue(detail.getRemark());
    }
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.ACTION_PAYMENTINFO_CHANGE
        || event.getActionName() == ActionName.ACTION_ACCOUNTLINE_CHANGE) {
      boolean apportioned = false;
      if (event.getParameters().size() > 0) {
        apportioned = (Boolean) event.getParameters().get(0);
      }
      doApportion(apportioned);
    }
    if (event.getActionName() == ActionName.ACTION_GENERALCREATE_REFRESH_REMAINTOTAL) {
      refreshData();
    }
    if (ActionName.ACTION_GENERALCREATE_REFRESH_BANKS.equals(event.getActionName())) {
      resetBankOptions();
    }
  }

  /** 项目改变时需要重新改变银行的下拉值 */
  private void resetBankOptions() {
    if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType()) == false
        || grid.getColumnCount() < BANK_COLUMN) {
      return;
    }

    for (int row = 0; row < grid.getRowCount(); row++) {
      Widget widget = grid.getWidget(row, BANK_COLUMN);
      if (widget instanceof LineBankRenderer) {
        LineBankRenderer bankField = (LineBankRenderer) widget;
        bankField.resetOptions();
      }
    }
  }

  private void doApportion(boolean apportioned) {
    // 按总额需要进行分摊计算
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType()) == false) {
      doRefresh();
      return;
    }
    // 实收金额发生变化-分摊账款实收-刷新表格
    if (apportioned == false) {
      new BReceiptDefrayalApportionHelper().apportion(bill, ep.getScale(), ep.getRoundingMode());
    }
    doRefresh();

    // 重置收款单单头中的产生的预存款金额
    bill.aggregate(ep.getScale(), ep.getRoundingMode());

    RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_REFRESH_AFFTER_APPORTION);
  }

  private boolean confirm() {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())) {
      RMsgBox.showError(ReceiptMessages.M.pleaseFillInFirst(getEP().getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business())));
      return false;
    } else if (bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      RMsgBox.showError(ReceiptMessages.M.pleaseFillInFirst(getEP().getFieldCaption(
          GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart())));
      return false;
    }
    return true;
  }

  private void doRefresh() {
    refreshLineNumber();
    grid.refresh();
    grid.setCurrentRow(currentRow);
    accountTotalNumber.setText(ReceiptMessages.M.resultTotal(bill.getAccountLines().size()));
  }

  private void refreshLineNumber() {
    for (int i = 0; i < lines.size(); i++) {
      BPaymentAccountLine line = lines.get(i);
      line.setLineNumber(i + 1);
    }
  }

  /**
   * 刷新科目下拉值选项
   */
  private void refreshSubjects() {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      subjects.clear();
      return;
    }

    GWTUtil.enableSynchronousRPC();
    AdvanceSubjectFilter filter = new AdvanceSubjectFilter();
    filter.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    filter.setDirectionType(DirectionType.receipt.getDirectionValue());
    filter.setCounterpartUuid(bill.getCounterpart().getUuid());
    PaymentCommonsService.Locator.getService().getSubjects(filter, new AsyncCallback<List<BUCN>>() {

      @Override
      public void onFailure(Throwable arg0) {
        String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
            WidgetRes.R.paymentSubject());
        RMsgBox.show(msg);
      }

      @Override
      public void onSuccess(List<BUCN> result) {
        subjects.clear();
        if (result == null || result.isEmpty()) {
          return;
        }
        subjects.addAll(result);
      }
    });
  }

  /**
   * 刷新产生预存款科目下拉值选项
   */
  private void refreshGenDepSubjects() {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      genDepSubjects.clear();
      return;
    }

    GWTUtil.enableSynchronousRPC();

    SubjectFilter filter = new SubjectFilter();
    filter.setState(true);
    filter.setSubjectType(BSubjectType.predeposit.name());
    filter.setDirectionType(DirectionType.receipt.getDirectionValue());

    AccountCpntsService.Locator.getService().querySubject(filter,
        new AsyncCallback<RPageData<BSubject>>() {

          @Override
          public void onFailure(Throwable arg0) {
            String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
                WidgetRes.R.paymentSubject());
            RMsgBox.show(msg);
          }

          @Override
          public void onSuccess(RPageData<BSubject> result) {
            genDepSubjects.clear();
            if (result.getValues().isEmpty()) {
              return;
            }

            for (BSubject subject : result.getValues()) {
              genDepSubjects.add(subject.getSubject());
            }
          }
        });
  }

  /**
   * 设置默认科目
   */
  private void setDefaultSubject(SubjectComboBox subjectField, BPaymentLineDeposit value) {
    if (subjectField.getOptions().getByValue(
        EPReceipt.getInstance().getDefaultOption().getPrePaySubject()) != null) {
      value.setSubject(EPReceipt.getInstance().getDefaultOption().getPrePaySubject());
    } else {
      if (subjectField.getOptions().size() > 0) {
        value.setSubject(subjectField.getOptions().getValue(0));
      } else {
        value.setSubject(null);
      }
    }
    subjectField.setValue(value.getSubject(), true);

    if (bill.getCounterpart() != null && !StringUtil.isNullOrBlank(bill.getCounterpart().getUuid()))
      getRemainTotal(value);
  }

  public void refreshData() {
    refreshSubjects();
    refreshGenDepSubjects();
    lines = bill.getAccountLines();
    grid.setDefaultDataRowCount(0);

    grid.setSort(dateRangeCol, OrderDir.asc, true);
    resetBankOptions();
  }

  private void refreshGridShow() {
    grid.setValues(lines);
    if (lines.size() > 0) {
      currentRow = 0;
    }
    doRefresh();
    grid.focusOnFirstField();
  }

  private void doAddAccount(final List<BAccount> accounts) {
    if (accounts == null || accounts.isEmpty()) {
      return;
    }

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(ReceiptMessages.M.loading2(ReceiptMessages.M.overdueTerm()));
    com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService.Locator.getService()
        .getOverdueTerms(DirectionType.receipt.getDirectionValue(), accounts,
            new RBAsyncCallback2<Map<BAccount, List<BPaymentOverdueTerm>>>() {
              @Override
              public void onException(Throwable caught) {
                RLoadingDialog.hide();
                String msg = ReceiptMessages.M.actionFailed(ReceiptMessages.M.load(),
                    ReceiptMessages.M.overdueTerm());
                RMsgBox.showError(msg, caught);
              }

              @Override
              public void onSuccess(Map<BAccount, List<BPaymentOverdueTerm>> result) {
                RLoadingDialog.hide();
                addAccount(result);
                // 重置收款单单头产生的预存款金额
                bill.aggregate(ep.getScale(), ep.getRoundingMode());
                accountTotalNumber.setText(ReceiptMessages.M.resultTotal(bill.getAccountLines()
                    .size()));
                RActionEvent.fire(AccountLineEditGrid.this,
                    ActionName.ACTION_BILLACCOUNTLINE_CHANGE);
              }
            });
  }

  private void addAccount(Map<BAccount, List<BPaymentOverdueTerm>> overdueTermMap) {
    for (BAccount account : overdueTermMap.keySet()) {
      BPaymentAccountLine accLine = new BPaymentAccountLine();
      accLine.setAcc1(account.getAcc1());
      accLine.setAcc2(account.getAcc2());
      accLine.setOriginTotal(account.getOriginTotal().clone());
      accLine.setUnpayedTotal(account.getTotal().clone());
      accLine.setTotal(BTotal.zero());
      for (int i = 0; i < bill.getAccountLines().size(); i++) {
        if (bill.getAccountLines().get(i).getAcc1().getSourceBill() == null) {
          bill.getAccountLines().remove(i);
        }
      }
      bill.getAccountLines().add(accLine);
      // 计算该条账款的滞纳金金额和税额以及添加滞纳金明细行
      List<BPaymentOverdueTerm> overdueTerms = overdueTermMap.get(account);
      if (overdueTerms != null && !overdueTerms.isEmpty()) {
        accLine.setOverdueTerms(overdueTerms);

        if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
          // 按科目直接算滞纳金
          BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(),
              ep.getOverdueDefault());
        }
      }

      // 收款单单头增加相应的应收金额
      bill.setUnpayedTotal(bill.getUnpayedTotal().add(accLine.getUnpayedTotal()));
      bill.setTotal(bill.getTotal().add(accLine.getTotal()));
    }
    if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      bill.aggregateOverdue(ep.getScale(), ep.getRoundingMode());
    } else {
      // 添加账款明细重新分摊计算(包含滞纳金)
      RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_ACCOUNTLINE_CHANGE);
    }

    if (!overdueTermMap.entrySet().isEmpty()) {
      refreshData();
    }

  }

  /** 可以提供给外面调用 */
  public void setDefaultReceiptEqualsUnpayed() {
    // 判断是否设置收款金额等于应收金额
    if (ep.getConfig().isReceiptEqualsUnpayed() == false) {
      return;
    }
    if (CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      if (bill.getCashs().size() == 1) { // 按总额收款只有一种付款方式时设置收款金额等于应收金额
        BigDecimal total = calcUnpayedTotal();
        bill.getCashs().get(0).setTotal(total);
        for (BPaymentAccountLine accLine : bill.getAccountLines()) {
          accLine.setDefrayalTotal(accLine.getUnpayedTotal().getTotal());
          accLine.getCashs().get(0).setTotal(accLine.getUnpayedTotal().getTotal());
        }
        grid.refresh();
        RActionEvent.fire(AccountLineEditGrid.this, "refreshCash");
      }
    } else if (CPaymentDefrayalType.lineSingle.equals(bill.getDefrayalType())) {
      for (BPaymentAccountLine accLine : bill.getAccountLines()) {
        accLine.setDefrayalTotal(accLine.getUnpayedTotal().getTotal());
        accLine.getTotal().setTotal(accLine.getUnpayedTotal().getTotal());
        accLine.getCashs().get(0).setTotal(accLine.getUnpayedTotal().getTotal());
      }

      // 重算滞纳金
      refreshOverdue();
      RActionEvent.fire(AccountLineEditGrid.this,
          ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE);

      grid.refresh();
      RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_LINECASHDEFRAYALLINE_CHANGE);
    }
  }

  private BigDecimal calcUnpayedTotal() {
    BigDecimal total = BigDecimal.ZERO;
    for (BPaymentAccountLine line : bill.getAccountLines()) {
      total = total.add(line.getUnpayedTotal().getTotal());
    }

    for (BPaymentCollectionLine line : bill.getCollectionLines()) {
      total = total.add(line.getUnpayedTotal().getTotal());
    }

    return total;
  }

  private void doStatementAction() {
    if (statementDialog == null) {
      statementDialog = new AccByStatementBrowserDialog(false, null, DirectionType.receipt,
          new AccByStatementBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountStatement> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountStatement statement : results)
                accounts.addAll(statement.getAccounts());
              doAddAccount(accounts);
              refreshData();
              setDefaultReceiptEqualsUnpayed();
            }
          }, ep.getCaptionMap(), ep.getCounterpartTypeMap());
    }
    if (isEdit) {
      statementDialog.setPaymentUuid(bill.getUuid());
    }
    statementDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    statementDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill.getCounterpart()
        .getUuid());
    statementDialog.center(bill.getHasAddedAccIds());
  }

  private void doInvoiceRegAction() {
    if (invoiceRegDialog == null) {
      invoiceRegDialog = new AccByInvoiceRegBrowserDialog(false, null, DirectionType.receipt,
          new AccByInvoiceRegBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountInvoice> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountInvoice invoice : results)
                accounts.addAll(invoice.getAccounts());

              doAddAccount(accounts);
              refreshData();
              setDefaultReceiptEqualsUnpayed();
            }
          }, ep.getCaptionMap(), ep.getCounterpartTypeMap());
    }
    if (isEdit) {
      invoiceRegDialog.setPaymentUuid(bill.getUuid());
    }
    invoiceRegDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    invoiceRegDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill
        .getCounterpart().getUuid());
    invoiceRegDialog.center(bill.getHasAddedAccIds());
  }

  private void doPaymentNoticeAction() {
    if (paymentNoticeDialog == null)
      paymentNoticeDialog = new AccByPaymentNoticeBrowserDialog(false, null, DirectionType.receipt,
          new AccByPaymentNoticeBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountNotice> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountNotice notice : results)
                accounts.addAll(notice.getAccounts());

              doAddAccount(accounts);
              refreshData();
              setDefaultReceiptEqualsUnpayed();
            }
          }, ep.getCaptionMap(), ep.getCounterpartTypeMap());
    if (isEdit) {
      paymentNoticeDialog.setPaymentUuid(bill.getUuid());
    }
    paymentNoticeDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    paymentNoticeDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill
        .getCounterpart().getUuid());
    paymentNoticeDialog.center(bill.getHasAddedAccIds());
  }

  private void doSourceBillAction() {
    if (sourceBillDialog == null)
      sourceBillDialog = new AccByBillBrowserDialog(false, null, DirectionType.receipt,
          new AccByBillBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountSourceBill> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountSourceBill sourceBill : results)
                accounts.addAll(sourceBill.getAccounts());

              doAddAccount(accounts);
              refreshData();
              setDefaultReceiptEqualsUnpayed();
            }
          }, ep.getCaptionMap(), ep.getCounterpartTypeMap());
    if (isEdit) {
      sourceBillDialog.setPaymentUuid(bill.getUuid());
    }
    sourceBillDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    sourceBillDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill
        .getCounterpart().getUuid());
    sourceBillDialog.setBillTypeMap(getEP().getBillTypeMap());
    sourceBillDialog.center(bill.getHasAddedAccIds());
  }

  private void doAccountAction() {
    if (accDialog == null) {
      accDialog = new AccBrowserDialog(false, null, new AccBrowserDialog.Callback() {

        @Override
        public void execute(List<BAccount> results) {
          doAddAccount(results);
          refreshData();
          setDefaultReceiptEqualsUnpayed();
        }
      }, DirectionType.receipt, ep.getCaptionMap(), ep.getCounterpartTypeMap());
    }
    if (isEdit) {
      accDialog.setPaymentUuid(bill.getUuid());
    }
    accDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    accDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill.getCounterpart()
        .getUuid());
    accDialog.setBillTypeMap(getEP().getBillTypeMap());
    accDialog.center(bill.getHasAddedAccIds());
  }

  public void doDelete() {
    List list = grid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(ReceiptMessages.M.seleteDataToAction(ReceiptMessages.M.delete(),
          ReceiptMessages.M.line()));
      return;
    }

    RMsgBox.showConfirm(
        ReceiptMessages.M.actionComfirm(ReceiptMessages.M.delete(),
            ReceiptMessages.M.selectedRows()), true, new RMsgBox.ConfirmCallback() {
          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed) {
              doDeleteRows();
              RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_ACCOUNTLINE_AGGREGATE);
              // 删除账款明细重新分摊
              doApportion(false);
            }
          }
        });
  }

  private void doDeleteRows() {
    List<Integer> selections = grid.getSelections();
    boolean deleteCurLine = false;
    for (int i = selections.size() - 1; i >= 0; i--) {
      int row = selections.get(i).intValue();
      BPaymentAccountLine accountLine = lines.get(row);
      removeOverdueLines(accountLine);
      doRefreshPaymentBeforDelete(accountLine);
      lines.remove(row);

      if (row == currentRow)
        deleteCurLine = true;
    }

    if (deleteCurLine)
      currentRow = lines.size() - 1;
    else {
      for (Integer row : selections) {
        if (row.intValue() < currentRow)
          currentRow--;
      }
    }
    // refreshData();
    RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUELINES);
  }

  /**
   * 移除或更新收款单的滞纳金明细行
   * 
   * @param accountLine
   *          移除的账款明细行
   */
  private void removeOverdueLines(BPaymentAccountLine accountLine) {
    for (BPaymentAccOverdue accOverdueLine : accountLine.getOverdues()) {
      for (int i = 0; i < bill.getOverdueLines().size(); i++) {
        BPaymentOverdueLine line = bill.getOverdueLines().get(i);
        if (line.getSubject().equals(accOverdueLine.getSubject())
            && line.getContract().getCode().equals(accOverdueLine.getContract().getCode())
            && line.getTaxRate().equals(accOverdueLine.getTaxRate())) {
          line.setOverdueTotal(line.getOverdueTotal().subtract(accOverdueLine.getTotal()));
          line.setUnpayedTotal(line.getOverdueTotal().clone());
          line.setTotal(line.getUnpayedTotal().clone());

          if (line.getOverdueTotal().equals(BTotal.zero())) {
            bill.getOverdueLines().remove(i);
          }

          break;
        }
      }
    }
    bill.aggregateOverdue(ep.getScale(), ep.getRoundingMode());
  }

  /**
   * 扣除收款单总的应收金额。如果是按科目收款，会扣除收款单总的实收金额，如果删除的是滞纳金明细，还会扣除收款单总的滞纳金。
   * 
   * @param line
   */
  private void doRefreshPaymentBeforDelete(BPaymentLine line) {
    // 扣除应收金额
    bill.setUnpayedTotal(bill.getUnpayedTotal().subtract(line.getUnpayedTotal()));
    bill.setTotal(bill.getTotal().subtract(line.getTotal()));
    if (bill.getTotal().getTotal().compareTo(BigDecimal.ZERO) < 0) {
      bill.setDefrayalTotal(BigDecimal.ZERO);
    } else if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
      // 扣除实收金额
      for (BPaymentLineDefrayal lineDefrayal : line.getCashs()) {
        if (lineDefrayal.getTotal() != null
            && BigDecimal.ZERO.compareTo(lineDefrayal.getTotal()) != 0)
          bill.setDefrayalTotal(bill.getDefrayalTotal().subtract(lineDefrayal.getTotal()));
      }
      for (BPaymentLineDefrayal lineDefrayal : line.getDeposits()) {
        if (lineDefrayal.getTotal() != null
            && BigDecimal.ZERO.compareTo(lineDefrayal.getTotal()) < 0)
          bill.setDefrayalTotal(bill.getDefrayalTotal().subtract(lineDefrayal.getTotal()));
      }
      for (BPaymentLineDefrayal lineDefrayal : line.getDeductions()) {
        if (lineDefrayal.getTotal() != null
            && BigDecimal.ZERO.compareTo(lineDefrayal.getTotal()) < 0)
          bill.setDefrayalTotal(bill.getDefrayalTotal().subtract(lineDefrayal.getTotal()));
      }
    }
  }

  private class GridRefreshHandler implements RefreshHandler<RGridCellInfo> {

    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      if (lines.isEmpty()) {
        return;
      }
      RGridCellInfo cell = event.getTarget();
      BPaymentAccountLine rowData = lines.get(cell.getRow());
      PaymentGridCellStyleUtil.refreshCellStye(cell, rowData.getAcc1().getDirection(),
          DirectionType.receipt);

      resetBankOptions();
    }

  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(addButton) || event.getSource().equals(statementAction)) {
        GWTUtil.blurActiveElement();
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
          @Override
          public void execute() {
            if (confirm())
              doStatementAction();
          }
        });
      } else if (event.getSource().equals(invoiceRegAction)) {
        if (confirm())
          doInvoiceRegAction();
      } else if (event.getSource().equals(paymentNoticeAction)) {
        if (confirm())
          doPaymentNoticeAction();
      } else if (event.getSource().equals(sourceBillAction)) {
        if (confirm())
          doSourceBillAction();
      } else if (event.getSource().equals(accountAction)) {
        if (confirm())
          doAccountAction();
      } else if (event.getSource().equals(deleteAction)) {
        doDelete();
      }
    }
  }
}
