/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	ReceiptCommonComponent.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月11日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.receipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.payment.PaymentService;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.account.service.subject.SubjectType;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.util.CommonUtil;
import com.hd123.m3.cre.controller.account.common.util.SubjectUsageUtils;
import com.hd123.m3.cre.controller.account.payment.common.PaymentConstants;
import com.hd123.m3.cre.controller.account.payment.receipt.common.ReceiptOption;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 收款单通用组件，处理与模块业务无关的方法。
 * 
 * @author LiBin
 *
 */
@Component
public class ReceiptCommonComponent {

  private static Logger logger = LoggerFactory.getLogger(ReceiptCommonComponent.class);

  @Autowired
  PaymentTypeService paymentTypeService;
  @Autowired
  PaymentService paymentService;
  @Autowired
  AccountOptionComponent accountOptionComponent;
  @Autowired
  AdvanceService advanceService;

  /** 取得收款单相关配置项 */
  public ReceiptOption getOpion() {

    ReceiptOption option = new ReceiptOption();

    try {
      // 长短款付款方式
      option
          .setReceiptPaymentType(UCN.newInstance(accountOptionComponent.getShortLongPaymentType()));
      // 长短款付款方式限额
      option.setReceiptPaymentTypeLimit(accountOptionComponent.getShortLongPayLimit());
      // 银行是否必填
      option.setBankRequired(accountOptionComponent.isReceiptBankRequired());
      // 默认收款方式
      option.setDefrayalType(accountOptionComponent.getDefaultDefrayalType());
      // 默认预存款科目
      option.setDepositSubject(UCN.newInstance(accountOptionComponent.getDefaultPreSubject()));
      // 默认财务收款方式
      option.setDefaultPaymentType(accountOptionComponent.getDefaltPaymentType());
      // 收款金额默认等于应收金额
      option.setReceiptEqualsUnpayed(accountOptionComponent.isReceiptEqualsUnpayed());
      // 是否启用核算
      option.setAccObjectEnabled(accountOptionComponent.isAccObjectEnabled());

    } catch (Exception e) {
      logger.error("加载收款单配置项失败", e);
    }

    return option;
  }

  /**
   * 根据付款方式代码取得指定的付款方式
   * 
   * @param code
   *          付款方式代码，为空将导致返回空
   * @return 找不到将返回空
   * @throws Exception
   */
  public UCN getPaymentTypeByCode(String code) throws Exception {
    try {
      if (StringUtil.isNullOrBlank(code)) {
        return null;
      }
      PaymentType paymentType = paymentTypeService.getByCode(code);

      if (paymentType == null || paymentType.isEnabled() == false) {
        return null;
      }

      return UCN.newInstance(paymentType);

    } catch (Exception e) {
      logger.error("查询付款方式失败", e);
      throw e;
    }
  }

  /**
   * 取得指定对方单位的预存款余额
   * 
   * @param accountUnit
   *          结算中心uuid，禁止为null。
   * @param counterpart
   *          对方单位uuid，禁止为null。
   * @param billUuid
   *          单据uuid，禁止为null，通常传入合同的uuid或者“-”(表示不限制合同)
   * @param subject
   *          科目uuid，禁止为null。
   * 
   * @return 至少返回BigDecimal.ZERO
   * @throws M3ServiceException
   */
  public BigDecimal getDepositSubjectRemainTotal(String accountUnit, String counterpart,
      String billUuid, String subject) throws M3ServiceException {
    try {
      Advance advance = advanceService.get(accountUnit, counterpart, billUuid, subject);

      return advance == null ? BigDecimal.ZERO : advance.getTotal();
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
  }

  public BigDecimal loadRemainDepositTotal(String accountUnit, String counterpart)
      throws M3ServiceException {

    List<Advance> advances = loadRemainDepositAdvances(accountUnit, counterpart);

    BigDecimal remainTotal = BigDecimal.ZERO;
    for (Advance advance : advances) {
      remainTotal = remainTotal.add(advance.getTotal());
    }

    return remainTotal;
  }

  public List<Advance> loadRemainDepositAdvances(String accountUnit, String counterpart)
      throws M3ServiceException {

    if (StringUtil.isNullOrBlank(accountUnit) || StringUtil.isNullOrBlank(counterpart)) {
      return new ArrayList<Advance>();
    }

    QueryDefinition def = new QueryDefinition();
    def.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnit);
    def.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpart);
    def.addCondition(Advances.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);

    QueryResult<Advance> advances = advanceService.query(def);

    List<UCN> subjects = getSubjects(accountUnit, counterpart);

    List<Advance> result = new ArrayList<Advance>();
    for (Advance advance : advances.getRecords()) {
      if (advance.getTotal() == null
          || Advances.NONE_BILL_NUMBER.equals(advance.getBill().getCode())) {
        continue;
      }

      if (CommonUtil.isContains(subjects, advance.getSubject())) {
        result.add(advance);
      }

    }

    return result;
  }

  public List<UCN> getSubjects(String accountUnit, String counterpart) throws M3ServiceException {
    try {
      QueryDefinition queryDef = new QueryDefinition();

      queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnit);
      queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpart);
      queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);
      queryDef.addCondition(Advances.CONDITION_SUBJECT_TYPE_EQUALS, SubjectType.predeposit.name());

      List<SubjectUsage> usages = SubjectUsageUtils.getSubjectUsage(UsageType.creditDeposit);
      Set<String> usageCodes = new HashSet<String>();
      for (SubjectUsage usage : usages) {
        usageCodes.add(usage.getCode());
      }
      if (usageCodes.isEmpty() == false)
        queryDef.addCondition(Advances.CONDITION_SUBJECTUSAGE_INCLUDE, usageCodes.toArray());

      QueryResult<Advance> qr = advanceService.query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return Collections.emptyList();

      List<UCN> results = new ArrayList<UCN>();
      for (Advance adv : qr.getRecords()) {
        if (results.contains(adv.getSubject())) {
          continue;
        }
        results.add(adv.getSubject());
      }
      return results;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  /***
   * 查询预存款科目
   * 
   * @param filter
   *          查询条件，为空将返回空列表。
   * @throws Exception
   */
  public List<UCN> getAdvanceSubjects(QueryFilter filter) throws Exception {
    if (filter == null || filter.getFilter() == null) {
      return Collections.emptyList();
    }

    QueryDefinition def = buildAdvanceDef(filter);

    QueryResult<Advance> qr = advanceService.query(def);

    if (qr == null || qr.getRecords().isEmpty()) {
      return Collections.emptyList();
    }

    List<UCN> results = new ArrayList<UCN>();
    for (Advance adv : qr.getRecords()) {
      if (results.contains(adv.getSubject())
          || Advances.NONE_BILL.getUuid().equals(adv.getBill().getUuid())) {
        continue;
      }
      results.add(adv.getSubject());
    }
    return results;
  }

  // 构造预存款查询条件
  private QueryDefinition buildAdvanceDef(QueryFilter filter) throws Exception {
    QueryDefinition queryDef = new QueryDefinition();

    queryDef.addCondition(Advances.CONDITION_SUBJECT_TYPE_EQUALS, SubjectType.predeposit.name());

    List<SubjectUsage> usages = SubjectUsageUtils.getSubjectUsage(UsageType.creditDeposit);
    Set<String> usageCodes = new HashSet<String>();
    for (SubjectUsage usage : usages) {
      usageCodes.add(usage.getCode());
    }
    if (usageCodes.isEmpty() == false) {
      queryDef.addCondition(Advances.CONDITION_SUBJECTUSAGE_INCLUDE, usageCodes.toArray());
    }

    String storeUuid = (String) filter.getFilter().get(PaymentConstants.FILTER_STORE_UUID);
    if (StringUtil.isNullOrBlank(storeUuid) == false) {
      queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, storeUuid);
    }
    String counterpartUuid = (String) filter.getFilter().get(
        PaymentConstants.FILTER_COUNTERPART_UUID);
    if (StringUtil.isNullOrBlank(counterpartUuid) == false) {
      queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpartUuid);
    }

    String direction = (String) filter.getFilter().get(PaymentConstants.FILTER_DIRECTION);
    if (StringUtil.isNullOrBlank(counterpartUuid) == false) {
      queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, Integer.valueOf(direction));
    }

    return queryDef;
  }

}
