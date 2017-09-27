/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayLineEditGrid.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.client.ui.gadget;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.deposit.commons.client.actionname.DepositActionName;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositTotal;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositLineDef;
import com.hd123.m3.account.gwt.deposit.payment.client.EPPayDeposit;
import com.hd123.m3.account.gwt.deposit.payment.client.rpc.PayDepositService;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
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
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenpeisi
 * 
 */
public class PayDepositLineEditGrid extends Composite implements RActionHandler, HasRActionHandlers {

  public PayDepositLineEditGrid() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    Widget wGrid = drawGrid();
    panel.add(wGrid);

    initWidget(panel);
  }

  public BDeposit getEntity() {
    return entity;
  }

  public void setEntity(BDeposit entity) {
    this.entity = entity;
  }

  private BDeposit entity;

  private int currentRow = 0;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef remainTotalCol;
  private RGridColumnDef contractTotalCol;
  private RGridColumnDef remarkCol;

  private RPopupMenu lineMenu;

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setHoverRow(true);
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setAllowHorizontalScrollBar(true);
    grid.setProvider(new GridDataProvider());
    Handler_grid handler = new Handler_grid();
    grid.addHighlightHandler(handler);
    grid.addClickHandler(handler);
    grid.addRefreshHandler(handler);

    lineNumberCol = new RGridColumnDef(PDepositLineDef.lineNumber);
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PDepositLineDef.subject_code);
    subjectCodeCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new RHyperlinkFieldRendererFactory()));
    subjectCodeCol.setWidth("120px");
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PDepositLineDef.subject_name);
    subjectNameCol.setWidth("160px");
    grid.addColumnDef(subjectNameCol);

    totalCol = new RGridColumnDef(PDepositLineDef.amount);
    totalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("100px");
    grid.addColumnDef(totalCol);

    remainTotalCol = new RGridColumnDef(PDepositLineDef.remainTotal);
    remainTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    remainTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    remainTotalCol.setWidth("100px");
    grid.addColumnDef(remainTotalCol);

    contractTotalCol = new RGridColumnDef(DepositMessage.M.contractTotal());
    contractTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    contractTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    contractTotalCol.setWidth("100px");
    grid.addColumnDef(contractTotalCol);

    remarkCol = new RGridColumnDef(PDepositLineDef.remark);
    remarkCol.setWidth("160px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  public void refresh(boolean focusOnFirst) {
    assert entity != null;

    if (entity.hasValues() == false) {
      BDepositLine line = new BDepositLine(entity);
      entity.getLines().clear();
      entity.getLines().add(line);
    }
    currentRow = 0;

    doRefresh(focusOnFirst);
  }

  private void doRefresh(boolean focusOnFirst) {
    refreshLineNumber();
    grid.refresh();
    grid.setCurrentRow(currentRow);
    RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.REFRESH);
    RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_LINE, new Integer(
        currentRow), new Boolean(focusOnFirst));
  }

  private void refreshLineNumber() {
    for (int i = 0; i < entity.getLines().size(); i++) {
      entity.getLines().get(i).setLineNumber(i + 1);
    }
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
        if (event.getActionName().equals(DepositActionName.PREV_LINE)) {
          currentRow = currentRow == 0 ? currentRow : currentRow - 1;
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_LINE,
              new Integer(currentRow), Boolean.FALSE);
        } else if (event.getActionName().equals(DepositActionName.IMPORT_FROM_CONTRACT)) {
          doImportFromContract();
        } else if (event.getActionName().equals(DepositActionName.ADD_LINE)) {
          doAdd();
        } else if (event.getActionName().equals(DepositActionName.INSERT_LINE)) {
          doInsert();
        } else if (event.getActionName().equals(DepositActionName.NEXT_LINE)) {
          currentRow = currentRow >= entity.getLines().size() - 1 ? currentRow : ++currentRow;
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_LINE,
              new Integer(currentRow), Boolean.FALSE);
        } else if (event.getActionName().equals(DepositActionName.CHANGE_VALUE)) {
          Integer row = (Integer) event.getParameters().get(0);
          grid.refreshRow(row);
        } else if (event.getActionName().equals(DepositActionName.DELETE_CURRENT_LINE)) {
          doDeleteCurRow();
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.REFRESH);
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_TOTAL);
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_LINE,
              Integer.valueOf(currentRow), Boolean.TRUE);
        } else if (event.getActionName().equals(DepositActionName.BATCH_DELETE_LINE)) {
          doBatchDelete();
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.REFRESH);
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_TOTAL);
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_LINE,
              Integer.valueOf(currentRow), Boolean.TRUE);
        } else if (event.getActionName() == DepositActionName.CHANGE_REMAINTOTAL) {
          doChangeRemainTotal();
          fetchLineExts();
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.REFRESH);
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_LINE,
              new Integer(currentRow), false);
        } else if (event.getActionName() == DepositActionName.ONSELECT_LINE) {
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.BEFORE_CHANGE_LINE, "");
          int row = (Integer) event.getParameters().get(0);
          doSelect(row);
          RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_LINE,
              Integer.valueOf(currentRow), Boolean.FALSE);
        }
      }
    });
    CommandQueue.awake();
  }

  private void doImportFromContract() {
    // 基本信息
    if (entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null
        || entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null) {
      RMsgBox.show(DepositMessage.M.fillBasicInfo());
      return;
    }
    // 先选择合同
    if (entity.getContract() == null || entity.getContract().getUuid() == null) {
      RMsgBox.show(DepositMessage.M.selectContractFirst());
      return;
    }

    RLoadingDialog.show();
    PayDepositService.Locator.getService().importFromContract(entity.getContract().getUuid(),
        entity.getAccountUnit().getUuid(), entity.getCounterpart().getUuid(),
        new RBAsyncCallback2<List<BDepositLine>>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.importFromContract(),
                EPPayDeposit.getInstance().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(List<BDepositLine> result) {
            RLoadingDialog.hide();
            if (result.isEmpty()) {
              RMsgBox.show(DepositMessage.M.noDepositTerm(entity.getContract().getCode()));
              return;
            }
            // 删除空行
            Set<String> subjectUuids = new HashSet<String>();
            for (int i = entity.getLines().size() - 1; i >= 0; i--) {
              BDepositLine line = entity.getLines().get(i);
              if (line.getSubject() == null || line.getSubject().getUuid() == null) {
                entity.getLines().remove(i);
              } else {
                subjectUuids.add(line.getSubject().getUuid());
              }
            }
            // 过滤重复科目，添加导入科目
            for (BDepositLine line : result) {
              if (subjectUuids.contains(line.getSubject().getUuid())) {
                continue;
              }
              entity.getLines().add(line);
            }
            refresh(true);
          }
        });
  }

  private void doAdd() {
    doAddLine();
    doRefresh(true);
  }

  private BDepositLine doAddLine() {
    int size = entity.getLines().size();
    if (size == 0) {
      BDepositLine line = new BDepositLine(entity);
      entity.getLines().add(line);
    } else {
      BDepositLine line = entity.getLines().get(size - 1);
      if (line.getSubject() != null
          && StringUtil.isNullOrBlank(line.getSubject().getUuid()) == false) {
        entity.getLines().add(new BDepositLine(entity));
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
      entity.getLines().add(currentRow, new BDepositLine(entity));
    } else {
      BDepositLine subject = entity.getLines().get(currentRow - 1);
      if (subject.getSubject() == null || StringUtil.isNullOrBlank(subject.getSubject().getUuid())) {
        currentRow--;
        return;
      }
      entity.getLines().add(currentRow, new BDepositLine(entity));
    }
    doRefresh(true);
  }

  /** 删除当前行，currentRow不变 */
  private void doDeleteCurRow() {
    entity.getLines().remove(currentRow);
    currentRow = entity.getLines().size() - 1;
    if (currentRow < 0)
      doAddLine();
    doRefresh(true);
  }

  private void doBatchDelete() {
    List list = grid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(DepositMessage.M.seleteDataToAction(DepositMessage.M.delete(),
          DepositMessage.M.line()));
      return;
    }
    doDeleteRows();
    doRefresh(false);
  }

  /** 删除数据行。如果当前行未被删除，则currentRow值仍取当前行索引；否则currentRow值取删除行中第一行的索引 */
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

  private void doChangeRemainTotal() {
    for (BDepositLine line : entity.getLines()) {
      line.setRemainTotal(BigDecimal.ZERO);
      line.setContractTotal(BigDecimal.ZERO);
    }
  }

  private void fetchLineExts() {
    if (entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null
        || entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null
        || entity.getLines().isEmpty())
      return;

    Set<String> subjectUuids = new HashSet<String>();
    for (BDepositLine line : entity.getLines()) {
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        continue;
      subjectUuids.add(line.getSubject().getUuid());
    }
    if (subjectUuids.isEmpty())
      return;

    String contractUuid = null;
    if (entity.getContract() != null)
      contractUuid = entity.getContract().getUuid();

    RLoadingDialog.show();
    GWTUtil.enableSynchronousRPC();
    PayDepositService.Locator.getService().getAdvances(entity.getAccountUnit().getUuid(),
        entity.getCounterpart().getUuid(), subjectUuids, contractUuid,
        new RBAsyncCallback2<Map<String, BDepositTotal>>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.fetch(),
                DepositMessage.M.referenceInfo());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Map<String, BDepositTotal> result) {
            RLoadingDialog.hide();
            for (BDepositLine line : entity.getLines()) {
              if (line.getSubject() == null || line.getSubject().getUuid() == null)
                continue;
              BDepositTotal value = result.get(line.getSubject().getUuid());
              line.setRemainTotal(value.getAdvanceTotal());
              if (value.getContractTotal() != null) {
                line.setContractTotal(value.getContractTotal());
                if (line.getTotal() == null || BigDecimal.ZERO.compareTo(line.getTotal()) == 0) {
                  line.setTotal(line.getUnDepositTotal());
                }
              }
            }
            refresh(false);
          }
        });
  }

  private void doSelect(int row) {
    currentRow = row;
    grid.setCurrentRow(currentRow);
    grid.refreshRow(currentRow);
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
        BDepositLine line = entity.getLines().get(cell.getRow());
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
            RActionEvent.fire(PayDepositLineEditGrid.this, DepositActionName.CHANGE_LINE,
                new Integer(currentRow), Boolean.FALSE);
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
          String msg = DepositMessage.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
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
        return entity.getLines().get(row).getTotal() == null ? null : entity.getLines().get(row)
            .getTotal().doubleValue();
      if (col == remainTotalCol.getIndex())
        return entity.getLines().get(row).getRemainTotal() == null ? null : entity.getLines()
            .get(row).getRemainTotal().doubleValue();
      if (col == contractTotalCol.getIndex())
        return entity.getLines().get(row).getContractTotal() == null ? null : entity.getLines()
            .get(row).getContractTotal().doubleValue();
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
