/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayEntityLoader.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.payment.client.EPPayDeposit;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.PayDepositUrlParams;
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
public class PayDepositLoader {
  private static BDeposit entity;
  private static EPPayDeposit ep = EPPayDeposit.getInstance();

  public static BDeposit getEntity() {
    return entity;
  }

  public static void decodeParams(JumpParameters params, final Command callback) {
    assert callback != null;
    String billUuid = null;
    String billNumber = null;

    if (ep.isProcessMode()) {
      billUuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      billUuid = params.getUrlRef().get(PayDepositUrlParams.Base.PN_UUID);
      billNumber = params.getUrlRef().get(PayDepositUrlParams.Base.PN_BILLNUMBER);
    }
    entity = (BDeposit) params.getExtend().get(EPPayDeposit.OPN_ENTITY);

    if (billUuid == null && billNumber == null) {
      RMsgBox.showErrorAndBack(DepositMessage.M.requiredUrlParams(), null);
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
    PayDepositService.Locator.getService().load(billUuid, new RBAsyncCallback2<BDeposit>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = DepositMessage.M.cannotFind(PayDepositUrlParams.MODULE_CAPTION);
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BDeposit result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          callback.execute();
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = DepositMessage.M.cannotFind(PayDepositUrlParams.MODULE_CAPTION);
          RMsgBox.showErrorAndBack(msg, null);
        }
      }
    });
  }

  private static void doLoadByNumber(final String billNumber, final Command callback) {
    RLoadingDialog.show();
    PayDepositService.Locator.getService().loadByNumber(billNumber,
        new RBAsyncCallback2<BDeposit>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.cannotFind(PayDepositUrlParams.MODULE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BDeposit result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = DepositMessage.M.cannotFind2(PayDepositUrlParams.MODULE_CAPTION,
                  billNumber);
              RMsgBox.showErrorAndBack(msg, null);
            }
          }
        });
  }
}
