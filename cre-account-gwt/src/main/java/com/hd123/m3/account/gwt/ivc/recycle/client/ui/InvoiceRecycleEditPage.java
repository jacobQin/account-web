/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月11日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.recycle.client.EPInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.gadget.InvoiceRecycleDetailGadget;
import com.hd123.m3.account.gwt.ivc.recycle.client.ui.gadget.InvoiceRecycleGeneralEditGadget;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.dd.PInvoiceRecycleDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmEditPage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票回收单|编辑页面
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceRecycleEditPage extends BaseBpmEditPage2<BInvoiceRecycle>{
  private static InvoiceRecycleEditPage instance = null;
  private Change_Handler changeHandler;
  private InvoiceRecycleGeneralEditGadget generalGadget;
  private InvoiceRecycleDetailGadget detailGadget;
  private RTextArea remarkField;
  private Widget remarkBox;
  
  public static InvoiceRecycleEditPage getInstance() {
    if (instance == null) {
      instance = new InvoiceRecycleEditPage();
    }
    return instance;
  }
  
  @Override
  protected EPInvoiceRecycle getEP() {
    return EPInvoiceRecycle.getInstance();
  }
  
  @Override
  protected void drawSelf(VerticalPanel root) {
    changeHandler = new Change_Handler();

    generalGadget = new InvoiceRecycleGeneralEditGadget();
    root.add(generalGadget);
    
    detailGadget = new InvoiceRecycleDetailGadget();
    root.add(detailGadget);
    
    generalGadget.addActionHandler(detailGadget);
    
    remarkBox = drawRemarkGadget();
    root.add(remarkBox);
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceRecycleDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.addChangeHandler(changeHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceRecycleDef.constants.remark());
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
