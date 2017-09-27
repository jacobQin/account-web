/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAdjustLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.StatementAdjustCreatePage;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.StatementAdjustEditPage;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustLineDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.renderer.LineNumberRendererFactory;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RBooleanRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustLineEditGrid extends Composite implements RActionHandler,
    HasRActionHandlers {

  @Override
  public void onAction(final RActionEvent event) {
    GWTUtil.blurActiveElement();
    CommandQueue.offer(new LocalCommand() {

      @Override
      public void onCall(CommandQueue queue) {
        queue.goon();
        if (event.getActionName() == StatementAdjustLineEditGadget.ActionName.PREVLINE) {
          currentRow = currentRow == 0 ? currentRow : --currentRow;
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          RActionEvent.fire(StatementAdjustLineEditGrid.this, ActionName.CHANGE_LINE, new Integer(
              currentRow), Boolean.FALSE);
        }
        if (event.getActionName() == StatementAdjustLineEditGadget.ActionName.NEXTLINE) {
          currentRow = currentRow >= statementAdjust.getLines().size() - 1 ? currentRow
              : ++currentRow;
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          RActionEvent.fire(StatementAdjustLineEditGrid.this, ActionName.CHANGE_LINE, new Integer(
              currentRow), Boolean.FALSE);
        }

        if (event.getActionName() == StatementAdjustEditPage.ActionName.CHANGE_LINE
            || event.getActionName() == StatementAdjustCreatePage.ActionName.CHANGE_LINE) {
          RActionEvent.fire(StatementAdjustLineEditGrid.this, ActionName.BEFORE_CHANGE_LINE, "");
          currentRow = ((Integer) event.getParameters().get(0)).intValue();
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          RActionEvent.fire(StatementAdjustLineEditGrid.this, ActionName.CHANGE_LINE, new Integer(
              currentRow), Boolean.FALSE);
        }

        if (event.getActionName() == StatementAdjustLineEditGadget.ActionName.CHANGE_VALUE) {
          Integer row = (Integer) event.getParameters().get(0);
          grid.refreshRow(row.intValue());
        }

        if (event.getActionName() == StatementAdjustLineEditGadget.ActionName.DELETE_LINE) {
          doDeleteCurRow();
        }

        if (event.getActionName() == StatementAdjustCreatePage.ActionName.BATCH_DELETE
            || event.getActionName() == StatementAdjustEditPage.ActionName.BATCH_DELETE) {
          doBatchDelete();
        }

        if (event.getActionName() == StatementAdjustLineEditGadget.ActionName.ADD_LINE
            || event.getActionName() == StatementAdjustCreatePage.ActionName.ADD_LINE
            || event.getActionName() == StatementAdjustEditPage.ActionName.ADD_LINE) {
          doAdd();
        }

        if (event.getActionName() == StatementAdjustCreatePage.ActionName.INSERT_LINE
            || event.getActionName() == StatementAdjustEditPage.ActionName.INSERT_LINE) {
          doInsert();
        }

      }
    });
    CommandQueue.awake();
  }

  public static class ActionName {
    /** 换行前触发的行为 */
    public static final String BEFORE_CHANGE_LINE = "before_change";
    /** 换行事件 */
    public static final String CHANGE_LINE = "change_line";
    /** 表格刷新事件 */
    public static final String REFRESH = "refresh";
  }

  public void refresh() {
    assert statementAdjust != null;

    if (statementAdjust.getLines().isEmpty()) {
      BStatementAdjustLine line = new BStatementAdjustLine();
      line.setInvoice(true);
      statementAdjust.getLines().add(line);
    }
    currentRow = 0;
    doRefresh(false);
  }

  public void setStatementAdjust(BStatementAdjust statementAdjust) {
    this.statementAdjust = statementAdjust;
    if (statementAdjust.getLines().isEmpty() == false) {
      accountDate = statementAdjust.getLines().get(0).getAccountDate();
    } else {
      accountDate = null;
    }
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public StatementAdjustLineEditGrid() {
    super();

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    Widget w = drawGrid();
    panel.add(w);

    initWidget(panel);
  }

  private BStatementAdjust statementAdjust;
  private int currentRow;
  private Date accountDate;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef invoiceCol;
  private RGridColumnDef accountDateCol;
  private RGridColumnDef beginDateCol;
  private RGridColumnDef endDateCol;
  private RGridColumnDef lastPayDateCol;
  private RGridColumnDef remarkCol;

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setProvider(new GridDataProvider());

    Handler_Grid handler = new Handler_Grid();
    grid.addHighlightHandler(handler);

    lineNumberCol = new RGridColumnDef(PStatementAdjustLineDef.lineNumber);
    lineNumberCol.setWidth("40px");
    lineNumberCol.setRendererFactory(new LineNumberRendererFactory());
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PStatementAdjustLineDef.subject_code);
    subjectCodeCol.setWidth("100px");
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PStatementAdjustLineDef.subject_name);
    subjectNameCol.setWidth("150px");
    grid.addColumnDef(subjectNameCol);

    directionCol = new RGridColumnDef(PStatementAdjustLineDef.direction);
    directionCol.setWidth("80px");
    grid.addColumnDef(directionCol);

    totalCol = new RGridColumnDef(PStatementAdjustLineDef.amount_total);
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    totalCol.setWidth("70px");
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PStatementAdjustLineDef.amount_tax);
    taxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    taxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    taxCol.setWidth("50px");
    grid.addColumnDef(taxCol);

    taxRateCol = new RGridColumnDef(PStatementAdjustLineDef.taxRate);
    taxRateCol.setWidth("100px");
    grid.addColumnDef(taxRateCol);

    invoiceCol = new RGridColumnDef(PStatementAdjustLineDef.invoice);
    invoiceCol.setRendererFactory(new RBooleanRendererFactory());
    invoiceCol.setHorizontalAlign(HasAlignment.ALIGN_CENTER);
    invoiceCol.setWidth("70px");
    grid.addColumnDef(invoiceCol);

    accountDateCol = new RGridColumnDef(PStatementAdjustLineDef.accountDate);
    accountDateCol.setWidth("100px");
    grid.addColumnDef(accountDateCol);

    beginDateCol = new RGridColumnDef(PStatementAdjustLineDef.dateRange_beginDate);
    beginDateCol.setWidth("100px");
    grid.addColumnDef(beginDateCol);

    endDateCol = new RGridColumnDef(PStatementAdjustLineDef.dateRange_endDate);
    endDateCol.setWidth("100px");
    grid.addColumnDef(endDateCol);

    lastPayDateCol = new RGridColumnDef(PStatementAdjustLineDef.lastPayDate);
    lastPayDateCol.setWidth("100px");
    grid.addColumnDef(lastPayDateCol);

    remarkCol = new RGridColumnDef(PStatementAdjustLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private void doRefresh(boolean isAdd) {
    setDefaultAccountDate();
    refreshLineNum();
    grid.refresh();
    grid.setCurrentRow(currentRow);
    RActionEvent.fire(StatementAdjustLineEditGrid.this, ActionName.REFRESH, "");
    RActionEvent.fire(StatementAdjustLineEditGrid.this, ActionName.CHANGE_LINE, new Integer(
        currentRow), new Boolean(isAdd));
  }

  private void setDefaultAccountDate() {
    for (BStatementAdjustLine line : statementAdjust.getLines()) {
      // 进入编辑页面，防止此字段为空
      if (line.getAccountDate() == null)
        line.setAccountDate(accountDate);
    }
  }

  private void refreshLineNum() {
    if (statementAdjust == null)
      return;
    for (int i = 0; i < statementAdjust.getLines().size(); i++) {
      BStatementAdjustLine line = statementAdjust.getLines().get(i);
      line.setLineNumber(i + 1);
    }
  }

  private void doBatchDelete() {
    List list = grid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(StatementAdjustMessages.M.seleteDataToAction(StatementAdjustMessages.M.delete(),
          StatementAdjustMessages.M.detailLines()));
      return;
    }
    doDeleteRows();
    doRefresh(false);
  }

  private void doAdd() {
    doAddLine();
    doRefresh(true);
  }

  private void doInsert() {
    doInsertLine();
    doRefresh(true);
  }

  /** 删除当前行，currentRow不变 */
  private void doDeleteCurRow() {
    statementAdjust.getLines().remove(currentRow);
    if (statementAdjust.getLines().size() == 0)
      doAddLine();
    else if (currentRow >= statementAdjust.getLines().size())
      currentRow--;
    doRefresh(true);
  }

  private void doDeleteRows() {
    List<Integer> selections = grid.getSelections();
    boolean deleteCurLine = false;
    for (int i = selections.size() - 1; i >= 0; i--) {
      Integer row = selections.get(i);
      int dataRow = row.intValue();
      statementAdjust.getLines().remove(dataRow);

      if (dataRow == currentRow)
        deleteCurLine = true;
    }

    if (statementAdjust.getLines().size() <= 0) {
      doAddLine();
    } else if (deleteCurLine) {
      currentRow = grid.getSelections().get(0).intValue();
      if (currentRow >= statementAdjust.getLines().size())
        currentRow--;
    } else {
      for (Integer row : grid.getSelections()) {
        if (row.intValue() < currentRow)
          currentRow--;
      }
    }
  }

  private BStatementAdjustLine doAddLine() {
    int size = statementAdjust.getLines().size();
    if (size == 0) {
      BStatementAdjustLine line = new BStatementAdjustLine();
      line.setInvoice(true);
      statementAdjust.getLines().add(line);
    } else {
      BStatementAdjustLine adjustLine = statementAdjust.getLines().get(size - 1);
      if (adjustLine.getSubject() != null && adjustLine.getSubject().getUuid() != null) {
        BStatementAdjustLine line = new BStatementAdjustLine();
        line.setInvoice(true);
        statementAdjust.getLines().add(line);
      }
    }

    currentRow = statementAdjust.getLines().size() - 1;
    return statementAdjust.getLines().get(currentRow);
  }

  private BStatementAdjustLine doInsertLine() {
    assert statementAdjust.getLines().size() != 0;

    BStatementAdjustLine currentLine = statementAdjust.getLines().get(currentRow);
    if (currentLine.getSubject() == null || currentLine.getSubject().getUuid() == null)
      return statementAdjust.getLines().get(currentRow);

    if (currentRow == 0) {
      BStatementAdjustLine line = new BStatementAdjustLine();
      line.setInvoice(true);
      statementAdjust.getLines().add(currentRow, line);
    } else {
      BStatementAdjustLine prevLine = statementAdjust.getLines().get(currentRow - 1);
      if (prevLine.getSubject() == null || prevLine.getSubject().getUuid() == null) {
        currentRow--;
        return statementAdjust.getLines().get(currentRow);
      }
      BStatementAdjustLine line = new BStatementAdjustLine();
      line.setInvoice(true);
      statementAdjust.getLines().add(currentRow, line);
    }
    return statementAdjust.getLines().get(currentRow);
  }

  private class GridDataProvider implements RGridDataProvider {
    @Override
    public int getRowCount() {
      return statementAdjust == null ? 0 : statementAdjust.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (statementAdjust == null || statementAdjust.getLines().isEmpty())
        return null;

      BStatementAdjustLine line = statementAdjust.getLines().get(row);
      if (col == lineNumberCol.getIndex())
        return line.getLineNumber() + "";
      else if (col == subjectCodeCol.getIndex())
        return line.getSubject() == null ? null : line.getSubject().getCode();
      else if (col == subjectNameCol.getIndex())
        return line.getSubject() == null ? null : line.getSubject().getName();
      else if (col == directionCol.getIndex())
        return DirectionType.getCaptionByValue(line.getDirection());
      else if (col == totalCol.getIndex())
        return Double.valueOf(line.getTotal().getTotal().doubleValue());
      else if (col == taxCol.getIndex())
        return Double.valueOf(line.getTotal().getTax().doubleValue());
      else if (col == taxRateCol.getIndex())
        return line.getTaxRate() == null ? null : line.getTaxRate().toString();
      else if (col == invoiceCol.getIndex())
        return line.isInvoice();
      else if (col == accountDateCol.getIndex())
        return line.getAccountDate() == null ? null : M3Format.fmt_yMd
            .format(line.getAccountDate());
      else if (col == beginDateCol.getIndex())
        return line.getBeginDate() == null ? null : M3Format.fmt_yMd.format(line.getBeginDate());
      else if (col == endDateCol.getIndex())
        return line.getEndDate() == null ? null : M3Format.fmt_yMd.format(line.getEndDate());
      else if (col == lastPayDateCol.getIndex())
        return line.getLastPayDate() == null ? null : M3Format.fmt_yMd
            .format(line.getLastPayDate());
      else if (col == remarkCol.getIndex())
        return line.getRemark();
      else
        return "";
    }
  }

  private class Handler_Grid implements HighlightHandler<Point> {

    @Override
    public void onHighlight(HighlightEvent<Point> event) {
      final int row = event.getHighlighted().getY();
      CommandQueue.offer(new LocalCommand() {

        @Override
        public void onCall(CommandQueue queue) {
          currentRow = row;
          RActionEvent.fire(StatementAdjustLineEditGrid.this, ActionName.CHANGE_LINE, new Integer(
              currentRow), Boolean.FALSE);
          queue.goon();
        }
      });
      CommandQueue.awake();
    }
  }
}
