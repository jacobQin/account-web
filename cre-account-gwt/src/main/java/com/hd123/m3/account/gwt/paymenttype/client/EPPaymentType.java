/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	EPPaymentType.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.client;

import com.hd123.m3.account.gwt.paymenttype.client.rpc.PaymentTypeService;
import com.hd123.m3.account.gwt.paymenttype.client.ui.PaymentTypeViewPage;
import com.hd123.m3.account.gwt.paymenttype.intf.client.dd.PPaymentTypeDef;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;
import com.hd123.rumba.webframe.gwt.entrypoint.client.TitleEntryPoint;

/**
 * @author zhuhairui
 * 
 */
public class EPPaymentType extends TitleEntryPoint {

  public static EPPaymentType getInstance() {
    return EPPaymentType.getInstance(EPPaymentType.class);
  }

  @Override
  protected void dispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = PaymentTypeViewPage.START_NODE;
      if (PaymentTypeViewPage.START_NODE.equals(start))
        showContentPage(PaymentTypeViewPage.getInstance(), urlParams);
    } catch (Exception e) {
      RMsgBox.showError(PaymentTypeMessages.M.showPageError(getModuleCaption()), e);
    }
  }

  @Override
  protected String getModuleCaption() {
    return PPaymentTypeDef.TABLE_CAPTION;
  }

  @Override
  protected ModuleServiceAsync getModuleService() {
    return PaymentTypeService.Locator.getService();
  }

}
