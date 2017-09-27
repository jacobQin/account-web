/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 * 
 * 项目名：	h5-web
 * 文件名：	HasFocusables.java
 * 模块说明：	
 * 修改历史：
 * 2011-5-9 - zhangyanbo - 创建。
 */
package com.hd123.m3.account.gwt.base.client.validation;

import com.google.gwt.user.client.ui.Focusable;

/**
 * 标识包含一组可定位焦点的控件 | 接口
 * 
 * @author zhangyanbo
 * 
 */
public interface HasFocusables {

  /**
   * 取得指定字段名对应的可定位焦点控件。
   * 
   * @param field
   * @return
   */
  Focusable getFocusable(String field);
}
