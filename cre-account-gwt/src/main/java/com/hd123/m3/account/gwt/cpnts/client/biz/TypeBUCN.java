/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	SPosition.java
 * 模块说明：	
 * 修改历史：
 * 2016年1月29日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.biz;

import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.commons.gwt.entity.client.HasCode;
import com.hd123.rumba.commons.gwt.entity.client.HasName;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.BWithUCN;

/**
 * 带有type属性的BUCN
 * 
 * @author LiBin
 * 
 */
public class TypeBUCN extends BEntity implements BWithUCN {

  private static final long serialVersionUID = -4227062364338089770L;

  private String code;
  private String name;
  private String type;

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toFriendlyStr() {
    return BUCN.toFriendlyStr(this);
  }

  @Override
  public void inject(Object source) {
    super.inject(source);
    if (source instanceof HasCode) {
      setCode(((HasCode) source).getCode());
    }
    if (source instanceof HasName) {
      setName(((HasName) source).getName());
    }
    if (source instanceof TypeBUCN) {
      setType(((TypeBUCN) source).getType());
    }
  }

}
