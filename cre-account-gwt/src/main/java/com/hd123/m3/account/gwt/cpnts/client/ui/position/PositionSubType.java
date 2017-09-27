/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	PositionSubType.java
 * 模块说明：	
 * 修改历史：
 * 2016年5月26日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.position;

import java.io.Serializable;

import com.hd123.rumba.commons.gwt.mini.client.StringUtil;

/**
 * @author chenrizhang
 *
 */
public class PositionSubType implements Serializable {
  private static final long serialVersionUID = 5112970457285980038L;

  private String positionType;
  private String positionSubType;

  public PositionSubType() {
  }

  public PositionSubType(String positionType) {
    this.positionType = positionType;
  }

  public PositionSubType(String positionType, String positionSubType) {
    this.positionType = positionType;
    this.positionSubType = positionSubType;
  }

  public String getPositionType() {
    return positionType;
  }

  public void setPositionType(String positionType) {
    this.positionType = positionType;
  }

  public String getPositionSubType() {
    return positionSubType;
  }

  public void setPositionSubType(String positionSubType) {
    this.positionSubType = positionSubType;
  }

  public String toFriendlyStr() {
    String caption = positionType == null ? "位置" : BPositionType.valueOf(positionType).getCaption();
    if (StringUtil.isNullOrBlank(positionSubType) == false) {
      caption += "-" + positionSubType;
    }
    return caption;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((positionType == null) ? 0 : positionType.hashCode());
    result = prime * result + ((positionSubType == null) ? 0 : positionSubType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final PositionSubType other = (PositionSubType) obj;
    if (positionType == null) {
      if (other.positionType != null)
        return false;
    } else if (!positionType.equals(other.positionType))
      return false;
    if (positionSubType == null) {
      if (other.positionSubType != null)
        return false;
    } else if (!positionSubType.equals(other.positionSubType))
      return false;
    return true;
  }

}
