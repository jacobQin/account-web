/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayGeneralCreateGadget.java
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
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CounterpartBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.EmployeeBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.deposit.commons.client.actionname.DepositActionName;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositDef;
import com.hd123.m3.account.gwt.deposit.payment.client.EPPayDeposit;
import com.hd123.m3.account.gwt.deposit.payment.client.rpc.PayDepositService;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
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
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author chenpeisi
 * 
 */
public class PayDepositGeneralCreateGadget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {

  private EPPayDeposit ep = EPPayDeposit.getInstance(EPPayDeposit.class);

  public PayDepositGeneralCreateGadget() {
    drawSelf();
  }

  private BDeposit entity;

  private RForm generalForm;
  private AccountUnitUCNBox accountUnitUCNBox;
  private RCombinedField countpartField;
  private CounterpartUCNBox countpartUCNBox;
  private RViewStringField countpartTypeField;
  private ContractBrowseBox contractBox;
  private RViewStringField contractNameField;

  private RForm paymentInfoForm;
  private RComboBox<BUCN> paymentTypeField;
  private RDateBox depositDateField;
  private RDateBox accountDateField;
  private EmployeeUCNBox employeeBox;
  private RComboBox<BBank> bankField;
  private RTextBox counterContact;

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

    accountUnitUCNBox = new AccountUnitUCNBox();
    accountUnitUCNBox.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    accountUnitUCNBox.setRequired(true);
    accountUnitUCNBox.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        entity.setAccountUnit(accountUnitUCNBox.getValue());
        doRefreshContract();
        refreshBanks();
      }
    });
    generalForm.addField(accountUnitUCNBox);

    countpartUCNBox = new CounterpartUCNBox(ep.getCaptionMap());
    countpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    countpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    countpartUCNBox.setRequired(true);
    countpartUCNBox.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        onChangeCounterpart();
        if (ep.getCounterpartTypeMap().size() > 1) {
          String counterpartType = countpartUCNBox.getRawValue() == null ? null : countpartUCNBox
              .getRawValue().getCounterpartType();
          countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap()
              .get(counterpartType));
        }
      }
    });

    countpartTypeField = new RViewStringField();

    countpartField = new RCombinedField() {
      {
        setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        if (ep.getCounterpartTypeMap().size() > 1) {
          addField(countpartUCNBox, 0.85f);
          addField(new HTML(), 0.05f);
          addField(countpartTypeField, 0.1f);
        } else {
          addField(countpartUCNBox, 1);
        }
      }

      @Override
      public boolean validate() {
        return countpartUCNBox.validate();
      }

      @Override
      public void clearMessages() {
        countpartUCNBox.clearMessages();
      }

      @Override
      public boolean isValid() {
        return countpartUCNBox.isValid();
      }

      @Override
      public List<Message> getInvalidMessages() {
        return countpartUCNBox.getInvalidMessages();
      }
    };
    countpartField.setRequired(true);
    generalForm.addField(countpartField);

    contractBox = new ContractBrowseBox(PDepositDef.constants.contract_code(), true,
        new ContractBrowseBox.Callback() {
          @Override
          public void execute(BContract result) {
            if (result == null) {
              entity.setContract(null);
              contractNameField.clearValue();
            } else {
              if (contractBox.isValid() == false)
                return;

              entity.setContract(new BUCN(result.getUuid(), result.getCode(), result.getName()));
              contractBox.setValue(result.getBillNumber());
              contractBox.setRawValue(result);
              contractNameField.setValue(result.getTitle());
            }

            onChangeContract();
            refreshBanks();
          }
        }, ep.getCaptionMap());
    contractBox.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        if (entity.getCounterpart() != null) {
          contractBox.setCounterpartLike(entity.getCounterpart().getCode());
          contractBox.setCounterpartEqual(entity.getCounterpart().getCounterpartType());
        }
        if (entity.getAccountUnit() != null)
          contractBox.setAccountUnitLike(entity.getAccountUnit().getCode());
      }
    });
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
    bankField.setEditable(false);
    bankField.setNullOptionText(DepositMessage.M.nullOption());
    bankField.setCaption(DepositMessage.M.bank());
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

  public void clearQueryConditions() {
    if (accountUnitUCNBox != null)
      ((AccountUnitBrowserDialog) accountUnitUCNBox.getBrowser()).clearConditions();

    if (countpartUCNBox != null)
      ((CounterpartBrowserDialog) countpartUCNBox.getBrowser()).clearConditions();

    if (contractBox != null)
      contractBox.clearConditions();

    if (contractBox.getRawValue() != null)
      contractBox.getRawValue().getMessages().clear();

    if (employeeBox != null)
      ((EmployeeBrowserDialog) employeeBox.getBrowser()).clearConditions();

    if (bankField != null)
      bankField.clearOptions();
  }

  public void refresh(BDeposit entity) {
    assert entity != null;
    this.entity = entity;

    initPaymentTypeField();
    accountUnitUCNBox.setValue(entity.getAccountUnit());
    countpartUCNBox.setValue(entity.getCounterpart());
    refreshBanks();
    String counterpartType = entity.getCounterpart() == null ? null : entity.getCounterpart()
        .getCounterpartType();
    if (ep.getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap().get(
          counterpartType));
    contractBox.setValue(entity.getContract() == null ? null : entity.getContract().getCode());
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());

    if (entity.getPaymentType() == null && paymentTypeField.getOptionCount() > 0)
      entity.setPaymentType(paymentTypeField.getOptions().getValue(0));
    paymentTypeField.setValue(entity.getPaymentType());

    if (entity.getDepositDate() == null)
      entity.setDepositDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    depositDateField.setValue(entity.getDepositDate());

    if (entity.getAccountDate() == null)
      entity.setAccountDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    accountDateField.setValue(entity.getAccountDate());

    if (entity.getDealer() == null || entity.getDealer().getCode() == null)
      entity.setDealer(ep.getCurrentEmployee());
    employeeBox.setValue(entity.getDealer());

    bankField.setValue(entity.getBank());
    counterContact.setValue(entity.getCounterContact());

    if (entity.getDepositTotal() == null)
      entity.setDepositTotal(BigDecimal.ZERO);
    depositTotalField.setValue(entity.getDepositTotal());

    permGroupField.setPerm(entity);

    generalForm.rebuild();
    paymentInfoForm.rebuild();
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

  public void focusOnFirstField() {
    accountUnitUCNBox.setFocus(true);
  }

  @Override
  public void onAction(RActionEvent event) {
    if (DepositActionName.CHANGE_TOTAL.equals(event.getActionName())) {
      depositTotalField.setValue(entity.caculateTotal());
      entity.setDepositTotal(depositTotalField.getValueAsBigDecimal());
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
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
      }
    }
  }

  private void refreshBanks() {
    if (accountUnitUCNBox.validate() == false)
      return;

    List<BBank> banks = ep.getBanks();
    List<BBank> results = new ArrayList<BBank>();
    for (BBank bank : banks) {
      if (entity.getAccountUnit().getUuid().equals(bank.getStore().getUuid())) {
        results.add(bank);
      }
    }
    bankField.clearValue();
    bankField.clearOptions();
    bankField.addOptionList(results);

    RActionEvent.fire(PayDepositGeneralCreateGadget.this, DepositActionName.CHANGE_REMAINTOTAL);
  }

  protected void onChangeCounterpart() {
    entity.setCounterpart(countpartUCNBox.getRawValue());

    doRefreshContract();

    if (countpartUCNBox.validate() == false)
      return;

    RActionEvent.fire(PayDepositGeneralCreateGadget.this, DepositActionName.CHANGE_REMAINTOTAL);
  }

  private void onChangeContract() {
    BContract contract = contractBox.getRawValue();
    if (contract == null || StringUtil.isNullOrBlank(contract.getUuid())) {
      RActionEvent.fire(PayDepositGeneralCreateGadget.this, DepositActionName.CHANGE_REMAINTOTAL);
      return;
    }

    doRefreshAccountUnit(contract);
    doRefreshCounterpart(contract);

    if (contractBox.validate() == false)
      return;

    RActionEvent.fire(PayDepositGeneralCreateGadget.this, DepositActionName.CHANGE_REMAINTOTAL);
  }

  private void doRefreshAccountUnit(BContract contract) {
    if (contract == null || StringUtil.isNullOrBlank(contract.getUuid()))
      return;

    entity.setAccountUnit(contract.getAccountUnit());
    accountUnitUCNBox.setValue(entity.getAccountUnit());
  }

  private void doRefreshCounterpart(BContract contract) {
    if (contract == null || StringUtil.isNullOrBlank(contract.getUuid()))
      return;

    entity.setCounterpart(contract.getCounterpart());

    countpartUCNBox.setValue(entity.getCounterpart());
    if (ep.getCounterpartTypeMap().size() > 1)
      countpartTypeField.setValue(entity.getCounterpart() == null ? null : ep
          .getCounterpartTypeMap().get(entity.getCounterpart().getCounterpartType()));
  }

  private void doRefreshContract() {
    if (entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null
        || entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null)
      return;

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(DepositMessage.M.loading());
    PayDepositService.Locator.getService().getContract(entity.getAccountUnit().getUuid(),
        entity.getCounterpart().getUuid(), entity.getCounterpart().getCounterpartType(),// TODO
                                                                                        // 对方单位类型
        new RBAsyncCallback2<BContract>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.load(),
                PDepositDef.constants.contract());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BContract result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity.setContract(new BUCN(result.getUuid(), result.getCode(), result.getName()));
              contractBox.setValue(result.getBillNumber());
              contractBox.setRawValue(result);
              contractNameField.setValue(result.getName());
            } else {
              entity.setContract(null);
              contractBox.clearValue();
              contractBox.clearRawValue();
              contractBox.clearValidResults();
              contractNameField.clearValue();
            }
          }
        });
  }
}
