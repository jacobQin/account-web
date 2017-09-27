/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-1 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.ui.dialog;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.invoice.commons.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.filter.AccountDataFilter;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
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
 * @author chenpeisi
 * 
 */
public class AccBrowserDialog extends RBrowserDialog<BAccount> {
  private RGridColumnDef subjectCol = new RGridColumnDef(WidgetRes.R.accountSubject(),
      BAccount.ORDER_BY_FIELD_SUBJECT);
  private RGridColumnDef sourceBillTypeCol = new RGridColumnDef(WidgetRes.R.sourceBillType(),
      BAccount.ORDER_BY_FIELD_SOURCEBILLTYPE);
  private RGridColumnDef sourceBillNumberCol = new RGridColumnDef(WidgetRes.R.sourceBillNumber(),
      BAccount.ORDER_BY_FIELD_SOURCEBILLNUMBER);
  private RGridColumnDef contractCol = new RGridColumnDef(WidgetRes.R.contract(),
      BAccount.ORDER_BY_FIELD_CONTRACT);
  private RGridColumnDef contractNameCol = new RGridColumnDef(WidgetRes.R.contractTitle(),
      BAccount.ORDER_BY_FIELD_CONTRACTTITLE);
  private RGridColumnDef totalCol = new RGridColumnDef(WidgetRes.R.total(),
      BAccount.ORDER_BY_FIELD_TOTAL);

  private Callback callback;
  private PageDataProvider provider;

  public AccBrowserDialog(int direction, Callback callback) {
    super(WidgetRes.R.accountSubject(), WidgetRes.R.sourceBillType(), WidgetRes.R
        .sourceBillNumber(), WidgetRes.R.contract(), WidgetRes.R.total());
    sourceBillNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    contractCol.setWidth(BillConfig.COLUMNWIDTH_BILLNUMBER);
    setColumnDefs(subjectCol, sourceBillTypeCol, sourceBillNumberCol, contractCol, contractNameCol,
        totalCol);

    setWidth("900px");
    this.callback = callback;

    setCaption(WidgetRes.R.captionPrefix() + WidgetRes.R.defaultCaption());

    provider = new PageDataProvider(direction);
    setProvider(provider);
    setFilterCallback(provider);
    setSingleSelection(false);
    setFilterStyle(FILTER_STYLE_PANEL);

    getPagingGrid().setHeight("400px");
    getGrid().setAllColumnsOverflowEllipsis(true);
    getGrid().setAllowVerticalScrollBar(true);
    getPagingGrid().getPagingBar().setShowButtons(false);
    getPagingGrid().getPagingBar().setShowButtonText(false);
    getPagingGrid().getPagingBar().setShowCurrentPage(false);
    getPagingGrid().getPagingBar().setShowPageCount(false);
    getPagingGrid().getPagingBar().setShowPageSize(false);
    getPagingGrid().getPagingBar().setShowRowRange(false);

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

  /**
   * 回调接口
   */
  public static interface Callback {
    void execute(List<BAccount> results);
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

  /** 来源单据类型 */
  public void setBillTypeMap(Map<String, BBillType> billTypeMap) {
    provider.setBillTypeMap(billTypeMap);
  }


  private class PageDataProvider implements RPageDataProvider<BAccount>, RFilterCallback {

    private Map<String, BBillType> billTypeMap;

    private RForm form;
    private RComboBox<String> sourceBillTypeField;
    private RTextBox sourcebillNumberField;
    private RTextBox statementField;
    private SubjectUCNBox subjectField;
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
      form.setWidth("100%");

      sourceBillTypeField = new RComboBox<String>(WidgetRes.R.typAndBillNumberEquals());
      sourceBillTypeField.setEditable(false);
      sourceBillTypeField.setMaxDropdownRowCount(10);
      refreshSourceBillType();

      sourcebillNumberField = new RTextBox();
      RCombinedField sourceBillCombineField = new RCombinedField() {
        {
          setCaption(WidgetRes.R.typAndBillNumberEquals());
          addField(sourceBillTypeField, 0.5f);
          addField(sourcebillNumberField, 0.5f);
        }
      };
      form.addField(sourceBillCombineField);

      statementField = new RTextBox(WidgetRes.R.statementStartWith());
      form.addField(statementField);

      subjectField = new SubjectUCNBox(BSubjectType.credit.name(), filter.getDirectionType());
      form.addField(subjectField);

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
      if (sourceBillNumberCol != null) {
        sourcebillNumberField.clearValue();
      }
      if (statementField != null) {
        statementField.clearValue();
      }
      if (subjectField != null) {
        subjectField.clearValue();
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
      if (statementField != null && sourceBillTypeField != null && subjectField != null
          && contractField != null && contractNameField != null) {
        filter.setSourceBillType(sourceBillTypeField.getValueAsString() == null ? null
            : sourceBillTypeField.getValue());
        filter.setSourceBillNumber(sourcebillNumberField.getValue());
        filter.setStatement(statementField.getValue());
        filter.setSubject(subjectField.getValue().getUuid());
        filter.setContract(contractField.getValue());
        filter.setContractName(contractNameField.getValue());
      }
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccount>> callback) {
      if (form.isValid() == false) {
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
      if (sortField != null)
        filter.appendOrder(sortField, sortDir);

      InvoiceRegService.Locator.getService().queryAccount(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BAccount rowData, List<BAccount> pageData) {
      switch (col) {
      case 0:
        return rowData.getAcc1().getSubject().toFriendlyStr();
      case 1:
        return billTypeMap.get(rowData.getAcc1().getSourceBill().getBillType()) == null ? null
            : billTypeMap.get(rowData.getAcc1().getSourceBill().getBillType()).getCaption();
      case 2:
        return rowData.getAcc1().getSourceBill().getBillNumber();
      case 3:
        return rowData.getAcc1().getContract().getCode();
      case 4:
        return rowData.getAcc1().getContract().getName();
      case 5:
        return GWTFormat.fmt_money.format(rowData.getTotal().getTotal().doubleValue());
      default:
        return null;
      }
    }

    /** 来源单据类型 */
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

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("账款")
    String defaultCaption();

    @DefaultStringValue("请选择：")
    String captionPrefix();

    @DefaultStringValue("账款科目")
    String accountSubject();

    @DefaultStringValue("来源单据类型")
    String sourceBillType();

    @DefaultStringValue("来源单据单号")
    String sourceBillNumber();

    @DefaultStringValue("合同")
    String contract();

    @DefaultStringValue("店招")
    String contractTitle();

    @DefaultStringValue("金额")
    String total();

    @DefaultStringValue("来源单据类型单号 等于")
    String typAndBillNumberEquals();

    @DefaultStringValue("账单号 起始于")
    String statementStartWith();

    @DefaultStringValue("科目 等于")
    String subjectEquals();

    @DefaultStringValue("合同编号 起始于")
    String contractStartWith();

    @DefaultStringValue("店招 类似于")
    String contractTitleLike();
  }
}
