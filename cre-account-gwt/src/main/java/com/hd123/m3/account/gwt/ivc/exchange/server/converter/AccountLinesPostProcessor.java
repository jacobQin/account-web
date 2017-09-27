/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	AccountLinesPostProcessor.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月24日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.server.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine2;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountAdjLine;
import com.hd123.rumba.commons.lang.Assert;

/**
 * 账款明细后处理器
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class AccountLinesPostProcessor {

  /** 根据账款明细及调整明细构造界面显示的账款明细. */
  public static List<BInvoiceExchAccountLine> postProcessAccountLines(
      List<BInvoiceExchAccountLine2> exchAccountLines2,
      List<BInvoiceExchangeAccountAdjLine> accountAdjLines) {
    Assert.assertArgumentNotNull(exchAccountLines2, "exchAccountLines2");
    Assert.assertArgumentNotNull(accountAdjLines, "accountAdjLines");

    Map<AccountLineId, BInvoiceExchAccountLine> lines = new HashMap<AccountLineId, BInvoiceExchAccountLine>();

    for (BInvoiceExchAccountLine2 line2 : exchAccountLines2) {
      AccountLineId key = new AccountLineId(line2.getAcc1().getId(), line2.getAcc2().getInvoice()
          .getInvoiceNumber());
      BInvoiceExchAccountLine line = lines.get(key);
      if (line != null) {
        line.setAmount(line.getAmount().add(line2.getTotal().getTotal()));
      } else {
        BInvoiceExchAccountLine l = AccountLine2ToAccountLineConverter.getInstance().convert(line2);
        lines.put(key, l);
      }
    }

    for (BInvoiceExchangeAccountAdjLine adj : accountAdjLines) {
      AccountLineId key = new AccountLineId(adj.getAcc1().getId(), adj.getAcc2().getInvoice()
          .getInvoiceNumber());
      BInvoiceExchAccountLine line = lines.get(key);
      if (line != null) {
        line.setAmount(line.getAmount().add(adj.getTotal().getTotal()));
      } else {
        BInvoiceExchAccountLine l = BAccountAdjLineToBAccountLineConverter.getInstance().convert(
            adj);
        lines.put(key, l);
      }
    }

    List<BInvoiceExchAccountLine> targets = new ArrayList<BInvoiceExchAccountLine>();
    targets.addAll(lines.values());

    return targets;
  }

  /** 同步新发票属性（代码及号码），保存前调用 */
  public static void syncModifyInvoiceProerties(BInvoiceExchange entity) {
    for (BInvoiceExchAccountLine line : entity.getExchAccountLines()) {
      AccountLineId id1 = new AccountLineId(line.getAcc1().getId(), line.getAcc2().getInvoice()
          .getInvoiceNumber());
      for (BInvoiceExchAccountLine2 line2 : entity.getExchAccountLines2()) {
        AccountLineId id2 = new AccountLineId(line2.getAcc1().getId(), line2.getAcc2().getInvoice()
            .getInvoiceNumber());
        if (id1.equals(id2)) {
          line2.setNewCode(line.getNewCode());
          line2.setNewNumber(line.getNewNumber());
          line2.setNewType(line.getNewInvoiceType());
          line2.setRemark(line.getRemark());
        }
      }

      for (BInvoiceExchangeAccountAdjLine line2 : entity.getAccountAdjLines()) {
        AccountLineId id2 = new AccountLineId(line2.getAcc1().getId(), line2.getAcc2().getInvoice()
            .getInvoiceNumber());
        if (id1.equals(id2)) {
          line2.setNewCode(line.getNewCode());
          line2.setNewNumber(line.getNewNumber());
          line2.setNewType(line.getNewInvoiceType());
        }
      }
    }
  }

  /** 账款明细行标识 */
  private static class AccountLineId {
    private String acc1Id;
    private String acc2InvoiceNumber;

    public AccountLineId(String acc1Id, String acc2InvoiceNumber) {
      this.acc1Id = acc1Id;
      this.acc2InvoiceNumber = acc2InvoiceNumber;
    }

    public String getAcc1Id() {
      return acc1Id;
    }

    public String getAcc2InvoiceNumber() {
      return acc2InvoiceNumber;
    }

    @Override
    public int hashCode() {
      int temp = acc1Id.hashCode() + acc2InvoiceNumber.hashCode();
      return temp;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof AccountLineId == false) {
        return false;
      }

      if (acc1Id == null || acc2InvoiceNumber == null) {
        return false;
      }

      AccountLineId other = (AccountLineId) obj;
      return acc1Id.equals(other.getAcc1Id())
          && acc2InvoiceNumber.equals(other.getAcc2InvoiceNumber());
    }

  }
}
