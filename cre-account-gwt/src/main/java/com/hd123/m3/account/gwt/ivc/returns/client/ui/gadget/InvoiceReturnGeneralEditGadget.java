/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReturnGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.returns.client.ui.gadget;

import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.StoreLinkField;
import com.hd123.m3.account.gwt.ivc.common.client.ActionName;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.returns.client.EPInvoiceReturn;
import com.hd123.m3.account.gwt.ivc.returns.client.biz.BInvoiceReturn;
import com.hd123.m3.account.gwt.ivc.returns.intf.client.dd.PInvoiceReturnDef;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;

/**
 * 发票领退单新建页面|基本信息
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class InvoiceReturnGeneralEditGadget extends RCaptionBox implements HasRActionHandlers{
  private EPInvoiceReturn ep = EPInvoiceReturn.getInstance();
  private BInvoiceReturn entity;

  private RForm basicForm;
  private RViewStringField billNumberField;
  private AccountUnitUCNBox storeField;
  private StoreLinkField storeViewField;
  private RComboBox<String> invoiceTypeField;
  private EmployeeUCNBox receiverField;
  private EmployeeUCNBox returnorField;
  private RDateBox returnDateFeild;

  private RCaptionBox operateBox;
  private RForm operateForm;
  private RHTMLField stateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  private PermGroupEditField permGroupField;
  private Change_Handler changeHandler = new Change_Handler();

  public void setValue(BInvoiceReturn entity, boolean isEdit) {
    if (entity == null) {
      return;
    }
    this.entity = entity;
    billNumberField.setVisible(isEdit);
    storeViewField.setVisible(isEdit);
    storeField.setVisible(!isEdit);

    billNumberField.setValue(entity.getBillNumber());
    storeField.setValue(entity.getAccountUnit());
    storeViewField.setRawValue(entity.getAccountUnit());
    invoiceTypeField.setValue(entity.getInvoiceType());
    receiverField.setValue(entity.getReceiver());
    returnorField.setValue(entity.getReturnor());
    returnDateFeild.setValue(entity.getReturnDate());

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
    receiverField.clearConditions();
    returnorField.clearConditions();
  }

  public InvoiceReturnGeneralEditGadget() {
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

    billNumberField = new RViewStringField(PInvoiceReturnDef.constants.billNumber());
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    basicForm.addField(billNumberField);

    storeField = new AccountUnitUCNBox();
    storeField.setRequired(true);
    storeField.setCaption(PInvoiceReturnDef.constants.accountUnit());
    storeField.addChangeHandler(changeHandler);
    basicForm.addField(storeField);

    storeViewField = new StoreLinkField(PInvoiceReturnDef.constants.accountUnit());
    basicForm.addField(storeViewField);

    invoiceTypeField = new RComboBox<String>(PInvoiceReturnDef.invoiceType);
    invoiceTypeField.removeNullOption();
    invoiceTypeField.setRequired(true);
    invoiceTypeField.setEditable(false);
    for (Map.Entry<String, String> item : ep.getInvoiceTypes().entrySet())
      invoiceTypeField.addOption(item.getKey(), item.getValue());
    invoiceTypeField.addChangeHandler(changeHandler);
    basicForm.addField(invoiceTypeField);

    receiverField = new EmployeeUCNBox();
    receiverField.setRequired(true);
    receiverField.setCaption(PInvoiceReturnDef.constants.receiver());
    receiverField.addChangeHandler(changeHandler);
    basicForm.addField(receiverField);
    
    returnorField = new EmployeeUCNBox();
    returnorField.setRequired(true);
    returnorField.setCaption(PInvoiceReturnDef.constants.returnor());
    returnorField.addChangeHandler(changeHandler);
    basicForm.addField(returnorField);

    returnDateFeild = new RDateBox(PInvoiceReturnDef.returnDate);
    returnDateFeild.addChangeHandler(changeHandler);
    basicForm.addField(returnDateFeild);

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

    stateField = new RHTMLField(PInvoiceReturnDef.bizState);
    stateField.addStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(stateField);

    createInfoField = new RSimpleOperateInfoField(PInvoiceReturnDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PInvoiceReturnDef.lastModifyInfo);
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
        if (entity.getReturnLines().size() > 0
            && !ObjectUtil.isEquals(storeField.getValue(), entity.getAccountUnit())) {
          if (entity.getReturnLines().size() == 1 && entity.getReturnLines().get(0).isEmpty()) {
            entity.setAccountUnit(storeField.getValue());
            RActionEvent.fire(InvoiceReturnGeneralEditGadget.this, ActionName.CHANGE_STORE);
            return;
          }
          RMsgBox.showConfirm(InvoiceCommonMessages.M.confirmClear(),
              new RMsgBox.ConfirmCallback() {
                @Override
                public void onClosed(boolean confirmed) {
                  if (confirmed) {
                    entity.setAccountUnit(storeField.getValue());
                    RActionEvent.fire(InvoiceReturnGeneralEditGadget.this, ActionName.CHANGE_STORE);
                  } else {
                    storeField.setValue(entity.getAccountUnit());
                  }
                }
              });
        }
      } else if (invoiceTypeField == event.getSource()) {
        if (entity.getReturnLines().size() > 0
            && !ObjectUtil.isEquals(invoiceTypeField.getValue(), entity.getInvoiceType())) {
          if (entity.getReturnLines().size() == 1 && entity.getReturnLines().get(0).isEmpty()) {
            entity.setInvoiceType(invoiceTypeField.getValue());
            RActionEvent.fire(InvoiceReturnGeneralEditGadget.this, ActionName.CHANGE_INVOICE_TYPE);
            return;
          }
          RMsgBox.showConfirm(InvoiceCommonMessages.M.confirmClear(),
              new RMsgBox.ConfirmCallback() {
                @Override
                public void onClosed(boolean confirmed) {
                  if (confirmed) {
                    entity.setInvoiceType(invoiceTypeField.getValue());
                    RActionEvent.fire(InvoiceReturnGeneralEditGadget.this,
                        ActionName.CHANGE_INVOICE_TYPE);
                  } else {
                    invoiceTypeField.setValue(entity.getInvoiceType());
                  }
                }
              });
        }
      } else if (receiverField == event.getSource()) {
        entity.setReceiver(receiverField.getValue());
      }  else if (returnorField == event.getSource()) {
        entity.setReturnor(returnorField.getValue());
      } else if (returnDateFeild == event.getSource()) {
        entity.setReturnDate(returnDateFeild.getValue());
      }
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }
}
