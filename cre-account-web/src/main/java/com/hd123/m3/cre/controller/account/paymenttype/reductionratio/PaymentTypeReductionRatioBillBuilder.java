/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	PaymentTypeBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月11日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype.reductionratio;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioBills;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;

/**
 * @author mengyinkun
 * 
 */
@Component
public class PaymentTypeReductionRatioBillBuilder extends FlecsQueryDefinitionBuilder {
  // 单号
  private static final String FILTER_BILLNUMBER = "billNumber";
  // 店招
  private static final String FILTER_SINGNBOARD = "signboard";
  // 项目
  private static final String FILTER_STORE_UUID = "storeUuid";

  // 排序字段
  private static final String SORT_CONTRACT = "contract";
  private static final String SORT_BIZSTATE = "bizState";
  private static final String SORT_BILLNUMBER = "billNumber";
  private static final String SORT_STORE = "store";
  private static final String SORT_REMARK = "remark";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null) {
      return;
    }
    if (fieldName.equalsIgnoreCase(FILTER_BILLNUMBER)) {
      queryDef.addFlecsCondition(PaymentTypeReductionRatioBills.FIELD_BILLNUMBER,
          Basices.OPERATOR_START_WITH, value);
    } else if (fieldName.equalsIgnoreCase(FILTER_STORE_UUID)) {
      queryDef.addFlecsCondition(PaymentTypeReductionRatioBills.FIELD_STORE,
          Basices.OPERATOR_EQUALS, value);
    } else if (fieldName.equalsIgnoreCase(FILTER_SINGNBOARD)) {
      queryDef.addFlecsCondition(PaymentTypeReductionRatioBills.FIELD_CONTRACT,
          Basices.OPERATOR_EQUALS, value);
    } else {
      super.buildFilter(queryDef, fieldName, value);
    }
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;
    if (sort.getProperty().equals(SORT_CONTRACT)) {
      queryDef.addOrder(PaymentTypeReductionRatioBills.ORDER_BY_CONTRACTNAME, dir);
    } else if (sort.getProperty().equals(SORT_BILLNUMBER)) {
      queryDef.addOrder(PaymentTypeReductionRatioBills.ORDER_BY_BILLNUMBER, dir);
    } else if (sort.getProperty().equals(SORT_BIZSTATE)) {
      queryDef.addOrder(PaymentTypeReductionRatioBills.ORDER_BY_BIZSTATE, dir);
    } else if (sort.getProperty().equals(SORT_STORE)) {
      queryDef.addOrder(PaymentTypeReductionRatioBills.ORDER_BY_STORECODE, dir);
    } else if (sort.getProperty().equals(SORT_REMARK)) {
      queryDef.addOrder(PaymentTypeReductionRatioBills.ORDER_BY_REMARK, dir);
    } else
      super.buildSort(queryDef, sort, dir);
  }
}
