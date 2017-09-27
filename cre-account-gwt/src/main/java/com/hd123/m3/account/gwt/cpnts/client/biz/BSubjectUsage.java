/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	BSubjectUsage.java
 * 模块说明：	
 * 修改历史：
 * 2013-8-15 - suizhe - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import java.io.Serializable;

/**
 * @author suizhe
 * 
 */
public class BSubjectUsage implements Serializable {
  private static final long serialVersionUID = -4091185682305987538L;

  private String code;
  private String name;
  private BSubjectType type;
  private BUsageType usageType;
  private String remark;

  /** 用途编码 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /** 用途名称 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** 科目类型 */
  public BSubjectType getType() {
    return type;
  }

  public void setType(BSubjectType type) {
    this.type = type;
  }

  /** 用途类型 */
  public BUsageType getUsageType() {
    return usageType;
  }

  public void setUsageType(BUsageType usageType) {
    this.usageType = usageType;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((remark == null) ? 0 : remark.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof BSubjectUsage))
      return false;

    BSubjectUsage other = (BSubjectUsage) obj;
    if (code == null || other.code == null)
      return false;

    return code.equals(other.code);
  }

  public BSubjectUsage clone() {
    BSubjectUsage r = new BSubjectUsage();
    r.code = code;
    r.name = name;
    r.type = type;
    r.usageType = usageType;
    r.remark = remark;
    return r;
  }
}
