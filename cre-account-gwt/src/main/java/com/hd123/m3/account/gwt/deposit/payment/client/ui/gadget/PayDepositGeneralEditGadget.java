/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayGeneralEditGadget.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.EmployeeBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.deposit.commons.client.actionname.DepositActionName;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositDef;
import com.hd123.m3.account.gwt.deposit.payment.client.EPPayDeposit;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author chenpeisi
 * 
 */
public class PayDepositGeneralEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {

  private EPPayDeposit ep = EPPayDeposit.getInstance(EPPayDeposit.class);

  public PayDepositGeneralEditGadget() {
    drawSelf();
  }

  private BDeposit entity;

  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private RViewStringField countPartField;
  private ContractBrowseBox contractBox;
  private RViewStringField contractNameField;

  private RForm paymentInfoForm;
  private RComboBox<BUCN> paymentTypeField;
  private RDateBox depositDateField;
  private RDateBox accountDateField;
  private EmployeeUCNBox employeeBox;
  private RComboBox<BBank> bankField;
  private RTextBox counterContact;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField reasonField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  private PermGroupEditField permGroupField;

  private RForm totalForm;
  private RViewNumberField depositTotalField;

  private Handler_textField handler = new Handler_textField();

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawGeneralPanel());
    mvp.add(0, drawPaymentInfoPanel());

    mvp.add(1, drawOperatePanel());

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    mvp.add(1, drawTotalPanel());

    setCaption(DepositMessage.M.generalInfo());
    setWidth("100%");
    setEditing(true);
    setContent(vp);
  }

  private Widget drawGeneralPanel() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    billNumberField = new RViewStringField(PDepositDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    accountUnitField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    generalForm.addField(accountUnitField);

    countPartField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    generalForm.addField(countPartField);

    contractBox = new ContractBrowseBox(PDepositDef.constants.contract_code(), false,
        new ContractBrowseBox.Callback() {
          @Override
          public void execute(BContract result) {
            if (result == null) {
              entity.setContract(null);
            } else {
              if (contractBox.isValid() == false)
                return;

              entity.setContract(new BUCN(result.getUuid(), result.getCode(), result.getName()));
              contractBox.setValue(result.getBillNumber());
              contractBox.setRawValue(result);
              contractNameField.setValue(result.getTitle());
            }

            onChangeContract();
          }
        }, ep.getCaptionMap());
    contractBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    generalForm.addField(contractBox);

    contractNameField = new RViewStringField(PDepositDef.constants.contract_name());
    generalForm.addField(contractNameField);

    RCaptionBox box = new RCaptionBox();
    box.setContent(generalForm);
    box.setCaption(DepositMessage.M.basicInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  private Widget drawPaymentInfoPanel() {
    paymentInfoForm = new RForm(1);
    paymentInfoForm.setWidth("100%");

    paymentTypeField = new RComboBox<BUCN>();
    paymentTypeField.setFieldDef(PDepositDef.paymentType);
    paymentTypeField.setMaxDropdownRowCount(10);
    paymentTypeField.setEditable(false);
    paymentTypeField.addChangeHandler(handler);
    paymentInfoForm.addField(paymentTypeField);

    depositDateField = new RDateBox(DepositMessage.M.paymentDate());
    depositDateField.setRequired(true);
    depositDateField.addChangeHandler(handler);
    paymentInfoForm.addField(depositDateField);

    accountDateField = new RDateBox(PDepositDef.accountDate);
    accountDateField.addChangeHandler(handler);
    paymentInfoForm.addField(accountDateField);

    employeeBox = new EmployeeUCNBox();
    employeeBox.addChangeHandler(handler);
    employeeBox.setCaption(PDepositDef.constants.dealer());
    paymentInfoForm.addField(employeeBox);

    bankField = new RComboBox<BBank>();
    bankField.setMaxDropdownRowCount(10);
    bankField.setMaxLength(66);
    bankField.setNullOptionText(DepositMessage.M.nullOption());
    bankField.setCaption(DepositMessage.M.bank());
    bankField.setEditable(false);
    bankField.addChangeHandler(handler);
    paymentInfoForm.addField(bankField);

    counterContact = new RTextBox(PDepositDef.counterContact);
    counterContact.setMaxLength(30);
    counterContact.addChangeHandler(handler);
    paymentInfoForm.addField(counterContact);

    paymentInfoForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(paymentInfoForm);
    box.setWidth("100%");
    box.setCaption(DepositMessage.M.paymentInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    return box;
  }

  private Widget drawTotalPanel() {
    totalForm = new RForm(1);
    totalForm.setWidth("100%");

    depositTotalField = new RViewNumberField(PDepositDef.constants.depositTotal());
    depositTotalField.setWidth("38%");
    depositTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    depositTotalField.setFormat(GWTFormat.fmt_money);
    totalForm.addField(depositTotalField);

    totalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(totalForm);
    box.setCaption(DepositMessage.M.total());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PDepositDef.constants.bizState());
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    reasonField = new RViewStringField(DepositMessage.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PDepositDef.constants.createInfo());
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PDepositDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfoField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositMessage.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  public void clearQueryConditions() {
    if (contractBox != null)
      contractBox.clearConditions();
    if (contractBox.getRawValue() != null)
      contractBox.getRawValue().getMessages().clear();
    if (employeeBox != null)
      ((EmployeeBrowserDialog) employeeBox.getBrowser()).clearConditions();
  }

  public void refresh(BDeposit entity) {
    assert entity != null;
    this.entity = entity;

    initPaymentTypeField();
    initBankField();

    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .toFriendlyStr());
    countPartField.setValue(entity.getCounterpart() == null ? null : entity.getCounterpart()
        .toFriendlyStr(ep.getCounterpartTypeMap()));
    contractBox.setValue(entity.getContract() == null ? null : entity.getContract().getCode());
    contractBox.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .getUuid());
    contractBox.setCounterpartUuid(entity.getCounterpart() == null ? null : entity.getCounterpart()
        .getUuid());
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());

    if (validatePaymentField(entity.getPaymentType()) == false)
      entity.setPaymentType(paymentTypeField.getOptions().getValue(0));
    paymentTypeField.setValue(entity.getPaymentType());

    if (entity.getDepositDate() == null)
      entity.setDepositDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    depositDateField.setValue(entity.getDepositDate());

    if (entity.getAccountDate() == null)
      entity.setAccountDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    accountDateField.setValue(entity.getAccountDate());

    employeeBox.setValue(entity.getDealer());
    if (validateBankField(entity.getBank()) == false)
      entity.setBank(bankField.getOptions().getValue(0));
    bankField.setValue(entity.getBank());
    counterContact.setValue(entity.getCounterContact());

    bizStateField.setValue(PDepositDef.bizState.getEnumCaption(entity.getBizState()));
    reasonField.setVisible(entity.getBpmMessage() != null);
    reasonField.setValue(entity.getBpmMessage());
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());

    if (entity.getDepositTotal() == null)
      entity.setDepositTotal(BigDecimal.ZERO);
    depositTotalField.setValue(entity.getDepositTotal());

    permGroupField.setPerm(entity);

    generalForm.rebuild();
    paymentInfoForm.rebuild();
    operateForm.rebuild();
    totalForm.rebuild();
  }

  private void initPaymentTypeField() {
    List<BUCN> paymentTypes = ep.getPaymentTypes();

    if (paymentTypes == null || paymentTypes.isEmpty())
      return;

    paymentTypeField.clearOptions();
    for (BUCN payment : paymentTypes) {
      paymentTypeField.addOption(payment, payment.toFriendlyStr());
    }
  }

  private void initBankField() {
    List<BBank> banks = ep.getBanks();
    List<BBank> results = new ArrayList<BBank>();
    bankField.clearOptions();
    for (BBank bank : banks) {
      if (entity.getStore().getUuid().equals(bank.getStore().getUuid())) {
        results.add(bank);
      }
    }
    bankField.addOptionList(results);
  }

  private boolean validatePaymentField(BUCN paymentType) {
    if (paymentTypeField.getOptionCount() <= 0)
      return false;
    if (paymentType == null || paymentType.getCode() == null)
      return false;
    for (BUCN p : paymentTypeField.getOptions().getValues()) {
      if (p == null || p.getCode() == null)
        continue;
      if (p.getCode().equals(paymentType.getCode()))
        return true;
    }
    return false;
  }

  private boolean validateBankField(BBank bank) {
    if (bankField.getOptionCount() <= 0)
      return false;
    if (bank == null || bank.getCode() == null)
      return false;
    for (BBank b : bankField.getOptions().getValues()) {
      if (b == null || b.getCode() == null)
        continue;
      if (b.getCode().equals(bank.getCode()))
        return true;
    }
    return false;
  }

  @Override
  public void onAction(RActionEvent event) {
    if (DepositActionName.CHANGE_TOTAL.equals(event.getActionName())) {
      depositTotalField.setValue(entity.caculateTotal());
      entity.setDepositTotal(depositTotalField.getValueAsBigDecimal());
    }
  }

  private class Handler_textField implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == employeeBox) {
        entity.setDealer(employeeBox.getValue());
      } else if (event.getSource() == counterContact) {
        entity.setCounterContact(counterContact.getValue());
      } else if (event.getSource() == paymentTypeField) {
        entity.setPaymentType(paymentTypeField.getValue());
      } else if (event.getSource() == depositDateField) {
        entity.setDepositDate(depositDateField.getValue());
      } else if (event.getSource() == accountDateField) {
        entity.setAccountDate(accountDateField.getValue());
      } else if (event.getSource() == bankField) {
        entity.setBank(bankField.getValue());
      } else if (event.getSource() == contractBox) {
        entity.setContract(new BUCN(contractBox.getRawValue().getUuid(), contractBox.getRawValue()
            .getCode(), contractBox.getRawValue().getName()));
      }
    }
  }

  private void onChangeContract() {
    BContract contract = contractBox.getRawValue();
    if (contract == null || StringUtil.isNullOrBlank(contract.getUuid()))
      entity.setContract(null);
    else {
      entity.setContract(new BUCN(contract.getUuid(), contract.getCode(), contract.getName()));
    }
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());

    if (contractBox.validate() == false)
      return;

    RActionEvent.fire(PayDepositGeneralEditGadget.this, DepositActionName.CHANGE_REMAINTOTAL);
  }
}
