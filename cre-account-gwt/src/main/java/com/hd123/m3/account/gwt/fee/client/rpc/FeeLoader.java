/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeLoader.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams.Base;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author subinzhu
 * 
 */
public class FeeLoader implements Base {

  private EPFee ep = EPFee.getInstance();

  private BFee entity;

  public BFee getEntity() {
    return entity;
  }

  public void decodeParams(JumpParameters params, final Command callback) {
    assert callback != null;
    String uuid = null;
    String entityBK = null;

    if (ep.isProcessMode()) {
      uuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      uuid = params.getUrlRef().get(PN_UUID);
      entityBK = params.getUrlRef().get(PN_NUMBER);
    }
    entity = (BFee) params.getExtend().get(EPFee.OPN_ENTITY);
    if (uuid != null) {
      if (entity != null && uuid.equals(entity.getUuid()) == false)
        entity = null;

      if (entity != null) {
        callback.execute();
        return;
      } else
        doLoad(uuid, callback);

    } else if (entityBK != null) {
      if (entity != null && entityBK.equals(entity.getBillNumber()) == false)
        entity = null;

      if (entity != null) {
        callback.execute();
        return;
      } else
        doLoadByBillnumber(entityBK, callback);

    } else {
      RMsgBox.showErrorAndBack(FeeMessages.M.requiredUrlParams(), null);
      return;
    }
  }

  private void doLoad(final String uuid, final Command callback) {
    RLoadingDialog.show();
    FeeService.Locator.getService().load(uuid, new RBAsyncCallback2<BFee>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = FeeMessages.M.actionFailed(FeeMessages.M.find(), PFeeDef.TABLE_CAPTION);
        showError(msg, caught);
      }

      @Override
      public void onSuccess(BFee result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          if (callback != null) {
            callback.execute();
          }
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = FeeMessages.M.cannotFind(PFeeDef.TABLE_CAPTION);
          showError(msg, null);
        }

      }
    });
  }

  private void doLoadByBillnumber(final String entityBK, final Command callback) {
    RLoadingDialog.show();
    FeeService.Locator.getService().loadByNumber(entityBK, new RBAsyncCallback2<BFee>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = FeeMessages.M.processError2(FeeMessages.M.find(), PFeeDef.TABLE_CAPTION,
            entityBK);
        showError(msg, caught);
      }

      @Override
      public void onSuccess(BFee result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          if (callback != null) {
            callback.execute();
          }
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = FeeMessages.M.cannotFind2(PFeeDef.TABLE_CAPTION, entityBK);
          showError(msg, null);
        }

      }
    });
  }

  private static void showError(String msg, Throwable caught) {
    RMsgBox.showError(msg, caught, new RMsgBox.Callback() {

      @Override
      public void onClosed(ButtonConfig clickedButton) {
        JumpParameters params = new JumpParameters(FeeUrlParams.Search.START_NODE);
        EPFee.getInstance().jump(params);
      }
    });
  }

}
