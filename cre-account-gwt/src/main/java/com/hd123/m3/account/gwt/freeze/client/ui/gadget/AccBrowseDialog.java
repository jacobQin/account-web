/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccBrowseDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.freeze.client.EPFreeze;
import com.hd123.m3.account.gwt.freeze.client.rpc.FreezeService;
import com.hd123.m3.account.gwt.freeze.client.ui.filter.AccountFilter;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * 账款冻结单-选择账款Dialog
 * 
 * @author zhuhairui
 * 
 */
public class AccBrowseDialog extends RBrowserDialog<BAccount> {

  private EPFreeze ep = EPFreeze.getInstance();
  private PageDataProvider provider;
  private List<String> accUuids = new ArrayList<String>();
  private String counterpartUuid;
  private String accountUnitUuid;
  private String counterpartType;

  private static RGridColumnDef subjectCol;
  private static RGridColumnDef sourceBillTypeCol;
  private static RGridColumnDef sourceBillCol;
  private static RGridColumnDef contractNumCol;
  private static RGridColumnDef totalCol;

  public static final String FIELD_SUBJECT = "subject.code";
  public static final String FIELD_SOURCEBILLTYPE = "acc1.sourceBill.billType";
  public static final String FIELD_SOURCEBILLNUMBER = "acc1.sourceBill.billNumber";
  public static final String FIELD_CONTRACT = "contract.billNumber";
  public static final String FIELD_TOTAL = "total.total";

  static {
    subjectCol = new RGridColumnDef(WidgetRes.M.accountSubject(), FIELD_SUBJECT);
    sourceBillTypeCol = new RGridColumnDef(WidgetRes.M.sourceBillType(), FIELD_SOURCEBILLTYPE);
    sourceBillCol = new RGridColumnDef(WidgetRes.M.sourceBill(), FIELD_SOURCEBILLNUMBER);
    contractNumCol = new RGridColumnDef(WidgetRes.M.contractNum(), FIELD_CONTRACT);
    totalCol = new RGridColumnDef(WidgetRes.M.total(), FIELD_TOTAL);
  }

  public AccBrowseDialog() {
    super(subjectCol, sourceBillTypeCol, sourceBillCol, contractNumCol, totalCol);
    setCaption(WidgetRes.M.seleteData2(WidgetRes.M.account()));

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
    setSingleSelection(false);

    getGrid().setAllColumnsOverflowEllipsis(true);
  }

  private class PageDataProvider implements RPageDataProvider<BAccount>, RFilterCallback {
    private AccountFilter filter = new AccountFilter();
    private RTextBox statementField;
    private RTextBox invoiceBillNumberField;
    private RTextBox contractNumField;
    private RTextBox invoiceCodeField;
    private RTextBox invoiceNumField;
    private SubjectUCNBox subjectField;
    private SourceBillTypeBox sourceBillTypeField;
    private RTextBox sourceBillNumberField;

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("750px");

      statementField = new RTextBox(WidgetRes.M.statementStart());
      form.addField(statementField);

      invoiceBillNumberField = new RTextBox(WidgetRes.M.invoiceBillNumStart());
      form.addField(invoiceBillNumberField);

      contractNumField = new RTextBox(WidgetRes.M.contractNumStart());
      form.addField(contractNumField);

      invoiceCodeField = new RTextBox();
      invoiceNumField = new RTextBox();
      form.addField(new RCombinedField() {
        {
          setCaption(WidgetRes.M.invoiceCodeAndNumStart());
          addField(invoiceCodeField, 0.5f);
          addField(invoiceNumField, 0.5f);
        }
      });

      subjectField = new SubjectUCNBox(BSubjectType.credit.name(), null);
      form.addField(subjectField);

      sourceBillTypeField = new SourceBillTypeBox();
      sourceBillNumberField = new RTextBox();
      form.addField(new RCombinedField() {
        {
          setCaption(WidgetRes.M.sourceBillTypeAndNum());
          addField(sourceBillTypeField, 0.5f);
          addField(sourceBillNumberField, 0.5f);
        }
      });
    }

    @Override
    public void clearConditions() {
      if (statementField != null)
        statementField.clearValue();
      if (invoiceBillNumberField != null)
        invoiceBillNumberField.clearValue();
      if (contractNumField != null)
        contractNumField.clearValue();
      if (invoiceCodeField != null)
        invoiceCodeField.clearValue();
      if (invoiceNumField != null)
        invoiceNumField.clearValue();
      if (subjectField != null) {
        subjectField.clearValue();
        subjectField.clearValidResults();
      }
      if (sourceBillTypeField != null)
        sourceBillTypeField.clearValue();
      if (sourceBillNumberField != null)
        sourceBillNumberField.clearValue();
      if (filter != null)
        filter.clear();
    }

    @Override
    public void onQuery() {
      if (statementField != null)
        filter.setStatementNum(statementField.getValue());
      if (invoiceBillNumberField != null)
        filter.setInvoiceBillNum(invoiceBillNumberField.getValue());
      if (contractNumField != null)
        filter.setContractNum(contractNumField.getValue());
      if (invoiceCodeField != null)
        filter.setInvoiceCode(invoiceCodeField.getValue());
      if (invoiceNumField != null)
        filter.setInvoiceNumber(invoiceNumField.getValue());
      if (subjectField != null)
        filter.setSubjectUuid(subjectField.getValue().getUuid());
      if (sourceBillTypeField != null)
        filter.setSourceBillType(sourceBillTypeField.getValue());
      if (sourceBillNumberField != null)
        filter.setSourceBillNumber(sourceBillNumberField.getValue());
      if (StringUtil.isNullOrBlank(counterpartUuid) == false)
        filter.setCounterpartUuid(counterpartUuid);
      if (StringUtil.isNullOrBlank(accountUnitUuid) == false)
        filter.setAccountUnitUuid(accountUnitUuid);
      if (StringUtil.isNullOrBlank(counterpartType) == false)
        filter.setCounterpartType(counterpartType);
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccount>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(subjectCol.getName(), OrderDir.asc);
      }

      filter.setCounterpartUuid(counterpartUuid);
      filter.setAccountUnitUuid(accountUnitUuid);
      filter.setCounterpartType(counterpartType);

      FreezeService.Locator.getService().queryAccount(filter, accUuids, callback);
    }

    @Override
    public Object getData(int row, int col, BAccount rowData, List<BAccount> pageData) {
      switch (col) {
      case 0:
        return rowData.getAcc1() != null ? rowData.getAcc1().getSubject() != null ? rowData
            .getAcc1().getSubject().getNameCode() : null : null;
      case 1:
        return rowData.getAcc1() != null ? rowData.getAcc1().getSourceBill() != null ? rowData
            .getAcc1().getSourceBill().getBillType() != null ? ep.getBillType().get(
            rowData.getAcc1().getSourceBill().getBillType()) : null : null : null;
      case 2:
        return rowData.getAcc1() != null ? rowData.getAcc1().getSourceBill() != null ? rowData
            .getAcc1().getSourceBill().getBillNumber() : null : null;
      case 3:
        return rowData.getAcc1() != null ? rowData.getAcc1().getContract() != null ? rowData
            .getAcc1().getContract().getCode() : null : null;
      case 4:
        return rowData.getTotal() != null ? rowData.getTotal().getTotal() != null ? M3Format.fmt_money
            .format(rowData.getTotal().getTotal().doubleValue()) : BigDecimal.ZERO
            : BigDecimal.ZERO;
      default:
        return null;
      }
    }
  }

  private class SourceBillTypeBox extends RComboBox<String> {
    private SourceBillTypeBox() {
      super();
      setEditable(false);
      setRequired(false);
      setMaxDropdownRowCount(10);
      addOption(null, WidgetRes.M.nullValue());
      for (String key : ep.getBillType().keySet()) {
        addOption(key, ep.getBillType().get(key));
      }
    }
  }

  public void setAccUuids(List<String> accUuids) {
    this.accUuids = accUuids;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  public interface WidgetRes extends CommonsMessages {
    public static WidgetRes M = GWT.create(WidgetRes.class);

    @DefaultMessage("账款科目")
    String accountSubject();

    @DefaultMessage("来源单据类型")
    String sourceBillType();

    @DefaultMessage("来源单据")
    String sourceBill();

    @DefaultMessage("合同编号")
    String contractNum();

    @DefaultMessage("金额")
    String total();

    @DefaultMessage("请选择-{0}")
    String seleteData2(String entityCaption);

    @DefaultMessage("账款")
    String account();

    @DefaultMessage("账单号 起始于")
    String statementStart();

    @DefaultMessage("发票登记单号 起始于")
    String invoiceBillNumStart();

    @DefaultMessage("合同编号 起始于")
    String contractNumStart();

    @DefaultMessage("发票代码\\号码 类似于")
    String invoiceCodeAndNumStart();

    @DefaultMessage("科目 等于")
    String subjectEqual();

    @DefaultMessage("来源单据类型单号 等于")
    String sourceBillTypeAndNum();

    @DefaultMessage("全部")
    String nullValue();

    @DefaultMessage(" 等于")
    String equal();

  }

}
