/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountingObjectBillController.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月30日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.accobject.AccountingObjectBill;
import com.hd123.m3.account.service.accobject.AccountingObjectBillService;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.accobject.common.AccObjectBillConstants;
import com.hd123.m3.cre.controller.account.accobject.common.AccountingObjectBillQueryBuilder;
import com.hd123.m3.cre.controller.account.accobject.converter.AccObjectBillModelConverter;
import com.hd123.m3.cre.controller.account.accobject.converter.ModelAccObjectBillConverter;
import com.hd123.m3.cre.controller.account.accobject.model.BAccObjectBillContractLine;
import com.hd123.m3.cre.controller.account.accobject.model.BAccObjectBillStoreLine;
import com.hd123.m3.cre.controller.account.accobject.model.BAccountingObjectBill;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 核算主体单控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/accobjectbill/*")
public class AccountingObjectBillController extends BizFlowModuleController {

  @Autowired
  AccountingObjectBillService service;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        AccObjectBillConstants.RESOURCE_KEY, AccObjectBillConstants.RESOURCE_ACCOBJECT_KEY));
    return permissions;
  }

  @RequestMapping("save")
  public @ResponseBody BAccountingObjectBill save(@RequestBody BAccountingObjectBill entity)
      throws M3ServiceException {
    AccountingObjectBill bill = ModelAccObjectBillConverter.getInstance().convert(entity);

    String uuid = service.save(bill, new BeanOperateContext(getSessionUser()));

    BAccountingObjectBill target = load(uuid);

    log(StringUtil.isNullOrBlank(entity.getUuid()) ? AuditActions.R.create()
        : AuditActions.R.modify(), target);
    return target;
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody BAccountingObjectBill load(@RequestParam("uuid") String uuid)
      throws M3ServiceException {
    AccountingObjectBill entity = service.get(uuid);
    try {
      validEntityPerm(entity);
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }

    BAccountingObjectBill result = AccObjectBillModelConverter.getInstance().convert(entity);
    return result;
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (json == null) {
      return null;
    }

    BAccountingObjectBill entity = JsonUtil.jsonToObject(json, BAccountingObjectBill.class);
    filterEntity(entity);

    AccountingObjectBill target = ModelAccObjectBillConverter.getInstance().convert(entity);
    return service.save(target, new BeanOperateContext(getSessionUser()));
  }

  /** 过滤掉空的明细 */
  private void filterEntity(BAccountingObjectBill entity) {
    if (entity == null) {
      return;
    }

    for (int index = entity.getStoreLines().size() - 1; index >= 0; index--) {
      BAccObjectBillStoreLine line = entity.getStoreLines().get(index);
      if (line.getSubjects() == null || line.getSubjects().isEmpty()
          || line.getAccObject() == null) {
        entity.getStoreLines().remove(index);
      }
    }

    for (int index = entity.getContractLines().size() - 1; index >= 0; index--) {
      BAccObjectBillContractLine line = entity.getContractLines().get(index);
      if (line.getContract() == null || line.getSubjects() == null || line.getSubjects().isEmpty()
          || line.getAccObject() == null) {
        entity.getContractLines().remove(index);
      }
    }
  }

  @Override
  protected String getObjectName() {
    return AccountingObjectBill.OBJECT_NAME;
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(AccountingObjectBillQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected String getServiceBeanId() {
    return AccountingObjectBillService.DEFAULT_CONTEXT_ID;
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("核算主体单")
    public String moduleCaption();
  }
}
