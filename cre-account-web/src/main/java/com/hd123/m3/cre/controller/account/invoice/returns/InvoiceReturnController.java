/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceReturnController.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月30日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.returns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.service.ivc.common.InvoiceLine;
import com.hd123.m3.account.service.ivc.returns.InvoiceReturn;
import com.hd123.m3.account.service.ivc.returns.InvoiceReturnService;
import com.hd123.m3.account.service.ivc.returns.InvoiceReturns;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStockService;
import com.hd123.m3.commons.biz.EntityService;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.invoice.common.BInvoiceLine;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryResult;

/**
 * 发票领退单
 * 
 * @author xiahongjian
 *
 */
@Controller
@RequestMapping("account/invoice/return/*")
public class InvoiceReturnController extends BizFlowModuleController {
  private static final String RESOURCE_INVOICE_RETURN = "account.invoice.return";

  @Autowired
  private InvoiceReturnService invoiceReturnService;
  @Autowired
  private UserService userService;
  @Autowired
  private InvoiceStockService invoiceStockService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_INVOICE_RETURN));
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

  @Override
  protected EntityService getEntityService() {
    final InvoiceReturnService service = getAppCtx().getBean(getServiceBeanId(),
        InvoiceReturnService.class);
    return new EntityService<InvoiceReturn>() {
      @Override
      public String save(InvoiceReturn entity, BeanOperateContext operateCtx)
          throws M3ServiceException {
        return service.save(entity, operateCtx);
      }

      @Override
      public InvoiceReturn get(String id) {
        return service.get(id, InvoiceReturns.PART_WHOLE);
      }

      @Override
      public QueryResult<InvoiceReturn> query(FlecsQueryDefinition definition) {
        return service.query(definition);
      }
    };
  }

  @ResponseBody
  @RequestMapping(value = "load", method = RequestMethod.GET)
  public BInvoiceReturn load(@RequestParam("uuid") String uuid) {
    BInvoiceReturn entity;
    InvoiceReturn source = invoiceReturnService.get(uuid, InvoiceReturns.PART_RETURNLINES);
    if (source == null)
      return null;

    entity = BInvoiceReturn.newInstance(source);
    Collection<String> numbers = new ArrayList<String>();
    for (BInvoiceLine line : entity.getReturnLines())
      numbers.add(line.getStartNumber());
    List<InvoiceStock> stocks = invoiceStockService.getsByInvoiceNumber(numbers);
    Map<String, InvoiceStock> invoiceMap = new HashMap<String, InvoiceStock>();
    for (InvoiceStock stock : stocks)
      invoiceMap.put(stock.getInvoiceNumber(), stock);
    for (BInvoiceLine line : entity.getReturnLines())
      line.setStockNumber(invoiceMap.get(line.getStartNumber()));
    return entity;
  }

  private BeanOperateContext newOperateCxt() {
    return new BeanOperateContext(getSessionUser());
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtils.isBlank(json))
      return null;

    InvoiceReturn entity = JsonUtil.jsonToObject(json, InvoiceReturn.class);
    if (entity != null && entity.getReturnLines() != null && !entity.getReturnLines().isEmpty()) {
      List<InvoiceLine> lines = entity.getReturnLines();
      Iterator<InvoiceLine> iterator = lines.iterator();
      while (iterator.hasNext()) {
        InvoiceLine line = iterator.next();
        if (StringUtils.isBlank(line.getInvoiceCode()))
          iterator.remove();
      }
    }
    return invoiceReturnService.save(entity, newOperateCxt());
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceReturnQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return "发票领退单";
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceReturnService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return InvoiceReturns.OBJECT_NAME;
  }
}
