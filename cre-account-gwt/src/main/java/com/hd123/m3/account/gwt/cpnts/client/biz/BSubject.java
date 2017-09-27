/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BSubject.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.BWithUCN;

/**
 * @author subinzhu
 * 
 */
public class BSubject implements BWithUCN, Serializable {

  private static final long serialVersionUID = -3014239808706142442L;

  private BUCN subject;
  private BTaxRate taxRate;
  private int direction;
  private String subjectType;
  private List<BSubjectStoreTaxRate> storeTaxRates = new ArrayList<BSubjectStoreTaxRate>();

  public BUCN getSubject() {
    return subject;
  }

  public void setSubject(BUCN subject) {
    this.subject = subject;
  }

  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public BTaxRate getTaxRate(String storeUuid) {
    if (storeUuid != null) {
      for (BSubjectStoreTaxRate storeTaxRate : storeTaxRates) {
        if (storeUuid.equals(storeTaxRate.getStore().getUuid())) {
          return storeTaxRate.getTaxRate();
        }
      }
    }
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  @Override
  public String getUuid() {
    return subject == null ? null : subject.getUuid();
  }

  @Override
  public String getCode() {
    return subject == null ? null : subject.getCode();
  }

  @Override
  public String getName() {
    return subject == null ? null : subject.getName();
  }

  public String getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(String subjectType) {
    this.subjectType = subjectType;
  }

  public List<BSubjectStoreTaxRate> getStoreTaxRates() {
    return storeTaxRates;
  }

  public void setStoreTaxRates(List<BSubjectStoreTaxRate> storeTaxRates) {
    this.storeTaxRates = storeTaxRates;
  }

  @Override
  public String toFriendlyStr() {
    if (subject == null)
      return "[]";
    if (subject.getCode() == null && subject.getName() == null)
      return "[]";
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append(ObjectUtil.nvl(subject.getCode(), ""));
    sb.append("]");
    sb.append(ObjectUtil.nvl(subject.getName(), ""));
    return sb.toString();
  }

  @Override
  public void setUuid(String uuid) throws UnsupportedOperationException {
    if (subject == null)
      subject = new BUCN();
    subject.setUuid(uuid);
  }

  @Override
  public void setCode(String code) throws UnsupportedOperationException {
    if (subject == null)
      subject = new BUCN();
    subject.setCode(code);
  }

  @Override
  public void setName(String name) throws UnsupportedOperationException {
    if (subject == null)
      subject = new BUCN();
    subject.setName(name);
  }

}
