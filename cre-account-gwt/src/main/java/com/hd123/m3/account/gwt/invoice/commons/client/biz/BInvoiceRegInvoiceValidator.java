/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BInvoiceRegInvoiceVallidator.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-26 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.biz;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegInvoiceDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * 发票登记单发票明细行验证器
 * 
 * @author chenpeisi
 * 
 */
public class BInvoiceRegInvoiceValidator implements RValidatable {
  private BInvoiceReg entity;
  private HasFocusables owner;
  private List<Message> messages = new ArrayList<Message>();

  public BInvoiceRegInvoiceValidator(BInvoiceReg entity, HasFocusables owner) {
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

    for (int i = 0; i < entity.getInvoices().size(); i++) {
      BInvoiceRegInvoice invoice = entity.getInvoices().get(i);

      if (invoice.getInvoiceNumber() == null || "".equals(invoice.getInvoiceNumber()))
        continue;

      noRecord = false;

      for (int j = i + 1; j < entity.getInvoices().size(); j++) {
        if (entity.getInvoices().get(j).getInvoiceNumber() != null
            && invoice.getInvoiceNumber().equals(entity.getInvoices().get(j).getInvoiceNumber())) {
          Message message = Message.error(InvoiceRegMessage.M.duplicate((i + 1), (j + 1)));
          messages.add(message);
        }
      }

      if (invoice.getInvoiceNumber() == null) {
        String msg = InvoiceRegMessage.M.notNull((i + 1),
            PInvoiceRegInvoiceDef.constants.invoiceNumber());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegInvoice.PN_INVOICENUMBER));
        messages.add(message);
      }

      if (invoice.getInvoiceDate() == null) {
        String msg = InvoiceRegMessage.M.notNull((i + 1),
            PInvoiceRegInvoiceDef.constants.invoiceDate());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegInvoice.PN_INVOICEDATE));
        messages.add(message);
      }

      if (invoice.getTotal() == null || invoice.getTotal().getTotal() == null) {
        String msg = InvoiceRegMessage.M.notNull((i + 1),
            PInvoiceRegInvoiceDef.constants.total_total());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegInvoice.PN_INVOICETOTAL));
        messages.add(message);
      } else if (invoice.getTotal().getTax() == null) {
        String msg = InvoiceRegMessage.M.notNull((i + 1),
            PInvoiceRegInvoiceDef.constants.total_tax());
        Message message = Message.error(msg, getLocator(i, BInvoiceRegInvoice.PN_INVOICETAX));
        messages.add(message);
      }
    }
    if (noRecord == true) {
      String msg = InvoiceRegMessage.M.addOneLeast() + InvoiceRegMessage.M.invoiceInfo();
      Message message = Message.error(msg);
      messages.add(message);
    }
    return isValid();
  }

  private InvoiceRegLocator getLocator(int lineIndex, String field) {
    Focusable f = owner != null ? owner.getFocusable(field) : null;
    return new InvoiceRegLocator(InvoiceRegLocator.invoiceType, lineIndex, f);
  }
}
