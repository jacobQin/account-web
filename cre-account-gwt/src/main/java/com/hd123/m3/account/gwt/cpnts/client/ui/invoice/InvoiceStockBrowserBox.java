/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-property-web
 * 文件名：	OperatorBrowserBox.java
 * 模块说明：	
 * 修改历史：
 * 2014-9-30 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.invoice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Messages;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2e.client.form.RBrowseBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 发票输入框。允许用户手动输入发票号码。
 * 
 * @author chenrizhang
 * 
 */
public class InvoiceStockBrowserBox extends RBrowseBox<BInvoiceStock> {

  private Map<String, Object> filter = new HashMap<String, Object>();
  private BInvoiceStock invoice;

  private boolean accountUnitControl = false;
  private boolean stockControl = false;
  private InvoiceStockBrowserDialog dialog;

  public InvoiceStockBrowserBox() {
    super();
    dialog = new InvoiceStockBrowserDialog();
    setBrowser(dialog);
    dialog.addSelectionHandler(new SelectionHandler<BInvoiceStock>() {

      @Override
      public void onSelection(SelectionEvent<BInvoiceStock> event) {
        BInvoiceStock value = event.getSelectedItem();
        setValue(value.getInvoiceNumber());
        clearValidResults();
        ValueChangeEvent.fire(InvoiceStockBrowserBox.this, getValue());

      }
    });

    addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        getInvoiceStockByNumber(getValue(), true);
      }
    });
  }

  /** 票据状态 */
  public void setStates(String... states) {
    if (states != null) {
      filter.put(BInvoiceStock.KEY_FILTER_STATE, Arrays.asList(states));
    } else {
      filter.remove(BInvoiceStock.KEY_FILTER_STATE);
    }
    dialog.setStates(states);
  }

  /** 票据类型 */
  public void setInvoiceTypes(String... invoiceTypes) {
    if (invoiceTypes != null) {
      filter.put(BInvoiceStock.KEY_FILTER_INVOICE_TYPE, Arrays.asList(invoiceTypes));
    } else {
      filter.remove(BInvoiceStock.KEY_FILTER_INVOICE_TYPE);
    }
    dialog.setInvoiceTypes(invoiceTypes);
  }

  /** 发票使用方式常量定义.0-正常，1-被红冲，2-红冲，3-被交换，4-被交换和红冲，5-交换 */
  public void setUseType(Integer useType) {
    if (useType != null) {
      filter.put(BInvoiceStock.KEY_FILTER_USE_TYPE, useType);
    } else {
      filter.remove(BInvoiceStock.KEY_FILTER_USE_TYPE);
    }
    dialog.setUseType(useType);
  }

  /** 项目 */
  public void setAccountUnit(String accountUnit) {
    if (StringUtil.isNullOrBlank(accountUnit)) {
      filter.remove(BInvoiceStock.KEY_FILTER_USE_TYPE);
    } else {
      filter.put(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT, accountUnit);
    }
    dialog.setAccountUnit(accountUnit);
  }

  public void setAccountUnitControl(boolean accountUnitControl) {
    this.accountUnitControl = accountUnitControl;
    dialog.setAccountUnitControl(accountUnitControl);
  }

  public void setStockControl(boolean stockControl) {
    this.stockControl = stockControl;
  }

  /** 票据类型：0-发票，1-收据 */
  public void setSort(Integer sort) {
    if (sort != null) {
      filter.put(BInvoiceStock.KEY_FILTER_SORT, sort);
    } else {
      filter.remove(BInvoiceStock.KEY_FILTER_SORT);
    }
    dialog.setSort(sort);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    getBrowser().setCaption(CommonsMessages.M.seleteData(caption));
  }

  @Override
  public void clearConditions() {
    super.clearConditions();
    if (invoice != null && invoice.getUuid() == null) {
      invoice = null;
    }
  }

  @Override
  public void setValue(String value, boolean fireEvents) {
    super.setValue(value, fireEvents);
    getInvoiceStockByNumber(value, false);
  }

  private void getInvoiceStockByNumber(final String number, final boolean fireEvents) {
    if (accountUnitControl && filter.get(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT) == null) {
      if (stockControl) {
        invoice = new BInvoiceStock();
        invoice.setInvoiceNumber(number);
        if (StringUtil.isNullOrBlank(number) == false) {
          validate();
        }
        if (fireEvents)
          ValueChangeEvent.fire(InvoiceStockBrowserBox.this, number);
      }
      return;
    }
    GWTUtil.enableSynchronousRPC();
    AccountCpntsService.Locator.getService().getInvoiceStockByNumber(number, filter,
        new RBAsyncCallback2<BInvoiceStock>() {

          @Override
          public void onException(Throwable caught) {
            addErrorMessage(caught.getMessage());
          }

          @Override
          public void onSuccess(BInvoiceStock result) {
            invoice = result;
            if (stockControl && result == null) {
              invoice = new BInvoiceStock();
              invoice.setInvoiceNumber(number);
            }
            setRawValue(result);
            if (StringUtil.isNullOrBlank(number) == false) {
              validate();
            }
            if (fireEvents)
              ValueChangeEvent.fire(InvoiceStockBrowserBox.this, number);
          }
        });
  }

  @Override
  public boolean validate() {
    boolean valid = super.validate();
    if (valid == false)
      return valid;

    if (stockControl && invoice != null && StringUtil.isNullOrBlank(invoice.getUuid())
        && StringUtil.isNullOrBlank(invoice.getInvoiceNumber()) == false) {
      addErrorMessage(formatMessage("未找到号码" + invoice.getInvoiceNumber() + ";"));
      return false;
    }
    return true;
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return super.addValueChangeHandler(handler);
  }

  /** 建议使用{@link InvoiceStockBrowserBox#addValueChangeHandler} */
  @Override
  @Deprecated
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return super.addChangeHandler(handler);
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("发票库存")
    String defaultCaption();

  }
}
