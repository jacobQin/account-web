/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BRebateBill.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.commons.gwt.base.client.biz.BStandardBill;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|返款单
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public class BRebateBill extends BStandardBill {
  private static final long serialVersionUID = -3480705786583501984L;
  private BUCN store;
  private BUCN tenant;
  private BContract contract;
  private String positions;
  private Date accountDate;
  private Date beginDate;
  private Date endDate;

  private BigDecimal backTotal;
  private BigDecimal poundageTotal;
  private BigDecimal shouldBackTotal;

  private BTaxRate poundageTaxRate;
  private BTaxRate backTaxRate;

  private boolean rebateInvoice = true;
  private boolean poundageInvoice = true;

  private BUCN rebateSubject;
  private BUCN poundageSubject;

  private String remark;

  private boolean single;

  private List<BRebateLine> lines = new ArrayList<BRebateLine>();

  private boolean hasAccount = false;

  /** 项目 */
  public BUCN getStore() {
    return store;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  /** 商户 */
  public BUCN getTenant() {
    return tenant;
  }

  public void setTenant(BUCN tenant) {
    this.tenant = tenant;
  }

  /** 合同 */
  public BContract getContract() {
    return contract;
  }

  public void setContract(BContract contract) {
    this.contract = contract;
  }

  /** 铺位编号字符串 */
  public String getPositions() {
    return positions;
  }

  public void setPositions(String positions) {
    this.positions = positions;
  }

  /** 记账日期 */
  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  /** 起始日期 */
  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  /** 截止日期 */
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /** 销售额返款 */
  public BigDecimal getBackTotal() {
    return backTotal;
  }

  public void setBackTotal(BigDecimal backTotal) {
    this.backTotal = backTotal;
  }

  /** 手续费 */
  public BigDecimal getPoundageTotal() {
    return poundageTotal;
  }

  public void setPoundageTotal(BigDecimal poundageTotal) {
    this.poundageTotal = poundageTotal;
  }

  /** 实际返款金额 */
  public BigDecimal getShouldBackTotal() {
    return shouldBackTotal;
  }

  public void setShouldBackTotal(BigDecimal shouldBackTotal) {
    this.shouldBackTotal = shouldBackTotal;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 销售额返款是否开发票，由客户端根据合同条款写入 */
  public boolean isRebateInvoice() {
    return rebateInvoice;
  }

  public void setRebateInvoice(boolean rebateInvoice) {
    this.rebateInvoice = rebateInvoice;
  }

  /** 手续费是否开发票，由客户端根据合同条款写入 */
  public boolean isPoundageInvoice() {
    return poundageInvoice;
  }

  public void setPoundageInvoice(boolean poundageInvoice) {
    this.poundageInvoice = poundageInvoice;
  }

  /** 返款科目 */
  public BUCN getRebateSubject() {
    return rebateSubject;
  }

  public void setRebateSubject(BUCN rebateSubject) {
    this.rebateSubject = rebateSubject;
  }

  /** 手续费科目,手续费不为0时禁止为null */
  public BUCN getPoundageSubject() {
    return poundageSubject;
  }

  public void setPoundageSubject(BUCN poundageSubject) {
    this.poundageSubject = poundageSubject;
  }

  /** 是否按笔返款 */
  public boolean isSingle() {
    return single;
  }

  public void setSingle(boolean single) {
    this.single = single;
  }

  /** 销售返款明细 */
  public List<BRebateLine> getLines() {
    return lines;
  }

  public void setLines(List<BRebateLine> lines) {
    this.lines = lines;
  }

  public boolean isHasAccount() {
    return hasAccount;
  }

  public void setHasAccount(boolean hasAccount) {
    this.hasAccount = hasAccount;
  }

  public BTaxRate getPoundageTaxRate() {
    return poundageTaxRate;
  }

  public void setPoundageTaxRate(BTaxRate poundageTaxRate) {
    this.poundageTaxRate = poundageTaxRate;
  }

  public BTaxRate getBackTaxRate() {
    return backTaxRate;
  }

  public void setBackTaxRate(BTaxRate backTaxRate) {
    this.backTaxRate = backTaxRate;
  }

}
