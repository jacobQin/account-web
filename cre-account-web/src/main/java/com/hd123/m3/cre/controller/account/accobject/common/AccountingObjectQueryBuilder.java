/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountingObjectQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月9日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject.common;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.accobject.AccountingObjectBills;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;

/**
 * 核算主体查询条件解析器
 * 
 * @author LiBin
 *
 */
@Component
public class AccountingObjectQueryBuilder extends FlecsQueryDefinitionBuilder {

  public static final String FILTER_STORE_UUID = "storeUuid";
  public static final String FILTER_CONTRACT_UUID = "contractUuid";
  public static final String FILTER_SUBJECT = "subject";
  /** 查询条件(String)：核算主体。 */
  public static final String FILTER_ACCOBJECT = "accObjectUuid";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null){
      return;
    }
    
    if (FILTER_STORE_UUID.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_STORE_EQUALS, value);
    } else if (FILTER_CONTRACT_UUID.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_CONTRACT_EQUALS, value);
    } else if (FILTER_SUBJECT.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_SUBJECT_EQUALS, value);
    } else if (FILTER_ACCOBJECT.equals(fieldName)) {
      queryDef.addCondition(AccountingObjectBills.CONDITION_ACCOBJECT_EQUALS, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

}
