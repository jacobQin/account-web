/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： BCollectionLine.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-23 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.commons.client.biz;

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
 * @author chenpeisi
 * 
 */
public class BDepositLine extends BEntity implements HasMessages, EditGridElement {
  private static final long serialVersionUID = 300200L;

  public static final String FN_SUBJECT = "subject";
  public static final String FN_TOTAL = "total";

  private BDeposit pay;
  private int lineNumber;
  private BUCN subject;
  private BigDecimal total = BigDecimal.ZERO;
  private BigDecimal remainTotal = BigDecimal.ZERO;
  private BigDecimal contractTotal = BigDecimal.ZERO;
  private String remark;

  public BDepositLine() {
  }

  public BDepositLine(BDeposit pay) {
    this.pay = pay;
  }

  public BDeposit getPay() {
    return pay;
  }

  public void setPay(BDeposit pay) {
    this.pay = pay;
  }

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getRemainTotal() {
    return remainTotal;
  }

  public void setRemainTotal(BigDecimal remainTotal) {
    this.remainTotal = remainTotal;
  }

  public BigDecimal getContractTotal() {
    return contractTotal;
  }

  public void setContractTotal(BigDecimal contractTotal) {
    this.contractTotal = contractTotal;
  }

  public BigDecimal getUnDepositTotal() {
    if (contractTotal == null) {
      return BigDecimal.ZERO;
    } else {
      if (remainTotal == null) {
        return contractTotal;
      } else {
        BigDecimal result = contractTotal.subtract(remainTotal);
        return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
      }
    }
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
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
    if (subject != null || total == null || total.compareTo(BigDecimal.ZERO) != 0
        || remainTotal.compareTo(BigDecimal.ZERO) != 0
        || contractTotal.compareTo(BigDecimal.ZERO) != 0 || !StringUtil.isNullOrBlank(remark)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean match(EditGridElement other) {
    if (other instanceof BDepositLine == false)
      return false;
    BDepositLine detail = (BDepositLine) other;
    if (subject != null && detail.getSubject() != null && subject.getUuid() != null
        && ObjectUtil.isEquals(subject, detail.getSubject())) {
      return true;
    }
    return false;
  }
}
