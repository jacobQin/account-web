/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月22日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.builder;

import java.util.List;
import java.util.Map;

import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;

/**
 * @author chenganbang
 *
 */
public class AccountQueryBuilder {
  private static AccountQueryBuilder instance = null;

  private AccountQueryBuilder() {
  }

  public static AccountQueryBuilder getInstance() {
    if (instance == null) {
      instance = new AccountQueryBuilder();
    }
    return instance;
  }

  public QueryDefinition buildDefinition(QueryFilter queryFilter) {
    QueryDefinition queryDef = new QueryDefinition();
    Map<String, Object> filterMap = queryFilter.getFilter();
    if (!filterMap.isEmpty()) {
      // 默认条件
      if (!StringUtil.isNullOrBlank((String) filterMap.get("statementUuid"))) {
        queryDef
            .addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, filterMap.get("statementUuid"));
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
        queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_EQUALS, Accounts.NONE_BILL_UUID);
      } else {
        queryDef.addCondition(Accounts.CONDITION_CAN_STATEMENT);
      }
      queryDef.addCondition(Accounts.CONDITION_ACTIVE);
      List<String> excludedUuids = (List<String>) filterMap.get("excludedUuids");
      if (excludedUuids != null && !excludedUuids.isEmpty()) {
        queryDef.addCondition(Accounts.CONDITION_ACCID_NOTIN, excludedUuids.toArray());
      }
      if (!StringUtil.isNullOrBlank((String) filterMap.get("counterpartUuid"))) {
        queryDef.addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS,
            filterMap.get("counterpartUuid"));
      }
      if (!StringUtil.isNullOrBlank((String) filterMap.get("contractUuid"))) {
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_ID_EQUALS, filterMap.get("contractUuid"));
      }
      // 查询条件
      if (!StringUtil.isNullOrBlank((String) filterMap.get("sourceBillType"))) {
        queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS,
            filterMap.get("sourceBillType"));
      }
      if (!StringUtil.isNullOrBlank((String) filterMap.get("sourceBillNumber"))) {
        queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_NUMBER_STARTWITH,
            filterMap.get("sourceBillNumber"));
      }
      if (!StringUtil.isNullOrBlank((String) filterMap.get("subjectUuid"))) {
        queryDef.addCondition(Accounts.CONDITION_SUBJECT_UUID_EQUALS, filterMap.get("subjectUuid"));
      }
      if (!StringUtil.isNullOrBlank((String) filterMap.get("subjectCode"))) {
        queryDef.addCondition(Accounts.CONDITION_SUBJECT_CODE_LIKE, filterMap.get("subjectCode"));
      }
      if (!StringUtil.isNullOrBlank((String) filterMap.get("subjectName"))) {
        queryDef.addCondition(Accounts.CONDITION_SUBJECT_NAME_LIKE, filterMap.get("subjectName"));
      }
      if (filterMap.get("beginDate") != null || filterMap.get("endDate") != null) {
        queryDef.addCondition(Accounts.CONDITION_ACCOUNT_DATE_BETWEEN, filterMap.get("beginDate"),
            filterMap.get("endDate"));
      }
    }
    // 排序
    if (queryFilter.getSorts() != null && queryFilter.getSorts().isEmpty() == false) {
      for (OrderSort sort : queryFilter.getSorts()) {
        queryDef.getOrders().add(
            new QueryOrder(convertAccountOrderField(sort.getProperty()), OrderDir.asc.name()
                .equalsIgnoreCase(sort.getDirection()) ? QueryOrderDirection.asc
                : QueryOrderDirection.desc));
      }
    } else {
      queryDef.getOrders().add(new QueryOrder(Accounts.ORDER_BY_SUBJECT, QueryOrderDirection.asc));
    }

    queryDef.setPage(queryFilter.getCurrentPage());
    queryDef.setPageSize(queryFilter.getPageSize());

    return queryDef;
  }

  private String convertAccountOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if ("subject".equals(fieldName)) {
      return Accounts.ORDER_BY_SUBJECT;
    } else if ("direction".equals(fieldName)) {
      return Accounts.ORDER_BY_DIRECTION;
    } else if ("total".equals(fieldName)) {
      return Accounts.ORDER_BY_TOTAL;
    } else if ("tax".equals(fieldName)) {
      return Accounts.ORDER_BY_TAX;
    } else if ("billType".equals(fieldName)) {
      return Accounts.ORDER_BY_SOURCEBILLTYPE;
    } else if ("billNumber".equals(fieldName)) {
      return Accounts.ORDER_BY_SOURCEBILLNUMBER;
    } else if ("sourceBill".equals(fieldName)) {
      return Accounts.ORDER_BY_SOURCEBILLNUMBER;
    }
    return null;
  }
}
