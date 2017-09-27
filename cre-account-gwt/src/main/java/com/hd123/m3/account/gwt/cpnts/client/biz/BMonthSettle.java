/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-common
 * 文件名： BMonthSettle.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-18 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.HasMessages;
import com.hd123.rumba.gwt.util.client.message.Message;

/**
 * 月度结转界面对象
 * 
 * @author subinzhu
 * 
 */
public class BMonthSettle extends BEntity implements HasMessages {
  private static final long serialVersionUID = 300100L;

  public static final String FIELD_NUMBER = "number";
  public static final String FIELD_BEGINTIME = "beginTime";
  public static final String FIELD_ENDTIME = "endTime";

  private String number;
  private Date beginTime;
  private Date endTime;
  private Date createTime;
  private List<Message> messages;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Date getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public void addError(String message) {
    if (message == null || "".equals(message.trim()))
      return;
    addMessage(Message.error(message));
  }

  @Override
  public List<Message> getMessages() {
    if (messages == null) {
      return new ArrayList<Message>();
    }
    return messages;
  }

  @Override
  public void addMessage(Message message) {
    if (message == null) {
      return;
    }

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
    if (messages == null || messages.isEmpty()) {
      return true;
    }

    for (Message message : messages) {
      if (message.isValid() == false) {
        return false;
      }
    }
    return true;
  }
}
