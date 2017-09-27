/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAcc2.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.acc.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;

/**
 * @author chenwenfeng
 * 
 */
public class BAcc2 implements Serializable {
  private static final long serialVersionUID = -3101967860129513779L;

  private String id;
  private BBill statement;
  private BInvoiceBill invoice;
  private BBill payment;
  private BSourceBill locker;
  private Date lastPayDate;
  private int graceDays = 0;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BBill getStatement() {
    return statement;
  }

  public void setStatement(BBill statement) {
    this.statement = statement;
  }

  public BInvoiceBill getInvoice() {
    return invoice;
  }

  public void setInvoice(BInvoiceBill invoice) {
    this.invoice = invoice;
  }

  public BBill getPayment() {
    return payment;
  }

  public void setPayment(BBill payment) {
    this.payment = payment;
  }

  public BSourceBill getLocker() {
    return locker;
  }

  public void setLocker(BSourceBill locker) {
    this.locker = locker;
  }

  public Date getLastPayDate() {
    return lastPayDate;
  }

  public void setLastPayDate(Date lastPayDate) {
    this.lastPayDate = lastPayDate;
  }

  public int getGraceDays() {
    return graceDays;
  }

  public void setGraceDays(int graceDays) {
    this.graceDays = graceDays;
  }

  public BAcc2 clone() {
    BAcc2 r = new BAcc2();
    r.id = id;
    r.statement = new BBill(statement.getBillUuid(), statement.getBillNumber());
    r.invoice = new BInvoiceBill(invoice.getBillUuid(), invoice.getBillNumber(),
        invoice.getInvoiceType(), invoice.getInvoiceCode(), invoice.getInvoiceNumber());
    r.payment = new BBill(payment.getBillUuid(), payment.getBillNumber());
    r.locker = new BSourceBill(locker.getBillUuid(), locker.getBillNumber(), locker.getBillType());
    r.lastPayDate = lastPayDate;
    r.graceDays = graceDays;

    return r;
  }

  public String toId() {
    ArrayList<String> list = new ArrayList<String>();
    list.add(statement == null ? BBill.NONE_ID : statement.id());
    list.add(invoice == null ? BBill.NONE_ID : invoice.id());
    list.add(payment == null ? BBill.NONE_ID : payment.id());
    list.add(locker == null ? BBill.NONE_ID : locker.id());
    return CollectionUtil.toString(list);
  }
}
