/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-common
 * 文件名： AccountCpntsServiceImpl.java
 * 模块说明：    
 * 修改历史：
 * 2013-9-29 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;

import com.hd123.ia.authen.service.organization.Organization;
import com.hd123.ia.authen.service.organization.OrganizationService;
import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BEmployee;
import com.hd123.m3.account.gwt.cpnts.client.biz.BMonthSettle;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.ContractFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SettleNoFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SubjectFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.BInvoiceStock;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.PositionFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.SPosition;
import com.hd123.m3.account.gwt.cpnts.client.ui.store.BStoreNavigator;
import com.hd123.m3.account.gwt.cpnts.server.converter.BizTypeBizConverter;
import com.hd123.m3.account.gwt.cpnts.server.converter.ContractBizConverter;
import com.hd123.m3.account.gwt.cpnts.server.converter.CounterpartBizConverter;
import com.hd123.m3.account.gwt.cpnts.server.converter.EmployeeBizConverter;
import com.hd123.m3.account.gwt.cpnts.server.converter.InvoiceStockBizConverter;
import com.hd123.m3.account.gwt.cpnts.server.converter.MonthSettleBizConverter;
import com.hd123.m3.account.gwt.cpnts.server.converter.PositionBizConverter;
import com.hd123.m3.account.gwt.cpnts.server.converter.PositionSimpleConverter;
import com.hd123.m3.account.gwt.cpnts.server.converter.SubjectBizConverter;
import com.hd123.m3.account.gwt.cpnts.server.query.ContractQueryBuilder;
import com.hd123.m3.account.gwt.cpnts.server.query.CounterpartQueryBuilder;
import com.hd123.m3.account.gwt.cpnts.server.query.InvoiceStockQueryBuilder;
import com.hd123.m3.account.gwt.cpnts.server.query.PositionQueryBuilder;
import com.hd123.m3.account.gwt.cpnts.server.query.StoreQueryBuilder;
import com.hd123.m3.account.gwt.cpnts.server.query.TenantQueryBuilder;
import com.hd123.m3.account.gwt.util.client.ObjectUtil;
import com.hd123.m3.account.gwt.util.server.SubjectUsageUtils;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractState;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStockService;
import com.hd123.m3.account.service.monthsettle.MonthSettle;
import com.hd123.m3.account.service.monthsettle.MonthSettleService;
import com.hd123.m3.account.service.monthsettle.MonthSettles;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.BasicState;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.option.BasicConfigService;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.converter.FlecsQueryDefinitionConverter;
import com.hd123.m3.commons.biz.store.MStore;
import com.hd123.m3.commons.data.receiptor.M3UserOrgService;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.commons.rs.service.store.RSStoreService;
import com.hd123.m3.commons.rs.service.store.Stores;
import com.hd123.m3.investment.rs.service.biztype.RSBizTypeService;
import com.hd123.m3.investment.rs.service.counterpart.RSCounterpartService;
import com.hd123.m3.investment.rs.service.res.floor.RSFloorService;
import com.hd123.m3.investment.rs.service.res.position.RSPositionService;
import com.hd123.m3.investment.rs.service.tenant.RSTenantService;
import com.hd123.m3.investment.service.biztype.BizType;
import com.hd123.m3.investment.service.biztype.BizTypeLevel;
import com.hd123.m3.investment.service.biztype.BizTypes;
import com.hd123.m3.investment.service.counterpart.Counterpart;
import com.hd123.m3.investment.service.counterpart.Counterparts;
import com.hd123.m3.investment.service.res.building.Building;
import com.hd123.m3.investment.service.res.building.BuildingService;
import com.hd123.m3.investment.service.res.building.Buildings;
import com.hd123.m3.investment.service.res.floor.Floor;
import com.hd123.m3.investment.service.res.floor.FloorService;
import com.hd123.m3.investment.service.res.floor.Floors;
import com.hd123.m3.investment.service.res.position.Position;
import com.hd123.m3.investment.service.res.position.PositionState;
import com.hd123.m3.investment.service.res.position.PositionType;
import com.hd123.m3.investment.service.res.position.Positions;
import com.hd123.m3.investment.service.tenant.tenant.Tenant;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.biz.query.converter.BeanQueryDefinitionConverter;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.server.RPageDataConverter;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author chenrizhang
 * 
 */
public class AccountCpntsServiceServlet extends AccRemoteServiceServlet implements
    AccountCpntsService {
  private static final long serialVersionUID = 300100L;

  @Override
  public RPageData<BEmployee> queryEmployees(CodeNameFilter filter, List<String> excepts)
      throws ClientBizException {
    Assert.assertArgumentNotNull(filter, "filter");
    try {
      QueryDefinition queryDef = new QueryDefinition();
      if (excepts != null && excepts.isEmpty() == false) {
        QueryCondition cond = new QueryCondition();
        cond.setOperation(UserService.CONDITION_UUID_NOT_IN);
        cond.getParameters()
            .addAll(
                excepts.size() > 1000 ? Arrays.asList(Arrays.copyOf(excepts.toArray(), 1000))
                    : excepts);
        queryDef.getConditions().add(cond);
      }
      if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
        QueryCondition cond = new QueryCondition();
        cond.setOperation(UserService.CONDITION_ID_START_WITH);
        cond.getParameters().add(filter.getCode().trim());
        queryDef.getConditions().add(cond);
      }
      if (StringUtil.isNullOrBlank(filter.getName()) == false) {
        QueryCondition cond = new QueryCondition();
        cond.setOperation(UserService.CONDITION_DISPLAY_NAME_LIKE);
        cond.getParameters().add(filter.getName().trim());
        queryDef.getConditions().add(cond);
      }

      QueryCondition cond = new QueryCondition();
      cond.setOperation(UserService.CONDITION_ONLINE_ONLY);
      queryDef.getConditions().add(cond);

      List<QueryOrder> orders = new ArrayList<QueryOrder>();
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (Order order : filter.getOrders()) {
          String orderField = null;
          if (ObjectUtil.isEquals(AccountCpntsContants.ORDER_BY_FIELD_NAME, order.getFieldName())) {
            orderField = UserService.ORDER_BY_DISPLAY_NAME;
          } else {
            orderField = UserService.ORDER_BY_LOGIN_NAME;
          }
          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      } else {
        QueryOrder order = new QueryOrder();
        order.setField(UserService.ORDER_BY_LOGIN_NAME);
        order.setDirection(QueryOrderDirection.asc);
        orders.add(order);
      }
      queryDef.setOrders(orders);
      queryDef.setPage(filter.getPage());
      queryDef.setPageSize(filter.getPageSize());

      QueryResult<User> qr = getUserService().query(queryDef, User.PART_CONTACTS);
      if (qr == null || qr.getRecords().isEmpty())
        return new RPageData<BEmployee>();
      RPageData<BEmployee> pd = new RPageData<BEmployee>();
      RPageDataUtil.copyPagingInfo(qr, pd);
      List<BEmployee> values = ConverterUtil.convert(qr.getRecords(),
          EmployeeBizConverter.getInstance());
      pd.setValues(values);
      return pd;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BEmployee getEmployeeByCode(String code) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(code)) {
        return null;
      }

      QueryDefinition queryDef = new QueryDefinition();
      queryDef.addCondition(UserService.CONDITION_ONLINE_ONLY);
      queryDef.addCondition(UserService.CONDITION_ID_EQUALS, code);
      QueryResult<User> qr = getUserService().query(queryDef, User.PART_CONTACTS);
      if (qr == null || qr.getRecords().isEmpty())
        return null;

      return EmployeeBizConverter.getInstance().convert(qr.getRecords().get(0));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public RPageData<BUCN> queryAccountUnits(CodeNameFilter filter, Boolean state)
      throws ClientBizException {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      if (state != null) {
        queryDef.addCondition(Stores.CONDITION_STATE_EQUALS, state ? BasicState.using.name()
            : BasicState.deleted.name());
      }
      // 受组织限制
      List<String> orgIds = getUserOrgService().getUserPermOrgIds(getSessionUser().getId());
      if (orgIds.isEmpty())
        return new RPageData();
      queryDef.addCondition(Stores.CONDITION_ORG_ID_IN, orgIds.toArray());

      String code = filter.getCode();
      if (StringUtil.isNullOrBlank(code) == false) {
        queryDef.addCondition(Stores.CONDITION_CODE_STARTWITH, code.trim());
      }

      String name = filter.getName();
      if (StringUtil.isNullOrBlank(name) == false) {
        queryDef.addCondition(Stores.CONDITION_NAME_LIKE, name.trim());
      }

      List<QueryOrder> orders = new ArrayList<QueryOrder>();
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (Order order : filter.getOrders()) {
          String orderField = null;
          if (AccountCpntsContants.ORDER_BY_FIELD_CODE.equals(order.getFieldName())) {
            orderField = Stores.ORDER_BY_CODE;
          } else if (AccountCpntsContants.ORDER_BY_FIELD_NAME.equals(order.getFieldName())) {
            orderField = Stores.ORDER_BY_NAME;
          }
          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      } else {
        String orderField = Stores.ORDER_BY_CODE;
        QueryOrderDirection dir = QueryOrderDirection.asc;
        orders.add(new QueryOrder(orderField, dir));
      }

      queryDef.setOrders(orders);
      queryDef.setPage(filter.getPage());
      queryDef.setPageSize(filter.getPageSize());

      QueryResult<MStore> qr = getStoreService().query(
          new FlecsQueryDefinitionConverter().convert(queryDef));
      return RPageDataConverter.convert(qr, UCNBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BUCN getAccountUnitByCode(String code, Boolean state) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(code))
        return null;

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      if (state != null) {
        queryDef.addCondition(Stores.CONDITION_STATE_EQUALS, state ? BasicState.using.name()
            : BasicState.deleted.name());
      }
      // 受组织限制
      List<String> orgIds = getUserOrgService().getUserPermOrgIds(getSessionUser().getId());
      if (orgIds.isEmpty())
        return null;
      queryDef.addCondition(Stores.CONDITION_ORG_ID_IN, orgIds.toArray());

      queryDef.addCondition(Stores.CONDITION_CODE_EQUALS, code.trim());

      queryDef.setPage(0);
      queryDef.setPageSize(1);

      QueryResult<MStore> qr = getStoreService().query(
          new FlecsQueryDefinitionConverter().convert(queryDef));
      MStore store = qr.getRecords().isEmpty() ? null : qr.getRecords().get(0);
      return store == null ? null : new BUCN(store.getUuid(), store.getCode(), store.getName());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BStoreNavigator> loadStoreNavigator() throws Exception {
    try {
      User user = getUserService().get(getSessionUser().getId());
      Organization myOrg = getOrganizationService().get(
          Organization.ROOT_PATH + user.getOrganizationPath().split(Organization.ROOT_PATH)[1]);
      // 取得所有组织
      QueryDefinition queryDef = new QueryDefinition();
      queryDef.addCondition(OrganizationService.CONDITION_ORGANIZATION_CONTAINS, myOrg.getUuid());
      queryDef.addOrder(OrganizationService.ORDER_BY_NAME);
      List<Organization> orgs = getOrganizationService().query(queryDef, Organization.PART_EXTENDS)
          .getRecords();
      List<String> permOrgIds = getUserOrgService().getUserPermOrgIds(getSessionUser().getId());
      List<MStore> stores = getUserStoreService().getUserStores(getSessionUser().getId());
      Map<String, MStore> storeMap = new HashMap<String, MStore>();
      for (MStore store : stores) {
        storeMap.put(store.getOrgId(), store);
      }
      List<BStoreNavigator> result = new ArrayList<BStoreNavigator>();
      Map<String, BStoreNavigator> nodes = new HashMap<String, BStoreNavigator>();
      for (Organization org : orgs) {
        if (permOrgIds.contains(org.getUuid()) == false)// 无授权，跳过
          continue;

        MStore store = storeMap.get(org.getUuid());
        if (store != null) {
          BStoreNavigator node = nodes.get(org.getPath());
          // 创建节点
          if (node == null) {
            node = new BStoreNavigator();
            nodes.put(org.getPath(), node);
          } else {
            result.removeAll(node.getChilds());// 父节点存在，删除结果集中的下级节点
          }
          node.setUuid(store.getUuid());
          node.setCode(store.getCode());
          node.setName(store.getName());

          BStoreNavigator parent = nodes.get(org.getUpperPath());
          if (parent == null) {
            parent = new BStoreNavigator();
            nodes.put(org.getUpperPath(), parent);
          }
          if (parent.getUuid() == null) {
            result.add(node);
          }
          parent.getChilds().add(node);
        }
      }
      return result;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @Override
  public Map<String, String> getCounterpartConfig() throws Exception {
    return getOptionService().getCounterpartType();
  }

  @Override
  public RPageData<com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart> queryCounterparts(
      CodeNameFilter filter) throws Exception {
    QueryDefinition queryDef = CounterpartQueryBuilder
        .build4query(filter, getSessionUser().getId());
    if (queryDef == null) {
      return null;
    }

    QueryResult<Counterpart> qr = getCounterPartService().query(
        new BeanQueryDefinitionConverter().convert(queryDef));
    return RPageDataConverter.convert(qr, new CounterpartBizConverter());
  }

  @Override
  public com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart getCounterpartByCode(
      String code, Map<String, Object> filter) throws Exception {
    QueryDefinition queryDef = CounterpartQueryBuilder.build4load(code, filter, getSessionUser()
        .getId());
    if (queryDef == null) {
      return null;
    }

    List<Counterpart> counterparts = getCounterPartService().query(
        new BeanQueryDefinitionConverter().convert(queryDef)).getRecords();
    if (counterparts.isEmpty()) {
      return null;
    }
    Counterpart counterpart = counterparts.get(0);
    for (Counterpart t : counterparts) {
      if (t.getState() == BasicState.using) {
        counterpart = t;
        break;
      }
    }
    return new CounterpartBizConverter().convert(counterpart);
  }

  @Override
  public RPageData<BCounterpart> queryCounterparts(CodeNameFilter filter, Boolean state)
      throws ClientBizException {
    try {
      QueryDefinition queryDef = new QueryDefinition();

      if (state != null) {
        queryDef.addCondition(Counterparts.CONDITION_STATE_EQUALS, state ? BasicState.using.name()
            : BasicState.deleted.name());
      }

      QueryCondition condition = new QueryCondition(Basices.CONDITION_PERM_GROUP_ID_IN);
      List<String> list = getUserGroups(getSessionUser().getId());
      if (list.size() > 0) {
        condition.addParameter(list.toArray());
      }
      if (!list.contains(HasPerm.DEFAULT_PERMGROUP)) {
        condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
      }
      queryDef.getConditions().add(condition);

      String code = filter.getCode();
      if (StringUtil.isNullOrBlank(code) == false) {
        queryDef.addCondition(Counterparts.CONDITION_CODE_STARTWITH, code.trim());
      }

      /*
       * String counterpartType = (String)
       * filter.get(AccountCpntsContants.CONDITION_COUNTERPARTTYPE); if
       * (StringUtil.isNullOrBlank(counterpartType) == false) {
       * queryDef.addCondition(Counterparts.CONDITION_MODULE_EQUALS,
       * counterpartType); } else { Map<String, String> map =
       * getOptionService().getCounterpartType();
       * queryDef.addCondition(Counterparts.CONDITION_MODULE_IN,
       * map.keySet().toArray()); }
       */
      if (StringUtil.isNullOrBlank(filter.getName()) == false) {
        queryDef.addCondition(Counterparts.CONDITION_NAME_LIKE, filter.getName().trim());
      }

      List<QueryOrder> orders = new ArrayList<QueryOrder>();
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (Order order : filter.getOrders()) {
          String orderField = null;
          if (AccountCpntsContants.ORDER_BY_FIELD_CODE.equals(order.getFieldName())) {
            orderField = Counterparts.ORDER_BY_CODE;
          } else if (AccountCpntsContants.ORDER_BY_FIELD_NAME.equals(order.getFieldName())) {
            orderField = Counterparts.ORDER_BY_NAME;
          }
          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      } else {
        String orderField = Counterparts.ORDER_BY_CODE;
        QueryOrderDirection dir = QueryOrderDirection.asc;
        orders.add(new QueryOrder(orderField, dir));
      }

      queryDef.setOrders(orders);
      queryDef.setPage(filter.getPage());
      queryDef.setPageSize(filter.getPageSize());

      QueryResult<Counterpart> qr = getCounterPartService().query(
          new BeanQueryDefinitionConverter().convert(queryDef));

      RPageData<BCounterpart> result = new RPageData<BCounterpart>();
      RPageDataUtil.copyPagingInfo(qr, result);
      if (qr == null || qr.getRecords().isEmpty())
        return new RPageData();
      result.setValues(ConverterUtil.convert(qr.getRecords(), BCounterpartConverter.getInstance()));

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BCounterpart getCounterpartByCode(String code, Boolean state) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(code))
        return null;

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      if (state != null) {
        queryDef.addCondition(Counterparts.CONDITION_STATE_EQUALS, state ? BasicState.using.name()
            : BasicState.deleted.name());
      }

      QueryCondition condition = new QueryCondition(Basices.CONDITION_PERM_GROUP_ID_IN);
      List<String> list = getUserGroups(getSessionUser().getId());
      if (list.size() > 0) {
        condition.addParameter(list.toArray());
      }
      if (!list.contains(HasPerm.DEFAULT_PERMGROUP)) {
        condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
      }
      queryDef.getConditions().add(condition);

      queryDef.addCondition(Counterparts.CONDITION_CODE_EQUALS, code.trim());
      queryDef.setPage(0);
      queryDef.setPageSize(1);

      QueryResult<Counterpart> qr = getCounterPartService().query(
          new BeanQueryDefinitionConverter().convert(queryDef));
      Counterpart counterpart = qr.getRecords().isEmpty() ? null : qr.getRecords().get(0);
      return counterpart == null ? null : BCounterpartConverter.getInstance().convert(counterpart);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BSubject> querySubject(SubjectFilter filter) throws ClientBizException {
    try {

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      if (filter.getState() != null) {
        queryDef.addCondition(Subjects.CONDITION_STATE_EQUALS, filter.getState());
      }

      if (filter.getSubjectType() != null) {
        queryDef.addCondition(Subjects.CONDITION_SUBJECT_TYPE_EQUALS, filter.getSubjectType());
      }

      if (filter.getDirectionType() != null) {
        queryDef.addCondition(Subjects.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());
      }

      if (filter.getUuids() != null && filter.getUuids().isEmpty() == false) {
        queryDef.addCondition(Subjects.CONDITION_UUID_IN, filter.getUuids().toArray());
      }

      if (StringUtil.isNullOrBlank(filter.getUsageType()) == false) {
        List<BSubjectUsage> usages = SubjectUsageUtils.getSubjectUsage(UsageType.valueOf(filter
            .getUsageType()));
        Set<String> usageCodes = new HashSet<String>();
        for (BSubjectUsage usage : usages) {
          usageCodes.add(usage.getCode());
        }
        if (usageCodes.isEmpty() == false) {
          queryDef.addCondition(Subjects.CONDITION_USAGE_IN, usageCodes.toArray());
        }
      }

      if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
        queryDef.addCondition(Subjects.CONDITION_CODE_STARTWITH, filter.getCode());
      }

      if (StringUtil.isNullOrBlank(filter.getName()) == false) {
        queryDef.addCondition(Subjects.CONDITION_NAME_LIKE, filter.getName());
      }

      List<QueryOrder> orders = new ArrayList<QueryOrder>();
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (Order order : filter.getOrders()) {
          String orderField = null;
          if (ObjectUtil.isEquals(AccountCpntsContants.ORDER_BY_FIELD_NAME, order.getFieldName())) {
            orderField = Subjects.ORDER_BY_NAME;
          } else {
            orderField = Subjects.ORDER_BY_CODE;
          }
          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      } else {
        String orderField = Subjects.ORDER_BY_CODE;
        QueryOrderDirection dir = QueryOrderDirection.asc;
        orders.add(new QueryOrder(orderField, dir));
      }

      queryDef.setOrders(orders);
      queryDef.setPage(filter.getPage());
      queryDef.setPageSize(filter.getPageSize());

      QueryResult<Subject> qr = getSubjectService().query(queryDef, Subject.PART_STORE_TAXRATE);
      if (qr == null || qr.getRecords().isEmpty())
        return new RPageData<BSubject>();

      RPageData<BSubject> result = new RPageData<BSubject>();
      RPageDataUtil.copyPagingInfo(qr, result);
      result.setValues(ConverterUtil.convert(qr.getRecords(), SubjectBizConverter.getInstance()));
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BSubject getSubjectByCode(SubjectFilter filter) throws ClientBizException {
    try {
      Subject subject = getSubjectService().getByCode(filter.getCode(), Subject.PART_WHOLE);

      if (subject == null || subject.isEnabled() == false)
        return null;

      String subjectType = filter.getSubjectType();
      if (subjectType != null && subjectType.equals(subject.getType().name()) == false)
        return null;

      Integer direction = filter.getDirectionType();
      if (direction != null && subject.getDirection() != direction)
        return null;

      String usageType = filter.getUsageType();
      if (StringUtil.isNullOrBlank(usageType) == false) {
        List<BSubjectUsage> usages = SubjectUsageUtils.getSubjectUsage(UsageType.valueOf(filter
            .getUsageType()));
        Set<String> usageCodes = new HashSet<String>();
        for (BSubjectUsage usage : usages) {
          usageCodes.add(usage.getCode());
        }

        boolean find = false;
        for (String usageCode : subject.getUsages()) {
          if (usageCodes.contains(usageCode)) {
            find = true;
            break;
          }
        }

        if (find == false) {
          return null;
        }
      }

      return SubjectBizConverter.getInstance().convert(subject);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BContract> queryContracts(ContractFilter filter) throws ClientBizException {
    try {
      RPageData<BContract> result = new RPageData<BContract>();
      boolean requiresAccountUnitAndCountpart = false;
      if (filter.getParams().get(Contracts.CONDITION_ACCOUNT_COUNTPART_REQUIRE) != null) {
        requiresAccountUnitAndCountpart = (Boolean) filter.getParams().get(
            Contracts.CONDITION_ACCOUNT_COUNTPART_REQUIRE);
      }

      String accountUnitUuid = filter.getAccountUnitUuid();
      String counterpartUuid = filter.getCounterpartUuid();
      if (requiresAccountUnitAndCountpart && (accountUnitUuid == null || counterpartUuid == null)) {
        return result;
      }

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      // 启用合同授权
      boolean useContractPermT = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      boolean useContractPermP = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR);
      List<String> userGroups = new ArrayList<String>();
      if (useContractPermT || useContractPermP) {
        userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups == null) {
          userGroups = new ArrayList<String>();
        }
        if (userGroups.contains(HasPerm.DEFAULT_PERMGROUP) == false) {
          userGroups.add(HasPerm.DEFAULT_PERMGROUP);
        }
      }

      // 启用合同授权
      if (useContractPermT || useContractPermP) {
        queryDef.addCondition(Contracts.CONDITION_CUSTOM_PERM_GROUP_ID_IN, userGroups.toArray());
      }

      // 受项目限制
      List<String> list = getUserStores(getSessionUser().getId());
      if (list.isEmpty()) {
        return new RPageData<BContract>();
      }
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_IN, list.toArray());
      queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING);

      queryDef = ContractQueryBuilder.buildDefinition(queryDef, filter);

      QueryResult<Contract> qr = getContractService().query(queryDef, Contracts.FETCH_POSITIONS);
      if (qr == null || qr.getRecords().isEmpty()) {
        return new RPageData<BContract>();
      }

      RPageDataUtil.copyPagingInfo(qr, result);

      List<BContract> contracts = ConverterUtil.convert(qr.getRecords(),
          ContractBizConverter.getInstance());
      result.setValues(contracts);
      return result;

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BContract getContractByNumber(ContractFilter filter) throws ClientBizException {
    try {
      if (filter == null) {
        return null;
      }

      Contract contract = getContractService().getByNumber(filter.getBillNumber());
      if (contract == null) {
        return null;
      } else if (!ContractState.using.equals(contract.getState())) {
        return null;
      } else if (!StringUtil.isNullOrBlank(filter.getCounterpartUuid())
          && contract.getCounterpart().getUuid().equals(filter.getCounterpartUuid()) == false) {
        return null;
      } else if (!StringUtil.isNullOrBlank(filter.getAccountUnitUuid())
          && contract.getBusinessUnit().getUuid().equals(filter.getAccountUnitUuid()) == false) {
        return null;
      } else if (filter.isRequiresAccountUnitAndCountpart()
          && (StringUtil.isNullOrBlank(filter.getAccountUnitUuid()) || StringUtil
              .isNullOrBlank(filter.getCounterpartUuid()))) {
        return null;
      } else if (filter.isTenant()
          && !com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart.COUNTERPART_TYPE_TENANT
              .equals(contract.getCounterpartType())) {
        return null;
      }
      if (filter.isUnlocked()) {
        if (Contracts.DEFAULT_LOCKER.equals(contract.getLocker()) == false) {
          return null;
        }
      }

      // 启用合同授权
      boolean useContractPerm = Counterparts.MODULE_PROPRIETOR
          .equals(contract.getCounterpartType()) ? permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)
          : permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      List<String> userGroups = new ArrayList<String>();
      if (useContractPerm) {
        userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups.contains(HasPerm.DEFAULT_PERMGROUP) == false) {
          userGroups.add(HasPerm.DEFAULT_PERMGROUP);
        }
      }

      // 启用合同授权
      if (useContractPerm) {
        if (userGroups.contains(contract.getPermGroupId()) == false)
          return null;
      }

      // 启用项目授权
      List<String> stores = getUserStores(getSessionUser().getId());
      if (stores.contains(contract.getBusinessUnit().getUuid()) == false) {
        return null;
      }

      return ContractBizConverter.getInstance().convert(contract);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BMonthSettle> querySettles(SettleNoFilter filter) throws ClientBizException {
    try {

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      String number = filter.getNumber();
      if (!StringUtil.isNullOrBlank(number)) {
        queryDef.addCondition(MonthSettles.CONDITION_NUMBER_STARTWITH, number);
      }
      BDateRange beginTimeDateRange = filter.getBeginTimeDateRange();
      if (beginTimeDateRange != null) {
        queryDef.addCondition(MonthSettles.CONDITION_BEGIN_TIME_BETWEEN,
            beginTimeDateRange.getBeginDate(), beginTimeDateRange.getEndDate());
      }
      BDateRange endTimeDateRange = filter.getEndTimeDateRange();
      if (endTimeDateRange != null) {
        queryDef.addCondition(
            MonthSettles.CONDITION_END_TIME_BETWEEN,
            endTimeDateRange.getBeginDate(),
            endTimeDateRange.getEndDate() == null ? null : DateUtils.addDays(
                endTimeDateRange.getEndDate(), 1));
      }

      List<QueryOrder> orders = new ArrayList<QueryOrder>();
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (Order order : filter.getOrders()) {
          String orderField = null;
          if (ObjectUtil.isEquals(BMonthSettle.FIELD_BEGINTIME, order.getFieldName())) {
            orderField = MonthSettles.ORDER_BY_BEGINTIME;
          } else if (ObjectUtil.isEquals(BMonthSettle.FIELD_ENDTIME, order.getFieldName())) {
            orderField = MonthSettles.ORDER_BY_ENDTIME;
          } else {
            orderField = MonthSettles.ORDER_BY_NUMBER;
          }
          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      } else {
        String orderField = MonthSettles.ORDER_BY_NUMBER;
        QueryOrderDirection dir = QueryOrderDirection.asc;
        orders.add(new QueryOrder(orderField, dir));
      }

      queryDef.setOrders(orders);
      queryDef.setPage(filter.getPage());
      queryDef.setPageSize(filter.getPageSize());

      QueryResult<MonthSettle> qr = getMonthSettleService().query(queryDef);
      if (qr == null || qr.getRecords().isEmpty())
        return new RPageData<BMonthSettle>();

      RPageData<BMonthSettle> result = new RPageData<BMonthSettle>();
      RPageDataUtil.copyPagingInfo(qr, result);

      result
          .setValues(ConverterUtil.convert(qr.getRecords(), MonthSettleBizConverter.getInstance()));
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String getSettleByNumber(String number) throws ClientBizException {
    try {

      MonthSettle monthSettle = getMonthSettleService().getByNumber(number);
      if (monthSettle == null)
        return null;
      return monthSettle.getNumber();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BUCN> queryFloor(CodeNameFilter filter) throws ClientBizException {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.setPage(filter.getPage());
      def.setPageSize(filter.getPageSize());

      def.getConditions().addAll(getFloorConditions());

      // 代码
      if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
        def.addCondition(Floors.CONDITION_CODE_START_WITH, filter.getCode().trim());
      }
      // 名称
      if (StringUtil.isNullOrBlank(filter.getName()) == false) {
        def.addCondition(Floors.CONDITION_NAME_LIKE, filter.getName().trim());
      }

      List<QueryOrder> orders = new ArrayList<QueryOrder>();
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (Order order : filter.getOrders()) {
          String orderField = null;
          if (ObjectUtil.isEquals(AccountCpntsContants.ORDER_BY_FIELD_CODE, order.getFieldName())) {
            orderField = Floors.ORDER_BY_CODE;
          } else {
            orderField = Floors.ORDER_BY_NAME;
          }
          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      } else {
        QueryOrder order = new QueryOrder();
        order.setField(Floors.ORDER_BY_CODE);
        order.setDirection(QueryOrderDirection.asc);
        orders.add(order);
      }
      def.setOrders(orders);

      QueryResult<Floor> qr = getFloorService().query(
          new FlecsQueryDefinitionConverter().convert(def));
      return RPageDataConverter.convert(qr, UCNBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BUCN getFloorByCode(String code) throws ClientBizException {
    if (StringUtil.isNullOrBlank(code))
      return null;

    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.setPage(0);
      def.setPageSize(1);

      def.getConditions().addAll(getFloorConditions());

      def.addCondition(Floors.CONDITION_CODE_EQUALS, code.trim());
      QueryResult<Floor> qr = getFloorService().query(
          new FlecsQueryDefinitionConverter().convert(def));
      if (qr.getRecords().isEmpty())
        return null;
      return UCNBizConverter.getInstance().convert(qr.getRecords().get(0));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public RPageData<TypeBUCN> queryPosition(String type, CodeNameFilter filter)
      throws ClientBizException {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.setPage(filter.getPage());
      def.setPageSize(filter.getPageSize());

      def.getConditions().addAll(getPositionConditions());

      if (StringUtil.isNullOrBlank(type) == false)
        def.addCondition(Positions.CONDITION_POSITIONTYPE_EQUALS, type);
      // 代码
      if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
        def.addCondition(Positions.CONDITION_CODE_START_WITH, filter.getCode().trim());
      }
      // 名称
      if (StringUtil.isNullOrBlank(filter.getName()) == false) {
        def.addCondition(Positions.CONDITION_NAME_LIKE, filter.getName().trim());
      }

      List<QueryOrder> orders = new ArrayList<QueryOrder>();
      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (Order order : filter.getOrders()) {
          String orderField = null;
          if (ObjectUtil.isEquals(AccountCpntsContants.ORDER_BY_FIELD_CODE, order.getFieldName())) {
            orderField = Positions.ORDER_BY_CODE;
          } else {
            orderField = Positions.ORDER_BY_NAME;
          }
          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      } else {
        QueryOrder order = new QueryOrder();
        order.setField(Positions.ORDER_BY_CODE);
        order.setDirection(QueryOrderDirection.asc);
        orders.add(order);
      }
      def.setOrders(orders);

      QueryResult<Position> qr = getPositionService().query(
          new FlecsQueryDefinitionConverter().convert(def));
      return RPageDataConverter.convert(qr, new PositionBizConverter());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public TypeBUCN getPositionByCode(String type, String code) throws ClientBizException {
    if (StringUtil.isNullOrBlank(code))
      return null;

    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.setPage(0);
      def.setPageSize(1);

      def.getConditions().addAll(getPositionConditions());

      if (StringUtil.isNullOrBlank(type) == false)
        def.addCondition(Positions.CONDITION_POSITIONTYPE_EQUALS, type);
      // 代码
      def.addCondition(Positions.CONDITION_CODE_EQUALS, code.trim());

      QueryResult<Position> qr = getPositionService().query(
          new FlecsQueryDefinitionConverter().convert(def));
      if (qr.getRecords().isEmpty())
        return null;
      return new PositionBizConverter().convert(qr.getRecords().get(0));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public Map<String, List<String>> getPositionSubTyoes() throws ClientBizException {
    Map<String, List<String>> result = new HashMap<String, List<String>>();
    result.put(PositionType.shoppe.name(),
        getBasicConfigService().getByCategory(Positions.KEY_SHOPPR_SUBTYPE));
    result.put(PositionType.booth.name(),
        getBasicConfigService().getByCategory(Positions.KEY_BOOTH_SUBTYPE));
    result.put(PositionType.adPlace.name(),
        getBasicConfigService().getByCategory(Positions.KEY_ADPLACE_SUBTYPE));
    return result;
  }

  @Override
  public RPageData<SPosition> queryPositions(PositionFilter filter) throws ClientBizException {
    FlecsQueryDefinition queryDef = PositionQueryBuilder.build4query(filter, getSessionUser()
        .getId());
    if (queryDef == null)
      return null;
    try {
      QueryResult<Position> qr = getPositionService().query(
          new FlecsQueryDefinitionConverter().convert(queryDef));
      return RPageDataConverter.convert(qr, PositionSimpleConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public SPosition getPositionByCode(PositionFilter filter) throws ClientBizException {
    FlecsQueryDefinition queryDef = PositionQueryBuilder.build4load(filter, getSessionUser()
        .getId());
    if (queryDef == null)
      return null;

    try {
      List<Position> positions = getPositionService().query(
          new FlecsQueryDefinitionConverter().convert(queryDef)).getRecords();
      if (positions.isEmpty())
        return null;

      Position position = positions.get(0);
      for (Position p : positions) {
        if (position != p && p.getState() == PositionState.using) {
          if (position.getState() == PositionState.using) {
            throw new Exception("意外的找到了多个结果。");
          }
          position = p;
          break;
        }
      }
      return PositionSimpleConverter.getInstance().convert(position);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<TypeBUCN> queryBuildings(String type, CodeNameFilter filter)
      throws ClientBizException {
    try {
      return new RPageData<TypeBUCN>();

    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public TypeBUCN getBuildingByCode(String type, String code) throws ClientBizException {
    try {
      return null;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public RPageData<BUCN> queryCategories(CodeNameFilter filter) throws ClientBizException {
    if (filter == null) {
      return null;
    }

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
      queryDef.addCondition(BizTypes.CONDITION_CODE_START_WITH, filter.getCode());
    }

    if (StringUtil.isNullOrBlank(filter.getName()) == false) {
      queryDef.addCondition(BizTypes.CONDITION_NAME_LIKE, filter.getName());
    }

    queryDef.addCondition(BizTypes.CONDITION_LEVEL_EQUALS, BizTypeLevel.one.name());

    String orderField = BizTypes.ORDER_BY_CODE;
    QueryOrderDirection dir = QueryOrderDirection.asc;
    if (filter.getOrders().isEmpty() == false) {
      dir = OrderDir.asc.equals(filter.getOrders().get(0).getDir()) ? QueryOrderDirection.asc
          : QueryOrderDirection.desc;
      if (ObjectUtil.isEquals(AccountCpntsContants.ORDER_BY_FIELD_NAME, filter.getOrders().get(0)
          .getFieldName())) {
        orderField = BizTypes.ORDER_BY_NAME;
      }
    }

    queryDef.addOrder(orderField, dir);
    queryDef.setPage(filter.getPage());
    queryDef.setPageSize(filter.getPageSize());

    try {
      QueryResult<BizType> qr = getbizBizTypeService().query(
          new FlecsQueryDefinitionConverter().convert(queryDef));
      return RPageDataConverter.convert(qr, new BizTypeBizConverter());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BUCN getCategoryByCode(String code) throws ClientBizException {

    if (StringUtil.isNullOrBlank(code)) {
      return null;
    }

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(BizTypes.CONDITION_CODE_EQUALS, code);

    queryDef.addCondition(BizTypes.CONDITION_LEVEL_EQUALS, BizTypeLevel.one.name());

    try {
      QueryResult<BizType> qr = getbizBizTypeService().query(
          new FlecsQueryDefinitionConverter().convert(queryDef));
      if (qr.getRecordCount() == 0) {
        return null;
      }
      return new BizTypeBizConverter().convert(qr.getRecords().get(0));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public RPageData<BInvoiceStock> queryInvoiceStocks(CodeNameFilter filter)
      throws ClientBizException {
    FlecsQueryDefinition queryDef = InvoiceStockQueryBuilder.build4query(filter, getSessionUser()
        .getId());
    if (queryDef == null) {
      return null;
    }

    QueryResult<InvoiceStock> qr = getInvoiceStockService().query(queryDef);
    return RPageDataConverter.convert(qr, new InvoiceStockBizConverter());
  }

  @Override
  public BInvoiceStock getInvoiceStockByNumber(String code, Map<String, Object> filter)
      throws ClientBizException {
    FlecsQueryDefinition queryDef = InvoiceStockQueryBuilder.build4load(code, filter,
        getSessionUser().getId());
    if (queryDef == null) {
      return null;
    }

    List<InvoiceStock> invoices = getInvoiceStockService().query(queryDef).getRecords();
    return invoices.isEmpty() ? null : new InvoiceStockBizConverter().convert(invoices.get(0));
  }

  // ------------------------------------------------------------------------------
  private List<QueryCondition> getFloorConditions() {
    List<QueryCondition> conditions = getPermConditions(Floors.OBJECT_NAME);
    // 没有开启楼层授权组但开启了楼宇授权组
    if (!permEnabled(Floors.OBJECT_NAME) && permEnabled(Buildings.OBJECT_NAME)) {
      List<QueryCondition> buildingConditions = getPermConditions(Buildings.OBJECT_NAME);
      // 根据授权组的uuid集合从数据库中获取对应的楼宇uuid集合
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.getConditions().addAll(buildingConditions);
      List<Building> buildings = getBuildingService().query(queryDef).getRecords();
      List<String> list = new ArrayList<String>();
      for (Building building : buildings) {
        list.add(building.getUuid());
      }
      QueryCondition c = new QueryCondition();
      c.setOperation(Floors.CONDITION_BUILDING_IN);
      if (list != null && list.size() > 0) {
        c.addParameter(list.toArray());
      }
      conditions.add(c);
    }
    return conditions;
  }

  private List<QueryCondition> getPositionConditions() {
    List<QueryCondition> conditions = getPermConditions(Positions.OBJECT_NAME);
    // 如果没有开启自身授权组
    if (!permEnabled(Positions.OBJECT_NAME)) {
      // 查看是否开启楼层授权组
      if (permEnabled(Floors.OBJECT_NAME)) {
        QueryCondition qc = new QueryCondition();
        qc.setOperation(Positions.CONDITION_FLOOR_IN);
        // 根据楼层授权组的uuid集合从数据库中获取对应的铺位uuid集合
        FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
        List<QueryCondition> floorConditions = getPermConditions(Floors.OBJECT_NAME);
        queryDef.getConditions().addAll(floorConditions);
        List<Floor> floors = getFloorService2().query(queryDef).getRecords();
        List<String> list = new ArrayList<String>();
        for (Floor floor : floors) {
          list.add(floor.getUuid());
        }
        if (list != null && list.size() > 0) {
          qc.addParameter(list.toArray());
        }
        conditions.add(qc);
      } else {// 没有开启楼层授权组
        // 查看是否开启楼宇授权组
        if (permEnabled(Buildings.OBJECT_NAME)) {
          QueryCondition qc = new QueryCondition();
          qc.setOperation(Positions.CONDITION_BUILDING_IN);
          // 根据楼层授权组的uuid集合从数据库中获取对应的铺位uuid集合
          FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
          List<QueryCondition> buildingList = getPermConditions(Buildings.OBJECT_NAME);
          queryDef.getConditions().addAll(buildingList);
          List<Building> buildings = getBuildingService().query(queryDef).getRecords();
          List<String> list = new ArrayList<String>();
          for (Building building : buildings) {
            list.add(building.getUuid());
          }
          if (list != null && list.size() > 0) {
            qc.addParameter(list.toArray());
          }
          conditions.add(qc);
        }
      }
    }
    return conditions;
  }

  @Override
  public RPageData<BUCN> queryStores(CodeNameFilter filter) throws Exception {
    FlecsQueryDefinition queryDef = StoreQueryBuilder.build4query(filter, getSessionUser().getId());
    if (queryDef == null) {
      return null;
    }

    QueryResult<MStore> qr = getStoreService().query(
        new FlecsQueryDefinitionConverter().convert(queryDef));
    return RPageDataConverter.convert(qr, UCNBizConverter.getInstance());
  }

  @Override
  public RPageData<BUCN> queryTenants(CodeNameFilter filter) throws Exception {
    try {
      FlecsQueryDefinition queryDef = TenantQueryBuilder.build4query(filter, getSessionUser()
          .getId());
      if (queryDef == null) {
        return null;
      }

      QueryResult<Tenant> qr = getTenantService().query(
          new FlecsQueryDefinitionConverter().convert(queryDef));
      return RPageDataConverter.convert(qr, UCNBizConverter.getInstance());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @Override
  public BUCN getStoreByCode(String code, Map<String, Object> filter) throws Exception {
    FlecsQueryDefinition queryDef = StoreQueryBuilder.build4load(code, filter, getSessionUser()
        .getId());
    if (queryDef == null) {
      return null;
    }

    List<MStore> stores = getStoreService().query(
        new FlecsQueryDefinitionConverter().convert(queryDef)).getRecords();
    if (stores.isEmpty()) {
      return null;
    }
    try {
      if (stores.size() == 1) {
        return UCNBizConverter.getInstance().convert(stores.get(0));
      }
      List<MStore> usings = new ArrayList<MStore>();
      for (MStore store : stores) {
        if (BasicState.using.name().equals(store.getState().name())) {
          usings.add(store);
        }
      }

      if (usings.size() == 1) {
        return UCNBizConverter.getInstance().convert(usings.get(0));
      } else {
        throw new M3ServiceException("意外的找到了多个结果。");
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  @Override
  public BUCN getTenantByCode(String code, Map<String, Object> filter) throws Exception {
    FlecsQueryDefinition queryDef = TenantQueryBuilder.build4load(code, filter, getSessionUser()
        .getId());
    if (queryDef == null) {
      return null;
    }

    List<Tenant> tenants = getTenantService().query(
        new FlecsQueryDefinitionConverter().convert(queryDef)).getRecords();
    if (tenants.isEmpty()) {
      return null;
    }
    Tenant tenant = tenants.get(0);
    for (Tenant t : tenants) {
      if (t.getState() == BasicState.using) {
        tenant = t;
        break;
      }
    }
    return UCNBizConverter.getInstance().convert(tenant);
  }

  // ------------------------------------------------------------------------------

  private RSFloorService getFloorService() {
    return getAppCtx().getBean(RSFloorService.class);
  }

  private RSStoreService getStoreService() {
    return getAppCtx().getBean(RSStoreService.class);
  }

  private RSPositionService getPositionService() {
    return getAppCtx().getBean(RSPositionService.class);
  }

  private BasicConfigService getBasicConfigService() {
    return getAppCtx().getBean(BasicConfigService.DEFAULT_CONTEXT_ID, BasicConfigService.class);
  }

  private SubjectService getSubjectService() {
    return getAppCtx().getBean(SubjectService.class);
  }

  private MonthSettleService getMonthSettleService() {
    return getAppCtx().getBean(MonthSettleService.class);
  }

  private RSCounterpartService getCounterPartService() {
    return getAppCtx().getBean(RSCounterpartService.class);
  }

  private InvoiceStockService getInvoiceStockService() {
    return getAppCtx().getBean(InvoiceStockService.class);
  }

  private RSBizTypeService getbizBizTypeService() {
    return getAppCtx().getBean(RSBizTypeService.class);
  }

  @Override
  public String getObjectName() {
    return null;
  }

  private static M3UserOrgService getUserOrgService() {
    return M3ServiceFactory.getBean(M3UserOrgService.DEFAULT_CONTEXT_ID, M3UserOrgService.class);
  }

  private BuildingService getBuildingService() {
    return M3ServiceFactory.getBean(BuildingService.DEFAULT_CONTEXT_ID, BuildingService.class);
  }

  private FloorService getFloorService2() {
    return M3ServiceFactory.getBean(FloorService.DEFAULT_CONTEXT_ID, FloorService.class);
  }

  private RSTenantService getTenantService() {
    return M3ServiceFactory.getService(RSTenantService.class);
  }

}
