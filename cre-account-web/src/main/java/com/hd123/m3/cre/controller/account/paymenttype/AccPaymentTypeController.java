/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	PaymentType.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月15日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.ModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.m3.cre.controller.account.paymenttype.paymenttype.AccountPaymentTypeQueryBuilder;
import com.hd123.m3.property.service.meter.category.MeterCategory;
import com.hd123.m3.sales.service.paytype.PaymentTypes;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.EntityNotFoundException;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.VersionConflictException;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 财务付款方式控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/paymentType/*")
public class AccPaymentTypeController extends ModuleController {
  @Autowired
  PaymentTypeService paymentTypeService;
  @Autowired
  AccountOptionComponent optionConponent;

  private static final String RESOURCE_KEY = "account.paymentType";

  @RequestMapping(value = "allValid")
  public @ResponseBody List<PaymentType> getAllValid(@RequestBody QueryFilter filter) {
    FlecsQueryDefinition def = new FlecsQueryDefinition();
    def.addFlecsCondition(PaymentTypes.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
    if(def.getOrders().isEmpty()){
      def.getOrders().add(new QueryOrder(PaymentTypes.ORDER_BY_CODE));
    }
    List<PaymentType> paymentTypes = paymentTypeService.query(def).getRecords();
    try {
      List<PaymentType> result = CollectionUtil.filterByKeyword(paymentTypes, filter.getKeyword());
      return filterPaymentTypes(filter, result);
    } catch (M3ServiceException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /** 过滤掉长短款付款方式 */
  private List<PaymentType> filterPaymentTypes(QueryFilter filter, List<PaymentType> paymentTypes) {
    Boolean filterLongShortPayment = (Boolean) filter.getFilter().get("filterLongShortPaymentType");
    if (filterLongShortPayment == null || filterLongShortPayment == false) {
      return paymentTypes;
    }

    PaymentType lsPaymentType = optionConponent.getShortLongPaymentType();
    if (lsPaymentType == null) {
      return paymentTypes;
    }

    for (int index = paymentTypes.size() - 1; index >= 0; index--) {
      PaymentType paymentType = paymentTypes.get(index);
      if (paymentType.getUuid().equals(lsPaymentType.getUuid())) {
        paymentTypes.remove(index);
        break;
      }
    }
    return paymentTypes;

  }

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_KEY));
    return permissions;
  }

  @RequestMapping("query")
  public @ResponseBody SummaryQueryResult<PaymentType> query(@RequestBody QueryFilter queryFilter) {
    if (queryFilter == null) {
      return new SummaryQueryResult<PaymentType>();
    }

    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    getQueryBuilder().build(queryDef, queryFilter);

    queryDef.setPage(queryFilter.getCurrentPage());
    queryDef.setPageSize(queryFilter.getPageSize());
    
    QueryResult<PaymentType> qr = paymentTypeService.query(queryDef);
    SummaryQueryResult result = SummaryQueryResult.newInstance(qr);
    buildSummary(result, queryFilter);
    return result;
  }

  @RequestMapping("check")
  public @ResponseBody Boolean check(String code) {
    PaymentType type = paymentTypeService.getByCode(code);
    if (type != null)
      return true;
    else
      return false;
  }

  @RequestMapping("save")
  @ResponseBody
  public PaymentType save(@RequestBody PaymentType entity) throws Exception {
    if (entity == null) {
      return null;
    }
    try {
      String uuid = paymentTypeService.save(entity, new BeanOperateContext(getSessionUser()));
      log(StringUtil.isNullOrBlank(entity.getUuid()) ? AuditActions.R.create()
          : AuditActions.R.modify(), uuid);
      return paymentTypeService.get(uuid);
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping("enable")
  @ResponseBody
  public void enable(@RequestBody PaymentType entity) throws M3ServiceException,
      IllegalArgumentException, EntityNotFoundException, VersionConflictException {
    if (entity == null) {
      return;
    }
    paymentTypeService.enable(entity.getUuid(), entity.getVersion(), new BeanOperateContext(
        getSessionUser()));
    log(R.R.enabled(), entity.getUuid());
  }

  @RequestMapping("disable")
  @ResponseBody
  public void disable(@RequestBody MeterCategory entity) throws M3ServiceException,
      IllegalArgumentException, EntityNotFoundException, VersionConflictException {
    if (entity == null) {
      return;
    }
    paymentTypeService.disable(entity.getUuid(), entity.getVersion(), new BeanOperateContext(
        getSessionUser()));
    log(R.R.disabled(), entity.getUuid());
  }

  /**
   * 状态数量合计
   * 
   * @param result
   * @param filter
   */
  private void buildSummary(SummaryQueryResult result, QueryFilter filter) {
    // 不限状态
    filter.setPageSize(1);
    filter.getFilter().put("enabled", null);
    FlecsQueryDefinition definition = getQueryBuilder().build(filter);
    result.getSummary().put("all",
        definition == null ? 0 : paymentTypeService.query(definition).getRecordCount());
    // 使用中
    filter.getFilter().put("enabled", "true");
    definition = getQueryBuilder().build(filter);
    result.getSummary().put("true", paymentTypeService.query(definition).getRecordCount());
    // 已删除
    filter.getFilter().put("enabled", "false");
    definition = getQueryBuilder().build(filter);
    result.getSummary().put("false", paymentTypeService.query(definition).getRecordCount());
  }

  private FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(AccountPaymentTypeQueryBuilder.class);
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("财务付款方式")
    public String moduleCaption();

    @DefaultStringValue("指定的对象不能为空")
    String entityIsNull();

    @DefaultStringValue("启用")
    String enabled();

    @DefaultStringValue("停用")
    String disabled();

  }
}
