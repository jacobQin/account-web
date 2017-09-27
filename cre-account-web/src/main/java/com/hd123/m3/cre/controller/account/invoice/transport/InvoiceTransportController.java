/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceTransportController.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.transport;

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
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStockService;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransport;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransportLine;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransportService;
import com.hd123.m3.account.service.ivc.transport.InvoiceTransports;
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
 * 发票挑拨单
 * 
 * @author xiahongjian
 *
 */
@Controller
@RequestMapping("account/invoice/transport/*")
public class InvoiceTransportController extends BizFlowModuleController {
  private static final String RESOURCE_INVOICE_TRANSPORT = "account.invoice.transport";
  
  @Autowired
  private InvoiceTransportService invoiceTransportService;
  @Autowired
  private UserService userService;
  @Autowired
  private InvoiceStockService invoiceStockService;

  @ResponseBody
  @RequestMapping(value = "load", method = RequestMethod.GET)
  public BInvoiceTransport load(@RequestParam("uuid") String uuid) {
    BInvoiceTransport entity;
    InvoiceTransport source = invoiceTransportService.get(uuid, InvoiceTransports.PART_TRANSPORTLINES);
    if (source == null)
      return null;
    
    entity = BInvoiceTransport.newInstance(source);
    Collection<String> numbers = new ArrayList<String>();
    for (BInvoiceLine line : entity.getTransportLines())
      numbers.add(line.getStartNumber());
    List<InvoiceStock> stocks = invoiceStockService.getsByInvoiceNumber(numbers);
    Map<String, InvoiceStock> invoiceMap = new HashMap<String, InvoiceStock>();
    for (InvoiceStock stock : stocks)
      invoiceMap.put(stock.getInvoiceNumber(), stock);
    for (BInvoiceLine line : entity.getTransportLines())
      line.setStockNumber(invoiceMap.get(line.getStartNumber()));
    return entity;
  }
  
  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_INVOICE_TRANSPORT));
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
    final InvoiceTransportService service = getAppCtx().getBean(getServiceBeanId(),
        InvoiceTransportService.class);
    return new EntityService<InvoiceTransport>() {
      @Override
      public String save(InvoiceTransport entity, BeanOperateContext operateCtx)
          throws M3ServiceException {
        return service.save(entity, operateCtx);
      }

      @Override
      public InvoiceTransport get(String id) {
        return service.get(id, InvoiceTransports.PART_WHOLE);
      }

      @Override
      public QueryResult<InvoiceTransport> query(FlecsQueryDefinition definition) {
        return service.query(definition);
      }
    };
  }
  
  private BeanOperateContext newOperateCxt() {
    return new BeanOperateContext(getSessionUser());
  }
  
  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtils.isBlank(json))
      return null;

    InvoiceTransport entity = JsonUtil.jsonToObject(json, InvoiceTransport.class);
    if (entity != null && entity.getTransportLines() != null && !entity.getTransportLines().isEmpty()) {
      List<InvoiceTransportLine> lines = entity.getTransportLines();
      Iterator<InvoiceTransportLine> iterator = lines.iterator();
      while(iterator.hasNext()) {
        InvoiceTransportLine line = iterator.next();
        if (StringUtils.isBlank(line.getInvoiceCode()))
          iterator.remove();
      }
    }
    return invoiceTransportService.save(entity, newOperateCxt());
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceTransportQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return "发票调拨单";
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceTransportService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return InvoiceTransports.OBJECT_NAME;
  }

}
