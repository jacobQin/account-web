/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月8日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.instock.client.EPInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.gadget.InvoiceInstockDetailViewGadget;
import com.hd123.m3.account.gwt.ivc.instock.client.ui.gadget.InvoiceInstockGeneralViewGadget;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.InvoiceInstockUrlParams.View;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.dd.PInvoiceInstockDef;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizAction;
import com.hd123.m3.commons.gwt.bpm.client.common.FAction;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmViewPage2;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票入库单|查看页面
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class InvoiceInstockViewPage extends BaseBpmViewPage2<BInvoiceInstock> implements View {
  private static InvoiceInstockViewPage instance = null;
  private InvoiceInstockGeneralViewGadget generalGadget;
  private InvoiceInstockDetailViewGadget detailGadget;
  private RTextArea remarkField;

  public static InvoiceInstockViewPage getInstance() {
    if (instance == null) {
      instance = new InvoiceInstockViewPage();
    }
    return instance;
  }

  @Override
  protected EPInvoiceInstock getEP() {
    return EPInvoiceInstock.getInstance();
  }

  @Override
  protected void refreshEntity() {
    if (entity.getAccountUnit() == null) {
      entity.setAccountUnit(new BUCN());
    }
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());
  }

  @Override
  protected void refreshToolButtons() {
    for (FAction action : actions) {
      BBizAction act = action.getBizAction();
      action.setEnabled(isPermitted(act));
      action.setVisible(act.getSrcStates().indexOf(entity.getBizState()) >= 0);
    }
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceInstockGeneralViewGadget();
    root.add(generalGadget);

    detailGadget = new InvoiceInstockDetailViewGadget();
    root.add(detailGadget);
    root.add(drawRemarkGadget());
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setFieldDef(PInvoiceInstockDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceInstockDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

}
