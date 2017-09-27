/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	ActionName.java
 * 模块说明：	
 * 修改历史：
 * 2015年9月23日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client;

/**
 * @author lixiaohong
 * 
 */
public class ActionName {
  /** 改变项目 */
  public static final String CHANGE_STORE = "change_store";
  /** 改变费用类型 */
  public static final String CHANGE_INVOICE_TYPE = "change_invoice_type";

  /** 动作:票据交换发生改变 */
  public static final String CHANGE_EXCHANGE_TYPE_ACTION = "changeExchangeTpyeAction";

  /** 动作:发票交换类型发生改变 */
  public static final String CHANGE_INVTOINVTYPE_ACTION = "changeInvToInvTypeAction";

  /** 动作:发票明细发生改变，通知刷新账款 */
  public static final String CHANGE_INVOICEDETAILS_ACTION = "changeInvoiceDetailsAction";

}
