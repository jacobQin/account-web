/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-8 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.gadget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.PaymentGridCellStyleUtil;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.AccountDataFilter;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
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
public class AccBrowserDialog extends RBrowserDialog<BAccount> {
  private RGridColumnDef subjectCol = new RGridColumnDef(WidgetRes.R.accountSubject(),
      BAccount.ORDER_BY_FIELD_SUBJECT);
  private RGridColumnDef subjectSourceCol = new RGridColumnDef(WidgetRes.R.accountSubjectSource(),
      BAccount.ORDER_BY_FIELD_SUBJECTUSAGE);
  private RGridColumnDef sourceBillTypeCol = new RGridColumnDef(WidgetRes.R.sourceBillType(),
      BAccount.ORDER_BY_FIELD_SOURCEBILLTYPE);
  private RGridColumnDef sourceBillNumberCol = new RGridColumnDef(WidgetRes.R.sourceBillNumber(),
      BAccount.ORDER_BY_FIELD_SOURCEBILLNUMBER);
  private RGridColumnDef contractCol = new RGridColumnDef(WidgetRes.R.contract(),
      BAccount.ORDER_BY_FIELD_CONTRACT);
  private RGridColumnDef contractColTitleCol = new RGridColumnDef(WidgetRes.R.contractTitle(),
      BAccount.ORDER_BY_FIELD_CONTRACTTITLE);
  private RGridColumnDef accountUnitCol = new RGridColumnDef(getFieldCaption(
      GRes.FIELDNAME_BUSINESS, GRes.R.business()), BAccount.ORDER_BY_FIELD_ACCOUNTUNIT);
  private RGridColumnDef counterpartCol = new RGridColumnDef(getFieldCaption(
      GRes.FIELDNAME_COUNTERPART, WidgetRes.R.counterpart()), BAccount.ORDER_BY_FIELD_COUNTERPART);
  private RGridColumnDef totalCol = new RGridColumnDef(WidgetRes.R.total(),
      BAccount.ORDER_BY_FIELD_TOTAL);
  private RGridColumnDef dateRangeCol = new RGridColumnDef(WidgetRes.R.dateRange(),
      BAccount.ORDER_BY_FIELD_BEGINTIME);

  private Callback callback;
  private PageDataProvider provider;
  private Map<String, String> captionMap;
  private Map<String, String> counterpartTypeMap;
  private boolean ignoreDirectionType = false;
  private DirectionType directionType;

  /**
   * 此方法用于支持“收款单选择账款的时候不限制收付方向为收的账款，为付的账款也允许添加进来”,即选择账款不区分收付方向。
   * 
   * 如果forSearchOrViewPage = false，则只需传递callback参数。
   * 
   * @param forSearchOrViewPage
   *          是否提供给搜索界面或查看界面调用，不是的话则只需传递direction和callback参数即可。
   * @param accUnitCaption
   * @param callback
   */
  public AccBrowserDialog(boolean forSearchOrViewPage, String accUnitCaption, Callback callback,
      DirectionType directionType, Map<String, String> captionMap,
      Map<String, String> counterTypeMap) {
    this(directionType.getDirectionValue(), forSearchOrViewPage, accUnitCaption, callback,
        captionMap, counterTypeMap);
    this.ignoreDirectionType = true;
    this.directionType = directionType;
    provider.setIgnoreDirectionType(this.ignoreDirectionType);
  }

  public AccBrowserDialog(int direction, boolean forSearchOrViewPage, String accUnitCaption,
      Callback callback, Map<String, String> captionMap, Map<String, String> counterTypeMap) {
    super(WidgetRes.R.accountSubject(), WidgetRes.R.accountSubjectSource(), WidgetRes.R
        .sourceBillType(), WidgetRes.R.sourceBillNumber(), WidgetRes.R.contract(), WidgetRes.R
        .contractTitle(), WidgetRes.R.counterpart(), WidgetRes.R.total());
    this.captionMap = captionMap;
    this.counterpartTypeMap = counterTypeMap;
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    if (forSearchOrViewPage) {
      setColumnDefs(subjectCol, subjectSourceCol, sourceBillTypeCol, sourceBillNumberCol,
          contractCol, contractColTitleCol, accountUnitCol, counterpartCol, totalCol, dateRangeCol);
    } else {
      setColumnDefs(subjectCol, subjectSourceCol, sourceBillTypeCol, sourceBillNumberCol,
          contractCol, contractColTitleCol, totalCol, dateRangeCol);
    }
    subjectCol.setWidth("120px");
    subjectSourceCol.setWidth("80px");
    sourceBillTypeCol.setWidth("100px");
    sourceBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractColTitleCol.setWidth("100px");
    accountUnitCol.setWidth("120px");
    counterpartCol.setWidth("120px");
    totalCol.setWidth("80px");
    dateRangeCol.setWidth("150px");
    setWidth("1010px");
    this.callback = callback;

    setCaption(WidgetRes.R.captionPrefix() + WidgetRes.R.defaultCaption());

    provider = new PageDataProvider(direction, forSearchOrViewPage, accUnitCaption);
    setProvider(provider);
    setFilterCallback(provider);
    setSingleSelection(false);
    setFilterStyle(FILTER_STYLE_PANEL);

    getGrid().setAllColumnsOverflowEllipsis(true);
    getGrid().setAllowVerticalScrollBar(true);

    getGrid().addRefreshHandler(new GridRefreshHandler());

    getPagingGrid().setHeight("400px");
    getPagingGrid().getPagingBar().setShowPageSize(true);

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
    addValueChangeHandler(new ValueChangeHandler<List<BAccount>>() {

      @Override
      public void onValueChange(ValueChangeEvent<List<BAccount>> event) {
        List<BAccount> results = event.getValue();
        AccBrowserDialog.this.callback.execute(results);
      }
    });
  }

  /** 用于传递已添加账款的id集合 */
  public void center(List<BAccountId> hasAddedUuids) {
    if (provider != null)
      provider.setHasAddedAccIds(hasAddedUuids);
    super.center();
  }

  /**
   * 回调接口
   */
  public static interface Callback {
    void execute(List<BAccount> results);
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

  private class PageDataProvider implements RPageDataProvider<BAccount>, RFilterCallback {

    private Map<String, BBillType> billTypeMap;
    private int direction;
    private boolean forSearchOrViewPage;
    private String accUnitCaption;

    private RForm searchForm;
    private AccountUnitUCNBox accountUnitField;
    private RCombinedField countpartField;
    private RTextBox counterpartField;
    private RTextBox statementField;
    private RTextBox contractField;
    private RTextBox contractNameField;
    private RTextBox invoiceRegBillNumberField;
    private RTextBox invoiceCodeField;
    private RTextBox invoiceNumberField;
    private SubjectUCNBox subjectField;
    private RComboBox<String> sourceBillTypeField;
    private RTextBox sourceBillNumberField;
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
      this.direction = direction;
      this.forSearchOrViewPage = forSearchOrViewPage;
      this.accUnitCaption = accUnitCaption;
      filter.setDirectionType(direction);
    }

    public void setHasAddedAccIds(List<BAccountId> hasAddedUuids) {
      filter.setHasAddedAccIds(hasAddedUuids);
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

    @Override
    public void buildConditions(RForm form) {
      this.searchForm = form;

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
        for (Map.Entry<String, String> entry : counterpartTypeMap.entrySet()) {
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

      invoiceRegBillNumberField = new RTextBox(WidgetRes.R.invoiceRegBillNumberStartWith());
      form.addField(invoiceRegBillNumberField);

      statementField = new RTextBox(WidgetRes.R.statementStartWith());
      form.addField(statementField);

      invoiceCodeField = new RTextBox();
      invoiceNumberField = new RTextBox();
      RCombinedField invoiceCodeOrNumberLikeField = new RCombinedField() {
        {
          setCaption(WidgetRes.R.invoiceCodeOrNumberLike());
          addField(invoiceCodeField, 0.5f);
          addField(invoiceNumberField, 0.5f);
        }
      };
      form.addField(invoiceCodeOrNumberLikeField);

      contractField = new RTextBox(WidgetRes.R.contractStartWith());
      form.addField(contractField);

      contractNameField = new RTextBox(WidgetRes.R.contractTitleLike());
      form.addField(contractNameField);

      sourceBillTypeField = new RComboBox<String>();
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

      subjectField = new SubjectUCNBox(BSubjectType.credit.name(), direction);
      form.addField(subjectField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      if (accountUnitField != null) {
        accountUnitField.clearValue();
      }
      if (counterpartField != null) {
        counterpartField.clearValue();
      }
      if (invoiceRegBillNumberField != null) {
        invoiceRegBillNumberField.clearValue();
      }
      if (statementField != null) {
        statementField.clearValue();
      }
      if (invoiceCodeField != null) {
        invoiceCodeField.clearValue();
      }
      if (invoiceNumberField != null) {
        invoiceNumberField.clearValue();
      }
      if (contractField != null) {
        contractField.clearValue();
      }
      if (contractNameField != null) {
        contractNameField.clearValue();
      }
      if (sourceBillTypeField != null) {
        sourceBillTypeField.clearValue();
      }
      if (sourceBillNumberField != null) {
        sourceBillNumberField.clearValue();
      }
      if (subjectField != null) {
        subjectField.clearValue();
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
      if (statementField != null && sourceBillTypeField != null && subjectField != null
          && contractField != null && contractNameField != null
          && invoiceRegBillNumberField != null && invoiceCodeField != null
          && invoiceNumberField != null && sourceBillNumberField != null) {
        if (forSearchOrViewPage && accountUnitField != null && counterpartField != null
            && counterpartTypeField != null) {
          filter.setAccountUnitUuid(accountUnitField.getValue() == null ? null : accountUnitField
              .getValue().getUuid());
          filter.setCounterpart(counterpartField.getValue());
          filter.setCounterpartType(counterpartTypeField.getValue());
        }
        filter.setInvoiceRegNumber(invoiceRegBillNumberField.getValue());
        filter.setStatement(statementField.getValue());
        filter.setInvoiceCode(invoiceCodeField.getValue());
        filter.setInvoiceNumber(invoiceNumberField.getValue());
        filter.setContract(contractField.getValue());
        filter.setContractName(contractNameField.getValue());
        filter.setSourceBillType(sourceBillTypeField.getValueAsString() == null ? null
            : sourceBillTypeField.getValue());
        filter.setSourceBillNumber(sourceBillNumberField.getValue());
        filter.setSubject(subjectField.getValue().getUuid());
        filter.setIgnoreDirectionType(isIgnoreDirectionType());
      }
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccount>> callback) {
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

      PaymentCommonsService.Locator.getService().queryAccount(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BAccount rowData, List<BAccount> pageData) {
      accounts.clear();
      accounts.addAll(pageData);
      if (rowData.getAcc1() == null)
        return null;
      if (forSearchOrViewPage) {
        switch (col) {
        case 0:
          return rowData.getAcc1().getSubject() == null ? null : rowData.getAcc1().getSubject()
              .toFriendlyStr();
        case 1:
          return rowData.getAcc1().getUsageName();
        case 2:
          return rowData.getAcc1().getSourceBill() == null ? null : billTypeMap.get(rowData
              .getAcc1().getSourceBill().getBillType()) == null ? null : billTypeMap.get(
              rowData.getAcc1().getSourceBill().getBillType()).getCaption();
        case 3:
          return rowData.getAcc1().getSourceBill() == null ? null : rowData.getAcc1()
              .getSourceBill().getBillNumber();
        case 4:
          return rowData.getAcc1().getContract() == null ? null : rowData.getAcc1().getContract()
              .getCode();
        case 5:
          return rowData.getAcc1().getContract() == null ? null : rowData.getAcc1().getContract()
              .getName();
        case 6:
          return rowData.getAcc1().getAccountUnit() == null ? null : rowData.getAcc1()
              .getAccountUnit().toFriendlyStr();
        case 7:
          return rowData.getAcc1().getCounterpart() == null ? null : rowData.getAcc1()
              .getCounterpart().toFriendlyStr(counterpartTypeMap);
        case 8:
          return rowData.getTotal() == null ? null : GWTFormat.fmt_money.format(rowData.getTotal()
              .getTotal().doubleValue());
        case 9:
          return buildDateRange(rowData.getAcc1().getBeginTime(), rowData.getAcc1().getEndTime());
        default:
          return null;
        }
      } else {
        switch (col) {
        case 0:
          return rowData.getAcc1().getSubject() == null ? null : rowData.getAcc1().getSubject()
              .toFriendlyStr();
        case 1:
          return rowData.getAcc1().getUsageName();
        case 2:
          return rowData.getAcc1().getSourceBill() == null ? null : billTypeMap.get(rowData
              .getAcc1().getSourceBill().getBillType()) == null ? null : billTypeMap.get(
              rowData.getAcc1().getSourceBill().getBillType()).getCaption();
        case 3:
          return rowData.getAcc1().getSourceBill() == null ? null : rowData.getAcc1()
              .getSourceBill().getBillNumber();
        case 4:
          return rowData.getAcc1().getContract() == null ? null : rowData.getAcc1().getContract()
              .getCode();
        case 5:
          return rowData.getAcc1().getContract() == null ? null : rowData.getAcc1().getContract()
              .getName();
        case 6:
          return rowData.getTotal() == null ? null : GWTFormat.fmt_money.format(rowData.getTotal()
              .getTotal().doubleValue());
        case 7:
          return buildDateRange(rowData.getAcc1().getBeginTime(), rowData.getAcc1().getEndTime());
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

    public void setBillTypeMap(Map<String, BBillType> billTypeMap) {
      this.billTypeMap = billTypeMap;
    }

    public void refreshSourceBillType() {
      sourceBillTypeField.clearOptions();
      sourceBillTypeField.setNullOptionText("全部");
      for (String billType : billTypeMap.keySet()) {
        sourceBillTypeField.addOption(billType, billTypeMap.get(billType).getCaption());
      }
    }
  }

  private List<BAccount> accounts = new ArrayList<BAccount>();

  private class GridRefreshHandler implements RefreshHandler<RGridCellInfo> {

    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      if (accounts.isEmpty()) {
        return;
      }
      RGridCellInfo cell = event.getTarget();
      BAccount rowData = accounts.get(cell.getRow());
      if (ignoreDirectionType) {
        PaymentGridCellStyleUtil.refreshCellStye(cell, rowData.getAcc1().getDirection(),
            directionType);
      }
    }

  }

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("账款")
    String defaultCaption();

    @DefaultStringValue("请选择：")
    String captionPrefix();

    @DefaultStringValue("账款科目")
    String accountSubject();

    @DefaultStringValue("科目用途")
    String accountSubjectSource();

    @DefaultStringValue("来源单据类型")
    String sourceBillType();

    @DefaultStringValue("来源单据单号")
    String sourceBillNumber();

    @DefaultStringValue("合同")
    String contract();

    @DefaultStringValue("店招")
    String contractTitle();

    @DefaultStringValue("商户")
    String counterpart();

    @DefaultStringValue("金额")
    String total();

    @DefaultStringValue("起止日期")
    String dateRange();

    @DefaultStringValue("商户 类似于")
    String counterpartLike();

    @DefaultStringValue("发票登记单号 起始于")
    String invoiceRegBillNumberStartWith();

    @DefaultStringValue("账单号 起始于")
    String statementStartWith();

    @DefaultStringValue("发票代码\\号码 类似于")
    String invoiceCodeOrNumberLike();

    @DefaultStringValue("合同编号 起始于")
    String contractStartWith();

    @DefaultStringValue("店招 类似于")
    String contractTitleLike();

    @DefaultStringValue("来源单据类型单号 等于")
    String sourceBillTypAndNumberEquals();

    @DefaultStringValue("科目 等于")
    String subjectEquals();

    @DefaultStringValue(" 等于")
    String captionEquals();

    @DefaultStringValue(" 类似于")
    String like();

    @DefaultStringValue("全部")
    String all();
  }
}
