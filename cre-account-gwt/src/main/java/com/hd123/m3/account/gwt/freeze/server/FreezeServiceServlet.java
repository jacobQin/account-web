/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FreezeServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-4 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.server;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.acc.server.AccountBizConverter;
import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.client.rpc.FreezeService;
import com.hd123.m3.account.gwt.freeze.client.ui.filter.AccountFilter;
import com.hd123.m3.account.gwt.freeze.intf.client.perm.FreezePermDef;
import com.hd123.m3.account.gwt.util.server.BillTypeUtils;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.freeze.Freeze;
import com.hd123.m3.account.service.freeze.Freezes;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 客户端服务接口实现
 * 
 * @author zhuhairui
 * 
 */
public class FreezeServiceServlet extends AccRemoteServiceServlet implements FreezeService {
  private static final long serialVersionUID = -3289575410086617278L;

  @Override
  public String getObjectName() {
    return Freezes.OBJECT_NAME;
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    if (isHost()) {
      Set<BPermission> permissions = new HashSet<BPermission>();
      permissions.add(FreezePermDef.READ);
      permissions.add(FreezePermDef.CREATE);
      permissions.add(FreezePermDef.FREEZE);
      permissions.add(FreezePermDef.UNFREEZE);
      return permissions;
    }
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        FreezePermDef.RESOURCE_FREEZE_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    try {
      Map<String, String> nameCaption = BillTypeUtils.getAllNameCaption();
      nameCaption.remove("invoiceExchange");
      moduleContext.put(KEY_BILLTYPES, CollectionUtil.toString(nameCaption));
    } catch (Exception e) {
      Logger.getLogger(FreezeServiceServlet.class).error("buildModuleContext error", e);
    }
  }

  @Override
  public RPageData<BFreeze> query(List<Condition> conditions, PageSort pageSort) throws Exception {
    try {

      FlecsQueryDefinition def = buildQueryDefinition(conditions, pageSort);
      QueryResult<Freeze> queryResult = getFreezeService().query(def);
      RPageData<BFreeze> rp = new RPageData<BFreeze>();
      RPageDataUtil.copyPagingInfo(queryResult, rp);
      rp.setValues(ConverterUtil.convert(queryResult.getRecords(), FreezeBizConverter.getInstance()));
      // 更新导航数据
      updateNaviEntities(getObjectName(), rp.getValues());
      return rp;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BFreeze load(String uuid) throws Exception {
    try {

      Freeze source = getFreezeService().get(uuid, Freeze.PART_WHOLE);
      if (source == null)
        return null;

      BFreeze result = FreezeBizConverter.getInstance().convert(source);
      validEntityPerm(result);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BFreeze loadByNumber(String billNumber) throws Exception {
    try {

      Freeze source = getFreezeService().getByNumber(billNumber, Freeze.PART_WHOLE);
      if (source == null)
        return null;

      BFreeze result = FreezeBizConverter.getInstance().convert(source);
      validEntityPerm(result);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void unfreeze(String uuid, long version, String unfreezeReason) throws Exception {
    try {

      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(Freezes.PROPERTY_MESSAGE, unfreezeReason);
      getFreezeService().unfreeze(uuid, version, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BFreeze create() throws Exception {
    try {
      Freeze freeze = getFreezeService().create();
      return FreezeBizConverter.getInstance().convert(freeze);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BFreeze freeze(List<String> accountUuids, BFreeze freeze, String freezeReason)
      throws Exception {
    try {

      if (accountUuids.isEmpty())
        return null;

      String permGroupId = freeze.getPermGroupId();
      String permGroupTitle = freeze.getPermGroupTitle();
      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(Freezes.PROPERTY_MESSAGE, freezeReason);
      String uuid = getFreezeService().freeze(accountUuids, permGroupId, permGroupTitle, operCtx);
      return load(uuid);
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public RPageData<BAccount> queryAccount(AccountFilter filter, List<String> accUuids)
      throws Exception {
    try {
      QueryDefinition definition = new QueryDefinition();

      definition.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
      definition.addCondition(Accounts.CONDITION_ACTIVE);

      if (!StringUtil.isNullOrBlank(filter.getAccountUnitUuid())) {
        definition.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS,
            filter.getAccountUnitUuid());
      }

      if (!StringUtil.isNullOrBlank(filter.getCounterpartUuid())) {
        definition.addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS,
            filter.getCounterpartUuid());
      }

      if (!StringUtil.isNullOrBlank(filter.getStatementNum())) {
        definition.addCondition(Accounts.CONDITION_STATEMENT_BILLNUMBER_STARTWITH,
            filter.getStatementNum());
      }

      if (!StringUtil.isNullOrBlank(filter.getCounterpartType())) {
        definition.addCondition(Accounts.CONDITION_COUNTERPART_TYPE_EQUALS,
            filter.getCounterpartType());
      }

      if (!StringUtil.isNullOrBlank(filter.getContractNum())) {
        definition.addCondition(Accounts.CONDITION_CONTRACT_NUMBER_STARTWITH,
            filter.getContractNum());
      }

      if (!StringUtil.isNullOrBlank(filter.getSubjectUuid())) {
        definition.addCondition(Accounts.CONDITION_SUBJECT_UUID_EQUALS, filter.getSubjectUuid());
      }

      if (!StringUtil.isNullOrBlank(filter.getInvoiceBillNum())) {
        definition.addCondition(Accounts.CONDITION_INVOICE_BILLNUMBER_STARTWITH,
            filter.getInvoiceBillNum());
      }

      if (!StringUtil.isNullOrBlank(filter.getInvoiceCode())) {
        definition.addCondition(Accounts.CONDITION_INVOICE_CODE_LIKE, filter.getInvoiceCode());
      }

      if (!StringUtil.isNullOrBlank(filter.getInvoiceNumber())) {
        definition.addCondition(Accounts.CONDITION_INVOICE_NUMBER_LIKE, filter.getInvoiceNumber());
      }

      if (!StringUtil.isNullOrBlank(filter.getSourceBillType())) {
        definition.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS,
            filter.getSourceBillType());
      }

      if (!StringUtil.isNullOrBlank(filter.getSourceBillNumber())) {
        definition.addCondition(Accounts.CONDITION_SOURCEBILL_NUMBER_EQUALS,
            filter.getSourceBillNumber());
      }

      if (accUuids.isEmpty() == false) {
        definition.addCondition(Accounts.CONDITION_UUID_NOTIN, accUuids.toArray());
      }

      String orderField = Accounts.ORDER_BY_SUBJECT;
      QueryOrderDirection dir = QueryOrderDirection.asc;
      List<com.hd123.rumba.gwt.base.client.QueryFilter.Order> orders = filter.getOrders();
      if (orders != null && orders.isEmpty() == false) {
        for (com.hd123.rumba.gwt.base.client.QueryFilter.Order order : orders) {
          if (Accounts.ORDER_BY_SUBJECT.equals(order.getFieldName())) {
            orderField = Accounts.ORDER_BY_SUBJECT;
          } else if (Accounts.ORDER_BY_SOURCEBILLTYPE.equals(order.getFieldName())) {
            orderField = Accounts.ORDER_BY_SOURCEBILLTYPE;
          } else if (Accounts.ORDER_BY_SOURCEBILLNUMBER.equals(order.getFieldName())) {
            orderField = Accounts.ORDER_BY_SOURCEBILLNUMBER;
          } else if (Accounts.ORDER_BY_CONTRACT.equals(order.getFieldName())) {
            orderField = Accounts.ORDER_BY_CONTRACT;
          } else if (Accounts.ORDER_BY_COUNTERPART.equals(order.getFieldName())) {
            orderField = Accounts.ORDER_BY_COUNTERPART;
          } else if (Accounts.ORDER_BY_TOTAL.equals(order.getFieldName())) {
            orderField = Accounts.ORDER_BY_TOTAL;
          }
          dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
        }
      }

      definition.getOrders().add(new QueryOrder(orderField, dir));
      definition.setPage(filter.getPage());
      definition.setPageSize(filter.getPageSize());

      QueryResult<Account> queryResult = getAccountService().query(definition);
      if (queryResult.getRecords().isEmpty())
        return null;

      RPageData<BAccount> rp = new RPageData<BAccount>();
      RPageDataUtil.copyPagingInfo(queryResult, rp);
      rp.setValues(ConverterUtil.convert(queryResult.getRecords(),
          AccountBizConverter.getInstance()));
      return rp;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions, PageSort pageSort)
      throws ClientBizException {
    FlecsQueryDefinition quertDef = buildQueryDefinition(conditions, pageSort.getSortOrders());
    quertDef.setPage(pageSort.getPage());
    quertDef.setPageSize(pageSort.getPageSize());
    return quertDef;
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions,
      List<Order> sortOrders) throws ClientBizException {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.getConditions().addAll(getPermConditions());
    queryDef.getFlecsConditions().addAll(
        FreezeQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(FreezeQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private com.hd123.m3.account.service.freeze.FreezeService getFreezeService() {
    return M3ServiceFactory.getService(com.hd123.m3.account.service.freeze.FreezeService.class);
  }

  private AccountService getAccountService() {
    return M3ServiceFactory.getService(AccountService.class);
  }

}
