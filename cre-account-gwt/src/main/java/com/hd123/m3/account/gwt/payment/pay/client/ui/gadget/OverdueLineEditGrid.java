/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridCellWidgetFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridColumnDef;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridDataProvider;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RHyperlinkField;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
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
 * @author subinzhu
 * 
 */
public class OverdueLineEditGrid extends RCaptionBox implements HasRActionHandlers {

  private BPayment bill;
  private EPPayment ep = EPPayment.getInstance();

  private Label totalNumberField;

  private EditGrid<BPaymentOverdueLine> editGrid;
  private EditGridColumnDef lineNumberCol;
  private EditGridColumnDef subjectCol;
  private EditGridColumnDef contractBillNumberCol;
  private EditGridColumnDef taxRateCol;
  private EditGridColumnDef overdueTotalCol;
  private EditGridColumnDef overdueTaxCol;
  private EditGridColumnDef calcTotalCol;
  private EditGridColumnDef remarkCol;

  private RAction deleteAction;

  private Handler_click clickHandler = new Handler_click();

  public OverdueLineEditGrid() {
    super();
    setCaption(PPaymentLineDef.TABLE_CAPTION);
    setWidth("100%");
    setEditing(true);

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    Widget w = drawGrid();
    panel.add(w);

    totalNumberField = new HTML(PaymentMessages.M.resultTotal(0));
    getCaptionBar().addButton(RSimplePanel.decorateMargin(totalNumberField, 0, 0, 0, 10));

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

    setContent(panel);
  }

  private Widget drawGrid() {
    editGrid = new EditGrid<BPaymentOverdueLine>();
    editGrid.setWidth("100%");
    editGrid.setDisplayWidgetStyle(true);
    editGrid.setShowRowSelector(true);
    editGrid.addColumnDefs(createColumnDef());
    editGrid.setShowAddRemoveColumn(false);
    editGrid.setDefaultDataRowCount(1);
    editGrid.setProvider(new DataProvider());
    editGrid.setCellWidgetFactory(new CellWidgetFactory());

    return editGrid;
  }

  private List<EditGridColumnDef> createColumnDef() {
    lineNumberCol = new EditGridColumnDef(PPaymentOverdueLineDef.lineNumber, "20px");
    subjectCol = new EditGridColumnDef(PPaymentOverdueLineDef.subject, true, "150px");
    contractBillNumberCol = new EditGridColumnDef(PPaymentOverdueLineDef.contract_billNumber, true,
        BillConfig.COLUMNWIDTH_BILLNUMBER);
    taxRateCol = new EditGridColumnDef(PPaymentOverdueLineDef.taxRate, "100px");
    overdueTotalCol = new EditGridColumnDef(PPaymentOverdueLineDef.overdueTotal_total, true, "90px");
    overdueTaxCol = new EditGridColumnDef(PPaymentOverdueLineDef.overdueTotal_tax, "90px");
    calcTotalCol = new EditGridColumnDef(PaymentMessages.M.calculate_total(), "calculate.total",
        false, "60px", HasHorizontalAlignment.ALIGN_RIGHT);
    remarkCol = new EditGridColumnDef(PPaymentOverdueLineDef.remark, true, "200px");

    if (ep.getConfig().isShowTax()) {
      return Arrays.asList(lineNumberCol, subjectCol, contractBillNumberCol, taxRateCol,
          overdueTotalCol, overdueTaxCol, calcTotalCol, remarkCol);
    } else {
      return Arrays.asList(lineNumberCol, subjectCol, contractBillNumberCol, overdueTotalCol,
          calcTotalCol, remarkCol);
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public void setValue(BPayment bill) {
    this.bill = bill;
    editGrid.rebuild();
    if (bill != null && bill.getOverdueLines().isEmpty() == false) {
      editGrid.setCurrentRow(0);
    }
  }

  public void refresh() {
    editGrid.refresh();
  }

  @Override
  public boolean validate() {
    clearValidResults();
    boolean valid = super.validate();

    for (int i = 0; i < bill.getOverdueLines().size(); i++) {
      BPaymentOverdueLine line = bill.getOverdueLines().get(i);
      for (int col = 0; col < editGrid.getColumnCount(); col++) {
        if (editGrid.getWidget(i + 1, col) instanceof RNumberBox) {
          valid = validateOverdueTotal((RNumberBox) editGrid.getWidget(i + 1, col), line);
        }
      }
    }

    return valid;
  }

  private class DataProvider implements EditGridDataProvider<BPaymentOverdueLine> {

    @Override
    public List<BPaymentOverdueLine> getData() {
      totalNumberField.setText(PaymentMessages.M.resultTotal(bill == null ? 0 : bill
          .getOverdueLines().size()));
      return bill == null ? new ArrayList<BPaymentOverdueLine>() : bill.getOverdueLines();
    }

    @Override
    public Object getData(int row, String colName) {
      if (bill == null || bill.getOverdueLines().isEmpty() || row < 0
          || row >= bill.getOverdueLines().size())
        return null;

      BPaymentOverdueLine rowData = bill.getOverdueLines().get(row);
      if (colName.equals(lineNumberCol.getName())) {
        return row + 1;
      } else if (colName.equals(subjectCol.getName())) {
        return rowData.getSubject() == null ? null : rowData.getSubject().toFriendlyStr();
      } else if (colName.equals(contractBillNumberCol.getName())) {
        return rowData.getContract() == null ? null : rowData.getContract().getCode();
      } else if (colName.equals(taxRateCol.getName())) {
        return rowData.getTaxRate() == null ? null : rowData.getTaxRate().toString();
      } else if (colName.equals(overdueTotalCol.getName())) {
        return rowData.getTotal() == null ? null : rowData.getTotal().getTotal();
      } else if (colName.equals(overdueTaxCol.getName())) {
        return rowData.getTotal() == null ? null : rowData.getTotal().getTax();
      } else if (colName.equals(calcTotalCol.getName())) {
        return rowData.getUnpayedTotal().getTotal() == null ? null : GWTFormat.fmt_money
            .format(rowData.getUnpayedTotal().getTotal().doubleValue());
      } else if (colName.equals(remarkCol.getName())) {
        return rowData.getRemark();
      }
      return null;
    }

    @Override
    public BPaymentOverdueLine create() {
      return null;
    }
  }

  private class CellWidgetFactory implements EditGridCellWidgetFactory<BPaymentOverdueLine> {
    @Override
    public Widget createEditWidget(FieldDef field, int dataRowIdx, BPaymentOverdueLine rowData) {
      if (field.getName().equals(subjectCol.getName())) {
        RHyperlinkField subjcetField = new RHyperlinkField();
        subjcetField.addClickHandler(new SubjectHandler(rowData.getSubject()));
        return subjcetField;
      } else if (field.getName().equals(contractBillNumberCol.getName())) {
        DispatchLinkField contractField = new DispatchLinkField();
        contractField.setLinkKey(GRes.R.dispatch_key());
        if (rowData.getContract() != null) {
          String type = BCounterpart.COUNPERPART_PROPRIETOR.equals(bill.getCounterpart()
              .getCounterpartType()) ? GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE
              : GRes.FIELDNAME_CONTRACT_BILLTYPE;
          contractField.setValue(type, rowData.getContract().getCode());
        }
        return contractField;
      } else if (field.getName().equals(overdueTotalCol.getName())) {
        final RNumberBox overdueTotalField = new RNumberBox(
            PPaymentOverdueLineDef.overdueTotal_total);
        overdueTotalField.setSelectAllOnFocus(true);
        overdueTotalField.setMinValue(0, true);
        overdueTotalField.setFormat(GWTFormat.fmt_money);
        overdueTotalField.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
        overdueTotalField.addChangeHandler(new OverdueTotalChange(dataRowIdx));
        return overdueTotalField;
      } else if (field.getName().equals(remarkCol.getName())) {
        RTextBox remarkField = new RTextBox(PPaymentOverdueLineDef.remark);
        remarkField.setSelectAllOnFocus(true);
        remarkField.addChangeHandler(new RemarkChange(dataRowIdx));
        return remarkField;
      }
      return null;
    }
  }

  private class SubjectHandler implements ClickHandler {

    private BUCN subject;

    public SubjectHandler(BUCN subject) {
      this.subject = subject;
    }

    @Override
    public void onClick(ClickEvent event) {
      if (subject == null || StringUtil.isNullOrBlank(subject.getUuid()))
        return;
      GwtUrl url = SubjectUrlParams.ENTRY_URL;
      url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
      url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subject.getUuid());
      try {
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
      } catch (Exception e) {
        String msg = PaymentMessages.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
        RMsgBox.showError(msg, e);
      }
    }
  }

  private class OverdueTotalChange implements ChangeHandler {

    private int row;

    public OverdueTotalChange(int row) {
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentOverdueLine line = bill.getOverdueLines().get(row);
      if (line == null)
        return;

      RNumberBox overdueTotalField = (RNumberBox) event.getSource();
      line.getTotal().setTotal(overdueTotalField.getValueAsBigDecimal());

      if (validateOverdueTotal(overdueTotalField, line)) {
        overdueTotalField.clearValidResults();
        if (line.getTotal().getTotal().compareTo(line.getUnpayedTotal().getTotal()) < 0) {
          line.getTotal().setTax(
              BTaxCalculator.tax(line.getTotal().getTotal(), line.getTaxRate(), ep.getScale(),
                  ep.getRoundingMode()));
        } else if (line.getTotal().getTotal().compareTo(line.getUnpayedTotal().getTotal()) == 0) {
          line.setTotal(line.getUnpayedTotal().clone());
        }
      }
      editGrid.refreshGridRow(row);
      RActionEvent.fire(OverdueLineEditGrid.this, ActionName.ACTION_OVERDUELINE_AGGREGATE);
    }
  }

  private boolean validateOverdueTotal(RNumberBox overdueTotalField, BPaymentOverdueLine line) {
    if (line.getTotal().getTotal() == null) {
      overdueTotalField.addErrorMessage(PaymentMessages.M.notNull(PPaymentOverdueLineDef.constants
          .overdueTotal_total()));
      return false;
    } else if (line.getUnpayedTotal().getTotal() != null
        && line.getTotal().getTotal().abs().compareTo(line.getUnpayedTotal().getTotal().abs()) > 0) {
      overdueTotalField.addErrorMessage(PaymentMessages.M.absCannotMoreThan(
          PPaymentOverdueLineDef.constants.overdueTotal_total(),
          PaymentMessages.M.calculate_total()));
      return false;
    } else if (line.getUnpayedTotal().getTotal() != null
        && line.getTotal().getTotal().multiply(line.getUnpayedTotal().getTotal())
            .compareTo(BigDecimal.ZERO) < 0) {
      overdueTotalField.addErrorMessage(PaymentMessages.M
          .signSymbolMustBeSameAs(PPaymentOverdueLineDef.constants.overdueTotal_total()));
      return false;
    }
    return true;
  }

  private class RemarkChange implements ChangeHandler {

    private int row;

    public RemarkChange(int row) {
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentOverdueLine line = bill.getOverdueLines().get(row);
      if (line == null)
        return;
      RTextBox box = (RTextBox) event.getSource();
      line.setRemark(box.getValue());
    }
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(deleteAction)) {
        doDelete();
      }
    }
  }

  private void doDelete() {
    List<Integer> list = editGrid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(PaymentMessages.M.seleteDataToAction(PaymentMessages.M.delete(),
          PaymentMessages.M.line()));
      return;
    }

    RMsgBox.showConfirm(
        PaymentMessages.M.actionComfirm(PaymentMessages.M.delete(),
            PaymentMessages.M.selectedRows()), true, new RMsgBox.ConfirmCallback() {
          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed) {
              doDeleteRows();
              editGrid.refresh();
            }
          }
        });
  }

  private void doDeleteRows() {
    List<Integer> selections = editGrid.getSelections();
    for (int i = selections.size() - 1; i >= 0; i--) {
      int row = selections.get(i);
      BPaymentOverdueLine line = bill.getOverdueLines().get(i);
      doRefreshPaymentBeforDelete(line);
      bill.getOverdueLines().remove(row);
    }

    RActionEvent.fire(OverdueLineEditGrid.this, ActionName.ACTION_OVERDUELINE_AGGREGATE);
    RActionEvent.fire(OverdueLineEditGrid.this, ActionName.ACTION_OVERDUELINE_REFRESHACCOUNT);
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

  public class ActionName {
    /** 合计 */
    public static final String ACTION_OVERDUELINE_AGGREGATE = "overdueLine_aggregate";
    /** 刷新账款明细 */
    public static final String ACTION_OVERDUELINE_REFRESHACCOUNT = "overdueLine_refreshAccounts";
  }
}
