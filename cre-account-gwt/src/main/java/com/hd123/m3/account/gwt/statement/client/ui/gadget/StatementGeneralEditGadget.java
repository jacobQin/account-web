/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementGeneralEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月6日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccRange;
import com.hd123.m3.account.gwt.statement.intf.client.dd.CStatementType;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.m3.account.gwt.statement.intf.client.dd.StatementTypeDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewDateField;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;

/**
 * @author zhr
 * 
 */
public class StatementGeneralEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {

  public StatementGeneralEditGadget() {
    super();
    setCaption(StatementMessages.M.generalInfo());
    setEditing(true);

    setContent(drawSelf());
  }

  public void setValue(BStatement entity) {
    this.entity = entity;

    refreshEntity();
  }

  private void refreshReference() {
    referenceBox.setVisible(entity.isShowRefer());
    if (referenceBox.isVisible()) {
      saleTotalField.setValue(entity.getSaleTotal() == null ? null : entity.getSaleTotal()
          .doubleValue());
    }
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

    totalField.setValue(entity.calculateTotal().getTotal());
    taxField.setValue(entity.calculateTotal().getTax());
  }

  public void focusOnFirstField() {
    settleNoField.setFocus(true);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == StatementLinePatchEditGridGadget.ActionName.REFRESH
        || event.getActionName() == StatementLineGridGadget.ActionName.REFRESH) {
      entity.aggregate();
      entity.calculateTotal();
      refreshTotal();
    }
  }

  private BStatement entity;
  private EPStatement ep = EPStatement.getInstance();

  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private DispatchLinkField contractNumField;
  private RViewStringField contractNameField;
  private RViewStringField counterpartField;
  private RViewStringField typeField;
  private SettleNoField settleNoField;

  private RForm statementForm;
  private RDateBox receiptAccDateField;
  private RViewDateField accountTimeField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField reasonField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  private PermGroupViewField permGroupField;

  private RCaptionBox referenceBox;
  private RForm referenceForm;
  private RViewNumberField saleTotalField;

  private RViewNumberField receiptTotalField;
  private RViewNumberField receiptTaxField;
  private RViewNumberField payTotalField;
  private RViewNumberField payTaxField;
  private RViewNumberField totalField;
  private RViewNumberField taxField;

  private ValueChange_Handler changeHandler = new ValueChange_Handler();

  private Widget drawSelf() {
    RMultiVerticalPanel mvp = new RMultiVerticalPanel();
    mvp.setWidth("100%");
    mvp.setSpacing(8);
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");

    mvp.add(0, drawBasicPanel());
    mvp.add(0, drawStatement());

    mvp.add(1, drawOperatePanel());

    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    mvp.add(1, drawReference());

    mvp.add(1, drawTotalPanel());
    return mvp;
  }

  private Widget drawBasicPanel() {
    RForm basicInfoForm = new RForm(1);
    basicInfoForm.setWidth("100%");

    billNumberField = new RViewStringField(StatementMessages.M.billNumber());
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    basicInfoForm.addField(billNumberField);

    accountUnitField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    basicInfoForm.addField(accountUnitField);

    contractNumField = new DispatchLinkField(PStatementDef.constants.contract_code());
    contractNumField.setLinkKey(GRes.R.dispatch_key());
    basicInfoForm.addField(contractNumField);

    contractNameField = new RViewStringField(PStatementDef.contract_name);
    basicInfoForm.addField(contractNameField);

    counterpartField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    basicInfoForm.addField(counterpartField);

    typeField = new RViewStringField(PStatementDef.type);
    basicInfoForm.addField(typeField);

    settleNoField = new SettleNoField(PStatementDef.settleNo);
    settleNoField.addValueChangeHandler(changeHandler);
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
    statementForm = new RForm(1);
    statementForm.setWidth("100%");

    receiptAccDateField = new RDateBox(PStatementDef.receiptAccDate);
    receiptAccDateField.addValueChangeHandler(changeHandler);

    accountTimeField = new RViewDateField(PStatementDef.accountTime);
    accountTimeField.setFormat(M3Format.fmt_yMd);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.statementInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(statementForm);
    return box;
  }

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PStatementDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    reasonField = new RViewStringField();
    reasonField.setCaption(StatementMessages.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PStatementDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PStatementDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(StatementMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawReference() {
    referenceForm = new RForm(1);
    referenceForm.setWidth("100%");

    saleTotalField = new RViewNumberField(StatementMessages.M.saleTotal());
    saleTotalField.setFormat(M3Format.fmt_money);
    referenceForm.addField(saleTotalField);

    referenceBox = new RCaptionBox();
    referenceBox.setCaption(StatementMessages.M.reference());
    referenceBox.setWidth("100%");
    referenceBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    referenceBox.setContent(referenceForm);
    return referenceBox;
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

  private void refreshEntity() {
    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit().toFriendlyStr());
    if (entity.getContract() == null) {
      contractNumField.clearValue();
    } else {
      if (entity.getCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getCounterpart()
              .getCounterpartType())) {
        contractNumField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity.getContract()
            .getCode());
      } else {
        contractNumField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getContract().getCode());
      }
    }
    contractNameField.setValue(entity.getContract().getName());
    counterpartField.setValue(entity.getCounterpart() == null ? null : entity.getCounterpart()
        .toFriendlyStr(ep.getCounterpartTypeMap()));
    typeField.setValue(CStatementType.patch.equals(entity.getType()) ? StatementTypeDef.constants
        .patch() : StatementTypeDef.constants.normal());
    settleNoField.setValue(entity.getSettleNo());
    // 刷新账单
    refreshStatement();
    // 刷新参考信息
    refreshReference();

    bizStateField.setValue(BBizStates.getCaption(entity.getBizState()));
    if (entity.getBpmMessage() != null) {
      reasonField.setVisible(true);
      reasonField.setValue(entity.getBpmMessage());
    } else {
      reasonField.setVisible(false);
    }
    permGroupField.refresh(ep.isPermEnabled(), entity);
    permGroupField.clearValidResults();
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();

    refreshTotal();
  }

  private void refreshStatement() {
    statementForm.clear();
    if (CStatementType.normal.equals(entity.getType())) {
      // 动态展示账期
      for (BStatementAccRange range : entity.getRanges()) {
        RViewStringField field = new RViewStringField(range.getCaption());
        field.setValue(EPStatement.buildDateRangeStr(range.getDateRange()));
        statementForm.addField(field);
      }
    }
    if (CStatementType.normal.equals(entity.getType())) {
      statementForm.addField(accountTimeField);
      accountTimeField.setValue(entity.getAccountTime());
    }
    statementForm.addField(receiptAccDateField);
    receiptAccDateField.setValue(entity.getReceiptAccDate());
    statementForm.rebuild();
  }

  private class ValueChange_Handler implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (receiptAccDateField == event.getSource()) {
        entity.setReceiptAccDate(receiptAccDateField.getValue());
      } else if (event.getSource() == settleNoField) {
        entity.setSettleNo(settleNoField.getValue());
      }
    }
  }

}
