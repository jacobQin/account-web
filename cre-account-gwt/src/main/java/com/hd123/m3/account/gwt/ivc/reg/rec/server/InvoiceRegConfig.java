/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	InvoiceRegConfig.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月8日 - mengyinkun - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.server;

/**
 * @author mengyinkun
 * 
 */
public class InvoiceRegConfig {
  private static InvoiceRegConfig instance = null;

  public static InvoiceRegConfig getInstance() {
    if (instance == null)
      instance = new InvoiceRegConfig();
    return instance;
  }

  /** 是否启用外部发票系统 */
  private Boolean isEnabledExtInvoiceSystem = false;

  public Boolean getIsEnabledExtInvoiceSystem() {
    return isEnabledExtInvoiceSystem;
  }

  public void setIsEnabledExtInvoiceSystem(Boolean isEnabledExtInvoiceSystem) {
    this.isEnabledExtInvoiceSystem = isEnabledExtInvoiceSystem;
  }
}
