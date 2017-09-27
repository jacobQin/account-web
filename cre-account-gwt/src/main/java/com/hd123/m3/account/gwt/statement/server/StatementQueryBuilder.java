/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-1 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.server;

import java.util.List;

import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.SPosition;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.query.FlecsQueryCondition;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;

/**
 * 账单flecs查询构造器
 * 
 * @author huangjunxian
 * 
 */
public class StatementQueryBuilder extends FlecsConditionDecoderImpl {
  private static StatementQueryBuilder instance = null;

  public static StatementQueryBuilder getInstance() {
    if (instance == null)
      instance = new StatementQueryBuilder();
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return Statements.FIELD_BILLNUMBER;
  }

  @Override
  protected void buildFlecsQueryCondition(List<FlecsQueryCondition> flecsConditions,
      Condition condition) {
    if (StatementUrlParams.Flecs.FIELD_POSITION.equals(condition.getFieldName())
        && condition.getOperand() instanceof SPosition) {
      SPosition operand = (SPosition) condition.getOperand();
      if (StringUtil.isNullOrBlank(operand.getUuid()) == false) {
        FlecsQueryCondition flecsCondition = new FlecsQueryCondition();
        flecsCondition.setFieldName(Statements.FIELD_POSITION);
        flecsCondition.setOperation(Statements.OPERATOR_EQUALS);
        flecsCondition.getParameters().add(operand.getUuid());
        flecsConditions.add(flecsCondition);
      } else if (operand.getSearchType() != null) {
        if (StringUtil.isNullOrBlank(operand.getSearchType().getPositionType()) == false) {
          FlecsQueryCondition flecsCondition = new FlecsQueryCondition();
          flecsCondition.setFieldName(Statements.FIELD_POSITION_TYPE);
          flecsCondition.setOperation(Statements.OPERATOR_EQUALS);
          flecsCondition.getParameters().add(operand.getSearchType().getPositionType());
          flecsConditions.add(flecsCondition);
        }
        if (StringUtil.isNullOrBlank(operand.getSearchType().getPositionSubType()) == false) {
          FlecsQueryCondition flecsCondition = new FlecsQueryCondition();
          flecsCondition.setFieldName(Statements.FIELD_POSITION_SUBTYPE);
          flecsCondition.setOperation(Statements.OPERATOR_EQUALS);
          flecsCondition.getParameters().add(operand.getSearchType().getPositionSubType());
          flecsConditions.add(flecsCondition);
        }
      }
    } else if (StatementUrlParams.Flecs.FIELD_BUILDING.equals(condition.getFieldName())
        && condition.getOperand() instanceof TypeBUCN) {
      TypeBUCN operand = (TypeBUCN) condition.getOperand();
      if (StringUtil.isNullOrBlank(operand.getUuid()) == false) {
        FlecsQueryCondition flecsCondition = new FlecsQueryCondition();
        flecsCondition.setFieldName(Statements.FIELD_BUILDING);
        flecsCondition.setOperation(Statements.OPERATOR_EQUALS);
        flecsCondition.getParameters().add(operand.getUuid());
        flecsConditions.add(flecsCondition);
      } else if (StringUtil.isNullOrBlank(operand.getType()) == false) {
        FlecsQueryCondition flecsCondition = new FlecsQueryCondition();
        flecsCondition.setFieldName(Statements.FIELD_BUILDING_TYPE);
        flecsCondition.setOperation(Statements.OPERATOR_EQUALS);
        flecsCondition.getParameters().add(operand.getType());
        flecsConditions.add(flecsCondition);
      }
    } else {
      super.buildFlecsQueryCondition(flecsConditions, condition);
    }
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (StatementUrlParams.Flecs.FIELD_BILLNUMBER.equals(fieldName))
      return Statements.FIELD_BILLNUMBER;
    else if (StatementUrlParams.Flecs.FIELD_PERMGROUP.equals(fieldName))
      return Statements.FIELD_PERMGROUP;
    else if (StatementUrlParams.Flecs.FIELD_ACCOUNTUNIT.equals(fieldName))
      return Statements.FIELD_ACCOUNTUNIT;
    else if (StatementUrlParams.Flecs.FIELD_COUNTERPART.equals(fieldName))
      return Statements.FIELD_COUNTERPART;
    else if (StatementUrlParams.Flecs.FIELD_COUNTERPARTTYPE.equals(fieldName))
      return Statements.FIELD_COUNTERPART_TYPE;
    else if (StatementUrlParams.Flecs.FIELD_CONTRACTNUMBER.equals(fieldName))
      return Statements.FIELD_CONTRACTNUMBER;
    else if (StatementUrlParams.Flecs.FIELD_CONTRACTNAME.equals(fieldName))
      return Statements.FIELD_CONTRACTNAME;
    else if (StatementUrlParams.Flecs.FIELD_SETTLENO.equals(fieldName))
      return Statements.FIELD_SETTLENO;
    else if (StatementUrlParams.Flecs.FIELD_BIZSTATE.equals(fieldName))
      return Statements.FIELD_BIZSTATE;
    else if (StatementUrlParams.Flecs.FIELD_SETTLESTATE.equals(fieldName))
      return Statements.FIELD_SETTLESTATE;
    else if (StatementUrlParams.Flecs.FIELD_ACCOUNTTIME.equals(fieldName))
      return Statements.FIELD_ACCOUNTTIME;
    else if (StatementUrlParams.Flecs.FIELD_LASTRECEIPTDATE.equals(fieldName))
      return Statements.FIELD_LASTRECEIPTDATE;
    else if (StatementUrlParams.Flecs.FIELD_PLANPAYDATE.equals(fieldName))
      return Statements.FIELD_PLANPAYDATE;
    else if (StatementUrlParams.Flecs.FIELD_ACCOUNTRANGE.equals(fieldName))
      return Statements.FIELD_ACCOUNTRANGE;
    else if (StatementUrlParams.Flecs.FIELD_SUBJECT.equals(fieldName))
      return Statements.FIELD_SUBJECT;
    else if (StatementUrlParams.Flecs.FIELD_SOURCEBILLTYPE.equals(fieldName))
      return Statements.FIELD_SOURCEBILLTYPE;
    else if (StatementUrlParams.Flecs.FIELD_SOURCENUMBER.equals(fieldName))
      return Statements.FIELD_SOURCEBILLNUMBER;
    else if (StatementUrlParams.Flecs.FIELD_STATEMENTSETTLE.equals(fieldName))
      return Statements.FIELD_CLEANED;
    else if (StatementUrlParams.Flecs.FIELD_INVOICEREG.equals(fieldName))
      return Statements.FIELD_INVOICED;
    else if (fieldName.trim().equals(StatementUrlParams.Search.FIELD_COOPMODE))
      return Statements.FIELD_COOPMODE;
    else if (fieldName.trim().equals(StatementUrlParams.Search.FIELD_CONTRACT_CATEGORY))
      return Statements.FIELD_CONTRACT_CATEGORY;
    else if (StatementUrlParams.Flecs.FIELD_CREATE_INFO_OPER_ID.equals(fieldName))
      return Statements.FIELD_CREATOR_CODE;
    else if (StatementUrlParams.Flecs.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName))
      return Statements.FIELD_CREATOR_NAME;
    else if (StatementUrlParams.Flecs.FIELD_CREATE_INFO_TIME.equals(fieldName))
      return Statements.FIELD_CREATE_TIME;
    else if (StatementUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName))
      return Statements.FIELD_LAST_MODIFIER_CODE;
    else if (StatementUrlParams.Flecs.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName))
      return Statements.FIELD_LAST_MODIFIER_NAME;
    else if (StatementUrlParams.Flecs.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName))
      return Statements.FIELD_LAST_MODIFY_TIME;
    else if (StatementUrlParams.Flecs.FIELD_TYPE.equals(fieldName))
      return Statements.FIELD_TYPE;
    else if (StatementUrlParams.Flecs.FIELD_POSITION.equals(fieldName)) {
      return Statements.FIELD_POSITION;
    } else if (StatementUrlParams.Flecs.FIELD_BUILDING.equals(fieldName)) {
      return Statements.FIELD_BUILDING;
    } else if (StatementUrlParams.Flecs.FIELD_PAYTOTAL_TOTAL.equals(fieldName)) {
      return Statements.FIELD_PAYTOTAL;
    } else if (StatementUrlParams.Flecs.FIELD_RECEIPTTOTOAL_TOTAL.equals(fieldName)) {
      return Statements.FIELD_RECEIPTTOTAL;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;
    if (StatementUrlParams.Flecs.FIELD_BILLNUMBER.equals(fieldName))
      return Statements.ORDER_BY_BILL_NUMBER;
    else if (StatementUrlParams.Flecs.FIELD_BIZSTATE.equals(fieldName))
      return Statements.ORDER_BY_BIZSTATE;
    else if (StatementUrlParams.Flecs.FIELD_CONTRACTNUMBER.equals(fieldName))
      return Statements.ORDER_BY_CONTRACTNUMBER;
    else if (StatementUrlParams.Flecs.FIELD_CONTRACTNAME.equals(fieldName))
      return Statements.ORDER_BY_CONTRACTNAME;
    else if (StatementUrlParams.Flecs.FIELD_COUNTERPART.equals(fieldName))
      return Statements.ORDER_BY_COUNTERPART_CODE;
    else if (StatementUrlParams.Flecs.FIELD_SETTLENO.equals(fieldName))
      return Statements.ORDER_BY_SETTLE;
    else if (StatementUrlParams.Flecs.FIELD_ACCOUNTTIME.equals(fieldName))
      return Statements.ORDER_BY_ACCOUNTTIME;
    else if (StatementUrlParams.Flecs.FIELD_PAYTOTAL.equals(fieldName))
      return Statements.ORDER_BY_PAYTOTAL;
    else if (StatementUrlParams.Flecs.FIELD_PAYED.equals(fieldName))
      return Statements.ORDER_BY_PAYED;
    else if (StatementUrlParams.Flecs.FIELD_RECEIPTTOTOAL.equals(fieldName))
      return Statements.ORDER_BY_RECEIPTTOTAL;
    else if (StatementUrlParams.Flecs.FIELD_RECEIPTED.equals(fieldName))
      return Statements.ORDER_BY_RECEIPTED;
    else if (StatementUrlParams.Flecs.FIELD_PLANPAYDATE.equals(fieldName))
      return Statements.ORDER_BY_PLANPAYDATE;
    else if (StatementUrlParams.Flecs.FIELD_LASTRECEIPTDATE.equals(fieldName))
      return Statements.ORDER_BY_LASTRECEIPTDATE;
    else if (StatementUrlParams.Flecs.FIELD_SETTLESTATE.equals(fieldName))
      return Statements.ORDER_BY_SETTLESTATE;
    else if (StatementUrlParams.Flecs.FIELD_TYPE.equals(fieldName))
      return Statements.ORDER_BY_TYPE;
    return null;
  }

}
