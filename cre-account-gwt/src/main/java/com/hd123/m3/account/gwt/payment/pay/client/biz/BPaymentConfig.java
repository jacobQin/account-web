/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BPaymentConfig.java
 * 模块说明：	
 * 修改历史：
 * 2015-9-6 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.biz;

import java.io.Serializable;

/**
 * 付款单配置
 * 
 * @author liuguilin
 * 
 */
public class BPaymentConfig implements Serializable {

  private static final long serialVersionUID = 4925848561060876178L;

  private boolean showTax;
  private boolean useInvoiceStock;

  public boolean isShowTax() {
    return showTax;
  }

  public void setShowTax(boolean showTax) {
    this.showTax = showTax;
  }

  /** 是否启用发票库存 */
  public boolean isUseInvoiceStock() {
    return useInvoiceStock;
  }

  public void setUseInvoiceStock(boolean useInvoiceStock) {
    this.useInvoiceStock = useInvoiceStock;
  }
}
