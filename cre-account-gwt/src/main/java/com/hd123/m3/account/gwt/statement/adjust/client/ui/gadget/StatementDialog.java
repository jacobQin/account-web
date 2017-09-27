/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-7 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.statement.adjust.client.EPStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatement;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatementLine;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustService;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustLineDef;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author zhuhairui
 * 
 */
public class StatementDialog extends RDialog {

  public StatementDialog() {
    super();
    drawSelf();
  }

  public void setSourceAccountIds(List<String> accIds) {
    this.accIds = accIds;
  }

  public List<QStatementLine> getSelections() {
    List<Integer> selections = grid.getSelections();
    List<QStatementLine> lines = new ArrayList<QStatementLine>();
    for (Integer selection : selections) {
      QStatementLine statementLine = entity.getLines().get(selection.intValue());
      lines.add(statementLine);
    }
    return lines;
  }

  public QStatement getQStatement() {
    return entity;
  }

  public void doLoad(String uuid) {

    RLoadingDialog.show(StatementAdjustMessages.M.loading());
    StatementAdjustService.Locator.getService().getStatementByUuid(uuid,
        new RBAsyncCallback2<QStatement>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.load(),
                StatementAdjustMessages.M.statement());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(QStatement result) {
            RLoadingDialog.hide();
            entity = result;
            for (String accId : accIds) {
              for (QStatementLine line : entity.getLines()) {
                if (line.getAccId().equals(accId)) {
                  entity.getLines().remove(line);
                  break;
                }
              }
            }
            grid.refresh();
          }
        });
  }

  public void addOKButtonHandler(ClickHandler handler) {
    okButton.addClickHandler(handler);
  }

  private QStatement entity;
  private EPStatementAdjust ep = EPStatementAdjust.getInstance();
  private List<String> accIds;

  private RGrid grid;
  private RGridColumnDef subjectCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillCol;

  private RToolbarButton okButton;

  private void drawSelf() {
    setCaptionText(StatementAdjustMessages.M.selectData2(StatementAdjustMessages.M.statement2()));
    getCaptionBar().setStyleName(RTextStyles.STYLE_BOLD);

    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    setWidget(root);

    RToolbar toolbar = new RToolbar();
    toolbar.setAutoShrink(false);
    root.add(toolbar);

    okButton = new RToolbarButton(RActionFacade.OK);
    toolbar.add(okButton);

    toolbar.add(new RToolbarButton(RActionFacade.CANCEL, new ClickHandler() {
      public void onClick(ClickEvent event) {
        hide();
      }
    }));

    setEscKeyToHide(true);
    setShowCloseButton(true);
    setWidth("700px");

    root.add(drawGrid());
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setProvider(new GridDataProvider());

    subjectCol = new RGridColumnDef(PStatementAdjustLineDef.subject);
    subjectCol.setWidth("100px");
    grid.addColumnDef(subjectCol);

    directionCol = new RGridColumnDef(PStatementAdjustLineDef.direction);
    directionCol.setWidth("60px");
    grid.addColumnDef(directionCol);

    totalCol = new RGridColumnDef(PStatementAdjustLineDef.amount);
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    totalCol.setWidth("50px");
    grid.addColumnDef(totalCol);

    dateRangeCol = new RGridColumnDef(PStatementAdjustLineDef.dateRange);
    dateRangeCol.setWidth("120px");
    grid.addColumnDef(dateRangeCol);

    sourceBillTypeCol = new RGridColumnDef(StatementAdjustMessages.M.sourceBillType());
    sourceBillTypeCol.setWidth("80px");
    grid.addColumnDef(sourceBillTypeCol);

    sourceBillCol = new RGridColumnDef(StatementAdjustMessages.M.sourceBill());
    sourceBillCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillCol.setWidth("140px");
    grid.addColumnDef(sourceBillCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return entity == null ? 0 : entity.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entity == null || entity.getLines().isEmpty())
        return null;

      QStatementLine rowDate = entity.getLines().get(row);
      if (col == subjectCol.getIndex())
        return rowDate.getSubject().getNameCode();
      else if (col == directionCol.getIndex())
        return DirectionType.getCaptionByValue(rowDate.getDirection());
      else if (col == totalCol.getIndex())
        return Double.valueOf(rowDate.getTotal().doubleValue());
      else if (col == dateRangeCol.getIndex())
        return rowDate.getDateRange();
      else if (col == sourceBillTypeCol.getIndex())
        return rowDate.getSourceBill() != null ? rowDate.getSourceBill().getBillType() != null ? ep
            .getBillType().get(rowDate.getSourceBill().getBillType()) : null : null;
      else if (col == sourceBillCol.getIndex()) {
        if (rowDate.getSourceBill() == null) {
          return null;
        }

        BDispatch dispatch = new BDispatch(rowDate.getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), rowDate.getSourceBill().getBillNumber());
        return dispatch;
      }
      return null;
    }
  }

}
