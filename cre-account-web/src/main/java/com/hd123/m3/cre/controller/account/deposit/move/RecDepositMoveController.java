/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	RecDepositController.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - wangyong - 创建。
 */
package com.hd123.m3.cre.controller.account.deposit.move;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.account.service.depositmove.DepositMoveService;
import com.hd123.m3.account.service.depositmove.DepositMoveTotal;
import com.hd123.m3.account.service.depositmove.DepositMoves;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.deposit.builder.RecDepositMoveQueryBuilder;
import com.hd123.m3.cre.controller.account.deposit.model.BDepositMove;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 预存款转移单控制器
 * 
 * @author WangYong
 *
 */

@Controller
@RequestMapping("account/deposit/move/*")
public class RecDepositMoveController extends BizFlowModuleController {

  @Autowired
  private DepositMoveService depositmoveService;
  @Autowired
  private AdvanceService advanceService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        AccountPermResourceDef.RESOURCE_DEPOSIT_MOVE));
    return permissions;
  }

  @Override
  protected void buildSummary(SummaryQueryResult result, QueryFilter queryFilter) {
    FlecsQueryDefinition definition = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    DepositMoveTotal total = depositmoveService.queryTotal(definition);
    result.getSummary().put("amount", total.getAmount());
    super.buildSummary(result, queryFilter);
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put("curUser", getSessionUser());
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody BDepositMove load(@RequestParam("uuid") String uuid)
      throws M3ServiceException {
    DepositMove depositMove = depositmoveService.get(uuid);
    if (depositMove == null) {
      throw new M3ServiceException("指定的预存款转移单已不存在。");
    }

    BDepositMove entity = BDepositMove.newInstance(depositMove);

    if (BizStates.INEFFECT.equals(entity.getBizState())) {
      entity.setOutBalance(getAdvance(entity.getAccountUnit().getUuid(), entity.getOutCounterpart()
          .getUuid(), entity.getOutSubject().getUuid(), entity.getOutContract() == null ? null
          : entity.getOutContract().getUuid()));
      entity.setInBalance(getAdvance(entity.getAccountUnit().getUuid(), entity.getInCounterpart()
          .getUuid(), entity.getInSubject().getUuid(), entity.getInContract() == null ? null
          : entity.getInContract().getUuid()));
    }

    return entity;
  }

  @RequestMapping(value = "getAdvance", method = RequestMethod.GET)
  public @ResponseBody BigDecimal getAdvance(@QueryParam("accountUnitUuid") String accountUnitUuid,
      @QueryParam("counterpartUuid") String counterpartUuid,
      @QueryParam("subjectUuid") String subjectUuid, @QueryParam("contractUuid") String contractUuid)
      throws M3ServiceException {
    try {
      if (StringUtil.isNullOrBlank(contractUuid)) {
        contractUuid = Advances.NONE_BILL_UUID;
      }

      Advance advance = advanceService.get(accountUnitUuid, counterpartUuid, contractUuid,
          subjectUuid);

      return advance == null ? BigDecimal.ZERO : advance.getTotal();

    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    DepositMove entity = JsonUtil.jsonToObject(json, DepositMove.class);
    return depositmoveService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(RecDepositMoveQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return "预存款转移单";
  }

  @Override
  protected String getServiceBeanId() {
    return DepositMoveService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return DepositMoves.OBJECT_NAME_RECEIPT;
  }

}
