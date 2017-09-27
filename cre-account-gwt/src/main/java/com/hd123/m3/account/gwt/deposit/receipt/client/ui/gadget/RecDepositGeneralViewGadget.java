/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayGeneralViewGadget.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.receipt.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositDef;
import com.hd123.m3.account.gwt.deposit.receipt.client.EPRecDeposit;
import com.hd123.m3.account.gwt.deposit.receipt.client.ui.page.RecDepositLogViewPage;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
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
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenpeisi
 * 
 */
public class RecDepositGeneralViewGadget extends RCaptionBox {

  private EPRecDeposit ep = EPRecDeposit.getInstance(EPRecDeposit.class);

  public RecDepositGeneralViewGadget() {
    drawSelf();
  }

  private BDeposit entity;

  private RForm generalForm;
  private RViewStringField billNumberField;
  private RViewStringField countPartField;
  private RViewStringField accountUnitField;
  private DispatchLinkField contractNumber;
  private RViewStringField contractNameField;
  private RViewStringField counterContactField;

  private RForm payInfoForm;
  private RViewStringField paymentTypeField;
  private RViewDateField depositDateField;
  private RViewDateField accountDateField;
  private RViewStringField dealerField;
  private RViewStringField bankField;

  private RForm operateForm;
  private RViewStringField reasonField;
  private RViewStringField bizStateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfoField;

  private PermGroupViewField permGroupField;

  private RForm totalForm;
  private RViewNumberField depositTotalField;

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
    mvp.add(0, drawPayInfoPanel());

    mvp.add(1, drawOperatePanel());

    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    mvp.add(1, drawTotelPanel());

    setCaption(DepositMessage.M.generalInfo());
    setWidth("100%");
    setContent(vp);
  }

  private Widget drawGeneralPanel() {
    generalForm = new RForm(1);
    generalForm.setWidth("100%");

    billNumberField = new RViewStringField(PDepositDef.constants.billNumber());
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    generalForm.addField(billNumberField);

    accountUnitField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    generalForm.addField(accountUnitField);

    countPartField = new RViewStringField(EPRecDeposit.getInstance().getFieldCaption(
        GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
    generalForm.addField(countPartField);

    contractNumber = new DispatchLinkField(PDepositDef.constants.contract_code());
    contractNumber.setLinkKey(GRes.R.dispatch_key());
    generalForm.addField(contractNumber);

    contractNameField = new RViewStringField(PDepositDef.constants.contract_name());
    generalForm.addField(contractNameField);

    RCaptionBox box = new RCaptionBox();
    box.setContent(generalForm);
    box.setCaption(DepositMessage.M.basicInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  private Widget drawPayInfoPanel() {
    payInfoForm = new RForm(1);
    payInfoForm.setWidth("100%");

    paymentTypeField = new RViewStringField(PDepositDef.paymentType);
    payInfoForm.addField(paymentTypeField);

    depositDateField = new RViewDateField(EPRecDeposit.depositDate);
    payInfoForm.addField(depositDateField);
    
    accountDateField = new RViewDateField(PDepositDef.accountDate);
    payInfoForm.addField(accountDateField);

    dealerField = new RViewStringField(PDepositDef.constants.dealer());
    payInfoForm.addField(dealerField);

    bankField = new RViewStringField(DepositMessage.M.bank());
    payInfoForm.addField(bankField);

    counterContactField = new RViewStringField(PDepositDef.constants.counterContact());
    payInfoForm.addField(counterContactField);

    payInfoForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(payInfoForm);
    box.setCaption(DepositMessage.M.receiptInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
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

    moreInfoField = new RHyperlink();
    moreInfoField.addClickHandler(new Handler_viewLogAction());
    moreInfoField.setHTML(DepositMessage.M.moreInfo());

    RCombinedField moreField = new RCombinedField() {
      {
        addField(new HTML(), 0.1f);
        addField(moreInfoField, 0.9f);
      }
    };
    operateForm.addField(moreField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositMessage.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawTotelPanel() {
    totalForm = new RForm(1);
    totalForm.setWidth("100%");

    depositTotalField = new RViewNumberField(PDepositDef.constants.depositTotal());
    depositTotalField.setWidth("38%");
    depositTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    depositTotalField.setFormat(M3Format.fmt_money);
    totalForm.addField(depositTotalField);

    totalForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setContent(totalForm);
    box.setCaption(DepositMessage.M.total());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    return box;
  }

  public void refresh(BDeposit entity) {
    assert entity != null;
    this.entity = entity;

    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .toFriendlyStr());
    countPartField.setValue(entity.getCounterpart() == null ? null : entity.getCounterpart()
        .toFriendlyStr(ep.getCounterpartTypeMap()));
    if (entity.getContract() == null) {
      contractNumber.setValue(entity.getContract() == null ? null : entity.getContract().getCode());
    } else {
      if (entity.getCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getCounterpart()
              .getCounterpartType())) {
        contractNumber.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity.getContract()
            .getCode());
      } else {
        contractNumber.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getContract().getCode());
      }
    }
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());
    counterContactField.setValue(entity.getCounterContact());

    paymentTypeField.setValue(entity.getPaymentType() == null ? null : entity.getPaymentType()
        .toFriendlyStr());
    depositDateField.setValue(entity.getDepositDate());
    accountDateField.setValue(entity.getAccountDate());
    dealerField.setValue(entity.getDealer() == null ? null : entity.getDealer().toFriendlyStr());
    bankField.setValue(entity.getBank() == null ? null : entity.getBank().toFriendlyStr());

    bizStateField.setValue(PDepositDef.bizState.getEnumCaption(entity.getBizState()));
    reasonField.setVisible(entity.getBpmMessage() != null);
    reasonField.setValue(entity.getBpmMessage());
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());

    depositTotalField.setValue(entity.getDepositTotal());

    permGroupField.refresh(ep.isPermEnabled(), entity);

    generalForm.rebuild();
    payInfoForm.rebuild();
    operateForm.rebuild();
    totalForm.rebuild();
  }

  private class Handler_viewLogAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(RecDepositLogViewPage.START_NODE);
      params.getUrlRef().set(RecDepositLogViewPage.PN_UUID, entity.getUuid());
      ep.jump(params);
    }
  }
}
