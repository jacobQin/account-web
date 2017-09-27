/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	BPositionState.java
 * 模块说明：	
 * 修改历史：
 * 2016年2月2日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.position;

/**
 * @author chenrizhang
 *
 */
public enum BPositionState {
  using("使用中"), deleted("已删除"), splitted("已拆分"), merged("已合并");

  private String caption;

  private BPositionState(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }

}
