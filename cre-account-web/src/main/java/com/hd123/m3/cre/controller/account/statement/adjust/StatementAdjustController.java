/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	StatementAdjustController.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - renjingzhan - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.adjust;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.statement.adjust.StatementAdjust;
import com.hd123.m3.account.service.statement.adjust.StatementAdjustService;
import com.hd123.m3.account.service.statement.adjust.StatementAdjusts;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.common.util.CommonUtil;
import com.hd123.m3.cre.controller.account.statement.builder.StatementAdjustQueryBuilder;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author renjingzhan
 *
 */
@Controller
@RequestMapping("account/statement/adjust/*")
public class StatementAdjustController extends BizFlowModuleController {

  /** 键值：单据类型 */
  protected static final String KEY_BILLTYPES = "billTypes";

  public static final String RES_MERGE_KEY = "account.statementAdjust";
  @Autowired
  private StatementAdjustService statementAdjustService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        AccountPermResourceDef.RESOURCE_STATEMENTADJUST,
        AccountPermResourceDef.RESOURCE_ACCOUNTDEFRAYAL));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put(KEY_BILLTYPES, JsonUtil.objectToJson(CommonUtil.getBillTypes()));
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  @ResponseBody
  protected StatementAdjust Load(@RequestParam("uuid") String uuid) throws Exception {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }
    try {
      StatementAdjust entity = statementAdjustService.get(uuid);
      if (entity == null) {
        return null;
      }
      validEntityPerm(entity);
      return entity;
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(json)) {
      return null;
    }
    StatementAdjust entity = JsonUtil.jsonToObject(json, StatementAdjust.class);
    BeanOperateContext operCtx = new BeanOperateContext(getSessionUser());
    try {
      return statementAdjustService.save(entity, operCtx);
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {

    return getAppCtx().getBean(StatementAdjustQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {

    return R.R.moduleCaption();
  }

  @Override
  protected String getServiceBeanId() {

    return StatementAdjustService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {

    return StatementAdjusts.OBJECT_NAME;
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("账单调整单")
    public String moduleCaption();

  }
}
