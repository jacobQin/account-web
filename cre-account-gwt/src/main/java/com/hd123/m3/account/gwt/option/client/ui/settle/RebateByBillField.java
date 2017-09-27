/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateByBillField.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月18日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui.settle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.option.client.BRebateByBillOption;
import com.hd123.m3.account.gwt.option.client.OptionMessages;
import com.hd123.m3.account.gwt.option.client.ui.OptionConstants;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 返款单是否按笔返款控件
 * @author chenganbang
 */
public class RebateByBillField extends RCombinedFlowField implements HasRActionHandlers {
  private RCheckBox yesField;
  private RViewStringField captionField;
  private RHyperlink deleteLink;
  
  private BRebateByBillOption value;
  
  public RebateByBillField (BRebateByBillOption value){
    assert value != null;
    
    this.value = value;
    setCaption("");
    
    drawSelf();
    
    addField(captionField);
    addHorizontalSpacing(4);
    addField(yesField);
    addHorizontalSpacing(4);
    addField(deleteLink);
  }
  
  private void drawSelf() {
    FieldHandlerChange handler = new FieldHandlerChange();

    captionField = new RViewStringField("");
    captionField.setWidth("100px");
    captionField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    captionField.setOverflowEllipsis(true);
    captionField.setValue(value.getStore().toFriendlyStr());

    yesField = new RCheckBox();
    yesField.addValueChangeHandler(handler);
    yesField.setText(OptionMessages.M.rebateByBill());
    yesField.setValue(value.isRebateByBill(), true);

    deleteLink = new RHyperlink();
    deleteLink.setHTML(OptionMessages.M.remove());
    deleteLink.addClickHandler(handler);
  }
  
  private class FieldHandlerChange implements ValueChangeHandler, ClickHandler {
    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (event.getSource() == yesField) {
        value.setRebateByBill(yesField.getValue());
      }
    }

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == deleteLink) {
        RActionEvent.fire(RebateByBillField.this, OptionConstants.ACTION_REMOVE_LINE, value);
      }
    }

  }
  
  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }
  
}
