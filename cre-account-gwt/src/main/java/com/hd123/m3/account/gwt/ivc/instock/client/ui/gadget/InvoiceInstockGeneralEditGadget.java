/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.client.ui.gadget;

import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.StoreLinkField;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.instock.client.EPInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.dd.PInvoiceInstockDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;

/**
 * 发票入库单新建页面|基本信息
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class InvoiceInstockGeneralEditGadget extends RCaptionBox {
  private EPInvoiceInstock ep = EPInvoiceInstock.getInstance();
  private BInvoiceInstock entity;

  private RForm basicForm;
  private RViewStringField billNumberField;
  private AccountUnitUCNBox storeField;
  private StoreLinkField storeViewField;
  private RComboBox<String> invoiceTypeField;
  private EmployeeUCNBox instockorField;
  private RDateBox instockDateFeild;
  private RCheckBox isReceiveBox;

  private RCaptionBox operateBox;
  private RForm operateForm;
  private RHTMLField stateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  private PermGroupEditField permGroupField;
  private Change_Handler changeHandler = new Change_Handler();

  public void setValue(BInvoiceInstock entity, boolean isEdit) {
    if (entity == null) {
      return;
    }
    this.entity = entity;
    billNumberField.setVisible(isEdit);
    storeViewField.setVisible(isEdit);
    storeField.setVisible(!isEdit);
    isReceiveBox.setReadOnly(isEdit);

    billNumberField.setValue(entity.getBillNumber());
    storeField.setValue(entity.getAccountUnit());
    storeField.setRequired(entity.isReceive());
    storeViewField.setRawValue(entity.getAccountUnit());
    invoiceTypeField.setValue(entity.getInvoiceType());
    isReceiveBox.setValue(entity.isReceive());    
    instockorField.setValue(entity.getInstockor());
    instockDateFeild.setValue(entity.getInstockDate());

    operateBox.setVisible(isEdit);

    stateField.setHTML(BBizStates.getCaption(entity.getBizState()));
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();

    permGroupField.setPerm(entity);
    basicForm.rebuild();
    clearValidResults();
  }

  public void clearConditions() {
    storeField.clearConditions();
    instockorField.clearConditions();
  }

  public InvoiceInstockGeneralEditGadget() {
    super();
    setCaption(InvoiceCommonMessages.M.generalInfo());
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);
    setEditing(true);
    drawSelf();
  }

  private void drawSelf() {
    RMultiVerticalPanel mvp = new RMultiVerticalPanel();
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    mvp.setSpacing(5);

    mvp.add(0, drawBasicInfo());

    mvp.add(1, drawStateAndOperateInfo());

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    mvp.add(1, permGroupField);

    setContent(mvp);
  }

  private Widget drawBasicInfo() {
    basicForm = new RForm(1);
    basicForm.setWidth("100%");
    basicForm.setCellSpacing(2);

    billNumberField = new RViewStringField(PInvoiceInstockDef.constants.billNumber());
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    basicForm.addField(billNumberField);

    storeField = new AccountUnitUCNBox();
    storeField.setCaption(PInvoiceInstockDef.constants.accountUnit());
    storeField.addChangeHandler(changeHandler);
    basicForm.addField(storeField);

    storeViewField = new StoreLinkField(PInvoiceInstockDef.constants.accountUnit());
    basicForm.addField(storeViewField);

    isReceiveBox = new RCheckBox(PInvoiceInstockDef.constants.isReceive());
    isReceiveBox.addChangeHandler(changeHandler);
    basicForm.addField(isReceiveBox);
    
    invoiceTypeField = new RComboBox<String>(PInvoiceInstockDef.invoiceType);
    invoiceTypeField.removeNullOption();
    invoiceTypeField.setRequired(true);
    invoiceTypeField.setEditable(false);
    for (Map.Entry<String, String> item : ep.getInvoiceTypes().entrySet())
      invoiceTypeField.addOption(item.getKey(), item.getValue());
    invoiceTypeField.addChangeHandler(changeHandler);
    basicForm.addField(invoiceTypeField);

    instockorField = new EmployeeUCNBox();
    instockorField.setCaption(PInvoiceInstockDef.constants.instockor());
    instockorField.setRequired(true);
    instockorField.addChangeHandler(changeHandler);
    basicForm.addField(instockorField);

    instockDateFeild = new RDateBox(PInvoiceInstockDef.instockDate);
    instockDateFeild.addChangeHandler(changeHandler);
    basicForm.addField(instockDateFeild);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceCommonMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;
  }

  private Widget drawStateAndOperateInfo() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");
    operateForm.setCellSpacing(1);

    stateField = new RHTMLField(PInvoiceInstockDef.bizState);
    stateField.addStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(stateField);

    createInfoField = new RSimpleOperateInfoField(PInvoiceInstockDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PInvoiceInstockDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    operateBox = new RCaptionBox();
    operateBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    operateBox.setCaption(InvoiceCommonMessages.M.operateInfo());
    operateBox.setWidth("100%");
    operateBox.setContent(operateForm);
    return operateBox;
  }

  private class Change_Handler implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (storeField == event.getSource()) {
        entity.setAccountUnit(storeField.getValue());
      } else if (invoiceTypeField == event.getSource()) {
        entity.setInvoiceType(invoiceTypeField.getValue());
      } else if (instockorField == event.getSource()) {
        entity.setInstockor(instockorField.getValue());
      } else if (instockDateFeild == event.getSource()) {
        entity.setInstockDate(instockDateFeild.getValue());
      }else if(isReceiveBox==event.getSource()){
        entity.setReceive(isReceiveBox.getValue());
        storeField.setRequired(entity.isReceive());
        basicForm.rebuild();
      }
    }
  }
}
