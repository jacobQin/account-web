/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositMoveInInfoEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.client.ui.gadget;

import java.math.BigDecimal;
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
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CounterpartBrowserDialog;
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
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.util.client.message.MessageLevel;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
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
public class PayDepositMoveInInfoEditGadget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {
  private EPPayDepositMove ep = EPPayDepositMove.getInstance();

  public PayDepositMoveInInfoEditGadget() {
    drawSelf();
  }

  private RForm inInfoForm;
  private BDepositMove entity;
  private RViewStringField accountUnitField;
  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;
  private AdvanceContractBrowserBox contractBox;
  private RViewStringField contractNameField;
  private AdvanceSubjectUCNBox subjectUCNBox;
  private RViewNumberField inBalanceField;

  private Handler_textField handler = new Handler_textField();

  private void drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(5);

    inInfoForm = new RForm(1);
    inInfoForm.setLabelWidth(0.4f);
    inInfoForm.setWidth("100%");
    root.add(inInfoForm);

    accountUnitField = new RViewStringField();
    accountUnitField.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    inInfoForm.addField(accountUnitField);

    counterpartUCNBox = new CounterpartUCNBox(true, false, false, ep.getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    counterpartUCNBox.setRequired(true);
    counterpartUCNBox.addChangeHandler(handler);
    counterpartUCNBox.addBeforeLoadDataHandler(new BeforeLoadDataHandler() {
      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent event) {
        if (entity.getOutCounterpart() == null || entity.getOutCounterpart().getCode() == null) {
          event.cancel();
          counterpartUCNBox.clearValue();
          counterpartUCNBox.clearValidResults();
          RMsgBox.show(DepositMoveMessage.M.selectCounterpart(DepositMoveMessage.M.out()));
        }
      }
    });
    counterpartUCNBox.setValidator(new RValidator() {
      @Override
      public Message validate(Widget sender, String value) {
        if (entity.getInCounterpart() != null
            && counterpartUCNBox.getRawValue() != null
            && entity.getOutCounterpart().getCounterpartType()
                .equals(counterpartUCNBox.getRawValue().getCounterpartType()) == false) {
          return new Message(counterpartUCNBox.getCaption() + " : "
              + DepositMoveMessage.M.counterpartConflict(), MessageLevel.ERROR);
        }
        return null;
      }
    });

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
    inInfoForm.addField(countpartField);

    contractBox = new AdvanceContractBrowserBox(PDepositMoveDef.constants.inContract_code(), false,
        true, false, new AdvanceContractBrowserBox.Callback() {
          @Override
          public void execute(BAdvanceContract result) {
            onChangeContract();
          }
        }, ep.getCaptionMap());
    contractBox.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        if (entity.getAccountUnit() != null)
          contractBox.setAccountUnitUuid(entity.getAccountUnit().getUuid());
        if (entity.getInCounterpart() != null) {
          contractBox.setCounterpartLike(entity.getInCounterpart().getCode());
          contractBox.setCounterpartEqual(entity.getInCounterpart().getCounterpartType());
        }
      }
    });
    contractBox.setDirection(DirectionType.payment.getDirectionValue());
    contractBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    contractBox.setQueryAdvance(false);
    contractBox.addBeforeLoadDataHandler(new BeforeLoadDataHandler<String>() {
      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<String> event) {
        if (entity.getOutCounterpart() == null || entity.getOutCounterpart().getCode() == null) {
          event.cancel();
          contractNameField.clearValue();
          RMsgBox.show(DepositMoveMessage.M.selectCounterpart(DepositMoveMessage.M.out()));
        }
      }
    });
    inInfoForm.addField(contractBox);

    contractNameField = new RViewStringField(PDepositMoveDef.constants.inContract_name());
    inInfoForm.addField(contractNameField);

    subjectUCNBox = new AdvanceSubjectUCNBox(PDepositMoveDef.constants.inSubject());
    subjectUCNBox.setDirection(DirectionType.payment.getDirectionValue());
    subjectUCNBox.setQueryAdvance(false);
    subjectUCNBox.setRequired(true);
    subjectUCNBox.addChangeHandler(handler);
    subjectUCNBox.addBeforeLoadDataHandler(new BeforeLoadDataHandler() {
      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent event) {
        if (entity.getOutCounterpart() == null || entity.getOutCounterpart().getCode() == null) {
          event.cancel();
          subjectUCNBox.clearValue();
          subjectUCNBox.clearValidResults();
          RMsgBox.show(DepositMoveMessage.M.selectCounterpart(DepositMoveMessage.M.out()));
        }
      }
    });
    inInfoForm.addField(subjectUCNBox);

    inBalanceField = new RViewNumberField(DepositMoveMessage.M.balance());
    inBalanceField.setWidth("50%");
    inBalanceField.setFormat(GWTFormat.fmt_money);
    inBalanceField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    inInfoForm.addField(inBalanceField);

    setCaption(DepositMoveMessage.M.in() + DepositMoveMessage.M.info());
    setWidth("100%");
    setContent(root);
    setStyleName(RCaptionBox.STYLENAME_CONCISE);
  }

  public void clearQueryConditions() {
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

    accountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .getNameCode());

    if (entity.getInCounterpart() != null) {
      counterpartUCNBox.setValue(entity.getInCounterpart());
      counterpartUCNBox.setCounterpartType(entity.getOutCounterpart() == null ? null : entity
          .getOutCounterpart().getCounterpartType());
      if (ep.getCounterpartTypeMap().size() > 1) {
        String counterpartType = entity.getInCounterpart() == null ? null : entity
            .getInCounterpart().getCounterpartType();
        countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap()
            .get(counterpartType));
      }
    } else {
      counterpartUCNBox.clearValue();
      counterpartUCNBox.clearValidResults();
      if (ep.getCounterpartTypeMap().size() > 1)
        countpartTypeField.clearValue();
    }

    contractBox.setValue(entity.getInContract() == null ? null : entity.getInContract().getCode());
    contractBox.setCounterpartEqual(entity.getOutCounterpart() == null ? null : entity
        .getOutCounterpart().getCounterpartType());
    contractNameField.setValue(entity.getInContract() == null ? null : entity.getInContract()
        .getName());
    contractBox.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .getUuid());

    if (entity.getInSubject() != null)
      subjectUCNBox.setValue(entity.getInSubject());
    else {
      subjectUCNBox.clearValue();
      subjectUCNBox.clearValidResults();
    }
    if (entity.getInBalance() == null)
      entity.setInBalance(BigDecimal.ZERO);
    inBalanceField.setValue(entity.getInBalance().doubleValue());
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == counterpartUCNBox) {
        onChangeCounterpart();
      } else if (event.getSource() == subjectUCNBox) {
        onChangeSubjecet();
      }
    }
  }

  private void onChangeCounterpart() {
    if (counterpartUCNBox.isValid() == false)
      return;
    if (ObjectUtil.isEquals(entity.getInCounterpart(), counterpartUCNBox.getValue()))
      return;

    entity.setInCounterpart(counterpartUCNBox.getRawValue());
    if (ep.getCounterpartTypeMap().size() > 1) {
      String counterpartType = counterpartUCNBox.getRawValue() == null ? null : counterpartUCNBox
          .getRawValue().getCounterpartType();
      countpartTypeField.setValue(counterpartType == null ? null : ep.getCounterpartTypeMap().get(
          counterpartType));
    }
    doRefreshContract();
    doRefrechBalance();
  }

  private void onChangeContract() {
    BAdvanceContract contract = contractBox.getRawValue();
    if (contract == null || contract.getContract() == null) {
      entity.setInContract(null);
      contractNameField.clearValue();
      return;
    } else {
      entity.setInContract(contract.getContract());
      contractNameField.setValue(contract.getTitle());
    }

    refreshCounterpart(contract);
    doRefrechBalance();
  }

  private void onChangeSubjecet() {
    if (ObjectUtil.isEquals(entity.getInSubject(), subjectUCNBox.getValue()))
      return;

    entity.setInSubject(subjectUCNBox.getValue());
    doRefrechBalance();
  }

  private void doRefreshContract() {
    if (entity.getInCounterpart() == null || entity.getInCounterpart().getUuid() == null
        || entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null) {
      entity.setInContract(null);
      contractBox.clearValue();
      contractBox.clearRawValue();
      contractNameField.clearValue();
      return;
    }

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(DepositMoveMessage.M.loading());
    PayDepositMoveService.Locator.getService().getUniqueContract(entity.getAccountUnit().getUuid(),
        entity.getInCounterpart().getUuid(), entity.getInCounterpart().getCounterpartType(),// 对方单位类型
        false, new RBAsyncCallback2<BAdvanceContract>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.load(),
                PDepositMoveDef.constants.inContract());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BAdvanceContract result) {
            RLoadingDialog.hide();
            if (result != null) {
              entity.setInContract(result.getContract());
              contractBox.setRawValue(result);
              contractBox.setValue(result.getContract().getCode());
              contractNameField.setValue(result.getContract().getName());
            } else {
              entity.setInContract(null);
              contractBox.setRawValue(null);
              contractBox.clearValue();
              contractNameField.clearValue();
            }
          }
        });
  }

  private void refreshCounterpart(BAdvanceContract contract) {
    if (contract == null || contract.getContract() == null)
      return;

    entity.setInCounterpart(contract.getCounterpart());
    counterpartUCNBox.setValue(entity.getInCounterpart());
    if (ep.getCounterpartTypeMap().size() > 1) {
      countpartTypeField.setValue(entity.getInCounterpart() == null ? null : ep
          .getCounterpartTypeMap().get(entity.getInCounterpart().getCounterpartType()));
    }
  }

  private void doRefrechBalance() {
    if (entity.getInCounterpart() == null || entity.getInCounterpart().getUuid() == null
        || entity.getInSubject() == null || entity.getInSubject().getUuid() == null
        || entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null)
      return;

    String contractUuid = null;
    if (entity.getInContract() != null)
      contractUuid = entity.getInContract().getUuid();

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(DepositMoveMessage.M.loading());

    PayDepositMoveService.Locator.getService().getBalance(entity.getAccountUnit().getUuid(),
        entity.getInCounterpart().getUuid(), entity.getInSubject().getUuid(), contractUuid,
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
            inBalanceField.setValue(result.doubleValue());
          }
        });
  }

  @Override
  public void clearValidResults() {
    inInfoForm.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return inInfoForm.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    return inInfoForm.getInvalidMessages();
  }

  @Override
  public boolean validate() {
    return inInfoForm.validate();
  }

  @Override
  public void clear() {
    entity.setInContract(null);
    entity.setInCounterpart(null);
    entity.setInSubject(null);
    entity.setInBalance(BigDecimal.ZERO);
    contractBox.clearValue();
    contractBox.clearRawValue();
    contractNameField.clearValue();
    counterpartUCNBox.clearValue();
    countpartTypeField.clearValue();
    subjectUCNBox.clearValue();
    inBalanceField.setValue(entity.getInBalance().doubleValue());
    clearValidResults();
  }

  @Override
  public void onAction(RActionEvent event) {
    if (DepositMoveActionName.GET_BALANCE.equals(event.getActionName())) {
      accountUnitField.setValue(entity.getAccountUnit() == null ? "" : entity.getAccountUnit()
          .toFriendlyStr());
      contractBox.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity
          .getAccountUnit().getUuid());
      doRefreshContract();
      doRefrechBalance();
    } else if (DepositMoveActionName.CLEAR_MOVEININFO.equals(event.getActionName())) {
      clear();
    } else if (DepositMoveActionName.CHANGE_COUNTERPART.equals(event.getActionName())) {
      counterpartUCNBox.setCounterpartType(entity.getOutCounterpart() == null ? null : entity
          .getOutCounterpart().getCounterpartType());
      contractBox.setCounterpartEqual(entity.getOutCounterpart() == null ? null : entity
          .getOutCounterpart().getCounterpartType());
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }
}
