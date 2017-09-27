/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentValidator.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * @author subinzhu
 * 
 */
public class BPaymentValidator implements RValidatable {

  private List<Message> messages = new ArrayList<Message>();
  private BPayment entity;

  public BPaymentValidator(BPayment entity) {
    assert entity != null;
    this.entity = entity;
  }

  @Override
  public void clearValidResults() {
    messages.clear();
  }

  @Override
  public boolean isValid() {
    return messages.isEmpty();
  }

  @Override
  public List<Message> getInvalidMessages() {
    return messages;
  }

  @Override
  public boolean validate() {
    clearValidResults();

    if (entity.getAccountLines().isEmpty() && entity.getOverdueLines().isEmpty()) {
      Message message = Message.error(PaymentMessages.M.notNull(PPaymentLineDef.TABLE_CAPTION));
      messages.add(message);
      return false;
    }

    if (entity.getDefrayalTotal().multiply(entity.getTotal().getTotal()).compareTo(BigDecimal.ZERO) < 0) {
      Message message = Message.error(PaymentMessages.M.signSymbolMustBeSameAs3(
          PaymentMessages.M.defrayalTotal(), PPaymentDef.constants.total_total()));
      messages.add(message);
    } else if (entity.getDefrayalTotal().compareTo(entity.getTotal().getTotal()) < 0) {
      Message message = Message.error(PaymentMessages.M.cannotLessThan(
          PaymentMessages.M.defrayalTotal(), PPaymentDef.constants.total_total()));
      messages.add(message);
    } else if (entity.getTotal() != null
        && entity.getTotal().getTotal().compareTo(BigDecimal.ZERO) < 0) {
      Message message = Message.error(PaymentMessages.M.cannotLessThan(
          PaymentMessages.M.total_total(), "0"));
      messages.add(message);
    }
    return isValid();
  }
}
