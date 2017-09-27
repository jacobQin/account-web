package com.hd123.m3.cre.controller.account.deposit.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.depositrepayment.DepositRepayments;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.cre.controller.account.AccountBasicConstants;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/** 预存款还款单查询构造器 */
@Component
public class DepositRepaymentQueryBuilder extends FlecsQueryDefinitionBuilder {
  private static final String FILTER_BIZSTATE = "bizState";
  /** 还款日期 */
  private static final String FILTER_REPAYMENTDATE = "repaymentDate";
  /** 记账日期 */
  private static final String FILTER_ACCOUNTDATE = "accountDate";
  /** 科目 */
  private static final String FILTER_SUBJECT = "subject";
  /** 付款方式 */
  private static final String FILTER_PAYMENTTYPE = "paymentType";
  /** 经办人 */
  private static final String FILTER_DEALER = "viewDealer";

  /** 排序 */
  /** 还款日期 */
  private static final String SORT_REPAYMENTDATE = "repaymentDate";
  /** 记账日期 */
  public static final String SORT_ACCOUNTDATE = "accountDate";
  /** 对方联系人 */
  public static final String SORT_COUNTERCONTACT = "counterContact";
  /** 合同 */
  public static final String SORT_CONTRACT = "contract";
  /** 付款方式 */
  public static final String SORT_PAYMENTTYPE = "paymentType";
  /** 经办人 */
  public static final String SORT_DEALER = "viewDealer";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null) {
      return;
    }

    if (DepositRepayments.CONDITION_DIRECTION_EQUALS.equals(fieldName)) {
      queryDef.addCondition(DepositRepayments.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);
    } else if (FILTER_BIZSTATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(DepositRepayments.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS,
              state);
        }
      } else if (value instanceof String) {
        queryDef
            .addFlecsCondition(DepositRepayments.FIELD_BIZSTATE, Basices.OPERATOR_EQUALS, value);
      }
    } else if (AccountBasicConstants.FILTER_BILLNUMBER.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_BILLNUMBER,
          DepositRepayments.OPERATOR_START_WITH, value);
    } else if (AccountBasicConstants.FILTER_STORE_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_ACCOUNTUNIT,
          DepositRepayments.OPERATOR_EQUALS, value);
    } else if (AccountBasicConstants.FILTER_CONTRACT_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_CONTRACTNUMBER,
          DepositRepayments.OPERATOR_EQUALS, value);
    } else if (AccountBasicConstants.FILTER_COUNTERPART_UUID.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_COUNTERPART,
          DepositRepayments.OPERATOR_EQUALS, value);
    } else if (FILTER_REPAYMENTDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_REPAYMENTDATE,
          DepositRepayments.OPERATOR_EQUALS, StringUtil.toDate(value.toString()));
    } else if (FILTER_ACCOUNTDATE.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_ACCOUNTDATE,
          DepositRepayments.OPERATOR_EQUALS, StringUtil.toDate(value.toString()));
    } else if (AccountBasicConstants.FILTER_SETTLENO.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_SETTLENO,
          DepositRepayments.OPERATOR_EQUALS, value);
    } else if (FILTER_SUBJECT.equals(fieldName)) {
      queryDef.addCondition(DepositRepayments.CONDITION_SUBJECT_EQUALS, value);
    } else if (FILTER_PAYMENTTYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_PAYMENTTYPE,
          DepositRepayments.OPERATOR_EQUALS, value);
    } else if (FILTER_DEALER.equals(fieldName)) {
      queryDef.addFlecsCondition(DepositRepayments.FIELD_DEALER, DepositRepayments.OPERATOR_EQUALS,
          value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;

    if (AccountBasicConstants.SORT_BILLNUMBER.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_BILLNUMBER, dir);
    } else if (AccountBasicConstants.SORT_STORE.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_ACCOUNTCENTER, dir);
    } else if (AccountBasicConstants.SORT_BIZSTATE.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_BIZSTATE, dir);
    } else if (AccountBasicConstants.SORT_COUNTERPART.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_COUNTERPART, dir);
    } else if (SORT_REPAYMENTDATE.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_REPAYMENTDATE, dir);
    } else if (SORT_ACCOUNTDATE.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_ACCOUNTDATE, dir);
    } else if (SORT_COUNTERCONTACT.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_COUNTERCONTACT, dir);
    } else if (AccountBasicConstants.SORT_SETTLENO.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_SETTLENO, dir);
    } else if (SORT_CONTRACT.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_CONTRACTNUMBER, dir);
    } else if (SORT_PAYMENTTYPE.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_PAYMENTTYPE, dir);
    } else if (SORT_DEALER.equals(sort.getProperty())) {
      queryDef.addOrder(DepositRepayments.ORDER_BY_DEALER, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
