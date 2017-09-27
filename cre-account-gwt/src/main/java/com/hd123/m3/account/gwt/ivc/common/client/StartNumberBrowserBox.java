/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名： m3-account-web-main
 * 文件名： StartNumberBrowserBox.java
 * 模块说明：    
 * 修改历史：
 * 2015年12月21日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.ui.PopupPanel;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.common.client.rpc.CommonService;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RPCExceptionDecoder;
import com.hd123.rumba.gwt.widget2e.client.form.RBrowseBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author lixiaohong
 * 
 */
public class StartNumberBrowserBox extends RBrowseBox<BInvoiceStock> {

  public static final String VALUE_OPERATOR = "value.operator";

  private InvoiceStockBrowserDialog dialog;
  private BUCN store;
  private String invoiceType;
  private BInvoiceStock rawValue;
  private List<String> states = new ArrayList<String>();
  private String sort;
  private List<String> useTypes = new ArrayList<String>();

  public StartNumberBrowserBox(String caption, String state, final boolean hasInvoiceTypeCol) {
    super(caption);
    this.states.add(state);
    dialog = new InvoiceStockBrowserDialog(state, hasInvoiceTypeCol);
    dialog.setCaption(CommonsMessages.M.seleteData(caption));
    setBrowser(dialog);
    dialog.addSelectionHandler(new SelectionHandler<BInvoiceStock>() {
      @Override
      public void onSelection(SelectionEvent<BInvoiceStock> event) {
        BInvoiceStock value = event.getSelectedItem();
        setValue(value.getInvoiceNumber());
        rawValue = value;
        ValueChangeEvent.fire(StartNumberBrowserBox.this, getValue());
      }
    });
    dialog.addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        dialog.setStoreLimit(store);
        dialog.setInvoiceTypeLimit(invoiceType,hasInvoiceTypeCol);
      }
    });

  }
  
  public StartNumberBrowserBox(String caption, String state) {
    super(caption);
    this.states.add(state);
    dialog = new InvoiceStockBrowserDialog(state, false);
    dialog.setCaption(CommonsMessages.M.seleteData(caption));
    setBrowser(dialog);
    dialog.addSelectionHandler(new SelectionHandler<BInvoiceStock>() {
      @Override
      public void onSelection(SelectionEvent<BInvoiceStock> event) {
        BInvoiceStock value = event.getSelectedItem();
        setValue(value.getInvoiceNumber());
        rawValue = value;
        ValueChangeEvent.fire(StartNumberBrowserBox.this, getValue());
      }
    });
    dialog.addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        dialog.setStoreLimit(store);
        dialog.setInvoiceType(invoiceType);
        dialog.setSort(sort);
        dialog.setStates(states);
        dialog.setUseTypes(useTypes);
      }
    });

  }

  public StartNumberBrowserBox(String caption) {
    this(caption, null);
  }

  public void setConditionLimit(BUCN store, String invoiceType) {
    this.store = store;
    this.invoiceType = invoiceType;
  }
  
  /**设置使用方式，取值参加{@link CommonConstants.UseType}定义的常量*/
  public void setUseTypes(String... useTypes) {
    this.useTypes.clear();
    for(String useType:useTypes){
      this.useTypes.add(useType);
    }
  }
  
  /**设置使用方式，取值参加{@link BStockState}定义的常量*/
  public void setStates(String... states) {
    this.states.clear();
    for(String state:states){
      this.states.add(state);
    }
  }

  /** 设置项目筛选条件*/
  public void setStoreLimit(BUCN store){
    this.store=store;
  }
  
  /**
   * 设置发票细分类型，取值{@link CommonConstants#SORT_INVOICE}或
   * {@link CommonConstants#SORT_RECEIPT}，其他取值将忽略。
   */
  public void setSort(String sort) {
    this.sort = sort;
  }

  public void clearConditions() {
    if (dialog != null) {
      dialog.clearConditions();
    }
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    clearConditions();
  }

  private Map<String, Object> buildFilter() {
    Map<String, Object> filter = new HashMap();
    filter.put(CommonConstants.FIELD_INVOICE_TYPE, invoiceType);
    filter.put(CommonConstants.FIELD_INVOICE_STORE, store == null ? null : store.getUuid());
    filter.put(CommonConstants.FIELD_INVOICE_STATE, states);
    filter.put(CommonConstants.FIELD_INVOICE_SORT, sort);
    filter.put(CommonConstants.FIELD_USETYPE, useTypes);
    return filter;
  }

  @Override
  public BInvoiceStock getRawValue() {
    if(StringUtil.isNullOrBlank(getValue())){
      return null;
    }
    return rawValue;
  }

  protected void onValidate(String textToValidate) {
    if (getValue() == null || "".equals(getValue())) {
      clearMessages();
      return;
    }
    GWTUtil.enableSynchronousRPC();
    clearMessages();
    CommonService.Locator.getService().getStockByNumber(getText(), buildFilter(),
        new RBAsyncCallback2<BInvoiceStock>() {
          public void onException(Throwable caught) {
            addErrorMessage(RPCExceptionDecoder.decode(caught).getMessage());
          }

          public void onSuccess(BInvoiceStock result) {
            rawValue = result;
            if (result == null) {
              addErrorMessage(InvoiceCommonMessages.M.notFindInvoice(getCaption(), getText()));
            }
          }
        });
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("发票号码")
    String defaultCaption();

  }
}
