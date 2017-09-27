/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
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
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountInvoice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridCellWidgetFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridColumnDef;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridDataProvider;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RHyperlinkField;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
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
 * 账款明细编辑表格
 * 
 * @author subinzhu
 * 
 */
public class AccountLineEditGrid extends RCaptionBox implements HasRActionHandlers, RActionHandler {

  private BPayment bill;
  private EPPayment ep = EPPayment.getInstance();

  private Label totalNumberField;
  private RToolbarSplitButton addButton;
  private RAction statementAction;
  private RAction invoiceRegAction;
  private RAction paymentNoticeAction;
  private RAction sourceBillAction;
  private RAction accountAction;
  private RAction deleteAction;

  private EditGrid<BPaymentAccountLine> editGrid;
  private EditGridColumnDef lineNumberCol;
  private EditGridColumnDef subjectCol;
  private EditGridColumnDef sourceBillNumberCol;
  private EditGridColumnDef sourceBillTypeCol;
  private EditGridColumnDef dateRangeCol;
  private EditGridColumnDef taxRateCol;
  private EditGridColumnDef originTotalCol;
  private EditGridColumnDef originTaxCol;
  private EditGridColumnDef unpayedTotalCol;
  private EditGridColumnDef unpayedTaxCol;
  private EditGridColumnDef totalCol;
  private EditGridColumnDef taxCol;
  private EditGridColumnDef leftTotalCol;
  private EditGridColumnDef overdueTotalCol;
  private EditGridColumnDef remarkCol;

  private AccByStatementBrowserDialog statementDialog;
  private AccByInvoiceRegBrowserDialog invoiceRegDialog;
  private AccByPaymentNoticeBrowserDialog paymentNoticeDialog;
  private AccByBillBrowserDialog sourceBillDialog;
  private AccBrowserDialog accDialog;

  private Handler_click clickHandler = new Handler_click();
  private List<Message> messages = new ArrayList<Message>();

  private boolean edit = false;

  public void setEdit(boolean edit) {
    this.edit = edit;
  }

  public AccountLineEditGrid() {
    super();
    setCaption(PPaymentLineDef.TABLE_CAPTION);
    setWidth("100%");
    setEditing(true);

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);
    panel.setStyleName(RCaptionBox.STYLENAME_STANDARD);

    Widget w = drawGrid();
    panel.add(w);

    totalNumberField = new HTML(PaymentMessages.M.resultTotal(0));
    getCaptionBar().addButton(RSimplePanel.decorateMargin(totalNumberField, 0, 0, 0, 10));

    RPopupMenu addMenu = new RPopupMenu();
    statementAction = new RAction(PaymentMessages.M.statementImport(), clickHandler);
    statementAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(statementAction));

    invoiceRegAction = new RAction(PaymentMessages.M.invoiceRegImport(), clickHandler);
    invoiceRegAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(invoiceRegAction));

    paymentNoticeAction = new RAction(PaymentMessages.M.paymentNoticeImport(), clickHandler);
    paymentNoticeAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(paymentNoticeAction));

    sourceBillAction = new RAction(PaymentMessages.M.sourceBillImport(), clickHandler);
    sourceBillAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(sourceBillAction));

    accountAction = new RAction(PaymentMessages.M.accountImport(), clickHandler);
    accountAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(accountAction));

    addButton = new RToolbarSplitButton(RActionFacade.APPEND);
    addButton.setMenu(addMenu);
    addButton.setShowText(false);
    addButton.addClickHandler(clickHandler);
    getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

    setContent(panel);
  }

  private Widget drawGrid() {
    editGrid = new EditGrid<BPaymentAccountLine>();
    editGrid.setWidth("100%");
    editGrid.setDisplayWidgetStyle(true);
    editGrid.setShowRowSelector(true);
    editGrid.addColumnDefs(createColumnDef());
    editGrid.setShowAddRemoveColumn(false);
    editGrid.setDefaultDataRowCount(1);
    editGrid.setProvider(new DataProvider());
    editGrid.setCellWidgetFactory(new CellWidgetFactory());
    editGrid.setAllowHorizontalScrollBar(true);

    return editGrid;
  }

  private List<EditGridColumnDef> createColumnDef() {
    lineNumberCol = new EditGridColumnDef(PPaymentAccountLineDef.lineNumber, "40px");
    subjectCol = new EditGridColumnDef(PPaymentAccountLineDef.acc1_subject, true, "130px");
    sourceBillNumberCol = new EditGridColumnDef(PPaymentAccountLineDef.acc1_sourceBill_billNumber,
        true, BillConfig.COLUMNWIDTH_BILLNUMBER);
    sourceBillTypeCol = new EditGridColumnDef(PPaymentAccountLineDef.acc1_sourceBill_billType,
        "100px");
    dateRangeCol = new EditGridColumnDef(CommonMessages.M.dateRange(), "dateRange", false, "180px");
    taxRateCol = new EditGridColumnDef(PPaymentAccountLineDef.acc1_taxRate_rate, false, "90px");
    originTotalCol = new EditGridColumnDef(PaymentMessages.M.originTotal_total(),
        "originTotal.total", false, "80px", HasHorizontalAlignment.ALIGN_RIGHT);
    originTaxCol = new EditGridColumnDef(PaymentMessages.M.originTotal_tax(), "originTotal.tax",
        false, "80px", HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTotalCol = new EditGridColumnDef(PaymentMessages.M.unpayedTotal_total(),
        "unpayedTotal.total", false, "90px", HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTaxCol = new EditGridColumnDef(PaymentMessages.M.unpayedTotal_tax(), "unpayedTotal.tax",
        false, "90px", HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol = new EditGridColumnDef(PPaymentAccountLineDef.total_total, true, "90px",
        HasHorizontalAlignment.ALIGN_RIGHT);
    taxCol = new EditGridColumnDef(PPaymentAccountLineDef.total_tax, false, "90px",
        HasHorizontalAlignment.ALIGN_RIGHT);
    leftTotalCol = new EditGridColumnDef(PaymentMessages.M.leftTotal_total(), "leftTotal.total",
        false, "80px", HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTotalCol = new EditGridColumnDef(PPaymentAccountLineDef.overdueTotal_total, false,
        "70px", HasHorizontalAlignment.ALIGN_RIGHT);
    remarkCol = new EditGridColumnDef(PPaymentAccountLineDef.remark, true, "90px");

    return Arrays.asList(lineNumberCol, subjectCol, sourceBillNumberCol, sourceBillTypeCol,
        dateRangeCol, taxRateCol, originTotalCol, originTaxCol, unpayedTotalCol, unpayedTaxCol,
        totalCol, taxCol, leftTotalCol, overdueTotalCol, remarkCol);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public void setValue(BPayment bill) {
    this.bill = bill;

    boolean showTax = ep.getConfig().isShowTax();
    taxRateCol.setVisible(showTax);
    originTaxCol.setVisible(showTax);
    unpayedTaxCol.setVisible(showTax);
    taxCol.setVisible(showTax);

    editGrid.rebuild();
    shareThenRefresh();
    if (bill != null && bill.getAccountLines().isEmpty() == false) {
      editGrid.setCurrentRow(0);
    }
  }

  public void refresh() {
    editGrid.refresh();
  }

  public void refreshButtons() {
    if (ep.isBpmMode() == false) {
      statementAction.setEnabled(ep.isPermitted(PaymentPermDef.ADDFROMSTATEMENT));
      invoiceRegAction.setEnabled(ep.isPermitted(PaymentPermDef.ADDFROMINVOICE));
      paymentNoticeAction.setEnabled(ep.isPermitted(PaymentPermDef.ADDFROMPAYNOTICE));
      sourceBillAction.setEnabled(ep.isPermitted(PaymentPermDef.ADDFROMSRCBILL));
      accountAction.setEnabled(ep.isPermitted(PaymentPermDef.ADDFROMACCOUNT));
    } else {
      statementAction.setEnabled(true);
      invoiceRegAction.setEnabled(true);
      paymentNoticeAction.setEnabled(true);
      sourceBillAction.setEnabled(true);
      accountAction.setEnabled(true);
    }
  }

  public void clearQueryConditions() {
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
  }

  @Override
  public boolean validate() {
    clearValidResults();
    boolean valid = super.validate();

    for (int i = 0; i < bill.getAccountLines().size(); i++) {
      BPaymentAccountLine line = bill.getAccountLines().get(i);
      for (int col = 0; col < editGrid.getColumnCount(); col++) {
        if (editGrid.getWidget(i + 1, col) instanceof RNumberBox) {
          valid = validateTotal((RNumberBox) editGrid.getWidget(i + 1, col), line);
        }
      }
    }

    return valid;
  }

  @Override
  public List getInvalidMessages() {
    List<Message> result = super.getInvalidMessages();
    result.addAll(messages);
    result.addAll(editGrid.getInvalidMessages());
    return result;
  }

  private class DataProvider implements EditGridDataProvider<BPaymentAccountLine> {
    @Override
    public List<BPaymentAccountLine> getData() {
      totalNumberField.setText(PaymentMessages.M.resultTotal(bill == null ? 0 : bill
          .getAccountLines().size()));
      return bill == null ? new ArrayList<BPaymentAccountLine>() : bill.getAccountLines();
    }

    @Override
    public Object getData(int row, String colName) {
      if (bill == null || bill.getAccountLines().isEmpty() || row < 0
          || row >= bill.getAccountLines().size())
        return null;

      BPaymentAccountLine rowData = bill.getAccountLines().get(row);
      if (colName.equals(lineNumberCol.getName())) {
        return row + 1;
      } else if (colName.equals(subjectCol.getName())) {
        return rowData.getAcc1().getSubject() == null ? null : rowData.getAcc1().getSubject()
            .toFriendlyStr();
      } else if (colName.equals(sourceBillNumberCol.getName())) {
        if (rowData.getAcc1().getSourceBill() == null) {
          return null;
        }
        return rowData.getAcc1().getSourceBill().getBillNumber();
      } else if (colName.equals(sourceBillTypeCol.getName())) {
        if (rowData.getAcc1().getSourceBill() == null) {
          return null;
        }
        BBillType type = EPPayment.getInstance().getBillTypeMap()
            .get(rowData.getAcc1().getSourceBill().getBillType());
        return type == null ? null : type.getCaption();
      } else if (colName.equals(dateRangeCol.getName())) {
        return buildDateRange(rowData.getAcc1().getBeginTime(), rowData.getAcc1().getEndTime());
      } else if (colName.equals(taxRateCol.getName())) {
        return rowData.getAcc1() == null || rowData.getAcc1().getTaxRate() == null ? null : rowData
            .getAcc1().getTaxRate().toString();
      } else if (colName.equals(originTotalCol.getName())) {
        return rowData.getOriginTotal() == null || rowData.getOriginTotal().getTotal() == null ? null
            : GWTFormat.fmt_money.format(rowData.getOriginTotal().getTotal().doubleValue());
      } else if (colName.equals(originTaxCol.getName())) {
        return rowData.getOriginTotal() == null || rowData.getOriginTotal().getTax() == null ? null
            : GWTFormat.fmt_money.format(rowData.getOriginTotal().getTax().doubleValue());
      } else if (colName.equals(unpayedTotalCol.getName())) {
        return rowData.getUnpayedTotal() == null || rowData.getUnpayedTotal().getTotal() == null ? null
            : GWTFormat.fmt_money.format(rowData.getUnpayedTotal().getTotal().doubleValue());
      } else if (colName.equals(unpayedTaxCol.getName())) {
        return rowData.getUnpayedTotal() == null || rowData.getUnpayedTotal().getTax() == null ? null
            : GWTFormat.fmt_money.format(rowData.getUnpayedTotal().getTax().doubleValue());
      } else if (colName.equals(totalCol.getName())) {
        return rowData.getTotal() == null ? null : rowData.getTotal().getTotal();
      } else if (colName.equals(taxCol.getName())) {
        return rowData.getTotal() == null ? null : rowData.getTotal().getTax();
      } else if (colName.equals(leftTotalCol.getName())) {
        return rowData.getLeftTotal() == null || rowData.getLeftTotal().getTotal() == null ? null
            : GWTFormat.fmt_money.format(rowData.getLeftTotal().getTotal().doubleValue());
      } else if (colName.equals(overdueTotalCol.getName())) {
        return rowData.getOverdueTotal() == null || rowData.getOverdueTotal().getTotal() == null ? null
            : GWTFormat.fmt_money.format(rowData.getOverdueTotal().getTotal().doubleValue());
      } else if (colName.equals(remarkCol.getName())) {
        return rowData.getRemark();
      }
      return null;
    }

    @Override
    public BPaymentAccountLine create() {
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

  private class CellWidgetFactory implements EditGridCellWidgetFactory<BPaymentAccountLine> {
    @Override
    public Widget createEditWidget(FieldDef field, int dataRowIdx, BPaymentAccountLine rowData) {
      if (field.getName().equals(subjectCol.getName())) {
        RHyperlinkField subjcetField = new RHyperlinkField();
        subjcetField.setWidth("130px");
        subjcetField.setOverflowEllipsis(true);
        subjcetField.addClickHandler(new SubjectHandler(rowData.getAcc1().getSubject()));
        return subjcetField;
      } else if (field.getName().equals(sourceBillNumberCol.getName())) {
        DispatchLinkField sourceBillNumberField = new DispatchLinkField();
        sourceBillNumberField.setLinkKey(GRes.R.dispatch_key());
        if (rowData.getAcc1().getSourceBill() != null) {
          sourceBillNumberField.setValue(rowData.getAcc1().getSourceBill().getBillType(), rowData
              .getAcc1().getSourceBill().getBillNumber());
        }
        return sourceBillNumberField;
      } else if (field.getName().equals(remarkCol.getName())) {
        RTextBox remarkField = new RTextBox(PPaymentAccountLineDef.remark);
        remarkField.setSelectAllOnFocus(true);
        remarkField.addChangeHandler(new RemarkChange(dataRowIdx));
        return remarkField;
      } else if (field.getName().equals(totalCol.getName())) {
        RNumberBox<Number> remarkField = new RNumberBox<Number>(PPaymentAccountLineDef.total_total);
        remarkField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
        remarkField.setSelectAllOnFocus(true);
        remarkField.addChangeHandler(new TotalChange(dataRowIdx));
        return remarkField;
      }
      return null;
    }
  }

  private class SubjectHandler implements ClickHandler {

    private BUCN subject;

    public SubjectHandler(BUCN subject) {
      this.subject = subject;
    }

    @Override
    public void onClick(ClickEvent event) {
      if (subject == null || StringUtil.isNullOrBlank(subject.getUuid()))
        return;
      GwtUrl url = SubjectUrlParams.ENTRY_URL;
      url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
      url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subject.getUuid());
      try {
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
      } catch (Exception e) {
        String msg = PaymentMessages.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
        RMsgBox.showError(msg, e);
      }
    }
  }

  private boolean validateTotal(RNumberBox totalField, BPaymentAccountLine line) {
    if (line.getTotal().getTotal() == null) {
      totalField
          .addErrorMessage(PaymentMessages.M.notNull(PPaymentAccountLineDef.constants.total()));
      return false;
    } else if (line.getUnpayedTotal().getTotal() != null
        && line.getUnpayedTotal().getTotal().multiply(totalField.getValueAsBigDecimal())
            .compareTo(BigDecimal.ZERO) < 0) {
      totalField.addErrorMessage(PaymentMessages.M.signSymbolMustBeSameAs(PaymentMessages.M
          .unpayedTotal_total()));
      return false;
    } else if (line.getUnpayedTotal().getTotal() != null
        && totalField.getValueAsBigDecimal().abs()
            .compareTo(line.getUnpayedTotal().getTotal().abs()) > 0) {
      totalField.addErrorMessage(PaymentMessages.M.absCannotMoreThan(
          PPaymentAccountLineDef.constants.total_total(), PaymentMessages.M.unpayedTotal_total()));
      return false;
    }
    return true;
  }

  private class RemarkChange implements ChangeHandler {

    private int row;

    public RemarkChange(int row) {
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentAccountLine line = bill.getAccountLines().get(row);
      if (line == null)
        return;
      RTextBox box = (RTextBox) event.getSource();
      line.setRemark(box.getValue());
    }

  }

  private class TotalChange implements ChangeHandler {

    private int row;

    public TotalChange(int row) {
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentAccountLine line = bill.getAccountLines().get(row);
      if (line == null) {
        return;
      }
      RNumberBox<Number> box = (RNumberBox<Number>) event.getSource();
      if (line.getTotal() == null) {
        line.setTotal(BTotal.zero());
      }
      line.getTotal().setTotal(box.getValueAsBigDecimal());
      editGrid.refresh(row);
      RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_ACCOUNTLINE_TOTALCHANGE);
    }

  }

  private class Handler_click implements ClickHandler {
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

  private boolean confirm() {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())) {
      RMsgBox.showError(PaymentMessages.M.pleaseFillInFirst(ep.getFieldCaption(
          GRes.FIELDNAME_BUSINESS, GRes.R.business())));
      return false;
    } else if (bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      RMsgBox.showError(PaymentMessages.M.pleaseFillInFirst(ep.getFieldCaption(
          GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart())));
      return false;
    }
    return true;
  }

  private void doStatementAction() {
    if (statementDialog == null) {
      statementDialog = new AccByStatementBrowserDialog(false, null, DirectionType.payment,
          new AccByStatementBrowserDialog.Callback() {
            @Override
            public void execute(List<BAccountStatement> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountStatement statement : results)
                accounts.addAll(statement.getAccounts());

              doAddAccount(accounts);
            }
          }, ep.getCaptionMap(), ep.getCounterpartTypeMap());
    }

    if (edit) {
      statementDialog.setPaymentUuid(bill.getUuid());
    }

    statementDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    statementDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill.getCounterpart()
        .getUuid());
    statementDialog.center(bill.getHasAddedAccIds());
  }

  private void doInvoiceRegAction() {
    if (invoiceRegDialog == null) {
      invoiceRegDialog = new AccByInvoiceRegBrowserDialog(false, null, DirectionType.payment,
          new AccByInvoiceRegBrowserDialog.Callback() {
            @Override
            public void execute(List<BAccountInvoice> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountInvoice invoice : results)
                accounts.addAll(invoice.getAccounts());

              doAddAccount(accounts);
            }
          }, ep.getCaptionMap(), ep.getCounterpartTypeMap());
    }

    if (edit) {
      invoiceRegDialog.setPaymentUuid(bill.getUuid());
    }
    invoiceRegDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    invoiceRegDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill
        .getCounterpart().getUuid());
    invoiceRegDialog.center(bill.getHasAddedAccIds());
  }

  private void doPaymentNoticeAction() {
    if (paymentNoticeDialog == null)
      paymentNoticeDialog = new AccByPaymentNoticeBrowserDialog(false, null, DirectionType.payment,
          new AccByPaymentNoticeBrowserDialog.Callback() {
            @Override
            public void execute(List<BAccountNotice> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountNotice notice : results)
                accounts.addAll(notice.getAccounts());

              doAddAccount(accounts);
            }
          }, ep.getCaptionMap(), ep.getCounterpartTypeMap());

    if (edit) {
      paymentNoticeDialog.setPaymentUuid(bill.getUuid());
    }

    paymentNoticeDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    paymentNoticeDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill
        .getCounterpart().getUuid());
    paymentNoticeDialog.center(bill.getHasAddedAccIds());
  }

  private void doSourceBillAction() {
    if (sourceBillDialog == null)
      sourceBillDialog = new AccByBillBrowserDialog(false, null, DirectionType.payment,
          new AccByBillBrowserDialog.Callback() {
            @Override
            public void execute(List<BAccountSourceBill> results) {
              List<BAccount> accounts = new ArrayList<BAccount>();
              for (BAccountSourceBill sourceBill : results)
                accounts.addAll(sourceBill.getAccounts());

              doAddAccount(accounts);
            }
          }, ep.getCaptionMap(), ep.getCounterpartTypeMap());

    if (edit) {
      sourceBillDialog.setPaymentUuid(bill.getUuid());
    }
    sourceBillDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    sourceBillDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill
        .getCounterpart().getUuid());
    sourceBillDialog.setBillTypeMap(ep.getBillTypeMap());
    sourceBillDialog.center(bill.getHasAddedAccIds());
  }

  private void doAccountAction() {
    if (accDialog == null) {
      accDialog = new AccBrowserDialog(false, null, new AccBrowserDialog.Callback() {
        @Override
        public void execute(List<BAccount> results) {
          doAddAccount(results);
        }
      }, DirectionType.payment, ep.getCaptionMap(), ep.getCounterpartTypeMap());
    }

    if (edit) {
      accDialog.setPaymentUuid(bill.getUuid());
    }

    accDialog.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    accDialog.setCounterpartUuid(bill.getCounterpart() == null ? null : bill.getCounterpart()
        .getUuid());
    accDialog.setBillTypeMap(ep.getBillTypeMap());
    accDialog.center(bill.getHasAddedAccIds());
  }

  private void doAddAccount(final List<BAccount> accounts) {
    if (accounts == null || accounts.isEmpty())
      return;

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(PaymentMessages.M.loading2(PaymentMessages.M.overdueTerm()));
    com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService.Locator.getService()
        .getOverdueTerms(DirectionType.payment.getDirectionValue(), accounts,
            new RBAsyncCallback2<Map<BAccount, List<BPaymentOverdueTerm>>>() {
              @Override
              public void onException(Throwable caught) {
                RLoadingDialog.hide();
                String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.load(),
                    PaymentMessages.M.overdueTerm());
                RMsgBox.showError(msg, caught);
              }

              @Override
              public void onSuccess(Map<BAccount, List<BPaymentOverdueTerm>> result) {
                RLoadingDialog.hide();
                addAccount(result);
                RActionEvent
                    .fire(AccountLineEditGrid.this, ActionName.ACTION_ACCOUNTLINE_AGGREGATE);
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
      accLine.setTotal(account.getTotal().clone());

      bill.getAccountLines().add(accLine);
      // 计算该条账款的滞纳金金额和税额以及添加滞纳金明细行
      List<BPaymentOverdueTerm> overdueTerms = overdueTermMap.get(account);
      if (overdueTerms != null && !overdueTerms.isEmpty()) {
        accLine.setOverdueTerms(overdueTerms);
        // 计算账款明细滞纳金
        BOverdueCalcHelper.calculate(bill, ep.getScale(), ep.getRoundingMode(), null);
      }
      // 收款单单头增加相应的应收金额
      bill.setUnpayedTotal(bill.getUnpayedTotal().add(accLine.getUnpayedTotal()));
      bill.setTotal(bill.getTotal().add(accLine.getTotal()));
    }
    bill.aggregateOverdue(ep.getScale(), ep.getRoundingMode());
    if (!overdueTermMap.entrySet().isEmpty()) {
      RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUE);
    }

    shareThenRefresh();
  }

  private void doDelete() {
    List<Integer> list = editGrid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(PaymentMessages.M.seleteDataToAction(PaymentMessages.M.delete(),
          PaymentMessages.M.line()));
      return;
    }

    RMsgBox.showConfirm(
        PaymentMessages.M.actionComfirm(PaymentMessages.M.delete(),
            PaymentMessages.M.selectedRows()), true, new RMsgBox.ConfirmCallback() {
          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed) {
              doDeleteRows();
              shareThenRefresh();
            }
          }
        });
  }

  private void doDeleteRows() {
    List<Integer> selections = editGrid.getSelections();
    for (int i = selections.size() - 1; i >= 0; i--) {
      int row = selections.get(i);
      BPaymentAccountLine accountLine = bill.getAccountLines().get(row);
      removeOverdueLines(accountLine);
      doRefreshPaymentBeforDelete(accountLine);
      bill.getAccountLines().remove(row);
    }

    RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_ACCOUNTLINE_AGGREGATE);
    RActionEvent.fire(AccountLineEditGrid.this, ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUE);
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

  @Override
  public void onAction(RActionEvent event) {
    if (ObjectUtil.equals(event.getActionName(), ActionName.ACTION_CASHDEFRAYAL_CHANGE)) {
      shareThenRefresh();
    }
  }

  private void shareThenRefresh() {
    shareTotal(bill);
    editGrid.refresh();
  }

  /** 分摊实收 */
  private void shareTotal(BPayment payment) {
    RActionEvent.fire(AccountLineEditGrid.this,
        AccountLineEditGrid.ActionName.ACTION_ACCOUNTLINE_TOTALCHANGE);
    /*
     * payment.aggregate(ep.getScale(), ep.getRoundingMode()); if
     * (payment.getDefrayalTotal().compareTo(BigDecimal.ZERO) < 0) { return; }
     * 
     * BigDecimal leftTotal = payment.getDefrayalTotal(); // 先分摊负的账款 for
     * (BPaymentAccountLine line : bill.getAccountLines()) {
     * line.setTotal(BTotal.zero()); if
     * (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) > 0) {
     * continue; } if (line.getUnpayedTotal().getTotal().compareTo(leftTotal) <=
     * 0) { line.getTotal().setTotal(line.getUnpayedTotal().getTotal());
     * leftTotal = leftTotal.subtract(line.getUnpayedTotal().getTotal()); } else
     * { line.getTotal().setTotal(leftTotal); leftTotal = BigDecimal.ZERO; }
     * 
     * BigDecimal tax = BTaxCalculator.tax(line.getTotal().getTotal(),
     * line.getAcc1().getTaxRate()); line.getTotal().setTax(tax);
     * 
     * }
     * 
     * // 分摊正的账款 for (BPaymentAccountLine line : bill.getAccountLines()) { if
     * (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) < 0) {
     * continue; } if (line.getUnpayedTotal().getTotal().compareTo(leftTotal) <=
     * 0) { line.getTotal().setTotal(line.getUnpayedTotal().getTotal());
     * leftTotal = leftTotal.subtract(line.getUnpayedTotal().getTotal()); } else
     * { line.getTotal().setTotal(leftTotal); leftTotal = BigDecimal.ZERO; }
     * 
     * BigDecimal tax = BTaxCalculator.tax(line.getTotal().getTotal(),
     * line.getAcc1().getTaxRate()); line.getTotal().setTax(tax); }
     */
  }

  public static class ActionName {
    /** 添加 */
    public static final String ACTION_ACCOUNTLINE_ADD = "accountLine_add";
    /** 删除 */
    public static final String ACTION_ACCOUNTLINE_DELETE = "accountLine_delete";
    /** 刷新滞纳金表格 */
    public static final String ACTION_ACCOUNTLINE_REFRESHOVERDUE = "accountLine_refreshOverdue";
    /** 合计 */
    public static final String ACTION_ACCOUNTLINE_AGGREGATE = "accountLine_aggregate";
    /** 修改金额 */
    public static final String ACTION_ACCOUNTLINE_TOTALCHANGE = "accountLine_totalChange";
    public static final String ACTION_CASHDEFRAYAL_CHANGE = "cashDefrayal_change";
  }

}
