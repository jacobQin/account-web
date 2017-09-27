/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名： cre-web
 * 文件名： InvoiceAbortController.java
 * 模块说明：    
 * 修改历史：
 * 2017年4月7日 - huangjin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.abort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.service.ivc.abort.InvoiceAbort;
import com.hd123.m3.account.service.ivc.abort.InvoiceAbortService;
import com.hd123.m3.account.service.ivc.abort.InvoiceAborts;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStockService;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.invoice.common.BInvoiceLine;
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
@RequestMapping("account/invoice/abort/*")
public class InvoiceAbortController extends BizFlowModuleController{
  public static final String RESOURCE_PATH = "account.invoice.abort";

  @Autowired
  InvoiceAbortService invoiceAbortService;
  @Autowired
  private UserService userService;
  @Autowired
  private InvoiceStockService invoiceStockService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_PATH));
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


  @ResponseBody
  @RequestMapping(value = "load", method = RequestMethod.GET)
  public BInvoiceAbort load(@RequestParam("uuid") String uuid) {
    BInvoiceAbort entity;
    InvoiceAbort source = invoiceAbortService.get(uuid);
    if (source == null)
      return null;
    
    entity = BInvoiceAbort.newInstance(source);
    Collection<String> numbers = new ArrayList<String>();
    for (BInvoiceLine line : entity.getAbortLines())
      numbers.add(line.getStartNumber());
    List<InvoiceStock> stocks = invoiceStockService.getsByInvoiceNumber(numbers);
    Map<String, InvoiceStock> invoiceMap = new HashMap<String, InvoiceStock>();
    for (InvoiceStock stock : stocks)
      invoiceMap.put(stock.getInvoiceNumber(), stock);
    for (BInvoiceLine line : entity.getAbortLines())
      line.setStockNumber(invoiceMap.get(line.getStartNumber()));
    return entity;
  }

  /**
   * 
   * @throws M3ServiceException
   */
  private void beforerSave(InvoiceAbort entity) throws M3ServiceException {
    // 过滤空行(如果原发票号码为null认为是空行)
    for (int index = entity.getAbortLines().size() - 1; index >= 0; index--) {
      if (StringUtil.isNullOrBlank(entity.getAbortLines().get(index).getStartNumber())) {
        entity.getAbortLines().remove(index);
      }
    }
  }
  
  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(json))
      return null;

    InvoiceAbort entity = JsonUtil.jsonToObject(json, InvoiceAbort.class);
    beforerSave(entity);
    return invoiceAbortService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceAbortQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return "发票作废单";
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceAbortService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return InvoiceAborts.OBJECT_NAME;
  }

}
