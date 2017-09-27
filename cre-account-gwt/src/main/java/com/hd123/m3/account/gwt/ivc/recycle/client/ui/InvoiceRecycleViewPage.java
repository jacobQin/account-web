/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月11日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.recycle.client.EPInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.gadget.InvoiceRecycleDetailViewGadget;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.gadget.InvoiceRecycleGeneralViewGadget;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.dd.PInvoiceRecycleDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmViewPage2;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票回收单|查看页面
 * 
 * @author lixiaohong
 *@since 1.9
 *
 */
public class InvoiceRecycleViewPage extends BaseBpmViewPage2<BInvoiceRecycle>{
  private static InvoiceRecycleViewPage instance = null;
  private InvoiceRecycleGeneralViewGadget generalGadget;
  private InvoiceRecycleDetailViewGadget detailGadget;
  private RTextArea remarkField;


  public static InvoiceRecycleViewPage getInstance() {
    if (instance == null) {
      instance = new InvoiceRecycleViewPage();
    }
    return instance;
  }
  
  @Override
  protected EPInvoiceRecycle getEP() {
    return EPInvoiceRecycle.getInstance();
  }

  @Override
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceRecycleGeneralViewGadget();
    root.add(generalGadget);
    
    detailGadget = new InvoiceRecycleDetailViewGadget();
    root.add(detailGadget);
    root.add(drawRemarkGadget());
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceRecycleDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceRecycleDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }
}
