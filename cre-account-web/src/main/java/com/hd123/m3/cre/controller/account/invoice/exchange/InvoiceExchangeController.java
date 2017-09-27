/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceExchangeController.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月28日 - wangyibo - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.exchange;

import java.text.MessageFormat;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.commons.SourceBill;
import com.hd123.m3.account.service.acc.Acc2;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountAdj;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.account.service.ivc.exchange.ExchangeType;
import com.hd123.m3.account.service.ivc.exchange.InvToInvType;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchAccountAdjLine;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchBalanceLine;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchange;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchangeService;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchanges;
import com.hd123.m3.account.service.ivc.stock.InvoiceStock;
import com.hd123.m3.account.service.ivc.stock.InvoiceStockService;
import com.hd123.m3.account.service.ivc.stock.InvoiceStocks;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountCommonComponent;
import com.hd123.m3.cre.controller.account.common.util.BillTypeUtils;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchAccountLine;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.BInvoiceExchange;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.InvoiceExchAccountLine2;
import com.hd123.m3.cre.controller.account.invoice.exchange.biz.InvoiceExchangeAccountDetails;
import com.hd123.m3.cre.controller.account.invoice.exchange.converter.AccountAdjToAccountAdjLineConverter;
import com.hd123.m3.cre.controller.account.invoice.exchange.converter.AccountLinesPostProcessor;
import com.hd123.m3.cre.controller.account.invoice.exchange.converter.AccountToInvoiceExchAccountLine2Converter;
import com.hd123.m3.cre.controller.account.invoice.exchange.handle.BInvoiceExchangeHandler;
import com.hd123.m3.cre.controller.account.invoice.exchange.handle.InvoiceExchangeHandler;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.collection.CollectionUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * 发票交换单
 * 
 * @author wangyibo
 *
 */
@Controller
@RequestMapping("account/invoice/exchange/*")
public class InvoiceExchangeController extends BizFlowModuleController {

  private static final String RESOURCE_INVOICE_EXCHANGE = "account.invoice.exchange";

  @Autowired
  InvoiceExchangeService invoiceExchangeService;
  @Autowired
  AccountService accountService;
  @Autowired
  private AccountCommonComponent accCommonComponent;
  @Autowired
  private BillTypeService billTypeService;
  @Autowired
  private UserService userService;
  @Autowired
  private InvoiceStockService invocieStockService;
  
  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_INVOICE_EXCHANGE));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    UCN user = new UCN(userService.get(getSessionUser().getId(), User.PART_CONTACTS).getUuid(),
        getSessionUser().getId(), getSessionUser().getFullName());
    moduleContext.put("currentUser", user);
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody BInvoiceExchange load(@RequestParam("uuid") String uuid) {
    InvoiceExchange exchange = invoiceExchangeService.get(uuid);
    if(exchange == null) {
      return null;
    }
    List<BillType> types = billTypeService.getAllTypes();
    BInvoiceExchange bExchange = BInvoiceExchangeHandler.getInstance(types).handler(exchange);
    /**处理,添加发票库存对象*/
    
    BeforeLoad(bExchange);
    
    return bExchange;
  }
  
  private void BeforeLoad(BInvoiceExchange bExchange) {
    
    Collection<String> numbers = new ArrayList<String>();
    
    if(bExchange.getBexchBalanceLines().size() > 0) {
      for (BInvoiceExchBalanceLine line : bExchange.getBexchBalanceLines()) {
        if(!StringUtil.isNullOrBlank(line.getOldNumber())) {
          numbers.add(line.getOldNumber());
        }
        if(!StringUtil.isNullOrBlank(line.getBalanceNumber())) {
          numbers.add(line.getBalanceNumber());
        }
      } 
    }
    
    if(bExchange.getBexchAccountLines().size()>0) {
      for (BInvoiceExchAccountLine line : bExchange.getBexchAccountLines()) {
        if(!StringUtil.isNullOrBlank(line.getNewNumber())) {
          numbers.add(line.getNewNumber());
        }
      }
    }
    List<InvoiceStock> invocies = new ArrayList<InvoiceStock>();
    Map<String, InvoiceStock> invocieStocks = new HashMap<String, InvoiceStock>();
    if(numbers.size() > 0) {
      invocies = invocieStockService.getsByInvoiceNumber(numbers);
      for (InvoiceStock invoice : invocies) {
        invocieStocks.put(invoice.getInvoiceNumber(), invoice);
      }
      
      for (BInvoiceExchBalanceLine line : bExchange.getBexchBalanceLines()) {
        if(!StringUtil.isNullOrBlank(line.getOldNumber())) {
          line.setOldInvoice(invocieStocks.get(line.getOldNumber()));          
        }
        if(!StringUtil.isNullOrBlank(line.getBalanceNumber())) {
          line.setBalanceInvocie(invocieStocks.get(line.getBalanceNumber()));
        }
      }
      
      for (BInvoiceExchAccountLine line : bExchange.getBexchAccountLines()) {
        if(!StringUtil.isNullOrBlank(line.getNewNumber())) {
          line.setNewInvocie(invocieStocks.get(line.getNewNumber()));
        }
      }
      
    }
    
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (json == null)
      return null;

    BInvoiceExchange entity = JsonUtil.jsonToObject(json, BInvoiceExchange.class);
    
    // 过滤空行(如果原发票号码为null认为是空行)
    for (int index = entity.getBexchBalanceLines().size() - 1; index >= 0; index--) {
      if (StringUtil.isNullOrBlank(entity.getBexchBalanceLines().get(index).getOldNumber())) {
        entity.getBexchBalanceLines().remove(index);
      }
    }
    
    InvoiceExchangeHandler.getInstance().handerBalance(entity);
    beforerSave(entity);
    
    InvoiceExchangeHandler.getInstance().hander(entity);
    return invoiceExchangeService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  /**
   * 如果是红冲需要创新账款
   * 
   * @throws M3ServiceException
   */
  private void beforerSave(BInvoiceExchange entity) throws M3ServiceException {
    
    // 如果是红冲需要刷新账款
    if (ExchangeType.invToInv.equals(entity.getType())
        && InvToInvType.balance.equals(entity.getInvToInvType())) {
      List<String> numbers = getInvoiceNumbers(entity.getExchBalanceLines());
      InvoiceExchangeAccountDetails details = getAccountLinesByInvoceNumbers(numbers,
          entity.getUuid());
      entity.setAccountAdjLines(details.getAccountAdjLines());
      entity.setExchAccountLines2(details.getExchAccountLines2());
    } else {// 只做检查,目的是避免界面检查后没做修改直接保存.
      List<String> numbers = getInvoiceNumbers(entity.getExchBalanceLines());
      getAccountLinesByInvoceNumbers(numbers, entity.getUuid());
    }

    // 检查
    List<InvoiceExchBalanceLine> lines = entity.getExchBalanceLines();

    for (int i = 0; i < lines.size(); i++) {
      for (int j = i + 1; j < lines.size(); j++) {
        if (lines.get(i).getOldNumber().equals(lines.get(j).getOldNumber())) {
          String msg = R.R.oldReceiptRepeat();
          if (ExchangeType.invToInv.equals(entity.getType())) {
            msg = R.R.oldInvoiceRepeat();
          }
          throw new M3ServiceException(msg);
        }
        if (ExchangeType.invToInv.equals(entity.getType()) == false) {
          continue;
        }

        if (lines.get(i).getBalanceNumber().equals(lines.get(j).getBalanceNumber())) {
          throw new M3ServiceException(R.R.balanceNumberRepeat());
        }
      }
    }
  }

  private List<String> getInvoiceNumbers(List<InvoiceExchBalanceLine> exchBalanceLines) {
    List<String> numbers = new ArrayList<String>();
    for (InvoiceExchBalanceLine line : exchBalanceLines) {
      numbers.add(line.getOldNumber());
    }
    return numbers;
  }

  @RequestMapping("getLines")
  public @ResponseBody InvoiceExchangeAccountDetails getLines(@RequestBody QueryFilter queryFilter)
      throws M3ServiceException {
    // TODO
    List<String> numbers = new ArrayList<String>();
    List<String> invoiceNumbers = new ArrayList<String>();
    String billUuid = "";

    numbers = (List<String>) queryFilter.getFilter().get("numbers");
    if (queryFilter.getFilter().get("uuid") != null) {
      billUuid = (String) queryFilter.getFilter().get("uuid");
    } else {
      billUuid = null;
    }
    // 过滤空行(如果原发票号码为null认为是空行)
    for (String number : numbers) {
      if (!StringUtil.isNullOrBlank(number)) {
        invoiceNumbers.add(number);
      }
    }

    return getAccountLinesByInvoceNumbers(invoiceNumbers, billUuid);
  }

  public InvoiceExchangeAccountDetails getAccountLinesByInvoceNumbers(List<String> invoiceNumbers,
      String billUuid) throws M3ServiceException {
    InvoiceExchangeAccountDetails details = new InvoiceExchangeAccountDetails();

    // @RequestBody QueryFilter queryFilter

    if (invoiceNumbers == null || invoiceNumbers.isEmpty()) {
      return details;
    }

    /** 查询账款 */
    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_INVOICE_NUMBER_IN, invoiceNumbers.toArray());
    QueryResult<Account> result = accountService.query(queryDef);
    // 账款所有类型
    List<BillType> types = billTypeService.getAllTypes();

    List<InvoiceExchAccountLine2> records = ArrayListConverter.newConverter(
        AccountToInvoiceExchAccountLine2Converter.getInstance()).convert(result.getRecords());
    details.setExchAccountLines2(records);

    checkAccounts(details.getExchAccountLines2(), invoiceNumbers, billUuid);
    // 检查后将账款的锁置为“-”
    for (InvoiceExchAccountLine2 line : details.getExchAccountLines2()) {
      line.getAcc2().setLocker(new SourceBill("-", "-", "-"));
      line.getAcc2().setId(toId(line.getAcc2()));
    }

    // 查询adj
    List<AccountAdj> adjs = accountService.getAdjusts(invoiceNumbers);
    AccountAdjToAccountAdjLineConverter adjConverter = AccountAdjToAccountAdjLineConverter
        .getInstance();
    List<InvoiceExchAccountAdjLine> adjLines = ArrayListConverter.newConverter(adjConverter)
        .convert(adjs);
    if (adjLines.isEmpty() == false) {
      details.setAccountAdjLines(adjLines);
    }

    List<BInvoiceExchAccountLine> lines = AccountLinesPostProcessor.postProcessAccountLines(
        details.getExchAccountLines2(), details.getAccountAdjLines());

    for (BInvoiceExchAccountLine bLine : lines) {
      if (bLine.getSourceBillType() != null) {
        bLine.setSourceBillCaption(getBillCaption(bLine, types));
      }
    }

    /** 保持顺序 */
    List<BInvoiceExchAccountLine> sortLines = new ArrayList<BInvoiceExchAccountLine>();
    for (String number : invoiceNumbers) {
      for (BInvoiceExchAccountLine line : lines) {
        if (number.equals(line.getOldNumber())) {
          sortLines.add(line);
        }
      }
    }
    details.setExchAccountLines(sortLines);
    return details;
  }

  /**
   * 检查账款明细是否合法。
   * <p>
   * 检查通过规则：
   * <p>
   * 1、账款只能是未锁定或者是被当前单据锁定。
   * <p>
   * 2、每个发票号码至少对应一个账款
   * 
   * @param accounts
   *          账款明细列表，禁止为null。
   * @param invoiceNumbers
   *          发票号码列表,禁止为null。
   * @param billUuid
   *          锁定单据uuid,可以为null。
   * @throws M3ServiceException
   * @throws ClientBizException
   *           检查不通过时抛出。
   */
  private void checkAccounts(List<InvoiceExchAccountLine2> accounts, List<String> invoiceNumbers,
      String billUuid) throws M3ServiceException {
    /** 检查账款的锁定是否符合要求 */
    for (InvoiceExchAccountLine2 line : accounts) {
      if ("-".equals(line.getAcc2().getLocker().getBillUuid())) {
        continue;
      } else if (line.getAcc2().getLocker().getBillUuid().equals(billUuid)) {
        continue;
      } else {
        String billType = R.R.bill();
        try {
          BillType type = BillTypeUtils.getBillTypeByName(line.getAcc2().getLocker().getBillType());
          billType = type.getCaption();
        } catch (Exception e) {
          // Do Nothing
        }
        throw new M3ServiceException(MessageFormat.format(R.R.accountLockedBy(), billType, line
            .getAcc2().getLocker().getBillNumber()));
      }
    }

    /** 检查发票号与账款的对应关系 */
    for (String invoiceNumber : invoiceNumbers) {
      boolean exist = false;
      for (InvoiceExchAccountLine2 line : accounts) {
        if (invoiceNumber.equals(line.getAcc2().getInvoice().getInvoiceNumber())) {
          exist = true;
          break;
        }
      }

      if (exist == false) {
        throw new M3ServiceException(MessageFormat.format(R.R.invoiceHasNoAccount(), invoiceNumber));
      }
    }
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceExchangeQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return R.R.billCaption();
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceExchangeService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return InvoiceExchanges.OBJECT_NAME;
  }

  public static interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("发票交换单")
    String billCaption();

    @DefaultStringValue("账款数据不正确，存在没有账款的发票。")
    String accountDataError();

    @DefaultStringValue("发票{0}对应的账款不存在。")
    String invoiceHasNoAccount();

    @DefaultStringValue("账款已被{0}{1}锁定")
    String accountLockedBy();

    @DefaultStringValue("单据")
    String bill();

    @DefaultStringValue("原收据号重复。")
    String oldReceiptRepeat();

    @DefaultStringValue("原发票号码重复。")
    String oldInvoiceRepeat();

    @DefaultStringValue("红冲发票号码重复。")
    String balanceNumberRepeat();

  }

  public String toId(Acc2 acc2) {
    ArrayList<String> list = new ArrayList<String>();
    list.add(acc2.getStatement() == null ? "-" : acc2.getStatement().id());
    list.add(acc2.getInvoice() == null ? "-" : acc2.getInvoice().id());
    list.add(acc2.getPayment() == null ? "-" : acc2.getPayment().id());
    list.add(acc2.getLocker() == null ? "-" : acc2.getLocker().id());
    return CollectionUtil.toString(list);
  }

  /** 账单类型 */
  private String getBillCaption(BInvoiceExchAccountLine source, List<BillType> billTypes) {
    if (source.getAcc1() == null || source.getAcc1().getSourceBill() == null) {
      return null;
    }

    if (billTypes == null) {
      return source.getAcc1().getSourceBill().getBillType();
    }

    for (BillType type : billTypes) {
      if (source.getAcc1().getSourceBill().getBillType().equals(type.getName())) {
        return type.getCaption();
      }
    }

    return source.getAcc1().getSourceBill().getBillType();
  }
  
  @RequestMapping("validateSave")
  public @ResponseBody QueryResult<Account> validateSave(@RequestBody QueryFilter queryFilter) {
    /** 查询账款 */
    QueryDefinition queryDef = new QueryDefinition();
    List<String> invoiceNumbers = (List<String>) queryFilter.getFilter().get("invoiceNumber");
    queryDef.addCondition(Accounts.CONDITION_INVOICE_NUMBER_IN, invoiceNumbers.toArray());
    QueryResult<Account> result = accountService.query(queryDef);
    
    return result;
  }
  
  @RequestMapping("queryStock")
  public @ResponseBody QueryResult<InvoiceStock> queryStock(@RequestBody QueryFilter queryFilter) {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    Map<String, Object> filter = queryFilter.getFilter();
    queryDef.addCondition(InvoiceStocks.CONDITION_INVOICESTATE_EQUAL,filter.get("states"));
    queryDef.addFlecsCondition(InvoiceStocks.FIELD_INVOICENUMBER,
        InvoiceStocks.OPERATOR_EQUALS, filter.get("invoiceNumber"));
    
    QueryResult<InvoiceStock> result = invocieStockService.query(queryDef);
    return result;
  }

}
