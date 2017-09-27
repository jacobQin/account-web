/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceReturn.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月8日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.returns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.common.InvoiceStandardBill;
import com.hd123.m3.account.service.ivc.returns.InvoiceReturn;
import com.hd123.m3.cre.controller.account.invoice.common.BInvoiceLine;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author xiahongjian
 *
 */
public class BInvoiceReturn extends InvoiceStandardBill {
  private static final long serialVersionUID = 8115948688905503329L;

  private UCN receiver;
  private UCN returnor;
  private Date returnDate;

  private List<BInvoiceLine> returnLines = new ArrayList<BInvoiceLine>();

  private static final Converter<InvoiceReturn, BInvoiceReturn> CONVERTER = ConverterBuilder
      .newBuilder(InvoiceReturn.class, BInvoiceReturn.class).build();

  public static BInvoiceReturn newInstance(InvoiceReturn source) {
    List<BInvoiceLine> lines = new ArrayList<BInvoiceLine>();
    BInvoiceReturn target = CONVERTER.convert(source);
    if (source != null) {
      for (InvoiceLine line : source.getReturnLines()) {
        lines.add(BInvoiceLine.newInstance(line));
      }      
    }
    target.setReturnLines(lines);
    return target;
  }
  
  /** 领用人 */
  public UCN getReceiver() {
    return receiver;
  }

  public void setReceiver(UCN receiver) {
    this.receiver = receiver;
  }

  /** 退票人 */
  public UCN getReturnor() {
    return returnor;
  }

  public void setReturnor(UCN returnor) {
    this.returnor = returnor;
  }

  /** 领退日期 */
  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date returnDate) {
    this.returnDate = returnDate;
  }

  /** 领退明细 */
  public List<BInvoiceLine> getReturnLines() {
    return returnLines;
  }

  public void setReturnLines(List<BInvoiceLine> returnLines) {
    this.returnLines = returnLines;
  }
}
