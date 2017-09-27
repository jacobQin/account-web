/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RecDepositRepaymentFilter.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月28日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.widget;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * @author chenganbang
 *
 */
public class RecDepositFilter extends QueryFilter {
  private static final long serialVersionUID = -2763908942645377211L;

  private String billNumber;
  private String accountUnitUuid;
  private String counterpartUuid;
  private String contractUuid;
  private String bizState;
  private boolean unRepaymented = true;

  /**
   * 单号
   * 
   * @return
   */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  /**
   * 项目
   * 
   * @return
   */
  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  /**
   * 对方单位
   * 
   * @return
   */
  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  /**
   * 合同
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
   * 状态
   * 
   * @return
   */
  public String getBizState() {
    return bizState;
  }

  public void setBizState(String bizState) {
    this.bizState = bizState;
  }

  /**
   * 是否限制为未还款
   * 
   * @return
   */
  public boolean isUnRepaymented() {
    return unRepaymented;
  }

  public void setUnRepaymented(boolean unRepaymented) {
    this.unRepaymented = unRepaymented;
  }

}
