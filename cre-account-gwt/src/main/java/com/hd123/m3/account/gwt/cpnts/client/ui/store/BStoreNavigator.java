/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BRentalPlanStoreNavigator.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月11日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.store;

import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author chenrizhang
 *
 */
public class BStoreNavigator extends BUCN {
  private static final long serialVersionUID = -4894277961241533941L;
  private List<BStoreNavigator> childs = new ArrayList<BStoreNavigator>();

  /** 子节点 */
  public List<BStoreNavigator> getChilds() {
    return childs;
  }

  public void setChilds(List<BStoreNavigator> childs) {
    this.childs = childs;
  }
}
