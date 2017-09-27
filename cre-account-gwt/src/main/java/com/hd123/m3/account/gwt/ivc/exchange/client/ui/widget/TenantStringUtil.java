/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	TenantStringUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年2月2日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;

/**
 * 商户拼接工具类
 * 
 * @author LiBin
 * 
 */
public class TenantStringUtil {

  public static String toTenantsString(List<BInvoiceExchAccountLine> lines) {
    if (lines == null || lines.isEmpty()) {
      return null;
    }

    Set<String> tenants = new HashSet<String>();
    for (BInvoiceExchAccountLine line : lines) {
      if (line.getAcc1().getCounterpart() != null) {
        tenants.add(line.getAcc1().getCounterpart().toFriendlyStr());
      }
    }
    
    return CollectionUtil.toString(tenants, ';');
  }
}
