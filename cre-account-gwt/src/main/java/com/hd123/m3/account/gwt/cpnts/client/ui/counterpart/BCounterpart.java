/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BCounterpart.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月7日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.counterpart;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenrizhang
 *
 */
public class BCounterpart extends BUCN {
  private static final long serialVersionUID = 6194977441260966116L;

  public static final String COUNTERPART_TYPE_TENANT = "_Tenant";
  public static final String COUNTERPART_TYPE_PROPRIETOR = "_Proprietor";

  public static final String KEY_FILTER_STATE = "filter_state";
  public static final String KEY_FILTER_MODULE = "filter_module";

  public BCounterpart() {
  }

  public BCounterpart(BUCN value, String counterpartType) {
    super(value);
    this.module = counterpartType;
  }

  private String module;
  private String moduleCaption;

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getModuleCaption() {
    return moduleCaption;
  }

  public void setModuleCaption(String moduleCaption) {
    this.moduleCaption = moduleCaption;
  }
}
