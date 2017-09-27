/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BBill.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;

/**
 * @author chenwenfeng
 * 
 */
public class BBill implements Serializable {
  private static final long serialVersionUID = 5119971402416840681L;

  public static final String NONE_ID = "-";

  private String billUuid;
  private String billNumber;

  public BBill() {
  }

  public BBill(String billUuid, String billNumber) {
    this.billUuid = billUuid;
    this.billNumber = billNumber;
  }

  public String getBillUuid() {
    return billUuid;
  }

  public void setBillUuid(String billUuid) {
    this.billUuid = billUuid;
  }

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((billNumber == null) ? 0 : billNumber.hashCode());
    result = prime * result + ((billUuid == null) ? 0 : billUuid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof BBill))
      return false;

    BBill other = (BBill) obj;

    if ((billUuid == null && other.billUuid != null)
        || (billUuid != null && other.billUuid == null))
      return false;
    if ((billNumber == null && other.billNumber != null)
        || (billNumber != null && other.billNumber == null))
      return false;
    if (billUuid == null && other.billUuid == null && billNumber == null
        && other.billNumber == null)
      return true;
    if (billUuid.equals(other.billUuid) && billNumber.equals(other.billNumber))
      return true;

    return false;
  }

  public String id() {
    return billUuid;
  }
}
