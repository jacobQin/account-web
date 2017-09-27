/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReturnViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.returns.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.returns.client.EPInvoiceReturn;
import com.hd123.m3.account.gwt.ivc.returns.client.biz.BInvoiceReturn;
import com.hd123.m3.account.gwt.ivc.returns.client.ui.gadget.InvoiceReturnDetailViewGadget;
import com.hd123.m3.account.gwt.ivc.returns.client.ui.gadget.InvoiceReturnGeneralViewGadget;
import com.hd123.m3.account.gwt.ivc.returns.intf.client.InvoiceReturnUrlParams.View;
import com.hd123.m3.account.gwt.ivc.returns.intf.client.dd.PInvoiceReturnDef;
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
public class InvoiceReturnViewPage extends BaseBpmViewPage2<BInvoiceReturn> implements View{
  private static InvoiceReturnViewPage instance = null;
  private InvoiceReturnGeneralViewGadget generalGadget;
  private InvoiceReturnDetailViewGadget detailGadget;
  private RTextArea remarkField;


  public static InvoiceReturnViewPage getInstance() {
    if (instance == null) {
      instance = new InvoiceReturnViewPage();
    }
    return instance;
  }
  
  @Override
  protected EPInvoiceReturn getEP() {
    return EPInvoiceReturn.getInstance();
  }

  @Override
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceReturnGeneralViewGadget();
    root.add(generalGadget);
    
    detailGadget = new InvoiceReturnDetailViewGadget();
    root.add(detailGadget);
    root.add(drawRemarkGadget());
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceReturnDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceReturnDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }
}
