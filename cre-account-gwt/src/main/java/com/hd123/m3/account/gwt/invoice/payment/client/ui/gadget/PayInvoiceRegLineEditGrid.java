/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegLineGrid.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.client.ui.gadget;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.invoice.commons.client.actionname.InvoiceRegActionName;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLine;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegLineDef;
import com.hd123.m3.account.gwt.invoice.payment.client.EPPayInvoiceReg;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenpeisi
 * 
 */
public class PayInvoiceRegLineEditGrid extends Composite implements RActionHandler,
    HasRActionHandlers {

  private BInvoiceReg entity;

  private int currentRow = -1;

  private RGrid grid;

  public PayInvoiceRegLineEditGrid() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    drawGrid();
    panel.add(grid);

    initWidget(panel);
  }

  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef unregTotalCol;
  private RGridColumnDef unregTaxCol;
  private RGridColumnDef regTotalCol;
  private RGridColumnDef regTaxCol;
  private RGridColumnDef remarkCol;

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setProvider(new GridDataProvider());
    Handler_grid handler = new Handler_grid();
    grid.addClickHandler(handler);
    grid.addHighlightHandler(handler);

    lineNumberCol = new RGridColumnDef(PInvoiceRegLineDef.constants.lineNumber());
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PInvoiceRegLineDef.constants.acc1_subject());
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("140px"));
    subjectCol.setWidth("160px");
    grid.addColumnDef(subjectCol);

    sourceBillNumberCol = new RGridColumnDef(
        PInvoiceRegLineDef.constants.acc1_sourceBill_billNumber());
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth("150px");
    grid.addColumnDef(sourceBillNumberCol);

    sourceBillTypeCol = new RGridColumnDef(PInvoiceRegLineDef.constants.acc1_sourceBill_billType());
    sourceBillTypeCol.setWidth("90px");
    grid.addColumnDef(sourceBillTypeCol);

    unregTotalCol = new RGridColumnDef(PInvoiceRegLineDef.constants.unregTotal_total());
    unregTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unregTotalCol.setWidth("90px");
    grid.addColumnDef(unregTotalCol);

    unregTaxCol = new RGridColumnDef(PInvoiceRegLineDef.constants.unregTotal_tax());
    unregTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unregTaxCol.setWidth("90px");
    grid.addColumnDef(unregTaxCol);

    regTotalCol = new RGridColumnDef(PInvoiceRegLineDef.constants.total_total());
    regTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    regTotalCol.setWidth("90px");
    grid.addColumnDef(regTotalCol);

    regTaxCol = new RGridColumnDef(PInvoiceRegLineDef.constants.total_tax());
    regTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    regTaxCol.setWidth("90px");
    grid.addColumnDef(regTaxCol);

    remarkCol = new RGridColumnDef(PInvoiceRegLineDef.constants.remark());
    remarkCol.setWidth("90px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == InvoiceRegActionName.PREV_LINE) {
      if (currentRow > 0) {
        RActionEvent.fire(this, InvoiceRegActionName.CHANGE_LINE, Integer.valueOf(--currentRow),
            Boolean.FALSE);
        grid.setCurrentRow(currentRow);
      }
    } else if (event.getActionName() == InvoiceRegActionName.NEXT_LINE) {
      if (currentRow < entity.getLines().size() - 1) {
        RActionEvent.fire(this, InvoiceRegActionName.CHANGE_LINE, Integer.valueOf(++currentRow),
            Boolean.FALSE);
        grid.setCurrentRow(currentRow);
      }
    } else if (event.getActionName() == InvoiceRegActionName.REFRESH_LINE) {
      refresh();
    } else if (event.getActionName() == InvoiceRegActionName.ONSELECT_LINE) {
      int row = (Integer) event.getParameters().get(0);
      doSelect(row);
      RActionEvent.fire(this, InvoiceRegActionName.CHANGE_LINE, Integer.valueOf(currentRow),
          Boolean.FALSE);
    } else if (event.getActionName() == InvoiceRegActionName.CHANGE_LINE_VALUE) {
      grid.refreshRow(currentRow);
      RActionEvent.fire(this, InvoiceRegActionName.AGGREGATE_LINE);
    } else if (event.getActionName() == InvoiceRegActionName.DELETE_CURRENT_LINE) {
      doDeleteCurRow();
      RActionEvent.fire(this, InvoiceRegActionName.REFRESH_LINE);
      RActionEvent.fire(this, InvoiceRegActionName.AGGREGATE_LINE);
      RActionEvent.fire(this, InvoiceRegActionName.CHANGE_LINE, Integer.valueOf(currentRow),
          Boolean.TRUE);
    } else if (event.getActionName() == InvoiceRegActionName.BATCH_DELETE_LINE) {
      doBatchDelete();
      RActionEvent.fire(this, InvoiceRegActionName.REFRESH_LINE);
      RActionEvent.fire(this, InvoiceRegActionName.AGGREGATE_LINE);
      RActionEvent.fire(this, InvoiceRegActionName.CHANGE_LINE, Integer.valueOf(currentRow),
          Boolean.TRUE);
    }
  }

  public void setEntity(BInvoiceReg entity) {
    this.entity = entity;
  }

  private void doSelect(int row) {
    currentRow = row;
    grid.setCurrentRow(currentRow);
    grid.refreshRow(currentRow);
  }

  public void refresh() {
    currentRow = 0;
    if (entity.getLines().size() == 0) {
      BInvoiceRegLine line = new BInvoiceRegLine();
      entity.getLines().add(line);
    }
    doRefresh(false);
  }

  private void doRefresh(boolean focusOnFirst) {
    refreshLineNumber();
    grid.refresh();
    grid.setCurrentRow(currentRow);
    RActionEvent.fire(this, InvoiceRegActionName.REFRESH_LINE);
    RActionEvent.fire(this, InvoiceRegActionName.AGGREGATE_LINE);
    RActionEvent.fire(this, InvoiceRegActionName.CHANGE_LINE, Integer.valueOf(currentRow),
        Boolean.valueOf(focusOnFirst));
  }

  public void doBatchDelete() {
    List list = grid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(InvoiceRegMessage.M.seleteDataToAction(InvoiceRegMessage.M.delete(),
          InvoiceRegMessage.M.line()));
      return;
    }
    doDeleteRows();
    doRefresh(true);
  }

  private void doDeleteCurRow() {
    entity.getLines().remove(currentRow);
    currentRow = entity.getLines().size() - 1;
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
      entity.getLines().remove(dataRow);

      if (dataRow == currentRow)
        deleteCurLine = true;
    }

    if (deleteCurLine)
      currentRow = entity.getLines().size() - 1;
    else {
      for (Integer row : grid.getSelections()) {
        if (row.intValue() < currentRow)
          currentRow--;
      }
    }
    if (currentRow < 0)
      doAddLine();
  }

  private BInvoiceRegLine doAddLine() {
    int size = entity.getLines().size();
    if (size == 0) {
      BInvoiceRegLine line = new BInvoiceRegLine();
      entity.getLines().add(line);
    } else {
      BInvoiceRegLine l = entity.getLines().get(size - 1);
      if (l.getAcc1().getSourceBill() != null
          && l.getAcc1().getSourceBill().getBillNumber() != null) {
        BInvoiceRegLine line = new BInvoiceRegLine();
        entity.getLines().add(line);
      }
    }
    currentRow = entity.getLines().size() - 1;
    return entity.getLines().get(currentRow);
  }

  private void refreshLineNumber() {
    for (int i = 0; i < entity.getLines().size(); i++) {
      BInvoiceRegLine srcbill = entity.getLines().get(i);
      srcbill.setLineNumber(i + 1);
    }
  }

  private class Handler_grid implements ClickHandler, HighlightHandler<Point> {
    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCol)) {
        BUCN subject = entity.getLines().get(cell.getRow()).getAcc1().getSubject();
        if (subject == null || StringUtil.isNullOrBlank(subject.getUuid()))
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subject.getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = InvoiceRegMessage.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    }

    public void onHighlight(HighlightEvent<Point> event) {
      final int row = event.getHighlighted().getY();
      CommandQueue.offer(new LocalCommand() {

        @Override
        public void onCall(CommandQueue queue) {
          if (currentRow != row) {
            currentRow = row;
            RActionEvent.fire(PayInvoiceRegLineEditGrid.this, InvoiceRegActionName.CHANGE_LINE,
                new Integer(currentRow), Boolean.FALSE);
          }
          queue.goon();
        }
      });
      CommandQueue.awake();
    }
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public Object getData(int row, int col) {
      if (entity == null || entity.getLines().size() == 0)
        return null;

      BInvoiceRegLine line = entity.getLines().get(row);

      switch (col) {
      case 0:
        return Integer.valueOf(line.getLineNumber());
      case 1:
        return line.getAcc1().getSubject() == null ? null : line.getAcc1().getSubject()
            .toFriendlyStr();
      case 2: {
        return getDispatch(line);
      }
      case 3: {
        return getBillTypeCaption(line);
      }
      case 4:
        return line.getUnregTotal() == null || line.getUnregTotal().getTotal() == null ? null
            : GWTFormat.fmt_money.format(line.getUnregTotal().getTotal().doubleValue());
      case 5:
        return line.getUnregTotal() == null || line.getUnregTotal().getTotal() == null ? null
            : GWTFormat.fmt_money.format(line.getUnregTotal().getTax().doubleValue());
      case 6:
        return line.getRegTotal() == null || line.getRegTotal().getTotal() == null ? null
            : GWTFormat.fmt_money.format(line.getRegTotal().getTotal().doubleValue());
      case 7:
        return line.getRegTotal() == null || line.getRegTotal().getTax() == null ? null
            : GWTFormat.fmt_money.format(line.getRegTotal().getTax().doubleValue());
      case 8:
        return line.getRemark();
      default:
        return null;
      }
    }

    public int getRowCount() {
      return entity == null ? 0 : entity.getLines().size();
    }

    private BDispatch getDispatch(BInvoiceRegLine line) {
      if (line.getAcc1().getSourceBill() == null) {
        return null;
      }

      BDispatch dispatch = new BDispatch(line.getAcc1().getSourceBill().getBillType());
      dispatch.addParams(GRes.R.dispatch_key(), line.getAcc1().getSourceBill().getBillNumber());
      return dispatch;
    }

    private String getBillTypeCaption(BInvoiceRegLine line) {
      if (line.getAcc1().getSourceBill() == null
          || line.getAcc1().getSourceBill().getBillType() == null)
        return null;
      BBillType billType = EPPayInvoiceReg.getInstance().getBillTypeMap()
          .get(line.getAcc1().getSourceBill().getBillType());
      if (billType == null)
        return null;
      return billType.getCaption();
    }
  }
}
