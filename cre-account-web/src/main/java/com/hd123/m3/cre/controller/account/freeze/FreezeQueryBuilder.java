/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	FreezeQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月17日 - renjingzhan - 创建。
 */
package com.hd123.m3.cre.controller.account.freeze;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.freeze.Freezes;
import com.hd123.m3.account.service.statement.adjust.StatementAdjusts;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author renjingzhan
 *
 */
@Component
public class FreezeQueryBuilder extends FlecsQueryDefinitionBuilder {

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if ("billNumber".equals(fieldName)) {
      if (value != null) {
        queryDef.addFlecsCondition(Freezes.FIELD_BILLNUMBER, Freezes.OPERATOR_START_WITH, value);
      }
    } else if ("accountUnit".equals(fieldName)) {
      queryDef
          .addFlecsCondition(Freezes.FIELD_ACCOUNTUNIT, StatementAdjusts.OPERATOR_EQUALS, value);
    } else if ("contract".equals(fieldName)) {
      queryDef.addCondition("contract.equals",
           value);
    } else if ("freezeDate".equals(fieldName)) {
      Date freezeDate = StringUtil.toDate((String) value, "yyyy-MM-dd");
      queryDef.addFlecsCondition(Freezes.FIELD_FREEZE_DATE, Freezes.OPERATOR_EQUALS, freezeDate);
    } else if ("unfreezeDate".equals(fieldName)) {
      Date unfreezeDate = StringUtil.toDate((String) value, "yyyy-MM-dd");
      queryDef
          .addFlecsCondition(Freezes.FIELD_UNFREEZE_DATE, Freezes.OPERATOR_EQUALS, unfreezeDate);
    } else if ("state".equals(fieldName)) {
      if (value != null) {
        if (value instanceof String) {
          queryDef.addCondition("state.equals", value);
        } else {
          List<String> list = (List<String>) value;
          queryDef.addCondition("state.equals", list.get(0));
        }
      }
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

}
