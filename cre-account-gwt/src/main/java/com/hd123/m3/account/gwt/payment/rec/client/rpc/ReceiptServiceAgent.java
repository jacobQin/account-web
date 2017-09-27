/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	ReceiptServiceAgent.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - subinzhu- 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.rpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author subinzhu
 * 
 */
public class ReceiptServiceAgent {

  private static EPReceipt ep = EPReceipt.getInstance();

  /** 产生预存款科目缓存键值前缀 */
  public static final String KEY_PREFIX_GENSUB = "keyPrefixGenSub";
  /** 扣预存款科目缓存键值前缀 */
  public static final String KEY_PREFIX_DECSUB = "keyPrefixDecSub";

  public static void executeTask(final BOperation operation, final BPayment entity,
      final BProcessContext processCtx, boolean saveBeforeAction,
      final AsyncCallback<Object> callback) {
    ReceiptService.Locator.getService().executeTask(operation, entity,
        ep.getProcessCtx().getTask(), saveBeforeAction, new RBAsyncCallback2<String>() {

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

  public static void filterSubjects(final String keyPrefix, List<BUCN> subjects, BUCN store,
      final BUCN subject, final BUCN contract, final Callback callback) {
    
    //为空则忽略此操作
    if(subjects ==null || subjects.isEmpty()){
      callback.execute(subjects);
      return;
    }
    
    //如果未启用核算主体则不需要进行过滤
    if(ep.getConfig().isAccObjectEnabled()==false){
      callback.execute(subjects);
      return;
    }

    String key = keyPrefix + ";" + subject.getUuid() + ";" + contract.getUuid();
    List<BUCN> result = subjectsMap.get(key);
    if (result != null) {
      callback.execute(result);
      return;
    }

    ReceiptService.Locator.getService().filterSubjects(subjects, store, subject, contract,
        new AsyncCallback<List<BUCN>>() {

          @Override
          public void onSuccess(List<BUCN> result) {
            RLoadingDialog.hide();
            subjectsMap.put(keyPrefix + ";" + subject.getUuid() + ";" + contract.getUuid(), result);
            callback.execute(result);
          }

          @Override
          public void onFailure(Throwable caught) {
            RLoadingDialog.hide();
            String msg = CommonsMessages.M.actionFailed("处理", "科目");
            RMsgBox.showError(msg, caught);
          }
        });
  }

  /** 科目缓存 */
  private static final Map<String, List<BUCN>> subjectsMap = new HashMap<String, List<BUCN>>();

  public interface Callback {
    public void execute(List<BUCN> value);
  }

  public interface TaskCallback {
    public void execute(String value);
  }
}
