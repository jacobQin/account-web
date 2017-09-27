/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeEviToEviViewWidget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月20日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeUtil;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchBalanceLineDef;
import com.hd123.m3.commons.gwt.util.client.grid.RPageDataUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票换发票明细查看控件
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeInvToInvViewWidget extends RCaptionBox {

  private BInvoiceExchange entity;

  private HTML totalCount;

  private RPagingGrid<BInvoiceExchBalanceLine> pagingGrid;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef balanceCodeCol;
  private RGridColumnDef balanceNumberCol;
  private RGridColumnDef amountCol;
  private RGridColumnDef remarkCol;

  public InvoiceExchangeInvToInvViewWidget() {
    setCaption(InvoiceExchangeMessages.M.invoiceDetail());
    getCaptionBar().setShowCollapse(true);
    setWidth("100%");
    drawSelf();
  }

  private void drawSelf() {
    drawGrid();
    pagingGrid = new RPagingGrid<BInvoiceExchBalanceLine>(grid, new GridDataProvider());
    setContent(pagingGrid);

    totalCount = new HTML(InvoiceCommonMessages.M.totalCount(0));
    getCaptionBar().addButton(totalCount);
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");

    lineNumberCol = new RGridColumnDef(InvoiceCommonMessages.M.lineNo());
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    invoiceCodeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.oldInvoiceCode);
    invoiceCodeCol.setWidth("180px");
    grid.addColumnDef(invoiceCodeCol);

    invoiceNumberCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.oldInvoiceNumber);
    invoiceNumberCol.setWidth("180px");
    grid.addColumnDef(invoiceNumberCol);
    
    balanceCodeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.balanceInvoiceCode);
    balanceCodeCol.setWidth("180px");
    grid.addColumnDef(balanceCodeCol);

    balanceNumberCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.balanceInvoiceNumber);
    balanceNumberCol.setWidth("180px");
    grid.addColumnDef(balanceNumberCol);

    amountCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.amount);
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setWidth("120px");
    grid.addColumnDef(amountCol);

    remarkCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  public void setValue(BInvoiceExchange entity) {
    this.entity = entity;
    totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity.getExchBalanceLines().size()));

    grid.refresh();
    pagingGrid.gotoFirstPage();
  }

  private class GridDataProvider implements RPageDataProvider<BInvoiceExchBalanceLine> {

    @Override
    public void fetchData(final int page, final int pageSize, String sortField, OrderDir sortDir,
        final AsyncCallback<RPageData<BInvoiceExchBalanceLine>> callback) {
      final RPageData<BInvoiceExchBalanceLine> pageData = RPageDataUtil.flip(
          entity.getExchBalanceLines(), page, pageSize);

      callback.onSuccess(pageData);
    }

    @Override
    public Object getData(int row, int col, BInvoiceExchBalanceLine rowData,
        List<BInvoiceExchBalanceLine> pageData) {
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == invoiceCodeCol.getIndex()) {
        return rowData.getOldCode();
      } else if (col == invoiceNumberCol.getIndex()) {
        return rowData.getOldNumber();
      } else if (col == amountCol.getIndex()) {
        return InvoiceExchangeUtil.formatAmount(rowData.getAmount());
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      } else if(col == balanceCodeCol.getIndex()){
        return rowData.getBalanceCode();
      }else if(col == balanceNumberCol.getIndex()){
        return rowData.getBalanceNumber();
      }
      return null;
    }
  }

}
