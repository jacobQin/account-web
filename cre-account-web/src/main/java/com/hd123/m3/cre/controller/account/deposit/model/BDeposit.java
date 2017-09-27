/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BDeposit.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月7日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.model;

import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * 预存(付)款单界面模型
 * 
 * @author LiBin
 *
 */
public class BDeposit extends Deposit {

  private static final long serialVersionUID = 808214417914948994L;

  /** 由于接口对象的经办人只提供了uuid存储，为了界面显示，需要处理该字段 */
  private UCN viewDealer;
  
  private static final Converter<Deposit, BDeposit> CONVETER = ConverterBuilder.newBuilder(Deposit.class,
      BDeposit.class).build();

  public static BDeposit newInstance(Deposit source) {
    return CONVETER.convert(source);
  }

  
  /**经办人*/
  public UCN getViewDealer() {
    return viewDealer;
  }

  public void setViewDealer(UCN viewDealer) {
    this.viewDealer = viewDealer;
  }
}
