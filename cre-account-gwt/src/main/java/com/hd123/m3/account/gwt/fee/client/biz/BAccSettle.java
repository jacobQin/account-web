/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BAccSettle.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-12 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.biz;

import java.io.Serializable;
import java.util.Date;

/**
 * 合同账期记录
 * 
 * @author liuguilin
 * 
 */
public class BAccSettle implements Serializable {
  private static final long serialVersionUID = 8435456172827107435L;

  private boolean exists;
  private Date lstCalcEndDate;

  public BAccSettle() {

  }

  public BAccSettle(boolean exists, Date lstCalcEndDate) {
    this.exists = exists;
    this.lstCalcEndDate = lstCalcEndDate;
  }

  public boolean isExists() {
    return exists;
  }

  public void setExists(boolean exists) {
    this.exists = exists;
  }

  public Date getLstCalcEndDate() {
    return lstCalcEndDate;
  }

  public void setLstCalcEndDate(Date lstCalcEndDate) {
    this.lstCalcEndDate = lstCalcEndDate;
  }

}
