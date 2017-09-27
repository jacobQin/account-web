/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BFeeDetail.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月30日 - lizongyi - 创建。
 */
package com.hd123.m3.cre.controller.account.fee;

import com.hd123.m3.account.service.fee.FeeDetail;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author lizongyi
 *
 */
public class BFeeDetail extends FeeDetail {

  private static final long serialVersionUID = 1547304134427034645L;

  private static final Converter<FeeDetail, BFeeDetail> CONVETER = ConverterBuilder.newBuilder(
      FeeDetail.class, BFeeDetail.class).build();

  public static BFeeDetail newInstance(FeeDetail source) {
    return CONVETER.convert(source);
  }

  private boolean locked;

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

}
