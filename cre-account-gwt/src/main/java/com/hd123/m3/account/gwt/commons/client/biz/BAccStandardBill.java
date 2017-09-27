/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BStandardBill.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

/**
 * @author chenwenfeng
 * 
 */
public class BAccStandardBill extends com.hd123.m3.commons.gwt.base.client.biz.BStandardBill {
  private static final long serialVersionUID = 3009627760672737366L;

  private String settleNo;
  private String remark;

  public String getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(String settleNo) {
    this.settleNo = settleNo;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
