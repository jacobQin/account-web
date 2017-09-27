/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget.InvoiceRegDetailEditGadget;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget.InvoiceRegGeneralEditGadget;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmEditPage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 发票登记单|编辑页面
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class InvoiceRegEditPage extends BaseBpmEditPage2<BInvoiceReg> {
  private static InvoiceRegEditPage instance = null;

  public static InvoiceRegEditPage getInstance() {
    if (instance == null) {
      instance = new InvoiceRegEditPage();
    }
    return instance;
  }

  private InvoiceRegGeneralEditGadget generalGadget;
  private InvoiceRegDetailEditGadget detailGadget;
  private RTextArea remarkField;

  @Override
  protected EPInvoiceRegReceipt getEP() {
    return EPInvoiceRegReceipt.getInstance();
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceRegGeneralEditGadget();
    root.add(generalGadget);

    detailGadget = new InvoiceRegDetailEditGadget();
    root.add(detailGadget);

    detailGadget.addActionHandler(generalGadget);
    generalGadget.addActionHandler(detailGadget);

    root.add(drawRemarkGadget());
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setWidth("100%");
    remarkField.setFieldDef(PInvoiceRegDef.remark);
    remarkField.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        entity.setRemark(remarkField.getValue());
      }
    });

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceRegDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());

    clearValidResults();
    FocusOnFirstFieldUtil.focusOnFirstField(generalGadget, detailGadget);
  }

  @Override
  public void onHide() {
    super.onHide();
    generalGadget.clearConditions();
    detailGadget.clearConditions();
  }
}
