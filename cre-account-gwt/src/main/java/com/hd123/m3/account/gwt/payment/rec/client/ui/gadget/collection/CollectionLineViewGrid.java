/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CollectionLineViewGrid.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月18日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCollectionLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
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
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 代收明细查看表格（按总额）
 * 
 * @author LiBin
 * 
 */
public class CollectionLineViewGrid extends RCaptionBox {
  private BPayment bill;
  /** 账款明细行 */
  private List<BPaymentCollectionLine> lines = new ArrayList<BPaymentCollectionLine>();

  private HTML totalCount;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;// 行号
  private RGridColumnDef subjectCol; // 科目
  private RGridColumnDef contractCodeCol; // 合同编号
  private RGridColumnDef contractNameCol; // 店招
  private RGridColumnDef beginDateCol;// 起始日期
  private RGridColumnDef endDateCol; // 截止日期
  private RGridColumnDef receivablleTotalCol;// 应收金额
  private RGridColumnDef realTotalCol;// 账款实收金额
  private RGridColumnDef unreceivableCol;// 未收金额
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef remarkCol; // 说明

  public void setBill(BPayment bill) {
    this.bill = bill;
    this.lines = bill.getCollectionLines();
    refreshTotalCount(0);
    grid.rebuild();
  }

  public CollectionLineViewGrid() {
    drawToolbar();
    drawSelf();
  }

  private void drawToolbar() {
    totalCount = new HTML(ReceiptMessages.M.resultTotal(0));
    getCaptionBar().addButton(totalCount);
  }

  private void drawSelf() {
    setBorder(false);
    setWidth("100%");
    setContent(drawGrid());
    getCaptionBar().setShowCollapse(true);
    setCaption(ReceiptMessages.M.collectionDetail());
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.addRefreshHandler(new GridRefreshHandler());
    grid.addClickHandler(new GridClickHandler());

    lineNumberCol = new RGridColumnDef(PPaymentCollectionLineDef.lineNumber);
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentCollectionLineDef.subject);
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("160px"));
    subjectCol.setOverflowEllipsis(true);
    subjectCol.setWidth("180px");
    grid.addColumnDef(subjectCol);

    contractCodeCol = new RGridColumnDef(PPaymentCollectionLineDef.contractCode);
    contractCodeCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    contractCodeCol.setOverflowEllipsis(true);
    contractCodeCol.setWidth("150px");
    grid.addColumnDef(contractCodeCol);

    contractNameCol = new RGridColumnDef(PPaymentCollectionLineDef.contractName);
    contractNameCol.setOverflowEllipsis(true);
    contractNameCol.setWidth("120px");
    grid.addColumnDef(contractNameCol);

    beginDateCol = new RGridColumnDef(PPaymentCollectionLineDef.beginTime);
    beginDateCol.setWidth("120px");
    grid.addColumnDef(beginDateCol);

    endDateCol = new RGridColumnDef(PPaymentCollectionLineDef.endTime);
    endDateCol.setWidth("120px");
    grid.addColumnDef(endDateCol);

    receivablleTotalCol = new RGridColumnDef(PPaymentCollectionLineDef.receivableTotal);
    receivablleTotalCol.setWidth("120px");
    receivablleTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(receivablleTotalCol);

    realTotalCol = new RGridColumnDef(PPaymentCollectionLineDef.realTotal);
    realTotalCol.setWidth("120px");
    realTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(realTotalCol);

    unreceivableCol = new RGridColumnDef(PPaymentCollectionLineDef.unreceivableTotal);
    unreceivableCol.setWidth("120px");
    unreceivableCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(unreceivableCol);

    invoiceCodeCol = new RGridColumnDef(ReceiptMessages.M.invoiceCode());
    invoiceCodeCol.setWidth("80px");
    invoiceCodeCol.setResizable(true);
    grid.addColumnDef(invoiceCodeCol);

    invoiceNumberCol = new RGridColumnDef(ReceiptMessages.M.invoiceNumber());
    invoiceNumberCol.setWidth("80px");
    invoiceNumberCol.setResizable(true);
    grid.addColumnDef(invoiceNumberCol);

    remarkCol = new RGridColumnDef(PPaymentCollectionLineDef.remark);
    remarkCol.setWidth("100px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    grid.setAllowHorizontalScrollBar(true);

    return grid;
  }

  public void onHide() {
    refreshTotalCount(0);
  }

  private void refreshTotalCount(int size) {
    totalCount.setHTML(ReceiptMessages.M.resultTotal(size));
  }

  private class GridRefreshHandler implements RefreshHandler<RGridCellInfo> {
    @Override
    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      refreshTotalCount(lines.size());
    }

  }

  private class GridClickHandler implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCol)) {
        BUCN subject = lines.get(cell.getRow()).getSubject();
        if (subject == null || StringUtil.isNullOrBlank(subject.getUuid())) {
          return;
        }
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

  }

  private class GridDataProvider implements RGridDataProvider,
      EditGridDataProduce<BPaymentCollectionLine> {

    @Override
    public BPaymentCollectionLine create() {
      return new BPaymentCollectionLine();
    }

    @Override
    public int getRowCount() {
      return lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (lines.size() == 0) {
        return null;
      }

      BPaymentCollectionLine line = lines.get(row);

      if (lineNumberCol.getIndex() == col) {
        return row + 1;
      } else if (subjectCol.getIndex() == col) {
        return line.getSubject() == null ? null : line.getSubject().toFriendlyStr();
      } else if (beginDateCol.getIndex() == col) {
        return GWTFormat.fmt_yMd.format(line.getBeginDate());
      } else if (endDateCol.getIndex() == col) {
        return GWTFormat.fmt_yMd.format(line.getEndDate());
      } else if (receivablleTotalCol.getIndex() == col) {
        return GWTFormat.fmt_money.format(line.getUnpayedTotal().getTotal());
      } else if (realTotalCol.getIndex() == col) {
        return getTotalStr(line.getTotal());
      } else if (unreceivableCol.getIndex() == col) {
        return GWTFormat.fmt_money.format(line.getUnReceivableTotal());
      } else if (remarkCol.getIndex() == col) {
        return line.getRemark();
      } else if (col == invoiceCodeCol.getIndex()) {
        return line.getInvoiceCodeStr();
      } else if (col == invoiceNumberCol.getIndex()) {
        return line.getInvoiceNumberStr();
      } else if (col == contractCodeCol.getIndex()) {
        if (line.getContract() == null || bill.getCounterpart() == null) {
          return null;
        }
        String type = BCounterpart.COUNPERPART_PROPRIETOR.equals(bill.getCounterpart()
            .getCounterpartType()) ? GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE
            : GRes.FIELDNAME_CONTRACT_BILLTYPE;
        BDispatch dispatch = new BDispatch(type);
        dispatch.addParams(GRes.R.dispatch_key(), line.getContract().getCode());
        return dispatch;
      } else if (col == contractNameCol.getIndex()) {
        return line.getContract() == null ? null : line.getContract().getName();
      }
      return null;
    }

    private String getTotalStr(BTotal total) {
      if (total == null || total.getTotal() == null) {
        return null;
      }
      return GWTFormat.fmt_money.format(total.getTotal());
    }
  }

}
