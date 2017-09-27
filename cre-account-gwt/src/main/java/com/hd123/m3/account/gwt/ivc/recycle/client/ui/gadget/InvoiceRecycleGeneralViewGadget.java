/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleGeneralViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月11日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.StoreLinkField;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.recycle.client.EPInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.InvoiceRecycleUrlParams;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.dd.PInvoiceRecycleDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 *  发票回收单查看页面|基本信息
 *  
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceRecycleGeneralViewGadget extends RCaptionBox{
  private BInvoiceRecycle entity;
  private EPInvoiceRecycle ep = EPInvoiceRecycle.getInstance();
  private RMultiVerticalPanel root;

  private RForm basicForm;
  private RViewStringField billNumberField;
  private StoreLinkField storeField;
  private RViewStringField invoiceTypeField;
  private RViewStringField receiverField;
  private RViewStringField returnorField;
  private RViewStringField recycleDateFeild;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfo;

  private PermGroupViewField permGroupField;

  public InvoiceRecycleGeneralViewGadget() {
    super();
    setCaption(InvoiceCommonMessages.M.generalInfo());
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);
    drawSelf();
  }

  public void setValue(BInvoiceRecycle entity) {
    if (entity == null) {
      return;
    }
    this.entity = entity;
    billNumberField.setValue(entity.getBillNumber());
    storeField.setRawValue(entity.getAccountUnit());
    invoiceTypeField.setValue(entity.getInvoiceType()==null?null:ep.getInvoiceTypes().get(entity.getInvoiceType()));
    receiverField.setValue(entity.getReceiver()==null?null:entity.getReceiver().toFriendlyStr());
    returnorField.setValue(entity.getReturnor()==null?null:entity.getReturnor().toFriendlyStr());
    recycleDateFeild.setValue(entity.getRecycleDate()==null?null:M3Format.fmt_yMd.format(entity.getRecycleDate()));
    
    bizStateField.setValue(BBizStates.getCaption(entity.getBizState()));
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();
    
    permGroupField.refresh(ep.isPermEnabled(), entity);
    basicForm.rebuild();
  }
  private void drawSelf() {
    root = new RMultiVerticalPanel();
    root.setWidth("100%");
    root.setColumnWidth(0, "50%");
    root.setColumnWidth(1, "50%");
    root.setSpacing(5);

    root.add(0, drawBasicInfo());
    root.add(1, drawOperateInfo());

    permGroupField = new PermGroupViewField();
    root.add(1, permGroupField);
    setContent(root);
  }

  private Widget drawOperateInfo() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");
    operateForm.setCellSpacing(1);

    bizStateField = new RViewStringField(PInvoiceRecycleDef.bizState);
    bizStateField.addStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    createInfoField = new RSimpleOperateInfoField(PInvoiceRecycleDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PInvoiceRecycleDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(new Handler_click());
    moreInfo.setEnabled(true);
    moreInfo.setHTML(CommonsMessages.M.moreInfo());
    operateForm.addField(moreInfo);

    RCaptionBox operateBox = new RCaptionBox();
    operateBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    operateBox.setCaption(InvoiceCommonMessages.M.operateInfo());
    operateBox.setWidth("100%");
    operateBox.setContent(operateForm);
    return operateBox;
  }

  private Widget drawBasicInfo() {
    basicForm = new RForm(1);
    basicForm.setWidth("100%");
    basicForm.setCellSpacing(2);

    billNumberField = new RViewStringField(PInvoiceRecycleDef.constants.billNumber());
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    basicForm.addField(billNumberField);

    storeField = new StoreLinkField(PInvoiceRecycleDef.constants.accountUnit());
    basicForm.addField(storeField);

    invoiceTypeField = new RViewStringField(PInvoiceRecycleDef.invoiceType);
    basicForm.addField(invoiceTypeField);

    receiverField = new RViewStringField(PInvoiceRecycleDef.receiver);
    basicForm.addField(receiverField);
    
    returnorField = new RViewStringField(PInvoiceRecycleDef.returnor);
    basicForm.addField(returnorField);
    
    recycleDateFeild = new RViewStringField(PInvoiceRecycleDef.recycleDate);
    basicForm.addField(recycleDateFeild);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceCommonMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;
  }

  private class Handler_click implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == moreInfo) {
        JumpParameters params = new JumpParameters(InvoiceRecycleUrlParams.START_NODE_LOG);
        params.getUrlRef().set(InvoiceRecycleUrlParams.KEY_ENTITY_UUID, entity.getUuid());
        ep.jump(params);
      }
    }
  }

}
