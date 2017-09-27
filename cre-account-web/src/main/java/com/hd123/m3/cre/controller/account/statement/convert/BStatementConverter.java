/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BStatementConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月14日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.convert;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hd123.bpm.service.util.DateRange;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementAccRange;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.cre.controller.account.statement.model.Accounts;
import com.hd123.m3.cre.controller.account.statement.model.BSettlement;
import com.hd123.m3.cre.controller.account.statement.model.BStatement;
import com.hd123.m3.cre.controller.account.statement.model.BStatementDetail;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author chenganbang
 * 
 */
public class BStatementConverter {
  private static BStatementConverter instance = null;

  private BStatementConverter() {
  }

  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  private static NumberFormat format = NumberFormat.getInstance();

  public static BStatementConverter getInstance() {
    if (instance == null) {
      instance = new BStatementConverter();
      format.setMinimumFractionDigits(2);
      format.setMaximumFractionDigits(2);
    }
    return instance;
  }

  /**
   * 转换
   * 
   * @param source
   * @param isLoad
   *          是否load()方法引起的转换
   * @return
   */
  public BStatement convert(Statement source, boolean isLoad) {
    BStatement target = new BStatement();
    target.inject(source);
    // 基本信息
    target.setAccountUnit(source.getAccountUnit());
    target.setContract(source.getContract());
    target.setCounterpart(source.getCounterpart());
    target.setCounterpartType(source.getCounterpartType());
    target.setSaleTotal(source.getSaleTotal());// 销售额
    target.setSendEmailCount(source.getSendEmailCount());

    // 应收信息
    target.setReceiptTotal(source.getReceiptTotal());
    target.setReceipted(source.getReceipted());
    target.setIvcReceiptTotal(source.getIvcReceiptTotal());
    target.setIvcReceipted(source.getIvcReceipted());
    // 应付信息
    target.setPayTotal(source.getPayTotal());
    target.setPayed(source.getPayed());
    target.setIvcPayTotal(source.getIvcPayTotal());
    target.setIvcPayed(source.getIvcPayed());
    // 账单信息
    target.setSettlement(buildSettlement(source.getRanges(), isLoad));
    target.setAccountTime(source.getAccountTime());
    target.setReceiptAccDate(source.getReceiptAccDate());

    target.setIvcReceiptAdj(source.getIvcReceiptAdj());
    target.setFreePayTotal(source.getFreePayTotal());
    target.setFreeReceiptTotal(source.getFreeReceiptTotal());
    target.setPayAdj(source.getPayAdj());
    target.setIvcPayAdj(source.getIvcPayAdj());
    target.setReceiptAdj(source.getReceiptAdj());

    target.setType(source.getType());
    target.setSettleState(source.getSettleState());
    target.setPlanDate(source.getPlanDate());
    target.setCoopMode(source.getCoopMode());
    target.setAccountType(source.getAccountType());
    target.setConfirmState(source.getConfirmState());
    if (BizStates.EFFECT.equals(source.getBizState())) {
      target.setStatementState("已生效" + source.getSettleState().getCaption());
    } else if (BizStates.INEFFECT.equals(source.getBizState())) {
      target.setStatementState("未生效");
    } else if (BizStates.ABORTED.equals(source.getBizState())) {
      target.setStatementState("已作废");
    }

    target.setOwedAmount(source.getReceiptTotal().getTotal().subtract(source.getReceipted())
        .subtract(source.getPayTotal().getTotal().subtract(source.getPayed())));

    target.setRanges(source.getRanges());
    target.setDesActiveLines(source.getDesActiveLines());
    target.setAttachs(source.getAttachs());

    // 用于页面展示的账款明细表格数据
    for (StatementLine line : source.getLines()) {
      UUID uuid = UUID.randomUUID();// 由于P对象没有进行uuid转换，自动生成uuid唯一标识
      line.setUuid(uuid.toString());
      target.getLines().add(line);

      BStatementDetail detail = lines2Details(line);
      detail.setStatementType(source.getType().name());
      target.getDetails().add(detail);
    }
    return target;
  }

  public BStatementDetail lines2Details(StatementLine line) {
    BStatementDetail detail = new BStatementDetail();
    detail.setUuid(line.getUuid());
    detail.setSubject(line.getAcc1().getSubject());
    detail.setDirection(line.getAcc1().getDirection());
    detail.setTaxRate(line.getAcc1().getTaxRate());
    detail.setTotal(line.getTotal().getTotal());
    detail.setTax(line.getTotal().getTax());
    detail.setBeginDate(line.getAcc1().getBeginTime());
    detail.setEndDate(line.getAcc1().getEndTime());
    detail.setAccountDate(line.getAcc1().getAccountDate());
    detail.setAccId(line.getAccId());
    if (line.getAcc2() == null) {
      detail.setIssueInvoice(false);
    } else if (line.getAcc2().getInvoice() == null) {
      detail.setIssueInvoice(false);
    } else if (Accounts.DISABLE_BILL_UUID.equals(line.getAcc2().getInvoice().getBillUuid())) {
      detail.setIssueInvoice(false);
    } else {
      detail.setIssueInvoice(true);
    }
    detail.setLastPayDate(line.getAcc2().getLastPayDate());
    detail.setFreeTotal(line.getFreeTotal());
    detail.setSourceBill(line.getAcc1().getSourceBill());
    detail.setFromStatement((line.getAcc1().getSourceBill() != null) && line.isFromStatement());
    return detail;
  }

  private List<BSettlement> buildSettlement(List<StatementAccRange> ranges, boolean isLoad) {
    List<BSettlement> result = new ArrayList<BSettlement>();

    for (StatementAccRange range : ranges) {
      BSettlement settlement = new BSettlement();
      DateRange dateRange = new DateRange(range.getDateRange().getBeginDate(), range.getDateRange()
          .getEndDate());
      // 为空表示为补录账单
      if (isLoad && StringUtil.isNullOrBlank(range.getCaption())) {
        settlement.setCaption("出账周期");
      } else {
        settlement.setCaption(range.getCaption());
      }
      settlement
          .setDateStr(sdf.format(dateRange.getStart()) + "~" + sdf.format(dateRange.getEnd()));

      result.add(settlement);
    }

    return result;
  }

}
