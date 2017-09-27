/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BankController.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月16日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.bank;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.bank.BankService;
import com.hd123.m3.account.service.bank.Banks;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.EntityUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.ModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.AccountBasicConstants;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.m3.sales.service.paytype.PaymentTypes;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 银行资料控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/bank/*")
public class AccBankController extends ModuleController {

  private static final String RESOURCE_KEY = "account.bank";
  private @Autowired BankService bankService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_KEY));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
  }

  @RequestMapping(value = "save", method = RequestMethod.POST)
  @ResponseBody
  public String save(@RequestBody Bank entity) throws Exception {
    if (entity == null) {
      return null;
    }
    try {
      String uuid = bankService.save(entity, new BeanOperateContext(getSessionUser()));
      log(StringUtil.isNullOrBlank(entity.getUuid()) ? AuditActions.R.create()
          : AuditActions.R.modify(), uuid);
      return uuid;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  @ResponseBody
  public Bank get(@RequestParam("uuid") String uuid) throws Exception {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }
    Bank entity = bankService.get(uuid);
    return entity;
  }

  @RequestMapping(value = "enable", method = RequestMethod.POST)
  @ResponseBody
  public void enable(@RequestParam("uuid") String uuid, @RequestParam("version") long version)
      throws Exception {
    if (StringUtil.isNullOrBlank(uuid)) {
      return;
    }
    try {
      bankService.enable(uuid, version, new BeanOperateContext(getSessionUser()));
      log("启用", uuid);
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping(value = "disable", method = RequestMethod.POST)
  @ResponseBody
  public void disable(@RequestParam("uuid") String uuid, @RequestParam("version") long version)
      throws Exception {
    if (StringUtil.isNullOrBlank(uuid)) {
      return;
    }
    try {
      bankService.disable(uuid, version, new BeanOperateContext(getSessionUser()));
      log("停用", uuid);
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  /**
   * 校验代码是否唯一（不考虑状态）
   * 
   * @param code
   * @param storeUuid
   * @param uuid
   * @return
   * @throws M3ServiceException
   */
  @RequestMapping(value = "checkDuplicateCode", method = RequestMethod.GET)
  @ResponseBody
  public boolean checkDuplicateCode(@RequestParam("code") String code,
      @RequestParam("storeUuid") String storeUuid, @RequestParam("uuid") String uuid)
      throws M3ServiceException {
    if (StringUtil.isNullOrBlank(code)) {
      return false;
    }
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Banks.CONDITION_CODE_EQUALS, code);
    List<Bank> banks = bankService.query(definition).getRecords();
    return banks.size() > 1 || (banks.size() == 1 && !banks.get(0).getUuid().equals(uuid));
  }

  @RequestMapping("query")
  public @ResponseBody SummaryQueryResult<Bank> queryBank(@RequestBody QueryFilter filter) {
    if (filter == null) {
      return new SummaryQueryResult<Bank>();
    }
    FlecsQueryDefinition queryDef = getQueryBuilder().build(filter, Banks.OBJECT_NAME,
        getSessionUserId());
    QueryResult<Bank> query = bankService.query(queryDef);
    SummaryQueryResult result = SummaryQueryResult.newInstance(query);
    buildSummary(result, filter);
    return result;

  }

  @RequestMapping(value = "all")
  public @ResponseBody List<Bank> getAll(@RequestBody QueryFilter filter) {
    FlecsQueryDefinition def = new FlecsQueryDefinition();
    def.addFlecsCondition(PaymentTypes.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
    List<Bank> banks = bankService.query(def).getRecords();

    if (filter == null) {
      return banks;
    }
    try {
      filterBanks(banks, filter);
      return banks;
    } catch (M3ServiceException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * 验证项目下科目是否存在唯一的银行资料中
   * 
   * @param queryFilter
   * @return
   */
  @RequestMapping("validSubjects")
  @ResponseBody
  public void validSubjects(@RequestBody QueryFilter queryFilter) throws Exception {
    if (queryFilter == null) {
      return;
    }
    String bankUuid = (String) queryFilter.getFilter().get("uuid");
    String storeUuid = (String) queryFilter.getFilter().get("storeUuid");
    List<String> subjectUuids = (List<String>) queryFilter.getFilter().get("subjectUuids");
    if (StringUtil.isNullOrBlank(storeUuid) || subjectUuids == null || subjectUuids.isEmpty()) {
      return;
    }
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Banks.CONDITION_STORE_EQUALS, storeUuid);
    definition.addCondition(Banks.CONDITION_CONTAINS_SUBJECT, subjectUuids.toArray());
    definition.setPageSize(1);
    List<Bank> records = bankService.query(definition).getRecords();
    if (records.isEmpty() == false) {
      if (StringUtil.isNullOrBlank(bankUuid)) {
        throw new M3ServiceException("一个项目中，一个科目只能属于一个银行资料");
      } else {
        List<String> uuids = EntityUtil.toUuids(records);
        if (uuids.size() != 1 || uuids.contains(bankUuid) == false) {
          throw new M3ServiceException("一个项目中，一个科目只能属于一个银行资料");
        }
      }
    }
  }

  private void filterBanks(List<Bank> banks, QueryFilter filter) throws M3ServiceException {
    String storeUuid = (String) filter.getFilter().get(AccountBasicConstants.FILTER_STORE_UUID);
    if (StringUtil.isNullOrBlank(storeUuid) == false) {
      Iterator<Bank> iterator = banks.iterator();
      while (iterator.hasNext()) {
        Bank bank = iterator.next();
        if (storeUuid.equals(bank.getStore().getUuid()) == false) {
          iterator.remove();
        }
      }

      CollectionUtil.filterByKeyword(banks, filter.getKeyword());

    }
  }

  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(AccBankQueryBuilder.class);
  }

  /**
   * 搜索页面状态显示数字
   * 
   * @param result
   * @param filter
   */
  private void buildSummary(SummaryQueryResult result, QueryFilter filter) {
    filter.setPage(1);
    // 查询全部
    filter.getFilter().put("enabled", null);
    FlecsQueryDefinition queryDefinition = getQueryBuilder().build(filter, Banks.OBJECT_NAME,
        getSessionUserId());
    result.getSummary().put("all", bankService.query(queryDefinition).getRecordCount());
    // 使用中
    filter.getFilter().put("enabled", "true");
    queryDefinition = getQueryBuilder().build(filter, Banks.OBJECT_NAME, getSessionUserId());
    result.getSummary().put("true", bankService.query(queryDefinition).getRecordCount());
    // 已停用
    filter.getFilter().put("enabled", "false");
    queryDefinition = getQueryBuilder().build(filter, Banks.OBJECT_NAME, getSessionUserId());
    result.getSummary().put("false", bankService.query(queryDefinition).getRecordCount());
  }

}
