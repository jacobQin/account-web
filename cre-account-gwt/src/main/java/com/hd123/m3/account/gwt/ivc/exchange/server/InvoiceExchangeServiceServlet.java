/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.server;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.AccSessions;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvToInvType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine2;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountAdjLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountDetails;
import com.hd123.m3.account.gwt.ivc.exchange.server.converter.AccountAdjToBAccountAdjLineConverter;
import com.hd123.m3.account.gwt.ivc.exchange.server.converter.AccountLinesPostProcessor;
import com.hd123.m3.account.gwt.ivc.exchange.server.converter.AccountToBInvoiceExchAccountLine2Converter;
import com.hd123.m3.account.gwt.ivc.exchange.server.converter.BInvoiceExchangeConverter;
import com.hd123.m3.account.gwt.ivc.exchange.server.converter.InvoiceExchangeConverter;
import com.hd123.m3.account.gwt.util.server.BillTypeUtils;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountAdj;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchange;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchangeService;
import com.hd123.m3.account.service.ivc.exchange.InvoiceExchanges;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.gwt.bpm.server.M3BpmService2Servlet;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.server.RPageDataConverter;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 发票交换GWT服务|实现
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeServiceServlet extends M3BpmService2Servlet<BInvoiceExchange> implements
    com.hd123.m3.account.gwt.ivc.exchange.client.rpc.InvoiceExchangeService {

  private static final long serialVersionUID = -5537855181868299433L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    Set<BPermission> permissions = new HashSet<BPermission>();
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getId(),
        Arrays.asList(EPInvoiceExchange.RESOURCE_KEY)));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    moduleContext.put(KEY_CURRENT_USER, encodeCurrentUser());
    Map<String, String> map = getOptionService().getCounterpartType();
    if (map != null && !map.isEmpty()) {
      moduleContext.put(AccSessions.KEY_COUNTERPART_TYPE, CollectionUtil.toString(map));
    }
  }

  @Override
  protected String doSave(BInvoiceExchange entity) throws Exception {
    Assert.assertAttributeNotNull(entity, "entity");

    beforeSave(entity);

    InvoiceExchange exchange = InvoiceExchangeConverter.getInstance().convert(entity);
    return getInvoiceExchangeService().save(exchange,
        ConvertHelper.getOperateContext(getSessionUser()));
  }

  /** 如果是红冲需要刷新账款 */
  private void beforeSave(BInvoiceExchange entity) throws ClientBizException {
    // 过滤空行(如果原发票号码为null认为是空行)
    for (int index = entity.getExchBalanceLines().size() - 1; index >= 0; index--) {
      if (StringUtil.isNullOrBlank(entity.getExchBalanceLines().get(index).getOldNumber())) {
        entity.getExchBalanceLines().remove(index);
      }
    }

    // 如果是红冲需要刷新账款
    if (BExchangeType.invToInv.equals(entity.getType())
        && BInvToInvType.balance.equals(entity.getInvToInvType())) {
      List<String> numbers = getInvoiceNumbers(entity.getExchBalanceLines());
      BInvoiceExchangeAccountDetails details = getAccountLinesByInvoceNumbers(numbers,
          entity.getUuid());
      entity.setAccountAdjLines(details.getAccountAdjLines());
      entity.setExchAccountLines2(details.getExchAccountLines2());
    } else { // 只做检查,目的是避免界面检查后没做修改直接保存.
      List<String> numbers = getInvoiceNumbers(entity.getExchBalanceLines());
      getAccountLinesByInvoceNumbers(numbers, entity.getUuid());
    }
    
    //检查
    List<BInvoiceExchBalanceLine> lines = entity.getExchBalanceLines();
    
    for(int i=0;i<lines.size();i++){
      for(int j=i+1;j<lines.size();j++){
        if(lines.get(i).getOldNumber().equals(lines.get(j).getOldNumber())){
          String msg = R.R.oldReceiptRepeat();
          if(BExchangeType.invToInv.equals(entity.getType())){
            msg = R.R.oldInvoiceRepeat();
          }
          throw new ClientBizException(msg);
        }
        
        if(BExchangeType.invToInv.equals(entity.getType()) == false){
          continue;
        }
        
        if(lines.get(i).getBalanceNumber().equals(lines.get(j).getBalanceNumber())){
          throw new ClientBizException(R.R.balanceNumberRepeat());
        }
      }
    }

  }

  private List<String> getInvoiceNumbers(List<BInvoiceExchBalanceLine> exchBalanceLines) {
    List<String> numbers = new ArrayList<String>();
    for (BInvoiceExchBalanceLine line : exchBalanceLines) {
      numbers.add(line.getOldNumber());
    }
    return numbers;
  }

  @Override
  protected BInvoiceExchange doGet(String id) throws Exception {
    InvoiceExchange exchange = getInvoiceExchangeService().get(id);
    return BInvoiceExchangeConverter.getInstance().convert(exchange);
  }

  @Override
  protected RPageData<BInvoiceExchange> doQuery(FlecsQueryDef definition) throws Exception {
    try {
      
      FlecsQueryDefinition queryDef = buildQueryDefinition(definition,
          InvoiceExchangeQueryBuilder.getInstance());
      QueryResult<InvoiceExchange> qr = getInvoiceExchangeService().query(queryDef);
      return RPageDataConverter.convert(qr, BInvoiceExchangeConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected String getObjectCaption() {
    return R.R.billCaption();
  }

  @Override
  public String getObjectName() {
    return InvoiceExchanges.OBJECT_NAME;
  }

  @Override
  protected void doChangeBizState(String uuid, long version, String state) throws Exception {
    getInvoiceExchangeService().changeBizState(uuid, version, state,
        ConvertHelper.getOperateContext(getSessionUser()));
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceExchangeService.DEFAULT_CONTEXT_ID;
  }

  @Override
  public List<BBillType> getAllTypes() {
    try {
      return BillTypeUtils.getAll();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public BInvoiceExchangeAccountDetails getAccountLinesByInvoceNumbers(List<String> invoiceNumbers,
      String billUuid) throws ClientBizException {

    BInvoiceExchangeAccountDetails details = new BInvoiceExchangeAccountDetails();

    if (invoiceNumbers == null || invoiceNumbers.isEmpty()) {
      return details;
    }

    /** 查询账款 */
    QueryDefinition def = new QueryDefinition();
    def.addCondition(Accounts.CONDITION_INVOICE_NUMBER_IN, invoiceNumbers.toArray());
    QueryResult<Account> result = getAccountService().query(def);

    List<BInvoiceExchAccountLine2> records = ArrayListConverter.newConverter(
        AccountToBInvoiceExchAccountLine2Converter.getInstance()).convert(result.getRecords());
    details.setExchAccountLines2(records);

    checkAccounts(details.getExchAccountLines2(), invoiceNumbers, billUuid);
    // 检查后将账款的锁置为“-”
    for (BInvoiceExchAccountLine2 line : details.getExchAccountLines2()) {
      line.getAcc2().setLocker(BAccount.NONE_LOCKER);
      line.getAcc2().setId(line.getAcc2().toId());
    }

    // 查询adj
    List<AccountAdj> adjs = getAccountService().getAdjusts(invoiceNumbers);
    AccountAdjToBAccountAdjLineConverter adjConverter = AccountAdjToBAccountAdjLineConverter
        .getInstance();
    List<BInvoiceExchangeAccountAdjLine> adjLines = ArrayListConverter.newConverter(adjConverter)
        .convert(adjs);
    if (adjLines.isEmpty() == false) {
      details.setAccountAdjLines(adjLines);
    }

    List<BInvoiceExchAccountLine> lines = AccountLinesPostProcessor.postProcessAccountLines(
        details.getExchAccountLines2(), details.getAccountAdjLines());
    
    /**保持顺序*/
    List<BInvoiceExchAccountLine> sortLines = new ArrayList<BInvoiceExchAccountLine>();
    for(String number:invoiceNumbers){
      for(BInvoiceExchAccountLine line:lines){
        if(number.equals(line.getOldNumber())){
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
   * @throws ClientBizException
   *           检查不通过时抛出。
   */
  private void checkAccounts(List<BInvoiceExchAccountLine2> accounts, List<String> invoiceNumbers,
      String billUuid) throws ClientBizException {
    /** 检查账款的锁定是否符合要求 */
    for (BInvoiceExchAccountLine2 line : accounts) {
      if (BBill.NONE_ID.equals(line.getAcc2().getLocker().getBillUuid())) {
        continue;
      } else if (line.getAcc2().getLocker().getBillUuid().equals(billUuid)) {
        continue;
      } else {
        String billType = R.R.bill();
        try {
          BBillType type = BillTypeUtils
              .getBillTypeByName(line.getAcc2().getLocker().getBillType());
          billType = type.getCaption();
        } catch (Exception e) {
          // Do Nothing
        }
        throw new ClientBizException(MessageFormat.format(R.R.accountLockedBy(), billType, line
            .getAcc2().getLocker().getBillNumber()));
      }

    }

    /** 检查发票号与账款的对应关系 */
    for (String invoiceNumber : invoiceNumbers) {
      boolean exist = false;
      for (BInvoiceExchAccountLine2 line : accounts) {
        if (invoiceNumber.equals(line.getAcc2().getInvoice().getInvoiceNumber())) {
          exist = true;
          break;
        }
      }

      if (exist == false) {
        throw new ClientBizException(MessageFormat.format(R.R.invoiceHasNoAccount(), invoiceNumber));
      }
    }

  }

  private String encodeCurrentUser() {
    Operator operator = getSessionUser();
    if (operator == null) {
      return null;
    }
    User user = getUserService().get(operator.getId());
    return JsonUtil.objectToJson(new UCN(user.getUuid(), user.getLoginName(), operator
        .getFullName()));
  }

  private InvoiceExchangeService getInvoiceExchangeService() {
    InvoiceExchangeService service = M3ServiceFactory.getService(InvoiceExchangeService.class);
    Assert.assertArgumentNotNull(service, "service");
    return service;
  }

  private AccountService getAccountService() {
    AccountService service = M3ServiceFactory.getService(AccountService.class);
    Assert.assertArgumentNotNull(service, "service");
    return service;
  }
  
  protected AccountOptionService getOptionService() {
     return  M3ServiceFactory.getService(AccountOptionService.class);
  }

  private UserService getUserService() {
    return M3ServiceFactory.getBean(UserService.DEFAULT_CONTEXT_ID, UserService.class);
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

}
