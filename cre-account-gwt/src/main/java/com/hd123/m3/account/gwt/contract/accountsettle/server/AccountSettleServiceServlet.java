/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： AccountSettleServiceServlet.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.server;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.naming.NamingException;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.hd123.bpm.service.execution.Definition;
import com.hd123.bpm.service.execution.ProcessService;
import com.hd123.bpm.service.execution.StartOperation;
import com.hd123.bpm.widget.interaction.server.ProcessClient;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.commons.Bill;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.contract.accountsettle.client.rpc.AccountSettleFilter;
import com.hd123.m3.account.gwt.contract.accountsettle.client.rpc.AccountSettleService;
import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.perm.AccountSettlePermDef;
import com.hd123.m3.account.gwt.contract.accountsettle.server.converter.AccountSettleBizConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.server.converter.ContractBizConverter;
import com.hd123.m3.account.service.accounting.AccountingService;
import com.hd123.m3.account.service.accounting.CalculateParameter;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.BillCalculateType;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.base.client.M3Sessions;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.service.counterpart.Counterparts;
import com.hd123.m3.mdata.service.typetag.TypeTag;
import com.hd123.m3.mdata.service.typetag.TypeTags;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author huangjunxian
 * 
 */
public class AccountSettleServiceServlet extends AccBPMServiceServlet
    implements AccountSettleService {

  private static final long serialVersionUID = 300200L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        AccountSettlePermDef.RESOURCE_ACCOUNTSETTLE_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    String defaultBPMKey = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_STATEMENTDEFAULTBPM);
    moduleContext.put(AccountSettleService.KEY_DEFAULTBPMKEY, defaultBPMKey);
    moduleContext.put(M3Sessions.KEY_ENABLE_PERM, Statements.OBJECT_NAME);
    if (moduleContext.get(M3Sessions.KEY_PERM_GROUPS) == null) {
      moduleContext.put(M3Sessions.KEY_PERM_GROUPS, encodeCurrentGroups());
    }

    moduleContext.put(AccountSettleService.KEY_BUILDING_TYPE,
        CollectionUtil.toString(new ArrayList<String>()));

  }

  private String encodeCurrentGroups() {
    List<Pair<String, String>> groups = getUserGroupList(getSessionUser().getId(), true);
    List<String> list = new ArrayList<String>();
    for (Pair<String, String> group : groups) {
      list.add(group.getKey());
      list.add(group.getValue());
    }
    return com.hd123.rumba.commons.collection.CollectionUtil.toString(list);
  }

  @Override
  public RPageData<BAccountSettle> query(AccountSettleFilter filter) throws ClientBizException {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.setPage(0);
      queryDef.setPageSize(0);

      // 条件
      queryDef.addCondition(Contracts.CONDITION_CAN_ACCOUNT);
      queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING);

      if (StringUtil.isNullOrBlank(filter.getAccountUnitUuid()) == false) {
        queryDef.addCondition(Contracts.CONDITION_ACCOUNT_UNIT_UUID_EQUALS,
            filter.getAccountUnitUuid().trim());
      }
      // 受项目限制
      List<String> list = getUserStores(getSessionUser().getId());
      if (list.isEmpty()) {
        return new RPageData<BAccountSettle>();
      }
      queryDef.addCondition(Contracts.CONDITION_ACCOUNT_UNIT_UUID_IN, list.toArray());
      // 启用合同授权组
      boolean useContractPermT = permEnabled(
          com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      boolean useContractPermP = permEnabled(
          com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR);
      if (useContractPermT || useContractPermP) {
        List<String> userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups == null) {
          userGroups = new ArrayList<String>();
        }
        if (userGroups.contains(HasPerm.DEFAULT_PERMGROUP) == false) {
          userGroups.add(HasPerm.DEFAULT_PERMGROUP);
        }

        queryDef.addCondition(Contracts.CONDITION_CUSTOM_PERM_GROUP_ID_IN,userGroups.toArray());
      }

      if (StringUtil.isNullOrBlank(filter.getCounterpartUuid()) == false) {
        queryDef.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS,
            filter.getCounterpartUuid().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getContractNumber()) == false) {
        queryDef.addCondition(Contracts.CONDITION_BILLNUMBER_LIKE,
            filter.getContractNumber().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getContractTitle()) == false) {
        queryDef.addCondition(Contracts.CONDITION_TITLE_LIKE, filter.getContractTitle().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getFloorUuid()) == false) {
        queryDef.addCondition(Contracts.CONDITION_FLOOR_UUID_EQUALS, filter.getFloorUuid().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getPositionType()) == false) {
        queryDef.addCondition(Contracts.CONDITION_POSITION_TYPE_EQUALS,
            filter.getPositionType().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getPositionSubType()) == false) {
        queryDef.addCondition(Contracts.CONDITION_POSITION_SUBTYPE_EQUALS,
            filter.getPositionSubType().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getPositionUuid()) == false) {
        queryDef.addCondition(Contracts.CONDITION_POSITION_UUID_EQUALS,
            filter.getPositionUuid().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getCoopMode()) == false) {
        queryDef.addCondition(Contracts.CONDITION_COOPMODE_EQUALS, filter.getCoopMode().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getContractCategory()) == false) {
        queryDef.addCondition(Contracts.CONDITION_CONTRACT_CATEGORY_EQUALS,
            filter.getContractCategory().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getSettleName()) == false) {
        queryDef.addCondition(Contracts.CONDITION_SETTLE_CAPTION_LIKE,
            filter.getSettleName().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getBillCalculateType()) == false) {
        queryDef.addCondition(Contracts.CONDITION_BILLCALCULATE_TYPE_EQUALS,
            BillCalculateType.valueOf(filter.getBillCalculateType().trim()));
      }
      if (StringUtil.isNullOrBlank(filter.getCounterpartType()) == false) {
        queryDef.addCondition(Contracts.CONDITION_COUNTERPART_TYPE_EQUALS,
            filter.getCounterpartType());
      }
      if (StringUtil.isNullOrBlank(filter.getCategory()) == false) {
        queryDef.addCondition(Contracts.CONDITION_BIZTYPE_EQUALS, filter.getCategory());
      }
      if (StringUtil.isNullOrBlank(filter.getBuildingUuid()) == false) {
        queryDef.addCondition(Contracts.CONDITION_BUILDING_UUID_EQUALS, filter.getBuildingUuid());
      }
      if (StringUtil.isNullOrBlank(filter.getBuildingType()) == false) {
        queryDef.addCondition(Contracts.CONDITION_BUILDINGTYPE_EQUALS, filter.getBuildingType());
      }

      // 排序
      List<QueryOrder> orders = new ArrayList<QueryOrder>();
      if (filter.getOrders() != null && filter.getOrders().isEmpty() == false) {
        for (Order order : filter.getOrders()) {
          orders.add(new QueryOrder(convertOrderField(order.getFieldName()),
              OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
                  : QueryOrderDirection.desc));
        }
      } else {
        orders.add(new QueryOrder(Contracts.ORDER_BY_COUNTERPART, QueryOrderDirection.asc));
        orders.add(new QueryOrder(Contracts.SETTLE_ORDER_BY_CAPTION, QueryOrderDirection.asc));
      }
      queryDef.setOrders(orders);

      QueryResult<AccountSettle> qr = getContractService().queryAccountSettle(queryDef);

      RPageData<BAccountSettle> rp = new RPageData<BAccountSettle>();
      RPageDataUtil.copyPagingInfo(qr, rp);
      rp.setValues(ConverterUtil.convert(qr.getRecords(), AccountSettleBizConverter.getInstance()));

      return rp;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String calculate(String calculateId, List<BAccountSettle> accountSettles,
      String processDefKey, String permGroupId, String permGroupTitle) throws ClientBizException {
    try {
      List<String> settleUuids = new ArrayList<String>();
      Map<String, List<BAccountSettle>> map = new HashMap<String, List<BAccountSettle>>();
      for (BAccountSettle settle : accountSettles) {
        settleUuids.add(settle.getUuid());
        List<BAccountSettle> list = map.get(settle.getSettlementUuid());
        if (list == null) {
          list = new ArrayList<BAccountSettle>();
          map.put(settle.getSettlementUuid(), list);
        }
        list.add(settle);
      }
      for (Entry<String, List<BAccountSettle>> entry : map.entrySet()) {
        sortSettleByBeginDate(entry.getValue());
        // 当前出账记录之前存在未出帐记录，则不能出账。
        if (checkCalcRecord(entry.getValue().get(0)) == false) {
          throw new Exception(R.R.existsNoProcessRecordBefore());
        }
        if (entry.getValue().size() > 1) {
          for (int i = entry.getValue().size() - 1; i > 0; i--) {
            BAccountSettle settle = entry.getValue().get(i);
            boolean found = false;
            for (int j = i - 1; j >= 0; j--) {
              BAccountSettle settleJ = entry.getValue().get(j);
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
      parameter.setContractId(accountSettles.get(0).getContractUuid());
      parameter.setPermGroupId(permGroupId);
      parameter.setPermGroupTitle(permGroupTitle);
      parameter.setStartProcess(false);
      Bill bill = getAccountingService().calculate(parameter,
          new BeanOperateContext(getSessionUser()));
      if (bill == null)
        return null;
      // 发起流程
      startStatementTask(bill, processDefKey);
      return bill.getBillNumber();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void sortSettleByBeginDate(List<BAccountSettle> list) {
    if (list.size() == 1) {
      return;
    }

    // 按起始日期升序排序
    Collections.sort(list, new Comparator<BAccountSettle>() {

      @Override
      public int compare(BAccountSettle o1, BAccountSettle o2) {
        if (o1.getBeginDate().compareTo(o2.getBeginDate()) == 0) {
          return o1.getEndDate().compareTo(o2.getEndDate());
        } else {
          return o1.getBeginDate().compareTo(o2.getBeginDate());
        }
      }
    });
  }

  @Override
  public List<BProcessDefinition> queryProcessDefinition() throws ClientBizException {
    try {
      List<Definition> defs = getProcessService().getDefinitionsByKeyPrefix(Statements.OBJECT_NAME);
      if (defs == null || defs.isEmpty()) {
        return new ArrayList<BProcessDefinition>();
      }
      Set<String> keys = new HashSet<String>();
      for (Definition def : defs) {
        keys.add(def.getDefinitionKey());
      }
      // 过滤无发齐全的流程
      Collection<String> canStartList = getProcessService().canStartProcesses(keys,
          getSessionUser().getId());
      List<BProcessDefinition> result = new ArrayList<BProcessDefinition>();
      for (Definition def : defs) {
        if (canStartList.contains(def.getDefinitionKey()) == false) {
          continue;
        }
        result.add(new BProcessDefinition(def.getDefinitionKey(), def.getName()));
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BContract getOnlyOneContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException {
    try {

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING, "");
      queryDef.addCondition(Contracts.CONDITION_CONTRACT_UNLOCKED, "");// 未被锁定
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_EQUALS, accountUnitUuid);
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS, counterpartUuid);

      // 对方单位uuid已确定，不需要指定合同类型。
      boolean useContractPerm = permEnabled(Counterparts.MODULE_PROPRIETOR.equals(counterpartType)
          ? com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR
          : com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      if (useContractPerm) {
        // 启用授权组
        List<String> userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups != null && userGroups.isEmpty() == false) {
          queryDef.addCondition(Basices.CONDITION_PERM_GROUP_ID_IN, userGroups.toArray());
        } else {
          return null;
        }
      }

      // 受项目限制
      List<String> stores = getUserStores(getSessionUser().getId());
      if (stores.contains(accountUnitUuid) == false) {
        return null;
      }

      QueryResult<Contract> result = getContractService().query(queryDef);
      return result.getRecordCount() == 1
          ? ContractBizConverter.getInstance().convert(result.getRecords().get(0)) : null;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String getCalculateId() throws ClientBizException {
    return UUID.randomUUID().toString();
  }

  @Override
  public String getObjectName() {
    return AccountSettle.class.getSimpleName();
  }

  private boolean checkCalcRecord(BAccountSettle accountSettle)
      throws IllegalArgumentException, NamingException {
    FlecsQueryDefinition def = new FlecsQueryDefinition();
    def.addCondition(Contracts.CONDITION_CAN_ACCOUNT);
    def.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS,
        accountSettle.getCounterpart().getUuid());
    def.addCondition(Contracts.CONDITION_BILLNUMBER_LIKE, accountSettle.getContractNumber());
    def.addCondition(Contracts.CONDITION_SETTLE_START_DATE_BETWEEN, null,
        DateUtils.addSeconds(accountSettle.getBeginDate(), -1));
    def.addCondition(Contracts.CONDITION_SETTLEMENT_EQUALS, accountSettle.getSettlementUuid());
    def.setPage(0);
    def.setPageSize(1);

    QueryResult<AccountSettle> qr = getContractService().queryAccountSettle(def);
    return qr.getRecords().isEmpty();
  }

  private void startStatementTask(Bill bill, String processDefKey) throws Exception {
    // 空账单或者未指定账单流程定义则忽略
    if (Contracts.EMPTY_BILL.equals(bill) || StringUtil.isNullOrBlank(processDefKey))
      return;
    Statement result = getStatementService().get(bill.getBillUuid());

    // 发起流程
    Map<String, Object> variables = new HashMap();
    variables = getBpmVariables(result, variables);
    variables.put(BpmConstants.KEY_BILLCAPTION,
        CodecUtils.encode(MessageFormat.format(R.R.billCaption(), result.getBillNumber(),
            result.getContract() == null ? null : result.getContract().getCode())));
    StartOperation operation = new StartOperation();
    operation.setVariables(variables);
    operation.setBusinessKey(result.getUuid());
    getProcessClient().start(processDefKey, operation);
  }

  protected ProcessClient getProcessClient() {
    return ProcessClient.getInstance(getAppCtx(), getSessionUser());
  }

  @Override
  public List<String> getCoopModes() throws ClientBizException {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.addFlecsCondition(TypeTags.FIELD_CATEGORY, TypeTags.OPERATOR_EQUALS,
          "investment.coopMode");
      queryDef.addFlecsCondition(TypeTags.FIELD_SUBTYPE, TypeTags.OPERATOR_EQUALS, "tenant");
      List<TypeTag> values = getTypeTagService().query(queryDef).getRecords();

      List<String> results = new ArrayList<String>();
      for (TypeTag coopType : values) {
        results.add(coopType.getName());
      }
      return results;
    } catch (Exception e) {
      return new ArrayList<String>();
    }
  }

  private String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (BAccountSettle.FIELD_COUNTERPART.equals(fieldName)) {
      return Contracts.ORDER_BY_COUNTERPART;
    } else if (BAccountSettle.FIELD_SETTLENAME.equals(fieldName)) {
      return Contracts.SETTLE_ORDER_BY_CAPTION;
    } else if (BAccountSettle.FIELD_BEGINDATE.equals(fieldName)) {
      return Contracts.SETTLE_ORDER_BY_BEGINDATE;
    } else if (BAccountSettle.FIELD_CONTRACT_TITLE.equals(fieldName)) {
      return Contracts.ORDER_BY_TITLE;
    } else if (BAccountSettle.FIELD_CONTRACT_NUMBER.equals(fieldName)) {
      return Contracts.ORDER_BY_BILLNUMBER;
    } else if (BAccountSettle.FIELD_ACCOUNTTIME.equals(fieldName)) {
      return Contracts.SETTLE_ORDER_BY_PLANDATE;
    } else if (BAccountSettle.FIELD_BILLCALCTYPE.equals(fieldName)) {
      return Contracts.SETTLE_ORDER_BY_BILLCALCULATETYPE;
    } else if (BAccountSettle.FIELD_FLOOR.equals(fieldName)) {
      return Contracts.ORDER_BY_FLOOR;
    } else if (BAccountSettle.FIELD_COOPMODE.equals(fieldName)) {
      return Contracts.ORDER_BY_COOPMODE;
    } else if (BAccountSettle.FIELD_CONTRACT_CATEGORY.equals(fieldName)) {
      return Contracts.ORDER_BY_CONTRACT_CATEGORY;
    }

    return null;
  }

  // EJB接口类
  private AccountingService getAccountingService() throws NamingException {
    return M3ServiceFactory.getService(AccountingService.class);
  }

  private StatementService getStatementService() throws NamingException {
    return M3ServiceFactory.getService(StatementService.class);
  }

  private ProcessService getProcessService() {
    return M3ServiceFactory.getService(ProcessService.class);
  }

  @Override
  protected String getObjectCaption() {
    return R.R.statementCaption();
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

}
