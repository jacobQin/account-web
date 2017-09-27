/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegInvoiceEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.invoice.commons.client.actionname.InvoiceRegActionName;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegInvoice;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegInvoiceDef;
import com.hd123.m3.account.gwt.invoice.receipt.client.EPRecInvoiceReg;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author chenpeisi
 * 
 */
public class RecInvoiceRegInvoiceEditGrid extends Composite implements HasRActionHandlers,
    RActionHandler {

  private BInvoiceReg entity;

  private int currentRow = -1;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef amountCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef invoiceDateCol;
  private RGridColumnDef remarkCol;

  public RecInvoiceRegInvoiceEditGrid() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    drawGrid();
    panel.add(grid);

    initWidget(panel);
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setProvider(new GridDataProvider());
    grid.addClickHandler(new Handler_grid());

    lineNumberCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.lineNumber());
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    grid.addColumnDef(lineNumberCol);

    invoiceTypeCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.invoiceType());
    invoiceTypeCol.setWidth("90px");
    grid.addColumnDef(invoiceTypeCol);

    invoiceNumberCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.invoiceNumber());
    invoiceNumberCol.setWidth("150px");
    grid.addColumnDef(invoiceNumberCol);

    totalCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.total_total());
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("90px");
    grid.addColumnDef(totalCol);

    amountCol = new RGridColumnDef(InvoiceRegMessage.M.amount());
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setWidth("90px");
    grid.addColumnDef(amountCol);

    taxCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.total_tax());
    taxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    taxCol.setWidth("90px");
    grid.addColumnDef(taxCol);

    taxRateCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.taxRate());
    taxRateCol.setWidth("120px");
    grid.addColumnDef(taxRateCol);

    invoiceDateCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.invoiceDate());
    invoiceDateCol.setWidth("90px");
    grid.addColumnDef(invoiceDateCol);

    remarkCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.remark());
    remarkCol.setWidth("90px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  public void setEntity(BInvoiceReg entity) {
    this.entity = entity;
  }

  public void refresh() {
    currentRow = 0;
    if (entity.getInvoices().size() == 0) {
      BInvoiceRegInvoice line = new BInvoiceRegInvoice();
      line.setTaxRate(EPRecInvoiceReg.getInstance().getDefaultValue().getTaxRate());
      entity.getInvoices().add(line);
    }
    currentRow = 0;
    doRefresh(false);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == InvoiceRegActionName.PREV_INVOICE) {
      if (currentRow > 0) {
        currentRow--;
        grid.setCurrentRow(currentRow);
        RActionEvent.fire(this, InvoiceRegActionName.CHANGE_INVOICE, Integer.valueOf(currentRow),
            Boolean.FALSE);
      }
    } else if (event.getActionName() == InvoiceRegActionName.NEXT_INVOICE) {
      if (currentRow < entity.getInvoices().size() - 1) {
        currentRow++;
        grid.setCurrentRow(currentRow);
        RActionEvent.fire(this, InvoiceRegActionName.CHANGE_INVOICE, Integer.valueOf(currentRow),
            Boolean.FALSE);
      }
    } else if (event.getActionName() == InvoiceRegActionName.ADD_INVOICE) {
      doAdd();
    } else if (event.getActionName() == InvoiceRegActionName.INSERT_INVOICE) {
      doInsert();
    } else if (event.getActionName() == InvoiceRegActionName.CHANGE_INVOICE_VALUE) {
      grid.refreshRow(currentRow);
      RActionEvent.fire(this, InvoiceRegActionName.AGGREGATE_INVOICE);
    } else if (event.getActionName() == InvoiceRegActionName.DELETE_CURRENT_INVOICE) {
      doDeleteCurRow();
      RActionEvent.fire(this, InvoiceRegActionName.REFRESH_INVOICE);
      RActionEvent.fire(this, InvoiceRegActionName.AGGREGATE_INVOICE);
      RActionEvent.fire(this, InvoiceRegActionName.CHANGE_INVOICE, Integer.valueOf(currentRow),
          Boolean.TRUE);
    } else if (event.getActionName() == InvoiceRegActionName.BATCH_DELETE_INVOICE) {
      doBatchDelete();
      RActionEvent.fire(this, InvoiceRegActionName.REFRESH_LINE);
      RActionEvent.fire(this, InvoiceRegActionName.AGGREGATE_INVOICE);
      RActionEvent.fire(this, InvoiceRegActionName.CHANGE_LINE, Integer.valueOf(currentRow),
          Boolean.TRUE);
    } else if (event.getActionName() == InvoiceRegActionName.ONSELECT_INVOICE) {
      int row = ((Integer) event.getParameters().get(0)).intValue();
      doSelect(row);
      RActionEvent.fire(this, InvoiceRegActionName.CHANGE_INVOICE, Integer.valueOf(currentRow),
          Boolean.FALSE);
    }
  }

  private void doRefresh(boolean focusOnFirst) {
    refreshLineNumber();
    grid.refresh();
    grid.setCurrentRow(currentRow);
    RActionEvent.fire(this, InvoiceRegActionName.REFRESH_INVOICE);
    RActionEvent.fire(this, InvoiceRegActionName.CHANGE_INVOICE, Integer.valueOf(currentRow),
        Boolean.valueOf(focusOnFirst));
  }

  private void refreshLineNumber() {
    for (int i = 0; i < entity.getInvoices().size(); i++) {
      BInvoiceRegInvoice invoice = entity.getInvoices().get(i);
      invoice.setLineNumber(i + 1);
    }
  }

  private void doBatchDelete() {
    List list = grid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(InvoiceRegMessage.M.seleteDataToAction(InvoiceRegMessage.M.delete(),
          InvoiceRegMessage.M.line()));
      return;
    }
    doDeleteRows();
    doRefresh(true);
  }

  private void doAdd() {
    doAddLine();
    doRefresh(true);
  }

  private void doInsert() {
    doInsertLine();
    doRefresh(true);
  }

  private void doDeleteCurRow() {
    entity.getInvoices().remove(currentRow);
    currentRow = entity.getInvoices().size() - 1;
    if (currentRow < 0)
      doAddLine();
    doRefresh(true);
  }

  private void doDeleteRows() {
    List<Integer> selections = grid.getSelections();
    boolean deleteCurLine = false;
    for (int i = selections.size() - 1; i >= 0; i--) {
      Integer row = selections.get(i);
      int dataRow = row.intValue();
      entity.getInvoices().remove(dataRow);

      if (dataRow == currentRow)
        deleteCurLine = true;
    }
    if (deleteCurLine)
      currentRow = entity.getInvoices().size() - 1;
    else {
      for (Integer row : grid.getSelections()) {
        if (row.intValue() < currentRow)
          currentRow--;
      }
    }

    if (currentRow < 0)
      doAddLine();
  }

  private BInvoiceRegInvoice doAddLine() {
    int size = entity.getInvoices().size();
    if (size == 0) {
      BInvoiceRegInvoice line = new BInvoiceRegInvoice();
      line.setTaxRate(EPRecInvoiceReg.getInstance().getDefaultValue().getTaxRate());
      entity.getInvoices().add(line);
    } else {
      BInvoiceRegInvoice l = entity.getInvoices().get(size - 1);
      if (l.getInvoiceNumber() != null) {
        BInvoiceRegInvoice line = new BInvoiceRegInvoice();
        line.setTaxRate(EPRecInvoiceReg.getInstance().getDefaultValue().getTaxRate());
        entity.getInvoices().add(line);
        line.setInvoiceType(l.getInvoiceType());
      }
    }
    currentRow = entity.getInvoices().size() - 1;
    return entity.getInvoices().get(currentRow);
  }

  private BInvoiceRegInvoice doInsertLine() {
    assert entity.getInvoices().size() != 0;

    BInvoiceRegInvoice currentLine = entity.getInvoices().get(currentRow);
    if (currentLine.getInvoiceNumber() == null)
      return entity.getInvoices().get(currentRow);

    if (currentRow == 0) {
      BInvoiceRegInvoice line = new BInvoiceRegInvoice();
      entity.getInvoices().add(currentRow, line);
    } else {
      BInvoiceRegInvoice prevLine = entity.getInvoices().get(currentRow - 1);
      if (prevLine.getInvoiceNumber() == null) {
        currentRow--;
        return entity.getInvoices().get(currentRow);
      }
      BInvoiceRegInvoice line = new BInvoiceRegInvoice();
      entity.getInvoices().add(currentRow, line);
    }
    return entity.getInvoices().get(currentRow);
  }

  public void doSelect(int row) {
    currentRow = row;
    grid.setCurrentRow(currentRow);
    grid.refreshRow(currentRow);

  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      currentRow = cell.getRow();
      RActionEvent.fire(RecInvoiceRegInvoiceEditGrid.this, InvoiceRegActionName.CHANGE_INVOICE,
          Integer.valueOf(currentRow), Boolean.FALSE);
    }
  }

  private class GridDataProvider implements RGridDataProvider {

    public Object getData(int row, int col) {
      if (entity == null || entity.getInvoices().size() == 0)
        return null;

      BInvoiceRegInvoice invoice = entity.getInvoices().get(row);

      switch (col) {
      case 0:
        return Integer.valueOf(invoice.getLineNumber());
      case 1:
        return EPRecInvoiceReg.getInstance().getInvoiceTypeMap().get(invoice.getInvoiceType());
      case 2:
        return invoice.getInvoiceNumber();
      case 3:
        return invoice.getTotal() == null || invoice.getTotal().getTotal() == null ? null
            : GWTFormat.fmt_money.format(invoice.getTotal().getTotal().doubleValue());
      case 4:
        return invoice.getTotal() == null || invoice.getTotal().getTotal() == null
            || invoice.getTotal().getTax() == null ? null : GWTFormat.fmt_money.format(invoice
            .getTotal().getTotal().doubleValue());
      case 5:
        return invoice.getTotal() == null || invoice.getTotal().getTax() == null ? null : invoice
            .getTotal().getTax();
      case 6:
        return invoice.getTaxRate() == null ? null : invoice.getTaxRate().caption();
      case 7:
        return invoice.getInvoiceDate() == null ? null : GWTFormat.fmt_yMd.format(invoice
            .getInvoiceDate());
      case 8:
        return invoice.getRemark();

      default:
        return null;
      }
    }

    public int getRowCount() {
      return entity == null ? 0 : entity.getInvoices().size();
    }
  }
}
