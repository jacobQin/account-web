/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BTotal.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - chenwenfeng - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author chenwenfeng
 * 
 */
public class BTotal implements Serializable {
  private static final long serialVersionUID = -3847952529023771059L;

  private BigDecimal total;
  private BigDecimal tax;

  public BTotal() {
  }

  public BTotal(BigDecimal total, BigDecimal tax) {
    this.total = total;
    this.tax = tax;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BTotal clone() {
    return new BTotal(total, tax);
  }

  /** 相加 */
  public BTotal add(BTotal augend) {
    if (augend == null)
      return this;

    BigDecimal total2 = total == null ? BigDecimal.ZERO : total;
    BigDecimal tax2 = tax == null ? BigDecimal.ZERO : tax;

    if (augend.getTotal() != null) {
      total2 = total2.add(augend.getTotal());
    }
    if (augend.getTax() != null) {
      tax2 = tax2.add(augend.getTax());
    }
    return new BTotal(total2, tax2);
  }

  /** 相减 */
  public BTotal subtract(BTotal subtrahend) {
    if (subtrahend == null)
      return this;

    BigDecimal total2 = total == null ? BigDecimal.ZERO : total;
    BigDecimal tax2 = tax == null ? BigDecimal.ZERO : tax;

    if (subtrahend.getTotal() != null)
      total2 = total2.subtract(subtrahend.getTotal());
    if (subtrahend.getTax() != null)
      tax2 = tax2.subtract(subtrahend.getTax());
    return new BTotal(total2, tax2);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tax == null) ? 0 : tax.hashCode());
    result = prime * result + ((total == null) ? 0 : total.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof BTotal))
      return false;

    BTotal other = (BTotal) obj;
    return isBigDecimalEquals(total, other.getTotal()) && isBigDecimalEquals(tax, other.getTax());
  }

  public static BTotal zero() {
    return new BTotal(BigDecimal.ZERO, BigDecimal.ZERO);
  }

  public static boolean isBigDecimalEquals(BigDecimal b1, BigDecimal b2) {
    if (b1 == null && b2 == null)
      return true;
    if (b1 == null || b2 == null)
      return false;
    return b1.compareTo(b2) == 0;
  }

  /** 取反 */
  public void negate() {
    setTotal(total.negate());
    setTax(tax.negate());
  }
}
