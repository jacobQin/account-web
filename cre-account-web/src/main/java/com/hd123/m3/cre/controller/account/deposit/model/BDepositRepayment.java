/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BDepositRepayment.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月28日 - shikenian - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.model;

import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

public class BDepositRepayment extends DepositRepayment {

  private static final long serialVersionUID = 5141732546503053583L;

  /** 由于接口对象的经办人只提供了uuid存储，为了界面显示，需要处理该字段 */
  private UCN viewDealer;

  private static final Converter<DepositRepayment, BDepositRepayment> CONVETER = ConverterBuilder
      .newBuilder(DepositRepayment.class, BDepositRepayment.class).build();

  public static BDepositRepayment newInstance(DepositRepayment source) {
    return CONVETER.convert(source);
  }

  /** 经办人 */
  public UCN getViewDealer() {
    return viewDealer;
  }

  public void setViewDealer(UCN viewDealer) {
    this.viewDealer = viewDealer;
  }
}
