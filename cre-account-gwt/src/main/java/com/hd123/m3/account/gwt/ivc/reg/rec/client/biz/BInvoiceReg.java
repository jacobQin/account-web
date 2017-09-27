/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInvoiceReg.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStandardBill;
import com.hd123.rumba.commons.gwt.mini.client.util.Pair;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * B对象|发票领用单
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class BInvoiceReg extends BInvoiceStandardBill {
  private static final long serialVersionUID = 1084446363761469663L;

  private BUCN counterpart;
  private String counterpartType;
  private int direction;
  private Date regDate;
  private boolean allowSplitReg;
  private boolean useInvoiceStock;
  private String invoiceCode;
  private String invoiceNumber;
  private BTotal originTotal;
  private BTotal accountTotal;
  private BTotal invoiceTotal;
  private BigDecimal totalDiff;
  private BigDecimal taxDiff;
  private boolean isPrinted;

  private List<BIvcRegLine> regLines = new ArrayList<BIvcRegLine>();

  public void sumTotal() {
    originTotal = new BTotal(BigDecimal.ZERO, BigDecimal.ZERO);
    accountTotal = new BTotal(BigDecimal.ZERO, BigDecimal.ZERO);
    invoiceTotal = new BTotal(BigDecimal.ZERO, BigDecimal.ZERO);
    for (BIvcRegLine line : regLines) {
      originTotal = originTotal.add(line.getOriginTotal());
      accountTotal = accountTotal.add(line.getUnregTotal());
      invoiceTotal = invoiceTotal.add(line.getTotal());
    }
    totalDiff = accountTotal.getTotal().subtract(invoiceTotal.getTotal());
    taxDiff = accountTotal.getTax().subtract(invoiceTotal.getTax());
  }

  /** 商户 */
  public BUCN getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BUCN counterpart) {
    this.counterpart = counterpart;
  }

  /** 对方单位类型 */
  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  /** 收款方向 */
  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  /** 开票日期 */
  public Date getRegDate() {
    return regDate;
  }

  public void setRegDate(Date regDate) {
    this.regDate = regDate;
  }

  /** 允许发票多次登记 */
  public boolean isAllowSplitReg() {
    return allowSplitReg;
  }

  public void setAllowSplitReg(boolean allowSplitReg) {
    this.allowSplitReg = allowSplitReg;
  }

  /** 使用发票库存管理 */
  public boolean isUseInvoiceStock() {
    return useInvoiceStock;
  }

  public void setUseInvoiceStock(boolean useInvoiceStock) {
    this.useInvoiceStock = useInvoiceStock;
  }

  /** 发票代码 */
  public String getInvoiceCode() {
    return invoiceCode;
  }

  public void setInvoiceCode(String invoiceCode) {
    this.invoiceCode = invoiceCode;
  }

  /** 是否打印发票 */
  public boolean isPrinted() {
    return isPrinted;
  }

  public void setPrinted(boolean isPrinted) {
    this.isPrinted = isPrinted;
  }

  /** 发票号码 */
  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  /** 发票总金额 */
  public BTotal getInvoiceTotal() {
    return invoiceTotal;
  }

  public void setInvoiceTotal(BTotal invoiceTotal) {
    this.invoiceTotal = invoiceTotal;
  }

  /** 今开票金额合计 */
  public BTotal getOriginTotal() {
    return originTotal;
  }

  public void setOriginTotal(BTotal originTotal) {
    this.originTotal = originTotal;
  }

  /** 本次应开票金额合计 */
  public BTotal getAccountTotal() {
    return accountTotal;
  }

  public void setAccountTotal(BTotal accountTotal) {
    this.accountTotal = accountTotal;
  }

  /** 金额差异 */
  public BigDecimal getTotalDiff() {
    return totalDiff;
  }

  public void setTotalDiff(BigDecimal totalDiff) {
    this.totalDiff = totalDiff;
  }

  /** 税额差异 */
  public BigDecimal getTaxDiff() {
    return taxDiff;
  }

  public void setTaxDiff(BigDecimal taxDiff) {
    this.taxDiff = taxDiff;
  }

  /** 发票登记明细 */
  public List<BIvcRegLine> getRegLines() {
    return regLines;
  }

  public void setRegLines(List<BIvcRegLine> regLines) {
    this.regLines = regLines;
  }

  /** 取得所有账款的accid与收款单uuid */
  public List<Pair<String, String>> getAccIds() {
    List<Pair<String, String>> accIds = new ArrayList<Pair<String, String>>();
    for (BIvcRegLine line : regLines) {
      accIds.add(new Pair<String, String>(line.getAcc1().getId(),
          line.getAcc2().getPayment() == null ? null : line.getAcc2().getPayment().getBillUuid()));
    }
    return accIds;
  }
}
