/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BEmployee.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-22 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.BWithUCN;

/**
 * @author chenrizhang
 * 
 */
public class BEmployee extends BEntity implements BWithUCN {
  private static final long serialVersionUID = -2777423179893040918L;

  public static final String FIELD_CODE = "code";
  public static final String FIELD_NAME = "name";
  public static final String FIELD_PHONE = "phone";
  public static final String FIELD_MOBILE = "mobile";
  public static final String FIELD_EMAIL = "email";

  private String code;
  private String name;
  private String phone;
  private String mobile;
  private String email;

  @Override
  public String toFriendlyStr() {
    return BUCN.toFriendlyStr(this);
  }

  /** 代码 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /** 姓名 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** 电话 */
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  /** 移动电话 */
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /** 电子邮件 */
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
