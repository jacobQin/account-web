/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-4 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.client.ClientException;

import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.commons.CounterpartType;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.commons.server.DefaultOptionLoader;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegPermDef;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDeposit;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.server.converter.BizPaymentConverter;
import com.hd123.m3.account.gwt.payment.commons.server.converter.PaymentBizConverter;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptConfig;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.accobject.AccountingObjectBillService;
import com.hd123.m3.account.service.accobject.AccountingObjectLine;
import com.hd123.m3.account.service.accobject.AccountingObjectLines;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
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
public class ReceiptServiceServlet extends AccBPMServiceServlet implements ReceiptService {

  private static final long serialVersionUID = 2336269374956451286L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    Set<String> RESOURCE_RECEIPT_SET = new HashSet();
    RESOURCE_RECEIPT_SET.addAll(ReceiptPermDef.RESOURCE_RECEIPT_SET);
    RESOURCE_RECEIPT_SET.add(InvoiceRegPermDef.RESOURCE_KEY);
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        RESOURCE_RECEIPT_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    try {
      moduleContext.put(SCALE, getScale());
      moduleContext.put(ROUNDING_MODE, getRoundingMode());
      moduleContext.put(KEY_COOPMODES, CollectionUtil.toString(getAllEnabledCoopModes()));
      moduleContext.put(KEY_RECEIPT_OVERDUE_DEFAULTS, getReceiptOverdueDefault());
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
      List<BPayment> resultList = new ArrayList<BPayment>();
      List<Payment> payments = queryResult.getRecords();
      pd.setValues(ConverterUtil.convert(payments, new PaymentBizConverter(false)));

      for (Payment p : payments) {
        resultList.add(loadForSearch(p.getUuid()));
      }
      pd.getValues().clear();
      pd.getValues().addAll(resultList);
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
      payment.setDirection(DirectionType.receipt.getDirectionValue());
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

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService()
          .createByStatements(statementUuids, DirectionType.receipt.getDirectionValue()));
      payment.setDepositSubject(getDefaultSubject());
      decorateAccounts(payment);
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createByInvoices(Collection<String> invoiceUuids) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService()
          .createByInvoices(invoiceUuids, DirectionType.receipt.getDirectionValue()));
      payment.setDepositSubject(getDefaultSubject());
      decorateAccounts(payment);
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createByPaymentNotices(Collection<String> noticeUuids) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService().createByNotices(
          noticeUuids, DirectionType.receipt.getDirectionValue()));
      payment.setDepositSubject(getDefaultSubject());
      decorateAccounts(payment);
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createBySourceBills(Collection<String> sourceBillUuids) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService()
          .createBySourceBills(sourceBillUuids, DirectionType.receipt.getDirectionValue()));
      payment.setDepositSubject(getDefaultSubject());
      decorateAccounts(payment);
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createByAccounts(Collection<String> accountUuids) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService()
          .createByAccounts(accountUuids, DirectionType.receipt.getDirectionValue()));
      payment.setDepositSubject(getDefaultSubject());
      decorateAccounts(payment);
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPayment createByAccountIds(Collection<String> accountIds) throws Exception {
    try {

      BPayment payment = new PaymentBizConverter(true).convert(getPaymentService()
          .createByAccountIds(accountIds, DirectionType.receipt.getDirectionValue()));
      payment.setDepositSubject(getDefaultSubject());
      decorateAccounts(payment);
      return payment;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void decorateAccounts(BPayment payment) {
    negateLineTotal(payment);
    processOverdues(payment);
  }

  /** 收款选择方向为付的账款时，金额需要取反 */
  private void negateLineTotal(BPayment payment) {
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      if (DirectionType.payment.getDirectionValue() == line.getAcc1().getDirection()) {
        line.getOriginTotal().negate();
        line.getTotal().negate();
        line.getUnpayedTotal().negate();
      }
    }
  }

  /** 针对收款单，将为付账款的滞纳金设置为0。 */
  private void processOverdues(BPayment payment) {
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      if (DirectionType.payment.getDirectionValue() == line.getAcc1().getDirection()) {
        line.getOverdues().clear();
        line.getOverdueTerms().clear();
        line.getOverdueTotal().setTotal(BigDecimal.ZERO);
      }
    }
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

      // 去除应收金额为0的代收明细行
      for (int i = bill.getCollectionLines().size() - 1; i >= 0; i--) {
        BPaymentCollectionLine collectionLine = bill.getCollectionLines().get(i);

        for (int j = collectionLine.getCashs().size() - 1; j >= 0; j--) {
          BPaymentLineCash cash = collectionLine.getCashs().get(j);
          if (cash.getTotal() == null || BigDecimal.ZERO.compareTo(cash.getTotal()) == 0) {
            collectionLine.getCashs().remove(j);
          }
        }
        if (collectionLine.getUnpayedTotal() == null
            || collectionLine.getUnpayedTotal().getTotal() == null
            || collectionLine.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) == 0) {
          bill.getCollectionLines().remove(i);
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

  private BPayment loadForSearch(String uuid) throws Exception {
    try {

      Payment payment = null;
      payment = getPaymentService().getByUuid(uuid, Payment.PART_LINE);

      if (payment == null)
        return null;
      if (!ObjectUtil.equals(payment.getDirection(), Direction.RECEIPT))
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
  public BPayment load(String uuid, boolean isForEdit) throws Exception {
    try {

      Payment payment = null;
      if (isForEdit) {
        payment = getPaymentService().getByUuid(uuid, Payment.PART_LINE,
            Payment.PART_COLLECTION_LINES, Payment.PART_DEFRAYAL, Payment.PART_ACCOVERDUE,
            Payment.PART_LINEDEFRAYAL, Payment.PART_OVERDUETERM);
      } else {
        payment = getPaymentService().getByUuid(uuid, Payment.PART_WHOLE);
      }

      if (payment == null)
        return null;
      if (!ObjectUtil.equals(payment.getDirection(), Direction.RECEIPT))
        return null;

      BPayment result = new PaymentBizConverter(true).convert(payment);
      // 验证授权组
      validEntityPerm(result);

      if (BizStates.INEFFECT.equals(payment.getBizState())) {
        loadRemainTotal(result);
      }

      if (isForEdit == false) {
        decorateLineInvoiceByAccId(result);
      }

      if (BBizStates.EFFECT.equals(payment.getBizState())) {
        boolean isProprietor = CounterpartType.PROPRIETOR.equals(payment.getCounterpartType());
        if (isProprietor) {
          result.setCanInvoice(false);
        } else {
          result.setCanInvoice(existActiveAccounts(payment));
        }
      }

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private boolean existActiveAccounts(Payment payment) {
    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);

    queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS, payment.getAccountUnit()
        .getUuid());

    queryDef.addCondition(Accounts.CONDITION_PAYMENT_UUID_EQUALS, payment.getUuid());
    queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, payment.getDirection());
    queryDef.addCondition(Accounts.CONDITION_CAN_INVOICE);

    List<Account> accounts = getAccountService().query(queryDef).getRecords();

    if (accounts.isEmpty()) {
      return false;
    }

    return true;

  }

  /** 根据账款id加载账款，目的是刷新账款明细的发票信息 */
  private void decorateLineInvoiceByAccId(BPayment payment) {

    if (payment == null) {
      return;
    }

    Map<String, BPaymentAccountLine> accountLines = new HashMap<String, BPaymentAccountLine>();
    Map<String, BPaymentCollectionLine> collectionLines = new HashMap<String, BPaymentCollectionLine>();

    List<String> accIds = new ArrayList<String>();
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      String accId = line.getAcc1().getId();
      accIds.add(accId);
      accountLines.put(accId, line);
    }

    for (BPaymentCollectionLine line : payment.getCollectionLines()) {
      if (StringUtil.isNullOrBlank(line.getAccountId())) {
        continue;
      }
      accIds.add(line.getAccountId());
      collectionLines.put(line.getAccountId(), line);
    }

    if (accIds.isEmpty()) {
      return;
    }

    QueryDefinition def = new QueryDefinition();
    def.addCondition(Accounts.CONDITION_ACCID_IN, accIds.toArray());
    def.addCondition(Accounts.CONDITION_PAYMENT_UUID_EQUALS, payment.getUuid());
    QueryResult<Account> result = getAccountService().query(def);
    if (result.getRecordCount() == 0) {
      return;
    }

    // 按accId分组
    Map<String, List<Account>> accounts = new HashMap<String, List<Account>>();
    for (Account account : result.getRecords()) {
      /** 过滤掉未进行登记的账款 */
      if (Accounts.NONE_BILL_UUID.equals(account.getAcc2().getInvoice().getBillUuid())) {
        continue;
      }
      if (Accounts.DISABLE_BILL_UUID.equals(account.getAcc2().getInvoice().getBillUuid())) {
        continue;
      }

      String accId = account.getAcc1().getId();
      if (accounts.get(accId) == null) {
        accounts.put(accId, new ArrayList<Account>());
      }
      accounts.get(accId).add(account);
    }

    for (Entry<String, List<Account>> entry : accounts.entrySet()) {
      BPaymentAccountLine accountLine = accountLines.get(entry.getKey());
      BPaymentCollectionLine collectionLine = collectionLines.get(entry.getKey());
      List<Account> accs = accounts.get(entry.getKey());
      if (accountLine != null) {
        accountLine.setInvoiceCodeStr(ReceipUtil.getInvoiceCodeStr(accs));
        accountLine.setInvoiceNumberStr(ReceipUtil.getInvoiceNumbertr(accs));
      }
      if (collectionLine != null) {
        collectionLine.setInvoiceCodeStr(ReceipUtil.getInvoiceCodeStr(accs));
        collectionLine.setInvoiceNumberStr(ReceipUtil.getInvoiceNumbertr(accs));
      }
    }

  }

  @Override
  public BPayment loadByNumber(String billNumber, boolean isForEdit) throws Exception {
    try {

      Payment payment = null;
      if (isForEdit) {
        payment = getPaymentService().getByNumber(billNumber, Payment.PART_LINE,
            Payment.PART_COLLECTION_LINES, Payment.PART_DEFRAYAL, Payment.PART_ACCOVERDUE,
            Payment.PART_LINEDEFRAYAL, Payment.PART_OVERDUETERM);
      } else {
        payment = getPaymentService().getByNumber(billNumber, Payment.PART_WHOLE);
      }

      if (payment == null)
        return null;
      if (!ObjectUtil.equals(payment.getDirection(), Direction.RECEIPT))
        return null;

      // 验证授权组
      BPayment result = new PaymentBizConverter(true).convert(payment);
      validEntityPerm(result);

      if (BizStates.INEFFECT.equals(payment.getBizState())) {
        loadRemainTotal(result);
      }

      if (isForEdit == false) {
        decorateLineInvoiceByAccId(result);
      }

      if (BBizStates.EFFECT.equals(payment.getBizState())) {
        boolean isProprietor = CounterpartType.PROPRIETOR.equals(payment.getCounterpartType());
        if (isProprietor) {
          result.setCanInvoice(false);
        } else {
          result.setCanInvoice(existActiveAccounts(payment));
        }
      }

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private String getReceiptOverdueDefault() {
    String receiptOverdueDefault = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_RECEIPT_OVERDUE_DEFAULT);
    return receiptOverdueDefault;
  }

  @Override
  public BReceiptConfig loadConfig() throws Exception {
    try {
      String receiptPaymentType = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_PAYMENTTYPE);
      String receiptPaymentTypeLimit = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_PAYMENTTYPE_LIMITS);
      BReceiptConfig config = new BReceiptConfig();
      String showTax = getOptionService().getOption(Payments.OPTION_PATH, Payments.KEY_REC_SHOWTAX);
      config.setShowTax(showTax == null ? false : Boolean.valueOf(showTax));
      config.setReceiptPaymentType(getPaymentTypeByCode(receiptPaymentType));
      config.setReceiptPaymentTypeLimit(receiptPaymentTypeLimit == null ? BigDecimal.ZERO
          : new BigDecimal(receiptPaymentTypeLimit));

      String bankRequired = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_BANK_REQUIRED);
      config.setBankRequired(Boolean.valueOf(bankRequired));

      String receiptEqualsUnpayed = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_EQUALS_UNPAYED);
      config.setReceiptEqualsUnpayed(Boolean.valueOf(receiptEqualsUnpayed));

      String accObjectEnabled = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_ACCOBJECT_ENABLED);
      config.setAccObjectEnabled(Boolean.valueOf(accObjectEnabled));

      String defalutReceiptPaymentType = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_DEFAULT_RECEIPT_PAYMENTTYPE);
      config.setDefalutReceiptPaymentType(defalutReceiptPaymentType);

      return config;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private BUCN getPaymentTypeByCode(String code) throws Exception {
    try {
      if (StringUtil.isNullOrBlank(code))
        return null;
      PaymentType paymentType = getPaymentTypeService().getByCode(code);
      if (paymentType == null)
        return null;
      BUCN result = new BUCN();
      result.setUuid(paymentType.getUuid());
      result.setCode(paymentType.getCode());
      result.setName(paymentType.getName());
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void saveConfig(BReceiptConfig config) throws Exception {
    try {
      getOptionService().setOption(Payments.OPTION_PATH, Payments.KEY_REC_SHOWTAX,
          String.valueOf(config.isShowTax()), ConvertHelper.getOperateContext(getSessionUser()));
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
        if ((line.getIvcTotal() == null || line.getIvcTotal().getTotal() == null)
            && line.getAcc1().getDirection() == 1) {
          line.setIvcTotal(line.getTotal().clone());
          continue;
        }
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

    if (!CPaymentDefrayalType.bill.equals(bill.getDefrayalType())) {
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

  @Override
  public BContract getContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException {
    try {
      return getSingleContract(accountUnitUuid, counterpartUuid, counterpartType);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private AdvanceService getAdvanceService() {
    return M3ServiceFactory.getService(AdvanceService.class);
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

  @Override
  public List<BPaymentAccountLine> sortLines(List<BPaymentAccountLine> lines, List<Order> orders)
      throws Exception {
    if (lines == null) {
      return new ArrayList<BPaymentAccountLine>();
    }
    Collections.sort(lines, PaymentAccountLineComparator.create(orders));
    return lines;
  }

  @Override
  public List<BUCN> filterSubjects(List<BUCN> subjects, BUCN store, BUCN subject, BUCN contract)
      throws ClientBizException {

    Assert.assertArgumentNotNull(store, "store");

    // 源科目组为空直接返回
    if (subjects == null || subjects.isEmpty()) {
      return new ArrayList<BUCN>();
    }

    if (subject == null || contract == null) {
      return new ArrayList<BUCN>();
    }

    List<String> subjectUuids = new ArrayList<String>();
    for (BUCN sb : subjects) {
      subjectUuids.add(sb.getUuid());
    }

    subjectUuids.add(subject.getUuid());

    try {

      // 仅查询当前项目下的数据
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addCondition(AccountingObjectLines.CONDITION_STORE_EQUALS, store.getUuid());
      def.addCondition(AccountingObjectLines.CONDITION_SUBJECT_IN, subjectUuids.toArray());
      List<AccountingObjectLine> conSubObjs = getAccountingObjectBillService().queryAccObjects(def)
          .getRecords();
      if (conSubObjs.isEmpty()) {
        return new ArrayList<BUCN>();
      }

      /** 来源科目(subject)对应的核算主体列表（最多有两条记录，一条到当前合同，一条到当前项目没指定合同） */
      // 取出来源科目对应的核算主体（如果定义了合同级别核算主体用之，否则用项目级别）
      AccountingObjectLine sourceSubObjectLine = getSubObjBySubject(conSubObjs, subject, contract);

      // 如果来源科目未定义项目级别核算主体，且也无该合同级别核算主体，则直接返回空。
      if (sourceSubObjectLine == null) {
        return new ArrayList<BUCN>();
      }

      // 开始过滤科目
      List<BUCN> result = new ArrayList<BUCN>();
      for (BUCN sub : subjects) {
        // 取备选科目的核算主体
        AccountingObjectLine line = getSubObjBySubject(conSubObjs, sub, contract);
        // 如果核算主体为空，弃之
        if (line == null) {
          continue;
        }

        // 如果与来源科目的核算主体相同，则表示该科目符合条件，否则弃之。
        if (line.getAccObject().equals(sourceSubObjectLine.getAccObject())) {
          result.add(sub);
        }
      }

      return result;

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /** 取得指定项目-科目-合同的核算主体 */
  private AccountingObjectLine getSubObjBySubject(List<AccountingObjectLine> conSubObjs,
      BUCN subject, BUCN contract) {

    // 优先取合同核算主体
    for (AccountingObjectLine line : conSubObjs) {
      if (line.getContract() == null) { // 合同为null的不考虑
        continue;
      }
      if (subject.getUuid().equals(line.getSubject().getUuid())
          && line.getContract().getUuid().equals(contract.getUuid())) {
        return line;
      }
    }

    // 取项目
    for (AccountingObjectLine line : conSubObjs) {
      if (line.getContract() != null) { // 合同不为null的不考虑
        continue;
      }
      if (subject.getUuid().equals(line.getSubject().getUuid())) {
        return line;
      }
    }

    return null;
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
        ReceiptQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(ReceiptQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() throws ClientBizException {
    FlecsQueryDefinition queryDefinition = new FlecsQueryDefinition();
    queryDefinition.getConditions().addAll(getPermConditions());
    queryDefinition.addCondition(Payments.CONDITION_DIRECTION_EQUALS,
        DirectionType.receipt.getDirectionValue());

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
      return DefaultOptionLoader.getInstance().getPreReceiveSubject();
    } catch (ClientBizException e) {
      return null;
    }
  }

  private com.hd123.m3.account.service.payment.PaymentService getPaymentService() {
    return M3ServiceFactory.getService(com.hd123.m3.account.service.payment.PaymentService.class);
  }

  private AccountingObjectBillService getAccountingObjectBillService() {
    return getAppCtx().getBean(AccountingObjectBillService.DEFAULT_CONTEXT_ID,
        AccountingObjectBillService.class);
  }

  private AccountService getAccountService() {
    return getAppCtx().getBean(AccountService.DEFAULT_CONTEXT_ID, AccountService.class);
  }

  private PaymentTypeService getPaymentTypeService() throws ClientBizException {
    return M3ServiceFactory.getService(PaymentTypeService.class);
  }

  @Override
  public String getObjectName() {
    return Payments.OBJECT_NAME_RECEIPT;
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("收款单")
    String moduleCaption();

    @DefaultStringValue("收款单：{0},结算单位：{1},对方单位：{2}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("获取单据类型出错：")
    String findBillTypeError();

    @DefaultStringValue("指定的\"收款单\"不存在。")
    String nullObject();
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

}
