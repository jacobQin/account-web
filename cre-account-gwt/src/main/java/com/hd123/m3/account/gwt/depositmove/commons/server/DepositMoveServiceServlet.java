/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	DepositMoveServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.server;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContractState;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.rpc.DepositMoveService;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.filter.AdvanceContractFilter;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.filter.AdvanceSubjectFilter;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractState;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectType;
import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.service.counterpart.Counterparts;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author zhuhairui
 * 
 */
public class DepositMoveServiceServlet extends AccRemoteServiceServlet implements
    DepositMoveService {
  private static final long serialVersionUID = -4207854379741677897L;

  @Override
  public String getObjectName() {
    return DepositMove.class.getSimpleName();
  }

  @Override
  public RPageData<BUCN> querySubject(AdvanceSubjectFilter filter) throws ClientBizException {
    try {
      RPageData<BUCN> results;
      if (filter.isQueryAdvance())
        results = querySubjectFromAdvance(filter);
      else
        results = querySubjectFromSubject(filter);
      return results;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private RPageData<BUCN> querySubjectFromAdvance(AdvanceSubjectFilter filter) {

    QueryDefinition queryDef = new QueryDefinition();

    queryDef.addCondition(Advances.CONDITION_TOTAL_LAGGER, BigDecimal.ZERO);

    queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnitUuid());

    queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, filter.getCounterpartUuid());

    queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());

    queryDef.addCondition(Advances.CONDITION_BILL_ID_EQUALS,
        filter.getContractUuid() == null ? Advances.NONE_BILL_UUID : filter.getContractUuid());

    if (StringUtil.isNullOrBlank(filter.getCode()) == false)
      queryDef.addCondition(Advances.CONDITION_SUBJECT_CODE_START_WITH, filter.getCode());

    if (StringUtil.isNullOrBlank(filter.getName()) == false)
      queryDef.addCondition(Advances.CONDITION_SUBJECT_NAME_LIKE, filter.getName());

    QueryResult<Advance> qr = getAdvanceService().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return null;

    List<BUCN> list = new ArrayList<BUCN>();

    List<String> subjectCodes = new ArrayList<String>();
    for (Advance adv : qr.getRecords()) {
      if (subjectCodes.contains(adv.getSubject().getCode()))
        continue;
      else {
        subjectCodes.add(adv.getSubject().getCode());
        list.add(new BUCN(adv.getSubject().getUuid(), adv.getSubject().getCode(), adv.getSubject()
            .getName()));
      }
    }
    RPageData<BUCN> result = RPageDataUtil.flip(list, filter);

    if (filter.getOrders().isEmpty() == false)
      result.setValues(sortBUCNList(result.getValues(), filter.getOrders().get(0)));

    return result;
  }

  private RPageData<BUCN> querySubjectFromSubject(AdvanceSubjectFilter filter)
      throws IllegalArgumentException, M3ServiceException {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    queryDef.addCondition(Subjects.CONDITION_STATE_EQUALS, Boolean.TRUE);

    queryDef
        .addCondition(Subjects.CONDITION_SUBJECT_TYPE_EQUALS, SubjectType.predeposit.toString());

    queryDef.addCondition(Subjects.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());

    if (StringUtil.isNullOrBlank(filter.getCode()) == false)
      queryDef.addCondition(Subjects.CONDITION_CODE_STARTWITH, filter.getCode());

    if (StringUtil.isNullOrBlank(filter.getName()) == false)
      queryDef.addCondition(Subjects.CONDITION_NAME_LIKE, filter.getName());

    List<QueryOrder> orders = new ArrayList<QueryOrder>();
    if (filter.getOrders() != null && filter.getOrders().size() > 0) {
      for (Order order : filter.getOrders()) {
        String orderField = null;
        if (ObjectUtil.equals(AccountCpntsContants.ORDER_BY_FIELD_NAME, order.getFieldName()))
          orderField = Subjects.ORDER_BY_NAME;
        else
          orderField = Subjects.ORDER_BY_CODE;

        orders.add(new QueryOrder(orderField,
            OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
                : QueryOrderDirection.desc));
      }
    } else {
      orders.add(new QueryOrder(Subjects.ORDER_BY_CODE, QueryOrderDirection.asc));
    }
    queryDef.setOrders(orders);
    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());

    QueryResult<Subject> qr = getSubjectService().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return null;

    RPageData<BUCN> result = new RPageData<BUCN>();
    RPageDataUtil.copyPagingInfo(qr, result);

    for (Subject subject : qr.getRecords()) {
      result.getValues().add(new BUCN(subject.getUuid(), subject.getCode(), subject.getName()));
    }
    return result;
  }

  @Override
  public BUCN getSubjectByCode(String code, int direction, String accountUnitUuid,
      String counterpartUuid, String contractUuid, boolean isQueryAdvance)
      throws ClientBizException {
    try {
      BUCN result = null;
      if (isQueryAdvance)
        result = getSubjectFromAdvanceByCode(code, direction, accountUnitUuid, counterpartUuid,
            contractUuid);
      else
        result = getSubjectFromSubjectByCode(code, direction);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private BUCN getSubjectFromAdvanceByCode(String code, int direction, String accountUnitUuid,
      String counterpartUuid, String contractUuid) {
    QueryDefinition queryDef = new QueryDefinition();

    queryDef.addCondition(Advances.CONDITION_TOTAL_LAGGER, BigDecimal.ZERO);

    queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, direction);

    queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnitUuid);

    queryDef.addCondition(Advances.CONDITION_SUBJECT_CODE_EQUALS, code);

    queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpartUuid);

    queryDef.addCondition(Advances.CONDITION_BILL_ID_EQUALS,
        contractUuid == null ? Advances.NONE_BILL_UUID : contractUuid);

    QueryResult<Advance> qr = getAdvanceService().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return null;

    return UCNBizConverter.getInstance().convert(qr.getRecords().get(0).getSubject());
  }

  private BUCN getSubjectFromSubjectByCode(String code, int direction) {

    Subject subject = getSubjectService().getByCode(code);

    if (subject == null || subject.isEnabled() == false)
      return null;

    if (SubjectType.predeposit.equals(subject.getType()) == false)
      return null;

    if (subject.getDirection() != direction)
      return null;

    return new BUCN(subject.getUuid(), subject.getCode(), subject.getName());
  }

  @Override
  public RPageData<BAdvanceContract> queryContract(AdvanceContractFilter filter)
      throws ClientBizException {
    try {

      RPageData<BAdvanceContract> results;
      if (filter.isQueryAdvance())
        results = queryContractFromAdvance(filter);
      else
        results = queryContractFromContract(filter);
      return results;

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private RPageData<BAdvanceContract> queryContractFromAdvance(AdvanceContractFilter filter) {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    String accountUnitUuid = filter.getAccountUnitUuid();
    if (StringUtil.isNullOrBlank(accountUnitUuid) == false) {
      queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnitUuid);
    }

    // 受项目限制
    List<String> storeIds = getUserStores(getSessionUser().getId());
    if (storeIds.isEmpty()) {
      return new RPageData<BAdvanceContract>();
    }
    queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_IN, storeIds.toArray());
    queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());

    if (StringUtil.isNullOrBlank(filter.getBillNumber()) == false)
      queryDef.addCondition(Advances.CONDITION_BILL_BILLNUMBER_STARTWITH, filter.getBillNumber());

    if (StringUtil.isNullOrBlank(filter.getAccountUnit()) == false)
      queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_CODE_LIKE, filter.getAccountUnit());

    if (StringUtil.isNullOrBlank(filter.getCounterpart()) == false)
      queryDef.addCondition(Advances.CONDITION_COUNTERPART_LIKE, filter.getCounterpart());

    if (StringUtil.isNullOrBlank(filter.getTitle()) == false) {
      queryDef.addCondition(Advances.CONDITION_CONTRACT_NAME_LIKE, filter.getTitle());
    }

    String counterpartType = filter.getCounterpartType();
    if (StringUtil.isNullOrBlank(counterpartType) == false) {
      queryDef.addCondition(Advances.CONDITION_COUNTERPAR_TYPE_EQUALS, counterpartType);
    }

    QueryResult<Advance> qr = getAdvanceService().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return null;

    List<BAdvanceContract> list = new ArrayList<BAdvanceContract>();

    List<String> contractUuid = new ArrayList<String>();
    for (Advance adv : qr.getRecords()) {
      if (adv.getBill().getUuid().equals(Advances.NONE_BILL_UUID))
        continue;
      if (contractUuid.contains(adv.getBill().getUuid()))
        continue;
      else {
        contractUuid.add(adv.getBill().getUuid());
        BAdvanceContract contract = new BAdvanceContract();
        contract.setAccountUnit(UCNBizConverter.getInstance().convert(adv.getAccountUnit()));
        contract.setContract(UCNBizConverter.getInstance().convert(adv.getBill()));
        contract.setCounterpart(BCounterpartConverter.getInstance().convert(adv.getCounterpart(),
            adv.getCounterpartType()));

        list.add(contract);
      }
    }
    List<BAdvanceContract> result = new ArrayList<BAdvanceContract>();
    result.addAll(list);
    if (filter.getOrders().isEmpty() == false) {
      result = sortBAdvanceContractList(list, filter.getOrders().get(0));
    }

    return RPageDataUtil.flip(result, filter);
  }

  private RPageData<BAdvanceContract> queryContractFromContract(AdvanceContractFilter filter)
      throws IllegalArgumentException, M3ServiceException {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    // 启用合同授权
    boolean useContractPermT = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
    boolean useContractPermP = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR);

    List<String> userGroups = new ArrayList<String>();
    // 启用合同授权
    if (useContractPermT || useContractPermP) {
      userGroups = getUserGroups(getSessionUser().getId());

      if (userGroups == null || userGroups.isEmpty()) {
        return new RPageData<BAdvanceContract>();
      }
      queryDef.addCondition(Contracts.CONDITION_CUSTOM_PERM_GROUP_ID_IN,
         userGroups.toArray());
    }

    // 受项目限制
    List<String> list = getUserStores(getSessionUser().getId());
    if (list.isEmpty()) {
      return new RPageData<BAdvanceContract>();
    }
    queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_IN, list.toArray());

    queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING);

    if (StringUtil.isNullOrBlank(filter.getAccountUnitUuid()) == false)
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_EQUALS,
          filter.getAccountUnitUuid());

    if (StringUtil.isNullOrBlank(filter.getBillNumber()) == false)
      queryDef.addCondition(Contracts.CONDITION_BILLNUMBER_STARTWITH, filter.getBillNumber());

    if (StringUtil.isNullOrBlank(filter.getCounterpart()) == false)
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_LIKE, filter.getCounterpart());

    if (StringUtil.isNullOrBlank(filter.getTitle()) == false)
      queryDef.addCondition(Contracts.CONDITION_TITLE_LIKE, filter.getTitle());

    if (StringUtil.isNullOrBlank(filter.getFloor()) == false)
      queryDef.addCondition(Contracts.CONDITION_FLOOR_LIKE, filter.getFloor());

    if (StringUtil.isNullOrBlank(filter.getBrand()) == false)
      queryDef.addCondition(Contracts.CONDITION_BRAND_LIKE, filter.getBrand());

    if (StringUtil.isNullOrBlank(filter.getPosition()) == false)
      queryDef.addCondition(Contracts.CONDITION_POSITION_LIKE, filter.getPosition());

    String state = filter.getState();
    if (BContractState.INEFFECT.equals(state)) {
      queryDef.addCondition(Contracts.CONDITION_EFFECT_STATE_EQUALS,
          Contracts.EFFECT_STATE_INEFFECT);
    } else if (BContractState.EFFECTED.equals(state)) {
      queryDef.addCondition(Contracts.CONDITION_EFFECT_STATE_EQUALS,
          Contracts.EFFECT_STATE_EFFECTED);
    } else if (BContractState.FINISHED.equals(state)) {
      queryDef.addCondition(Contracts.CONDITION_EFFECT_STATE_EQUALS,
          Contracts.EFFECT_STATE_FINISHED);
    }
    String counterpartType = filter.getCounterpartType();
    if (StringUtil.isNullOrBlank(counterpartType) == false) {
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_TYPE_EQUALS, counterpartType);
    }
    List<QueryOrder> orders = new ArrayList<QueryOrder>();
    if (filter.getOrders() != null && filter.getOrders().size() > 0) {
      for (Order order : filter.getOrders()) {
        String orderField = null;
        if (ObjectUtil.equals(BAdvanceContract.FIELD_COUNTERPART, order.getFieldName()))
          orderField = Contracts.ORDER_BY_COUNTERPART;
        else if (ObjectUtil.equals(BAdvanceContract.FIELD_FLOOR, order.getFieldName()))
          orderField = Contracts.ORDER_BY_FLOOR;
        else
          orderField = Contracts.ORDER_BY_BILLNUMBER;

        orders.add(new QueryOrder(orderField,
            OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
                : QueryOrderDirection.desc));
      }
    } else {
      orders.add(new QueryOrder(Contracts.ORDER_BY_BILLNUMBER, QueryOrderDirection.asc));
    }
    queryDef.setOrders(orders);
    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());

    QueryResult<Contract> qr = getContractService().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return null;

    RPageData<BAdvanceContract> result = new RPageData<BAdvanceContract>();
    RPageDataUtil.copyPagingInfo(qr, result);

    for (Contract contract : qr.getRecords()) {
      BAdvanceContract bContract = new BAdvanceContract();
      bContract.setAccountUnit(UCNBizConverter.getInstance().convert(contract.getBusinessUnit()));
      bContract.setCounterpart(BCounterpartConverter.getInstance().convert(
          contract.getCounterpart(), contract.getCounterpartType()));
      bContract.setContract(new BUCN(contract.getUuid(), contract.getBillNumber(), contract
          .getTitle()));
      bContract.setFloor(UCNBizConverter.getInstance().convert(contract.getFloor()));
      bContract.setTitle(contract.getTitle());

      result.getValues().add(bContract);
    }
    return result;
  }

  @Override
  public BAdvanceContract getContractByBillNumber(String billNumber, int direction,
      String accountUnituuid, boolean isQueryAdvance) throws ClientBizException {
    try {

      BAdvanceContract result = null;
      if (isQueryAdvance) {
        result = getContractFromAdvanceByBillNumber(billNumber, direction);
      } else {
        result = getContractFromContractByBillNumber(billNumber, accountUnituuid);
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }

  }

  private BAdvanceContract getContractFromAdvanceByBillNumber(String billNumber, int direction) {

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, direction);
    queryDef.addCondition(Advances.CONDITION_BILL_BILLNUMBER_EQUALS, billNumber);

    QueryResult<Advance> qr = getAdvanceService().query(queryDef);

    if (qr == null || qr.getRecords().isEmpty())
      return null;

    Advance adv = qr.getRecords().get(0);
    BAdvanceContract result = new BAdvanceContract();
    result.setAccountUnit(UCNBizConverter.getInstance().convert(adv.getAccountUnit()));
    result.setContract(UCNBizConverter.getInstance().convert(adv.getBill()));
    result.setCounterpart(BCounterpartConverter.getInstance().convert(adv.getCounterpart(),
        adv.getCounterpartType()));
    // 受项目限制
    List<String> stores = getUserStores(getSessionUser().getId());
    if (stores.contains(result.getAccountUnit().getUuid()) == false) {
      return null;
    }
    // 启用合同授权
    boolean useContractPerm = permEnabled(Counterparts.MODULE_PROPRIETOR.equals(adv
        .getCounterpartType()) ? com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR
        : com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
    if (useContractPerm) {
      List<String> userGroupIds = getUserGroups(getSessionUser().getId());
      userGroupIds.add("-");
      Contract contract = getContractService().get(result.getContract().getUuid());
      if (userGroupIds.contains(contract.getPermGroupId()) == false) {
        return null;
      }
    }

    return result;
  }

  private BAdvanceContract getContractFromContractByBillNumber(String billNumber,
      String accountUnitUuid) {
    if (accountUnitUuid == null)
      return null;
    // 受项目限制
    List<String> stores = getUserStores(getSessionUser().getId());
    if (stores.contains(accountUnitUuid) == false) {
      return null;
    }

    Contract contract = getContractService().getByNumber(billNumber);
    if (contract == null)
      return null;
    else if (ContractState.using.equals(contract.getState()) == false)
      return null;
    else if (contract.getBusinessUnit().getUuid().equals(accountUnitUuid) == false)
      return null;

    // 启用合同授权
    boolean useContractPerm = permEnabled(Counterparts.MODULE_PROPRIETOR.equals(contract
        .getCounterpartType()) ? com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR
        : com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
    if (useContractPerm) {
      List<String> userGroups = getUserGroups(getSessionUser().getId());

      if (userGroups == null || userGroups.isEmpty()) {
        return null;
      }
      if (userGroups.contains(contract.getPermGroupId()) == false) {
        return null;
      }
    }

    BAdvanceContract result = new BAdvanceContract();
    result.setAccountUnit(UCNBizConverter.getInstance().convert(contract.getBusinessUnit()));
    result.setCounterpart(BCounterpartConverter.getInstance().convert(contract.getCounterpart(),
        contract.getCounterpartType()));
    result.setContract(new BUCN(contract.getUuid(), contract.getBillNumber(), contract.getTitle()));

    return result;
  }

  public static Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);

  /** 内存排序 */
  private List<BUCN> sortBUCNList(List<BUCN> values, Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    if (sortField == null)
      return values;
    List<BUCN> result = new ArrayList<BUCN>(values);
    Collections.sort(result, new Comparator<BUCN>() {
      public int compare(BUCN o1, BUCN o2) {
        Comparable c1, c2;
        if (sortField.equals(AccountCpntsContants.ORDER_BY_FIELD_CODE)) {
          c1 = o1.getCode();
          c2 = o2.getCode();
        } else if (sortField.equals(AccountCpntsContants.ORDER_BY_FIELD_NAME)) {
          c1 = o1.getName();
          c2 = o2.getName();
        } else {
          c1 = null;
          c2 = null;
        }
        int compare = 0;
        if (c1 != null && c2 != null)
          compare = cmp.compare(c1, c2);
        else if (c1 == null)
          compare = -1;
        else
          compare = 1;
        if (sortDir == OrderDir.desc)
          compare *= -1;
        return compare;
      }
    });
    return result;
  }

  /** 内存排序 */
  private List<BAdvanceContract> sortBAdvanceContractList(List<BAdvanceContract> values, Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    if (sortField == null)
      return values;
    List<BAdvanceContract> result = new ArrayList<BAdvanceContract>(values);
    Collections.sort(result, new Comparator<BAdvanceContract>() {
      public int compare(BAdvanceContract o1, BAdvanceContract o2) {
        Comparable c1, c2;
        if (sortField.equals(BAdvanceContract.FIELD_COUNTERPART)) {
          c1 = o1.getCounterpart().getCode();
          c2 = o2.getCounterpart().getCode();
        } else if (sortField.equals(BAdvanceContract.FIELD_BILLNUMBER)) {
          c1 = o1.getContract().getCode();
          c2 = o2.getContract().getCode();
        } else if (sortField.equals(BAdvanceContract.FIELD_ACCOUNTUNIT)) {
          c1 = o1.getAccountUnit().getCode();
          c2 = o2.getAccountUnit().getCode();
        } else if (sortField.equals(BAdvanceContract.FIELD_TITLE)) {
          c1 = o1.getContract().getName();
          c2 = o2.getContract().getName();
        } else {
          c1 = null;
          c2 = null;
        }
        int compare = 0;
        if (c1 != null && c2 != null)
          compare = cmp.compare(c1, c2);
        else if (c1 == null)
          compare = -1;
        else
          compare = 1;
        if (sortDir == OrderDir.desc)
          compare *= -1;
        return compare;
      }
    });
    return result;
  }

  private SubjectService getSubjectService() throws RuntimeException {
    return M3ServiceFactory.getService(SubjectService.class);
  }

  private AdvanceService getAdvanceService() throws RuntimeException {
    return M3ServiceFactory.getService(AdvanceService.class);
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("{0},{1}不是有效的数据，或者缺少授权组权限。")
    String accountUnitAndCounterpart();

    @DefaultStringValue("{0}不是有效的数据，或者缺少授权组权限。")
    String lackOfAuthorizatioGroups();
  }
}
