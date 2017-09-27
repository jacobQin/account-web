/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	AccountSettleQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月9日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.m3.account.service.contract.AccountSettles;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.cre.controller.account.statement.StatementConstants;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author chenganbang
 *
 */
public class AccountSettleQueryBuilder {
  private static AccountSettleQueryBuilder instance = null;

  private AccountSettleQueryBuilder() {
  }

  public static AccountSettleQueryBuilder getInstance() {
    if (instance == null) {
      instance = new AccountSettleQueryBuilder();
    }
    return instance;
  }

  public List<QueryOrder> buildQueryOrders(List<OrderSort> orders) {
    List<QueryOrder> orderList = new ArrayList<QueryOrder>();

    for (OrderSort sort : orders) {
      QueryOrder qo = new QueryOrder();
      String field = sort.getProperty();
      if (StatementConstants.ORDER_BY_ACCOUNTUUID.equals(field)) {// 项目
        qo.setField(AccountSettles.ORDER_BY_ACCOUNT);
      } else if (StatementConstants.ORDER_BY_COUNTERPART.equals(field)) {// 对方单位
        qo.setField(AccountSettles.ORDER_BY_COUNTERPART);
      } else if (StatementConstants.ORDER_BY_CONTRACT.equals(field)) {// 合同
        qo.setField(AccountSettles.ORDER_BY_CONTRACTNUMBER);
      } else if (StatementConstants.ORDER_BY_FLOOR.equals(field)) {// 楼层
        qo.setField(AccountSettles.ORDER_BY_FLOOR);
      } else if (StatementConstants.ORDER_BY_COOPMODE.equals(field)) {// 合作方式
        qo.setField(AccountSettles.ORDER_BY_COOPMODE);
      } else if (StatementConstants.ORDER_BY_CAPTION.equals(field)) {// 结算周期名称
        qo.setField(AccountSettles.ORDER_BY_SETTLE_CAPTION);
      } else if (StatementConstants.ORDER_BY_BEGINDATE.equals(field)) {
        qo.setField(AccountSettles.ORDER_BY_BEGINEDATE);
      } else if (StatementConstants.ORDER_BY_ACCOUNTTIME.equals(field)) {// 出账时间
        qo.setField(AccountSettles.ORDER_BY_ACCOUNTTIME);
      } else if (StatementConstants.ORDER_BY_PLAN_DATE.equals(field)) {// 计算出账日期
        qo.setField(AccountSettles.ORDER_BY_PLANDATE);
      } else if (StatementConstants.ORDER_BY_CALCULATE_TYPE.equals(field)) {// 出账方式
        qo.setField(AccountSettles.ORDER_BY_BILLCALCULATETYPE);
      } else if (StatementConstants.ORDER_BY_BILLNUMBER.equals(field)) {
        qo.setField(AccountSettles.ORDER_BY_STATEMENTNUM);
      }
      if (!StringUtil.isNullOrBlank(qo.getField())) {
        qo.setDirection(QueryOrderDirection.valueOf(sort.getDirection().toLowerCase()));
      }
      orderList.add(qo);
    }
    return orderList;
  }

  public FlecsQueryDefinition buildConditions(FlecsQueryDefinition condition,
      Map<String, Object> params) throws Exception {
    if (params != null) {
      Set<String> keys = params.keySet();
      for (String key : keys) {
        Object value = params.get(key);
        if (StatementConstants.FILTER_STORE_UUID.equals(key)) {
          condition.addFlecsCondition(AccountSettles.FIELD_ACCOUNTUNIT,
              AccountSettles.OPERATOR_EQUALS, value);
        } else if (StatementConstants.FILTER_COUNTERPART_UUID.equals(key)) {
          condition.addFlecsCondition(AccountSettles.FIELD_COUNTERPART,
              AccountSettles.OPERATOR_EQUALS, value);
        } else if (StatementConstants.FILTER_CONTRACT_UUID.equals(key)) {
          condition.addFlecsCondition(AccountSettles.FIELD_CONTRACTUUID,
              AccountSettles.OPERATOR_EQUALS, value);
        } else if (StatementConstants.FILTER_PLANDATE.equals(key)) {// 计划出账日期
          if (value != null) {
            LinkedHashMap<String, String> dateMap = (LinkedHashMap<String, String>) value;
            Date beginDate = StringUtil.toDate(dateMap.get("beginDate"), "yyyy-MM-dd");
            Date endDate = StringUtil.toDate(dateMap.get("endDate"), "yyyy-MM-dd");
            if (beginDate != null && endDate == null) {
              condition.addFlecsCondition(AccountSettles.FIELD_PLANDATE,
                  AccountSettles.OPERATOR_GREATER_EQUALS, beginDate);
            } else if (beginDate == null && endDate != null) {
              condition.addFlecsCondition(AccountSettles.FIELD_PLANDATE,
                  AccountSettles.OPERATOR_LESS_EQUALS, endDate);
            } else if (beginDate != null && endDate != null) {
              List<Date> dates = new ArrayList<Date>(2);
              if (beginDate.getTime() > endDate.getTime()) {
                dates.add(endDate);
                dates.add(beginDate);
              } else {
                dates.add(beginDate);
                dates.add(endDate);
              }
              condition.addCondition(Contracts.CONDITION_SETTLE_PLAN_DATE_BETWEEN, dates.toArray());
            }
          }
        } else if (StatementConstants.FILTER_FLOOR_UUID.equals(key)) {
          condition.addFlecsCondition(AccountSettles.FIELD_FLOOR_UUID,
              AccountSettles.OPERATOR_EQUALS, value);
        } else if (StatementConstants.FILTER_POSITION_UUID.equals(key)) {
          condition.addFlecsCondition(AccountSettles.FIELD_POSITION_UUID,
              AccountSettles.OPERATOR_EQUALS, value);
        } else if (StatementConstants.FILTER_COOPMODE.equals(key)) {
          condition.addFlecsCondition(AccountSettles.FIELD_COOPMODE,
              AccountSettles.OPERATOR_EQUALS, value);
        } else if (StatementConstants.FILTER_SETTLEMENT_CAPTION.equals(key)) {
          condition.addFlecsCondition(AccountSettles.FIELD_SETTLE_CAPTION,
              AccountSettles.OPERATOR_INCLUDE, value);
        } else if (StatementConstants.FILTER_ACCOUNTTIME.equals(key)) {
          if (value != null) {
            Date accountDate = StringUtil.toDate((String) value, "yyyy-MM-dd HH:mm:ss");
            condition.addFlecsCondition(AccountSettles.FIELD_ACCOUNTTIME,
                AccountSettles.OPERATOR_EQUALS, accountDate);
          }
        } else if (StatementConstants.FILTER_BILL_CALCULATE_TYPE.equals(key)) {
          if (!StringUtil.isNullOrBlank((String) value)) {
            condition.addFlecsCondition(AccountSettles.FIELD_BILLCALCULATETYPE,
                AccountSettles.OPERATOR_EQUALS, value);
          }
        } else if (StatementConstants.FILTER_ACCOUNTED.equals(key)) {
          String val = (String) value;
          if (!StringUtil.isNullOrBlank(val)) {
            condition.addFlecsCondition(AccountSettles.FIELD_ISACCOUNTSETTLE,
                AccountSettles.OPERATOR_EQUALS, StringUtil.toBoolean(val));
          }
        } else if (StatementConstants.FILTER_BILLNUMBER.equals(key)) {
          condition.addFlecsCondition(AccountSettles.FIELD_STATEMENTNUM,
              AccountSettles.OPERATOR_START_WITH, value);
        }
      }
    }
    return condition;
  }
}
