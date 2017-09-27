/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RemarkGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * 说明面板
 * 
 * @author chenganbang
 */
public class RemarkGadget extends RCaptionBox {
  private RTextArea remarkField;
  private BRebateBill entity;

  public RemarkGadget() {
    super();
    remarkField = new RTextArea(PRebateBillDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setRemark(event.getValue());
      }
    });

    setCaption(PRebateBillDef.constants.remark());
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);
    setContent(remarkField);
  }

  @Override
  public void setEditing(boolean editing) {
    super.setEditing(editing);
    remarkField.setReadOnly(!editing);
  }

  public void setValue(BRebateBill entity) {
    this.entity = entity;
    if (entity != null) {
      remarkField.setValue(entity.getRemark());
    }
  }
}
