/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegLineViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLine;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegLineDef;
import com.hd123.m3.account.gwt.invoice.receipt.client.EPRecInvoiceReg;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenpeisi
 * 
 */
public class RecInvoiceRegLineViewGadget extends RCaptionBox {

  public RecInvoiceRegLineViewGadget() {
    drawSelf();
  }

  private BInvoiceReg entity;

  private Label captionCount;
  private RGrid grid;

  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef unregTotalCol;
  private RGridColumnDef unregTaxCol;
  private RGridColumnDef regTotalCol;
  private RGridColumnDef regTaxCol;
  private RGridColumnDef remarkCol;

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");

    vp.add(drawGrid());

    captionCount = new Label(InvoiceRegMessage.M.resultTotal(0));
    setCaption(InvoiceRegMessage.M.lineInfo());
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
        captionCount.setText(InvoiceRegMessage.M.resultTotal(entity.getLines().size()));
      }
    });
    grid.addClickHandler(new Handler_grid());

    lineNumberCol = new RGridColumnDef(PInvoiceRegLineDef.lineNumber);
    lineNumberCol.setWidth("50px");
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PInvoiceRegLineDef.acc1_subject);
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("140px"));
    subjectCol.setWidth("160px");
    grid.addColumnDef(subjectCol);

    sourceBillNumberCol = new RGridColumnDef(PInvoiceRegLineDef.acc1_sourceBill_billNumber);
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth("160px");
    grid.addColumnDef(sourceBillNumberCol);

    sourceBillTypeCol = new RGridColumnDef(PInvoiceRegLineDef.constants.acc1_sourceBill_billType());
    sourceBillTypeCol.setWidth("90px");
    grid.addColumnDef(sourceBillTypeCol);

    unregTotalCol = new RGridColumnDef(PInvoiceRegLineDef.constants.unregTotal_total());
    unregTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unregTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    unregTotalCol.setWidth("90px");
    grid.addColumnDef(unregTotalCol);

    unregTaxCol = new RGridColumnDef(PInvoiceRegLineDef.constants.unregTotal_tax());
    unregTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unregTaxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    unregTaxCol.setWidth("90px");
    grid.addColumnDef(unregTaxCol);

    regTotalCol = new RGridColumnDef(PInvoiceRegLineDef.constants.total_total());
    regTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    regTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    regTotalCol.setWidth("90px");
    grid.addColumnDef(regTotalCol);

    regTaxCol = new RGridColumnDef(PInvoiceRegLineDef.constants.total_tax());
    regTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    regTaxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    regTaxCol.setWidth("90px");
    grid.addColumnDef(regTaxCol);

    remarkCol = new RGridColumnDef(PInvoiceRegLineDef.constants.remark());
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  public void refresh(BInvoiceReg entity) {
    assert entity != null;
    this.entity = entity;

    grid.refresh();
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCol)) {
        BInvoiceRegLine line = entity.getLines().get(grid.getCurrentRow());
        if (line == null)
          return;
        if (line.getAcc1().getSubject() == null
            || StringUtil.isNullOrBlank(line.getAcc1().getSubject().getUuid()))
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID,
            line.getAcc1().getSubject().getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = InvoiceRegMessage.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    }
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      if (entity == null || entity.getLines() == null) {
        return 0;
      }
      return entity.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entity.getLines() == null || entity.getLines().isEmpty())
        return null;

      BInvoiceRegLine line = entity.getLines().get(row);

      if (col == lineNumberCol.getIndex()) {
        return line.getLineNumber();
      } else if (col == subjectCol.getIndex()) {
        return line.getAcc1().getSubject() == null ? null : line.getAcc1().getSubject()
            .toFriendlyStr();
      } else if (col == sourceBillNumberCol.getIndex()) {
        if (line.getAcc1().getSourceBill() == null) {
          return null;
        }

        BDispatch dispatch = new BDispatch(line.getAcc1().getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), line.getAcc1().getSourceBill().getBillNumber());
        return dispatch;
      } else if (col == sourceBillTypeCol.getIndex()) {
        if (line.getAcc1().getSourceBill() == null
            || line.getAcc1().getSourceBill().getBillType() == null)
          return null;
        BBillType type = EPRecInvoiceReg.getInstance().getBillTypeMap()
            .get(line.getAcc1().getSourceBill().getBillType());
        if (type == null)
          return null;
        return type.getCaption();
      } else if (col == unregTotalCol.getIndex()) {
        return line.getUnregTotal() == null ? null : line.getUnregTotal().getTotal().doubleValue();
      } else if (col == unregTaxCol.getIndex()) {
        return line.getUnregTotal() == null ? null : line.getUnregTotal().getTax().doubleValue();
      } else if (col == regTotalCol.getIndex()) {
        return line.getRegTotal() == null ? null : line.getRegTotal().getTotal().doubleValue();
      } else if (col == regTaxCol.getIndex()) {
        return line.getRegTotal() == null ? null : line.getRegTotal().getTax().doubleValue();
      } else if (col == remarkCol.getIndex()) {
        return line.getRemark();
      }
      return null;
    }
  }
}
