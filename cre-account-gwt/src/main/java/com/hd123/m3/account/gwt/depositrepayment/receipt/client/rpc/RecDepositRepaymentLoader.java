/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecDepositRepaymentLoader.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-25 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.EPRecDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.RecDepositRepaymentUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class RecDepositRepaymentLoader {
  private static BDepositRepayment entity;
  private static EPRecDepositRepayment ep = EPRecDepositRepayment.getInstance();

  public static BDepositRepayment getEntity() {
    return entity;
  }

  public static void decodeParams(JumpParameters params, final Command callback) {
    assert callback != null;
    String billUuid = null;
    String billNumber = null;

    if (ep.isProcessMode()) {
      billUuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      billUuid = params.getUrlRef().get(RecDepositRepaymentUrlParams.Base.PN_ENTITY_UUID);
      billNumber = params.getUrlRef().get(RecDepositRepaymentUrlParams.Base.PN_ENTITY_BILLNUMBER);
    }
    entity = (BDepositRepayment) params.getExtend().get(EPRecDepositRepayment.OPN_ENTITY);

    if (billUuid == null && billNumber == null) {
      RMsgBox.showErrorAndBack(DepositRepaymentMessage.M.requiredUrlParams(), null);
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
    RecDepositRepaymentService.Locator.getService().load(billUuid,
        new RBAsyncCallback2<BDepositRepayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M
                .cannotFind(RecDepositRepaymentUrlParams.MODULE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BDepositRepayment result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = DepositRepaymentMessage.M
                  .cannotFind(RecDepositRepaymentUrlParams.MODULE_CAPTION);
              RMsgBox.showErrorAndBack(msg, null);
            }

          }
        });
  }

  private static void doLoadByNumber(final String billNumber, final Command callback) {
    RLoadingDialog.show();
    RecDepositRepaymentService.Locator.getService().loadByNumber(billNumber,
        new RBAsyncCallback2<BDepositRepayment>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M
                .cannotFind(RecDepositRepaymentUrlParams.MODULE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BDepositRepayment result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = DepositRepaymentMessage.M.cannotFind2(
                  RecDepositRepaymentUrlParams.MODULE_CAPTION, billNumber);
              RMsgBox.showErrorAndBack(msg, null);
            }

          }
        });
  }
}
