/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeLoader.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.internalfee.client.EPInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.InternalFeeMessages;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.gwt.internalfee.intf.client.InternalFeeUrlParams;
import com.hd123.m3.account.gwt.internalfee.intf.client.InternalFeeUrlParams.Base;
import com.hd123.m3.account.gwt.internalfee.intf.client.dd.PInternalFeeDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author liuguilin
 * 
 */
public class InternalFeeLoader implements Base {

  private EPInternalFee ep = EPInternalFee.getInstance();

  private BInternalFee entity;

  public BInternalFee getEntity() {
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
    entity = (BInternalFee) params.getExtend().get(EPInternalFee.OPN_ENTITY);
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
      RMsgBox.showErrorAndBack(InternalFeeMessages.M.requiredUrlParams(), null);
      return;
    }
  }

  private void doLoad(final String uuid, final Command callback) {
    RLoadingDialog.show();
    InternalFeeService.Locator.getService().load(uuid, new RBAsyncCallback2<BInternalFee>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = InternalFeeMessages.M.actionFailed(InternalFeeMessages.M.find(),
            PInternalFeeDef.TABLE_CAPTION);
        showError(msg, caught);
      }

      @Override
      public void onSuccess(BInternalFee result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          if (callback != null) {
            callback.execute();
          }
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = InternalFeeMessages.M.cannotFind(PInternalFeeDef.TABLE_CAPTION);
          showError(msg, null);
        }

      }
    });
  }

  private void doLoadByBillnumber(final String entityBK, final Command callback) {
    RLoadingDialog.show();
    InternalFeeService.Locator.getService().loadByNumber(entityBK,
        new RBAsyncCallback2<BInternalFee>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InternalFeeMessages.M.processError2(InternalFeeMessages.M.find(),
                PInternalFeeDef.TABLE_CAPTION, entityBK);
            showError(msg, caught);
          }

          @Override
          public void onSuccess(BInternalFee result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              if (callback != null) {
                callback.execute();
              }
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = InternalFeeMessages.M.cannotFind2(PInternalFeeDef.TABLE_CAPTION,
                  entityBK);
              showError(msg, null);
            }

          }
        });
  }

  private static void showError(String msg, Throwable caught) {
    RMsgBox.showError(msg, caught, new RMsgBox.Callback() {

      @Override
      public void onClosed(ButtonConfig clickedButton) {
        JumpParameters params = new JumpParameters(InternalFeeUrlParams.Search.START_NODE);
        EPInternalFee.getInstance().jump(params);
      }
    });
  }

}
