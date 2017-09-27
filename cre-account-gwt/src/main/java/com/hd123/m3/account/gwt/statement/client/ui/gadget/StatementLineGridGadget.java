/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementLineGridGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月6日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementLineDef;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * @author zhr
 * 
 */
public class StatementLineGridGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {

  public StatementLineGridGadget() {
    super();
    setEditing(true);
    setContentSpacing(0);
    setCaption(StatementMessages.M.account_detail());
    setContent(drawGrid());

    batchAddAction = new RAction(RActionFacade.APPEND, clickHandler);
    RToolbarButton batchAddButton = new RToolbarButton(batchAddAction);
    batchAddAction.setHotKey(null);
    batchAddButton.setShowText(false);
    getCaptionBar().addButton(batchAddButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteAction.setHotKey(null);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

    getCaptionBar().addButton(new RToolbarSeparator());
    setLastPayDateAction = new RAction(StatementMessages.M.setLastPayDate(), clickHandler);
    getCaptionBar().addButton(new RToolbarButton(setLastPayDateAction));
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
  }

  public void setValue(BStatement entity) {
    this.entity = entity;
    this.values = entity.getLines();

    grid.rebuild();
    if (values.isEmpty() == false) {
      grid.setCurrentRow(0);
    }
    boolean canEditAccount = EPStatement.getInstance().isPermitted(StatementPermDef.MODIFYACCOUNT);
    batchAddAction.setEnabled(canEditAccount);
    deleteAction.setEnabled(canEditAccount);
  }

  private OtherFieldDef acc2Invoice = new OtherFieldDef("acc2.invoice",
      StatementMessages.M.invoice(), true);
  private OtherFieldDef dateRange = new OtherFieldDef("dateRange", StatementMessages.M.dateRange(),
      true);

  private EPStatement ep = EPStatement.getInstance();
  private BStatement entity;
  private List<BStatementLine> values;

  private RAction batchAddAction;
  private RAction deleteAction;
  private RAction setLastPayDateAction;
  private StatementAccountDialog dialog;

  private StatementDateEditDialog dateDialog;
  private RGrid grid;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef lastPayDateCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef freeTotalCol;
  private RGridColumnDef rateCol;
  private RGridColumnDef invoiceCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillNumberCol;

  private Handler_click clickHandler = new Handler_click();

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setProvider(new GridDataProvider());

    subjectCodeCol = new RGridColumnDef(PStatementLineDef.acc1_subject_code);
    subjectCodeCol.setWidth("65px");
    subjectCodeCol.setResizable(true);
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PStatementLineDef.acc1_subject_name);
    subjectNameCol.setWidth("80px");
    subjectNameCol.setResizable(true);
    grid.addColumnDef(subjectNameCol);

    directionCol = new RGridColumnDef(PStatementLineDef.acc1_direction);
    directionCol.setWidth("60px");
    directionCol.setResizable(true);
    grid.addColumnDef(directionCol);

    lastPayDateCol = new RGridColumnDef(PStatementLineDef.acc2_lastPayDate);
    lastPayDateCol.setRendererFactory(new DateFieldRendererFactory());
    lastPayDateCol.setWidth("80px");
    lastPayDateCol.setResizable(true);
    grid.addColumnDef(lastPayDateCol);

    totalCol = new RGridColumnDef(PStatementLineDef.total_total);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("65px");
    totalCol.setResizable(true);
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PStatementLineDef.total_tax);
    taxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    taxCol.setWidth("65px");
    taxCol.setResizable(true);
    grid.addColumnDef(taxCol);

    freeTotalCol = new RGridColumnDef(PStatementLineDef.freeTotal_total);
    freeTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    freeTotalCol.setWidth("65px");
    freeTotalCol.setResizable(true);
    grid.addColumnDef(freeTotalCol);

    rateCol = new RGridColumnDef(PStatementLineDef.acc1_taxRate_rate);
    rateCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    rateCol.setWidth("65px");
    rateCol.setResizable(true);
    grid.addColumnDef(rateCol);

    invoiceCol = new RGridColumnDef(acc2Invoice);
    invoiceCol.setWidth("60px");
    invoiceCol.setResizable(true);
    grid.addColumnDef(invoiceCol);

    dateRangeCol = new RGridColumnDef(dateRange);
    dateRangeCol.setWidth("120px");
    dateRangeCol.setResizable(true);
    grid.addColumnDef(dateRangeCol);

    sourceBillTypeCol = new RGridColumnDef(PStatementLineDef.acc1_sourceBill_billType);
    sourceBillTypeCol.setWidth("100px");
    sourceBillTypeCol.setResizable(true);
    grid.addColumnDef(sourceBillTypeCol);

    sourceBillNumberCol = new RGridColumnDef(PStatementLineDef.acc1_sourceBill);
    sourceBillNumberCol.setWidth("120px");
    sourceBillNumberCol.setResizable(true);
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()) {
      @Override
      public RCellRenderer<BDispatch> makeRenderer(final RGrid grid, final RGridColumnDef colDef,
          final int row, final int col, BDispatch data) {
        if (data == null)
          return null;
        if (StatementMessages.M.self_billNumber().equals(
            data.getParams().get(GRes.R.dispatch_key()))) {
          HTMLField field = new HTMLField(row, col);
          BDispatch dispatch = new BDispatch();
          dispatch.addParams(GRes.R.dispatch_key(), StatementMessages.M.self_caption());
          field.setValue(dispatch);
          return field;
        } else {
          return super.makeRenderer(grid, colDef, row, col, data);
        }
      }
    });
    grid.addColumnDef(sourceBillNumberCol);
    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private class HTMLField extends Composite implements RCellRenderer<BDispatch> {
    private int gridRow;
    private int gridColumn;
    private HTML field;

    public HTMLField() {
      super();
      field = new HTML();
      initWidget(field);
    }

    public HTMLField(int gridRow, int gridColumn) {
      this();
      this.gridRow = gridRow;
      this.gridColumn = gridColumn;
    }

    @Override
    public BDispatch getValue() {
      return null;
    }

    @Override
    public void setValue(BDispatch value) {
      field.setText(value == null ? null : value.getParams().get(GRes.R.dispatch_key()));
    }

    @Override
    public int getRow() {
      return gridRow;
    }

    @Override
    public int getColumn() {
      return gridColumn;
    }

  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return values == null ? 0 : values.size();
    }

    @Override
    public Object getData(int row, int col) {
      BStatementLine rowData = values.get(row);

      if (col == subjectCodeCol.getIndex())
        return rowData.getAcc1().getSubject() == null ? null : rowData.getAcc1().getSubject()
            .getCode();
      else if (col == subjectNameCol.getIndex())
        return rowData.getAcc1().getSubject() == null ? null : rowData.getAcc1().getSubject()
            .getName();
      else if (col == directionCol.getIndex())
        return DirectionType.getCaptionByValue(rowData.getAcc1().getDirection());
      else if (col == lastPayDateCol.getIndex())
        return rowData.getAcc2().getLastPayDate();
      else if (col == totalCol.getIndex())
        return rowData.getTotal() == null ? null : M3Format.fmt_money.format(rowData.getTotal().getTotal());
      else if (col == taxCol.getIndex())
        return rowData.getTotal() == null ? null : M3Format.fmt_money.format(rowData.getTotal().getTax());
      else if (col == freeTotalCol.getIndex())
        return rowData.getFreeTotal() == null ? null : M3Format.fmt_money.format(rowData.getFreeTotal().getTotal());
      else if (col == rateCol.getIndex())
        return rowData.getAcc1().getTaxRate() == null ? null : rowData.getAcc1().getTaxRate()
            .toString();
      else if (col == invoiceCol.getIndex())
        return rowData.getInvoice() ? StatementMessages.M.yes() : StatementMessages.M.no();
      else if (col == dateRangeCol.getIndex())
        return dateRange(rowData.getAcc1().getBeginTime(), rowData.getAcc1().getEndTime());
      else if (col == sourceBillTypeCol.getIndex())
        return rowData.getAcc1().getSourceBill() == null ? null : ep.getBillType().get(
            rowData.getAcc1().getSourceBill().getBillType());
      else if (col == sourceBillNumberCol.getIndex()) {
        if (rowData.getAcc1().getSourceBill() == null) {
          return null;
        }

        BDispatch dispatch = new BDispatch(rowData.getAcc1().getSourceBill().getBillType());
        dispatch
            .addParams(GRes.R.dispatch_key(), rowData.getAcc1().getSourceBill().getBillNumber());
        return dispatch;
      }
      return null;
    }
  }

  private String dateRange(Date beginTime, Date endTime) {
    if (beginTime == null && endTime == null)
      return null;
    StringBuffer sb = new StringBuffer();
    if (beginTime != null)
      sb.append(M3Format.fmt_yMd.format(beginTime));
    sb.append("~");
    if (endTime != null)
      sb.append(M3Format.fmt_yMd.format(endTime));
    return sb.toString();
  }

  private class Handler_click implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == batchAddAction) {
        doAdd();
      } else if (event.getSource() == deleteAction) {
        doDeleteLine();
      } else if (event.getSource() == setLastPayDateAction) {
        doSetLastPayDate();
      }
    }
  }

  private List<Integer> list;

  private void doSetLastPayDate() {
    list = grid.getSelections();
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
            entity.getLines().get(row.intValue()).getAcc2().setLastPayDate(date);
          }
          grid.deselectAllRows(false);
          grid.rebuild();
        }
      });
    }
    dateDialog.onShow();
  }

  private void doAdd() {
    if (dialog == null) {
      dialog = new StatementAccountDialog();
      dialog.addValueChangeHandler(new ValueChangeHandler<List<BStatementLine>>() {

        @Override
        public void onValueChange(ValueChangeEvent<List<BStatementLine>> event) {
          for (BStatementLine line : event.getValue()) {
            if (values.contains(line) == false) {
              line.setFreeTotal(BTotal.zero());
              values.add(line);
            }
          }
          doRefresh();
          grid.setCurrentRow(values.size() - 1);
        }
      });
    }
    Set<String> addUuids = new HashSet();
    for (BStatementLine line : values) {
      addUuids.add(line.getAcc1().getId());
    }
    dialog.center(entity.getUuid(), entity.getCounterpart().getUuid(),
        entity.getContract() == null ? null : entity.getContract().getUuid(), addUuids);
  }

  private void doDeleteLine() {
    List<Integer> selections = grid.getSelections();
    if (selections.isEmpty()) {
      RMsgBox.showError(StatementMessages.M.seleteDataToAction(StatementMessages.M.delete(),
          StatementMessages.M.account_detail()));
      return;
    }

    for (int i = selections.size() - 1; i >= 0; i--) {
      int row = selections.get(i).intValue();
      if (row >= 0 && row < values.size()) {

        BStatementLine line = entity.getLines().get(row);
        // 账单产生的账款明细行不能删除
        if (canRemove(line) == false)
          continue;

        values.remove(row);
      }
    }
    doRefresh();
  }

  private boolean canRemove(BStatementLine line) {
    if (line == null || line.getAcc1() == null || line.getAcc1().getSourceBill() == null)
      return false;

    if (line.isFromStatement())
      return false;

    return true;
  }

  private void doRefresh() {
    grid.deselectAllRows(false);
    grid.rebuild();
    RActionEvent.fire(StatementLineGridGadget.this, ActionName.REFRESH,
        new Integer(grid.getCurrentRow()));
  }

  public static class ActionName {
    /** 表格刷新事件 */
    public static final String REFRESH = "refresh";
  }

  public class DateFieldRendererFactory implements RCellRendererFactory<Date> {
    public DateFieldRendererFactory() {

    }

    @Override
    public RCellRenderer<Date> makeRenderer(RGrid grid, RGridColumnDef colDef, int row, int col,
        Date data) {
      DateFieldRenderer renderer = new DateFieldRenderer();
      renderer.setRow(row);
      renderer.setColumn(col);
      renderer.setValue(data);
      return renderer;
    }

  }

  public class DateFieldRenderer extends RVerticalPanel implements RCellRenderer<Date> {

    private RDateBox field;
    /** 所在的表格行 */
    private int row = -1;
    /** 所在的表格列 */
    private int column = -1;

    public DateFieldRenderer() {
      super();

      field = new RDateBox();
      field.addValueChangeHandler(new ValueChangeHandler<Date>() {

        @Override
        public void onValueChange(ValueChangeEvent<Date> event) {
          BStatementLine line = entity.getLines().get(row);
          line.getAcc2().setLastPayDate(field.getValue());
        }
      });
      DateFieldRenderer.this.add(field);
    }

    @Override
    public Date getValue() {
      return field.getValue();
    }

    @Override
    public void setValue(Date value) {
      field.setValue(value);
    }

    @Override
    public int getRow() {
      return row;
    }

    @Override
    public int getColumn() {
      return column;
    }

    void setRow(int row) {
      this.row = row;
    }

    void setColumn(int column) {
      this.column = column;
    }

  }

}
