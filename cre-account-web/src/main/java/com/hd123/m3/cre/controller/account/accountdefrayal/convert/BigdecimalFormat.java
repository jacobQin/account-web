/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BigdecimalFormat.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月26日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal.convert;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author chenganbang
 *
 */
public class BigdecimalFormat {
  private static BigdecimalFormat instance = null;
  private static DecimalFormat format = new DecimalFormat(",##0.00");

  private BigdecimalFormat() {
  }

  public static BigdecimalFormat getInstance() {
    if (instance == null) {
      instance = new BigdecimalFormat();
    }
    return instance;
  }

  public String format(BigDecimal source) {
    if (source == null) {
      return "";
    }
    return format.format(source);
  }

  public static void main(String[] args) {
    BigdecimalFormat.getInstance().format(new BigDecimal(1045.645));
  }
}
