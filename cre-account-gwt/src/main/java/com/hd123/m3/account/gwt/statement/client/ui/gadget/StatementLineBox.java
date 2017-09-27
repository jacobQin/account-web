/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： StatementLineGrid.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-29 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.hd123.m3.account.gwt.acc.client.BAccountSourceType;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementLineDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRenderer;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHTMLFieldRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHTMLFieldRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkFieldRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkFieldRendererFactory;
import com.hd123.rumba.gwt.widget2.client.misc.HasTextStyle;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;

/**
 * 账款信息CaptionBox
 * 
 * @author huangjunxian
 * 
 */
public class StatementLineBox extends RCaptionBox {
  private static final String HPERYLINK_STYLENAME = "rb-Hyperlink";

  public StatementLineBox() {
    super();
    drawSelf();
  }

  private EPStatement ep = EPStatement.getInstance();
  private List<BStatementLine> lines;
  private String billNumber;

  private RGrid grid;

  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef freeTotalCol;
  private RGridColumnDef rateCol;
  private RGridColumnDef invoiceCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef lastPayDateCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillNumberCol;

  private void drawSelf() {
    drawGrid();

    setContentSpacing(0);
    setCaption(StatementMessages.M.accountInfo());
    setWidth("100%");
    setContent(grid);
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setProvider(new GridDataProvider());
    grid.addClickHandler(new Handler_grid());
    grid.addRefreshHandler(new RefreshHandler<RGridCellInfo>() {

      @Override
      public void onRefresh(RefreshEvent<RGridCellInfo> event) {
        doRefresh(event);
      }
    });

    subjectCodeCol = new RGridColumnDef(PStatementLineDef.acc1_subject_code);
    subjectCodeCol.setRendererFactory(new RHyperlinkFieldRendererFactory());
    subjectCodeCol.setWidth("65px");
    subjectCodeCol.setResizable(true);
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PStatementLineDef.acc1_subject_name);
    subjectNameCol.setRendererFactory(new RHTMLFieldRendererFactory());
    subjectNameCol.setWidth("80px");
    subjectNameCol.setResizable(true);
    grid.addColumnDef(subjectNameCol);

    directionCol = new RGridColumnDef(PStatementLineDef.acc1_direction);
    directionCol.setRendererFactory(new RHTMLFieldRendererFactory());
    directionCol.setWidth("50px");
    directionCol.setResizable(true);
    grid.addColumnDef(directionCol);

    totalCol = new RGridColumnDef(PStatementLineDef.total_total);
    totalCol.setRendererFactory(new RHTMLFieldRendererFactory());
    totalCol.setWidth("60px");
    totalCol.setResizable(true);
    totalCol.setHorizontalAlign(HasAlignment.ALIGN_RIGHT);
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PStatementLineDef.total_tax);
    taxCol.setRendererFactory(new RHTMLFieldRendererFactory());
    taxCol.setWidth("60px");
    taxCol.setResizable(true);
    taxCol.setHorizontalAlign(HasAlignment.ALIGN_RIGHT);
    grid.addColumnDef(taxCol);

    freeTotalCol = new RGridColumnDef(PStatementLineDef.freeTotal_total);
    freeTotalCol.setRendererFactory(new RHTMLFieldRendererFactory());
    freeTotalCol.setWidth("60px");
    freeTotalCol.setResizable(true);
    freeTotalCol.setHorizontalAlign(HasAlignment.ALIGN_RIGHT);
    grid.addColumnDef(freeTotalCol);

    rateCol = new RGridColumnDef(PStatementLineDef.acc1_taxRate);
    rateCol.setRendererFactory(new RHTMLFieldRendererFactory());
    rateCol.setWidth("65px");
    rateCol.setResizable(true);
    grid.addColumnDef(rateCol);

    invoiceCol = new RGridColumnDef(StatementMessages.M.isInvoiced());
    invoiceCol.setRendererFactory(new RHTMLFieldRendererFactory());
    invoiceCol.setWidth("50px");
    invoiceCol.setResizable(true);
    grid.addColumnDef(invoiceCol);

    dateRangeCol = new RGridColumnDef(StatementMessages.M.dateRange());
    dateRangeCol.setRendererFactory(new RHTMLFieldRendererFactory());
    dateRangeCol.setWidth("110px");
    dateRangeCol.setResizable(true);
    grid.addColumnDef(dateRangeCol);

    lastPayDateCol = new RGridColumnDef(PStatementLineDef.acc2_lastPayDate);
    lastPayDateCol.setWidth("90px");
    lastPayDateCol.setResizable(true);
    grid.addColumnDef(lastPayDateCol);

    sourceBillTypeCol = new RGridColumnDef(PStatementLineDef.acc1_sourceBill_billType);
    sourceBillTypeCol.setRendererFactory(new RHTMLFieldRendererFactory());
    sourceBillTypeCol.setWidth("100px");
    sourceBillTypeCol.setResizable(true);
    grid.addColumnDef(sourceBillTypeCol);

    sourceBillNumberCol = new RGridColumnDef(PStatementLineDef.acc1_sourceBill.getCaption());
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
  }

  private String buildTotalStr(BigDecimal value) {
    if (value == null)
      return null;
    return M3Format.fmt_money.format(value.doubleValue());
  }

  private String buildDateRange(Date beginDate, Date endDate) {
    if (beginDate == null && endDate == null)
      return null;
    StringBuffer sb = new StringBuffer();
    if (beginDate != null)
      sb.append(M3Format.fmt_yMd.format(beginDate));
    sb.append("~");
    if (endDate != null)
      sb.append(M3Format.fmt_yMd.format(endDate));
    return sb.toString();
  }

  private void doRefresh(RefreshEvent<RGridCellInfo> event) {
    RGridCellInfo cell = event.getTarget();
    if (cell == null)
      return;
    BStatementLine line = lines.get(cell.getRow());
    if (BAccountSourceType.liquidate.equals(line.getAccSrcType())
        || BAccountSourceType.maxSubject.equals(line.getAccSrcType())
        || BAccountSourceType.amountLimit.equals(line.getAccSrcType())) {
      if (cell.getWidget() instanceof RHTMLFieldRenderer) {
        ((RHTMLFieldRenderer) cell.getWidget()).addTextStyleName(RTextStyles.STYLE_BOLD);
        ((RHTMLFieldRenderer) cell.getWidget()).addTextStyleName(RTextStyles.STYLE_RED);
      } else if (cell.getWidget() instanceof RHyperlinkFieldRenderer) {
        ((RHyperlinkFieldRenderer) cell.getWidget()).removeTextStyleName(HPERYLINK_STYLENAME);
        ((RHyperlinkFieldRenderer) cell.getWidget()).addTextStyleName(RTextStyles.STYLE_BOLD);
        ((RHyperlinkFieldRenderer) cell.getWidget()).addTextStyleName(RTextStyles.STYLE_RED);
      } else if (cell.getWidget() instanceof DispatchLinkRenderer) {
        ((DispatchLinkRenderer) cell.getWidget()).removeTextStyleName(HPERYLINK_STYLENAME);
        ((DispatchLinkRenderer) cell.getWidget()).addTextStyleName(RTextStyles.STYLE_BOLD);
        ((DispatchLinkRenderer) cell.getWidget()).addTextStyleName(RTextStyles.STYLE_RED);
      } else if (cell.getWidget() instanceof HTMLField) {
        ((HTMLField) cell.getWidget()).addTextStyleName(RTextStyles.STYLE_BOLD);
        ((HTMLField) cell.getWidget()).addTextStyleName(RTextStyles.STYLE_RED);
      }
    } else {
      if (cell.getWidget() instanceof RHTMLFieldRenderer) {
        ((RHTMLFieldRenderer) cell.getWidget()).removeTextStyleName(RTextStyles.STYLE_BOLD);
        ((RHTMLFieldRenderer) cell.getWidget()).removeTextStyleName(RTextStyles.STYLE_RED);
      } else if (cell.getWidget() instanceof RHyperlinkFieldRenderer) {
        ((RHyperlinkFieldRenderer) cell.getWidget()).addTextStyleName(HPERYLINK_STYLENAME);
        ((RHyperlinkFieldRenderer) cell.getWidget()).removeTextStyleName(RTextStyles.STYLE_BOLD);
        ((RHyperlinkFieldRenderer) cell.getWidget()).removeTextStyleName(RTextStyles.STYLE_RED);
      } else if (cell.getWidget() instanceof DispatchLinkRenderer) {
        ((DispatchLinkRenderer) cell.getWidget()).addTextStyleName(HPERYLINK_STYLENAME);
        ((DispatchLinkRenderer) cell.getWidget()).removeTextStyleName(RTextStyles.STYLE_BOLD);
        ((DispatchLinkRenderer) cell.getWidget()).removeTextStyleName(RTextStyles.STYLE_RED);
      } else if (cell.getWidget() instanceof HTMLField) {
        ((HTMLField) cell.getWidget()).removeTextStyleName(RTextStyles.STYLE_BOLD);
        ((HTMLField) cell.getWidget()).removeTextStyleName(RTextStyles.STYLE_RED);
      }
    }
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      if (lines == null)
        return 0;
      else
        return lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (lines == null)
        return null;
      BStatementLine line = lines.get(row);
      if (line == null || line.getAcc1() == null)
        return null;
      if (col == subjectCodeCol.getIndex())
        return line.getAcc1().getSubject() == null ? null : line.getAcc1().getSubject().getCode();
      else if (col == subjectNameCol.getIndex())
        return line.getAcc1().getSubject() == null ? null : line.getAcc1().getSubject().getName();
      else if (col == directionCol.getIndex())
        return DirectionType.getCaptionByValue(line.getAcc1().getDirection());
      else if (col == totalCol.getIndex())
        return line.getTotal() == null ? null : buildTotalStr(line.getTotal().getTotal());
      else if (col == taxCol.getIndex())
        return line.getTotal() == null ? null : buildTotalStr(line.getTotal().getTax());
      else if (col == freeTotalCol.getIndex())
        return line.getFreeTotal() == null ? null : buildTotalStr(line.getFreeTotal().getTotal());
      else if (col == rateCol.getIndex())
        return line.getAcc1().getTaxRate() == null ? null : line.getAcc1().getTaxRate().toString();
      else if (col == invoiceCol.getIndex())
        return line.getInvoice() ? StatementMessages.M.yes() : StatementMessages.M.no();
      else if (col == dateRangeCol.getIndex())
        return buildDateRange(line.getAcc1().getBeginTime(), line.getAcc1().getEndTime());
      else if (col == lastPayDateCol.getIndex()) {
        return line.getAcc2().getLastPayDate() == null ? null : M3Format.fmt_yMd.format(line
            .getAcc2().getLastPayDate());
      } else if (col == sourceBillTypeCol.getIndex())
        return line.getAcc1().getSourceBill() == null ? null : ep.getBillType().get(
            line.getAcc1().getSourceBill().getBillType());
      else if (col == sourceBillNumberCol.getIndex()) {
        if (line.getAcc1().getSourceBill() == null) {
          return null;
        }

        BDispatch dispatch = new BDispatch(line.getAcc1().getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), line.getAcc1().getSourceBill().getBillNumber());
        return dispatch;
      }
      return null;
    }
  }

  private class HTMLField extends Composite implements RCellRenderer<BDispatch>, HasTextStyle {
    private int gridRow;
    private int gridColumn;
    private RHTMLField field;

    public HTMLField() {
      super();
      field = new RHTMLField();
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

    @Override
    public void addTextStyleName(String style) {
      field.addTextStyleName(style);
    }

    @Override
    public void removeTextStyleName(String style) {
      field.removeTextStyleName(style);
    }

  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BStatementLine line = lines.get(cell.getRow());
      if (line == null)
        return;

      if (cell.getColumnDef().equals(subjectCodeCol)) {
        StatementSubjectAccInfoDialog.getInstance().show(billNumber, line);
      }
    }
  }

  public void setValue(String billNumber, List<BStatementLine> lines) {
    this.billNumber = billNumber;
    this.lines = lines;
    refresh();
  }

  public void refresh() {
    grid.rebuild();
  }

}