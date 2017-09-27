/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BStatementLine.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.biz;

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.acc.client.BAccountSourceType;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;

/**
 * 账单科目明细|表示层实体
 * 
 * @author huangjunxian
 * 
 */
public class BStatementLine extends BEntity {

  private static final long serialVersionUID = 300100L;
  /* 未进行指定业务 */
  public static final String NONE_BILL_UUID = "-";
  public static final String NONE_BILL_NUMBER = "-";
  /* 未指定发票 */
  public static final String NONE_INVOICE_TYPE = "-";
  public static final String NONE_INVOICE_CODE = "-";
  public static final String NONE_INVOICE_NUMBER = "-";
  /* 不允许指定业务 */
  public static final String DISABLE_BILL_UUID = "~";
  public static final String DISABLE_BILL_NUMBER = "~";

  private BAcc1 acc1;
  private BAcc2 acc2;
  private String accSrcType = BAccountSourceType.other;
  private boolean systemAdded;
  private boolean active;
  private BTotal total;
  private BTotal freeTotal;
  private boolean fromStatement;
  private String settlementId;
  private String remark;

  /** 账款信息 */
  public BAcc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(BAcc1 acc1) {
    this.acc1 = acc1;
  }

  /** 业务信息 */
  public BAcc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(BAcc2 acc2) {
    this.acc2 = acc2;
  }

  /** 账款来源类型 */
  public String getAccSrcType() {
    return accSrcType;
  }

  public void setAccSrcType(String accSrcType) {
    this.accSrcType = accSrcType;
  }

  /** 是否是系统计算过程附加的账款 */
  public boolean isSystemAdded() {
    return systemAdded;
  }

  public void setSystemAdded(boolean systemAdded) {
    this.systemAdded = systemAdded;
  }

  /** 是否有效账款 */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /** 金额税额 */
  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  /** 免租金额 */
  public BTotal getFreeTotal() {
    return freeTotal;
  }

  public void setFreeTotal(BTotal freeTotal) {
    this.freeTotal = freeTotal;
  }

  /** 是否来自账单 */
  public boolean isFromStatement() {
    return fromStatement;
  }

  public void setFromStatement(boolean fromStatement) {
    this.fromStatement = fromStatement;
  }

  /** 结算周期定义id */
  public String getSettlementId() {
    return settlementId;
  }

  public void setSettlementId(String settlementId) {
    this.settlementId = settlementId;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public boolean getInvoice() {
    if (acc2 == null)
      return false;
    if (acc2.getInvoice() == null)
      return false;
    if (DISABLE_BILL_UUID.equals(acc2.getInvoice().getBillUuid()))
      return false;
    else
      return true;
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
    final BStatementLine other = (BStatementLine) obj;
    if (getUuid() == null) {
      if (other.getUuid() != null)
        return false;
    } else if (!getUuid().equals(other.getUuid())) {
      return false;
    }
    return true;
  }

  public boolean isValid() {
    if (acc1 == null || acc1.getSubject() == null || acc1.getSubject().getUuid() == null)
      return false;
    return true;
  }

}
