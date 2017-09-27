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

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
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
 * 滞纳金明细编辑表格
 * 
 * @author subinzhu
 * 
 */
public class OverdueLineViewBox extends RCaptionBox {

  /** 账款明细行 */
  private List<BPaymentOverdueLine> lines;
  private BPayment bill;

  private Label countLabel;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef contractBillNumberCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef overdueTotalCol;
  private RGridColumnDef overdueTaxCol;
  private RGridColumnDef calcTotalCol;
  private RGridColumnDef remarkCol;

  public void refresh(BPayment bill) {
    this.bill = bill;
    this.lines = bill.getOverdueLines();
  }

  public OverdueLineViewBox() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    drawGrid();
    panel.add(grid);

    setContentSpacing(3);
    setCaption(PPaymentAccountLineDef.TABLE_CAPTION);
    setContent(panel);

    countLabel = new HTML(PaymentMessages.M.resultTotal(0));
    getCaptionBar().addButton(RSimplePanel.decorateMargin(countLabel, 0, 0, 0, 10));
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setProvider(new GridDataProvider());
    grid.addClickHandler(new Handler_grid());

    lineNumberCol = new RGridColumnDef(PPaymentOverdueLineDef.lineNumber);
    lineNumberCol.setWidth("40px");
    lineNumberCol.setResizable(true);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentOverdueLineDef.subject);
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("130px"));
    subjectCol.setWidth("150px");
    subjectCol.setResizable(true);
    grid.addColumnDef(subjectCol);

    contractBillNumberCol = new RGridColumnDef(PPaymentOverdueLineDef.contract_billNumber);
    contractBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractBillNumberCol.setResizable(true);
    contractBillNumberCol
        .setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    grid.addColumnDef(contractBillNumberCol);

    taxRateCol = new RGridColumnDef(PPaymentOverdueLineDef.taxRate);
    taxRateCol.setWidth("100px");
    taxRateCol.setResizable(true);
    grid.addColumnDef(taxRateCol);

    overdueTotalCol = new RGridColumnDef(PPaymentOverdueLineDef.overdueTotal_total);
    overdueTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTotalCol.setWidth("90px");
    overdueTotalCol.setResizable(true);
    grid.addColumnDef(overdueTotalCol);

    overdueTaxCol = new RGridColumnDef(PPaymentOverdueLineDef.overdueTotal_tax);
    overdueTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTaxCol.setWidth("90px");
    overdueTaxCol.setResizable(true);
    grid.addColumnDef(overdueTaxCol);

    calcTotalCol = new RGridColumnDef(PaymentMessages.M.calculate_total());
    calcTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    calcTotalCol.setWidth("100px");
    calcTotalCol.setResizable(true);
    grid.addColumnDef(calcTotalCol);

    remarkCol = new RGridColumnDef(PPaymentOverdueLineDef.remark);
    remarkCol.setWidth("160px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  public void refreshGrid() {
    boolean showTax = EPPayment.getInstance().getConfig().isShowTax();
    taxRateCol.setVisible(showTax);
    overdueTaxCol.setVisible(showTax);
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
        BUCN subject = lines.get(cell.getRow()).getSubject();
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

      BPaymentOverdueLine line = lines.get(row);
      if (col == lineNumberCol.getIndex())
        return line.getLineNumber();
      else if (col == subjectCol.getIndex())
        return line.getSubject() == null ? null : line.getSubject().toFriendlyStr();
      else if (col == contractBillNumberCol.getIndex()) {
        if (line.getContract() == null || bill.getCounterpart() == null) {
          return null;
        }
        String type = BCounterpart.COUNPERPART_PROPRIETOR.equals(bill.getCounterpart()
            .getCounterpartType()) ? GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE
            : GRes.FIELDNAME_CONTRACT_BILLTYPE;
        BDispatch dispatch = new BDispatch(type);
        dispatch.addParams(GRes.R.dispatch_key(), line.getContract().getCode());
        return dispatch;
      } else if (col == taxRateCol.getIndex())
        return line.getTaxRate() == null ? null : line.getTaxRate().toString();
      else if (col == calcTotalCol.getIndex())
        return line.getUnpayedTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getUnpayedTotal().getTotal().doubleValue());
      else if (col == overdueTotalCol.getIndex())
        return line.getTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getTotal().getTotal().doubleValue());
      else if (col == overdueTaxCol.getIndex())
        return line.getTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getTotal().getTax().doubleValue());
      else if (col == remarkCol.getIndex())
        return line.getRemark();
      else
        return null;
    }
  }
}
