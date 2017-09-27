/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月4日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author zhr
 * 
 */
public class StatementGeneralCreateGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {

  public StatementGeneralCreateGadget() {
    super();
    setCaption(StatementMessages.M.generalInfo());
    setEditing(true);

    setContent(drawSelf());
  }

  public void setValue(BStatement entity) {
    this.entity = entity;
    refreshEntity();
  }

  public void refreshTotal() {
    receiptTotalField.setValue(entity.getReceiptTotal() == null ? null : entity.getReceiptTotal()
        .getTotal() == null ? null : entity.getReceiptTotal().getTotal().doubleValue());
    receiptTaxField.setValue(entity.getReceiptTotal() == null ? null : entity.getReceiptTotal()
        .getTax() == null ? null : entity.getReceiptTotal().getTax().doubleValue());
    payTotalField.setValue(entity.getPayTotal() == null ? null
        : entity.getPayTotal().getTotal() == null ? null : entity.getPayTotal().getTotal()
            .doubleValue());
    payTaxField.setValue(entity.getPayTotal() == null ? null
        : entity.getPayTotal().getTax() == null ? null : entity.getPayTotal().getTax()
            .doubleValue());

    // 仅仅展示，无业务
    totalField.setValue(entity.calculateTotal().getTotal());
    taxField.setValue(entity.calculateTotal().getTax());
  }

  public void focusOnFirstField() {
    accountUnitField.setFocus(true);
  }

  public void onHide() {
    accountUnitField.clearConditions();
    contractField.clearConditions();
    counterpartUCNBox.clearConditions();
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == StatementLinePatchEditGridGadget.ActionName.REFRESH) {
      entity.aggregate();
      entity.calculateTotal();
      refreshTotal();
    }
  }

  private BStatement entity;
  private EPStatement ep = EPStatement.getInstance();

  private AccountUnitUCNBox accountUnitField;
  private ContractBrowseBox contractField;
  private RViewStringField contractNameField;
  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeField;
  private SettleNoField settleNoField;

  private RDateBox receiptAccDateField;

  private PermGroupEditField permGroupField;

  private RViewNumberField receiptTotalField;
  private RViewNumberField receiptTaxField;
  private RViewNumberField payTotalField;
  private RViewNumberField payTaxField;
  private RViewNumberField totalField;
  private RViewNumberField taxField;

  private Change_Handler changeHandler = new Change_Handler();

  private Widget drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel();
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    root.add(mvp);

    mvp.add(0, drawBasicPanel());
    mvp.add(0, drawStatement());

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    mvp.add(1, drawTotalPanel());
    return root;
  }

  private Widget drawBasicPanel() {
    RForm basicInfoForm = new RForm(1);
    basicInfoForm.setWidth("100%");

    accountUnitField = new AccountUnitUCNBox();
    accountUnitField.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    accountUnitField.setRequired(true);
    accountUnitField.addValueChangeHandler(changeHandler);
    basicInfoForm.addField(accountUnitField);

    contractField = new ContractBrowseBox(PStatementDef.constants.contract_code(), true,
        new ContractBrowseBox.Callback() {

          @Override
          public void execute(final BContract result) {
            if (result == null) {
              entity.setContract(null);
              contractNameField.clearValue();
            } else {
              if (entity.getLines().isEmpty() == false && entity.getLines().get(0) != null
                  && entity.getLines().get(0).getAcc1().getSubject() != null
                  && entity.getLines().get(0).getAcc1().getSubject().getUuid() != null) {
                String msg = StatementMessages.M.selectContract();
                RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

                  @Override
                  public void onClosed(boolean confirmed) {
                    if (confirmed == false) {
                      BContract contract = createContract();
                      contractField.setRawValue(contract);
                      contractNameField.setValue(contract.getName());
                      return;
                    } else {
                      entity.setContract(new BUCN(result.getUuid(), result.getBillNumber(), result
                          .getTitle()));
                      contractNameField.setValue(result.getName());
                      entity.getLines().clear();
                      RActionEvent.fire(StatementGeneralCreateGadget.this, ActionName.CLEAR_LINE,
                          "");
                      entity.aggregate();
                      entity.calculateTotal();
                      refreshTotal();
                    }
                  }
                });
              } else {
                entity.setContract(new BUCN(result.getUuid(), result.getBillNumber(), result
                    .getTitle()));
                contractNameField.setValue(result.getName());
              }
            }
            onChangeContract();
          }
        }, ep.getCaptionMap());
    contractField.addValueChangeHandler(changeHandler);
    contractField.setCounterTypeMap(ep.getCounterpartTypeMap());
    contractField.setRequired(true);
    // 此步骤，是将项目于商户作为合同的条件。
    contractField.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {

      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        contractField.setCounterpartLike(entity.getCounterpart() == null ? null : entity
            .getCounterpart().getCode());
        contractField.setAccountUnitLike(entity.getAccountUnit() == null ? null : entity
            .getAccountUnit().getCode());
        contractField.setCounterpartEqual(entity.getCounterpart() == null ? null : entity
            .getCounterpart().getCounterpartType());
      }
    });
    basicInfoForm.addField(contractField);

    contractNameField = new RViewStringField(PStatementDef.constants.contract_name());
    basicInfoForm.addField(contractNameField);

    counterpartUCNBox = new CounterpartUCNBox(ep.getCaptionMap());
    counterpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    counterpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    counterpartUCNBox.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        onChangeCounterpart();
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
    basicInfoForm.addField(countpartField);

    settleNoField = new SettleNoField(PStatementDef.settleNo);
    settleNoField.addValueChangeHandler(changeHandler);
    settleNoField.setMaxLength(6);
    settleNoField.setRequired(true);
    settleNoField.refreshOption(3);
    basicInfoForm.addField(settleNoField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.basicInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(basicInfoForm);
    return box;
  }

  private Widget drawStatement() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    receiptAccDateField = new RDateBox(PStatementDef.receiptAccDate);
    receiptAccDateField.addValueChangeHandler(changeHandler);
    form.addField(receiptAccDateField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.statementInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(form);
    return box;
  }

  private Widget drawTotalPanel() {
    RForm totalForm = new RForm(1);
    totalForm.setWidth("100%");

    receiptTotalField = new RViewNumberField();
    receiptTotalField.setFormat(M3Format.fmt_money);
    receiptTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    receiptTaxField = new RViewNumberField();
    receiptTaxField.setFormat(M3Format.fmt_money);
    receiptTaxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField receiptField = new RCombinedField() {
      {
        setCaption(StatementMessages.M.receiptTotal());
        addField(receiptTotalField, 0.5f);
        addField(receiptTaxField, 0.5f);
      }
    };
    totalForm.addField(receiptField);

    payTotalField = new RViewNumberField();
    payTotalField.setFormat(M3Format.fmt_money);
    payTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    payTaxField = new RViewNumberField();
    payTaxField.setFormat(M3Format.fmt_money);
    payTaxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField payField = new RCombinedField() {
      {
        setCaption(StatementMessages.M.payTotal());
        addField(payTotalField, 0.5f);
        addField(payTaxField, 0.5f);
      }
    };
    totalForm.addField(payField);

    totalField = new RViewNumberField();
    totalField.setFormat(M3Format.fmt_money);
    totalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    taxField = new RViewNumberField();
    taxField.setFormat(M3Format.fmt_money);
    taxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField sumTotalField = new RCombinedField() {
      {
        setCaption(StatementMessages.M.total());
        addField(totalField, 0.5f);
        addField(taxField, 0.5f);
      }
    };
    totalForm.addField(sumTotalField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.sumTotal());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(totalForm);
    return box;
  }

  private BContract createContract() {
    BContract contract = new BContract();
    contract.setUuid(entity.getContract().getUuid());
    contract.setBillNumber(entity.getContract().getCode());
    contract.setName(entity.getContract().getName());
    return contract;
  }

  private void onChangeContract() {
    BContract contract = contractField.getRawValue();
    if (contract == null || StringUtil.isNullOrBlank(contract.getUuid()))
      return;
      entity.setCounterpart(contract.getCounterpart());
      if (ep.getCounterpartTypeMap().size() > 1)
        countpartTypeField.setValue(contract.getCounterpart() == null ? null : ep
            .getCounterpartTypeMap().get(contract.getCounterpart().getCounterpartType()));
      counterpartUCNBox.setValue(entity.getCounterpart());
      entity.setAccountUnit(contract.getAccountUnit());
      accountUnitField.setValue(entity.getAccountUnit());
  }

  private void refreshEntity() {
    if (entity.getAccountUnit() != null)
      accountUnitField.setValue(entity.getAccountUnit());
    else {
      accountUnitField.clearValue();
      accountUnitField.clearValidResults();
    }

    if (entity.getContract() != null) {
      contractField.setValue(entity.getContract().getCode());
      contractNameField.setValue(entity.getContract().getName());
    } else {
      contractField.clearValue();
      contractField.clearValidResults();
      contractNameField.clearValue();
    }

    if (entity.getCounterpart() != null) {
      counterpartUCNBox.setValue(entity.getCounterpart());
      if (ep.getCounterpartTypeMap().size() > 1)
        countpartTypeField.setValue(ep.getCounterpartTypeMap().get(
            entity.getCounterpart().getCounterpartType()));
    } else {
      counterpartUCNBox.clearValue();
      counterpartUCNBox.clearValidResults();
      countpartTypeField.clearValue();
    }
    if (entity.getSettleNo() == null) {
      entity.setSettleNo(SettleNoField.getCurrentSettleNo());
    }
    settleNoField.setValue(entity.getSettleNo());
    if (entity.getReceiptAccDate() != null)
      receiptAccDateField.setValue(entity.getReceiptAccDate());
    else {
      receiptAccDateField.clearValue();
      receiptAccDateField.clearValidResults();
    }

    permGroupField.setPerm(entity);
    permGroupField.clearValidResults();

    refreshTotal();
  }

  private void onChangeAccountUnit() {
    if (entity.isExistsValidLine()) {
      String msg = StatementMessages.M.selectAccountUnit();
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false) {
            accountUnitField.setValue(entity.getAccountUnit());
          } else {
            entity.setAccountUnit(accountUnitField.getValue());
            entity.getLines().clear();
            RActionEvent.fire(StatementGeneralCreateGadget.this, ActionName.CLEAR_LINE, "");
            entity.aggregate();
            entity.calculateTotal();
            fetchContract();
            refreshEntity();
          }
        }
      });
    } else {
      entity.setAccountUnit(accountUnitField.getValue());
      fetchContract();
      refreshEntity();
    }
  }

  private void onChangeCounterpart() {
    if (counterpartUCNBox.isValid() == false) {
      countpartTypeField.clearValue();
      return;
    }

    if (entity.isExistsValidLine()) {
      String msg = StatementMessages.M.selectCounterpart();
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false) {
            counterpartUCNBox.setValue(entity.getCounterpart());
            if (ep.getCounterpartTypeMap().size() > 1)
              countpartTypeField.setValue((entity.getCounterpart() == null ? null : ep
                  .getCounterpartTypeMap().get(entity.getCounterpart().getCounterpartType())));
          } else {
            entity.setCounterpart(counterpartUCNBox.getRawValue());
            if (ep.getCounterpartTypeMap().size() > 1)
              countpartTypeField.setValue((entity.getCounterpart() == null
                  || entity.getCounterpart().getCounterpartType() == null ? null : ep
                  .getCounterpartTypeMap().get(entity.getCounterpart().getCounterpartType())));
            entity.getLines().clear();
            RActionEvent.fire(StatementGeneralCreateGadget.this, ActionName.CLEAR_LINE, "");
            fetchContract();
            entity.aggregate();
            entity.calculateTotal();
            refreshEntity();
          }
        }
      });
    } else {
      entity.setCounterpart(counterpartUCNBox.getRawValue());
      if (ep.getCounterpartTypeMap().size() > 1) {
        countpartTypeField.setValue((entity.getCounterpart() == null
            || entity.getCounterpart().getCounterpartType() == null ? null : ep
            .getCounterpartTypeMap().get(entity.getCounterpart().getCounterpartType())));
      }
      fetchContract();
      refreshEntity();
    }
  }

  private class Change_Handler implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (event.getSource() == accountUnitField) {
        onChangeAccountUnit();
      } else if (event.getSource() == settleNoField) {
        entity.setSettleNo(settleNoField.getValue());
      } else if (event.getSource() == receiptAccDateField) {
        entity.setReceiptAccDate(receiptAccDateField.getValue());
      }
    }
  }

  private void fetchContract() {
    if (counterpartUCNBox.getValue() == null || accountUnitField.getValue() == null)
      return;

    GWTUtil.enableSynchronousRPC();
    StatementService.Locator.getService().getOnlyOneContract(accountUnitField.getValue().getUuid(),
        counterpartUCNBox.getValue().getUuid(), "_Tenant",// TODO 对方单位类型
        new RBAsyncCallback2<BContract>() {

          @Override
          public void onException(Throwable caught) {
          }

          @Override
          public void onSuccess(BContract result) {
            if (result != null) {
              contractField.setRawValue(result);
              contractField.setValue(result.getBillNumber());
              contractNameField.setValue(result.getName());
              entity.setContract(new BUCN(result.getUuid(), result.getBillNumber(), result
                  .getTitle()));
            } else {
              entity.setContract(null);
              contractField.clearValue();
              contractField.clearRawValue();
              contractField.clearValidResults();
              contractNameField.clearValue();
            }
          }
        });
  }

  public static class ActionName {
    /** 清除明细 */
    public static final String CLEAR_LINE = "clear_line";
  }

}
