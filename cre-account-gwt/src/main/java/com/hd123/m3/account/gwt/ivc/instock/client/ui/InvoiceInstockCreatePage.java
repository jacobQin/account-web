/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.client.ui;

import java.util.Date;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.instock.client.EPInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.gadget.InvoiceInstockDetailGadget;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.gadget.InvoiceInstockGeneralEditGadget;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.InvoiceInstockUrlParams.Create;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.dd.PInvoiceInstockDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Agent.Callback;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpm2Message;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmCreatePage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.DateUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票入库单新建页面
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class InvoiceInstockCreatePage extends BaseBpmCreatePage2<BInvoiceInstock> implements Create {
  private static InvoiceInstockCreatePage instance = null;

  private RAction saveAndCreateAction;

  private ActionHandler actionHandler;
  private Change_Handler changeHandler;
  private InvoiceInstockGeneralEditGadget generalGadget;
  private InvoiceInstockDetailGadget detailGadget;
  private RTextArea remarkField;
  private Widget remarkBox;

  public static InvoiceInstockCreatePage getInstance() {
    if (instance == null) {
      instance = new InvoiceInstockCreatePage();
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

    generalGadget = new InvoiceInstockGeneralEditGadget();
    root.add(generalGadget);

    detailGadget = new InvoiceInstockDetailGadget();
    root.add(detailGadget);

    remarkBox = drawRemarkGadget();
    root.add(remarkBox);
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceInstockDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addChangeHandler(changeHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceInstockDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  protected EPInvoiceInstock getEP() {
    return EPInvoiceInstock.getInstance();
  }

  @Override
  protected void newEntity(JumpParameters params, AsyncCallback<BInvoiceInstock> callback) {
    BInvoiceInstock target = new BInvoiceInstock();
    target.setInstockor(getEP().getCurrentEmployee());
    target.setBizState(BBizStates.INEFFECT);
    target.setInstockDate(DateUtil.truncateToDate(new Date()));
    if (target.getInvoiceType() == null) {
      for (Map.Entry<String, String> item : getEP().getInvoiceTypes().entrySet()) {
        if (target.getInvoiceType() == null) {
          target.setInvoiceType(item.getKey());
        }
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
    getEP().getServiceAgent().save(entity, new Callback<BInvoiceInstock>() {

      @Override
      public void execute(BInvoiceInstock result) {
        entity = new BInvoiceInstock();
        entity.setInstockor(getEP().getCurrentEmployee());
        entity.setBizState(BBizStates.INEFFECT);
        entity.setInstockDate(DateUtil.truncateToDate(new Date()));
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
