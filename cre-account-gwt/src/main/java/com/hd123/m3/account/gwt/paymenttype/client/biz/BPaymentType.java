/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	BPaymentType.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.client.biz;

import com.hd123.m3.commons.gwt.base.client.biz.HasSummary;
import com.hd123.rumba.commons.gwt.entity.client.BStandardEntity;

/**
 * 付款方式B对象
 * 
 * @author zhuhairui
 * 
 */
public class BPaymentType extends BStandardEntity implements HasSummary{

  private static final long serialVersionUID = 5228587165816062097L;
  private String code;
  private String name;
  private boolean enabled;
  private String remark;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public String toString() {
    return toFriendlyStr();
  }

  public String toFriendlyStr() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append(getCode() == null ? "" : getCode());
    sb.append("]");
    sb.append(getName() == null ? "" : getName());
    return sb.toString();
  }
}
