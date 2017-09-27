/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayServiceServlet.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.receipt.server;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositTotal;
import com.hd123.m3.account.gwt.deposit.commons.server.BizDepositConverter;
import com.hd123.m3.account.gwt.deposit.commons.server.DepositBizConverter;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositService;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.perm.RecDepositPermDef;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.bank.BankService;
import com.hd123.m3.account.service.bank.Banks;
import com.hd123.m3.account.service.contract.model.deposit.DepositDetail;
import com.hd123.m3.account.service.contract.model.deposit.DepositTerm;
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.account.service.deposit.DepositLine;
import com.hd123.m3.account.service.deposit.DepositService;
import com.hd123.m3.account.service.deposit.Deposits;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.commons.biz.Basices;
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
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
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
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 
 * @author chenpeisi
 * 
 */
public class RecDepositServiceServlet extends AccBPMServiceServlet implements RecDepositService {
  private static final long serialVersionUID = 300200L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        RecDepositPermDef.RESOURCE_RECDEPOSIT_SET);
    return permissions;
  }

  @Override
  public String getObjectName() {
    return Deposits.OBJECT_NAME_RECEIPT;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
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
  public BUCN getCurrentEmployee(String id) throws ClientBizException {
    try {
      User user = getUserService().get(id);
      return new BUCN(user.getUuid(), user.getId(), user.getFullName());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BDeposit> query(List<Condition> conditions, List<String> visibleColumnNames,
      PageSort pageSort) throws ClientBizException {
    try {
      FlecsQueryDefinition queryDefinition = buildQueryDefinition(conditions, pageSort);
      if (queryDefinition == null)
        return new RPageData<BDeposit>();

      QueryResult<Deposit> queryResult = getDepositService().query(queryDefinition);

      RPageData<BDeposit> result = new RPageData<BDeposit>();
      RPageDataUtil.copyPagingInfo(queryResult, result);
      List<Deposit> deposits = queryResult.getRecords();
      result.setValues(ConverterUtil.convert(queryResult.getRecords(),
          DepositBizConverter.getInstance()));

      Map<String, BUCN> map = getEmployees(deposits);
      for (BDeposit pay : result.getValues()) {
        pay.setDealer(map.get(pay.getDealer().getUuid()));
      }

      // 更新导航数据
      updateNaviEntities(getObjectName(), result.getValues());
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private Map<String, BUCN> getEmployees(List<Deposit> deposits) {
    List<String> empUuids = new ArrayList<String>();
    for (Deposit deposit : deposits) {
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

    QueryResult<User> users = getUserService().query(queryDef);

    Map<String, BUCN> result = new HashMap<String, BUCN>();
    for (User user : users.getRecords()) {
      result.put(user.getUuid(), new BUCN(user.getUuid(), user.getId(), user.getFullName()));
    }
    return result;
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
        RecDepositQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(RecDepositQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() throws ClientBizException {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    queryDef.addCondition(Deposits.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);
    queryDef.getConditions().addAll(getPermConditions());
    return queryDef;
  }

  @Override
  public BDeposit load(String uuid) throws ClientBizException {
    try {
      Deposit source = getDepositService().get(uuid);

      if (source == null)
        return null;

      if (source.getDirection() == Direction.PAYMENT)
        return null;

      BDeposit result = DepositBizConverter.getInstance().convert(source);
      // 验证对象授权
      validEntityPerm(result);
      result.setDealer(getEmployee(source.getDealer()));
      // 载入其它信息
      decorateInfo(result);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void decorateInfo(BDeposit entity) throws ClientBizException {
    Set<String> subjectUuids = new HashSet<String>();
    for (BDepositLine line : entity.getLines()) {
      subjectUuids.add(line.getSubject().getUuid());
    }
    if (BizStates.EFFECT.equals(entity.getBizState()) == false) {
      // 载入余额
      Map<String, BigDecimal> advMap = getAdvanceMap(entity.getAccountUnit().getUuid(), entity
          .getCounterpart().getUuid(), subjectUuids, entity.getContract() == null ? null : entity
          .getContract().getUuid());

      for (String key : advMap.keySet()) {
        for (BDepositLine line : entity.getLines()) {
          if (line.getSubject() == null || line.getSubject().getUuid() == null)
            continue;
          if (key.equals(line.getSubject().getUuid()))
            line.setRemainTotal(advMap.get(key));
        }
      }
      // 载入合同金额
      if (entity.getContract() != null && entity.getContract().getUuid() != null) {
        Map<String, BigDecimal> totalMap = getContractService().getContractDepositTotals(
            entity.getContract().getUuid(), subjectUuids);
        for (BDepositLine line : entity.getLines()) {
          BigDecimal contractTotal = totalMap.get(line.getSubject().getUuid());
          line.setContractTotal(contractTotal == null ? BigDecimal.ZERO : contractTotal);
        }
      }
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
  public BDeposit loadByNumber(String billNumber) throws ClientBizException {
    try {

      Deposit source = getDepositService().get(billNumber);

      if (source == null)
        return null;

      if (source.getDirection() == Direction.PAYMENT)
        return null;

      BDeposit result = DepositBizConverter.getInstance().convert(source);
      // 验证对象授权
      validEntityPerm(result);
      result.setDealer(getEmployee(source.getDealer()));
      // 载入其它信息
      decorateInfo(result);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDeposit create() throws ClientBizException {
    try {
      Deposit source = getDepositService().create(Direction.RECEIPT);

      BDeposit deposit = DepositBizConverter.getInstance().convert(source);

      decorateDeposit(deposit);

      return deposit;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDeposit createByAdvance(String advanceUuid) throws ClientBizException {
    try {

      Deposit source = getDepositService().create(advanceUuid);

      BDeposit deposit = DepositBizConverter.getInstance().convert(source);

      decorateDeposit(deposit);

      return deposit;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void decorateDeposit(BDeposit deposit) {
    BDefaultOption defaultOption = getDefaultOption();
    deposit.setPaymentType(defaultOption.getPaymentType());
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

  @Override
  public void effect(String uuid, long version) throws ClientBizException {
    try {

      getDepositService().changeBizState(uuid, version, BizStates.EFFECT,ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void abort(String uuid, long version) throws ClientBizException {
    try {
      getDepositService().changeBizState(uuid, version,BizStates.ABORTED, ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String executeTask(BOperation operation, BDeposit entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");
      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (saveBeforeAction
          && !BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction())) {
        String uuid = getDepositService().save(BizDepositConverter.getInstance().convert(entity),
            ConvertHelper.getOperateContext(getSessionUser()));
        Deposit result = getDepositService().get(uuid);
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
  public void remove(String uuid, long oca) throws ClientBizException {
    try {
      getDepositService().changeBizState(uuid, oca,BizStates.DELETED, ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDeposit save(BDeposit entity, BProcessContext task) throws ClientBizException {
    try {
      // 先判断流程权限
      doBeforeExecute(entity, task);

      Deposit target = null;
      if (entity.getUuid() == null)
        target = getDepositService().create(Direction.RECEIPT);
      else
        target = getDepositService().get(entity.getUuid());

      write(entity, target);

      String deposit = getDepositService().save(target,
          ConvertHelper.getOperateContext(getSessionUser()));
      Deposit result = getDepositService().get(deposit);

      doAfterSave(result, task);

      return DepositBizConverter.getInstance().convert(result);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  protected Map<String, Object> getBpmVariables(Object obj, BTaskContext ctx) {
    Map<String, Object> variables = super.getBpmVariables(obj, ctx);
    if (obj instanceof Deposit) {
      Deposit bill = (Deposit) obj;
      variables.put(
          BpmConstants.KEY_BILLCAPTION,
          CodecUtils.encode(MessageFormat.format(R.R.billCaption(), bill.getBillNumber(),
              bill.getContract() == null ? "" : bill.getContract().getCode())));
    }
    return variables;
  }

  private void write(BDeposit source, Deposit target) {
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
    target.setDepositDate(source.getDepositDate());
    target.setDepositTotal(source.getDepositTotal());
    target.setAccountDate(source.getAccountDate());
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
    List<DepositLine> lines = new ArrayList<DepositLine>();
    for (BDepositLine line : source.getLines()) {
      DepositLine pLine = new DepositLine();
      pLine.setUuid(line.getUuid());
      pLine.setLineNumber(line.getLineNumber());
      pLine.setRemark(line.getRemark());
      pLine.setSubject(BizUCNConverter.getInstance().convert(line.getSubject()));
      pLine.setAmount(line.getTotal());
      pLine.setRemainTotal(line.getRemainTotal());
      pLine.setContractTotal(line.getContractTotal());
      lines.add(pLine);
    }
    target.setLines(lines);
  }

  @Override
  public BDepositTotal getAdvance(String accountUnitUuid, String counterpartUuid,
      String subjectUuid, String contractUuid) throws ClientBizException {
    try {
      if (contractUuid == null)
        contractUuid = Advances.NONE_BILL_UUID;

      Advance advance = getAdvanceService().get(accountUnitUuid, counterpartUuid, contractUuid,
          subjectUuid);
      BDepositTotal result = new BDepositTotal();
      result.setAdvanceTotal(advance == null ? BigDecimal.ZERO : advance.getTotal());
      // 获取合同金额
      if (StringUtil.isNullOrBlank(contractUuid) == false) {
        BigDecimal contractTotal = getContractService().getContractDepositTotal(contractUuid,
            subjectUuid);
        if (contractTotal == null) {
          contractTotal = BigDecimal.ZERO;
        }
        result.setContractTotal(contractTotal);
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public Map<String, BDepositTotal> getAdvances(String accountUnitUuid, String counterpartUuid,
      Collection<String> subjectUuids, String contractUuid) throws ClientBizException {
    try {
      Map<String, BigDecimal> advanceMap = getAdvanceMap(accountUnitUuid, counterpartUuid,
          subjectUuids, contractUuid);
      Map<String, BDepositTotal> result = new HashMap<String, BDepositTotal>();
      for (String subjectUuid : subjectUuids) {
        BigDecimal advanceTotal = advanceMap.get(subjectUuid);
        BDepositTotal total = new BDepositTotal();
        total.setAdvanceTotal(advanceTotal == null ? BigDecimal.ZERO : advanceTotal);
        result.put(subjectUuid, total);
      }
      if (StringUtil.isNullOrBlank(contractUuid) == false) {
        Map<String, BigDecimal> contractTotalMap = getContractService().getContractDepositTotals(
            contractUuid, subjectUuids);
        for (Map.Entry<String, BigDecimal> entry : contractTotalMap.entrySet()) {
          if (result.get(entry.getKey()) == null) {
            continue;
          }
          result.get(entry.getKey()).setContractTotal(entry.getValue());
        }
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  public Map<String, BigDecimal> getAdvanceMap(String accountUnitUuid, String counterpartUuid,
      Collection<String> subjectUuids, String contractUuid) throws ClientBizException {
    try {
      Map<String, BigDecimal> advanceMap = new HashMap<String, BigDecimal>();

      if (subjectUuids == null || subjectUuids.isEmpty())
        return advanceMap;

      if (contractUuid == null)
        contractUuid = Advances.NONE_BILL_UUID;

      QueryDefinition queryDef = new QueryDefinition();

      queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnitUuid.trim());

      queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpartUuid.trim());

      queryDef.addCondition(Advances.CONDITION_BILL_ID_EQUALS, contractUuid.trim());

      queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);

      queryDef.addCondition(Advances.CONDITION_SUBJECT_UUID_IN, subjectUuids.toArray());

      QueryResult<Advance> qr = getAdvanceService().query(queryDef);

      if (qr == null || qr.getRecords() == null || qr.getRecords().isEmpty())
        return advanceMap;

      for (Advance adv : qr.getRecords()) {
        advanceMap.put(adv.getSubject().getUuid(), adv.getTotal());
      }
      return advanceMap;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BContract getContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException {
    try {
      return getSingleContract(accountUnitUuid, counterpartUuid, counterpartType);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BDepositLine> importFromContract(String contractUuid, String accountUnitUuid,
      String counterpartUuid) throws ClientBizException {
    if (StringUtil.isNullOrBlank(contractUuid)) {
      return new ArrayList<BDepositLine>();
    }

    try {
      List<DepositTerm> terms = getContractService().getDepositTerms(contractUuid);
      Collection<String> subjectUuids = new HashSet<String>();
      for (DepositTerm term : terms) {
        for (DepositDetail detail : term.getDetails()) {
          if (Direction.RECEIPT == detail.getDirection() == false) {
            continue;
          }
          subjectUuids.add(detail.getSubject().getUuid());
        }
      }
      if (subjectUuids.isEmpty()) {
        return new ArrayList<BDepositLine>();
      }
      Map<String, BigDecimal> advances = getAdvanceMap(accountUnitUuid, counterpartUuid,
          subjectUuids, contractUuid);
      List<BDepositLine> lines = new ArrayList<BDepositLine>();
      for (DepositTerm term : terms) {
        for (DepositDetail detail : term.getDetails()) {
          if (Direction.RECEIPT == detail.getDirection() == false) {
            continue;
          }
          BDepositLine line = new BDepositLine();
          line.setSubject(UCNBizConverter.getInstance().convert(detail.getSubject()));
          BigDecimal remainTotal = advances.get(detail.getSubject().getUuid());
          line.setRemainTotal(remainTotal == null ? BigDecimal.ZERO : remainTotal);
          line.setContractTotal(detail.getAmount() == null ? BigDecimal.ZERO : detail.getAmount());
          line.setTotal(line.getUnDepositTotal());
          if (BigDecimal.ZERO.compareTo(line.getUnDepositTotal()) == 0) {
            continue;
          }
          lines.add(line);
        }
      }
      return lines;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private AdvanceService getAdvanceService() {
    return M3ServiceFactory.getService(AdvanceService.class);
  }

  private DepositService getDepositService() {
    return M3ServiceFactory.getService(DepositService.class);
  }

  private PaymentTypeService getPaymentTypeService() {
    return M3ServiceFactory.getService(PaymentTypeService.class);
  }

  private BankService getBankService() {
    return M3ServiceFactory.getService(BankService.class);
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("预存款单")
    String moduleCaption();

    @DefaultStringValue("预存款单：{0},合同编号：{1}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("获取单据类型出错：")
    String findBillTypeError();

    @DefaultStringValue("{0},{1}不是有效的数据，或者缺少授权组权限。")
    String accountUnitAndCounterpart();

    @DefaultStringValue("{0}不是有效的数据，或者缺少授权组权限。")
    String lackOfAuthorizatioGroups();

    @DefaultStringValue("指定的\"预存款单\"不存在。")
    String nullObject();
  }
}
