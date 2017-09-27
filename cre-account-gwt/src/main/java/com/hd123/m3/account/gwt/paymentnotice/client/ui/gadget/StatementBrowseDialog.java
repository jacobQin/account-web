/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementBrowseDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-15 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui.gadget;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeService;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.filter.StatementFilter;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * @author zhuhairui
 * 
 */
public class StatementBrowseDialog extends RBrowserDialog<QStatement> {

  private RGridColumnDef billNumberCol = new RGridColumnDef(WidgetRes.M.statementBillNum(),
      QStatement.FIELD_BILLNUMBER);
  private RGridColumnDef settleNoCol = new RGridColumnDef(WidgetRes.M.settleNo(),
      QStatement.FIELD_SETTLENO);
  private RGridColumnDef contractNumCol = new RGridColumnDef(WidgetRes.M.contract_number(),
      QStatement.FIELD_CONTRACTNUM);
  private RGridColumnDef contractTitleCol = new RGridColumnDef(WidgetRes.M.contractTitle(),
      QStatement.FIELD_CONTRACTTITLE);
  private RGridColumnDef accountTypeNameCol = new RGridColumnDef(WidgetRes.M.accountTypeName(),
      QStatement.FIELD_ACCOUNTTYPENAME);

  private PageDataProvider provider;
  private String noticeUuid;
  private String accountUnitUuid;
  private String counterpartUuid;
  private String counterpartType;
  private List<String> statementUuids;

  public StatementBrowseDialog() {
    super(WidgetRes.M.statementBillNum(), WidgetRes.M.settleNo(), WidgetRes.M.contract_number(),
        WidgetRes.M.accountTypeName(), WidgetRes.M.contractTitle());

    contractTitleCol.setSortable(false);
    setColumnDefs(billNumberCol, settleNoCol, contractNumCol, accountTypeNameCol, contractTitleCol);

    billNumberCol.setWidth("160px");
    settleNoCol.setWidth("70px");
    contractNumCol.setWidth("160px");
    accountTypeNameCol.setWidth("80px");
    setWidth("800px");
    setCaption(WidgetRes.M.seleteData(WidgetRes.M.statement()));
    getGrid().setAllColumnsOverflowEllipsis(true);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
    setSingleSelection(false);

  }

  private class PageDataProvider implements RPageDataProvider<QStatement>, RFilterCallback {
    private StatementFilter filter = new StatementFilter();
    private RTextBox billNumberField;
    private RTextBox settleNoField;
    private RTextBox contractNumField;
    private RTextBox contractTitleField;

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<QStatement>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      filter.setNoticeUuid(noticeUuid);
      filter.setAccountUnitUuid(accountUnitUuid);
      filter.setCounterPartUuid(counterpartUuid);
      filter.setCounterpartType(counterpartType);
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(billNumberCol.getName(), OrderDir.asc);
      }
      PaymentNoticeService.Locator.getService().queryStatement(filter, statementUuids, callback);
    }

    @Override
    public Object getData(int row, int col, QStatement rowData, List<QStatement> pageData) {
      switch (col) {
      case 0:
        return rowData.getStatement() != null ? rowData.getStatement().getBillNumber() : null;
      case 1:
        return rowData.getSettleNo();
      case 2:
        return rowData.getContract() != null ? rowData.getContract().getCode() : null;
      case 3:
        return rowData.getAccountTypeName();
      case 4:
        return rowData.getContract() != null ? rowData.getContract().getName() : null;
      default:
        return null;
      }
    }

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("600px");

      billNumberField = new RTextBox(WidgetRes.M.statementBillNumStart());
      form.addField(billNumberField);

      settleNoField = new RTextBox(WidgetRes.M.settleNoEquel());
      form.addField(settleNoField);

      contractNumField = new RTextBox(WidgetRes.M.contract_numberStart());
      form.addField(contractNumField);

      contractTitleField = new RTextBox(WidgetRes.M.contractTitleLike());
      form.addField(contractTitleField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      if (billNumberField != null)
        billNumberField.clearValue();
      if (settleNoField != null)
        settleNoField.clearValue();
      if (contractNumField != null)
        contractNumField.clearValue();
      if (contractTitleField != null)
        contractTitleField.clearValue();
      if (filter != null)
        filter.clear();
    }

    @Override
    public void onQuery() {
      if (billNumberField != null) {
        filter.setBillNumber(billNumberField.getValue());
      }
      if (settleNoField != null) {
        filter.setSettleNo(settleNoField.getValue());
      }
      if (contractNumField != null) {
        filter.setContractNum(contractNumField.getValue());
      }
      if (contractTitleField != null) {
        filter.setContractTitle(contractTitleField.getValue());
      }
    }
  }

  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  public String getNoticeUuid() {
    return noticeUuid;
  }

  public void setNoticeUuid(String noticeUuid) {
    this.noticeUuid = noticeUuid;
  }

  public void setStatementUuids(List<String> statementUuids) {
    this.statementUuids = statementUuids;
  }

  public interface WidgetRes extends CommonsMessages {
    public static WidgetRes M = GWT.create(WidgetRes.class);

    @DefaultMessage("账单编号 起始于")
    String statementBillNumStart();

    @DefaultMessage("结转期 等于")
    String settleNoEquel();

    @DefaultMessage("商户 类似于")
    String counterpartLike();

    @DefaultMessage("合同编号 起始于")
    String contract_numberStart();

    @DefaultMessage("店招 类似于")
    String contractTitleLike();

    @DefaultMessage("账单编号")
    String statementBillNum();

    @DefaultMessage("结转期")
    String settleNo();

    @DefaultMessage("合同编号")
    String contract_number();

    @DefaultMessage("店招")
    String contractTitle();

    @DefaultMessage("账单")
    String statement();

    @DefaultMessage("科目类型")
    String accountTypeName();

  }

}
