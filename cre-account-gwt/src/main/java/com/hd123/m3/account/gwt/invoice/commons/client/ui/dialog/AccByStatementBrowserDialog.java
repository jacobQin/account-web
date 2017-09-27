/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	InvoiceRegStatementDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.ui.dialog;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.account.gwt.invoice.commons.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.filter.AccountDataFilter;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
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
 * 从账单添加
 * 
 * @author chenpeisi
 * 
 */
public class AccByStatementBrowserDialog extends RBrowserDialog<BAccountStatement> {

  private RGridColumnDef billNumberCol = new RGridColumnDef(WidgetRes.R.billNumber(),
      BAccountStatement.ORDER_BY_FIELD_BILLNUMBER);
  private RGridColumnDef contractCol = new RGridColumnDef(WidgetRes.R.contract(),
      BAccountStatement.ORDER_BY_FIELD_CONTRACT);
  private RGridColumnDef contractNameCol = new RGridColumnDef(WidgetRes.R.contractTitle(),
      BAccountStatement.ORDER_BY_FIELD_CONTRACTTITLE);
  private RGridColumnDef totalCol = new RGridColumnDef(WidgetRes.R.total(),
      BAccountStatement.ORDER_BY_FIELD_TOTAL);
  private RGridColumnDef accountTimeCol = new RGridColumnDef(WidgetRes.R.accountTime(),
      BAccountStatement.ORDER_BY_FIELD_ACCOUNTTIME);
  private RGridColumnDef accountRangeCol = new RGridColumnDef(WidgetRes.R.settleNo(),
      BAccountStatement.ORDER_BY_FIELD_SETTLENO);

  private Callback callback;
  private PageDataProvider provider;

  public AccByStatementBrowserDialog(int direction, Callback callback) {
    super(WidgetRes.R.billNumber(), WidgetRes.R.contract(), WidgetRes.R.total(), WidgetRes.R
        .accountTime(), WidgetRes.R.settleNo());
    billNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    setColumnDefs(billNumberCol, contractCol, contractNameCol, totalCol, accountTimeCol,
        accountRangeCol);

    setWidth("950px");
    this.callback = callback;

    setCaption(WidgetRes.R.captionPrefix() + WidgetRes.R.defaultCaption());

    provider = new PageDataProvider(direction);
    setProvider(provider);
    setFilterCallback(provider);
    setSingleSelection(false);
    setFilterStyle(FILTER_STYLE_PANEL);

    getGrid().setAllColumnsOverflowEllipsis(true);
    getGrid().setAllowVerticalScrollBar(true);

    getPagingGrid().setHeight("400px");
    getPagingGrid().getPagingBar().setShowButtons(false);
    getPagingGrid().getPagingBar().setShowButtonText(false);
    getPagingGrid().getPagingBar().setShowCurrentPage(false);
    getPagingGrid().getPagingBar().setShowPageCount(false);
    getPagingGrid().getPagingBar().setShowPageSize(false);
    getPagingGrid().getPagingBar().setShowRowRange(false);
    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null && getGrid() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
    addValueChangeHandler(new ValueChangeHandler<List<BAccountStatement>>() {

      @Override
      public void onValueChange(ValueChangeEvent<List<BAccountStatement>> event) {
        List<BAccountStatement> results = event.getValue();
        AccByStatementBrowserDialog.this.callback.execute(results);
      }
    });
  }

  /**
   * 回调接口
   */
  public static interface Callback {
    void execute(List<BAccountStatement> results);
  }

  /** 用于传递已添加账款的id集合 */
  public void center(List<BAccountId> accountIds) {
    if (provider != null)
      provider.setAccountIds(accountIds);
    super.center();
  }

  /** 记录发票登记单uuid */
  public void setInvoiceRegUuid(String invoiceRegUuid) {
    provider.setInvoiceRegUuid(invoiceRegUuid);
  }

  /** 设置结算单位 */
  public void setAccountUnit(String accountUnit) {
    provider.setAccountUnit(accountUnit);
  }

  /** 设置对方单位 */
  public void setCounterpart(String counterpart) {
    provider.setCounterpart(counterpart);
  }


  private class PageDataProvider implements RPageDataProvider<BAccountStatement>, RFilterCallback {

    private RForm form;
    private RTextBox billNumberField;
    private RDateRangeField accountTimeField;
    private RTextBox contractField;
    private RTextBox contractNameField;
    private AccountDataFilter filter = new AccountDataFilter();

    public PageDataProvider(int direction) {
      super();
      filter.setDirectionType(direction);
    }

    /** 已选择账款的id */
    public void setAccountIds(List<BAccountId> accountIds) {
      filter.setAccountIds(accountIds);
    }

    /** 当前发票登记单uuid */
    public void setInvoiceRegUuid(String invoiceRegUuid) {
      filter.setInvoiceRegUuid(invoiceRegUuid);
    }

    /** 设置结算单位 */
    public void setAccountUnit(String accountUnit) {
      filter.setAccountUnit(accountUnit);
    }

    /** 设置对方单位 */
    public void setCounterpart(String counterpart) {
      filter.setCounterpart(counterpart);
    }

    @Override
    public void buildConditions(RForm form) {
      this.form = form;

      billNumberField = new RTextBox(WidgetRes.R.billNumberStartWith());
      form.addField(billNumberField);

      accountTimeField = new RDateRangeField(WidgetRes.R.accountTimeBetween());
      form.addField(accountTimeField);

      contractField = new RTextBox(WidgetRes.R.contractStartWith());
      form.addField(contractField);

      contractNameField = new RTextBox(WidgetRes.R.contractTitleLike());
      form.addField(contractNameField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      if (billNumberField != null) {
        billNumberField.clearValue();
      }
      if (accountTimeField != null) {
        accountTimeField.clearValue();
      }
      if (contractField != null) {
        contractField.clearValue();
      }
      if (contractNameField != null) {
        contractNameField.clearValue();
      }
      if (filter != null) {
        filter.clear();
      }
    }

    @Override
    public void onQuery() {
      if (billNumberField != null && accountTimeField != null && contractField != null
          && contractNameField != null) {
        filter.setStatement(billNumberField.getValue());
        filter.setAccountTime(accountTimeField.getValue() == null ? new BDateRange()
            : new BDateRange(accountTimeField.getValue().getStart(), accountTimeField.getValue()
                .getEnd()));
        filter.setContract(contractField.getValue());
        filter.setContractName(contractNameField.getValue());
      }
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccountStatement>> callback) {
      if (form.isValid() == false) {
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
      if (sortField != null)
        filter.appendOrder(sortField, sortDir);
      InvoiceRegService.Locator.getService().queryAccountByStatement(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BAccountStatement rowData,
        List<BAccountStatement> pageData) {
      switch (col) {
      case 0:
        return rowData.getBillNumber();
      case 1:
        return rowData.getContract().getCode();
      case 2:
        return rowData.getContract().getName();
      case 3:
        return GWTFormat.fmt_money.format(rowData.getTotal().getTotal().doubleValue());
      case 4:
        return rowData.getAccountTime() == null ? null : GWTFormat.fmt_yMd.format(rowData
            .getAccountTime());
      case 5:
        return rowData.getSettleNo();
      default:
        return null;
      }
    }

  }

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("账单")
    String defaultCaption();

    @DefaultStringValue("请选择：")
    String captionPrefix();

    @DefaultStringValue("单号")
    String billNumber();

    @DefaultStringValue("合同")
    String contract();

    @DefaultStringValue("店招")
    String contractTitle();

    @DefaultStringValue("金额")
    String total();

    @DefaultStringValue("出账日期")
    String accountTime();

    @DefaultStringValue("结转期")
    String settleNo();

    @DefaultStringValue("单号 起始于")
    String billNumberStartWith();

    @DefaultStringValue("合同编号   起始于")
    String contractStartWith();

    @DefaultStringValue("店招 类似于")
    String contractTitleLike();

    @DefaultStringValue("对账组 等于")
    String accountGroupEquals();

    @DefaultStringValue("出账日期 介于")
    String accountTimeBetween();
  }
}
