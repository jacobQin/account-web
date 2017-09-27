/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccByStatementBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-13 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartConfigLoader;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartTextBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.IvcRegAccountFilter;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.util.Pair;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.form.RDateRangeField;
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
public class IvcRegAccStatementBrowserDialog extends RBrowserDialog<BAccountStatement> {

  private static EPInvoiceRegReceipt ep = EPInvoiceRegReceipt.getInstance();

  private RGridColumnDef billNumberCol;
  private RGridColumnDef contractCol;
  private RGridColumnDef contractTitleCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef accountTimeCol;
  private RGridColumnDef settleNoCol;

  private PageDataProvider provider;
  private boolean accountUnitControl;
  private boolean counterpartControl;

  private List<String> counterpartTypes;
  private IvcRegAccountFilter filter = new IvcRegAccountFilter();

  public IvcRegAccStatementBrowserDialog() {
    super();
    drawCol();
    setCaption(M.defaultCaption());
    setWidth("700px");

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setSingleSelection(false);
    setFilterStyle(FILTER_STYLE_PANEL);

    getGrid().setAllColumnsOverflowEllipsis(true);
  }

  private void drawCol() {
    billNumberCol = new RGridColumnDef(M.billNumber(), BAccountStatement.ORDER_BY_FIELD_BILLNUMBER);

    contractCol = new RGridColumnDef(M.contract(), BAccountStatement.ORDER_BY_FIELD_CONTRACT);
    contractCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);

    contractTitleCol = new RGridColumnDef(M.contractTitle(),
        BAccountStatement.ORDER_BY_FIELD_CONTRACTTITLE);
    contractTitleCol.setWidth("120px");

    accountUnitCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()), BAccountStatement.ORDER_BY_FIELD_ACCOUNTUNIT);
    accountUnitCol.setWidth("140px");

    counterpartCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        M.counterpart()), BAccountStatement.ORDER_BY_FIELD_COUNTERPART);
    counterpartCol.setWidth("180px");

    totalCol = new RGridColumnDef(M.total(), BAccountStatement.ORDER_BY_FIELD_TOTAL);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("100px");

    accountTimeCol = new RGridColumnDef(M.accountTime(),
        BAccountStatement.ORDER_BY_FIELD_ACCOUNTTIME);
    accountTimeCol.setWidth("100px");

    settleNoCol = new RGridColumnDef(M.settleNo(), BAccountStatement.ORDER_BY_FIELD_SETTLENO);
    settleNoCol.setWidth("80px");

    setColumnDefs(billNumberCol, contractCol, contractTitleCol, accountUnitCol, counterpartCol,
        totalCol, accountTimeCol, settleNoCol);
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
  }

  /** 账款除外 */
  public void setAccExcepts(List<Pair<String, String>> excepts) {
    filter.setExcepts(excepts);
  }

  private class PageDataProvider implements RPageDataProvider<BAccountStatement>, RFilterCallback {

    private RForm searchForm;
    private RTextBox billNumberField;
    private AccountUnitUCNBox accountUnitField;
    private CounterpartTextBox counterpartField;
    private RDateRangeField accountTimeField;
    private RTextBox contractField;
    private RTextBox contractNameField;

    @Override
    public void buildConditions(RForm form) {
      this.searchForm = form;

      billNumberField = new RTextBox(M.billNumberStartWith());
      form.addField(billNumberField);

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

      accountTimeField = new RDateRangeField(M.accountTimeBetween());
      form.addField(accountTimeField);

      contractField = new RTextBox(M.contractStartWith());
      form.addField(contractField);

      contractNameField = new RTextBox(M.contractTitleLike());
      form.addField(contractNameField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      if (searchForm == null)
        return;
      billNumberField.clearValue();
      accountUnitField.clearValue();
      counterpartField.clearValue();
      accountTimeField.clearValue();
      contractField.clearValue();
      contractNameField.clearValue();
    }

    @Override
    public void onQuery() {
      if (searchForm == null)
        return;

      filter.setStatementBillNumber(billNumberField.getValue());
      if (accountUnitField.isVisible()) {
        filter.setAccountUnitUuid(accountUnitField.getValue().getUuid());
      }
      filter.setContract(contractField.getValue());
      filter.setContractTitle(contractNameField.getValue());
      filter.setCounterpart(counterpartField.getCounterpart());
      filter.setCounterpartType(counterpartField.getCounterpartType());
      filter.setStatementAccountTime(new BDateRange(accountTimeField.getStartDate(), BDateRange
          .endOfTheDate(accountTimeField.getEndDate())));
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccountStatement>> callback) {
      if (accountUnitControl && StringUtil.isNullOrBlank(filter.getAccountUnitUuid())) {
        callback.onSuccess(new RPageData<BAccountStatement>());
        return;
      } else if (counterpartControl && StringUtil.isNullOrBlank(filter.getCounterpartUuid())) {
        callback.onSuccess(new RPageData<BAccountStatement>());
        return;
      }
      if (searchForm.isValid() == false) {
        RPageData<BAccountStatement> oldData = new RPageData<BAccountStatement>();
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
        filter.appendOrder(billNumberCol.getName(), OrderDir.desc);
      }

      InvoiceRegService.Locator.getService().queryAccStatements(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BAccountStatement rowData,
        List<BAccountStatement> pageData) {
      if (billNumberCol.isVisible() && col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      if (contractCol.isVisible() && col == contractCol.getIndex())
        return rowData.getContract() == null ? null : rowData.getContract().getCode();
      if (contractTitleCol.isVisible() && col == contractTitleCol.getIndex())
        return rowData.getContract() == null ? null : rowData.getContract().getName();
      if (accountUnitCol.isVisible() && col == accountUnitCol.getIndex())
        return BUCN.toFriendlyStr(rowData.getAccountUnit(), "");
      if (counterpartCol.isVisible() && col == counterpartCol.getIndex()) {
        if (counterpartTypes != null && counterpartTypes.size() == 1) {
          return rowData.getCounterpart().toFriendlyStr();
        } else {
          return M.counterpartStr(rowData.getCounterpart().toFriendlyStr(), CounterpartConfigLoader
              .getInstance().getCaption(rowData.getCounterpart().getCounterpartType()));
        }
      }
      if (totalCol.isVisible() && col == totalCol.getIndex())
        return (rowData.getTotal() == null || rowData.getTotal().getTotal() == null) ? null
            : GWTFormat.fmt_money.format(rowData.getTotal().getTotal().doubleValue());
      if (accountTimeCol.isVisible() && col == accountTimeCol.getIndex())
        return rowData.getAccountTime() == null ? null : GWTFormat.fmt_yMd.format(rowData
            .getAccountTime());
      if (settleNoCol.isVisible() && col == settleNoCol.getIndex())
        return rowData.getSettleNo();
      return null;
    }

  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("请选择：账单")
    String defaultCaption();

    @DefaultMessage("单号")
    String billNumber();

    @DefaultMessage("合同")
    String contract();

    @DefaultMessage("店招")
    String contractTitle();

    @DefaultMessage("商户")
    String counterpart();

    @DefaultMessage("金额")
    String total();

    @DefaultMessage("出账日期")
    String accountTime();

    @DefaultMessage("结转期")
    String settleNo();

    @DefaultMessage("单号 起始于")
    String billNumberStartWith();

    @DefaultMessage("商户 类似于")
    String counterpartLike();

    @DefaultMessage("合同编号 起始于")
    String contractStartWith();

    @DefaultMessage("店招 类似于")
    String contractTitleLike();

    @DefaultMessage("出账日期 介于")
    String accountTimeBetween();

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
