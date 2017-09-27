/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	BOption.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;

/**
 * @author zhuhairui
 * 
 */
public class BOption implements Serializable {

  private static final long serialVersionUID = 7744046493701975184L;
  /** 配置项 */
  private BConfigOption configOption;
  /** 默认值 */
  private BDefaultOption defaultOption;

  public BConfigOption getConfigOption() {
    return configOption;
  }

  public void setConfigOption(BConfigOption configOption) {
    this.configOption = configOption;
  }

  public BDefaultOption getDefaultOption() {
    return defaultOption;
  }

  public void setDefaultOption(BDefaultOption defaultOption) {
    this.defaultOption = defaultOption;
  }

}
