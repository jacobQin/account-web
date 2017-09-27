/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockRegDetails.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月1日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStockRegLine;
import com.hd123.m3.account.gwt.ivc.stock.client.rpc.InvoiceStockService;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;

/**
 * 发票库存发票登记明细显示对话框
 * 
 * @author LiBin
 * 
 */
public class InvoiceStockRegDetailsDialog extends RDialog {

  /** 表格数据 */
  private List<BInvoiceStockRegLine> lines = new ArrayList<BInvoiceStockRegLine>();

  private RGrid grid;
  private RGridColumnDef tenantCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef contractCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef beginEndDateCol;
  private RGridColumnDef receivableTotalCol;
  private RGridColumnDef regTotalCol;

  public InvoiceStockRegDetailsDialog() {
    setCaptionHTML(M.M.regDetail());
    setShowButtons(true);
    setButtons(new ButtonConfig[] {
      RDialog.BUTTON_OK });
    setWidth("920px");
    setWidget(drawGrid());
  }

  /**
   * 居中显示对话框，将根据传入的发票号码重新加载发票登记明细。
   * 
   * @param invoiceNumber
   *          发票号码，传入null或者空字符串将导致列表被清空。
   */
  public void show(String invoiceNumber) {
    super.center();
    reloadRegLines(invoiceNumber);
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setProvider(new DetailGridDataProvider());

    tenantCol = new RGridColumnDef(M.M.tenant());
    tenantCol.setWidth("120px");
    grid.addColumnDef(tenantCol);

    subjectCol = new RGridColumnDef(M.M.subject());
    subjectCol.setWidth("80px");
    grid.addColumnDef(subjectCol);

    contractCol = new RGridColumnDef(M.M.contract());
    contractCol.setWidth("120px");
    grid.addColumnDef(contractCol);

    sourceBillTypeCol = new RGridColumnDef(M.M.sourceBillType());
    sourceBillTypeCol.setWidth("100px");
    grid.addColumnDef(sourceBillTypeCol);

    sourceBillNumberCol = new RGridColumnDef(M.M.sourceBillNumber());
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    grid.addColumnDef(sourceBillNumberCol);

    beginEndDateCol = new RGridColumnDef(M.M.beginEndDate());
    beginEndDateCol.setWidth("140px");
    grid.addColumnDef(beginEndDateCol);

    receivableTotalCol = new RGridColumnDef(M.M.receivableTotal());
    receivableTotalCol.setWidth("80px");
    receivableTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(receivableTotalCol);

    regTotalCol = new RGridColumnDef(M.M.regTotal());
    regTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    regTotalCol.setWidth("80px");
    grid.addColumnDef(regTotalCol);
    
    grid.setAllColumnsOverflowEllipsis(true);
    grid.setAllColumnsResizable(true);

    return grid;
  }
  
  

  /** 重新加载 */
  private void reloadRegLines(String invoiceNumber) {
    RLoadingDialog.show();
    InvoiceStockService.Locator.getService().getRegLinesByNumber(invoiceNumber, new AsyncCallback<List<BInvoiceStockRegLine>>() {
      @Override
      public void onSuccess(List<BInvoiceStockRegLine> result) {
        RLoadingDialog.hide();
        setLines(result);
      }
      
      @Override
      public void onFailure(Throwable caught) {
        RLoadingDialog.hide();
        RMsgBox.showError(M.M.getRegLinesError(), caught);
      }
    });
  }

  private void setLines(List<BInvoiceStockRegLine> lines) {
    this.lines = lines;
    grid.refresh();
  }

  private class DetailGridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      BInvoiceStockRegLine line = lines.get(row);
      if (col == tenantCol.getIndex()) {
        return (line.getTenant() == null) ? null : line.getTenant().toFriendlyStr();
      } else if (col == subjectCol.getIndex()) {
        return line.getSubject() == null ? null : line.getSubject().toFriendlyStr();
      } else if (col == contractCol.getIndex()) {
        return line.getContract() == null ? null : line.getContract().toFriendlyStr();
      }  else if (col == sourceBillTypeCol.getIndex()) {
        return line.getSourceBillTypeCaption();
      } else if (col == sourceBillNumberCol.getIndex()) {
        if (line.getSourceBillNumber() == null) {
          return null;
        }

        BDispatch dispatch = new BDispatch(line.getSouceBillType());
        dispatch.addParams(GRes.R.dispatch_key(), line.getSourceBillNumber());
        return dispatch;
      } else if (col == beginEndDateCol.getIndex()) {
        return buildDateRange(line.getBeginDate(), line.getEndDate());
      } else if (col == receivableTotalCol.getIndex()) {
        return M3Format.fmt_money.format(line.getReceivableTotal());
      } else if (col == regTotalCol.getIndex()) {
        return M3Format.fmt_money.format(line.getRegTotal());
      }
      return null;
    }
  }

  private String buildDateRange(Date beginDate, Date endDate) {
    if (beginDate == null && endDate == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    if (beginDate != null) {
      sb.append(M3Format.fmt_yMd.format(beginDate));
    }
    sb.append("~");
    if (endDate != null) {
      sb.append(M3Format.fmt_yMd.format(endDate));
    }
    return sb.toString();
  }

  public interface M extends Messages {
    public static M M = GWT.create(M.class);

    @DefaultMessage("开票明细")
    String regDetail();

    @DefaultMessage("商户")
    String tenant();

    @DefaultMessage("科目")
    String subject();

    @DefaultMessage("合同")
    String contract();

    @DefaultMessage("店招")
    String shop();

    @DefaultMessage("来源单据类型")
    String sourceBillType();

    @DefaultMessage("来源单号")
    String sourceBillNumber();

    @DefaultMessage("起止日期")
    String beginEndDate();

    @DefaultMessage("应收金额")
    String receivableTotal();

    @DefaultMessage("开票金额")
    String regTotal();
    
    @DefaultMessage("获取开票明细出错。")
    String getRegLinesError();
  }

}
