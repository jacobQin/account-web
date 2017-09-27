package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.acc.Acc1;
import com.hd123.m3.account.service.acc.Acc2;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 收付款单账款明细行
 * 
 * @author LiBin
 * 
 */
public class BPaymentAccountLine extends BPaymentLine {

  private static final long serialVersionUID = -4170188784252028318L;

  public static final String FN_ACCOUNTLINE_TOTAL = "accountLineTotal";
  public static final String FN_ACCOUNTLINE_UNPAYEDTOTAL = "accountLineUnpayedTotal";
  public static final String FN_ACCOUNTLINE_REMARK = "accountLineRemark";

  /** 排序字段：起始时间 */
  public static String ORDER_Y_EGINTIME = "beginTime";
  /** 排序字段：结束时间 */
  public static String ORDER_Y_ENDTIME = "endTime";
  /** 排序字段：来源单号 */
  public static String ORDER_Y_SOURCEILL = "acc1.sourceill.billNumber";
  /** 排序字段：来源单号 */
  public static String ORDER_Y_SUJECT = "acc1.subject";

  private Acc1 acc1 = new Acc1();
  private Acc2 acc2 = new Acc2();
  private Total originTotal = Total.zero();
  private Total overdueTotal = Total.zero();
  private String ivcType;
  private String ivcCode;
  private String ivcNumber;
  private Date ivcDate;
  private Total ivcTotal;
  
  private boolean priorityDeduction;

  private List<BPaymentAccOverdue> overdues = new ArrayList<BPaymentAccOverdue>();

  /** 滞纳金条款 */
  private List<BPaymentOverdueTerm> overdueTerms = new ArrayList<BPaymentOverdueTerm>();

  public Acc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(Acc1 acc1) {
    this.acc1 = acc1;
  }

  public Acc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(Acc2 acc2) {
    this.acc2 = acc2;
  }

  public Total getOriginTotal() {
    return originTotal;
  }

  public void setOriginTotal(Total originTotal) {
    this.originTotal = originTotal;
  }

  public Total getOverdueTotal() {
    return overdueTotal;
  }

  public void setOverdueTotal(Total overdueTotal) {
    this.overdueTotal = overdueTotal;
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

  public Total getIvcTotal() {
    return ivcTotal;
  }

  public void setIvcTotal(Total ivcTotal) {
    this.ivcTotal = ivcTotal;
  }

  /**优先抵扣*/
  public boolean isPriorityDeduction() {
    return priorityDeduction;
  }
  
  public void setPriorityDeduction(boolean priorityDeduction) {
    this.priorityDeduction = priorityDeduction;
  }
  
  public List<BPaymentAccOverdue> getOverdues() {
    return overdues;
  }

  public void setOverdues(List<BPaymentAccOverdue> overdues) {
    this.overdues = overdues;
  }

  public List<BPaymentOverdueTerm> getOverdueTerms() {
    return overdueTerms;
  }

  public void setOverdueTerms(List<BPaymentOverdueTerm> overdueTerms) {
    this.overdueTerms = overdueTerms;
  }

  /**
   * 计算该条账款的滞纳金以及添加其滞纳金明细。
   * 
   * @param overdueDays
   *          滞纳天数
   * @scale 小数精度
   * @roundingMode 舍入算法
   */
  public void calculateOverdue(long overdueDays, int scale, RoundingMode roundingMode) {
    if (overdueDays <= 0) {
      this.getOverdues().clear();
      this.setOverdueTotal(Total.zero());
      return;
    }

    BigDecimal totalOftotal = this.getTotal().getTotal();

    if (totalOftotal == null || totalOftotal.compareTo(BigDecimal.ZERO) <= 0) {
      // this.getOverdues().clear();
      // 设置该条账款的滞纳金金额和税额
      this.setOverdueTotal(Total.zero());
      return;
    }

    BigDecimal overdueTotalOftotal = BigDecimal.ZERO;
    BigDecimal overdueTotalOftax = BigDecimal.ZERO;
    List<BPaymentAccOverdue> newOverdues = new ArrayList<BPaymentAccOverdue>();
    for (int i = 0; i < overdueTerms.size(); i++) {
      BPaymentOverdueTerm overdueTerm = overdueTerms.get(i);

      if (!overdueTerm.isAllSubjects()) {
        boolean find = false;
        for (UCN s : overdueTerm.getSubjects()) {
          if (acc1.getSubject().equals(s)) {
            find = true;
            break;
          }
        }
        if (!find)
          continue;
      }

      // 计算该条滞纳金条款的滞纳金金额和税额
      BigDecimal overdueTotalocal = overdueTerm.calculateOverdueTotalTotal(totalOftotal,
          overdueDays);
      BigDecimal overdueTax = overdueTerm.calculateOverdueTotalTax(overdueTotalocal, scale,
          roundingMode);

      if (BigDecimal.ZERO.compareTo(overdueTotalocal) == 0
          && BigDecimal.ZERO.compareTo(overdueTax) == 0 && this.getOverdues().isEmpty())
        continue;

      overdueTotalOftotal = overdueTotalOftotal.add(overdueTotalocal);
      overdueTotalOftax = overdueTotalOftax.add(overdueTax);

      BPaymentAccOverdue accOverdueLine = new BPaymentAccOverdue();
      accOverdueLine.setContract(this.getAcc1().getContract());
      accOverdueLine.setSubject(overdueTerm.getSubject());
      accOverdueLine.setDirection(this.getAcc1().getDirection());
      accOverdueLine.setTaxRate(overdueTerm.getTaxRate());
      accOverdueLine.setTotal(new Total(overdueTotalocal, overdueTax));
      accOverdueLine.setIssueInvoice(overdueTerm.isInvoice());
      if (overdueTotalocal.compareTo(BigDecimal.ZERO) != 0) {
        newOverdues.add(accOverdueLine);
      }

    }
    // 设置新生成的滞纳金明细
    this.getOverdues().clear();
    this.getOverdues().addAll(newOverdues);
    // 设置该条账款的滞纳金金额和税额
    this.setOverdueTotal(new Total(overdueTotalOftotal, overdueTotalOftax));
  }

}
