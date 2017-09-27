/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceRegConfig.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月14日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.biz;

import java.io.Serializable;

/**
 * 发票登记单配置项
 * 
 * @author chenrizhang
 *
 */
public class BInvoiceRegOptions implements Serializable {
  private static final long serialVersionUID = 1650562549466089895L;

  private boolean allowSplitReg;
  private boolean useInvoiceStock;

  /** 是否允许多次登记 */
  public boolean isAllowSplitReg() {
    return allowSplitReg;
  }

  public void setAllowSplitReg(boolean allowSplitReg) {
    this.allowSplitReg = allowSplitReg;
  }

  /** 是否使用发票库存 */
  public boolean isUseInvoiceStock() {
    return useInvoiceStock;
  }

  public void setUseInvoiceStock(boolean useInvoiceStock) {
    this.useInvoiceStock = useInvoiceStock;
  }

}
