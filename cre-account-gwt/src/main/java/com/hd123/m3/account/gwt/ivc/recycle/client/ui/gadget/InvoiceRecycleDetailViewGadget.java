/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleDetailViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月11日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.ui.gadget;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.intf.client.dd.PInvoiceLineDef;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycleLine;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RGridSummaryMethod;
import com.hd123.rumba.gwt.widget2.client.grid.RGridSummaryProvider;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * 发票回收单|调拨明细查看页面
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class InvoiceRecycleDetailViewGadget extends RCaptionBox {
  private BInvoiceRecycle entity;
  private HTML totalCount;

  private EditGrid<BInvoiceRecycleLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef startNumberCol;
  private RGridColumnDef quantityCol;
  private RGridColumnDef endNumberCol;
  private RGridColumnDef realQtyCol;
  private RGridColumnDef balanceQtyCol;
  private RGridColumnDef remarkCol;

  public void setValue(BInvoiceRecycle entity) {
    this.entity = entity;
    if (entity.getLines() == null) {
      entity.setLines(new ArrayList<BInvoiceRecycleLine>());
    }
    grid.setDefaultDataRowCount(0);
    grid.setValues(entity.getLines());

    totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity.getLines().size()));

    grid.refresh();
  }

  public InvoiceRecycleDetailViewGadget() {
    super();
    setCaption(InvoiceCommonMessages.M.invoiceDetail());
    getCaptionBar().setShowCollapse(true);
    setWidth("100%");
    drawSelf();
  }

  private void drawSelf() {
    drawGrid();

    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.add(grid);
    setContent(root);

    totalCount = new HTML(InvoiceCommonMessages.M.totalCount(0));
    getCaptionBar().addButton(totalCount);
  }

  private void drawGrid() {
    grid = new EditGrid<BInvoiceRecycleLine>();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setRequired(true);
    grid.setCaption(InvoiceCommonMessages.M.invoiceDetail());

    grid.setCustomSummaryProvider(new RGridSummaryProvider() {

      @Override
      public String getSummary(int col) {
        if (col == startNumberCol.getIndex()) {
          return "合计：";
        }
        if (col == quantityCol.getIndex()) {
          int total = 0;
          for (BInvoiceRecycleLine line : entity.getLines()) {
            total = total + line.getQuantity();
          }
          return total + "";
        }
        if (col == realQtyCol.getIndex()) {
          int total = 0;
          for (BInvoiceRecycleLine line : entity.getLines()) {
            total = total + line.getRealQty();
          }
          return total + "";
        }
        if (col == balanceQtyCol.getIndex()) {
          int total = 0;
          for (BInvoiceRecycleLine line : entity.getLines()) {
            total = total + line.getBalanceQty();
          }
          return total + "";
        }
        return null;
      }

    });

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    lineNumberCol = new RGridColumnDef(InvoiceCommonMessages.M.lineNo());
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    invoiceCodeCol = new RGridColumnDef(PInvoiceLineDef.invoiceCode);
    invoiceCodeCol.setWidth("150px");
    grid.addColumnDef(invoiceCodeCol);

    startNumberCol = new RGridColumnDef(PInvoiceLineDef.startNumber);
    startNumberCol.setSummaryMethod(RGridSummaryMethod.CUSTOM);
    startNumberCol.setWidth("180px");
    grid.addColumnDef(startNumberCol);

    quantityCol = new RGridColumnDef(PInvoiceLineDef.recycle_quantity);
    quantityCol.setSummaryMethod(RGridSummaryMethod.CUSTOM);
    quantityCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    quantityCol.setWidth("100px");
    grid.addColumnDef(quantityCol);

    endNumberCol = new RGridColumnDef(PInvoiceLineDef.endNumber);
    endNumberCol.setWidth("180px");
    grid.addColumnDef(endNumberCol);

    realQtyCol = new RGridColumnDef(PInvoiceLineDef.realQty);
    realQtyCol.setSummaryMethod(RGridSummaryMethod.CUSTOM);
    realQtyCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    realQtyCol.setWidth("100px");
    grid.addColumnDef(realQtyCol);

    balanceQtyCol = new RGridColumnDef(PInvoiceLineDef.balanceQty);
    balanceQtyCol.setSummaryMethod(RGridSummaryMethod.CUSTOM);
    balanceQtyCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    balanceQtyCol.setWidth("100px");
    grid.addColumnDef(balanceQtyCol);

    remarkCol = new RGridColumnDef(PInvoiceLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private class GridDataProvider implements RGridDataProvider,
      EditGridDataProduce<BInvoiceRecycleLine> {

    @Override
    public BInvoiceRecycleLine create() {
      BInvoiceRecycleLine detail = new BInvoiceRecycleLine();
      return detail;
    }

    @Override
    public int getRowCount() {
      return entity == null || entity.getLines() == null ? 0 : entity.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      BInvoiceRecycleLine rowData = entity.getLines().get(row);
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == invoiceCodeCol.getIndex()) {
        return rowData.getInvoiceCode();
      } else if (col == startNumberCol.getIndex()) {
        return rowData.getStartNumber();
      } else if (col == quantityCol.getIndex()) {
        return rowData.getQuantity();
      } else if (col == endNumberCol.getIndex()) {
        return rowData.getEndNumber();
      } else if (col == realQtyCol.getIndex()) {
        return rowData.getRealQty();
      } else if (col == balanceQtyCol.getIndex()) {
        return rowData.getBalanceQty();
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      return null;
    }
  }
}
