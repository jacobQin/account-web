/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BAccountDetail.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-1 - huangjunxian- 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.commons.gwt.util.client.date.BDateRange;

/**
 * 结算详情
 * 
 * @author chenganbang
 * 
 */
public class BStatementAccDetail implements Serializable {

  private static final long serialVersionUID = 300100L;

  private String accSrcType;
  private String caption;
  private BigDecimal total;
  private BigDecimal freeTotal;
  private BDateRange dateRange;
  private List<BStringPairGroup> groups = new ArrayList<BStringPairGroup>();
  private List<String> details = new ArrayList<String>();
  private List<BStatementAccDetail> childs = new ArrayList<BStatementAccDetail>();
  private List<BStatementAccFreeDetail> freeDetails = new ArrayList<BStatementAccFreeDetail>();

  /** 账款来源类型 */
  public String getAccSrcType() {
    return accSrcType;
  }

  public void setAccSrcType(String accSrcType) {
    this.accSrcType = accSrcType;
  }

  /** 账款名称 */
  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  /** 金额 */
  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getFreeTotal() {
    return freeTotal;
  }

  public void setFreeTotal(BigDecimal freeTotal) {
    this.freeTotal = freeTotal;
  }

  /** 账款有效期 */
  public BDateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(BDateRange dateRange) {
    this.dateRange = dateRange;
  }

  /** 表格组数据 */
  public List<BStringPairGroup> getGroups() {
    return groups;
  }

  public void setGroups(List<BStringPairGroup> groups) {
    this.groups = groups;
  }

  /** 计算明细 */
  public List<String> getDetails() {
    return details;
  }

  public void setDetails(List<String> details) {
    this.details = details;
  }

  /** 子项 */
  public List<BStatementAccDetail> getChilds() {
    return childs;
  }

  public void setChilds(List<BStatementAccDetail> childs) {
    this.childs = childs;
  }

  /** 免租记录明细 */
  public List<BStatementAccFreeDetail> getFreeDetails() {
    return freeDetails;
  }

  public void setFreeDetails(List<BStatementAccFreeDetail> freeDetails) {
    this.freeDetails = freeDetails;
  }

}
