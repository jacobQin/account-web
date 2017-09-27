/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeServiceAgent.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月21日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountDetails;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;

/**
 * 发票交换单GWT服务代理
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeServiceAgent {

  public static void getAccountLinesByInvoiceNumber(List<String> invoiceNumbers, String billUuid,
      final Callback<BInvoiceExchangeAccountDetails> callback) {
    RLoadingDialog.show();
    InvoiceExchangeService.Locator.getService().getAccountLinesByInvoceNumbers(invoiceNumbers,
        billUuid, new AsyncCallback<BInvoiceExchangeAccountDetails>() {
          @Override
          public void onSuccess(BInvoiceExchangeAccountDetails result) {
            RLoadingDialog.hide();
            callback.execute(result);
          }

          @Override
          public void onFailure(Throwable caught) {
            RLoadingDialog.hide();
            RMsgBox.showError(InvoiceExchangeMessages.M.queryAccountError(), caught);
          }
        });
  }
  
  public static void getAllTypes(final Callback<List<BBillType>> callback){
    InvoiceExchangeService.Locator.getService().getAllTypes(new AsyncCallback<List<BBillType>>() {
      @Override
      public void onSuccess(List<BBillType> result) {
        callback.execute(result);
      }
      
      @Override
      public void onFailure(Throwable caught) {
        RMsgBox.showError("获取单据类型出错",caught);
      }
    });
  }

  public interface Callback<T> {
    public void execute(T entity);
  }
}
