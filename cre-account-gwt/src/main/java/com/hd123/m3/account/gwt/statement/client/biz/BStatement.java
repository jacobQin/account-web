/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BStatement.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 账单|表示层实体
 * 
 * @author huangjunxian
 * 
 */
public class BStatement extends BAccStandardBill implements HasStore {
  public static final String modify_planPayDate = "_planPayDate";
  public static final String modify_lastReceiptDate = "_lastReceiptDate";

  private static final long serialVersionUID = 300100L;

  private BUCN accountUnit;
  private BCounterpart counterpart;
  private BUCN contract;
  private String type;
  private String settleState;
  private Date receiptAccDate;
  private BTotal payTotal;
  private BTotal freePayTotal;
  private BTotal ivcPayTotal;
  private BTotal receiptTotal;
  private BTotal freeReceiptTotal;
  private BTotal ivcReceiptTotal;
  private Date accountTime;
  private Date planDate;
  private boolean showRefer;
  private boolean showDesLineInfo;
  private BigDecimal saleTotal = BigDecimal.ZERO;
  private String coopMode;
  private String accountType;

  // 实际收支情况
  private BigDecimal payAdj = BigDecimal.ZERO;
  private BigDecimal payed = BigDecimal.ZERO;
  private BigDecimal ivcPayAdj = BigDecimal.ZERO;
  private BigDecimal ivcPayed = BigDecimal.ZERO;

  private BigDecimal receiptAdj = BigDecimal.ZERO;
  private BigDecimal receipted = BigDecimal.ZERO;
  private BigDecimal ivcReceiptAdj = BigDecimal.ZERO;
  private BigDecimal ivcReceipted = BigDecimal.ZERO;

  private List<BStatementAccRange> ranges = new ArrayList<BStatementAccRange>();
  private List<BStatementLine> lines = new ArrayList<BStatementLine>();
  private List<BStatementSumLine> sumLines = new ArrayList<BStatementSumLine>();
  private List<BAttachment> attachs = new ArrayList<BAttachment>();

  /** 结算单位 */
  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 对方单位 */
  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  /** 合同 */
  public BUCN getContract() {
    return contract;
  }

  public void setContract(BUCN contract) {
    this.contract = contract;
  }

  /** 应付金额 */
  public BTotal getPayTotal() {
    return payTotal;
  }

  public void setPayTotal(BTotal payTotal) {
    this.payTotal = payTotal;
  }

  /** 付款减免金额 */
  public BTotal getFreePayTotal() {
    return freePayTotal;
  }

  public void setFreePayTotal(BTotal freePayTotal) {
    this.freePayTotal = freePayTotal;
  }

  /** 应付开票金额 */
  public BTotal getIvcPayTotal() {
    return ivcPayTotal;
  }

  public void setIvcPayTotal(BTotal ivcPayTotal) {
    this.ivcPayTotal = ivcPayTotal;
  }

  /** 应收金额 */
  public BTotal getReceiptTotal() {
    return receiptTotal;
  }

  public void setReceiptTotal(BTotal receiptTotal) {
    this.receiptTotal = receiptTotal;
  }

  /** 收款减免金额 */
  public BTotal getFreeReceiptTotal() {
    return freeReceiptTotal;
  }

  public void setFreeReceiptTotal(BTotal freeReceiptTotal) {
    this.freeReceiptTotal = freeReceiptTotal;
  }

  /** 应收开票金额 */
  public BTotal getIvcReceiptTotal() {
    return ivcReceiptTotal;
  }

  public void setIvcReceiptTotal(BTotal ivcReceiptTotal) {
    this.ivcReceiptTotal = ivcReceiptTotal;
  }

  /** 出账时间 */
  public Date getAccountTime() {
    return accountTime;
  }

  public void setAccountTime(Date accountTime) {
    this.accountTime = accountTime;
  }

  /** 计划出账日期 */
  public Date getPlanDate() {
    return planDate;
  }

  public void setPlanDate(Date planDate) {
    this.planDate = planDate;
  }

  /** 结款状态 */
  public String getSettleState() {
    return settleState;
  }

  public void setSettleState(String settleState) {
    this.settleState = settleState;
  }

  /** 应收记账日期 */
  public Date getReceiptAccDate() {
    return receiptAccDate;
  }

  public void setReceiptAccDate(Date receiptAccDate) {
    this.receiptAccDate = receiptAccDate;
  }

  /** 账单类型 */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /** 销售金额 */
  public BigDecimal getSaleTotal() {
    return saleTotal;
  }

  public void setSaleTotal(BigDecimal saleTotal) {
    this.saleTotal = saleTotal;
  }

  /** 合作方式 */
  public String getCoopMode() {
    return coopMode;
  }

  public void setCoopMode(String coopMode) {
    this.coopMode = coopMode;
  }

  /** 科目类型 */
  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  /** 应付款调整金额 */
  public BigDecimal getPayAdj() {
    return payAdj;
  }

  public void setPayAdj(BigDecimal payAdj) {
    this.payAdj = payAdj;
  }

  /** 已付款金额 */
  public BigDecimal getPayed() {
    return payed;
  }

  public void setPayed(BigDecimal payed) {
    this.payed = payed;
  }

  /** 应付发票调整金额 */
  public BigDecimal getIvcPayAdj() {
    return ivcPayAdj;
  }

  public void setIvcPayAdj(BigDecimal ivcPayAdj) {
    this.ivcPayAdj = ivcPayAdj;
  }

  /** 已付发票登记金额 */
  public BigDecimal getIvcPayed() {
    return ivcPayed;
  }

  public void setIvcPayed(BigDecimal ivcPayed) {
    this.ivcPayed = ivcPayed;
  }

  /** 应收调整金额 */
  public BigDecimal getReceiptAdj() {
    return receiptAdj;
  }

  public void setReceiptAdj(BigDecimal receiptAdj) {
    this.receiptAdj = receiptAdj;
  }

  /** 已收款金额 */
  public BigDecimal getReceipted() {
    return receipted;
  }

  public void setReceipted(BigDecimal receipted) {
    this.receipted = receipted;
  }

  /** 应收发票调整金额 */
  public BigDecimal getIvcReceiptAdj() {
    return ivcReceiptAdj;
  }

  public void setIvcReceiptAdj(BigDecimal ivcReceiptAdj) {
    this.ivcReceiptAdj = ivcReceiptAdj;
  }

  /** 已收发票金额 */
  public BigDecimal getIvcReceipted() {
    return ivcReceipted;
  }

  public void setIvcReceipted(BigDecimal ivcReceipted) {
    this.ivcReceipted = ivcReceipted;
  }

  /** 是否显示参考信息 */
  public boolean isShowRefer() {
    return showRefer;
  }

  public void setShowRefer(boolean showRefer) {
    this.showRefer = showRefer;
  }

  /** 存在无效账款 */
  public boolean isShowDesLineInfo() {
    return showDesLineInfo;
  }

  public void setShowDesLineInfo(boolean showDesLineInfo) {
    this.showDesLineInfo = showDesLineInfo;
  }

  /** 周期明细 */
  public List<BStatementAccRange> getRanges() {
    return ranges;
  }

  public void setRanges(List<BStatementAccRange> ranges) {
    this.ranges = ranges;
  }

  /** 科目明细 */
  public List<BStatementLine> getLines() {
    return lines;
  }

  public void setLines(List<BStatementLine> lines) {
    this.lines = lines;
  }

  public List<BStatementSumLine> getSumLines() {
    return sumLines;
  }

  public void setSumLines(List<BStatementSumLine> sumLines) {
    this.sumLines = sumLines;
  }

  /** 附件 */
  public List<BAttachment> getAttachs() {
    return attachs;
  }

  public void setAttachs(List<BAttachment> attachs) {
    this.attachs = attachs;
  }

  @Override
  public String toString() {
    return getBillNumber();
  }

  /** 按照付款登记方向合计。 */
  public void aggregate() {
    BigDecimal receiveTotal = BigDecimal.ZERO;
    BigDecimal receiveTax = BigDecimal.ZERO;
    BigDecimal paymentTotal = BigDecimal.ZERO;
    BigDecimal paymentTax = BigDecimal.ZERO;
    // 开票
    BigDecimal ivcPayTotalocal = BigDecimal.ZERO;
    BigDecimal ivcPayTax = BigDecimal.ZERO;
    BigDecimal ivcReceiptTotalocal = BigDecimal.ZERO;
    BigDecimal ivcReceiptTax = BigDecimal.ZERO;

    for (BStatementLine line : getLines()) {
      if (DirectionType.payment.getDirectionValue() == line.getAcc1().getDirection()) {
        if (line.getTotal() == null)
          continue;
        // 应付金额
        paymentTotal = paymentTotal.add(line.getTotal().getTotal());
        paymentTax = paymentTax.add(line.getTotal().getTax());
        // 应付开票金额
        if (line.getAcc2().getInvoice() != null
            && line.getAcc2().getInvoice().getBillUuid() != null
            && BAccount.NONE_BILL_UUID.equals(line.getAcc2().getInvoice().getBillUuid())) {
          ivcPayTotalocal = ivcPayTotalocal.add(line.getTotal().getTotal());
          ivcPayTax = ivcPayTax.add(line.getTotal().getTax());
        }
      }

      if (DirectionType.receipt.getDirectionValue() == line.getAcc1().getDirection()) {
        if (line.getTotal() == null)
          continue;
        // 应收金额
        receiveTotal = receiveTotal.add(line.getTotal().getTotal());
        receiveTax = receiveTax.add(line.getTotal().getTax());
        // 应收开票金额
        if (line.getAcc2().getInvoice() != null
            && line.getAcc2().getInvoice().getBillUuid() != null
            && BAccount.NONE_BILL_UUID.equals(line.getAcc2().getInvoice().getBillUuid())) {
          ivcReceiptTotalocal = ivcReceiptTotalocal.add(line.getTotal().getTotal());
          ivcReceiptTax = ivcReceiptTax.add(line.getTotal().getTax());
        }
      }
    }
    setReceiptTotal(new BTotal(receiveTotal, receiveTax));
    setPayTotal(new BTotal(paymentTotal, paymentTax));
    setIvcPayTotal(new BTotal(ivcPayTotalocal, ivcPayTax));
    setIvcReceiptTotal(new BTotal(ivcReceiptTotalocal, ivcReceiptTax));
  }

  /** 刷新概要中的合计，总金额/税额，仅仅展示，无业务操作 */
  public BTotal calculateTotal() {
    BTotal sumTotal = new BTotal();
    BigDecimal pTotal = null;
    BigDecimal pTax = null;
    if (payTotal != null) {
      pTotal = payTotal.getTotal();
      pTax = payTotal.getTax();
    }

    BigDecimal rTotal = null;
    BigDecimal rTax = null;
    if (receiptTotal != null) {
      rTotal = receiptTotal.getTotal();
      rTax = receiptTotal.getTax();
    }

    BigDecimal total = null;
    if (pTotal != null && rTotal != null)
      total = rTotal.subtract(pTotal);
    else if (pTotal != null && rTotal == null)
      total = BigDecimal.ZERO.subtract(pTotal);
    else if (pTotal == null && rTotal != null)
      total = rTotal;

    BigDecimal tax = null;
    if (pTax != null && rTax != null)
      tax = rTax.subtract(pTax);
    else if (pTax != null && rTax == null)
      tax = BigDecimal.ZERO.subtract(pTax);
    else if (pTax == null && rTax != null)
      tax = rTax;

    sumTotal.setTotal(total);
    sumTotal.setTax(tax);

    return sumTotal;
  }

  public boolean isExistsValidLine() {
    for (BStatementLine line : lines) {
      if (line.isValid())
        return true;
    }
    return false;
  }

  @Override
  public HasUCN getStore() {
    return accountUnit;
  }
}
