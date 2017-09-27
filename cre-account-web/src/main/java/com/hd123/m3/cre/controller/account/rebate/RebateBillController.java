/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	RebateBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.rebate;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.rs.service.data.RSAccDataService;
import com.hd123.m3.account.rs.service.settle.AccSettle;
import com.hd123.m3.account.rs.service.settle.AccSettles;
import com.hd123.m3.account.rs.service.settle.RSAccSettleService;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.contract.PosMode;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.option.AccountSettleOption;
import com.hd123.m3.account.service.rebate.CalculateResult;
import com.hd123.m3.account.service.rebate.RebateBill;
import com.hd123.m3.account.service.rebate.RebateBillService;
import com.hd123.m3.account.service.rebate.RebateBills;
import com.hd123.m3.account.service.rebate.RebateLine;
import com.hd123.m3.account.service.rebate.SalesPayment;
import com.hd123.m3.account.service.report.product.ProductPayment;
import com.hd123.m3.account.service.report.product.ProductRpt;
import com.hd123.m3.account.service.report.product.ProductRptService;
import com.hd123.m3.account.service.report.product.ProductRpts;
import com.hd123.m3.account.service.report.product.SalesReceiver;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.date.DateRange;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.QueryDefinitionUtil;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.investment.service.contract.contract.ContractDoc;
import com.hd123.m3.investment.service.contract.contract.ContractService;
import com.hd123.m3.investment.service.contract.model.Contract;
import com.hd123.m3.investment.service.contract.model.Term;
import com.hd123.m3.investment.service.contract.model.account.AccountTerm;
import com.hd123.m3.investment.service.contract.model.account.formula.RebateFormula;
import com.hd123.m3.investment.service.contract.model.account.formula.ratesite.BankRateFormula;
import com.hd123.m3.investment.service.contract.model.settle.SettlePeriod;
import com.hd123.m3.investment.service.contract.model.settle.SettleTerm;
import com.hd123.m3.sales.service.paytype.PaymentTypeService;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.biz.query.converter.BeanQueryDefinitionConverter;
import com.hd123.rumba.commons.collection.CollectionUtil;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author chenganbang
 *
 */
@Controller
@RequestMapping("account/rebate/*")
public class RebateBillController extends BizFlowModuleController {
  public static final String RESOURCE_KEY = "account.rebate";
  public static final String RESOURCE_KEY_PAYMENT = "account.payment.pay";

  public static final String OPTION_REBATE_SINGLE = "rebateSingle";
  public static final String OPTION_ACCOUNT_OPTIONS = "accountOptions";
  public static final String OPTION_PAYMENT_TYPE = "paymentType";

  @Autowired
  private AccountOptionService accountOptionService;
  @Autowired
  private PaymentTypeService paymentTypeService;
  @Autowired
  private RebateBillService rebateBillService;
  @Autowired
  private ContractService contractService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private RSAccDataService rsAccDataService;
  @Autowired
  private RSAccSettleService rsAccSettleService;
  @Autowired
  private ProductRptService productRptService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        RESOURCE_KEY, RESOURCE_KEY_PAYMENT));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);

    moduleContext.put(OPTION_REBATE_SINGLE,
        accountOptionService.getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_REBATE_SINGLE));
    moduleContext.put(OPTION_PAYMENT_TYPE, paymentTypeService.getAllEnabled());
    moduleContext.put(OPTION_ACCOUNT_OPTIONS, accountOptionService.getAccountSettleOptions(
        getUserStores(getSessionUserId()), AccountSettleOption.FETCH_REBATEBYBILL));
  }

  @Override
  protected void decorateRecords(SummaryQueryResult queryResult) {
    List<RebateBill> bills = queryResult.getRecords();
    List<BRebateBill> records = new ArrayList<BRebateBill>(bills.size());
    for (RebateBill bill : bills) {
      records.add(RebateBillBizConverter.getInstance().convert(bill));
    }
    decorate(records);
    queryResult.setRecords(records);
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(json)) {
      return null;
    }
    BRebateBill bill = JsonUtil.jsonToObject(json, BRebateBill.class);
    RebateBill entity = JsonUtil.jsonToObject(json, RebateBill.class);
    entity.setContract(bill.getAgreement());
    return getEntityService().save(entity, new BeanOperateContext(getSessionUser()));
  }

  @RequestMapping("load")
  @ResponseBody
  public RebateBill load(@RequestParam String uuid) throws Exception {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }
    RebateBill entity = rebateBillService.get(uuid);
    validEntityPerm(entity);

    BRebateBill result = RebateBillBizConverter.getInstance().convert(entity);
    result.setAgreement(getContractDoc(entity.getContract().getUuid()));
    List<BRebateBill> bills = new ArrayList<BRebateBill>();
    bills.add(result);
    decorate(bills);
    return result;
  }

  @RequestMapping("queryProductRpts")
  @ResponseBody
  public QueryResult<BSalesBill> queryProductRpts(@RequestBody QueryFilter queryFilter)
      throws Exception {
    FlecsQueryDefinition queryDef = getQueryBuilder().build4Query(queryFilter);
    if (queryDef == null) {
      return null;
    }

    Map<String, Object> filter = queryFilter.getFilter();
    String posMode = (String) filter.get("posMode");

    // 查询销售明细
    List<ProductRpt> rpts = productRptService.query(queryDef, ProductRpts.PART_WHOLE).getRecords();
    // 检查指定的销售数据是否已经进行销售返款
    List<String> rptUuids = new ArrayList<String>();
    for (ProductRpt productRpt : rpts) {
      rptUuids.add(productRpt.getUuid());
    }
    // 已经进行销售返款
    List<String> rebateRptUuids = rebateBillService.filterRebatedRpts(rptUuids);
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
    CalculateResult rebateAmounts = rebateBillService.calculateRebateAmount(productRpts);
    CalculateResult poundageAmounts = rebateBillService.calculateBankRateAmount(productRpts);

    for (ProductRpt rpt : productRpts) {
      RebateLine line = new RebateLine();
      line.setSrcBill(rpt.getSrcBill());// 销售单号
      line.setSalesDate(rpt.getOcrTime());
      line.setAccountDate(rpt.getAccountDate());
      line.setRptUuid(rpt.getUuid());
      line.setAmount(rpt.getTotal());// 销售金额
      line.setPoundageAmount(poundageAmounts.getResult().get(rpt.getUuid()));// 手续费
      line.setRebateAmount(rebateAmounts.getResult().get(rpt.getUuid()));// 返款金额
      for (ProductPayment payment : rpt.getPayments()) {
        if (payment.getReceiver() == SalesReceiver.market
            || payment.getReceiver() == SalesReceiver.contract
            && PosMode.usePublic.name().equals(posMode)) {
          SalesPayment salePayment = new SalesPayment();

          salePayment.setReceiver(SalesReceiver.valueOf(payment.getReceiver().name()));
          salePayment.setPayment(payment.getPayment());
          salePayment.setBank(payment.getBank());
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
        bill.setBillNumber(rpt.getSrcBill().getCode());
        bill.setAmount(BigDecimal.ZERO);
        bill.setAccountDate(new DateRange(rpt.getOcrTime(), rpt.getOcrTime()));
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
      if (queryFilter.getSorts().isEmpty() == false) {
        sortField = queryFilter.getSorts().get(0).getProperty();
        direction = OrderSort.ASC == queryFilter.getSorts().get(0).getDirection() ? 1 : -1;
      } else {
        sortField = "billNumber";
        direction = -1;
      }

      Collections.sort(values, new Comparator<BSalesBill>() {

        @Override
        public int compare(BSalesBill o1, BSalesBill o2) {
          if ("billNumber".equals(sortField)) {
            return direction * o1.getBillNumber().compareTo(o2.getBillNumber());
          } else if ("amount".equals(sortField)) {
            return direction * o1.getAmount().compareTo(o2.getAmount());
          }
          return 0;
        }
      });
    }

    QueryResult<BSalesBill> result = new QueryResult<BSalesBill>();
    result.setRecords(values);
    result.setPage(queryFilter.getCurrentPage());
    result.setPageSize(queryFilter.getPageSize());
    return result;
  }

  private void decorate(List<BRebateBill> bills) {
    if (bills == null || bills.isEmpty()) {
      return;
    }

    List<String> billUuids = new ArrayList<String>();
    for (BRebateBill bill : bills) {
      if (BizStates.EFFECT.equals(bill.getBizState())) {
        billUuids.add(bill.getUuid());
      }
    }

    if (billUuids.isEmpty()) {
      return;
    }

    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Accounts.CONDITION_ACTIVE);
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_SOURCEBILL_UUID_IN,
            billUuids.toArray()));
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_CAN_PAYMENT));
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS,
            Accounts.NONE_LOCK_UUID));
    List<Account> accounts = accountService.query(definition).getRecords();

    for (BRebateBill bill : bills) {
      if (BizStates.EFFECT.equals(bill.getBizState())) {
        String uuid = bill.getUuid();
        for (Account account : accounts) {
          String srcUuid = account.getAcc1().getSourceBill().getBillUuid();
          if (uuid.equals(srcUuid)) {
            bill.setHasAccount(true);
            break;
          }
        }
      }
    }
  }

  @RequestMapping(value = "getContractDoc", method = RequestMethod.GET)
  @ResponseBody
  public BContract getContractDoc(@RequestParam String contractUuid) {
    ContractDoc contractDoc = contractService.get(contractUuid);
    if (contractDoc == null) {
      return null;
    }

    BContract contract = new BContract();
    Contract doc = contractDoc.getContract();

    contract.setUuid(doc.getId());
    contract.setCode(doc.getSerialNumber());
    contract.setName(doc.getSignboard());
    contract.setStore(doc.getStore());
    contract.setTenant(doc.getTenant());

    contract.setBeginDate(doc.getBeginDate());
    contract.setEndDate(doc.getEndDate());
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
          contract.setRebateSubject(accountTerm.getSubject());
          contract.setRebateDirection(accountTerm.getDirection());
          contract.setBackTaxRate(accountTerm.getTaxRate());
        } else if (accountTerm.getFormula() instanceof BankRateFormula) {
          contract.setBankRate(true);
          contract.setPoundageInvoice(accountTerm.isInvoice());
          contract.setPoundageSubject(accountTerm.getSubject());
          contract.setPoundageDirection(accountTerm.getDirection());
          contract.setPoundageTaxRate(accountTerm.getTaxRate());
        }
      } else if (term instanceof SettleTerm) {// 账款周期条款
        SettleTerm settleTerm = (SettleTerm) term;
        // 得到账款类型为销售返款
        if (settleTerm.getAccountTypes().contains(UsageType.salePayment.name())) {
          for (SettlePeriod settle : settleTerm.getSettles()) {
            contract.getRanges().add(new DateRange(settle.getBeginDate(), settle.getEndDate()));
          }
        }
      }
    }

    return contract;
  }

  /**
   * 查询得到未开的最小销售额返款开始日期
   * 
   * @param contractId
   * @return
   */
  private DateRange getLstUnaccountedSette(String contractId) {
    List<SubjectUsage> usages = rsAccDataService.getAllSubjectUsage();
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

    QueryResult<AccSettle> qr = rsAccSettleService.query(new BeanQueryDefinitionConverter()
        .convert(def));
    if (qr.getRecords().isEmpty()) {
      return null;
    }
    AccSettle settle = qr.getRecords().get(0);
    return new DateRange(settle.getBeginDate(), settle.getEndDate());
  }

  @Override
  protected RebateBillQueryBuilder getQueryBuilder() {
    return getAppCtx().getBean(RebateBillQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return "销售额返款单";
  }

  @Override
  protected String getServiceBeanId() {
    return RebateBillService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return RebateBills.OBJECT_NAME_PAY;
  }

}
