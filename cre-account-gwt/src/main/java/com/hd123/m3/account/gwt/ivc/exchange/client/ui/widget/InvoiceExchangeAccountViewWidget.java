/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeEviToEviAccountEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月21日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeUtil;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchBalanceLineDef;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.m3.commons.gwt.util.client.grid.RPageDataUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * 收据换发票账款明细查看控件
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeAccountViewWidget extends RCaptionBox {

  private BInvoiceExchange entity;

  private HTML totalCount;

  private RViewStringField counterpartField;

  private RPagingGrid<BInvoiceExchAccountLine> pagingGrid;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef beginEndDateCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillCol;
  private RGridColumnDef oldCodeCol;
  private RGridColumnDef oldNumberCol;
  private RGridColumnDef newCodeCol;
  private RGridColumnDef newNumberCol;
  private RGridColumnDef amountCol;
  private RGridColumnDef remarkCol;

  public InvoiceExchangeAccountViewWidget() {
    setCaption(InvoiceExchangeMessages.M.accountDetail());
    getCaptionBar().setShowCollapse(true);
    setWidth("100%");
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    setContent(root);

    root.add(drawTenantBox());

    drawGrid();
    pagingGrid = new RPagingGrid<BInvoiceExchAccountLine>(grid, new GridDataProvider());
    root.add(pagingGrid);

    totalCount = new HTML(InvoiceCommonMessages.M.totalCount(0));
    getCaptionBar().addButton(totalCount);
  }

  private Widget drawTenantBox() {
    RForm form = new RForm();
    form.setWidth("800px");
    form.setCellSpacing(10);
    counterpartField = new RViewStringField(InvoiceCommonMessages.M.tenant());
    form.add(counterpartField);
    form.add(new HTML(""));
    return form;
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");

    lineNumberCol = new RGridColumnDef(InvoiceCommonMessages.M.lineNo());
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.subject);
    subjectCol.setWidth("140px");
    grid.addColumnDef(subjectCol);

    beginEndDateCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.beginEndDate);
    beginEndDateCol.setWidth("150px");
    grid.addColumnDef(beginEndDateCol);

    sourceBillTypeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.sourceBillType);
    sourceBillTypeCol.setWidth("100px");
    grid.addColumnDef(sourceBillTypeCol);

    sourceBillCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.sourceBill);
    sourceBillCol.setWidth("160px");
    sourceBillCol.setRendererFactory(new CellRendererFactory());
    grid.addColumnDef(sourceBillCol);

    oldCodeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.oldReceiptCode);
    oldCodeCol.setWidth("80px");
    grid.addColumnDef(oldCodeCol);

    oldNumberCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.oldReceiptNumber);
    oldNumberCol.setWidth("100px");
    grid.addColumnDef(oldNumberCol);

    newCodeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.newReceiptCode);
    newCodeCol.setWidth("80px");
    grid.addColumnDef(newCodeCol);

    newNumberCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.newReceiptNumber);
    newNumberCol.setWidth("100px");
    grid.addColumnDef(newNumberCol);

    amountCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.amount);
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setWidth("80px");
    grid.addColumnDef(amountCol);

    remarkCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);

  }

  public void setValue(BInvoiceExchange entity) {
    this.entity = entity;
    totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity.getExchAccountLines().size()));
    refreshColumnsCaption();
    refreshTenantField();
    pagingGrid.gotoFirstPage();
  }

  private void refreshColumnsCaption() {
    if (BExchangeType.eviToEvi.equals(entity.getType())) {
      oldCodeCol.setFieldDef(PInvoiceExchBalanceLineDef.oldReceiptCode);
      oldNumberCol.setFieldDef(PInvoiceExchBalanceLineDef.oldReceiptNumber);
      newCodeCol.setFieldDef(PInvoiceExchBalanceLineDef.newReceiptCode);
      newNumberCol.setFieldDef(PInvoiceExchBalanceLineDef.newReceiptNumber);
    } else if (BExchangeType.eviToInv.equals(entity.getType())) {
      oldCodeCol.setFieldDef(PInvoiceExchBalanceLineDef.oldReceiptCode);
      oldNumberCol.setFieldDef(PInvoiceExchBalanceLineDef.oldReceiptNumber);
      newCodeCol.setFieldDef(PInvoiceExchBalanceLineDef.newInvoiceCode);
      newNumberCol.setFieldDef(PInvoiceExchBalanceLineDef.newInvoiceNumber);
    } else if (BExchangeType.invToInv.equals(entity.getType())) {
      oldCodeCol.setFieldDef(PInvoiceExchBalanceLineDef.oldInvoiceCode);
      oldNumberCol.setFieldDef(PInvoiceExchBalanceLineDef.oldInvoiceNumber);
      newCodeCol.setFieldDef(PInvoiceExchBalanceLineDef.newInvoiceCode);
      newNumberCol.setFieldDef(PInvoiceExchBalanceLineDef.newInvoiceNumber);
    } else {
      assert false;
    }
    grid.rebuild();
  }

  private void refreshTenantField() {
    counterpartField.setValue(TenantStringUtil.toTenantsString(entity.getExchAccountLines()));
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        final Object data) {
      if (sourceBillCol.getName().equals(colDef.getName())) {
        return new SourceBillNumberRenderer(row, col);
      }
      return null;
    }
  }

  /**
   * 来源单号表格列渲染器
   * 
   * @author LiBin
   * 
   */
  private class SourceBillNumberRenderer extends RCombinedFlowField implements
      RCellRenderer<String> {

    private DispatchLinkField sourceBillNumberField;
    /** 所在的表格行 */
    private int row = -1;

    /** 所在的表格列 */
    private int column = -1;

    private String value;

    public SourceBillNumberRenderer(int row, int column) {
      sourceBillNumberField = new DispatchLinkField();
      sourceBillNumberField.setLinkKey(GRes.R.dispatch_key());
      addField(sourceBillNumberField);
      setRow(row);
      setColumn(column);

      if (row >= entity.getExchAccountLines().size()) {
        return;
      }
      BInvoiceExchAccountLine detail = entity.getExchAccountLines().get(getRow());
      if (detail == null) {
        return;
      }
      setValue(detail.getSourceBill());
    }

    @Override
    public String getValue() {
      return value;
    }

    @Override
    public void setValue(String value) {
      this.value = value;
      sourceBillNumberField.clearValue();
      if (value == null) {
        return;
      }
      BInvoiceExchAccountLine detail = entity.getExchAccountLines().get(getRow());
      sourceBillNumberField.setValue(detail.getSourceBillType(), detail.getSourceBill());
    }

    @Override
    public int getRow() {
      return row;
    }

    @Override
    public int getColumn() {
      return column;
    }

    void setRow(int row) {
      this.row = row;
    }

    void setColumn(int column) {
      this.column = column;
    }

  }

  private class GridDataProvider implements RPageDataProvider<BInvoiceExchAccountLine> {

    @Override
    public void fetchData(final int page, final int pageSize, String sortField, OrderDir sortDir,
        final AsyncCallback<RPageData<BInvoiceExchAccountLine>> callback) {
      final RPageData<BInvoiceExchAccountLine> pageData = RPageDataUtil.flip(
          entity.getExchAccountLines(), page, pageSize);

      callback.onSuccess(pageData);
    }

    @Override
    public Object getData(int row, int col, BInvoiceExchAccountLine rowData,
        List<BInvoiceExchAccountLine> pageData) {
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == oldCodeCol.getIndex()) {
        return rowData.getOldCode();
      } else if (col == oldNumberCol.getIndex()) {
        return rowData.getOldNumber();
      } else if (col == newCodeCol.getIndex()) {
        return rowData.getNewCode();
      } else if (col == newNumberCol.getIndex()) {
        return rowData.getNewNumber();
      } else if (col == amountCol.getIndex()) {
        return InvoiceExchangeUtil.formatAmount(rowData.getAmount());
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      } else if (col == subjectCol.getIndex()) {
        return rowData.getSubject();
      } else if (col == beginEndDateCol.getIndex()) {
        return InvoiceExchangeUtil.getBeginEndDate(rowData.getAcc1());
      } else if (col == sourceBillCol.getIndex()) {
        return rowData.getSourceBill();
      } else if (col == sourceBillTypeCol.getIndex()) {
        return EPInvoiceExchange.getInstance()
            .getBillTypeCaptionByName(rowData.getSourceBillType());
      }
      return null;
    }
  }

}
