/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountSettleTool.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月17日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.settle;

import java.util.List;

import com.hd123.m3.account.service.contract.AccountSettle;

/**
 * @author mengyinkun
 * 
 */
public class AccountSettleTool {
  private String calculateId;
  private List<AccountSettle> accountSettles;
  private String processDefKey;
  private String permGroupId;
  private String permGroupTitle;

  /**
   * @return Returns the calculateId.
   */
  public String getCalculateId() {
    return calculateId;
  }

  /**
   * @param calculateId
   *          The calculateId to set.
   */
  public void setCalculateId(String calculateId) {
    this.calculateId = calculateId;
  }

  /**
   * @return Returns the accountSettles.
   */
  public List<AccountSettle> getAccountSettles() {
    return accountSettles;
  }

  /**
   * @param accountSettles
   *          The accountSettles to set.
   */
  public void setAccountSettles(List<AccountSettle> accountSettles) {
    this.accountSettles = accountSettles;
  }

  /**
   * @return Returns the processDefKey.
   */
  public String getProcessDefKey() {
    return processDefKey;
  }

  /**
   * @param processDefKey
   *          The processDefKey to set.
   */
  public void setProcessDefKey(String processDefKey) {
    this.processDefKey = processDefKey;
  }

  /**
   * @return Returns the permGroupId.
   */
  public String getPermGroupId() {
    return permGroupId;
  }

  /**
   * @param permGroupId
   *          The permGroupId to set.
   */
  public void setPermGroupId(String permGroupId) {
    this.permGroupId = permGroupId;
  }

  /**
   * @return Returns the permGroupTitle.
   */
  public String getPermGroupTitle() {
    return permGroupTitle;
  }

  /**
   * @param permGroupTitle
   *          The permGroupTitle to set.
   */
  public void setPermGroupTitle(String permGroupTitle) {
    this.permGroupTitle = permGroupTitle;
  }

}
