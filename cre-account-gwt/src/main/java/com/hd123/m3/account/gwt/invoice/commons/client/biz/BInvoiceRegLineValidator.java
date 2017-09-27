/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BInvoiceRegLineValidator.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-26 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegLineDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * 发票登记单账款明细行验证器
 * 
 * @author chenpeisi
 * 
 */
public class BInvoiceRegLineValidator implements RValidatable {

  private BInvoiceReg entity;
  private HasFocusables owner;
  private List<Message> messages = new ArrayList<Message>();

  public BInvoiceRegLineValidator(BInvoiceReg entity, HasFocusables owner) {
    assert entity != null;
    this.entity = entity;
    this.owner = owner;
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

    boolean noRecord = true;

    for (int i = 0; i < entity.getLines().size(); i++) {
      BInvoiceRegLine line = entity.getLines().get(i);

      if (line.getAcc1().getSourceBill() == null
          || line.getAcc1().getSourceBill().getBillUuid() == null) {
        continue;
      }
      noRecord = false;

      if (line.getUnregTotal() == null || line.getUnregTotal().getTotal() == null
          || line.getUnregTotal().getTax() == null) {
        String msg = InvoiceRegMessage.M
            .notNull((i + 1), PInvoiceRegLineDef.constants.unregTotal());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegLine.FN_ACCOUNTTOTAL));
        messages.add(message);
      } else if (line.getRegTotal() == null || line.getRegTotal().getTotal() == null) {
        String msg = InvoiceRegMessage.M.notNull((i + 1),
            PInvoiceRegLineDef.constants.total_total());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegLine.FN_ACCOUNTTOTAL));
        messages.add(message);
      } else if (line.getRegTotal().getTotal().compareTo(BigDecimal.ZERO) == 0) {
        String msg = InvoiceRegMessage.M.mustGreaterThanZreo2((i + 1),
            PInvoiceRegLineDef.constants.total_total());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegLine.FN_ACCOUNTTOTAL));
        messages.add(message);
      } else if (line.getRegTotal().getTotal().compareTo(line.getUnregTotal().getTotal()) > 0) {
        String msg = InvoiceRegMessage.M.maxValue2((i + 1),
            PInvoiceRegLineDef.constants.total_total(),
            PInvoiceRegLineDef.constants.unregTotal_total());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegLine.FN_ACCOUNTTOTAL));
        messages.add(message);
      } else if (line.getRegTotal().getTotal().multiply(line.getUnregTotal().getTotal())
          .compareTo(BigDecimal.ZERO) < 0) {
        String msg = InvoiceRegMessage.M.inconsistent2((i + 1),
            PInvoiceRegLineDef.constants.total_total(),
            PInvoiceRegLineDef.constants.unregTotal_total());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegLine.FN_ACCOUNTTOTAL));
        messages.add(message);
      } else if (line.getRegTotal().getTax() == null) {
        String msg = InvoiceRegMessage.M.notNull((i + 1), PInvoiceRegLineDef.constants.total_tax());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegLine.FN_ACCOUNTTAX));
        messages.add(message);
      } else if (line.getRegTotal().getTax().compareTo(BigDecimal.ZERO) != 0
          && line.getRegTotal().getTax().compareTo(line.getUnregTotal().getTax()) > 0) {
        String msg = InvoiceRegMessage.M.maxValue2((i + 1),
            PInvoiceRegLineDef.constants.total_tax(),
            PInvoiceRegLineDef.constants.unregTotal_total());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegLine.FN_ACCOUNTTAX));
        messages.add(message);
      } else if (line.getRegTotal().getTax().multiply(line.getUnregTotal().getTax())
          .compareTo(BigDecimal.ZERO) < 0) {
        String msg = InvoiceRegMessage.M
            .inconsistent2((i + 1), PInvoiceRegLineDef.constants.total_tax(),
                PInvoiceRegLineDef.constants.unregTotal_tax());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegLine.FN_ACCOUNTTAX));
        messages.add(message);
      }
    }
    if (noRecord == true) {
      String msg = InvoiceRegMessage.M.addOneLeast() + InvoiceRegMessage.M.lineInfo();
      Message message = Message.error(msg);
      messages.add(message);
    }
    return isValid();
  }

  private InvoiceRegLocator getLocator(int lineIndex, String field) {
    Focusable f = owner != null ? owner.getFocusable(field) : null;
    return new InvoiceRegLocator(InvoiceRegLocator.lineType, lineIndex, f);
  }
}
