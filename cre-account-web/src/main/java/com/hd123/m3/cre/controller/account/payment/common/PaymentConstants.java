/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	PaymentConstants.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月14日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.common;

import com.hd123.m3.cre.controller.account.AccountBasicConstants;

/**
 * 收付款单常量定义
 * 
 * @author LiBin
 *
 */
public class PaymentConstants  extends AccountBasicConstants {

  /** =========================查询字段============================ */
  /** 单号(String) */
  public static final String FILTER_PAYMENTDATE = "paymentDate";

  /** =========================排序字段============================ **/
  /** 收款日期 */
  public static final String SORT_PAYMENTDATE = "paymentDate";
  /** 本次应收金额 */
  public static final String SORT_UNPAYEDTOTAL = "unpayedTotal";
  /** 实收金额 */
  public static final String SORT_TOTAL = "total";
  
}