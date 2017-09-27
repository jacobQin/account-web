/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	UCN2StrUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月26日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal.convert;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * @author chenganbang
 *
 */
public class UCN2StrUtil {
  private static UCN2StrUtil instance = null;

  private UCN2StrUtil() {
  }

  public static UCN2StrUtil getInstance() {
    if (instance == null) {
      instance = new UCN2StrUtil();
    }
    return instance;
  }

  public String toFriendString(UCN source) {
    if (source == null) {
      return "";
    }

    return source.getName() + "[" + source.getCode() + "]";
  }
}
