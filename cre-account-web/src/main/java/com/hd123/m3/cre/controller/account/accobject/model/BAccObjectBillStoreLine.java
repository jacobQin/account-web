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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 按项目设置明细
 * 
 * @author LiBin
 *
 */
public class BAccObjectBillStoreLine implements Serializable {

  private static final long serialVersionUID = 3340001560501287236L;

  private UCN accObject;
  private List<UCN> subjects = new ArrayList<UCN>();

  /**核算主体*/
  public UCN getAccObject() {
    return accObject;
  }

  public void setAccObject(UCN accObject) {
    this.accObject = accObject;
  }
  
  /**科目列表*/
  public List<UCN> getSubjects() {
    return subjects;
  }
  
  public void setSubjects(List<UCN> subjects) {
    this.subjects = subjects;
  }

}
