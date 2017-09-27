/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentAccountLine.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAcc2;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridElement;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 收付款单账款明细行
 * 
 * @author subinzhu
 * 
 */
public class BPaymentAccountLine extends BPaymentLine implements EditGridElement {

  private static final long serialVersionUID = -4170188784252028318L;

  public static final String FN_ACCOUNTLINE_TOTAL = "accountLineTotal";
  public static final String FN_ACCOUNTLINE_UNPAYEDTOTAL = "accountLineUnpayedTotal";
  public static final String FN_ACCOUNTLINE_REMARK = "accountLineRemark";
  
  /**排序字段：起始时间*/
  public static String ORDER_BY_BEGINTIME = "beginTime";
  /**排序字段：结束时间*/
  public static String ORDER_BY_ENDTIME = "endTime";
  /**排序字段：来源单号*/
  public static String ORDER_BY_SOURCEBILL = "acc1.sourceBill.billNumber";
  /**排序字段：来源单号*/
  public static String ORDER_BY_SUBJECT = "acc1.subject";

  private BAcc1 acc1 = new BAcc1();
  private BAcc2 acc2 = new BAcc2();
  private BTotal originTotal = BTotal.zero();
  private BTotal overdueTotal = BTotal.zero();
  private String ivcType;
  private String ivcCode;
  private String ivcNumber;
  private Date ivcDate;
  private BTotal ivcTotal;

  /** 辅助属性，仅在编辑界面使用，用于判断该条明细是否是在编辑界面中新添加的。 */
  private boolean isNewAdded = false;

  private List<BPaymentAccOverdue> overdues = new ArrayList<BPaymentAccOverdue>();

  /** 滞纳金差额 */
  private List<BPaymentAccOverdue> balanceOverdues = new ArrayList<BPaymentAccOverdue>();

  /** 滞纳金条款 */
  private List<BPaymentOverdueTerm> overdueTerms = new ArrayList<BPaymentOverdueTerm>();

  public BAcc1 getAcc1() {
    return acc1;
  }

  public void setAcc1(BAcc1 acc1) {
    this.acc1 = acc1;
  }

  public BAcc2 getAcc2() {
    return acc2;
  }

  public void setAcc2(BAcc2 acc2) {
    this.acc2 = acc2;
  }

  public BTotal getOriginTotal() {
    return originTotal;
  }

  public void setOriginTotal(BTotal originTotal) {
    this.originTotal = originTotal;
  }

  public BTotal getOverdueTotal() {
    return overdueTotal;
  }

  public void setOverdueTotal(BTotal overdueTotal) {
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

  public BTotal getIvcTotal() {
    return ivcTotal;
  }

  public void setIvcTotal(BTotal ivcTotal) {
    this.ivcTotal = ivcTotal;
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

  public List<BPaymentAccOverdue> getBalanceOverdues() {
    return balanceOverdues;
  }

  public void setBalanceOverdues(List<BPaymentAccOverdue> balanceOverdues) {
    this.balanceOverdues = balanceOverdues;
  }

  public boolean isNewAdded() {
    return isNewAdded;
  }

  public void setNewAdded(boolean isNewAdded) {
    this.isNewAdded = isNewAdded;
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
      this.getBalanceOverdues().clear();
      this.setOverdueTotal(BTotal.zero());
      return;
    }

    BigDecimal totalOftotal = this.getTotal().getTotal();

    if (totalOftotal == null || totalOftotal.compareTo(BigDecimal.ZERO) <= 0) {
      // this.getOverdues().clear();
      // 设置该条账款的滞纳金金额和税额
      this.setOverdueTotal(BTotal.zero());
      return;
    }

    balanceOverdues.clear();

    BigDecimal overdueTotalOftotal = BigDecimal.ZERO;
    BigDecimal overdueTotalOftax = BigDecimal.ZERO;
    List<BPaymentAccOverdue> newOverdues = new ArrayList<BPaymentAccOverdue>();
    for (int i = 0; i < overdueTerms.size(); i++) {
      BPaymentOverdueTerm overdueTerm = overdueTerms.get(i);

      if (!overdueTerm.isAllSubjects()) {
        boolean find = false;
        for (BUCN s : overdueTerm.getSubjects()) {
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
      accOverdueLine.setLine(this);
      accOverdueLine.setContract(this.getAcc1().getContract());
      accOverdueLine.setSubject(overdueTerm.getSubject());
      accOverdueLine.setDirection(this.getAcc1().getDirection());
      accOverdueLine.setTaxRate(overdueTerm.getTaxRate());
      accOverdueLine.setTotal(new BTotal(overdueTotalocal, overdueTax));
      accOverdueLine.setIssueInvoice(overdueTerm.isInvoice());
      if (overdueTotalocal.compareTo(BigDecimal.ZERO) != 0) {
        newOverdues.add(accOverdueLine);
      }

      if (!this.getOverdues().isEmpty()) {
        // 产生过滞纳金明细，要添加滞纳金明细差额
        BTotal balanceTotal = new BTotal(overdueTotalocal, overdueTax).subtract(overdues.get(i)
            .getTotal());

        if (BTotal.zero().equals(balanceTotal))
          continue;

        BPaymentAccOverdue balanceOverdueLine1 = new BPaymentAccOverdue();
        balanceOverdueLine1.setContract(this.getAcc1().getContract());
        balanceOverdueLine1.setSubject(overdueTerm.getSubject());
        balanceOverdueLine1.setTaxRate(overdueTerm.getTaxRate());
        balanceOverdueLine1.setTotal(balanceTotal);
        balanceOverdues.add(balanceOverdueLine1);
      }
    }
    // 设置新生成的滞纳金明细
    this.getOverdues().clear();
    this.getOverdues().addAll(newOverdues);
    // 设置该条账款的滞纳金金额和税额
    this.setOverdueTotal(new BTotal(overdueTotalOftotal, overdueTotalOftax));
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean match(EditGridElement other) {
    return false;
  }
}
