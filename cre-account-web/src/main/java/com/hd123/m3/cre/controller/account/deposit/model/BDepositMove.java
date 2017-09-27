/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BDepositMove.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月11日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.model;

import java.math.BigDecimal;

import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author LiBin
 *
 */
public class BDepositMove extends DepositMove{
  
  private static final long serialVersionUID = 383357355127713422L;
  
  private static final Converter<DepositMove, BDepositMove> CONVETER = ConverterBuilder.newBuilder(DepositMove.class,
      BDepositMove.class).build();

  public static BDepositMove newInstance(DepositMove source) {
    return CONVETER.convert(source);
  }
  
  private BigDecimal outBalance;
  private BigDecimal inBalance;
  
  public BigDecimal getOutBalance() {
    return outBalance;
  }
  
  public void setOutBalance(BigDecimal outBalance) {
    this.outBalance = outBalance;
  }
  
  public BigDecimal getInBalance() {
    return inBalance;
  }
  
  public void setInBalance(BigDecimal inBalance) {
    this.inBalance = inBalance;
  }

}
