/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeGeneralViewWidget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月19日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.StoreLinkField;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.page.InvoiceExchangeLogPage;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.InvoiceExchangeUrlParams;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchangeDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票交换单查看页面|基本信息控件
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeGeneralViewWidget extends RCaptionBox {
  private BInvoiceExchange entity;
  private EPInvoiceExchange ep = EPInvoiceExchange.getInstance();
  private RMultiVerticalPanel root;

  private RForm basicForm;
  private RViewStringField billNumberField;
  private StoreLinkField storeField;
  private RViewStringField exchangeTypeField;
  private RViewStringField invToInvTypeField;
  private RViewStringField exchangerField;
  private RViewStringField exchangeDateFeild;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfo;

  private PermGroupViewField permGroupField;

  public InvoiceExchangeGeneralViewWidget() {
    setCaption(InvoiceCommonMessages.M.generalInfo());
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);
    drawSelf();
  }

  public void setValue(BInvoiceExchange entity) {
    if (entity == null) {
      return;
    }
    this.entity = entity;
    billNumberField.setValue(entity.getBillNumber());
    storeField.setRawValue(entity.getAccountUnit());
    exchangeTypeField.setValue(entity.getType().getCaption());
    exchangerField.setValue(entity.getExchanger().toFriendlyStr());
    exchangeDateFeild.setValue(entity.getExchangeDate() == null ? null : M3Format.fmt_yMd
        .format(entity.getExchangeDate()));

    bizStateField.setValue(BBizStates.getCaption(entity.getBizState()));
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();

    permGroupField.refresh(ep.isPermEnabled(), entity);
    refreshShow();

    basicForm.rebuild();
  }

  private void refreshShow() {
    if (BExchangeType.invToInv.equals(entity.getType())) {
      invToInvTypeField.setVisible(true);
      if(entity.getInvToInvType() != null){
        invToInvTypeField.setValue(entity.getInvToInvType().getCaption());
      }else {
        invToInvTypeField.setValue(null);
      }
    } else {
      invToInvTypeField.setVisible(false);
    }
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
    setContent(root);
  }

  private Widget drawOperateInfo() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");
    operateForm.setCellSpacing(1);

    bizStateField = new RViewStringField(PInvoiceExchangeDef.bizState);
    bizStateField.addStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    createInfoField = new RSimpleOperateInfoField(PInvoiceExchangeDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PInvoiceExchangeDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(new MoreInfoClickHandler());
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

  private Widget drawBasicInfo() {
    basicForm = new RForm(1);
    basicForm.setWidth("100%");
    basicForm.setCellSpacing(2);

    billNumberField = new RViewStringField(PInvoiceExchangeDef.constants.billNumber());
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    basicForm.addField(billNumberField);

    storeField = new StoreLinkField(PInvoiceExchangeDef.constants.accountUnit());
    basicForm.addField(storeField);

    exchangeTypeField = new RViewStringField(PInvoiceExchangeDef.exchangeType);
    basicForm.addField(exchangeTypeField);

    invToInvTypeField = new RViewStringField(PInvoiceExchangeDef.invToInvType);
    basicForm.add(invToInvTypeField);

    exchangerField = new RViewStringField(PInvoiceExchangeDef.exchanger);
    basicForm.addField(exchangerField);

    exchangeDateFeild = new RViewStringField(PInvoiceExchangeDef.exchangeDate);
    basicForm.addField(exchangeDateFeild);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceCommonMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;
  }

  private class MoreInfoClickHandler implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == moreInfo) {
        JumpParameters params = new JumpParameters(InvoiceExchangeLogPage.START_NODE);
        params.getUrlRef().set(InvoiceExchangeUrlParams.PN_UUID, entity.getUuid());
        ep.jump(params);
      }
    }
  }

}
