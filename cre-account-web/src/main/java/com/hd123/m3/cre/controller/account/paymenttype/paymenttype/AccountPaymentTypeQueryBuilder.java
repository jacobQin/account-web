/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	PaymentTypeQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月14日 - wanggang - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype.paymenttype;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.account.service.paymenttype.PaymentTypes;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;

/**
 * @author wanggang
 *
 */
@Component
public class AccountPaymentTypeQueryBuilder extends FlecsQueryDefinitionBuilder {

	private static final String FILTER_STATE = "state";
	private static final String FILTER_STATE_ENABLED = "enabled";
	private static final String FILTER_PAYMENTTYPE = "paymentType";

	private static final String FILTER_CODE = "code";
	private static final String FILTER_EXPECTS = "expects";

	@Override
	protected void buildFilter(FlecsQueryDefinition queryDef, String fieldName,
			Object value) throws Exception {
		if (queryDef == null || fieldName == null || value == null)
			return;

		if (FILTER_STATE_ENABLED.equals(fieldName)) {
			if (value instanceof List) {
				for (Object state : ((List) value)) {
					queryDef.addFlecsCondition(PaymentTypes.FIELD_ENABLED,
							Basices.OPERATOR_EQUALS,
							StringUtil.toBoolean(state.toString()));
				}
			} else if (value instanceof String) {
				queryDef.addFlecsCondition(PaymentTypes.FIELD_ENABLED,
						Basices.OPERATOR_EQUALS,
						StringUtil.toBoolean(value.toString()));
			}
			if (value instanceof Boolean) {
				queryDef.addFlecsCondition(PaymentTypes.FIELD_ENABLED,
						Basices.OPERATOR_EQUALS, value);
			}
		} else if (FILTER_PAYMENTTYPE.equals(fieldName)) {
			queryDef.addCondition(Payments.CONDITION_KEYWORD_EQUALS, value);
		} else {
			super.buildFilter(queryDef, fieldName, value);
		}
	}

	@Override
	protected void buildSort(FlecsQueryDefinition queryDef, OrderSort sort,
			QueryOrderDirection dir) {
		if (queryDef == null || sort == null) {
			return;
		}

		if (sort.getProperty().equals(FILTER_PAYMENTTYPE)) {
			queryDef.addOrder(PaymentTypes.ORDER_BY_CODE, dir);
		} else {
			super.buildSort(queryDef, sort, dir);
		}
	}
}