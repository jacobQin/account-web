/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentLoader.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams.Base;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author subinzhu
 * 
 */
public class PaymentLoader implements Base {

  private EPPayment ep = EPPayment.getInstance();
  private BPayment entity;

  public BPayment getEntity() {
    return entity;
  }

  public void decodeParams(JumpParameters params, boolean isForEdit, final Command callback) {
    assert callback != null;
    String uuid = null;
    String billNumber = null;

    if (ep.isProcessMode()) {
      uuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      uuid = params.getUrlRef().get(PN_UUID);
      billNumber = params.getUrlRef().get(PN_BILLNUMBER);
    }
    entity = (BPayment) params.getExtend().get(EPPayment.OPN_ENTITY);

    if (uuid != null) {
      if (entity != null && uuid.equals(entity.getUuid()) == false)
        entity = null;

      if (entity != null) {
        callback.execute();
        return;
      } else
        doLoad(uuid, true, isForEdit, callback);

    } else if (billNumber != null) {
      if (entity != null && billNumber.equals(entity.getBillNumber()) == false)
        entity = null;

      if (entity != null) {
        callback.execute();
        return;
      } else
        doLoadByBillnumber(billNumber, isForEdit, callback);

    } else {
      RMsgBox.showErrorAndBack(PaymentMessages.M.requiredUrlParams(), null);
      return;
    }
  }

  private void doLoad(final String uuid, final boolean errorToBack, boolean isForEdit,
      final Command callback) {
    RLoadingDialog.show();
    PaymentService.Locator.getService().load(uuid, isForEdit, new RBAsyncCallback2<BPayment>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.find(),
            PaymentMessages.M.payment());
        if (errorToBack)
          RMsgBox.showErrorAndBack(msg, caught);
        else
          RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BPayment result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          callback.execute();
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = PaymentMessages.M.cannotFind(PaymentMessages.M.payment());
          if (errorToBack)
            RMsgBox.showErrorAndBack(msg, null);
          else
            RMsgBox.showError(msg, null);
        }
      }
    });
  }

  private void doLoadByBillnumber(final String entityBK, boolean isForEdit, final Command callback) {
    RLoadingDialog.show();
    PaymentService.Locator.getService().loadByNumber(entityBK, isForEdit,
        new RBAsyncCallback2<BPayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.processError2(PaymentMessages.M.find(),
                PaymentMessages.M.payment(), entityBK);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPayment result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = PaymentMessages.M.cannotFind2(PaymentMessages.M.payment(), entityBK);
              RMsgBox.showErrorAndBack(msg, null);
            }
          }
        });
  }

}
