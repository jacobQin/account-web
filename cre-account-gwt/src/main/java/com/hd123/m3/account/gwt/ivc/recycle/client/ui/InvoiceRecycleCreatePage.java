/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月11日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.ui;

import java.util.Date;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.recycle.client.EPInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.gadget.InvoiceRecycleDetailGadget;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.gadget.InvoiceRecycleGeneralEditGadget;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.dd.PInvoiceRecycleDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Agent.Callback;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpm2Message;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmCreatePage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票回收单|新建页面
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceRecycleCreatePage extends BaseBpmCreatePage2<BInvoiceRecycle>{
  private static InvoiceRecycleCreatePage instance = null;

  private RAction saveAndCreateAction;

  private ActionHandler actionHandler;
  private Change_Handler changeHandler;
  private InvoiceRecycleGeneralEditGadget generalGadget;
  private InvoiceRecycleDetailGadget detailGadget;
  private RTextArea remarkField;
  private Widget remarkBox;

  public static InvoiceRecycleCreatePage getInstance() {
    if (instance == null) {
      instance = new InvoiceRecycleCreatePage();
    }
    return instance;
  }
  
  private void refreshToolBar(){
    saveAndCreateAction.setVisible((getEP().getProcessCtx().getUrlParams().getDefinitionkey()==null)&&(!getEP().getProcessCtx().isProcessMode()));
    saveAndCreateAction.setEnabled(getEP().isPermitted(BAction.CREATE.getKey()));
  }

  @Override
  public void onHide() {
    super.onHide();
    generalGadget.clearConditions();
  }

  protected void drawToolbar() {
    actionHandler = new ActionHandler();
    
    saveAction = new RAction(RActionFacade.SAVE, actionHandler);
    saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    saveAndCreateAction = new RAction(RActionFacade.SAVE_AND_CREATE, actionHandler);
    RToolbarButton saveAndCreateButton = new RToolbarButton(saveAndCreateAction);
    getToolbar().add(saveAndCreateButton);

    // BPM出口按钮
    injectBpmActions();

    getToolbar().addSeparator();
    cancelAction = new RAction(RActionFacade.CANCEL, actionHandler);
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
    getToolbar().minimizeSeparators();
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    changeHandler = new Change_Handler();

    generalGadget = new InvoiceRecycleGeneralEditGadget();
    root.add(generalGadget);

    detailGadget = new InvoiceRecycleDetailGadget();
    root.add(detailGadget);
    
    generalGadget.addActionHandler(detailGadget);

    remarkBox = drawRemarkGadget();
    root.add(remarkBox);
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceRecycleDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addChangeHandler(changeHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceRecycleDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  protected EPInvoiceRecycle getEP() {
    return EPInvoiceRecycle.getInstance();
  }

  @Override
  protected void newEntity(JumpParameters params, AsyncCallback<BInvoiceRecycle> callback) {
    BInvoiceRecycle target = new BInvoiceRecycle();
    target.setReceiver(getEP().getCurrentEmployee());
    target.setRecycleDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    target.setBizState(BBizStates.INEFFECT);
    if (target.getInvoiceType() == null) {
      for (Map.Entry<String, String> item : getEP().getInvoiceTypes().entrySet()) {
          target.setInvoiceType(item.getKey());
          break;
      }
    }
    callback.onSuccess(target);
  }

  @Override
  protected void refreshEntity() {
    refreshToolBar();
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    generalGadget.setValue(entity, false);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());

    clearValidResults();
    FocusOnFirstFieldUtil.focusOnFirstField(generalGadget);
  }

  /**
   * 保存并新建
   */
  protected void doSaveAndCreate() {
    GWTUtil.blurActiveElement();
    if (!validate()) {
      return;
    }

    beforeSave();
    getEP().getServiceAgent().save(entity, new Callback<BInvoiceRecycle>() {

      @Override
      public void execute(BInvoiceRecycle result) {
        entity = new BInvoiceRecycle();
        entity.setReceiver(getEP().getCurrentEmployee());
        entity.setRecycleDate(RDateUtil.truncate(new Date(),RDateUtil.DAY_OF_MONTH));
        entity.setBizState(BBizStates.INEFFECT);
        if (entity.getInvoiceType() == null) {
          for (Map.Entry<String, String> item : getEP().getInvoiceTypes().entrySet()) {
            if (entity.getInvoiceType() == null) {
              entity.setInvoiceType(item.getKey());
            }
          }
        }
        refreshEntity();
        getMessagePanel().putMessage(
            Message.info(BaseBpm2Message.M.saveSuccessfully(result.toFriendlyStr())));
      }
    });
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == saveAction) {
        doSave();
      } else if (event.getSource() == saveAndCreateAction) {
        doSaveAndCreate();
      } else if (event.getSource() == cancelAction) {
        doCancel();
      }
    }
  }

  private class Change_Handler implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (remarkField == event.getSource()) {
        entity.setRemark(remarkField.getValue());
      }
    }
  }
}
