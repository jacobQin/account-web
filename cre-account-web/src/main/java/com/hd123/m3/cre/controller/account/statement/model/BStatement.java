/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BStatement.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月14日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.statement.Statement;

/**
 * @author chenganbang
 *
 */
public class BStatement extends Statement {
  private static final long serialVersionUID = -7221623431078336117L;

  private List<BStatementDetail> details = new ArrayList<BStatementDetail>();
  private List<BSettlement> settlement = new ArrayList<BSettlement>();
  private BigDecimal owedAmount = BigDecimal.ZERO;
  private List<BStatementSumLine> sumLines = new ArrayList<BStatementSumLine>();
  private List<BMoreBtnPerm> moreBtn = new ArrayList<BMoreBtnPerm>();

  private String statementState;
  private boolean showRefer = false;
  private boolean lastAccountSettle = true;
  private boolean hasAdjust = false;

  /**
   * 账款明细
   * 
   * @return
   */
  public List<BStatementDetail> getDetails() {
    return details;
  }

  public void setDetails(List<BStatementDetail> details) {
    this.details = details;
  }

  /** 费用周期 */
  public List<BSettlement> getSettlement() {
    return settlement;
  }

  public void setSettlement(List<BSettlement> settlement) {
    this.settlement = settlement;
  }

  /** 欠款金额 */
  public BigDecimal getOwedAmount() {
    return owedAmount;
  }

  public void setOwedAmount(BigDecimal owedAmount) {
    this.owedAmount = owedAmount;
  }

  /**
   * 账款明细行(用于查看页的账款明细表格显示)
   * 
   * @return
   */
  public List<BStatementSumLine> getSumLines() {
    return sumLines;
  }

  public void setSumLines(List<BStatementSumLine> sumLines) {
    this.sumLines = sumLines;
  }

  /**
   * 是否显示销售额
   * 
   * @return
   */
  public boolean isShowRefer() {
    return showRefer;
  }

  public void setShowRefer(boolean showRefer) {
    this.showRefer = showRefer;
  }

  /**
   * 最近出账日期的账单
   * 
   * @return
   */
  public boolean isLastAccountSettle() {
    return lastAccountSettle;
  }

  public void setLastAccountSettle(boolean lastAccountSettle) {
    this.lastAccountSettle = lastAccountSettle;
  }

  public List<BMoreBtnPerm> getMoreBtn() {
    return moreBtn;
  }

  public void setMoreBtn(List<BMoreBtnPerm> moreBtn) {
    this.moreBtn = moreBtn;
  }

  /**
   * 账单状态
   * 
   * @return
   */
  public String getStatementState() {
    return statementState;
  }

  public void setStatementState(String statementState) {
    this.statementState = statementState;
  }

  /**
   * 是否有相关联的账单调整单
   * 
   * @return
   */
  public boolean isHasAdjust() {
    return hasAdjust;
  }

  public void setHasAdjust(boolean hasAdjust) {
    this.hasAdjust = hasAdjust;
  }

}
