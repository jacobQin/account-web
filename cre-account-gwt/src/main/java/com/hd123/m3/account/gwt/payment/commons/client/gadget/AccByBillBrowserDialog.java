/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccByBillBrowserDialog.java
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
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.PaymentGridCellStyleUtil;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.AccountDataFilter;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
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
public class AccByBillBrowserDialog extends RBrowserDialog<BAccountSourceBill> {

  private RGridColumnDef sourceBillTypeCol = new RGridColumnDef(WidgetRes.R.sourceBillType(),
      BAccountSourceBill.ORDER_BY_FIELD_SOURCEBILLTYPE);
  private RGridColumnDef sourceBillNumberCol = new RGridColumnDef(WidgetRes.R.sourceBillNumber(),
      BAccountSourceBill.ORDER_BY_FIELD_SOURCEBILLNUMBER);
  private RGridColumnDef accountUnitCol = new RGridColumnDef(getFieldCaption(
      GRes.FIELDNAME_BUSINESS, GRes.R.business()), BAccountSourceBill.ORDER_BY_FIELD_ACCOUNTUNIT);
  private RGridColumnDef counterpartCol = new RGridColumnDef(getFieldCaption(
      GRes.FIELDNAME_COUNTERPART, WidgetRes.R.counterpart()),
      BAccountSourceBill.ORDER_BY_FIELD_COUNTERPART);
  private RGridColumnDef contractCol = new RGridColumnDef(WidgetRes.R.contract(),
      BAccountSourceBill.ORDER_BY_FIELD_CONTRACT);
  private RGridColumnDef contractTitleCol = new RGridColumnDef(WidgetRes.R.contractTitle(),
      BAccountSourceBill.ORDER_BY_FIELD_CONTRACTTITLE);
  private RGridColumnDef totalCol = new RGridColumnDef(WidgetRes.R.total(),
      BAccountSourceBill.ORDER_BY_FIELD_TOTAL);

  private Callback callback;
  private PageDataProvider provider;
  private Map<String, String> captionMap;
  private Map<String, String> counterTypeMap;
  private boolean ignoreDirectionType = false;
  private DirectionType directionType;

  /**
   * 此方法用于支持“收款单选择账款的时候不限制收付方向为收的账款，为付的账款也允许添加进来”,即选择账款不区分收付方向。 </p>
   * 如果forSearchOrViewPage = false，则只需传递callback参数。
   * 
   * @param direction
   * @param forSearchOrViewPage
   *          是否提供给搜索界面或查看界面调用，不是的话则只需传递direction和callback参数即可。
   * @param accUnitCaption
   * @param callback
   */
  public AccByBillBrowserDialog(boolean forSearchOrViewPage, String accUnitCaption,
      DirectionType directionType, Callback callback, Map<String, String> captionMap,
      Map<String, String> counterTypeMap) {
    this(directionType.getDirectionValue(), forSearchOrViewPage, accUnitCaption, callback,
        captionMap, counterTypeMap);
    this.ignoreDirectionType = true;
    this.directionType = directionType;
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
  public AccByBillBrowserDialog(int direction, boolean forSearchOrViewPage, String accUnitCaption,
      Callback callback, Map<String, String> captionMap, Map<String, String> counterTypeMap) {
    super(WidgetRes.R.sourceBillType(), WidgetRes.R.sourceBillNumber(), WidgetRes.R.counterpart(),
        WidgetRes.R.contract(), WidgetRes.R.contractTitle(), WidgetRes.R.total());
    this.captionMap = captionMap;
    this.counterTypeMap = counterTypeMap;
    sourceBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractTitleCol.setWidth("120px");
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    if (forSearchOrViewPage) {
      setColumnDefs(sourceBillTypeCol, sourceBillNumberCol, accountUnitCol, counterpartCol,
          contractCol, contractTitleCol, totalCol);
    } else {
      setColumnDefs(sourceBillTypeCol, sourceBillNumberCol, contractCol, contractTitleCol, totalCol);
    }
    setWidth("900px");
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
    addValueChangeHandler(new ValueChangeHandler<List<BAccountSourceBill>>() {

      @Override
      public void onValueChange(ValueChangeEvent<List<BAccountSourceBill>> event) {
        List<BAccountSourceBill> results = event.getValue();
        AccByBillBrowserDialog.this.callback.execute(results);
      }
    });
  }

  /**
   * 回调接口
   */
  public static interface Callback {
    void execute(List<BAccountSourceBill> results);
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

  /** 来源单据类型 */
  public void setBillTypeMap(Map<String, BBillType> billTypeMap) {
    provider.setBillTypeMap(billTypeMap);
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

  private class PageDataProvider implements RPageDataProvider<BAccountSourceBill>, RFilterCallback {

    private Map<String, BBillType> billTypeMap;
    private boolean forSearchOrViewPage;
    private String accUnitCaption;

    private RForm searchForm;
    private AccountUnitUCNBox accountUnitField;
    private RComboBox<String> sourceBillTypeField;
    private RTextBox sourceBillNumberField;
    private RCombinedField countpartField;
    private RTextBox counterpartField;
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

    public void setHasAddedAccIds(List<BAccountId> accountIds) {
      filter.setHasAddedAccIds(accountIds);
    }

    /** 当前收付款单uuid */
    public void setPaymentUuid(String paymentUuid) {
      filter.setPaymentUuid(paymentUuid);
    }

    /** 项目uuid */
    public void setAccountUnitUuid(String accountUnitUuid) {
      filter.setAccountUnitUuid(accountUnitUuid);
    }

    /** 商户uuid */
    public void setCounterpartUuid(String counterpartUuid) {
      filter.setCounterpartUuid(counterpartUuid);
    }

    /** 来源单据类型 */
    public void setBillTypeMap(Map<String, BBillType> billTypeMap) {
      this.billTypeMap = billTypeMap;
    }

    @Override
    public void buildConditions(RForm form) {
      this.searchForm = form;
      searchForm.setWidth("100%");

      sourceBillTypeField = new RComboBox<String>(WidgetRes.R.sourceBillTypAndNumberEquals());
      sourceBillTypeField.setEditable(false);
      sourceBillTypeField.setMaxDropdownRowCount(10);
      refreshSourceBillType();
      sourceBillNumberField = new RTextBox();
      RCombinedField sourceBillCombineField = new RCombinedField() {
        {
          setCaption(WidgetRes.R.sourceBillTypAndNumberEquals());
          addField(sourceBillTypeField, 0.5f);
          addField(sourceBillNumberField, 0.5f);
        }
      };
      form.addField(sourceBillCombineField);

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
            addField(counterpartTypeField, 0.5f);
            addField(counterpartField, 0.5f);
          }
        };
        form.addField(countpartField);

      }

      contractField = new RTextBox(WidgetRes.R.contractStartWith());
      form.addField(contractField);

      contractNameField = new RTextBox(WidgetRes.R.contractTitleLike());
      form.addField(contractNameField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      if (sourceBillTypeField != null) {
        sourceBillTypeField.clearValue();
      }
      if (sourceBillNumberField != null) {
        sourceBillNumberField.clearValue();
      }
      if (accountUnitField != null) {
        accountUnitField.clearValue();
      }
      if (counterpartField != null) {
        counterpartField.clearValue();
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
      if (sourceBillTypeField != null && sourceBillNumberField != null && contractField != null
          && contractNameField != null) {
        filter.setSourceBillType(sourceBillTypeField.getValue());
        filter.setSourceBillNumber(sourceBillNumberField.getValue());
        if (forSearchOrViewPage && accountUnitField != null && counterpartField != null
            && counterpartTypeField != null) {
          filter.setAccountUnitUuid(accountUnitField.getValue() == null ? null : accountUnitField
              .getValue().getUuid());
          filter.setCounterpart(counterpartField.getValue());
          filter.setCounterpartType(counterpartTypeField.getValue());
        }
        filter.setContract(contractField.getValue());
        filter.setContractName(contractNameField.getValue());
      }
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccountSourceBill>> callback) {
      if (searchForm.isValid() == false) {
        RPageData<BAccountSourceBill> oldData = new RPageData<BAccountSourceBill>();
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
        filter.appendOrder(sourceBillTypeCol.getName(), OrderDir.asc);
      }

      filter.setIgnoreDirectionType(isIgnoreDirectionType());

      PaymentCommonsService.Locator.getService().queryAccountByBill(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BAccountSourceBill rowData,
        List<BAccountSourceBill> pageData) {
      accounts.clear();
      accounts.addAll(pageData);

      if (forSearchOrViewPage) {
        switch (col) {
        case 0:
          return rowData.getSourceBill() == null ? null : billTypeMap.get(rowData.getSourceBill()
              .getBillType()) == null ? null : billTypeMap.get(
              rowData.getSourceBill().getBillType()).getCaption();
        case 1:
          return rowData.getSourceBill() == null ? null : rowData.getSourceBill().getBillNumber();
        case 2:
          return (rowData.getAccounts() == null || rowData.getAccounts().isEmpty()) ? null
              : rowData.getAccounts().get(0).getAcc1().getAccountUnit().toFriendlyStr();
        case 3:
          return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
              counterTypeMap);
        case 4:
          return rowData.getContract() == null ? null : rowData.getContract().getCode();
        case 5:
          return rowData.getContract() == null ? null : rowData.getContract().getName();
        case 6:
          return (rowData.getTotal() == null || rowData.getTotal().getTotal() == null) ? null
              : GWTFormat.fmt_money.format(rowData.getTotal().getTotal().doubleValue());
        default:
          return null;
        }
      } else {
        switch (col) {
        case 0:
          return rowData.getSourceBill() == null ? null : billTypeMap.get(rowData.getSourceBill()
              .getBillType()) == null ? null : billTypeMap.get(
              rowData.getSourceBill().getBillType()).getCaption();
        case 1:
          return rowData.getSourceBill() == null ? null : rowData.getSourceBill().getBillNumber();
        case 2:
          return rowData.getContract() == null ? null : rowData.getContract().getCode();
        case 3:
          return rowData.getContract() == null ? null : rowData.getContract().getName();
        case 4:
          return (rowData.getTotal() == null || rowData.getTotal().getTotal() == null) ? null
              : GWTFormat.fmt_money.format(rowData.getTotal().getTotal().doubleValue());
        default:
          return null;
        }
      }
    }

    public void refreshSourceBillType() {
      sourceBillTypeField.clearOptions();
      sourceBillTypeField.setNullOptionText("全部");
      for (String billType : billTypeMap.keySet()) {
        sourceBillTypeField.addOption(billType, billTypeMap.get(billType).getCaption());
      }
    }
  }

  private List<BAccountSourceBill> accounts = new ArrayList<BAccountSourceBill>();

  private class GridRefreshHandler implements RefreshHandler<RGridCellInfo> {

    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      if (accounts.isEmpty()) {
        return;
      }
      RGridCellInfo cell = event.getTarget();
      BAccountSourceBill rowData = accounts.get(cell.getRow());
      if (ignoreDirectionType) {
        int direction = PaymentGridCellStyleUtil.getAccsDirection(rowData.getAccounts());
        PaymentGridCellStyleUtil.refreshCellStye(cell, direction,directionType);
      }

    }

  }

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("单据")
    String defaultCaption();

    @DefaultStringValue("请选择：")
    String captionPrefix();

    @DefaultStringValue("来源单据类型")
    String sourceBillType();

    @DefaultStringValue("来源单据单号")
    String sourceBillNumber();

    @DefaultStringValue("商户")
    String counterpart();

    @DefaultStringValue("合同编号")
    String contract();

    @DefaultStringValue("店招")
    String contractTitle();

    @DefaultStringValue("金额")
    String total();

    @DefaultStringValue("来源单据类型单号 等于")
    String sourceBillTypAndNumberEquals();

    @DefaultStringValue("商户 类似于")
    String counterpartLike();

    @DefaultStringValue("合同编号 起始于")
    String contractStartWith();

    @DefaultStringValue("店招 类似于")
    String contractTitleLike();

    @DefaultStringValue(" 等于")
    String captionEquals();

    @DefaultStringValue(" 类似于")
    String like();

    @DefaultStringValue("全部")
    String all();
  }
}
