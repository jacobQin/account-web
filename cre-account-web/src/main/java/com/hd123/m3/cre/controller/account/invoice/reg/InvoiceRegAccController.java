/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceRegAccController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月16日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.acc.Acc2;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.acc.BizId;
import com.hd123.m3.account.service.ivc.reg.InvoiceReg;
import com.hd123.m3.account.service.ivc.reg.IvcRegLine;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentService;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.QueryPagingUtil;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.controllers.BaseController;
import com.hd123.m3.commons.servlet.permed.DataPermedHelper;
import com.hd123.m3.cre.controller.account.common.AccountDataFilter;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.CommonConstants;
import com.hd123.m3.cre.controller.account.common.model.AccountId;
import com.hd123.m3.cre.controller.account.common.model.BAccount;
import com.hd123.m3.cre.controller.account.common.model.BAccountPayment;
import com.hd123.m3.cre.controller.account.common.model.BAccountStatement;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.m3.cre.controller.account.common.util.SettleUtil;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.util.DateUtil;

/**
 * 收款发票登记单账款查询控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/invoice/reg/acc/*")
public class InvoiceRegAccController extends BaseController {

  @Autowired
  private AccountService accountService;
  @Autowired
  private StatementService statementService;
  @Autowired
  private DataPermedHelper permedHelper;
  @Autowired
  private PaymentService paymentService;
  @Autowired
  private AccountOptionComponent optionComponent;
  
  @RequestMapping(value = "createByPayment", method = RequestMethod.GET)
  public @ResponseBody InvoiceReg createByPayment(@RequestParam("billUuid")String billUuid) throws Exception {
    
    InvoiceReg target = createAndInitReg();

    QueryDefinition queryDef = buildAccountQueryDef(new AccountDataFilter());
    queryDef.addCondition(Accounts.CONDITION_PAYMENT_UUID_EQUALS, billUuid);

    List<Account> accounts = accountService.query(queryDef).getRecords();
    if (accounts == null || accounts.isEmpty()) {
      throw new Exception("不存在可登记的账款");
    }

    Payment payment = paymentService.get(billUuid);
    if (payment == null) {
      throw new Exception("找不到指定的收款单");
    }
    target.setAccountUnit(payment.getAccountUnit());
    target.setCounterpart(payment.getCounterpart());
    target.setCounterpartType(payment.getCounterpartType());
    target.setRegDate(payment.getPaymentDate());

    for (Account account : accounts) {
      target.getRegLines().add(createRegLine(account));
    }
    return target;
  }
  
  @RequestMapping(value = "createByStatement", method = RequestMethod.GET)
  public @ResponseBody InvoiceReg createByStatement(@RequestParam("billUuid")String billUuid) throws Exception {
    
    InvoiceReg target = createAndInitReg();


    QueryDefinition queryDef = buildAccountQueryDef(new AccountDataFilter());
    queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_EQUALS, billUuid);

    List<Account> accounts = accountService.query(queryDef).getRecords();
    if (accounts == null || accounts.isEmpty())
      throw new Exception("不存在可登记的账款");

    target.setAccountUnit(accounts.get(0).getAcc1().getAccountUnit());
    target.setCounterpart(accounts.get(0).getAcc1().getCounterpart());
    target.setCounterpartType(accounts.get(0).getAcc1().getCounterpartType());

    for (Account account : accounts) {
      target.getRegLines().add(createRegLine(account));
    }
    return target;
  }

  @RequestMapping(value = "createByNotice", method = RequestMethod.GET)
  public @ResponseBody InvoiceReg createByNotice(@RequestParam("billUuid")String billUuid) throws Exception {
    
    InvoiceReg target = createAndInitReg();

    QueryDefinition queryDef = buildAccountQueryDef(new AccountDataFilter());
    queryDef.addCondition(Accounts.CONDITION_NOTICE_UUID_EQUALS, billUuid);

    List<Account> accounts = accountService.query(queryDef).getRecords();
    if (accounts == null || accounts.isEmpty())
      throw new Exception("不存在可登记的账款");

    target.setAccountUnit(accounts.get(0).getAcc1().getAccountUnit());
    target.setCounterpart(accounts.get(0).getAcc1().getCounterpart());
    target.setCounterpartType(accounts.get(0).getAcc1().getCounterpartType());

    for (Account account : accounts) {
      target.getRegLines().add(createRegLine(account));
    }
    return target;
  }
  
  private InvoiceReg createAndInitReg(){
    InvoiceReg target = new InvoiceReg();
    target.setDirection(Direction.RECEIPT);
    target.setRegDate(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
    target.setBizState(BBizStates.INEFFECT);
    target.setInvoiceType(getDefalutInvoiceType());
    target.setUseInvoiceStock(optionComponent.isUseInvoiceStock());
    target.setAllowSplitReg(optionComponent.isAllowSplitReg());
    return target;
  }

  private IvcRegLine createRegLine(Account account) {
    IvcRegLine line = new IvcRegLine();
    line.setAcc1(account.getAcc1());
    line.setAcc2(account.getAcc2());
    line.setOriginTotal(account.getOriginTotal());
    line.setUnregTotal(account.getTotal());
    line.setTotal(account.getTotal());
    line.setTotalDiff(BigDecimal.ZERO);
    line.setTaxDiff(BigDecimal.ZERO);

    line.setInvoiceType(getDefalutInvoiceType());
      
    return line;
  }
  
  @RequestMapping("queryAccByReceipt")
  public @ResponseBody QueryResult<BAccountPayment> queryAccPayments(@RequestBody AccountDataFilter filter)
      throws Exception {
    try {

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null){
        return null;
      }

      queryDef.addCondition(Accounts.CONDITION_PAYMENT_UUID_NOTIN, Accounts.DISABLE_BILL_UUID,
          Accounts.NONE_BILL_UUID);
      queryDef.setPage(0);
      queryDef.setPageSize(0);

      QueryResult<Account> qr = accountService.query(queryDef);
      if (qr.getRecords().isEmpty()){
        return null;
      }
      
      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      /** 根据收款单进行分组 */
      Map<String, BAccountPayment> map = new HashMap<String, BAccountPayment>();
      for (Account account : accounts) {
        BAccountPayment payment = map.get(account.getAcc2().getPayment().getBillUuid());
        if (payment == null) {
          payment = new BAccountPayment();
          payment.setUuid(account.getAcc2().getPayment().getBillUuid());
          payment.setBillNumber(account.getAcc2().getPayment().getBillNumber());
          payment.setAccountUnit(account.getAcc1().getAccountUnit());
          payment.setContract(account.getAcc1().getContract());
          payment.setCounterpart(account.getAcc1().getCounterpart());
          payment.setCounterpartType(account.getAcc1().getCounterpartType());
          map.put(payment.getUuid(), payment);
        }
        payment.getAccounts().add(account);
        payment.setTotal(payment.aggregateTotal());
      }
      
      // 排序
      OrderSort defaultsort = getDefaultSort(BAccountPayment.ORDER_BY_FIELD_BILLNUMBER,
          OrderSort.DESC);
      if(filter.getSorts().isEmpty()==false){
        defaultsort = filter.getSorts().get(0);
      }
      
      List<BAccountPayment> sortAccStatements = SettleUtil.sortAccountPaymentList(
          map.values(), getOrder(filter, defaultsort));
      
      QueryResult<BAccountPayment> queryResult = QueryPagingUtil.flip(sortAccStatements, filter.getCurrentPage(),
          filter.getPageSize());

      return queryResult;
      
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @RequestMapping("queryAccount")
  public @ResponseBody QueryResult<Account> queryAccount(@RequestBody AccountDataFilter filter)
      throws Exception {
    
      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null){
        return null;
      }

      queryDef.addOrder(Accounts.ORDER_BY_SUBJECT, QueryOrderDirection.asc);
      QueryResult<Account> qr = accountService.query(queryDef);

      if (qr.getRecords().isEmpty()) {
        return null;
      }

      List<Account> accounts = filterAccount(qr.getRecords(), filter);

      // 排序
      OrderSort defaultsort = getDefaultSort(CommonConstants.ORDER_BY_FIELD_SUBJECT, OrderSort.ASC);
      if(filter.getSorts().isEmpty()==false){
        defaultsort = filter.getSorts().get(0);
      }
      List<Account> sortAccounts = SettleUtil.sortAccountList(accounts,
          getOrder(filter, defaultsort));

      QueryResult<Account> queryResult = QueryPagingUtil.flip(sortAccounts, filter.getCurrentPage(),
          filter.getPageSize());

      return queryResult;
  }

  @RequestMapping("queryAccByStatement")
  public @ResponseBody QueryResult<BAccountStatement> queryAccountByStatement(
      @RequestBody AccountDataFilter filter) throws Exception {
    try {
      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_NOTIN, Accounts.DISABLE_BILL_UUID,
          Accounts.NONE_BILL_UUID);
      queryDef.setPage(0);
      queryDef.setPageSize(0);

      QueryResult<Account> qr = accountService.query(queryDef);
      if (qr.getRecords().isEmpty()) {
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
      if(filter.getSorts().isEmpty()==false){
        defaultsort = filter.getSorts().get(0);
      }
      
      List<BAccountStatement> sortAccStatements = SettleUtil.sortAccountStatementList(
          accountStatements, getOrder(filter, defaultsort));

      // 分页
      QueryResult<BAccountStatement> statements = QueryPagingUtil.flip(sortAccStatements,
          filter.getCurrentPage(), filter.getPageSize());
      return statements;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }
  
  private String getDefalutInvoiceType(){
    String defalutInvoiceType = optionComponent.getDefalutInvoiceType() == null ? "common"
        : optionComponent.getDefalutInvoiceType();
    return defalutInvoiceType;
  }

  /** 过滤调用已经添加的账款 */
  private List<Account> filterAccount(List<Account> list, AccountDataFilter filter) {
    // 已经添加的账款id列表
    List<AccountId> ids = filter.getHasAddedAccIds();

    if (ids == null || ids.isEmpty()) {
      return list;
    }

    for (int i = list.size() - 1; i >= 0; i--) {
      Account account = list.get(i);
      for (AccountId id : ids) {
        if (id.getAccId().equals(account.getAcc1().getId()) && id.getBizId().equals(getBizId(account.getAcc2()))) {
          list.remove(i);
          break;
        }
      }
    }

    return list;
  }
  
  private String getBizId(Acc2 acc2) {
    BizId bizId = new BizId(acc2.getStatement(), acc2.getInvoice(), acc2.getPayment(), Accounts.NONE_LOCKER);
    return bizId.toId();
  }


  private void decorateAccountStatement(String accountUnitUuid, List<BAccountStatement> list) {
    decorateAccountSettleInfo(accountUnitUuid, list);
  }

  /**
   * 从账单取出出账日期和账单周期。
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

  private OrderSort getDefaultSort(String property, String direction) {
    OrderSort sort = new OrderSort();
    sort.setProperty(property);
    sort.setDirection(direction);
    return sort;
  }

  private OrderSort getOrder(AccountDataFilter filter, OrderSort defaultOrderDir) {
    if (filter.getSorts() != null && filter.getSorts().size() > 0) {
      return filter.getSorts().get(0);
    }
    return defaultOrderDir;
  }

  /** 查询有效账款 */
  private QueryDefinition buildAccountQueryDef(AccountDataFilter filter) {
    if (filter == null) {
      return null;
    }

    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);
    queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);

    if (StringUtil.isNullOrBlank(filter.getAccountUnitUuid()) == false) {
      queryDef
          .addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnitUuid());
    } else {
      // 受项目限制
      List<String> list = permedHelper.getUserStoreIds(getSessionUser().getId());
      if (list.isEmpty()) {
        return null;
      }
      queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_IN, list.toArray());
    }

    // 合同授权组限制
    if (permedHelper
        .isPermEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT)
        || permedHelper
            .isPermEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)) {
      List<String> userGroupIds = permedHelper.getUserGroupIds(getSessionUser().getId());
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

    if (StringUtil.isNullOrBlank(filter.getLockerUuid()) == false) {
      queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, filter.getLockerUuid());
      queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
      queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    } else {
      queryDef.addCondition(Accounts.CONDITION_CAN_INVOICE);
    }
    if (StringUtil.isNullOrBlank(filter.getStatement()) == false) {
      queryDef.addCondition(Accounts.CONDITION_STATEMENT_BILLNUMBER_STARTWITH,
          filter.getStatement());
    }
    if(StringUtil.isNullOrBlank(filter.getPayment()) == false){
      queryDef.addCondition(Accounts.CONDITION_PAYMENT_BILLNUMBER_STARTWITH, filter.getPayment());
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
}
