/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	SCounterpart.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-8 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.util.Map;

import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenrizhang
 * 
 */
public class BCounterpart extends BUCN {
  private static final long serialVersionUID = -7778984132006127407L;

  public static final String FIELD_CODE = "code";
  public static final String FIELD_NAME = "name";

  /** 对方单位类型：商户 */
  public static final String COUNPERPART_TENANT = "_Tenant";
  /** 对方单位类型：业主 */
  public static final String COUNPERPART_PROPRIETOR = "_Proprietor";
  
  private String counterpartType;

  /** 对方单位类型 */
  public String getCounterpartType() {
    return counterpartType;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  public String toFriendlyStr(Map<String, String> counterpartTypeMap) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(ObjectUtil.nvl(this.getCode(), ""));
    sb.append("]");
    sb.append(ObjectUtil.nvl(this.getName(), ""));
    if (counterpartTypeMap.size() > 1) {
      sb.append("[");
      sb.append(ObjectUtil.nvl(counterpartTypeMap.get(this.getCounterpartType()), ""));
      sb.append("]");
    }
    return sb.toString();
  }
  
  public String toNameStr(Map<String, String> counterpartTypeMap) {
    StringBuilder sb = new StringBuilder();
    sb.append(ObjectUtil.nvl(this.getName(), ""));
    if (counterpartTypeMap.size() > 1) {
      sb.append("[");
      sb.append(ObjectUtil.nvl(counterpartTypeMap.get(this.getCounterpartType()), ""));
      sb.append("]");
    }
    return sb.toString();
  }

}
