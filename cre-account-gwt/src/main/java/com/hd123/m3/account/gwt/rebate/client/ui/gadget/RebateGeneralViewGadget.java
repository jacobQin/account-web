/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractLinkField;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewDateField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;

/**
 * 销售额返款单查看页面的概要面板
 * @author chenganbang
 */
public class RebateGeneralViewGadget extends RCaptionBox {
  
  private EPRebateBill getEP(){
    return EPRebateBill.getInstance();
  }
  
  private RViewStringField codeField;//单号
  private RViewStringField storeField;//项目
  private RViewStringField counterpartField;//商户
  private ContractLinkField contractField;//合同
  private RViewStringField positionField;//铺位编号
  private RViewDateField accountDateField;//记账日期
  private RViewDateField beginDateField;//起始日期
  private RViewDateField endDateField;//截止日期
  
  private RebateOperateGadget operateGadget;//状态与操作
  private PermGroupViewField permViewField;//授权组
  
  public RebateGeneralViewGadget(){
    super();
    setCaption(CommonsMessages.M.generalInfo());
    setWidth("100%");
    setEditing(false);
    getCaptionBar().setShowCollapse(true);
    
    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    
    mvp.add(0, drawLeftInfo());
    mvp.add(1, drawRightInfo());// 根据是否授权动态添加
    
    setContent(mvp);
  }
  
  public void setValue(BRebateBill entity){
    if (entity != null) {
      codeField.setValue(entity.getBillNumber());
      storeField.setValue(entity.getStore().toFriendlyStr());
      counterpartField.setValue(entity.getTenant().toFriendlyStr());
      contractField.setRawValue(entity.getContract());
      positionField.setValue(entity.getPositions());
      accountDateField.setValue(entity.getAccountDate());
      beginDateField.setValue(entity.getBeginDate());
      endDateField.setValue(entity.getEndDate());
      permViewField.refresh(getEP().isPermEnabled(), entity);
      
      operateGadget.setValue(entity);
    }
  }

  /**
   * 状态与操作|授权
   * @return
   */
  private Widget drawRightInfo() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);

    operateGadget = new RebateOperateGadget(true);
    root.add(operateGadget);
    
    permViewField = new PermGroupViewField();
    permViewField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    root.add(permViewField);// 根据是否授权动态添加
    
    return root;
  }

  /**
   * 基本信息
   * @return
   */
  private Widget drawLeftInfo() {
    RForm form = new RForm(1);
    form.setWidth("100%");
    
    codeField = new RViewStringField(PRebateBillDef.constants.billNumber());
    codeField.addTextStyleName(RTextStyles.STYLE_BOLD);
    form.addField(codeField);
    
    storeField = new RViewStringField(PRebateBillDef.constants.store());
    form.addField(storeField);
    
    counterpartField = new RViewStringField(PRebateBillDef.constants.tenant());
    form.addField(counterpartField);
    
    contractField = new ContractLinkField();
    form.addField(contractField);
    
    positionField = new RViewStringField(PRebateBillDef.constants.positions());
    form.addField(positionField);
    
    accountDateField = new RViewDateField(PRebateBillDef.constants.accountDate());
    form.addField(accountDateField);
    
    beginDateField = new RViewDateField(PRebateBillDef.constants.beginDate());
    form.addField(beginDateField);
    
    endDateField = new RViewDateField(PRebateBillDef.constants.endDate());
    form.addField(endDateField);
    
    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(CommonsMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }
}
