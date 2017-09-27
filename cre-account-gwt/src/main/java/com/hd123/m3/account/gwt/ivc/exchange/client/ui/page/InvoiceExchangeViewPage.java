/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.page;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvToInvType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeAccountViewWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeEviToEviViewWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeGeneralViewWidget;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget.InvoiceExchangeInvToInvViewWidget;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.InvoiceExchangeUrlParams.View;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchangeDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmViewPage2;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;

/**
 * 发票交换单|查看页面
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeViewPage extends BaseBpmViewPage2<BInvoiceExchange> implements View {

  private static InvoiceExchangeViewPage instance = null;

  private InvoiceExchangeGeneralViewWidget generalWidget;
  private RTextArea remarkField;

  // 票据明细组件
  private InvoiceExchangeInvToInvViewWidget invToInvViewWidget;
  private InvoiceExchangeEviToEviViewWidget eviToEviViewWidget;

  // 账款明细组件
  private InvoiceExchangeAccountViewWidget accountViewWidget;

  private RMultiVerticalPanel mainPanel;

  public static InvoiceExchangeViewPage getInstance() {
    if (instance == null) {
      instance = new InvoiceExchangeViewPage();
    }
    return instance;
  }

  @Override
  protected EPInvoiceExchange getEP() {
    return EPInvoiceExchange.getInstance();
  }

  @Override
  protected void refreshEntity() {
    generalWidget.setValue(entity);
    eviToEviViewWidget.setValue(entity);
    invToInvViewWidget.setValue(entity);
    accountViewWidget.setValue(entity);

    remarkField.setValue(entity.getRemark());

    refreshShow();
  }

  private void refreshShow() {
    removeMainPanelWidgets();
    if (BExchangeType.invToInv.equals(entity.getType())) {
      mainPanel.add(0, invToInvViewWidget);
      if (BInvToInvType.exchange.equals(entity.getInvToInvType())) {
        mainPanel.add(0, accountViewWidget);
      }
    } else {
      mainPanel.add(0, eviToEviViewWidget);
      mainPanel.add(0, accountViewWidget);
    }
  }

  private void removeMainPanelWidgets() {
    mainPanel.remove(eviToEviViewWidget);
    mainPanel.remove(invToInvViewWidget);
    mainPanel.remove(accountViewWidget);
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalWidget = new InvoiceExchangeGeneralViewWidget();
    root.add(generalWidget);

    mainPanel = new RMultiVerticalPanel(1);
    root.add(mainPanel);

    eviToEviViewWidget = new InvoiceExchangeEviToEviViewWidget();
    invToInvViewWidget = new InvoiceExchangeInvToInvViewWidget();

    accountViewWidget = new InvoiceExchangeAccountViewWidget();

    root.add(drawRemarkWidget());
  }

  private Widget drawRemarkWidget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceExchangeDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceExchangeDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

}
