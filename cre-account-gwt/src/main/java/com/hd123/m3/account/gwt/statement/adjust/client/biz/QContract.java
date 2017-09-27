/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BContract.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.biz;

import java.io.Serializable;

import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;

/**
 * @author zhuhairui
 * 
 */
public class QContract implements Serializable {

  private static final long serialVersionUID = 5744803099527940256L;

  private BBill contract;
  private String businessUnitCode;
  private BCounterpart counterpart;
  private String title;

  /** 合同 */
  public BBill getContract() {
    return contract;
  }

  public void setContract(BBill contract) {
    this.contract = contract;
  }

  /** 项目代码 */
  public String getBusinessUnitCode() {
    return businessUnitCode;
  }

  public void setBusinessUnitCode(String businessUnitCode) {
    this.businessUnitCode = businessUnitCode;
  }

  /** 商户代码 */
  public BCounterpart getCounterpart() {
    return counterpart;
  }

  public void setCounterpart(BCounterpart counterpart) {
    this.counterpart = counterpart;
  }

  /** 店招 */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
