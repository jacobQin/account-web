/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BDepositRepaymentValidator.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-19 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentLineDef;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * 预存款还款单验证器
 * 
 * @author zhuhairui
 * 
 */
public class BDepositRepaymentValidator implements RValidatable {
  private List<Message> messages = new ArrayList<Message>();
  private BDepositRepayment entity;
  private HasFocusables owner;

  public BDepositRepaymentValidator(BDepositRepayment entity, HasFocusables owner) {
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
      BDepositRepaymentLine line = entity.getLines().get(i);
      if (line.getSubject() == null) {
        continue;
      }
      noRecord = false;

      if (line.getSubject().getUuid() == null) {
        if (line.getSubject().getCode() != null) {
          Message message = Message.error(
              DepositRepaymentMessage.M.notFindSubjectCode((i + 1), line.getSubject().getCode()),
              getLocator(i, BDepositRepaymentLine.FN_SUBJECT));
          messages.add(message);
        } else {
          Message message = Message.error(
              DepositRepaymentMessage.M.notNull((i + 1),
                  PDepositRepaymentLineDef.constants.subject()),
              getLocator(i, BDepositRepaymentLine.FN_SUBJECT));
          messages.add(message);
        }
      } else {
        for (int j = i + 1; j < entity.getLines().size(); j++) {
          if (entity.getLines().get(j).getSubject() != null
              && line.getSubject().getUuid()
                  .equals(entity.getLines().get(j).getSubject().getUuid())) {
            Message message = Message.error(DepositRepaymentMessage.M.duplicate((i + 1), (j + 1),
                DepositRepaymentMessage.M.repaymentLines()));
            messages.add(message);
          }
        }
      }
      if (line.getAmount() == null) {
        Message message = Message
            .error(
                DepositRepaymentMessage.M.notNull((i + 1),
                    PDepositRepaymentLineDef.constants.amount()),
                getLocator(i, BDepositRepaymentLine.FN_AMOUNT));
        messages.add(message);
      } else if ((line.getAmount().compareTo(BigDecimal.ZERO) <= 0)) {
        Message message = Message.error(DepositRepaymentMessage.M.minValue((i + 1),
            PDepositRepaymentLineDef.constants.amount()),
            getLocator(i, BDepositRepaymentLine.FN_AMOUNT));
        messages.add(message);
      } else if ((line.getAmount().compareTo(line.getRemainAmount()) > 0)) {
        Message message = Message.error(
            DepositRepaymentMessage.M.maxValue((i + 1),
                PDepositRepaymentLineDef.constants.amount(),
                DepositRepaymentMessage.M.accountBalance()),
            getLocator(i, BDepositRepaymentLine.FN_AMOUNT));
        messages.add(message);
      }
    }

    if (noRecord == true) {
      Message message = Message.error(DepositRepaymentMessage.M
          .lineNotNull(DepositRepaymentMessage.M.repaymentLines()));
      messages.add(message);
    }
    return isValid();
  }

  private LineLocator getLocator(int lineIndex, String field) {
    Focusable f = owner != null ? owner.getFocusable(field) : null;
    return new LineLocator(lineIndex, f);
  }

}
