/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceRecycle.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月8日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.recycle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.service.ivc.common.InvoiceStandardBill;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycle;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleLine;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author xiahongjian
 *
 */
public class BInvoiceRecycle extends InvoiceStandardBill {

  private static final long serialVersionUID = 1026078020358136095L;

  private UCN receiver;
  private UCN returnor;
  private Date recycleDate;

  List<BInvoiceRecycleLine> lines = new ArrayList<BInvoiceRecycleLine>();

  private static final Converter<InvoiceRecycle, BInvoiceRecycle> CONVERTER = ConverterBuilder
      .newBuilder(InvoiceRecycle.class, BInvoiceRecycle.class).build();

  public static BInvoiceRecycle newInstance(InvoiceRecycle source) {
    List<BInvoiceRecycleLine> lines = new ArrayList<BInvoiceRecycleLine>();
    BInvoiceRecycle target = CONVERTER.convert(source);
    if (source != null) {
      for (InvoiceRecycleLine line : source.getLines()) {
        lines.add(BInvoiceRecycleLine.newInstance(line));
      }      
    }
    target.setLines(lines);
    return target;
  }
  
  
  /** 接受人 */
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

  /** 回收时间 */
  public Date getRecycleDate() {
    return recycleDate;
  }

  public void setRecycleDate(Date recycleDate) {
    this.recycleDate = recycleDate;
  }

  /** 回收明细 */
  public List<BInvoiceRecycleLine> getLines() {
    return lines;
  }

  public void setLines(List<BInvoiceRecycleLine> lines) {
    this.lines = lines;
  }
}
