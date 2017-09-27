/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAccFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.rpc;

import java.util.Date;

import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * 账款查询filter
 * 
 * @author huangjunxian
 * 
 */
public class StatementAccFilter extends QueryFilter implements StatementUrlParams.AccountFilter {

  private static final long serialVersionUID = 300100L;

  private String statementUuid;
  private String sourceBillType;
  private String sourceBillNumber;
  private String subjectUuid;
  private String subjectCode;
  private String subjectName;
  private Date beginDate;
  private Date endDate;
  private String counterpartUuid;
  private String contractUuid;

  public String getStatementUuid() {
    return statementUuid;
  }

  public void setStatementUuid(String statementUuid) {
    this.statementUuid = statementUuid;
  }

  public String getSourceBillType() {
    return sourceBillType;
  }

  public void setSourceBillType(String sourceBillType) {
    this.sourceBillType = sourceBillType;
  }

  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  public String getSubjectUuid() {
    return subjectUuid;
  }

  public void setSubjectUuid(String subjectUuid) {
    this.subjectUuid = subjectUuid;
  }

  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
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

  public BUCN getSubject() {
    if (subjectUuid == null || "".equals(subjectUuid))
      return null;
    return new BUCN(subjectUuid, subjectCode, subjectName);
  }

  public void setSubject(BUCN src) {
    if (src == null) {
      subjectUuid = null;
      subjectCode = null;
      subjectName = null;
    } else {
      subjectUuid = src.getUuid();
      subjectCode = src.getCode();
      subjectName = src.getName();
    }
  }
}
