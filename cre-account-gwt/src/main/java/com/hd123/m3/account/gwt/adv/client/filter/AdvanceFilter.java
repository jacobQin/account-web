/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceFilter.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.filter;

import com.hd123.m3.account.gwt.adv.intf.client.AdvanceUrlParams.CounterpartUnitFilter;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * 预存款账户查询filter
 * 
 * @author zhuhairui
 * 
 */
public class AdvanceFilter extends CodeNameFilter implements CounterpartUnitFilter {
  private static final long serialVersionUID = -4954422331087322215L;

  public AdvanceFilter() {
    super();
    setPage(0);
    setPageSize(20);
  }

  public String getCounterpartUnitLike() {
    return (String) get(KEY_COUNTERPARTUNIT_LIKE);
  }

  public void setCounterpartUnitLike(String counterpartUnitLike) {
    if (counterpartUnitLike == null || "".equals(counterpartUnitLike.trim()))
      this.remove(KEY_COUNTERPARTUNIT_LIKE);
    else
      put(KEY_COUNTERPARTUNIT_LIKE, counterpartUnitLike.trim());
  }

  public String getCounterpartType() {
    return (String) get(KEY_COUNTERPARTTYPE);
  }

  public void setCounterpartType(String type) {
    if (StringUtil.isNullOrBlank(type))
      remove(KEY_COUNTERPARTTYPE);
    else
      put(KEY_COUNTERPARTTYPE, type.trim());
  }
  
  @Override
  public void encodeUrlParams(UrlParameters params) {
    super.encodeUrlParams(params);

    String counterpartUnitLike = getCounterpartUnitLike();
    if (counterpartUnitLike != null)
      params.set(KEY_COUNTERPARTUNIT_LIKE, counterpartUnitLike);
  }

  @Override
  public void decodeUrlParams(UrlParameters params) {
    super.decodeUrlParams(params);

    setPageSize(20);

    setCounterpartUnitLike(params.get(KEY_COUNTERPARTUNIT_LIKE));
  }

}
