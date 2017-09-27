/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceTransportCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client.ui;

import java.util.Date;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.transport.client.EPInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.gadget.InvoiceTransportDetailGadget;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.gadget.InvoiceTransportGeneralEditGadget;
import com.hd123.m3.account.gwt.ivc.transport.intf.client.dd.PInvoiceTransportDef;
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
 * 发票调拨单|新建页面
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceTransportCreatePage extends BaseBpmCreatePage2<BInvoiceTransport>{
  private static InvoiceTransportCreatePage instance = null;

  private RAction saveAndCreateAction;

  private ActionHandler actionHandler;
  private Change_Handler changeHandler;
  private InvoiceTransportGeneralEditGadget generalGadget;
  private InvoiceTransportDetailGadget detailGadget;
  private RTextArea remarkField;
  private Widget remarkBox;

  public static InvoiceTransportCreatePage getInstance() {
    if (instance == null) {
      instance = new InvoiceTransportCreatePage();
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

    generalGadget = new InvoiceTransportGeneralEditGadget();
    root.add(generalGadget);

    detailGadget = new InvoiceTransportDetailGadget();
    root.add(detailGadget);
    
    generalGadget.addActionHandler(detailGadget);

    remarkBox = drawRemarkGadget();
    root.add(remarkBox);
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceTransportDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addChangeHandler(changeHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceTransportDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  protected EPInvoiceTransport getEP() {
    return EPInvoiceTransport.getInstance();
  }

  @Override
  protected void newEntity(JumpParameters params, AsyncCallback<BInvoiceTransport> callback) {
    BInvoiceTransport target = new BInvoiceTransport();
    target.setTransportor(getEP().getCurrentEmployee());
    target.setTransportDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
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
    getEP().getServiceAgent().save(entity, new Callback<BInvoiceTransport>() {

      @Override
      public void execute(BInvoiceTransport result) {
        entity = new BInvoiceTransport();
        entity.setTransportor(getEP().getCurrentEmployee());
        entity.setTransportDate(RDateUtil.truncate(new Date(),RDateUtil.DAY_OF_MONTH));
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
