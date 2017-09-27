/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementServiceAgent.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-18 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author huangjunxian
 * 
 */
public class StatementServiceAgent {
  private static EPStatement ep = EPStatement.getInstance();

  public static void executeTask(final BOperation operation, final BStatement entity,
      final BProcessContext processCtx, boolean saveBeforeAction, final AsyncCallback<Object> callback) {
    StatementService.Locator.getService().executeTask(operation, entity, processCtx.getTask(),
        saveBeforeAction, new RBAsyncCallback2<String>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            callback.onFailure(caught);
            String msg = CommonsMessages.M.actionFailed(operation.getOutgoingDef().getCaption(),
                ep.getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(String result) {
            RLoadingDialog.hide();
            callback.onSuccess(result);
            String msg = CommonsMessages.M.onSuccess2(operation.getOutgoingDef().getCaption(),
                ep.getModuleCaption());
            if (BBizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction())) {
              ep.jumpToSearchPage(Message.info(msg));
            } else {
              String uuid = result;
              if (uuid == null && entity != null) {
                uuid = entity.getUuid();
              }
              ep.jumpToNextTaskViewPage(uuid, Message.info(msg));
            }
          }
        });
  }

  public static void save(BStatement entity, final Callback callback) {
    RLoadingDialog.show(CommonsMessages.M.actionDoing(CommonsMessages.M.save()));
    StatementService.Locator.getService().save(entity, ep.getProcessCtx(),
        new RBAsyncCallback2<BStatement>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.save(),
                ep.getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BStatement result) {
            RLoadingDialog.hide();
            if (callback != null)
              callback.execute(result);
          }
        });
  }

  public interface Callback {
    public void execute(BStatement entity);
  }

}
