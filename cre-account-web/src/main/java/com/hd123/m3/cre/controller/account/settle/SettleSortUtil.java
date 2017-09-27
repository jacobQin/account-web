/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	SettleSortUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月19日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.settle;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author mengyinkun
 * 
 */
public class SettleSortUtil {

  public static final String FIELD_COUNTERPART = "store";
  public static final String FIELD_SETTLENAME = "settlement";
  public static final String FIELD_BEGINDATE = "beginDate";
  public static final String FIELD_CONTRACT_NUMBER = "contract";
  public static final String FIELD_ACCOUNTTIME = "planDate";
  public static final String FIELD_FLOOR = "floor";
  public static final String FIELD_COOPMODE = "coopMode";

  public static void sortRecords(List<AccountSettle> records, final String sortField, OrderSort sort) {
    if (records.isEmpty()) {
      return;
    }
    if (StringUtil.isNullOrBlank(sortField)) {
      // 按照商户+合同+出账日期+费用类型升序排序
      Collections.sort(records, new Comparator<AccountSettle>() {

        @Override
        public int compare(AccountSettle o1, AccountSettle o2) {
          if (compareField(o1.getContract().getCounterpart().getCode(), o2.getContract()
              .getCounterpart().getCode(), 1) == 0) {
            if (compareField(o1.getContract().getBillNumber(), o2.getContract().getBillNumber(), 1) == 0) {
              if (compareField(o1.getPlanDate(), o2.getPlanDate(), 1) == 0) {
                if (compareField(o1.getSettlement().getCaption(), o2.getSettlement().getCaption(),
                    1) == 0) {
                  return compareField(o1.getBeginDate(), o2.getBeginDate(), 1);
                } else {
                  return compareField(o1.getSettlement().getCaption(), o2.getSettlement()
                      .getCaption(), 1);
                }
              } else {
                return compareField(o1.getPlanDate(), o2.getPlanDate(), 1);
              }
            } else {
              return compareField(o1.getContract().getCounterpart().getCode(), o2.getContract()
                  .getCounterpart().getCode(), 1);
            }
          } else {
            return compareField(o1.getContract().getCounterpart().getCode(), o2.getContract()
                .getCounterpart().getCode(), 1);
          }
        }

      });
    } else {
      final int direction = OrderSort.ASC.equals(sort.getDirection()) ? 1 : -1;
      Collections.sort(records, new Comparator<AccountSettle>() {

        public int compare(AccountSettle o1, AccountSettle o2) {
          if (FIELD_COUNTERPART.equals(sortField)) {
            return compareField(o1.getContract().getCounterpart().getCode(), o2.getContract()
                .getCounterpart().getCode(), direction);
          } else if (FIELD_SETTLENAME.equals(sortField)) {
            return compareField(o1.getSettlement().getCaption(), o2.getSettlement().getCaption(),
                direction);
          } else if (FIELD_BEGINDATE.equals(sortField)) {
            int result = compareField(o1.getBeginDate(), o2.getBeginDate(), direction);
            if (result == 0) {
              return compareField(o1.getEndDate(), o2.getEndDate(), direction);
            }
            return result;
          } else if (FIELD_CONTRACT_NUMBER.equals(sortField)) {
            return compareField(o1.getContract().getBillNumber(), o2.getContract().getBillNumber(),
                direction);
          } else if (FIELD_FLOOR.equals(sortField)) {
            return compareField(o1.getContract().getFloor() == null ? null : o1.getContract()
                .getFloor().getCode(), o2.getContract().getFloor() == null ? null : o2
                .getContract().getFloor().getCode(), direction);
          } else if (FIELD_COOPMODE.equals(sortField)) {
            return compareField(o1.getContract().getCoopMode(), o2.getContract().getCoopMode(),
                direction);
          } else if (FIELD_ACCOUNTTIME.equals(sortField)) {
            return compareField(o1.getPlanDate(), o2.getPlanDate(), direction);
          }
          return 0;
        }
      });
    }
  }

  private static int compareField(Object field1, Object field2, int direction) {
    if (field1 == null && field2 == null) {
      return 0;
    } else if (field1 == null && field2 != null) {
      return 1 * direction;
    } else if (field1 != null && field2 == null) {
      return -1 * direction;
    } else {
      if (field1 instanceof String && field2 instanceof String) {
        return ((String) field1).compareTo((String) field2) * direction;
      } else if (field1 instanceof Date && field2 instanceof Date) {
        return ((Date) field1).compareTo((Date) field2) * direction;
      } else {
        return field1.toString().compareTo(field2.toString()) * direction;
      }
    }
  }
}