/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	AccountToBInvoiceStockRegLineConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月1日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.server.converter;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStockRegLine;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * Account -> BInvoiceStockRegLine
 * 
 * @author LiBin
 * 
 */
public class AccountToBInvoiceStockRegLineConverter implements
    Converter<Account, BInvoiceStockRegLine> {

  private List<BillType> billTypes = new ArrayList<BillType>();

  private static AccountToBInvoiceStockRegLineConverter instance = null;

  public static AccountToBInvoiceStockRegLineConverter getInstance(List<BillType> billTypes) {
    if (instance == null) {
      instance = new AccountToBInvoiceStockRegLineConverter();
    }

    instance.billTypes = billTypes;

    return instance;
  }

  @Override
  public BInvoiceStockRegLine convert(Account source) throws ConversionException {
    if (source == null) {
      return null;
    }

    BInvoiceStockRegLine target = new BInvoiceStockRegLine();
    target.setTenant(UCNBizConverter.getInstance().convert(source.getAcc1().getCounterpart()));
    target.setSubject(UCNBizConverter.getInstance().convert(source.getAcc1().getSubject()));
    target.setContract(UCNBizConverter.getInstance().convert(source.getAcc1().getContract()));
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
