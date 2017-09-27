package com.hd123.m3.account.gwt.ivc.common.client;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockDialog.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BStockState;
import com.hd123.m3.account.gwt.ivc.common.client.rpc.CommonService;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.QueryFilter;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * 发票库存对话框
 * 
 * @author lixiaohong
 * 
 */
public class InvoiceStockBrowserDialog extends RBrowserDialog<BInvoiceStock> {
  private List<String> states = new ArrayList<String>();
  private BUCN store;
  private String invoiceType;
  private PageDataProvider provider;
  private boolean needRebuild = false;
  private Map<String, String> mapInvoiceType;
  private String sort;
  private List<String> useTypes;

  /**
   * @param state
   * @param hasInvoiceTypeCol
   *          是否显示发票类型列
   */
  public InvoiceStockBrowserDialog(String state, boolean hasInvoiceTypeCol) {
    super(M.invoiceCode(), M.invoiceNumber(), M.state());
    if (state != null) {
      this.states.add(state);
    }
    if (hasInvoiceTypeCol) {
      setColumnDefs(new RGridColumnDef(M.invoiceCode(), CommonConstants.FIELD_INVOICE_CODE),
          new RGridColumnDef(M.invoiceNumber(), CommonConstants.FIELD_INVOICE_NUMBER),
          new RGridColumnDef(M.invoiceType(), CommonConstants.FIELD_INVOICE_TYPE));
    } else {
      setColumnDefs(new RGridColumnDef(M.invoiceCode(), CommonConstants.FIELD_INVOICE_CODE),
          new RGridColumnDef(M.invoiceNumber(), CommonConstants.FIELD_INVOICE_NUMBER));
      setWidth("700px");
    }

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
    setCaption(CommonsMessages.M.seleteData(M.defaultCaption()));

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
    getGrid().setAllColumnsOverflowEllipsis(true);
  }

  public void clearConditions() {
    if (getFilterCallback() != null) {
      getFilterCallback().clearConditions();
      getFilterCallback().onQuery();
    }
    if (getPagingGrid().getCurrentPage() != 0) {
      getPagingGrid().gotoFirstPage();
    }
  }

  /** 设置项目，项目的搜索条件默认不显示 */
  public void setStoreLimit(BUCN store) {
    setStoreLimit(store, false);
  }

  public void setStoreLimit(BUCN store, boolean showFilter) {
    provider.storeField.setValue(store);
    provider.storeField.setVisible(showFilter);
    this.store = store;
    provider.onQuery();
    needRebuild = true;
  }

  /** 设置 项目等于于 搜索条件是否显示，默认为（false） */
  public void setStoreFilter(boolean showFilter) {
    provider.storeField.setVisible(showFilter);
    needRebuild = true;
  }

  /** 设置发票类型，发票类型搜索条件默认不显示 */
  public void setInvoiceType(String invoiceType) {
    setInvoiceTypeLimit(invoiceType, false);
  }

  /** 设置使用方式，取值参加{@link CommonConstants.UseType}定义的常量 */
  public void setUseTypes(List<String> useTypes) {
    this.useTypes = useTypes;
  }

  /**
   * 设置发票细分类型，取值{@link CommonConstants#SORT_INVOICE}或
   * {@link CommonConstants#SORT_RECEIPT}，其他取值将忽略。
   */
  public void setSort(String sort) {
    this.sort = sort;
  }

  public void setInvoiceTypeLimit(String invoiceType, boolean showFilter) {
    provider.invoiceTypeField.setValue(invoiceType);
    provider.invoiceTypeField.setVisible(showFilter);
    this.invoiceType = invoiceType;
    provider.onQuery();
    needRebuild = true;
  }

  /** 设置 发票类型等于 搜索条件是否显示，默认为（false） */
  public void setInvoiceTypeFilter(boolean showFilter) {
    provider.invoiceTypeField.setVisible(showFilter);
    needRebuild = true;
  }

  /** 设置发票代码，发票代码的搜索条件显示 */
  public void setInvoiceCodeLimit(String invoiceCode) {
    setInvoiceCodeLimit(invoiceCode, true);
  }

  public void setInvoiceCodeLimit(String invoiceCode, boolean showFilter) {
    provider.invoiceCode.setValue(invoiceCode);
    provider.invoiceCode.setVisible(showFilter);
    provider.onQuery();
    needRebuild = true;
  }

  /** 设置发票号码，发票号码的搜索条件显示 */
  public void setInvoiceNumberLimit(String invoiceNumber) {
    setInvoiceNumberLimit(invoiceNumber, true);
  }

  public void setInvoiceNumberLimit(String invoiceNumber, boolean showFilter) {
    provider.invoiceNumber.setValue(invoiceNumber);
    provider.invoiceNumber.setVisible(showFilter);
    provider.onQuery();
    needRebuild = true;
  }

  /** 设置 发票代码起始于 搜索条件是否显示，默认为（true） */
  public void setInvoiceCodeFilter(boolean showFilter) {
    provider.invoiceCode.setVisible(showFilter);
    needRebuild = true;
  }

  /** 设置 发票号码 搜索条件是否显示，默认为（true） */
  public void setInvoiceNumberFilter(boolean showFilter) {
    provider.invoiceNumber.setVisible(showFilter);
    needRebuild = true;
  }

  /**
   * 设置状态
   * 
   * @param state
   * @param showFilter
   *          设置是否显示状态搜索条件
   */
  public void setStateLimit(String state, boolean showFilter) {
    provider.stateField.setValue(state);
    provider.stateField.setVisible(showFilter);
    provider.onQuery();
    states.clear();
    this.states.add(state);
    needRebuild = true;
  }

  /*
   * 设置状态
   * 
   * @param states
   */
  public void setStates(List<String> states) {
    provider.stateField.setVisible(false);
    provider.onQuery();
    this.states.clear();
    this.states.addAll(states);
    needRebuild = true;
  }

  @Override
  public void show() {
    if (needRebuild && provider.form != null) {
      provider.form.rebuild();
      needRebuild = false;
    }
    super.show();
  }

  @Override
  public void center() {
    if (needRebuild && provider.form != null) {
      provider.form.rebuild();
      needRebuild = false;
    }
    super.center();
  }

  private class PageDataProvider implements RPageDataProvider<BInvoiceStock>, RFilterCallback {
    private QueryFilter filter = new QueryFilter();

    public PageDataProvider() {
      invoiceCode = new RTextBox(M.invoiceCode() + M.equals());

      invoiceNumber = new RTextBox(M.invoiceNumber() + M.like());
      stateField = new RComboBox<String>();
      stateField.addOption(BStockState.instock.name(), BStockState.instock.getCaption());
      stateField.addOption(BStockState.received.name(), BStockState.received.getCaption());
      stateField.addOption(BStockState.used.name(), BStockState.used.getCaption());
      stateField.addOption(BStockState.aborted.name(), BStockState.aborted.getCaption());
      stateField.setCaption(M.stateEquals());
      stateField.setEditable(false);
      stateField.setVisible(false);

      storeField = new AccountUnitUCNBox();
      storeField.setVisible(false);
      storeField.setCaption(M.storeUuidEqual());

      invoiceTypeField = new RComboBox<String>();
      invoiceTypeField.setEditable(false);
      for (Map.Entry<String, String> item : getInvoiceTypes().entrySet()) {
        invoiceTypeField.addOption(item.getKey(), item.getValue());
      }
      invoiceTypeField.setCaption(M.invoiceTypeEqual());
    }

    private RForm form;
    private AccountUnitUCNBox storeField;
    private RTextBox invoiceCode;
    private RTextBox invoiceNumber;
    private RComboBox<String> invoiceTypeField;
    private RComboBox<String> stateField;

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BInvoiceStock>> callback) {
      if (form.validate() == false) {
        RPageData<BInvoiceStock> oldData = new RPageData<BInvoiceStock>();
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
        filter.appendOrder(CommonConstants.FIELD_INVOICE_NUMBER);
      }
      filter.put(CommonConstants.FIELD_INVOICE_STATE, states);
      try {
        CommonService.Locator.getService().queryStocks(filter, callback);
      } catch (Exception e) {
        RMsgBox.showError(e.getMessage());
      }
    }

    @Override
    public Object getData(int row, int col, BInvoiceStock rowData, List<BInvoiceStock> pageData) {
      switch (col) {
      case 0:
        return rowData.getInvoiceCode();
      case 1:
        return rowData.getInvoiceNumber();
      case 2: {
        return rowData.getInvoiceType() == null ? null : getInvoiceTypes().get(
            rowData.getInvoiceType());
      }
      default:
        return null;
      }
    }

    @Override
    public void buildConditions(RForm form) {
      this.form = form;

      form.addField(storeField);
      form.addField(invoiceTypeField);
      form.addField(invoiceCode);
      form.addField(invoiceNumber);
      form.addField(stateField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      if (storeField != null && storeField.isVisible()) {
        storeField.clearValue();
      }

      if (invoiceTypeField != null && invoiceTypeField.isVisible()) {
        invoiceTypeField.clearValue();
      }

      if (invoiceCode != null) {
        invoiceCode.clearValue();
      }
      if (invoiceNumber != null) {
        invoiceNumber.clearValue();
      }

      if (stateField != null) {
        if (stateField.isReadOnly() == false) {
          stateField.clearValue();
          if (states.size() == 1) {
            stateField.setValue(states.get(0));
          }
        }
      }
    }

    @Override
    public void onQuery() {
      if (storeField != null) {
        if (storeField.isVisible()) {
          filter.put(CommonConstants.FIELD_INVOICE_STORE, storeField.getValue() == null ? null
              : storeField.getValue().getUuid());
        } else if (store != null) {
          filter.put(CommonConstants.FIELD_INVOICE_STORE, store.getUuid());
        } else {
          filter.put(CommonConstants.FIELD_INVOICE_STORE, null);
        }
      }

      if (invoiceTypeField != null) {
        if (invoiceTypeField.isVisible()) {
          filter.put(CommonConstants.FIELD_INVOICE_TYPE, invoiceTypeField.getValue());
        } else if (invoiceType != null) {
          filter.put(CommonConstants.FIELD_INVOICE_TYPE, invoiceType);
        }
      }

      if (invoiceCode != null) {
        filter.put(CommonConstants.FIELD_INVOICE_CODE, invoiceCode.getValue());
      }
      if (invoiceNumber != null) {
        filter.put(CommonConstants.FIELD_INVOICE_NUMBER, invoiceNumber.getValue());
      }

      if (stateField != null&&stateField.isVisible()) {
        String state = stateField.getValue();
        states.clear();
        states.add(state);
        filter.put(CommonConstants.FIELD_INVOICE_STATE, states);
      } else {
        filter.put(CommonConstants.FIELD_INVOICE_STATE, states);
      }

      filter.put(CommonConstants.FIELD_INVOICE_SORT, sort);
      filter.put(CommonConstants.FIELD_USETYPE, useTypes);

    }
  }

  private Map<String, String> getInvoiceTypes() {
    if (mapInvoiceType == null) {
      GWTUtil.enableSynchronousRPC();
      CommonService.Locator.getService().getInvoiceTypes(new AsyncCallback<Map<String, String>>() {

        @Override
        public void onSuccess(Map<String, String> result) {
          mapInvoiceType = result;
        }

        @Override
        public void onFailure(Throwable caught) {
          // do nothing
        }
      });
    }
    return mapInvoiceType;
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("发票号码")
    String defaultCaption();

    @DefaultMessage("代码")
    String invoiceCode();

    @DefaultMessage("项目 等于")
    String storeUuidEqual();

    @DefaultMessage("发票类型 等于")
    String invoiceTypeEqual();

    @DefaultMessage("号码")
    String invoiceNumber();

    @DefaultMessage("发票类型")
    String invoiceType();

    @DefaultMessage("状态")
    String state();

    @DefaultMessage("状态 等于")
    String stateEquals();

    @DefaultMessage(" 等于")
    String equals();

    @DefaultMessage(" 类似于")
    String like();
  }
}
