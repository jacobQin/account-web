/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositRepaymentGeneralCreateGadget.java
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CounterpartBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.actionname.DepositRepaymentActionName;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentDef;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.EPRecDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.widget.RecDepositBrowseBox;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox.ConfirmCallback;
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
 * @author zhuhairui
 * 
 */
public class RecDepositRepaymentGeneralCreateGadget extends RCaptionBox implements
    HasRActionHandlers, RActionHandler {

  private EPRecDepositRepayment ep = EPRecDepositRepayment.getInstance();

  public RecDepositRepaymentGeneralCreateGadget() {
    drawSelf();
  }

  private BDepositRepayment entity;

  private RForm generalForm;

  private AccountUnitUCNBox accountUnitUCNBox;
  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;
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

    mvp.add(1, drawPermGroupPanel());
    mvp.add(1, drawTotalPanel());

    setCaption(DepositRepaymentMessage.M.generalInfo());
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
        onChangeAccountUnit();
      }
    });
    generalForm.addField(accountUnitUCNBox);

    counterpartUCNBox = new CounterpartUCNBox(ep.getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    counterpartUCNBox.setRequired(true);
    counterpartUCNBox.addChangeHandler(handler);

    countpartTypeField = new RViewStringField();

    countpartField = new RCombinedField() {
      {
        setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        if (ep.getCounterpartTypeMap().size() > 1) {
          addField(counterpartUCNBox, 0.85f);
          addField(new HTML(), 0.05f);
          addField(countpartTypeField, 0.1f);
        } else {
          addField(counterpartUCNBox, 1);
        }
      }

      @Override
      public boolean validate() {
        return counterpartUCNBox.validate();
      }

      @Override
      public void clearMessages() {
        counterpartUCNBox.clearMessages();
      }

      @Override
      public boolean isValid() {
        return counterpartUCNBox.isValid();
      }

      @Override
      public List<Message> getInvalidMessages() {
        return counterpartUCNBox.getInvalidMessages();
      }
    };
    countpartField.setRequired(true);
    generalForm.addField(countpartField);

    contractBox = new ContractBrowseBox(PDepositRepaymentDef.constants.contract_code(), true,
        new ContractBrowseBox.Callback() {
          @Override
          public void execute(BContract result) {
            onChangeContract();
            refreshBanks();
          }
        }, ep.getCaptionMap());
    contractBox.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        if (entity.getAccountUnit() != null)
          contractBox.setAccountUnitLike(entity.getAccountUnit().getCode());
        if (entity.getCounterpart() != null) {
          contractBox.setCounterpartLike(entity.getCounterpart().getCode());
          contractBox.setCounterpartEqual(entity.getCounterpart().getCounterpartType());
        }
      }
    });
    contractBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    generalForm.addField(contractBox);

    contractNameField = new RViewStringField(PDepositRepaymentDef.constants.contract_name());
    generalForm.addField(contractNameField);

    depositBox = new RecDepositBrowseBox(new RecDepositBrowseBox.Callback() {
      @Override
      public void execute(BDeposit result) {
        entity.getLines().clear();
        if (result == null || StringUtil.isNullOrBlank(result.getUuid())) {
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

  private Widget drawPermGroupPanel() {
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

  public void clearQueryConditions() {
    if (accountUnitUCNBox != null)
      ((AccountUnitBrowserDialog) accountUnitUCNBox.getBrowser()).clearConditions();

    if (counterpartUCNBox != null)
      ((CounterpartBrowserDialog) counterpartUCNBox.getBrowser()).clearConditions();

    if (contractBox != null)
      contractBox.clearConditions();

    if (contractBox.getRawValue() != null)
      contractBox.getRawValue().getMessages().clear();

    if (employeeBox != null)
      employeeBox.clearConditions();

    if (bankField != null)
      bankField.clearOptions();

    if (depositBox != null) {
      depositBox.clearConditions();
    }
  }

  public void refresh(BDepositRepayment entity) {
    assert entity != null;
    this.entity = entity;

    initPaymentTypeField();

    accountUnitUCNBox.setValue(entity.getAccountUnit());
    if(entity.getAccountUnit() !=null){
      refreshBanks();
    }
    if (entity.getCounterpart() != null) {
      counterpartUCNBox.setValue(entity.getCounterpart());
      if (ep.getCounterpartTypeMap().size() > 1)
        countpartTypeField.setValue(ep.getCounterpartTypeMap().get(
            entity.getCounterpart().getCounterpartType()));
    } else {
      counterpartUCNBox.clearValue();
      counterpartUCNBox.clearValidResults();
      if (ep.getCounterpartTypeMap().size() > 1)
        countpartTypeField.clearValue();
    }
    contractBox.setValue(entity.getContract() == null ? null : entity.getContract().getCode());
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());
    depositBox.setValue(entity.getDeposit() == null ? null : entity.getDeposit().getCode());

    if (entity.getPaymentType() == null && paymentTypeField.getOptionCount() > 0)
      entity.setPaymentType(paymentTypeField.getOptions().getValue(0));
    paymentTypeField.setValue(entity.getPaymentType());

    if (entity.getRepaymentDate() == null)
      entity.setRepaymentDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    repaymentDateField.setValue(entity.getRepaymentDate());

    if (entity.getAccountDate() == null)
      entity.setAccountDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    accountDateField.setValue(entity.getAccountDate());

    if (entity.getDealer() == null || entity.getDealer().getUuid() == null)
      entity.setDealer(ep.getCurrentEmployee());
    employeeBox.setValue(entity.getDealer());

    if (entity.getBank() == null && bankField.getOptionCount() > 0)
      entity.setBank(bankField.getOptions().getValue(0));
    bankField.setValue(entity.getBank());
    counterContact.setValue(entity.getCounterContact());

    permGroupField.setPerm(entity);

    if (entity.getRepaymentTotal() == null)
      entity.setRepaymentTotal(BigDecimal.ZERO);
    repaymentTotalField.setValue(entity.getRepaymentTotal());

    doFireActionEvent();

    generalForm.rebuild();
    repaymentInfoForm.rebuild();
    totalForm.rebuild();
  }

  private void initPaymentTypeField() {
    List<BUCN> paymentTypes = EPRecDepositRepayment.getInstance().getPaymentTypes();

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
    if (DepositRepaymentActionName.CHANGE_REPAYMENTTOTAL.equals(event.getActionName())) {
      repaymentTotalField.setValue(entity.caculateTotal());
      entity.setRepaymentTotal(repaymentTotalField.getValueAsBigDecimal());
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  private class Handler_textField implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == counterpartUCNBox) {
        onChangeCounterpart();
      } else if (event.getSource() == employeeBox) {
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

  private void onChangeAccountUnit() {
    if (ObjectUtil.isEquals(entity.getAccountUnit(), accountUnitUCNBox.getValue()))
      return;

    if (entity.hasValues() == false) {
      entity.setAccountUnit(accountUnitUCNBox.getValue());
      doRefreshContract();
      if (accountUnitUCNBox.validate() == false)
        return;
      refreshBanks();
      doFireActionEvent();
      return;
    }

    String msg = DepositRepaymentMessage.M.changeAccountUnit();

    RMsgBox.showConfirm(msg, new ConfirmCallback() {

      @Override
      public void onClosed(boolean confirmed) {
        if (confirmed == false) {
          accountUnitUCNBox.setValue(entity.getAccountUnit());
          return;
        }

        entity.setAccountUnit(accountUnitUCNBox.getValue());
        doRefreshContract();
        entity.getLines().clear();
        clearDepositInfo();
        doFireActionEvent();
        RActionEvent.fire(RecDepositRepaymentGeneralCreateGadget.this,
            DepositRepaymentActionName.REFRESH);
      }
    });
    refreshBanks();
  }

  private void refreshBanks() {
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
  }

  private void onChangeCounterpart() {
    if (ObjectUtil.isEquals(entity.getCounterpart(), counterpartUCNBox.getValue()))
      return;

    if (entity.hasValues() == false) {
      entity.setCounterpart(counterpartUCNBox.getRawValue());
      if (ep.getCounterpartTypeMap().size() > 1) {
        String counterpartType = counterpartUCNBox.getRawValue() == null ? null : counterpartUCNBox
            .getRawValue().getCounterpartType();
        countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap()
            .get(counterpartType));
      }
      doRefreshContract();
      doFireActionEvent();
      return;
    }
    String msg = DepositRepaymentMessage.M.changeCounterpart();
    RMsgBox.showConfirm(msg, new ConfirmCallback() {

      @Override
      public void onClosed(boolean confirmed) {
        if (confirmed == false) {
          counterpartUCNBox.setValue(entity.getCounterpart());
          if (ep.getCounterpartTypeMap().size() > 1)
            countpartTypeField.setValue(ep.getCounterpartTypeMap().get(
                entity.getCounterpart().getCounterpartType()));
          return;
        }
        entity.setCounterpart(counterpartUCNBox.getRawValue());
        if (ep.getCounterpartTypeMap().size() > 1) {
          String counterpartType = counterpartUCNBox.getRawValue() == null ? null
              : counterpartUCNBox.getRawValue().getCounterpartType();
          countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap()
              .get(counterpartType));
        }
        doRefreshContract();
        entity.getLines().clear();
        clearDepositInfo();
        repaymentTotalField.clearValue();
        doFireActionEvent();
        RActionEvent.fire(RecDepositRepaymentGeneralCreateGadget.this,
            DepositRepaymentActionName.REFRESH);
      }
    });
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
        clearDepositInfo();
        repaymentTotalField.clearValue();
        doFireActionEvent();
        RActionEvent.fire(RecDepositRepaymentGeneralCreateGadget.this,
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
    if (contract == null || contract.getUuid() == null) {
      entity.setContract(null);
      contractNameField.clearValue();
    } else {
      entity
          .setContract(new BUCN(contract.getUuid(), contract.getBillNumber(), contract.getTitle()));
      contractNameField.setValue(contract.getTitle());

      doRefreshAccountUnit(contract);
      doRefreshCounterpart(contract);
    }
  }

  private void doRefreshAccountUnit(BContract contract) {
    if (contract == null || StringUtil.isNullOrBlank(contract.getUuid()))
      return;

    entity.setAccountUnit(contract.getAccountUnit());
    accountUnitUCNBox.setValue(entity.getAccountUnit());
  }

  private void doRefreshCounterpart(BContract contract) {
    entity.setCounterpart(contract.getCounterpart());
    counterpartUCNBox.setValue(entity.getCounterpart());
    if (ep.getCounterpartTypeMap().size() > 1) {
      countpartTypeField.setValue(entity.getCounterpart() == null ? null : ep
          .getCounterpartTypeMap().get(entity.getCounterpart().getCounterpartType()));
    }
  }

  private void doRefreshContract() {
    if (entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null
        || entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null) {
      return;
    }

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(DepositRepaymentMessage.M.loading());
    RecDepositRepaymentService.Locator.getService().getUniqueAdvance(
        entity.getAccountUnit().getUuid(), entity.getCounterpart().getUuid(),
        entity.getCounterpart().getCounterpartType(),
        // 对方单位类型
        new RBAsyncCallback2<BContract>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.actionFailed(DepositRepaymentMessage.M.load(),
                PDepositRepaymentDef.constants.contract());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BContract result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity.setContract(new BUCN(result.getUuid(), result.getBillNumber(), result
                  .getTitle()));
              contractBox.setValue(result.getBillNumber());
              contractBox.setRawValue(result);
              contractNameField.setValue(result.getName());
            } else {
              entity.setContract(new BUCN());
              contractBox.clearValue();
              contractBox.clearRawValue();
              contractBox.clearValidResults();
              contractNameField.clearValue();
            }
          }
        });
  }

  private void doFireActionEvent() {
    RActionEvent.fire(RecDepositRepaymentGeneralCreateGadget.this,
        DepositRepaymentActionName.CHANGE_ACCOUNTUNIT, entity.getAccountUnit() == null ? null
            : entity.getAccountUnit().getUuid());
    RActionEvent.fire(RecDepositRepaymentGeneralCreateGadget.this,
        DepositRepaymentActionName.CHANGE_COUNTERPART, entity.getCounterpart() == null ? null
            : entity.getCounterpart().getUuid());
    RActionEvent.fire(RecDepositRepaymentGeneralCreateGadget.this,
        DepositRepaymentActionName.CHANGE_CONTRACT, entity.getContract() == null ? null : entity
            .getContract().getUuid());
    RActionEvent.fire(RecDepositRepaymentGeneralCreateGadget.this,
        DepositRepaymentActionName.CHANGE_DEPOSIT, entity.getDeposit() == null ? null : entity
            .getDeposit().getUuid(), ep.isEnableByDeposit());
  }

}
