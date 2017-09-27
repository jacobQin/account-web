/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BContract.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-17 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.BWithUCN;
import com.hd123.rumba.gwt.base.client.HasMessages;
import com.hd123.rumba.gwt.util.client.message.Message;

/**
 * @author subinzhu
 * 
 */
public class BContract implements Serializable, HasMessages, BWithUCN {
  private static final long serialVersionUID = 3674080825101570690L;

  public static final String FIELD_BILLNUMBER = "billNumber";
  public static final String FIELD_ACCOUNTUNIT = "accountUnit";
  public static final String FIELD_COUNTERPART = "counterpart";
  public static final String FIELD_COUNTERPARTTYPE = "counterpartType";
  public static final String FIELD_TITLE = "title";
  public static final String FIELD_POSITION = "position";
  public static final String FIELD_BRAND = "brand";
  public static final String FIELD_FLOOR = "floor";
  public static final String FIELD_COOPMODE = "coopMode";
  public static final String FIELD_CONTRACT_CATEGORY = "contractCategory";
  public static final String FIELD_STATE = "state";

  private String uuid;
  private String billNumber;
  private BUCN accountUnit;
  private BCounterpart counterpart;
  private String title;
  private BUCN floor;
  private String coopMode;
  private String contractCategory;
  private Date beginDate;
  private Date endDate;

  private List<Message> messages;
  private List<BUCN> positions = new ArrayList<BUCN>();

  public BUCN clone() {
    BUCN bucn = new BUCN();
    bucn.setCode(getCode());
    bucn.setName(getName());
    bucn.setUuid(getUuid());
    return bucn;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

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

  public String getCoopMode() {
    return coopMode;
  }

  public void setCoopMode(String coopMode) {
    this.coopMode = coopMode;
  }

  public String getContractCategory() {
    return contractCategory;
  }

  public void setContractCategory(String contractCategory) {
    this.contractCategory = contractCategory;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * 拼接铺位编号字符串
   * 
   * @param positions
   * @return
   */
  public String getPositionStr(List<BUCN> positions) {
    StringBuilder str = new StringBuilder("");
    if (positions != null) {
      for (int i = 0; i < positions.size(); i++) {
        if (i == positions.size() - 1) {
          str.append(positions.get(i).getCode());
        } else {
          str.append(positions.get(i).getCode() + ",");
        }
      }
    }
    return str.toString();
  }

  public String getState() {
    if (beginDate != null && endDate != null) {
      if (beginDate.after(new Date())) {
        return BContractState.R.R.ineffect();
      } else if (beginDate.before(new Date()) && endDate.after(new Date())) {
        return BContractState.R.R.effected();
      } else if (endDate.before(new Date())) {
        return BContractState.R.R.finished();
      }
    }
    return null;
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

  @Override
  public String getCode() {
    return getBillNumber();
  }

  @Override
  public void setCode(String code) throws UnsupportedOperationException {
    setBillNumber(code);
  }

  @Override
  public String getName() {
    return getTitle();
  }

  @Override
  public void setName(String name) throws UnsupportedOperationException {
    setTitle(name);
  }

  @Override
  public String toFriendlyStr() {
    return BUCN.toFriendlyStr(this);
  }

  public List<BUCN> getPositions() {
    return positions;
  }

  public void setPositions(List<BUCN> positions) {
    this.positions = positions;
  }

}
