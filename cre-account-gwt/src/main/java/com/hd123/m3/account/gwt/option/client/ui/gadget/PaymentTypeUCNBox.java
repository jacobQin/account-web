/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui.gadget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.option.client.rpc.OptionService;
import com.hd123.m3.account.gwt.option.client.ui.gadget.dataprovider.PageDataProvider;
import com.hd123.m3.account.gwt.option.client.ui.gadget.dataprovider.PaymentTypePageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.gwt.widget2e.client.dialog.RSimpleBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleUCNBox;

/**
 * @author zhuhairui
 * 
 */
public class PaymentTypeUCNBox extends RSimpleUCNBox {

  PageDataProvider pageDataProvider = new PaymentTypePageDataProvider();

  public PaymentTypeUCNBox() {
    super();

    setFieldCaption(WidgetRes.R.paymentType());
    getBrowser().setColumnDefs(
        new RGridColumnDef(RSimpleBrowserDialog.COLUMN_CAPTION_CODE,
            AccountCpntsContants.ORDER_BY_FIELD_CODE),
        new RGridColumnDef(RSimpleBrowserDialog.COLUMN_CAPTION_NAME,
            AccountCpntsContants.ORDER_BY_FIELD_NAME));
    getBrowser().setProvider(pageDataProvider);
    getBrowser().setFilterCallback(pageDataProvider);
    getBrowser().setWidth("500px");
    getBrowser().setCaption(WidgetRes.R.pleaseChoosePaymentType());
    getBrowser().addCloseHandler(new CloseHandler<PopupPanel>() {
      @Override
      public void onClose(CloseEvent<PopupPanel> arg0) {
        clearConditions();
      }
    });
    getBrowser().getGrid().setAllColumnsOverflowEllipsis(true);
  }

  @Override
  protected void queryByCode(String code, AsyncCallback callback) {
    OptionService.Locator.getService().getPaymentTypeByCode(code, callback);
  }

  @Override
  protected void fetchBrowserData(CodeNameFilter filter, AsyncCallback callback) {
    // OptionService.Locator.getService().queryPaymentTypes(filter, callback);
  }

  /** 清除搜索条件 */
  public void clearConditions() {
    if (getBrowser() != null && getBrowser().getFilterCallback() != null) {
      getBrowser().getGrid().clearSort();
      getBrowser().getFilterCallback().clearConditions();
      getBrowser().getFilterCallback().onQuery();
    }
  }

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("付款方式")
    String paymentType();

    @DefaultStringValue("请选择：付款方式")
    String pleaseChoosePaymentType();
  }
}
