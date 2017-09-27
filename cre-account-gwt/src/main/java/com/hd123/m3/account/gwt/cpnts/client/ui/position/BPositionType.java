/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-client
 * 文件名：	PositionType.java
 * 模块说明：	
 * 修改历史：
 * 2013-6-6 - viva - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.position;

/**
 * @author viva
 * 
 */
public enum BPositionType {

  shoppe("铺位"), booth("场地"), adPlace("广告位");

  private String caption;

  private BPositionType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
