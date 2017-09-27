/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegInvoiceViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegInvoice;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegInvoiceDef;
import com.hd123.m3.account.gwt.invoice.receipt.client.EPRecInvoiceReg;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author chenpeisi
 * 
 */
public class RecInvoiceRegInvoiceViewGadget extends RCaptionBox {

  public RecInvoiceRegInvoiceViewGadget()  {
    drawSelf();
  }

  private BInvoiceReg entity;

  private Label captionCount;
  private RGrid grid;

  private RGridColumnDef lineNumberCol;
  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef amountCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef invoiceDateCol;
  private RGridColumnDef remarkCol;

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");

    vp.add(drawGrid());

    captionCount = new Label(InvoiceRegMessage.M.resultTotal(0));
    setCaption(InvoiceRegMessage.M.invoiceInfo());
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
        captionCount.setText(InvoiceRegMessage.M.resultTotal(entity.getInvoices().size()));
      }
    });

    lineNumberCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.lineNumber());
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    grid.addColumnDef(lineNumberCol);

    invoiceTypeCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.invoiceType());
    invoiceTypeCol.setWidth("90px");
    grid.addColumnDef(invoiceTypeCol);

    invoiceNumberCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.invoiceNumber());
    invoiceNumberCol.setWidth("150px");
    grid.addColumnDef(invoiceNumberCol);

    totalCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.total_total());
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("90px");
    grid.addColumnDef(totalCol);

    amountCol = new RGridColumnDef(InvoiceRegMessage.M.amount());
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    amountCol.setWidth("90px");
    grid.addColumnDef(amountCol);

    taxCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.total_tax());
    taxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    taxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    taxCol.setWidth("90px");
    grid.addColumnDef(taxCol);

    taxRateCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.taxRate());
    taxRateCol.setWidth("120px");
    grid.addColumnDef(taxRateCol);

    invoiceDateCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.invoiceDate());
    invoiceDateCol.setWidth("90px");
    grid.addColumnDef(invoiceDateCol);

    remarkCol = new RGridColumnDef(PInvoiceRegInvoiceDef.constants.remark());
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  public void refresh(BInvoiceReg entity) {
    assert entity != null;
    this.entity = entity;

    grid.refresh();
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      if (entity == null || entity.getInvoices() == null) {
        return 0;
      }
      return entity.getInvoices().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entity.getLines() == null || entity.getInvoices().isEmpty())
        return null;

      BInvoiceRegInvoice invoice = entity.getInvoices().get(row);

      if (col == lineNumberCol.getIndex()) {
        return invoice.getLineNumber();
      } else if (col == invoiceTypeCol.getIndex()) {
        return invoice.getInvoiceType() == null ? null : EPRecInvoiceReg.getInstance()
            .getInvoiceTypeMap().get(invoice.getInvoiceType());
      } else if (col == invoiceNumberCol.getIndex()) {
        return invoice.getInvoiceNumber();
      } else if (col == totalCol.getIndex()) {
        return invoice.getTotal() == null ? null : invoice.getTotal().getTotal().doubleValue();
      } else if (col == amountCol.getIndex()) {
        return invoice.getAmount();
      } else if (col == taxCol.getIndex()) {
        return invoice.getTotal() == null ? null : invoice.getTotal().getTax().doubleValue();
      } else if (col == taxRateCol.getIndex()) {
        return invoice.getTaxRate() == null ? null : invoice.getTaxRate().caption();
      } else if (col == invoiceDateCol.getIndex()) {
        return invoice.getInvoiceDate() == null ? null : GWTFormat.fmt_yMd.format(invoice
            .getInvoiceDate());
      } else if (col == remarkCol.getIndex()) {
        return invoice.getRemark();
      }

      return null;
    }
  }
}
