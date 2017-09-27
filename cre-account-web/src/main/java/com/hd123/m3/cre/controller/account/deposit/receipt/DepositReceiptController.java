/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	RecDepositController.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月7日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.receipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.commons.CounterpartType;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.model.deposit.DepositDetail;
import com.hd123.m3.account.service.contract.model.deposit.DepositTerm;
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.account.service.deposit.DepositLine;
import com.hd123.m3.account.service.deposit.DepositService;
import com.hd123.m3.account.service.deposit.Deposits;
import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountCommonComponent;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.common.model.SUser;
import com.hd123.m3.cre.controller.account.deposit.builder.DepositQueryBuilder;
import com.hd123.m3.cre.controller.account.deposit.model.BDeposit;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.util.DateUtil;

/**
 * 预存款单控制器
 * 
 * @author LiBin
 *
 */

@Controller
@RequestMapping("account/deposit/receipt/*")
public class DepositReceiptController extends BizFlowModuleController {

  /** 键值：当前用户 */
  private static final String KEY_USER = "currentUser";

  @Autowired
  private AccountCommonComponent accCommonComponent;
  @Autowired
  private DepositService depositService;
  @Autowired
  private ContractService contractService;
  @Autowired
  private AdvanceService advanceService;
  @Autowired
  private AccountOptionComponent optionComponent;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    Set<String> resourceIds = new HashSet<String>();
    resourceIds.add(AccountPermResourceDef.RESOURCE_DEPOSIT_RECEIPT);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        resourceIds));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    // 覆盖当前用户，增加属性
    moduleContext.put(KEY_USER, getSUser());
  }

  @Override
  public SummaryQueryResult query(@RequestBody QueryFilter queryFilter) {
    if (queryFilter == null) {
      return new SummaryQueryResult();
    }
    queryFilter.getFilter().put(Payments.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);
    return super.query(queryFilter);
  }

  @Override
  protected void buildSummary(SummaryQueryResult result, QueryFilter queryFilter) {
    FlecsQueryDefinition definition = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    com.hd123.m3.account.service.deposit.DepositTotal total = depositService.queryTotal(definition);
    result.getSummary().put("depositTotal", total.getDepositTotal());
    super.buildSummary(result, queryFilter);
  }

  @Override
  protected void decorateRecords(SummaryQueryResult queryResult) {
    Map<String, UCN> map = getEmployees(queryResult.getRecords());

    List<BDeposit> records = new ArrayList<BDeposit>();
    for (Object object : queryResult.getRecords()) {
      Deposit deposit = (Deposit) object;
      BDeposit biz = BDeposit.newInstance(deposit);
      biz.setViewDealer(map.get(deposit.getDealer()));
      records.add(biz);
    }
    queryResult.setRecords(records);
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    Deposit entity = JsonUtil.jsonToObject(json, Deposit.class);

    // 移除空行
    for (int index = entity.getLines().size() - 1; index >= 0; index--) {
      DepositLine line = entity.getLines().get(index);
      if (line.getSubject() == null || line.getSubject().getUuid() == null) {
        entity.getLines().remove(index);
      }
    }

    return depositService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  /** 根据收款单标识（单号或者uuid）加载收款单，包括明细信息 */
  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody BDeposit load(@RequestParam("uuid") String id) throws M3ServiceException {
    Deposit deposit = depositService.get(id);
    if (deposit == null || deposit.getDirection() == Direction.PAYMENT) {
      return null;
    }

    BDeposit result = BDeposit.newInstance(deposit);
    if (result.getDealer() != null) {
      User user = getUserService().get(result.getDealer());
      if (user != null) {
        result.setViewDealer(new UCN(user.getUuid(), user.getId(), user.getFullName()));
      }
    }

    // 载入其它信息
    decorateInfo(result);

    return result;
  }

  /** 创建预存款单，同时设置默认值 */
  @RequestMapping(value = "create", method = RequestMethod.GET)
  public @ResponseBody BDeposit create() throws M3ServiceException {
    try {
      BDeposit deposit = new BDeposit();
      deposit.setBizState(BizStates.INEFFECT);
      deposit.setDirection(Direction.RECEIPT);
      decorateDeposit(deposit);

      return deposit;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  /** 预存款模块跳转到预存款单模块新建时调用 */
  @RequestMapping(value = "createByAdvance", method = RequestMethod.GET)
  public @ResponseBody BDeposit createByAdvance(@QueryParam("advanceUuid") String advanceUuid)
      throws M3ServiceException {
    try {

      Deposit source = depositService.create(advanceUuid);
      BDeposit deposit = BDeposit.newInstance(source);
      decorateDeposit(deposit);
      return deposit;

    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  /** 根据合同导入明细 */
  @RequestMapping(value = "importFromContract", method = RequestMethod.GET)
  public @ResponseBody List<DepositLine> importFromContract(
      @QueryParam("accountUnitUuid") String accountUnitUuid,
      @QueryParam("counterpartUuid") String counterpartUuid,
      @QueryParam("contractUuid") String contractUuid) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(contractUuid)) {
      return new ArrayList<DepositLine>();
    }

    try {
      List<DepositTerm> terms = contractService.getDepositTerms(contractUuid);
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
        return new ArrayList<DepositLine>();
      }
      Map<String, BigDecimal> advances = getAdvanceMap(accountUnitUuid, counterpartUuid,
          subjectUuids, contractUuid);
      List<DepositLine> lines = new ArrayList<DepositLine>();
      for (DepositTerm term : terms) {
        for (DepositDetail detail : term.getDetails()) {
          if (Direction.RECEIPT == detail.getDirection() == false) {
            continue;
          }
          DepositLine line = new DepositLine();
          line.setSubject(detail.getSubject());
          BigDecimal remainTotal = advances.get(detail.getSubject().getUuid());
          line.setRemainTotal(remainTotal == null ? BigDecimal.ZERO : remainTotal);
          line.setContractTotal(detail.getAmount() == null ? BigDecimal.ZERO : detail.getAmount());
          line.setAmount(line.getUnDepositTotal());
          if (BigDecimal.ZERO.compareTo(line.getUnDepositTotal()) == 0) {
            continue;
          }
          lines.add(line);
        }
      }
      return lines;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping(value = "getAdvance", method = RequestMethod.GET)
  public @ResponseBody DepositTotal getAdvance(
      @QueryParam("accountUnitUuid") String accountUnitUuid,
      @QueryParam("counterpartUuid") String counterpartUuid,
      @QueryParam("subjectUuid") String subjectUuid, @QueryParam("contractUuid") String contractUuid)
      throws M3ServiceException {

    try {
      if (StringUtil.isNullOrBlank(contractUuid)) {
        contractUuid = Advances.NONE_BILL_UUID;
      }

      Advance advance = advanceService.get(accountUnitUuid, counterpartUuid, contractUuid,
          subjectUuid);
      DepositTotal result = new DepositTotal();
      result.setAdvanceTotal(advance == null ? BigDecimal.ZERO : advance.getTotal());
      // 获取合同金额
      if (StringUtil.isNullOrBlank(contractUuid) == false) {
        BigDecimal contractTotal = contractService.getContractDepositTotal(contractUuid,
            subjectUuid);
        if (contractTotal == null) {
          contractTotal = BigDecimal.ZERO;
        }
        result.setContractTotal(contractTotal);
      }
      return result;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping(value = "getAdvances", method = RequestMethod.POST)
  public @ResponseBody Map<String, DepositTotal> getAdvances(@RequestBody AdvancesParams params)
      throws M3ServiceException {
    try {
      Map<String, BigDecimal> advanceMap = getAdvanceMap(params.getAccountUnitUuid(),
          params.getCounterpartUuid(), params.getSubjectUuids(), params.getContractUuid());
      Map<String, DepositTotal> result = new HashMap<String, DepositTotal>();
      for (String subjectUuid : params.getSubjectUuids()) {
        BigDecimal advanceTotal = advanceMap.get(subjectUuid);
        DepositTotal total = new DepositTotal();
        total.setAdvanceTotal(advanceTotal == null ? BigDecimal.ZERO : advanceTotal);
        result.put(subjectUuid, total);
      }
      if (StringUtil.isNullOrBlank(params.getContractUuid()) == false) {
        Map<String, BigDecimal> contractTotalMap = contractService.getContractDepositTotals(
            params.getContractUuid(), params.getSubjectUuids());
        for (Map.Entry<String, BigDecimal> entry : contractTotalMap.entrySet()) {
          if (result.get(entry.getKey()) == null) {
            continue;
          }
          result.get(entry.getKey()).setContractTotal(entry.getValue());
        }
      }
      return result;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  private void decorateDeposit(BDeposit deposit) {
    deposit.setPaymentType(getDefaltPaymentType());

    deposit.setAccountDate(DateUtil.datePart(new Date()));
    deposit.setDepositDate(DateUtil.datePart(new Date()));
    deposit.setCounterpartType(CounterpartType.TENANT);
    deposit.setViewDealer(getSUser());
    deposit.setDealer(deposit.getViewDealer().getUuid());
    if (deposit.getLines().isEmpty()) {
      DepositLine line = new DepositLine();
      line.setRemainTotal(BigDecimal.ZERO);
      line.setContractTotal(BigDecimal.ZERO);
      deposit.getLines().add(line);
    }
  }

  private UCN getDefaltPaymentType() {
    PaymentType type = optionComponent.getDefaltPaymentType();
    if (type == null) {
      return null;
    }
    return new UCN(type.getUuid(), type.getCode(), type.getName());
  }

  private void decorateInfo(BDeposit entity) throws M3ServiceException {
    Set<String> subjectUuids = new HashSet<String>();
    for (DepositLine line : entity.getLines()) {
      subjectUuids.add(line.getSubject().getUuid());
    }
    if (BizStates.INEFFECT.equals(entity.getBizState())) {
      // 载入余额
      Map<String, BigDecimal> advMap = getAdvanceMap(entity.getAccountUnit().getUuid(), entity
          .getCounterpart().getUuid(), subjectUuids, entity.getContract() == null ? null : entity
          .getContract().getUuid());

      for (String key : advMap.keySet()) {
        for (DepositLine line : entity.getLines()) {
          if (line.getSubject() == null || line.getSubject().getUuid() == null)
            continue;
          if (key.equals(line.getSubject().getUuid()))
            line.setRemainTotal(advMap.get(key));
        }
      }
      // 载入合同金额
      if (entity.getContract() != null && entity.getContract().getUuid() != null) {
        Map<String, BigDecimal> totalMap = contractService.getContractDepositTotals(entity
            .getContract().getUuid(), subjectUuids);
        for (DepositLine line : entity.getLines()) {
          BigDecimal contractTotal = totalMap.get(line.getSubject().getUuid());
          line.setContractTotal(contractTotal == null ? BigDecimal.ZERO : contractTotal);
        }
      }
    }

  }

  private Map<String, BigDecimal> getAdvanceMap(String accountUnitUuid, String counterpartUuid,
      Collection<String> subjectUuids, String contractUuid) throws M3ServiceException {
    try {
      Map<String, BigDecimal> advanceMap = new HashMap<String, BigDecimal>();

      if (subjectUuids == null || subjectUuids.isEmpty()) {
        return advanceMap;
      }

      if (contractUuid == null) {
        contractUuid = Advances.NONE_BILL_UUID;
      }

      QueryDefinition queryDef = new QueryDefinition();
      queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnitUuid.trim());
      queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpartUuid.trim());
      queryDef.addCondition(Advances.CONDITION_BILL_ID_EQUALS, contractUuid.trim());
      queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);
      queryDef.addCondition(Advances.CONDITION_SUBJECT_UUID_IN, subjectUuids.toArray());
      QueryResult<Advance> qr = advanceService.query(queryDef);

      if (qr.getRecords().isEmpty()) {
        return advanceMap;
      }

      for (Advance adv : qr.getRecords()) {
        advanceMap.put(adv.getSubject().getUuid(), adv.getTotal());
      }
      return advanceMap;
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(DepositQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected String getServiceBeanId() {
    return DepositService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return Deposits.OBJECT_NAME_RECEIPT;
  }

  private SUser getSUser() {
    IsOperator operator = getSessionUser();
    return accCommonComponent.getSUser(operator.getId());
  }

  private Map<String, UCN> getEmployees(List<Deposit> deposits) {
    List<String> empUuids = new ArrayList<String>();
    for (Deposit deposit : deposits) {
      if (empUuids.contains(deposit.getDealer())) {
        continue;
      } else {
        empUuids.add(deposit.getDealer());
      }
    }
    if (empUuids.isEmpty()) {
      return new HashMap<String, UCN>();
    }

    QueryDefinition queryDef = new QueryDefinition();
    QueryCondition cond = new QueryCondition();
    cond.setOperation(UserService.CONDITION_UUID_IN);
    cond.getParameters().addAll(
        empUuids.size() > 1000 ? Arrays.asList(Arrays.copyOf(empUuids.toArray(), 1000)) : empUuids);
    queryDef.getConditions().add(cond);

    QueryResult<User> users = getUserService().query(queryDef);
    Map<String, UCN> result = new HashMap<String, UCN>();
    for (User user : users.getRecords()) {
      result.put(user.getUuid(), new UCN(user.getUuid(), user.getId(), user.getFullName()));
    }
    return result;
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("预存款单")
    public String moduleCaption();

  }

}
