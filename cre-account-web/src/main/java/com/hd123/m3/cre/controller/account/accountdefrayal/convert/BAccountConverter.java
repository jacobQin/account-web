/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BPaymentConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月9日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal.convert;

import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.cre.controller.account.accountdefrayal.model.BAccount;

/**
 * @author chenganbang
 *
 */
public class BAccountConverter {
  private static BAccountConverter instance = null;

  private BAccountConverter() {
  }

  public static BAccountConverter getInstance() {
    if (instance == null) {
      instance = new BAccountConverter();
    }
    return instance;
  }

  public BAccount convert(Account source) {
    BAccount result = new BAccount();
    result.setContractName(source.getAcc1().getContract().getName());
    result.setCounterpartName(source.getAcc1().getCounterpart().getName());
    result.setSettlement(source.getAcc1().getBeginTime(), source.getAcc1().getEndTime());
    result.setAccountDate(source.getAcc1().getAccountDate());
    result.setOriginTotal(source.getOriginTotal().getTotal());
    result.setBillNumber(source.getAcc2().getPayment().getBillNumber());
    result.setTotal(source.getTotal().getTotal());
    String pivcType = source.getAcc2().getInvoice().getInvoiceType();
    String pivcNumber = source.getAcc2().getInvoice().getInvoiceNumber();
    result.setPivcType("-".equals(pivcType) ? "" : pivcType);
    result.setPivcNumber("-".equals(pivcNumber) ? "" : pivcNumber);
    return result;
  }
}
