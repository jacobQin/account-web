/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillCreatePage.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateGeneralCreateGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateInfoGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateLineGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RemarkGadget;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmCreatePage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 销售额返款单|新建页面
 * 
 * @author chenganbang
 */
public class RebateBillCreatePage extends BaseBpmCreatePage2<BRebateBill> {

  private static RebateBillCreatePage instance = null;

  public static RebateBillCreatePage getInstance() {
    if (instance == null) {
      instance = new RebateBillCreatePage();
    }
    return instance;
  }

  private RebateGeneralCreateGadget generalGadget;
  private RebateInfoGadget totalGadget;
  private RebateLineGadget lineGadget;
  private RemarkGadget remarkGadget;

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new RebateGeneralCreateGadget();
    root.add(generalGadget);

    totalGadget = new RebateInfoGadget();
    totalGadget.setEditing(true);
    root.add(totalGadget);

    lineGadget = new RebateLineGadget();
    lineGadget.setEditing(true);
    generalGadget.addActionHandler(lineGadget);
    lineGadget.addActionHandler(totalGadget);
    root.add(lineGadget);

    remarkGadget = new RemarkGadget();
    remarkGadget.setEditing(true);
    root.add(remarkGadget);
  }

  @Override
  protected EPRebateBill getEP() {
    return EPRebateBill.getInstance();
  }

  @Override
  protected void newEntity(JumpParameters params, AsyncCallback<BRebateBill> callback) {
    BRebateBill entity = new BRebateBill();
    entity.setAccountDate(RDateUtil.truncate(new Date(), RDateUtil.DAY_OF_MONTH));
    entity.setBizState(BBizStates.INEFFECT);
    entity.setSingle(false);
    callback.onSuccess(entity);
  }

  @Override
  protected void refreshEntity() {
    if (entity.getStore() == null && getEP().getUserStores().size() == 1) {
      entity.setStore(new BUCN(getEP().getUserStores().get(0)));
    }
    generalGadget.setValue(entity);
    totalGadget.setValue(entity);
    lineGadget.setValue(entity, false);
    remarkGadget.setValue(entity);

    clearValidResults();
    FocusOnFirstFieldUtil.focusOnFirstField(generalGadget);
  }

  @Override
  public void onHide() {
    generalGadget.clearConditions();
    if (lineGadget != null && lineGadget.getDialog() != null) {
      lineGadget.getDialog().clearConditions();
    }
  }

}
