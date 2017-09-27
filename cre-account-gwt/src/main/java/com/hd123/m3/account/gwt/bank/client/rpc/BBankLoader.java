/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： BankLoader.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.client.rpc;

import com.hd123.m3.account.gwt.bank.client.BankMessages;
import com.hd123.m3.account.gwt.bank.client.EPBank;
import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.m3.account.gwt.bank.intf.client.BankUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenrizhang
 * 
 */
public class BBankLoader implements BankUrlParams.Base {
  private static EPBank ep = EPBank.getInstance();

  public static void decodeParams(JumpParameters params, final Callback callback) {
    assert callback != null;

    String entityUuid = params.getUrlRef().get(PN_ENTITY_UUID);
    String entityCode = params.getUrlRef().get(PN_ENTITY_CODE);
    Object object = params.getExtend().get(EPBank.OPN_ENTITY);
    BBank entity = (object instanceof BBank ? (BBank) object : null);

    if (entityUuid == null && entityCode == null) {
      RMsgBox.showErrorAndBack(BankMessages.M.requiredUrlParams(), null);
      return;
    }
    if (entityUuid != null) {
      if (entity != null && !entityUuid.equals(entity.getUuid()))
        entity = null;

      if (entity != null) {
        callback.execute(entity);
        return;
      }
      doLoad(entityUuid, callback);
    } else if (entityCode != null) {
      if (entity != null && entityCode.equals(entity.getCode()) == false)
        entity = null;

      if (entity != null) {
        callback.execute(entity);
        return;
      }

      doLoadByCode(entityCode, callback);
    }
  }

  public static void doLoad(String entityUuid, final Callback callback) {
    RLoadingDialog.show();
    BankService.Locator.getService().load(entityUuid, new RBAsyncCallback2<BBank>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = BankMessages.M.actionFailed(BankMessages.M.find(), ep.getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BBank result) {

        RLoadingDialog.hide();
        if (result == null) {
          String msg = BankMessages.M.cannotFind(ep.getModuleCaption());
          RMsgBox.show(msg, new RMsgBox.Callback() {

            @Override
            public void onClosed(ButtonConfig clickedButton) {
              JumpParameters params = new JumpParameters(BankUrlParams.Search.START_NODE);
              ep.jump(params);
            }
          });
          return;
        }
        callback.execute(result);

      }
    });
  }

  public static void doLoadByCode(final String entityCode, final Callback callback) {
    RLoadingDialog.show();
    BankService.Locator.getService().loadByCode(entityCode, new RBAsyncCallback2<BBank>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = BankMessages.M.actionFailed(BankMessages.M.find(), ep.getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BBank result) {
        RLoadingDialog.hide();
        if (result == null) {
          String msg = BankMessages.M.cannotFind2(ep.getModuleCaption(), entityCode);
          RMsgBox.show(msg, new RMsgBox.Callback() {

            @Override
            public void onClosed(ButtonConfig clickedButton) {
              JumpParameters params = new JumpParameters(BankUrlParams.Search.START_NODE);
              ep.jump(params);
            }
          });
          return;
        }
        callback.execute(result);
      }
    });
  }

  /**
   * 回调接口
   */
  public static interface Callback {

    void execute(BBank result);
  }
}
