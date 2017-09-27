/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccObjectSortUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月9日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.hd123.m3.cre.controller.account.accobject.model.BAccObjectDetail;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.util.client.OrderDir;

/**
 * 核算主体明细排序工具类
 * 
 * @author LiBin
 *
 */
public class AccObjectSortUtil {

  public static final String ORDER_BY_FIELD_CONTRACT = "contract";
  public static final String ORDER_BY_FIELD_ACCOBJECT = "accObject";

  /** 账单内存排序 */
  public static List<BAccObjectDetail> sortAccObjectDetails(Collection<BAccObjectDetail> values,
      Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    List<BAccObjectDetail> result = new ArrayList<BAccObjectDetail>(values);
    if (sortField == null) {
      return result;
    }

    Collections.sort(result, new Comparator<BAccObjectDetail>() {
      public int compare(BAccObjectDetail o1, BAccObjectDetail o2) {
        if (sortField.equals(ORDER_BY_FIELD_CONTRACT)) {
          if (o1.getContract() == null) {
            return 0;
          } else if (o2.getContract() == null) {
            return 1;
          } else {
            return AccObjectSortUtil.compare(o1.getContract().getCode(),
                o2.getContract().getCode(), sortDir);
          }
        } else if (sortField.equals(ORDER_BY_FIELD_ACCOBJECT)) {
          return AccObjectSortUtil.compare(o1.getAccObject().getCode(),
              o2.getAccObject().getCode(), sortDir);
        } else {
          return 0;
        }
      }
    });
    return result;
  }

  private static int compare(Object c1, Object c2, OrderDir sortDir) {
    if (c1 == null && c2 == null)
      return 0;
    else if (c1 == null)
      return sortDir == OrderDir.desc ? 1 : -1;
    else if (c2 == null)
      return sortDir == OrderDir.desc ? -1 : 1;

    int compare = 0;
    if (c1 instanceof String && c2 instanceof String) {
      compare = ((String) c1).compareTo(((String) c2));
    } else if (c1 instanceof Date && c2 instanceof Date) {
      compare = ((Date) c1).compareTo(((Date) c2));
    } else if (c1 instanceof BigDecimal && c2 instanceof BigDecimal) {
      compare = ((BigDecimal) c1).compareTo(((BigDecimal) c2));
    }
    if (sortDir == OrderDir.desc)
      compare *= -1;

    return compare;
  }
}
