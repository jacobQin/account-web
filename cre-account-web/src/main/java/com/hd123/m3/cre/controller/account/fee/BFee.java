/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	BFee.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月14日 - lizongyi - 创建。
 */
package com.hd123.m3.cre.controller.account.fee;

import java.math.BigDecimal;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.fee.Fee;
import com.hd123.m3.account.service.fee.FeeLine;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterBuilder;

/**
 * @author lizongyi
 *
 */
public class BFee extends Fee {

  private static final long serialVersionUID = -4789146978512823332L;

  private Contract contractEntity;
  private boolean hasAccounts = false;

  private static final Converter<Fee, BFee> CONVETER = ConverterBuilder.newBuilder(Fee.class,
      BFee.class).build();

  public static BFee newInstance(Fee source) {
    return CONVETER.convert(source);
  }

  public Contract getContractEntity() {
    return contractEntity;
  }

  public void setContractEntity(Contract contractEntity) {
    this.contractEntity = contractEntity;
  }

  public boolean isHasAccounts() {
    return hasAccounts;
  }

  public void setHasAccounts(boolean hasAccounts) {
    this.hasAccounts = hasAccounts;
  }

  public void resetTotal() {
    setTotal(new Total(BigDecimal.ZERO, BigDecimal.ZERO));
    if (getLines() == null || getLines().isEmpty()) {
      return;
    }

    for (FeeLine line : getLines()) {
      if (line.getTotal() == null) {
        continue;
      }
      if (line.getTotal().getTotal() != null) {
        getTotal().setTotal(getTotal().getTotal().add(line.getTotal().getTotal()));
      }
      if (line.getTotal().getTax() != null) {
        getTotal().setTax(getTotal().getTax().add(line.getTotal().getTax()));
      }
    }

  }
}
