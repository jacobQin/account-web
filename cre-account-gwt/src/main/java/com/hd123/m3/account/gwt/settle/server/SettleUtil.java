/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	SortUtil.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-18 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.settle.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountInvoice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountPayment;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.util.client.OrderDir;

/**
 * 内存排序工具类
 * 
 * @author subinzhu
 * 
 */
public class SettleUtil {

  /** 账款内存排序 */
  public static List<BAccount> sortAccountList(List<BAccount> values, Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    if (sortField == null)
      return values;
    List<BAccount> result = new ArrayList<BAccount>(values);
    Collections.sort(result, new Comparator<BAccount>() {
      public int compare(BAccount o1, BAccount o2) {
        if (sortField.equals(BAccount.ORDER_BY_FIELD_SUBJECT)) {
          return SettleUtil.compare(o1.getAcc1().getSubject().getCode(), o2.getAcc1().getSubject()
              .getCode(), sortDir);
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_SUBJECTUSAGE)) {
          return SettleUtil.compare(o1.getAcc1().getUsageName(), o2.getAcc1().getUsageName(),
              sortDir);
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_SOURCEBILLTYPE)) {
          return SettleUtil.compare(o1.getAcc1().getSourceBill().getBillType(), o2.getAcc1()
              .getSourceBill().getBillType(), sortDir);
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_SOURCEBILLNUMBER)) {
          return SettleUtil.compare(o1.getAcc1().getSourceBill().getBillNumber(), o2.getAcc1()
              .getSourceBill().getBillNumber(), sortDir);
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_CONTRACT)) {
          return SettleUtil.compare(o1.getAcc1().getContract().getCode(), o2.getAcc1()
              .getContract().getCode(), sortDir);
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_CONTRACTTITLE)) {
          if (o1.getAcc1().getContract() == null || o2.getAcc1().getContract() == null) {
            return SettleUtil.compare(o1.getAcc1().getContract(), o2.getAcc1().getContract(),
                sortDir);
          } else {
            return SettleUtil.compare(o1.getAcc1().getContract().getName(), o2.getAcc1()
                .getContract().getName(), sortDir);
          }
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_ACCOUNTUNIT)) {
          return SettleUtil.compare(o1.getAcc1().getAccountUnit().getCode(), o2.getAcc1()
              .getAccountUnit().getCode(), sortDir);
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_COUNTERPART)) {
          return SettleUtil.compare(o1.getAcc1().getCounterpart().getCode(), o2.getAcc1()
              .getCounterpart().getCode(), sortDir);
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_TOTAL)) {
          return SettleUtil.compare(o1.getTotal().getTotal(), o2.getTotal().getTotal(), sortDir);
        } else if (sortField.equals(BAccount.ORDER_BY_FIELD_BEGINTIME)) {
          return SettleUtil.compare(o1.getAcc1().getBeginTime(), o2.getAcc1().getBeginTime(),
              sortDir);
        } else {
          return 0;
        }
      }
    });
    return result;
  }

  /** 发票登记单内存排序 */
  public static List<BAccountInvoice> sortAccountInvoiceList(List<BAccountInvoice> values,
      Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    if (sortField == null)
      return values;
    List<BAccountInvoice> result = new ArrayList<BAccountInvoice>(values);
    Collections.sort(result, new Comparator<BAccountInvoice>() {
      public int compare(BAccountInvoice o1, BAccountInvoice o2) {
        if (sortField.equals(BAccountInvoice.ORDER_BY_FIELD_BILLNUMBER)) {
          return SettleUtil.compare(o1.getBillNumber(), o2.getBillNumber(), sortDir);
        } else if (sortField.equals(BAccountInvoice.ORDER_BY_FIELD_ACCOUNTUNIT)) {
          return SettleUtil.compare(o1.getAccounts().get(0).getAcc1().getAccountUnit().getCode(),
              o2.getAccounts().get(0).getAcc1().getAccountUnit().getCode(), sortDir);
        } else if (sortField.equals(BAccountInvoice.ORDER_BY_FIELD_COUNTERPART)) {
          return SettleUtil.compare(o1.getCounterpart().getCode(), o2.getCounterpart().getCode(),
              sortDir);
        } else if (sortField.equals(BAccountInvoice.ORDER_BY_FIELD_TOTAL)) {
          return SettleUtil.compare(o1.getTotal().getTotal(), o2.getTotal().getTotal(), sortDir);
        } else if (sortField.equals(BAccountInvoice.ORDER_BY_FIELD_INVOICECODE)) {
          return SettleUtil.compare(o1.getInvoiceCode(), o2.getInvoiceCode(), sortDir);
        } else if (sortField.equals(BAccountInvoice.ORDER_BY_FIELD_INVOICENUMBER)) {
          return SettleUtil.compare(o1.getInvoiceNumber(), o2.getInvoiceNumber(), sortDir);
        } else if (sortField.equals(BAccountInvoice.ORDER_BY_FIELD_REGDATE)) {
          return SettleUtil.compare(o1.getInvoiceRegDate(), o2.getInvoiceRegDate(), sortDir);
        } else {
          return 0;
        }
      }
    });
    return result;
  }

  /** 收付款通知单内存排序 */
  public static List<BAccountNotice> sortAccountNoticeList(Collection<BAccountNotice> values,
      Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    List<BAccountNotice> result = new ArrayList<BAccountNotice>(values);
    if (sortField == null) {
      result.addAll(values);
      return result;
    }
    Collections.sort(result, new Comparator<BAccountNotice>() {
      public int compare(BAccountNotice o1, BAccountNotice o2) {
        if (sortField.equals(BAccountNotice.ORDER_BY_FIELD_BILLNUMBER)) {
          return SettleUtil.compare(o1.getBillNumber(), o2.getBillNumber(), sortDir);
        } else if (sortField.equals(BAccountNotice.ORDER_BY_FIELD_ACCOUNTUNIT)) {
          return SettleUtil.compare(o1.getAccounts().get(0).getAcc1().getAccountUnit().getCode(),
              o2.getAccounts().get(0).getAcc1().getAccountUnit().getCode(), sortDir);
        } else if (sortField.equals(BAccountNotice.ORDER_BY_FIELD_COUNTERPART)) {
          return SettleUtil.compare(o1.getAccounts().get(0).getAcc1().getCounterpart().getCode(),
              o2.getAccounts().get(0).getAcc1().getCounterpart().getCode(), sortDir);
        } else if (sortField.equals(BAccountNotice.ORDER_BY_FIELD_TOTAL)) {
          return SettleUtil.compare(o1.getTotal().getTotal(), o2.getTotal().getTotal(), sortDir);
        } else {
          return 0;
        }
      }
    });
    return result;
  }

  /** 来源单据内存排序 */
  public static List<BAccountSourceBill> sortAccountSourceBillList(List<BAccountSourceBill> values,
      Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    if (sortField == null)
      return values;

    List<BAccountSourceBill> result = new ArrayList<BAccountSourceBill>(values);
    Collections.sort(result, new Comparator<BAccountSourceBill>() {
      public int compare(BAccountSourceBill o1, BAccountSourceBill o2) {
        if (sortField.equals(BAccountSourceBill.ORDER_BY_FIELD_SOURCEBILLTYPE)) {
          return SettleUtil.compare(o1.getSourceBill().getBillType(), o2.getSourceBill()
              .getBillType(), sortDir);
        } else if (sortField.equals(BAccountSourceBill.ORDER_BY_FIELD_SOURCEBILLNUMBER)) {
          return SettleUtil.compare(o1.getSourceBill().getBillNumber(), o2.getSourceBill()
              .getBillNumber(), sortDir);
        } else if (sortField.equals(BAccountSourceBill.ORDER_BY_FIELD_ACCOUNTUNIT)) {
          return SettleUtil.compare(o1.getAccounts().get(0).getAcc1().getAccountUnit().getCode(),
              o2.getAccounts().get(0).getAcc1().getAccountUnit().getCode(), sortDir);
        } else if (sortField.equals(BAccountSourceBill.ORDER_BY_FIELD_COUNTERPART)) {
          return SettleUtil.compare(o1.getCounterpart().getCode(), o2.getCounterpart().getCode(),
              sortDir);
        } else if (sortField.equals(BAccountSourceBill.ORDER_BY_FIELD_CONTRACT)) {
          return SettleUtil
              .compare(o1.getContract().getCode(), o2.getContract().getCode(), sortDir);
        } else if (sortField.equals(BAccountSourceBill.ORDER_BY_FIELD_CONTRACTTITLE)) {
          if (o1.getContract() == null || o2.getContract() == null) {
            return SettleUtil.compare(o1.getContract(), o2.getContract(), sortDir);
          } else {
            return SettleUtil.compare(o1.getContract().getName(), o2.getContract().getName(),
                sortDir);
          }
        } else if (sortField.equals(BAccountSourceBill.ORDER_BY_FIELD_TOTAL)) {
          return SettleUtil.compare(o1.getTotal().getTotal(), o2.getTotal().getTotal(), sortDir);
        } else {
          return 0;
        }
      }
    });
    return result;
  }

  /** 账单内存排序 */
  public static List<BAccountStatement> sortAccountStatementList(
      Collection<BAccountStatement> values, Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    List<BAccountStatement> result = new ArrayList<BAccountStatement>(values);
    if (sortField == null) {
      result.addAll(values);
      return result;
    }

    Collections.sort(result, new Comparator<BAccountStatement>() {
      public int compare(BAccountStatement o1, BAccountStatement o2) {
        if (sortField.equals(BAccountStatement.ORDER_BY_FIELD_BILLNUMBER)) {
          return SettleUtil.compare(o1.getBillNumber(), o2.getBillNumber(), sortDir);
        } else if (sortField.equals(BAccountStatement.ORDER_BY_FIELD_CONTRACT)) {
          return SettleUtil
              .compare(o1.getContract().getCode(), o2.getContract().getCode(), sortDir);
        } else if (sortField.equals(BAccountStatement.ORDER_BY_FIELD_CONTRACTTITLE)) {
          if (o1.getContract() == null || o2.getContract() == null) {
            return SettleUtil.compare(o1.getContract(), o2.getContract(), sortDir);
          } else {
            return SettleUtil.compare(o1.getContract().getName(), o2.getContract().getName(),
                sortDir);
          }
        } else if (sortField.equals(BAccountStatement.ORDER_BY_FIELD_ACCOUNTUNIT)) {
          return SettleUtil.compare(o1.getAccounts().get(0).getAcc1().getAccountUnit().getCode(),
              o2.getAccounts().get(0).getAcc1().getAccountUnit().getCode(), sortDir);
        } else if (sortField.equals(BAccountStatement.ORDER_BY_FIELD_COUNTERPART)) {
          return SettleUtil.compare(o1.getCounterpart().getCode(), o2.getCounterpart().getCode(),
              sortDir);
        } else if (sortField.equals(BAccountStatement.ORDER_BY_FIELD_TOTAL)) {
          return SettleUtil.compare(o1.getTotal().getTotal(), o2.getTotal().getTotal(), sortDir);
        } else if (sortField.equals(BAccountStatement.ORDER_BY_FIELD_ACCOUNTTIME)) {
          return SettleUtil.compare(o1.getAccountTime(), o2.getAccountTime(), sortDir);
        } else if (sortField.equals(BAccountStatement.ORDER_BY_FIELD_SETTLENO)) {
          return SettleUtil.compare(o1.getSettleNo(), o2.getSettleNo(), sortDir);
        } else {
          return 0;
        }
      }
    });
    return result;
  }

  /** 账单内存排序 */
  public static List<BAccountPayment> sort(Collection<BAccountPayment> values, Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    List<BAccountPayment> result = new ArrayList<BAccountPayment>(values);
    if (sortField == null) {
      result.addAll(values);
      return result;
    }

    Collections.sort(result, new Comparator<BAccountPayment>() {
      public int compare(BAccountPayment o1, BAccountPayment o2) {
        if (sortField.equals(BAccountPayment.ORDER_BY_FIELD_BILLNUMBER)) {
          return SettleUtil.compare(o1.getBillNumber(), o2.getBillNumber(), sortDir);
        } else if (sortField.equals(BAccountPayment.ORDER_BY_FIELD_CONTRACT)) {
          return SettleUtil
              .compare(o1.getContract().getCode(), o2.getContract().getCode(), sortDir);
        } else if (sortField.equals(BAccountPayment.ORDER_BY_FIELD_CONTRACTTITLE)) {
          if (o1.getContract() == null || o2.getContract() == null) {
            return SettleUtil.compare(o1.getContract(), o2.getContract(), sortDir);
          } else {
            return SettleUtil.compare(o1.getContract().getName(), o2.getContract().getName(),
                sortDir);
          }
        } else if (sortField.equals(BAccountPayment.ORDER_BY_FIELD_ACCOUNTUNIT)) {
          return SettleUtil.compare(o1.getAccounts().get(0).getAcc1().getAccountUnit().getCode(),
              o2.getAccounts().get(0).getAcc1().getAccountUnit().getCode(), sortDir);
        } else if (sortField.equals(BAccountPayment.ORDER_BY_FIELD_COUNTERPART)) {
          return SettleUtil.compare(o1.getCounterpart().getCode(), o2.getCounterpart().getCode(),
              sortDir);
        } else if (sortField.equals(BAccountPayment.ORDER_BY_FIELD_TOTAL)) {
          return SettleUtil.compare(o1.getTotal().getTotal(), o2.getTotal().getTotal(), sortDir);
        } else {
          return 0;
        }
      }
    });
    return result;
  }

  private static int compare(Object c1, Object c2, OrderDir sortDir) {
    if (c1 == null && c2 == null)
      return 0;
    else if (c1 == null)
      return sortDir == OrderDir.desc ? 1 : -1;
    else if (c2 == null)
      return sortDir == OrderDir.desc ? -1 : 1;

    int compare = 0;
    if (c1 instanceof String && c2 instanceof String) {
      compare = ((String) c1).compareTo(((String) c2));
    } else if (c1 instanceof Date && c2 instanceof Date) {
      compare = ((Date) c1).compareTo(((Date) c2));
    } else if (c1 instanceof BigDecimal && c2 instanceof BigDecimal) {
      compare = ((BigDecimal) c1).compareTo(((BigDecimal) c2));
    }
    if (sortDir == OrderDir.desc)
      compare *= -1;

    return compare;
  }
}
