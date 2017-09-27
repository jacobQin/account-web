/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeGeneralEditWidget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月19日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.StoreLinkField;
import com.hd123.m3.account.gwt.ivc.common.client.ActionName;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvToInvType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchangeDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox.Callback;
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
 * 发票交换单基本信息|编辑控件
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeGeneralEditWidget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {

  private EPInvoiceExchange ep = EPInvoiceExchange.getInstance();
  private BInvoiceExchange entity;

  private RForm basicForm;
  private RViewStringField billNumberField;
  private AccountUnitUCNBox storeField;
  private StoreLinkField storeViewField;
  private RComboBox<BExchangeType> exchangeTypeField;
  private RComboBox<BInvToInvType> invToInvTypeField;
  private RViewStringField exchangeTypeViewField;
  private RViewStringField invToInvTypeViewField;
  private EmployeeUCNBox exchangerField;
  private RDateBox exchangeDateFeild;

  private RCaptionBox operateBox;
  private RForm operateForm;
  private RHTMLField stateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  private PermGroupEditField permGroupField;
  private FiledValueChangeHandler changeHandler = new FiledValueChangeHandler();

  public void setValue(BInvoiceExchange entity, boolean isEditPage) {
    if (entity == null) {
      return;
    }
    this.entity = entity;

    billNumberField.setVisible(isEditPage);
    storeViewField.setVisible(isEditPage);
    storeField.setVisible(!isEditPage);
    exchangeTypeViewField.setVisible(isEditPage);
    exchangeTypeField.setVisible(!isEditPage);

    billNumberField.setValue(entity.getBillNumber());
    storeField.setValue(entity.getAccountUnit());
    storeViewField.setRawValue(entity.getAccountUnit());
    exchangeTypeField.setValue(entity.getType(),true);
    invToInvTypeField.setValue(entity.getInvToInvType());
    if (entity.getType() != null) {
      exchangeTypeViewField.setValue(entity.getType().getCaption());
    }

    invToInvTypeField.setVisible(false);
    invToInvTypeViewField.setVisible(false);
    if (BExchangeType.invToInv.equals(entity.getType())) {
      if (isEditPage) {
        invToInvTypeViewField.setVisible(true);
        if (entity.getInvToInvType() != null) {
          invToInvTypeViewField.setValue(entity.getInvToInvType().getCaption());
        }
      } else {
        invToInvTypeField.setVisible(true);
      }
    }

    exchangerField.setValue(entity.getExchanger());
    exchangeDateFeild.setValue(entity.getExchangeDate());

    operateBox.setVisible(isEditPage);

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
    exchangerField.clearConditions();
  }

  public InvoiceExchangeGeneralEditWidget() {
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

    addActionHandler(this);
  }

  private Widget drawBasicInfo() {
    basicForm = new RForm(1);
    basicForm.setWidth("100%");
    basicForm.setCellSpacing(2);

    billNumberField = new RViewStringField(PInvoiceExchangeDef.constants.billNumber());
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    basicForm.addField(billNumberField);

    storeField = new AccountUnitUCNBox();
    storeField.setCaption(PInvoiceExchangeDef.constants.accountUnit());
    storeField.setRequired(true);
    storeField.addChangeHandler(changeHandler);
    basicForm.addField(storeField);

    storeViewField = new StoreLinkField(PInvoiceExchangeDef.constants.accountUnit());
    basicForm.addField(storeViewField);

    exchangeTypeField = new RComboBox<BExchangeType>(PInvoiceExchangeDef.exchangeType);
    exchangeTypeField.setRequired(true);
    exchangeTypeField.setEditable(false);
    exchangeTypeField.removeNullOption();
    exchangeTypeField.addChangeHandler(changeHandler);
    basicForm.addField(exchangeTypeField);

    exchangeTypeViewField = new RViewStringField(PInvoiceExchangeDef.exchangeType);
    basicForm.addField(exchangeTypeViewField);

    invToInvTypeField = new RComboBox<BInvToInvType>(PInvoiceExchangeDef.invToInvType);
    invToInvTypeField.setRequired(true);
    invToInvTypeField.setEditable(false);
    invToInvTypeField.removeNullOption();
    invToInvTypeField.addChangeHandler(changeHandler);
    basicForm.addField(invToInvTypeField);

    invToInvTypeViewField = new RViewStringField(PInvoiceExchangeDef.invToInvType);
    basicForm.addField(invToInvTypeViewField);

    exchangerField = new EmployeeUCNBox();
    exchangerField.setCaption(PInvoiceExchangeDef.constants.exchanger());
    exchangerField.setRequired(true);
    exchangerField.addChangeHandler(changeHandler);
    basicForm.addField(exchangerField);

    exchangeDateFeild = new RDateBox(PInvoiceExchangeDef.exchangeDate);
    exchangeDateFeild.addChangeHandler(changeHandler);
    basicForm.addField(exchangeDateFeild);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceCommonMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);

    addOptions();

    return box;
  }

  private void addOptions() {
    exchangeTypeField.addOption(BExchangeType.eviToEvi, BExchangeType.eviToEvi.getCaption());
    exchangeTypeField.addOption(BExchangeType.eviToInv, BExchangeType.eviToInv.getCaption());
    exchangeTypeField.addOption(BExchangeType.invToInv, BExchangeType.invToInv.getCaption());

    invToInvTypeField.addOption(BInvToInvType.balance, BInvToInvType.balance.getCaption());
    invToInvTypeField.addOption(BInvToInvType.exchange, BInvToInvType.exchange.getCaption());
  }

  private Widget drawStateAndOperateInfo() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");
    operateForm.setCellSpacing(1);

    stateField = new RHTMLField(PInvoiceExchangeDef.bizState);
    stateField.addStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(stateField);

    createInfoField = new RSimpleOperateInfoField(PInvoiceExchangeDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PInvoiceExchangeDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    operateBox = new RCaptionBox();
    operateBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    operateBox.setCaption(InvoiceCommonMessages.M.operateInfo());
    operateBox.setWidth("100%");
    operateBox.setContent(operateForm);
    return operateBox;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  private class FiledValueChangeHandler implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (storeField == event.getSource()) {
        handStoreChange();
      } else if (exchangeTypeField == event.getSource()) {
        invToInvTypeField.setValue(BInvToInvType.balance, true);
        handExchangeTypeChange();
      } else if (exchangerField == event.getSource()) {
        entity.setExchanger(exchangerField.getValue());
      } else if (exchangeDateFeild == event.getSource()) {
        entity.setExchangeDate(exchangeDateFeild.getValue());
      } else if (invToInvTypeField == event.getSource()) {
        entity.setInvToInvType(invToInvTypeField.getValue());
        fireInvToInvTypeChangeAction();
      }
    }

    private void handStoreChange() {
      BUCN store = entity.getAccountUnit();
      BUCN newStore = storeField.getValue();
      if (store != null && store.equals(newStore)) {
        return;
      }
      if (hasDetails()) {
        storeChangeConfirm();
      } else {
        setStoreAndFireStoreChangeActionEvent();
      }
    }

    private void handExchangeTypeChange() {
      if (needConfirmTypeChange() == false) {
        return;
      } else if (hasDetails()) {
        exchangeTypeChangeConfirm();
      } else {
        setExchangeTypeAndFireExchangeTypeChangeActionEvent();
      }
    }

    private boolean hasDetails() {
      for (BInvoiceExchBalanceLine line : entity.getExchBalanceLines()) {
        if (StringUtil.isNullOrBlank(line.getOldNumber()) == false) {
          return true;
        }
      }

      for (BInvoiceExchAccountLine line : entity.getExchAccountLines()) {
        if (StringUtil.isNullOrBlank(line.getOldNumber()) == false) {
          return true;
        }
      }
      return false;
    }

    private boolean needConfirmTypeChange() {
      if (entity.getType() == null || exchangeTypeField.getValue() == null) {
        return false;
      }

      if (entity.getType().equals(exchangeTypeField.getValue())) {
        return false;
      }

      return true;
    }

    private void setStoreAndFireStoreChangeActionEvent() {
      entity.setAccountUnit(storeField.getValue());
      fireStoreChangeAction();
    }

    private void setExchangeTypeAndFireExchangeTypeChangeActionEvent() {
      entity.setType(exchangeTypeField.getValue());
      if(BExchangeType.invToInv.equals(entity.getType())){
        entity.setInvToInvType(BInvToInvType.balance);
      }
      fireExchangeTypeChangeAction();
    }

    private void storeChangeConfirm() {
      RMsgBox.show(InvoiceExchangeMessages.M.confirmClearDetails(), RMsgBox.ICON_WARN,
          RMsgBox.BUTTONS_OKCANCEL, RMsgBox.BUTTON_OK, new Callback() {
            @Override
            public void onClosed(ButtonConfig clickedButton) {
              if (clickedButton == RMsgBox.BUTTON_OK) {
                setStoreAndFireStoreChangeActionEvent();
              } else {
                storeField.setValue(entity.getAccountUnit());
              }
            }
          });
    }

    private void exchangeTypeChangeConfirm() {
      RMsgBox.show(InvoiceExchangeMessages.M.confirmClearDetails(), RMsgBox.ICON_WARN,
          RMsgBox.BUTTONS_OKCANCEL, RMsgBox.BUTTON_OK, new Callback() {
            @Override
            public void onClosed(ButtonConfig clickedButton) {
              if (clickedButton == RMsgBox.BUTTON_OK) {
                setExchangeTypeAndFireExchangeTypeChangeActionEvent();
              } else {
                exchangeTypeField.setValue(entity.getType());
              }
            }
          });
    }
  }

  private void fireStoreChangeAction() {
    RActionEvent.fire(InvoiceExchangeGeneralEditWidget.this, ActionName.CHANGE_STORE,
        entity.getStore());
  }

  private void fireExchangeTypeChangeAction() {
    RActionEvent.fire(InvoiceExchangeGeneralEditWidget.this,
        ActionName.CHANGE_EXCHANGE_TYPE_ACTION, entity.getType());
  }

  private void fireInvToInvTypeChangeAction() {
    RActionEvent.fire(InvoiceExchangeGeneralEditWidget.this, ActionName.CHANGE_INVTOINVTYPE_ACTION,
        entity.getInvToInvType());
    fireInvoiceDetailsChangeAction();
  }

  /**红冲-交换之间切换时（主要是红冲到交换）通知刷新*/
  private void fireInvoiceDetailsChangeAction() {
    RActionEvent.fire(InvoiceExchangeGeneralEditWidget.this,
        ActionName.CHANGE_INVOICEDETAILS_ACTION, entity.getExchAccountLines());
  }
  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName().equals(ActionName.CHANGE_EXCHANGE_TYPE_ACTION)) {
      invToInvTypeField.clearValidResults();
      if (BExchangeType.invToInv.equals(entity.getType())) {
        invToInvTypeField.setVisible(true);
        basicForm.rebuild();
      } else {
        invToInvTypeField.setVisible(false);
        basicForm.rebuild();
        entity.setInvToInvType(null);
      }
    }
  }

}
