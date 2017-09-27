/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateGeneralEditGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateInfoGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RebateLineGadget;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.RemarkGadget;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmEditPage2;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;

/**
 * 销售额返款单|编辑页面
 * 
 * @author chenganbang
 */
public class RebateBillEditPage extends BaseBpmEditPage2<BRebateBill> {
  private static RebateBillEditPage instance = null;

  private RebateBillEditPage() {
    super();
  }

  public static RebateBillEditPage getInstance() {
    if (instance == null) {
      instance = new RebateBillEditPage();
    }
    return instance;
  }
  
  private RebateGeneralEditGadget generalGadget;
  private RebateInfoGadget totalGadget;
  private RebateLineGadget lineGadget;
  private RemarkGadget remarkGadget;

  @Override
  protected EPRebateBill getEP() {
    return EPRebateBill.getInstance();
  }

  @Override
  protected void drawSelf(VerticalPanel root) {
    generalGadget = new RebateGeneralEditGadget();
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
  protected void refreshEntity() {
    generalGadget.setValue(entity);
    totalGadget.setValue(entity);
    lineGadget.setValue(entity, false);
    remarkGadget.setValue(entity);

    FocusOnFirstFieldUtil.focusOnFirstField(generalGadget);
  }
  
  @Override
  public void onHide() {
    if (lineGadget != null && lineGadget.getDialog() != null) {
      lineGadget.getDialog().clearConditions();
    }
  }

}
