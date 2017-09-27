/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	DepositPaymentController.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.payment;

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
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.account.service.deposit.DepositLine;
import com.hd123.m3.account.service.deposit.DepositService;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.account.service.depositrepayment.DepositRepaymentLine;
import com.hd123.m3.account.service.depositrepayment.DepositRepaymentService;
import com.hd123.m3.account.service.depositrepayment.DepositRepaymentTotal;
import com.hd123.m3.account.service.depositrepayment.DepositRepayments;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountCommonComponent;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.common.AdvanceSubjectFilter;
import com.hd123.m3.cre.controller.account.common.model.SUser;
import com.hd123.m3.cre.controller.account.deposit.builder.DepositRepaymentQueryBuilder;
import com.hd123.m3.cre.controller.account.deposit.model.BDepositRepayment;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.util.DateUtil;

/**
 * 预存款还款单控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/deposit/repayment/*")
public class DepositRepaymentController extends BizFlowModuleController {

  /** 键值:保证金按单管理 */
  public static final String OPTION_BYDEPOSIT_ENABLED = "byDepositEnabled";

  @Autowired
  private AccountCommonComponent accCommonComponent;
  @Autowired
  private DepositRepaymentService depositRepaymentService;
  @Autowired
  private AdvanceService advanceService;
  @Autowired
  private AccountOptionComponent optionComponent;
  @Autowired
  DepositService depositService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    Set<String> resourceIds = new HashSet<String>();
    resourceIds.add(AccountPermResourceDef.RESOURCE_DEPOSIT_RECDEPOSITREPAYMENT);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        resourceIds));
    return permissions;
  }

  @Override
  protected void buildSummary(SummaryQueryResult result, QueryFilter queryFilter) {
    FlecsQueryDefinition definition = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    DepositRepaymentTotal total = depositRepaymentService.queryTotal(definition);
    result.getSummary().put("repaymentTotal", total.getRepaymentTotal());
    super.buildSummary(result, queryFilter);
  }

  /** 创建预存款还款单,并且同事设置默认值 */
  @RequestMapping(value = "create", method = RequestMethod.GET)
  public @ResponseBody BDepositRepayment create() throws M3ServiceException {
    try {
      BDepositRepayment depositRepayment = new BDepositRepayment();
      depositRepayment.setBizState(BizStates.INEFFECT);
      depositRepayment.setDirection(Direction.RECEIPT);
      decorateDepositRepayment(depositRepayment);
      return depositRepayment;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }

  }

  @RequestMapping(value = "getLinesByDeposit", method = RequestMethod.POST)
  public @ResponseBody List<DepositRepaymentLine> getDepositLinesByDeposit(
      @RequestBody Deposit deposit) throws M3ServiceException {
    try {

      Deposit d = depositService.get(deposit.getUuid());
      if (d == null) {
        return new ArrayList<DepositRepaymentLine>();
      }

      List<String> subejcts = new ArrayList<String>();
      List<DepositRepaymentLine> repaymentLines = new ArrayList<DepositRepaymentLine>();
      for (DepositLine depositLine : d.getLines()) {
        DepositRepaymentLine line = new DepositRepaymentLine();
        line.setAmount(depositLine.getAmount());
        line.setSubject(depositLine.getSubject());
        line.setRemark(depositLine.getRemark());
        repaymentLines.add(line);

        subejcts.add(depositLine.getSubject().getUuid());
      }

      String contractUuid = null;
      if (d.getContract() != null) {
        contractUuid = d.getContract().getUuid();
      }

      Map<String, BigDecimal> remainTotals = getAdvanceMap(d.getAccountUnit().getUuid(), d
          .getCounterpart().getUuid(), subejcts, contractUuid);
      for (DepositRepaymentLine repaymentLine : repaymentLines) {
        BigDecimal remainTotal = remainTotals.get(repaymentLine.getSubject().getUuid());
        if (remainTotal != null) {
          repaymentLine.setRemainAmount(remainTotal);
        }
      }

      return repaymentLines;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }

  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    // 覆盖当前用户,增加属性
    moduleContext.put(OPTION_BYDEPOSIT_ENABLED, true);
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    DepositRepayment entity = JsonUtil.jsonToObject(json, DepositRepayment.class);
    // 移除空行
    for (int index = entity.getLines().size() - 1; index >= 0; index--) {
      DepositRepaymentLine line = entity.getLines().get(index);
      if (line.getSubject() == null || line.getSubject().getUuid() == null) {
        entity.getLines().remove(index);
      }
    }

    return depositRepaymentService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody BDepositRepayment load(@RequestParam("uuid") String id)
      throws M3ServiceException {
    DepositRepayment depositRepayment = depositRepaymentService.get(id);
    if (depositRepayment == null || depositRepayment.getDirection() == Direction.PAYMENT) {
      return null;
    }
    BDepositRepayment result = BDepositRepayment.newInstance(depositRepayment);
    if (result.getDealer() != null) {
      User user = getUserService().get(result.getDealer());
      if (user != null) {
        result.setViewDealer(new UCN(user.getUuid(), user.getId(), user.getFullName()));
      }
    }

    if (BizStates.INEFFECT.equals(result.getBizState())) {
      decorateDepositLine(result);
    }

    return result;
  }

  @RequestMapping(value = "getAdvance", method = RequestMethod.GET)
  public @ResponseBody BigDecimal getAdvance(@QueryParam("accountUuid") String accountUnitUuid,
      @QueryParam("counterpartUuid") String counterpartUuid,
      @RequestParam("subjectUuid") String subjectUuid,
      @RequestParam("contractUuid") String contractUuid) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(contractUuid)) {
        contractUuid = Advances.NONE_BILL_UUID;
      }
      Advance advance = getAdvanceService().get(accountUnitUuid, counterpartUuid, contractUuid,
          subjectUuid);
      if (advance == null) {
        return BigDecimal.ZERO;
      } else {
        return advance.getTotal();
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @RequestMapping("advancesubjects")
  public @ResponseBody List<UCN> getAdvanceSubjects(@RequestBody AdvanceSubjectFilter filter)
      throws Exception {

    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Advances.CONDITION_TOTAL_LAGGER, BigDecimal.ZERO);
    queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnitUuid());
    queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());
    queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, filter.getCounterpartUuid());
    queryDef.addCondition(Advances.CONDITION_BILL_ID_EQUALS,
        filter.getContractUuid() == null ? Advances.NONE_BILL_UUID : filter.getContractUuid());
    if (StringUtil.isNullOrBlank(filter.getKeyword()) == false) {
      queryDef.addCondition(Advances.CONDITION_SUBJECT_CODE_START_WITH, filter.getKeyword());
      queryDef.addCondition(Advances.CONDITION_SUBJECT_NAME_LIKE, filter.getKeyword());
    }
    queryDef.addOrder(Advances.ORDER_BY_SUBJECT_CODE);
    QueryResult<Advance> qr = getAdvanceService().query(queryDef);

    if (qr.getRecords().isEmpty()) {
      return new ArrayList<UCN>();
    }
    List<UCN> subjects = new ArrayList<UCN>();
    List<String> subjectCodes = new ArrayList<String>();
    for (Advance adv : qr.getRecords()) {
      if (subjectCodes.contains(adv.getSubject().getCode())) {
        continue;
      } else {
        subjectCodes.add(adv.getSubject().getCode());
        subjects.add(new UCN(adv.getSubject().getUuid(), adv.getSubject().getCode(), adv
            .getSubject().getName()));
      }
    }

    return subjects;
  }

  private AdvanceService getAdvanceService() {
    return M3ServiceFactory.getService(AdvanceService.class);
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
  public SummaryQueryResult query(@RequestBody QueryFilter queryFilter) {
    if (queryFilter == null) {
      return new SummaryQueryResult();
    }
    queryFilter.getFilter().put(DepositRepayments.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);
    return super.query(queryFilter);
  }

  protected void decorateRecords(SummaryQueryResult queryResult) {
    Map<String, UCN> map = getEmployees(queryResult.getRecords());
    List<BDepositRepayment> records = new ArrayList<BDepositRepayment>();
    for (Object object : queryResult.getRecords()) {
      DepositRepayment depositRepayment = (DepositRepayment) object;
      BDepositRepayment biz = BDepositRepayment.newInstance(depositRepayment);
      biz.setViewDealer(map.get(depositRepayment.getDealer()));
      records.add(biz);
    }
    queryResult.setRecords(records);
  }

  private void decorateDepositLine(BDepositRepayment result) {
    List<String> subjectUuids = new ArrayList<String>();

    for (DepositRepaymentLine line : result.getLines()) {
      if (subjectUuids.contains(line.getSubject().getUuid())) {
        continue;
      } else {
        subjectUuids.add(line.getSubject().getUuid());
      }
    }

    if (subjectUuids.isEmpty()) {
      return;
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
      for (DepositRepaymentLine line : result.getLines()) {
        if (advance.getSubject().getUuid().equals(line.getSubject().getUuid())) {
          line.setRemainAmount(advance.getTotal());
        }
      }
    }
  }

  private Map<String, UCN> getEmployees(List<DepositRepayment> depositRepayments) {
    List<String> empUuids = new ArrayList<String>();
    for (DepositRepayment depositRepayment : depositRepayments) {
      if (empUuids.contains(depositRepayment.getDealer())) {
        continue;
      } else {
        empUuids.add(depositRepayment.getDealer());
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

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(DepositRepaymentQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected String getServiceBeanId() {
    return DepositRepaymentService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return DepositRepayments.OBJECT_NAME_RECEIPT;
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("预存款还款单")
    public String moduleCaption();

  }

  private SUser getSUser() {
    IsOperator operator = getSessionUser();
    return accCommonComponent.getSUser(operator.getId());
  }

  private void decorateDepositRepayment(BDepositRepayment depositRepayment) {
    depositRepayment.setPaymentType(getDefaltPaymentType());
    depositRepayment.setRepaymentDate(DateUtil.datePart(new Date()));
    depositRepayment.setAccountDate(DateUtil.datePart(new Date()));
    depositRepayment.setCounterpartType(CounterpartType.TENANT);
    depositRepayment.setViewDealer(getSUser());
    depositRepayment.setDealer(depositRepayment.getViewDealer().getUuid());
  }

  private UCN getDefaltPaymentType() {
    PaymentType type = optionComponent.getDefaltPaymentType();
    if (type == null) {
      return null;
    }
    return new UCN(type.getUuid(), type.getCode(), type.getName());
  }

}
