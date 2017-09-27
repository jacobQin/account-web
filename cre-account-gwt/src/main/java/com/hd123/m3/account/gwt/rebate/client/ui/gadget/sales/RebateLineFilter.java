/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	SaleDetailsFilter.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月9日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales;

import java.util.Date;

import com.hd123.bpm.widget.base.client.QueryFilter;

/**
 * 销售明细过滤
 * 
 * @author chenganbang
 */
public class RebateLineFilter extends QueryFilter {
  private static final long serialVersionUID = -4652776742813094487L;

  private String contractUuid;
  private Date beginDate;
  private Date endDate;
  private String saleNumber;
  private Date orcTime;
  private boolean bankRate = false;
  private boolean shouldBack = false;
  private String posMode;

  /**
   * 合同uuid
   * 
   * @return
   */
  public String getContractUuid() {
    return contractUuid;
  }

  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
  }

  /**
   * 起始时间
   * 
   * @return
   */
  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  /**
   * 截止时间
   * 
   * @return
   */
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * 来源单据单号
   * 
   * @return
   */
  public String getSaleNumber() {
    return saleNumber;
  }

  public void setSaleNumber(String saleNumber) {
    this.saleNumber = saleNumber;
  }

  /**
   * 记账时间
   * 
   * @return
   */
  public Date getOrcTime() {
    return orcTime;
  }

  public void setOrcTime(Date orcTime) {
    this.orcTime = orcTime;
  }

  /**
   * 银行手续费扣率条款
   * 
   * @return
   */
  public boolean isBankRate() {
    return bankRate;
  }

  public void setBankRate(boolean bankRate) {
    this.bankRate = bankRate;
  }

  /**
   * 销售额返款条款
   * 
   * @return
   */
  public boolean isShouldBack() {
    return shouldBack;
  }

  public void setShouldBack(boolean shouldBack) {
    this.shouldBack = shouldBack;
  }

  /**
   * 合同收银方式
   * 
   * @return
   */
  public String getPosMode() {
    return posMode;
  }

  public void setPosMode(String posMode) {
    this.posMode = posMode;
  }
}
