/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BAccount.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月5日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

import com.hd123.m3.account.commons.Bill;
import com.hd123.m3.account.commons.InvoiceBill;
import com.hd123.m3.account.commons.SourceBill;

/**
 * @author chenganbang
 *
 */
public class Accounts {
  /* 未进行指定业务 */
  public static final String NONE_BILL_UUID = "-";
  public static final String NONE_BILL_NUMBER = "-";
  /* 未指定发票 */
  public static final String NONE_INVOICE_TYPE = "-";
  public static final String NONE_INVOICE_CODE = "-";
  public static final String NONE_INVOICE_NUMBER = "-";
  /* 不允许指定业务 */
  public static final String DISABLE_BILL_UUID = "~";
  public static final String DISABLE_BILL_NUMBER = "~";
  /* 未锁定 */
  public static final String NONE_LOCK_UUID = "-";
  public static final String NONE_LOCK_NUMBER = "-";
  public static final String NONE_LOCK_NAME = "-";

  public static final Bill NONE_BILL = new Bill(NONE_BILL_UUID, NONE_BILL_NUMBER);
  public static final InvoiceBill NONE_INVOICEBILL = new InvoiceBill(NONE_BILL_UUID,
      NONE_BILL_NUMBER, NONE_INVOICE_TYPE, NONE_INVOICE_CODE, NONE_INVOICE_NUMBER);

  public static final Bill DISABLE_BILL = new Bill(DISABLE_BILL_UUID, DISABLE_BILL_NUMBER);
  public static final InvoiceBill DISABLE_INVOICEBILL = new InvoiceBill(DISABLE_BILL_UUID,
      DISABLE_BILL_NUMBER, NONE_INVOICE_TYPE, NONE_INVOICE_CODE, NONE_INVOICE_NUMBER);

  public static final SourceBill NONE_LOCKER = new SourceBill(NONE_BILL_UUID, NONE_LOCK_NUMBER,
      NONE_LOCK_NAME);
}
