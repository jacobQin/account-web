/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	AccEpTitleModule.java
 * 模块说明：	
 * 修改历史：
 * 2015-10-29 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.base.client;

import java.util.HashMap;
import java.util.Map;

import com.hd123.m3.commons.gwt.base.client.M3EPTitleModule;
import com.hd123.m3.commons.gwt.base.client.M3Sessions;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;

/**
 * 不含有BPM流程的模块EP基础此类
 * 
 * @author huangjunxian
 * 
 */
public abstract class AccEPTitleModule extends M3EPTitleModule {
  private Map<String, String> counterpartTypeMap;
  private Map<String, String> captionMap;

  /**
   * 获取对方单位类型。
   * 
   * @return
   */
  public Map<String, String> getCounterpartTypeMap() {
    if (counterpartTypeMap == null) {
      String str = getModuleContext().get(AccSessions.KEY_COUNTERPART_TYPE);
      if (str == null) {
        counterpartTypeMap = new HashMap<String, String>();
      } else {
        counterpartTypeMap = CollectionUtil.toMap(str);
      }
    }
    return counterpartTypeMap;
  }

  public Map<String, String> getCaptionMap() {
    if (captionMap == null) {
      String str = getModuleContext().get(M3Sessions.KEY_FORM_CAPTION);
      if (str == null) {
        captionMap = new HashMap<String, String>();
      } else {
        captionMap = CollectionUtil.toMap(str);
      }
    }
    return captionMap;
  }
}
