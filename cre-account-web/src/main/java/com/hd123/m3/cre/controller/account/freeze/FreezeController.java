/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	FreezeController.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月17日 - renjingzhan - 创建。
 */
package com.hd123.m3.cre.controller.account.freeze;

import java.util.ArrayList;
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

import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.freeze.Freeze;
import com.hd123.m3.account.service.freeze.FreezeLine;
import com.hd123.m3.account.service.freeze.FreezeService;
import com.hd123.m3.account.service.freeze.Freezes;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.StandardBill;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.PermedModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.util.CommonUtil;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author renjingzhan
 *
 */
@Controller
@RequestMapping("account/freeze/*")
public class FreezeController extends PermedModuleController {

  /** 键值：单据类型 */
  protected static final String KEY_BILLTYPES = "billTypes";

  public static final String RES_MERGE_KEY = "account.freeze";
  @Autowired
  private FreezeService freezeService;
  @Autowired
  protected AccountService accountService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RES_MERGE_KEY));

    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put(KEY_BILLTYPES, JsonUtil.objectToJson(CommonUtil.getBillTypes()));
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  @ResponseBody
  protected Freeze Load(@RequestParam("uuid") String uuid) throws Exception {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }
    try {
      Freeze entity = freezeService.get(uuid, Freeze.PART_LINE);
      if (entity == null) {
        return null;
      }
      validEntityPerm(entity);
      return entity;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping("save")
  @ResponseBody
  public StandardBill save(@RequestBody Freeze entity) throws Exception {
    List<String> accountUuids = new ArrayList<String>();
    for (FreezeLine freezeLine : entity.getLines()) {
      accountUuids.add(freezeLine.getAccountUuid());
    }
    try {
      BeanOperateContext operCtx = new BeanOperateContext(getSessionUser());
      operCtx.setAttribute(Freezes.PROPERTY_MESSAGE, entity.getFreezeReason());

      String uuid = freezeService.freeze(accountUuids, entity.getPermGroupId(),
          entity.getPermGroupTitle(), operCtx);
      log(StringUtil.isNullOrBlank(entity.getUuid()) ? AuditActions.R.create()
          : AuditActions.R.modify(), uuid);
      return freezeService.get(uuid);
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping("query")
  public @ResponseBody SummaryQueryResult query(@RequestBody QueryFilter queryFilter) {

    if (queryFilter == null) {
      return new SummaryQueryResult();
    }

    FlecsQueryDefinition queryDef = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());

    QueryResult qr = freezeService.query(queryDef);

    updateNaviEntities(getObjectName(), qr.getRecords());

    SummaryQueryResult result = SummaryQueryResult.newInstance(qr);

    buildSummary(result, queryFilter);

    return result;
  }


  private void buildSummary(SummaryQueryResult result, QueryFilter queryFilter) {
    queryFilter.setPage(1);
    // 查询全部
    queryFilter.getFilter().put("state", null);
    FlecsQueryDefinition queryDefinition = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    result.getSummary().put("all", freezeService.query(queryDefinition).getRecordCount());
    // 已冻结
    queryFilter.getFilter().put("state", "froze");
    queryDefinition = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    result.getSummary().put("froze", freezeService.query(queryDefinition).getRecordCount());
    // 已解冻
    queryFilter.getFilter().put("state", "unfroze");
    queryDefinition = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    result.getSummary().put("unfroze", freezeService.query(queryDefinition).getRecordCount());
  }

  protected FlecsQueryDefinitionBuilder getQueryBuilder() {

    return getAppCtx().getBean(FreezeQueryBuilder.class);
  }

  @RequestMapping("unfreeze")
  public @ResponseBody void unfreeze(@RequestParam("uuid") String uuid,
      @RequestParam("version") long version, @RequestParam("unfreezeReason") String unfreezeReason) throws M3ServiceException {
    BeanOperateContext boc = new BeanOperateContext(getSessionUser());
    boc.setAttribute(Freezes.PROPERTY_MESSAGE, unfreezeReason);
    try {
      freezeService.unfreeze(uuid, version, boc);
      log(AuditActions.R.modify(), uuid);
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
   
  }

  @Override
  protected String getObjectName() {

    return R.R.moduleCaption();
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("account.freeze")
    public String moduleCaption();

  }
}
