/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BRebateBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.rebate;

import com.hd123.m3.account.service.rebate.RebateBill;

/**
 * @author chenganbang
 *
 */
public class BRebateBill extends RebateBill {

  private static final long serialVersionUID = 2576859229369856439L;

  private BContract agreement;
  private boolean hasAccount = false;

  public BContract getAgreement() {
    return agreement;
  }

  public void setAgreement(BContract agreement) {
    this.agreement = agreement;
  }

  public boolean isHasAccount() {
    return hasAccount;
  }

  public void setHasAccount(boolean hasAccount) {
    this.hasAccount = hasAccount;
  }

}
