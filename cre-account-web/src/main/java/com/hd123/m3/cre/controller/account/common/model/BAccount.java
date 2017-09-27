/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BAccount.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月26日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.model;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.payment.PaymentOverdueTerm;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 账款对象
 * 
 * @author LiBin
 *
 */
public class BAccount extends Account {

  private static final long serialVersionUID = -8222958282993475941L;

  private static final Converter<Account, BAccount> CONVETER = ConverterBuilder.newBuilder(
      Account.class, BAccount.class).build();

  public static BAccount newInstance(Account source) {
    BAccount account = CONVETER.convert(source);
    decorate(account);
    return account;
  }

  private static void decorate(BAccount account) {

  }

  private String subjectUsageName;
  private List<PaymentOverdueTerm> terms = new ArrayList<PaymentOverdueTerm>();
  private String direction;
  private String subjectCode;
  private UCN bank;

  /** 账款科目用途名称 */
  public String getSubjectUsageName() {
    return subjectUsageName;
  }

  public void setSubjectUsageName(String subjectUsageName) {
    this.subjectUsageName = subjectUsageName;
  }

  /** 滞纳金条款 */
  public List<PaymentOverdueTerm> getTerms() {
    return terms;
  }

  public void setTerms(List<PaymentOverdueTerm> terms) {
    this.terms = terms;
  }

  /** 收付方向名称：收或者付 */
  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  /** 科目代码 */
  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  /** 银行*/
  public UCN getBank() {
    return bank;
  }

  
  public void setBank(UCN bank) {
    this.bank = bank;
  }

}
