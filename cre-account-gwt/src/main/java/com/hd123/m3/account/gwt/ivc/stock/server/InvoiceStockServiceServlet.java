/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.ivc.stock.client.EPInvoiceStock;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStockRegLine;
import com.hd123.m3.account.gwt.ivc.stock.client.rpc.InvoiceStockService;
import com.hd123.m3.account.gwt.ivc.stock.server.converter.AccountAdjToBInvoiceStockRegLineConverter;
import com.hd123.m3.account.gwt.ivc.stock.server.converter.AccountToBInvoiceStockRegLineConverter;
import com.hd123.m3.account.gwt.ivc.stock.server.converter.InvoiceStockBizConverter;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountAdj;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStocks;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.bpm.server.M3BpmService2Servlet;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.server.RPageDataConverter;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 发票库存|客户端服务。
 * 
 * @author lixiaohong
 * @since 1.7
 */
public class InvoiceStockServiceServlet extends M3BpmService2Servlet<BInvoiceStock> implements
    InvoiceStockService {
  private static final long serialVersionUID = -3384100889689279912L;

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    moduleContext.put(INVOICE_TYPE, CollectionUtil.toString(getInvoiceTypes()));
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    Set<BPermission> permissions = new HashSet<BPermission>();
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getId(),
        Arrays.asList(EPInvoiceStock.RESOURCE_KEY)));
    return permissions;
  }

  public Map<String, String> getInvoiceTypes() {
    Map mapInvoiceTypes = new HashMap<String, String>();
    try {
      for (BInvoiceType type : InvoiceTypeUtils.getAll()) {
        mapInvoiceTypes.put(type.getCode(), type.getName());
      }
      return mapInvoiceTypes;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  protected String doSave(BInvoiceStock entity) throws Exception {
    return null;
  }

  @Override
  protected BInvoiceStock doGet(String id) throws Exception {
    return null;
  }

  @Override
  protected String getServiceBeanId() {
    return null;
  }

  @Override
  protected RPageData<BInvoiceStock> doQuery(FlecsQueryDef definition) throws Exception {
    try {
      
      FlecsQueryDefinition queryDef = buildQueryDefinition(definition,
          InvoiceStockQueryBuilder.getInstance());
      QueryResult<InvoiceStock> qr = getInvoiceStockService().query(queryDef);
      return RPageDataConverter.convert(qr, InvoiceStockBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public List<BInvoiceStockRegLine> getRegLinesByNumber(String invoiceNumber) throws Exception {
    List<BInvoiceStockRegLine> lines = new ArrayList<BInvoiceStockRegLine>();
    if (StringUtil.isNullOrBlank(invoiceNumber)) {
      return lines;
    }

    QueryDefinition def = new QueryDefinition();
    def.addCondition(Accounts.CONDITION_INVOICE_NUMBER_IN, invoiceNumber);

    Map<String, BInvoiceStockRegLine> regLines = new HashMap<String, BInvoiceStockRegLine>();
    List<BillType> types = getBillTypeService().getAllTypes();
    List<Account> accounts = getAccountService().query(def).getRecords();
    for (Account acc : accounts) {
      if (regLines.get(acc.getAcc1().getId()) == null) {
        BInvoiceStockRegLine line = AccountToBInvoiceStockRegLineConverter.getInstance(types)
            .convert(acc);
        regLines.put(acc.getAcc1().getId(), line);
      } else {
        BInvoiceStockRegLine line = regLines.get(acc.getAcc1().getId());
        line.setRegTotal(line.getRegTotal().add(acc.getTotal().getTotal()));
      }
    }

    List<String> invoiceNumbers = new ArrayList<String>();
    invoiceNumbers.add(invoiceNumber);

    List<AccountAdj> accountAdjs = getAccountService().getAdjusts(invoiceNumbers);
    for (AccountAdj adj : accountAdjs) {
      if (regLines.get(adj.getAcc1().getId()) == null) {
        BInvoiceStockRegLine line = AccountAdjToBInvoiceStockRegLineConverter.getInstance(types)
            .convert(adj);
        regLines.put(adj.getAcc1().getId(), line);
      } else {
        BInvoiceStockRegLine line = regLines.get(adj.getAcc1().getId());
        line.setRegTotal(line.getRegTotal().add(adj.getTotal().getTotal()));
      }
    }
    
    if(regLines.isEmpty()){
      return lines;
    }
    
    lines.addAll(regLines.values());
    
    return lines;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.billCaption();
  }

  @Override
  public String getObjectName() {
    return InvoiceStocks.OBJECT_NAME;
  }

  private com.hd123.m3.account.service.ivc.stock.InvoiceStockService getInvoiceStockService() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.ivc.stock.InvoiceStockService.class);
  }

  private AccountService getAccountService() {
    return getAppCtx().getBean(AccountService.DEFAULT_CONTEXT_ID, AccountService.class);
  }

  private BillTypeService getBillTypeService() {
    return getAppCtx().getBean(BillTypeService.class);
  }

  public static interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("发票库存")
    String billCaption();
  }
}
