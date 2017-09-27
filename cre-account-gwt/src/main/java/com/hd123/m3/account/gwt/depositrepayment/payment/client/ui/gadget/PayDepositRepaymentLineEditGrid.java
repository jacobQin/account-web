/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositRepaymentLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.actionname.DepositRepaymentActionName;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentLineDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.form.RAbstractField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkFieldRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.util.MessageHelper;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositRepaymentLineEditGrid extends Composite implements RActionHandler,
    HasRActionHandlers {

  public PayDepositRepaymentLineEditGrid() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    Widget wGrid = drawGrid();
    panel.add(wGrid);

    initWidget(panel);
  }

  public BDepositRepayment getEntity() {
    return entity;
  }

  public void setEntity(BDepositRepayment entity) {
    this.entity = entity;
  }

  public void refresh(boolean focusOnFirst) {
    assert entity != null;

    if (entity.hasValues() == false) {
      BDepositRepaymentLine line = new BDepositRepaymentLine();
      entity.getLines().clear();
      entity.getLines().add(line);
    }
    currentRow = 0;

    doRefresh(focusOnFirst);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(final RActionEvent event) {
    GWTUtil.blurActiveElement();
    CommandQueue.offer(new LocalCommand() {

      @Override
      public void onCall(CommandQueue queue) {
        queue.goon();
        if (event.getActionName().equals(DepositRepaymentActionName.PREV_LINE)) {
          currentRow = currentRow == 0 ? currentRow : currentRow - 1;
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.CHANGE_LINE, new Integer(currentRow), Boolean.FALSE);
        } else if (event.getActionName().equals(DepositRepaymentActionName.NEXT_LINE)) {
          currentRow = currentRow >= entity.getLines().size() - 1 ? currentRow : ++currentRow;
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.CHANGE_LINE, new Integer(currentRow), Boolean.FALSE);
        } else if (event.getActionName().equals(DepositRepaymentActionName.CHANGE_VALUE)) {
          Integer row = (Integer) event.getParameters().get(0);
          grid.refreshRow(row);
        } else if (event.getActionName().equals(DepositRepaymentActionName.DELETE_CURRENT_LINE)) {
          doDeleteCurRow();
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.REFRESH);
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.CHANGE_REPAYMENTTOTAL);
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.CHANGE_LINE, Integer.valueOf(currentRow), Boolean.TRUE);
        } else if (event.getActionName().equals(DepositRepaymentActionName.BATCH_DELETE_LINE)) {
          doBatchDelete();
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.REFRESH);
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.CHANGE_REPAYMENTTOTAL);
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.CHANGE_LINE, Integer.valueOf(currentRow), Boolean.TRUE);
        } else if (event.getActionName().equals(DepositRepaymentActionName.ADD_LINE)) {
          doAdd();
        } else if (event.getActionName().equals(DepositRepaymentActionName.INSERT_LINE)) {
          doInsert();
        } else if (event.getActionName() == DepositRepaymentActionName.REFRESH) {
          refresh(true);
        } else if (event.getActionName() == DepositRepaymentActionName.ONSELECT_LINE) {
          int row = (Integer) event.getParameters().get(0);
          doSelect(row);
          RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.CHANGE_LINE, Integer.valueOf(currentRow), Boolean.FALSE);
        }
      }
    });
    CommandQueue.awake();
  }

  public void doSelect(int row) {
    currentRow = row;
    grid.setCurrentRow(currentRow);
    grid.refreshRow(currentRow);
  }

  private BDepositRepayment entity;

  private int currentRow = 0;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef remainTotalCol;
  private RGridColumnDef remarkCol;

  private RPopupMenu lineMenu;

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setHoverRow(true);
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setAllowHorizontalScrollBar(true);
    grid.setProvider(new GridDataProvider());
    Handler_grid handler = new Handler_grid();
    grid.addHighlightHandler(handler);
    grid.addClickHandler(handler);
    grid.addRefreshHandler(handler);

    lineNumberCol = new RGridColumnDef(PDepositRepaymentLineDef.lineNumber);
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PDepositRepaymentLineDef.subject_code);
    subjectCodeCol.setWidth("120px");
    subjectCodeCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new RHyperlinkFieldRendererFactory()));
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PDepositRepaymentLineDef.subject_name);
    subjectNameCol.setWidth("160px");
    grid.addColumnDef(subjectNameCol);

    totalCol = new RGridColumnDef(PDepositRepaymentLineDef.amount);
    totalCol.setWidth("120px");
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(totalCol);

    remainTotalCol = new RGridColumnDef("账户余额");
    remainTotalCol.setWidth("120px");
    remainTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    remainTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    grid.addColumnDef(remainTotalCol);

    remarkCol = new RGridColumnDef(PDepositRepaymentLineDef.remark);
    remarkCol.setWidth("160px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private void doRefresh(boolean focusOnFirst) {
    refreshLineNumber();
    grid.refresh();
    grid.setCurrentRow(currentRow);
    RActionEvent.fire(PayDepositRepaymentLineEditGrid.this, DepositRepaymentActionName.REFRESH);
    RActionEvent.fire(PayDepositRepaymentLineEditGrid.this, DepositRepaymentActionName.CHANGE_LINE,
        new Integer(currentRow), new Boolean(focusOnFirst));
  }

  private void refreshLineNumber() {
    for (int i = 0; i < entity.getLines().size(); i++) {
      entity.getLines().get(i).setLineNumber(i + 1);
    }
  }

  private void doAdd() {
    doAddLine();
    doRefresh(true);
  }

  private BDepositRepaymentLine doAddLine() {
    int size = entity.getLines().size();
    if (size == 0) {
      BDepositRepaymentLine line = new BDepositRepaymentLine();
      entity.getLines().add(line);
    } else {
      BDepositRepaymentLine l = entity.getLines().get(size - 1);
      if (l.getSubject() != null && l.getSubject().getCode() != null) {
        BDepositRepaymentLine line = new BDepositRepaymentLine();
        entity.getLines().add(line);
      }
    }
    currentRow = entity.getLines().size() - 1;
    return entity.getLines().get(currentRow);
  }

  private void doInsert() {
    if (entity.getLines().get(currentRow).getSubject() == null
        || StringUtil.isNullOrBlank(entity.getLines().get(currentRow).getSubject().getUuid())) {
      return;
    }
    if (currentRow == 0) {
      entity.getLines().add(currentRow, new BDepositRepaymentLine());
    } else {
      BDepositRepaymentLine subject = entity.getLines().get(currentRow - 1);
      if (subject.getSubject() == null || StringUtil.isNullOrBlank(subject.getSubject().getUuid())) {
        currentRow--;
        return;
      }
      entity.getLines().add(currentRow, new BDepositRepaymentLine());
    }
    doRefresh(true);
  }

  private void doDeleteCurRow() {
    entity.getLines().remove(currentRow);
    if (entity.getLines().isEmpty()) {
      doAddLine();
    } else if (currentRow >= entity.getLines().size()) {
      currentRow--;
    }
    doRefresh(true);
  }

  private void doBatchDelete() {
    List list = grid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(DepositRepaymentMessage.M.seleteDataToAction(DepositRepaymentMessage.M.delete(),
          DepositRepaymentMessage.M.line()));
      return;
    }
    doDeleteRows();
    doRefresh(false);
  }

  private void doDeleteRows() {
    List<Integer> selections = grid.getSelections();
    /** 是否删除当前行。此变量影响删除后的当前行值。 */
    boolean deleteCurLine = false;
    for (int i = selections.size() - 1; i >= 0; i--) {
      int row = selections.get(i);
      if (row == currentRow)
        deleteCurLine = true;

      entity.getLines().remove(row);
    }

    if (entity.getLines().isEmpty()) {
      doAddLine();
    } else if (deleteCurLine) {
      currentRow = grid.getSelections().get(0).intValue();
      if (currentRow >= entity.getLines().size())
        currentRow--;
    } else {
      int rowNumber = currentRow;
      for (Integer row : grid.getSelections()) {
        if (row < rowNumber)
          currentRow--;
      }
    }
  }

  private class Handler_grid implements ClickHandler, HighlightHandler<Point>,
      RefreshHandler<RGridCellInfo> {
    @Override
    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = event.getTarget();
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(lineNumberCol)) {
        BDepositRepaymentLine line = entity.getLines().get(cell.getRow());
        if (grid.getWidget(cell.getRow(), cell.getCol()) instanceof RAbstractField)
          MessageHelper.bindToField(line,
              (RAbstractField) grid.getWidget(cell.getRow(), cell.getCol()), true);
      }
    }

    @Override
    public void onHighlight(HighlightEvent<Point> event) {
      final int row = event.getHighlighted().getY();
      CommandQueue.offer(new LocalCommand() {

        @Override
        public void onCall(CommandQueue queue) {
          if (currentRow != row) {
            currentRow = row;
            RActionEvent.fire(PayDepositRepaymentLineEditGrid.this,
                DepositRepaymentActionName.CHANGE_LINE, new Integer(currentRow), Boolean.FALSE);
          }
          queue.goon();
        }
      });
      CommandQueue.awake();
    }

    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCodeCol)) {
        BUCN subject = entity.getLines().get(cell.getRow()).getSubject();
        if (subject == null || StringUtil.isNullOrBlank(subject.getUuid()))
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subject.getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = DepositRepaymentMessage.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    }
  }

  private class GridDataProvider implements RGridDataProvider {
    @Override
    public Object getData(int row, int col) {
      if (entity == null || entity.getLines().size() == 0)
        return null;
      if (col == lineNumberCol.getIndex())
        return Integer.valueOf(entity.getLines().get(row).getLineNumber());
      if (col == subjectCodeCol.getIndex())
        return entity.getLines().get(row).getSubject() == null ? null : entity.getLines().get(row)
            .getSubject().getCode();
      if (col == subjectNameCol.getIndex())
        return entity.getLines().get(row).getSubject() == null ? null : entity.getLines().get(row)
            .getSubject().getName();
      if (col == totalCol.getIndex())
        return entity.getLines().get(row).getAmount() == null ? null : entity.getLines().get(row)
            .getAmount().doubleValue();
      if (col == remainTotalCol.getIndex())
        return entity.getLines().get(row).getRemainAmount() == null ? null : entity.getLines()
            .get(row).getRemainAmount().doubleValue();
      if (col == remarkCol.getIndex())
        return entity.getLines().get(row).getRemark();
      return null;
    }

    @Override
    public int getRowCount() {
      return entity == null ? 0 : entity.getLines().size();
    }
  }

}
