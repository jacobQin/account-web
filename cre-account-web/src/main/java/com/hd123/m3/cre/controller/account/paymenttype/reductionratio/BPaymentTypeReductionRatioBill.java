/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	PaymentTypeReductionRatioBill.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月14日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype.reductionratio;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatio;
import com.hd123.m3.commons.biz.entity.StandardBill;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * @author mengyinkun
 * 
 */
public class BPaymentTypeReductionRatioBill extends StandardBill {

  private static final long serialVersionUID = -3737136406053628062L;

  private UCN store;
  private UCN contract;

  private List<PaymentTypeReductionRatio> details = new ArrayList<PaymentTypeReductionRatio>();

  private String remark;

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

  /** 付款方式提成比例明细 */
  public List<PaymentTypeReductionRatio> getDetails() {
    return details;
  }

  public void setDetails(List<PaymentTypeReductionRatio> details) {
    this.details = details;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
