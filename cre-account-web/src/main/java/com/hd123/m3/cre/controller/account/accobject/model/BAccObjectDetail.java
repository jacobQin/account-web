/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BAccObjectDetail.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月9日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 核算主体明细
 * 
 * @author LiBin
 *
 */
public class BAccObjectDetail implements Serializable {

  private static final long serialVersionUID = -3719472889462071692L;

  private UCN store;
  private UCN contract;
  private UCN accObject;

  /** 类型：按项目 */
  public static final String BY_STORE = "byStore";
  /** 类型：按合同 */
  public static final String BY_CONTRACT = "byContract";

  private List<UCN> subjects = new ArrayList<UCN>();

  private String type;

  /** 项目 */
  public UCN getStore() {
    return store;
  }

  public void setStore(UCN store) {
    this.store = store;
  }

  /** 合同 */
  public UCN getContract() {
    return contract;
  }

  public void setContract(UCN contract) {
    this.contract = contract;
  }

  /** 核算主体 */
  public UCN getAccObject() {
    return accObject;
  }

  public void setAccObject(UCN accObject) {
    this.accObject = accObject;
  }

  /** 科目列表 */
  public List<UCN> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<UCN> subjects) {
    this.subjects = subjects;
  }

  /** 类型：分为“按项目设置”和“按合同设置” */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
