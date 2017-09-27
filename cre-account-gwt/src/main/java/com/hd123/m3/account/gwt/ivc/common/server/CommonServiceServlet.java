/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CommonServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.ivc.common.client.CommonConstants;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.common.client.rpc.CommonService;
import com.hd123.m3.account.gwt.ivc.common.server.converter.InvoiceStockBizConverter;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStocks;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.gwt.base.client.QueryFilter;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.server.RPageDataConverter;
import com.hd123.rumba.webframe.gwt.base.server.ModuleServiceServlet;
import com.hd123.rumba.webframe.session.Session;

/**
 * @author lixiaohong
 * 
 */
public class CommonServiceServlet extends ModuleServiceServlet implements CommonService {

  private static final long serialVersionUID = 8417509555494867740L;

  @Override
  public RPageData<BInvoiceStock> queryStocks(QueryFilter filter) throws ClientBizException {
    if (filter == null) {
      return new RPageData<BInvoiceStock>();
    }
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    List<String> storeUuids = new ArrayList<String>(); // 用户有管理权的项目uuid集合
    storeUuids = CommonQueryBuilder.getUserStores(Session.getInstance().getCurrentUser().getId());
    String storeUuid = (String) filter.get(CommonConstants.FIELD_INVOICE_STORE);
    if (storeUuid == null) {
      return null;
    }
    if (StringUtils.isBlank(storeUuid) == false) {
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTORE_IN, storeUuid);
    } else {
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTORE_IN, storeUuids.toArray());
    }

    String invoiceType = (String) filter.get(CommonConstants.FIELD_INVOICE_TYPE);
    if (StringUtils.isBlank(invoiceType) == false) {
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICETYPE_EQUAL, invoiceType);
    }

    String number = (String) filter.get(CommonConstants.FIELD_INVOICE_NUMBER);
    if (StringUtils.isBlank(number) == false) {
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICENUMBER_LIKE, number);
    }

    String code = (String) filter.get(CommonConstants.FIELD_INVOICE_CODE);
    if (StringUtils.isBlank(code) == false) {
      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICECODE_EQUAL, code);
    }

//    String state = (String) filter.get(CommonConstants.FIELD_INVOICE_STATE);
//    if (StringUtils.isBlank(state) == false) {
//      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL, state);
//    }

    if (filter != null && filter.get(CommonConstants.FIELD_INVOICE_STATE) instanceof List) {
      for (String state : ((List<String>) filter.get(CommonConstants.FIELD_INVOICE_STATE))) {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL, state);
      }
    }
    
    String sort = (String) filter.get(CommonConstants.FIELD_INVOICE_SORT);
    if (CommonConstants.SORT_INVOICE.equals(sort)) {
      queryDef.addCondition(InvoiceStocks.CONDITION_ONLY_INVOCIE);
    } else if (CommonConstants.SORT_RECEIPT.equals(sort)) {
      queryDef.addCondition(InvoiceStocks.CONDITION_ONLY_RECEIPT);
    }

    Object obj = filter.get(CommonConstants.FIELD_USETYPE);
    if (obj != null) {
      List<String> useTypes = (List<String>) obj;
      for (String useType : useTypes) {
        queryDef.addCondition(InvoiceStocks.CONDITION_USETYPE_EQUAL, Integer.valueOf(useType));
      }
    }

    String orderField = CommonConstants.FIELD_INVOICE_CODE;
    QueryOrderDirection dir = QueryOrderDirection.asc;

    if (filter.getOrders().isEmpty() == false) {
      dir = convertOrderDirection(filter.getOrders().get(0).getDir(), QueryOrderDirection.asc);
      if (CommonConstants.FIELD_INVOICE_CODE.equals(filter.getOrders().get(0).getFieldName())) {
        orderField = CommonConstants.FIELD_INVOICE_CODE;
      }

      if (CommonConstants.FIELD_INVOICE_NUMBER.equals(filter.getOrders().get(0).getFieldName())) {
        orderField = CommonConstants.FIELD_INVOICE_NUMBER;
      }
      if (CommonConstants.FIELD_INVOICE_STATE.equals(filter.getOrders().get(0).getFieldName())) {
        orderField = CommonConstants.FIELD_INVOICE_STATE;
      }
    }
    queryDef.addOrder(orderField, dir);

    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());

    try {
      QueryResult<InvoiceStock> qr = getInvoiceStockService().query(queryDef);

      return RPageDataConverter.convert(qr, InvoiceStockBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BInvoiceStock getStockByNumber(String number, Map<String, Object> filter)
      throws ClientBizException {
    if (StringUtils.isBlank(number)) {
      return null;
    }
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      List<String> storeUuids = new ArrayList<String>(); // 用户有管理权的项目uuid集合
      storeUuids = CommonQueryBuilder.getUserStores(Session.getInstance().getCurrentUser().getId());

      String storeUuid = (String) filter.get(CommonConstants.FIELD_INVOICE_STORE);
      if (storeUuid == null) {
        return null;
      }
      if (StringUtils.isBlank(storeUuid) == false) {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTORE_IN, storeUuid);
      } else {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTORE_IN, storeUuids.toArray());
      }

      String invoiceType = (String) filter.get(CommonConstants.FIELD_INVOICE_TYPE);
      if (StringUtils.isBlank(invoiceType) == false) {
        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICETYPE_EQUAL, invoiceType);
      }

//      String state = (String) filter.get(CommonConstants.FIELD_INVOICE_STATE);
//      if (StringUtils.isBlank(state) == false) {
//        queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL, state);
//      }
      
      if (filter != null && filter.get(CommonConstants.FIELD_INVOICE_STATE) instanceof List) {
        for (String state : ((List<String>) filter.get(CommonConstants.FIELD_INVOICE_STATE))) {
          queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL, state);
        }
      }
      
      String sort = (String) filter.get(CommonConstants.FIELD_INVOICE_SORT);
      if (CommonConstants.SORT_INVOICE.equals(sort)) {
        queryDef.addCondition(InvoiceStocks.CONDITION_ONLY_INVOCIE);
      } else if (CommonConstants.SORT_RECEIPT.equals(sort)) {
        queryDef.addCondition(InvoiceStocks.CONDITION_ONLY_RECEIPT);
      }

      Object obj = filter.get(CommonConstants.FIELD_USETYPE);
      if (obj != null) {
        List<String> useTypes = (List<String>) obj;
        for (String useType : useTypes) {
          queryDef.addCondition(InvoiceStocks.CONDITION_USETYPE_EQUAL, Integer.valueOf(useType));
        }
      }

      queryDef.addCondition(InvoiceStocks.CONDITION_INVOICENUMBER_IN, number);
      QueryResult<InvoiceStock> qr = getInvoiceStockService().query(queryDef);
      if (qr.getRecords().size() > 0) {
        return InvoiceStockBizConverter.getInstance().convert(qr.getRecords().get(0));
      } else {
        return null;
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private QueryOrderDirection convertOrderDirection(OrderDir orderDir,
      QueryOrderDirection defaultDirection) {
    if (orderDir == null)
      return defaultDirection;
    if (OrderDir.desc.equals(orderDir))
      return QueryOrderDirection.desc;
    return QueryOrderDirection.asc;
  }

  @Override
  public Map<String, String> getInvoiceTypes() throws ClientBizException {
    Map mapInvoiceTypes = new HashMap<String, String>();
    try {
      for (BInvoiceType type : InvoiceTypeUtils.getAll()) {
        mapInvoiceTypes.put(type.getCode(), type.getName());
      }
      return mapInvoiceTypes;
    } catch (Exception e) {
      return null;
    }
  }

  private com.hd123.m3.account.service.ivc.stock.InvoiceStockService getInvoiceStockService() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.ivc.stock.InvoiceStockService.class);
  }
}
