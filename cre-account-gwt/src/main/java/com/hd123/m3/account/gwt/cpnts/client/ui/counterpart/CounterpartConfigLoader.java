/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BCounterpart.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月7日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.counterpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合作伙伴配置读取器
 * 
 * @author chenrizhang
 *
 */
public class CounterpartConfigLoader {

  private static CounterpartConfigLoader instance = null;

  public static CounterpartConfigLoader getInstance() {
    if (instance == null)
      instance = new CounterpartConfigLoader();
    return instance;
  }

  private Map<String, String> captions = new HashMap<String, String>();

  public void initial() {
    if (captions.isEmpty() == false)
      return;
    
    captions.put("_Proprietor", "业主");
    captions.put("_Tenant", "商户");
  }

  public List<String> getCounterpartTypes() {
    List<String> counterpartTypes = new ArrayList<String>();
    counterpartTypes.addAll(captions.keySet());
    return counterpartTypes;
  }

  /** 取得对方单位类型标题 */
  public String getCaption(String counterpartType) {
    if (captions == null)
      return null;
    return captions.get(counterpartType);
  }

  /** 取得对方单位类型标题 */
  public String getCaption(String counterpartType, String defaultCaption) {
    String caption = getCaption(counterpartType);
    return caption == null ? defaultCaption : caption;
  }
}
