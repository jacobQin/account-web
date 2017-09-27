/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReceiveViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.receive.client.EPInvoiceReceive;
import com.hd123.m3.account.gwt.ivc.receive.client.biz.BInvoiceReceive;
import com.hd123.m3.account.gwt.ivc.receive.client.ui.gadget.InvoiceReceiveDetailViewGadget;
import com.hd123.m3.account.gwt.ivc.receive.client.ui.gadget.InvoiceReceiveGeneralViewGadget;
import com.hd123.m3.account.gwt.ivc.receive.intf.client.InvoiceReceiveUrlParams.View;
import com.hd123.m3.account.gwt.ivc.receive.intf.client.dd.PInvoiceReceiveDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmViewPage2;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票领用单|查看页面
 * 
 * @author lixiaohong
 *@since 1.7
 *
 */
public class InvoiceReceiveViewPage extends BaseBpmViewPage2<BInvoiceReceive> implements View{
  private static InvoiceReceiveViewPage instance = null;
  private InvoiceReceiveGeneralViewGadget generalGadget;
  private InvoiceReceiveDetailViewGadget detailGadget;
  private RTextArea remarkField;


  public static InvoiceReceiveViewPage getInstance() {
    if (instance == null) {
      instance = new InvoiceReceiveViewPage();
    }
    return instance;
  }
  
  @Override
  protected EPInvoiceReceive getEP() {
    return EPInvoiceReceive.getInstance();
  }

  @Override
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceReceiveGeneralViewGadget();
    root.add(generalGadget);
    
    detailGadget = new InvoiceReceiveDetailViewGadget();
    root.add(detailGadget);
    root.add(drawRemarkGadget());
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceReceiveDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceReceiveDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }
}
