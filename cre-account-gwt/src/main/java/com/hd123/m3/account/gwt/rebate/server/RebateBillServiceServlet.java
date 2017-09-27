/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BContract;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateLine;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesPayment;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesReceiver;
import com.hd123.m3.account.gwt.rebate.client.rpc.RebateBillService;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales.RebateLineFilter;
import com.hd123.m3.account.gwt.rebate.server.converter.BizRebateBillConverter;
import com.hd123.m3.account.gwt.rebate.server.converter.RebateBillBizConverter;
import com.hd123.m3.account.rs.service.data.RSAccDataService;
import com.hd123.m3.account.rs.service.settle.AccSettle;
import com.hd123.m3.account.rs.service.settle.AccSettles;
import com.hd123.m3.account.rs.service.settle.RSAccSettleService;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.PosMode;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.option.AccountSettleOption;
import com.hd123.m3.account.service.rebate.CalculateResult;
import com.hd123.m3.account.service.rebate.RebateBill;
import com.hd123.m3.account.service.rebate.RebateBills;
import com.hd123.m3.account.service.report.product.ProductPayment;
import com.hd123.m3.account.service.report.product.ProductRpt;
import com.hd123.m3.account.service.report.product.ProductRptService;
import com.hd123.m3.account.service.report.product.ProductRpts;
import com.hd123.m3.account.service.report.product.SalesReceiver;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.date.DateRange;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.QueryDefinitionUtil;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.bpm.server.M3BpmService2Servlet;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.service.contract.contract.ContractDoc;
import com.hd123.m3.investment.service.contract.model.Term;
import com.hd123.m3.investment.service.contract.model.account.AccountTerm;
import com.hd123.m3.investment.service.contract.model.account.formula.RebateFormula;
import com.hd123.m3.investment.service.contract.model.account.formula.ratesite.BankRateFormula;
import com.hd123.m3.investment.service.contract.model.settle.SettlePeriod;
import com.hd123.m3.investment.service.contract.model.settle.SettleTerm;
import com.hd123.m3.sales.service.paytype.PaymentTypeService;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.biz.query.converter.BeanQueryDefinitionConverter;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.collection.CollectionUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.server.RPageDataConverter;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 销售额返款单|客户端服务。
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public class RebateBillServiceServlet extends M3BpmService2Servlet<BRebateBill> implements
    RebateBillService {
  private static final long serialVersionUID = -9052472551799680527L;

  private static Logger logger = LoggerFactory.getLogger(RebateBillServiceServlet.class);

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    // 得到系统级别的是否按笔返款配置
    moduleContext.put(OPTION_REBATE_SINGLE,
        getOptionService().getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_REBATE_SINGLE));
    moduleContext.put(OPTION_PAYMENT_TYPE,
        JsonUtil.objectToJson(getPaymentTypeService().getAllEnabled()));

    moduleContext.put(
        OPTION_ACCOUNT_OPTIONS,
        JsonUtil.objectToJson(getOptionService().getAccountSettleOptions(
            getUserStores(getSessionUser().getId()), AccountSettleOption.FETCH_REBATEBYBILL)));
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    Set<BPermission> permissions = new HashSet<BPermission>();
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getId(),
        Arrays.asList(EPRebateBill.RESOURCE_KEY)));
    // 添加"创建付款单"的权限。
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getId(),
        PaymentPermDef.RESOURCE_PAYMENT_SET));
    return permissions;
  }

  @Override
  protected String doSave(BRebateBill entity) throws Exception {
    try {
      return getRebateBillService().save(new BizRebateBillConverter().convert(entity),
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected BRebateBill doGet(String id) throws Exception {
    Assert.assertArgumentNotNull(id, "id");
    try {
      RebateBill entity = getRebateBillService().get(id);
      BRebateBill result = RebateBillBizConverter.getInstance().convert(entity);
      validEntityPerm(result);

      decode(result);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected String getServiceBeanId() {
    return com.hd123.m3.account.service.rebate.RebateBillService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected RPageData<BRebateBill> doQuery(FlecsQueryDef definition) throws Exception {
    try {

      FlecsQueryDefinition queryDef = buildQueryDefinition(definition,
          RebateBillQueryBuilder.getInstance());
      QueryResult<RebateBill> qr = getRebateBillService().query(queryDef);
      return RPageDataConverter.convert(qr, RebateBillBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public RPageData<BSalesBill> queryProductRpts(RebateLineFilter filter) throws ClientBizException {
    FlecsQueryDefinition queryDef = RebateBillQueryBuilder.getInstance().build4Query(filter);
    if (queryDef == null) {
      return null;
    }

    String posMode = filter.getPosMode();
    try {
      // 查询销售明细
      List<ProductRpt> rpts = getProductRptService().query(queryDef, ProductRpts.PART_WHOLE)
          .getRecords();
      // 检查指定的销售数据是否已经进行销售返款
      List<String> rptUuids = new ArrayList<String>();
      for (ProductRpt productRpt : rpts) {
        rptUuids.add(productRpt.getUuid());
      }
      // 已经进行销售返款
      List<String> rebateRptUuids = getRebateBillService().filterRebatedRpts(rptUuids);
      // 所有未进行返款的销售数据
      List<ProductRpt> productRpts = new ArrayList<ProductRpt>();
      for (ProductRpt productRpt : rpts) {
        // 如果已经返款，则跳过
        if (rebateRptUuids != null && !rebateRptUuids.isEmpty()
            && rebateRptUuids.contains(productRpt.getUuid())) {
          continue;
        } else {
          productRpts.add(productRpt);
        }
      }

      Map<String, BSalesBill> map = new HashMap<String, BSalesBill>();
      Map<String, List<ProductRpt>> rptMap = new HashMap<String, List<ProductRpt>>();
      // 调用计算器得到销售额返款和手续费
      CalculateResult rebateAmounts = getRebateBillService().calculateRebateAmount(productRpts);
      CalculateResult poundageAmounts = getRebateBillService().calculateBankRateAmount(productRpts);

      for (ProductRpt rpt : productRpts) {
        BRebateLine line = new BRebateLine();
        line.setSrcBill(new UCNBizConverter().convert(rpt.getSrcBill()));// 销售单号
        line.setSalesDate(rpt.getOcrTime());
        line.setAccountDate(rpt.getAccountDate());
        line.setRptUuid(rpt.getUuid());
        logger.info("销售单号：" + rpt.getSrcBill().getCode() + ",rptUuid:" + rpt.getUuid() + ",销售金额："
            + rpt.getTotal());
        line.setAmount(rpt.getTotal());// 销售金额
        line.setPoundageAmount(poundageAmounts.getResult().get(rpt.getUuid()));// 手续费
        line.setRebateAmount(rebateAmounts.getResult().get(rpt.getUuid()));// 返款金额
        logger.info("销售单号：" + rpt.getSrcBill().getCode() + ",rptUuid:" + rpt.getUuid() + ",返款金额："
            + line.getRebateAmount());
        for (ProductPayment payment : rpt.getPayments()) {
          if (payment.getReceiver() == SalesReceiver.market
              || payment.getReceiver() == SalesReceiver.contract
              && PosMode.usePublic.name().equals(posMode)) {
            BSalesPayment salePayment = new BSalesPayment();

            salePayment.setReceiver(BSalesReceiver.valueOf(payment.getReceiver().name()));
            salePayment.setPayment(new UCNBizConverter().convert(payment.getPayment()));
            salePayment.setBank(new UCNBizConverter().convert(payment.getBank()));
            salePayment.setTotal(payment.getTotal());
            line.getPayments().add(salePayment);
          }
        }
        if (line.getPayments().isEmpty())
          continue;

        BSalesBill bill = map.get(rpt.getSrcBill().getUuid());
        if (bill == null) {
          bill = new BSalesBill();
          bill.setUuid(rpt.getSrcBill().getUuid());
          bill.setBillType(rpt.getSrcBill().getName());
          bill.setBillnumber(rpt.getSrcBill().getCode());
          bill.setAmount(BigDecimal.ZERO);
          bill.setAccountDate(new BDateRange(rpt.getOcrTime(), rpt.getOcrTime()));
          map.put(bill.getUuid(), bill);
          rptMap.put(bill.getUuid(), new ArrayList<ProductRpt>());
        }
        if (bill.getAccountDate().getBeginDate().after(rpt.getOcrTime())) {
          bill.getAccountDate().setBeginDate(rpt.getOcrTime());
        } else if (bill.getAccountDate().getEndDate().before(rpt.getOcrTime())) {
          bill.getAccountDate().setEndDate(rpt.getOcrTime());
        }
        rptMap.get(bill.getUuid()).add(rpt);

        bill.setAmount(bill.getAmount().add(rpt.getTotal()));
        bill.getLines().add(line);
      }

      List<BSalesBill> values = new ArrayList<BSalesBill>();
      values.addAll(map.values());

      if (values.isEmpty() == false) {
        final String sortField;
        final int direction;
        if (filter.getOrders().isEmpty() == false) {
          sortField = filter.getOrders().get(0).getFieldName();
          direction = OrderDir.asc == filter.getOrders().get(0).getDir() ? 1 : -1;
        } else {
          sortField = BSalesBill.FIELD_BILLNUMBER;
          direction = -1;
        }

        Collections.sort(values, new Comparator<BSalesBill>() {

          @Override
          public int compare(BSalesBill o1, BSalesBill o2) {
            if (BSalesBill.FIELD_BILLNUMBER.equals(sortField)) {
              return direction * o1.getBillnumber().compareTo(o2.getBillnumber());
            } else if (BSalesBill.FIELD_AMOUNT.equals(sortField)) {
              return direction * o1.getAmount().compareTo(o2.getAmount());
            }
            return 0;
          }
        });
      }

      return RPageDataUtil.flip(values, filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BContract getContractDoc(String contractId) {
    ContractDoc contractDoc = getInvContractService().get(contractId);
    if (contractDoc == null)
      return null;

    BContract contract = null;
    contract = new BContract();
    contract.setUuid(contractDoc.getContract().getId());
    contract.setCode(contractDoc.getContract().getSerialNumber());
    contract.setName(contractDoc.getContract().getSignboard());
    contract.setAccountUnit(new UCNBizConverter().convert(contractDoc.getContract().getStore()));
    contract.setCounterpart(new UCNBizConverter().convert(contractDoc.getContract().getTenant()));

    contract.setBeginDate(contractDoc.getContract().getBeginDate());
    contract.setEndDate(contractDoc.getContract().getEndDate());
    DateRange accountSettle = getLstUnaccountedSette(contractDoc.getContract().getId());
    if (accountSettle != null) {
      contract.setRebateBeginDate(accountSettle.getBeginDate());
      contract.setLastestAccountDate(accountSettle.getBeginDate());
      contract.setRebateEndDate(accountSettle.getEndDate().after(new Date()) ? DateUtils.truncate(
          new Date(), Calendar.DAY_OF_MONTH) : accountSettle.getEndDate());
    } else {
      contract.setRebateBeginDate(contract.getBeginDate());
      contract.setRebateEndDate(contract.getEndDate());
    }

    contract.setPosMode(contractDoc.getContract().getPosMode().name());

    List<String> positions = new ArrayList<String>();
    for (UCN position : contractDoc.getContract().getPositions()) {
      positions.add(position.getCode());
    }
    contract.setPositions(CollectionUtil.toString(positions));

    for (Term term : contractDoc.getContract().getTerms()) {
      // 判断是否有销售额返款条款和银行手续费扣率条款
      if (term instanceof AccountTerm) {
        AccountTerm accountTerm = (AccountTerm) term;
        if (accountTerm.getFormula() instanceof RebateFormula) {
          contract.setRebateBack(true);
          contract.setRebateInvoice(accountTerm.isInvoice());
          contract
              .setRebateSubject(UCNBizConverter.getInstance().convert(accountTerm.getSubject()));
          contract.setRebateDirection(accountTerm.getDirection());
          contract.setBackTaxRate(TaxRateBizConverter.getInstance().convert(
              accountTerm.getTaxRate()));
        } else if (accountTerm.getFormula() instanceof BankRateFormula) {
          contract.setBankRate(true);
          contract.setPoundageInvoice(accountTerm.isInvoice());
          contract.setPoundageSubject(UCNBizConverter.getInstance().convert(
              accountTerm.getSubject()));
          contract.setPoundageDirection(accountTerm.getDirection());
          contract.setPoundageTaxRate(TaxRateBizConverter.getInstance().convert(
              accountTerm.getTaxRate()));
        }
      } else if (term instanceof SettleTerm) {// 账款周期条款
        SettleTerm settleTerm = (SettleTerm) term;
        // 得到账款类型为销售返款
        if (settleTerm.getAccountTypes().contains(UsageType.salePayment.name())) {
          for (SettlePeriod settle : settleTerm.getSettles()) {
            contract.getRanges().add(new BDateRange(settle.getBeginDate(), settle.getEndDate()));
          }
        }
      }
    }

    return contract;
  }

  private void decode(BRebateBill bill) {
    if (bill == null)
      return;
    bill.setContract(getContractDoc(bill.getContract().getUuid()));

    if (BizStates.EFFECT.equals(bill.getBizState())) {
      FlecsQueryDefinition definition = new FlecsQueryDefinition();
      definition.addCondition(Accounts.CONDITION_ACTIVE);
      definition.getConditions().add(
          QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_SOURCEBILL_UUID_EQUALS,
              bill.getUuid()));
      definition.getConditions().add(
          QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_CAN_PAYMENT));
      definition.getConditions().add(
          QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS,
              Accounts.NONE_LOCK_UUID));

      List<Account> accounts = getAccountService().query(definition).getRecords();
      bill.setHasAccount(accounts.isEmpty() == false);
    }
  }

  /**
   * 查询得到未开的最小销售额返款开始日期
   * 
   * @param contractId
   * @return
   */
  private DateRange getLstUnaccountedSette(String contractId) {
    List<SubjectUsage> usages = getRSAccDataService().getAllSubjectUsage();
    Set<String> codes = new HashSet<String>();
    for (SubjectUsage usage : usages) {
      if (UsageType.salePayment.equals(usage.getUsageType())) {
        codes.add(usage.getCode());
      }
    }
    if (codes.isEmpty()) {
      return null;
    }

    QueryDefinition def = new QueryDefinition();
    def.addCondition(AccSettles.CONDITION_UNACCOUNTED);
    def.addCondition(AccSettles.CONDITION_SUBJECTUSAGE_IN, codes.toArray());
    def.addCondition(AccSettles.CONDITION_CONTRACT_EQUALS, contractId);
    def.addOrder(AccSettles.ORDER_BY_BEGINDATE, QueryOrderDirection.asc);
    def.setPageSize(1);

    QueryResult<AccSettle> qr = getRSAccSettleService().query(
        new BeanQueryDefinitionConverter().convert(def));
    if (qr.getRecords().isEmpty())
      return null;

    AccSettle settle = qr.getRecords().get(0);
    return new DateRange(settle.getBeginDate(), settle.getEndDate());
  }

  // ---------------------------------------------------------------------------------------
  @Override
  protected String getObjectCaption() {
    return R.R.billCaption();
  }

  @Override
  public String getObjectName() {
    return RebateBills.OBJECT_NAME_PAY;
  }

  private com.hd123.m3.account.service.rebate.RebateBillService getRebateBillService() {
    return M3ServiceFactory.getService(com.hd123.m3.account.service.rebate.RebateBillService.class);
  }

  protected AccountOptionService getOptionService() {
    return getAppCtx().getBean(AccountOptionService.class);
  }

  private ProductRptService getProductRptService() {
    return M3ServiceFactory.getService(ProductRptService.class);
  }

  private PaymentTypeService getPaymentTypeService() {
    return M3ServiceFactory.getService(PaymentTypeService.class);
  }

  protected ContractService getContractService() {
    try {
      return M3ServiceFactory.getService(ContractService.class);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private AccountService getAccountService() {
    return M3ServiceFactory.getService(AccountService.class);
  }

  private RSAccSettleService getRSAccSettleService() {
    return getAppCtx().getBean(RSAccSettleService.DEFAULT_RS_CONTEXT_ID, RSAccSettleService.class);
  }

  private RSAccDataService getRSAccDataService() {
    return getAppCtx().getBean(RSAccDataService.DEFAULT_RS_CONTEXT_ID, RSAccDataService.class);
  }

  /**
   * 合同资料服务接口(招商有关)
   * 
   * @return
   */
  private com.hd123.m3.investment.service.contract.contract.ContractService getInvContractService() {
    try {
      return M3ServiceFactory
          .getService(com.hd123.m3.investment.service.contract.contract.ContractService.class);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("销售额返款单")
    String billCaption();
  }
}
