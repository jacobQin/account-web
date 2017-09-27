/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	StatementPermDef.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月18日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement;

/**
 * 账单模块相关权限定义
 * 
 * @author chenganbang
 *
 */
public class StatementPermDef {
  /** 修改账款权 */
  public static final String RESOURCE_MODIFY_ACCOUNT = "account.statement.modifyAccount";
  /** 付款发票登记(新建) */
  public static final String RESOURCE_PAYINVOICEREG_CREATE = "account.payInvoiceReg.create";
  /** 收款发票登记(新建) */
  public static final String RESOURCE_RECINVOICEREG_CREATE = "account.invoice.reg.rec.create";
  /** 付款(新建) */
  public static final String RESOURCE_PAYMENT_CREATE = "account.payment.pay.create";
  /** 收款(新建) */
  public static final String RESOURCE_RECEIPT_CREATE = "account.payment.rec.create";
}
