/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceRegOption.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月16日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg.config;

import java.io.Serializable;

/**
 * 收款发票登记单配置
 * 
 * @author LiBin
 *
 */
public class InvoiceRegOption implements Serializable {

  private static final long serialVersionUID = -1205891828860906977L;

  private boolean useInvoiceStock;
  private boolean allowSplitReg;

  /** 是否启用发票库存 */
  public boolean isUseInvoiceStock() {
    return useInvoiceStock;
  }

  public void setUseInvoiceStock(boolean useInvoiceStock) {
    this.useInvoiceStock = useInvoiceStock;
  }

  /** 是否启用账款可以多次开票 */
  public boolean isAllowSplitReg() {
    return allowSplitReg;
  }

  public void setAllowSplitReg(boolean allowSplitReg) {
    this.allowSplitReg = allowSplitReg;
  }

}
