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
import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.BInvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStocks;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
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
public class InvoiceStockQueryBuilder extends CommonQueryBuilder {
  /**
   * 构建查询的语句条件<br>
   */
  public static FlecsQueryDefinition build4query(CodeNameFilter filter, String userId) {
    if (filter == null) {
      return null;
    }

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef
        .addCondition(InvoiceStocks.CONDITION_PERM_STORE_ID_IN, getUserStores(userId).toArray());

    if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICECODE_EQUAL, filter.getCode());
    }
    if (StringUtil.isNullOrBlank(filter.getName()) == false) {
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICENUMBER_LIKE, filter.getName());
    }
    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_SORT) != null) {
      queryDef.addCondition(InvoiceStocks.FIELD_SORT, InvoiceStocks.OPERATOR_EQUALS,
          filter.get(BInvoiceStock.KEY_FILTER_SORT));
    }
    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT) != null) {
      queryDef.addFlecsCondition(InvoiceStocks.FIELD_STORE, InvoiceStocks.OPERATOR_EQUALS,
          filter.get(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT));
    }
    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_INVOICE_TYPE) instanceof List) {
      for (String invoiceType : ((List<String>) filter.get(BInvoiceStock.KEY_FILTER_INVOICE_TYPE))) {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICETYPE_EQUAL, invoiceType);
      }
    }
    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_USE_TYPE) != null) {
      queryDef.addCondition(InvoiceStocks.CONDITION_USETYPE_EQUAL,
          filter.get(BInvoiceStock.KEY_FILTER_USE_TYPE));
    }
    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_STATE) instanceof List) {
      for (String state : ((List<String>) filter.get(BInvoiceStock.KEY_FILTER_STATE))) {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL, state);
      }
    }

    String orderField = InvoiceStocks.ORDER_BY_INVOICECODE;
    QueryOrderDirection dir = QueryOrderDirection.asc;
    if (filter.getOrders().isEmpty() == false) {
      Order order = filter.getOrders().get(0);
      if (ObjectUtil.equals(AccountCpntsContants.ORDER_BY_FIELD_NAME, order.getFieldName())) {
        orderField = InvoiceStocks.ORDER_BY_INVOICENUMBER;
      }

      dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
          : QueryOrderDirection.desc;
    }
    queryDef.addOrder(orderField, dir);
    if (InvoiceStocks.ORDER_BY_INVOICENUMBER.equals(orderField) == false) {
      queryDef.addOrder(InvoiceStocks.ORDER_BY_INVOICENUMBER, QueryOrderDirection.asc);
    }

    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());

    return queryDef;
  }

  /**
   * 构建加载的语句条件<br>
   */
  public static FlecsQueryDefinition build4load(String code, Map<String, Object> filter,
      String userId) {
    if (StringUtil.isNullOrBlank(code)) {
      return null;
    }
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef
        .addCondition(InvoiceStocks.CONDITION_PERM_STORE_ID_IN, getUserStores(userId).toArray());
    queryDef.addFlecsCondition(InvoiceStocks.FIELD_INVOICENUMBER, InvoiceStocks.OPERATOR_EQUALS,
        code);

    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT) != null) {
      queryDef.addCondition(InvoiceStocks.FIELD_STORE, InvoiceStocks.OPERATOR_EQUALS,
          filter.get(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT));
    }
    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_SORT) != null) {
      queryDef.addCondition(InvoiceStocks.FIELD_SORT, InvoiceStocks.OPERATOR_EQUALS,
          filter.get(BInvoiceStock.KEY_FILTER_SORT));
    }
    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_INVOICE_TYPE) instanceof List) {
      for (String invoiceType : ((List<String>) filter.get(BInvoiceStock.KEY_FILTER_INVOICE_TYPE))) {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICETYPE_EQUAL, invoiceType);
      }
    }
    if (filter != null && filter.get(BInvoiceStock.KEY_FILTER_STATE) instanceof List) {
      for (String state : ((List<String>) filter.get(BInvoiceStock.KEY_FILTER_STATE))) {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL, state);
      }
    }

    return queryDef;
  }
}
