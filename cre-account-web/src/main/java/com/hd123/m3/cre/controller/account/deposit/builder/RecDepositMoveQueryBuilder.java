/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名： cre-account
 * 文件名： RecDepositMoveQueryBuilder.java
 * 模块说明：    
 * 修改历史：
 * 2017年3月30日 - wangyong - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.depositmove.DepositMoves;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 
 * @author WangYong
 *
 */

@Component
public class RecDepositMoveQueryBuilder extends FlecsQueryDefinitionBuilder {
  private static final String FILTER_BILLNUMBER = "billNumber";
  private static final String FILTER_BIZSTATE = "bizState";
  private static final String FILTER_ACCOUNTUUIT = "accountUnit";

  // 转出方信息
  public static final String FILTER_OUTCOUNTERPART = "outCounterpart";
  public static final String FILTER_OUTCOUNTERPART_TYPE = "outCounterpartType";
  public static final String FILTER_OUTCONTRACT = "outContract";
  public static final String FILTER_OUTSUBJECT = "outSubject";
  // 转入方信息
  public static final String FILTER_INCOUNTERPART = "inCounterpart";
  public static final String FILTER_INCOUNTERPART_TYPE = "inCounterpartType";
  public static final String FILTER_INCONTRACT = "inContract";
  public static final String FILTER_INSUBJECT = "inSubject";
  //
  public static final String FILTER_BUSINESSUNIT = "businessUnit";// 项目
  public static final String FILTER_AMOUNT = "amount";
  public static final String FILTER_ACCOUNTDATE = "accountDate";
  public static final String FILTER_SETTLENO = "settleNo";

  /** =========================排序字段============================ */
  private static final String SORT_BILLNUMBER = "billNumber";
  private static final String SORT_BIZSTATE = "bizState";
  private static final String SORT_ACCOUNTUNIT = "accountUnit";
  private static final String SORT_OUTCOUNTERPART = "outCounterpart";
  private static final String SORT_INCOUNTERPART = "inCounterpart";
  private static final String SORT_OUTCONTRACT = "outContract";
  private static final String SORT_OUTSUBJECT = "outSubject";
  private static final String SORT_AMOUNT = "amount";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_BILLNUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_BILLNUMBER, DepositMoves.OPERATOR_START_WITH,
          value);
    } else if (FILTER_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(DepositMoves.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(DepositMoves.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if (FILTER_ACCOUNTUUIT.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_ACCOUNTCENTER, DepositMoves.OPERATOR_EQUALS,
          value);
    } else if (FILTER_OUTCOUNTERPART_TYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_OUTCOUNTERPART_TYPE,
          DepositMoves.OPERATOR_EQUALS, value);
    } else if (FILTER_INCOUNTERPART_TYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_INCOUNTERPART_TYPE,
          DepositMoves.OPERATOR_EQUALS, value);
    } else if (FILTER_OUTCOUNTERPART.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_OUTCOUNTERPART, DepositMoves.OPERATOR_EQUALS,
          value);
    } else if (FILTER_INCOUNTERPART.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_INCOUNTERPART, DepositMoves.OPERATOR_EQUALS,
          value);
    }

    else if (FILTER_OUTCONTRACT.equals(fieldName)) {
      queryDef.addCondition(DepositMoves.CONDITION_OUTCONTRACT_EQUALS, value);
    } else if (FILTER_INCONTRACT.equals(fieldName)) {
      queryDef.addCondition(DepositMoves.CONDITION_INCONTRACT_EQUALS, value);
    } else if (FILTER_OUTSUBJECT.equals(fieldName)) {
      queryDef
          .addFlecsCondition(DepositMoves.FIELD_OUTSUBJECT, DepositMoves.OPERATOR_EQUALS, value);
    } else if (FILTER_INSUBJECT.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_INSUBJECT, DepositMoves.OPERATOR_EQUALS,
          value);
    } else if (FILTER_BUSINESSUNIT.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_BUSINESS, DepositMoves.OPERATOR_EQUALS, value);
    } else if (FILTER_SETTLENO.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_SETTLENO, DepositMoves.OPERATOR_EQUALS, value);
    } else if (FILTER_ACCOUNTDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositMoves.FIELD_ACCOUNTDATE, DepositMoves.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (SORT_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_BIZSTATE, dir);
    } else if (SORT_BILLNUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_BILLNUMBER, dir);
    } else if (SORT_ACCOUNTUNIT.equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_ACCOUNTCENTER, dir);
    } else if (SORT_OUTCOUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_OUTCOUNTERPART, dir);
    } else if (SORT_INCOUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_INCOUNTERPART, dir);
    } else if (SORT_OUTCONTRACT.equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_OUTCONTRACTNUMBER, dir);
    } else if (SORT_OUTSUBJECT.equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_OUTSUBJECT, dir);
    } else if (SORT_AMOUNT.equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_MOVETOTAL, dir);
    } else if ("inContract".equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_INCONTRACTNUMBER, dir);
    } else if ("inCounterpart".equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_INCONTRACTNUMBER, dir);
    } else if ("inSubject".equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_INSUBJECT, dir);
    } else if ("settleNo".equals(sort.getProperty())) {
      queryDef.addOrder(DepositMoves.ORDER_BY_SETTLENO, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
