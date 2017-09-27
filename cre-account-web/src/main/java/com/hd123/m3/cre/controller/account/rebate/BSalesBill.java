/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BSalesBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.rebate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.rebate.RebateLine;
import com.hd123.m3.commons.biz.date.DateRange;
import com.hd123.m3.commons.biz.entity.Bill;

/**
 * @author chenganbang
 *
 */
public class BSalesBill extends Bill {

  private static final long serialVersionUID = 2862456996859979777L;

  private DateRange accountDate;
  private BigDecimal amount;
  private List<RebateLine> lines = new ArrayList<RebateLine>();

  public DateRange getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(DateRange accountDate) {
    this.accountDate = accountDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public List<RebateLine> getLines() {
    return lines;
  }

  public void setLines(List<RebateLine> lines) {
    this.lines = lines;
  }

}
