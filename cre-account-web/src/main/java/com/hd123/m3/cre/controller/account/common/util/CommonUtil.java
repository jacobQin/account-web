/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	CommonUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月11日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.util;

import java.util.List;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.account.service.freeze.Freeze;
import com.hd123.m3.account.service.invoice.InvoiceReg;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchange;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.paymentnotice.PaymentNotice;
import com.hd123.m3.sales.service.themeactivity.ThemeActivity;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 通用工具类
 * 
 * @author LiBin
 *
 */
public class CommonUtil {
  
  /**
   * 判断UCN列表中是否包含指定的UCN对象
   * @param sources
   *        源UCN列表，为空或者null将直接返回false。
   * @param source
   *        需要判断的对象，为null将直接返回false。
   * @return true表示包含指定对象，false表示不包含指定的对象。
   */
  public static boolean isContains(List<UCN> sources,UCN source) {
    if (sources == null || sources.isEmpty() || source == null) {
      return false;
    }

    for (UCN s : sources) {
      if (s.equals(source)) {
        return true;
      }
    }

    return false;
  }
  
  /**取得账款来源单据类型*/
  public static List<BillType> getBillTypes() {
    // 排除掉的单据类型
    BillType freeze = new BillType();// 账款冻结单
    freeze.setClassName(Freeze.class.getName());
    freeze.setDirection(0);
    BillType payDeposit = new BillType();// 预付款单
    payDeposit.setClassName(Deposit.class.getName());
    payDeposit.setDirection(Direction.PAYMENT);
    BillType receiptDeposit = new BillType();// 预存款单
    receiptDeposit.setClassName(Deposit.class.getName());
    receiptDeposit.setDirection(Direction.RECEIPT);
    BillType payDepositRepayment = new BillType();// 预付款还款单
    payDepositRepayment.setClassName(DepositRepayment.class.getName());
    payDepositRepayment.setDirection(Direction.PAYMENT);
    BillType receiptDepositRepayment = new BillType();// 预存款还款单
    receiptDepositRepayment.setClassName(DepositRepayment.class.getName());
    receiptDepositRepayment.setDirection(Direction.RECEIPT);
    BillType payDepositMove = new BillType();// 预付款转移单
    payDepositMove.setClassName(DepositMove.class.getName());
    payDepositMove.setDirection(Direction.PAYMENT);
    BillType receiptDepositMove = new BillType();// 预存款转移单
    receiptDepositMove.setClassName(DepositMove.class.getName());
    receiptDepositMove.setDirection(Direction.RECEIPT);
    BillType notice = new BillType();// 收付款通知单
    notice.setClassName(PaymentNotice.class.getName());
    notice.setDirection(0);
    BillType receiptInvoice = new BillType();// 收款发票登记单
    receiptInvoice.setClassName(InvoiceReg.class.getName());
    receiptInvoice.setDirection(Direction.RECEIPT);
    
    BillType newReceiptInvoice = new BillType();// 收款发票登记单
    newReceiptInvoice.setClassName(com.hd123.m3.account.service.ivc.reg.InvoiceReg.class.getName());
    newReceiptInvoice.setDirection(Direction.RECEIPT);

    BillType payInvoice = new BillType();// 付款发票登记单
    payInvoice.setClassName(InvoiceReg.class.getName());
    payInvoice.setDirection(Direction.PAYMENT);
    

    BillType payment = new BillType();// 付款单
    payment.setClassName(Payment.class.getName());
    payment.setDirection(Direction.PAYMENT);
    
    BillType invoiceExchange = new BillType();// 发票交换单
    invoiceExchange.setClassName(InvoiceExchange.class.getName());
    invoiceExchange.setDirection(0);
    
    BillType themeActivity = new BillType();// 主体促销单
    themeActivity.setClassName(ThemeActivity.class.getSimpleName());
    themeActivity.setDirection(1);

    List<BillType> billTypes;
    try {
      billTypes = BillTypeUtils.getAllWithout(freeze, payDeposit, receiptDeposit,
          payDepositMove, receiptDepositMove, payDepositRepayment, receiptDepositRepayment, notice,
          payInvoice,newReceiptInvoice, receiptInvoice,payment,invoiceExchange,themeActivity);
      return billTypes;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    
  }

}
