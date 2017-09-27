/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BReceiptValidator.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineLocator;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * @author subinzhu
 * 
 */
public class BReceiptValidator implements RValidatable {

  private List<Message> messages = new ArrayList<Message>();
  private BPayment entity;
  boolean validateLine = true;

  public BReceiptValidator(BPayment entity, boolean validateLine) {
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
    boolean isCollectionIsEmpty = true;
    for (BPaymentCollectionLine line : entity.getCollectionLines()) {
      if (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) > 0) {
        isCollectionIsEmpty = false;
        break;
      }
    }
    if (entity.getAccountLines().isEmpty() && entity.getOverdueLines().isEmpty()
        && isCollectionIsEmpty) {
      Message message = Message.error(ReceiptMessages.M.notNull(PPaymentLineDef.TABLE_CAPTION));
      messages.add(message);
      return false;
    }

    if (entity.getDefrayalTotal().multiply(entity.getTotal().getTotal()).compareTo(BigDecimal.ZERO) < 0) {
      Message message = Message.error(ReceiptMessages.M.signSymbolMustBeSameAs3(
          PPaymentDef.constants.defrayalTotal(), PPaymentDef.constants.total_total()));
      messages.add(message);
    }

    if (validateLine) {
      validateAccountLines();
      validateOverdueLines();
    }
    return isValid();
  }

  /** 验证账款明细 */
  private void validateAccountLines() {
    for (int i = 0; i < entity.getAccountLines().size(); i++) {
      BPaymentAccountLine line = entity.getAccountLines().get(i);

      Message message = null;
      if (line.getTotal() == null || line.getTotal().getTotal() == null) {
        message = Message.error(ReceiptMessages.M.lineNotNull2(
            PPaymentAccountLineDef.TABLE_CAPTION, (i + 1),
            PPaymentAccountLineDef.constants.total_total()));
        messages.add(message);
      } else if (line.getUnpayedTotal() != null
          && line.getUnpayedTotal().getTotal() != null
          && (line.getTotal().getTotal().multiply(line.getUnpayedTotal().getTotal())
              .compareTo(BigDecimal.ZERO) < 0)) {
        if (CPaymentDefrayalType.line.equals(entity.getDefrayalType())) {
          if (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) > 0) {
            message = Message.error(ReceiptMessages.M.signSymbolMustBeSameAs2(
                PPaymentAccountLineDef.TABLE_CAPTION, (i + 1),
                PPaymentAccountLineDef.constants.total_total(),
                PPaymentAccountLineDef.constants.unpayedTotal_total()));
            messages.add(message);
          }
        }
      } else if ((line.getTotal().getTotal().abs()
          .compareTo(line.getUnpayedTotal().getTotal().abs()) > 0)) {
        message = Message.error(ReceiptMessages.M.absCannotMoreThan2(
            PPaymentAccountLineDef.TABLE_CAPTION, (i + 1),
            PPaymentAccountLineDef.constants.total_total(),
            PPaymentAccountLineDef.constants.unpayedTotal_total()));
        messages.add(message);
      }

      if (CPaymentDefrayalType.bill.equals(entity.getDefrayalType())) {
        if (line.getUnpayedTotal().getTotal() != null
            && BigDecimal.ZERO.compareTo(line.getUnpayedTotal().getTotal()) <= 0
            && line.getTotal().getTotal() != null
            && line.getDefrayalTotal().compareTo(line.getTotal().getTotal()) < 0) {
          message = Message.error(ReceiptMessages.M.cannotMoreThan2(
              PPaymentAccountLineDef.TABLE_CAPTION, (i + 1),
              PPaymentAccountLineDef.constants.total_total(),
              PPaymentAccountLineDef.constants.defrayalTotal()));
          messages.add(message);
        }
      }

      PaymentLineLocator locator = getAccountLocator(PaymentLineLocator.ACCOUNTTAB_ID, i,
          BPaymentAccountLine.FN_ACCOUNTLINE_REMARK);
      if (!StringUtil.isNullOrBlank(line.getRemark())
          && line.getRemark().length() > PPaymentAccountLineDef.remark.getMaxLength()) {
        message = Message.error(ReceiptMessages.M.cannotMoreThan2(
            PPaymentAccountLineDef.TABLE_CAPTION, (i + 1),
            PPaymentAccountLineDef.constants.remark() + ReceiptMessages.M.length(),
            String.valueOf(PPaymentAccountLineDef.remark.getMaxLength())), locator);
        messages.add(message);
      }
    }
  }

  /** 验证滞纳金明细 */
  private void validateOverdueLines() {
    for (int i = 0; i < entity.getOverdueLines().size(); i++) {
      BPaymentOverdueLine line = entity.getOverdueLines().get(i);

      Message message = null;
      PaymentLineLocator locator = getOverdueLocator(PaymentLineLocator.OVERDUETAB_ID, i,
          BPaymentOverdueLine.FN_OVERDUELINE_TOTAL);
      if (line.getTotal() == null || line.getTotal().getTotal() == null) {
        message = Message.error(ReceiptMessages.M.lineNotNull2(
            PPaymentOverdueLineDef.TABLE_CAPTION, (i + 1),
            PPaymentOverdueLineDef.constants.overdueTotal_total()), locator);
        messages.add(message);
      } else if (line.getUnpayedTotal() != null
          && line.getUnpayedTotal().getTotal() != null
          && (line.getUnpayedTotal().getTotal().multiply(line.getTotal().getTotal())
              .compareTo(BigDecimal.ZERO) < 0)) {
        message = Message.error(ReceiptMessages.M.signSymbolMustBeSameAs2(
            PPaymentOverdueLineDef.TABLE_CAPTION, (i + 1),
            PPaymentOverdueLineDef.constants.overdueTotal_total(),
            ReceiptMessages.M.calculate_total()), locator);
        messages.add(message);
      } else if (line.getUnpayedTotal() != null
          && line.getUnpayedTotal().getTotal() != null
          && (line.getTotal().getTotal().abs().compareTo(line.getUnpayedTotal().getTotal().abs()) > 0)) {
        message = Message.error(ReceiptMessages.M.absCannotMoreThan2(
            PPaymentOverdueLineDef.TABLE_CAPTION, (i + 1),
            PPaymentOverdueLineDef.constants.overdueTotal_total(),
            ReceiptMessages.M.calculate_total()), locator);
        messages.add(message);
      }

      locator = getOverdueLocator(PaymentLineLocator.OVERDUETAB_ID, i,
          BPaymentOverdueLine.FN_OVERDUELINE_REMARK);
      if (!StringUtil.isNullOrBlank(line.getRemark())
          && line.getRemark().length() > PPaymentAccountLineDef.remark.getMaxLength()) {
        locator = getOverdueLocator(PaymentLineLocator.OVERDUETAB_ID, i,
            BPaymentOverdueLine.FN_OVERDUELINE_REMARK);
        message = Message.error(ReceiptMessages.M.cannotMoreThan2(
            PPaymentOverdueLineDef.TABLE_CAPTION, (i + 1),
            PPaymentOverdueLineDef.constants.remark() + ReceiptMessages.M.length(),
            String.valueOf(PPaymentOverdueLineDef.remark.getMaxLength())), locator);
        messages.add(message);
      }

    }
  }

  private PaymentLineLocator getAccountLocator(int tabId, int lineNumber, String fieldId) {
    return new PaymentLineLocator(tabId, lineNumber, fieldId);
  }

  private PaymentLineLocator getOverdueLocator(int tabId, int lineNumber, String fieldId) {
    return new PaymentLineLocator(tabId, lineNumber, fieldId);
  }
}
