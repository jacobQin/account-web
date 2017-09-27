/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeLoader.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-19 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 收付款通知单实体数据载入器
 * 
 * @author zhuhairui
 * 
 */
public class PaymentNoticeLoader {

  public void decoderParams(JumpParameters params, final Command callback) {
    assert callback != null;
    String uuid = null;
    String billNumber = null;

    if (ep.isProcessMode()) {
      uuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      uuid = params.getUrlRef().get(PaymentNoticeUrlParams.View.PN_ENTITY_UUID);
      billNumber = params.getUrlRef().get(PaymentNoticeUrlParams.View.PN_ENTITY_BILLNUMBER);
    }
    entity = (BPaymentNotice) params.getExtend().get(EPPaymentNotice.OPN_ENTITY);

    if (uuid == null && billNumber == null) {
      RMsgBox.showError(PaymentNoticeMessages.M.requiredUrlParams(), null);
      return;
    }

    if (uuid != null) {
      if (entity != null && uuid.equals(entity.getUuid()) == false)
        entity = null;
      if (entity != null) {
        callback.execute();
        return;
      }
      doLoad(uuid, callback);
    } else if (billNumber != null) {
      if (entity != null && billNumber.equals(entity.getBillNumber()) == false)
        entity = null;

      if (entity != null) {
        callback.execute();
        return;
      }
      doLoadByBillNumber(billNumber, callback);
    }
  }

  public BPaymentNotice getEntity() {
    return entity;
  }

  private BPaymentNotice entity;
  private EPPaymentNotice ep = EPPaymentNotice.getInstance();

  private void doLoad(final String uuid, final Command callback) {
    RLoadingDialog.show(PaymentNoticeMessages.M.loading());
    PaymentNoticeService.Locator.getService().load(uuid, new RBAsyncCallback2<BPaymentNotice>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = PaymentNoticeMessages.M.notFind(ep.getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BPaymentNotice result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          callback.execute();
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = PaymentNoticeMessages.M.cannotFind(ep.getModuleCaption());
          RMsgBox.showErrorAndBack(msg, null);
        }

      }
    });
  }

  private void doLoadByBillNumber(final String billNumber, final Command callback) {
    RLoadingDialog.show(PaymentNoticeMessages.M.loading());
    PaymentNoticeService.Locator.getService().loadByNumber(billNumber,
        new RBAsyncCallback2<BPaymentNotice>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentNoticeMessages.M.notFind(ep.getModuleCaption());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BPaymentNotice result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = PaymentNoticeMessages.M.cannotFind2(ep.getModuleCaption(), billNumber);
              RMsgBox.showErrorAndBack(msg, null);
            }

          }
        });
  }
}
