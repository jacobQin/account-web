/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	DepositQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月7日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.deposit.Deposits;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.cre.controller.account.AccountBasicConstants;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 预存（付）款单查询构造器
 * 
 * @author LiBin
 *
 */
@Component
public class DepositQueryBuilder extends FlecsQueryDefinitionBuilder {

  private static final String FILTER_BIZSTATE = "bizState";
  /** 预存日期(Date) */
  public static final String FILTER_DEPOSITDATE = "depositDate";
  /** 记账日期(Date) */
  public static final String FILTER_ACCOUNTDATE = "accountDate";
  /** 科目(String) */
  public static final String FILTER_SUBJECT = "subject";
  /** 付款方式(String) */
  public static final String FILTER_PAYMENTTYPE = "paymentType";
  /** 经办人(String) */
  public static final String FILTER_DEALER = "viewDealer";
  /**位置*/
  public static final String FILTER_POSITION = "position";
  /**未还款*/
  public static final String FILTER_UNPAYED = "unPayedLimit";

  /** 预存日期(Date) */
  public static final String SORT_DEPOSITDATE = "depositDate";
  /** 记账日期(Date) */
  public static final String SORT_ACCOUNTDATE = "accountDate";
  /** 对方联系人(String) */
  public static final String SORT_COUNTERCONTACT = "counterContact";
  /** 合同(String) */
  public static final String SORT_CONTRACT = "contract";
  /** 付款方式(String) */
  public static final String SORT_PAYMENTTYPE = "paymentType";
  /** 经办人(String) */
  public static final String SORT_DEALER = "viewDealer";
  /** 位置(String) */
  public static final String SORT_POSITION = "position";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null) {
      return;
    }

    if (Deposits.CONDITION_DIRECTION_EQUALS.equals(fieldName)) {
      queryDef.addCondition(Deposits.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);
    } else if (FILTER_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(Deposits.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, state);
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(Deposits.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if (AccountBasicConstants.FILTER_BILLNUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_BILLNUMBER, Deposits.OPERATOR_START_WITH, value);
    } else if (AccountBasicConstants.FILTER_STORE_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_ACCOUNTUNIT, Deposits.OPERATOR_EQUALS, value);
    } else if (AccountBasicConstants.FILTER_CONTRACT_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_CONTRACTUUID, Deposits.OPERATOR_EQUALS, value);
    } else if (AccountBasicConstants.FILTER_COUNTERPART_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_COUNTERPART, Deposits.OPERATOR_EQUALS, value);
    } else if (FILTER_DEPOSITDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_DEPOSITDATE, Deposits.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
    } else if (FILTER_ACCOUNTDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_ACCOUNTDATE, Deposits.OPERATOR_EQUALS,
          StringUtil.toDate(value.toString()));
    } else if (AccountBasicConstants.FILTER_SETTLENO.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_SETTLENO, Deposits.OPERATOR_EQUALS, value);
    } else if (FILTER_SUBJECT.equals(fieldName)) {
      queryDef.addCondition(Deposits.CONDITION_SUBJECT_EQUALS, value);
    } else if (FILTER_PAYMENTTYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_PAYMENTTYPE, Deposits.OPERATOR_EQUALS, value);
    } else if (FILTER_DEALER.equals(fieldName)) {
      queryDef.addFlecsCondition(Deposits.FIELD_DEALER, Deposits.OPERATOR_EQUALS, value);
    }else if (FILTER_POSITION.equals(fieldName)){
      queryDef.addFlecsCondition(Deposits.FIELD_POSITIONUUID, Deposits.OPERATOR_EQUALS, value);
    } else if (FILTER_UNPAYED.equals(fieldName)){
      queryDef.addCondition(Deposits.CONDITION_UNREPAYMENTED);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (AccountBasicConstants.SORT_STORE.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_ACCOUNTUNIT, dir);
    } else if (AccountBasicConstants.SORT_BILLNUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_BILLNUMBER, dir);
    } else if (AccountBasicConstants.SORT_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_BIZSTATE, dir);
    } else if (AccountBasicConstants.SORT_COUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_COUNTERPART, dir);
    } else if (SORT_DEPOSITDATE.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_DEPOSITDATE, dir);
    } else if (SORT_ACCOUNTDATE.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_ACCOUNTDATE, dir);
    } else if (SORT_COUNTERCONTACT.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_COUNTERCONTACT, dir);
    } else if (AccountBasicConstants.SORT_SETTLENO.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_SETTLENO, dir);
    } else if (SORT_CONTRACT.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_CONTRACTNUMBER, dir);
    } else if (SORT_PAYMENTTYPE.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_PAYMENTTYPE, dir);
    } else if (SORT_DEALER.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_DEALER, dir);
    }  else if (SORT_POSITION.equals(sort.getProperty())) {
      queryDef.addOrder(Deposits.ORDER_BY_POSITIONCODE, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
