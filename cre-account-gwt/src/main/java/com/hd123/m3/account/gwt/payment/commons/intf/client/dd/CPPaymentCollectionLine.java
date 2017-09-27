/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CPPaymentCollectionLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月14日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.intf.client.dd;

import com.google.gwt.i18n.client.Constants;

/**
 * 代收明细数据字典常量
 * 
 * @author LiBin
 *
 */
public interface CPPaymentCollectionLine extends Constants {
  
  public String tableCaption();
  
  public String lineNumber();
  
  public String subject();
  
  public String contract();
  
  public String contractCode();
  
  public String contractName();
  
  public String beginTime();
  
  public String endTime();
  
  public String receivableTotal();
  
  public String realTotal();
  
  public String unreceivableTotal();
  
  public String remark();
  
}
