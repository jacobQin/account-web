/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceStandardBill.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月4日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client.biz;

import com.hd123.m3.commons.gwt.base.client.biz.BStandardBill;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票单据数据对象基类
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class BInvoiceStandardBill extends BStandardBill implements HasStore {

  private static final long serialVersionUID = -6994007297366023363L;

  private BUCN accountUnit;
  private String invoiceType;
  private String remark;
  private int sort;

  /** 结算单位 */
  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 票据类型 */
  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 发票类型，0-发票，1-收据 */
  public int getSort() {
    return sort;
  }

  public void setSort(int sort) {
    this.sort = sort;
  }

  @Override
  public BUCN getStore() {
    return accountUnit;
  }
}
