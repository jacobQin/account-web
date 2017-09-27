/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegDetailViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月9日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.InvoiceRegMessages;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BIvcRegLine;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票领用单|发票明细查看页面
 * 
 * @author chenrizhang
 * 
 * @since 1.7
 * 
 */
public class InvoiceRegDetailViewGadget extends RCaptionBox {
  private EPInvoiceRegReceipt ep = EPInvoiceRegReceipt.getInstance();

  private BInvoiceReg entity;
  private HTML totalCount;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef periodCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef originTotalCol;
  private RGridColumnDef unregTotalCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef remarkCol;

  public void setValue(BInvoiceReg entity) {
    this.entity = entity;
    unregTotalCol.setVisible(entity.isAllowSplitReg());
    grid.rebuild();
  }

  public InvoiceRegDetailViewGadget() {
    super();
    setCaption(InvoiceRegMessages.M.regDetails());
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

    totalCount = new HTML(InvoiceRegMessages.M.totalCount(0));
    getCaptionBar().addButton(totalCount);
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setProvider(new GridDataProvider());
    grid.addClickHandler(new GridClickHandler());
    grid.addRefreshHandler(new RefreshHandler<RGridCellInfo>() {

      @Override
      public void onRefresh(RefreshEvent<RGridCellInfo> event) {
        totalCount.setHTML(InvoiceRegMessages.M.totalCount(entity.getRegLines().size()));
      }
    });

    lineNumberCol = new RGridColumnDef(InvoiceRegMessages.M.lineNo());
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(InvoiceRegMessages.M.regDetail_subject());
    subjectCol.setWidth("140px");
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("120px"));
    grid.addColumnDef(subjectCol);

    periodCol = new RGridColumnDef(InvoiceRegMessages.M.regDetail_period());
    periodCol.setWidth("160px");
    grid.addColumnDef(periodCol);

    sourceBillNumberCol = new RGridColumnDef(InvoiceRegMessages.M.regDetail_sourceBillNumber());
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth("160px");
    grid.addColumnDef(sourceBillNumberCol);

    sourceBillTypeCol = new RGridColumnDef(InvoiceRegMessages.M.regDetail_sourceBillType());
    sourceBillTypeCol.setWidth("80px");
    grid.addColumnDef(sourceBillTypeCol);

    originTotalCol = new RGridColumnDef(PInvoiceRegDef.originTotal);
    originTotalCol.setWidth("90px");
    originTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    grid.addColumnDef(originTotalCol);

    unregTotalCol = new RGridColumnDef(PInvoiceRegDef.unregTotal);
    unregTotalCol.setWidth("90px");
    unregTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unregTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    grid.addColumnDef(unregTotalCol);

    totalCol = new RGridColumnDef(PInvoiceRegDef.total);
    totalCol.setWidth("100px");
    totalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PInvoiceRegDef.taxTotal);
    taxCol.setWidth("90px");
    taxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    taxCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    grid.addColumnDef(taxCol);
    invoiceNumberCol = new RGridColumnDef(PInvoiceRegDef.invoice_number);
    invoiceNumberCol.setWidth("120px");
    grid.addColumnDef(invoiceNumberCol);

    invoiceCodeCol = new RGridColumnDef(PInvoiceRegDef.invoice_code);
    invoiceCodeCol.setWidth("120px");
    grid.addColumnDef(invoiceCodeCol);
    remarkCol = new RGridColumnDef(PInvoiceRegDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private class GridClickHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCol)) {
        BIvcRegLine line = entity.getRegLines().get(grid.getCurrentRow());
        if (line == null)
          return;
        if (line.getAcc1().getSubject() == null
            || StringUtil.isNullOrBlank(line.getAcc1().getSubject().getUuid()))
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID,
            line.getAcc1().getSubject().getUuid());
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
      }
    }

  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return entity == null || entity.getRegLines() == null ? 0 : entity.getRegLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      BIvcRegLine rowData = entity.getRegLines().get(row);
      BAcc1 acc = rowData.getAcc1();
      if (acc == null)
        return null;
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == subjectCol.getIndex()) {
        return acc.getSubject().toFriendlyStr();
      } else if (col == periodCol.getIndex()) {
        if (acc.getBeginTime() == null && acc.getEndTime() == null)
          return null;
        return InvoiceRegMessages.M.period(
            acc.getBeginTime() == null ? "" : M3Format.fmt_yMd.format(acc.getBeginTime()),
            acc.getEndTime() == null ? "" : M3Format.fmt_yMd.format(acc.getEndTime()));
      } else if (col == sourceBillNumberCol.getIndex()) {
        if (acc.getSourceBill() == null) {
          return null;
        }
        BDispatch dispatch = new BDispatch(acc.getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), acc.getSourceBill().getBillNumber());
        return dispatch;
      } else if (col == sourceBillTypeCol.getIndex()) {
        BBillType billType = ep.getBillTypes().get(acc.getSourceBill().getBillType());
        return billType == null ? null : billType.getCaption();
      } else if (col == originTotalCol.getIndex()) {
        if (entity.isAllowSplitReg()) {
          return rowData.getOriginTotal().getTotal();
        } else {
          return rowData.getUnregTotal().getTotal();
        }
      } else if (col == unregTotalCol.getIndex()) {
        return rowData.getUnregTotal().getTotal();
      } else if (col == totalCol.getIndex()) {
        return rowData.getTotal().getTotal();
      } else if (col == taxCol.getIndex()) {
        return rowData.getTotal().getTax();
      } else if (col == invoiceCodeCol.getIndex()) {
        return rowData.getInvoiceCode();
      } else if (col == invoiceNumberCol.getIndex()) {
        return rowData.getInvoiceNumber();
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      return null;
    }
  }
}
