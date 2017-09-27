/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BDepositRepaymentLine.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-19 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.HasMessages;
import com.hd123.rumba.gwt.util.client.message.Message;

/**
 * @author zhuhairui
 * 
 */
public class BDepositRepaymentLine extends BEntity implements HasMessages, EditGridElement {

  private static final long serialVersionUID = 7170695742390339153L;

  public static final String FN_SUBJECT = "subject";
  public static final String FN_AMOUNT = "amount";

  private int lineNumber;
  private BUCN subject;
  private BigDecimal amount = BigDecimal.ZERO;
  private BigDecimal remainAmount = BigDecimal.ZERO;
  private String remark;

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getRemainAmount() {
    return remainAmount;
  }

  public void setRemainAmount(BigDecimal remainAmount) {
    this.remainAmount = remainAmount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  private List<Message> messages;

  @Override
  public List<Message> getMessages() {
    if (messages == null) {
      messages = new ArrayList<Message>();
    }
    return messages;
  }

  @Override
  public void addMessage(Message message) {
    if (message == null)
      return;

    if (messages == null) {
      messages = new ArrayList<Message>();
    }
    messages.add(message);
  }

  @Override
  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  @Override
  public boolean isValid() {
    return messages.isEmpty();
  }

  @Override
  public boolean isEmpty() {
    if (subject != null || amount == null || amount.compareTo(BigDecimal.ZERO) != 0
        || remainAmount.compareTo(BigDecimal.ZERO) != 0 || !StringUtil.isNullOrBlank(remark)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean match(EditGridElement other) {
    if (other instanceof BDepositRepaymentLine == false)
      return false;
    BDepositRepaymentLine detail = (BDepositRepaymentLine) other;
    if (subject != null && detail.getSubject() != null && subject.getUuid() != null
        && ObjectUtil.isEquals(subject, detail.getSubject())) {
      return true;
    }
    return false;
  }

}
