/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositRepaymentGeneralEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-26 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.actionname.DepositRepaymentActionName;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentDef;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.EPRecDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.widget.RecDepositBrowseBox;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox.ConfirmCallback;
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
 * @author zhuhairui
 * 
 */
public class RecDepositRepaymentGeneralEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {

  private EPRecDepositRepayment ep = EPRecDepositRepayment.getInstance();

  public RecDepositRepaymentGeneralEditGadget() {
    drawSelf();
  }

  private BDepositRepayment entity;

  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField accountCenterField;
  private RViewStringField countPartField;
  private ContractBrowseBox contractBox;
  private RViewStringField contractNameField;
  private RecDepositBrowseBox depositBox;

  private RForm repaymentInfoForm;
  private RComboBox<BUCN> paymentTypeField;
  private RDateBox repaymentDateField;
  private RDateBox accountDateField;
  private EmployeeUCNBox employeeBox;
  private RComboBox<BBank> bankField;
  private RTextBox counterContact;

  private PermGroupEditField permGroupField;

  private RForm totalForm;
  private RViewNumberField repaymentTotalField;

  private RForm operateForm;
  private RViewStringField reasonField;
  private RViewStringField stateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

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
    mvp.add(0, drawRepaymentInfoPanel());

    mvp.add(1, drawOperatePanel());
    mvp.add(1, drawPermGroup());
    mvp.add(1, drawTotalPanel());

    setCaption(DepositRepaymentMessage.M.generalInfo());
    setWidth("100%");
    setEditing(true);
    setContent(vp);
  }

  private Widget drawGeneralPanel() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    billNumberField = new RViewStringField(PDepositRepaymentDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    accountCenterField = new RViewStringField();
    accountCenterField.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    generalForm.addField(accountCenterField);

    countPartField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    generalForm.addField(countPartField);

    contractBox = new ContractBrowseBox(PDepositRepaymentDef.constants.contract_code(), false,
        new ContractBrowseBox.Callback() {
          @Override
          public void execute(BContract result) {
            onChangeContract();
          }
        }, ep.getCaptionMap());
    contractBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    generalForm.addField(contractBox);

    contractNameField = new RViewStringField(PDepositRepaymentDef.constants.contract_name());
    generalForm.addField(contractNameField);

    depositBox = new RecDepositBrowseBox(new RecDepositBrowseBox.Callback() {
      @Override
      public void execute(BDeposit result) {
        entity.getLines().clear();
        if (result == null) {
          entity.setDeposit(null);
        } else {
          entity.setDeposit(new BUCN(result.getUuid(), result.getBillNumber(), result
              .getBillNumber()));
          List<BDepositLine> lines = result.getLines();
          for (BDepositLine line : lines) {
            BDepositRepaymentLine repaymentLine = new BDepositRepaymentLine();
            repaymentLine.setLineNumber(line.getLineNumber());
            repaymentLine.setSubject(line.getSubject());
            repaymentLine.setAmount(line.getTotal());
            // 设置一个较大值，避免验证
            repaymentLine.setRemainAmount(BigDecimal.valueOf(999999999));
            repaymentLine.setRemark(line.getRemark());
            entity.getLines().add(repaymentLine);
          }
        }
        doFireActionEvent();
      }
    });
    depositBox.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        depositBox.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity
            .getAccountUnit().getUuid());
        depositBox.setCounterpartUuid(entity.getCounterpart() == null ? null : entity
            .getCounterpart().getUuid());
        depositBox.setContractUuid(entity.getContract() == null ? null : entity.getContract()
            .getUuid());
      }
    });
    depositBox.setVisible(ep.isEnableByDeposit());
    generalForm.addField(depositBox);

    RCaptionBox box = new RCaptionBox();
    box.setContent(generalForm);
    box.setCaption(DepositRepaymentMessage.M.basicInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  private Widget drawRepaymentInfoPanel() {
    repaymentInfoForm = new RForm(1);
    repaymentInfoForm.setWidth("100%");

    paymentTypeField = new RComboBox<BUCN>();
    paymentTypeField.setFieldDef(PDepositRepaymentDef.paymentType);
    paymentTypeField.setMaxDropdownRowCount(10);
    paymentTypeField.setEditable(false);
    paymentTypeField.addChangeHandler(handler);
    repaymentInfoForm.addField(paymentTypeField);

    repaymentDateField = new RDateBox(PDepositRepaymentDef.constants.repaymentDate());
    repaymentDateField.setRequired(true);
    repaymentDateField.addChangeHandler(handler);
    repaymentInfoForm.addField(repaymentDateField);

    accountDateField = new RDateBox(PDepositRepaymentDef.accountDate);
    accountDateField.addChangeHandler(handler);
    repaymentInfoForm.addField(accountDateField);

    employeeBox = new EmployeeUCNBox();
    employeeBox.addChangeHandler(handler);
    employeeBox.setCaption(PDepositRepaymentDef.constants.dealer());
    repaymentInfoForm.addField(employeeBox);

    bankField = new RComboBox<BBank>();
    bankField.setMaxLength(66);
    bankField.setMaxDropdownRowCount(10);
    bankField.setNullOptionText(DepositRepaymentMessage.M.nullOption());
    bankField.setCaption(DepositRepaymentMessage.M.bank());
    bankField.setEditable(false);
    bankField.addChangeHandler(handler);
    repaymentInfoForm.addField(bankField);

    counterContact = new RTextBox(PDepositRepaymentDef.counterContact);
    counterContact.setMaxLength(30);
    counterContact.addChangeHandler(handler);
    repaymentInfoForm.addField(counterContact);

    repaymentInfoForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(repaymentInfoForm);
    box.setWidth("100%");
    box.setCaption(DepositRepaymentMessage.M.repaymentInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    return box;
  }

  private Widget drawPermGroup() {
    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    permGroupField.setCaption(DepositRepaymentMessage.M.permGroup());
    return permGroupField;
  }

  private Widget drawTotalPanel() {
    totalForm = new RForm(1);
    totalForm.setWidth("100%");

    repaymentTotalField = new RViewNumberField(PDepositRepaymentDef.constants.repaymentTotal());
    repaymentTotalField.setWidth("38%");
    repaymentTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    repaymentTotalField.setFormat(GWTFormat.fmt_money);
    totalForm.addField(repaymentTotalField);

    totalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(totalForm);
    box.setCaption(DepositRepaymentMessage.M.total());
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

    stateField = new RViewStringField(PDepositRepaymentDef.constants.bizState());
    stateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(stateField);

    reasonField = new RViewStringField(DepositRepaymentMessage.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PDepositRepaymentDef.constants.createInfo());
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(
        PDepositRepaymentDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfoField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositRepaymentMessage.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  public void clearQueryConditions() {
    if (contractBox != null)
      contractBox.getBrowser().clearConditions();

    if (contractBox.getRawValue() != null)
      contractBox.getRawValue().getMessages().clear();

    if (employeeBox != null)
      employeeBox.clearConditions();

    if (depositBox != null) {
      depositBox.clearConditions();
    }
  }

  public void refresh(BDepositRepayment entity) {
    assert entity != null;
    this.entity = entity;

    initPaymentTypeField();
    initBankField();

    billNumberField.setValue(entity.getBillNumber());
    accountCenterField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
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

    depositBox.setValue(entity.getDeposit() == null ? null : entity.getDeposit().getCode());

    if (validatePaymentField(entity.getPaymentType()) == false)
      entity.setPaymentType(paymentTypeField.getOptions().getValue(0));
    paymentTypeField.setValue(entity.getPaymentType());

    if (entity.getRepaymentDate() == null)
      entity.setRepaymentDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    repaymentDateField.setValue(entity.getRepaymentDate());

    if (entity.getAccountDate() == null)
      entity.setAccountDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    accountDateField.setValue(entity.getAccountDate());

    employeeBox.setValue(entity.getDealer());

    if (validateBankField(entity.getBank()) == false)
      entity.setBank(bankField.getOptions().getValue(0));
    bankField.setValue(entity.getBank());

    counterContact.setValue(entity.getCounterContact());

    stateField.setValue(PDepositRepaymentDef.bizState.getEnumCaption(entity.getBizState()));
    reasonField.setVisible(entity.getBpmMessage() != null);
    reasonField.setValue(entity.getBpmMessage());
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());

    permGroupField.setPerm(entity);

    repaymentTotalField.setValue(entity.getRepaymentTotal());

    doFireActionEvent();

    generalForm.rebuild();
    repaymentInfoForm.rebuild();
    operateForm.rebuild();
    totalForm.rebuild();
  }

  private void initPaymentTypeField() {
    List<BUCN> paymentTypes = EPRecDepositRepayment.getInstance().getPaymentTypes();

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

  public void setFocusOnFirstField() {
    generalForm.focusOnFirstField();
  }

  @Override
  public void onAction(RActionEvent event) {
    if (DepositRepaymentActionName.CHANGE_REPAYMENTTOTAL.equals(event.getActionName())) {
      repaymentTotalField.setValue(entity.caculateTotal());
      entity.setRepaymentTotal(repaymentTotalField.getValueAsBigDecimal());
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
      } else if (event.getSource() == repaymentDateField) {
        entity.setRepaymentDate(repaymentDateField.getValue());
      } else if (event.getSource() == accountDateField) {
        entity.setAccountDate(accountDateField.getValue());
      } else if (event.getSource() == bankField) {
        entity.setBank(bankField.getValue());
      }
    }
  }

  /**
   * 清空预存款单信息
   */
  private void clearDepositInfo() {
    if (depositBox != null) {
      depositBox.clearValue();
      depositBox.clearRawValue();
      depositBox.clearMessages();

      entity.setDeposit(null);
      entity.getLines().clear();
    }
  }

  private void onChangeContract() {
    if (entity.hasValues() == false) {
      doChangeContract();
      doFireActionEvent();
      return;
    }

    if (contractBox.validate() == false || isSameContract())
      return;

    String msg = "改变合同将清空预付款还款单明细行，是否继续？";
    RMsgBox.showConfirm(msg, new ConfirmCallback() {

      @Override
      public void onClosed(boolean confirmed) {
        if (confirmed == false) {
          contractBox.setValue(entity.getContract() == null ? null : entity.getContract().getCode());
          contractNameField.setValue(entity.getContract() == null ? null : entity.getContract()
              .getName());
          return;
        }
        doChangeContract();
        entity.getLines().clear();
        repaymentTotalField.clearValue();
        clearDepositInfo();
        doFireActionEvent();
        RActionEvent.fire(RecDepositRepaymentGeneralEditGadget.this,
            DepositRepaymentActionName.REFRESH);
      }
    });
  }

  private boolean isSameContract() {
    BUCN oldValue = entity.getContract();
    BContract newValue = contractBox.getRawValue();
    if (oldValue == null || oldValue.getUuid() == null || newValue == null
        || newValue.getUuid() == null) {
      return false;
    }

    if (oldValue.getUuid().equals(newValue.getUuid())) {
      return true;
    }
    return false;
  }

  private void doChangeContract() {
    BContract contract = contractBox.getRawValue();
    if (contract == null) {
      entity.setContract(null);
    } else {
      entity
          .setContract(new BUCN(contract.getUuid(), contract.getBillNumber(), contract.getTitle()));
    }
    contractNameField.setValue(contract == null ? null : contract.getName());
  }

  public void doFireActionEvent() {
    RActionEvent.fire(RecDepositRepaymentGeneralEditGadget.this,
        DepositRepaymentActionName.CHANGE_ACCOUNTUNIT, entity.getAccountUnit() == null ? null
            : entity.getAccountUnit().getUuid());
    RActionEvent.fire(RecDepositRepaymentGeneralEditGadget.this,
        DepositRepaymentActionName.CHANGE_COUNTERPART, entity.getCounterpart().getUuid());
    RActionEvent.fire(RecDepositRepaymentGeneralEditGadget.this,
        DepositRepaymentActionName.CHANGE_CONTRACT, entity.getContract() == null ? null : entity
            .getContract().getUuid());
    RActionEvent.fire(RecDepositRepaymentGeneralEditGadget.this,
        DepositRepaymentActionName.CHANGE_DEPOSIT, entity.getDeposit() == null ? null : entity
            .getDeposit().getUuid(), ep.isEnableByDeposit());
  }

}
