/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.payment.pay.client.ui.page.PaymentCreatePage;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateGeneralViewGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateInfoGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateLineGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RemarkGadget;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmViewPage2;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 销售额返款单|查看页面
 * 
 * @author chenganbang
 */
public class RebateBillViewPage extends BaseBpmViewPage2<BRebateBill> {
  private static RebateBillViewPage instance = null;

  public static RebateBillViewPage getInstance() {
    if (instance == null) {
      instance = new RebateBillViewPage();
    }
    return instance;
  }

  private RebateBillViewPage() {
    super();
    drawToolBar();
  }

  private RAction payAction;
  private RebateGeneralViewGadget generalGadget;
  private RebateInfoGadget infoGadget;
  private RebateLineGadget lineGadget;
  private RemarkGadget remarkGadget;

  protected void drawToolBar() {
    payAction = new RAction("立即返款", new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        GwtUrl url = PaymentUrlParams.ENTRY_URL;
        url.getRef().set(JumpParameters.PN_START, PaymentUrlParams.Create.START_NODE);
        url.getRef().set(PaymentCreatePage.PN_CREATEBYSOURCEBILL, entity.getUuid());
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
      }
    });
    getToolbar().add(new RToolbarButton(payAction));
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new RebateGeneralViewGadget();
    root.add(generalGadget);

    infoGadget = new RebateInfoGadget();
    root.add(infoGadget);

    lineGadget = new RebateLineGadget();
    root.add(lineGadget);

    remarkGadget = new RemarkGadget();
    remarkGadget.setEditing(false);
    root.add(remarkGadget);
  }

  @Override
  protected EPBpmModule2 getEP() {
    return EPRebateBill.getInstance();
  }

  @Override
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    infoGadget.setValue(entity);
    lineGadget.setValue(entity, true);
    remarkGadget.setValue(entity);
  }

  @Override
  protected void refreshToolButtons() {
    super.refreshToolButtons();

    payAction.setVisible(entity.isHasAccount());
    payAction.setEnabled(getEP().isPermitted(PaymentPermDef.CREATE));
    getToolbar().minimizeSeparators();
  }
}
