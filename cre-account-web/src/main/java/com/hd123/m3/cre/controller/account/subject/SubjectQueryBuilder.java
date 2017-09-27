/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	SubjectQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月18日 - lizongyi - 创建。
 */
package com.hd123.m3.cre.controller.account.subject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;

/**
 * @author lizongyi
 *
 */
@Component
public class SubjectQueryBuilder extends FlecsQueryDefinitionBuilder {

  private static final String FILTER_CODE = "code";
  private static final String FILTER_STATE = "state";
  private static final String FILTER_STATE_ENABLED = "enabled";
  private static final String FILTER_USAGETYPE = "usageType";
  private static final String FILTER_CUSTOMTYPE = "customType";
  private static final String FILTER_EXPECTS = "expects";
  private static final String FILTER_DIRECTION = "direction";
  private static final String FILTER_SUBJECTTYPE = "subjectType";

  private static final String FILTER_FETCHPARTS = "fetchParts";

  /** 排序字段：科目 */
  private static final String SORT_SUBJECT = "subject";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;

    if (FILTER_STATE.equals(fieldName)) {
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          queryDef.addFlecsCondition(Subjects.FIELD_STATE, Basices.OPERATOR_EQUALS,
              FILTER_STATE_ENABLED.equals(state));
        }
      } else if (value instanceof String) {
        queryDef.addFlecsCondition(Subjects.FIELD_STATE, Basices.OPERATOR_EQUALS,
            FILTER_STATE_ENABLED.equals(value));
      }
    } else if (FILTER_SUBJECTTYPE.equals(fieldName)) {
      queryDef.addCondition(Subjects.CONDITION_SUBJECT_TYPE_EQUALS, value);
    } else if (FILTER_DIRECTION.equals(fieldName)) {
      queryDef.addCondition(Subjects.CONDITION_DIRECTION_EQUALS, Integer.valueOf(value.toString()));
    } else if (FILTER_CODE.equals(fieldName)) {
      queryDef.addFlecsCondition(Subjects.FIELD_CODE, Subjects.OPERATOR_START_WITH, value);
    } else if (FILTER_USAGETYPE.equals(fieldName)) {
      Set<String> usageCodes = new HashSet<String>();
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          usageCodes.add((String) state);
        }
      } else if (value instanceof String) {
        usageCodes.add((String) value);
      }
      if (usageCodes.isEmpty() == false) {
        queryDef.addCondition(Subjects.CONDITION_USAGE_IN, usageCodes.toArray());
      }
    } else if (FILTER_EXPECTS.equals(fieldName)) {
      Set<String> expects = new HashSet<String>();
      if (value instanceof List) {
        for (Object state : ((List) value)) {
          expects.add((String) state);
        }
      } else if (value instanceof String) {
        expects.add((String) value);
      }
      if (expects.isEmpty() == false) {
        queryDef.addCondition(Subjects.CONDITION_UUID_NOT_IN, expects.toArray());
      }
    } else if (FILTER_FETCHPARTS.equals(fieldName)) {

    } else if (FILTER_CUSTOMTYPE.equals(fieldName)) {
      queryDef.addFlecsCondition(Subjects.FIELD_CUSTOMTYPE, Subjects.OPERATOR_EQUALS, value);
    } else if ("keyword".equals(fieldName)) {
      queryDef.addCondition(Subjects.CONDITION_KEYWORD_EQUALS, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null) {
      return;
    }
    if (SORT_SUBJECT.equals(sort.getProperty())) {
      queryDef.addOrder(Subjects.ORDER_BY_CODE, dir);
    } else {
      super.buildSort(queryDef, sort, dir);
    }
  }
}
