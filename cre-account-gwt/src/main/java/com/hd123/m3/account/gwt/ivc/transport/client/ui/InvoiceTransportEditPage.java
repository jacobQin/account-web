/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceTransportEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.transport.client.EPInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.gadget.InvoiceTransportDetailGadget;
import com.hd123.m3.account.gwt.ivc.transport.client.ui.gadget.InvoiceTransportGeneralEditGadget;
import com.hd123.m3.account.gwt.ivc.transport.intf.client.dd.PInvoiceTransportDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmEditPage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票调拨单|编辑页面
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceTransportEditPage extends BaseBpmEditPage2<BInvoiceTransport>{
  private static InvoiceTransportEditPage instance = null;
  private Change_Handler changeHandler;
  private InvoiceTransportGeneralEditGadget generalGadget;
  private InvoiceTransportDetailGadget detailGadget;
  private RTextArea remarkField;
  private Widget remarkBox;
  
  public static InvoiceTransportEditPage getInstance() {
    if (instance == null) {
      instance = new InvoiceTransportEditPage();
    }
    return instance;
  }
  
  @Override
  protected EPInvoiceTransport getEP() {
    return EPInvoiceTransport.getInstance();
  }
  
  @Override
  protected void drawSelf(VerticalPanel root) {
    changeHandler = new Change_Handler();

    generalGadget = new InvoiceTransportGeneralEditGadget();
    root.add(generalGadget);
    
    detailGadget = new InvoiceTransportDetailGadget();
    root.add(detailGadget);
    
    generalGadget.addActionHandler(detailGadget);
    
    remarkBox = drawRemarkGadget();
    root.add(remarkBox);
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceTransportDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addChangeHandler(changeHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceTransportDef.constants.remark());
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
    generalGadget.setValue(entity,true);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());

    clearValidResults();
    FocusOnFirstFieldUtil.focusOnFirstField(generalGadget);
    
  }

  private class Change_Handler implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (remarkField == event.getSource()) {
        entity.setRemark(remarkField.getValue());
      }
    }
  }
}
