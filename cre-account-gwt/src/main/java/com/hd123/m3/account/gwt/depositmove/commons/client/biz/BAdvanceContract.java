/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BDepositContract.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.HasMessages;
import com.hd123.rumba.gwt.util.client.message.Message;

/**
 * @author zhuhairui
 * 
 */
public class BAdvanceContract implements Serializable, HasMessages {
  private static final long serialVersionUID = 5435059290452433105L;

  public static final String FIELD_BILLNUMBER = "billNumber";
  public static final String FIELD_ACCOUNTUNIT = "accountUnit";
  public static final String FIELD_COUNTERPART = "counterpart";
  public static final String FIELD_TITLE = "title";
  public static final String FIELD_POSITION = "position";
  public static final String FIELD_BRAND = "brand";
  public static final String FIELD_FLOOR = "floor";


  private BUCN contract;
  private BUCN accountUnit;
  private BCounterpart counterpart;
  private String title;
  private BUCN floor;
  
  private List<Message> messages;
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public BUCN getFloor() {
    return floor;
  }

  public void setFloor(BUCN floor) {
    this.floor = floor;
  }

  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
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

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }
}
