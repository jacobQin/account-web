/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiveServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-4 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.ClientException;

import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.commons.server.DefaultOptionLoader;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.server.converter.BizPaymentConverter;
import com.hd123.m3.account.gwt.payment.commons.server.converter.PaymentBizConverter;
import com.hd123.m3.account.gwt.payment.pay.client.biz.BPaymentConfig;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentService;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.invoice.InvoiceRegs;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author subinzhu
 * 
 */
public class PaymentServiceServlet extends AccBPMServiceServlet implements PaymentService {

  private static final long serialVersionUID = 2336269374956451286L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        PaymentPermDef.RESOURCE_PAYMENT_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    try {
      moduleContext.put(SCALE, getScale());
      moduleContext.put(ROUNDING_MODE, getRoundingMode());
      moduleContext.put(KEY_COOPMODES, CollectionUtil.toString(getAllEnabledCoopModes()));
      moduleContext.put(KEY_SHOW_TAX,
          getOptionService().getOption(Payments.OPTION_PATH, Payments.KEY_PAY_SHOWTAX));
      moduleContext.put(KEY_USE_INVOICE_STOCK,
          getOptionService().getOption(Payments.OPTION_PATH, Payments.KEY_PAY_USEINVOICESTOCK));

      moduleContext.put(
          KEY_PAY_TAX_DIFF_HI,
          getAccountOptionService().getOption(InvoiceRegs.OPTION_PATH,
              InvoiceRegs.KEY_PAY_TAX_DIFF_HI));
      moduleContext.put(
          KEY_PAY_TAX_DIFF_LO,
          getAccountOptionService().getOption(InvoiceRegs.OPTION_PATH,
              InvoiceRegs.KEY_PAY_TAX_DIFF_LO));
      moduleContext.put(
          KEY_PAY_TOTAL_DIFF_HI,
          getAccountOptionService().getOption(InvoiceRegs.OPTION_PATH,
              InvoiceRegs.KEY_PAY_TOTAL_DIFF_HI));
      moduleContext.put(
          KEY_PAY_TOTAL_DIFF_LO,
          getAccountOptionService().getOption(InvoiceRegs.OPTION_PATH,
              InvoiceRegs.KEY_PAY_TOTAL_DIFF_LO));

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public RPageData<BPayment> query(List<Condition> conditions, PageSort pageSort) throws Exception {
    try {

      FlecsQueryDefinition def = buildQueryDefinition(conditions, pageSort);
      if (def == null)
        return new RPageData<BPayment>();

      QueryResult<Payment> queryResult = getPaymentService().query(def);

      RPageData<BPayment> pd = new RPageData<BPayment>();
      RPageDataUtil.copyPagingInfo(queryResult, pd);
      List<Payment> payments = queryResult.getRecords();
      pd.setValues(ConverterUtil.convert(payments, new PaymentBizConverter(false)));
      // 更新导航数据
      updateNaviEntities(getObjectName(), pd.getValues());

      return pd;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment create() throws Exception {
    try {
      BPayment payment = new BPayment();
      payment.setDirection(DirectionType.payment.getDirectionValue());
      payment.setBizState(BizStates.INEFFECT);
      payment.setDefrayalType(PaymentDefrayalType.bill.name());
      payment.setDepositSubject(getDefaultSubject());
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createByStatements(Collection<String> statementUuids) throws Exception {
    try {

      Payment payment = getPaymentService().createByStatements(statementUuids,
          DirectionType.payment.getDirectionValue());

      BPayment biz = new PaymentBizConverter(true).convert(payment);
      decorateAccounts(biz);
      biz.setDepositSubject(getDefaultSubject());
      return biz;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createByInvoices(Collection<String> invoiceUuids) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService()
          .createByInvoices(invoiceUuids, DirectionType.payment.getDirectionValue()));
      decorateAccounts(payment);
      payment.setDepositSubject(getDefaultSubject());

      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createByPaymentNotices(Collection<String> noticeUuids) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService().createByNotices(
          noticeUuids, DirectionType.payment.getDirectionValue()));
      decorateAccounts(payment);
      payment.setDepositSubject(getDefaultSubject());
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createBySourceBills(Collection<String> sourceBillUuids) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService()
          .createBySourceBills(sourceBillUuids, DirectionType.payment.getDirectionValue()));
      decorateAccounts(payment);
      payment.setDepositSubject(getDefaultSubject());

      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createByAccounts(Collection<String> accountUuids) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService()
          .createByAccounts(accountUuids, DirectionType.payment.getDirectionValue()));
      decorateAccounts(payment);
      payment.setDepositSubject(getDefaultSubject());
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void decorateAccounts(BPayment payment) {
    negateLineTotal(payment);
  }

  /** 收款选择方向为收的账款时，金额需要取反 */
  private void negateLineTotal(BPayment payment) {
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      if (DirectionType.receipt.getDirectionValue() == line.getAcc1().getDirection()) {
        line.getOriginTotal().negate();
        line.getTotal().negate();
        line.getUnpayedTotal().negate();
      }
    }
    payment.aggregate(Integer.valueOf(getScale()), RoundingMode.valueOf(getRoundingMode()));
  }

  @Override
  public BPayment save(BPayment bill, BProcessContext task) throws Exception {
    try {

      // 先判断流程权限
      doBeforeExecute(bill, task);

      // 去除账款应收金额为0的明细行
      for (int i = bill.getAccountLines().size() - 1; i >= 0; i--) {
        BPaymentAccountLine accountLine = bill.getAccountLines().get(i);
        if (accountLine.getTotal() == null || accountLine.getTotal().getTotal() == null
            || accountLine.getTotal().getTotal().compareTo(BigDecimal.ZERO) == 0) {
          bill.getAccountLines().remove(i);
        }
      }

      // 去除滞纳金金额为0明细行
      for (int i = bill.getOverdueLines().size() - 1; i >= 0; i--) {
        BPaymentOverdueLine accountLine = bill.getOverdueLines().get(i);
        if (accountLine.getTotal() == null || accountLine.getTotal().getTotal() == null
            || accountLine.getTotal().getTotal().compareTo(BigDecimal.ZERO) == 0) {
          bill.getOverdueLines().remove(i);
        }
      }

      Payment target = BizPaymentConverter.getInstance().convert(bill);
      String uuid = getPaymentService().save(target,
          ConvertHelper.getOperateContext(getSessionUser()));

      target = getPaymentService().get(uuid);

      doAfterSave(target, task);

      return new PaymentBizConverter().convert(target);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void delete(String uuid, long oca) throws Exception {
    try {

      getPaymentService().remove(uuid, oca, ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void abort(String uuid, String comment, long oca) throws Exception {
    try {

      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(Payments.PROPERTY_MESSAGE, comment);

      getPaymentService().abort(uuid, oca, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void effect(String uuid, String comment, long oca) throws Exception {
    try {

      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(Payments.PROPERTY_MESSAGE, comment);

      getPaymentService().effect(uuid, oca, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String executeTask(BOperation operation, BPayment entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");

      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction()) == false
          && saveBeforeAction) {
        String uuid = getPaymentService().save(new BizPaymentConverter().convert(entity),
            ConvertHelper.getOperateContext(getSessionUser()));
        Payment result = getPaymentService().get(uuid);
        doExecuteTask(result, operation, task);
        return result.getUuid();
      } else {
        doExecuteTask(operation, task);
        return entity == null ? null : entity.getUuid();
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BTotal fetchReceiptTotal(String accountUnitUuid, String counterpartUuid)
      throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(accountUnitUuid, "accountUnitUuid");
      Assert.assertArgumentNotNull(counterpartUuid, "counterpartUuid");

      Total receiptTotal = getAccountService().getActiveTotal(accountUnitUuid, counterpartUuid,
          Direction.RECEIPT);

      return TotalBizConverter.getInstance().convert(receiptTotal);
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BPayment load(String uuid, boolean isForEdit) throws Exception {
    try {

      Payment payment = null;
      if (isForEdit) {
        payment = getPaymentService().getByUuid(uuid, Payment.PART_LINE, Payment.PART_DEFRAYAL,
            Payment.PART_ACCOVERDUE, Payment.PART_LINEDEFRAYAL, Payment.PART_OVERDUETERM);
      } else {
        payment = getPaymentService().getByUuid(uuid, Payment.PART_WHOLE);
      }

      if (payment == null)
        return null;
      if (!ObjectUtil.equals(payment.getDirection(), Direction.PAYMENT))
        return null;

      BPayment result = new PaymentBizConverter(true).convert(payment);

      // 验证授权组
      validEntityPerm(result);

      if (BizStates.INEFFECT.equals(payment.getBizState()))
        loadRemainTotal(result);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment loadByNumber(String billNumber, boolean isForEdit) throws Exception {
    try {

      Payment payment = null;
      if (isForEdit) {
        payment = getPaymentService().getByNumber(billNumber, Payment.PART_LINE,
            Payment.PART_DEFRAYAL, Payment.PART_ACCOVERDUE, Payment.PART_LINEDEFRAYAL,
            Payment.PART_OVERDUETERM);
      } else {
        payment = getPaymentService().getByNumber(billNumber, Payment.PART_WHOLE);
      }

      if (payment == null)
        return null;
      if (!ObjectUtil.equals(payment.getDirection(), Direction.PAYMENT))
        return null;

      // 验证授权组
      BPayment result = new PaymentBizConverter(true).convert(payment);
      validEntityPerm(result);

      if (BizStates.INEFFECT.equals(payment.getBizState()))
        loadRemainTotal(result);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void saveConfig(BPaymentConfig config) throws Exception {
    try {
      getOptionService().setOption(Payments.OPTION_PATH, Payments.KEY_PAY_SHOWTAX,
          String.valueOf(config.isShowTax()), ConvertHelper.getOperateContext(getSessionUser()));
      getOptionService().setOption(Payments.OPTION_PATH, Payments.KEY_PAY_USEINVOICESTOCK,
          String.valueOf(config.isUseInvoiceStock()),
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void registe(BPayment bill) throws Exception {
    try {

      getPaymentService().registe(BizPaymentConverter.getInstance().convert(bill),
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void saveInvoice(BPayment bill) throws Exception {
    try {

      getPaymentService().saveInvoice(BizPaymentConverter.getInstance().convert(bill),
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment loadRegiste(String uuid) throws Exception {
    Payment payment = getPaymentService().getByUuid(uuid, Payment.PART_LINE);
    if (payment == null) {
      throw new ClientException(R.R.nullObject());
    }

    BPayment result = new PaymentBizConverter(true).convert(payment);
    // 验证授权组
    validEntityPerm(result);

    List<BPaymentAccountLine> deletes = new ArrayList<BPaymentAccountLine>();
    for (BPaymentAccountLine line : result.getAccountLines()) {
      if (line.getAcc2().getInvoice().getBillUuid().equals(Accounts.NONE_BILL_UUID)) {
        if (BizStates.ABORTED.equals(payment.getBizState())
            || BizStates.FINISHED.equals(payment.getBizState())) {
          continue;
        }
        if (line.getIvcTotal() == null || line.getIvcTotal().getTotal() == null)
          line.setIvcTotal(line.getTotal().clone());
        continue;
      }
      deletes.add(line);
    }
    result.getAccountLines().removeAll(deletes);
    result.getOverdueLines().clear();

    return result;
  }

  /**
   * 加载预存款科目余额
   * 
   * @param result
   * @throws ClientBizException
   */
  private void loadRemainTotal(BPayment bill) throws ClientBizException {
    String accountUnitUuid = bill.getAccountUnit().getUuid();
    String couonterPartUuid = bill.getCounterpart().getUuid();

    for (BPaymentDepositDefrayal d : bill.getDeposits()) {
      BigDecimal remainTotal = getDepositSubjectRemainTotal(accountUnitUuid, couonterPartUuid,
          (d.getContract() == null || StringUtil.isNullOrBlank(d.getContract().getUuid())) ? "-"
              : d.getContract().getUuid(), d.getSubject().getUuid());
      d.setRemainTotal(remainTotal);
    }

    if (CPaymentDefrayalType.line.equals(bill.getDefrayalType())) {
      for (BPaymentLine line : bill.getAccountLines()) {
        for (BPaymentLineDeposit d : line.getDeposits()) {
          BigDecimal remainTotal = getDepositSubjectRemainTotal(
              accountUnitUuid,
              couonterPartUuid,
              (d.getContract() == null || StringUtil.isNullOrBlank(d.getContract().getUuid())) ? "-"
                  : d.getContract().getUuid(), d.getSubject().getUuid());
          d.setRemainTotal(remainTotal);
        }
      }
      for (BPaymentLine line : bill.getOverdueLines()) {
        for (BPaymentLineDeposit d : line.getDeposits()) {
          BigDecimal remainTotal = getDepositSubjectRemainTotal(
              accountUnitUuid,
              couonterPartUuid,
              (d.getContract() == null || StringUtil.isNullOrBlank(d.getContract().getUuid())) ? "-"
                  : d.getContract().getUuid(), d.getSubject().getUuid());
          d.setRemainTotal(remainTotal);
        }
      }
    }
  }

  private BigDecimal getDepositSubjectRemainTotal(String accountUnit, String counterpart,
      String billUuid, String subject) throws ClientBizException {
    try {

      Advance advance = getAdvanceService().get(accountUnit, counterpart, billUuid, subject);

      return advance == null ? BigDecimal.ZERO : advance.getTotal();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private AdvanceService getAdvanceService() {
    return M3ServiceFactory.getService(AdvanceService.class);
  }

  @Override
  protected Map<String, Object> getBpmVariables(Object obj, BTaskContext ctx) {
    Map<String, Object> variables = super.getBpmVariables(obj, ctx);
    if (obj instanceof Payment) {
      Payment bill = (Payment) obj;
      variables.put(BpmConstants.KEY_BILLCAPTION, CodecUtils.encode(MessageFormat.format(
          R.R.billCaption(), bill.getBillNumber(), toUCNStr(bill.getAccountUnit()),
          toUCNStr(bill.getCounterpart()))));
    }
    return variables;
  }

  private String toUCNStr(UCN ucn) {
    if (ucn == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(ucn.getCode());
    sb.append("]");
    sb.append(ucn.getName());
    return sb.toString();
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions, PageSort pageSort)
      throws ClientBizException {
    FlecsQueryDefinition queryDef = buildQueryDefinition(conditions, pageSort.getSortOrders());
    if (queryDef == null)
      return null;

    queryDef.setPage(pageSort.getPage());
    queryDef.setPageSize(pageSort.getPageSize());
    return queryDef;
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions,
      List<Order> sortOrders) throws ClientBizException {
    FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();
    if (queryDef == null)
      return null;
    queryDef.getFlecsConditions().addAll(
        PaymentQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(PaymentQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() throws ClientBizException {
    FlecsQueryDefinition queryDefinition = new FlecsQueryDefinition();
    queryDefinition.getConditions().addAll(getPermConditions());
    queryDefinition.addCondition(Payments.CONDITION_DIRECTION_EQUALS,
        DirectionType.payment.getDirectionValue());

    return queryDefinition;
  }

  private String getScale() {
    String scale = getOptionService().getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE);
    return StringUtil.isNullOrBlank(scale) ? "2" : scale;
  }

  private String getRoundingMode() {
    String roundingMode = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_ROUNDING_MODE);
    return StringUtil.isNullOrBlank(roundingMode) ? RoundingMode.HALF_UP.name() : roundingMode;
  }

  private BUCN getDefaultSubject() {
    try {
      return DefaultOptionLoader.getInstance().getPrePaySubject();
    } catch (ClientBizException e) {
      return null;
    }
  }

  private com.hd123.m3.account.service.payment.PaymentService getPaymentService() {
    return M3ServiceFactory.getService(com.hd123.m3.account.service.payment.PaymentService.class);
  }

  private AccountOptionService getAccountOptionService() {
    return M3ServiceFactory.getService(AccountOptionService.class);
  }

  private AccountService getAccountService() {
    return M3ServiceFactory.getService(AccountService.class);
  }

  @Override
  public String getObjectName() {
    return Payments.OBJECT_NAME_PAY;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("付款单")
    String moduleCaption();

    @DefaultStringValue("付款单：{0},结算单位：{1},对方单位：{2}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("获取单据类型出错：")
    String findBillTypeError();

    @DefaultStringValue("指定的\"付款单\"不存在。")
    String nullObject();
  }

}
