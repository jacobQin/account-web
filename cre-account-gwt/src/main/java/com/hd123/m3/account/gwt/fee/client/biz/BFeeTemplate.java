/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BFeeTemplate.java
 * 模块说明：	
 * 修改历史：
 * 2015-9-11 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 导出模板
 * 
 * @author liuguilin
 * 
 */
public class BFeeTemplate implements Serializable {

  private static final long serialVersionUID = 2963107548848704719L;

  private BUCN accountUnit;
  private Date accountDate;
  private Date beginDate;
  private Date endDate;
  private BUCN subject;
  private BigDecimal total;

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
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

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

}
