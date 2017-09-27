/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositSubjectFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-22 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.filter;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 科目查询筛选器
 * 
 * @author chenpeisi
 * 
 */
public class AdvanceSubjectFilter extends QueryFilter {
  private static final long serialVersionUID = -6459326283073889549L;

  private String code;
  private String name;

  // 收付方向
  private int directionType;
  // 商户uuid
  private String counterpartUuid;
  // 合同uuid
  private String contractUuid;
  // 项目uuid
  private String accountUnitUuid;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDirectionType() {
    return directionType;
  }

  public void setDirectionType(int directionType) {
    this.directionType = directionType;
  }

  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  public String getContractUuid() {
    return contractUuid;
  }

  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
  }

  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

}
