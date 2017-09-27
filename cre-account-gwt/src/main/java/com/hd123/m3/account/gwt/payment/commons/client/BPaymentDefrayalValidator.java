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
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentHasFocusables;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCashDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDepositDefrayalDef;
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
public class BPaymentDefrayalValidator implements RValidatable {

  private List<Message> messages = new ArrayList<Message>();
  private BPayment entity;
  private PaymentHasFocusables owner;

  public BPaymentDefrayalValidator(BPayment entity, PaymentHasFocusables owner) {
    assert entity != null;
    assert CPaymentDefrayalType.bill.equals(entity.getDefrayalType());
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
    validateBillDepositDefrayal();
    return isValid();
  }

  /** 验证总的实收明细 */
  private void validateCashDefrayal() {
    String caption = DirectionType.receipt.getDirectionValue() == entity.getDirection() ? PPaymentDefrayalDef.TABLE_CAPTION
        + "/" + PPaymentCashDefrayalDef.constants.tableCaption()
        : CommonMessages.M.paymentInfo() + "/" + CommonMessages.M.defrayal();

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
          continue;
        }
      }
    }

  }

  /** 验证总的扣预存款付款明细 */
  private void validateBillDepositDefrayal() {
    String caption = DirectionType.receipt.getDirectionValue() == entity.getDirection() ? PPaymentDefrayalDef.TABLE_CAPTION
        + "/" + PPaymentDepositDefrayalDef.constants.tableCaption()
        : CommonMessages.M.paymentInfo() + "/" + CommonMessages.M.depositBillPay();
    for (int i = 0; i < entity.getDeposits().size(); i++) {
      BPaymentDepositDefrayal deposit = entity.getDeposits().get(i);
      Message message = null;
      PaymentDefrayalLineLocator locator = getLocator(i,
          BPaymentDepositDefrayal.FN_DEPOSITDEFRAYALTOTAL);
      if (deposit.getTotal() != null
          && deposit.getTotal().compareTo(BoundaryValue.BIGDECIMAL_MAXVALUE_S2) > 0) {
        String msg = CommonMessages.M.cannotMoreThan2(caption, (i + 1),
            PPaymentDepositDefrayalDef.constants.total(),
            BoundaryValue.BIGDECIMAL_MAXVALUE_S2.toString());
        message = Message.error(msg, locator);
        messages.add(message);

        if (locator.getField() instanceof RAbstractField) {
          ((RAbstractField) locator.getField()).addErrorMessage(CommonMessages.M.cannotMoreThan(
              PPaymentDepositDefrayalDef.constants.total(), BoundaryValue.BIGDECIMAL_MAXVALUE_S2.toString()));
        }
        continue;
      }

      if (deposit.getTotal() != null && deposit.getTotal().compareTo(BigDecimal.ZERO) < 0) {
        String msg = CommonMessages.M.cannotLessThan2(caption, (i + 1),
            PPaymentDepositDefrayalDef.constants.total(), "0");
        message = Message.error(msg, locator);
        messages.add(message);

        if (locator.getField() instanceof RAbstractField) {
          ((RAbstractField) locator.getField()).addErrorMessage(CommonMessages.M.cannotLessThan(
              PPaymentDepositDefrayalDef.constants.total(), "0"));
        }
      } else if (deposit.getTotal() != null
          && deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0) {
        String msg = CommonMessages.M.cannotMoreThan2(caption, (i + 1),
            PPaymentDepositDefrayalDef.constants.total(), CommonMessages.M.remainTotal());
        message = Message.error(msg, locator);
        messages.add(message);

        if (locator.getField() instanceof RAbstractField) {
          ((RAbstractField) locator.getField()).addErrorMessage(CommonMessages.M.cannotMoreThan(
              PPaymentDepositDefrayalDef.constants.total(), CommonMessages.M.remainTotal()));
        }
      }
    }
  }

  private PaymentDefrayalLineLocator getLocator(int lineIndex, String field) {
    Focusable f = owner != null ? owner.getFocusable(lineIndex, field) : null;
    return new PaymentDefrayalLineLocator(lineIndex, f);
  }
}
