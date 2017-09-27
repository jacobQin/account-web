/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	PaymentTypeReductionRatioDetailBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月15日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype.reductionratio;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioes;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author mengyinkun
 * 
 */
@Component
public class PaymentTypeReductionRatioDetailBuilder extends FlecsQueryDefinitionBuilder {
  // 查询条件
  private static final String FIELD_STORE = "storeUuid";
  private static final String FIELD_CONTRACT = "signboard";
  private static final String FIELD_PAYMENTTYPE = "paymentTypeUuid";
  private static final String FIELD_EFFECTDATE = "effectDate";
  // 排序条件
  private static final String SORT_CONTRACT = "contract";
  private static final String SORT_PAYMENTTYPE = "paymentType";
  private static final String SORT_EFFECTDATE = "effectDate";
  private static final String SORT_RATIO = "ratio";

  @Override
  protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName, Object value)
      throws Exception {
    if (queryDef == null || fieldName == null || value == null)
      return;
    if (fieldName.equalsIgnoreCase(FIELD_STORE)) {
      queryDef.addFlecsCondition(PaymentTypeReductionRatioes.FIELD_STORE, Basices.OPERATOR_EQUALS,
          value);
    } else if (fieldName.equalsIgnoreCase(FIELD_CONTRACT)) {
      queryDef.addFlecsCondition(PaymentTypeReductionRatioes.FIELD_CONTRACT,
          Basices.OPERATOR_EQUALS, value);
    } else if (fieldName.equalsIgnoreCase(FIELD_PAYMENTTYPE)) {
      queryDef.addFlecsCondition(PaymentTypeReductionRatioes.FIELD_PAYMENTTYPE,
          Basices.OPERATOR_EQUALS, value);
    } else if (fieldName.equalsIgnoreCase(FIELD_EFFECTDATE)) {
      queryDef.addFlecsCondition(PaymentTypeReductionRatioes.FIELD_EFFECTDATE,
          Basices.OPERATOR_EQUALS, StringUtil.toDate(value.toString()));
    } else
      super.buildFilter(queryDef, fieldName, value);
  }

  @Override
  protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort, QueryOrderDirection dir) {
    if (queryDef == null || sort == null)
      return;
    if (sort.getProperty().equalsIgnoreCase(SORT_CONTRACT)) {
      queryDef.addOrder(PaymentTypeReductionRatioes.ORDER_BY_CONTRACTNAME, dir);
    } else if (sort.getProperty().equalsIgnoreCase(SORT_PAYMENTTYPE)) {
      queryDef.addOrder(PaymentTypeReductionRatioes.ORDER_BY_PAYMENTTYPE, dir);
    } else if (sort.getProperty().equals(SORT_EFFECTDATE)) {
      queryDef.addOrder(PaymentTypeReductionRatioes.ORDER_BY_EFFECTDATE, dir);
      queryDef.addOrder(PaymentTypeReductionRatioes.ORDER_BY_ENDTDATE, dir);
    } else if (sort.getProperty().equals(SORT_RATIO)) {
      queryDef.addOrder(PaymentTypeReductionRatioes.ORDER_BY_RATIO, dir);
    } else
      super.buildSort(queryDef, sort, dir);
  }

}
