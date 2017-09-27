/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2010，所有权利保留。
 * 
 * 项目名：	M3
 * 文件名：	BSubject.java
 * 模块说明：	
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */
package com.hd123.m3.account.gwt.subject.client.biz;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.rumba.commons.gwt.entity.client.BStandardEntity;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.BWithUCN;

/**
 * @author cRazy
 * 
 */
public class BSubject extends BStandardEntity implements BWithUCN, HasSummary {
  private static final long serialVersionUID = 300200L;

  private String code;
  private String name;
  private int direction;
  private boolean enabled;
  private String type;
  private String customType;
  private BTaxRate taxRate;
  private String remark;

  private List<String> usages = new ArrayList<String>();
  
  private List<BSubjectStoreTaxRate> storeTaxRates = new ArrayList<BSubjectStoreTaxRate>();

  @Override
  public String toFriendlyStr() {
    return BUCN.toFriendlyStr(this);
  }

  @Override
  public String toString() {
    return toFriendlyStr();
  }

  /** 代码 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /** 名称 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** 方向 */
  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  /** 是否启用 */
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /** 科目类型 */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /** 自定义类型 */
  public String getCustomType() {
    return customType;
  }

  public void setCustomType(String customType) {
    this.customType = customType;
  }

  /** 税率 */
  public BTaxRate getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BTaxRate taxRate) {
    this.taxRate = taxRate;
  }

  /** 科目用途 */
  public List<String> getUsages() {
    return usages;
  }

  public void setUsages(List<String> usages) {
    this.usages = usages;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 项目税率明细*/
  public List<BSubjectStoreTaxRate> getStoreTaxRates() {
    return storeTaxRates;
  }

  public void setStoreTaxRates(List<BSubjectStoreTaxRate> storeTaxRates) {
    this.storeTaxRates = storeTaxRates;
  }
}
