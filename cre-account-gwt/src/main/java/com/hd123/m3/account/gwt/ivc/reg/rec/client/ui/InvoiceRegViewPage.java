/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget.InvoiceRegDetailViewGadget;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget.InvoiceRegGeneralViewGadget;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegPermDef;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegUrlParams;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpm2Message;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmViewPage2;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票登记单|查看页面
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class InvoiceRegViewPage extends BaseBpmViewPage2<BInvoiceReg> {
  private static InvoiceRegViewPage instance = null;

  public static InvoiceRegViewPage getInstance() {
    if (instance == null) {
      instance = new InvoiceRegViewPage();
    }
    return instance;
  }

  private RAction openInvoiceAction;
  private InvoiceRegGeneralViewGadget generalGadget;
  private InvoiceRegDetailViewGadget detailGadget;
  private RTextArea remarkField;

  @Override
  protected EPInvoiceRegReceipt getEP() {
    return EPInvoiceRegReceipt.getInstance();
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceRegGeneralViewGadget();
    root.add(generalGadget);

    detailGadget = new InvoiceRegDetailViewGadget();
    root.add(detailGadget);

    root.add(drawRemarkGadget());
  }

  @Override
  protected void drawToolbar() {
    super.drawToolbar();
    openInvoiceAction = new RAction("开发票", new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        RLoadingDialog.show("正在开票...");
        InvoiceRegService.Locator.getService().print(entity.getUuid(), entity.getVersion(),
            new AsyncCallback<Void>() {

              @Override
              public void onSuccess(Void result) {
                // 开票成功，跳转到查看页面
                RLoadingDialog.hide();
                getEP().jumpToViewPage(entity.getUuid(),
                    Message.info("开票成功"));
              }

              @Override
              public void onFailure(Throwable caught) {
                RLoadingDialog.hide();
                RMsgBox.showError("开发票失败");
              }
            });
      }
    });
    getToolbar().add(new RToolbarButton(openInvoiceAction));
  }

  @Override
  public void refreshToolbar(RToolbar toolbar) {
    super.refreshToolbar(toolbar);
    openInvoiceAction.setVisible(entity.isPrinted() == false
        && getEP().getEnabledExtInvoiceSystem() == true && entity.getBizState().equals("effect"));
    openInvoiceAction.setEnabled(getEP().isPermitted(InvoiceRegPermDef.CREATE));
    getToolbar().minimizeSeparators();
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea();
    remarkField.setWidth("100%");
    remarkField.setReadOnly(true);
    remarkField.setFieldDef(PInvoiceRegDef.remark);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInvoiceRegDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());
  }

}
