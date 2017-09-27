/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentDefrayalValidator.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentHasFocusables;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCashDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDepositDefrayalDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptConfig;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RAbstractField;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * 收付款单按总额收款时单头收付信息验证器。
 * 
 * @author subinzhu
 * 
 */
public class BPaymentDefrayalValidator2 implements RValidatable {

  private List<Message> messages = new ArrayList<Message>();
  private BPayment entity;
  private PaymentHasFocusables owner;
  private BReceiptConfig config = EPReceipt.getInstance().getConfig();

  public BPaymentDefrayalValidator2(BPayment entity, PaymentHasFocusables owner) {
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
    validateCashDefrayal();
    validateBankNull();
    validateDepositDefrayal();
    return isValid();
  }

  /** 验证总的实收明细 */
  private void validateCashDefrayal() {
    String caption = PPaymentDefrayalDef.TABLE_CAPTION + "/"
        + PPaymentCashDefrayalDef.constants.tableCaption();
    if (config.getReceiptPaymentType() != null) {
      BigDecimal total = BigDecimal.ZERO;
      for (BPaymentCashDefrayal cash : entity.getCashs()) {
        if (cash.getPaymentType().equals(config.getReceiptPaymentType())) {
          total = total.add(cash.getTotal());
        }
      }
      if (total.abs().compareTo(config.getReceiptPaymentTypeLimit()) > 0) {
        Message message = Message.error(PPaymentDefrayalDef.TABLE_CAPTION
            + ":"
            + ReceiptMessages.M.errorTotalPaymentLimit(config.getReceiptPaymentType()
                .toFriendlyStr(), config.getReceiptPaymentTypeLimit()));
        messages.add(message);
      }
    }
    if (entity.getUnpayedTotal() != null
        && entity.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0
        && entity.getDefrayalTotal() != null
        && entity.getDefrayalTotal().compareTo(BigDecimal.ZERO) < 0) {
      Message message = Message.error(PPaymentDefrayalDef.TABLE_CAPTION + ":"
          + ReceiptMessages.M.defrayalTotalZero_error());
      messages.add(message);
    }

    if (entity.getUnpayedTotal() != null
        && entity.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) < 0
        && entity.getDefrayalTotal() != null
        && (entity.getDefrayalTotal().compareTo(BigDecimal.ZERO) > 0 || entity.getDefrayalTotal()
            .compareTo(entity.getUnpayedTotal().getTotal()) < 0)) {

      String msg = ReceiptMessages.M.totalBetween(entity.getUnpayedTotal().getTotal().toString());

      if (entity.getCollectionLines().isEmpty() == false) {
        for (BPaymentCollectionLine line : entity.getCollectionLines()) {
          if (line.getDefrayalTotal() != null
              && BigDecimal.ZERO.compareTo(line.getDefrayalTotal()) != 0) {
            msg = ReceiptMessages.M.existCollectionLines();
            break;
          }
        }
      }

      Message message = Message.error(PPaymentDefrayalDef.TABLE_CAPTION + ":" + msg);
      messages.add(message);
    }

    for (int i = 0; i < entity.getCashs().size(); i++) {
      BPaymentCashDefrayal cash = entity.getCashs().get(i);
      PaymentDefrayalLineLocator locator = getLocator(i, BPaymentCashDefrayal.FN_CASHDEFRAYALTOTAL);
      if (cash.getPaymentType() != null
          && !StringUtil.isNullOrBlank(cash.getPaymentType().getUuid())) {
        if (cash.getTotal() != null
            && cash.getTotal().compareTo(BoundaryValue.BIGDECIMAL_MAXVALUE_S2) > 0) {
          String msg = CommonMessages.M.cannotMoreThan2(caption, (i + 1),
              PPaymentDepositDefrayalDef.constants.total(),
              BoundaryValue.BIGDECIMAL_MAXVALUE_S2.toString());
          Message message = Message.error(msg, locator);
          messages.add(message);

          if (locator.getField() instanceof RAbstractField) {
            ((RAbstractField) locator.getField()).addErrorMessage(CommonMessages.M.cannotMoreThan(
                PPaymentDepositDefrayalDef.constants.total(),
                BoundaryValue.BIGDECIMAL_MAXVALUE_S2.toString()));
          }
        }
      }
      if (entity.getUnpayedTotal() != null && entity.getUnpayedTotal().getTotal() != null
          && entity.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) >= 0
          && cash.getPaymentType() != null
          && !StringUtil.isNullOrBlank(cash.getPaymentType().getUuid())) {
        if (cash.getTotal() != null && cash.getTotal().compareTo(BigDecimal.ZERO) < 0
            && !cash.getPaymentType().equals(config.getReceiptPaymentType())) {
          String msg = ReceiptMessages.M.cannotLessThan2(caption, (i + 1),
              PPaymentDepositDefrayalDef.constants.total(), BigDecimal.ZERO.toString());
          Message message = Message.error(msg, locator);
          messages.add(message);
        }
      }

    }

  }

  /**
   * 验证银行是否为空。
   * <p>
   * 银行不允许为null的条件：</br> 1、config.isBankRequired()=true;</br> 2、收款信息/实收明细行金额不为0。
   **/
  private void validateBankNull() {

    String caption = PPaymentDefrayalDef.TABLE_CAPTION + "/" + ReceiptMessages.M.bank();
    if (config.isBankRequired() == false) {
      return;
    }

    for (int i = 0; i < entity.getCashs().size(); i++) {
      PaymentDefrayalLineLocator locator = getLocator(i, BPaymentCashDefrayal.FN_BANK);
      BPaymentCashDefrayal cash = entity.getCashs().get(i);

      if (cash.getTotal() == null || cash.getTotal().compareTo(BigDecimal.ZERO) == 0) {
        continue;
      }
      
      if (cash.getBank() == null || StringUtil.isNullOrBlank(cash.getBank().getCode())) {
        String msg = ReceiptMessages.M.lineNotNull((i + 1), caption);
        Message message = Message.error(msg, locator);
        messages.add(message);
      }

    }
  }

  private void validateDepositDefrayal() {
    String caption = PPaymentDefrayalDef.TABLE_CAPTION + "/"
        + PPaymentDepositDefrayalDef.constants.tableCaption();
    for (int i = 0; i < entity.getDeposits().size(); i++) {
      BPaymentDepositDefrayal deposit = entity.getDeposits().get(i);
      PaymentDefrayalLineLocator locator = getLocator(i,
          BPaymentDepositDefrayal.FN_DEPOSITDEFRAYALTOTAL);
      if (entity.getUnpayedTotal() != null && entity.getUnpayedTotal().getTotal() != null
          && deposit.getTotal() != null
          && entity.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) < 0
          && deposit.getTotal().compareTo(BigDecimal.ZERO) > 0) {
        String msg = ReceiptMessages.M.indexError(caption, i + 1, ReceiptMessages.M.noDeposit());
        Message message = Message.error(msg, locator);
        messages.add(message);
      }
    }
  }

  private PaymentDefrayalLineLocator getLocator(int lineIndex, String field) {
    Focusable f = owner != null ? owner.getFocusable(lineIndex, field) : null;
    return new PaymentDefrayalLineLocator(lineIndex, f);
  }
}
