/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	Date2StrUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月9日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal.convert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenganbang
 *
 */
public class Date2StrUtil {
  private static Date2StrUtil instance = null;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  private Date2StrUtil() {
  }

  public static Date2StrUtil getInstance() {
    if (instance == null) {
      instance = new Date2StrUtil();
    }
    return instance;
  }

  public String convert(Date date) {
    String str = "";
    if (date != null) {
      str = sdf.format(date);
    }
    return str;
  }
}
