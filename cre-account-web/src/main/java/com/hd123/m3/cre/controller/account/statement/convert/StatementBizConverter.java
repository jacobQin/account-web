/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	StatementBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月5日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.convert;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.acc.Acc1;
import com.hd123.m3.account.service.statement.SettleState;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.m3.account.service.statement.StatementType;
import com.hd123.m3.cre.controller.account.statement.model.Accounts;
import com.hd123.m3.cre.controller.account.statement.model.BStatement;
import com.hd123.m3.cre.controller.account.statement.model.BStatementDetail;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author chenganbang
 *
 */
public class StatementBizConverter {
  private static StatementBizConverter instance = null;

  private StatementBizConverter() {
  }

  public static StatementBizConverter getInstance() {
    if (instance == null) {
      instance = new StatementBizConverter();
    }
    return instance;
  }

  public BStatement convert(BStatement source) {
    List<BStatementDetail> details = source.getDetails();
    if (StringUtil.isNullOrBlank(source.getUuid())) {// 新建
      source.setType(StatementType.patch);
      source.setSettleState(SettleState.initial);

      for (BStatementDetail detail : details) {
        // 过滤空白行
        if (detail.getSubject() != null) {
          source.getLines().add(detail2Line(null, detail, true, source.getType().name()));
        }
      }
    } else {// 编辑
      List<StatementLine> lines = source.getLines();
      List<StatementLine> result = new ArrayList<StatementLine>(lines.size());
      boolean had = false;
      for (BStatementDetail detail : details) {
        // 过滤空白行
        if (detail.getSubject() != null) {
          had = false;
          for (StatementLine line : lines) {
            if (line.getUuid().equals(detail.getUuid())) {
              result.add(detail2Line(line, detail, false, source.getType().name()));
              had = true;
              break;
            }
          }
          if (!had) {
            result.add(detail2Line(null, detail, false, source.getType().name()));
          }
        }
      }
      source.setLines(result);
    }

    return source;
  }

  private StatementLine detail2Line(StatementLine line, BStatementDetail detail, boolean isCreate,
      String type) {
    if (line == null) {
      line = new StatementLine();
      line.setAcc1(new Acc1());
      line.setFromStatement(true);
    }

    line.getAcc1().setSubject(detail.getSubject());
    line.getAcc1().setDirection(detail.getDirection());
    line.getAcc1().setTaxRate(detail.getTaxRate());
    line.getAcc1().setBeginTime(detail.getBeginDate());
    line.getAcc1().setEndTime(detail.getEndDate());
    if (isCreate || (StatementType.patch.name().equals(type))) {
      line.getAcc1().setAccountDate(detail.getBeginDate());
    } else {
      line.getAcc1().setAccountDate(detail.getAccountDate());
    }
    line.getAcc1().setSourceBill(detail.getSourceBill());

    line.getAcc2().setLastPayDate(detail.getLastPayDate());
    // 是否开发票
    if (detail.isIssueInvoice()) {
      line.getAcc2().setInvoice(Accounts.NONE_INVOICEBILL);
    } else {
      line.getAcc2().setInvoice(Accounts.DISABLE_INVOICEBILL);
    }
    line.setTotal(new Total(detail.getTotal(), detail.getTax()));
    line.setFreeTotal(detail.getFreeTotal());

    return line;
  }
}
