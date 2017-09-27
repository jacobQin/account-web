/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： BPaymentLineDefrayalValidator.java
 * 模块说明：    
 * 修改历史：
 * 2013-11-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineLocator;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDepositDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineCashDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineDepositDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * 收付款单按科目收款时明细行中收付信息验证器。
 * 
 * @author subinzhu
 * 
 */
public class BPaymentLineDefrayalValidator implements RValidatable {

  private List<Message> messages = new ArrayList<Message>();
  private BPayment entity;

  public BPaymentLineDefrayalValidator(BPayment entity) {
    assert entity != null;
    assert !CPaymentDefrayalType.bill.equals(entity.getDefrayalType());
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

    // 验证账款明细行中的收付明细
    for (int i = 0; i < entity.getAccountLines().size(); i++) {
      BPaymentAccountLine line = entity.getAccountLines().get(i);
      int tabId;
      tabId = PaymentLineLocator.ACCOUNTTAB_ID;
      validateLineCashDefrayal(line, tabId, i, true);
      validateLineDepositDefrayal(line, tabId, i, true);
      validateLineDepositDefrayalRepeat(line, tabId, i, true);
    }

    // 验证滞纳金明细行中的收付明细
    for (int i = 0; i < entity.getOverdueLines().size(); i++) {
      BPaymentOverdueLine line = entity.getOverdueLines().get(i);
      int tabId;
      tabId = PaymentLineLocator.OVERDUETAB_ID;
      validateLineCashDefrayal(line, tabId, i, false);
      validateLineDepositDefrayal(line, tabId, i, false);
      validateLineDepositDefrayalRepeat(line, tabId, i, false);
    }

    // 验证总的扣预存款付款明细中的金额是否超过余额
    validateBillDepositDefrayal();
    return isValid();
  }

  /**
   * 验证按科目收款时明细行中的预存款冲扣明细
   * 
   * @param line
   *          要验证的收付款单明细行。
   * @param tabId
   *          收付款单明细中的tab的id，可选值：账款明细tabId(0)、滞纳金tabId(1)。
   * @param i
   *          要验证的收付款单明细行行号。
   * @param isAccountLine
   *          要验证的收付款单明细行是否是账款明细行。
   */
  private void validateLineCashDefrayal(BPaymentLine line, int tabId, int i, boolean isAccountLine) {
    String caption = (isAccountLine ? PPaymentAccountLineDef.TABLE_CAPTION
        : PPaymentOverdueLineDef.TABLE_CAPTION)
        + CommonMessages.M.lineNumber(i + 1)
        + "/"
        + PPaymentLineCashDef.TABLE_CAPTION;
    for (int j = 0; j < line.getCashs().size(); j++) {
      BPaymentLineCash cash = line.getCashs().get(j);
      PaymentLineDefrayalLineLocator locator = isAccountLine ? getAccountLocator(tabId, i, j,
          BPaymentLineCash.FN_LINECASHTOTAL) : getOverdueLocator(tabId, i, j,
          BPaymentLineCash.FN_LINECASHTOTAL);
      if (cash.getPaymentType() != null
          && !StringUtil.isNullOrBlank(cash.getPaymentType().getUuid())) {
        if (cash.getTotal() != null
            && cash.getTotal().compareTo(BoundaryValue.BIGDECIMAL_MAXVALUE_S2) > 0) {
          String msg = CommonMessages.M.cannotMoreThan2(caption, (i + 1),
              PPaymentDepositDefrayalDef.constants.total(),
              BoundaryValue.BIGDECIMAL_MAXVALUE_S2.toString());
          Message message = Message.error(msg, locator);
          messages.add(message);
          continue;
        }
      }
    }
  }

  /**
   * 验证按科目收款时明细行中的预存款冲扣明细
   * 
   * @param line
   *          要验证的收付款单明细行。
   * @param tabId
   *          收付款单明细中的tab的id，可选值：账款明细tabId(0)、滞纳金tabId(1)。
   * @param i
   *          要验证的收付款单明细行行号。
   * @param isAccountLine
   *          要验证的收付款单明细行是否是账款明细行。
   */
  private void validateLineDepositDefrayal(BPaymentLine line, int tabId, int i,
      boolean isAccountLine) {
    String caption = (isAccountLine ? PPaymentAccountLineDef.TABLE_CAPTION
        : PPaymentOverdueLineDef.TABLE_CAPTION)
        + CommonMessages.M.lineNumber(i + 1)
        + "/"
        + (DirectionType.receipt.getDirectionValue() == entity.getDirection() ? PPaymentLineDepositDef.TABLE_CAPTION
            : CommonMessages.M.depositLinePay());
    for (int j = 0; j < line.getDeposits().size(); j++) {
      BPaymentLineDeposit deposit = line.getDeposits().get(j);
      Message message = null;
      PaymentLineDefrayalLineLocator locator = isAccountLine ? getAccountLocator(tabId, i, j,
          BPaymentLineDeposit.FN_LINEDEPOSITTOTAL) : getOverdueLocator(tabId, i, j,
          BPaymentLineDeposit.FN_LINEDEPOSITTOTAL);
      if (deposit.getTotal() != null
          && deposit.getTotal().compareTo(BoundaryValue.BIGDECIMAL_MAXVALUE_S2) > 0) {
        String msg = CommonMessages.M.cannotMoreThan2(caption, (i + 1),
            PPaymentDepositDefrayalDef.constants.total(),
            BoundaryValue.BIGDECIMAL_MAXVALUE_S2.toString());
        message = Message.error(msg, locator);
        messages.add(message);
        continue;
      }

      if (deposit.getTotal() != null && deposit.getTotal().compareTo(BigDecimal.ZERO) < 0) {
        String msg = CommonMessages.M.cannotLessThan2(caption, (j + 1),
            PPaymentLineDepositDef.constants.total(), "0");
        message = Message.error(msg, locator);
        messages.add(message);
      } else if (deposit.getTotal() != null
          && deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0) {
        String msg = CommonMessages.M.cannotMoreThan2(caption, (j + 1),
            PPaymentLineDepositDef.constants.total(), CommonMessages.M.remainTotal());
        message = Message.error(msg, locator);
        messages.add(message);
      }
    }
  }

  /**
   * 验证预收付款冲扣是否重复
   * 
   * @param line
   * @param tabId
   * @param lineNumber
   * @param isAccountLine
   */
  private void validateLineDepositDefrayalRepeat(BPaymentLine line, int tabId, int lineNumber,
      boolean isAccountLine) {
    List<BPaymentLineDeposit> deposits = line.getDeposits();
    if (deposits == null || deposits.isEmpty())
      return;

    for (int i = deposits.size() - 1; i >= 0; i--) {
      BPaymentLineDeposit deposit = deposits.get(i);
      for (int j = 0; j < i; j++) {
        BPaymentLineDeposit d = line.getDeposits().get(j);
        if (deposit.getSubject() != null && d.getSubject() != null
            && deposit.getSubject().equals(d.getSubject())
            && ObjectUtil.equals(deposit.getContract(), d.getContract())) {
          String caption = (isAccountLine ? PPaymentAccountLineDef.TABLE_CAPTION
              : PPaymentOverdueLineDef.TABLE_CAPTION)
              + CommonMessages.M.lineNumber(lineNumber + 1)
              + "/"
              + (DirectionType.receipt.getDirectionValue() == entity.getDirection() ? PPaymentLineDepositDef.TABLE_CAPTION
                  : CommonMessages.M.depositLinePay());
          String msg = CommonMessages.M.lineRepeatError(
              caption + CommonMessages.M.lineNumber(i + 1) + CommonMessages.M.subjectAndContract(),
              j + 1);

          PaymentLineDefrayalLineLocator locator = getOverdueLocator(tabId, lineNumber, i,
              BPaymentLineDeposit.FN_LINEDEPOSITSUBJECT);
          Message message = Message.error(msg, locator);
          messages.add(message);
          break;
        }
      }
    }
  }

  /** 验证总的扣预存款付款明细中的金额是否超过余额 */
  private void validateBillDepositDefrayal() {
    for (int i = 0; i < entity.getDeposits().size(); i++) {
      BPaymentDepositDefrayal deposit = entity.getDeposits().get(i);
      if (deposit.getSubject() != null && !StringUtil.isNullOrBlank(deposit.getSubject().getUuid())) {
        if (deposit.getTotal().compareTo(deposit.getRemainTotal()) > 0) {
          Message message = Message.error(CommonMessages.M.cannotMoreThan2(
              CommonMessages.M.paymentInfo() + "/"
                  + PPaymentDepositDefrayalDef.constants.tableCaption(), (i + 1),
              PPaymentDepositDefrayalDef.constants.total(), CommonMessages.M.remainTotal()));
          messages.add(message);
        }
      }
    }
  }

  private PaymentLineDefrayalLineLocator getAccountLocator(int tabId, int lineNumber,
      int lineNumber2, String fieldId) {
    return new PaymentLineDefrayalLineLocator(tabId, lineNumber, lineNumber2, fieldId);
  }

  private PaymentLineDefrayalLineLocator getOverdueLocator(int tabId, int lineNumber,
      int lineNumber2, String fieldId) {
    return new PaymentLineDefrayalLineLocator(tabId, lineNumber, lineNumber2, fieldId);
  }
}
