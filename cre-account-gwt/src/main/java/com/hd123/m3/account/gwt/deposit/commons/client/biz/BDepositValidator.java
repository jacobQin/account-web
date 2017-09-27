/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： BCollectionValidator.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-23 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositLineDef;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * @author chenpeisi
 * 
 */
public class BDepositValidator implements RValidatable {

  public BDepositValidator(BDeposit entity, HasFocusables owner) {
    assert entity != null;
    this.entity = entity;
    this.owner = owner;
  }

  private List<Message> messages = new ArrayList<Message>();
  private BDeposit entity;
  private HasFocusables owner;

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
      BDepositLine line = entity.getLines().get(i);
      if (line.getSubject() == null) {
        continue;
      }
      noRecord = false;

      if (line.getSubject().getUuid() == null) {
        Message message = Message.error(
            DepositMessage.M.notNull((i + 1), PDepositLineDef.constants.subject()),
            getLocator(i, BDepositLine.FN_SUBJECT));
        messages.add(message);
      } else {
        for (int j = i + 1; j < entity.getLines().size(); j++) {
          if (entity.getLines().get(j).getSubject() != null
              && line.getSubject().getUuid()
                  .equals(entity.getLines().get(j).getSubject().getUuid())) {
            Message message = Message.error(DepositMessage.M.duplicate((i + 1), (j + 1)),
                getLocator(i, BDepositLine.FN_SUBJECT));
            messages.add(message);
          }
        }
      }

      if (line.getTotal() == null) {
        Message message = Message.error(
            DepositMessage.M.notNull((i + 1), PDepositLineDef.constants.amount()),
            getLocator(i, BDepositLine.FN_TOTAL));
        messages.add(message);
      } else if ((line.getTotal().compareTo(BigDecimal.ZERO) <= 0)) {
        Message message = Message.error(
            DepositMessage.M.minValue((i + 1), PDepositLineDef.constants.amount()),
            getLocator(i, BDepositLine.FN_TOTAL));
        messages.add(message);
      } else if (line.getTotal().compareTo(BoundaryValue.BIGDECIMAL_MAXVALUE_S2) > 0) {
        Message message = Message.error(
            DepositMessage.M.maxValue((i + 1), PDepositLineDef.constants.amount()),
            getLocator(i, BDepositLine.FN_TOTAL));
        messages.add(message);
      }
    }

    if (noRecord == true) {
      Message message = Message.error(DepositMessage.M.lineNotNull(DepositMessage.M.receiptLine()));
      messages.add(message);
    }
    return isValid();
  }

  private LineLocator getLocator(int lineIndex, String field) {
    Focusable f = owner != null ? owner.getFocusable(field) : null;
    return new LineLocator(lineIndex, f);
  }
}
