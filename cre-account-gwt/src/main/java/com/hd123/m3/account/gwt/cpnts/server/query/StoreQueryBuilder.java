/**
 * 
 */
package com.hd123.m3.account.gwt.cpnts.server.query;

import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.base.client.BaseWidgetConstants;
import com.hd123.m3.commons.biz.BasicState;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.data.receiptor.M3UserOrgService;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.commons.rs.service.store.Stores;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author lizongyi
 * 
 */
public class StoreQueryBuilder extends CommonQueryBuilder {
  /**
   * 构建查询的语句条件<br>
   * <li>代码起始于<li>名称类似于<li>状态限制
   */
  public static FlecsQueryDefinition build4query(CodeNameFilter filter, String userId) {
    if (filter == null) {
      return null;
    }

    List<String> orgIds = getUserOrgService().getUserPermOrgIds(userId);
    if (orgIds.isEmpty())
      return null;

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(Stores.CONDITION_ORG_ID_IN, orgIds.toArray());

    if ((BBasicState.USING).equals(filter.get(BaseWidgetConstants.KEY_FILTER_STATE))) {
      queryDef.addCondition(Stores.CONDITION_STATE_EQUALS, BasicState.using.name());
    } else if (BBasicState.DELETED.equals(filter.get(BaseWidgetConstants.KEY_FILTER_STATE))) {
      queryDef.addCondition(Stores.CONDITION_STATE_EQUALS, BasicState.deleted.name());
    }
    if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
      queryDef.addCondition(Stores.CONDITION_CODE_STARTWITH, filter.getCode());
    }
    if (StringUtil.isNullOrBlank(filter.getName()) == false) {
      queryDef.addCondition(Stores.CONDITION_NAME_LIKE, filter.getName());
    }

    String orderField = Stores.ORDER_BY_CODE;
    QueryOrderDirection dir = QueryOrderDirection.asc;

    if (filter.getOrders().isEmpty() == false) {
      dir = OrderDir.asc.equals(filter.getOrders().get(0).getDir()) ? QueryOrderDirection.asc
          : QueryOrderDirection.desc;
      if (ObjectUtil.equals(BaseWidgetConstants.ORDER_BY_FIELD_NAME, filter.getOrders().get(0)
          .getFieldName())) {
        orderField = Stores.ORDER_BY_NAME;
      }
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

    List<String> orgIds = getUserOrgService().getUserPermOrgIds(userId);
    if (orgIds.isEmpty())
      return null;

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(Stores.CONDITION_ORG_ID_IN, orgIds.toArray());

    if ((BBasicState.USING).equals(filter.get(BaseWidgetConstants.KEY_FILTER_STATE))) {
      queryDef.addCondition(Stores.CONDITION_STATE_EQUALS, BasicState.using.name());
    } else if (BBasicState.DELETED.equals(filter.get(BaseWidgetConstants.KEY_FILTER_STATE))) {
      queryDef.addCondition(Stores.CONDITION_STATE_EQUALS, BasicState.deleted.name());
    }
    queryDef.addCondition(Stores.CONDITION_CODE_EQUALS, code);

    return queryDef;
  }

  private static M3UserOrgService getUserOrgService() {
    return M3ServiceFactory.getBean(M3UserOrgService.DEFAULT_CONTEXT_ID, M3UserOrgService.class);
  }
}
