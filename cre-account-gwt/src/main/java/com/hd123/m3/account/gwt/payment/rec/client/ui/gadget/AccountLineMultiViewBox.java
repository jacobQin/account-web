package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
import com.hd123.m3.account.gwt.payment.commons.client.PaymentGridCellStyleUtil;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptConfig;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

public class AccountLineMultiViewBox extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {
  /** 账款明细行 */
  private List<BPaymentAccountLine> lines;
  private int currentRow = -1;

  private Label countLabel;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef originTotalCol;
  private RGridColumnDef originTaxCol;
  private RGridColumnDef unpayedTotalCol;
  private RGridColumnDef unpayedTaxCol;
  private RGridColumnDef leftTotalCol;
  private RGridColumnDef defrayalTotalCol;
  private RGridColumnDef overdueTotalCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef remarkCol;

  private AccountLineViewGadget accountLineViewGadget;

  public void setLines(BPayment bill) {
    this.lines = bill.getAccountLines();
    accountLineViewGadget.setBill(bill);
  }

  public AccountLineMultiViewBox() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);
    panel.setStyleName(RCaptionBox.STYLENAME_STANDARD);

    drawGrid();
    panel.add(grid);
    accountLineViewGadget = new AccountLineViewGadget();
    panel.add(accountLineViewGadget);
    getCaptionBar().setShowCollapse(true);
    setContentSpacing(3);
    setWidth("100%");
    setCaption(PPaymentAccountLineDef.TABLE_CAPTION);
    setContent(panel);

    countLabel = new HTML(ReceiptMessages.M.resultTotal(0));
    getCaptionBar().addButton(RSimplePanel.decorateMargin(countLabel, 0, 0, 0, 10));
    addActionHandler();
  }

  private void addActionHandler() {
    accountLineViewGadget.addActionHandler(this);
    addActionHandler(accountLineViewGadget);
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setProvider(new GridDataProvider());
    Handler_grid handler = new Handler_grid();
    grid.addHighlightHandler(handler);
    grid.addClickHandler(handler);
    grid.addRefreshHandler(handler);

    lineNumberCol = new RGridColumnDef(PPaymentAccountLineDef.lineNumber);
    lineNumberCol.setWidth("35px");
    lineNumberCol.setResizable(true);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_subject);
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("130px"));
    subjectCol.setWidth("150px");
    subjectCol.setResizable(true);
    grid.addColumnDef(subjectCol);

    sourceBillNumberCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_sourceBill_billNumber);
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    sourceBillNumberCol.setResizable(true);
    grid.addColumnDef(sourceBillNumberCol);

    sourceBillTypeCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_sourceBill_billType);
    sourceBillTypeCol.setWidth("90px");
    sourceBillTypeCol.setResizable(true);
    grid.addColumnDef(sourceBillTypeCol);

    dateRangeCol = new RGridColumnDef(CommonMessages.M.dateRange());
    dateRangeCol.setWidth("150px");
    dateRangeCol.setResizable(true);
    grid.addColumnDef(dateRangeCol);

    taxRateCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_taxRate_rate);
    taxRateCol.setWidth("90px");
    taxRateCol.setResizable(true);
    grid.addColumnDef(taxRateCol);

    originTotalCol = new RGridColumnDef(ReceiptMessages.M.originTotal_total());
    originTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTotalCol.setWidth("65px");
    originTotalCol.setResizable(true);
    grid.addColumnDef(originTotalCol);

    originTaxCol = new RGridColumnDef(ReceiptMessages.M.originTotal_tax());
    originTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTaxCol.setWidth("65px");
    originTaxCol.setResizable(true);
    grid.addColumnDef(originTaxCol);

    unpayedTotalCol = new RGridColumnDef(PPaymentAccountLineDef.unpayedTotal_total);
    unpayedTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTotalCol.setWidth("90px");
    unpayedTotalCol.setResizable(true);
    grid.addColumnDef(unpayedTotalCol);

    unpayedTaxCol = new RGridColumnDef(PPaymentAccountLineDef.unpayedTotal_tax);
    unpayedTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTaxCol.setWidth("90px");
    unpayedTaxCol.setResizable(true);
    grid.addColumnDef(unpayedTaxCol);

    leftTotalCol = new RGridColumnDef(ReceiptMessages.M.leftTotal_total());
    leftTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    leftTotalCol.setWidth("65px");
    leftTotalCol.setResizable(true);
    grid.addColumnDef(leftTotalCol);

    defrayalTotalCol = new RGridColumnDef(PPaymentAccountLineDef.defrayalTotal);
    defrayalTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    defrayalTotalCol.setWidth("65px");
    defrayalTotalCol.setResizable(true);
    grid.addColumnDef(defrayalTotalCol);

    overdueTotalCol = new RGridColumnDef(PPaymentAccountLineDef.overdueTotal_total);
    overdueTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTotalCol.setWidth("50px");
    overdueTotalCol.setResizable(true);
    grid.addColumnDef(overdueTotalCol);

    invoiceCodeCol = new RGridColumnDef(ReceiptMessages.M.invoiceCode());
    invoiceCodeCol.setWidth("80px");
    invoiceCodeCol.setResizable(true);
    grid.addColumnDef(invoiceCodeCol);

    invoiceNumberCol = new RGridColumnDef(ReceiptMessages.M.invoiceNumber());
    invoiceNumberCol.setWidth("80px");
    invoiceNumberCol.setResizable(true);
    grid.addColumnDef(invoiceNumberCol);

    remarkCol = new RGridColumnDef(PPaymentAccountLineDef.remark);
    remarkCol.setWidth("90px");
    remarkCol.setResizable(true);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    grid.setAllowHorizontalScrollBar(true);
  }

  public void refreshData() {
    if (lines.size() > 0) {
      currentRow = 0;
    } else {
      currentRow = -1;
    }
    countLabel.setText(ReceiptMessages.M.resultTotal(lines.size()));
    doRefresh();
  }

  /**
   * @param showDefrayalCol
   *          是否显示实收金额列
   */
  public void refreshGrid() {
    BReceiptConfig config = EPReceipt.getInstance().getConfig();
    boolean showTax = config.isShowTax();
    taxRateCol.setVisible(showTax);
    originTaxCol.setVisible(showTax);
    unpayedTaxCol.setVisible(showTax);

    grid.rebuild();
  }

  private void doRefresh() {
    grid.refresh();
    grid.setCurrentRow(currentRow);
    RActionEvent.fire(this, ActionName.ACTION_CHANGELINENO, Integer.valueOf(currentRow));
  }

  private class GridDataProvider implements RGridDataProvider {
    @Override
    public int getRowCount() {
      return lines == null ? 0 : lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (lines.size() == 0)
        return null;

      BPaymentAccountLine line = lines.get(row);
      if (col == lineNumberCol.getIndex())
        return line.getLineNumber();
      else if (col == subjectCol.getIndex())
        return line.getAcc1().getSubject() == null ? null : line.getAcc1().getSubject()
            .toFriendlyStr();
      else if (col == sourceBillNumberCol.getIndex()) {
        if (line.getAcc1().getSourceBill() == null) {
          return null;
        }
        BDispatch dispatch = new BDispatch(line.getAcc1().getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), line.getAcc1().getSourceBill().getBillNumber());
        return dispatch;
      } else if (col == dateRangeCol.getIndex())
        return buildDateRange(line.getAcc1().getBeginTime(), line.getAcc1().getEndTime());
      else if (col == taxRateCol.getIndex())
        return line.getAcc1().getTaxRate() == null ? null : line.getAcc1().getTaxRate().toString();
      else if (col == originTotalCol.getIndex())
        return line.getOriginTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getOriginTotal().getTotal().doubleValue());
      else if (col == originTaxCol.getIndex())
        return line.getOriginTotal().getTax() == null ? null : GWTFormat.fmt_money.format(line
            .getOriginTotal().getTax().doubleValue());
      else if (col == unpayedTotalCol.getIndex())
        return line.getUnpayedTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getUnpayedTotal().getTotal().doubleValue());
      else if (col == unpayedTaxCol.getIndex())
        return line.getUnpayedTotal().getTax() == null ? null : GWTFormat.fmt_money.format(line
            .getUnpayedTotal().getTax().doubleValue());
      else if (col == leftTotalCol.getIndex())
        return line.getLeftTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getLeftTotal().getTotal().doubleValue());
      else if (col == defrayalTotalCol.getIndex())
        return line.getDefrayalTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getDefrayalTotal().doubleValue());
      else if (col == overdueTotalCol.getIndex())
        return line.getOverdueTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getOverdueTotal().getTotal().doubleValue());
      else if (col == sourceBillTypeCol.getIndex()) {
        BSourceBill sourceBill = line.getAcc1().getSourceBill();
        if (sourceBill == null || sourceBill.getBillType() == null) {
          return null;
        }

        BBillType type = EPReceipt.getInstance().getBillTypeMap()
            .get(line.getAcc1().getSourceBill().getBillType());
        return type == null ? null : type.getCaption();
      } else if (col == remarkCol.getIndex())
        return line.getRemark();
      else if (col == invoiceCodeCol.getIndex()) {
        return line.getInvoiceCodeStr();
      } else if (col == invoiceNumberCol.getIndex()) {
        return line.getInvoiceNumberStr();
      }
      return null;
    }
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

  private class Handler_grid implements ClickHandler, HighlightHandler<Point>,
      RefreshHandler<RGridCellInfo> {
    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCol)) {
        BUCN subject = lines.get(cell.getRow()).getAcc1().getSubject();
        if (subject == null || StringUtil.isNullOrBlank(subject.getUuid()))
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subject.getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = ReceiptMessages.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    }

    @Override
    public void onHighlight(HighlightEvent<Point> event) {
      final int row = event.getHighlighted().getY();
      currentRow = row;
      RActionEvent.fire(AccountLineMultiViewBox.this, ActionName.ACTION_CHANGELINENO,
          Integer.valueOf(currentRow));
    }

    @Override
    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      if (lines.isEmpty()) {
        return;
      }
      RGridCellInfo cell = event.getTarget();
      BPaymentAccountLine rowData = lines.get(cell.getRow());
      PaymentGridCellStyleUtil.refreshCellStye(cell, rowData.getAcc1().getDirection(),DirectionType.receipt);
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == AccountLineViewGadget.ActionName.ACTION_PREV) {
      if (currentRow > 0) {
        RActionEvent.fire(this, ActionName.ACTION_CHANGELINENO, Integer.valueOf(--currentRow));
        grid.setCurrentRow(currentRow);
      }
    } else if (event.getActionName() == AccountLineViewGadget.ActionName.ACTION_NEXT) {
      if (currentRow < lines.size() - 1) {
        RActionEvent.fire(this, ActionName.ACTION_CHANGELINENO, Integer.valueOf(++currentRow));
        grid.setCurrentRow(currentRow);
      }
    }
  }

  public static class ActionName {
    /** 换行 */
    public static final String ACTION_CHANGELINENO = "changeLineNo";
  }

}
