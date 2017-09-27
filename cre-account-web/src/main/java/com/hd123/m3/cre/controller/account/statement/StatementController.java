/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	StatementController.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月6日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.commons.CounterpartType;
import com.hd123.m3.account.commons.SourceBill;
import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.acc.Acc2;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.accounting.AccountingService;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.contract.model.settle.Settlement;
import com.hd123.m3.account.service.invoice.InvoiceReg;
import com.hd123.m3.account.service.invoice.InvoiceRegService;
import com.hd123.m3.account.service.invoice.InvoiceRegs;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.payment.PaymentService;
import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayals;
import com.hd123.m3.account.service.statement.SettleState;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementAccRange;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.StatementSubjectAccount;
import com.hd123.m3.account.service.statement.StatementTotal;
import com.hd123.m3.account.service.statement.StatementType;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.account.service.statement.adjust.StatementAdjustService;
import com.hd123.m3.account.service.statement.adjust.StatementAdjusts;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.date.DateRange;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.flow.BizFlow;
import com.hd123.m3.commons.biz.option.PermOptionService;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.QueryDefinitionUtil;
import com.hd123.m3.commons.biz.util.EntityUtil;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.commons.servlet.permed.DataPermedHelper;
import com.hd123.m3.commons.servlet.permed.UserGroup;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.m3.cre.controller.account.common.util.SubjectUsageUtils;
import com.hd123.m3.cre.controller.account.statement.builder.AccountQueryBuilder;
import com.hd123.m3.cre.controller.account.statement.builder.AccountSettleQueryBuilder;
import com.hd123.m3.cre.controller.account.statement.builder.StatementQueryBuilder;
import com.hd123.m3.cre.controller.account.statement.convert.BAccountSettleLogConverter;
import com.hd123.m3.cre.controller.account.statement.convert.BStatementConverter;
import com.hd123.m3.cre.controller.account.statement.convert.StatementAccDetailBizConverter;
import com.hd123.m3.cre.controller.account.statement.convert.StatementBizConverter;
import com.hd123.m3.cre.controller.account.statement.email.EmailSender;
import com.hd123.m3.cre.controller.account.statement.email.EmailSenderService;
import com.hd123.m3.cre.controller.account.statement.email.config.EmailConfig;
import com.hd123.m3.cre.controller.account.statement.model.BAccountSettleLog;
import com.hd123.m3.cre.controller.account.statement.model.BMoreBtnPerm;
import com.hd123.m3.cre.controller.account.statement.model.BStatement;
import com.hd123.m3.cre.controller.account.statement.model.BStatementAccDetail;
import com.hd123.m3.cre.controller.account.statement.model.BStatementSumLine;
import com.hd123.m3.investment.service.tenant.tenant.TenantContactInfo;
import com.hd123.m3.investment.service.tenant.tenant.TenantService;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 账单控制器
 * 
 * @author chenganbang
 * 
 */
@Controller
@RequestMapping("account/statement/*")
public class StatementController extends BizFlowModuleController {
  public static final String RESOURCE_PATH = "account/receivable/statement";
  private static final String BILL_TYPE_LIST = "billTypeList";
  private static final String FILTER_STATEMENT_STATE = "statementState";
  private static final String SCALE = "scale";
  private static final String ROUNDING_MODE = "roundingMode";

  private Set<String> permissions = new HashSet<String>();

  @Autowired
  private ContractService contractService;
  @Autowired
  private PermOptionService permOptionService;
  @Autowired
  private StatementService statementService;
  @Autowired
  private BillTypeService billTypeService;
  @Autowired
  private InvoiceRegService payIvcService;// 付款
  @Autowired
  private com.hd123.m3.account.service.ivc.reg.InvoiceRegService receiptIvcService;// 收款
  @Autowired
  private PaymentService paymentService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private StatementAdjustService adjustService;
  @Autowired
  private AccountOptionService optionService;
  @Autowired
  private DataPermedHelper dataPermedHelper;
  @Autowired
  private AccountingService accountingService;
  @Autowired
  private TenantService tenantService;
  @Autowired
  private SubjectService subjectService;
  @Autowired
  private EmailSenderService emailSenderService;
  @Autowired
  private EmailSender emailSender;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);

    Set<String> resourceIds = new HashSet<String>();
    resourceIds.add(AccountPermResourceDef.RESOURCE_STATMENT);
    resourceIds.add(AccountPermResourceDef.RESOURCE_ACCOUNTSETTLE);
    resourceIds.add(AccountPermResourceDef.RESOURCE_PAYINVOICEREG);
    resourceIds.add(AccountPermResourceDef.RESOURCE_RECINVOICEREG);
    resourceIds.add(AccountPermResourceDef.RESOURCE_PAYMENT);
    resourceIds.add(AccountPermResourceDef.RESOURCE_RECEIPT);
    resourceIds.add(AccountPermResourceDef.RESOURCE_STATEMENTADJUST);
    resourceIds.add(AccountPermResourceDef.RESOURCE_ACCOUNTDEFRAYAL);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        resourceIds));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put(BILL_TYPE_LIST, JsonUtil.objectToJson(queryAllTypes()));
    moduleContext.put(SCALE, getScale());
    moduleContext.put(ROUNDING_MODE, getRoundingMode());
  }

  @Override
  protected void buildSummary(SummaryQueryResult result, QueryFilter queryFilter) {
    BizFlow flow = getBizFlowService().getBizFlow(getObjectName());
    if (flow == null) {
      return;
    }
    FlecsQueryDefinition definition = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    StatementTotal total = statementService.queryTotal(definition);
    result.getSummary().put("receiptedTotal", total.getReceiptedTotal());
    result.getSummary().put("receiptTotal", total.getReceiptTotal());
    result.getSummary().put(
        "remainTotal",
        total.getReceiptedTotal() != null && total.getReceiptTotal() != null ? total
            .getReceiptTotal().subtract(total.getReceiptedTotal())
            .setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);

    // 不限状态
    queryFilter.setPageSize(1);
    queryFilter.getFilter().put(FILTER_STATEMENT_STATE, null);
    result.getSummary().put("all", getEntityService().query(definition).getRecordCount());
    Map<String, Object> filter = queryFilter.getFilter() == null ? new HashMap<String, Object>()
        : queryFilter.getFilter();
    for (com.hd123.m3.commons.biz.flow.BizState bizState : flow.getStates()) {
      String tag = bizState.getName();
      // 未生效和已作废状态
      if ("ineffect".equals(tag) || "aborted".equals(tag)) {
        filter.put(FILTER_STATEMENT_STATE, tag);
        definition = getQueryBuilder().build(queryFilter, getObjectName(), getSessionUserId());
        result.getSummary().put(tag, getEntityService().query(definition).getRecordCount());
      } else if ("effect".equals(tag)) {
        // 已生效未结款
        filter.put(FILTER_STATEMENT_STATE, "effectAndUnsettled");
        definition = getQueryBuilder().build(queryFilter, getObjectName(), getSessionUserId());
        definition.addCondition(Statements.CONDITION_SETTLESTATE_EQUALS, SettleState.initial);
        result.getSummary().put("effectAndUnsettled",
            getEntityService().query(definition).getRecordCount());
        // 已生效部分结款
        filter.put(FILTER_STATEMENT_STATE, "effectAndPartSettled");
        definition = getQueryBuilder().build(queryFilter, getObjectName(), getSessionUserId());
        definition
            .addCondition(Statements.CONDITION_SETTLESTATE_EQUALS, SettleState.partialSettled);
        result.getSummary().put("effectAndPartSettled",
            getEntityService().query(definition).getRecordCount());
        // 已生效已结款
        filter.put(FILTER_STATEMENT_STATE, "effectAndSettled");
        definition = getQueryBuilder().build(queryFilter, getObjectName(), getSessionUserId());
        definition.addCondition(Statements.CONDITION_SETTLESTATE_EQUALS, SettleState.settled);
        result.getSummary().put("effectAndSettled",
            getEntityService().query(definition).getRecordCount());
      }
    }

  }

  @Override
  protected void decorateRecords(SummaryQueryResult queryResult) {
    if (queryResult != null) {
      List<Statement> records = queryResult.getRecords();
      List<BStatement> list = new ArrayList<BStatement>(records.size());
      for (Statement record : records) {
        BStatement statement = BStatementConverter.getInstance().convert(record, false);
        list.add(statement);
      }
      queryResult.setRecords(list);
    }
  }

  /**
   * 查询账单出账日志
   * 
   * @param queryFilter
   * @return
   * @throws Exception
   */
  @RequestMapping("queryAccountSettleLog")
  @ResponseBody
  public QueryResult<BAccountSettleLog> queryAccountSettleLog(@RequestBody QueryFilter queryFilter)
      throws Exception {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    // 启用授权组
    List<UserGroup> userGroups = dataPermedHelper.getUserGroups(getSessionUserId());
    queryDef.addCondition(AccountDefrayals.CONDITION_PERM_CONTRACT_GROUP_ID_IN,
        EntityUtil.toUuids(userGroups).toArray());
    // 受项目限制
    List<String> list = getUserStores(getSessionUser().getId());
    if (list.isEmpty()) {
      return new QueryResult<BAccountSettleLog>();
    }
    queryDef.addCondition(Contracts.CONDITION_ACCOUNT_UNIT_UUID_IN, list.toArray());
    queryDef.setPage(queryFilter.getCurrentPage());
    queryDef.setPageSize(queryFilter.getPageSize());

    queryDef = AccountSettleQueryBuilder.getInstance().buildConditions(queryDef,
        queryFilter.getFilter());
    queryDef.setOrders(AccountSettleQueryBuilder.getInstance().buildQueryOrders(
        queryFilter.getSorts()));

    QueryResult<AccountSettle> qr = contractService.queryAccountSettle(queryDef);
    return BAccountSettleLogConverter.getInstance().convert(qr);
  }

  @RequestMapping(value = "isMaxSettled", method = RequestMethod.GET)
  @ResponseBody
  public int isMaxSettled(@RequestParam("uuid") String accountSettleUuid) throws Exception {
    // 0表示否；1表示是
    if (StringUtil.isNullOrBlank(accountSettleUuid)) {
      return 0;
    }
    try {
      boolean maxSettled = statementService.isSettleMax(accountSettleUuid);
      return maxSettled ? 1 : 0;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping(value = "cleanSettle", method = RequestMethod.GET)
  @ResponseBody
  public void cleanSettle(@RequestParam("uuid") String accountSettleUuid) throws Exception {
    // 0表示否；1表示是
    if (StringUtil.isNullOrBlank(accountSettleUuid)) {
      return;
    }
    try {
      accountingService.cleanEmptySettle(accountSettleUuid);
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(json)) {
      return null;
    }

    BStatement entity = JsonUtil.jsonToObject(json, BStatement.class);
    BeanOperateContext operCtx = new BeanOperateContext(getSessionUser());
    entity = StatementBizConverter.getInstance().convert(entity);
    try {
      preHandlerLines(entity, operCtx);
      String uuid = statementService.save(entity, operCtx);
      return uuid;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  @ResponseBody
  public BStatement doLoad(@RequestParam("uuid") String uuid) throws Exception {
    BStatement result = new BStatement();
    Statement statement = statementService.get(uuid);
    if (statement == null) {
      statement = statementService.getByNumber(uuid, Statement.FETCH_PART_WHOLE);
    }
    validEntityPerm(statement);
    if (statement != null) {
      result = BStatementConverter.getInstance().convert(statement, true);
      aggregate(result);
      decorateNormal(result);
      initMoreBtns(result);
    }
    return result;
  }

  public BStatement loadByBillNumber(String billNumber) throws Exception {
    BStatement result = new BStatement();
    Statement statement = statementService.getByNumber(billNumber, Statement.FETCH_PART_WHOLE);
    if (statement != null) {
      result = BStatementConverter.getInstance().convert(statement, true);
    }
    return result;
  }

  @RequestMapping(value = "sendEmail", method = RequestMethod.GET)
  @ResponseStatus(value = HttpStatus.OK)
  public void sendEmail(@RequestParam("billNumber") String billNumber) throws Exception {

    BStatement statement = loadByBillNumber(billNumber);
    EmailConfig config = new EmailConfig();
    // 收件人列表
    List<InternetAddress> users = getAllUsingUserInfo(statement.getCounterpart().getUuid());

    emailSenderService.beforeSendEmail(billNumber, "emailTemplate/statementTemplate.docx",
        "c://temp/statement.docx", 1, "c://temp/statement.pdf");
    emailSender.sendEmail("c://temp/statement.pdf", statement.getSettleNo() + "-"
        + statement.getContract().getName() + ".pdf", config, users);

    statementService.markSendEmail(statement.getUuid(), statement.getVersion(),
        new BeanOperateContext(getSessionUser()));
  }

  /**
   * 取到商户联系方式
   * 
   * @param uuid
   * @return
   * @throws AddressException
   * @throws M3ServiceException
   */
  private List<InternetAddress> getAllUsingUserInfo(String uuid) throws AddressException,
      M3ServiceException {
    List<InternetAddress> addresses = new ArrayList<InternetAddress>();

    List<TenantContactInfo> contactInfos = tenantService.get(uuid).getContactInfos();
    if (contactInfos.size() == 0) {
      throw new M3ServiceException("系统中无可用邮件发送者，邮件发送失败");
    }
    for (TenantContactInfo tenantContactInfo : contactInfos) {
      String email = tenantContactInfo.getEmail();
      if (StringUtil.isNullOrBlank(email)) {
        continue;
      } else {
        addresses.add(new InternetAddress(email));
      }
    }
    if (addresses.size() == 0) {
      throw new M3ServiceException("系统中无可用邮件发送者，邮件发送失败");
    } else {
      return addresses;
    }
  }

  /**
   * 查询所有的单据类型
   * 
   * @return
   */
  @RequestMapping("queryAllBillType")
  @ResponseBody
  public List<BillType> queryAllTypes() {
    List<BillType> result = billTypeService.getAllTypes();
    if (result == null) {
      result = new ArrayList<BillType>();
    }
    return result;
  }

  /**
   * 根据账款id获取账款明细
   * 
   * @param accId
   * @param isActive
   * @return
   */
  @RequestMapping("getAccountDetail")
  @ResponseBody
  public BStatementAccDetail getAccountDetail(@RequestParam("accId") String accId,
      @RequestParam("isActive") boolean isActive) {
    return StatementAccDetailBizConverter.getInstance().convert(
        accountService.getDetail(accId, isActive));
  }

  @RequestMapping("queryAccount")
  @ResponseBody
  public SummaryQueryResult<StatementLine> queryAccount(@RequestBody QueryFilter queryFilter) {
    SummaryQueryResult<StatementLine> result = new SummaryQueryResult<StatementLine>();
    QueryDefinition definition = AccountQueryBuilder.getInstance().buildDefinition(queryFilter);
    QueryResult<Account> accounts = accountService.query(definition);

    for (Account account : accounts.getRecords()) {
      StatementLine line = new StatementLine();
      line.setUuid(account.getUuid());
      line.setAcc1(account.getAcc1());
      line.setAcc2(account.getAcc2());
      line.setTotal(account.getTotal());
      result.getRecords().add(line);
    }
    result.setPage(accounts.getPage());
    result.setPageCount(accounts.getPageCount());
    result.setRecordCount(accounts.getRecordCount());
    return result;
  }

  // ------------------------------------------------------------------------------------
  private void preHandlerLines(BStatement entity, BeanOperateContext operCtx) {

    if (!StatementType.patch.name().equals(entity.getType().name())) {
      for (StatementLine line : entity.getLines()) {
        line.getAcc2().setLocker(Accounts.NONE_LOCKER);
      }
      return;
    }

    // 注意此处将 Acc1中的SourceBill赋值
    BillType statementBillType = billTypeService.getByClassName(Statement.class.getName());
    SourceBill sourceBill = new SourceBill(Accounts.NONE_BILL_UUID, Accounts.NONE_BILL_NUMBER,
        statementBillType.getName());

    // 为账单周期accountRange赋值
    Date beginDate = null;
    Date endDate = null;
    for (StatementLine line : entity.getLines()) {
      if (line.getAcc1() == null || line.getAcc1().getSubject() == null
          || line.getAcc1().getSubject().getUuid() == null) {
        continue;
      }
      line.setFreeTotal(Total.zero());
      repairAcc1(entity, line, sourceBill);
      repairAcc2(entity, line);
      if (beginDate != null) {
        beginDate = DateRange.min(beginDate, line.getAcc1().getBeginTime());
      } else {
        beginDate = line.getAcc1().getBeginTime();
      }
      if (endDate != null) {
        endDate = DateRange.max(endDate, line.getAcc1().getEndTime());
      } else {
        endDate = line.getAcc1().getEndTime();
      }
    }
    entity.setAccountTime(operCtx.getTime());
    entity.getRanges().clear();
    StatementAccRange range = new StatementAccRange();
    range.setDateRange(new DateRange(beginDate, endDate));
    entity.getRanges().add(range);
    // 付款免租金额、收款免租金额=0
    entity.setFreePayTotal(Total.zero());
    entity.setFreeReceiptTotal(Total.zero());
  }

  private void repairAcc1(BStatement entity, StatementLine line, SourceBill sourceBill) {
    line.getAcc1().setSourceBill(sourceBill);
    line.getAcc1().setAccountUnit(entity.getAccountUnit());
    line.getAcc1().setContract(entity.getContract());
    line.getAcc1().setCounterpart(entity.getCounterpart());
    line.getAcc1().setCounterpartType(entity.getCounterpartType());
  }

  private void repairAcc2(BStatement entity, StatementLine line) {
    // 锁定者
    line.getAcc2().setLocker(Accounts.NONE_LOCKER);
    // 收付款信息
    line.getAcc2().setPayment(Accounts.NONE_BILL);
    // 账单信息
    line.getAcc2().setStatement(Accounts.NONE_BILL);
  }

  private void aggregate(BStatement entity) {
    // 按科目分组
    Collection<List> group = CollectionUtil.group(entity.getLines(),
        new Comparator<StatementLine>() {
          @Override
          public int compare(StatementLine o1, StatementLine o2) {
            if (ObjectUtils.equals(o1.getAcc1().getSubject(), o2.getAcc1().getSubject())
                && o1.getAcc1().getDirection() == o2.getAcc1().getDirection())
              return 0;
            else
              return 1;
          }
        });

    List<StatementSubjectAccount> accounts = statementService.getStatementAccount(entity.getUuid());
    for (List<StatementLine> list : group) {
      StatementLine line = list.get(0);
      if (line.getAcc1() == null || line.getAcc1().getSubject() == null) {
        continue;
      }
      BStatementSumLine sl = new BStatementSumLine();
      sl.setUuid(line.getUuid());
      sl.setSubject(line.getAcc1().getSubject());
      sl.setDirection(line.getAcc1().getDirection());
      if (line.getAccSrcType() != null) {
        sl.setAccSrcType(line.getAccSrcType().name());
      }

      StatementSubjectAccount account = findAccount(accounts, sl.getSubject().getUuid(),
          sl.getDirection());

      if (account != null) {
        sl.setSettled(account.getSettled() == null ? BigDecimal.ZERO : account.getSettled()
            .getTotal());
        sl.setInvoiced(account.getInvoiced() == null ? BigDecimal.ZERO : account.getInvoiced()
            .add(account.getInvoiceAdj()).getTotal());
      }
      // 对账款明细进行起始日期升序
      Collections.sort(list, new Comparator<StatementLine>() {
        @Override
        public int compare(StatementLine o1, StatementLine o2) {
          if (o1.getAcc1().getBeginTime() == null || o2.getAcc1().getBeginTime() == null)
            return 0;
          return o1.getAcc1().getBeginTime().before(o2.getAcc1().getBeginTime()) ? 0 : 1;
        }
      });

      Total total = Total.zero();
      BigDecimal invoiceTotal = BigDecimal.ZERO;
      for (StatementLine l : list) {
        total = total.add(l.getTotal());
        if (getInvoice(l.getAcc2()) && l.getTotal() != null && l.getTotal().getTotal() != null) {
          invoiceTotal = invoiceTotal.add(l.getTotal().getTotal());
        }
        sl.setTaxRate(l.getAcc1().getTaxRate());
        sl.getLines().add(l);
      }
      sl.setTotal(total);
      sl.setNeedInvoice(invoiceTotal);
      // 计算欠款金额
      if (sl.getTotal() == null || sl.getTotal().getTotal() == null) {
        sl.setOwedAmount(sl.getSettled());
      } else if (sl.getSettled() == null) {
        sl.setOwedAmount(sl.getTotal().getTotal());
      } else {
        sl.setOwedAmount(sl.getTotal().getTotal().subtract(sl.getSettled()));
      }
      entity.getSumLines().add(sl);
    }

    // 排序-科目代码升序
    Collections.sort(entity.getSumLines(), new Comparator<BStatementSumLine>() {

      @Override
      public int compare(BStatementSumLine o1, BStatementSumLine o2) {
        if (o1.getSubject().getCode() == null || o2.getSubject().getCode() == null)
          return 0;
        return o1.getSubject().getCode().compareTo(o2.getSubject().getCode());
      }
    });
  }

  /**
   * 是否开发票
   * 
   * @param acc2
   * @return
   */
  public boolean getInvoice(Acc2 acc2) {
    if (acc2 == null || acc2.getInvoice() == null || "~".equals(acc2.getInvoice().getBillUuid())) {
      return false;
    } else {
      return true;
    }
  }

  private StatementSubjectAccount findAccount(List<StatementSubjectAccount> accounts,
      String subjectUuid, int direction) {
    if (accounts == null || accounts.isEmpty()) {
      return null;
    }
    for (StatementSubjectAccount acc : accounts) {
      if (ObjectUtils.equals(subjectUuid, acc.getSubject()) && direction == acc.getDirection()) {
        return acc;
      }
    }
    return null;
  }

  /**
   * 初始化"更多操作"的按钮权限
   * 
   * @param entity
   */
  private void initMoreBtns(BStatement entity) {
    entity.getMoreBtn().add(initReceiptIvcVis(entity));
    entity.getMoreBtn().add(initReceiptVis(entity));
    // entity.getMoreBtn().add(initPayIvcVis(entity));
    entity.getMoreBtn().add(initPayVis(entity));
  }

  private Set<String> getPermissions() {
    Set<Permission> permissions = getAuthorizedPermissions(getSessionUser());
    this.permissions.clear();
    for (Permission permission : permissions) {
      this.permissions.add(permission.toString());
    }
    return this.permissions;
  }

  /**
   * 付款开票登记权限
   * 
   * @param entity
   * @return
   */
  @RequestMapping("initPayIvcVis")
  @ResponseBody
  public BMoreBtnPerm initPayIvcVis(@RequestBody BStatement entity) {
    if (permissions == null || permissions.isEmpty()) {
      permissions = getPermissions();
    }
    // 付款发票登记按钮
    BMoreBtnPerm payIvcBtn = new BMoreBtnPerm();
    if (entity == null) {
      return payIvcBtn;
    }
    boolean isProprietor = CounterpartType.PROPRIETOR.equals(entity.getCounterpartType());
    boolean isEffect = "effect".equals(entity.getBizState());
    boolean hasPayIvc = isEffect
        && permissions.contains(StatementPermDef.RESOURCE_PAYINVOICEREG_CREATE) && !isProprietor;
    // 根据科目收付方向决定收款发票登记
    hasPayIvc = hasPayIvc && subjectDir(entity.getLines(), -1);

    if (hasPayIvc) {
      if (entity.getIvcPayed().compareTo(entity.getIvcPayTotal().getTotal()) == 0
          && entity.getIvcReceipted().compareTo(entity.getIvcReceiptTotal().getTotal()) == 0) {
        // 当实收开票金额等于应收开票金额，且实付开票金额等于应付开票金额，则付款发票登记按钮隐藏。
        hasPayIvc = false;
      } else {
        // 存在未生效的全部收取的付款单，点击付款发票登记按钮，在当前页面报错。
        List<Account> accounts = getAccountByStatement(entity.getUuid(), -1, false);
        if (accounts.isEmpty()) {
          payIvcBtn.setClickable(false);
          payIvcBtn.setErrMsg(R.R.accountEmpty());
        }
      }
    }
    payIvcBtn.setType("payIvcBtnId");
    payIvcBtn.setVisible(hasPayIvc);
    return payIvcBtn;
  }

  /**
   * 付款按钮权限
   * 
   * @param entity
   * @return
   */
  @RequestMapping("initPayVis")
  @ResponseBody
  public BMoreBtnPerm initPayVis(@RequestBody BStatement entity) {
    if (permissions == null || permissions.isEmpty()) {
      permissions = getPermissions();
    }
    // 付款按钮
    BMoreBtnPerm payBtn = new BMoreBtnPerm();
    if (entity == null) {
      return payBtn;
    }
    boolean isEffect = "effect".equals(entity.getBizState());
    boolean hasPay = isEffect && permissions.contains(StatementPermDef.RESOURCE_PAYMENT_CREATE);

    if (entity.getPayTotal().getTotal().compareTo(entity.getReceiptTotal().getTotal()) == 0
        && entity.getPayTotal().getTotal().compareTo(BigDecimal.ZERO) == 0) {
      // 空账单
      hasPay = false;
    } else if (entity.getPayed().compareTo(entity.getPayTotal().getTotal()) == 0
        && entity.getReceipted().compareTo(entity.getReceiptTotal().getTotal()) == 0) {
      // 只有实收金额等于应收金额，实付金额等于应付金额的时候，付款按钮隐藏。
      hasPay = false;
    } else {
      // 存在未生效的发票登记单，点击付款按钮，当前页面报错
      InvoiceReg payIvc = getPayIvc(entity.getBillNumber());
      if (payIvc != null) {
        payBtn.setClickable(false);
        payBtn.setErrMsg("存在未生效的付款发票登记单：" + payIvc.getBillNumber());
      } else {
        com.hd123.m3.account.service.ivc.reg.InvoiceReg receiptIvc = getReceiptIvc(entity
            .getBillNumber());
        if (receiptIvc != null) {
          payBtn.setClickable(false);
          payBtn.setErrMsg("存在未生效的收款发票登记单：" + receiptIvc.getBillNumber());
        }
      }
    }

    payBtn.setType("payBtnId");
    payBtn.setVisible(hasPay);
    return payBtn;
  }

  /**
   * 收款发票登记权限
   * 
   * @param entity
   * @return
   */
  @RequestMapping("initReceiptIvcVis")
  @ResponseBody
  public BMoreBtnPerm initReceiptIvcVis(@RequestBody BStatement entity) {
    if (permissions == null || permissions.isEmpty()) {
      permissions = getPermissions();
    }
    BMoreBtnPerm receiptIvcBtn = new BMoreBtnPerm();
    if (entity == null) {
      return receiptIvcBtn;
    }
    // 基本条件
    boolean isProprietor = CounterpartType.PROPRIETOR.equals(entity.getCounterpartType());
    boolean isEffect = "effect".equals(entity.getBizState());
    boolean hasReceiptIvc = isEffect
        && permissions.contains(StatementPermDef.RESOURCE_RECINVOICEREG_CREATE) && !isProprietor;
    // 根据科目收付方向决定收款发票登记
    hasReceiptIvc = hasReceiptIvc && subjectDir(entity.getLines(), 1);

    if (hasReceiptIvc) {
      if (entity.getIvcReceipted().compareTo(entity.getIvcReceiptTotal().getTotal()) == 0
          && entity.getIvcPayed().compareTo(entity.getIvcPayTotal().getTotal()) == 0) {
        // 当实收开票金额等于应收开票金额，实付开票金额等于应付开票金额，则收款发票登记按钮隐藏。
        hasReceiptIvc = false;
      } else {
        // 当实收开票金额不等于应收开票金额，且存在未生效的全部收取的收款单，点击收款发票登记按钮，在当前页面报错。
        List<Account> accounts = getAccountByStatement(entity.getUuid(), 1, false);
        if (accounts.isEmpty()) {
          receiptIvcBtn.setClickable(false);
          receiptIvcBtn.setErrMsg(R.R.accountEmpty());
        }
      }
    }

    receiptIvcBtn.setType("receiptIvcBtnId");
    receiptIvcBtn.setVisible(hasReceiptIvc);
    return receiptIvcBtn;
  }

  /**
   * 收款按钮权限
   * 
   * @param entity
   * @return
   */
  @RequestMapping("initReceiptVis")
  @ResponseBody
  public BMoreBtnPerm initReceiptVis(@RequestBody BStatement entity) {
    if (permissions == null || permissions.isEmpty()) {
      permissions = getPermissions();
    }
    // 收款按钮
    BMoreBtnPerm receiptBtn = new BMoreBtnPerm();
    if (entity == null) {
      return receiptBtn;
    }
    boolean isEffect = "effect".equals(entity.getBizState());
    boolean hasReceipt = isEffect && permissions.contains(StatementPermDef.RESOURCE_RECEIPT_CREATE);

    // 空账单
    if (entity.getPayTotal().getTotal().compareTo(entity.getReceiptTotal().getTotal()) == 0
        && entity.getPayTotal().getTotal().compareTo(BigDecimal.ZERO) == 0) {
      hasReceipt = false;
    } else {
      if (entity.getReceipted().compareTo(entity.getReceiptTotal().getTotal()) == 0
          && entity.getPayed().compareTo(entity.getPayTotal().getTotal()) == 0) {
        // 只有实收金额等于应收金额，实付金额等于应付金额的时候，收款按钮隐藏。
        hasReceipt = false;
      } else {
        // 存在未生效的收款发票登记单，点击收款，当前页面报错
        InvoiceReg payIvc = getPayIvc(entity.getBillNumber());
        if (payIvc != null) {
          receiptBtn.setClickable(false);
          receiptBtn.setErrMsg("存在未生效的付款发票登记单：" + payIvc.getBillNumber());
        } else {
          com.hd123.m3.account.service.ivc.reg.InvoiceReg receiptIvc = getReceiptIvc(entity
              .getBillNumber());
          if (receiptIvc != null) {
            receiptBtn.setClickable(false);
            receiptBtn.setErrMsg("存在未生效的收款发票登记单：" + receiptIvc.getBillNumber());
          }
        }
      }
    }

    receiptBtn.setType("receiptBtnId");
    receiptBtn.setVisible(hasReceipt);
    return receiptBtn;
  }

  /**
   * 是否显示销售额和删除、作废按钮的权限问题
   * 
   * @param entity
   * @throws Exception
   */
  private void decorateNormal(BStatement entity) throws Exception {
    // 正常账单
    if (StatementType.normal.name().equals(entity.getType().name())) {
      // 周期账款类型为提成或销售返款才需要显示销售额
      Set<String> settlementIds = new HashSet<String>();
      for (StatementAccRange line : entity.getRanges()) {
        if (StringUtil.isNullOrBlank(line.getSettlementId())) {
          continue;
        }
        settlementIds.add(line.getSettlementId());
      }
      if (settlementIds.isEmpty()) {
        return;
      }
      List<Settlement> settlements = contractService.getSettlements(settlementIds);
      if (settlements.isEmpty()) {
        return;
      }
      Set<String> accountTypes = new HashSet<String>();
      for (Settlement s : settlements) {
        accountTypes.addAll(s.getDef().getAccountTypes());
      }
      List<SubjectUsage> list = SubjectUsageUtils.getAll();
      for (SubjectUsage usage : list) {
        if (accountTypes.contains(usage.getCode()) == false) {
          continue;
        }
        if (UsageType.saleDeduct.name().equals(usage.getUsageType().name())
            || UsageType.salePayment.name().equals(usage.getUsageType().name())) {
          entity.setShowRefer(true);
          break;
        }
      }
      // 只能删除当前最大出账日期的数据
      entity.setLastAccountSettle(statementService.isLastestSettle(entity.getUuid()));
      // 存在关联的调整单，删除前提示信息
      FlecsQueryDefinition definition = new FlecsQueryDefinition();
      definition.addCondition(StatementAdjusts.CONDITION_STATEMENT_EQUALS, entity.getUuid());
      definition.addCondition(StatementAdjusts.CONDITION_BIZSTATE_IN, BizStates.INEFFECT,
          BizStates.EFFECT);
      int size = adjustService.query(definition).getRecords().size();
      if (size > 0) {
        entity.setHasAdjust(true);
      }
    }
  }

  /**
   * 根据科目收付方向决定收(付)款发票登记按钮的显示或隐藏
   * 
   * @param lines
   * @param direction
   *          收付方向
   * @return
   */
  private boolean subjectDir(List<StatementLine> lines, int direction) {
    boolean dir = false;
    int sumLineSize = lines.size();
    if (sumLineSize != 0) {
      for (int i = 0; i < sumLineSize; i++) {
        if (lines.get(i).getAcc1().getDirection() == direction) {
          dir = true;
          break;
        }
      }
    }
    return dir;
  }

  /**
   * 指定账单是否可登记
   * 
   * @param statementUuid
   * @param direction
   *          收付方向
   * @param isPayOrReceipt
   *          是否收付款按钮
   * @return
   * @throws AccountServiceException
   */
  private List<Account> getAccountByStatement(String statementUuid, int direction,
      boolean isPayOrReceipt) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Accounts.CONDITION_ACTIVE);
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_STATEMENT_UUID_EQUALS,
            statementUuid));
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_CAN_INVOICE));
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS,
            Accounts.NONE_LOCK_UUID));
    if (isPayOrReceipt) {
      definition.getConditions().add(
          QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_CAN_PAYMENT));
    } else {
      definition.getConditions().add(
          QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_CAN_INVOICE));
      definition.getConditions().add(
          QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_DIRECTION_EQUALS, direction));
    }

    List<Account> accounts = accountService.query(definition).getRecords();
    return accounts;
  }

  /**
   * 查未生效的付款发票登记单
   * 
   * @param billNumber
   * @return
   */
  private InvoiceReg getPayIvc(String billNumber) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addFlecsCondition(InvoiceRegs.FIELD_SOURCEBILLNUMBER, InvoiceRegs.OPERATOR_EQUALS,
        billNumber);
    definition.addCondition(InvoiceRegs.CONDITION_BIZSTATE_EQUALS, BizStates.INEFFECT);
    List<InvoiceReg> invoicRegs = payIvcService.query(definition).getRecords();

    if (!invoicRegs.isEmpty()) {
      return invoicRegs.get(0);
    }
    return null;
  }

  /**
   * 查未生效的收款发票登记单
   * 
   * @param billNumber
   * @return
   */
  private com.hd123.m3.account.service.ivc.reg.InvoiceReg getReceiptIvc(String billNumber) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addFlecsCondition(
        com.hd123.m3.account.service.ivc.reg.InvoiceRegs.FIELD_SOURCEBILL_BILLNUMBER,
        Basices.OPERATOR_EQUALS, billNumber);
    definition.addFlecsCondition(com.hd123.m3.account.service.ivc.reg.InvoiceRegs.FIELD_BIZSTATE,
        Basices.OPERATOR_EQUALS, BizStates.INEFFECT);
    List<com.hd123.m3.account.service.ivc.reg.InvoiceReg> invoicRegs = receiptIvcService.query(
        definition).getRecords();
    if (!invoicRegs.isEmpty()) {
      return invoicRegs.get(0);
    }
    return null;
  }

  private String getScale() {
    String scale = optionService.getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE);
    return scale;
  }

  private Integer getRoundingMode() {
    String roundingMode = optionService.getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_ROUNDING_MODE);
    return StringUtil.toEnum(roundingMode, RoundingMode.class, RoundingMode.HALF_UP).ordinal();
  }

  // ------------------------------------------------------------------------------------------------
  @Override
  protected String getObjectCaption() {
    return R.R.caption();
  }

  @Override
  protected String getServiceBeanId() {
    return StatementService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return Statements.OBJECT_NAME;
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(StatementQueryBuilder.class);
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("账单")
    public String caption();

    @DefaultStringValue("不存在可登记的账款")
    public String accountEmpty();

    @DefaultStringValue("不存在可收付的账款")
    public String paymentEmpty();

  }

}
