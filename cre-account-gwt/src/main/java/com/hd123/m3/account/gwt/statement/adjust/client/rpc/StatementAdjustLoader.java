/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAdjustLoader.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.statement.adjust.client.EPStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 账单调整单实体数据载入器
 * 
 * @author zhuhairui
 * 
 */
public class StatementAdjustLoader {

  public void decoderParams(JumpParameters params, final Command callback) {
    assert callback != null;
    String uuid = null;
    String billNumber = null;
    String scence = null;

    if (ep.isProcessMode()) {
      uuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      uuid = params.getUrlRef().get(StatementAdjustUrlParams.Base.PN_ENTITY_UUID);
      billNumber = params.getUrlRef().get(StatementAdjustUrlParams.Base.PN_ENTITY_BILLNUMBER);
    }
    entity = (BStatementAdjust) params.getExtend().get(EPStatementAdjust.OPN_ENTITY);
    scence = params.getUrlRef().get(StatementAdjustUrlParams.Base.SCENCE);

    if (uuid == null && billNumber == null) {
      RMsgBox.showError(StatementAdjustMessages.M.requiredUrlParams(), null);
      return;
    }

    if (uuid != null) {
      if (entity != null && uuid.equals(entity.getUuid()) == false)
        entity = null;
      if (entity != null) {
        callback.execute();
        return;
      }
      doLoad(uuid, scence, callback);
    } else if (billNumber != null) {
      if (entity != null && billNumber.equals(entity.getBillNumber()) == false)
        entity = null;

      if (entity != null) {
        callback.execute();
        return;
      }
      doLoadByBillNumber(billNumber, scence, callback);
    }
  }

  public BStatementAdjust getEntity() {
    return entity;
  }

  private BStatementAdjust entity;
  private EPStatementAdjust ep = EPStatementAdjust.getInstance();

  private void doLoad(final String uuid, String scence, final Command callback) {
    RLoadingDialog.show(StatementAdjustMessages.M.loading());
    StatementAdjustService.Locator.getService().load(uuid, scence,
        new RBAsyncCallback2<BStatementAdjust>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.notFind(ep.getModuleCaption());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BStatementAdjust result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = StatementAdjustMessages.M.cannotFind(ep.getModuleCaption());
              RMsgBox.showErrorAndBack(msg, null);
            }
          }
        });
  }

  private void doLoadByBillNumber(final String billNumber, String scence, final Command callback) {
    RLoadingDialog.show(StatementAdjustMessages.M.loading());
    StatementAdjustService.Locator.getService().loadByNumber(billNumber, scence,
        new RBAsyncCallback2<BStatementAdjust>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.notFind(ep.getModuleCaption());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BStatementAdjust result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              callback.execute();
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = StatementAdjustMessages.M.cannotFind2(ep.getModuleCaption(), billNumber);
              RMsgBox.showErrorAndBack(msg, null);
            }
          }
        });
  }
}
