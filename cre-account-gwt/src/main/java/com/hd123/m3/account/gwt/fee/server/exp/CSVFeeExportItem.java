/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CSVFeeImportItem.java
 * 模块说明：	
 * 修改历史：
 * 2015-9-25 - huangjunxian - 创建。
 */
package com.hd123.m3.account.gwt.fee.server.exp;

import com.alex.csvhelper.CsvBean;
import com.alex.csvhelper.annotation.Column;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.subject.Subject;

/**
 * 费用单导出Bean
 * 
 * @author huangjunxian
 * 
 */
public class CSVFeeExportItem extends CsvBean {

  private static final long serialVersionUID = -1145249366092364320L;

  public static final String COL_项目代码 = "项目代码";
  public static final String COL_项目名称 = "项目名称";
  public static final String COL_合同号 = "合同号";
  public static final String COL_店招 = "店招";
  public static final String COL_记账日期 = "记账日期";
  public static final String COL_开始日期 = "开始日期";
  public static final String COL_截止日期 = "截止日期";
  public static final String COL_科目代码 = "科目代码";
  public static final String COL_科目名称 = "科目名称";
  public static final String COL_收款金额 = "收款金额";
  public static final String COL_是否开票 = "是否开票";
  public static final String[] COLUMNS = new String[] {
      COL_项目代码, COL_项目名称, COL_合同号, COL_店招, COL_记账日期, COL_开始日期, COL_截止日期, COL_科目代码, COL_科目名称,
      COL_收款金额, COL_是否开票 };

  @Column(name = COL_项目代码, nullable = true, needTab = true)
  private String accountUnitCode;
  @Column(name = COL_项目名称, nullable = true, needTab = true)
  private String accountUnitName;
  @Column(name = COL_合同号, nullable = true, needTab = true)
  private String contractNumber;
  @Column(name = COL_店招, nullable = true, needTab = true)
  private String contractName;
  @Column(name = COL_记账日期, nullable = true, needTab = true)
  private String accountDate;
  @Column(name = COL_开始日期, nullable = true, needTab = true)
  private String beginDate;
  @Column(name = COL_截止日期, nullable = true, needTab = true)
  private String endDate;
  @Column(name = COL_科目代码, nullable = true, needTab = true)
  private String subjectCode;
  @Column(name = COL_科目名称, nullable = true, needTab = true)
  private String subjectName;
  @Column(name = COL_收款金额, nullable = true, needTab = true)
  private String total;
  @Column(name = COL_是否开票, nullable = true, needTab = true)
  private String invoice;

  private Contract contract;
  private Subject subject;

  public String getAccountUnitCode() {
    return accountUnitCode;
  }

  public void setAccountUnitCode(String accountUnitCode) {
    this.accountUnitCode = accountUnitCode;
  }

  public String getAccountUnitName() {
    return accountUnitName;
  }

  public void setAccountUnitName(String accountUnitName) {
    this.accountUnitName = accountUnitName;
  }

  public String getContractNumber() {
    return contractNumber;
  }

  public void setContractNumber(String contractNumber) {
    this.contractNumber = contractNumber;
  }

  public String getContractName() {
    return contractName;
  }

  public void setContractName(String contractName) {
    this.contractName = contractName;
  }

  public String getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(String accountDate) {
    this.accountDate = accountDate;
  }

  public String getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(String beginDate) {
    this.beginDate = beginDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
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

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public Contract getContract() {
    return contract;
  }

  public void setContract(Contract contract) {
    this.contract = contract;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public String getInvoice() {
    return invoice;
  }

  public void setInvoice(String invoice) {
    this.invoice = invoice;
  }

}
