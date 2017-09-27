/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.server;

import com.hd123.m3.account.gwt.ivc.stock.intf.client.InvoiceStockUrlParams;
import com.hd123.m3.account.service.ivc.stock.InvoiceStocks;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 发票库存|查询条件构造。
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class InvoiceStockQueryBuilder extends FlecsConditionDecoderImpl {
  private static InvoiceStockQueryBuilder instance;

  public static InvoiceStockQueryBuilder getInstance() {
    if (instance == null) {
      instance = new InvoiceStockQueryBuilder();
    }
    return instance;
  }

  @Override
  protected String getDefaultOrderField() {
    return InvoiceStocks.ORDER_BY_INVOICECODE;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }

    if (InvoiceStockUrlParams.Search.FIELD_INVOICE_NUNBER.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_INVOICENUMBER;
    } else if (InvoiceStockUrlParams.Search.FIELD_INVOICE_CODE.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_INVOICECODE;
    } else if (InvoiceStockUrlParams.Search.FIELD_ACCOUNT_UNIT.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_STORE;
    } else if (InvoiceStockUrlParams.Search.FIELD_ABORTDATE.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_ABORTDATE;
    } else if (InvoiceStockUrlParams.Search.FIELD_STATE.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_STATE;
    } else if (InvoiceStockUrlParams.Search.FIELD_INVOICE_TYPE.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_INVOICETYPE;
    } else if (InvoiceStockUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_CREATOR_CODE;
    } else if (InvoiceStockUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_CREATOR_NAME;
    } else if (InvoiceStockUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_CREATED;
    } else if (InvoiceStockUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_LAST_MODIFIER_CODE;
    } else if (InvoiceStockUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME
        .equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_LAST_MODIFIER_NAME;
    } else if (InvoiceStockUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return InvoiceStocks.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }
  
  @Override
  protected String convertOrderField(String fieldName) {

    if (StringUtil.isNullOrBlank(fieldName)) {
      return null;
    }
    if (fieldName.trim().equals(InvoiceStockUrlParams.Search.FIELD_INVOICE_TYPE)) {
      return InvoiceStocks.ORDER_BY_INVOICETYPE;
    }else if (fieldName.trim().equals(InvoiceStockUrlParams.Search.FIELD_INVOICE_CODE)) {
      return InvoiceStocks.ORDER_BY_INVOICECODE;
    } else if (fieldName.trim().equals(InvoiceStockUrlParams.Search.FIELD_INVOICE_NUNBER)) {
      return InvoiceStocks.ORDER_BY_INVOICENUMBER;
    }else if (fieldName.trim().equals(InvoiceStockUrlParams.Search.FIELD_STATE)) {
      return InvoiceStocks.ORDER_BY_STATE;
    }  else if (fieldName.trim().equals(InvoiceStockUrlParams.Search.FIELD_REMARK)) {
      return InvoiceStocks.ORDER_BY_REMARK;
    }else if (fieldName.trim().equals(InvoiceStockUrlParams.Search.FIELD_ACCOUNT_UNIT)) {
      return InvoiceStocks.ORDER_BY_STORE;
    }  else if (fieldName.trim().equals(InvoiceStockUrlParams.Search.FIELD_HOLDER)) {
      return InvoiceStocks.ORDER_BY_HOLDER;
    } else if (fieldName.trim().equals(InvoiceStockUrlParams.Search.FIELD_AMOUNT)) {
      return InvoiceStocks.ORDER_BY_AMOUNT;
    }
    return null;
  }
}
