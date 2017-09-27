/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-common
 * 文件名：	BStringPairGroupItem.java
 * 模块说明：	
 * 修改历史：
 * 2014-7-24 - huangjunxian- 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjunxian
 * 
 */
public class BStringPairGroupItem implements Serializable {
  private static final long serialVersionUID = 8673781415811093461L;

  private List<String> keys = new ArrayList<String>();
  private Map<String, String> pairs = new HashMap<String, String>();
  private List<String> details = new ArrayList<String>();

  public List<String> getKeys() {
    return keys;
  }

  public void setKeys(List<String> keys) {
    this.keys = keys;
  }

  public Map<String, String> getPairs() {
    return pairs;
  }

  public void setPairs(Map<String, String> pairs) {
    this.pairs = pairs;
  }

  public String getValue(String key) {
    if (pairs == null)
      return null;
    else
      return pairs.get(key);
  }

  public List<String> getDetails() {
    return details;
  }

  public void setDetails(List<String> details) {
    this.details = details;
  }

}
