/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.gadget;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
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
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef originTotalCol;
  private RGridColumnDef originTaxCol;
  private RGridColumnDef unpayedTotalCol;
  private RGridColumnDef unpayedTaxCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef leftTotalCol;
  private RGridColumnDef overdueTotalCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef remarkCol;

  public void setLines(List<BPaymentAccountLine> lines) {
    this.lines = lines;
  }

  public AccountLineViewBox() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);
    panel.setStyleName(RCaptionBox.STYLENAME_STANDARD);

    panel.add(drawGrid());

    setContentSpacing(3);
    setCaption(PPaymentAccountLineDef.TABLE_CAPTION);
    setContent(grid);

    countLabel = new HTML(PaymentMessages.M.resultTotal(0));
    getCaptionBar().addButton(RSimplePanel.decorateMargin(countLabel, 0, 0, 0, 10));
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setAllowHorizontalScrollBar(true);
    grid.setProvider(new GridDataProvider());
    grid.addClickHandler(new Handler_grid());

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

    originTotalCol = new RGridColumnDef(PaymentMessages.M.originTotal_total());
    originTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTotalCol.setWidth("65px");
    originTotalCol.setResizable(true);
    grid.addColumnDef(originTotalCol);

    originTaxCol = new RGridColumnDef(PaymentMessages.M.originTotal_tax());
    originTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTaxCol.setWidth("65px");
    originTaxCol.setResizable(true);
    grid.addColumnDef(originTaxCol);

    unpayedTotalCol = new RGridColumnDef(PaymentMessages.M.unpayedTotal_total());
    unpayedTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTotalCol.setWidth("90px");
    unpayedTotalCol.setResizable(true);
    grid.addColumnDef(unpayedTotalCol);

    unpayedTaxCol = new RGridColumnDef(PaymentMessages.M.unpayedTotal_tax());
    unpayedTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTaxCol.setWidth("90px");
    unpayedTaxCol.setResizable(true);
    grid.addColumnDef(unpayedTaxCol);

    totalCol = new RGridColumnDef(PPaymentAccountLineDef.total_total);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("90px");
    totalCol.setResizable(true);
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PPaymentAccountLineDef.total_tax);
    taxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    taxCol.setWidth("90px");
    taxCol.setResizable(true);
    grid.addColumnDef(taxCol);

    leftTotalCol = new RGridColumnDef(PaymentMessages.M.leftTotal_total());
    leftTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    leftTotalCol.setWidth("65px");
    leftTotalCol.setResizable(true);
    grid.addColumnDef(leftTotalCol);

    overdueTotalCol = new RGridColumnDef(PPaymentAccountLineDef.overdueTotal_total);
    overdueTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTotalCol.setWidth("50px");
    overdueTotalCol.setResizable(true);
    grid.addColumnDef(overdueTotalCol);

    remarkCol = new RGridColumnDef(PPaymentAccountLineDef.remark);
    remarkCol.setWidth("90px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  public void refreshGrid() {
    boolean showTax = EPPayment.getInstance().getConfig().isShowTax();
    taxRateCol.setVisible(showTax);
    originTaxCol.setVisible(showTax);
    unpayedTaxCol.setVisible(showTax);
    taxCol.setVisible(showTax);
    grid.rebuild();
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCol)) {
        BUCN subject = lines.get(cell.getRow()).getAcc1().getSubject();
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subject.getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = PaymentMessages.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    }
  }

  private class GridDataProvider implements RGridDataProvider {
    @Override
    public int getRowCount() {
      int count = lines == null ? 0 : lines.size();
      countLabel.setText(PaymentMessages.M.resultTotal(count));
      return count;
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
      else if (col == totalCol.getIndex())
        return line.getTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getTotal().getTotal().doubleValue());
      else if (col == taxCol.getIndex())
        return line.getTotal().getTax() == null ? null : GWTFormat.fmt_money.format(line.getTotal()
            .getTax().doubleValue());
      else if (col == leftTotalCol.getIndex())
        return line.getLeftTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getLeftTotal().getTotal().doubleValue());
      else if (col == overdueTotalCol.getIndex())
        return line.getOverdueTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getOverdueTotal().getTotal().doubleValue());
      else if (col == sourceBillTypeCol.getIndex()){
        if (line.getAcc1().getSourceBill() == null) {
          return null;
        }
        BBillType type = EPPayment.getInstance().getBillTypeMap()
            .get(line.getAcc1().getSourceBill().getBillType());
        return type == null ? null : type.getCaption();
      }
      else if (col == remarkCol.getIndex())
        return line.getRemark();
      else
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

}
