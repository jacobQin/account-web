/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	InvoiceRegServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.acc.client.BAccountId;
import com.hd123.m3.account.gwt.acc.server.AccountBizConverter;
import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.SourceBillBizConverter;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.invoice.commons.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.filter.AccountDataFilter;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.account.gwt.settle.server.SettleUtil;
import com.hd123.m3.account.gwt.util.server.BillTypeUtils;
import com.hd123.m3.account.gwt.util.server.CollectionUtil;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.account.service.freeze.Freeze;
import com.hd123.m3.account.service.invoice.InvoiceReg;
import com.hd123.m3.account.service.invoice.InvoiceRegs;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.paymentnotice.PaymentNotice;
import com.hd123.m3.account.service.paymentnotice.PaymentNoticeLine;
import com.hd123.m3.account.service.paymentnotice.PaymentNoticeService;
import com.hd123.m3.account.service.paymentnotice.PaymentNotices;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author chenpeisi
 * 
 */
public class InvoiceRegServiceServlet extends AccRemoteServiceServlet implements InvoiceRegService {
  private static final long serialVersionUID = 300100L;

  @Override
  public String getObjectName() {
    return null;
  }

  @Override
  public RPageData<BAccount> queryAccount(AccountDataFilter filter) throws ClientBizException {
    try {

      FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();

      // 合同授权组限制
      if (permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT)
          || permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)) {
        List<String> userGroupIds = getUserGroups(getSessionUser().getId());
        QueryCondition condition = new QueryCondition(Accounts.CONDITION_CONTRACT_PERMGROUP_ID_IN,
            userGroupIds.toArray());
        condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
        queryDef.getConditions().add(condition);
      }
      queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());

      queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnit()
          .trim());
      queryDef.addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS, filter.getCounterpart()
          .trim());

      String invoiceRegUuid = filter.getInvoiceRegUuid();
      if (StringUtil.isNullOrBlank(invoiceRegUuid) == false) {
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, invoiceRegUuid.trim());
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
        queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);
      } else {
        queryDef.addCondition(Accounts.CONDITION_CAN_INVOICE);
      }

      String billType = filter.getSourceBillType();
      if (StringUtil.isNullOrBlank(billType) == false)
        queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS, billType.trim());

      String billNumber = filter.getSourceBillNumber();
      if (StringUtil.isNullOrBlank(billNumber) == false)
        queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_NUMBER_EQUALS, billNumber.trim());

      String statement = filter.getStatement();
      if (StringUtil.isNullOrBlank(statement) == false)
        queryDef.addCondition(Accounts.CONDITION_STATEMENT_BILLNUMBER_STARTWITH, statement.trim());

      String subject = filter.getSubject();
      if (StringUtil.isNullOrBlank(subject) == false)
        queryDef.addCondition(Accounts.CONDITION_SUBJECT_UUID_EQUALS, subject.trim());

      String contract = filter.getContract();
      if (StringUtil.isNullOrBlank(contract) == false)
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_NUMBER_STARTWITH, contract.trim());

      String contractName = filter.getContractName();
      if (StringUtil.isNullOrBlank(contractName) == false)
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_NAME_LIKE, contractName);

      queryDef.setPage(0);
      queryDef.setPageSize(0);

      QueryResult<Account> qr = getAccountService().query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter.getAccountIds());

      RPageData<BAccount> results = new RPageData<BAccount>();
      RPageDataUtil.copyPagingInfo(qr, results);

      results.setValues(ConverterUtil.convert(accounts, AccountBizConverter.getInstance()));

      Order order = null;
      if (filter.getOrders() != null && filter.getOrders().size() > 0)
        order = filter.getOrders().get(0);
      else
        order = new Order(BAccount.ORDER_BY_FIELD_SUBJECT, OrderDir.asc);
      results.setValues(SettleUtil.sortAccountList(results.getValues(), order));
      results.setTotalCount(results.getValues().size());

      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private List<Account> filterAccount(List<Account> list, List<BAccountId> ids) {
    if (ids == null || ids.isEmpty())
      return list;

    for (int i = list.size() - 1; i >= 0; i--) {
      Account account = list.get(i);
      ArrayList<String> idList = new ArrayList<String>();
      idList.add(account.getAcc2().getStatement() == null ? BBill.NONE_ID : account.getAcc2()
          .getStatement().id());
      idList.add(account.getAcc2().getPayment() == null ? BBill.NONE_ID : account.getAcc2()
          .getPayment().id());
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

      FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();

      // 合同授权组限制
      if (permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT)
          || permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)) {
        List<String> userGroupIds = getUserGroups(getSessionUser().getId());
        QueryCondition condition = new QueryCondition(Accounts.CONDITION_CONTRACT_PERMGROUP_ID_IN,
            userGroupIds.toArray());
        condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
        queryDef.getConditions().add(condition);
      }

      queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnit()
          .trim());

      queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());

      queryDef.addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS, filter.getCounterpart()
          .trim());

      queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_NOTIN, Accounts.NONE_BILL_UUID,
          Accounts.DISABLE_BILL_UUID);

      String invoiceRegUuid = filter.getInvoiceRegUuid();
      if (StringUtil.isNullOrBlank(invoiceRegUuid) == false) {
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, invoiceRegUuid.trim());
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
        queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);
      } else {
        queryDef.addCondition(Accounts.CONDITION_CAN_INVOICE);
      }

      String statementBillNumber = filter.getStatement();
      if (StringUtil.isNullOrBlank(statementBillNumber) == false)
        queryDef.addCondition(Accounts.CONDITION_STATEMENT_BILLNUMBER_STARTWITH,
            statementBillNumber.trim());

      BDateRange accountTime = filter.getAccountTime();
      if (accountTime != null)
        queryDef.addCondition(Accounts.CONDITION_ACCOUNT_TIME_BETWEEN, accountTime.getBeginDate(),
            accountTime.getEndDate());

      String contract = filter.getContract();
      if (StringUtil.isNullOrBlank(contract) == false)
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_NUMBER_STARTWITH, contract.trim());

      String contractName = filter.getContractName();
      if (StringUtil.isNullOrBlank(contractName) == false)
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_NAME_LIKE, contractName);

      queryDef.setPage(0);
      queryDef.setPageSize(0);

      QueryResult<Account> qr = getAccountService().query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter.getAccountIds());

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

      RPageData<BAccountStatement> results = new RPageData<BAccountStatement>();
      RPageDataUtil.copyPagingInfo(qr, results);

      for (List<Account> group : groups) {
        BAccountStatement result = new BAccountStatement();
        result.setUuid(group.get(0).getAcc2().getStatement().getBillUuid());
        result.setBillNumber(group.get(0).getAcc2().getStatement().getBillNumber());
        result.setContract(UCNBizConverter.getInstance().convert(
            group.get(0).getAcc1().getContract()));

        result.setAccounts(ConverterUtil.convert(group, AccountBizConverter.getInstance()));

        result.setTotal(result.aggregateTotal());

        results.getValues().add(result);
      }

      decorateAccountStatement(results.getValues());

      Order order = null;
      if (filter.getOrders() != null && filter.getOrders().size() > 0)
        order = filter.getOrders().get(0);
      else
        order = new Order(BAccountStatement.ORDER_BY_FIELD_BILLNUMBER, OrderDir.desc);
      results.setValues(SettleUtil.sortAccountStatementList(results.getValues(), order));
      results.setTotalCount(results.getValues().size());

      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /**
   * 从账单取出出账日期和账单周期。
   * 
   * @param list
   * @throws IllegalArgumentException
   *           @
   */
  private void decorateAccountStatement(List<BAccountStatement> list) {
    List<String> statementUuids = toStatementUuids(list);

    if (statementUuids == null || statementUuids.isEmpty())
      return;

    List<Statement> statements = getStatementByUuids(statementUuids);

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
   * @param statementUuids
   *          账单uuis集合
   * @return 账单
   * @throws IllegalArgumentException
   *           @
   */
  private List<Statement> getStatementByUuids(List<String> statementUuids) {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

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

      FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();

      // 合同授权组限制
      if (permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT)
          || permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)) {
        List<String> userGroupIds = getUserGroups(getSessionUser().getId());
        QueryCondition condition = new QueryCondition(Accounts.CONDITION_CONTRACT_PERMGROUP_ID_IN,
            userGroupIds.toArray());
        condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
        queryDef.getConditions().add(condition);
      }

      queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnit()
          .trim());

      queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());

      queryDef.addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS, filter.getCounterpart()
          .trim());

      List<String> billTypes = new ArrayList<String>();
      for (BBillType type : BillTypeUtils.getAll()) {
        if (Statement.class.getName().equals(type.getClassName())
            || PaymentNotice.class.getName().equals(type.getClassName())
            || Deposit.class.getName().equals(type.getClassName())
            || DepositRepayment.class.getName().equals(type.getClassName())
            || Freeze.class.getName().equals(type.getClassName())
            || DepositMove.class.getName().equals(type.getClassName())
            || InvoiceReg.class.getName().equals(type.getClassName())
            || Payment.class.getName().equals(type.getClassName())
            && type.getDirection() != filter.getDirectionType())
          continue;
        billTypes.add(type.getName());
      }

      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_IN, billTypes.toArray());

      String invoiceRegUuid = filter.getInvoiceRegUuid();
      if (StringUtil.isNullOrBlank(invoiceRegUuid) == false) {
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, invoiceRegUuid.trim());
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
        queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);
      } else {
        queryDef.addCondition(Accounts.CONDITION_CAN_INVOICE);
      }

      String billType = filter.getSourceBillType();
      if (StringUtil.isNullOrBlank(billType) == false)
        queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS, billType.trim());

      String billNumber = filter.getSourceBillNumber();
      if (StringUtil.isNullOrBlank(billNumber) == false)
        queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_NUMBER_EQUALS, billNumber.trim());

      String contract = filter.getContract();
      if (StringUtil.isNullOrBlank(contract) == false)
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_NUMBER_STARTWITH, contract.trim());

      String contractName = filter.getContractName();
      if (StringUtil.isNullOrBlank(contractName) == false)
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_NAME_LIKE, contractName);

      queryDef.setPage(0);
      queryDef.setPageSize(0);

      QueryResult<Account> qr = getAccountService().query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter.getAccountIds());

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

      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BAccountNotice> queryAccountByPaymentNotice(AccountDataFilter filter)
      throws ClientBizException {
    try {

      FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();

      // 合同授权组限制
      if (permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT)
          || permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)) {
        List<String> userGroupIds = getUserGroups(getSessionUser().getId());
        QueryCondition condition = new QueryCondition(Accounts.CONDITION_CONTRACT_PERMGROUP_ID_IN,
            userGroupIds.toArray());
        condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
        queryDef.getConditions().add(condition);
      }

      queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnit()
          .trim());

      queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());

      queryDef.addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS, filter.getCounterpart()
          .trim());

      // 用于跟收付款通知单关联
      queryDef.addCondition(Accounts.CONDIRION_NOTICE);

      String invoiceRegUuid = filter.getInvoiceRegUuid();
      if (StringUtil.isNullOrBlank(invoiceRegUuid) == false) {
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, invoiceRegUuid.trim());
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
        queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);
      } else {
        queryDef.addCondition(Accounts.CONDITION_CAN_INVOICE);
      }

      String paymentNoticeBillNumber = filter.getPaymentNotice();
      if (StringUtil.isNullOrBlank(paymentNoticeBillNumber) == false)
        queryDef.addCondition(Accounts.CONDITION_NOTICE_NUMBER_STARTWITH,
            paymentNoticeBillNumber.trim());

      BDateRange accountTime = filter.getAccountTime();
      if (accountTime != null)
        queryDef.addCondition(Accounts.CONDITION_ACCOUNT_TIME_BETWEEN, accountTime.getBeginDate(),
            accountTime.getEndDate());

      String contract = filter.getContract();
      if (StringUtil.isNullOrBlank(contract) == false)
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_NUMBER_STARTWITH, contract.trim());

      queryDef.setPage(0);
      queryDef.setPageSize(0);

      QueryResult<Account> qr = getAccountService().query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<Account> accounts = filterAccount(qr.getRecords(), filter.getAccountIds());

      Map<String, List<Account>> accountMap = decorateAccount(accounts);

      if (accountMap == null || accountMap.isEmpty())
        return null;

      RPageData<BAccountNotice> results = new RPageData<BAccountNotice>();
      RPageDataUtil.copyPagingInfo(qr, results);

      for (String notice : accountMap.keySet()) {
        BAccountNotice result = new BAccountNotice();
        result.setBillNumber(notice);
        result.setAccounts(ConverterUtil.convert(accountMap.get(notice),
            AccountBizConverter.getInstance()));

        result.setTotal(result.aggregateTotal());
        results.getValues().add(result);
      }

      Order order = null;
      if (filter.getOrders() != null && filter.getOrders().size() > 0)
        order = filter.getOrders().get(0);
      else
        order = new Order(BAccountNotice.ORDER_BY_FIELD_BILLNUMBER, OrderDir.desc);
      results.setValues(SettleUtil.sortAccountNoticeList(results.getValues(), order));
      results.setTotalCount(results.getValues().size());

      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private Map<String, List<Account>> decorateAccount(List<Account> accounts) {
    List<String> statementUuids = new ArrayList<String>();

    for (Account account : accounts) {
      if (statementUuids.contains(account.getAcc2().getStatement().getBillUuid()))
        continue;
      else
        statementUuids.add(account.getAcc2().getStatement().getBillUuid());
    }

    if (statementUuids.isEmpty())
      return null;

    List<PaymentNotice> notices = queryNotices(statementUuids);

    if (notices == null || notices.isEmpty())
      return null;

    Map<String, List<Account>> accountMap = new HashMap<String, List<Account>>();

    for (PaymentNotice notice : notices) {
      for (PaymentNoticeLine line : notice.getLines()) {
        for (Account account : accounts) {
          if (ObjectUtil.equals(account.getAcc2().getStatement(), line.getStatement())) {
            if (accountMap.keySet().contains(notice.getBillNumber())) {
              accountMap.get(notice.getBillNumber()).add(account);
            } else {
              List<Account> reuslt = new ArrayList<Account>();
              reuslt.add(account);
              accountMap.put(notice.getBillNumber(), reuslt);
            }
          }
        }
      }
    }
    return accountMap;
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
  public void saveConfig(BInvoiceRegConfig config, int direction) throws ClientBizException {
    try {
      BeanOperateContext operateCtx = ConvertHelper.getOperateContext(getSessionUser());

      if (Direction.RECEIPT == direction) {
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_REC_REGTOTAL_WRITABLE, String.valueOf(config.getRegTotalWritable()),
            operateCtx);
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_REC_TAX_DIFF_HI, String.valueOf(config.getTaxDiffHi()), operateCtx);
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_REC_TAX_DIFF_LO, String.valueOf(config.getTaxDiffLo()), operateCtx);
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_REC_TOTAL_DIFF_HI, String.valueOf(config.getTotalDiffHi()), operateCtx);
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_REC_TOTAL_DIFF_LO, String.valueOf(config.getTotalDiffLo()), operateCtx);
      } else {
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_PAY_REGTOTAL_WRITABLE, String.valueOf(config.getRegTotalWritable()),
            operateCtx);
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_PAY_TAX_DIFF_HI, String.valueOf(config.getTaxDiffHi()), operateCtx);
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_PAY_TAX_DIFF_LO, String.valueOf(config.getTaxDiffLo()), operateCtx);
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_PAY_TOTAL_DIFF_HI, String.valueOf(config.getTotalDiffHi()), operateCtx);
        getAccountOptionService().setOption(InvoiceRegs.OPTION_PATH,
            InvoiceRegs.KEY_PAY_TOTAL_DIFF_LO, String.valueOf(config.getTotalDiffLo()), operateCtx);
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /** 查询账款默认限制 */
  private FlecsQueryDefinition buildDefaultQueryDefinition() {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);

    return queryDef;
  }

  private AccountService getAccountService() {
    return M3ServiceFactory.getService(AccountService.class);
  }

  private AccountOptionService getAccountOptionService() {
    return M3ServiceFactory.getService(AccountOptionService.class);
  }

  private StatementService getStatementService() {
    return M3ServiceFactory.getService(StatementService.class);
  }

  private PaymentNoticeService getPaymentNotice() {
    return M3ServiceFactory.getService(PaymentNoticeService.class);
  }

}
