/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	BuildingQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-18 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.query;

import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.service.counterpart.Counterparts;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author chenrizhang
 * 
 */
public class CounterpartQueryBuilder extends CommonQueryBuilder {
  /**
   * 构建查询的语句条件<br>
   */
  public static QueryDefinition build4query(CodeNameFilter filter, String userId) {
    if (filter == null) {
      return null;
    }

    QueryDefinition queryDef = new QueryDefinition();
    QueryCondition condition = new QueryCondition();
    condition.setOperation(Basices.CONDITION_PERM_GROUP_ID_IN);
    List<String> list = getUserGroups(userId);
    if (list.size() > 0) {
      condition.addParameter(list.toArray());
    }
    if (!list.contains(HasPerm.DEFAULT_PERMGROUP)) {
      condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
    }
    queryDef.getConditions().add(condition);

    if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
      queryDef.addCondition(Counterparts.CONDITION_CODE_STARTWITH, filter.getCode());
    }
    if (StringUtil.isNullOrBlank(filter.getName()) == false) {
      queryDef.addCondition(Counterparts.CONDITION_NAME_LIKE, filter.getName());
    }
    if (filter.get(BCounterpart.KEY_FILTER_STATE) != null) {
      queryDef.addCondition(Counterparts.CONDITION_STATE_EQUALS,
          filter.get(BCounterpart.KEY_FILTER_STATE));
    }
    if (filter.get(BCounterpart.KEY_FILTER_MODULE) instanceof List) {
      queryDef.addCondition(Counterparts.CONDITION_MODULE_IN,
          ((List) filter.get(BCounterpart.KEY_FILTER_MODULE)).toArray());
    } else {
      Map<String, String> map = getOptionService().getCounterpartType();
      queryDef.addCondition(Counterparts.CONDITION_MODULE_IN, map.keySet().toArray());
    }

    String orderField = Counterparts.ORDER_BY_CODE;
    QueryOrderDirection dir = QueryOrderDirection.asc;
    if (filter.getOrders().isEmpty() == false) {
      Order order = filter.getOrders().get(0);
      if (ObjectUtil.equals(AccountCpntsContants.ORDER_BY_FIELD_NAME, order.getFieldName())) {
        orderField = Counterparts.ORDER_BY_NAME;
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
   */
  public static QueryDefinition build4load(String code, Map<String, Object> filter, String userId) {
    if (StringUtil.isNullOrBlank(code)) {
      return null;
    }

    QueryDefinition queryDef = new QueryDefinition();
    QueryCondition condition = new QueryCondition();
    condition.setOperation(Basices.CONDITION_PERM_GROUP_ID_IN);
    List<String> list = getUserGroups(userId);
    if (list.size() > 0) {
      condition.addParameter(list.toArray());
    }
    if (!list.contains(HasPerm.DEFAULT_PERMGROUP)) {
      condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
    }
    queryDef.getConditions().add(condition);

    if (filter != null && filter.get(BCounterpart.KEY_FILTER_STATE) instanceof List) {
      for (String state : ((List<String>) filter.get(BCounterpart.KEY_FILTER_STATE))) {
        queryDef.addCondition(Counterparts.CONDITION_STATE_EQUALS, state);
      }
    }

    if (filter != null && filter.get(BCounterpart.KEY_FILTER_MODULE) instanceof List) {
      queryDef.addCondition(Counterparts.CONDITION_MODULE_IN,
          ((List) filter.get(BCounterpart.KEY_FILTER_MODULE)).toArray());
    } else {
      Map<String, String> map = getOptionService().getCounterpartType();
      queryDef.addCondition(Counterparts.CONDITION_MODULE_IN, map.keySet().toArray());
    }

    queryDef.addCondition(Counterparts.CONDITION_CODE_EQUALS, code);

    return queryDef;
  }

  protected static AccountOptionService getOptionService() {
    return M3ServiceFactory.getService(AccountOptionService.class);
  }
}
