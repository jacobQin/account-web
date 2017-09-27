/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceStockRegLine.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.biz.entity.UCN;


/**
 * 发票库存登记明细
 * 
 * @author wangyibo
 *
 */
public class InvoiceStockRegLine implements Serializable{

  private static final long serialVersionUID = 5899632985415310444L;
  
  private UCN tenant;
  private UCN subject;
  private UCN contract;
  private String souceBillType;
  private String sourceBillTypeCaption;
  private String sourceBillNumber;
  private Date beginDate;
  private Date endDate;
  private BigDecimal receivableTotal = BigDecimal.ZERO;
  private BigDecimal regTotal = BigDecimal.ZERO;

  /** 商户 */
  public UCN getTenant() {
    return tenant;
  }

  public void setTenant(UCN tenant) {
    this.tenant = tenant;
  }

  /** 科目 */
  public UCN getSubject() {
    return subject;
  }

  public void setSubject(UCN subject) {
    this.subject = subject;
  }

  /** 合同 */
  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

  /** 来源单据类型 */
  public String getSouceBillType() {
    return souceBillType;
  }

  public void setSouceBillType(String souceBillType) {
    this.souceBillType = souceBillType;
  }

  /** 来源单据类型标题 */
  public String getSourceBillTypeCaption() {
    return sourceBillTypeCaption;
  }

  public void setSourceBillTypeCaption(String sourceBillTypeCaption) {
    this.sourceBillTypeCaption = sourceBillTypeCaption;
  }

  /** 来源单号 */
  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  /** 起始日期 */
  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  /** 结束日期 */
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /** 应收金额 */
  public BigDecimal getReceivableTotal() {
    return receivableTotal;
  }

  public void setReceivableTotal(BigDecimal receivableTotal) {
    this.receivableTotal = receivableTotal;
  }

  /** 开票金额 */
  public BigDecimal getRegTotal() {
    return regTotal;
  }

  public void setRegTotal(BigDecimal regTotal) {
    this.regTotal = regTotal;
  }

  
  
}
