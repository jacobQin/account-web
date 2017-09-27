/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.page;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvToInvType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeEviToEviAccountEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeEviToEviEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeEviToInvAccountEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeGeneralEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeInvToInvAccountEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeInvToInvEditWidget;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.InvoiceExchangeUrlParams.Edit;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchangeDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmEditPage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;

/**
 * 发票交换单|编辑页面
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeEditPage extends BaseBpmEditPage2<BInvoiceExchange> implements Edit {

  private static InvoiceExchangeEditPage instance = null;

  private FieldValueChangeHandler changeHandler;
  private InvoiceExchangeGeneralEditWidget generalWidget;
  private RTextArea remarkField;

  private RMultiVerticalPanel mainPanel;

  // 组件
  private InvoiceExchangeEviToEviEditWidget eviToEviEditWidget;
  private InvoiceExchangeInvToInvEditWidget invToInvEditWidget;

  // 账款明细组件
  private InvoiceExchangeEviToEviAccountEditWidget eviToEviAccountEditWidget;
  private InvoiceExchangeEviToInvAccountEditWidget eviToInvAccountEditWidget;
  private InvoiceExchangeInvToInvAccountEditWidget invToInvAccountEditWidget;

  public static InvoiceExchangeEditPage getInstance() {
    if (instance == null) {
      instance = new InvoiceExchangeEditPage();
    }
    return instance;
  }

  @Override
  protected EPInvoiceExchange getEP() {
    return EPInvoiceExchange.getInstance();
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    changeHandler = new FieldValueChangeHandler();

    generalWidget = new InvoiceExchangeGeneralEditWidget();
    root.add(generalWidget);

    mainPanel = new RMultiVerticalPanel(1);
    root.add(mainPanel);

    eviToEviEditWidget = new InvoiceExchangeEviToEviEditWidget();
    invToInvEditWidget = new InvoiceExchangeInvToInvEditWidget();

    eviToEviAccountEditWidget = new InvoiceExchangeEviToEviAccountEditWidget();
    eviToInvAccountEditWidget = new InvoiceExchangeEviToInvAccountEditWidget();
    invToInvAccountEditWidget = new InvoiceExchangeInvToInvAccountEditWidget();

    addActionListener();
    
    root.add(drawRemarkWidget());
  }

  private void addActionListener(){
    invToInvEditWidget.addActionHandler(invToInvAccountEditWidget);
    eviToEviEditWidget.addActionHandler(eviToEviAccountEditWidget);
    eviToEviEditWidget.addActionHandler(eviToInvAccountEditWidget);
  }
  
  private Widget drawRemarkWidget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceExchangeDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addChangeHandler(changeHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceExchangeDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  protected void refreshEntity() {
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }

    generalWidget.setValue(entity, true);
    remarkField.setValue(entity.getRemark());

    refreshShow();

    clearValidResults();
    FocusOnFirstFieldUtil.focusOnFirstField(generalWidget);
  }

  private void refreshShow() {
    removeMainPanelWidgets();
    if (BExchangeType.invToInv.equals(entity.getType())) {
      invToInvEditWidget.setValue(entity);
      mainPanel.add(0, invToInvEditWidget);
      if (BInvToInvType.exchange.equals(entity.getInvToInvType())) {
        invToInvAccountEditWidget.setValue(entity);
        mainPanel.add(0, invToInvAccountEditWidget);
      }

    } else if (BExchangeType.eviToEvi.equals(entity.getType())) {
      eviToEviEditWidget.setValue(entity);
      eviToEviAccountEditWidget.setValue(entity);
      
      mainPanel.add(0, eviToEviEditWidget);
      mainPanel.add(0, eviToEviAccountEditWidget);
    } else {
      eviToEviEditWidget.setValue(entity);
      eviToInvAccountEditWidget.setValue(entity);
      
      mainPanel.add(0, eviToEviEditWidget);
      mainPanel.add(0, eviToInvAccountEditWidget);
    }
  }

  private void removeMainPanelWidgets() {
    mainPanel.remove(eviToEviEditWidget);
    mainPanel.remove(invToInvEditWidget);
    mainPanel.remove(eviToEviAccountEditWidget);
    mainPanel.remove(eviToInvAccountEditWidget);
    mainPanel.remove(invToInvAccountEditWidget);
  }

  private class FieldValueChangeHandler implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (remarkField == event.getSource()) {
        entity.setRemark(remarkField.getValue());
      }
    }
  }

}