/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	ReceiptCommonController.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月25日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.receipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.accobject.AccountingObjectBillService;
import com.hd123.m3.account.service.accobject.AccountingObjectLine;
import com.hd123.m3.account.service.accobject.AccountingObjectLines;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.QueryPagingUtil;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.BaseController;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.model.ReceiptOverdueDefault;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.m3.cre.controller.account.payment.model.BPayment;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueLine;
import com.hd123.m3.cre.controller.account.payment.model.ContractAdvance;
import com.hd123.m3.cre.controller.account.payment.model.DepositTotal;
import com.hd123.m3.cre.controller.account.payment.receipt.common.OverdueCalcHelper;
import com.hd123.m3.cre.controller.account.payment.receipt.common.ReceiptDefrayalApportionHelper;
import com.hd123.m3.cre.controller.account.payment.receipt.common.ReceiptUtil;
import com.hd123.m3.cre.controller.account.subject.SubjectQueryBuilder;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 收款单控制器（协助处理一些与模块本身无关的方法）
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/receiptCommon/*")
public class ReceiptCommonController extends BaseController {

  @Autowired
  ReceiptDefrayalApportionHelper apportionHelper;
  @Autowired
  OverdueCalcHelper overdueCalcHelper;
  @Autowired
  ReceiptCommonComponent commonComponent;
  @Autowired
  SubjectQueryBuilder subjectQueryBuilder;
  @Autowired
  AccountingObjectBillService accObjectService;
  @Autowired
  AccountOptionComponent accOptionComponent;
  @Autowired
  SubjectService subjectService;

  /** 按总额收款分摊计算 */
  @RequestMapping("apportion")
  public @ResponseBody BPayment apportion(@RequestBody BPayment payment) {
    payment.preProcess();
    ReceiptUtil.sortAccountLinesByBeginTime(payment);

    frontPriorityDeductionAccountLines(payment);

    BPayment bill = apportionHelper.apportion(payment, accOptionComponent.getScale(),
        accOptionComponent.getRoundingMode());

    ReceiptUtil.sortAccountLinesByBeginTime(bill);

    return calcOverdue(bill);
  }

  private void frontPriorityDeductionAccountLines(BPayment payment) {
    List<BPaymentAccountLine> accountLines = new ArrayList<BPaymentAccountLine>();
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      accountLines.add(line);
    }

    // 将优先冲扣的数据放在前面
    payment.getAccountLines().clear();
    for (BPaymentAccountLine line : accountLines) {
      if (line.isPriorityDeduction()) {
        payment.getAccountLines().add(line);
      }
    }

    for (BPaymentAccountLine line : accountLines) {
      if (line.isPriorityDeduction() == false) {
        payment.getAccountLines().add(line);
      }
    }
  }

  /** 滞纳金计算 */
  @RequestMapping("calcOverdue")
  public @ResponseBody BPayment calcOverdue(@RequestBody BPayment bill) {

    ReceiptUtil.sortAccountLinesByBeginTime(bill);

    // 先把明细行滞纳金修改过的保存下来
    Map<String, Total> overdueMap = new HashMap<String, Total>();
    for (BPaymentOverdueLine line : bill.getOverdueLines()) {
      if (line.getTotal() != null && line.getUnpayedTotal() != null) {
        if (line.getTotal().getTotal().compareTo(line.getUnpayedTotal().getTotal()) != 0
            && line.isCalculated()) {
          overdueMap.put(line.getAccountId(), line.getTotal());
        }
      }
    }

    BPayment result = overdueCalcHelper.calculate(bill);
    result.aggregate(accOptionComponent.getScale(), accOptionComponent.getRoundingMode());

    // 还原已修改的明细
    Total overdueTotal = Total.zero();
    ReceiptOverdueDefault overdueDefault = accOptionComponent.getReceiptOverdueDefault();
    for (BPaymentOverdueLine line : result.getOverdueLines()) {
      line.setCalculated(true);

      Total total = overdueMap.get(line.getAccountId());
      if (total != null) {
        line.setTotal(total);
      } else if (ReceiptOverdueDefault.calcValue.equals(overdueDefault)) {
        line.setTotal(line.getUnpayedTotal());
      }
      overdueTotal = overdueTotal.add(line.getTotal());
    }
    result.setOverdueTotal(overdueTotal);

    return result;
  }

  @RequestMapping("advancesubjects")
  public @ResponseBody List<UCN> getAdvanceSubjects(@RequestBody QueryFilter filter)
      throws Exception {
    Map<String, String> accObjectLimitParams = (Map<String, String>) filter.getFilter().get(
        "accObjectLimitParams");
    filter.getFilter().put("accObjectLimitParams", null);

    if (accObjectLimitParams != null && accOptionComponent.isAccObjectEnabled()) {
      filter.setPage(0);
      filter.setPageSize(0);
    }

    List<UCN> result = commonComponent.getAdvanceSubjects(filter);

    List<UCN> filterResult = CollectionUtil.filterByKeyword(result, filter.getKeyword());

    if (accObjectLimitParams == null || accOptionComponent.isAccObjectEnabled() == false) {
      return filterResult;
    }

    List<String> subjectUuids = new ArrayList<String>();
    for (UCN sb : filterResult) {
      subjectUuids.add(sb.getUuid());
    }

    List<String> filterSubjects = filterSubjectsByAccObject(subjectUuids, accObjectLimitParams);
    List<UCN> finalResult = new ArrayList<UCN>();
    for (UCN subject : filterResult) {
      for (String subjectUuid : filterSubjects) {
        if (subjectUuid.equals(subject.getUuid())) {
          finalResult.add(subject);
        }
      }
    }

    return finalResult;

  }

  /** 取得对方单位的预存款余额 */
  @RequestMapping(value = "loadRemainDepositTotal")
  public @ResponseBody String loadRemainDepositTotal(@RequestBody QueryFilter filter)
      throws Exception {
    Assert.assertArgumentNotNull(filter, "filter");

    String accountUnit = (String) filter.getFilter().get("accountUnit");
    String counterpart = (String) filter.getFilter().get("counterpart");

    if (StringUtil.isNullOrBlank(accountUnit) || StringUtil.isNullOrBlank(counterpart)) {
      return BigDecimal.ZERO.toString();
    }

    String billUuid = (String) filter.getFilter().get("billUuid");

    String subject = (String) filter.getFilter().get("subject");

    if (StringUtil.isNullOrBlank(billUuid) && StringUtil.isNullOrBlank(subject)) {
      return commonComponent.loadRemainDepositTotal(accountUnit, counterpart).toString();
    }

    return commonComponent
        .getDepositSubjectRemainTotal(accountUnit, counterpart, billUuid, subject).toString();

  }

  /** 取得对方单位的预存款余额 */
  @RequestMapping(value = "loadAdvances")
  public @ResponseBody DepositTotal loadAdvances(@RequestBody QueryFilter filter)
      throws M3ServiceException {
    Assert.assertArgumentNotNull(filter, "filter");

    String accountUnit = (String) filter.getFilter().get("accountUnit");
    String counterpart = (String) filter.getFilter().get("counterpart");

    if (StringUtil.isNullOrBlank(accountUnit) || StringUtil.isNullOrBlank(counterpart)) {
      return new DepositTotal();
    }

    List<Advance> advances = commonComponent.loadRemainDepositAdvances(accountUnit, counterpart);

    DepositTotal depositTotal = new DepositTotal();
    BigDecimal remainTotal = BigDecimal.ZERO;

    for (Advance advance : advances) {
      if (advance.getTotal().compareTo(BigDecimal.ZERO) == 0) {
        continue;
      }
      ContractAdvance contractAdvance = new ContractAdvance();
      contractAdvance.setContract(advance.getBill());
      contractAdvance.setTotal(advance.getTotal());
      depositTotal.getAdvances().add(contractAdvance);
      remainTotal = remainTotal.add(advance.getTotal());
    }
    depositTotal.setTotal(remainTotal);

    return depositTotal;

  }

  @RequestMapping("querySubjects")
  public @ResponseBody SummaryQueryResult<Subject> query(@RequestBody QueryFilter queryFilter) {
    if (queryFilter == null) {
      return new SummaryQueryResult<Subject>();
    }

    Map<String, String> accObjectLimitParams = (Map<String, String>) queryFilter.getFilter().get(
        "accObjectLimitParams");
    queryFilter.getFilter().put("accObjectLimitParams", null);

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    subjectQueryBuilder.build(queryDef, queryFilter);

    if (accObjectLimitParams != null && accOptionComponent.isAccObjectEnabled()) {
      queryDef.setPage(0);
      queryDef.setPageSize(0);
    }

    QueryResult<Subject> qr = subjectService.query(queryDef);

    if (accObjectLimitParams == null || accOptionComponent.isAccObjectEnabled() == false) {
      return SummaryQueryResult.newInstance(qr);
    }

    qr = filterSubjectsByAccObject(qr.getRecords(), queryFilter, accObjectLimitParams);

    return SummaryQueryResult.newInstance(qr);
  }

  private QueryResult<Subject> filterSubjectsByAccObject(List<Subject> subjects,
      QueryFilter queryFilter, Map<String, String> accObjectLimitParams) {
    // 源科目组为空直接返回
    if (subjects == null || subjects.isEmpty()) {
      return new QueryResult<Subject>();
    }

    List<String> subjectUuids = new ArrayList<String>();
    for (Subject sb : subjects) {
      subjectUuids.add(sb.getUuid());
    }

    List<String> filterSubjects = filterSubjectsByAccObject(subjectUuids, accObjectLimitParams);
    List<Subject> result = new ArrayList<Subject>();
    for (Subject subject : subjects) {
      for (String subjectUuid : filterSubjects) {
        if (subjectUuid.equals(subject.getUuid())) {
          result.add(subject);
        }
      }
    }

    return QueryPagingUtil.flip(result, queryFilter.getCurrentPage(), queryFilter.getPageSize());

  }

  private List<String> filterSubjectsByAccObject(List<String> subjectUuids,
      Map<String, String> accObjectLimitParams) {
    String storeUuid = accObjectLimitParams.get("storeUuid");
    String contractUuid = accObjectLimitParams.get("contractUuid");
    String subjectUuid = accObjectLimitParams.get("subjectUuid");

    if (storeUuid == null || contractUuid == null || subjectUuid == null) {
      return new ArrayList<String>();
    }

    subjectUuids.add(subjectUuid);

    // 仅查询当前项目下的数据
    FlecsQueryDefinition def = new FlecsQueryDefinition();
    def.addCondition(AccountingObjectLines.CONDITION_STORE_EQUALS, storeUuid);
    def.addCondition(AccountingObjectLines.CONDITION_SUBJECT_IN, subjectUuids.toArray());
    List<AccountingObjectLine> conSubObjs = accObjectService.queryAccObjects(def).getRecords();
    if (conSubObjs.isEmpty()) {
      return new ArrayList<String>();
    }

    /** 来源科目(subject)对应的核算主体列表（最多有两条记录，一条到当前合同，一条到当前项目没指定合同） */
    // 取出来源科目对应的核算主体（如果定义了合同级别核算主体用之，否则用项目级别）
    AccountingObjectLine sourceSubObjectLine = getSubObjBySubject(conSubObjs, subjectUuid,
        contractUuid);

    // 如果来源科目未定义项目级别核算主体，且也无该合同级别核算主体，则直接返回空。
    if (sourceSubObjectLine == null) {
      return new ArrayList<String>();
    }

    // 开始过滤科目
    List<String> result = new ArrayList<String>();
    for (String subUuid : subjectUuids) {
      // 取备选科目的核算主体
      AccountingObjectLine line = getSubObjBySubject(conSubObjs, subUuid, contractUuid);
      // 如果核算主体为空，弃之
      if (line == null) {
        continue;
      }

      // 如果与来源科目的核算主体相同，则表示该科目符合条件，否则弃之。
      if (line.getAccObject().equals(sourceSubObjectLine.getAccObject())) {
        result.add(subUuid);
      }
    }

    return result;
  }

  /** 取得指定项目-科目-合同的核算主体 */
  private AccountingObjectLine getSubObjBySubject(List<AccountingObjectLine> conSubObjs,
      String subjectUuid, String contractUuid) {

    // 优先取合同核算主体
    for (AccountingObjectLine line : conSubObjs) {
      if (line.getContract() == null) { // 合同为null的不考虑
        continue;
      }
      if (subjectUuid.equals(line.getSubject().getUuid())
          && line.getContract().getUuid().equals(contractUuid)) {
        return line;
      }
    }

    // 取项目
    for (AccountingObjectLine line : conSubObjs) {
      if (line.getContract() != null) { // 合同不为null的不考虑
        continue;
      }
      if (subjectUuid.equals(line.getSubject().getUuid())) {
        return line;
      }
    }

    return null;
  }
}