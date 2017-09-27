/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2015-10-9 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 滞纳金明细编辑表格
 * 
 * @author lixiaohong
 * 
 */
public class OverdueLineEditGrid extends RCaptionBox implements RActionHandler, HasRActionHandlers {

  private EPReceipt ep = EPReceipt.getInstance();
  private BPayment bill;
  /** 账款明细行 */
  private List<BPaymentOverdueLine> lines;
  private int currentRow = -1;

  private Label overdueTotalNumber;
  private RAction deleteAction;

  private EditGrid<BPaymentOverdueLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef contractBillNumberCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef overdueTotalCol;
  private RGridColumnDef calcTotalCol;
  private RGridColumnDef overdueTaxCol;
  private RGridColumnDef remarkCol;

  private Handler_click clickHandler = new Handler_click();

  public BPayment getBill() {
    return bill;
  }

  public void setBill(BPayment bill) {
    this.bill = bill;
  }

  public void onHide() {
    overdueTotalNumber.setText(ReceiptMessages.M.resultTotal(0));
  }

  public OverdueLineEditGrid() {
    super();
    setContent(drawGrid());
    setEditing(true);
    setCaption(PPaymentLineDef.TABLE_CAPTION);
    setStyleName(RCaptionBox.STYLENAME_STANDARD);
    setBorder(false);
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);

    overdueTotalNumber = new Label(ReceiptMessages.M.resultTotal(0));
    getCaptionBar().addButton(overdueTotalNumber);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);
  }

  private Widget drawGrid() {
    grid = new EditGrid<BPaymentOverdueLine>();
    grid.setCaption(PPaymentLineDef.TABLE_CAPTION);
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.addClickHandler(new Handler_grid());
    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    CellRendererFactory rendererFactory = new CellRendererFactory();

    lineNumberCol = new RGridColumnDef(PPaymentOverdueLineDef.lineNumber);
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    lineNumberCol.setResizable(true);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentOverdueLineDef.subject);
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("130px"));
    subjectCol.setOverflowEllipsis(true);
    subjectCol.setResizable(true);
    subjectCol.setWidth("150px");
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
    overdueTotalCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(overdueTotalCol);

    calcTotalCol = new RGridColumnDef(ReceiptMessages.M.calculate_total());
    calcTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    calcTotalCol.setWidth("90px");
    calcTotalCol.setResizable(true);
    grid.addColumnDef(calcTotalCol);

    overdueTaxCol = new RGridColumnDef(PPaymentOverdueLineDef.overdueTotal_tax);
    overdueTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    overdueTaxCol.setWidth("90px");
    overdueTaxCol.setResizable(true);
    grid.addColumnDef(overdueTaxCol);
    
    remarkCol = new RGridColumnDef(PPaymentOverdueLineDef.remark);
    remarkCol.setWidth("200px");
    remarkCol.setResizable(true);
    remarkCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      String fieldName = colDef.getName();
      if (fieldName.equals(overdueTotalCol.getName())) {
        return new OverdueTotalRenderer(grid, colDef, row, col, data);
      }
      if (fieldName.equals(remarkCol.getName())) {
        return new RemarkTextRenderer(grid, colDef, row, col, data);
      }
      return null;
    }
  }

  private class OverdueTotalRenderer extends EditGridCellWidgetRenderer {

    public OverdueTotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RNumberBox field;

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox(PPaymentOverdueLineDef.overdueTotal_total);
      field.setSelectAllOnFocus(true);
      field.setWidth("100%");
      field.setFormat(GWTFormat.fmt_money);
      field.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          if (field.isValid()) {
            BPaymentOverdueLine value = bill.getOverdueLines().get(getRow());
            value.getTotal().setTotal(field.getValueAsBigDecimal());
            value.getTotal().setTax(BTaxCalculator.tax(value.getTotal().getTotal(), value.getTaxRate()));
            grid.refreshRow(getRow());
            RActionEvent.fire(OverdueLineEditGrid.this,
                ActionName.ACTION_BILLOVERDUELINE_UNPAYEDTOTALCHANGE);
          }
        }
      });
      field.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BPaymentOverdueLine detail = bill.getOverdueLines().get(getRow());
          if (field.getValueAsBigDecimal() == null) {
            return Message.error(ReceiptMessages.M.notNull(PPaymentOverdueLineDef.constants
                .overdueTotal_total()));
          } else if (detail.getUnpayedTotal() != null
              && detail.getUnpayedTotal().getTotal() != null
              && (detail.getUnpayedTotal().getTotal().multiply(field.getValueAsBigDecimal())
                  .compareTo(BigDecimal.ZERO) < 0)) {
            return Message.error(ReceiptMessages.M.sameSign(
                PPaymentOverdueLineDef.constants.overdueTotal_total(),
                ReceiptMessages.M.calculate_total()));
          } else if (detail.getUnpayedTotal() != null
              && detail.getUnpayedTotal().getTotal() != null
              && (field.getValueAsBigDecimal().abs()
                  .compareTo(detail.getUnpayedTotal().getTotal().abs()) > 0)) {
            return Message.error(ReceiptMessages.M.absCannotMoreThan(
                PPaymentOverdueLineDef.constants.overdueTotal_total(),
                ReceiptMessages.M.calculate_total()));
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentOverdueLine detail = bill.getOverdueLines().get(getRow());
      field.setValue(detail.getTotal().getTotal());
    }

  }

  private class RemarkTextRenderer extends EditGridCellWidgetRenderer {
    private RTextBox field;

    public RemarkTextRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler(false));
      field.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BPaymentOverdueLine value = bill.getOverdueLines().get(getRow());
          value.setRemark(field.getValue());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentOverdueLine detail = bill.getOverdueLines().get(getRow());
      field.setValue(detail.getRemark());
    }

  }

  public void refreshData() {
    lines = bill.getOverdueLines();
    grid.setDefaultDataRowCount(0);
    grid.setValues(lines);
    if (lines.size() > 0) {
      currentRow = 0;
    }
    doRefresh();
  }

  private void doRefresh() {
    refreshLineNumber();
    grid.refresh();
    grid.setCurrentRow(currentRow);
    overdueTotalNumber.setText(ReceiptMessages.M.resultTotal(bill.getOverdueLines().size()));
  }

  private void refreshLineNumber() {
    for (int i = 0; i < lines.size(); i++) {
      BPaymentOverdueLine line = lines.get(i);
      line.setLineNumber(i + 1);
    }
  }

  public void doDelete() {
    List list = grid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(ReceiptMessages.M.seleteDataToAction(ReceiptMessages.M.delete(),
          ReceiptMessages.M.line()));
      return;
    }

    RMsgBox.showConfirm(
        ReceiptMessages.M.actionComfirm(ReceiptMessages.M.delete(),
            ReceiptMessages.M.selectedRows()), true, new RMsgBox.ConfirmCallback() {
          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed) {
              doDeleteRows();
              doRefresh();
              RActionEvent.fire(OverdueLineEditGrid.this,
                  ActionName.ACTION_BILLOVERDUELINE_UNPAYEDTOTALCHANGE);
            }
          }
        });
  }

  private void doDeleteRows() {
    List<Integer> selections = grid.getSelections();
    boolean deleteCurLine = false;
    for (int i = selections.size() - 1; i >= 0; i--) {
      int row = selections.get(i).intValue();
      BPaymentOverdueLine line = lines.get(i);
      doRefreshPaymentBeforDelete(line);
      lines.remove(row);

      if (row == currentRow)
        deleteCurLine = true;
    }
    // 重置收款单单头中的产生的预存款金额
    bill.aggregate(ep.getScale(), ep.getRoundingMode());

    if (deleteCurLine)
      currentRow = lines.size() - 1;
    else {
      for (Integer row : grid.getSelections()) {
        if (row.intValue() < currentRow)
          currentRow--;
      }
    }
  }

  /**
   * 扣除收款单总的应收金额和滞纳金。如果是按科目收款，会扣除收款单总的实收金额。
   * 
   * @param line
   */
  private void doRefreshPaymentBeforDelete(BPaymentOverdueLine line) {
    // 扣除滞纳金
    bill.setOverdueTotal(bill.getOverdueTotal().subtract(line.getTotal()));
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;
      if (cell.getColumnDef().equals(subjectCol)) {
        BUCN subject = lines.get(cell.getRow()).getSubject();
        if (subject == null || StringUtil.isNullOrBlank(subject.getUuid()))
          return;
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
      EditGridDataProduce<BPaymentOverdueLine> {
    @Override
    public int getRowCount() {
      return lines == null ? 0 : lines.size();
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
      else if (col == overdueTotalCol.getIndex())
        return line.getTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getTotal().getTotal().doubleValue());
      else if (col == calcTotalCol.getIndex())
        return line.getUnpayedTotal().getTotal() == null ? null : GWTFormat.fmt_money.format(line
            .getUnpayedTotal().getTotal().doubleValue());
      else if (col == overdueTaxCol.getIndex())
        return line.getTotal().getTax() == null ? null : GWTFormat.fmt_money.format(line.getTotal()
            .getTax().doubleValue());
      else if (col == remarkCol.getIndex())
        return line.getRemark();
      else
        return null;
    }

    @Override
    public BPaymentOverdueLine create() {
      BPaymentOverdueLine detail = new BPaymentOverdueLine();
      return detail;
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.ACTION_LINEOVERDUELINE_CASHDEFRAYALCHANGE
        || event.getActionName() == ActionName.ACTION_LINEOVERDUELINE_DEPOSITDEFRAYALCHANGE) {
      if (currentRow >= 0 && currentRow < lines.size()) {
        grid.refreshRow(currentRow);
        RActionEvent.fire(this, ActionName.ACTION_OVERDUELINE_AGGREGATE);
      }
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE
        || event.getActionName() == ActionName.ACTION_BILLACCOUNTLINE2_ADD) {
      refreshData();
    }
  }

  public void refreshGrid() {
    taxRateCol.setVisible(ep.getConfig().isShowTax());
    overdueTaxCol.setVisible(ep.getConfig().isShowTax());
    grid.rebuild();
  }

  /**
   * 设置当前行
   * 
   * @param row
   */
  public void setCurrentRow(int row) {
    if (row >= grid.getRowCount() || row < 0)
      return;

    currentRow = row;
    grid.setCurrentRow(currentRow);
    grid.refreshRow(currentRow);
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(deleteAction)) {
        doDelete();
      }
    }
  }
}
