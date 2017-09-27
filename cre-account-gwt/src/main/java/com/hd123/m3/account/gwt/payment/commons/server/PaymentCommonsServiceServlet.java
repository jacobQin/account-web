/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.ia.authen.service.user.User;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.account.gwt.acc.server.AccountBizConverter;
import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.BillTypeBizConverter;
import com.hd123.m3.account.gwt.commons.server.SourceBillBizConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.payment.commons.client.AdvanceSubjectFilter;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueTerm;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BRemainTotal;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.AccountDataFilter;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountInvoice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.account.gwt.settle.server.SettleUtil;
import com.hd123.m3.account.gwt.util.server.BillTypeUtils;
import com.hd123.m3.account.gwt.util.server.CollectionUtil;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.gwt.util.server.SubjectUsageUtils;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.bank.BankService;
import com.hd123.m3.account.service.bank.Banks;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.contract.model.overdue.OverdueTerm;
import com.hd123.m3.account.service.ivc.reg.InvoiceReg;
import com.hd123.m3.account.service.ivc.reg.InvoiceRegService;
import com.hd123.m3.account.service.ivc.reg.InvoiceRegs;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.paymentnotice.PaymentNotice;
import com.hd123.m3.account.service.paymentnotice.PaymentNoticeLine;
import com.hd123.m3.account.service.paymentnotice.PaymentNoticeService;
import com.hd123.m3.account.service.paymentnotice.PaymentNotices;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.account.service.paymenttype.PaymentTypes;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectType;
import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.m3.commons.biz.util.EntityUtil;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author subinzhu
 * 
 */
public class PaymentCommonsServiceServlet extends AccRemoteServiceServlet implements
    PaymentCommonsService {

  private static final long serialVersionUID = 200425140536913300L;

  @Override
  public List<BUCN> getSubjects(AdvanceSubjectFilter filter) throws ClientBizException {
    try {
      QueryDefinition queryDef = new QueryDefinition();

      queryDef
          .addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnitUuid());
      queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, filter.getCounterpartUuid());
      queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());
      queryDef.addCondition(Advances.CONDITION_SUBJECT_TYPE_EQUALS, SubjectType.predeposit.name());

      List<BSubjectUsage> usages = SubjectUsageUtils.getSubjectUsage(UsageType.creditDeposit);
      Set<String> usageCodes = new HashSet<String>();
      for (BSubjectUsage usage : usages) {
        usageCodes.add(usage.getCode());
      }
      if (usageCodes.isEmpty() == false)
        queryDef.addCondition(Advances.CONDITION_SUBJECTUSAGE_INCLUDE, usageCodes.toArray());

      QueryResult<Advance> qr = getAdvanceService().query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return Collections.emptyList();

      List<BUCN> results = new ArrayList<BUCN>();
      for (Advance adv : qr.getRecords()) {
        BUCN subject = new BUCN(adv.getSubject().getUuid(), adv.getSubject().getCode(), adv
            .getSubject().getName());
        if (results.contains(subject))
          continue;
        results.add(subject);
      }
      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /** 查询有效账款 */
  private QueryDefinition buildAccountQueryDef(AccountDataFilter filter) {
    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);
    if (filter.isIgnoreDirectionType() == false) {
      queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());
    }

    if (StringUtil.isNullOrBlank(filter.getAccountUnitUuid()) == false) {
      queryDef
          .addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnitUuid());
    } else {
      // 受项目限制
      List<String> list = getUserStores(getSessionUser().getId());
      if (list.isEmpty()) {
        return null;
      }
      queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_IN, list.toArray());
    }

    // 合同授权组限制
    if (permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT)
        || permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)) {
      List<String> userGroupIds = getUserGroups(getSessionUser().getId());
      QueryCondition condition = new QueryCondition(Accounts.CONDITION_CONTRACT_PERMGROUP_ID_IN,
          userGroupIds.toArray());
      condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
      queryDef.getConditions().add(condition);
    }

    if (filter.getAccountTime() != null) {
      queryDef.addCondition(Accounts.CONDITION_ACCOUNT_TIME_BETWEEN, filter.getAccountTime()
          .getBeginDate(), filter.getAccountTime().getEndDate());
    }
    if (StringUtil.isNullOrBlank(filter.getContract()) == false) {
      queryDef.addCondition(Accounts.CONDITION_CONTRACT_NUMBER_STARTWITH, filter.getContract());
    }
    if (StringUtil.isNullOrBlank(filter.getContractName()) == false) {
      queryDef.addCondition(Accounts.CONDITION_CONTRACT_NAME_LIKE, filter.getContractName());
    }
    if (StringUtil.isNullOrBlank(filter.getCounterpartUuid()) == false) {
      queryDef
          .addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS, filter.getCounterpartUuid());
    }
    if (StringUtil.isNullOrBlank(filter.getCounterpart()) == false) {
      queryDef.addCondition(Accounts.CONDITION_COUNTERPART_CODENAME_LIKE, filter.getCounterpart());
    }
    if (StringUtil.isNullOrBlank(filter.getCounterpartType()) == false) {
      queryDef
          .addCondition(Accounts.CONDITION_COUNTERPART_TYPE_EQUALS, filter.getCounterpartType());
    }
    if (StringUtil.isNullOrBlank(filter.getInvoiceCode()) == false) {
      queryDef.addCondition(Accounts.CONDITION_INVOICE_CODE_LIKE, filter.getInvoiceCode());
    }
    if (StringUtil.isNullOrBlank(filter.getInvoiceNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_INVOICE_NUMBER_LIKE, filter.getInvoiceNumber());
    }
    if (StringUtil.isNullOrBlank(filter.getInvoiceRegNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_INVOICE_BILLNUMBER_STARTWITH,
          filter.getInvoiceRegNumber());
    }
    // if (filter.getRegTime() != null) {// 目前有两个发票登记，不适用这个
    // queryDef.addCondition(Accounts.CONDITION_INVOICE_REGDATE_BETWEEN,
    // filter.getRegTime()
    // .getBeginDate(), filter.getRegTime().getEndDate());
    // }
    if (StringUtil.isNullOrBlank(filter.getPaymentNotice()) == false) {
      queryDef.addCondition(Accounts.CONDITION_NOTICE_NUMBER_STARTWITH, filter.getPaymentNotice());
    }
    if (StringUtil.isNullOrBlank(filter.getPaymentUuid()) == false) {
      queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, filter.getPaymentUuid());
      queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
      queryDef.addCondition(Accounts.CONDITION_PAYMENT_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    } else {
      queryDef.addCondition(Accounts.CONDITION_CAN_PAYMENT);
    }
    if (StringUtil.isNullOrBlank(filter.getStatement()) == false) {
      queryDef.addCondition(Accounts.CONDITION_STATEMENT_BILLNUMBER_STARTWITH,
          filter.getStatement());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillType()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS, filter.getSourceBillType());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_NUMBER_EQUALS,
          filter.getSourceBillNumber());
    }
    if (StringUtil.isNullOrBlank(filter.getSubject()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SUBJECT_UUID_EQUALS, filter.getSubject());
    }

    queryDef.setPage(0);
    queryDef.setPageSize(0);

    return queryDef;
  }

  @Override
  public RPageData<BAccount> queryAccount(AccountDataFilter filter) throws ClientBizException {
    try {

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      queryDef.addOrder(Accounts.ORDER_BY_SUBJECT, QueryOrderDirection.asc);
      QueryResult<Account> qr = getAccountService().query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      RPageData<BAccount> result = new RPageData<BAccount>();
      RPageDataUtil.copyPagingInfo(qr, result);

      result.setValues(ConverterUtil.convert(accounts, AccountBizConverter.getInstance()));

      decorateSubject(result.getValues());

      Order order = null;
      if (filter.getOrders() != null && filter.getOrders().size() > 0)
        order = filter.getOrders().get(0);
      else
        order = new Order(BAccount.ORDER_BY_FIELD_SUBJECT, OrderDir.asc);
      result.setValues(SettleUtil.sortAccountList(result.getValues(), order));
      result.setTotalCount(result.getValues().size());

      return RPageDataUtil.flip(result.getValues(), filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private List<Account> filterAccount(List<Account> list, AccountDataFilter filter) {

    negateAccountTotal(list, filter);

    List<BAccountId> ids = filter.getHasAddedAccIds();

    if (ids == null || ids.isEmpty())
      return list;

    for (int i = list.size() - 1; i >= 0; i--) {
      Account account = list.get(i);
      ArrayList<String> idList = new ArrayList<String>();
      idList.add(account.getAcc2().getStatement() == null ? BBill.NONE_ID : account.getAcc2()
          .getStatement().id());
      idList.add(account.getAcc2().getInvoice() == null ? BBill.NONE_ID : account.getAcc2()
          .getInvoice().id());
      String bizId = com.hd123.rumba.commons.collection.CollectionUtil.toString(idList);
      for (BAccountId id : ids) {
        if (id.getAccId().equals(account.getAcc1().getId()) && id.getBizId().equals(bizId)) {
          list.remove(i);
          break;
        }
      }
    }

    return list;
  }

  @Override
  public RPageData<BAccountStatement> queryAccountByStatement(AccountDataFilter filter)
      throws ClientBizException {
    try {

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_NOTIN, Accounts.NONE_BILL_UUID,
          Accounts.DISABLE_BILL_UUID);

      QueryResult<Account> qr = getAccountService().query(queryDef);
      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      RPageData<BAccountStatement> results = new RPageData<BAccountStatement>();
      RPageDataUtil.copyPagingInfo(qr, results);

      /** 根据账单进行分组 */
      Collection<List> groups = CollectionUtil.group(accounts, new Comparator<Account>() {
        @Override
        public int compare(Account o1, Account o2) {
          if (ObjectUtil.equals(o1.getAcc2().getStatement(), o2.getAcc2().getStatement()))
            return 0;
          else
            return 1;
        }
      });

      for (List<Account> group : groups) {
        BAccountStatement result = new BAccountStatement();
        result.setUuid(group.get(0).getAcc2().getStatement().getBillUuid());
        result.setBillNumber(group.get(0).getAcc2().getStatement().getBillNumber());
        result.setContract(UCNBizConverter.getInstance().convert(
            group.get(0).getAcc1().getContract()));
        result.setCounterpart(BCounterpartConverter.getInstance().convert(
            group.get(0).getAcc1().getCounterpart(), group.get(0).getAcc1().getCounterpartType()));

        result.setAccounts(ConverterUtil.convert(group, AccountBizConverter.getInstance()));

        result.setTotal(result.aggregateTotal());

        results.getValues().add(result);
      }

      decorateAccountStatement(filter.getAccountUnitUuid(), results.getValues());
      Order order = null;
      if (filter.getOrders() != null && filter.getOrders().size() > 0)
        order = filter.getOrders().get(0);
      else
        order = new Order(BAccountStatement.ORDER_BY_FIELD_BILLNUMBER, OrderDir.desc);
      results.setValues(SettleUtil.sortAccountStatementList(results.getValues(), order));
      results.setTotalCount(results.getValues().size());

      return RPageDataUtil.flip(results.getValues(), filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /** 将total的total取反 */
  private void negateAccountTotal(List<Account> accounts, AccountDataFilter filter) {
    if (filter.isIgnoreDirectionType()) {
      for (Account acc : accounts) { // 针对收款单，为付的账款金额需要取反。
        if (filter.getDirectionType() != acc.getAcc1().getDirection()) {
          acc.getOriginTotal().setTotal(acc.getOriginTotal().getTotal().negate());
          acc.getOriginTotal().setTax(acc.getOriginTotal().getTax().negate());
          acc.getTotal().setTotal(acc.getTotal().getTotal().negate());
          acc.getTotal().setTax(acc.getTotal().getTax().negate());
        }
      }
    }
  }

  /**
   * 从账单取出出账日期和账单周期。
   * 
   * @param accountUnitUuid
   * 
   * @param list
   * @throws IllegalArgumentException
   *           @
   */
  private void decorateAccountStatement(String accountUnitUuid, List<BAccountStatement> list) {
    List<String> statementUuids = toStatementUuids(list);

    if (statementUuids == null || statementUuids.isEmpty())
      return;

    List<Statement> statements = getStatementByUuids(accountUnitUuid, statementUuids);

    if (statements == null || statements.isEmpty())
      return;

    for (BAccountStatement source : list) {
      for (Statement statement : statements) {
        if (source.getAccounts().get(0).getAcc2().getStatement().getBillUuid()
            .equals(statement.getUuid())) {
          source.setAccountTime(statement.getAccountTime());
          source.setSettleNo(statement.getSettleNo());
        }
      }
    }
  }

  /**
   * 查询科目
   * 
   * @param list
   * @throws Exception
   */
  private void decorateSubject(List<BAccount> list) throws Exception {
    Set<String> subjectUuids = new HashSet<String>();
    for (BAccount a : list) {
      subjectUuids.add(a.getAcc1().getSubject().getUuid());
    }

    FlecsQueryDefinition def = new FlecsQueryDefinition();
    def.addCondition(Subjects.CONDITION_UUID_IN, subjectUuids.toArray());
    def.setPage(0);
    def.setPageSize(0);
    QueryResult<Subject> qr = getSubjectService().query(def, Subject.PART_USAGE);

    Map<String, String> usageMap = new HashMap<String, String>();
    for (Subject s : qr.getRecords()) {
      if (s.getUsages().isEmpty()) {
        continue;
      }

      BSubjectUsage usage = SubjectUsageUtils.getSubjectUsage(s.getUsages().get(0));
      if (usage == null) {
        continue;
      }
      usageMap.put(s.getUuid(), usage.getName());
    }

    // 更新科目用途
    for (BAccount a : list) {
      a.getAcc1().setUsageName(usageMap.get(a.getAcc1().getSubject().getUuid()));
    }
  }

  private List<String> toStatementUuids(List<BAccountStatement> list) {
    if (list == null || list.isEmpty())
      return Collections.emptyList();

    List<String> statementUuids = new ArrayList<String>();
    for (BAccountStatement source : list) {
      if (statementUuids.contains(source.getUuid()))
        continue;
      else
        statementUuids.add(source.getUuid());
    }
    return statementUuids;
  }

  /**
   * 批量取得账单。
   * 
   * @param accountUnitUuid
   * 
   * @param statementUuids
   *          账单uuis集合
   * @return 账单
   * @throws IllegalArgumentException
   *           @
   */
  private List<Statement> getStatementByUuids(String accountUnitUuid, List<String> statementUuids) {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    if (!StringUtil.isNullOrBlank(accountUnitUuid))
      queryDef.addCondition(Statements.CONDITION_ACCOUNTUNIT_EQUALS, accountUnitUuid);

    queryDef.addCondition(Statements.CONDITION_UUID_IN, statementUuids.toArray());

    QueryResult<Statement> qr = getStatementService().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return Collections.emptyList();

    return qr.getRecords();
  }

  @Override
  public RPageData<BAccountSourceBill> queryAccountByBill(AccountDataFilter filter)
      throws ClientBizException {
    try {

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      List<String> billTypes = new ArrayList<String>();
      for (BBillType type : BillTypeUtils.getAll()) {
        billTypes.add(type.getName());
      }
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_IN, billTypes.toArray());

      queryDef.addOrder(Accounts.ORDER_BY_SUBJECT, QueryOrderDirection.asc);

      QueryResult<Account> qr = getAccountService().query(queryDef);
      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      /** 根据来源单据进行分组 */
      Collection<List> groups = CollectionUtil.group(accounts, new Comparator<Account>() {

        @Override
        public int compare(Account o1, Account o2) {
          if (ObjectUtil.equals(o1.getAcc1().getSourceBill(), o2.getAcc1().getSourceBill()))
            return 0;
          else
            return 1;
        }
      });

      RPageData<BAccountSourceBill> results = new RPageData<BAccountSourceBill>();
      RPageDataUtil.copyPagingInfo(qr, results);

      for (List<Account> group : groups) {
        BAccountSourceBill result = new BAccountSourceBill();
        result.setSourceBill(SourceBillBizConverter.getInstance().convert(
            group.get(0).getAcc1().getSourceBill()));
        result.setContract(UCNBizConverter.getInstance().convert(
            group.get(0).getAcc1().getContract()));
        result.setCounterpart(BCounterpartConverter.getInstance().convert(
            group.get(0).getAcc1().getCounterpart(), group.get(0).getAcc1().getCounterpartType()));

        result.setAccounts(ConverterUtil.convert(group, AccountBizConverter.getInstance()));

        result.setTotal(result.aggregateTotal());

        results.getValues().add(result);
      }
      Order order = null;
      if (filter.getOrders() != null && filter.getOrders().size() > 0)
        order = filter.getOrders().get(0);
      else
        order = new Order(BAccountSourceBill.ORDER_BY_FIELD_SOURCEBILLTYPE, OrderDir.asc);
      results.setValues(SettleUtil.sortAccountSourceBillList(results.getValues(), order));
      results.setTotalCount(results.getValues().size());

      return RPageDataUtil.flip(results.getValues(), filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BAccountNotice> queryAccountByPaymentNotice(AccountDataFilter filter)
      throws ClientBizException {
    try {

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;
      // 用于跟收付款通知单关联
      queryDef.addCondition(Accounts.CONDIRION_NOTICE);

      QueryResult<Account> qr = getAccountService().query(queryDef);
      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      List<BAccountNotice> notices = decorateAccount(accounts);

      if (notices == null || notices.isEmpty())
        return null;

      RPageData<BAccountNotice> results = new RPageData<BAccountNotice>();
      RPageDataUtil.copyPagingInfo(qr, results);

      results.setValues(notices);

      Order order = null;
      if (filter.getOrders() != null && filter.getOrders().size() > 0)
        order = filter.getOrders().get(0);
      else
        order = new Order(BAccountNotice.ORDER_BY_FIELD_BILLNUMBER, OrderDir.desc);
      results.setValues(SettleUtil.sortAccountNoticeList(results.getValues(), order));
      results.setTotalCount(results.getValues().size());

      return RPageDataUtil.flip(results.getValues(), filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private List<BAccountNotice> decorateAccount(List<Account> accounts) {
    List<String> statementUuids = new ArrayList<String>();

    for (Account account : accounts) {
      if (statementUuids.contains(account.getAcc2().getStatement().getBillUuid()))
        continue;
      else
        statementUuids.add(account.getAcc2().getStatement().getBillUuid());
    }

    if (statementUuids.isEmpty())
      return Collections.emptyList();

    List<PaymentNotice> notices = queryNotices(statementUuids);

    if (notices == null || notices.isEmpty())
      return Collections.emptyList();

    List<BAccountNotice> results = new ArrayList<BAccountNotice>();

    for (PaymentNotice notice : notices) {
      BAccountNotice result = new BAccountNotice();
      result.setUuid(notice.getUuid());
      result.setBillNumber(notice.getBillNumber());
      for (PaymentNoticeLine line : notice.getLines()) {
        for (Account account : accounts) {
          if (ObjectUtil.equals(account.getAcc2().getStatement(), line.getStatement())) {
            result.getAccounts().add(AccountBizConverter.getInstance().convert(account));
          }
        }
      }
      result.setTotal(result.aggregateTotal());
      results.add(result);
    }
    return results;
  }

  /**
   * 根据账单uuid批量取得收付款通知单。
   * 
   * @param statementUuids
   *          账单的uuid集合
   * @return 收付款通知单集合
   * @throws IllegalArgumentException
   *           @
   */
  private List<PaymentNotice> queryNotices(List<String> statementUuids) {

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    queryDef.addCondition(PaymentNotices.CONDITION_STATEMENT_UUID_IN, statementUuids.toArray());
    queryDef.addCondition(PaymentNotices.CONDITION_BIZSTATE_EQUALS, BizStates.EFFECT);
    queryDef.getFetchParts().add(PaymentNotices.Fetch_PART_LINES);
    QueryResult<PaymentNotice> qr = getPaymentNotice().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return Collections.emptyList();

    return qr.getRecords();

  }

  @Override
  public RPageData<BAccountInvoice> queryAccountByInvoiceReg(AccountDataFilter filter)
      throws ClientBizException {
    try {

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      List<String> invoiceUuids = new ArrayList<String>();
      invoiceUuids.add(Accounts.NONE_BILL_UUID);
      invoiceUuids.add(Accounts.DISABLE_BILL_UUID);
      queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_NOTIN, invoiceUuids.toArray());

      QueryResult<Account> qr = getAccountService().query(queryDef);
      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      /** 根据发票登记单进行分组 */
      Collection<List> groups = CollectionUtil.group(accounts, new Comparator<Account>() {
        @Override
        public int compare(Account o1, Account o2) {
          if (ObjectUtil.equals(o1.getAcc2().getInvoice(), o2.getAcc2().getInvoice()))
            return 0;
          else
            return 1;
        }
      });

      List<BAccountInvoice> values = new ArrayList<BAccountInvoice>();
      for (List<Account> group : groups) {
        BAccountInvoice result = new BAccountInvoice();
        result.setUuid(group.get(0).getAcc2().getInvoice().getBillUuid());
        result.setBillNumber(group.get(0).getAcc2().getInvoice().getBillNumber());
        result.setAccounts(ConverterUtil.convert(group, AccountBizConverter.getInstance()));
        result.setCounterpart(BCounterpartConverter.getInstance().convert(
            group.get(0).getAcc1().getCounterpart(), group.get(0).getAcc1().getCounterpartType()));
        result.setTotal(result.aggregateTotal());
        Set<String> invoiceCodes = new HashSet<String>();
        Set<String> invoiceNumbers = new HashSet<String>();
        for (Account account : group) {
          invoiceCodes.add(account.getAcc2().getInvoice().getInvoiceCode());
          invoiceNumbers.add(account.getAcc2().getInvoice().getInvoiceNumber());
        }
        result.setInvoiceCode(com.hd123.rumba.commons.collection.CollectionUtil
            .toString(invoiceCodes));
        result.setInvoiceNumber(com.hd123.rumba.commons.collection.CollectionUtil
            .toString(invoiceNumbers));
        values.add(result);
      }

      decorateAccountInvoiceReg(values, filter.getRegTime());

      Order order = null;
      if (filter.getOrders() != null && filter.getOrders().size() > 0)
        order = filter.getOrders().get(0);
      else
        order = new Order(BAccountInvoice.ORDER_BY_FIELD_BILLNUMBER, OrderDir.desc);
      values = SettleUtil.sortAccountInvoiceList(values, order);

      return RPageDataUtil.flip(values, filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /**
   * 从发票登记单取出结算中心、发票代码和登记日期。
   * 
   * @param values
   * @param regRange
   *          开票日期，不为空时会对发票登记进行限制。
   */
  private void decorateAccountInvoiceReg(List<BAccountInvoice> list, BDateRange regRange) {
    List<String> invoiceRegUuids = toInvoiceRegUuids(list);

    if (invoiceRegUuids == null || invoiceRegUuids.isEmpty())
      return;

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(InvoiceRegs.CONDITION_UUID_IN, invoiceRegUuids.toArray());
    List<InvoiceReg> list1 = getInvoiceRegService().query(queryDef).getRecords();
    if (list1 != null && list1.isEmpty() == false) {
      Map<String, InvoiceReg> map = EntityUtil.toMap(list1);
      for (BAccountInvoice source : list) {
        if (map.containsKey(source.getUuid())) {
          InvoiceReg invoiceReg = map.get(source.getUuid());
          if (regRange != null && regRange.include(invoiceReg.getRegDate()) == false) {
            continue;
          }
          source.setCounterpart(BCounterpartConverter.getInstance().convert(
              invoiceReg.getCounterpart(), invoiceReg.getCounterpartType()));
          source.setInvoiceRegDate(invoiceReg.getRegDate());
        }
      }
    }

    queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(com.hd123.m3.account.service.invoice.InvoiceRegs.CONDITION_UUID_IN,
        invoiceRegUuids.toArray());
    List<com.hd123.m3.account.service.invoice.InvoiceReg> list2 = getInvoiceRegService2().query(
        queryDef).getRecords();
    if (list2 != null && list2.isEmpty() == false) {
      Map<String, com.hd123.m3.account.service.invoice.InvoiceReg> map = EntityUtil.toMap(list2);
      for (BAccountInvoice source : list) {
        if (map.containsKey(source.getUuid())) {
          com.hd123.m3.account.service.invoice.InvoiceReg invoiceReg = map.get(source.getUuid());
          if (regRange != null && regRange.include(invoiceReg.getRegDate()) == false) {
            continue;
          }
          source.setCounterpart(BCounterpartConverter.getInstance().convert(
              invoiceReg.getCounterpart(), invoiceReg.getCounterpartType()));
          source.setInvoiceRegDate(invoiceReg.getRegDate());
        }
      }
    }

    Iterator<BAccountInvoice> iterator = list.iterator();
    while (iterator.hasNext()) {
      BAccountInvoice value = iterator.next();
      if (value.getInvoiceRegDate() == null)// 发票日期为空的过滤掉
        iterator.remove();
    }
  }

  private List<String> toInvoiceRegUuids(List<BAccountInvoice> list) {
    if (list == null || list.isEmpty())
      return Collections.emptyList();

    List<String> invoiceRegUuids = new ArrayList<String>();
    for (BAccountInvoice source : list) {
      if (invoiceRegUuids.contains(source.getUuid()))
        continue;
      else
        invoiceRegUuids.add(source.getUuid());
    }
    return invoiceRegUuids;
  }

  @Override
  public Map<BAccount, List<BPaymentOverdueTerm>> getOverdueTerms(int direction,
      Collection<BAccount> accounts) throws ClientBizException {
    try {

      Map<BAccount, List<BPaymentOverdueTerm>> terms = new HashMap<BAccount, List<BPaymentOverdueTerm>>();

      if (accounts.isEmpty()) {
        return terms;
      }

      for (BAccount account : accounts) {
        terms.put(account, new ArrayList<BPaymentOverdueTerm>());
      }

      List<String> contractIds = new ArrayList<String>();
      for (BAccount result : accounts) {
        if (!contractIds.contains(result.getAcc1().getContract().getUuid()))
          contractIds.add(result.getAcc1().getContract().getUuid());
      }

      Map<String, List<OverdueTerm>> source = getContractService().getOverdueTerms(contractIds);
      if (source.isEmpty()) {
        return terms;
      }

      for (BAccount account : accounts) {
        List<OverdueTerm> termList = source.get(account.getAcc1().getContract().getUuid());
        if (termList == null || termList.isEmpty())
          continue;

        List<BPaymentOverdueTerm> bterms = new ArrayList<BPaymentOverdueTerm>();
        for (OverdueTerm term : termList) {
          if (term.getDirection() != direction)
            continue;
          if (term.isAllSubjects() == false
              && existsTermSubject(term.getSubjects(), account.getAcc1().getSubject().getUuid()) == false)
            continue;

          BPaymentOverdueTerm paymentTerm = new BPaymentOverdueTerm();
          paymentTerm.setContract(account.getAcc1().getContract());
          paymentTerm.setDirection(term.getDirection());
          paymentTerm.setInvoice(term.isInvoice());
          paymentTerm.setRate(term.getRate());
          paymentTerm.setSubject(UCNBizConverter.getInstance().convert(term.getOverdueSubject()));
          paymentTerm.setTaxRate(TaxRateBizConverter.getInstance().convert(term.getTaxRate()));
          paymentTerm.setAllSubjects(term.isAllSubjects());

          List<BUCN> subjects = new ArrayList<BUCN>();
          for (UCN subject : term.getSubjects()) {
            subjects.add(UCNBizConverter.getInstance().convert(subject));
          }
          paymentTerm.setSubjects(subjects);

          bterms.add(paymentTerm);
        }

        if (bterms.isEmpty() == false) {
          terms.get(account).addAll(bterms);
        }
      }

      return terms;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private boolean existsTermSubject(List<UCN> termSubjects, String subjectUuid) {
    for (UCN termSubject : termSubjects) {
      if (termSubject.getUuid().equals(subjectUuid))
        return true;
    }
    return false;
  }

  @Override
  public BigDecimal getDepositSubjectRemainTotal(String accountUnit, String counterpart,
      String billUuid, String subject) throws ClientBizException {
    try {

      Advance advance = getAdvanceService().get(accountUnit, counterpart, billUuid, subject);

      return advance == null ? BigDecimal.ZERO : advance.getTotal();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BRemainTotal> getDepositSubjectRemainTotals(String accountUnit, String counterpart,
      List<BRemainTotal> remainTotalIds) throws ClientBizException {
    try {

      for (BRemainTotal t : remainTotalIds) {
        BigDecimal remainTotal = getDepositSubjectRemainTotal(accountUnit, counterpart,
            t.getContract(), t.getSubject());
        t.setRemainTotal(remainTotal);
      }

      return remainTotalIds;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BigDecimal getDepositCounterpartRemainTotal(String accountUnit, String counterpart)
      throws ClientBizException {
    if (StringUtil.isNullOrBlank(counterpart) || StringUtil.isNullOrBlank(counterpart)) {
      return BigDecimal.ZERO;
    }

    QueryDefinition def = new QueryDefinition();
    def.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnit);
    def.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpart);
    def.addCondition(Advances.CONDITION_DIRECTION_EQUALS, DirectionType.receipt.getDirectionValue());

    QueryResult<Advance> advances = getAdvanceService().query(def);

    AdvanceSubjectFilter subjectFilter = new AdvanceSubjectFilter();
    subjectFilter.setAccountUnitUuid(accountUnit);
    subjectFilter.setCounterpartUuid(counterpart);
    subjectFilter.setDirectionType(DirectionType.receipt.getDirectionValue());

    List<BUCN> subjects = getSubjects(subjectFilter);

    BigDecimal remainTotal = BigDecimal.ZERO;
    for (Advance advance : advances.getRecords()) {
      if (advance.getTotal() == null
          || Advances.NONE_BILL_NUMBER.equals(advance.getBill().getCode())) {
        continue;
      }

      if (isPredeposit(advance.getSubject(), subjects)) {
        remainTotal = remainTotal.add(advance.getTotal());
      }

    }

    return remainTotal;
  }

  private boolean isPredeposit(UCN sourceSubject, List<BUCN> predepositSubjects) {
    if (sourceSubject == null || predepositSubjects == null || predepositSubjects.isEmpty()) {
      return false;
    }

    for (BUCN subject : predepositSubjects) {
      if (sourceSubject.getUuid().equals(subject.getUuid())) {
        return true;
      }
    }

    return false;
  }

  @Override
  public List<BUCN> getContracts(String businessUnitUuid, String counterPartUuid) throws Exception {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING);
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_EQUALS, businessUnitUuid);
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS, counterPartUuid);

      QueryResult<Contract> qr = getContractService().query(queryDef);
      if (qr == null || qr.getRecords().isEmpty())
        return Collections.emptyList();

      List<BUCN> results = new ArrayList<BUCN>();
      for (Contract contract : qr.getRecords()) {
        results.add(new BUCN(contract.getUuid(), contract.getBillNumber(), contract.getTitle()));
      }
      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /** 加载来源单据类型 */
  @Override
  public List<BBillType> getBillTypes(int direction) {
    List<BBillType> result = new ArrayList<BBillType>();
    List<BillType> types = getBillTypeService().getAccountTypes();
    for (BillType type : types) {
      if (direction == 0 || type.getDirection() == 0 || type.getDirection() == direction) {
        result.add(BillTypeBizConverter.getInstance().convert(type));
      }
    }
    return result;
  }

  @Override
  public BDefaultOption getDefaultOption() throws Exception {
    try {
      BDefaultOption defaultOption = new BDefaultOption();

      TaxRate taxRate = JsonUtil.jsonToObject(
          getAccountOptionService().getOption(AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_TAXRATE),
          TaxRate.class);
      defaultOption.setTaxRate(TaxRateBizConverter.getInstance().convert(taxRate));
      defaultOption.setPaymentType(getPaymentType(getAccountOptionService().getOption(
          AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_PAYMENT)));
      defaultOption.setPrePaySubject(getSubject(getAccountOptionService().getOption(
          AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_PREPAY_SUBJECT)));

      return defaultOption;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private BUCN getPaymentType(String uuid) throws ClientBizException {
    if (StringUtil.isNullOrBlank(uuid))
      return null;

    PaymentType paymentType = getPaymentTypeService().get(uuid);
    if (paymentType == null)
      return null;
    BUCN result = new BUCN();
    result.setUuid(paymentType.getUuid());
    result.setCode(paymentType.getCode());
    result.setName(paymentType.getName());

    return result;
  }

  private BUCN getSubject(String uuid) throws Exception {
    if (StringUtil.isNullOrBlank(uuid))
      return null;

    Subject subject = getSubjectService().get(uuid);
    if (subject == null)
      return null;

    if (SubjectType.predeposit.equals(subject.getType()) == false)
      return null;

    List<BSubjectUsage> usages = SubjectUsageUtils.getSubjectUsage(UsageType.creditDeposit);
    Set<String> usageCodes = new HashSet<String>();
    for (BSubjectUsage usage : usages) {
      usageCodes.add(usage.getCode());
    }

    boolean find = false;
    for (String usageCode : subject.getUsages()) {
      if (usageCodes.contains(usageCode)) {
        find = true;
        break;
      }
    }

    if (find == false) {
      return null;
    }

    BUCN result = new BUCN();
    result.setUuid(subject.getUuid());
    result.setCode(subject.getCode());
    result.setName(subject.getName());

    return result;
  }

  @Override
  public List<BUCN> getPaymentTypes() throws Exception {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addFlecsCondition(PaymentTypes.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
      List<PaymentType> paymentTypes = getPaymentTypeService().query(def).getRecords();
       
      
      if (paymentTypes == null || paymentTypes.isEmpty())
        return new ArrayList<BUCN>();

      List<BUCN> payments = new ArrayList<BUCN>();
      for (PaymentType paymentType : paymentTypes) {
        BUCN result = new BUCN(paymentType.getUuid(), paymentType.getCode(), paymentType.getName());
        payments.add(result);
      }

      return payments;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BBank> getBanks() throws Exception {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addFlecsCondition(Banks.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
      List<Bank> banks = getBankService().query(def).getRecords();
       
      if (banks == null || banks.isEmpty())
        return new ArrayList<BBank>();

      List<BBank> results = new ArrayList<BBank>();
      for (Bank bank : banks) {
        BBank result = new BBank();
        result.setCode(bank.getCode());
        result.setName(bank.getName());
        result.setStore(bank.getStore() == null ? null : new BUCN(bank.getStore().getUuid(), bank
            .getStore().getCode(), bank.getStore().getName()));
        result.setBankAccount(bank.getAccount());
        results.add(result);
      }

      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BUCN getCurrentEmployee(String userId) throws Exception {
    try {
      User user = getUserService().get(userId);
      return new BUCN(user.getUuid(), user.getId(), user.getFullName());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public Map<String, String> getInvoiceTypeMap() throws Exception {
    try {
      List<BInvoiceType> invoiceTypes = InvoiceTypeUtils.getAll();

      Map<String, String> invoiceTypeMap = new HashMap<String, String>();

      if (invoiceTypes == null || invoiceTypes.isEmpty())
        return invoiceTypeMap;

      for (BInvoiceType invoiceType : invoiceTypes) {
        invoiceTypeMap.put(invoiceType.getCode(), invoiceType.getName());
      }
      return invoiceTypeMap;

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private AccountService getAccountService() {
    return M3ServiceFactory.getService(AccountService.class);
  }

  private StatementService getStatementService() {
    return M3ServiceFactory.getService(StatementService.class);
  }

  private PaymentNoticeService getPaymentNotice() {
    return M3ServiceFactory.getService(PaymentNoticeService.class);
  }

  private InvoiceRegService getInvoiceRegService() {
    return M3ServiceFactory.getService(InvoiceRegService.class);
  }

  private com.hd123.m3.account.service.invoice.InvoiceRegService getInvoiceRegService2() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.invoice.InvoiceRegService.class);
  }

  private AdvanceService getAdvanceService() {
    return M3ServiceFactory.getService(AdvanceService.class);
  }

  private AccountOptionService getAccountOptionService() {
    return M3ServiceFactory.getService(AccountOptionService.class);
  }

  private PaymentTypeService getPaymentTypeService() throws ClientBizException {
    return M3ServiceFactory.getService(PaymentTypeService.class);
  }

  private BankService getBankService() throws ClientBizException {
    return M3ServiceFactory.getService(BankService.class);
  }

  private SubjectService getSubjectService() throws ClientBizException {
    return M3ServiceFactory.getService(SubjectService.class);
  }

  private BillTypeService getBillTypeService() {
    return getAppCtx().getBean(BillTypeService.class);
  }

  @Override
  public String getObjectName() {
    return Payment.class.getSimpleName();
  }

}
