/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceStockController.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.stock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountAdj;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStockService;
import com.hd123.m3.account.service.ivc.stock.InvoiceStocks;
import com.hd123.m3.account.service.ivc.stock.StockState;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.ModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.commons.servlet.permed.DataPermedHelper;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 发票库存
 * 
 * @author wangyibo
 */
@Controller
@RequestMapping("account/invoice/stock/*")
public class InvoiceStockController extends ModuleController {

  private static final String RESOURCE_INVOICE_STOCK = "account.invoice.stock";

  @Autowired
  private InvoiceStockService invoiceStoreService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private BillTypeService billTypeService;

  @Autowired
  private DataPermedHelper dataPermedHelper;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_INVOICE_STOCK));
    return permissions;
  }

  @RequestMapping("query")
  public @ResponseBody SummaryQueryResult<InvoiceStock> query(@RequestBody QueryFilter filter) {
    if (filter == null) {
      return new SummaryQueryResult<InvoiceStock>();
    }

    FlecsQueryDefinition queryDef = getQueryBuilder().build(filter);
    String userId = getSessionUserId(); 
    queryDef.getConditions().addAll(dataPermedHelper.getPermConditions(InvoiceStocks.OBJECT_NAME, userId));
    QueryResult<InvoiceStock> queryResult = invoiceStoreService.query(queryDef);

    SummaryQueryResult result = SummaryQueryResult.newInstance(queryResult);
    buildSummary(result, filter);

    return result;
  }
  
  @RequestMapping("getInvoices")
  public @ResponseBody List<InvoiceStock> getInvoices (@RequestBody QueryFilter filter) {
    
    List<InvoiceStock> invoices = new ArrayList<InvoiceStock>();
    Collection<String> invoiceNumbers  = new ArrayList<String>();
    Object numbers = filter.getFilter().get("invoiceNumber");
    if(numbers instanceof Collection) {
       invoiceNumbers = (Collection<String>) numbers;
    } else if (numbers instanceof String) {
       invoiceNumbers.add((String)numbers);
    }
    
    invoices = invoiceStoreService.getsByInvoiceNumber(invoiceNumbers);
    
    return invoices;
  }
  

  /** 获取账目明细 */
  @RequestMapping("queryRegLine")
  public @ResponseBody List<InvoiceStockRegLine> queryRegLine(
      @RequestParam("invoiceNumber") String invoiceNumber) {
    if (StringUtil.isNullOrBlank(invoiceNumber)) {
      return null;
    }

    List<InvoiceStockRegLine> lines = new ArrayList<InvoiceStockRegLine>();

    QueryDefinition def = new QueryDefinition();
    def.addCondition(Accounts.CONDITION_INVOICE_NUMBER_IN, invoiceNumber);
    Map<String, InvoiceStockRegLine> regLines = new HashMap<String, InvoiceStockRegLine>();
    List<BillType> types = billTypeService.getAllTypes();
    List<Account> accounts = accountService.query(def).getRecords();

    for (Account acc : accounts) {
      if (regLines.get(acc.getAcc1().getId()) == null) {
        InvoiceStockRegLine line = AccountToInvoiceStockRegLineConverter.getInstance(types)
            .convert(acc);
        regLines.put(acc.getAcc1().getId(), line);
      } else {
        InvoiceStockRegLine line = regLines.get(acc.getAcc1().getId());
        line.setRegTotal(line.getRegTotal().add(acc.getTotal().getTotal()));
      }
    }

    List<String> invoiceNumbers = new ArrayList<String>();
    invoiceNumbers.add(invoiceNumber);

    List<AccountAdj> accountAdjs = accountService.getAdjusts(invoiceNumbers);
    for (AccountAdj adj : accountAdjs) {
      if (regLines.get(adj.getAcc1().getId()) == null) {
        InvoiceStockRegLine line = AccountAdjToInvoiceStockRegLineConverter.getInstance(types)
            .convert(adj);
        regLines.put(adj.getAcc1().getId(), line);
      } else {
        InvoiceStockRegLine line = regLines.get(adj.getAcc1().getId());
        line.setRegTotal(line.getRegTotal().add(adj.getTotal().getTotal()));
      }
    }
    if (regLines.isEmpty()) {
      return lines;
    }

    lines.addAll(regLines.values());
    return lines;
  }

  /** 状态数量 */
  private void buildSummary(SummaryQueryResult<InvoiceStock> result, QueryFilter filter) {

    filter.setPage(1);
    filter.getFilter().put("state", null);
    FlecsQueryDefinition definition = getQueryBuilder().build(filter);
    result.getSummary().put("all",
        definition == null ? 0 : invoiceStoreService.query(definition).getRecordCount());

    filter.getFilter().put("state", StockState.instock.name());
    definition = getQueryBuilder().build(filter);
    result.getSummary().put("instock",
        definition == null ? 0 : invoiceStoreService.query(definition).getRecordCount());

    filter.getFilter().put("state", StockState.received.name());
    definition = getQueryBuilder().build(filter);
    result.getSummary().put("received",
        definition == null ? 0 : invoiceStoreService.query(definition).getRecordCount());

    filter.getFilter().put("state", StockState.used.name());
    definition = getQueryBuilder().build(filter);
    result.getSummary().put("used",
        definition == null ? 0 : invoiceStoreService.query(definition).getRecordCount());

    filter.getFilter().put("state", StockState.aborted.name());
    definition = getQueryBuilder().build(filter);
    result.getSummary().put("aborted",
        definition == null ? 0 : invoiceStoreService.query(definition).getRecordCount());

    filter.getFilter().put("state", StockState.usedRecovered.name());
    definition = getQueryBuilder().build(filter);
    result.getSummary().put("usedRecovered",
        definition == null ? 0 : invoiceStoreService.query(definition).getRecordCount());

    filter.getFilter().put("state", StockState.abortedRecovered.name());
    definition = getQueryBuilder().build(filter);
    result.getSummary().put("abortedRecovered",
        definition == null ? 0 : invoiceStoreService.query(definition).getRecordCount());

  }

  private InvoiceStockQueryBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceStockQueryBuilder.class);
  }

}
