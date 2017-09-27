/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegGeneralViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月8日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartLinkField;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.StoreLinkField;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.InvoiceRegMessages;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegUrlParams;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票入库单查看页面|基本信息
 * 
 * @author chenrizhang
 * @since 1.0
 * 
 */
public class InvoiceRegGeneralViewGadget extends RCaptionBox {
  private BInvoiceReg entity;
  private EPInvoiceRegReceipt ep = EPInvoiceRegReceipt.getInstance();
  private RMultiVerticalPanel root;

  private RViewStringField billNumberField;
  private StoreLinkField storeField;
  private CounterpartLinkField counterpartField;
  private RViewStringField invoiceTypeField;
  private RViewStringField regDateFeild;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfo;

  private RForm sumForm;
  private RViewNumberField sumOriginTotalField;
  private RViewNumberField sumUnregTotalField;
  private RViewNumberField sumTotalField;

  private PermGroupViewField permGroupField;

  public InvoiceRegGeneralViewGadget() {
    super();
    setCaption(InvoiceCommonMessages.M.generalInfo());
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);
    drawSelf();
  }

  private void drawSelf() {
    root = new RMultiVerticalPanel();
    root.setWidth("100%");
    root.setColumnWidth(0, "50%");
    root.setColumnWidth(1, "50%");
    root.setSpacing(5);

    root.add(0, drawBasicInfo());
    root.add(1, drawOperateInfo());

    permGroupField = new PermGroupViewField();
    root.add(1, permGroupField);
    root.add(1, drawSummaryGadget());
    setContent(root);
  }

  private Widget drawBasicInfo() {
    RForm basicForm = new RForm(1);
    basicForm.setWidth("100%");
    basicForm.setCellSpacing(2);

    billNumberField = new RViewStringField(PInvoiceRegDef.constants.billNumber());
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    basicForm.addField(billNumberField);

    storeField = new StoreLinkField(PInvoiceRegDef.constants.accountUnit());
    basicForm.addField(storeField);

    counterpartField = new CounterpartLinkField(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    basicForm.addField(counterpartField);
    
    invoiceTypeField = new RViewStringField(PInvoiceRegDef.invoiceType);
    if (ep.getEnabledExtInvoiceSystem() == false) {
      basicForm.addField(invoiceTypeField);
    }
    regDateFeild = new RViewStringField(PInvoiceRegDef.regDate);
    basicForm.addField(regDateFeild);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceCommonMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;
  }

  private Widget drawOperateInfo() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");
    operateForm.setCellSpacing(1);

    bizStateField = new RViewStringField(PInvoiceRegDef.bizState);
    bizStateField.addStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    createInfoField = new RSimpleOperateInfoField(PInvoiceRegDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PInvoiceRegDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        JumpParameters params = new JumpParameters(InvoiceRegUrlParams.START_NODE_LOG);
        params.getUrlRef().set(InvoiceRegUrlParams.KEY_ENTITY_UUID, entity.getUuid());
        ep.jump(params);
      }
    });
    moreInfo.setEnabled(true);
    moreInfo.setHTML(CommonsMessages.M.moreInfo());
    operateForm.addField(moreInfo);

    RCaptionBox operateBox = new RCaptionBox();
    operateBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    operateBox.setCaption(InvoiceCommonMessages.M.operateInfo());
    operateBox.setWidth("100%");
    operateBox.setContent(operateForm);
    return operateBox;
  }

  private Widget drawSummaryGadget() {
    sumForm = new RForm(1);
    sumForm.setWidth("100%");
    sumForm.setCellSpacing(2);

    sumOriginTotalField = new RViewNumberField(PInvoiceRegDef.originTotal);
    sumOriginTotalField.setFormat(M3Format.fmt_money);
    sumOriginTotalField.setWidth("50%");
    sumOriginTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    sumForm.addField(sumOriginTotalField);

    sumUnregTotalField = new RViewNumberField(PInvoiceRegDef.unregTotal);
    sumUnregTotalField.setFormat(M3Format.fmt_money);
    sumUnregTotalField.setWidth("50%");
    sumUnregTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    sumForm.addField(sumUnregTotalField);

    sumTotalField = new RViewNumberField(PInvoiceRegDef.total);
    sumTotalField.setWidth("50%");
    sumTotalField.setFormat(M3Format.fmt_money);
    sumTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    sumForm.addField(sumTotalField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceRegMessages.M.summary());
    box.setWidth("100%");
    box.setContent(sumForm);
    return box;
  }

  public void setValue(BInvoiceReg entity) {
    if (entity == null) {
      return;
    }
    this.entity = entity;
    billNumberField.setValue(entity.getBillNumber());
    storeField.setRawValue(entity.getAccountUnit());
    counterpartField.setRawValue(new BCounterpart(entity.getCounterpart(), entity
        .getCounterpartType()));
    if (ep.getEnabledExtInvoiceSystem() == false) {
      invoiceTypeField.setValue(entity.getInvoiceType() == null ? null : ep.getInvoiceTypes().get(
          entity.getInvoiceType()));
    }
    regDateFeild.setValue(entity.getRegDate() == null ? null : M3Format.fmt_yMd.format(entity
        .getRegDate()));

    bizStateField.setValue(BBizStates.getCaption(entity.getBizState()));
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());

    permGroupField.refresh(ep.isPermEnabled(), entity);

    entity.sumTotal();
    sumOriginTotalField.setValue(entity.getOriginTotal().getTotal());
    sumUnregTotalField.setValue(entity.getAccountTotal().getTotal());
    sumUnregTotalField.setVisible(entity.isAllowSplitReg());
    sumTotalField.setValue(entity.getInvoiceTotal().getTotal());
    sumForm.rebuild();
  }
}
