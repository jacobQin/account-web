/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名： cre-web
 * 文件名： InvoiceReceiveController.java
 * 模块说明：    
 * 修改历史：
 * 2017年3月28日 - huangjin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.receive;

import java.util.ArrayList;
import java.util.Collection;
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
import com.hd123.m3.account.service.ivc.receive.InvoiceReceive;
import com.hd123.m3.account.service.ivc.receive.InvoiceReceiveService;
import com.hd123.m3.account.service.ivc.receive.InvoiceReceives;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStockService;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.invoice.common.BInvoiceLine;
import com.hd123.m3.cre.controller.account.invoice.receive.biz.BInvoiceReceive;
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
@RequestMapping("account/invoice/receive/*")
public class InvoiceReceiveController extends BizFlowModuleController{
  public static final String RESOURCE_PATH = "account/invoice/receive";


  @Autowired
  private UserService userService;
  
  @Autowired
  InvoiceReceiveService invoiceReceiveService;
  
  @Autowired
  InvoiceStockService invoiceStockService;
  
  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        AccountPermResourceDef.RESOURCE_INVOICE_RECEIVE));
    return permissions;
  }
  
  //获取上下文模块信息
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
  private void beforerSave(InvoiceReceive entity) throws M3ServiceException {
    // 过滤空行(如果原发票号码为null认为是空行)
    for (int index = entity.getReceiveLines().size() - 1; index >= 0; index--) {
      if (StringUtil.isNullOrBlank(entity.getReceiveLines().get(index).getStartNumber())) {
        entity.getReceiveLines().remove(index);
      }
    }
  }
  
  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(json))
      return null;

    InvoiceReceive entity = JsonUtil.jsonToObject(json, InvoiceReceive.class);
   
    beforerSave(entity);
    return invoiceReceiveService.save(entity, new BeanOperateContext(getSessionUser()));
  }
  
  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody BInvoiceReceive load(@RequestParam("uuid") String uuid) throws M3ServiceException {
    BInvoiceReceive entity = new BInvoiceReceive();
    InvoiceReceive source = invoiceReceiveService.get(uuid);
    if(source == null){
      return null;
    }
    
    entity = BInvoiceReceive.newInstance(source);
    
    Collection<String> numbers = new ArrayList<String>();
    
    for (BInvoiceLine invoiceLine : entity.getReceiveLines()) {
      numbers.add(invoiceLine.getStartNumber());
    }
    if(numbers.size() == 0) {
      return null;
    }
    List<InvoiceStock> stocks = invoiceStockService.getsByInvoiceNumber(numbers);
    
    for (BInvoiceLine invoiceLine : entity.getReceiveLines()) {
      for (InvoiceStock invoiceStock : stocks) {
        if(invoiceLine.getStartNumber().equals(invoiceStock.getInvoiceNumber())) {
          invoiceLine.setStockNumber(invoiceStock);
        }
      }
    }
    return entity; 
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceReceiveQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return "发票领用单";
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceReceiveService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return InvoiceReceives.OBJECT_NAME;
  }
}
