/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AccountToInvoiceStockRegLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.stock;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author wangyibo
 *
 */
public class AccountToInvoiceStockRegLineConverter implements
    Converter<Account, InvoiceStockRegLine> {
  
  private List<BillType> billTypes = new ArrayList<BillType>();
  
  private static AccountToInvoiceStockRegLineConverter instance = null;
  
  public static AccountToInvoiceStockRegLineConverter getInstance(List<BillType> billTypes) {
    if (instance == null) {
      instance = new AccountToInvoiceStockRegLineConverter();
    }

    instance.billTypes = billTypes;

    return instance;
  }
  
  @Override
  public InvoiceStockRegLine convert(Account source) throws ConversionException {
    if (source == null) {
      return null;
    }

    InvoiceStockRegLine target = new InvoiceStockRegLine();
    target.setTenant(source.getAcc1().getCounterpart());
    target.setSubject(source.getAcc1().getSubject());
    target.setContract(source.getAcc1().getContract());
    target.setSouceBillType(source.getAcc1().getSourceBill().getBillType());
    target.setSourceBillTypeCaption(getBillCaption(source.getAcc1().getSourceBill().getBillType()));
    target.setSourceBillNumber(source.getAcc1().getSourceBill().getBillNumber());
    target.setBeginDate(source.getAcc1().getBeginTime());
    target.setEndDate(source.getAcc1().getEndTime());
    target.setReceivableTotal(source.getOriginTotal().getTotal());
    target.setRegTotal(source.getTotal().getTotal());

    return target;
  }

  private String getBillCaption(String name) {
    if (StringUtil.isNullOrBlank(name)) {
      return name;
    }

    if (billTypes == null) {
      return name;
    }

    for (BillType type : billTypes) {
      if (name.equals(type.getName())) {
        return type.getCaption();
      }
    }

    return name;
  }
}
