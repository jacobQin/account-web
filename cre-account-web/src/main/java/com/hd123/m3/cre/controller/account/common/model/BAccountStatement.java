/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BAccountStatement.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月25日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 账款按账单分组
 * 
 * @author LiBin
 *
 */
public class BAccountStatement implements Serializable {
  private static final long serialVersionUID = 5102691907253880185L;

  public static final String ORDER_BY_FIELD_BILLNUMBER = "billNumber";
  public static final String ORDER_BY_FIELD_CONTRACT = "contract";
  public static final String ORDER_BY_FIELD_CONTRACTTITLE = "contractTitle";
  public static final String ORDER_BY_FIELD_ACCOUNTUNIT = "accountUnit";
  public static final String ORDER_BY_FIELD_COUNTERPART = "counterpart";
  public static final String ORDER_BY_FIELD_TOTAL = "total";
  public static final String ORDER_BY_FIELD_ACCOUNTTIME = "accountTime";
  public static final String ORDER_BY_FIELD_SETTLENO = "settleNo";

  private String uuid;
  private String billNumber;
  private UCN accountUnit;
  private UCN counterpart;
  private String counterpartType;

  private UCN contract;
  private String contractCategory;

  private Total total;
  private Date accountTime;
  private String settleNo;
  private List<BAccount> accounts = new ArrayList<BAccount>();
  
  private String direction;

  /** 账单标识 */
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /** 账单号 */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  /** 项目 */
  public UCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(UCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 对方单位 */
  public UCN getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(UCN counterpart) {
    this.counterpart = counterpart;
  }

  /** 对方单位类型 */
  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  /** 合同 */
  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }
  
  /** 合同类型 */
  public String getContractCategory() {
    return contractCategory;
  }
  
  public void setContractCategory(String contractCategory) {
    this.contractCategory = contractCategory;
  }

  /** 金额 */
  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  /** 出账日期 */
  public Date getAccountTime() {
    return accountTime;
  }

  public void setAccountTime(Date accountTime) {
    this.accountTime = accountTime;
  }

  /** 结转期 */
  public String getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(String settleNo) {
    this.settleNo = settleNo;
  }

  /** 账款列表 */
  public List<BAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<BAccount> accounts) {
    this.accounts = accounts;
  }

  /** 账款明细的方向 */
  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  /** 如果账款明细含有全部为收就返回收(1),全部为付返回付(-1)，否则就返回0。 */
  public static int getAccsDirection(List<BAccount> accounts) {
    boolean isReceipt = false;
    boolean isPayment = false;
    for (BAccount acc : accounts) {
      if (Direction.RECEIPT == acc.getAcc1().getDirection()) {
        isReceipt = true;
      } else {
        isPayment = true;
      }
    }

    if ((isReceipt ^ isPayment) == false) {
      return 0;
    }

    if (isReceipt) {
      return Direction.RECEIPT;
    }

    return Direction.PAYMENT;
  }

  /** 账款按账单分组之后进行金额合计 */
  public Total aggregateTotal() {
    if (accounts == null || accounts.isEmpty()) {
      return Total.zero();
    }

    Total t = Total.zero();
    for (Account account : accounts) {
      t = t.add(account.getTotal());
    }
    return t;
  }
}
