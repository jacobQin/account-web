/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	ReceipUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月23日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.server;

import java.util.List;

import com.hd123.m3.account.service.acc.Account;

/**
 * 收款单工具类，提供一些与业务无关的方法
 * 
 * @author LiBin
 *
 */
public class ReceipUtil {

  /**从账款明细中取出发票代码拼接成字符串*/
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
  
  /**从账款明细中取出发票号码拼接成字符串*/
  public static String getInvoiceNumbertr(List<Account> accs) {
    if (accs == null || accs.isEmpty()) {
      return null;
    }

    StringBuilder sb = new StringBuilder();
    for (Account acc : accs) {
      if (acc.getAcc2().getInvoice() != null && acc.getAcc2().getInvoice().getInvoiceNumber() != null) {
        if (sb.length() > 0) {
          sb.append(",");
        }
        sb.append(acc.getAcc2().getInvoice().getInvoiceNumber());
      }
    }
    return sb.toString();
  }
  
}
