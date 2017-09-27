/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BAccObjectBillContractLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月6日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject.model;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 按合同设置明细
 * 
 * @author LiBin
 *
 */
public class BAccObjectBillContractLine extends BAccObjectBillStoreLine {

  private static final long serialVersionUID = 4887350575724859328L;

  private UCN contract;

  /** 合同 */
  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

}
