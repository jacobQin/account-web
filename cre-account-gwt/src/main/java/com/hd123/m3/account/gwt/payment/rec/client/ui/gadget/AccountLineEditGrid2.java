/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
import com.hd123.m3.account.gwt.payment.commons.client.PaymentGridCellStyleUtil;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BOverdueCalcHelper;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccOverdue;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueTerm;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByBillBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByInvoiceRegBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByPaymentNoticeBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.AccByStatementBrowserDialog;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineLocator;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountInvoice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.SortDef;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.event.SortEvent;
import com.hd123.rumba.gwt.widget2.client.event.SortHandler;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
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
 * 账款明细编辑表格（按 科目多收款）
 * 
 * @author subinzhu
 * 
 */
public class AccountLineEditGrid2 extends RCaptionBox implements RActionHandler, HasRActionHandlers {

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

  private RGrid grid;
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
  private RGridColumnDef leftTotalCol;
  private RGridColumnDef defrayalTotalCol;
  private RGridColumnDef overdueTotalCol;
  private RGridColumnDef remarkCol;
  private boolean isEdit = false;

  // 事件处理
  private ActionHandler actionHandler = new ActionHandler();

  private LineAccountLineEditGadget lineAccountLineEditGadget;

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
    lineAccountLineEditGadget.setBill(bill);
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

  public void setIsEdit(boolean isEdit) {
    this.isEdit = isEdit;
  }

  protected EPReceipt getEP() {
    return EPReceipt.getInstance();
  }

  public AccountLineEditGrid2() {
    super();
    drawSelf();
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);
    panel.setStyleName(RCaptionBox.STYLENAME_STANDARD);

    drawGrid();
    panel.add(grid);
    lineAccountLineEditGadget = new LineAccountLineEditGadget();
    this.addActionHandler(lineAccountLineEditGadget);
    panel.add(lineAccountLineEditGadget);

    setContent(panel);

    setEditing(true);
    setCaption(PPaymentLineDef.TABLE_CAPTION);
    setBorder(false);
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);

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
    addActionHandler();
  }

  private void addActionHandler() {
    lineAccountLineEditGadget.addActionHandler(this);
    addActionHandler(lineAccountLineEditGadget);
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setProvider(new GridDataProvider());
    Handler_grid handler = new Handler_grid();
    grid.addHighlightHandler(handler);
    grid.addClickHandler(handler);

    lineNumberCol = new RGridColumnDef(PPaymentAccountLineDef.lineNumber);
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    lineNumberCol.setResizable(true);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_subject);
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("130px"));
    subjectCol.setOverflowEllipsis(true);
    subjectCol.setResizable(true);
    subjectCol.setSortable(true);
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
    dateRangeCol.setWidth("150px");
    dateRangeCol.setResizable(true);
    dateRangeCol.setSortable(true);
    grid.addColumnDef(dateRangeCol);

    taxRateCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_taxRate_rate);
    taxRateCol.setWidth("90px");
    taxRateCol.setResizable(true);
    grid.addColumnDef(taxRateCol);

    originTotalCol = new RGridColumnDef(ReceiptMessages.M.originTotal_total());
    originTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTotalCol.setWidth("80px");
    originTotalCol.setResizable(true);
    grid.addColumnDef(originTotalCol);

    originTaxCol = new RGridColumnDef(ReceiptMessages.M.originTotal_tax());
    originTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTaxCol.setWidth("80px");
    originTaxCol.setResizable(true);
    grid.addColumnDef(originTaxCol);

    unpayedTotalCol = new RGridColumnDef(PPaymentDef.unpayedTotal_total);
    unpayedTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTotalCol.setWidth("90px");
    unpayedTotalCol.setResizable(true);
    grid.addColumnDef(unpayedTotalCol);

    unpayedTaxCol = new RGridColumnDef(PPaymentDef.unpayedTotal_tax);
    unpayedTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTaxCol.setWidth("90px");
    unpayedTaxCol.setResizable(true);
    grid.addColumnDef(unpayedTaxCol);

    leftTotalCol = new RGridColumnDef(ReceiptMessages.M.leftTotal_total());
    leftTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    leftTotalCol.setWidth("80px");
    leftTotalCol.setResizable(true);
    grid.addColumnDef(leftTotalCol);

    defrayalTotalCol = new RGridColumnDef(PPaymentAccountLineDef.defrayalTotal);
    defrayalTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    defrayalTotalCol.setWidth("80px");
    defrayalTotalCol.setResizable(true);
    grid.addColumnDef(defrayalTotalCol);

    overdueTotalCol = new RGridColumnDef(PPaymentAccountLineDef.overdueTotal_total);
    overdueTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTotalCol.setWidth("70px");
    overdueTotalCol.setResizable(true);
    grid.addColumnDef(overdueTotalCol);

    remarkCol = new RGridColumnDef(PPaymentAccountLineDef.remark);
    remarkCol.setResizable(true);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    grid.setAllowHorizontalScrollBar(true);
    grid.addSortHandler(new GridColumnSortHandler());
    grid.setSort(dateRangeCol, OrderDir.asc, true);
    grid.addRefreshHandler(new GridRefreshHandler());
  }

  public void refreshData() {
    lines = bill.getAccountLines();
    if (lines.size() > 0) {
      currentRow = 0;
    }
    grid.setSort(dateRangeCol, OrderDir.asc, true);
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

  private void doRefresh(boolean focusOnFirst) {
    refreshLineNumber();
    grid.refresh();
    // grid.setCurrentRow(currentRow);
    RActionEvent.fire(this, ActionName.ACTION_ACCOUNTLINE_CHANGELINENO,
        Integer.valueOf(currentRow), Boolean.valueOf(focusOnFirst));
    accountTotalNumber.setText(ReceiptMessages.M.resultTotal(bill.getAccountLines().size()));
  }

  private void refreshLineNumber() {
    for (int i = 0; i < lines.size(); i++) {
      BPaymentAccountLine line = lines.get(i);
      line.setLineNumber(i + 1);
    }
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
              doRefresh(true);
              RActionEvent.fire(AccountLineEditGrid2.this, ActionName.ACTION_ACCOUNTLINE_AGGREGATE);
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
    // 重置收款单单头中的产生的预存款金额
    bill.setDepositTotal(bill.getDefrayalTotal().subtract(bill.getTotal().getTotal()));

    if (deleteCurLine)
      currentRow = lines.size() - 1;
    else {
      for (Integer row : selections) {
        if (row.intValue() < currentRow)
          currentRow--;
      }
    }

    RActionEvent.fire(AccountLineEditGrid2.this, ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUELINES);
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
    } else if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
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

  private void doDeleteCurRow() {
    BPaymentAccountLine accountLine = lines.get(currentRow);
    removeOverdueLines(accountLine);
    doRefreshPaymentBeforDelete(accountLine);
    // 重置收款单单头中的产生的预存款金额
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    RActionEvent.fire(AccountLineEditGrid2.this, ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUELINES);

    lines.remove(currentRow);
    currentRow = lines.size() - 1;
    doRefresh(true);
    RActionEvent.fire(AccountLineEditGrid2.this, ActionName.ACTION_ACCOUNTLINE_AGGREGATE);
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
              doRefresh(true);
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

  private class GridDataProvider implements RGridDataProvider {
    @Override
    public int getRowCount() {
      return lines == null ? 0 : lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (lines.size() == 0)
        return null;

      BPaymentAccountLine line = lines.get(row);
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
      else if (col == leftTotalCol.getIndex())
        return line.getLeftTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getLeftTotal().getTotal().doubleValue());
      else if (col == defrayalTotalCol.getIndex()
          && !CPaymentDefrayalType.bill.equals(bill.getDefrayalType()))
        return line.getDefrayalTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getDefrayalTotal().doubleValue());
      else if (col == overdueTotalCol.getIndex())
        return line.getOverdueTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getOverdueTotal().getTotal().doubleValue());
      else if (col == remarkCol.getIndex())
        return line.getRemark();
      else
        return null;
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
      GWTUtil.blurActiveElement();
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
      currentRow = event.getHighlighted().getY();
      RActionEvent.fire(AccountLineEditGrid2.this, ActionName.ACTION_ACCOUNTLINE_CHANGELINENO,
          Integer.valueOf(currentRow), Boolean.FALSE);
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_PREV) {
      if (currentRow > 0) {
        RActionEvent.fire(this, ActionName.ACTION_ACCOUNTLINE_CHANGELINENO,
            Integer.valueOf(--currentRow), Boolean.FALSE);
        grid.setCurrentRow(currentRow);
      }
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_NEXT) {
      if (currentRow < lines.size() - 1) {
        RActionEvent.fire(this, ActionName.ACTION_ACCOUNTLINE_CHANGELINENO,
            Integer.valueOf(++currentRow), Boolean.FALSE);
        grid.setCurrentRow(currentRow);
      }
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE) {
      if (currentRow >= 0 && currentRow < lines.size())
        grid.refreshRow(currentRow);
      RActionEvent.fire(AccountLineEditGrid2.this,
          ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE);
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_DELETE) {
      doDeleteCurRow();
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_CHANGE) {
      if (currentRow >= 0 && currentRow < lines.size())
        grid.refreshRow(currentRow);
    } else if ((event.getActionName() == ActionName.CREATEPAGE_CHANGE_LINE || event.getActionName() == ActionName.EDITPAGE_CHANGE_LINE)) {
      PaymentLineLocator locator = (PaymentLineLocator) event.getParameters().get(0);
      if (PaymentLineLocator.ACCOUNTTAB_ID.intValue() == locator.getTabId().intValue()) {
        currentRow = locator.getLineNumber();
        grid.setCurrentRow(currentRow);
        grid.refreshRow(currentRow);
      }
      RActionEvent.fire(AccountLineEditGrid2.this, event.getActionName(), locator);
    } else if ((event.getActionName() == ActionName.CREATEPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE || event
        .getActionName() == ActionName.EDITPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE)) {
      PaymentLineDefrayalLineLocator locator = (PaymentLineDefrayalLineLocator) event
          .getParameters().get(0);
      if (PaymentLineDefrayalLineLocator.ACCOUNTMULTITAB_ID.intValue() == locator.getTabId()
          .intValue()) {
        currentRow = locator.getOutLineNumber().intValue();
        grid.setCurrentRow(currentRow);
        grid.refreshRow(currentRow);
      }
      RActionEvent.fire(AccountLineEditGrid2.this, event.getActionName(), locator);
    } else if ((event.getSource() instanceof LineAccountLineEditGadget == false) // 必须循环监听
        && event.getActionName() == ActionName.ACTION_GENERALCREATE_REFRESH_BANKS) {
      RActionEvent.fire(AccountLineEditGrid2.this, event.getActionName());
    }
  }

  public void refreshGrid() {
    boolean showTax = ep.getConfig().isShowTax();
    taxRateCol.setVisible(showTax);
    originTaxCol.setVisible(showTax);
    unpayedTaxCol.setVisible(showTax);

    grid.rebuild();
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

  private void doAddAccount(final List<BAccount> accounts) {
    if (accounts == null || accounts.isEmpty())
      return;

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
                RActionEvent.fire(AccountLineEditGrid2.this,
                    ActionName.ACTION_BILLACCOUNTLINE_CHANGE2);
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
      RActionEvent.fire(AccountLineEditGrid2.this, ActionName.ACTION_ACCOUNTLINE_CHANGE);
    }

    if (!overdueTermMap.entrySet().isEmpty()) {
      RActionEvent.fire(AccountLineEditGrid2.this, ActionName.ACTION_BILLACCOUNTLINE2_ADD);
    }
  }

  private void doAccountAction() {
    if (accDialog == null) {
      accDialog = new AccBrowserDialog(false, null, new AccBrowserDialog.Callback() {

        @Override
        public void execute(List<BAccount> results) {
          doAddAccount(results);
          refreshData();
        }
      },DirectionType.receipt, ep.getCaptionMap(), ep.getCounterpartTypeMap());
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
      sourceBillDialog = new AccByBillBrowserDialog(false, null,DirectionType.receipt,
          new AccByBillBrowserDialog.Callback() {

            @Override
            public void execute(List<BAccountSourceBill> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountSourceBill sourceBill : results)
                accounts.addAll(sourceBill.getAccounts());

              doAddAccount(accounts);
              refreshData();
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

  private class GridRefreshHandler implements RefreshHandler<RGridCellInfo> {

    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      if (lines.isEmpty()) {
        return;
      }
      RGridCellInfo cell = event.getTarget();
      BPaymentAccountLine rowData = lines.get(cell.getRow());
      PaymentGridCellStyleUtil.refreshCellStye(cell, rowData.getAcc1().getDirection(),
          DirectionType.receipt);
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
