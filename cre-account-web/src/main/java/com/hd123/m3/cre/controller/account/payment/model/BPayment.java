package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.hd123.m3.account.commons.AccStandardBill;
import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.commons.biz.tax.TaxCalculator;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收付款单
 * 
 * @author Libin
 * 
 */
public class BPayment extends AccStandardBill {
  private static final long serialVersionUID = -4815651085305503335L;

  public static final String FN_COUNTERPART = "counterpart";
  public static final String FN_PAYMENTDATE = "paymentDate";
  public static final String FN_DEALER = "dealer";
  public static final String FN_DEFRAYALTYPE = "defrayalType";
  public static final String FN_SUBJECT = "subject";

  private UCN accountUnit;
  private UCN counterpart;
  private String counterpartType;
  private int direction;
  private PaymentDefrayalType defrayalType;
  private UCN dealer;
  private Date paymentDate;
  private Total unpayedTotal = Total.zero();
  private Total total = Total.zero();
  private Total overdueTotal = Total.zero();
  private BigDecimal defrayalTotal = BigDecimal.ZERO;
  private BigDecimal depositTotal = BigDecimal.ZERO;
  private UCN depositSubject;
  private Date incomeDate;
  private String ivcType;
  private String ivcCode;
  private String ivcNumber;
  private Date ivcDate;
  private String ivcRemark;
  private boolean canInvoice;
  private BigDecimal depositBalance = BigDecimal.ZERO;

  /** 账款明细 */
  private List<BPaymentAccountLine> accountLines = new ArrayList<BPaymentAccountLine>();
  /** 滞纳金明细 */
  private List<BPaymentOverdueLine> overdueLines = new ArrayList<BPaymentOverdueLine>();
  /** 代收明细 */
  private List<BPaymentCollectionLine> collectionLines = new ArrayList<BPaymentCollectionLine>();
  /** 实收明细行 */
  private List<BPaymentCashDefrayal> cashes = new ArrayList<BPaymentCashDefrayal>();
  /** 扣预存款明细行 */
  private List<BPaymentDepositDefrayal> deposits = new ArrayList<BPaymentDepositDefrayal>();
  
  /**合同预存款余额明细*/
  private List<ContractAdvance> contractAdvances = new ArrayList<ContractAdvance>();

  public UCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(UCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public UCN getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(UCN counterpart) {
    this.counterpart = counterpart;
  }

  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public PaymentDefrayalType getDefrayalType() {
    return defrayalType;
  }

  public void setDefrayalType(PaymentDefrayalType defrayalType) {
    this.defrayalType = defrayalType;
  }

  public UCN getDealer() {
    return dealer;
  }

  public void setDealer(UCN dealer) {
    this.dealer = dealer;
  }

  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  /** 本次应收金额 */
  public Total getUnpayedTotal() {
    return unpayedTotal;
  }

  public void setUnpayedTotal(Total unpayedTotal) {
    this.unpayedTotal = unpayedTotal;
  }

  /** 账款实收金额 */
  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  public Total getOverdueTotal() {
    return overdueTotal;
  }

  /** 滞纳金 */
  public void setOverdueTotal(Total overdueTotal) {
    this.overdueTotal = overdueTotal;
  }

  /** 支付金额（实收金额） */
  public BigDecimal getDefrayalTotal() {
    return defrayalTotal;
  }

  public void setDefrayalTotal(BigDecimal defrayalTotal) {
    this.defrayalTotal = defrayalTotal;
  }

  /** 预存款余额 */
  public BigDecimal getDepositTotal() {
    return depositTotal;
  }

  public void setDepositTotal(BigDecimal depositTotal) {
    this.depositTotal = depositTotal;
  }

  public UCN getDepositSubject() {
    return depositSubject;
  }

  public void setDepositSubject(UCN depositSubject) {
    this.depositSubject = depositSubject;
  }
  
  public List<ContractAdvance> getContractAdvances() {
    return contractAdvances;
  }
  
  public void setContractAdvances(List<ContractAdvance> contractAdvances) {
    this.contractAdvances = contractAdvances;
  }

  public Date getIncomeDate() {
    return incomeDate;
  }

  public void setIncomeDate(Date incomeDate) {
    this.incomeDate = incomeDate;
  }

  public String getIvcType() {
    return ivcType;
  }

  public void setIvcType(String ivcType) {
    this.ivcType = ivcType;
  }

  public String getIvcCode() {
    return ivcCode;
  }

  public void setIvcCode(String ivcCode) {
    this.ivcCode = ivcCode;
  }

  public String getIvcNumber() {
    return ivcNumber;
  }

  public void setIvcNumber(String ivcNumber) {
    this.ivcNumber = ivcNumber;
  }

  public Date getIvcDate() {
    return ivcDate;
  }

  public void setIvcDate(Date ivcDate) {
    this.ivcDate = ivcDate;
  }

  public String getIvcRemark() {
    return ivcRemark;
  }

  public void setIvcRemark(String ivcRemark) {
    this.ivcRemark = ivcRemark;
  }

  /** 是否可以进行发票登记 */
  public boolean isCanInvoice() {
    return canInvoice;
  }

  public void setCanInvoice(boolean canInvoice) {
    this.canInvoice = canInvoice;
  }

  /** 预存款余额 */
  public BigDecimal getDepositBalance() {
    return depositBalance;
  }

  public void setDepositBalance(BigDecimal depositBalance) {
    this.depositBalance = depositBalance;
  }

  public List<BPaymentAccountLine> getAccountLines() {
    return accountLines;
  }

  public void setAccountLines(List<BPaymentAccountLine> accountLines) {
    this.accountLines = accountLines;
  }

  public List<BPaymentOverdueLine> getOverdueLines() {
    return overdueLines;
  }

  public void setOverdueLines(List<BPaymentOverdueLine> overdueLines) {
    this.overdueLines = overdueLines;
  }

  /** 代收明细 */
  public List<BPaymentCollectionLine> getCollectionLines() {
    return collectionLines;
  }

  public void setCollectionLines(List<BPaymentCollectionLine> collectionLines) {
    this.collectionLines = collectionLines;
  }

  public List<BPaymentCashDefrayal> getCashes() {
    return cashes;
  }

  public void setCashes(List<BPaymentCashDefrayal> cashes) {
    this.cashes = cashes;
  }

  public List<BPaymentDepositDefrayal> getDeposits() {
    return deposits;
  }

  public void setDeposits(List<BPaymentDepositDefrayal> deposits) {
    this.deposits = deposits;
  }

  @Override
  public String toString() {
    return getBillNumber();
  }

  /** 合计本次应收付金额以及账款应收付金额预存款金额 */
  public void aggregate(int scale, RoundingMode roundingMode) {
    Total unpayedTempTotal = Total.zero();
    Total temptotal = Total.zero();

    defrayalTotal = BigDecimal.ZERO;
    for (BPaymentCashDefrayal cash : getCashes()) {
      if (cash.getTotal() != null) {
        defrayalTotal = defrayalTotal.add(cash.getTotal());
      }
    }

    for (BPaymentLine line : getAccountLines()) {
      if (line.getUnpayedTotal() != null) {
        unpayedTempTotal = unpayedTempTotal.add(line.getUnpayedTotal());
      }
      if (line.getTotal() != null) {
        // temptotal = temptotal.add(line.getTotal());
      }
    }

    for (BPaymentLine line : getCollectionLines()) {
      if (line.getUnpayedTotal() != null) {
        unpayedTempTotal = unpayedTempTotal.add(line.getUnpayedTotal());
      }
      if (line.getTotal() != null) {
        // temptotal = temptotal.add(line.getTotal());
      }
    }

    temptotal.setTotal(defrayalTotal);

    for (BPaymentDepositDefrayal defrayal : getDeposits()) {
      temptotal.setTotal(temptotal.getTotal().add(defrayal.getTotal()));
    }

    unpayedTempTotal.getTotal().setScale(2, RoundingMode.HALF_UP);
    unpayedTempTotal.getTax().setScale(scale, roundingMode);
    temptotal.getTotal().setScale(2, RoundingMode.HALF_UP);
    temptotal.getTax().setScale(scale, roundingMode);
    this.unpayedTotal = unpayedTempTotal.clone();
    this.total = temptotal.clone();
    this.defrayalTotal = temptotal.getTotal();

    if (unpayedTempTotal.getTotal().compareTo(total.getTotal()) < 0) {
      this.total = unpayedTotal.clone();
    }

    aggregateDepositTotal();
  }

  /** 计算预存款：支付金额+扣预存款-账款实收（本次应收） */
  private void aggregateDepositTotal() {
    BigDecimal receivedTotal = defrayalTotal;

    // 只有应收大于等于0才算预存款
    if (receivedTotal.compareTo(unpayedTotal.getTotal()) > 0
        && unpayedTotal.getTotal().compareTo(BigDecimal.ZERO) >= 0) {
      this.depositTotal = receivedTotal.subtract(unpayedTotal.getTotal());
    } else {
      this.depositTotal = BigDecimal.ZERO;
    }

    if (PaymentDefrayalType.bill.equals(getDefrayalType()) == false) {
      this.depositTotal = BigDecimal.ZERO;
      for (BPaymentAccountLine line : accountLines) {
        if (line.getDefrayalTotal().compareTo(line.getUnpayedTotal().getTotal()) > 0) {
          // 未收为负数，不进行预存款计算。
          if (line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) < 0) {
            continue;
          }
          line.setDepositTotal(line.getDefrayalTotal().subtract(line.getUnpayedTotal().getTotal()));
          this.depositTotal = this.depositTotal.add(line.getDepositTotal());
        }
      }
    }
  }

  /** 合计滞纳金 */
  public void aggregateOverdue(int scale, RoundingMode roundingMode) {
    Total overdueTempTotal = Total.zero();
    for (BPaymentOverdueLine line : getOverdueLines()) {
      overdueTempTotal = overdueTempTotal.add(line.getTotal());
    }
    overdueTempTotal.getTotal().setScale(2, RoundingMode.HALF_UP);
    overdueTempTotal.getTax().setScale(scale, roundingMode);
    this.overdueTotal = overdueTempTotal;
  }

  /** 预处理工作（界面到服务）：进行一些冗余数据处理 */
  @JsonIgnore
  public void preProcess() {
    for (BPaymentCollectionLine line : getCollectionLines()) {
      line.preProcess();
    }
  }

  public void doBeforeSave() {
    // 去除应收金额为0的代收明细行
    for (int i = this.getCollectionLines().size() - 1; i >= 0; i--) {
      BPaymentCollectionLine collectionLine = this.getCollectionLines().get(i);

      for (int j = collectionLine.getCashes().size() - 1; j >= 0; j--) {
        BPaymentLineCash cash = collectionLine.getCashes().get(j);
        if (cash.getTotal() == null || BigDecimal.ZERO.compareTo(cash.getTotal()) == 0) {
          collectionLine.getCashes().remove(j);
        }
      }
      if (collectionLine.getUnpayedTotal() == null
          || collectionLine.getUnpayedTotal().getTotal() == null
          || collectionLine.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) == 0) {
        this.getCollectionLines().remove(i);
      }
    }

    // 去除扣预存款扣除金额为0的明细行
    for (int i = getDeposits().size() - 1; i >= 0; i--) {
      BPaymentDepositDefrayal defrayal = getDeposits().get(i);
      if (defrayal.getTotal() == null || BigDecimal.ZERO.compareTo(defrayal.getTotal()) == 0) {
        getDeposits().remove(i);
      }
    }

    // 如果没有产生预存款，将预存款科目设置为null
    if (getDepositTotal() == null || BigDecimal.ZERO.compareTo(getDepositTotal()) == 0) {
      setDepositSubject(null);
    }

    // 计算修改滞纳金金额
    for (BPaymentOverdueLine line : getOverdueLines()) {
      BigDecimal tax = TaxCalculator.tax(line.getTotal().getTotal(), line.getTaxRate(), 2,
          RoundingMode.UP);
      line.getTotal().setTax(tax);
    }
    
    recalculateUnpayedTotal();

  }
  
  private void recalculateUnpayedTotal(){
    unpayedTotal = Total.zero();
    for(BPaymentAccountLine line:accountLines){
      unpayedTotal = unpayedTotal.add(line.getUnpayedTotal());
    }
    for(BPaymentCollectionLine line:collectionLines){
      unpayedTotal = unpayedTotal.add(line.getUnpayedTotal());
    }
  }

}
