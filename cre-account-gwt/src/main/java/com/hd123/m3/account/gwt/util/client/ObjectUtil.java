/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2007，所有权利保留。
 * 
 * 项目名：	rumba
 * 文件名：	ObjectUtil.java
 * 模块说明：	
 * 修改历史：
 * Nov 28, 2007 - lxm - 创建。
 */
package com.hd123.m3.account.gwt.util.client;

import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * @author lxm
 * 
 */
public class ObjectUtil {

  public static boolean isEquals(Object o1, Object o2) {
    if (o1 == null && o2 == null)
      return true;
    else if (o1 == null && o2 != null)
      return false;
    else if (o1 != null && o2 == null)
      return false;
    else
      return o1.equals(o2);
  }

  /**
   * 检查参数是否为null。
   * 
   * @param param
   *          指定参数值。
   * @param paramName
   *          指定参数名。
   * @throws ClientBizException
   */
  public static void checkParameterNotNull(Object param, String paramName)
      throws ClientBizException {
    if (param == null)
      throw new ClientBizException("必须的参数" + paramName + "不能为null");
  }

  /**
   * 提供业务级别的断言功能。
   * <p>
   * 不同于Java环境所提供的assert，如果断言不成立，则将以异常的方式抛出错误，且不会受到javac编译参数“-ea”的影响。
   * 
   * @param judge
   * @param echo
   * @throws ClientBizException
   */
  public static void assertion(boolean judge, String echo) throws ClientBizException {
    if (!judge)
      throw new ClientBizException(echo);
  }

  /**
   * 如果o1是空, 返回o2; 否则返回o1
   * 
   * @param o1
   * @param o2
   */
  public static Object nvl(Object o1, Object o2) {
    return o1 == null ? o2 : o1;
  }
}
