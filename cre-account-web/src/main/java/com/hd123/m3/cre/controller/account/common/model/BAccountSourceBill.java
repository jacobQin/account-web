/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BAccountSourceBill.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月27日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.commons.SourceBill;
import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 账款按来源单据分组。
 * 
 * @author LiBin
 *
 */
public class BAccountSourceBill implements Serializable {

  private static final long serialVersionUID = -120366455991496447L;

  public static final String ORDER_BY_FIELD_SOURCEBILLTYPE = "sourceBillType";
  public static final String ORDER_BY_FIELD_SOURCEBILLNUMBER = "sourceBillNumber";
  public static final String ORDER_BY_FIELD_ACCOUNTUNIT = "accountUnit";
  public static final String ORDER_BY_FIELD_COUNTERPART = "counterpart";
  public static final String ORDER_BY_FIELD_CONTRACT = "contract";
  public static final String ORDER_BY_FIELD_CONTRACTTITLE = "contractTitle";
  public static final String ORDER_BY_FIELD_TOTAL = "total";

  private SourceBill sourceBill;
  private UCN counterpart;
  private String counterpartType;
  private Total total;
  private UCN contract;

  private String direction;
  private String sourceBillNumber;

  private List<BAccount> accounts = new ArrayList<BAccount>();

  /** 来源单据 */
  public SourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(SourceBill sourceBill) {
    this.sourceBill = sourceBill;
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

  /** 金额 */
  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  /** 合同 */
  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
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

  /** 来源单据单号 */
  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  /** 账款按来源单据分组之后进行金额合计 */
  public Total aggregateTotal() {
    if (accounts == null || accounts.isEmpty())
      return Total.zero();

    Total t = Total.zero();
    for (Account account : accounts) {
      t = t.add(account.getTotal());
    }
    return t;
  }

  /** 如果账款明细含有全部为收就返回收(1),全部为付返回付(-1)，否则就返回0。 */
  public int getAccsDirection() {
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

}
