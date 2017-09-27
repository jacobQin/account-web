/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositMoveLoader.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.account.gwt.depositmove.payment.client.EPPayDepositMove;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.PayDepositMoveUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 预付款转移单加载器
 * 
 * @author zhuhairui
 * 
 */
public class PayDepositMoveLoader {
  private static BDepositMove entity;
  private static EPPayDepositMove ep = EPPayDepositMove.getInstance();

  public static BDepositMove getEntity() {
    return entity;
  }

  public static void decodeParams(JumpParameters params, final Command callback) {
    assert callback != null;
    String billUuid = null;
    String billNumber = null;

    if (ep.isProcessMode()) {
      billUuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      billUuid = params.getUrlRef().get(PayDepositMoveUrlParams.Base.PN_ENTITY_UUID);
      billNumber = params.getUrlRef().get(PayDepositMoveUrlParams.Base.PN_ENTITY_BILLNUMBER);
    }
    entity = (BDepositMove) params.getExtend().get(EPPayDepositMove.OPN_ENTITY);
    if (billUuid == null && billNumber == null) {
      RMsgBox.showErrorAndBack(DepositMoveMessage.M.requiredUrlParams(), null);
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
    PayDepositMoveService.Locator.getService().load(billUuid, new RBAsyncCallback2<BDepositMove>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = DepositMoveMessage.M.cannotFind(PayDepositMoveUrlParams.MODULE_CAPTION);
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BDepositMove result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          callback.execute();
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = DepositMoveMessage.M.cannotFind(PayDepositMoveUrlParams.MODULE_CAPTION);
          RMsgBox.showErrorAndBack(msg, null);
        }
      }
    });
  }

  private static void doLoadByNumber(final String billNumber, final Command callback) {
    RLoadingDialog.show();
    PayDepositMoveService.Locator.getService().loadByNumber(billNumber,
        new RBAsyncCallback2<BDepositMove>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.cannotFind(PayDepositMoveUrlParams.MODULE_CAPTION);
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BDepositMove result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = DepositMoveMessage.M.cannotFind2(PayDepositMoveUrlParams.MODULE_CAPTION,
                  billNumber);
              RMsgBox.showErrorAndBack(msg, null);
            }
          }
        });
  }
}
