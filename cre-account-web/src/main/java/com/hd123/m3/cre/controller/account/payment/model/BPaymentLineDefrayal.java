package com.hd123.m3.cre.controller.account.payment.model;

import java.math.BigDecimal;

import com.hd123.rumba.commons.biz.entity.Entity;

/**
 * 
 * 收付款单按科目收款时的收付明细
 * 
 * @author LiBin
 * 
 */
public class BPaymentLineDefrayal extends Entity {
  private static final long serialVersionUID = 8602509522613123994L;

  private int itemNo;
  private String lineUuid;
  private BigDecimal total = BigDecimal.ZERO;
  private String remark;

  public int getItemNo() {
    return itemNo;
  }

  public void setItemNo(int itemNo) {
    this.itemNo = itemNo;
  }

  public String getLineUuid() {
    return lineUuid;
  }

  public void setLineUuid(String lineUuid) {
    this.lineUuid = lineUuid;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
