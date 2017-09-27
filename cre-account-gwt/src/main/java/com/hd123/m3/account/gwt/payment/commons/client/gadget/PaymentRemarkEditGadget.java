/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptRemarkEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-5 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.gadget;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;

/**
 * @author subinzhu
 * 
 */
public class PaymentRemarkEditGadget extends RCaptionBox {

  public PaymentRemarkEditGadget() {
    drawSelf();
    setEditing(true);
  }

  private RTextArea remarkField;
  private BPayment receive;

  public void setEntity(BPayment receive) {
    this.receive = receive;
  }

  public void refresh() {
    assert receive != null;

    remarkField.setValue(receive.getRemark());
  }

  private void drawSelf() {
    remarkField = new RTextArea(PPaymentDef.remark);
    remarkField.addValueChangeHandler(new Handler_textField());
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    setWidth("100%");
    setContent(remarkField);
    setCaption(PPaymentDef.constants.remark());
  }

  public boolean validate() {
    return remarkField.validate();
  }

  public List<Message> getInvalidMessages() {
    return remarkField.getInvalidMessages();
  }

  private class Handler_textField implements ValueChangeHandler<String> {

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
      receive.setRemark(event.getValue());
    }

  }
}
