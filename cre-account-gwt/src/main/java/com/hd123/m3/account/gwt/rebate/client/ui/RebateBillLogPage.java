/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui;

import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.intf.client.RebateBillUrlParams;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmLogPage2;

/**
 * 销售额返款单|日志界面
 * @author chenganbang
 */
public class RebateBillLogPage extends BaseBpmLogPage2<BRebateBill> {
  
  private static RebateBillLogPage instance = null;
  private RebateBillLogPage(){
    super();
  }
  
  public static RebateBillLogPage getInstance() {
    if (instance == null) {
      instance = new RebateBillLogPage();
    }
    return instance;
  }

  @Override
  public String getStartNode() {
    return RebateBillUrlParams.START_NODE_LOG;
  }

  @Override
  protected EPBpmModule2 getEP() {
    return EPRebateBill.getInstance();
  }
  
}
