/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BInternalFee.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.m3.account.gwt.commons.client.biz.BAccStandardBill;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.commons.gwt.base.client.biz.HasStore;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * 内部费用单
 * 
 * @author liuguilin
 * 
 */
public class BInternalFee extends BAccStandardBill implements HasStore {

  private static final long serialVersionUID = -5119800076435759854L;

  private BUCN store;
  private BUCN vendor;
  private Date beginDate;
  private Date endDate;
  private Date accountDate;
  private int direction = DirectionType.payment.getDirectionValue();
  private BTotal total = BTotal.zero();

  private List<BInternalFeeLine> lines = new ArrayList<BInternalFeeLine>();

  public BUCN getVendor() {
    return vendor;
  }

  public void setVendor(BUCN vendor) {
    this.vendor = vendor;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(Date accountDate) {
    this.accountDate = accountDate;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public BTotal getTotal() {
    return total;
  }

  public void setTotal(BTotal total) {
    this.total = total;
  }

  public List<BInternalFeeLine> getLines() {
    return lines;
  }

  public void setLines(List<BInternalFeeLine> lines) {
    this.lines = lines;
  }

  public void setStore(BUCN store) {
    this.store = store;
  }

  @Override
  public BUCN getStore() {
    return store;
  }

  @Override
  public String toString() {
    return this.getBillNumber();
  }

  /** 合计金额/税额。 */
  public void aggregate() {
    BigDecimal localTotal = BigDecimal.ZERO;
    BigDecimal tax = BigDecimal.ZERO;

    for (BInternalFeeLine line : getLines()) {
      localTotal = localTotal
          .add(line.getTotal() == null || line.getTotal().getTotal() == null ? BigDecimal.ZERO
              : line.getTotal().getTotal());
      tax = tax.add(line.getTotal() == null || line.getTotal().getTax() == null ? BigDecimal.ZERO
          : line.getTotal().getTax());
    }
    this.getTotal().setTotal(localTotal);
    this.getTotal().setTax(tax);
  }

}
