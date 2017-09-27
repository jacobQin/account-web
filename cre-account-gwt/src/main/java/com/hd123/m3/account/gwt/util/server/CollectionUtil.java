/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	CollectionUtil.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-8 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.util.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenpeisi
 * 
 */
public class CollectionUtil {

  public static Collection group(Collection elements, Comparator c) {
    List<List> result = new ArrayList<List>();
    for (Object element : elements) {
      boolean found = false;
      for (List<Object> group : result) {
        if (c.compare(group.get(0), element) == 0) {
          group.add(element);
          found = true;
          break;
        }
      }
      if (!found) {
        List<Object> group = new ArrayList<Object>();
        group.add(element);
        result.add(group);
      }
    }
    return result;
  }
}
