/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	InvoiceTypeUtils.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.util.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.service.invoicetype.InvoiceType;
import com.hd123.m3.account.service.invoicetype.InvoiceTypeService;
import com.hd123.m3.commons.rpc.M3ServiceFactory;

/**
 * 发票类型工具类。
 * 
 * @author chenpeisi
 * 
 */
public class InvoiceTypeUtils {
  private static final List<BInvoiceType> types = new ArrayList<BInvoiceType>();
  private static final Map<String, BInvoiceType> map = new HashMap<String, BInvoiceType>();

  private static boolean inited = false;

  private static void init() throws Exception {
    if (inited)
      return;

    try {
      List<InvoiceType> list = M3ServiceFactory.getService(InvoiceTypeService.class).getAllTypes();
      for (InvoiceType u : list) {
        BInvoiceType type = new BInvoiceType();
        type.setCode(u.getCode());
        type.setName(u.getName());
        type.setSort(u.getSort());
        types.add(type);
        map.put(type.getCode(), type);
      }
      inited = true;
    } catch (Exception e) {
      Logger.getLogger(InvoiceTypeUtils.class).error("InvoiceTypeUtils init error", e);
      throw new Exception(e.getMessage());
    }
  }

  /**
   * 根据代码获取发票类型。
   * 
   * @param code
   *          发票类型代码
   * @return 发票类型
   * @throws Exception
   */
  public static BInvoiceType getInvoiceTypeByCode(String code) throws Exception {
    init();
    BInvoiceType u = map.get(code);
    return u == null ? null : u.clone();
  }

  /**
   * 获取所有发票类型。
   * 
   * @return 发票类型列表
   * @throws Exception
   */
  public static List<BInvoiceType> getAll() throws Exception {
    init();
    List<BInvoiceType> list = new ArrayList<BInvoiceType>();
    for (BInvoiceType type : types) {
      list.add(type.clone());
    }
    return list;
  }
}
