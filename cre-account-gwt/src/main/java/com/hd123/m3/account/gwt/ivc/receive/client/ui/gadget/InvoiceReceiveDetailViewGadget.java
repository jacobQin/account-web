/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReceiveDetailViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月9日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.client.ui.gadget;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.intf.client.dd.PInvoiceLineDef;
import com.hd123.m3.account.gwt.ivc.receive.client.biz.BInvoiceReceive;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RGridSummaryMethod;
import com.hd123.rumba.gwt.widget2.client.grid.RGridSummaryProvider;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 发票领用单|发票明细查看页面
 * 
 * @author lixiaohong
 * 
 * @since 1.7
 *
 */
public class InvoiceReceiveDetailViewGadget extends RCaptionBox{
  private BInvoiceReceive entity;
  private HTML totalCount;
  
  private EditGrid<BInvoiceLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef startNumberCol;
  private RGridColumnDef quantityCol;
  private RGridColumnDef endNumberCol;
  private RGridColumnDef remarkCol;
  
  public void setValue(BInvoiceReceive entity) {
    this.entity = entity;
    if (entity.getReceiveLines() == null) {
      entity.setReceiveLines(new ArrayList<BInvoiceLine>());
    }
    grid.setDefaultDataRowCount(0);
    grid.setValues(entity.getReceiveLines());

    totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity.getReceiveLines().size()));

    grid.refresh();
  }
  
  public InvoiceReceiveDetailViewGadget() {
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
    grid = new EditGrid<BInvoiceLine>();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setRequired(true);
    grid.setCaption(InvoiceCommonMessages.M.invoiceDetail());

    grid.addActionHandler(new RActionHandler() {

      @Override
      public void onAction(RActionEvent event) {
        if (EditGrid.ACTION_APPEND.equals(event.getActionName())
            || EditGrid.ACTION_REMOVE.equals(event.getActionName())) {
          totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity == null ? 0 : entity
              .getReceiveLines().size()));
        }
      }
    });

    grid.setCustomSummaryProvider(new RGridSummaryProvider() {

      @Override
      public String getSummary(int col) {
        if (col == startNumberCol.getIndex()) {
          return "合计：";
        }
        if (col == quantityCol.getIndex()) {
          int total = 0;
          for (BInvoiceLine line : entity.getReceiveLines()) {
            total = total + line.getQuantity();
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
    invoiceCodeCol.setWidth("180px");
    grid.addColumnDef(invoiceCodeCol);

    startNumberCol = new RGridColumnDef(PInvoiceLineDef.startNumber);
    startNumberCol.setSummaryMethod(RGridSummaryMethod.CUSTOM);
    startNumberCol.setWidth("180px");
    grid.addColumnDef(startNumberCol);

    quantityCol = new RGridColumnDef(PInvoiceLineDef.receive_quantity);
    quantityCol.setSummaryMethod(RGridSummaryMethod.CUSTOM);
    quantityCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    quantityCol.setWidth("120px");
    grid.addColumnDef(quantityCol);

    endNumberCol = new RGridColumnDef(PInvoiceLineDef.endNumber);
    endNumberCol.setWidth("180px");
    grid.addColumnDef(endNumberCol);

    remarkCol = new RGridColumnDef(PInvoiceLineDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }
  
  private class GridDataProvider implements RGridDataProvider, EditGridDataProduce<BInvoiceLine> {

    @Override
    public BInvoiceLine create() {
      BInvoiceLine detail = new BInvoiceLine();
      return detail;
    }

    @Override
    public int getRowCount() {
      return entity == null || entity.getReceiveLines() == null ? 0 : entity.getReceiveLines()
          .size();
    }

    @Override
    public Object getData(int row, int col) {
      BInvoiceLine rowData = entity.getReceiveLines().get(row);
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
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      return null;
    }
  }
}
