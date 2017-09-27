/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hd123.m3.account.gwt.adv.client.biz.BAdvance;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceData;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceLog;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceFilter;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceLogFilter;
import com.hd123.m3.account.gwt.adv.client.rpc.AdvanceService;
import com.hd123.m3.account.gwt.adv.intf.client.AdvanceUrlParams;
import com.hd123.m3.account.gwt.adv.intf.client.perm.AdvancePermDef;
import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.perm.PayDepositPermDef;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.perm.RecDepositPermDef;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.perm.PayDepositMovePermDef;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.perm.RecDepositMovePermDef;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.perm.PayDepositRepaymentPermDef;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.perm.RecDepositRepaymentPermDef;
import com.hd123.m3.account.gwt.util.server.BillTypeUtils;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceLog;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.commons.biz.BasicState;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.rs.service.counterpart.RSCounterpartService;
import com.hd123.m3.investment.service.counterpart.Counterpart;
import com.hd123.m3.investment.service.counterpart.Counterparts;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.biz.query.converter.BeanQueryDefinitionConverter;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author zhuhairui
 * 
 */
public class AdvanceServiceServlet extends AccRemoteServiceServlet implements AdvanceService {
  private static final long serialVersionUID = -2373900497038520161L;

  @Override
  public String getObjectName() {
    return Advance.class.getSimpleName();
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    try {
      moduleContext.put(KEY_BILLTYPES, CollectionUtil.toString(BillTypeUtils.getAllNameCaption()));
    } catch (Exception e) {
      Logger.getLogger(AdvanceServiceServlet.class).error("AdvanceServiceServlet error", e);
    }
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        AdvancePermDef.RESOURCE_ADVANCE_SET);
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        PayDepositPermDef.RESOURCE_PAYDEPOSIT_SET));
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        RecDepositPermDef.RESOURCE_RECDEPOSIT_SET));
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        PayDepositRepaymentPermDef.RESOURCE_DEPOSITREPAYMENT_SET));
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        RecDepositRepaymentPermDef.RESOURCE_RECDEPOSITREPAYMENT_SET));
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        PayDepositMovePermDef.RESOURCE_PAYDEPOSITMOVE_SET));
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        RecDepositMovePermDef.RESOURCE_RECDEPOSITMOVE_SET));
    return permissions;
  }

  @Override
  public BAdvanceData getAdvanceDataByAccountUnitUuid(String accountUnitUuid) throws Exception {
    try {
      QueryDefinition definition = new QueryDefinition();

      definition.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnitUuid);
      Map<String, String> counterparts = getOptionService().getCounterpartType();
      if (counterparts.isEmpty() == false) {
        definition.addCondition(Counterparts.CONDITION_MODULE_IN, counterparts.keySet().toArray());
      }
      QueryResult<Advance> queryResult = getAdvanceService().query(definition);

      List<BAdvance> recAdvance = new ArrayList<BAdvance>();
      List<BAdvance> payAdvance = new ArrayList<BAdvance>();

      for (Advance source : queryResult.getRecords()) {
        BAdvance target = AdvanceBizConverter.getInstance().convert(source);
        if (DirectionType.receipt.getDirectionValue() == source.getDirection())
          recAdvance.add(target);
        else
          payAdvance.add(target);
      }

      BAdvanceData result = new BAdvanceData(recAdvance, payAdvance);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BAdvanceData getAdvanceDataByCounterpartUnitUuid(String counterpartUnitUuid)
      throws Exception {
    try {
      QueryDefinition definition = new QueryDefinition();
      definition.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpartUnitUuid);
      // 受项目限制
      List<String> list = getUserStores(getSessionUser().getId());
      if (list.isEmpty()) {
        return new BAdvanceData(new ArrayList<BAdvance>(), new ArrayList<BAdvance>());
      }
      definition.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_IN, list.toArray());

      QueryResult<Advance> queryResult = getAdvanceService().query(definition);
      if (queryResult == null)
        return new BAdvanceData();

      List<BAdvance> recAdvance = new ArrayList<BAdvance>();
      List<BAdvance> payAdvance = new ArrayList<BAdvance>();

      for (Advance source : queryResult.getRecords()) {
        BAdvance target = AdvanceBizConverter.getInstance().convert(source);
        if (DirectionType.receipt.getDirectionValue() == source.getDirection())
          recAdvance.add(target);
        else
          payAdvance.add(target);
      }

      BAdvanceData result = new BAdvanceData(recAdvance, payAdvance);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BCounterpart> queryCounterpartUnit(AdvanceFilter filter) throws Exception {
    try {
      QueryDefinition queryDef = new QueryDefinition();

      QueryCondition condition = new QueryCondition(Basices.CONDITION_PERM_GROUP_ID_IN);
      List<String> list = getUserGroups(getSessionUser().getId());
      if (list.size() > 0) {
        condition.addParameter(list.toArray());
      }
      if (!list.contains(HasPerm.DEFAULT_PERMGROUP)) {
        condition.addParameter(HasPerm.DEFAULT_PERMGROUP);
      }
      queryDef.getConditions().add(condition);

      // 状态为使用中
      queryDef.addCondition(Counterparts.CONDITION_STATE_EQUALS, BasicState.using.name());
      // 名称，代码模糊查询
      if (StringUtil.isNullOrBlank(filter.getCounterpartUnitLike()) == false) {
        queryDef
            .addCondition(Counterparts.CONDITION_CODENAME_LIKE, filter.getCounterpartUnitLike());
      }
      if (StringUtil.isNullOrBlank(filter.getCounterpartType()) == false) {
        queryDef.addCondition(Counterparts.CONDITION_MODULE_EQUALS, filter.getCounterpartType());
      } else {
        Map<String, String> counterparts = getOptionService().getCounterpartType();
        if (counterparts.isEmpty() == false) {
          queryDef.addCondition(Counterparts.CONDITION_MODULE_IN, counterparts.keySet().toArray());
        }
      }

      List<Order> orders = filter.getOrders();
      for (Order order : orders) {
        QueryOrderDirection dir = OrderDir.desc.equals(order.getDir()) ? QueryOrderDirection.desc
            : QueryOrderDirection.asc;
        if (Counterparts.ORDER_BY_CODE.equals(order.getFieldName())) {
          queryDef.addOrder(Counterparts.ORDER_BY_CODE, dir);
        } else if (Counterparts.ORDER_BY_NAME.equals(order.getFieldName())) {
          queryDef.addOrder(Counterparts.ORDER_BY_NAME, dir);
        }
      }

      queryDef.setPage(filter.getPage());
      queryDef.setPageSize(filter.getPageSize());

      QueryResult<Counterpart> qr = getCounterPartService().query(
          new BeanQueryDefinitionConverter().convert(queryDef));
      RPageData<BCounterpart> rp = new RPageData<BCounterpart>();
      rp.setCurrentPage(qr.getPaging().getPage());
      rp.setPageCount(qr.getPaging().getPageCount());
      rp.setTotalCount((int) qr.getPaging().getRecordCount());

      List<BCounterpart> result = new ArrayList<BCounterpart>();
      for (Counterpart counterPart : qr.getRecords()) {
        BCounterpart target = new BCounterpart();
        target.setCode(counterPart.getCode());
        target.setCounterpartType(counterPart.getModule());
        target.setName(counterPart.getName());
        target.setUuid(counterPart.getUuid());
        result.add(target);
      }

      rp.setValues(result);
      return rp;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BAdvanceLog> queryLog(AdvanceLogFilter filter) throws Exception {
    try {
      String accountUnitCode = filter.getAccountUnitCode();
      String counterpartCode = filter.getCounterpartUnit();
      String counterpartType = filter.getCounterpartType();
      String subjectCode = filter.getSubject();
      Date beginTime = filter.getBeginLogTime();
      Date endTime = filter.getEndLogTime();
      String direction = filter.getAdvanceType();
      Boolean isRec = Boolean.TRUE;
      if (AdvanceUrlParams.Base.DEPOSITPAY.equals(direction))
        isRec = Boolean.FALSE;
      else if (AdvanceUrlParams.Base.DEPOSITREC.equals(direction))
        isRec = Boolean.TRUE;

      QueryDefinition definition = new QueryDefinition();

      if (isRec) {
        definition.addCondition(Advances.CONDITION_LOG_DIRECTION_EQUALS,
            DirectionType.receipt.getDirectionValue());
      } else {
        definition.addCondition(Advances.CONDITION_LOG_DIRECTION_EQUALS,
            DirectionType.payment.getDirectionValue());
      }

      if (!StringUtil.isNullOrBlank(accountUnitCode)) {
        definition.addCondition(Advances.CONDITION_LOG_ACCOUNTUNIT_CODE_EQUALS, accountUnitCode);
      }

      if (!StringUtil.isNullOrBlank(counterpartType)) {
        definition.addCondition(Advances.CONDITION_LOG_COUNTERPAR_TYPE_EQUALS, counterpartType);
      }

      if (!StringUtil.isNullOrBlank(counterpartCode)) {
        definition.addCondition(Advances.CONDITION_LOG_COUNTERPAR_CODEORNAME_LIKE, counterpartCode);
      }

      if (!StringUtil.isNullOrBlank(subjectCode)) {
        definition.addCondition(Advances.CONDITION_LOG_SUBJECT_CODE_EQUALS, subjectCode);

      }

      if (beginTime != null || endTime != null) {
        List<Date> rangeDate = new ArrayList<Date>();
        rangeDate.add(0, beginTime);
        rangeDate.add(1, endTime);
        definition.addCondition(Advances.CONDITION_LOG_TIME_BETWEEN, rangeDate.toArray());
      }

      definition.setPage(filter.getPage());
      definition.setPageSize(filter.getPageSize());

      QueryResult<AdvanceLog> queryResult = getAdvanceService().queryLog(definition);

      RPageData<BAdvanceLog> rp = new RPageData<BAdvanceLog>();
      RPageDataUtil.copyPagingInfo(queryResult, rp);
      List<BAdvanceLog> result = new ArrayList<BAdvanceLog>();
      for (AdvanceLog source : queryResult.getRecords()) {
        result.add(AdvancelogBizConverter.getInstance().convert(source));
      }

      rp.setValues(result);

      return rp;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private com.hd123.m3.account.service.adv.AdvanceService getAdvanceService() {
    return M3ServiceFactory.getService(com.hd123.m3.account.service.adv.AdvanceService.class);
  }

  private RSCounterpartService getCounterPartService() {
    return M3ServiceFactory.getService(RSCounterpartService.class);
  }
}
