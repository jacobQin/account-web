/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	ReceiptCommonController.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月25日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.bank.BankService;
import com.hd123.m3.account.service.bank.Banks;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.model.overdue.OverdueTerm;
import com.hd123.m3.account.service.payment.PaymentOverdueTerm;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.QueryPagingUtil;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.controllers.BaseController;
import com.hd123.m3.commons.servlet.permed.DataPermedHelper;
import com.hd123.m3.cre.controller.account.common.AccountDataFilter;
import com.hd123.m3.cre.controller.account.common.CommonConstants;
import com.hd123.m3.cre.controller.account.common.model.AccountId;
import com.hd123.m3.cre.controller.account.common.model.BAccount;
import com.hd123.m3.cre.controller.account.common.model.BAccountSourceBill;
import com.hd123.m3.cre.controller.account.common.model.BAccountStatement;
import com.hd123.m3.cre.controller.account.common.util.BillTypeUtils;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.m3.cre.controller.account.common.util.CommonUtil;
import com.hd123.m3.cre.controller.account.common.util.SettleUtil;
import com.hd123.m3.cre.controller.account.common.util.SubjectUsageUtils;
import com.hd123.m3.investment.service.contract.contract.Contracts;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.util.DateUtil;

/**
 * 提供给收付款单使用
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/payment/common/*")
public class CommonController extends BaseController {

  public static final String NONE_ID = "-";

  @Autowired
  protected AccountService accountService;
  @Autowired
  protected StatementService statementService;
  @Autowired
  protected SubjectService subjectService;
  @Autowired
  protected AdvanceService advanceService;
  @Autowired
  protected ContractService contractService;
  @Autowired
  protected BankService bankService;

  @RequestMapping("queryAccountByStatement")
  public @ResponseBody QueryResult<BAccountStatement> queryAccountByStatement(
      @RequestBody AccountDataFilter filter) throws Exception {
    try {
      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null) {
        return null;
      }

      queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_NOTIN, Accounts.NONE_BILL_UUID,
          Accounts.DISABLE_BILL_UUID);

      QueryResult<Account> qr = accountService.query(queryDef);
      if (qr == null || qr.getRecords().isEmpty()) {
        return null;
      }

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      List<BAccountStatement> accountStatements = new ArrayList<BAccountStatement>();
      /** 根据账单进行分组 */
      Collection<List> groups = CollectionUtil.group(accounts, new Comparator<Account>() {
        @Override
        public int compare(Account o1, Account o2) {
          if (ObjectUtils.equals(o1.getAcc2().getStatement(), o2.getAcc2().getStatement())) {
            return 0;
          } else {
            return 1;
          }
        }
      });

      for (List<Account> group : groups) {
        BAccountStatement result = new BAccountStatement();
        result.setAccountUnit(group.get(0).getAcc1().getAccountUnit());
        result.setUuid(group.get(0).getAcc2().getStatement().getBillUuid());
        result.setBillNumber(group.get(0).getAcc2().getStatement().getBillNumber());
        result.setContract(group.get(0).getAcc1().getContract());
        result.setCounterpart(group.get(0).getAcc1().getCounterpart());
        result.setCounterpartType(group.get(0).getAcc1().getCounterpartType());

        List<BAccount> baccounts = new ArrayList<BAccount>();
        for (Account account : group) {
          baccounts.add(BAccount.newInstance(account));
        }
        result.setAccounts(baccounts);

        result.setTotal(result.aggregateTotal());

        accountStatements.add(result);
      }

      decorateAccountStatement(filter.getAccountUnitUuid(), accountStatements);

      // 排序
      OrderSort defaultsort = getDefaultSort(BAccountStatement.ORDER_BY_FIELD_BILLNUMBER,
          OrderSort.DESC);
      List<BAccountStatement> sortAccStatements = SettleUtil.sortAccountStatementList(
          accountStatements, getOrder(filter, defaultsort));

      // 分页
      QueryResult<BAccountStatement> statements = QueryPagingUtil.flip(sortAccStatements,
          filter.getCurrentPage(), filter.getPageSize());

      for (BAccountStatement statement : statements.getRecords()) {
        // 对收款单来说,0当作“收”处理,付款单反之
        if (filter.getDirectionType() == 1) {
          if (BAccountStatement.getAccsDirection(statement.getAccounts()) == -1) {
            statement.setDirection("付");
          } else {
            statement.setDirection("收");
          }
        } else {
          if (BAccountStatement.getAccsDirection(statement.getAccounts()) == 1) {
            statement.setDirection("收");
          } else {
            statement.setDirection("付");
          }
        }

      }

      if (filter.getDirectionType() == Direction.PAYMENT) {
        return statements;
      }

      Set<String> subjects = new HashSet<String>();
      for (BAccountStatement bAccountStatement : sortAccStatements) {
        for (BAccount account : bAccountStatement.getAccounts()) {
          subjects.add(account.getAcc1().getSubject().getUuid());
        }
      }

      List<Bank> banks = getBanks(filter.getAccountUnitUuid(), subjects);

      for (BAccountStatement bAccountStatement : sortAccStatements) {
        for (BAccount account : bAccountStatement.getAccounts()) {
          Bank bank = getBank(banks, account.getAcc1().getSubject().getUuid());
          if (bank != null) {
            account.setBank(new UCN(bank.getUuid(), bank.getCode(), bank.getName()));
          }
        }
      }

      return statements;

    } catch (Exception e) {
      LOGGER.error("根据账单查询账款出错", e);
      throw e;
    }
  }

  private List<Bank> getBanks(String accountUnit, Set<String> subjects) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Banks.CONDITION_STORE_EQUALS, accountUnit);
    definition.addCondition(Banks.FIELD_ENABLED);
    definition.addCondition(Banks.CONDITION_CONTAINS_SUBJECT, subjects.toArray());
    definition.getFetchParts().add("subjects");
    return bankService.query(definition).getRecords();
  }

  private Bank getBank(List<Bank> banks, String subjectUuid) {
    for (Bank bank : banks) {
      if (bank.getSubjects() != null) {
        for (UCN subject : bank.getSubjects()) {
          if (subject.getUuid().equals(subjectUuid)) {
            return bank;
          }
        }
      }
    }

    return null;
  }

  @RequestMapping("queryAccount")
  public @ResponseBody QueryResult<BAccount> queryAccount(@RequestBody AccountDataFilter filter)
      throws Exception {
    try {
      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      queryDef.addOrder(Accounts.ORDER_BY_SUBJECT, QueryOrderDirection.asc);
      QueryResult<Account> qr = accountService.query(queryDef);

      if (qr == null || qr.getRecords().isEmpty()) {
        return null;
      }

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      // 排序
      OrderSort defaultsort = getDefaultSort(CommonConstants.ORDER_BY_FIELD_SUBJECT, OrderSort.ASC);
      List<Account> sortAccounts = SettleUtil.sortAccountList(accounts,
          getOrder(filter, defaultsort));

      List<BAccount> records = decorateAccount(sortAccounts);

      QueryResult<BAccount> queryResult = QueryPagingUtil.flip(records, filter.getCurrentPage(),
          filter.getPageSize());

      for (BAccount account : queryResult.getRecords()) {
        account.setDirection(Direction.getName(account.getAcc1().getDirection()));
        account.setSubjectCode(account.getAcc1().getSubject().getCode());
      }

      if (filter.getDirectionType() == Direction.PAYMENT) {
        return queryResult;
      }

      Set<String> subjects = new HashSet<String>();
      for (BAccount bAccount : queryResult.getRecords()) {
        subjects.add(bAccount.getAcc1().getSubject().getUuid());
      }

      List<Bank> banks = getBanks(filter.getAccountUnitUuid(), subjects);

      for (BAccount bAccount : queryResult.getRecords()) {
        getBank(banks, bAccount.getAcc1().getSubject().getUuid());
      }
      return queryResult;
    } catch (Exception e) {
      throw e;
    }
  }

  @RequestMapping("queryAccountByBill")
  public @ResponseBody QueryResult<BAccountSourceBill> queryAccountByBill(
      @RequestBody AccountDataFilter filter) throws Exception {
    try {
      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null) {
        return null;
      }

      List<String> billTypes = new ArrayList<String>();
      for (BillType type : BillTypeUtils.getAll()) {
        billTypes.add(type.getName());
      }
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_IN, billTypes.toArray());

      queryDef.addOrder(Accounts.ORDER_BY_SOURCEBILLNUMBER, QueryOrderDirection.desc);

      QueryResult<Account> qr = accountService.query(queryDef);
      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      /** 根据来源单据进行分组 */
      Collection<List> groups = CollectionUtil.group(accounts, new Comparator<Account>() {

        @Override
        public int compare(Account o1, Account o2) {
          if (ObjectUtil.equals(o1.getAcc1().getSourceBill(), o2.getAcc1().getSourceBill())) {
            return 0;
          } else {
            return 1;
          }
        }
      });

      List<BAccountSourceBill> results = new ArrayList<BAccountSourceBill>();

      for (List<Account> group : groups) {
        BAccountSourceBill result = new BAccountSourceBill();
        result.setSourceBill(group.get(0).getAcc1().getSourceBill());
        result.setContract(group.get(0).getAcc1().getContract());
        result.setCounterpart(group.get(0).getAcc1().getCounterpart());
        result.setCounterpartType(group.get(0).getAcc1().getCounterpartType());

        List<BAccount> baccounts = new ArrayList<BAccount>();
        for (Account account : group) {
          baccounts.add(BAccount.newInstance(account));
        }
        result.setAccounts(baccounts);

        result.setTotal(result.aggregateTotal());

        results.add(result);
      }

      // 排序
      OrderSort defaultsort = getDefaultSort(BAccountSourceBill.ORDER_BY_FIELD_SOURCEBILLTYPE,
          OrderSort.ASC);
      List<BAccountSourceBill> sortAccSrcBills = SettleUtil.sortAccountSourceBillList(results,
          getOrder(filter, defaultsort));

      decorateAccountSourceBill(sortAccSrcBills, filter.getDirectionType());

      // 分页
      QueryResult<BAccountSourceBill> queryResult = QueryPagingUtil.flip(sortAccSrcBills,
          filter.getCurrentPage(), filter.getPageSize());

      if (filter.getDirectionType() == Direction.PAYMENT) {
        return queryResult;
      }

      Set<String> subjects = new HashSet<String>();
      for (BAccountSourceBill sourceBill : queryResult.getRecords()) {
        for (BAccount bAccount : sourceBill.getAccounts()) {
          subjects.add(bAccount.getAcc1().getSubject().getUuid());
        }
      }

      List<Bank> banks = getBanks(filter.getAccountUnitUuid(), subjects);

      for (BAccountSourceBill sourceBill : queryResult.getRecords()) {
        for (BAccount bAccount : sourceBill.getAccounts()) {
          Bank bank = getBank(banks, bAccount.getAcc1().getSubject().getUuid());
          if (bank != null) {
            bAccount.setBank(new UCN(bank.getUuid(), bank.getCode(), bank.getName()));
          }
        }
      }

      return queryResult;

    } catch (Exception e) {
      throw e;
    }
  }

  private OrderSort getDefaultSort(String property, String direction) {
    OrderSort sort = new OrderSort();
    sort.setProperty(property);
    sort.setDirection(direction);
    return sort;
  }

  private List<BAccount> decorateAccount(List<Account> accounts) throws Exception {
    List<BAccount> result = new ArrayList<BAccount>();
    for (Account account : accounts) {
      result.add(BAccount.newInstance(account));
    }

    decorateSubject(result);
    decorateOverdueTerms(result);

    return result;
  }

  // 处理来源单据账款
  private void decorateAccountSourceBill(List<BAccountSourceBill> accountSourceBills,
      int directionType) {
    for (BAccountSourceBill sourceBill : accountSourceBills) {
      // 对收款单来说,0当作“收”处理,付款单反之
      if (directionType == 1) {
        if (sourceBill.getAccsDirection() == -1) {
          sourceBill.setDirection("付");
        } else {
          sourceBill.setDirection("收");
        }
      } else {
        if (sourceBill.getAccsDirection() == 1) {
          sourceBill.setDirection("收");
        } else {
          sourceBill.setDirection("付");
        }
      }

      sourceBill.setSourceBillNumber(sourceBill.getSourceBill().getBillNumber());
    }

    List<BAccount> accounts = new ArrayList<BAccount>();
    for (BAccountSourceBill accountSourceBill : accountSourceBills) {
      accounts.addAll(accountSourceBill.getAccounts());
    }

    decorateOverdueTerms(accounts);
  }

  /** 将total取反 */
  private void negateAccountTotal(List<Account> accounts, AccountDataFilter filter) {
    if (filter.isIgnoreDirectionType() && filter.isOppositeDirection()) {
      for (Account acc : accounts) {
        if (filter.getDirectionType() != acc.getAcc1().getDirection()) {
          acc.getOriginTotal().setTotal(acc.getOriginTotal().getTotal().negate());
          acc.getOriginTotal().setTax(acc.getOriginTotal().getTax().negate());
          acc.getTotal().setTotal(acc.getTotal().getTotal().negate());
          acc.getTotal().setTax(acc.getTotal().getTax().negate());
        }
      }
    }
  }

  private void decorateAccountStatement(String accountUnitUuid, List<BAccountStatement> list) {
    decorateAccountSettleInfo(accountUnitUuid, list);

    List<BAccount> accounts = new ArrayList<BAccount>();
    for (BAccountStatement statement : list) {
      accounts.addAll(statement.getAccounts());
    }
    decorateOverdueTerms(accounts);

    Set<String> uuids = new HashSet<String>();
    // 批量查合同类型并赋值
    for (BAccountStatement accountStatement : list) {
      uuids.add(accountStatement.getUuid());
    }
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Contracts.CONDITION_UUID_IN, uuids.toArray());

    for (Contract contract : contractService.query(definition).getRecords()) {
      for (BAccountStatement accountStatement : list) {
        if (accountStatement.getContract().getUuid().equals(contract.getUuid())) {
          accountStatement.setContractCategory(contract.getContractCategory());
        }
      }
    }
  }

  /**
   * 处理滞纳金条款
   * 
   * @param accounts
   */
  private void decorateOverdueTerms(List<BAccount> accounts) {

    if (accounts.isEmpty()) {
      return;
    }

    Set<String> contractIds = new HashSet<String>();
    for (BAccount result : accounts) {
      if (!contractIds.contains(result.getAcc1().getContract().getUuid()))
        contractIds.add(result.getAcc1().getContract().getUuid());
    }

    Map<String, List<OverdueTerm>> source = contractService.getOverdueTerms(contractIds);
    if (source.isEmpty()) {
      return;
    }

    for (BAccount account : accounts) {
      List<OverdueTerm> termList = source.get(account.getAcc1().getContract().getUuid());
      if (termList == null || termList.isEmpty()) {
        continue;
      }

      for (OverdueTerm term : termList) {
        if (term.getDirection() != Direction.RECEIPT) {
          continue;
        }
        if (term.isAllSubjects() == false
            && CommonUtil.isContains(term.getSubjects(), account.getAcc1().getSubject()) == false) {
          continue;
        }

        PaymentOverdueTerm paymentTerm = new PaymentOverdueTerm();
        paymentTerm.setContract(account.getAcc1().getContract());
        paymentTerm.setDirection(term.getDirection());
        paymentTerm.setInvoice(term.isInvoice());
        paymentTerm.setRate(term.getRate());
        paymentTerm.setSubject(term.getOverdueSubject());
        paymentTerm.setTaxRate(term.getTaxRate());
        paymentTerm.setAllSubjects(term.isAllSubjects());

        paymentTerm.setSubjects(term.getSubjects());

        account.getTerms().add(paymentTerm);
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
   * @throws BusinessException
   */
  private void decorateAccountSettleInfo(String accountUnitUuid, List<BAccountStatement> list) {
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
   * @throws BusinessException
   */
  private List<Statement> getStatementByUuids(String accountUnitUuid, List<String> statementUuids)
      throws IllegalArgumentException {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    if (!StringUtil.isNullOrBlank(accountUnitUuid))
      queryDef.addCondition(Statements.CONDITION_ACCOUNTUNIT_EQUALS, accountUnitUuid);

    queryDef.addCondition(Statements.CONDITION_UUID_IN, statementUuids.toArray());

    QueryResult<Statement> qr = statementService.query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return Collections.emptyList();

    return qr.getRecords();
  }

  /** 查询有效账款 */
  private QueryDefinition buildAccountQueryDef(AccountDataFilter filter) {
    if (filter == null)
      return null;

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
      List<String> list = getDataPermedHelper().getUserStoreIds(getSessionUser().getId());
      if (list.isEmpty()) {
        return null;
      }
      queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_IN, list.toArray());
    }

    // 合同授权组限制
    if (getDataPermedHelper().isPermEnabled(
        com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT)
        || getDataPermedHelper().isPermEnabled(
            com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)) {
      List<String> userGroupIds = getDataPermedHelper().getUserGroupIds(getSessionUser().getId());
      QueryCondition condition = new QueryCondition(Accounts.CONDITION_CONTRACT_PERMGROUP_ID_IN,
          userGroupIds.toArray());
      condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
      queryDef.getConditions().add(condition);
    }

    if (filter.getAccountDatePeriod() != null) {
      queryDef.addCondition(
          Accounts.CONDITION_ACCOUNT_TIME_BETWEEN,
          filter.getAccountDatePeriod().getBeginDate(),
          filter.getAccountDatePeriod().getEndDate() == null ? null : DateUtil.datePart(filter
              .getAccountDatePeriod().getEndDate()));
    }
    if (StringUtil.isNullOrBlank(filter.getContractId()) == false) {
      queryDef.addCondition(Accounts.CONDITION_CONTRACT_ID_EQUALS, filter.getContractId());
    }
    if (StringUtil.isNullOrBlank(filter.getContractNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_CONTRACT_NUMBER_STARTWITH,
          filter.getContractNumber());
    }
    if (StringUtil.isNullOrBlank(filter.getContractName()) == false) {
      queryDef.addCondition(Accounts.CONDITION_CONTRACT_NAME_LIKE, filter.getContractName());
    }
    if (StringUtil.isNullOrBlank(filter.getCounterpartUuid()) == false) {
      queryDef
          .addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS, filter.getCounterpartUuid());
    }
    if (StringUtil.isNullOrBlank(filter.getCounterpartName()) == false) {
      queryDef.addCondition(Accounts.CONDITION_COUNTERPART_CODENAME_LIKE,
          filter.getCounterpartName());
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

    if (filter.isOppositeDirection()) {
      if (StringUtil.isNullOrBlank(filter.getLockerUuid()) == false) {
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, filter.getLockerUuid());
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
        queryDef.addCondition(Accounts.CONDITION_PAYMENT_UUID_EQUALS, Accounts.NONE_BILL_UUID);
      } else {
        queryDef.addCondition(Accounts.CONDITION_CAN_PAYMENT);
      }
    } else {
      queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
    }
    if (StringUtil.isNullOrBlank(filter.getStatement()) == false) {
      queryDef.addCondition(Accounts.CONDITION_STATEMENT_BILLNUMBER_STARTWITH,
          filter.getStatement());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillType()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS, filter.getSourceBillType());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_NUMBER_STARTWITH,
          filter.getSourceBillNumber());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillType()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS, filter.getSourceBillType());
    }
    if (StringUtil.isNullOrBlank(filter.getSubjectUuid()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SUBJECT_UUID_EQUALS, filter.getSubjectUuid());
    }

    queryDef.setPage(0);
    queryDef.setPageSize(0);

    return queryDef;
  }

  /** 过滤调用已经添加的账款 */
  private List<Account> filterAccount(List<Account> list, AccountDataFilter filter) {

    negateAccountTotal(list, filter);

    // 已经添加的账款id列表
    List<AccountId> ids = filter.getHasAddedAccIds();

    if (ids == null || ids.isEmpty()) {
      return list;
    }

    for (int i = list.size() - 1; i >= 0; i--) {
      Account account = list.get(i);
      String bizId = getBizId(account);
      for (AccountId id : ids) {
        if (id.getAccId().equals(account.getAcc1().getId()) && id.getBizId().equals(bizId)) {
          list.remove(i);
          break;
        }
      }
    }

    return list;
  }

  private String getBizId(Account account) {
    ArrayList<String> idList = new ArrayList<String>();
    idList.add(account.getAcc2().getStatement() == null ? Accounts.NONE_BILL_UUID : account
        .getAcc2().getStatement().id());
    idList.add(account.getAcc2().getInvoice() == null ? Accounts.NONE_BILL_UUID : account.getAcc2()
        .getInvoice().id());
    idList.add(Accounts.NONE_BILL_UUID);
    idList.add(Accounts.NONE_BILL_UUID);
    String bizId = CollectionUtil.toString(idList);

    return bizId;

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
    QueryResult<Subject> qr = subjectService.query(def, Subject.PART_USAGE);

    Map<String, String> usageMap = new HashMap<String, String>();
    for (Subject s : qr.getRecords()) {
      if (s.getUsages().isEmpty()) {
        continue;
      }

      SubjectUsage usage = SubjectUsageUtils.getSubjectUsage(s.getUsages().get(0));
      if (usage == null) {
        continue;
      }
      usageMap.put(s.getUuid(), usage.getName());
    }

    // 更新科目用途
    for (BAccount a : list) {
      a.setSubjectUsageName(usageMap.get(a.getAcc1().getSubject().getUuid()));
    }
  }

  private OrderSort getOrder(AccountDataFilter filter, OrderSort defaultOrderDir) {
    if (filter.getSorts() != null && filter.getSorts().size() > 0) {
      return filter.getSorts().get(0);
    }
    return defaultOrderDir;
  }

  protected DataPermedHelper getDataPermedHelper() {
    return getAppCtx().getBean(DataPermedHelper.class);
  }

}
