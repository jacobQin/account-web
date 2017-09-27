/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
import com.hd123.m3.account.gwt.payment.commons.client.PaymentGridCellStyleUtil;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineCashDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDepositDef;
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
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 账款明细查看表格
 * 
 * @author subinzhu
 * 
 */
public class AccountLineViewBox extends RCaptionBox {

  /** 账款明细行 */
  private List<BPaymentAccountLine> lines;

  private Label countLabel;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef originTotalCol;
  private RGridColumnDef originTaxCol;
  private RGridColumnDef unpayedTotalCol;
  private RGridColumnDef unpayedTaxCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef leftTotalCol;
  private RGridColumnDef defrayalTotalCol;
  private RGridColumnDef overdueTotalCol;
  private RGridColumnDef linePaymentTypeCol;
  private RGridColumnDef lineTotalCol;
  private RGridColumnDef lineBankCol;
  private RGridColumnDef lineGenDepSubjectCol;
  private RGridColumnDef lineGenDepTotalCol;
  private RGridColumnDef lineDepSubjectCol;
  private RGridColumnDef lineDepTotalCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef remarkCol;

  public void setLines(List<BPaymentAccountLine> lines) {
    this.lines = lines;
  }

  public AccountLineViewBox() {
    super();
    setWidth("100%");
    drawGrid();
    setContentSpacing(3);
    setCaption(PPaymentAccountLineDef.TABLE_CAPTION);
    setContent(grid);
    getCaptionBar().setShowCollapse(true);
    countLabel = new HTML(ReceiptMessages.M.resultTotal(0));
    getCaptionBar().addButton(RSimplePanel.decorateMargin(countLabel, 0, 0, 0, 10));
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setProvider(new GridDataProvider());

    Handler_grid handler = new Handler_grid();

    grid.addClickHandler(handler);
    grid.addRefreshHandler(handler);

    lineNumberCol = new RGridColumnDef(PPaymentAccountLineDef.lineNumber);
    lineNumberCol.setWidth("35px");
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_subject);
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("110px"));
    subjectCol.setWidth("130px");
    grid.addColumnDef(subjectCol);

    sourceBillNumberCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_sourceBill_billNumber);
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    grid.addColumnDef(sourceBillNumberCol);

    sourceBillTypeCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_sourceBill_billType);
    sourceBillTypeCol.setWidth("90px");
    grid.addColumnDef(sourceBillTypeCol);

    dateRangeCol = new RGridColumnDef(CommonMessages.M.dateRange());
    dateRangeCol.setWidth("100px");
    grid.addColumnDef(dateRangeCol);

    taxRateCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_taxRate_rate);
    taxRateCol.setWidth("80px");
    grid.addColumnDef(taxRateCol);

    originTotalCol = new RGridColumnDef(ReceiptMessages.M.originTotal_total());
    originTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTotalCol.setWidth("65px");
    grid.addColumnDef(originTotalCol);

    originTaxCol = new RGridColumnDef(ReceiptMessages.M.originTotal_tax());
    originTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTaxCol.setWidth("65px");
    grid.addColumnDef(originTaxCol);

    unpayedTotalCol = new RGridColumnDef(PPaymentAccountLineDef.unpayedTotal_total);
    unpayedTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTotalCol.setWidth("90px");
    grid.addColumnDef(unpayedTotalCol);

    unpayedTaxCol = new RGridColumnDef(PPaymentAccountLineDef.unpayedTotal_tax);
    unpayedTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTaxCol.setWidth("90px");
    grid.addColumnDef(unpayedTaxCol);

    totalCol = new RGridColumnDef(PPaymentAccountLineDef.total_total);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("90px");
    grid.addColumnDef(totalCol);

    leftTotalCol = new RGridColumnDef(ReceiptMessages.M.leftTotal_total());
    leftTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    leftTotalCol.setWidth("65px");
    grid.addColumnDef(leftTotalCol);

    defrayalTotalCol = new RGridColumnDef(PPaymentAccountLineDef.defrayalTotal);
    defrayalTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    defrayalTotalCol.setWidth("65px");
    grid.addColumnDef(defrayalTotalCol);

    overdueTotalCol = new RGridColumnDef(PPaymentAccountLineDef.overdueTotal_total);
    overdueTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTotalCol.setWidth("50px");
    grid.addColumnDef(overdueTotalCol);

    linePaymentTypeCol = new RGridColumnDef(PPaymentLineCashDef.paymentType);
    linePaymentTypeCol.setWidth("70px");
    grid.addColumnDef(linePaymentTypeCol);

    lineTotalCol = new RGridColumnDef(PPaymentLineCashDef.total);
    lineTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    lineTotalCol.setWidth("70px");
    grid.addColumnDef(lineTotalCol);

    lineBankCol = new RGridColumnDef(PPaymentLineCashDef.bankAccount);
    lineBankCol.setWidth("70px");
    grid.addColumnDef(lineBankCol);

    lineGenDepSubjectCol = new RGridColumnDef("产生预存款科目");
    lineGenDepSubjectCol.setWidth("100px");
    grid.addColumnDef(lineGenDepSubjectCol);

    lineGenDepTotalCol = new RGridColumnDef("产生预存款");
    lineGenDepTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    lineGenDepTotalCol.setWidth("100px");
    grid.addColumnDef(lineGenDepTotalCol);

    lineDepSubjectCol = new RGridColumnDef(PPaymentLineDepositDef.subject);
    lineDepSubjectCol.setWidth("70px");
    grid.addColumnDef(lineDepSubjectCol);

    lineDepTotalCol = new RGridColumnDef(PPaymentLineDepositDef.total_deposit);
    lineDepTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    lineDepTotalCol.setWidth("90px");
    grid.addColumnDef(lineDepTotalCol);

    invoiceCodeCol = new RGridColumnDef(ReceiptMessages.M.invoiceCode());
    invoiceCodeCol.setWidth("80px");
    grid.addColumnDef(invoiceCodeCol);

    invoiceNumberCol = new RGridColumnDef(ReceiptMessages.M.invoiceNumber());
    invoiceNumberCol.setWidth("80px");
    grid.addColumnDef(invoiceNumberCol);

    remarkCol = new RGridColumnDef(PPaymentAccountLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    grid.setAllowHorizontalScrollBar(true);
    grid.setAllColumnsResizable(true);
  }

  public void refreshData() {
    countLabel.setText(ReceiptMessages.M.resultTotal(lines.size()));
    doRefresh();
  }

  /**
   * @param showDefrayalCol
   *          是否显示实收金额列
   */
  public void refreshGrid(String defrayalType) {
    if (CPaymentDefrayalType.bill.equals(defrayalType)) {
      defrayalTotalCol.setVisible(false);
      totalCol.setVisible(true);
      linePaymentTypeCol.setVisible(false);
      lineTotalCol.setVisible(false);
      lineBankCol.setVisible(false);
      lineDepSubjectCol.setVisible(false);
      lineDepTotalCol.setVisible(false);
      lineGenDepSubjectCol.setVisible(false);
      lineGenDepTotalCol.setVisible(false);
    } else if (!CPaymentDefrayalType.bill.equals(defrayalType)) {
      defrayalTotalCol.setVisible(true);
      totalCol.setVisible(false);
      linePaymentTypeCol.setVisible(true);
      lineTotalCol.setVisible(true);
      lineBankCol.setVisible(true);
      lineDepSubjectCol.setVisible(true);
      lineDepTotalCol.setVisible(true);
      lineGenDepSubjectCol.setVisible(true);
      lineGenDepTotalCol.setVisible(true);
    }

    BReceiptConfig config = EPReceipt.getInstance().getConfig();
    boolean showTax = config.isShowTax();
    taxRateCol.setVisible(showTax);
    originTaxCol.setVisible(showTax);
    unpayedTaxCol.setVisible(showTax);

    grid.rebuild();
  }

  private void doRefresh() {
    grid.refresh();
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
      if (col == lineNumberCol.getIndex()) {
        return line.getLineNumber();
      } else if (col == subjectCol.getIndex()) {
        return line.getAcc1().getSubject() == null ? null : line.getAcc1().getSubject()
            .toFriendlyStr();
      } else if (col == sourceBillNumberCol.getIndex()) {
        if (line.getAcc1().getSourceBill() == null) {
          return null;
        }

        BDispatch dispatch = new BDispatch(line.getAcc1().getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), line.getAcc1().getSourceBill().getBillNumber());
        return dispatch;
      } else if (col == dateRangeCol.getIndex()) {
        return buildDateRange(line.getAcc1().getBeginTime(), line.getAcc1().getEndTime());
      } else if (col == taxRateCol.getIndex()) {
        return line.getAcc1().getTaxRate() == null ? null : line.getAcc1().getTaxRate().toString();
      } else if (col == originTotalCol.getIndex()) {
        return line.getOriginTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getOriginTotal().getTotal().doubleValue());
      } else if (col == originTaxCol.getIndex()) {
        return line.getOriginTotal().getTax() == null ? null : GWTFormat.fmt_money.format(line
            .getOriginTotal().getTax().doubleValue());
      } else if (col == unpayedTotalCol.getIndex()) {
        return line.getUnpayedTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getUnpayedTotal().getTotal().doubleValue());
      } else if (col == unpayedTaxCol.getIndex()) {
        return line.getUnpayedTotal().getTax() == null ? null : GWTFormat.fmt_money.format(line
            .getUnpayedTotal().getTax().doubleValue());
      } else if (col == totalCol.getIndex()) {
        return line.getTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getTotal().getTotal().doubleValue());
      } else if (col == leftTotalCol.getIndex()) {
        return line.getLeftTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getLeftTotal().getTotal().doubleValue());
      } else if (col == defrayalTotalCol.getIndex()) {
        return line.getDefrayalTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getDefrayalTotal().doubleValue());
      } else if (col == overdueTotalCol.getIndex()) {
        return line.getOverdueTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getOverdueTotal().getTotal().doubleValue());
      } else if (col == linePaymentTypeCol.getIndex()) {
        return line.getCashs().size() == 0 ? null
            : (line.getCashs().get(0).getPaymentType() == null ? null : line.getCashs().get(0)
                .getPaymentType().toFriendlyStr());
      } else if (col == lineTotalCol.getIndex()) {
        return line.getCashs().size() == 0 ? null
            : (line.getCashs().get(0).getTotal() == null ? null : GWTFormat.fmt_money.format(line
                .getCashs().get(0).getTotal().doubleValue()));
      } else if (col == lineBankCol.getIndex()) {
        return line.getCashs().size() == 0 ? null
            : (line.getCashs().get(0).getBank() == null ? null : line.getCashs().get(0).getBank()
                .toFriendlyStr());
      } else if (col == lineDepSubjectCol.getIndex()) {
        return line.getDeposits().size() == 0 ? null
            : (line.getDeposits().get(0).getSubject() == null ? null : line.getDeposits().get(0)
                .getSubject().toFriendlyStr());
      } else if (col == lineDepTotalCol.getIndex()) {
        return line.getDeposits().size() == 0 ? null
            : (line.getDeposits().get(0).getTotal() == null ? null : GWTFormat.fmt_money
                .format(line.getDeposits().get(0).getTotal().doubleValue()));
      } else if (col == sourceBillTypeCol.getIndex()) {
        BSourceBill sourceBill = line.getAcc1().getSourceBill();
        if (sourceBill == null || sourceBill.getBillType() == null) {
          return null;
        }

        BBillType type = EPReceipt.getInstance().getBillTypeMap()
            .get(line.getAcc1().getSourceBill().getBillType());
        return type == null ? null : type.getCaption();
      } else if (col == remarkCol.getIndex()) {
        return line.getRemark();
      } else if (col == invoiceCodeCol.getIndex()) {
        return line.getInvoiceCodeStr();
      } else if (col == invoiceNumberCol.getIndex()) {
        return line.getInvoiceNumberStr();
      } else if (col == lineGenDepSubjectCol.getIndex()) {
        return line.getDepositSubject() == null ? null : line.getDepositSubject().toFriendlyStr();
      } else if (col == lineGenDepTotalCol.getIndex()) {
        return GWTFormat.fmt_money.format(line.getDepositTotal());
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

  private class Handler_grid implements ClickHandler, RefreshHandler<RGridCellInfo> {
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

    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      if (lines.isEmpty()) {
        return;
      }
      RGridCellInfo cell = event.getTarget();
      BPaymentAccountLine rowData = lines.get(cell.getRow());
      PaymentGridCellStyleUtil.refreshCellStye(cell, rowData.getAcc1().getDirection(),
          DirectionType.receipt);

    }

  }

}
