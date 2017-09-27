/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositMoveOutInfoEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.client.ui.gadget;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CounterpartBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.depositmove.commons.client.actionname.DepositMoveActionName;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.dialog.AdvanceSubjectBrowserDialog;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.form.AdvanceContractBrowserBox;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.form.AdvanceSubjectUCNBox;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.account.gwt.depositmove.intf.client.dd.PDepositMoveDef;
import com.hd123.m3.account.gwt.depositmove.payment.client.EPPayDepositMove;
import com.hd123.m3.account.gwt.depositmove.payment.client.rpc.PayDepositMoveService;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox.ConfirmCallback;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
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
public class PayDepositMoveOutInfoEditGadget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {
  private EPPayDepositMove ep = EPPayDepositMove.getInstance();

  public PayDepositMoveOutInfoEditGadget() {
    drawSelf();
  }

  private BDepositMove entity;

  private RForm outInfoForm;
  private AccountUnitUCNBox accountUnitUCNBox;
  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;
  private AdvanceContractBrowserBox contractBox;
  private RViewStringField contractNameField;
  private AdvanceSubjectUCNBox subjectUCNBox;
  private HTML captionHtml;
  private RNumberBox amountField;
  private RViewNumberField outBalanceField;
  private RDateBox accountDateField;

  private Handler_textField handler = new Handler_textField();
  private Handler_textValidator validator = new Handler_textValidator();

  private void drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(5);

    outInfoForm = new RForm(1);
    outInfoForm.setLabelWidth(0.4f);
    outInfoForm.setWidth("100%");
    root.add(outInfoForm);

    accountUnitUCNBox = new AccountUnitUCNBox();
    accountUnitUCNBox.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    accountUnitUCNBox.setRequired(true);
    accountUnitUCNBox.addChangeHandler(handler);
    outInfoForm.addField(accountUnitUCNBox);

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
    outInfoForm.addField(countpartField);

    contractBox = new AdvanceContractBrowserBox(PDepositMoveDef.constants.outContract_code(), true,
        false, true, new AdvanceContractBrowserBox.Callback() {
          @Override
          public void execute(BAdvanceContract result) {
            onChangeContract();
          }
        }, ep.getCaptionMap());
    contractBox.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        if (entity.getAccountUnit() != null)
          contractBox.setAccountUnitLike(entity.getAccountUnit().getCode());
        if (entity.getOutCounterpart() != null) {
          contractBox.setCounterpartLike(entity.getOutCounterpart().getCode());
          contractBox.setCounterpartEqual(entity.getOutCounterpart().getCounterpartType());
        }
      }
    });
    contractBox.setDirection(DirectionType.payment.getDirectionValue());
    contractBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    contractBox.setQueryAdvance(true);
    outInfoForm.addField(contractBox);

    contractNameField = new RViewStringField(PDepositMoveDef.constants.outContract_name());
    outInfoForm.addField(contractNameField);

    subjectUCNBox = new AdvanceSubjectUCNBox(PDepositMoveDef.constants.outSubject());
    subjectUCNBox.setDirection(DirectionType.payment.getDirectionValue());
    subjectUCNBox.setQueryAdvance(true);
    subjectUCNBox.setRequired(true);
    subjectUCNBox.addChangeHandler(handler);
    outInfoForm.addField(subjectUCNBox);

    captionHtml = new HTML();
    captionHtml.setHTML(PDepositMoveDef.constants.amount() + "/" + DepositMoveMessage.M.balance()
        + "<font color='red'>*</font>");
    captionHtml.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    amountField = new RNumberBox(PDepositMoveDef.constants.amount());
    amountField.setValidator(validator);
    amountField.setScale(2);
    amountField.setTextAlignment(RTextBox.ALIGN_RIGHT);
    amountField.setFormat(GWTFormat.fmt_money);
    amountField.setRequired(true);
    amountField.setMinValue(BigDecimal.ZERO, false);
    amountField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2);
    amountField.setSelectAllOnFocus(true);
    amountField.setValueType(BigDecimal.class);
    amountField.addChangeHandler(handler);

    outBalanceField = new RViewNumberField();
    outBalanceField.setFormat(GWTFormat.fmt_money);
    outBalanceField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    outInfoForm.addField(new RCombinedField() {
      {
        addField(captionHtml, 0.4f);
        addField(amountField, 0.3f);
        addField(outBalanceField, 0.3f);
      }
    }, false);

    accountDateField = new RDateBox(PDepositMoveDef.accountDate);
    accountDateField.addChangeHandler(handler);
    outInfoForm.addField(accountDateField);

    setCaption(DepositMoveMessage.M.out() + DepositMoveMessage.M.info());
    setWidth("100%");
    setContent(root);
    setStyleName(RCaptionBox.STYLENAME_CONCISE);
  }

  public void clearQueryConditions() {
    if (accountUnitUCNBox != null)
      ((AccountUnitBrowserDialog) accountUnitUCNBox.getBrowser()).clearConditions();

    if (counterpartUCNBox != null)
      ((CounterpartBrowserDialog) counterpartUCNBox.getBrowser()).clearConditions();

    if (contractBox != null)
      contractBox.clearConditions();

    if (subjectUCNBox != null)
      ((AdvanceSubjectBrowserDialog) subjectUCNBox.getBrowser()).clearConditions();
  }

  public void refresh(BDepositMove entity) {
    assert entity != null;
    this.entity = entity;

    accountUnitUCNBox.setValue(entity.getAccountUnit());
    if (entity.getOutCounterpart() != null) {
      counterpartUCNBox.setValue(entity.getOutCounterpart());
      if (ep.getCounterpartTypeMap().size() > 1) {
        String counterpartType = entity.getOutCounterpart() == null ? null : entity
            .getOutCounterpart().getCounterpartType();
        countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap()
            .get(counterpartType));
      }
    } else {
      counterpartUCNBox.clearValue();
      counterpartUCNBox.clearValidResults();
      if (ep.getCounterpartTypeMap().size() > 1)
        countpartTypeField.clearValue();
    }
    contractBox
        .setValue(entity.getOutContract() == null ? null : entity.getOutContract().getCode());
    contractBox.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .getUuid());
    contractNameField.setValue(entity.getOutContract() == null ? null : entity.getOutContract()
        .getName());
    subjectUCNBox.setValue(entity.getOutSubject());
    subjectUCNBox.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity
        .getAccountUnit().getUuid());
    subjectUCNBox.setCounterpartUuid(entity.getOutCounterpart() == null ? null : entity
        .getOutCounterpart().getUuid());
    subjectUCNBox.setContractUuid(entity.getOutContract() == null ? null : entity.getOutContract()
        .getUuid());

    if (entity.getAmount() == null)
      entity.setAmount(BigDecimal.ZERO);
    amountField.setValue(entity.getAmount().doubleValue());

    if (entity.getOutBalance() == null)
      entity.setOutBalance(BigDecimal.ZERO);
    outBalanceField.setValue(entity.getOutBalance().doubleValue());

    if (entity.getAccountDate() == null)
      entity.setAccountDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    accountDateField.setValue(entity.getAccountDate());
  }

  public void setFocusOnFirstField() {
    accountUnitUCNBox.setFocus(true);
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == accountUnitUCNBox) {
        onChangeAccountUnit();
      } else if (event.getSource() == counterpartUCNBox) {
        onChangeCounterpart();
      } else if (event.getSource() == subjectUCNBox) {
        onChangeSubject();
      } else if (event.getSource() == amountField) {
        entity.setAmount(amountField.getValueAsBigDecimal());
        amountField.setValue(entity.getAmount());
      } else if (event.getSource() == accountDateField) {
        entity.setAccountDate(accountDateField.getValue());
      }
    }
  }

  private void onChangeAccountUnit() {
    if (ObjectUtil.isEquals(entity.getAccountUnit(), accountUnitUCNBox.getValue()))
      return;

    if (entity.getOutSubject() != null && entity.getOutSubject().getCode() != null) {
      String msg = DepositMoveMessage.M.changeAccountUnit();
      RMsgBox.showConfirm(msg, new ConfirmCallback() {

        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false) {
            accountUnitUCNBox.setValue(entity.getAccountUnit());
            return;
          }
          entity.setAccountUnit(accountUnitUCNBox.getValue());
          doRefreshContract();
          doRefreshSubject();
          doRefrechBalance();
          RActionEvent
              .fire(PayDepositMoveOutInfoEditGadget.this, DepositMoveActionName.GET_BALANCE);
        }
      });
    } else {
      entity.setAccountUnit(accountUnitUCNBox.getValue());
      doRefreshContract();
      doRefreshSubject();
      doRefrechBalance();
      RActionEvent.fire(PayDepositMoveOutInfoEditGadget.this, DepositMoveActionName.GET_BALANCE);
    }
  }

  private void onChangeCounterpart() {
    if (ObjectUtil.isEquals(entity.getOutCounterpart(), counterpartUCNBox.getValue()))
      return;

    boolean hasOutSubject = entity.getOutSubject() != null
        && entity.getOutSubject().getCode() != null;
    boolean hasInInfo = (entity.getInCounterpart() != null && entity.getInCounterpart().getCode() != null)
        || (entity.getInContract() != null && entity.getInContract().getCode() != null);

    String msg = "";

    if (hasInInfo) {
      msg = DepositMoveMessage.M.changeContract(hasOutSubject ? PDepositMoveDef.constants
          .outSubject()
          + DepositMoveMessage.M.and()
          + DepositMoveMessage.M.in()
          + DepositMoveMessage.M.info() : DepositMoveMessage.M.in() + DepositMoveMessage.M.info());
    } else {
      msg = hasOutSubject ? DepositMoveMessage.M.changeContract(PDepositMoveDef.constants
          .outSubject() + DepositMoveMessage.M.info()) : "";
    }

    if (hasOutSubject || hasInInfo) {
      RMsgBox.showConfirm(msg, new ConfirmCallback() {
        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false) {
            counterpartUCNBox.setValue(entity.getOutCounterpart());
            if (ep.getCounterpartTypeMap().size() > 1) {
              String counterpartType = entity.getOutCounterpart() == null ? null : entity
                  .getOutCounterpart().getCounterpartType();
              countpartTypeField.setValue(counterpartType == null ? null : ep
                  .getCounterpartTypeMap().get(counterpartType));
            }
            return;
          }
          entity.setOutCounterpart(counterpartUCNBox.getRawValue());
          if (ep.getCounterpartTypeMap().size() > 1) {
            String counterpartType = counterpartUCNBox.getRawValue() == null ? null
                : counterpartUCNBox.getRawValue().getCounterpartType();
            countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap()
                .get(counterpartType));
          }
          doRefreshContract();
          doRefreshSubject();
          doRefrechBalance();
          RActionEvent.fire(PayDepositMoveOutInfoEditGadget.this,
              DepositMoveActionName.CHANGE_COUNTERPART);
          RActionEvent.fire(PayDepositMoveOutInfoEditGadget.this,
              DepositMoveActionName.CLEAR_MOVEININFO);
        }
      });
    } else {
      entity.setOutCounterpart(counterpartUCNBox.getRawValue());
      if (ep.getCounterpartTypeMap().size() > 1) {
        String counterpartType = counterpartUCNBox.getRawValue() == null ? null : counterpartUCNBox
            .getRawValue().getCounterpartType();
        countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap()
            .get(counterpartType));
      }
      doRefreshContract();
      doRefreshSubject();
      doRefrechBalance();
      RActionEvent.fire(PayDepositMoveOutInfoEditGadget.this,
          DepositMoveActionName.CHANGE_COUNTERPART);
    }
  }

  private void onChangeContract() {
    if (entity.getOutContract() != null
        && contractBox.getRawValue() != null
        && contractBox.getRawValue().getContract() != null
        && entity.getOutContract().getCode()
            .equals(contractBox.getRawValue().getContract().getCode())
        && entity.getOutContract().getName()
            .equals(contractBox.getRawValue().getContract().getName())) {
      return;
    }

    boolean hasOutSubject = entity.getOutSubject() != null
        && entity.getOutSubject().getCode() != null;
    boolean hasInInfo = (entity.getInCounterpart() != null && entity.getInCounterpart().getCode() != null)
        || (entity.getInContract() != null && entity.getInContract().getCode() != null);

    String msg = "";

    if (contractBox.getRawValue() == null || contractBox.getRawValue().getContract() == null) {
      subjectUCNBox.setContractUuid(null);
    } else {
      subjectUCNBox.setContractUuid(contractBox.getRawValue().getContract().getUuid());
    }

    if (hasInInfo) {
      msg = DepositMoveMessage.M.changeContract(hasOutSubject ? PDepositMoveDef.constants
          .outSubject()
          + DepositMoveMessage.M.and()
          + DepositMoveMessage.M.in()
          + DepositMoveMessage.M.info() : DepositMoveMessage.M.in() + DepositMoveMessage.M.info());
    } else {
      msg = hasOutSubject ? DepositMoveMessage.M.changeContract(PDepositMoveDef.constants
          .outSubject() + DepositMoveMessage.M.info()) : "";
    }

    if (hasInInfo || hasOutSubject) {
      RMsgBox.showConfirm(msg, new ConfirmCallback() {
        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false) {
            contractBox.setValue(entity.getOutContract() == null ? null : entity.getOutContract()
                .getCode());
            contractNameField.setValue(entity.getOutContract() == null ? null : entity
                .getOutContract().getName());
            return;
          }
          BAdvanceContract contract = contractBox.getRawValue();
          if (contract == null || contract.getContract() == null) {
            entity.setOutContract(null);
            contractNameField.clearValue();
            doRefreshSubject();
            RActionEvent.fire(PayDepositMoveOutInfoEditGadget.this,
                DepositMoveActionName.CLEAR_MOVEININFO);
            return;
          } else {
            entity.setOutContract(contract.getContract());
            contractBox.setValue(contract.getContract().getCode());
            contractNameField.setValue(contract.getContract().getName());
          }

          doRefreshAccountUnit(contract);
          doRefreshCounterpart(contract);
          doRefreshSubject();
          doRefrechBalance();
          RActionEvent.fire(PayDepositMoveOutInfoEditGadget.this,
              DepositMoveActionName.CLEAR_MOVEININFO);
        }
      });
    } else {
      BAdvanceContract contract = contractBox.getRawValue();
      if (contract == null || contract.getContract() == null) {
        entity.setOutContract(null);
        contractNameField.clearValue();
        return;
      } else {
        entity.setOutContract(contract.getContract());
        contractBox.setValue(contract.getContract().getCode());
        contractNameField.setValue(contract.getContract().getName());
      }

      doRefreshAccountUnit(contract);
      doRefreshCounterpart(contract);
      doRefreshSubject();
    }
  }

  private void onChangeSubject() {
    if (ObjectUtil.isEquals(entity.getOutSubject(), subjectUCNBox.getValue()))
      return;
    entity.setOutSubject(subjectUCNBox.getValue());
    doRefrechBalance();
  }

  private void doRefreshAccountUnit(BAdvanceContract contract) {
    if (contract == null || contract.getAccountUnit() == null)
      return;

    entity.setAccountUnit(contract.getAccountUnit());
    accountUnitUCNBox.setValue(entity.getAccountUnit());
    RActionEvent.fire(PayDepositMoveOutInfoEditGadget.this, DepositMoveActionName.GET_BALANCE);
  }

  private void doRefreshCounterpart(BAdvanceContract contract) {
    if (contract == null || contract.getCounterpart() == null)
      return;

    entity.setOutCounterpart(contract.getCounterpart());
    counterpartUCNBox.setValue(entity.getOutCounterpart());
    if (ep.getCounterpartTypeMap().size() > 1) {
      String counterpartType = entity.getOutCounterpart().getCounterpartType();
      countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap().get(
          counterpartType));
    }
    RActionEvent.fire(PayDepositMoveOutInfoEditGadget.this,
        DepositMoveActionName.CHANGE_COUNTERPART);
  }

  private void doRefreshContract() {
    if (entity.getOutCounterpart() == null || entity.getOutCounterpart().getUuid() == null
        || entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null) {
      entity.setOutContract(null);
      contractBox.clearValue();
      contractBox.clearRawValue();
      contractNameField.clearValue();
      return;
    }
    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(DepositMoveMessage.M.loading());
    PayDepositMoveService.Locator.getService().getUniqueContract(entity.getAccountUnit().getUuid(),
        entity.getOutCounterpart().getUuid(), entity.getOutCounterpart().getCounterpartType(),// TODO
                                                                                              // 对方单位类型
        true, new RBAsyncCallback2<BAdvanceContract>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.load(),
                PDepositMoveDef.constants.outContract());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BAdvanceContract result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity.setOutContract(result.getContract());
              contractBox.setRawValue(result);
              contractBox.setValue(result.getContract().getCode());
              contractNameField.setValue(result.getContract().getName());
            } else {
              entity.setOutContract(null);
              contractBox.setRawValue(null);
              contractBox.clearValue();
              contractBox.clearValidResults();
              contractNameField.clearValue();
            }
          }
        });
  }

  private void doRefreshSubject() {
    entity.setOutSubject(null);
    subjectUCNBox.setValue(entity.getOutSubject());
    subjectUCNBox.clearValidResults();
    subjectUCNBox.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity
        .getAccountUnit().getUuid());
    subjectUCNBox.setCounterpartUuid(entity.getOutCounterpart() == null ? null : entity
        .getOutCounterpart().getUuid());
    subjectUCNBox.setContractUuid(entity.getOutContract() == null ? null : entity.getOutContract()
        .getUuid());
  }

  private void doRefrechBalance() {
    if (entity.getOutCounterpart() == null || entity.getOutCounterpart().getUuid() == null
        || entity.getOutSubject() == null || entity.getOutSubject().getUuid() == null
        || entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null) {
      entity.setOutBalance(BigDecimal.ZERO);
      outBalanceField.setValue(entity.getOutBalance().doubleValue());
      return;
    }

    String contractUuid = null;
    if (entity.getOutContract() != null)
      contractUuid = entity.getOutContract().getUuid();

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(DepositMoveMessage.M.loading());

    PayDepositMoveService.Locator.getService().getBalance(entity.getAccountUnit().getUuid(),
        entity.getOutCounterpart().getUuid(), entity.getOutSubject().getUuid(), contractUuid,
        new RBAsyncCallback2<BigDecimal>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.load(),
                DepositMoveMessage.M.balance());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BigDecimal result) {
            RLoadingDialog.hide();
            outBalanceField.setValue(result.doubleValue());
            if (entity.getAmount() != null) {
              if (entity.getAmount().compareTo(result) > 0) {
                amountField.addErrorMessage(PDepositMoveDef.constants.amount()
                    + DepositMoveMessage.M.notExcees() + DepositMoveMessage.M.balance() + "。");
              } else {
                amountField.clearValidResults();
              }
            }
          }
        });
  }

  @Override
  public void clearValidResults() {
    outInfoForm.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return accountUnitUCNBox.isValid() && counterpartUCNBox.isValid() && contractBox.isValid()
        && subjectUCNBox.isValid() && amountField.isValid() && accountDateField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(accountUnitUCNBox.getMessages());
    messages.addAll(counterpartUCNBox.getMessages());
    messages.addAll(contractBox.getMessages());
    messages.addAll(subjectUCNBox.getMessages());
    messages.addAll(amountField.getMessages());
    messages.addAll(accountDateField.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    clearValidResults();
    boolean isValid = counterpartUCNBox.validate();
    isValid &= accountUnitUCNBox.validate();
    isValid &= contractBox.validate();
    isValid &= subjectUCNBox.validate();
    isValid &= amountField.validate();
    isValid &= accountDateField.validate();

    return isValid;
  }

  @Override
  public void onAction(RActionEvent event) {

  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  private class Handler_textValidator implements RValidator {

    @Override
    public Message validate(Widget sender, String value) {
      if (sender instanceof RNumberBox) {
        if (amountField.getValueAsBigDecimal() == null)
          return null;
        if (outBalanceField == null || outBalanceField.getValueAsBigDecimal() == null)
          return null;
        if (amountField.getValueAsBigDecimal().compareTo(outBalanceField.getValueAsBigDecimal()) > 0) {
          return Message.error(
              PDepositMoveDef.constants.amount() + DepositMoveMessage.M.notExcees()
                  + DepositMoveMessage.M.balance() + "。", sender);
        }
      } else if (sender instanceof CounterpartUCNBox) {
        if (entity == null || entity.getOutCounterpart() == null
            || entity.getOutCounterpart().getUuid() == null || entity.getOutSubject() == null
            || entity.getOutSubject().getUuid() == null || entity.getInCounterpart() == null
            || entity.getInCounterpart().getUuid() == null || entity.getInSubject() == null
            || entity.getInSubject().getUuid() == null)
          return null;
        if (((entity.getOutContract() == null || entity.getOutContract().getUuid() == null) && (entity
            .getInContract() == null || entity.getInContract().getUuid() == null))
            && entity.getOutCounterpart().getUuid().equals(entity.getInCounterpart().getUuid())
            && entity.getOutSubject().getUuid().equals(entity.getInSubject().getUuid())) {
          return Message.error(
              DepositMoveMessage.M.out() + DepositMoveMessage.M.info() + "和"
                  + DepositMoveMessage.M.in() + DepositMoveMessage.M.info()
                  + DepositMoveMessage.M.notSame() + "。", sender);
        }
        if (entity.getOutContract() != null && entity.getOutContract().getUuid() != null
            && entity.getInContract() != null && entity.getInContract().getUuid() != null
            && entity.getOutContract().getUuid().equals(entity.getInContract().getUuid())
            && entity.getOutCounterpart().getUuid().equals(entity.getInCounterpart().getUuid())
            && entity.getOutSubject().getUuid().equals(entity.getInSubject().getUuid())) {
          return Message.error(
              DepositMoveMessage.M.out() + DepositMoveMessage.M.info() + "和"
                  + DepositMoveMessage.M.in() + DepositMoveMessage.M.info()
                  + DepositMoveMessage.M.notSame() + "。", sender);
        }
      }
      return null;
    }
  }

}
