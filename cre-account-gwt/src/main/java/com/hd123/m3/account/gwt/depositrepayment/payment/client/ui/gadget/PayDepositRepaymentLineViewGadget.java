/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositRepaymentLineViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentLineDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkFieldRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositRepaymentLineViewGadget extends RCaptionBox {

  public PayDepositRepaymentLineViewGadget() {
    drawSelf();
  }

  public void refresh(BDepositRepayment pay) {
    assert pay != null;
    this.entity = pay;

    grid.refresh();
  }

  public void refreshCommands() {
    remainTotalCol.setVisible(BBizStates.INEFFECT.equals(entity.getBizState()));
    grid.rebuild();
  }

  private BDepositRepayment entity;

  private Label captionCount;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef remainTotalCol;
  private RGridColumnDef remarkCol;

  private RPopupMenu lineMenu;

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");

    vp.add(drawGrid());

    captionCount = new Label(DepositRepaymentMessage.M.resultTotal(0));
    setCaption(DepositRepaymentMessage.M.repaymentLines());
    setWidth("100%");
    setContent(vp);

    getCaptionBar().addButton(RSimplePanel.decoratePadding(captionCount, 0, 10, 0, 10));
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(false);
    grid.setHoverRow(true);
    grid.setProvider(new GridDataProvider());
    grid.addLoadDataHandler(new LoadDataHandler() {

      @Override
      public void onLoadData(LoadDataEvent event) {
        if (entity == null || entity.getLines() == null)
          return;
        captionCount.setText(DepositRepaymentMessage.M.resultTotal(entity.getLines().size()));
      }
    });
    grid.addClickHandler(new Handler_grid());

    lineNumberCol = new RGridColumnDef(PDepositRepaymentLineDef.lineNumber);
    lineNumberCol.setWidth("50px");
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PDepositRepaymentLineDef.subject_code);
    subjectCodeCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new RHyperlinkFieldRendererFactory()));
    subjectCodeCol.setWidth("120px");
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PDepositRepaymentLineDef.subject_name);
    subjectNameCol.setWidth("160px");
    grid.addColumnDef(subjectNameCol);

    totalCol = new RGridColumnDef(PDepositRepaymentLineDef.amount);
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("120px");
    grid.addColumnDef(totalCol);

    remainTotalCol = new RGridColumnDef("账户余额");
    remainTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    remainTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    remainTotalCol.setWidth("120px");
    grid.addColumnDef(remainTotalCol);

    remarkCol = new RGridColumnDef(PDepositRepaymentLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCodeCol)) {
        BDepositRepaymentLine line = entity.getLines().get(grid.getCurrentRow());
        if (line == null)
          return;
        if (line.getSubject() == null || StringUtil.isNullOrBlank(line.getSubject().getUuid()))
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, line.getSubject().getUuid());
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
    public int getRowCount() {
      if (entity == null || entity.getLines() == null) {
        return 0;
      }
      return entity.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entity.getLines() == null || entity.getLines().isEmpty())
        return null;
      if (col == lineNumberCol.getIndex()) {
        return entity.getLines().get(row).getLineNumber();
      } else if (col == subjectCodeCol.getIndex()) {
        return entity.getLines().get(row).getSubject().getCode();
      } else if (col == subjectNameCol.getIndex()) {
        return entity.getLines().get(row).getSubject().getName();
      } else if (col == remarkCol.getIndex()) {
        return entity.getLines().get(row).getRemark();
      } else if (col == totalCol.getIndex()) {
        return entity.getLines().get(row).getAmount() == null ? null : entity.getLines().get(row)
            .getAmount().doubleValue();
      } else if (col == remainTotalCol.getIndex()) {
        return entity.getLines().get(row).getRemainAmount() == null ? null : entity.getLines()
            .get(row).getRemainAmount().doubleValue();
      }
      return null;
    }
  }

}
