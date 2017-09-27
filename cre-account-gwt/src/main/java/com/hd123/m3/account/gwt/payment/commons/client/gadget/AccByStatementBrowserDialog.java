/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccByStatementBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-13 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.gadget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.PaymentGridCellStyleUtil;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.AccountDataFilter;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateRangeField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author subinzhu
 * 
 */
public class AccByStatementBrowserDialog extends RBrowserDialog<BAccountStatement> {

  private RGridColumnDef billNumberCol = new RGridColumnDef(WidgetRes.R.billNumber(),
      BAccountStatement.ORDER_BY_FIELD_BILLNUMBER);
  private RGridColumnDef contractCol = new RGridColumnDef(WidgetRes.R.contract(),
      BAccountStatement.ORDER_BY_FIELD_CONTRACT);
  private RGridColumnDef accountUnitCol = new RGridColumnDef(getFieldCaption(
      GRes.FIELDNAME_BUSINESS, GRes.R.business()), BAccountStatement.ORDER_BY_FIELD_ACCOUNTUNIT);
  private RGridColumnDef counterpartCol = new RGridColumnDef(getFieldCaption(
      GRes.FIELDNAME_COUNTERPART, WidgetRes.R.counterpart()),
      BAccountStatement.ORDER_BY_FIELD_COUNTERPART);
  private RGridColumnDef totalCol = new RGridColumnDef(WidgetRes.R.total(),
      BAccountStatement.ORDER_BY_FIELD_TOTAL);
  private RGridColumnDef accountTimeCol = new RGridColumnDef(WidgetRes.R.accountTime(),
      BAccountStatement.ORDER_BY_FIELD_ACCOUNTTIME);
  private RGridColumnDef accountRangeCol = new RGridColumnDef(WidgetRes.R.settleNo(),
      BAccountStatement.ORDER_BY_FIELD_SETTLENO);

  private RGridColumnDef contractTitleCol = new RGridColumnDef(WidgetRes.R.contractTitle(),
      BAccountStatement.ORDER_BY_FIELD_CONTRACTTITLE);

  private Callback callback;
  private PageDataProvider provider;
  private Map<String, String> captionMap;
  private Map<String, String> counterTypeMap;
  private boolean ignoreDirectionType = false;
  private DirectionType directionType;

  /**
   * 此方法用于支持“收付款单选择账款的时候不限制收付方向为收的账款，为付的账款也允许添加进来”,即选择账款不区分收付方向。
   * 如果forSearchOrViewPage = false，则只需传递callback参数。
   * 
   * @param direction
   * @param forSearchOrViewPage
   *          是否提供给搜索界面或查看界面调用，不是的话则只需传递direction和callback参数即可。
   * @param accUnitCaption
   * @param type
   *          收付款类型
   * @param callback
   */
  public AccByStatementBrowserDialog(boolean forSearchOrViewPage, String accUnitCaption,
      DirectionType type, Callback callback, Map<String, String> captionMap,
      Map<String, String> counterTypeMap) {
    this(type.getDirectionValue(), forSearchOrViewPage, accUnitCaption, callback, captionMap,
        counterTypeMap);
    this.ignoreDirectionType = true;
    this.directionType = type;
    provider.setIgnoreDirectionType(this.ignoreDirectionType);
  }

  /**
   * 如果forSearchOrViewPage = false，则只需传递direction和callback参数。
   * 
   * @param direction
   * @param forSearchOrViewPage
   *          是否提供给搜索界面或查看界面调用，不是的话则只需传递direction和callback参数即可。
   * @param accUnitCaption
   * @param callback
   */
  public AccByStatementBrowserDialog(int direction, boolean forSearchOrViewPage,
      String accUnitCaption, Callback callback, Map<String, String> captionMap,
      Map<String, String> counterTypeMap) {
    super(WidgetRes.R.billNumber(), WidgetRes.R.contract(), WidgetRes.R.contractTitle(),
        WidgetRes.R.counterpart(), WidgetRes.R.total(), WidgetRes.R.accountTime(), WidgetRes.R
            .settleNo());
    this.captionMap = captionMap;
    this.counterTypeMap = counterTypeMap;
    billNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractTitleCol.setWidth("120px");
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    accountTimeCol.setWidth("100px");
    if (forSearchOrViewPage) {
      setColumnDefs(billNumberCol, contractCol, contractTitleCol, accountUnitCol, counterpartCol,
          totalCol, accountTimeCol, accountRangeCol);
    } else {
      setColumnDefs(billNumberCol, contractCol, contractTitleCol, totalCol, accountTimeCol,
          accountRangeCol);
    }
    setWidth("950px");
    this.callback = callback;

    setCaption(WidgetRes.R.captionPrefix() + WidgetRes.R.defaultCaption());

    provider = new PageDataProvider(direction, forSearchOrViewPage, accUnitCaption);
    setProvider(provider);
    setFilterCallback(provider);
    setSingleSelection(false);
    setFilterStyle(FILTER_STYLE_PANEL);

    getGrid().setAllColumnsOverflowEllipsis(true);
    getGrid().setAllowVerticalScrollBar(true);

    getPagingGrid().setHeight("400px");
    getPagingGrid().getPagingBar().setShowPageSize(true);

    getGrid().addRefreshHandler(new GridRefreshHandler());

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null) {
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

  /**
   * 回调接口
   */
  public static interface Callback {
    void execute(List<BAccountStatement> results);
  }

  /** 用于传递已添加账款的id集合 */
  public void center(List<BAccountId> accountIds) {
    if (provider != null)
      provider.setHasAddedAccIds(accountIds);
    super.center();
  }

  /** 记录收付款单uuid */
  public void setPaymentUuid(String paymentUuid) {
    provider.setPaymentUuid(paymentUuid);
  }

  /** 项目uuid */
  public void setAccountUnitUuid(String accountUnitUuid) {
    provider.setAccountUnitUuid(accountUnitUuid);
  }

  /** 商户uuid */
  public void setCounterpartUuid(String counterpartUuid) {
    provider.setCounterpartUuid(counterpartUuid);
  }

  private class PageDataProvider implements RPageDataProvider<BAccountStatement>, RFilterCallback {
    private boolean forSearchOrViewPage;
    private String accUnitCaption;

    private RForm searchForm;
    private AccountUnitUCNBox accountUnitField;
    private RTextBox billNumberField;
    private RCombinedField countpartField;
    private RTextBox counterpartField;
    private RDateRangeField accountTimeField;
    private RTextBox contractField;
    private RTextBox contractNameField;
    private RComboBox<String> counterpartTypeField;
    private AccountDataFilter filter = new AccountDataFilter();

    private boolean ignoreDirectionType = false;

    /** 是否忽略收款方向，如果为true，directionType属性值将失效，默认值为false */
    public boolean isIgnoreDirectionType() {
      return ignoreDirectionType;
    }

    public void setIgnoreDirectionType(boolean ignoreDirectionType) {
      this.ignoreDirectionType = ignoreDirectionType;
    }

    public PageDataProvider(int direction, boolean forSearchOrViewPage, String accUnitCaption) {
      super();
      this.forSearchOrViewPage = forSearchOrViewPage;
      this.accUnitCaption = accUnitCaption;
      filter.setDirectionType(direction);
    }

    /** 已选择账款的id */
    public void setHasAddedAccIds(List<BAccountId> accountIds) {
      filter.setHasAddedAccIds(accountIds);
    }

    /** 当前收付款单uuid */
    public void setPaymentUuid(String invoiceRegUuid) {
      filter.setPaymentUuid(invoiceRegUuid);
    }

    /** 项目uuid */
    public void setAccountUnitUuid(String accountUnitUuid) {
      filter.setAccountUnitUuid(accountUnitUuid);
    }

    /** 商户uuid */
    public void setCounterpartUuid(String counterpartUuid) {
      filter.setCounterpartUuid(counterpartUuid);
    }

    @Override
    public void buildConditions(RForm form) {
      this.searchForm = form;

      billNumberField = new RTextBox(WidgetRes.R.billNumberStartWith());
      form.addField(billNumberField);

      if (forSearchOrViewPage) {
        accountUnitField = new AccountUnitUCNBox();
        accountUnitField.setCaption(accUnitCaption + WidgetRes.R.captionEquals());
        accountUnitField.getBrowser().setCaption(WidgetRes.R.captionPrefix() + accUnitCaption);
        form.addField(accountUnitField);

        counterpartField = new RTextBox();

        counterpartTypeField = new RComboBox<String>();
        counterpartTypeField.setEditable(false);
        counterpartTypeField.setNullOptionText(WidgetRes.R.all());
        counterpartTypeField.setMaxDropdownRowCount(10);
        for (Map.Entry<String, String> entry : counterTypeMap.entrySet()) {
          counterpartTypeField.addOption(entry.getKey(), entry.getValue());
        }
        final String counterpartTypeFieldCaption = getFieldCaption(GRes.FIELDNAME_COUNTERPART,
            WidgetRes.R.counterpart()) + WidgetRes.R.like();
        countpartField = new RCombinedField() {
          {
            setCaption(counterpartTypeFieldCaption);
            addField(counterpartTypeField, 0.4f);
            addField(counterpartField, 0.6f);
          }
        };
        form.addField(countpartField);
      }

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
      if (accountUnitField != null) {
        accountUnitField.clearValue();
      }
      if (counterpartField != null) {
        counterpartField.clearValue();
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
      if (counterpartTypeField != null) {
        counterpartTypeField.clearValue();
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
        if (forSearchOrViewPage && accountUnitField != null && counterpartField != null
            && counterpartTypeField != null) {
          filter.setAccountUnitUuid(accountUnitField.getValue() == null ? null : accountUnitField
              .getValue().getUuid());
          filter.setCounterpart(counterpartField.getValue());
          filter.setCounterpartType(counterpartTypeField.getValue());
        }
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

      filter.setIgnoreDirectionType(isIgnoreDirectionType());

      PaymentCommonsService.Locator.getService().queryAccountByStatement(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BAccountStatement rowData,
        List<BAccountStatement> pageData) {

      accounts.clear();
      accounts.addAll(pageData);

      if (forSearchOrViewPage) {
        switch (col) {
        case 0:
          return rowData.getBillNumber();
        case 1:
          return rowData.getContract() == null ? null : rowData.getContract().getCode();
        case 2:
          return rowData.getContract() == null ? null : rowData.getContract().getName();
        case 3:
          return (rowData.getAccounts() == null || rowData.getAccounts().isEmpty()) ? null
              : rowData.getAccounts().get(0).getAcc1().getAccountUnit().toFriendlyStr();

        case 4:
          return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
              counterTypeMap);
        case 5:
          return (rowData.getTotal() == null || rowData.getTotal().getTotal() == null) ? null
              : GWTFormat.fmt_money.format(rowData.getTotal().getTotal().doubleValue());
        case 6:
          return rowData.getAccountTime() == null ? null : GWTFormat.fmt_yMd.format(rowData
              .getAccountTime());
        case 7:
          return rowData.getSettleNo();
        default:
          return null;
        }
      } else {
        switch (col) {
        case 0:
          return rowData.getBillNumber();
        case 1:
          return rowData.getContract() == null ? null : rowData.getContract().getCode();
        case 2:
          return rowData.getContract() == null ? null : rowData.getContract().getName();
        case 3:
          return (rowData.getTotal() == null || rowData.getTotal().getTotal() == null) ? null
              : GWTFormat.fmt_money.format(rowData.getTotal().getTotal().doubleValue());
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

  }

  private List<BAccountStatement> accounts = new ArrayList<BAccountStatement>();

  private class GridRefreshHandler implements RefreshHandler<RGridCellInfo> {

    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      if (accounts.isEmpty()) {
        return;
      }
      RGridCellInfo cell = event.getTarget();
      BAccountStatement rowData = accounts.get(cell.getRow());
      if (ignoreDirectionType) {
        int direction = PaymentGridCellStyleUtil.getAccsDirection(rowData.getAccounts());
        PaymentGridCellStyleUtil.refreshCellStye(cell, direction, directionType);
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

    @DefaultStringValue("商户")
    String counterpart();

    @DefaultStringValue("金额")
    String total();

    @DefaultStringValue("出账日期")
    String accountTime();

    @DefaultStringValue("结转期")
    String settleNo();

    @DefaultStringValue("单号 起始于")
    String billNumberStartWith();

    @DefaultStringValue("商户 类似于")
    String counterpartLike();

    @DefaultStringValue("合同编号 起始于")
    String contractStartWith();

    @DefaultStringValue("店招 类似于")
    String contractTitleLike();

    @DefaultStringValue("出账日期 介于")
    String accountTimeBetween();

    @DefaultStringValue(" 等于")
    String captionEquals();

    @DefaultStringValue(" 类似于")
    String like();

    @DefaultStringValue("全部")
    String all();
  }
}
