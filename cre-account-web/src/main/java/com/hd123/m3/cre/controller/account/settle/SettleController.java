/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	SettleController.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月3日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.settle;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NamingException;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.bpm.service.definition.ProcessDefinition;
import com.hd123.bpm.service.execution.ProcessService;
import com.hd123.bpm.service.execution.StartOperation;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.commons.Bill;
import com.hd123.m3.account.service.accounting.AccountingService;
import com.hd123.m3.account.service.accounting.CalculateParameter;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.QueryPagingUtil;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.controllers.module.BpmConstants;
import com.hd123.m3.commons.servlet.controllers.module.PermedModuleController;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.OperateContext;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * @author mengyinkun
 * 
 */
@Controller
@RequestMapping("account/settle/*")
public class SettleController extends PermedModuleController {

  private static final String KEY_STATEMENTDEFAULTBPM = "statementDefaultBPM";

  @Autowired
  private ContractService contractService;
  @Autowired
  private AccountingService accountingService;
  @Autowired
  private StatementService statementService;

  @Autowired
  private CommonComponent commonComponent;

  @Autowired
  private AccountOptionService optionService;
  @Autowired
  private ProcessService processService;

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put(KEY_STATEMENTDEFAULTBPM, getDefaultBPMKey(new QueryFilter()));
  }

  @RequestMapping("queryAccountSettle")
  @ResponseBody
  public QueryResult<AccountSettle> queryAccountSettle(@RequestBody QueryFilter filter)
      throws ClientBizException {
    try {

      filter.setPage(filter.getCurrentPage());
      FlecsQueryDefinition queryDef = buildFilter(filter);

      // 合同授权组
      boolean useContractPermT = getDataPermedAuxiliary().isPermEnabled(
          com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      boolean useContractPermP = getDataPermedAuxiliary().isPermEnabled(
          com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR);

      if (useContractPermP || useContractPermT) {
        List<String> list = getDataPermedAuxiliary().getUserGroupIds(getSessionUserId());
        list.add(HasPerm.DEFAULT_PERMGROUP);
        queryDef.addCondition(Contracts.CONDITION_CUSTOM_PERM_GROUP_ID_IN, list.toArray());
      }
      // 项目限制
      queryDef.addCondition(Contracts.CONDITION_ACCOUNT_UNIT_UUID_IN, getDataPermedAuxiliary()
          .getUserStoreIds(getSessionUserId()).toArray());

      // 查询
      QueryResult<AccountSettle> qr = contractService.queryAccountSettle(queryDef);
      // 排序
      List<AccountSettle> records = qr.getRecords();
      if (filter.getSorts().size() > 0) {
        OrderSort sort = filter.getSorts().get(0);
        SettleSortUtil.sortRecords(records, sort.getProperty(), sort);
      } else {
        SettleSortUtil.sortRecords(records, null, null);
      }
      // 分页
      QueryResult result = QueryPagingUtil.flip(records, filter.getCurrentPage() + 1,
          filter.getPageSize());
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private String getDefaultBPMKey(QueryFilter filter) {
    String defaultBPMKey = optionService.getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_STATEMENTDEFAULTBPM);
    List<ProcessDefinition> result = commonComponent.queryProcess(filter).getRecords();
    for (ProcessDefinition def : result) {
      if (def.getKey().equals(defaultBPMKey)) {
        return defaultBPMKey;
      }
    }
    return null;
  }

  private FlecsQueryDefinition buildFilter(QueryFilter filter) {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.setPage(0);
    queryDef.setPageSize(0);
    queryDef.addCondition(Contracts.CONDITION_CAN_ACCOUNT);
    queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING);
    // 项目
    String accountUnitUuid = (String) filter.getFilter().get("accountUnitUuid");
    if (StringUtil.isNullOrBlank(accountUnitUuid) == false) {
      queryDef.addCondition(Contracts.CONDITION_ACCOUNT_UNIT_UUID_EQUALS, accountUnitUuid.trim());
    }
    // 对方单位
    String counterpartUuid = (String) filter.getFilter().get("counterpartUuid");
    if (StringUtil.isNullOrBlank(counterpartUuid) == false) {
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS, counterpartUuid.trim());
    }
    // 合同编号
    String contractNumber = (String) filter.getFilter().get("contractNumber");
    if (StringUtil.isNullOrBlank(contractNumber) == false) {
      queryDef.addCondition(Contracts.CONDITION_ID_EQUALS, contractNumber.trim());
    }
    // 签约编号
    String signNumber = (String) filter.getFilter().get("signNumber");
    if (StringUtil.isNullOrBlank(signNumber) == false) {
      queryDef.addCondition(Contracts.CONDITION_SIGNNUMBER_LIKE, signNumber);
    }
    // 楼层uuid
    String floorUuid = (String) filter.getFilter().get("floorUuid");
    if (StringUtil.isNullOrBlank(floorUuid) == false) {
      queryDef.addCondition(Contracts.CONDITION_FLOOR_UUID_EQUALS, floorUuid.trim());
    }
    // 位置类型
    Map<String, Map> positions = (Map<String, Map>) filter.getFilter().get("position");
    if (positions != null && positions.isEmpty() == false) {
      Map<String, String> positionDetail = positions.get("entity");
      String positionTypes = positionDetail.get("positionType");
      if (StringUtil.isNullOrBlank(positionTypes) == false) {
        if (StringUtil.isNullOrBlank(positionTypes) == false) {
          queryDef.addCondition(Contracts.CONDITION_POSITION_TYPE_EQUALS, positionTypes);
        }
      }
      // 位置子类型
      String subType = positionDetail.get("subType");
      if (StringUtil.isNullOrBlank(subType) == false) {
        queryDef.addCondition(Contracts.CONDITION_POSITION_SUBTYPE_EQUALS, subType);
      }
      // 位置uuid
      String positionUuid = positionDetail.get("uuid");
      if (StringUtil.isNullOrBlank(positionUuid) == false) {
        queryDef.addCondition(Contracts.CONDITION_POSITION_UUID_EQUALS, positionUuid.trim());
      }
    }
    // 合作方式
    String coopMode = (String) filter.getFilter().get("coopMode");
    if (StringUtil.isNullOrBlank(coopMode) == false) {
      queryDef.addCondition(Contracts.CONDITION_COOPMODE_EQUALS, coopMode.trim());
    }
    // 结账周期名称
    String settleName = (String) filter.getFilter().get("settlement");
    if (StringUtil.isNullOrBlank(settleName) == false) {
      queryDef.addCondition(Contracts.CONDITION_SETTLE_CAPTION_LIKE, settleName.trim());
    }
    // 对方单位类型
    String counterpartType = (String) filter.getFilter().get("counterpartType");
    if (StringUtil.isNullOrBlank(counterpartType) == false) {
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_NAME_LIKE, counterpartType);
    }
    // 业态
    String category = (String) filter.getFilter().get("category");
    if (StringUtil.isNullOrBlank(category) == false) {
      queryDef.addCondition(Contracts.CONDITION_BIZTYPE_EQUALS, category);
    }
    // 楼宇uuid
    Map<String, Map> entity = (Map<String, Map>) filter.getFilter().get("building");
    if (entity != null && entity.isEmpty() == false) {
      Map<String, String> detail = entity.get("entity");
      String buildingUuid = detail.get("uuid");
      if (StringUtil.isNullOrBlank(buildingUuid) == false) {
        queryDef.addCondition(Contracts.CONDITION_BUILDING_UUID_EQUALS, buildingUuid);
      }
      // 楼宇类型
      String buildingType = detail.get("type");
      if (StringUtil.isNullOrBlank(buildingType) == false) {
        queryDef.addCondition(Contracts.CONDITION_BUILDINGTYPE_EQUALS, buildingType);
      }
    }
    return queryDef;
  }

  // 开单日+合同分组
  private List<AccountSettleGroup> handlerRecords(List<AccountSettle> records) {
    if (records == null || records.isEmpty()) {
      return new ArrayList<AccountSettleGroup>();
    }
    // 按合同+开单日分组
    Collection<List<AccountSettle>> group = group(records, new Comparator<AccountSettle>() {

      @Override
      public int compare(AccountSettle o1, AccountSettle o2) {
        if (o1.getContract().getUuid().equals(o2.getContract().getUuid())
            && o1.getPlanDate().compareTo(o2.getPlanDate()) == 0) {
          return 0;
        } else {
          return 1;
        }
      }
    });
    // 对每一个分组按起始日期升序排序
    List<AccountSettleGroup> result = new ArrayList<AccountSettleGroup>();
    for (List<AccountSettle> list : group) {
      Collections.sort(list, new Comparator<AccountSettle>() {

        @Override
        public int compare(AccountSettle o1, AccountSettle o2) {
          if (o1.getBeginDate().compareTo(o2.getBeginDate()) == 0) {
            return o1.getEndDate().compareTo(o2.getEndDate());
          } else {
            return o1.getBeginDate().compareTo(o2.getBeginDate());
          }
        }
      });
      AccountSettleGroup asg = new AccountSettleGroup();
      asg.setContractUuid(list.get(0).getContract().getUuid());
      asg.setSettles(list);
      result.add(asg);
    }
    // 对分组按开单日期升序排序
    Collections.sort(result, new Comparator<AccountSettleGroup>() {

      @Override
      public int compare(AccountSettleGroup ol1, AccountSettleGroup ol2) {
        AccountSettle o1 = ol1.getSettles().get(0);
        AccountSettle o2 = ol2.getSettles().get(0);
        if (o1.getPlanDate().compareTo(o2.getPlanDate()) == 0) {
          return o1.getBeginDate().compareTo(o2.getBeginDate());
        } else {
          return o1.getPlanDate().compareTo(o2.getPlanDate());
        }
      }
    });
    return result;
  }

  private static Collection group(Collection elements, Comparator c) {
    List<List> result = new ArrayList<List>();
    for (Object element : elements) {
      boolean found = false;
      for (List<Object> group : result) {
        if (c.compare(group.get(0), element) == 0) {
          group.add(element);
          found = true;
          break;
        }
      }
      if (!found) {
        List<Object> group = new ArrayList<Object>();
        group.add(element);
        result.add(group);
      }
    }
    return result;
  }

  @RequestMapping("calculate")
  @ResponseBody
  public String calculate(@RequestBody AccountSettleGroup group) throws Exception {
    try {
      String billNumber = calculateOne(group.getContractUuid(), group.getSettles(),
          group.getProcessDefKey(), group.getPermGroupId(), group.getPermGroupTitle());
      return billNumber;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping("byGroup")
  @ResponseBody
  public List<AccountSettleGroup> group(@RequestBody AccountSettleTool acc) throws Exception {
    List<AccountSettle> accountSettles = acc.getAccountSettles();
    List<AccountSettleGroup> group = handlerRecords(accountSettles);
    for (AccountSettleGroup accountSettleGroup : group) {
      accountSettleGroup.setPermGroupId(acc.getPermGroupId());
      accountSettleGroup.setPermGroupTitle(acc.getPermGroupTitle());
      accountSettleGroup.setProcessDefKey(acc.getProcessDefKey());
    }
    return group;
  }

  private String calculateOne(String calculateId, List<AccountSettle> accountSettles,
      String processDefKey, String permGroupId, String permGroupTitle) throws Exception {
    try {
      List<String> settleUuids = new ArrayList<String>();
      Map<String, List<AccountSettle>> map = new HashMap<String, List<AccountSettle>>();
      for (AccountSettle settle : accountSettles) {
        settleUuids.add(settle.getUuid());
        List<AccountSettle> list = map.get(settle.getSettlement().getUuid());
        if (list == null) {
          list = new ArrayList<AccountSettle>();
          map.put(settle.getSettlement().getUuid(), list);
        }
        list.add(settle);
      }

      for (Entry<String, List<AccountSettle>> entry : map.entrySet()) {
        sortSettleByBeginDate(entry.getValue());
        // // 当前出账记录之前存在未出帐记录，则不能出账。
        if (checkCalcRecord(entry.getValue().get(0)) == false) {
          throw new Exception(R.R.existsNoProcessRecordBefore());
        }
        if (entry.getValue().size() > 1) {
          for (int i = entry.getValue().size() - 1; i > 0; i--) {
            AccountSettle settle = entry.getValue().get(i);
            boolean found = false;
            for (int j = i - 1; j >= 0; j--) {
              AccountSettle settleJ = entry.getValue().get(j);
              Date endDate = DateUtils.truncate(settleJ.getEndDate(), Calendar.DATE);
              endDate = DateUtils.addDays(endDate, 1);
              if (settle.getBeginDate().compareTo(endDate) == 0) {
                found = true;
                break;
              }
            }
            if (found == false) {
              throw new Exception(R.R.existsNoProcessRecordBefore());
            }
          }
        }
      }
      CalculateParameter parameter = new CalculateParameter();
      parameter.setCalculateId(calculateId);
      parameter.setAccountSettleUuids(settleUuids);
      parameter.setContractId(accountSettles.get(0).getContract().getUuid());
      parameter.setPermGroupId(permGroupId);
      parameter.setPermGroupTitle(permGroupTitle);
      parameter.setStartProcess(false);
      Bill bill = accountingService.calculate(parameter, new BeanOperateContext(getSessionUser()));
      if (bill == null)
        return null;
      // 发起流程
      startStatementTask(bill, processDefKey);
      return bill.getBillNumber();
    } catch (Exception e) {
      throw e;
    }
  }

  private void sortSettleByBeginDate(List<AccountSettle> list) {
    if (list.size() == 1) {
      return;
    }

    // 按起始日期升序排序
    Collections.sort(list, new Comparator<AccountSettle>() {

      @Override
      public int compare(AccountSettle o1, AccountSettle o2) {
        if (o1.getBeginDate().compareTo(o2.getBeginDate()) == 0) {
          return o1.getEndDate().compareTo(o2.getEndDate());
        } else {
          return o1.getBeginDate().compareTo(o2.getBeginDate());
        }
      }
    });
  }

  private boolean checkCalcRecord(AccountSettle accountSettle) throws IllegalArgumentException,
      NamingException {
    FlecsQueryDefinition def = new FlecsQueryDefinition();
    def.addCondition(Contracts.CONDITION_CAN_ACCOUNT);
    def.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS, accountSettle.getContract()
        .getCounterpart().getUuid());
    def.addCondition(Contracts.CONDITION_BILLNUMBER_LIKE, accountSettle.getContract()
        .getBillNumber());
    def.addCondition(Contracts.CONDITION_SETTLE_START_DATE_BETWEEN, null,
        DateUtils.addSeconds(accountSettle.getBeginDate(), -1));
    def.addCondition(Contracts.CONDITION_SETTLEMENT_EQUALS, accountSettle.getSettlement().getUuid());
    def.setPage(0);
    def.setPageSize(1);

    QueryResult<AccountSettle> qr = contractService.queryAccountSettle(def);
    return qr.getRecords().isEmpty();
  }

  private void startStatementTask(Bill bill, String processDefKey) throws Exception {
    // 空账单或者未指定账单流程定义则忽略
    if (Contracts.EMPTY_BILL.equals(bill) || StringUtil.isNullOrBlank(processDefKey))
      return;
    Statement entity = statementService.get(bill.getBillUuid());

    // 发起流程
    Map<String, Object> variables = new HashMap();
    variables = commonComponent.getBpmVariables(entity, variables);
    variables.put(
        BpmConstants.KEY_BILLCAPTION,
        CodecUtils.encode(MessageFormat.format(R.R.billCaption(), entity.getBillNumber(),
            entity.getContract() == null ? null : entity.getContract().getCode())));
    StartOperation operation = new StartOperation();
    operation.setVariables(variables);
    operation.setBusinessKey(entity.getUuid());
    processService.startProcess(processDefKey, operation, new OperateContext(getSessionUser()));

    String bpmInstance = processService.startProcess(processDefKey, operation, new OperateContext(
        getSessionUser()));
    entity.setBpmInstance(bpmInstance);
    statementService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("账单")
    String statementCaption();

    @DefaultStringValue("账单：{0},合同编号：{1}")
    String billCaption();

    @DefaultStringValue("找不到指定流程实例({0})所属的任务。")
    String noFoundTask();

    @DefaultStringValue("当前出账的时间范围前，存在未出帐的记录，不能进行出账。")
    String existsNoProcessRecordBefore();
  }

  @Override
  protected String getObjectName() {
    return Statements.OBJECT_NAME;
  }
}
