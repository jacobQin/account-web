/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名： cre-web
 * 文件名： InvoiceInstockController.java
 * 模块说明：    
 * 修改历史：
 * 2017年3月21日 - huangjin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.instock;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.service.ivc.instock.InvoiceInstock;
import com.hd123.m3.account.service.ivc.instock.InvoiceInstockService;
import com.hd123.m3.account.service.ivc.instock.InvoiceInstocks;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountCommonComponent;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 
 * @author huangjin
 *
 */
@Controller
@RequestMapping("account/invoice/instock/*")
public class InvoiceInstockController extends BizFlowModuleController{
  
  public static final String RESOURCE_PATH = "account/invoice/instock";
  
  @Autowired
  private UserService userService;
  
  @Autowired
  InvoiceInstockService invoiceInstockService;
  
  @Autowired
  private AccountCommonComponent accCommonComponent;
  
  private BeanOperateContext newOperateCxt() {
    return new BeanOperateContext(getSessionUser());
  }
  
  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        AccountPermResourceDef.RESOURCE_INVOICE_INSTOCK));
    return permissions;
  }
  
  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    IsOperator operator = getSessionUser();
    User user = userService.get(operator.getId(), User.PART_CONTACTS);
    UCN unc = new UCN(user.getUuid(), operator.getId(), operator.getFullName());
    moduleContext.put("curUser", unc);
  }
  
  /**
   * 
   * @throws M3ServiceException
   */
  private void beforerSave(InvoiceInstock entity) throws M3ServiceException {
    // 过滤空行(如果原发票号码为null认为是空行)
    for (int index = entity.getInstockLines().size() - 1; index >= 0; index--) {
      if (StringUtil.isNullOrBlank(entity.getInstockLines().get(index).getStartNumber())) {
        entity.getInstockLines().remove(index);
      }
    }
  }
  
  @RequestMapping("save")
  public @ResponseBody InvoiceInstock save(@RequestBody InvoiceInstock entity) throws Exception {
    String uuid = invoiceInstockService.save(entity, newOperateCxt());

    InvoiceInstock target = load(uuid);
    log(StringUtil.isNullOrBlank(entity.getUuid()) ? AuditActions.R.create()
        : AuditActions.R.modify(), uuid);
    return target;
  }
  
  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody InvoiceInstock load(@RequestParam("uuid") String uuid) throws M3ServiceException {
    InvoiceInstock entity = invoiceInstockService.get(uuid);
    if(entity == null){
      return null;
    }
    return entity; 
  }
  

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(json))
      return null;

    InvoiceInstock entity = JsonUtil.jsonToObject(json, InvoiceInstock.class);
    beforerSave(entity);
    return invoiceInstockService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceInstockQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return "发票入库单";
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceInstockService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return InvoiceInstocks.OBJECT_NAME;
  }
  
}
