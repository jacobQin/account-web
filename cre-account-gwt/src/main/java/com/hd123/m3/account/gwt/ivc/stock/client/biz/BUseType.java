/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BUseType.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.client.biz;

/**
 * 发票使用方式常量定义.0-正常，1-被红冲，2-红冲，3-被交换，4-被交换和红冲，5-交换
 * 
 * @author lixiaohong
 *
 */
public class BUseType {
  public static int NORMAL = 0;
  public static int BALANCED = 1;
  public static int BALANCING = 2;
  public static int EXCHANGED = 3;
  public static int BALANCED_EXCHANGED = 4;
  public static int EXCHANGING = 5;
}
