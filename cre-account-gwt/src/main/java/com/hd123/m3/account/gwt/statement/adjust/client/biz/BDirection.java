/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BDirection.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-21 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.biz;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuhairui
 * 
 */
public class BDirection {
  public static final Integer PAY = new Integer(-1);
  public static final String CAPTION_PAY = "付";

  public static final Integer RECEIVE = new Integer(1);
  public static final String CAPTION_RECEIVE = "收";

  public static final Map<Integer, String> MAP = new HashMap();

  static {
    MAP.put(PAY, CAPTION_PAY);
    MAP.put(RECEIVE, CAPTION_RECEIVE);
  }
}
