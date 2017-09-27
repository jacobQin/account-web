/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceTransportViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.transport.client.EPInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.gadget.InvoiceTransportDetailViewGadget;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.gadget.InvoiceTransportGeneralViewGadget;
import com.hd123.m3.account.gwt.ivc.transport.intf.client.dd.PInvoiceTransportDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmViewPage2;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票调拨单|查看页面
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceTransportViewPage extends BaseBpmViewPage2<BInvoiceTransport>{
  private static InvoiceTransportViewPage instance = null;
  private InvoiceTransportGeneralViewGadget generalGadget;
  private InvoiceTransportDetailViewGadget detailGadget;
  private RTextArea remarkField;


  public static InvoiceTransportViewPage getInstance() {
    if (instance == null) {
      instance = new InvoiceTransportViewPage();
    }
    return instance;
  }
  
  @Override
  protected EPInvoiceTransport getEP() {
    return EPInvoiceTransport.getInstance();
  }

  @Override
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceTransportGeneralViewGadget();
    root.add(generalGadget);
    
    detailGadget = new InvoiceTransportDetailViewGadget();
    root.add(detailGadget);
    root.add(drawRemarkGadget());
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceTransportDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceTransportDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }
}
