/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceLogFilter.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.filter;

import java.util.Date;

import com.hd123.m3.account.gwt.adv.intf.client.AdvanceUrlParams.LogFilter;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.QueryFilter;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 预存款账户日志查询filter
 * 
 * @author zhuhairui
 * 
 */
public class AdvanceLogFilter extends QueryFilter implements LogFilter {
  private static final long serialVersionUID = -7115515185584080022L;

  public AdvanceLogFilter() {
    super();
    setPage(0);
    setPageSize(DEFAULT_PAGE_SIZE);
  }

  public String getAccountUnitCode() {
    return (String) get(KEY_ACCOUNTUNIT);
  }

  public void setAccountUnitCode(String accountUnit) {
    put(KEY_ACCOUNTUNIT, accountUnit);
  }

  public String getCounterpartUnit() {
    return (String) get(KEY_COUNTERPARTUNIT);
  }

  public void setCounterpartUnit(String counterpartUnit) {
    put(KEY_COUNTERPARTUNIT, counterpartUnit);
  }

  public String getCounterpartType() {
    return (String) get(KEY_COUNTERPARTTYPE);
  }

  public void setCounterpartType(String counterpartType) {
    put(KEY_COUNTERPARTTYPE, counterpartType);
  }

  public String getSubject() {
    return (String) get(KEY_SUBJECT);
  }

  public void setSubject(String subject) {
    put(KEY_SUBJECT, subject);
  }

  public String getAdvanceType() {
    return (String) get(KEY_ADVANCETYPE);
  }

  public void setAdvanceType(String direction) {
    put(KEY_ADVANCETYPE, direction);
  }

  public Date getBeginLogTime() {
    return (Date) get(KEY_LOGTIME_BEGIN);
  }

  public void setBeginLogTime(Date beginTime) {
    put(KEY_LOGTIME_BEGIN, beginTime);
  }

  public Date getEndLogTime() {
    return (Date) get(KEY_LOGTIME_END);
  }

  public void setEndLogTime(Date endTime) {
    put(KEY_LOGTIME_END, endTime);
  }

  @Override
  public void encodeUrlParams(UrlParameters params) {
    super.encodeUrlParams(params);

    String accountUnit = getAccountUnitCode();
    if (accountUnit != null)
      params.set(KEY_ACCOUNTUNIT, accountUnit);

    String counterpartUnit = getCounterpartUnit();
    if (counterpartUnit != null)
      params.set(KEY_COUNTERPARTUNIT, counterpartUnit);

    String counterpartType = getCounterpartType();
    if (counterpartType != null)
      params.set(KEY_COUNTERPARTTYPE, counterpartType);

    String subject = getSubject();
    if (subject != null)
      params.set(KEY_SUBJECT, subject);

    String direction = getAdvanceType();
    if (direction != null)
      params.set(KEY_ADVANCETYPE, direction);

    Date beginTime = getBeginLogTime();
    if (beginTime != null)
      params.set(KEY_LOGTIME_BEGIN,
          StringUtil.dateToString(beginTime, StringUtil.DEFAULT_DATE_FORMAT));
    Date endTime = getEndLogTime();
    if (endTime != null)
      params.set(KEY_LOGTIME_END, StringUtil.dateToString(endTime, StringUtil.DEFAULT_DATE_FORMAT));
  }

  @Override
  public void decodeUrlParams(UrlParameters params) {
    super.decodeUrlParams(params);

    setAccountUnitCode(params.get(KEY_ACCOUNTUNIT));
    setCounterpartUnit(params.get(KEY_COUNTERPARTUNIT));
    setCounterpartType(params.get(KEY_COUNTERPARTTYPE));
    setSubject(params.get(KEY_SUBJECT));
    setAdvanceType(params.get(KEY_ADVANCETYPE));

    Date beginTime = StringUtil.toDate(params.get(KEY_LOGTIME_BEGIN),
        StringUtil.DEFAULT_DATE_FORMAT);
    if (beginTime != null)
      setBeginLogTime(beginTime);

    Date endTime = StringUtil.toDate(params.get(KEY_LOGTIME_END), StringUtil.DEFAULT_DATE_FORMAT);
    if (endTime != null)
      setEndLogTime(endTime);
  }

}
