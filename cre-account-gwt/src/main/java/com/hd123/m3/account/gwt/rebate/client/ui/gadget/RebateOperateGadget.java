/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateOperateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.intf.client.RebateBillUrlParams;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 销售额返款单的状态与操作面板
 * @author chenganbang
 */
public class RebateOperateGadget extends RCaptionBox {
  
  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField causeField;
  private RSimpleOperateInfoField createInfo;
  private RSimpleOperateInfoField lastModifyInfo;
  private RHyperlink moreInfo;
  private BRebateBill entity;
  
  public RebateOperateGadget(boolean showMoreInfo){
    super();
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PRebateBillDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    causeField = new RViewStringField("原因");
    operateForm.addField(causeField);

    createInfo = new RSimpleOperateInfoField(PRebateBillDef.constants.createInfo());
    operateForm.addField(createInfo);

    lastModifyInfo = new RSimpleOperateInfoField(PRebateBillDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfo);

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(new HandlerClicker());
    moreInfo.setHTML(CommonsMessages.M.moreInfo());
    moreInfo.setVisible(showMoreInfo);
    operateForm.addField(moreInfo);

    operateForm.rebuild();

    setStyleName(RCaptionBox.STYLENAME_CONCISE);
    setCaption(CommonsMessages.M.operateInfo());
    setWidth("100%");
    setContent(operateForm);
    
  }
  
  private EPRebateBill getEP(){
    return EPRebateBill.getInstance();
  }
  
  public void setValue(BRebateBill bRebateBill){
    this.entity = bRebateBill;
    bizStateField.setValue(PRebateBillDef.bizState.getEnumCaption(entity.getBizState()));
    
    causeField.setValue(entity.getBpmMessage());
    causeField.setVisible(!StringUtil.isNullOrBlank(entity.getBpmMessage()));
    
    createInfo.setOperateInfo(entity.getCreateInfo());
    lastModifyInfo.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();
  }
  
  class HandlerClicker implements ClickHandler{
    @Override
    public void onClick(ClickEvent event) {
      gotoLogPage();
    }

    private void gotoLogPage() {
      JumpParameters params = new JumpParameters(RebateBillUrlParams.START_NODE_LOG);
      params.getUrlRef().set(RebateBillUrlParams.Base.PN_UUID, entity.getUuid());
      getEP().jump(params);
    }
  }
}
