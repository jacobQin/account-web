/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	RebateBillBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.rebate;

import com.hd123.m3.account.service.rebate.RebateBill;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author chenganbang
 *
 */
public class RebateBillBizConverter {
  private static RebateBillBizConverter instance = null;

  private Converter<RebateBill, BRebateBill> converter = ConverterBuilder
      .newBuilder(RebateBill.class, BRebateBill.class).ignore("store").build();

  private RebateBillBizConverter() {
  }

  public static RebateBillBizConverter getInstance() {
    if (instance == null) {
      instance = new RebateBillBizConverter();
    }
    return instance;
  }

  public BRebateBill convert(RebateBill source) {
    BRebateBill target = converter.convert(source);
    target.setStore(source.getStore());
    return target;
  }
}
