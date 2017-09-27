/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustServiceAgent.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-6 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.statement.adjust.client.EPStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustServiceAgent {
  private static EPStatementAdjust ep = EPStatementAdjust.getInstance();

  public static void executeTask(final BOperation operation, final BStatementAdjust entity,
      final BProcessContext processCtx, boolean saveBeforeAction,
      final AsyncCallback<Object> callback) {
    StatementAdjustService.Locator.getService().executeTask(operation, entity,
        processCtx.getTask(), saveBeforeAction, new RBAsyncCallback2<String>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            callback.onFailure(caught);
            String msg = StatementAdjustMessages.M.actionFailed(operation.getOutgoingDef()
                .getCaption(), ep.getModuleCaption());
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

  public interface TaskCallback {
    public void execute(String value);
  }

}
