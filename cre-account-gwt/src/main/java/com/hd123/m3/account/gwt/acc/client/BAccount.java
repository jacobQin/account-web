/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccount.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-8 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.acc.client;

import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;

/**
 * @author chenwenfeng
 * 
 */
public class BAccount extends BEntity {
  private static final long serialVersionUID = -740026954824759909L;

  public static final String ORDER_BY_FIELD_SUBJECT = "subject";
  public static final String ORDER_BY_FIELD_SUBJECTUSAGE= "subjectUsage";
  public static final String ORDER_BY_FIELD_SOURCEBILLTYPE = "sourceBillType";
  public static final String ORDER_BY_FIELD_SOURCEBILLNUMBER = "sourceBillNumber";
  public static final String ORDER_BY_FIELD_CONTRACT = "contract";
  public static final String ORDER_BY_FIELD_CONTRACTTITLE = "contractTitle";
  public static final String ORDER_BY_FIELD_ACCOUNTUNIT = "accountUnit";
  public static final String ORDER_BY_FIELD_COUNTERPART = "counterpart";
  public static final String ORDER_BY_FIELD_TOTAL = "total";
  public static final String ORDER_BY_FIELD_BEGINTIME = "beginTime";

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

  public static final BBill NONE_BILL = new BBill(NONE_BILL_UUID, NONE_BILL_NUMBER);
  public static final BInvoiceBill NONE_INVOICEBILL = new BInvoiceBill(NONE_BILL_UUID,
      NONE_BILL_NUMBER, NONE_INVOICE_TYPE, NONE_INVOICE_CODE, NONE_INVOICE_NUMBER);

  public static final BBill DISABLE_BILL = new BBill(DISABLE_BILL_UUID, DISABLE_BILL_NUMBER);
  public static final BInvoiceBill DISABLE_INVOICEBILL = new BInvoiceBill(DISABLE_BILL_UUID,
      DISABLE_BILL_NUMBER, NONE_INVOICE_TYPE, NONE_INVOICE_CODE, NONE_INVOICE_NUMBER);

  public static final BSourceBill NONE_LOCKER = new BSourceBill(NONE_BILL_UUID, NONE_LOCK_NUMBER,
      NONE_LOCK_NAME);

  private BAcc1 acc1;
  private BAcc2 acc2;
  private BTotal originTotal;
  private BTotal total;

  public BAcc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(BAcc1 acc1) {
    this.acc1 = acc1;
  }

  public BAcc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(BAcc2 acc2) {
    this.acc2 = acc2;
  }

  public BTotal getOriginTotal() {
    return originTotal;
  }

  public void setOriginTotal(BTotal originTotal) {
    this.originTotal = originTotal;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }
}
