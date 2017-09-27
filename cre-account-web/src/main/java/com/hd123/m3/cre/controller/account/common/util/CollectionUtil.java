/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	CollectionUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月25日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.mdata.service.typetag.TypeTags;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 集合工具类
 * 
 * @author LiBin
 *
 */
public class CollectionUtil extends com.hd123.rumba.commons.collection.CollectionUtil {
  /**
   * 对集合中元素进行分组
   * 
   * @param elements
   *          集合
   * @param c
   *          比较器。
   * @return 分组集合
   */
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

  /**
   * 按关键字过滤列表数据(仅处理含有getName,getCode方法的对象)：代码起始于...||名称包含...
   * 
   * @throws M3ServiceException
   *           没有getName()或者没有getCode()、及其他调用异常时抛出。
   **/
  public static List filterByKeyword(List objects, String keyword) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(keyword)) {
      return objects;
    }
    try {
      Iterator<TypeTags> iterator = objects.iterator();
      while (iterator.hasNext()) {
        Object object = iterator.next();
        Class cls = object.getClass();
        Method getCodeMethod = cls.getDeclaredMethod("getCode");
        if (getCodeMethod != null) {
          String code = (String) getCodeMethod.invoke(object);
          if (StringUtil.isNullOrBlank(code)) {
            continue;
          }
          if (code.toUpperCase().startsWith(keyword.toUpperCase())) {
            continue;
          } else {
            Method getNameMethod = cls.getDeclaredMethod("getName");
            if (getCodeMethod != null) {
              String name = (String) getNameMethod.invoke(object);
              if (StringUtil.isNullOrBlank(name)) {
                continue;
              }
              if (name.contains(keyword) == false) {
                iterator.remove();
              }
            }
          }
        }
      }

      return objects;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

}
