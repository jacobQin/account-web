/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BPayment.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月9日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal.model;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.m3.cre.controller.account.accountdefrayal.convert.Date2StrUtil;

/**
 * 收付款单(用户显示科目明细数据)
 * 
 * @author chenganbang
 *
 */
public class BAccount {
  private String contractName;
  private String counterpartName;
  private String settlement;
  private Date accountDate;
  private BigDecimal originTotal = BigDecimal.ZERO;
  private String billNumber;
  private Date paymentDate;
  private BigDecimal total = BigDecimal.ZERO;
  private String pivcType;
  private String pivcNumber;

  public String getContractName() {
    return contractName;
  }

  public void setContractName(String contractName) {
    this.contractName = contractName;
  }

  public String getCounterpartName() {
    return counterpartName;
  }

  public void setCounterpartName(String counterpartName) {
    this.counterpartName = counterpartName;
  }

  public String getSettlement() {
    return settlement;
  }

  public void setSettlement(Date beginDate, Date endDate) {
    if (beginDate != null && endDate != null) {
      settlement = Date2StrUtil.getInstance().convert(beginDate) + " ~ "
          + Date2StrUtil.getInstance().convert(endDate);
    } else if (beginDate != null && endDate == null) {
      settlement = Date2StrUtil.getInstance().convert(beginDate) + " ~ ";
    } else if (beginDate == null && endDate != null) {
      settlement = " ~ " + Date2StrUtil.getInstance().convert(endDate);
    } else {
      settlement = "";
    }
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public String getPivcType() {
    return pivcType;
  }

  public void setPivcType(String pivcType) {
    this.pivcType = pivcType;
  }

  public String getPivcNumber() {
    return pivcNumber;
  }

  public void setPivcNumber(String pivcNumber) {
    this.pivcNumber = pivcNumber;
  }

  public BigDecimal getOriginTotal() {
    return originTotal;
  }

  public void setOriginTotal(BigDecimal originTotal) {
    this.originTotal = originTotal;
  }

}
