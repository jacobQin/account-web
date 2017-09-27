/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeLineFilter.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.filter;

import com.hd123.rumba.gwt.base.client.QueryFilter;

/**
 * @author subinzhu
 * 
 */
public class FeeLineFilter extends QueryFilter {
  private static final long serialVersionUID = 300200;

  private static final String KEY_SUBJECT = "subject";
  private static final int DEFAULT_PAGE_SIZE = 10;

  public FeeLineFilter() {
    super();
    setPage(0);
    setPageSize(DEFAULT_PAGE_SIZE);
  }

  public FeeLineFilter(QueryFilter filter) {
    super();
    assign(filter);
  }

  public String getSubjectLike() {
    return (String) get(KEY_SUBJECT);
  }

  public void setSubjectLike(String subject) {
    if (subject == null || "".equals(subject.trim()))
      remove(KEY_SUBJECT);
    else
      put(KEY_SUBJECT, subject);
  }
}
