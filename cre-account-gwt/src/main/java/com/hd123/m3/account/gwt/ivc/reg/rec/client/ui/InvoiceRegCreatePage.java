/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui;

import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget.InvoiceRegDetailEditGadget;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget.InvoiceRegGeneralCreateGadget;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegUrlParams;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmCreatePage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票登记单新建页面
 * 
 * @author chenrizhang
 * @since 1.0
 * 
 */
public class InvoiceRegCreatePage extends BaseBpmCreatePage2<BInvoiceReg> {
  private static InvoiceRegCreatePage instance = null;

  public static InvoiceRegCreatePage getInstance() {
    if (instance == null) {
      instance = new InvoiceRegCreatePage();
    }
    return instance;
  }

  private InvoiceRegGeneralCreateGadget generalGadget;
  private InvoiceRegDetailEditGadget detailGadget;
  private RTextArea remarkField;

  @Override
  public void onHide() {
    super.onHide();
    generalGadget.clearConditions();
    detailGadget.clearConditions();
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new InvoiceRegGeneralCreateGadget();
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
  protected EPInvoiceRegReceipt getEP() {
    return EPInvoiceRegReceipt.getInstance();
  }

  @Override
  protected void newEntity(JumpParameters params, AsyncCallback<BInvoiceReg> callback) {
    if (params.getUrlRef().get(InvoiceRegUrlParams.KEY_CREATE_BY_PAYMENT) != null) {
      InvoiceRegService.Locator.getService().createByPayment(
          params.getUrlRef().get(InvoiceRegUrlParams.KEY_CREATE_BY_PAYMENT), callback);
    } else if (params.getUrlRef().get(InvoiceRegUrlParams.KEY_CREATE_BY_STATEMENT) != null) {
      InvoiceRegService.Locator.getService().createByStatement(
          params.getUrlRef().get(InvoiceRegUrlParams.KEY_CREATE_BY_STATEMENT), callback);
    } else if (params.getUrlRef().get(InvoiceRegUrlParams.KEY_CREATE_BY_NOTICE) != null) {
      InvoiceRegService.Locator.getService().createByNotice(
          params.getUrlRef().get(InvoiceRegUrlParams.KEY_CREATE_BY_NOTICE), callback);
    } else {
      BInvoiceReg target = new BInvoiceReg();
      target.setDirection(DirectionType.receipt.getDirectionValue());
      target.setRegDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
      target.setBizState(BBizStates.INEFFECT);
      callback.onSuccess(target);
    }
  }

  @Override
  protected void refreshEntity() {
    entity.setAllowSplitReg(getEP().getOptions().isAllowSplitReg());
    entity.setUseInvoiceStock(getEP().getOptions().isUseInvoiceStock());
    if (entity.getAccountUnit() == null && getEP().getUserStores().size() == 1) {
      entity.setAccountUnit(new BUCN(getEP().getUserStores().get(0)));
    }
    
    if (StringUtil.isNullOrBlank(getEP().getDefaultInvoiceType()) == false) {
      entity.setInvoiceType(getEP().getDefaultInvoiceType());
    }
    generalGadget.setValue(entity);
    detailGadget.setValue(entity);
    remarkField.setValue(entity.getRemark());

    clearValidResults();
    FocusOnFirstFieldUtil.focusOnFirstField(generalGadget);
  }
}
