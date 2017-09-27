/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementBrowseBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatement;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustService;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
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
import com.hd123.rumba.gwt.widget2e.client.form.RBrowseBox;

/**
 * 账单浏览框。
 * 
 * @author zhuhairui
 * 
 */
public class StatementBrowseBox extends RBrowseBox<QStatement> {

  private StatementBrowseCallback callback;
  private Map<String, String> captionMap;
  private Map<String, String> counterpartTypeMap;

  public void setContract(String contract) {
    if (callback != null)
      callback.setContract(contract);
  }

  public void setCounterpart(String counterpart) {
    if (callback != null)
      callback.setCounterpart(counterpart);
  }

  public void setCounterpartType(String counterpartType) {
    if (callback != null)
      callback.setCounterpartType(counterpartType);
  }

  public void setAccountUnitCode(String accountUnit) {
    if (callback != null)
      callback.setAccountUnitCode(accountUnit);
  }

  public StatementBrowseBox(Map<String, String> captionMap, Map<String, String> counterTypeMap) {
    super();
    this.captionMap = captionMap;
    this.counterpartTypeMap = counterTypeMap;
    callback = new StatementBrowseCallback();
    RBrowserDialog<QStatement> browser = new RBrowserDialog<QStatement>(
        WidgetRes.M.selectStatement(), callback, callback, new RGridColumnDef(
            WidgetRes.M.statementNum(), QStatement.FIELD_BILLNUMBER), new RGridColumnDef(
            WidgetRes.M.settleNo(), QStatement.FIELD_SETTLENO), new RGridColumnDef(getFieldCaption(
            GRes.FIELDNAME_COUNTERPART, WidgetRes.M.counterpart()), QStatement.FIELD_COUNTERPART),
        new RGridColumnDef(WidgetRes.M.contractNum(), QStatement.FIELD_CONTRACTNUMBER));

    browser.setFilterStyle(RBrowserDialog.FILTER_STYLE_PANEL);
    browser.setSingleSelection(true);
    setBrowser(browser);

  }

  private String getFieldCaption(String field, String defaultCaption) {
    if (field == null)
      return defaultCaption;
    if (captionMap == null) {
      return defaultCaption;
    } else {
      String caption = captionMap.get(field);
      return caption == null ? defaultCaption : caption;
    }
  }

  private class StatementBrowseCallback implements RPageDataProvider<QStatement>, RFilterCallback {

    private RTextBox statementNumField;
    private RTextBox settleNoField;
    private RCombinedField countpartField;
    private RTextBox counterpartField;
    private RTextBox contractNumField;
    private RTextBox accountUnitField;
    private RComboBox<String> counterpartTypeField;

    private StatementBrowseFilter filter = new StatementBrowseFilter();

    public void setContract(String contract) {
      filter.setContractNum(contract);
      if (contractNumField != null)
        contractNumField.setValue(contract);
    }

    public void setCounterpart(String counterpart) {
      filter.setCounterpart(counterpart);
      if (counterpartField != null)
        counterpartField.setValue(counterpart);
    }

    public void setCounterpartType(String counterpartType) {
      filter.setCounterpartType(counterpartType);
      if (counterpartTypeField != null)
        counterpartTypeField.setValue(counterpartType);
    }

    public void setAccountUnitCode(String accountUnit) {
      filter.setAccountUnit(accountUnit);
      if (accountUnitField != null)
        accountUnitField.setValue(accountUnit);
    }

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("600px");

      statementNumField = new RTextBox(WidgetRes.M.statementNumStartWith());
      form.addField(statementNumField);
      settleNoField = new RTextBox(WidgetRes.M.settleNoEqual());
      form.addField(settleNoField);

      counterpartField = new RTextBox(getFieldCaption(GRes.FIELDNAME_COUNTERPART,
          WidgetRes.M.counterpart())
          + WidgetRes.M.like());
      counterpartField.setValue(filter.getCounterpart());

      counterpartTypeField = new RComboBox<String>(getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE,
          GRes.R.counterpartType()) + WidgetRes.M.captionEquals());
      counterpartTypeField.setEditable(false);
      counterpartTypeField.setNullOptionText(WidgetRes.M.all());
      counterpartTypeField.setMaxDropdownRowCount(10);
      for (Map.Entry<String, String> entry : counterpartTypeMap.entrySet()) {
        counterpartTypeField.addOption(entry.getKey(), entry.getValue());
      }
      counterpartTypeField.setValue(filter.getCounterpartType());

      final String counterpartTypeFieldCaption = getFieldCaption(GRes.FIELDNAME_COUNTERPART,
          WidgetRes.M.counterpart()) + WidgetRes.M.like();
      countpartField = new RCombinedField() {
        {
          setCaption(counterpartTypeFieldCaption);
          addField(counterpartTypeField, 0.4f);
          addField(counterpartField, 0.6f);
        }
      };
      form.addField(countpartField);

      contractNumField = new RTextBox(WidgetRes.M.contractNumStartWith());
      contractNumField.setValue(filter.getContractNum());
      form.addField(contractNumField);

      accountUnitField = new RTextBox(getFieldCaption(GRes.FIELDNAME_BUSINESS,
          WidgetRes.M.accountUnit())
          + WidgetRes.M.like());
      accountUnitField.setValue(filter.getAccountUnit());
      form.addField(accountUnitField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      if (statementNumField != null)
        statementNumField.clearValue();
      if (settleNoField != null)
        settleNoField.clearValue();
      if (counterpartField != null)
        counterpartField.clearValue();
      if (contractNumField != null)
        contractNumField.clearValue();
      if (accountUnitField != null)
        accountUnitField.clearValue();
      if (counterpartTypeField != null)
        counterpartTypeField.clearValue();
    }

    @Override
    public void onQuery() {
      if (statementNumField != null)
        filter.setStatementNum(statementNumField.getValue());
      if (settleNoField != null)
        filter.setSettleNo(settleNoField.getValue());
      if (counterpartField != null)
        filter.setCounterpart(counterpartField.getValue());
      if (contractNumField != null)
        filter.setContractNum(contractNumField.getValue());
      if (accountUnitField != null)
        filter.setAccountUnit(accountUnitField.getValue());
      if (counterpartTypeField != null)
        filter.setCounterpartType(counterpartTypeField.getValue());
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<QStatement>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortDir != null)
        filter.appendOrder(sortField, sortDir);

      StatementAdjustService.Locator.getService().queryStatement(filter, callback);
    }

    @Override
    public Object getData(int row, int col, QStatement rowData, List<QStatement> pageData) {
      switch (col) {
      case 0:
        return rowData.getStatement().getBillNumber();
      case 1:
        return rowData.getSettleNo();
      case 2:
        return rowData.getCounterpart().toFriendlyStr(counterpartTypeMap);
      case 3:
        return rowData.getContract().getCode();
      default:
        return null;
      }
    }
  }

  public interface WidgetRes extends CommonsMessages {
    public static WidgetRes M = GWT.create(WidgetRes.class);

    @DefaultMessage("选择：账单")
    String selectStatement();

    @DefaultMessage("账单编号")
    String statementNum();

    @DefaultMessage("结转期")
    String settleNo();

    @DefaultMessage("商户")
    String counterpart();

    @DefaultMessage("合同编号")
    String contractNum();

    @DefaultMessage("账单编号 起始于")
    String statementNumStartWith();

    @DefaultMessage("结转期 等于")
    String settleNoEqual();

    @DefaultMessage("商户 类似于")
    String counterpartLike();

    @DefaultMessage("合同编号 起始于")
    String contractNumStartWith();

    @DefaultMessage("项目")
    String accountUnit();

    @DefaultMessage(" 等于")
    String captionEquals();

    @DefaultMessage(" 类似于")
    String like();

    @DefaultMessage("全部")
    String all();

  }

}
