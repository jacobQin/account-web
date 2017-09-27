/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BMoreBtnPerm.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月19日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

/**
 * 账单查看页"更多操作"下拉菜单按钮权限
 * 
 * @author chenganbang
 *
 */
public class BMoreBtnPerm {
  private String type;
  private boolean visible = false;
  private boolean clickable = true;
  private String errMsg;

  /**
   * 类型
   * 
   * @return
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * 是否可见
   * 
   * @return
   */
  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  /**
   * 是否可点击
   * 
   * @return
   */
  public boolean isClickable() {
    return clickable;
  }

  public void setClickable(boolean clickable) {
    this.clickable = clickable;
  }

  /**
   * 可见但不可点击的报错信息
   * 
   * @return
   */
  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

}
