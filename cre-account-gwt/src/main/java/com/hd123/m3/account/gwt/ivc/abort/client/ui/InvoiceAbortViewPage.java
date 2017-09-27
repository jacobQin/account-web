/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceAbortViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月18日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.abort.client.EPInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.client.biz.BInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.client.ui.gadget.InvoiceAbortDetailViewGadget;
import com.hd123.m3.account.gwt.ivc.abort.client.ui.gadget.InvoiceAbortGeneralViewGadget;
import com.hd123.m3.account.gwt.ivc.abort.intf.client.InvoiceAbortUrlParams.View;
import com.hd123.m3.account.gwt.ivc.abort.intf.client.dd.PInvoiceAbortDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmViewPage2;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票作废单|查看页面
 * 
 * @author lixiaohong
 *@since 1.7
 *
 */
public class InvoiceAbortViewPage extends BaseBpmViewPage2<BInvoiceAbort> implements View{
  private static InvoiceAbortViewPage instance = null;
  private InvoiceAbortGeneralViewGadget generalGadget;
  private InvoiceAbortDetailViewGadget detailGadget;
  private RTextArea remarkField;


  public static InvoiceAbortViewPage getInstance() {
    if (instance == null) {
      instance = new InvoiceAbortViewPage();
    }
    return instance;
  }
  
  @Override
  protected EPInvoiceAbort getEP() {
    return EPInvoiceAbort.getInstance();
  }

  @Override
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceAbortGeneralViewGadget();
    root.add(generalGadget);
    
    detailGadget = new InvoiceAbortDetailViewGadget();
    root.add(detailGadget);
    root.add(drawRemarkGadget());
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceAbortDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceAbortDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }
}
