/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegEntityLoader.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.payment.client.EPPayInvoiceReg;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.PayInvoiceRegUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenpeisi
 * 
 */
public class PayInvoiceRegLoader {

  private static EPPayInvoiceReg ep = EPPayInvoiceReg.getInstance();

  private static BInvoiceReg entity;

  public static BInvoiceReg getEntity() {
    return entity;
  }

  public static void decodeParams(JumpParameters params, final Command callback) {
    assert callback != null;
    String billUuid = null;
    String billNumber = null;

    if (ep.isProcessMode()) {
      billUuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      billUuid = params.getUrlRef().get(PayInvoiceRegUrlParams.Base.PN_UUID);
      billNumber = params.getUrlRef().get(PayInvoiceRegUrlParams.Base.PN_BILLNUMBER);
    }
    entity = null;

    if (billUuid == null && billNumber == null) {
      RMsgBox.showErrorAndBack(InvoiceRegMessage.M.requiredUrlParams(), null);
      return;
    }

    if (billUuid != null) {
      if (entity != null && !billUuid.equals(entity.getUuid()))
        entity = null;

      if (entity != null) {
        callback.execute();
        return;
      }
      doLoad(billUuid, callback);
    } else {
      if (entity != null && !billNumber.equals(entity.getBillNumber()))
        entity = null;

      if (entity != null) {
        callback.execute();
        return;
      }
      doLoadByNumber(billNumber, callback);
    }
  }

  private static void doLoad(String billUuid, final Command callback) {
    RLoadingDialog.show();
    PayInvoiceRegService.Locator.getService().load(billUuid, new RBAsyncCallback2<BInvoiceReg>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = InvoiceRegMessage.M.cannotFind(PayInvoiceRegUrlParams.MODULE_CAPTION);
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BInvoiceReg result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          callback.execute();
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = InvoiceRegMessage.M.cannotFind(PayInvoiceRegUrlParams.MODULE_CAPTION);
          RMsgBox.showErrorAndBack(msg, null);
        }

      }
    });
  }

  private static void doLoadByNumber(final String billNumber, final Command callback) {
    RLoadingDialog.show();
    PayInvoiceRegService.Locator.getService().loadByNumber(billNumber,
        new RBAsyncCallback2<BInvoiceReg>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.cannotFind(PayInvoiceRegUrlParams.MODULE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BInvoiceReg result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = InvoiceRegMessage.M.cannotFind2(PayInvoiceRegUrlParams.MODULE_CAPTION,
                  billNumber);
              RMsgBox.showErrorAndBack(msg, null);
            }

          }
        });
  }
}
