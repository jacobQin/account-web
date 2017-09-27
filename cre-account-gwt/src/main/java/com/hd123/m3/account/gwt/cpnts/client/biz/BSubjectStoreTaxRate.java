/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BSubjectStoreTaxRate.java
 * 模块说明：	
 * 修改历史：
 * 2016年5月31日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import java.io.Serializable;

import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 项目税率映射明细表
 * 
 * @author lixiaohong
 *
 */
public class BSubjectStoreTaxRate implements Serializable {
  private static final long serialVersionUID = 3350755952720302751L;
  private BUCN store;
  private BTaxRate taxRate;
  private String remark;

  public BUCN getStore() {
    return store;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
