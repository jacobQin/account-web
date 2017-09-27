/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月4日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementLineDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridCellWidgetFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridColumnDef;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridDataProvider;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * @author zhr
 * 
 */
public class StatementLinePatchEditGridGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {

  public StatementLinePatchEditGridGadget() {
    super();
    setCaption(StatementMessages.M.statementLine());
    getCaptionBar().setShowCollapse(true);
    setEditing(true);
    setContentSpacing(0);
    setWidth("100%");

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    Widget w = drawGrid();
    panel.add(w);

    resultTotalHtml = new HTML(StatementMessages.M.resultTotal(0));
    getCaptionBar().addButton(resultTotalHtml);
    addMenu = new RPopupMenu();
    addAction = new RAction(RActionFacade.APPEND, clickHandler);
    addAction.setCaption(StatementMessages.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));
    insertAction = new RAction(StatementMessages.M.insertLine(), clickHandler);
    insertAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(insertAction));

    addButton = new RToolbarSplitButton(addAction);
    addButton.setMenu(addMenu);
    addButton.setShowText(false);
    getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteAction.setHotKey(null);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

    getCaptionBar().addButton(new RToolbarSeparator());
    setLastPayDateAction = new RAction(StatementMessages.M.setLastPayDate(), clickHandler);
    getCaptionBar().addButton(new RToolbarButton(setLastPayDateAction));

    setContent(panel);
  }

  public EditGrid<BStatementLine> getEditGrid() {
    return editGrid;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == StatementGeneralCreateGadget.ActionName.CLEAR_LINE) {
      values.clear();
      editGrid.refresh();
    }
  }

  public List<Message> getInvalidMessages() {
    List<Message> result = super.getInvalidMessages();
    result.addAll(messages);
    return result;
  }

  public boolean validateLines() {
    messages.clear();

    boolean existsValid = false;
    for (int i = 0; i < values.size(); i++) {
      BStatementLine line = values.get(i);
      if (line.isValid() == false)
        continue;
      existsValid = true;
      if (line.getTotal() == null || line.getTotal().getTotal() == null) {
        messages.add(Message.error(M.isNull(i + 1, M.total()), editGrid.getWidget(i + 1, 4)));
      } else if (BigDecimal.ZERO.compareTo(line.getTotal().getTotal()) == 0) {
        messages.add(Message.error(M.totalPatch(i + 1), editGrid.getWidget(i + 1, 4)));
      }
      if (line.getAcc1().getBeginTime() == null) {
        messages.add(Message.error(M.isNull(i + 1, PStatementLineDef.constants.acc1_beginTime()),
            editGrid.getWidget(i + 1, 8)));
      } else {
        if (line.getAcc1().getEndTime() != null
            && line.getAcc1().getBeginTime().compareTo(line.getAcc1().getEndTime()) > 0) {
          messages.add(Message.error(M.beginTimeMustBeforeEndTime(i + 1),
              editGrid.getWidget(i + 1, 8)));
        }
      }
      if (line.getAcc1().getEndTime() == null) {
        messages.add(Message.error(M.isNull(i + 1, PStatementLineDef.constants.acc1_endTime()),
            editGrid.getWidget(i + 1, 9)));
      } else {
        // do nothing
      }
    }
    if (existsValid == false) {
      messages.add(Message.error(M.emptyLine()));
    }
    return messages.isEmpty();
  }

  public void setValue(List<BStatementLine> values) {
    this.values = values;
    editGrid.rebuild();
    if (values.isEmpty() == false) {
      editGrid.setCurrentRow(0);
    }
  }

  private EPStatement ep = EPStatement.getInstance();

  private OtherFieldDef lineNumber = new OtherFieldDef("lineNumber", "行号", true);
  private OtherFieldDef total = new OtherFieldDef("total", "补录金额", true);
  private OtherFieldDef acc2invoice = new OtherFieldDef("acc2.invoice", "开发票", true);

  private List<BStatementLine> values = new ArrayList<BStatementLine>();
  private List<Message> messages = new ArrayList<Message>();

  private HTML resultTotalHtml;
  private RAction addAction;
  private RAction insertAction;
  private RPopupMenu addMenu;
  private RToolbarSplitButton addButton;
  private RAction deleteAction;
  private RAction setLastPayDateAction;

  private StatementDateEditDialog dateDialog;
  private EditGrid<BStatementLine> editGrid;
  private EditGridColumnDef lineNumberCol;
  private EditGridColumnDef subjectCol;
  private EditGridColumnDef directionCol;
  private EditGridColumnDef totalCol;
  private EditGridColumnDef taxRateCol;
  private EditGridColumnDef taxCol;
  private EditGridColumnDef invoiceCol;
  private EditGridColumnDef beginDateCol;
  private EditGridColumnDef endDateCol;
  private EditGridColumnDef lastPayDateCol;
  private EditGridColumnDef remarkCol;

  private Handler_Click clickHandler = new Handler_Click();

  private Widget drawGrid() {
    editGrid = new EditGrid<BStatementLine>();
    editGrid.setWidth("100%");
    editGrid.setDisplayWidgetStyle(true);
    editGrid.setShowRowSelector(true);
    editGrid.addColumnDefs(createColumnDef());
    editGrid.setShowAddRemoveColumn(false);
    editGrid.setDefaultDataRowCount(1);
    editGrid.setProvider(new DateProvider());
    editGrid.setCellWidgetFactory(new CellWidgetFactory());

    return editGrid;
  }

  private List<EditGridColumnDef> createColumnDef() {
    lineNumberCol = new EditGridColumnDef(lineNumber, "20px");
    subjectCol = new EditGridColumnDef(PStatementLineDef.acc1_subject, true, "150px");
    directionCol = new EditGridColumnDef(PStatementLineDef.acc1_direction, "60px");
    totalCol = new EditGridColumnDef(total, true, "80px");
    taxRateCol = new EditGridColumnDef(PStatementLineDef.acc1_taxRate, "60px");
    taxCol = new EditGridColumnDef(PStatementLineDef.total_tax, false, "60px",
        HasHorizontalAlignment.ALIGN_RIGHT);
    invoiceCol = new EditGridColumnDef(acc2invoice, true, "80px");
    beginDateCol = new EditGridColumnDef(PStatementLineDef.acc1_beginTime, true, "90px");
    endDateCol = new EditGridColumnDef(PStatementLineDef.acc1_endTime, true, "90px");
    lastPayDateCol = new EditGridColumnDef(PStatementLineDef.acc2_lastPayDate, true, "90px");
    remarkCol = new EditGridColumnDef(PStatementLineDef.remark, true, "100px");

    return Arrays.asList(lineNumberCol, subjectCol, directionCol, totalCol, taxRateCol, taxCol,
        invoiceCol, beginDateCol, endDateCol, lastPayDateCol, remarkCol);
  }

  private void doAdd() {
    GWTUtil.blurActiveElement();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        doAddLine();
        doRefresh();
        editGrid.setCurrentRow(values.size() - 1);
      }
    });
  }

  private void doAddLine() {
    int size = values.size();
    if (size == 0) {
      values.add(createLine());
    } else {
      BStatementLine line = values.get(size - 1);
      if (line.getAcc1().getSubject() != null && line.getAcc1().getSubject().getUuid() != null) {
        values.add(createLine());
      }
    }
  }

  private void doRefresh() {
    editGrid.refresh();
    resultTotalHtml.setText(StatementMessages.M.resultTotal(values.size()));
    RActionEvent.fire(StatementLinePatchEditGridGadget.this, ActionName.REFRESH, new Integer(
        editGrid.getCurrentRow()));
  }

  private BStatementLine createLine() {
    BStatementLine line = new BStatementLine();
    line.setAcc1(new BAcc1());
    line.setAcc2(new BAcc2());
    line.setFromStatement(true);
    return line;
  }

  private void doInsert() {
    int index = doInsertLine();
    doRefresh();
    editGrid.setCurrentRow(index);
  }

  private int doInsertLine() {
    BStatementLine currentLine = values.get(editGrid.getCurrentRow());
    if (currentLine.getAcc1().getSubject() == null
        || currentLine.getAcc1().getSubject().getUuid() == null)
      return editGrid.getCurrentRow();

    if (editGrid.getCurrentRow() == 0) {
      values.add(editGrid.getCurrentRow(), createLine());
      return editGrid.getCurrentRow();
    } else {
      BStatementLine prevLine = values.get(editGrid.getCurrentRow() - 1);
      if (prevLine.getAcc1().getSubject() == null
          || prevLine.getAcc1().getSubject().getUuid() == null) {
        return editGrid.getCurrentRow();
      }
      values.add(editGrid.getCurrentRow(), createLine());
      return editGrid.getCurrentRow();
    }
  }

  private List<Integer> list;

  private void doSetLastPayDate() {
    list = editGrid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(StatementMessages.M.seleteDataToAction(StatementMessages.M.setLastPayDate(),
          StatementMessages.M.detailLines()));
      return;
    }

    if (dateDialog == null) {
      dateDialog = new StatementDateEditDialog(new StatementDateEditDialog.Callback() {

        @Override
        public void execute(Date date) {
          for (Integer row : list) {
            values.get(row.intValue()).getAcc2().setLastPayDate(date);
          }
          editGrid.refresh();
        }
      });
    }
    dateDialog.onShow();
  }

  void doDelete() {
    List list = editGrid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(M.seleteDataToAction(M.delete(), M.detailLines()));
      return;
    }
    doDeleteRows();
    doRefresh();
  }

  private void doDeleteRows() {
    List<Integer> selections = editGrid.getSelections();
    for (int i = selections.size() - 1; i >= 0; i--) {
      Integer row = selections.get(i);
      int dataRow = row.intValue();
      values.remove(dataRow);
    }

    if (values.size() <= 0) {
      doAddLine();
    }
  }

  private class DateProvider implements EditGridDataProvider<BStatementLine> {

    @Override
    public List<BStatementLine> getData() {
      return values;
    }

    @Override
    public Object getData(int row, String colName) {
      if (values == null || row < 0 || row >= values.size())
        return null;

      resultTotalHtml.setText(StatementMessages.M.resultTotal(values.size()));

      BStatementLine rowData = values.get(row);
      if (colName.equals(lineNumberCol.getName()))
        return row + 1;
      else if (colName.equals(subjectCol.getName()))
        return rowData.getAcc1().getSubject();
      else if (colName.equals(directionCol.getName()))
        return DirectionType.getCaptionByValue(rowData.getAcc1().getDirection());
      else if (colName.equals(totalCol.getName()))
        return rowData.getTotal() == null ? null : rowData.getTotal().getTotal();
      else if (colName.equals(taxRateCol.getName()))
        return rowData.getAcc1().getTaxRate() == null ? null : rowData.getAcc1().getTaxRate()
            .caption();
      else if (colName.equals(taxCol.getName()))
        return rowData.getTotal() == null ? null : rowData.getTotal().getTax() == null ? null
            : M3Format.fmt_money.format(rowData.getTotal().getTax().doubleValue());
      else if (colName.equals(invoiceCol.getName()))
        return getInvoice(rowData);
      else if (colName.equals(beginDateCol.getName()))
        return rowData.getAcc1().getBeginTime();
      else if (colName.equals(endDateCol.getName()))
        return rowData.getAcc1().getEndTime();
      else if (colName.equals(lastPayDateCol.getName()))
        return rowData.getAcc2().getLastPayDate();
      else if (colName.equals(remarkCol.getName()))
        return rowData.getRemark();
      return null;
    }

    @Override
    public BStatementLine create() {
      return createLine();
    }
  }

  private boolean getInvoice(BStatementLine rowData) {
    if (rowData.getAcc2() == null || rowData.getAcc2().getInvoice() == null) {
      rowData.getAcc2().setInvoice(BAccount.NONE_INVOICEBILL);
      return true;
    } else if (BAccount.DISABLE_BILL_UUID.equals(rowData.getAcc2().getInvoice().getBillUuid())) {
      return false;
    } else if (BAccount.NONE_BILL_UUID.equals(rowData.getAcc2().getInvoice().getBillUuid())) {
      return true;
    } else {
      return true;
    }
  }

  private class CellWidgetFactory implements EditGridCellWidgetFactory<BStatementLine> {

    @Override
    public Widget createEditWidget(FieldDef field, int dataRowIdx, final BStatementLine rowData) {
      if (field.getName().equals(subjectCol.getName())) {
        SubjectUCNBox subjectField = new SubjectUCNBox(BSubjectType.credit.name(), null);
        subjectField.setRequired(false);
        subjectField.addChangeHandler(new SubjectChange(dataRowIdx));
        return subjectField;
      } else if (field.getName().equals(totalCol.getName())) {
        RNumberBox totalField = new RNumberBox();
        totalField.setSelectAllOnFocus(true);
        totalField.setCaption(M.total());
        totalField.setRequired(false);
        totalField.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
        totalField.setFormat(M3Format.fmt_money);
        totalField.addChangeHandler(new TotalChange(dataRowIdx));
        return totalField;
      } else if (field.getName().equals(invoiceCol.getName())) {
        RCheckBox invoiceField = new RCheckBox();
        invoiceField.addChangeHandler(new InvoiceChange(dataRowIdx));
        return invoiceField;
      } else if (field.getName().equals(beginDateCol.getName())) {
        RDateBox beginDateField = new RDateBox(PStatementLineDef.constants.acc1_beginTime());
        beginDateField.setRequired(false);
        beginDateField.addChangeHandler(new BeginDateChange(dataRowIdx));
        return beginDateField;
      } else if (field.getName().equals(endDateCol.getName())) {
        RDateBox endDateField = new RDateBox(PStatementLineDef.constants.acc1_endTime());
        endDateField.setRequired(false);
        endDateField.addChangeHandler(new EndDateChange(dataRowIdx));
        return endDateField;
      } else if (field.getName().equals(lastPayDateCol.getName())) {
        RDateBox endDateField = new RDateBox(PStatementLineDef.constants.acc2_lastPayDate());
        endDateField.setRequired(false);
        endDateField.addChangeHandler(new LastPayDateChange(dataRowIdx));
        return endDateField;
      } else if (field.getName().equals(remarkCol.getName())) {
        RTextBox remarkField = new RTextBox(PStatementLineDef.remark);
        remarkField.addChangeHandler(new RemarkChange(dataRowIdx));
        return remarkField;
      }
      return null;
    }
  }

  private class SubjectChange implements ChangeHandler {
    private int row;

    public SubjectChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BStatementLine value = values.get(row);
      if (value == null)
        return;

      SubjectUCNBox box = (SubjectUCNBox) event.getSource();
      // 1、科目
      value.getAcc1().setSubject(box.getValue());
      BSubject rawValue = (BSubject) box.getRawValue();
      // 2、收付方向
      value.getAcc1().setDirection(rawValue.getDirection());
      // 3、税率
      value.getAcc1().setTaxRate(rawValue.getTaxRate());

      if (value.getTotal() != null && value.getTotal().getTotal() != null) {
        value.getTotal().setTax(
            BTaxCalculator.tax(value.getTotal().getTotal(), value.getAcc1().getTaxRate(),
                ep.getScale(), ep.getRoundingMode()));
      }

      // 4、通知新建、编辑刷新Total面板
      RActionEvent.fire(StatementLinePatchEditGridGadget.this, ActionName.REFRESH, "");
      editGrid.refresh(row);
    }
  }

  private class TotalChange implements ChangeHandler {
    private int row;

    public TotalChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BStatementLine value = values.get(row);
      if (value == null)
        return;

      RNumberBox box = (RNumberBox) event.getSource();
      box.setSelectAllOnFocus(true);
      BigDecimal totalLocal = box.getValueAsBigDecimal();
      value.setTotal(new BTotal(totalLocal, BTaxCalculator.tax(totalLocal, value.getAcc1()
          .getTaxRate(), ep.getScale(), ep.getRoundingMode())));
      // 通知新建、编辑刷新Total面板
      RActionEvent.fire(StatementLinePatchEditGridGadget.this, ActionName.REFRESH, "");
      editGrid.refresh(row);
    }
  }

  private class InvoiceChange implements ChangeHandler {
    private int row;

    public InvoiceChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BStatementLine value = values.get(row);
      if (value == null)
        return;

      RCheckBox box = (RCheckBox) event.getSource();
      BInvoiceBill invoice = null;
      if (box.isChecked())
        invoice = BAccount.NONE_INVOICEBILL;
      else
        invoice = BAccount.DISABLE_INVOICEBILL;

      value.getAcc2().setInvoice(invoice);
      // 通知新建、编辑刷新ivcPayTotal、 ivcReceiptTotal
      RActionEvent.fire(StatementLinePatchEditGridGadget.this, ActionName.REFRESH, "");
      editGrid.refresh(row);
    }
  }

  private class BeginDateChange implements ChangeHandler {
    private int row;

    public BeginDateChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BStatementLine value = values.get(row);
      if (value == null)
        return;

      RDateBox box = (RDateBox) event.getSource();
      Date date = box.getValue();
      value.getAcc1().setBeginTime(date);
      value.getAcc1().setAccountDate(date);
      value.getAcc1().setOcrTime(date);
    }
  }

  private class EndDateChange implements ChangeHandler {
    private int row;

    public EndDateChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BStatementLine value = values.get(row);
      if (value == null)
        return;

      RDateBox box = (RDateBox) event.getSource();
      value.getAcc1().setEndTime(box.getValue());
    }
  }

  private class LastPayDateChange implements ChangeHandler {
    private int row;

    public LastPayDateChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BStatementLine value = values.get(row);
      if (value == null)
        return;

      RDateBox box = (RDateBox) event.getSource();
      value.getAcc2().setLastPayDate(box.getValue());
    }
  }

  private class RemarkChange implements ChangeHandler {
    private int row;

    public RemarkChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BStatementLine value = values.get(row);
      if (value == null)
        return;

      RTextBox box = (RTextBox) event.getSource();
      value.setRemark(box.getValue());
    }
  }

  private class Handler_Click implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == addAction) {
        doAdd();
      } else if (event.getSource() == insertAction) {
        doInsert();
      } else if (event.getSource() == deleteAction) {
        doDelete();
      } else if (event.getSource() == setLastPayDateAction) {
        doSetLastPayDate();
      }
    }
  }

  public static class ActionName {
    /** 表格刷新事件 */
    public static final String REFRESH = "refresh";
  }

  public static M M = GWT.create(M.class);

  public static interface M extends Messages {
    @DefaultMessage("行号")
    String lineNumber();

    @DefaultMessage("补录金额")
    String total();

    @DefaultMessage("开发票")
    String invoice();

    @DefaultMessage("账单明细：第{0}行的起始日期不能大于截止日期。")
    String beginTimeMustBeforeEndTime(int i);

    @DefaultMessage("明细行")
    String detailLines();

    @DefaultMessage("请先选择需要{0}的{1}。")
    String seleteDataToAction(String action, String entityCaption);

    @DefaultMessage("删除")
    String delete();

    @DefaultMessage("账单明细：第{0}的补录金额不能为0。")
    String totalPatch(int i);

    @DefaultMessage("账单明细：第{0}行的{1}不能为空。")
    String isNull(int i, String caption);

    @DefaultMessage("账单明细：第{0}行与第{1}行重复。")
    String duplicateGift(int i, int j);

    @DefaultMessage("账单明细行不能为空。")
    String emptyLine();

  }

}
