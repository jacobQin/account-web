/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	SubjectFilter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-16 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.filter;

import java.util.Set;

import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author chenpeisi
 * 
 */
public class SubjectFilter extends CodeNameFilter {
  private static final long serialVersionUID = 4213966897086989102L;

  private Boolean state;
  private String subjectType;
  private Integer directionType;
  private String usageType;
  private Set<String> uuids;

  public Boolean getState() {
    return state;
  }

  public void setState(Boolean state) {
    this.state = state;
  }

  public String getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(String subjectType) {
    this.subjectType = subjectType;
  }

  public Integer getDirectionType() {
    return directionType;
  }

  public void setDirectionType(Integer directionType) {
    this.directionType = directionType;
  }

  public String getUsageType() {
    return usageType;
  }

  public void setUsageType(String usageType) {
    this.usageType = usageType;
  }

  public Set<String> getUuids() {
    return uuids;
  }

  public void setUuids(Set<String> uuids) {
    this.uuids = uuids;
  }

}
