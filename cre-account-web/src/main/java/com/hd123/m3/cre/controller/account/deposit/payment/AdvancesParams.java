/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AdvancesParams.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - Shikenian - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.payment;

import java.io.Serializable;
import java.util.List;

/**
 * 批量获取预存款余额参数包装
 * 
 * @author Shikenian
 *
 */
public class AdvancesParams implements Serializable {

  private static final long serialVersionUID = -5046586178223496173L;

  private String accountUnitUuid;
  private String counterpartUuid;
  private List<String> subjectUuids;
  private String contractUuid;

  public String getAccountUnitUuid() {
    return accountUnitUuid;
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
  }

  public String getCounterpartUuid() {
    return counterpartUuid;
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
  }

  public List<String> getSubjectUuids() {
    return subjectUuids;
  }

  public void setSubjectUuids(List<String> subjectUuids) {
    this.subjectUuids = subjectUuids;
  }

  public String getContractUuid() {
    return contractUuid;
  }

  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
  }

}
