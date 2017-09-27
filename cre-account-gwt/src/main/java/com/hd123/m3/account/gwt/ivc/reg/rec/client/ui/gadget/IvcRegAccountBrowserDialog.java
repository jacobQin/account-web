/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	AccountBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月14日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget;

import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartConfigLoader;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartTextBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.IvcRegAccountFilter;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.util.Pair;
import com.hd123.rumba.gwt.base.client.BUCN;
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
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author chenrizhang
 *
 */
public class IvcRegAccountBrowserDialog extends RBrowserDialog<BAccount> {

  private static EPInvoiceRegReceipt ep = EPInvoiceRegReceipt.getInstance();

  private RGridColumnDef subjectCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef contractCol;
  private RGridColumnDef contractTitleCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef dateRangeCol;

  private PageDataProvider provider;
  private boolean accountUnitControl;
  private boolean counterpartControl;

  private List<String> counterpartTypes;
  private IvcRegAccountFilter filter = new IvcRegAccountFilter();

  public IvcRegAccountBrowserDialog() {
    super();
    drawCol();

    setCaption(M.defaultCaption());

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);

    setSingleSelection(false);
    setFilterStyle(FILTER_STYLE_PANEL);

    getGrid().setAllColumnsOverflowEllipsis(true);
  }

  private void drawCol() {
    subjectCol = new RGridColumnDef(M.accountSubject(), BAccount.ORDER_BY_FIELD_SUBJECT);
    subjectCol.setWidth("120px");

    sourceBillTypeCol = new RGridColumnDef(M.sourceBillType(),
        BAccount.ORDER_BY_FIELD_SOURCEBILLTYPE);
    sourceBillTypeCol.setWidth("100px");

    sourceBillNumberCol = new RGridColumnDef(M.sourceBillNumber(),
        BAccount.ORDER_BY_FIELD_SOURCEBILLNUMBER);
    sourceBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);

    contractCol = new RGridColumnDef(M.contract(), BAccount.ORDER_BY_FIELD_CONTRACT);
    contractCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);

    contractTitleCol = new RGridColumnDef(M.contractTitle(), BAccount.ORDER_BY_FIELD_CONTRACTTITLE);
    contractTitleCol.setWidth("100px");

    accountUnitCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()), BAccount.ORDER_BY_FIELD_ACCOUNTUNIT);
    accountUnitCol.setWidth("120px");

    counterpartCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        M.counterpart()), BAccount.ORDER_BY_FIELD_COUNTERPART);
    counterpartCol.setWidth("120px");

    totalCol = new RGridColumnDef(M.total(), BAccount.ORDER_BY_FIELD_TOTAL);
    totalCol.setWidth("80px");
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);

    dateRangeCol = new RGridColumnDef(M.dateRange(), BAccount.ORDER_BY_FIELD_BEGINTIME);
    dateRangeCol.setWidth("150px");

    setColumnDefs(subjectCol, sourceBillTypeCol, sourceBillNumberCol, contractCol,
        contractTitleCol, accountUnitCol, counterpartCol, totalCol, dateRangeCol);
  }

  public void setCounterpartTypes(List<String> counterpartTypes) {
    this.counterpartTypes = counterpartTypes != null ? counterpartTypes : CounterpartConfigLoader
        .getInstance().getCounterpartTypes();
    if (provider.counterpartField != null) {
      provider.counterpartField.setCounterpartTypes(counterpartTypes);
      provider.counterpartField.setCaption(M.fieldLike(provider.counterpartField.getCaption()));
    }
  }

  public void setAccountUnitControl(boolean accountUnitControl) {
    this.accountUnitControl = accountUnitControl;
    accountUnitCol.setVisible(accountUnitControl == false);
    if (provider.searchForm != null) {
      provider.accountUnitField.setVisible(accountUnitControl == false);
      provider.searchForm.rebuild();
    }
    getGrid().rebuild();
  }

  public void setCounterpartControl(boolean counterpartControl) {
    this.counterpartControl = counterpartControl;
    counterpartCol.setVisible(counterpartControl == false);
    if (provider.searchForm != null) {
      provider.counterpartField.setVisible(counterpartControl == false);
      provider.searchForm.rebuild();
    }
    getGrid().rebuild();
  }

  public void setInvoiceRegUuid(String invoiceRegUuid) {
    filter.setInvoiceRegUuid(invoiceRegUuid);
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    filter.setAccountUnitUuid(accountUnitUuid);
  }

  public void setCounterpartUuid(String counterpartUuid) {
    filter.setCounterpartUuid(counterpartUuid);
  }

  public void setDirection(Integer direction) {
    filter.setAccDirection(direction);
    if (provider != null && provider.subjectField != null) {
      provider.subjectField.setDirection(direction);
    }
  }

  /** 账款除外 */
  public void setAccExcepts(List<Pair<String, String>> excepts) {
    filter.setExcepts(excepts);
  }

  private class PageDataProvider implements RPageDataProvider<BAccount>, RFilterCallback {

    private RForm searchForm;
    private AccountUnitUCNBox accountUnitField;
    private CounterpartTextBox counterpartField;
    private RTextBox contractField;
    private RTextBox contractNameField;
    private SubjectUCNBox subjectField;
    private RComboBox<String> sourceBillTypeField;
    private RTextBox sourceBillNumberField;

    public PageDataProvider() {
      super();
    }

    @Override
    public void buildConditions(RForm form) {
      this.searchForm = form;

      accountUnitField = new AccountUnitUCNBox();
      accountUnitField.setCaption(M.fieldEquals(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
          GRes.R.business())));
      accountUnitField.setVisible(accountUnitControl == false);
      form.addField(accountUnitField);

      counterpartField = new CounterpartTextBox();
      if (counterpartTypes != null) {
        counterpartField.setCounterpartTypes(counterpartTypes);
        counterpartField.setCaption(M.fieldLike(counterpartField.getCaption()));
      }
      counterpartField.setVisible(counterpartControl == false);
      form.addField(counterpartField);

      contractField = new RTextBox(M.contractStartWith());
      form.addField(contractField);

      contractNameField = new RTextBox(M.contractTitleLike());
      form.addField(contractNameField);

      sourceBillTypeField = new RComboBox<String>();
      sourceBillTypeField.setEditable(false);
      sourceBillTypeField.setMaxDropdownRowCount(10);
      sourceBillTypeField.setNullOptionText(M.all());
      for (Entry<String, BBillType> entry : ep.getBillTypes().entrySet()) {
        sourceBillTypeField.addOption(entry.getKey(), entry.getValue().getCaption());
      }
      sourceBillNumberField = new RTextBox();
      RCombinedField sourceBillCombineField = new RCombinedField() {
        {
          setCaption(M.sourceBillTypAndNumberEquals());
          addField(sourceBillTypeField, 0.5f);
          addField(sourceBillNumberField, 0.5f);
        }
      };
      form.addField(sourceBillCombineField);

      subjectField = new SubjectUCNBox(BSubjectType.credit.name(),
          filter.getAccDirection() == null ? 0 : filter.getAccDirection());
      form.addField(subjectField);
    }

    @Override
    public void clearConditions() {
      if (searchForm == null)
        return;
      accountUnitField.clearValue();
      counterpartField.clearValue();
      contractField.clearValue();
      contractNameField.clearValue();
      sourceBillTypeField.clearValue();
      sourceBillNumberField.clearValue();
      subjectField.clearValue();
      subjectField.clearConditions();
    }

    @Override
    public void onQuery() {
      if (searchForm == null)
        return;

      if (accountUnitField.isVisible()) {
        filter.setAccountUnitUuid(accountUnitField.getValue().getUuid());
      }
      filter.setContract(contractField.getValue());
      filter.setContractTitle(contractNameField.getValue());
      filter.setCounterpart(counterpartField.getCounterpart());
      filter.setCounterpartType(counterpartField.getCounterpartType());
      filter.setSourceBillNumber(sourceBillNumberField.getValue());
      filter.setSourceBillType(sourceBillTypeField.getValue());
      filter.setSubject(subjectField.getValue().getUuid());
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccount>> callback) {
      if (accountUnitControl && StringUtil.isNullOrBlank(filter.getAccountUnitUuid())) {
        callback.onSuccess(new RPageData<BAccount>());
        return;
      } else if (counterpartControl && StringUtil.isNullOrBlank(filter.getCounterpartUuid())) {
        callback.onSuccess(new RPageData<BAccount>());
        return;
      }
      if (searchForm.isValid() == false) {
        RPageData<BAccount> oldData = new RPageData<BAccount>();
        oldData.setPageCount(getPagingGrid().getPageCount());
        oldData.setCurrentPage(getPagingGrid().getCurrentPage());
        oldData.setTotalCount(getPagingGrid().getTotalCount());
        oldData.setValues(getPagingGrid().getValues());
        callback.onSuccess(oldData);
        return;
      }
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(subjectCol.getName(), OrderDir.asc);
      }

      InvoiceRegService.Locator.getService().queryAccounts(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BAccount rowData, List<BAccount> pageData) {
      if (rowData.getAcc1() == null)
        return null;
      if (subjectCol.isVisible() && col == subjectCol.getIndex())
        return BUCN.toFriendlyStr(rowData.getAcc1().getSubject(), "");
      if (sourceBillTypeCol.isVisible() && col == sourceBillTypeCol.getIndex()) {
        if (rowData.getAcc1().getSourceBill() == null
            || ep.getBillTypes().get(rowData.getAcc1().getSourceBill().getBillType()) == null)
          return null;
        return ep.getBillTypes().get(rowData.getAcc1().getSourceBill().getBillType()).getCaption();
      }
      if (sourceBillNumberCol.isVisible() && col == sourceBillNumberCol.getIndex())
        return rowData.getAcc1().getSourceBill() == null ? null : rowData.getAcc1().getSourceBill()
            .getBillNumber();
      if (contractCol.isVisible() && col == contractCol.getIndex())
        return rowData.getAcc1().getContract() == null ? null : rowData.getAcc1().getContract()
            .getCode();
      if (contractTitleCol.isVisible() && col == contractTitleCol.getIndex())
        return rowData.getAcc1().getContract() == null ? null : rowData.getAcc1().getContract()
            .getName();
      if (accountUnitCol.isVisible() && col == accountUnitCol.getIndex())
        return rowData.getAcc1().getAccountUnit() == null ? null : rowData.getAcc1()
            .getAccountUnit().toFriendlyStr();
      if (counterpartCol.isVisible() && col == counterpartCol.getIndex()
          && rowData.getAcc1().getCounterpart() != null) {
        if (counterpartTypes != null && counterpartTypes.size() == 1) {
          return rowData.getAcc1().getCounterpart().toFriendlyStr();
        } else {
          return M.counterpartStr(
              rowData.getAcc1().getCounterpart().toFriendlyStr(),
              CounterpartConfigLoader.getInstance().getCaption(
                  rowData.getAcc1().getCounterpart().getCounterpartType()));
        }
      }
      if (totalCol.isVisible() && col == totalCol.getIndex())
        return rowData.getTotal() == null ? null : GWTFormat.fmt_money.format(rowData.getTotal()
            .getTotal().doubleValue());
      if (dateRangeCol.isVisible() && col == dateRangeCol.getIndex()) {
        if (rowData.getAcc1().getBeginTime() == null && rowData.getAcc1().getEndTime() == null)
          return null;
        StringBuffer sb = new StringBuffer();
        if (rowData.getAcc1().getBeginTime() != null)
          sb.append(M3Format.fmt_yMd.format(rowData.getAcc1().getBeginTime()));
        sb.append("~");
        if (rowData.getAcc1().getEndTime() != null)
          sb.append(M3Format.fmt_yMd.format(rowData.getAcc1().getEndTime()));
        return sb.toString();
      }
      return null;
    }
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("请选择：账款")
    String defaultCaption();

    @DefaultMessage("账款科目")
    String accountSubject();

    @DefaultMessage("科目用途")
    String subjectUsage();

    @DefaultMessage("来源单据类型")
    String sourceBillType();

    @DefaultMessage("来源单据单号")
    String sourceBillNumber();

    @DefaultMessage("合同")
    String contract();

    @DefaultMessage("店招")
    String contractTitle();

    @DefaultMessage("商户")
    String counterpart();

    @DefaultMessage("金额")
    String total();

    @DefaultMessage("起止日期")
    String dateRange();

    @DefaultMessage("商户 类似于")
    String counterpartLike();

    @DefaultMessage("发票登记单号 起始于")
    String invoiceRegBillNumberStartWith();

    @DefaultMessage("账单号 起始于")
    String statementStartWith();

    @DefaultMessage("发票代码\\号码 类似于")
    String invoiceCodeOrNumberLike();

    @DefaultMessage("合同编号 起始于")
    String contractStartWith();

    @DefaultMessage("店招 类似于")
    String contractTitleLike();

    @DefaultMessage("来源单据类型单号 起始于")
    String sourceBillTypAndNumberEquals();

    @DefaultMessage("科目 等于")
    String subjectEquals();

    @DefaultMessage("{0} 等于")
    String fieldEquals(String field);

    @DefaultMessage("{0} 类似于")
    String fieldLike(String field);

    @DefaultMessage("全部")
    String all();

    @DefaultMessage("{0}[{1}]")
    String counterpartStr(String counterpart, String counterpartType);
  }
}
