/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-api
 * 文件名：	InvoiceStockState.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月1日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.invoice;

/**
 * 库存状态|枚举
 * 
 * @author LiBin
 * @since 1.7
 */
public enum BInvoiceStockState {
  /** 已入库 */
  instock("已入库"),
  /** 已领用 */
  received("已领用"),
  /** 已使用 */
  used("已使用"),
  /** 已作废 */
  aborted("已作废 ");

  private String caption;

  public String getCaption() {
    return caption;
  }

  BInvoiceStockState(String caption) {
    this.caption = caption;
  }
}
