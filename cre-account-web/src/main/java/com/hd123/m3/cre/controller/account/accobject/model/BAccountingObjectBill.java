/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BAccountingObjectBill.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月5日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject.model;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.commons.biz.entity.StandardBill;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 核算主体单数据模型
 * 
 * @author LiBin
 *
 */
public class BAccountingObjectBill extends StandardBill {

  private static final long serialVersionUID = 2087719283593411099L;

  private UCN store;
  private String remark;

  private List<BAccObjectBillStoreLine> storeLines = new ArrayList<BAccObjectBillStoreLine>();
  private List<BAccObjectBillContractLine> contractLines = new ArrayList<BAccObjectBillContractLine>();

  /** 项目 */
  public UCN getStore() {
    return store;
  }

  public void setStore(UCN store) {
    this.store = store;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /** 按项目设置明细 */
  public List<BAccObjectBillStoreLine> getStoreLines() {
    return storeLines;
  }

  public void setStoreLines(List<BAccObjectBillStoreLine> storeLines) {
    this.storeLines = storeLines;
  }

  /** 按合同设置明细 */
  public List<BAccObjectBillContractLine> getContractLines() {
    return contractLines;
  }

  public void setContractLines(List<BAccObjectBillContractLine> contractLines) {
    this.contractLines = contractLines;
  }

}
