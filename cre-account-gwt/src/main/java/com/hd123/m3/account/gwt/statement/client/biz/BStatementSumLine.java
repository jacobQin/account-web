/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BStatementSumLine.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.biz;

import java.math.BigDecimal;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;

/**
 * 账单科目汇总明细
 * 
 * @author huangjunxian
 * 
 */
public class BStatementSumLine extends BEntity {

  private static final long serialVersionUID = 300100L;

  private String subjectUuid;
  private String subjectCode;
  private String subjectName;
  private int direction;
  private BTotal total;
  private BigDecimal needInvoice;
  private BigDecimal settled;
  private BigDecimal invoiced;

  /** 科目uuid */
  public String getSubjectUuid() {
    return subjectUuid;
  }

  public void setSubjectUuid(String subjectUuid) {
    this.subjectUuid = subjectUuid;
  }

  /** 科目代码 */
  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  /** 科目名称 */
  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  /** 收付方向 */
  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  /** 总金额 */
  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  /** 开票金额 */
  public BigDecimal getNeedInvoice() {
    return needInvoice;
  }

  public void setNeedInvoice(BigDecimal needInvoice) {
    this.needInvoice = needInvoice;
  }

  /** 已结算金额 */
  public BigDecimal getSettled() {
    return settled;
  }

  public void setSettled(BigDecimal settled) {
    this.settled = settled;
  }

  /** 已开票金额 */
  public BigDecimal getInvoiced() {
    return invoiced;
  }

  public void setInvoiced(BigDecimal invoiced) {
    this.invoiced = invoiced;
  }

}
