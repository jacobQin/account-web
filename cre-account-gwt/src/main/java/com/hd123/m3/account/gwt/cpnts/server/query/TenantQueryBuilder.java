/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	TenantQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月11日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.query;

import java.util.Map;

import com.hd123.m3.account.gwt.base.client.BaseWidgetConstants;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.investment.service.tenant.tenant.Tenants;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author chenganbang
 */
public class TenantQueryBuilder extends CommonQueryBuilder{
  /**
   * 构建查询的语句条件<br>
   * <li>代码起始于<li>名称类似于<li>状态限制
   */
  public static FlecsQueryDefinition build4query(CodeNameFilter filter, String userId) {
    if (filter == null) {
      return null;
    }

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.getConditions().addAll(getPermConditions(Tenants.OBJECT_NAME_MERCHANT, userId));

    if (filter.get(BaseWidgetConstants.KEY_FILTER_STATE) != null) {
      queryDef.addFlecsCondition(Tenants.FIELD_STATE, Basices.OPERATOR_EQUALS,
          filter.get(BaseWidgetConstants.KEY_FILTER_STATE));
    }
    String code = filter.getCode();
    if (StringUtil.isNullOrBlank(code) == false) {
      queryDef.addCondition(Tenants.CONDITION_CODE_STARTWITH, code);
    }
    String name = filter.getName();
    if (StringUtil.isNullOrBlank(name) == false) {
      queryDef.addCondition(Tenants.CONDITION_NAME_LIKE, name);
    }

    String orderField = Tenants.ORDER_BY_CODE;
    QueryOrderDirection dir = QueryOrderDirection.asc;

    if (filter.getOrders().isEmpty() == false) {
      Order order = filter.getOrders().get(0);
      if (ObjectUtil.equals(BaseWidgetConstants.ORDER_BY_FIELD_NAME, order.getFieldName())) {
        orderField = Tenants.ORDER_BY_NAME;
      }

      dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
          : QueryOrderDirection.desc;
    }

    queryDef.addOrder(orderField, dir);
    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());

    return queryDef;
  }

  /**
   * 构建加载的语句条件<br>
   * <li>代码等于(为空返回空)<li>名称类似于 <li>状态限制
   */
  public static FlecsQueryDefinition build4load(String code, Map<String, Object> filter,
      String userId) {
    if (StringUtil.isNullOrBlank(code)) {
      return null;
    }

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.getConditions().addAll(getPermConditions(Tenants.OBJECT_NAME_MERCHANT, userId));

    if (filter != null && filter.get(BaseWidgetConstants.KEY_FILTER_STATE) != null) {
      queryDef.addFlecsCondition(Tenants.FIELD_STATE, Basices.OPERATOR_EQUALS,
          filter.get(BaseWidgetConstants.KEY_FILTER_STATE));
    }
    queryDef.addCondition(Tenants.CONDITION_CODE_EQUALS, code);

    return queryDef;
  }
}
