/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	ReceiptController.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月12日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.receipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hd123.m3.account.commons.CounterpartType;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.bank.BankService;
import com.hd123.m3.account.service.bank.Banks;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.commons.biz.FetchParts;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.rs.util.FetchPartUtils;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountCommonComponent;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.payment.AbstractPaymentController;
import com.hd123.m3.cre.controller.account.payment.converter.PaymentBizConverter;
import com.hd123.m3.cre.controller.account.payment.model.BPayment;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentCollectionLine;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentDepositDefrayal;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineCash;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentLineDeposit;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentOverdueLine;
import com.hd123.m3.cre.controller.account.payment.model.DepositTotal;
import com.hd123.m3.cre.controller.account.payment.receipt.common.ReceiptUtil;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 收款单控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/payment/receipt/*")
public class ReceiptController extends AbstractPaymentController {
  /** 键值：收款单配置项 */
  private static final String KEY_OPTION = "receiptOption";

  @Autowired
  AccountService accountService;
  @Autowired
  ReceiptCommonComponent receiptCommonComponent;
  @Autowired
  AccountCommonComponent accountCommonComponent;
  @Autowired
  ReceiptCommonController commonController;
  @Autowired
  protected BankService bankService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    Set<String> resourceIds = new HashSet<String>();
    resourceIds.add(AccountPermResourceDef.RESOURCE_RECEIPT);
    resourceIds.add(AccountPermResourceDef.RESOURCE_RECINVOICEREG);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        resourceIds));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    // 配置项
    moduleContext.put(KEY_OPTION, receiptCommonComponent.getOpion());
  }

  @Override
  public SummaryQueryResult query(@RequestBody QueryFilter queryFilter) {
    if (queryFilter == null) {
      return new SummaryQueryResult();
    }

    queryFilter.getFilter().put(Payments.CONDITION_DIRECTION_EQUALS, Direction.RECEIPT);

    return super.query(queryFilter);
  }

  @Override
  protected void decorateRecords(SummaryQueryResult queryResult) {
    List<BPayment> payments = new ArrayList<BPayment>();
    for (Object record : queryResult.getRecords()) {
      BPayment payment = PaymentBizConverter.getInstance().convert((Payment) record);
      payments.add(payment);
    }
    queryResult.setRecords(payments);
  }

  @Override
  protected BPayment doBeforeSave(BPayment entity) {
    return commonController.apportion(entity);
  }

  @Override
  protected int getDirection() {
    return Direction.RECEIPT;
  }

  @Override
  protected PaymentDefrayalType getDefaultPaymentDefrayalType() {
    return PaymentDefrayalType.valueOf(optionComponent.getDefaultDefrayalType());
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected String getObjectName() {
    return Payments.OBJECT_NAME_RECEIPT;
  }

  @Override
  protected void doAfterCreate(BPayment payment) throws M3ServiceException {
    super.doAfterCreate(payment);
    decorateRemainTotal(payment);
  }

  private void decorateRemainTotal(BPayment payment) throws M3ServiceException {

    QueryFilter filter = new QueryFilter();
    filter.getFilter().put("accountUnit", payment.getAccountUnit().getUuid());
    filter.getFilter().put("counterpart", payment.getCounterpart().getUuid());

    DepositTotal depositTotal = commonController.loadAdvances(filter);

    payment.setDepositBalance(depositTotal.getTotal());
    payment.setContractAdvances(depositTotal.getAdvances());
  }

  protected void doAfterLoad(BPayment receipt) throws M3ServiceException {
    if (BizStates.INEFFECT.equals(receipt.getBizState())) {
      loadRemainTotal(receipt);
    }

    for (BPaymentCollectionLine line : receipt.getCollectionLines()) {
      line.setShouldReceiptTotal(line.getTotal().getTotal());
    }

    if (PaymentDefrayalType.lineSingle.equals(receipt.getDefrayalType())) {
      for (BPaymentCollectionLine line : receipt.getCollectionLines()) {
        if (line.getCashes().isEmpty() == false) {
          line.setPaymentType(line.getCashes().get(0).getPaymentType());
          line.setBank(line.getCashes().get(0).getBank());
        }
      }

    }
    decorateLineInvoiceByAccId(receipt);

    if (BizStates.EFFECT.equals(receipt.getBizState())) {
      boolean isProprietor = CounterpartType.PROPRIETOR.equals(receipt.getCounterpartType());
      if (isProprietor) {
        receipt.setCanInvoice(false);
      } else {
        receipt.setCanInvoice(existActiveAccounts(receipt));
      }
    }

  }

  protected void setDefaultBank(BPayment payment) {
    if (payment.getDefrayalType().equals(PaymentDefrayalType.bill)) {
      return;
    }
    Set<String> subjects = new HashSet<String>();
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      subjects.add(line.getAcc1().getSubject().getUuid());
    }
    List<Bank> banks = getBanks(payment.getAccountUnit().getUuid(), subjects);
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      Bank bank = getBank(banks, line.getAcc1().getSubject().getUuid());
      if (bank != null) {
        List<BPaymentLineCash> lines = new ArrayList<BPaymentLineCash>();
        BPaymentLineCash cash = new BPaymentLineCash();
        cash.setBank(bank);
        lines.add(cash);
        line.setCashes(lines);
      }
    }
  }

  private List<Bank> getBanks(String accountUnit, Set<String> subjects) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Banks.CONDITION_STORE_EQUALS, accountUnit);
    definition.addCondition(Banks.FIELD_ENABLED);
    definition.addCondition(Banks.CONDITION_CONTAINS_SUBJECT, subjects.toArray());
    definition.getFetchParts().add("subjects");
    return bankService.query(definition).getRecords();

  }

  private Bank getBank(List<Bank> banks, String subjectUuid) {
    for (Bank bank : banks) {
      if (bank.getSubjects() != null) {
        for (UCN subject : bank.getSubjects()) {
          if (subject.getUuid().equals(subjectUuid)) {
            return bank;
          }
        }
      }
    }

    return null;
  }

  private boolean existActiveAccounts(BPayment payment) {
    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);

    queryDef.addCondition(Accounts.CONDITION_ACCOUNTUNIT_UUID_EQUALS, payment.getAccountUnit()
        .getUuid());

    queryDef.addCondition(Accounts.CONDITION_PAYMENT_UUID_EQUALS, payment.getUuid());
    queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, payment.getDirection());
    queryDef.addCondition(Accounts.CONDITION_CAN_INVOICE);

    List<Account> accounts = accountService.query(queryDef).getRecords();

    if (accounts.isEmpty()) {
      return false;
    }

    return true;

  }

  /** 加载预存款科目余额 */
  private void loadRemainTotal(BPayment bill) throws M3ServiceException {
    String accountUnitUuid = bill.getAccountUnit().getUuid();
    String couonterPartUuid = bill.getCounterpart().getUuid();

    decorateRemainTotal(bill);

    for (BPaymentDepositDefrayal d : bill.getDeposits()) {
      String billUuid = (d.getContract() == null || StringUtil.isNullOrBlank(d.getContract()
          .getUuid())) ? "-" : d.getContract().getUuid();
      BigDecimal remainTotal = receiptCommonComponent.getDepositSubjectRemainTotal(accountUnitUuid,
          couonterPartUuid, billUuid, d.getSubject().getUuid());
      d.setRemainTotal(remainTotal);
    }

    if (PaymentDefrayalType.bill.name().equals(bill.getDefrayalType()) == false) {
      for (BPaymentAccountLine line : bill.getAccountLines()) {
        for (BPaymentLineDeposit d : line.getDeposits()) {
          String billUuid = (d.getContract() == null || StringUtil.isNullOrBlank(d.getContract()
              .getUuid())) ? "-" : d.getContract().getUuid();
          BigDecimal remainTotal = receiptCommonComponent.getDepositSubjectRemainTotal(
              accountUnitUuid, couonterPartUuid, billUuid, d.getSubject().getUuid());
          d.setRemainTotal(remainTotal);
        }
      }
      for (BPaymentOverdueLine line : bill.getOverdueLines()) {
        for (BPaymentLineDeposit d : line.getDeposits()) {
          String billUuid = (d.getContract() == null || StringUtil.isNullOrBlank(d.getContract()
              .getUuid())) ? "-" : d.getContract().getUuid();
          BigDecimal remainTotal = receiptCommonComponent.getDepositSubjectRemainTotal(
              accountUnitUuid, couonterPartUuid, billUuid, d.getSubject().getUuid());
          d.setRemainTotal(remainTotal);
        }
      }
    }
  }

  /** 根据账款id加载账款，目的是刷新账款明细的发票信息 */
  private void decorateLineInvoiceByAccId(BPayment payment) {
    if (payment == null || payment.getBizState().equals(BizStates.EFFECT) == false) {
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
    QueryResult<Account> result = accountService.query(def);
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
        accountLine.setInvoiceCodeStr(ReceiptUtil.getInvoiceCodeStr(accs));
        accountLine.setInvoiceNumberStr(ReceiptUtil.getInvoiceNumbertr(accs));
      }
      if (collectionLine != null) {
        collectionLine.setInvoiceCodeStr(ReceiptUtil.getInvoiceCodeStr(accs));
        collectionLine.setInvoiceNumberStr(ReceiptUtil.getInvoiceNumbertr(accs));
      }
    }

  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("收款单")
    public String moduleCaption();

  }

}
