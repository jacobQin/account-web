/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeLineViewGrid.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeLine;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeLineDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCheckBoxRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RDateTimeRendererFactory;
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
 * 费用单明细查看Grid(View页面)
 * 
 * @author subinzhu
 * 
 */
public class FeeLineViewGrid extends Composite {
  private BFee bill;

  private RGrid grid;
  private HTML resultTotalHtml;

  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef beginDateCol;
  private RGridColumnDef endDateCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef issueInvoiceCol;
  private RGridColumnDef remarkCol;

  public BFee getBill() {
    return bill;
  }

  public void setBill(BFee bill) {
    this.bill = bill;
    resultTotalHtml.setHTML(CommonsMessages.M
        .resultTotal((bill == null || bill.getLines().isEmpty()) ? 0 : bill.getLines().size()));
  }

  public void refresh() {
    grid.refresh();
  }

  public FeeLineViewGrid() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    panel.add(drawGrid());

    RCaptionBox box = new RCaptionBox();
    box.setContentSpacing(0);
    box.setCaption(PFeeLineDef.TABLE_CAPTION);
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

    lineNumberCol = new RGridColumnDef(PFeeLineDef.lineNumber);
    lineNumberCol.setWidth("40px");
    lineNumberCol.setName("lineNumber");
    lineNumberCol.setRendererFactory(new RHTMLFieldRendererFactory());
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PFeeLineDef.subject_code);
    subjectCodeCol.setRendererFactory(new RHyperlinkRendererFactory());
    subjectCodeCol.setWidth("80px");
    subjectCodeCol.setName("subject.code");
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PFeeLineDef.subject_name);
    subjectNameCol.setWidth("100px");
    subjectNameCol.setName("subject.name");
    grid.addColumnDef(subjectNameCol);

    beginDateCol = new RGridColumnDef(PFeeDef.dateRange_beginDate);
    beginDateCol.setWidth("120px");
    beginDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    grid.addColumnDef(beginDateCol);

    endDateCol = new RGridColumnDef(PFeeDef.dateRange_endDate);
    endDateCol.setWidth("120px");
    endDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    grid.addColumnDef(endDateCol);

    totalCol = new RGridColumnDef(PFeeLineDef.total_total);
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    totalCol.setWidth("80px");
    totalCol.setName("total.total");
    grid.addColumnDef(totalCol);

    taxRateCol = new RGridColumnDef(PFeeLineDef.taxRate);
    taxRateCol.setWidth("100px");
    taxRateCol.setName("taxRate");
    grid.addColumnDef(taxRateCol);

    taxCol = new RGridColumnDef(PFeeLineDef.total_tax);
    taxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    taxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    taxCol.setWidth("80px");
    taxCol.setName("total.tax");
    grid.addColumnDef(taxCol);

    issueInvoiceCol = new RGridColumnDef(PFeeLineDef.issueInvoice);
    issueInvoiceCol.setWidth("60px");
    issueInvoiceCol.setHorizontalAlign(HorizontalPanel.ALIGN_CENTER);
    issueInvoiceCol.setRendererFactory(new CheckBoxRendererFactory());
    grid.addColumnDef(issueInvoiceCol);

    remarkCol = new RGridColumnDef(PFeeLineDef.remark);
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

      BFeeLine line = bill.getLines().get(row);

      if (col == lineNumberCol.getIndex())
        return line.getLineNumber() + "";
      else if (col == subjectCodeCol.getIndex())
        return line.getSubject() == null ? null : line.getSubject().getCode();
      else if (col == subjectNameCol.getIndex())
        return line.getSubject() == null ? null : line.getSubject().getName();
      else if (col == beginDateCol.getIndex())
        return line.getDateRange().getBeginDate();
      else if (col == endDateCol.getIndex())
        return line.getDateRange().getEndDate();
      else if (col == totalCol.getIndex())
        return Double.valueOf(line.getTotal().getValue().doubleValue());
      else if (col == taxRateCol.getIndex())
        return line.getTaxRate().caption();
      else if (col == taxCol.getIndex())
        return line.getTax();
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

      BFeeLine line = bill.getLines().get(row);
      if (line == null)
        return;
      if (colDef.equals(subjectCodeCol)) {
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, line.getSubject().getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
        } catch (Exception e) {
          String msg = FeeMessages.M.navigateError(url.getUrl());
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
