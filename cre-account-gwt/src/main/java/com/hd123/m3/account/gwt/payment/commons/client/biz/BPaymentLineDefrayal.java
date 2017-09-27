/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BPaymentLineDefrayal.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.biz;

import java.math.BigDecimal;

import com.hd123.rumba.commons.gwt.entity.client.BEntity;

/**
 * 
 * 收付款单按科目收款时的收付明细
 * 
 * @author subinzhu
 * 
 */
public class BPaymentLineDefrayal extends BEntity {
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
