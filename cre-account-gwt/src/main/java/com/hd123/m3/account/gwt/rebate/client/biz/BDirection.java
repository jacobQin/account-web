/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BDirection.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月26日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.biz;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenganbang
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
