/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BStatementSumLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月17日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 账单科目汇总明细
 * 
 * @author chenganbang
 *
 */
public class BStatementSumLine {
  private String uuid;
  private int direction;

  private UCN subject;
  private TaxRate taxRate;
  private Total total;
  private BigDecimal needInvoice = BigDecimal.ZERO;
  private BigDecimal settled = BigDecimal.ZERO;
  private BigDecimal invoiced = BigDecimal.ZERO;
  private BigDecimal owedAmount = BigDecimal.ZERO;
  private String accSrcType;
  private List<StatementLine> lines = new ArrayList<StatementLine>();

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * 收付方向
   * 
   * @return
   */
  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  /**
   * 科目
   * 
   * @return
   */
  public UCN getSubject() {
    return subject;
  }

  public void setSubject(UCN subject) {
    this.subject = subject;
  }

  /**
   * 税率
   * 
   * @return
   */
  public TaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(TaxRate taxRate) {
    this.taxRate = taxRate;
  }

  /**
   * 金额(税额)
   * 
   * @return
   */
  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  /**
   * 开票金额
   * 
   * @return
   */
  public BigDecimal getNeedInvoice() {
    return needInvoice;
  }

  public void setNeedInvoice(BigDecimal needInvoice) {
    this.needInvoice = needInvoice;
  }

  /**
   * 已结算金额
   * 
   * @return
   */
  public BigDecimal getSettled() {
    return settled;
  }

  public void setSettled(BigDecimal settled) {
    this.settled = settled;
  }

  /**
   * 已开票金额
   * 
   * @return
   */
  public BigDecimal getInvoiced() {
    return invoiced;
  }

  public void setInvoiced(BigDecimal invoiced) {
    this.invoiced = invoiced;
  }

  /**
   * 欠款金额
   * 
   * @return
   */
  public BigDecimal getOwedAmount() {
    return owedAmount;
  }

  public void setOwedAmount(BigDecimal owedAmount) {
    this.owedAmount = owedAmount;
  }

  /**
   * 明细行
   * 
   * @return
   */
  public List<StatementLine> getLines() {
    return lines;
  }

  public void setLines(List<StatementLine> lines) {
    this.lines = lines;
  }

  /**
   * 账款来源类型
   * 
   * @return
   */
  public String getAccSrcType() {
    return accSrcType;
  }

  public void setAccSrcType(String accSrcType) {
    this.accSrcType = accSrcType;
  }

}
