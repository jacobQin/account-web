/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementEntityLoader.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.rpc;

import com.google.gwt.user.client.Command;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 账单实体数据载入器
 * 
 * @author huangjunxian
 * 
 */
public class StatementEntityLoader implements StatementUrlParams.Base {
  private EPStatement ep = EPStatement.getInstance();
  private BStatement entity;

  public BStatement getEntity() {
    return entity;
  }

  public void decodeParams(JumpParameters params, final Command callback) {
    assert callback != null;
    String uuid = null;
    String entityBK = null;
    String startNode = null;

    if (ep.isProcessMode()) {
      uuid = ep.getProcessCtx().getTask().getVariable(BpmModuleUrlParams.KEY_ENTITY_UUID);
    } else {
      uuid = params.getUrlRef().get(PN_UUID);
      entityBK = params.getUrlRef().get(PN_BILLNUMBER);
    }
    entity = (BStatement) params.getExtend().get(EPStatement.OPN_ENTITY);
    startNode = params.getUrlRef().get(JumpParameters.PN_START);

    if (uuid != null) {
      if (entity != null && uuid.equals(entity.getUuid()) == false) {
        entity = null;
      }

      if (entity != null) {
        callback.execute();
        return;
      } else {
        doLoad(uuid, startNode, true, callback);
      }
    } else if (entityBK != null) {
      if (entity != null && entityBK.equals(entity.getBillNumber()) == false) {
        entity = null;
      }

      if (entity != null) {
        callback.execute();
        return;
      } else {
        doLoadByBK(entityBK, startNode, true, callback);
      }
    } else {
      RMsgBox.showErrorAndBack(StatementMessages.M.requiredUrlParams(), null);
      return;
    }
  }

  private void doLoad(final String uuid, String startNode, final Boolean errorToBack,
      final Command callback) {
    RLoadingDialog.show();
    StatementService.Locator.getService().load(uuid, startNode, new RBAsyncCallback2<BStatement>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = StatementMessages.M.actionFailed(StatementMessages.M.find(),
            StatementUrlParams.MODULE_CAPTION);
        showError(msg, caught);
      }

      @Override
      public void onSuccess(BStatement result) {
        RLoadingDialog.hide();
        if (result != null) {
          entity = result;
          callback.execute();
        } else if (ep.isProcessMode()) {
          RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
        } else {
          String msg = StatementMessages.M.cannotFind(StatementUrlParams.MODULE_CAPTION);
          showError(msg, null);
        }
      }
    });
  }

  private void doLoadByBK(final String entityBK, String startNode, final Boolean errorToBack,
      final Command callback) {
    RLoadingDialog.show();
    StatementService.Locator.getService().loadByNumber(entityBK, startNode,
        new RBAsyncCallback2<BStatement>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.find(),
                StatementUrlParams.MODULE_CAPTION);
            showError(msg, caught);
          }

          @Override
          public void onSuccess(BStatement result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity = result;
              ep.jumpToViewPage(entity.getUuid());
            } else if (ep.isProcessMode()) {
              RMsgBox.showErrorAndBack(CommonsMessages.M.taskIsOver(), null);
            } else {
              String msg = StatementMessages.M.cannotFind2(StatementUrlParams.MODULE_CAPTION,
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
        JumpParameters params = new JumpParameters(StatementUrlParams.Search.START_NODE);
        EPStatement.getInstance().jump(params);
      }
    });
  }

}
