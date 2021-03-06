/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositRepaymentServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-25 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.server;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.server.DepositBizConverter;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.gwt.depositrepayment.commons.server.converter.BizDepositRepaymentConverter;
import com.hd123.m3.account.gwt.depositrepayment.commons.server.converter.DepositRepaymentBizConverter;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.widget.RecDepositFilter;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.perm.RecDepositRepaymentPermDef;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.bank.BankService;
import com.hd123.m3.account.service.bank.Banks;
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.account.service.deposit.DepositService;
import com.hd123.m3.account.service.deposit.Deposits;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.account.service.depositrepayment.DepositRepaymentLine;
import com.hd123.m3.account.service.depositrepayment.DepositRepaymentService;
import com.hd123.m3.account.service.depositrepayment.DepositRepayments;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.BpmOperateContext;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.sales.service.paytype.PaymentTypes;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author zhuhairui
 * 
 */
public class RecDepositRepaymentServiceServlet extends AccBPMServiceServlet implements
    RecDepositRepaymentService {
  private static final long serialVersionUID = 4567756448132625069L;
  private static final String IS_ENABLE_BY_DEPOSIT = "isEnableByDeposit";

  @Override
  public String getObjectName() {
    return DepositRepayments.OBJECT_NAME_RECEIPT;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected Map<String, Object> getBpmVariables(Object obj, BTaskContext ctx) {
    Map<String, Object> variables = super.getBpmVariables(obj, ctx);
    if (obj instanceof DepositRepayment) {
      DepositRepayment bill = (DepositRepayment) obj;
      variables.put(
          BpmConstants.KEY_BILLCAPTION,
          CodecUtils.encode(MessageFormat.format(R.R.billCaption(), bill.getBillNumber(),
              bill.getContract() == null ? "" : bill.getContract().getCode())));
    }
    return variables;
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        RecDepositRepaymentPermDef.RESOURCE_RECDEPOSITREPAYMENT_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    moduleContext.put(IS_ENABLE_BY_DEPOSIT, getByDepositEnabled());
  }

  private String getByDepositEnabled() {
    String byDepositEnabled = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_BYDEPOSIT_ENABLED);
    if (StringUtil.isNullOrBlank(byDepositEnabled)) {
      return "false";
    }
    return byDepositEnabled;
  }

  @Override
  public BDepositRepayment loadByNumber(String billNumber) throws ClientBizException {
    try {

      DepositRepayment source = getDepositRepaymentService().get(billNumber);

      BDepositRepayment result = DepositRepaymentBizConverter.getInstance().convert(source);
      if (validateEntity(result) == false)
        return null;

      result.setDealer(getEmployee(source.getDealer()));

      if (BizStates.EFFECT.equals(source.getBizState()) == false)
        decorateDepositLine(result);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositRepayment load(String uuid) throws ClientBizException {
    try {

      DepositRepayment source = getDepositRepaymentService().get(uuid);

      BDepositRepayment result = DepositRepaymentBizConverter.getInstance().convert(source);
      if (validateEntity(result) == false)
        return null;

      result.setDealer(getEmployee(source.getDealer()));

      if (BizStates.EFFECT.equals(source.getBizState()) == false)
        decorateDepositLine(result);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private BUCN getEmployee(String uuid) throws ClientBizException {
    try {
      if (uuid == null)
        return null;
      User user = getUserService().get(uuid);

      if (user == null)
        return null;

      return new BUCN(user.getUuid(), user.getId(), user.getFullName());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BDepositRepayment> query(List<Condition> conditions,
      List<String> visibleColumnNames, PageSort pageSort) throws ClientBizException {
    try {

      FlecsQueryDefinition queryDefinition = buildQueryDefinition(conditions, pageSort);
      if (queryDefinition == null)
        return new RPageData<BDepositRepayment>();

      QueryResult<DepositRepayment> queryResult = getDepositRepaymentService().query(
          queryDefinition);

      RPageData<BDepositRepayment> result = new RPageData<BDepositRepayment>();
      RPageDataUtil.copyPagingInfo(queryResult, result);
      List<DepositRepayment> deposits = queryResult.getRecords();
      result.setValues(ConverterUtil.convert(queryResult.getRecords(),
          DepositRepaymentBizConverter.getInstance()));

      Map<String, BUCN> map = getEmployees(deposits);
      for (BDepositRepayment pay : result.getValues()) {
        pay.setDealer(map.get(pay.getDealer().getUuid()));
      }

      // 更新导航数据
      updateNaviEntities(getObjectName(), result.getValues());
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BDeposit> queryDeposit(RecDepositFilter filter) throws Exception {
    FlecsQueryDefinition definition = buildQueryDepositDefinition(filter, true);
    if (definition == null) {
      return new RPageData<BDeposit>();
    }
    QueryResult<Deposit> queryResult = getDepositService().query(definition);
    RPageData<BDeposit> result = new RPageData<BDeposit>();
    RPageDataUtil.copyPagingInfo(queryResult, result);
    result.setValues(ConverterUtil.convert(queryResult.getRecords(),
        DepositBizConverter.getInstance()));
    return result;
  }

  /**
   * 构造查询预存款单的条件，当项目或对方单位为空，返回null
   * 
   * @param filter
   * @param isQuery
   * @return definition
   */
  private FlecsQueryDefinition buildQueryDepositDefinition(RecDepositFilter filter, boolean isQuery) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    Set<String> fetch = new HashSet<String>(1);
    fetch.add(Deposit.PART_LINE);
    definition.setFetchParts(fetch);
    // 如果项目或对方单位为空，则返回空。
    String accountUnitUuid = filter.getAccountUnitUuid();
    String counterpartUuid = filter.getCounterpartUuid();
    if (isQuery) {
      if (StringUtil.isNullOrBlank(accountUnitUuid) || StringUtil.isNullOrBlank(counterpartUuid)) {
        return null;
      } else {
        definition.addCondition(Deposits.CONDITION_ACCOUNTUNIT_EQUALS, accountUnitUuid);
        definition.addCondition(Deposits.CONDITION_COUNTERPART_EQUALS, counterpartUuid);
      }
    } else {
      if (!StringUtil.isNullOrBlank(accountUnitUuid)) {
        definition.addCondition(Deposits.CONDITION_ACCOUNTUNIT_EQUALS, accountUnitUuid);
      }
      if (!StringUtil.isNullOrBlank(counterpartUuid)) {
        definition.addCondition(Deposits.CONDITION_COUNTERPART_EQUALS, counterpartUuid);
      }
    }
    if (!StringUtil.isNullOrBlank(filter.getBillNumber())) {
      definition.addCondition(Deposits.CONDITION_BILLNUMBER_STARTWITH, filter.getBillNumber());
    }
    if (!StringUtil.isNullOrBlank(filter.getContractUuid())) {
      definition.addCondition(Deposits.CONDITION_CONTRACT_EQUALS, filter.getContractUuid());
    }
    if (!StringUtil.isNullOrBlank(filter.getBizState())) {
      definition.addCondition(Deposits.CONDITION_BIZSTATE_EQUALS, filter.getBizState());
    }
    if (filter.isUnRepaymented()) {
      definition.addCondition(Deposits.CONDITION_UNREPAYMENTED);
    }

    // 排序
    List<com.hd123.rumba.gwt.base.client.QueryFilter.Order> orders = filter.getOrders();
    if (!orders.isEmpty()) {
      for (com.hd123.rumba.gwt.base.client.QueryFilter.Order order : orders) {
        String fieldName = order.getFieldName();
        OrderDir dir = order.getDir();
        if (Deposits.ORDER_BY_BILLNUMBER.equals(fieldName)) {
          definition
              .addOrder(Deposits.ORDER_BY_BILLNUMBER, QueryOrderDirection.valueOf(dir.name()));
        }
      }
    }
    return definition;
  }

  @Override
  public BDeposit getDepositByBillNumber(RecDepositFilter filter) throws Exception {
    BDeposit result = new BDeposit();
    result.setBillNumber(filter.getBillNumber());
    if (StringUtil.isNullOrBlank(filter.getBillNumber())) {
      return result;
    }
    FlecsQueryDefinition definition = buildQueryDepositDefinition(filter, false);
    if (definition == null) {
      return result;
    }
    QueryResult<Deposit> queryResult = getDepositService().query(definition);
    if (queryResult == null || queryResult.getRecords().isEmpty()
        || queryResult.getRecords().size() != 1) {
      return result;
    }
    Deposit deposit = queryResult.getRecords().get(0);
    return DepositBizConverter.getInstance().convert(deposit);
  }

  @Override
  public void remove(String uuid, long version) throws ClientBizException {
    try {

      getDepositRepaymentService().remove(uuid, version,
          BeanOperateContext.newInstance(ConvertHelper.getOperateContext(getSessionUser())));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void effect(String uuid, String comment, long oca) throws Exception {
    try {

      BpmOperateContext coperCtx = new BpmOperateContext(getSessionUser(), comment);

      getDepositRepaymentService().effect(uuid, oca, coperCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void abort(String uuid, String comment, long oca) throws Exception {
    try {

      BpmOperateContext coperCtx = new BpmOperateContext(getSessionUser(), comment);

      getDepositRepaymentService().changeBizState(uuid, oca,BizStates.ABORTED, coperCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositRepayment create() throws ClientBizException {
    try {
      DepositRepayment source = getDepositRepaymentService().create(Direction.RECEIPT);
      BDepositRepayment depositRepayment = DepositRepaymentBizConverter.getInstance().convert(
          source);

      decorateDepositRepayment(depositRepayment);

      return depositRepayment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositRepayment createByAdvance(String advanceUuid) throws ClientBizException {
    try {

      DepositRepayment source = getDepositRepaymentService().create(advanceUuid);

      BDepositRepayment depositRepayment = DepositRepaymentBizConverter.getInstance().convert(
          source);

      decorateDepositRepayment(depositRepayment);

      return depositRepayment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositRepayment save(BDepositRepayment entity, BProcessContext task)
      throws ClientBizException {
    try {

      // 先判断流程权限
      doBeforeExecute(entity, task);

      DepositRepayment target = null;
      if (entity.getUuid() == null)
        target = getDepositRepaymentService().create(Direction.RECEIPT);
      else
        target = getDepositRepaymentService().get(entity.getUuid());
      write(entity, target);

      String uuid = getDepositRepaymentService().save(target,
          ConvertHelper.getOperateContext(getSessionUser()));
      DepositRepayment result = getDepositRepaymentService().get(uuid);

      doAfterSave(result, task);

      return DepositRepaymentBizConverter.getInstance().convert(result);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BContract getUniqueAdvance(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException {
    try {
      return getSingleContract(accountUnitUuid, counterpartUuid, counterpartType);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BigDecimal getAdvance(String accountUnitUuid, String counterpartUuid, String subjectUuid,
      String contractUuid) throws ClientBizException {
    try {

      if (contractUuid == null)
        contractUuid = Advances.NONE_BILL_UUID;

      Advance advance = getAdvanceService().get(accountUnitUuid, counterpartUuid, contractUuid,
          subjectUuid);

      if (advance == null)
        return BigDecimal.ZERO;

      return advance.getTotal();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BUCN> getPaymentTypes() throws ClientBizException {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addFlecsCondition(PaymentTypes.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
      List<PaymentType> paymentTypes = getPaymentTypeService().query(def).getRecords();
      
      if (paymentTypes == null || paymentTypes.isEmpty())
        return Collections.emptyList();
      List<BUCN> result = new ArrayList<BUCN>();
      for (PaymentType paymentType : paymentTypes) {
        result.add(new BUCN(paymentType.getUuid(), paymentType.getCode(), paymentType.getName()));
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BBank> getBanks() throws ClientBizException {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addFlecsCondition(Banks.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
      List<Bank> banks = getBankService().query(def).getRecords();
      if (banks == null || banks.isEmpty())
        return Collections.emptyList();
      List<BBank> results = new ArrayList<BBank>();
      for (Bank bank : banks) {
        BBank result = new BBank();
        result.setCode(bank.getCode());
        result.setName(bank.getName());
        result.setStore(UCNBizConverter.getInstance().convert(bank.getStore()));
        result.setBankAccount(bank.getAccount());
        results.add(result);
      }
      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String executeTask(BOperation operation, BDepositRepayment entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");
      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (saveBeforeAction
          && !BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction())) {
        String uuid = getDepositRepaymentService().save(
            BizDepositRepaymentConverter.getInstance().convert(entity),
            ConvertHelper.getOperateContext(getSessionUser()));
        DepositRepayment result = getDepositRepaymentService().get(uuid);
        doExecuteTask(result, operation, task);
        return result.getUuid();
      } else {
        doExecuteTask(operation, task);
        return entity == null ? null : entity.getUuid();
      }
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BUCN getCurrentEmployee(String id) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(id))
        return null;

      User user = getUserService().get(id);
      if (user == null)
        return null;

      return new BUCN(user.getUuid(), user.getId(), user.getFullName());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions, PageSort pageSort)
      throws ClientBizException {
    FlecsQueryDefinition queryDef = buildQueryDefinition(conditions, pageSort.getSortOrders());
    if (queryDef == null)
      return null;

    queryDef.setPage(pageSort.getPage());
    queryDef.setPageSize(pageSort.getPageSize());
    return queryDef;
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions,
      List<Order> sortOrders) throws ClientBizException {
    FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();
    if (queryDef == null)
      return null;

    queryDef.getFlecsConditions().addAll(
        RecDepositRepaymentQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(RecDepositRepaymentQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() throws ClientBizException {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    queryDef.addCondition(DepositRepayments.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);
    queryDef.getConditions().addAll(getPermConditions());

    return queryDef;
  }

  private boolean validateEntity(BDepositRepayment source) throws Exception {
    if (source == null)
      return false;
    if (source.getDirection() == Direction.PAYMENT)
      return false;

    validEntityPerm(source);
    return true;
  }

  private Map<String, BUCN> getEmployees(List<DepositRepayment> deposits) {
    List<String> empUuids = new ArrayList<String>();
    for (DepositRepayment deposit : deposits) {
      if (empUuids.contains(deposit.getDealer()))
        continue;
      empUuids.add(deposit.getDealer());
    }
    if (empUuids.isEmpty())
      return new HashMap<String, BUCN>();

    QueryDefinition queryDef = new QueryDefinition();
    QueryCondition cond = new QueryCondition();
    cond.setOperation(UserService.CONDITION_UUID_IN);
    cond.getParameters().addAll(
        empUuids.size() > 1000 ? Arrays.asList(Arrays.copyOf(empUuids.toArray(), 1000)) : empUuids);
    queryDef.getConditions().add(cond);

    QueryResult<User> employees = getUserService().query(queryDef);

    Map<String, BUCN> result = new HashMap<String, BUCN>();

    for (User employee : employees.getRecords()) {
      result.put(employee.getUuid(),
          new BUCN(employee.getUuid(), employee.getId(), employee.getFullName()));
    }
    return result;
  }

  private void decorateDepositLine(BDepositRepayment result) {
    List<String> subjectUuids = new ArrayList<String>();

    for (BDepositRepaymentLine line : result.getLines()) {
      if (subjectUuids.contains(line.getSubject().getUuid()))
        continue;
      else
        subjectUuids.add(line.getSubject().getUuid());
    }

    QueryDefinition queryDef = new QueryDefinition();
    if (result.getAccountUnit() != null)
      queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, result.getAccountUnit()
          .getUuid());

    queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);

    queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, result.getCounterpart()
        .getUuid());

    queryDef.addCondition(Advances.CONDITION_SUBJECT_UUID_IN, subjectUuids.toArray());

    queryDef.addCondition(Advances.CONDITION_BILL_ID_EQUALS, result.getContract() == null
        || result.getContract().getUuid() == null ? Advances.NONE_BILL_UUID : result.getContract()
        .getUuid());

    QueryResult<Advance> qr = getAdvanceService().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return;

    for (Advance advance : qr.getRecords()) {
      for (BDepositRepaymentLine line : result.getLines()) {
        if (advance.getSubject().getUuid().equals(line.getSubject().getUuid())) {
          line.setRemainAmount(advance.getTotal());
        }
      }
    }
  }

  private void decorateDepositRepayment(BDepositRepayment depositRepayment) {
    BDefaultOption defaultOption = getDefaultOption();
    depositRepayment.setPaymentType(defaultOption.getPaymentType());
  }

  private BDefaultOption getDefaultOption() {
    BDefaultOption defaultOption = new BDefaultOption();

    defaultOption.setPaymentType(getPaymentType(getOptionService().getOption(
        AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_PAYMENT)));

    return defaultOption;
  }

  private BUCN getPaymentType(String uuid) {
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

  private void write(BDepositRepayment source, DepositRepayment target) {
    assert source != null;
    assert target != null;

    target.setPermGroupId(source.getPermGroupId());
    target.setPermGroupTitle(source.getPermGroupTitle());
    target.setRemark(source.getRemark());
    target.setVersion(source.getVersion());
    target.setDirection(Direction.RECEIPT);
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setDealer(source.getDealer() == null ? null : source.getDealer().getUuid());
    target.setCounterContact(source.getCounterContact());
    target.setRepaymentDate(source.getRepaymentDate());
    target.setAccountDate(source.getAccountDate());
    target.setRepaymentTotal(source.getRepaymentTotal());
    target.setPaymentType(BizUCNConverter.getInstance().convert(source.getPaymentType()));
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    if (source.getBank() != null) {
      target.setBankAccount(source.getBank().getBankAccount());
      target.setBankCode(source.getBank().getCode());
      target.setBankName(source.getBank().getName());
    } else {
      target.setBankAccount(null);
      target.setBankCode(null);
      target.setBankName(null);
    }
    target.setDeposit(BizUCNConverter.getInstance().convert(source.getDeposit()));

    List<DepositRepaymentLine> lines = new ArrayList<DepositRepaymentLine>();
    for (BDepositRepaymentLine line : source.getLines()) {
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        continue;
      DepositRepaymentLine pLine = new DepositRepaymentLine();
      pLine.setUuid(line.getUuid());
      pLine.setLineNumber(line.getLineNumber());
      pLine.setRemark(line.getRemark());
      pLine.setSubject(BizUCNConverter.getInstance().convert(line.getSubject()));
      pLine.setAmount(line.getAmount());
      lines.add(pLine);
    }
    target.setLines(lines);
  }

  private DepositRepaymentService getDepositRepaymentService() {
    return M3ServiceFactory.getService(DepositRepaymentService.class);
  }

  private AdvanceService getAdvanceService() {
    return M3ServiceFactory.getService(AdvanceService.class);
  }

  private PaymentTypeService getPaymentTypeService() {
    return M3ServiceFactory.getService(PaymentTypeService.class);
  }

  private BankService getBankService() {
    return M3ServiceFactory.getService(BankService.class);
  }

  private DepositService getDepositService() {
    return M3ServiceFactory.getService(DepositService.class);
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("预存款还款单")
    String moduleCaption();

    @DefaultStringValue("预存款还款单：{0},合同编号：{1}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("获取单据类型出错：")
    String findBillTypeError();

    @DefaultStringValue("{0},{1}不是有效的数据，或者缺少授权组权限。")
    String accountUnitAndCounterpart();

    @DefaultStringValue("{0}不是有效的数据，或者缺少授权组权限。")
    String lackOfAuthorizatioGroups();

    @DefaultStringValue("指定的\"预存款还款单\"不存在。")
    String nullObject();
  }
}
