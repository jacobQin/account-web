/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月8日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.instock.client.EPInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.gadget.InvoiceInstockDetailGadget;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.gadget.InvoiceInstockGeneralEditGadget;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.InvoiceInstockUrlParams.Edit;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.dd.PInvoiceInstockDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmEditPage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票入库单|编辑页面
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class InvoiceInstockEditPage extends BaseBpmEditPage2<BInvoiceInstock> implements Edit{
  private static InvoiceInstockEditPage instance = null;
  private Change_Handler changeHandler;
  private InvoiceInstockGeneralEditGadget generalGadget;
  private InvoiceInstockDetailGadget detailGadget;
  private RTextArea remarkField;
  private Widget remarkBox;
  
  public static InvoiceInstockEditPage getInstance() {
    if (instance == null) {
      instance = new InvoiceInstockEditPage();
    }
    return instance;
  }
  
  @Override
  protected EPInvoiceInstock getEP() {
    return EPInvoiceInstock.getInstance();
  }
  
  @Override
  protected void drawSelf(VerticalPanel root) {
    changeHandler = new Change_Handler();

    generalGadget = new InvoiceInstockGeneralEditGadget();
    root.add(generalGadget);
    
    detailGadget = new InvoiceInstockDetailGadget();
    root.add(detailGadget);
    
    remarkBox = drawRemarkGadget();
    root.add(remarkBox);
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceInstockDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addChangeHandler(changeHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceInstockDef.constants.remark());
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
