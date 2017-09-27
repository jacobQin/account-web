/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeLineViewGrid.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-20 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.internalfee.client.InternalFeeMessages;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFeeLine;
import com.hd123.m3.account.gwt.internalfee.intf.client.dd.PInternalFeeLineDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCheckBoxRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHTMLFieldRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 内部费用单明细查看Grid(View页面)
 * 
 * @author liuguilin
 * 
 */
public class InternalFeeLineViewGrid extends Composite {
  private BInternalFee bill;

  private RGrid grid;
  private HTML resultTotalHtml;

  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef issueInvoiceCol;
  private RGridColumnDef remarkCol;

  public BInternalFee getBill() {
    return bill;
  }

  public void refresh(BInternalFee bill) {
    this.bill = bill;
    resultTotalHtml.setHTML(CommonsMessages.M.resultTotal((bill == null || bill.getLines()
        .isEmpty()) ? 0 : bill.getLines().size()));
    grid.refresh();
  }

  public InternalFeeLineViewGrid() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    panel.add(drawGrid());

    RCaptionBox box = new RCaptionBox();
    box.setContentSpacing(0);
    box.setCaption(PInternalFeeLineDef.TABLE_CAPTION);
    box.setContent(panel);
    box.getCaptionBar().setShowCollapse(true);
    box.setWidth("100%");

    resultTotalHtml = new HTML(CommonsMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(resultTotalHtml);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    this.initWidget(box);
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");

    Handler_grid handler = new Handler_grid();
    grid.addClickHandler(handler);

    lineNumberCol = new RGridColumnDef(PInternalFeeLineDef.lineNumber);
    lineNumberCol.setWidth("40px");
    lineNumberCol.setName("lineNumber");
    lineNumberCol.setRendererFactory(new RHTMLFieldRendererFactory());
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PInternalFeeLineDef.subject_code);
    subjectCodeCol.setRendererFactory(new RHyperlinkRendererFactory());
    subjectCodeCol.setWidth("80px");
    subjectCodeCol.setName("subject.code");
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PInternalFeeLineDef.subject_name);
    subjectNameCol.setWidth("100px");
    subjectNameCol.setName("subject.name");
    grid.addColumnDef(subjectNameCol);

    totalCol = new RGridColumnDef(PInternalFeeLineDef.total_total);
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    totalCol.setWidth("80px");
    totalCol.setName("total.total");
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PInternalFeeLineDef.total_tax);
    taxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    taxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    taxCol.setWidth("80px");
    taxCol.setName("total.tax");
    grid.addColumnDef(taxCol);

    taxRateCol = new RGridColumnDef(PInternalFeeLineDef.taxRate_rate);
    taxRateCol.setWidth("120px");
    taxRateCol.setName("total.taxDef");
    grid.addColumnDef(taxRateCol);

    issueInvoiceCol = new RGridColumnDef(PInternalFeeLineDef.issueInvoice);
    issueInvoiceCol.setWidth("60px");
    issueInvoiceCol.setHorizontalAlign(HorizontalPanel.ALIGN_CENTER);
    issueInvoiceCol.setRendererFactory(new CheckBoxRendererFactory());
    grid.addColumnDef(issueInvoiceCol);

    remarkCol = new RGridColumnDef(PInternalFeeLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setProvider(new GridDataProvider());
    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return bill == null ? 0 : bill.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (bill == null || bill.getLines().size() == 0)
        return null;

      BInternalFeeLine line = bill.getLines().get(row);

      if (col == lineNumberCol.getIndex())
        return String.valueOf(row + 1);
      else if (col == subjectCodeCol.getIndex())
        return line.getSubject() == null ? null : line.getSubject().getCode();
      else if (col == subjectNameCol.getIndex())
        return line.getSubject() == null ? null : line.getSubject().getName();
      else if (col == totalCol.getIndex())
        return Double.valueOf(line.getTotal().getTotal().doubleValue());
      else if (col == taxCol.getIndex())
        return Double.valueOf(line.getTotal().getTax().doubleValue());
      else if (col == taxRateCol.getIndex())
        return line.getTaxRate() == null ? null : line.getTaxRate().caption();
      else if (col == issueInvoiceCol.getIndex())
        return line.isIssueInvoice();
      else if (col == remarkCol.getIndex())
        return line.getRemark();
      return null;
    }
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      RGridColumnDef colDef = cell.getColumnDef();
      int row = cell.getRow();

      BInternalFeeLine line = bill.getLines().get(row);
      if (line == null)
        return;
      if (colDef.equals(subjectCodeCol)) {
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, line.getSubject().getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
        } catch (Exception e) {
          String msg = InternalFeeMessages.M.navigateError(url.getUrl());
          RMsgBox.showError(msg, e);
        }
      }
    }
  }

  private class CheckBoxRendererFactory implements RCellRendererFactory<Boolean> {
    @Override
    public RCellRenderer<Boolean> makeRenderer(RGrid grid, RGridColumnDef colDef, int row, int col,
        Boolean data) {
      RCheckBoxRenderer rc = new RCheckBoxRenderer();
      rc.setReadOnly(true);
      rc.setValue(data);
      return rc;
    }
  }

}
