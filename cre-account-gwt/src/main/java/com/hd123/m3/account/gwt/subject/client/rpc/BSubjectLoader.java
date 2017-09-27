/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： SubjectLoader.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.subject.client.rpc;

import com.hd123.m3.account.gwt.subject.client.EPSubject;
import com.hd123.m3.account.gwt.subject.client.SubjectMessages;
import com.hd123.m3.account.gwt.subject.client.biz.BSubject;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenrizhang
 * 
 */
public class BSubjectLoader implements SubjectUrlParams.Base {
  private static EPSubject ep = EPSubject.getInstance();

  public static void decodeParams(JumpParameters params, final Callback callback) {
    assert callback != null;

    String entityUuid = params.getUrlRef().get(PN_ENTITY_UUID);
    String entityCode = params.getUrlRef().get(PN_ENTITY_CODE);
    Object object = params.getExtend().get(EPSubject.OPN_ENTITY);
    BSubject entity = (object instanceof BSubject ? (BSubject) object : null);

    if (entityUuid == null && entityCode == null) {
      RMsgBox.showErrorAndBack(SubjectMessages.M.requiredUrlParams(), null);
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
    SubjectService.Locator.getService().load(entityUuid, new RBAsyncCallback2<BSubject>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = SubjectMessages.M.actionFailed(SubjectMessages.M.find(), ep.getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BSubject result) {

        RLoadingDialog.hide();
        if (result == null) {
          String msg = SubjectMessages.M.cannotFind(ep.getModuleCaption());
          RMsgBox.show(msg, new RMsgBox.Callback() {

            @Override
            public void onClosed(ButtonConfig clickedButton) {
              JumpParameters urlParams = new JumpParameters();
              urlParams.setStart(SubjectUrlParams.Search.START_NODE);
              ep.jump(urlParams);
            }
          });
          return;
        }
        if (callback != null) {
          callback.execute(result);
        }

      }
    });
  }

  public static void doLoadByCode(final String entityCode, final Callback callback) {
    RLoadingDialog.show();
    SubjectService.Locator.getService().loadByCode(entityCode, new RBAsyncCallback2<BSubject>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = SubjectMessages.M.actionFailed(SubjectMessages.M.find(), ep.getModuleCaption());
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(BSubject result) {
        RLoadingDialog.hide();
        if (result == null) {
          String msg = SubjectMessages.M.cannotFind2(ep.getModuleCaption(), entityCode);
          RMsgBox.show(msg, new RMsgBox.Callback() {

            @Override
            public void onClosed(ButtonConfig clickedButton) {
              JumpParameters params = new JumpParameters(SubjectUrlParams.Search.START_NODE);
              ep.jump(params);
            }
          });
          return;
        }
        if (callback != null) {
          callback.execute(result);
        }
      }
    });
  }

  /**
   * 回调接口
   */
  public static interface Callback {

    void execute(BSubject result);
  }
}
