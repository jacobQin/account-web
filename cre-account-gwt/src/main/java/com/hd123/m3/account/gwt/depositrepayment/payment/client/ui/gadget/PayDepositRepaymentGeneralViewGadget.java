/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositRepaymentGeneralViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentDef;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.EPPayDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.PayDepositRepaymentLogViewPage;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewDateField;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositRepaymentGeneralViewGadget extends RCaptionBox {

  private EPPayDepositRepayment ep = EPPayDepositRepayment.getInstance();

  public PayDepositRepaymentGeneralViewGadget() {
    drawSelf();
  }

  private BDepositRepayment entity;
  private boolean fromBPM = false;

  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private RViewStringField countPartField;
  private DispatchLinkField contractNumberField;
  private RViewStringField contractNameField;

  private RForm repaymentInfoForm;
  private RViewStringField paymentTypeField;
  private RViewDateField repaymentdateField;
  private RViewDateField accountDateField;
  private RViewStringField dealerField;
  private RViewStringField bankField;
  private RViewStringField counterContactField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField reasonField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfoField;

  private PermGroupViewField permGroupField;

  private RForm totalForm;
  private RViewNumberField repaymentTotalField;

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
    mvp.add(1, drawTotelPanel());

    setCaption(DepositRepaymentMessage.M.generalInfo());
    setWidth("100%");
    setContent(vp);
  }

  private Widget drawGeneralPanel() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    billNumberField = new RViewStringField(PDepositRepaymentDef.constants.billNumber());
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    accountUnitField = new RViewStringField(EPPayDepositRepayment.getInstance().getFieldCaption(
        GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    generalForm.addField(accountUnitField);

    countPartField = new RViewStringField(EPPayDepositRepayment.getInstance().getFieldCaption(
        GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
    generalForm.addField(countPartField);

    contractNumberField = new DispatchLinkField(PDepositRepaymentDef.constants.contract_code());
    contractNumberField.setLinkKey(GRes.R.dispatch_key());
    generalForm.addField(contractNumberField);

    contractNameField = new RViewStringField(PDepositRepaymentDef.constants.contract_name());
    generalForm.addField(contractNameField);

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

    paymentTypeField = new RViewStringField(PDepositRepaymentDef.constants.paymentType());
    repaymentInfoForm.addField(paymentTypeField);

    repaymentdateField = new RViewDateField(PDepositRepaymentDef.constants.repaymentDate());
    repaymentInfoForm.addField(repaymentdateField);

    accountDateField = new RViewDateField(PDepositRepaymentDef.constants.accountDate());
    repaymentInfoForm.addField(accountDateField);
    
    dealerField = new RViewStringField(PDepositRepaymentDef.constants.dealer());
    repaymentInfoForm.addField(dealerField);

    bankField = new RViewStringField(DepositRepaymentMessage.M.bank());
    repaymentInfoForm.addField(bankField);

    counterContactField = new RViewStringField(PDepositRepaymentDef.constants.counterContact());
    repaymentInfoForm.addField(counterContactField);

    repaymentInfoForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(repaymentInfoForm);
    box.setCaption(DepositRepaymentMessage.M.repaymentInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PDepositRepaymentDef.constants.bizState());
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    reasonField = new RViewStringField(DepositRepaymentMessage.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PDepositRepaymentDef.constants.createInfo());
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(
        PDepositRepaymentDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfoField);

    moreInfoField = new RHyperlink();
    moreInfoField.addClickHandler(new Handler_viewLogAction());
    moreInfoField.setHTML(DepositRepaymentMessage.M.moreInfo());

    RCombinedField moreField = new RCombinedField() {
      {
        addField(new HTML(), 0.1f);
        addField(moreInfoField, 0.9f);
      }
    };
    operateForm.addField(moreField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositRepaymentMessage.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawPermGroup() {
    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    permGroupField.setCaption(DepositRepaymentMessage.M.permGroup());
    return permGroupField;
  }

  private Widget drawTotelPanel() {
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

  public void setISBPM(Boolean isBPM) {
    fromBPM = isBPM;
  }

  public void refresh(BDepositRepayment entity) {
    assert entity != null;
    this.entity = entity;

    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .toFriendlyStr());
    countPartField.setValue(entity.getCounterpart().toFriendlyStr(ep.getCounterpartTypeMap()));
    if (entity.getContract() == null) {
      contractNumberField.setValue(null);
    } else {
      if (entity.getCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getCounterpart()
              .getCounterpartType())) {
        contractNumberField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity
            .getContract().getCode());
      } else {
        contractNumberField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getContract()
            .getCode());
      }
    }
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());
    counterContactField.setValue(entity.getCounterContact());

    paymentTypeField.setValue(entity.getPaymentType() == null ? null : entity.getPaymentType()
        .toFriendlyStr());
    repaymentdateField.setValue(entity.getRepaymentDate());
    accountDateField.setValue(entity.getAccountDate());
    dealerField.setValue(entity.getDealer() == null ? null : entity.getDealer().toFriendlyStr());
    bankField.setValue(entity.getBank() == null ? null : entity.getBank().toFriendlyStr());

    bizStateField.setValue(PDepositRepaymentDef.bizState.getEnumCaption(entity.getBizState()));
    reasonField.setVisible(entity.getBpmMessage() != null);
    reasonField.setValue(entity.getBpmMessage());
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());

    permGroupField.refresh(EPPayDepositRepayment.getInstance().isPermEnabled(), entity);
    repaymentTotalField.setValue(entity.getRepaymentTotal());

    generalForm.rebuild();
    repaymentInfoForm.rebuild();
    operateForm.rebuild();
    totalForm.rebuild();
  }

  private class Handler_viewLogAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(PayDepositRepaymentLogViewPage.START_NODE);
      params.getUrlRef().set(PayDepositRepaymentLogViewPage.PN_ENTITY_UUID, entity.getUuid());
      EPPayDepositRepayment.getInstance().jump(params);
    }
  }
}
