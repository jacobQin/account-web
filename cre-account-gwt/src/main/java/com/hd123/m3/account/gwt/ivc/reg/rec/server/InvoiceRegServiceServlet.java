/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.acc.server.AccountBizConverter;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.commons.client.biz.RoundingMode;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceRegOptions;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BIvcRegLine;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.IvcRegAccountFilter;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegPermDef;
import com.hd123.m3.account.gwt.ivc.reg.rec.server.converter.BizInvoiceRegConverter;
import com.hd123.m3.account.gwt.ivc.reg.rec.server.converter.InvoiceRegBizConverter;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountPayment;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.account.gwt.settle.server.SettleUtil;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.account.service.ivc.reg.InvoiceReg;
import com.hd123.m3.account.service.ivc.reg.InvoiceRegs;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentService;
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
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.bpm.server.M3BpmService2Servlet;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.util.Pair;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.server.RPageDataConverter;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 发票登记单|客户端服务。
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class InvoiceRegServiceServlet extends M3BpmService2Servlet<BInvoiceReg> implements
    InvoiceRegService {
  private static final long serialVersionUID = -6845258785654242243L;

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    moduleContext.put(ENABLED_EXTINVOICESYSTEM,
        getOptionService().getOption(AccOptions.OPTION_PATH, AccOptions.EXTINVSYSTEM_ENABLED));
    moduleContext.put(SCALE, getScale());
    moduleContext.put(ROUNDING_MODE, getRoundingMode());
    moduleContext.put(KEY_INVOICE_TYPE, CollectionUtil.toString(getInvoiceTypes()));
    moduleContext.put(KEY_BILL_TYPES, JsonUtil.objectToJson(getBillTypes()));
    moduleContext.put(OPTION_ALLOW_SPLIT_REG,
        getOptionService().getOption(getObjectName(), InvoiceRegs.OPTION_REC_ALLOW_SPLIT_REG));
    moduleContext.put(OPTION_USEINVOICESTOCK,
        getOptionService().getOption(getObjectName(), InvoiceRegs.OPTION_REC_USEINVOICESTOCK));

    String defalutInvoiceType = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_DEFAULT_INVOICETYPE);
    if ("null".equals(defalutInvoiceType)) {
      defalutInvoiceType = null;
    }
    moduleContext.put(KEY_DEFALUT_INVOICE_TYPE, defalutInvoiceType);
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    Set<BPermission> permissions = new HashSet<BPermission>();
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getId(),
        Arrays.asList(InvoiceRegPermDef.RESOURCE_KEY)));
    return permissions;
  }

  @Override
  public BInvoiceReg createByPayment(String paymentUuid) throws Exception {
    BInvoiceReg target = new BInvoiceReg();
    target.setDirection(DirectionType.receipt.getDirectionValue());
    target.setRegDate(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
    target.setBizState(BBizStates.INEFFECT);

    QueryDefinition queryDef = buildAccountQueryDef();
    queryDef.addCondition(Accounts.CONDITION_PAYMENT_UUID_EQUALS, paymentUuid);

    List<Account> accounts = getAccountService().query(queryDef).getRecords();
    if (accounts == null || accounts.isEmpty()) {
      throw new Exception("不存在可登记的账款");
    }

    Payment payment = getPaymentService().get(paymentUuid);
    if (payment == null) {
      throw new Exception("找不到指定的收款单");
    }
    target.setAccountUnit(UCNBizConverter.getInstance().convert(payment.getAccountUnit()));
    target.setCounterpart(UCNBizConverter.getInstance().convert(payment.getCounterpart()));
    target.setCounterpartType(payment.getCounterpartType());
    target.setRegDate(payment.getPaymentDate());

    for (Account account : accounts) {
      target.getRegLines().add(createRegLine(account));
    }
    return target;
  }

  @Override
  public BInvoiceReg createByStatement(String statementUuid) throws Exception {
    BInvoiceReg target = new BInvoiceReg();
    target.setDirection(DirectionType.receipt.getDirectionValue());
    target.setRegDate(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
    target.setBizState(BBizStates.INEFFECT);

    QueryDefinition queryDef = buildAccountQueryDef();
    queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_EQUALS, statementUuid);

    List<Account> accounts = getAccountService().query(queryDef).getRecords();
    if (accounts == null || accounts.isEmpty())
      throw new Exception("不存在可登记的账款");

    target.setAccountUnit(UCNBizConverter.getInstance().convert(
        accounts.get(0).getAcc1().getAccountUnit()));
    target.setCounterpart(UCNBizConverter.getInstance().convert(
        accounts.get(0).getAcc1().getCounterpart()));
    target.setCounterpartType(accounts.get(0).getAcc1().getCounterpartType());

    for (Account account : accounts) {
      target.getRegLines().add(createRegLine(account));
    }
    return target;
  }

  @Override
  public BInvoiceReg createByNotice(String noticeUuid) throws Exception {
    BInvoiceReg target = new BInvoiceReg();
    target.setDirection(DirectionType.receipt.getDirectionValue());
    target.setRegDate(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
    target.setBizState(BBizStates.INEFFECT);

    QueryDefinition queryDef = buildAccountQueryDef();
    queryDef.addCondition(Accounts.CONDITION_NOTICE_UUID_EQUALS, noticeUuid);

    List<Account> accounts = getAccountService().query(queryDef).getRecords();
    if (accounts == null || accounts.isEmpty())
      throw new Exception("不存在可登记的账款");

    target.setAccountUnit(UCNBizConverter.getInstance().convert(
        accounts.get(0).getAcc1().getAccountUnit()));
    target.setCounterpart(UCNBizConverter.getInstance().convert(
        accounts.get(0).getAcc1().getCounterpart()));
    target.setCounterpartType(accounts.get(0).getAcc1().getCounterpartType());

    for (Account account : accounts) {
      target.getRegLines().add(createRegLine(account));
    }
    return target;
  }

  private BIvcRegLine createRegLine(Account account) {
    BIvcRegLine line = new BIvcRegLine();
    line.setAcc1(Acc1BizConverter.getInstance().convert(account.getAcc1()));
    line.setAcc2(Acc2BizConverter.getInstance().convert(account.getAcc2()));
    line.setOriginTotal(TotalBizConverter.getInstance().convert(account.getOriginTotal()));
    line.setUnregTotal(TotalBizConverter.getInstance().convert(account.getTotal()));
    line.setTotal(TotalBizConverter.getInstance().convert(account.getTotal()));
    line.setTotalDiff(BigDecimal.ZERO);
    line.setTaxDiff(BigDecimal.ZERO);

    String defalutInvoiceType = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_DEFAULT_INVOICETYPE);
    if ("null".equals(defalutInvoiceType)) {
      defalutInvoiceType = null;
    }

    if (StringUtil.isNullOrBlank(defalutInvoiceType) == false) {
      line.setInvoiceType(defalutInvoiceType);
    }

    return line;
  }

  @Override
  public RPageData<BAccount> queryAccounts(IvcRegAccountFilter filter) throws Exception {
    if (filter == null)
      return null;

    QueryDefinition queryDef = buildAccountQueryDef(filter);
    if (queryDef == null)
      return null;

    if (filter.getExcepts() == null || filter.getExcepts().isEmpty()) {// 无需过滤
      QueryResult<Account> qr = getAccountService().query(queryDef);
      return RPageDataConverter.convert(qr, new AccountBizConverter());
    }
    queryDef.setPage(0);
    queryDef.setPageSize(0);
    List<Account> accounts = getAccountService().query(queryDef).getRecords();
    List<BAccount> values = new ArrayList<BAccount>();
    for (Account account : accounts) {
      boolean eliminate = false;
      for (Pair<String, String> except : filter.getExcepts()) {
        if (ObjectUtils.equals(except.getLeft(), account.getAcc1().getId())
            && ObjectUtils.equals(except.getRight(), account.getAcc2().getPayment() == null ? null
                : account.getAcc2().getPayment().getBillUuid())) {
          eliminate = true;
          break;
        }
      }
      if (eliminate == false) {
        values.add(AccountBizConverter.getInstance().convert(account));
      }
    }

    return RPageDataUtil.flip(values, filter.getPage(), filter.getPageSize());
  }

  @Override
  public RPageData<BAccountStatement> queryAccStatements(IvcRegAccountFilter filter)
      throws Exception {
    try {
      

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_NOTIN, Accounts.DISABLE_BILL_UUID,
          Accounts.NONE_BILL_UUID);
      queryDef.setPage(0);
      queryDef.setPageSize(0);

      List<Account> accounts = getAccountService().query(queryDef).getRecords();
      if (accounts == null || accounts.isEmpty())
        return null;

      /** 根据账单进行分组 */
      Map<String, BAccountStatement> map = new HashMap<String, BAccountStatement>();
      for (Account account : accounts) {
        BAccountStatement statement = map.get(account.getAcc2().getStatement().getBillUuid());
        if (statement == null) {
          statement = new BAccountStatement();
          statement.setUuid(account.getAcc2().getStatement().getBillUuid());
          statement.setBillNumber(account.getAcc2().getStatement().getBillNumber());
          statement.setAccountUnit(UCNBizConverter.getInstance().convert(
              account.getAcc1().getAccountUnit()));
          statement.setContract(UCNBizConverter.getInstance().convert(
              account.getAcc1().getContract()));
          statement.setCounterpart(BCounterpartConverter.getInstance().convert(
              account.getAcc1().getCounterpart(), account.getAcc1().getCounterpartType()));
          map.put(statement.getUuid(), statement);
        }
        statement.getAccounts().add(AccountBizConverter.getInstance().convert(account));
        statement.setTotal(statement.aggregateTotal());
      }

      FlecsQueryDefinition queryDef2 = new FlecsQueryDefinition();
      queryDef2.addCondition(Statements.CONDITION_UUID_IN, map.keySet().toArray());
      List<Statement> statements = getStatementService().query(queryDef2).getRecords();

      List<BAccountStatement> values = new ArrayList<BAccountStatement>();
      for (Statement statement : statements) {
        if (filter.getStatementAccountTime() != null
            && filter.getStatementAccountTime().include(statement.getAccountTime()) == false) {
          continue;
        }
        map.get(statement.getUuid()).setAccountTime(statement.getAccountTime());
        map.get(statement.getUuid()).setSettleNo(statement.getSettleNo());
        values.add(map.get(statement.getUuid()));
      }

      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        values = SettleUtil.sortAccountStatementList(values, filter.getOrders().get(0));
      } else {
        values = SettleUtil.sortAccountStatementList(values, new Order(
            BAccountStatement.ORDER_BY_FIELD_BILLNUMBER, OrderDir.desc));
      }
      return RPageDataUtil.flip(values, filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BAccountPayment> queryAccPayments(IvcRegAccountFilter filter) throws Exception {
    try {
      

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      queryDef.addCondition(Accounts.CONDITION_PAYMENT_UUID_NOTIN, Accounts.DISABLE_BILL_UUID,
          Accounts.NONE_BILL_UUID);
      queryDef.setPage(0);
      queryDef.setPageSize(0);

      List<Account> accounts = getAccountService().query(queryDef).getRecords();
      if (accounts == null || accounts.isEmpty())
        return null;

      /** 根据收款单进行分组 */
      Map<String, BAccountPayment> map = new HashMap<String, BAccountPayment>();
      for (Account account : accounts) {
        BAccountPayment payment = map.get(account.getAcc2().getPayment().getBillUuid());
        if (payment == null) {
          payment = new BAccountPayment();
          payment.setUuid(account.getAcc2().getPayment().getBillUuid());
          payment.setBillNumber(account.getAcc2().getPayment().getBillNumber());
          payment.setAccountUnit(UCNBizConverter.getInstance().convert(
              account.getAcc1().getAccountUnit()));
          payment.setContract(UCNBizConverter.getInstance()
              .convert(account.getAcc1().getContract()));
          payment.setCounterpart(BCounterpartConverter.getInstance().convert(
              account.getAcc1().getCounterpart(), account.getAcc1().getCounterpartType()));
          map.put(payment.getUuid(), payment);
        }
        payment.getAccounts().add(AccountBizConverter.getInstance().convert(account));
        payment.setTotal(payment.aggregateTotal());
      }

      List<BAccountPayment> values = new ArrayList<BAccountPayment>();
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        values = SettleUtil.sort(map.values(), filter.getOrders().get(0));
      } else {
        values = SettleUtil.sort(map.values(), new Order(BAccountPayment.ORDER_BY_FIELD_BILLNUMBER,
            OrderDir.desc));
      }
      return RPageDataUtil.flip(values, filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BAccountNotice> queryAccNotices(IvcRegAccountFilter filter) throws Exception {
    try {
      

      QueryDefinition queryDef = buildAccountQueryDef(filter);
      if (queryDef == null)
        return null;

      queryDef.addCondition(Accounts.CONDIRION_NOTICE);
      queryDef.setPage(0);
      queryDef.setPageSize(0);

      List<Account> accounts = getAccountService().query(queryDef).getRecords();
      if (accounts == null || accounts.isEmpty())
        return null;

      /** 根据账单进行分组 */
      Map<String, List<BAccount>> map = new HashMap<String, List<BAccount>>();
      for (Account account : accounts) {
        List<BAccount> list = map.get(account.getAcc2().getStatement().getBillUuid());
        if (list == null) {
          list = new ArrayList<BAccount>();
          map.put(account.getAcc2().getStatement().getBillUuid(), list);
        }
        list.add(AccountBizConverter.getInstance().convert(account));
      }

      FlecsQueryDefinition queryDef2 = new FlecsQueryDefinition();
      queryDef2.addCondition(PaymentNotices.CONDITION_STATEMENT_UUID_IN, map.keySet().toArray());
      queryDef2.addCondition(PaymentNotices.CONDITION_BIZSTATE_EQUALS, BizStates.EFFECT);
      queryDef2.getFetchParts().add(PaymentNotices.Fetch_PART_LINES);
      List<PaymentNotice> paymentNotices = getPaymentNoticeService().query(queryDef2).getRecords();

      List<BAccountNotice> values = new ArrayList<BAccountNotice>();
      for (PaymentNotice notice : paymentNotices) {
        BAccountNotice value = new BAccountNotice();
        value.setUuid(notice.getUuid());
        value.setBillNumber(notice.getBillNumber());
        value.setAccountUnit(UCNBizConverter.getInstance().convert(notice.getAccountUnit()));
        value.setCounterpart(BCounterpartConverter.getInstance().convert(notice.getCounterpart(),
            notice.getCounterpartType()));
        for (PaymentNoticeLine line : notice.getLines()) {
          if (map.containsKey(line.getStatement().getBillUuid()))
            value.getAccounts().addAll(map.get(line.getStatement().getBillUuid()));
        }
        value.setTotal(value.aggregateTotal());
        values.add(value);
      }
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        values = SettleUtil.sortAccountNoticeList(values, filter.getOrders().get(0));
      } else {
        values = SettleUtil.sortAccountNoticeList(values, new Order(
            BAccountNotice.ORDER_BY_FIELD_BILLNUMBER, OrderDir.desc));
      }
      return RPageDataUtil.flip(values, filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /** 构建有效账款简单通用查询定义 */
  private QueryDefinition buildAccountQueryDef() {
    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);
    queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS,
        DirectionType.receipt.getDirectionValue());
    queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);

    // 受项目限制
    List<String> list = getUserStores(getSessionUser().getId());
    if (list.isEmpty()) {
      return null;
    }
    queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_IN, list.toArray());

    // 合同授权组限制
    if (permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT)
        || permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)) {
      List<String> userGroupIds = getUserGroups(getSessionUser().getId());
      QueryCondition condition = new QueryCondition(Accounts.CONDITION_CONTRACT_PERMGROUP_ID_IN,
          userGroupIds.toArray());
      condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
      queryDef.getConditions().add(condition);
    }
    queryDef.addOrder(Accounts.ORDER_BY_SUBJECT, QueryOrderDirection.asc);

    return queryDef;
  }

  /** 查询有效账款 */
  private QueryDefinition buildAccountQueryDef(IvcRegAccountFilter filter) {
    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);

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

    if (StringUtil.isNullOrBlank(filter.getContract()) == false) {
      queryDef.addCondition(Accounts.CONDITION_CONTRACT_NUMBER_STARTWITH, filter.getContract());
    }
    if (StringUtil.isNullOrBlank(filter.getContractTitle()) == false) {
      queryDef.addCondition(Accounts.CONDITION_CONTRACT_NAME_LIKE, filter.getContractTitle());
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
    if (StringUtil.isNullOrBlank(filter.getStatementBillNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_STATEMENT_BILLNUMBER_STARTWITH,
          filter.getStatementBillNumber());
    }
    if (StringUtil.isNullOrBlank(filter.getPaymentBillNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_PAYMENT_BILLNUMBER_STARTWITH,
          filter.getPaymentBillNumber());
    }
    if (StringUtil.isNullOrBlank(filter.getNoticeBillNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_NOTICE_NUMBER_STARTWITH,
          filter.getNoticeBillNumber());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillType()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS, filter.getSourceBillType());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_NUMBER_STARTWITH,
          filter.getSourceBillNumber());
    }
    if (StringUtil.isNullOrBlank(filter.getSubject()) == false) {
      queryDef.addCondition(Accounts.CONDITION_SUBJECT_UUID_EQUALS, filter.getSubject());
    }
    if (filter.getAccDirection() != null) {
      queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, filter.getAccDirection());
    }
    // 此处已做修改，账款id可能被多次收款，应该分开开票。
    // if (filter.getExcepts() != null && filter.getExcepts().isEmpty() ==
    // false) {
    // queryDef.addCondition(Accounts.CONDITION_ACCID_NOTIN,
    // filter.getExcepts().toArray());
    // }
    if (StringUtil.isNullOrBlank(filter.getInvoiceRegUuid()) == false) {
      queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, filter.getInvoiceRegUuid());
    }
    queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);

    String orderField = Accounts.ORDER_BY_SUBJECT;
    QueryOrderDirection dir = QueryOrderDirection.asc;
    if (filter.getOrders().isEmpty() == false) {
      Order order = filter.getOrders().get(0);
      dir = order.getDir() == OrderDir.asc ? QueryOrderDirection.asc : QueryOrderDirection.desc;
      if (BAccount.ORDER_BY_FIELD_ACCOUNTUNIT.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_ACCOUNTUNIT_CODE;
      } else if (BAccount.ORDER_BY_FIELD_BEGINTIME.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_BEGINTIME;
      } else if (BAccount.ORDER_BY_FIELD_CONTRACT.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_CONTRACT;
      } else if (BAccount.ORDER_BY_FIELD_CONTRACTTITLE.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_CONTRACT_NAME;
      } else if (BAccount.ORDER_BY_FIELD_COUNTERPART.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_COUNTERPART;
      } else if (BAccount.ORDER_BY_FIELD_SOURCEBILLNUMBER.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_SOURCEBILLNUMBER;
      } else if (BAccount.ORDER_BY_FIELD_SOURCEBILLTYPE.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_SOURCEBILLTYPE;
      } else if (BAccount.ORDER_BY_FIELD_SUBJECT.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_SUBJECT;
      } else if (BAccount.ORDER_BY_FIELD_TOTAL.equals(order.getFieldName())) {
        orderField = Accounts.ORDER_BY_TOTAL;
      }
    }
    queryDef.addOrder(orderField, dir);
    if (Accounts.ORDER_BY_SUBJECT.equals(orderField) == false) {
      queryDef.addOrder(Accounts.ORDER_BY_SUBJECT, QueryOrderDirection.asc);
    }

    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());

    return queryDef;
  }

  @Override
  public void saveOptions(BInvoiceRegOptions config) throws Exception {
    try {
      if (config == null)
        return;

      getOptionService().setOption(getObjectName(), InvoiceRegs.OPTION_REC_ALLOW_SPLIT_REG,
          String.valueOf(config.isAllowSplitReg()), new BeanOperateContext(getSessionUser()));
      getOptionService().setOption(getObjectName(), InvoiceRegs.OPTION_REC_USEINVOICESTOCK,
          String.valueOf(config.isUseInvoiceStock()), new BeanOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @Override
  protected RPageData<BInvoiceReg> doQuery(FlecsQueryDef definition) throws Exception {
    try {
      
      FlecsQueryDefinition queryDef = buildQueryDefinition(definition,
          InvoiceRegQueryBuilder.getInstance());
      QueryResult<InvoiceReg> qr = getInvoiceRegService().query(queryDef);
      return RPageDataConverter.convert(qr, new InvoiceRegBizConverter());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected String doSave(BInvoiceReg entity) throws Exception {
    String extinvSystemRequired = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.EXTINVSYSTEM_ENABLED);
    if (Boolean.valueOf(extinvSystemRequired) == true) {
      entity.setUseInvoiceStock(false);
      entity.setInvoiceCode("-");
      entity.setInvoiceNumber("-");
      List<BIvcRegLine> regLines = entity.getRegLines();
      for (BIvcRegLine bIvcRegLine : regLines) {
        bIvcRegLine.setInvoiceCode("-");
        bIvcRegLine.setInvoiceNumber("-");
        bIvcRegLine.setInvoiceType("-");
      }
    }
    InvoiceReg target = new BizInvoiceRegConverter().convert(entity);
    return getInvoiceRegService().save(target, new BeanOperateContext(getSessionUser()));
  }

  @Override
  protected BInvoiceReg doGet(String id) throws Exception {
    if (id == null)
      return null;
    try {
      InvoiceReg entity = getInvoiceRegService().get(id);
      BInvoiceReg target = new InvoiceRegBizConverter().convert(entity);
      decorate(target);
      return target;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  /**
   * 更新登记单中的本次应开票金额
   */
  private void decorate(BInvoiceReg entity) {
    if (entity == null || BizStates.INEFFECT.equals(entity.getBizState()) == false)// 只有未生效的需要更新本次开票金额
      return;
    if (StringUtil.toBoolean(
        getOptionService().getOption(getObjectName(), InvoiceRegs.OPTION_REC_ALLOW_SPLIT_REG),
        false) == false) {// 不允许多次登记时，不需要更新。
      return;
    }

    Set<String> accIds = new HashSet<String>();
    List<Pair<String, String>> accPairs = new ArrayList<Pair<String, String>>();
    for (BIvcRegLine line : entity.getRegLines()) {
      accIds.add(line.getAcc1().getId());
      accPairs.add(new Pair<String, String>(line.getAcc1().getId(),
          line.getAcc2().getPayment() == null ? null : line.getAcc2().getPayment().getBillUuid()));
    }

    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACCID_IN, accIds.toArray());
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);
    queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, entity.getUuid());
    queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    List<Account> accounts = getAccountService().query(queryDef).getRecords();
    Map<String, Total> map = new HashMap<String, Total>();
    for (Account account : accounts) {
      String key = account.getAcc1().getId()
          + (account.getAcc2().getPayment() == null ? null : account.getAcc2().getPayment()
              .getBillUuid());// 账款id+收款单作为key
      Total total = map.get(key) == null ? Total.zero() : map.get(key);
      map.put(key, total.add(account.getTotal()));
    }

    for (BIvcRegLine line : entity.getRegLines()) {
      String key = line.getAcc1().getId()
          + (line.getAcc2().getPayment() == null ? null : line.getAcc2().getPayment().getBillUuid());// 账款id+收款单作为key
      Total total = map.get(key) == null ? Total.zero() : map.get(key);
      line.setUnregTotal(TotalBizConverter.getInstance().convert(total));
    }
  }

  @Override
  protected void doChangeBizState(String uuid, long version, String state) throws Exception {
    getInvoiceRegService().changeBizState(uuid, version, state,
        ConvertHelper.getOperateContext(getSessionUser()));
  }

  @Override
  protected String getServiceBeanId() {
    return com.hd123.m3.account.service.ivc.reg.InvoiceRegService.DEFAULT_CONTEXT_ID;
  }

  protected UserService getUserService() {
    return getAppCtx().getBean(UserService.DEFAULT_CONTEXT_ID, UserService.class);
  }

  protected AccountOptionService getOptionService() {
    return getAppCtx().getBean(AccountOptionService.class);
  }

  @Override
  protected String getObjectCaption() {
    return R.R.billCaption();
  }

  @Override
  public String getObjectName() {
    return InvoiceRegs.OBJECT_NAME_RECEIPT;
  }

  private Map<String, String> getInvoiceTypes() {
    Map<String, String> mapInvoiceTypes = new HashMap<String, String>();
    try {
      for (BInvoiceType type : InvoiceTypeUtils.getAll()) {
        mapInvoiceTypes.put(type.getCode(), type.getName());
      }
      return mapInvoiceTypes;
    } catch (Exception e) {
      return null;
    }
  }

  /** 加载来源单据类型 */
  private List<BillType> getBillTypes() {
    List<BillType> result = new ArrayList<BillType>();
    List<BillType> types = getBillTypeService().getAccountTypes();
    for (BillType type : types) {
      if (type.getDirection() == 0 || type.getDirection() == Direction.RECEIPT) {
        result.add(type);
      }
    }
    return result;
  }

  private String getScale() {
    String scale = getOptionService().getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE);
    return scale == null ? "2" : scale;
  }

  private String getRoundingMode() {
    String roundingMode = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_ROUNDING_MODE);
    return roundingMode == null ? RoundingMode.HALF_UP.name() : roundingMode;
  }

  private com.hd123.m3.account.service.ivc.reg.InvoiceRegService getInvoiceRegService() {
    return getAppCtx().getBean(com.hd123.m3.account.service.ivc.reg.InvoiceRegService.class);
  }

  private AccountService getAccountService() {
    return getAppCtx().getBean(AccountService.class);
  }

  private PaymentNoticeService getPaymentNoticeService() {
    return getAppCtx().getBean(PaymentNoticeService.class);
  }

  private PaymentService getPaymentService() {
    return getAppCtx().getBean(PaymentService.class);
  }

  private StatementService getStatementService() {
    return getAppCtx().getBean(StatementService.class);
  }

  private BillTypeService getBillTypeService() {
    return getAppCtx().getBean(BillTypeService.class);
  }

  public static interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("发票领用单")
    String billCaption();
  }

  @Override
  public void print(String uuid, long version) throws Exception {
    getInvoiceRegService().print(uuid, version, ConvertHelper.getOperateContext(getSessionUser()));
  }

}
