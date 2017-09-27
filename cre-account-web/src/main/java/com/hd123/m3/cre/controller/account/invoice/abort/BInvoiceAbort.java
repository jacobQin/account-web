/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceAbort.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月8日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.abort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.service.ivc.abort.InvoiceAbort;
import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.common.InvoiceStandardBill;
import com.hd123.m3.cre.controller.account.invoice.common.BInvoiceLine;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author xiahongjian
 *
 */
public class BInvoiceAbort extends InvoiceStandardBill {
  private static final long serialVersionUID = 1595866785059387173L;

  private UCN aborter;
  private Date abortDate;

  private List<BInvoiceLine> abortLines = new ArrayList<BInvoiceLine>();

  private static final Converter<InvoiceAbort, BInvoiceAbort> CONVERTER = ConverterBuilder
      .newBuilder(InvoiceAbort.class, BInvoiceAbort.class).build();

  public static BInvoiceAbort newInstance(InvoiceAbort source) {
    List<BInvoiceLine> lines = new ArrayList<BInvoiceLine>();
    BInvoiceAbort target = CONVERTER.convert(source);
    if (source != null) {
      for (InvoiceLine line : source.getAbortLines()) {
        lines.add(BInvoiceLine.newInstance(line));
      }      
    }
    target.setAbortLines(lines);
    return target;
  }
  
  
  /** 作废人 */
  public UCN getAborter() {
    return aborter;
  }

  public void setAborter(UCN aborter) {
    this.aborter = aborter;
  }

  /** 作废日期 */
  public Date getAbortDate() {
    return abortDate;
  }

  public void setAbortDate(Date abortDate) {
    this.abortDate = abortDate;
  }

  /** 作废明细 */
  public List<BInvoiceLine> getAbortLines() {
    return abortLines;
  }

  public void setAbortLines(List<BInvoiceLine> abortLines) {
    this.abortLines = abortLines;
  }
}
