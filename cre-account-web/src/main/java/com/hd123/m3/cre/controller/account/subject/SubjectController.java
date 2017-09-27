/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	SubjectController.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月11日 - lizongyi - 创建。
 */
package com.hd123.m3.cre.controller.account.subject;

import java.util.ArrayList;
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

import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectStoreTaxRate;
import com.hd123.m3.account.service.subject.customtype.CustomSubjectType;
import com.hd123.m3.account.service.subject.customtype.CustomSubjectTypeService;
import com.hd123.m3.account.service.subject.type.SubjectType;
import com.hd123.m3.account.service.subject.type.SubjectTypeService;
import com.hd123.m3.account.service.subject.type.SubjectTypes;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.ModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.commons.servlet.permed.DataPermedHelper;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.entity.VersionConflictException;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author lizongyi
 *
 */
@Controller
@RequestMapping("account/subject/*")
public class SubjectController extends ModuleController {

  private static final String RESOURCE_KEY = "account.subject";
  private static final String FILTER_FETCHPARTS = "fetchParts";

  /** 授权组配置项 */
  private static final String KEY_PERMED_GROUPS = "permedGroups";
  /** 用途 */
  private static final String USAGES = "usages";
  /** 自定义类型 */
  private static final String CUSTOMTYPES = "customTypes";

  @Autowired
  private SubjectService subjectService;
  @Autowired
  private SubjectTypeService subjectTypeService;
  @Autowired
  private CustomSubjectTypeService customSubjectTypeService;

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext
        .put(KEY_PERMED_GROUPS, getDataPermedAuxiliary().getUserGroups(getSessionUserId()));

    List<SubjectType> usageTypes = subjectTypeService.query(new FlecsQueryDefinition(),
        SubjectTypes.PART_PERMGROUP).getRecords();
    for (SubjectType subjectType : usageTypes) {
      List<UCN> list = subjectType.getPermGroups();
      if (list.size() != 0 && "-".equals(list.get(0).getUuid())) {
        list.clear();
      }
    }
    moduleContext.put(USAGES, usageTypes);
    moduleContext.put(CUSTOMTYPES, customSubjectTypeService.getAll());
  }

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_KEY));
    return permissions;
  }

  @RequestMapping(value = "query", method = RequestMethod.POST)
  public @ResponseBody SummaryQueryResult<Subject> query(@RequestBody QueryFilter queryFilter) {
    if (queryFilter == null) {
      return new SummaryQueryResult<Subject>();
    }

    FlecsQueryDefinition queryDef = getQueryBuilder().build(queryFilter);
    String fetchParts = (String) queryFilter.getFilter().get(FILTER_FETCHPARTS);

    QueryResult<Subject> qr = subjectService.query(queryDef, fetchParts);
    SummaryQueryResult result = SummaryQueryResult.newInstance(qr);
    buildSummary(result, queryFilter);
    return result;
  }

  @RequestMapping(value = "checkDuplicateCode", method = RequestMethod.GET)
  @ResponseBody
  public boolean checkDuplicateCode(@RequestParam("code") String code,
      @RequestParam("uuid") String uuid) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(code)) {
      return false;
    }
    Subject subject = subjectService.getByCode(code);
    return subject != null && !subject.getUuid().equals(uuid);
  }

  @RequestMapping(value = "save", method = RequestMethod.POST)
  public @ResponseBody String save(@RequestBody Subject entity) throws M3ServiceException {
    if (entity == null) {
      return null;
    }
    try {
      Iterator<SubjectStoreTaxRate> iterator = entity.getStoreTaxRates().iterator();
      while (iterator.hasNext()) {
        SubjectStoreTaxRate value = iterator.next();
        if (value.getStore() == null || value.getTaxRate() == null) {
          iterator.remove();
        }
      }
      String uuid = subjectService.save(entity, new BeanOperateContext(getSessionUser()));
      log(StringUtil.isNullOrBlank(entity.getUuid()) ? AuditActions.R.create()
          : AuditActions.R.modify(), uuid);
      return uuid;
    } catch (IllegalArgumentException e) {
      throw new M3ServiceException(e.getMessage());
    } catch (VersionConflictException e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping(value = "saveUsages", method = RequestMethod.POST)
  public @ResponseBody void saveAllUsages(@RequestBody List<SubjectType> entities)
      throws M3ServiceException {
    if (entities == null || entities.isEmpty()) {
      return;
    }

    List<SubjectType> usages = new ArrayList<SubjectType>();
    for (SubjectType subjectType : entities) {
      if (!StringUtil.isNullOrBlank(subjectType.getCode())) {
        usages.add(subjectType);
      }
    }
    subjectTypeService.saveAll(usages, new BeanOperateContext(getSessionUser()));
  }

  @RequestMapping(value = "saveCustomTypes", method = RequestMethod.POST)
  public @ResponseBody void saveAllTypes(@RequestBody List<CustomSubjectType> entities)
      throws M3ServiceException {
    if (entities == null || entities.isEmpty()) {
      return;
    }

    List<CustomSubjectType> customTypes = new ArrayList<CustomSubjectType>();
    for (CustomSubjectType customType : entities) {
      if (!StringUtil.isNullOrBlank(customType.getName())) {
        customTypes.add(customType);
      }
    }
    customSubjectTypeService.saveAll(customTypes, new BeanOperateContext(getSessionUser()));
  }

  @RequestMapping(value = "getCustomTypes", method = RequestMethod.GET)
  public @ResponseBody List<CustomSubjectType> getCustomTypes() throws M3ServiceException {
    return customSubjectTypeService.getAll();
  }

  @RequestMapping(value = "getSubjectTypes", method = RequestMethod.GET)
  public @ResponseBody List<SubjectType> getSubjectTypes() throws M3ServiceException {

    List<SubjectType> usageTypes = subjectTypeService.query(new FlecsQueryDefinition(),
        SubjectTypes.PART_PERMGROUP).getRecords();
    return usageTypes;
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody Subject get(@RequestParam("uuid") String uuid) throws Exception {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }
    Subject entity = subjectService.get(uuid, Subject.PART_WHOLE);
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
      subjectService.enable(uuid, version, new BeanOperateContext(getSessionUser()));
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
      subjectService.disable(uuid, version, new BeanOperateContext(getSessionUser()));
      log("停用", uuid);
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  private FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(SubjectQueryBuilder.class);
  }

  private DataPermedHelper getDataPermedAuxiliary() {
    return getAppCtx().getBean(DataPermedHelper.class);
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
    filter.getFilter().put("state", null);
    FlecsQueryDefinition queryDefinition = getQueryBuilder().build(filter);
    result.getSummary().put("all", subjectService.query(queryDefinition).getRecordCount());
    // 使用中
    filter.getFilter().put("state", "enabled");
    queryDefinition = getQueryBuilder().build(filter);
    result.getSummary().put("enabled", subjectService.query(queryDefinition).getRecordCount());
    // 已停用
    filter.getFilter().put("state", "disabled");
    queryDefinition = getQueryBuilder().build(filter);
    result.getSummary().put("disabled", subjectService.query(queryDefinition).getRecordCount());
  }
}
