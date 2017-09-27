/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountPermResourceDef.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月2日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common;

/**
 * 账务权限资源标志定义
 * 
 * @author LiBin
 *
 */
public class AccountPermResourceDef {
  /** 账单 */
  public static final String RESOURCE_STATMENT = "account.statement";
  /** 出账 */
  public static final String RESOURCE_ACCOUNTSETTLE = "account.accountSettle";
  /** 付款发票登记 */
  public static final String RESOURCE_PAYINVOICEREG = "account.payInvoiceReg";
  /** 收款发票登记 */
  public static final String RESOURCE_RECINVOICEREG = "account.invoice.reg.rec";
  /** 付款 */
  public static final String RESOURCE_PAYMENT = "account.payment.pay";
  /** 收款单 */
  public static final String RESOURCE_RECEIPT = "account.payment.rec";
  /** 账单调整 */
  public static final String RESOURCE_STATEMENTADJUST = "account.statementAdjust";
  /** 科目收付情况 */
  public static final String RESOURCE_ACCOUNTDEFRAYAL = "account.report.accountdefrayal";
  /** 核算主体单 */
  public static final String RESOURCE_ACCOBJECTBILL = "account.accobjectbill";
  /** 核算主体 */
  public static final String RESOURCE_ACCOBJECT = "account.accobject";
  /** 费用单 */
  public static final String RESOURCE_FEE = "account.fee";
  /**付款方式提成比例*/
  public static final String RESOURCE_PAYMENTREDUCTIONRATIO = "account.paymentType.reductionRatio";
  /** 预存款单 */
  public static final String RESOURCE_DEPOSIT_RECEIPT = "account.recDeposit";
  /** 预存款转移单 */
  public static final String RESOURCE_DEPOSIT_MOVE = "account.recDepositMove";
  /** 预付款单 */
  public static final String RESOURCE_DEPOSIT_PAYMENT = "account.payDeposit";
  /** 预存款还款单 */
  public static final String RESOURCE_DEPOSIT_RECDEPOSITREPAYMENT = "account.recDepositRepayment";
  /**发票入库单 */
  public static final String RESOURCE_INVOICE_INSTOCK = "account.invoice.instock";
  /**发票领用单 */
  public static final String RESOURCE_INVOICE_RECEIVE = "account.invoice.receive";
  
}
