/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	ReceipUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月23日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.receipt.common;

import java.util.Comparator;
import java.util.List;

import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.cre.controller.account.payment.model.BPayment;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 收款单工具类，提供一些与业务无关的方法
 * 
 * @author LiBin
 *
 */
public class ReceiptUtil {

  /** 从账款明细中取出发票代码拼接成字符串 */
  public static String getInvoiceCodeStr(List<Account> accs) {
    if (accs == null || accs.isEmpty()) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    for (Account acc : accs) {
      if (acc.getAcc2().getInvoice() != null && acc.getAcc2().getInvoice().getInvoiceCode() != null) {
        if (sb.length() > 0) {
          sb.append(",");
        }
        sb.append(acc.getAcc2().getInvoice().getInvoiceCode());
      }
    }
    return sb.toString();
  }

  /** 从账款明细中取出发票号码拼接成字符串 */
  public static String getInvoiceNumbertr(List<Account> accs) {
    if (accs == null || accs.isEmpty()) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    for (Account acc : accs) {
      if (acc.getAcc2().getInvoice() != null
          && acc.getAcc2().getInvoice().getInvoiceNumber() != null) {
        if (sb.length() > 0) {
          sb.append(",");
        }
        sb.append(acc.getAcc2().getInvoice().getInvoiceNumber());
      }
    }
    return sb.toString();
  }

  /** 根据账款起始时间对收款单账款明细进行排序 */
  public static void sortAccountLinesByBeginTime(BPayment payment) {
    if (payment == null || payment.getAccountLines() == null || payment.getAccountLines().isEmpty()) {
      return;
    }

    Collections.sort(payment.getAccountLines(), new Comparator<BPaymentAccountLine>() {
      @Override
      public int compare(BPaymentAccountLine o1, BPaymentAccountLine o2) {
        if (o1.getAcc1().getBeginTime() == null) {
          return -1;
        }
        if (o2.getAcc1().getBeginTime() == null) {
          return 1;
        }

        int result = o1.getAcc1().getBeginTime().compareTo(o2.getAcc1().getBeginTime());

        if (result != 0) {
          return result;
        }

        if (o1.getAcc1().getEndTime() == null) {
          return -1;
        }
        if (o2.getAcc1().getEndTime() == null) {
          return 1;
        }

        return o1.getAcc1().getBeginTime().compareTo(o2.getAcc1().getBeginTime());

      }
    });

  }

}
