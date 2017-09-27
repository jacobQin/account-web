/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BStatementSubject.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月15日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.commons.Bill;
import com.hd123.m3.account.commons.SourceBill;
import com.hd123.m3.commons.biz.entity.LevelRef;
import com.hd123.m3.cre.controller.account.accountdefrayal.convert.Date2StrUtil;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.gwt.entity.client.BEntity;

/**
 * 科目收付情况
 * 
 * @author chenganbang
 *
 */
public class BAccountDefrayal extends BEntity {
  private static final long serialVersionUID = 8835046486765657616L;

  private int light;
  private String settleState;
  private LevelRef bizType;
  private UCN contract;
  private UCN accountUnit;
  private UCN counterpart;
  private String counterpartType;
  private String position = "";
  private UCN subject;
  private int direction;
  private String settlement;
  private BigDecimal needSettle = BigDecimal.ZERO;
  private BigDecimal settled = BigDecimal.ZERO;
  private BigDecimal owedAmount = BigDecimal.ZERO;
  private BigDecimal invoiced = BigDecimal.ZERO;
  private String ivcNumber;
  private String settleNo;
  private Date lastPayDate;
  private String coopMode;
  private SourceBill srcBill;
  private Bill statement;
  private Date accountDate;
  private boolean locked = false;

  /**
   * 亮灯情况，取值1和-1
   * 
   * @return
   */
  public int getLight() {
    return light;
  }

  public void setLight(int light) {
    this.light = light;
  }

  /**
   * 状态(每条条款的状态)
   * 
   * @return
   */
  public String getSettleState() {
    return settleState;
  }

  public void setSettleState(String settleState) {
    this.settleState = settleState;
  }

  /**
   * 业态，取自招商合同
   * 
   * @return
   */
  public LevelRef getBizType() {
    return bizType;
  }

  public void setBizType(LevelRef bizType) {
    this.bizType = bizType;
  }

  /**
   * 合同
   * 
   * @return
   */
  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

  /**
   * 商户
   * 
   * @return
   */
  public UCN getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(UCN counterpart) {
    this.counterpart = counterpart;
  }

  /**
   * 商户类型
   * 
   * @return
   */
  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  /**
   * 位置
   * 
   * @return
   */
  public String getPosition() {
    return position;
  }

  public void setPosition(List<UCN> positions) {
    if (positions != null && !positions.isEmpty()) {
      for (int i = 0; i < positions.size(); i++) {
        UCN ucn = positions.get(i);
        position = position + ucn.getName() + "[" + ucn.getCode()
            + (i == positions.size() - 1 ? "]" : "],");
      }
    }
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
   * 费用周期
   * 
   * @return
   */
  public String getSettlement() {
    return settlement;
  }

  public void setSettlement(Date beginDate, Date endDate) {
    if (beginDate != null && endDate != null) {
      settlement = Date2StrUtil.getInstance().convert(beginDate) + " ~ "
          + Date2StrUtil.getInstance().convert(endDate);
    } else if (beginDate != null && endDate == null) {
      settlement = Date2StrUtil.getInstance().convert(beginDate) + " ~ ";
    } else if (beginDate == null && endDate != null) {
      settlement = " ~ " + Date2StrUtil.getInstance().convert(endDate);
    } else {
      settlement = "";
    }
  }

  /**
   * 应收金额
   * 
   * @return
   */
  public BigDecimal getNeedSettle() {
    return needSettle;
  }

  public void setNeedSettle(BigDecimal needSettle) {
    this.needSettle = needSettle;
  }

  /**
   * 实收金额
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
   * 发票登记金额
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
   * 发票号码
   * 
   * @return
   */
  public String getIvcNumber() {
    return ivcNumber;
  }

  public void setIvcNumber(String ivcNumber) {
    this.ivcNumber = ivcNumber;
  }

  /**
   * 结转期
   * 
   * @return
   */
  public String getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(String settleNo) {
    this.settleNo = settleNo;
  }

  /**
   * 最后缴款期
   * 
   * @return
   */
  public Date getLastPayDate() {
    return lastPayDate;
  }

  public void setLastPayDate(Date lastPayDate) {
    this.lastPayDate = lastPayDate;
  }

  /**
   * 合作方式
   * 
   * @return
   */
  public String getCoopMode() {
    return coopMode;
  }

  public void setCoopMode(String coopMode) {
    this.coopMode = coopMode;
  }

  /**
   * 来源单据
   * 
   * @return
   */
  public SourceBill getSrcBill() {
    return srcBill;
  }

  public void setSrcBill(SourceBill srcBill) {
    this.srcBill = srcBill;
  }

  /**
   * 账单
   * 
   * @return
   */
  public Bill getStatement() {
    return statement;
  }

  public void setStatement(Bill statement) {
    this.statement = statement;
  }

  /**
   * 记账日期
   * 
   * @return
   */
  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  /** 结算单位 */
  public UCN getAccountUnit() {
    return accountUnit;
  }

  public void setAccountUnit(UCN accountUnit) {
    this.accountUnit = accountUnit;
  }

  /** 账款是否被锁定 */
  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

}
