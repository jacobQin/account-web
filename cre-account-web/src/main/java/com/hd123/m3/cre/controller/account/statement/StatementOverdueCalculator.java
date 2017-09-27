/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	StatementOverdueCalculator.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月9日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.statement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.model.overdue.OverdueTerm;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.util.DateUtil;

/**
 * 账单预估滞纳金计算器
 * 
 * @author LiBin
 *
 */
@Component
public class StatementOverdueCalculator {

  @Autowired
  ContractService contractService;
  @Autowired
  AccountOptionComponent optionComponent;

  public void calculateOverdue(Statement statement) {
    statement.setEstimateOverdueTotal(BigDecimal.ZERO);
    for (StatementLine line : statement.getLines()) {
      line.setEstimateOverdueTotal(BigDecimal.ZERO);
    }

    if (statement.getEstimatePayDate() == null) {
      return;
    }

    List<String> contractIds = new ArrayList<String>();
    contractIds.add(statement.getContract().getUuid());

    Map<String, List<OverdueTerm>> overdueTerms = contractService.getOverdueTerms(contractIds);
    if (overdueTerms.isEmpty()) {
      return;
    }

    for (StatementLine line : statement.getLines()) {
      if (line.getAcc2().getLastPayDate() == null
          || line.getAcc2().getLastPayDate().compareTo(statement.getEstimatePayDate()) >= 0) {
        continue;
      }

      calculateLineOverdue(overdueTerms.get(statement.getContract().getUuid()), line,
          statement.getEstimatePayDate());

      statement.setEstimateOverdueTotal(statement.getEstimateOverdueTotal().add(
          line.getEstimateOverdueTotal()));
    }

  }

  private void calculateLineOverdue(List<OverdueTerm> overdueTerms, StatementLine line, Date payDate) {
    // 账款明细最后结算日为空跳过
    Date lastPayDate = line.getAcc2().getLastPayDate();
    int graceDay = line.getAcc2().getGraceDays();
    if (lastPayDate == null) {
      return;
    }
    // 在宽限日内跳过
    if (!payDate.after(DateUtil.addDay(lastPayDate, graceDay))) {
      return;
    }

    long overdueDays = (payDate.getTime() - lastPayDate.getTime()) / (24 * 60 * 60 * 1000);

    calculateOverdue(overdueTerms, line, overdueDays);
  }

  private void calculateOverdue(List<OverdueTerm> overdueTerms, StatementLine line, long overdueDays) {
    line.setEstimateOverdueTotal(BigDecimal.ZERO);

    if (overdueDays <= 0) {
      return;
    }

    BigDecimal totalOftotal = line.getTotal().getTotal();

    if (totalOftotal == null || totalOftotal.compareTo(BigDecimal.ZERO) <= 0) {
      return;
    }

    for (int i = 0; i < overdueTerms.size(); i++) {
      OverdueTerm overdueTerm = overdueTerms.get(i);

      if (overdueTerm.isAllSubjects() == false) {
        boolean find = false;
        for (UCN s : overdueTerm.getSubjects()) {
          if (line.getAcc1().getSubject().equals(s)) {
            find = true;
            break;
          }
        }
        if (find == false)
          continue;
      }

      // 计算该条滞纳金条款的滞纳金金额
      BigDecimal overdueTotal = totalOftotal.multiply(overdueTerm.getRate())
          .multiply(new BigDecimal(overdueDays)).setScale(getScale(), getRoundingMode());

      line.setEstimateOverdueTotal(line.getEstimateOverdueTotal().add(overdueTotal));
    }
  }

  private int getScale() {
    return optionComponent.getScale();
  }

  private RoundingMode getRoundingMode() {
    return optionComponent.getRoundingMode();
  }

}
