/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceRecycleController.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月5日 - xiahongjian - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.recycle;

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
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycle;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleLine;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleService;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycles;
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

/**
 * 发票回收单
 * 
 * @author xiahongjian
 *
 */
@Controller
@RequestMapping("account/invoice/recycle/*")
public class InvoiceRecycleController extends BizFlowModuleController {
  private static final String RESOURCE_INVOICE_RECYCLE = "account.invoice.recycle";

  @Autowired
  private InvoiceRecycleService invoiceRecycleService;
  @Autowired
  private UserService userService;
  @Autowired
  private InvoiceStockService invoiceStockService;

  @ResponseBody
  @RequestMapping(value = "load", method = RequestMethod.GET)
  public BInvoiceRecycle load(@RequestParam("uuid") String uuid) {
    BInvoiceRecycle entity;
    InvoiceRecycle source = invoiceRecycleService.get(uuid);
    if (source == null)
      return null;

    entity = BInvoiceRecycle.newInstance(source);
    Collection<String> numbers = new ArrayList<String>();
    for (BInvoiceLine line : entity.getLines())
      numbers.add(line.getStartNumber());
    List<InvoiceStock> stocks = invoiceStockService.getsByInvoiceNumber(numbers);
    Map<String, InvoiceStock> invoiceMap = new HashMap<String, InvoiceStock>();
    for (InvoiceStock stock : stocks)
      invoiceMap.put(stock.getInvoiceNumber(), stock);
    for (BInvoiceLine line : entity.getLines())
      line.setStockNumber(invoiceMap.get(line.getStartNumber()));
    return entity;
  }

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_INVOICE_RECYCLE));
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
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtils.isBlank(json))
      return null;

    InvoiceRecycle entity = JsonUtil.jsonToObject(json, InvoiceRecycle.class);
    if (entity != null && entity.getLines() != null && !entity.getLines().isEmpty()) {
      List<InvoiceRecycleLine> lines = entity.getLines();
      Iterator<InvoiceRecycleLine> iterator = lines.iterator();
      while (iterator.hasNext()) {
        InvoiceRecycleLine line = iterator.next();
        if (StringUtils.isBlank(line.getInvoiceCode()))
          iterator.remove();
      }
    }
    return invoiceRecycleService.save(entity, newOperateCxt());
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceRecycleQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return "发票回收单";
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceRecycleService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return InvoiceRecycles.OBJECT_NAME;
  }

  private BeanOperateContext newOperateCxt() {
    return new BeanOperateContext(getSessionUser());
  }
}
