/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAccountDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementAccFilter;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementLineDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateRangeField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * 账款查询弹出框
 * 
 * @author huangjunxian
 * 
 */
public class StatementAccountDialog extends RBrowserDialog<BStatementLine> {

  private StatementAccFilter filter = new StatementAccFilter();
  private Collection<String> excludedUuids;
  private String counterpartUuid;
  private String contractUuid;
  private AccountFilterCallbackImpl filterCallback;

  private RGridColumnDef subjectCol = new RGridColumnDef(StatementMessages.M.account_Subject(),
      StatementUrlParams.AccountFilter.FIELD_SUBJECT);
  private RGridColumnDef directionCol = new RGridColumnDef(
      PStatementLineDef.acc1_direction.getCaption(),
      StatementUrlParams.AccountFilter.FIELD_DIRECTION);
  private RGridColumnDef totalCol = new RGridColumnDef(StatementMessages.M.account_total(),
      StatementUrlParams.AccountFilter.FIELD_TOTAL);
  private RGridColumnDef taxCol = new RGridColumnDef(PStatementLineDef.total_tax.getCaption(),
      StatementUrlParams.AccountFilter.FIELD_TAX);
  private RGridColumnDef invoiceCol = new RGridColumnDef(StatementMessages.M.isInvoiced(), "");
  private RGridColumnDef dateRangeCol = new RGridColumnDef(StatementMessages.M.dateRange(),
      StatementUrlParams.AccountFilter.FIELD_DATERANGE);
  private RGridColumnDef billTypeCol = new RGridColumnDef(
      PStatementLineDef.acc1_sourceBill_billType.getCaption(),
      StatementUrlParams.AccountFilter.FIELD_SOURCEBILLTYPE);
  private RGridColumnDef billNumberCol = new RGridColumnDef(
      PStatementLineDef.acc1_sourceBill.getCaption(),
      StatementUrlParams.AccountFilter.FIELD_SOURCEBILLNUMBER);

  public StatementAccountDialog() {
    super(StatementMessages.M.account_Subject(), PStatementLineDef.acc1_direction.getCaption(),
        StatementMessages.M.account_total(), PStatementLineDef.total_tax.getCaption(),
        StatementMessages.M.isInvoiced(), StatementMessages.M.dateRange(),
        PStatementLineDef.acc1_sourceBill_billType.getCaption(), PStatementLineDef.acc1_sourceBill
            .getCaption());

    initialColDefs();
    setColumnDefs(subjectCol, directionCol, totalCol, taxCol, invoiceCol, dateRangeCol,
        billTypeCol, billNumberCol);
    setWidth("800px");
    setProvider(new DataProvider());
    setSingleSelection(false);
    getGrid().setAllColumnsOverflowEllipsis(true);
    getGrid().getColumnDef(4).setSortable(false);

    filterCallback = new AccountFilterCallbackImpl();
    setFilterCallback(filterCallback);
    setFilterStyle(FILTER_STYLE_PANEL);
    setCaption(StatementMessages.M.choose() + StatementMessages.M.account());
  }

  @Override
  public void hide() {
    super.hide();
    clearConditions();
  }


  private void initialColDefs() {
    subjectCol.setWidth("100px");
    directionCol.setWidth("80px");
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("80px");
    taxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    taxCol.setWidth("80px");
    invoiceCol.setWidth("80px");
    dateRangeCol.setWidth("120px");
    billTypeCol.setWidth("100px");
    billNumberCol.setWidth("120px");
  }

  private class DataProvider implements RPageDataProvider<BStatementLine> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BStatementLine>> callback) {
      filter.setCounterpartUuid(counterpartUuid);
      filter.setContractUuid(contractUuid);
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null)
        filter.appendOrder(sortField, sortDir);
      StatementService.Locator.getService().queryAccount(filter, excludedUuids, callback);
    }

    @Override
    public Object getData(int row, int col, BStatementLine rowData, List<BStatementLine> pageData) {
      if (rowData.getAcc1() == null)
        return null;
      switch (col) {
      case 0:
        return rowData.getAcc1().getSubject() == null ? null : rowData.getAcc1().getSubject()
            .toFriendlyStr();
      case 1:
        return DirectionType.getCaptionByValue(rowData.getAcc1().getDirection());
      case 2:
        return rowData.getTotal() == null ? null : buildTotalStr(rowData.getTotal().getTotal());
      case 3:
        return rowData.getTotal() == null ? null : buildTotalStr(rowData.getTotal().getTax());
      case 4:
        return rowData.getInvoice() ? StatementMessages.M.yes() : StatementMessages.M.no();
      case 5:
        return buildDateRange(rowData.getAcc1().getBeginTime(), rowData.getAcc1().getEndTime());
      case 6:
        return rowData.getAcc1().getSourceBill() == null ? null : EPStatement.getInstance()
            .getBillType().get(rowData.getAcc1().getSourceBill().getBillType());
      case 7:
        return rowData.getAcc1().getSourceBill() == null ? null : rowData.getAcc1().getSourceBill()
            .getBillNumber();
      default:
        return null;
      }
    }
  }

  private String buildDateRange(Date beginDate, Date endDate) {
    if (beginDate == null && endDate == null)
      return null;
    StringBuffer sb = new StringBuffer();
    if (beginDate != null)
      sb.append(M3Format.fmt_yMd.format(beginDate));
    sb.append("~");
    if (endDate != null)
      sb.append(M3Format.fmt_yMd.format(endDate));
    return sb.toString();
  }

  private String buildTotalStr(BigDecimal value) {
    if (value == null)
      return null;
    return M3Format.fmt_money.format(value.doubleValue());
  }

  private class AccountFilterCallbackImpl implements RFilterCallback {

    private RComboBox<String> billTypeField;
    private RTextBox billNumberField;
    private RTextBox subjectCodeField;
    private RTextBox subjectNameField;
    private RDateRangeField dateRangeField;

    public AccountFilterCallbackImpl() {
    }

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("700px");

      billTypeField = new SourceBillTypeBox();
      billTypeField.setNullOptionText(StatementMessages.M.all());
      billTypeField.setFieldCaption(StatementMessages.M.filter_billType());
      form.addField(billTypeField);

      billNumberField = new RTextBox(StatementMessages.M.filter_billNumber());
      form.addField(billNumberField);

      subjectCodeField = new RTextBox(StatementMessages.M.filter_subjectCode());
      form.addField(subjectCodeField);

      subjectNameField = new RTextBox(StatementMessages.M.filter_subjectName());
      form.addField(subjectNameField);

      dateRangeField = new RDateRangeField(StatementMessages.M.filter_dateRange());
      form.addField(dateRangeField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      billTypeField.clearValue();
      billNumberField.clearValue();
      subjectCodeField.clearValue();
      subjectNameField.clearValue();
      dateRangeField.clearValue();

    }

    @Override
    public void onQuery() {
      filter.setSourceBillType(billTypeField.getValue());
      filter.setSourceBillNumber(billNumberField.getValue());
      filter.setSubjectCode(subjectCodeField.getValue());
      filter.setSubjectName(subjectNameField.getValue());
      filter.setBeginDate(dateRangeField.getStartDate());
      filter.setEndDate(dateRangeField.getEndDate());
    }
  }

  public void center(String statementUuid, String counterpartUuid, String contractUuid,
      Collection<String> excludedUuids) {
    this.filter.setStatementUuid(statementUuid);
    this.counterpartUuid = counterpartUuid;
    this.contractUuid = contractUuid;
    this.excludedUuids = excludedUuids;
    center();
  }
}
