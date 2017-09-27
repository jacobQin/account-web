/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BAdvanceData.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.biz;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuhairui
 * 
 */
public class BAdvanceData implements Serializable {
  private static final long serialVersionUID = 5470464527387641653L;

  private List<BAdvance> recAdvance = null;
  private List<BAdvance> payAdvance = null;

  public BAdvanceData() {
  }

  public BAdvanceData(List<BAdvance> recAdvance, List<BAdvance> payAdvance) {
    this.recAdvance = recAdvance;
    this.payAdvance = payAdvance;
  }

  public List<BAdvance> getRecAdvance() {
    return recAdvance;
  }

  public List<BAdvance> getPayAdvance() {
    return payAdvance;
  }

}
