/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FreezeLoader.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-3 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.freeze.client.EPFreeze;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeMessages;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 账款冻结单实体数据载入器
 * 
 * @author zhuhairui
 * 
 */
public class FreezeLoader {

  private BFreeze entity;
  private EPFreeze ep = EPFreeze.getInstance();

  public BFreeze getEntity() {
    return entity;
  }

  public void decoderParams(JumpParameters params, final Command callback) {
    assert callback != null;
    final String uuid = params.getUrlRef().get(FreezeUrlParams.View.PN_ENTITY_UUID);
    final String billNumber = params.getUrlRef().get(FreezeUrlParams.View.PN_ENTITY_BILLNUMBER);
    entity = (BFreeze) params.getExtend().get(EPFreeze.OPN_ENTITY);

    if (uuid == null && billNumber == null) {
      RMsgBox.showError(FreezeMessages.M.requiredUrlParams(), null);
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

  private void doLoad(final String uuid, final Command callback) {
    RLoadingDialog.show(FreezeMessages.M.loading());
    FreezeService.Locator.getService().load(uuid, new RBAsyncCallback2<BFreeze>() {
      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = FreezeMessages.M.notFind(ep.getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BFreeze result) {
        RLoadingDialog.hide();
        if (result == null) {
          String msg = FreezeMessages.M.cannotFind(ep.getModuleCaption());
          RMsgBox.showErrorAndBack(msg, null);
          return;
        }
        entity = result;
        callback.execute();
      }
    });
  }

  private void doLoadByBillNumber(final String billNumber, final Command callback) {
    RLoadingDialog.show(FreezeMessages.M.loading());

    FreezeService.Locator.getService().loadByNumber(billNumber, new RBAsyncCallback2<BFreeze>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = FreezeMessages.M.notFind(ep.getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BFreeze result) {
        RLoadingDialog.hide();
        if (result == null) {
          String msg = FreezeMessages.M.cannotFind(ep.getModuleCaption());
          RMsgBox.showErrorAndBack(msg, null);
          return;
        }
        entity = result;
        callback.execute();
      }
    });
  }
}
