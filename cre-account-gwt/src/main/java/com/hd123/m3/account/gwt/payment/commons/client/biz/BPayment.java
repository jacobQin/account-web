/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BReceive.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 收付款单
 * 
 * @author subinzhu
 * 
 */
public class BPayment extends BAccStandardBill implements HasStore {
  private static final long serialVersionUID = -4815651085305503335L;

  public static final String FN_COUNTERPART = "counterpart";
  public static final String FN_PAYMENTDATE = "paymentDate";
  public static final String FN_DEALER = "dealer";
  public static final String FN_DEFRAYALTYPE = "defrayalType";
  public static final String FN_SUBJECT = "subject";

  private BUCN accountUnit;
  private BCounterpart counterpart;
  private int direction;
  private String defrayalType;
  private BUCN dealer;
  private Date paymentDate;
  private BTotal receiptTotal = BTotal.zero();
  private BTotal unpayedTotal = BTotal.zero();
  private BTotal total = BTotal.zero();
  private BTotal overdueTotal = BTotal.zero();
  private BigDecimal defrayalTotal = BigDecimal.ZERO;
  private BigDecimal depositTotal = BigDecimal.ZERO;
  private BUCN depositSubject;
  private Date incomeDate;
  private String ivcType;
  private String ivcCode;
  private String ivcNumber;
  private Date ivcDate;
  private String ivcRemark;
  private boolean canInvoice;

  /** 账款明细 */
  private List<BPaymentAccountLine> accountLines = new ArrayList<BPaymentAccountLine>();
  /** 滞纳金明细 */
  private List<BPaymentOverdueLine> overdueLines = new ArrayList<BPaymentOverdueLine>();
  /** 代收明细 */
  private List<BPaymentCollectionLine> collectionLines = new ArrayList<BPaymentCollectionLine>();
  /** 实收明细行 */
  private List<BPaymentCashDefrayal> cashs = new ArrayList<BPaymentCashDefrayal>();
  /** 扣预存款明细行 */
  private List<BPaymentDepositDefrayal> deposits = new ArrayList<BPaymentDepositDefrayal>();

  public BUCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(BUCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public String getDefrayalType() {
    return defrayalType;
  }

  public void setDefrayalType(String defrayalType) {
    this.defrayalType = defrayalType;
  }

  public BUCN getDealer() {
    return dealer;
  }

  public void setDealer(BUCN dealer) {
    this.dealer = dealer;
  }

  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  public BTotal getReceiptTotal() {
    return receiptTotal;
  }

  public void setReceiptTotal(BTotal receiptTotal) {
    this.receiptTotal = receiptTotal;
  }

  /** 本次应收金额 */
  public BTotal getUnpayedTotal() {
    return unpayedTotal;
  }

  public void setUnpayedTotal(BTotal unpayedTotal) {
    this.unpayedTotal = unpayedTotal;
  }

  /** 账款实收金额 */
  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  public BTotal getOverdueTotal() {
    return overdueTotal;
  }

  /** 滞纳金 */
  public void setOverdueTotal(BTotal overdueTotal) {
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

  public BUCN getDepositSubject() {
    return depositSubject;
  }

  public void setDepositSubject(BUCN depositSubject) {
    this.depositSubject = depositSubject;
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

  public List<BPaymentCashDefrayal> getCashs() {
    return cashs;
  }

  public void setCashs(List<BPaymentCashDefrayal> cashs) {
    this.cashs = cashs;
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

  /** 已添加的账款Id */
  public List<BAccountId> getHasAddedAccIds() {
    if (accountLines == null || accountLines.isEmpty())
      return Collections.emptyList();

    List<BAccountId> hasAddedAccIds = new ArrayList<BAccountId>();
    for (BPaymentAccountLine accLine : accountLines) {
      if (accLine.getAcc1() == null || StringUtil.isNullOrEmpty(accLine.getAcc1().getId())
          || accLine.getAcc2() == null || StringUtil.isNullOrEmpty(accLine.getAcc2().getId()))
        continue;

      BAccountId accountId = new BAccountId();
      accountId.setAccId(accLine.getAcc1().getId());
      ArrayList<String> list = new ArrayList<String>();
      list.add(accLine.getAcc2().getStatement() == null ? BBill.NONE_ID : accLine.getAcc2()
          .getStatement().id());
      list.add(accLine.getAcc2().getInvoice() == null ? BBill.NONE_ID : accLine.getAcc2()
          .getInvoice().id());
      accountId.setBizId(CollectionUtil.toString(list));
      hasAddedAccIds.add(accountId);
    }
    return hasAddedAccIds;
  }

  /** 合计支付金额：按科目单收款时 */
  private void aggregateDefrayalTotal(int scale, RoundingMode roundingMode) {
    defrayalTotal = BigDecimal.ZERO;
    for (BPaymentLine line : getAccountLines()) {
      if (line.getDefrayalTotal() != null) {
        defrayalTotal = defrayalTotal.add(line.getDefrayalTotal());
      }
    }

    for (BPaymentLine line : getCollectionLines()) {
      if (line.getDefrayalTotal() != null) {
        defrayalTotal = defrayalTotal.add(line.getDefrayalTotal());
      }
    }
  }

  /** 合计本次应收付金额以及账款应收付金额预存款金额 */
  public void aggregate(int scale, RoundingMode roundingMode) {
    BTotal unpayedTempTotal = BTotal.zero();
    BTotal temptotal = BTotal.zero();

    defrayalTotal = BigDecimal.ZERO;
    for (BPaymentCashDefrayal cash : getCashs()) {
      if (cash.getTotal() != null) {
        defrayalTotal = defrayalTotal.add(cash.getTotal());
      }
    }
    if (CPaymentDefrayalType.lineSingle.equals(getDefrayalType())) {
      aggregateDefrayalTotal(scale, roundingMode);
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

    if (CPaymentDefrayalType.lineSingle.equals(getDefrayalType())==false) {
      for (BPaymentDepositDefrayal defrayal : getDeposits()) {
        temptotal.setTotal(temptotal.getTotal().add(defrayal.getTotal()));
      }
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

    // 只有应收为正时才算预存款
    if (receivedTotal.compareTo(unpayedTotal.getTotal()) > 0
        && unpayedTotal.getTotal().compareTo(BigDecimal.ZERO) > 0) {
      this.depositTotal = receivedTotal.subtract(unpayedTotal.getTotal());
    } else {
      this.depositTotal = BigDecimal.ZERO;
    }
    
    if (CPaymentDefrayalType.bill.equals(getDefrayalType()) == false) {
      this.depositTotal = BigDecimal.ZERO;
      for(BPaymentAccountLine line:accountLines){
        line.setDepositTotal(BigDecimal.ZERO);
        if(line.getDefrayalTotal().compareTo(line.getUnpayedTotal().getTotal())>0){
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
    BTotal overdueTempTotal = BTotal.zero();
    for (BPaymentOverdueLine line : getOverdueLines()) {
      overdueTempTotal = overdueTempTotal.add(line.getTotal());
    }
    overdueTempTotal.getTotal().setScale(2, RoundingMode.HALF_UP);
    overdueTempTotal.getTax().setScale(scale, roundingMode);
    this.overdueTotal = overdueTempTotal;
  }

  /**
   * 只有按科目收款新建或编辑时才会用到。从收款单明细行中合计出收款信息 ，实收信息放进单头的cashs列表，扣预存款信息放进单头的deposits列表。
   */
  public void aggreateCashsAndDepositsFromPaymentLine() {
    this.getCashs().clear();
    this.getDeposits().clear();

    for (BPaymentLine line : accountLines) {
      // 账款明细
      for (BPaymentLineCash lineCash : line.getCashs()) {
        if (lineCash.getTotal() != null)
          addCash(lineCash);
      }
      for (BPaymentLineDeposit lineDeposit : line.getDeposits()) {
        if (lineDeposit.getSubject() != null
            && !StringUtil.isNullOrBlank(lineDeposit.getSubject().getUuid())
            && lineDeposit.getTotal() != null)
          addDeposit(lineDeposit);
      }
    }

    // 代收明细
    for (BPaymentLine line : collectionLines) {
      for (BPaymentLineCash lineCash : line.getCashs()) {
        if (lineCash.getTotal() != null)
          addCash(lineCash);
      }
    }
  }

  private void addCash(BPaymentLineCash lineCash) {
    boolean finded = false;
    for (BPaymentCashDefrayal cash : this.getCashs()) {
      if (ObjectUtil.isEquals(cash.getPaymentType(), lineCash.getPaymentType())
          && ObjectUtil.isEquals(cash.getBank(), lineCash.getBank())) {
        cash.setTotal(cash.getTotal().add(lineCash.getTotal()));
        finded = true;
        break;
      }
    }
    if (!finded) {
      BPaymentCashDefrayal cash = new BPaymentCashDefrayal();
      cash.setBank(lineCash.getBank());
      cash.setPaymentType(lineCash.getPaymentType());
      cash.setTotal(lineCash.getTotal());
      this.getCashs().add(cash);
    }
  }

  private void addDeposit(BPaymentLineDeposit lineDeposit) {
    boolean finded = false;
    for (BPaymentDepositDefrayal deposit : this.getDeposits()) {
      if (ObjectUtil.isEquals(deposit.getSubject(), lineDeposit.getSubject())
          && ObjectUtil.isEquals(deposit.getContract(), lineDeposit.getContract())) {
        deposit.setRemainTotal(lineDeposit.getRemainTotal());
        deposit.setTotal(deposit.getTotal().add(lineDeposit.getTotal()));
        finded = true;
        break;
      }
    }
    if (!finded) {
      BPaymentDepositDefrayal deposit = new BPaymentDepositDefrayal();
      deposit.setSubject(lineDeposit.getSubject());
      deposit.setContract(lineDeposit.getContract());
      deposit.setRemainTotal(lineDeposit.getRemainTotal());
      deposit.setTotal(lineDeposit.getTotal());
      this.getDeposits().add(deposit);
    }
  }

  @Override
  public BUCN getStore() {
    return accountUnit;
  }

  public String getContractNameToStr() {
    List<String> list = new ArrayList<String>();
    for (BPaymentAccountLine line : this.getAccountLines()) {
      if (!list.contains(line.getAcc1().getContract().getName())) {
        list.add(line.getAcc1().getContract().getName());
      }
    }
    return list.toString().substring(1, list.toString().length() - 1);
  }
}
