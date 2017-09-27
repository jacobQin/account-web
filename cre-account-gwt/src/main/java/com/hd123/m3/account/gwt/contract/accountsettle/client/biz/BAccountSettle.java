/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccountSettle.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.biz;

import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 出账实体|表示层
 * 
 * @author huangjunxian
 * 
 */
public class BAccountSettle extends BEntity {

  private static final long serialVersionUID = 300200L;

  public static final String FIELD_COUNTERPART = "counterpart";
  public static final String FIELD_SETTLENAME = "settleName";
  public static final String FIELD_BEGINDATE = "beginDate";
  public static final String FIELD_CONTRACT_TITLE = "contractTitle";
  public static final String FIELD_CONTRACT_NUMBER = "contractNumber";
  public static final String FIELD_ACCOUNTTIME = "accountTime";
  public static final String FIELD_BILLCALCTYPE = "billCalcType";
  public static final String FIELD_FLOOR = "floor";
  public static final String FIELD_COOPMODE = "coopMode";
  public static final String FIELD_CONTRACT_CATEGORY = "contractCategory";

  private BCounterpart counterpart;
  private String settlementUuid;
  private String settleName;
  private Date beginDate;
  private Date endDate;
  private String contractUuid;
  private String contractTitle;
  private String contractNumber;
  private Date accountTime;
  private String billCalcType;
  private BUCN floor;
  private String coopMode;

  /**
   * 商户
   */
  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart tenant) {
    this.counterpart = tenant;
  }

  /***
   * 周期定义uuid
   */
  public String getSettlementUuid() {
    return settlementUuid;
  }

  public void setSettlementUuid(String settlementUuid) {
    this.settlementUuid = settlementUuid;
  }

  /**
   * 结算周期名称
   */
  public String getSettleName() {
    return settleName;
  }

  public void setSettleName(String settleName) {
    this.settleName = settleName;
  }

  /**
   * 结算周期日期范围
   */
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

  public String getContractUuid() {
    return contractUuid;
  }

  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
  }

  /**
   * 合同标题
   */
  public String getContractTitle() {
    return contractTitle;
  }

  public void setContractTitle(String contractTitle) {
    this.contractTitle = contractTitle;
  }

  /**
   * 合同编号
   */
  public String getContractNumber() {
    return contractNumber;
  }

  public void setContractNumber(String contractNumber) {
    this.contractNumber = contractNumber;
  }

  /**
   * 出账时间
   */
  public Date getAccountTime() {
    return accountTime;
  }

  public void setAccountTime(Date accountTime) {
    this.accountTime = accountTime;
  }

  /**
   * 出账方式
   */
  public String getBillCalcType() {
    return billCalcType;
  }

  public void setBillCalcType(String billCalcType) {
    this.billCalcType = billCalcType;
  }

  /**
   * 楼层
   */
  public BUCN getFloor() {
    return floor;
  }

  public void setFloor(BUCN floor) {
    this.floor = floor;
  }

  /**
   * 合作方式
   */
  public String getCoopMode() {
    return coopMode;
  }

  public void setCoopMode(String coopMode) {
    this.coopMode = coopMode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (getUuid() == null ? 0 : getUuid().hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    final BAccountSettle other = (BAccountSettle) obj;
    if (getUuid() == null) {
      if (other.getUuid() != null)
        return false;
    } else if (!getUuid().equals(other.getUuid())) {
      return false;
    }
    return true;
  }

}
