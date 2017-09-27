/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BankQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月14日 - gengpan - 创建。
 */
package com.hd123.m3.cre.controller.account.bank;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.bank.Banks;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author gengpan
 *
 */
@Component
public class AccBankQueryBuilder extends FlecsQueryDefinitionBuilder {
  // 查询条件
  // 项目
  private static final String FIELD_STOREUUID = "store";
  // 状态
  private static final String FILTER_STATE = "enabled";

  // 排序
  // 银行资料代码
  private static final String SORT_CODE = "accBank";
  // 银行资料名称
  private static final String SORT_NAME = "name";
  // 状态
  private static final String SORT_STATUS = "state";
  // 项目
  private static final String SORT_STORE = "store";
  // 银行名称
  private static final String SORT_BANK = "bank";
  // 账号
  private static final String SORT_ACCOUNT = "account";
  // 说明
  private static final String SORT_REMARK = "remark";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null) {
      return;
    }
    if (fieldName.equals(FILTER_STATE)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(Banks.FIELD_ENABLED, Basices.OPERATOR_EQUALS,
              StringUtil.toBoolean((String) state));
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(Banks.FIELD_ENABLED, Basices.OPERATOR_EQUALS,
            StringUtil.toBoolean((String) value));
      }
    } else if (fieldName.equals(FIELD_STOREUUID)) {
      queryDef.addCondition(Banks.CONDITION_STORE_EQUALS, value);
    } else if ("subject".equals(fieldName)) {
      queryDef.addCondition(Banks.CONDITION_CONTAINS_SUBJECT, Arrays.asList(value).toArray());
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;
    if (sort.getProperty().equals(SORT_CODE)) {
      queryDef.addOrder(Banks.ORDER_BY_CODE, dir);
    } else if (sort.getProperty().equals(SORT_NAME)) {
      queryDef.addOrder(Banks.ORDER_BY_NAME, dir);
    } else if (sort.getProperty().equals(SORT_STATUS)) {
      queryDef.addOrder(Banks.ORDER_BY_enabled, dir);
    } else if (sort.getProperty().equals(SORT_STORE)) {
      queryDef.addOrder(Banks.ORDER_BY_STORE, dir);
    } else if (sort.getProperty().equals(SORT_BANK)) {
      queryDef.addOrder(Banks.ORDER_BY_bank, dir);
    } else if (sort.getProperty().equals(SORT_ACCOUNT)) {
      queryDef.addOrder(Banks.ORDER_BY_ACCOUNT, dir);
    } else if (sort.getProperty().equals(SORT_REMARK)) {
      queryDef.addOrder(Banks.ORDER_BY_REMARK, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }

}
