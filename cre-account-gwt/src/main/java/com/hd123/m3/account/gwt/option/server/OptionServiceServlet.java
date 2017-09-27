/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.bpm.service.execution.Definition;
import com.hd123.bpm.service.execution.ProcessService;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BConfigOption;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.commons.client.biz.BOption;
import com.hd123.m3.account.gwt.commons.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.commons.client.biz.BReceiptOverdueDefault;
import com.hd123.m3.account.gwt.commons.server.DefaultOptionLoader;
import com.hd123.m3.account.gwt.option.client.BIncompleteMonthByRealDaysOption;
import com.hd123.m3.account.gwt.option.client.BRebateByBillOption;
import com.hd123.m3.account.gwt.option.client.BStoreMonthDaysOption;
import com.hd123.m3.account.gwt.option.client.rpc.OptionService;
import com.hd123.m3.account.gwt.option.client.ui.gadget.dataprovider.filter.SubjectFilter;
import com.hd123.m3.account.gwt.option.intf.client.perm.OptionPermDef;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.service.option.AccountSettleOption;
import com.hd123.m3.account.service.paymentnotice.PaymentNotices;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.account.service.paymenttype.PaymentTypes;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.QueryDefinitionUtil;
import com.hd123.m3.commons.biz.query.converter.FlecsQueryDefinitionConverter;
import com.hd123.m3.commons.biz.store.MStore;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.m3.commons.biz.tax.TaxType;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.data.receiptor.M3UserOrgService;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.commons.rs.service.store.RSStoreService;
import com.hd123.m3.commons.rs.service.store.Stores;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryCondition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ArrayListConverter;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author zhuhairui
 * 
 */
public class OptionServiceServlet extends AccRemoteServiceServlet implements OptionService {

  private static final long serialVersionUID = 8174809846424288207L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        OptionPermDef.RESOURCE_OPTION_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);

    String statementDefaultBpm = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_STATEMENTDEFAULTBPM);
    moduleContext.put(OptionService.KEY_STATEMENTDEFAULTBPMKEY, statementDefaultBpm);
    moduleContext.put(OptionService.KEY_STATEMENTBPMKEYPREFIX, Statements.OBJECT_NAME);

    moduleContext.put(OptionService.KEY_INVOICE_TYPE, CollectionUtil.toString(getInvoiceTypes()));

    String paymentNoticeDefaultBpm = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_PAYMENTNOTICDEFAULTBPM);
    moduleContext.put(OptionService.KEY_PAYMENTNOTICEDEFAULTBPMKEY, paymentNoticeDefaultBpm);
    moduleContext.put(OptionService.KEY_PAYMENTNOTICEBPMKEYPREFIX, PaymentNotices.OBJECT_NAME);
  }

  @Override
  public String getObjectName() {
    return null;
  }

  @Override
  public BOption getOption() throws Exception {
    try {
      BOption option = new BOption();
      option.setConfigOption(getConfigOption());
      option.setDefaultOption(getDefaultOption());
      return option;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BConfigOption getConfigOption() throws Exception {
    try {
      BConfigOption configOption = new BConfigOption();

      String monthDaysString = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_MONTH_DAYS);

      try {
        configOption.setMonthDays(StringUtil.isNullOrBlank(monthDaysString) ? BigDecimal.ZERO
            : new BigDecimal(monthDaysString));
      } catch (Exception e) {
        configOption.setMonthDays(BigDecimal.ZERO);
      }

      configOption.setIncompleteMonthByRealDays(Boolean.valueOf(getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.OPTION_INCOMPLETEMONTH_REALDAYS)));

      configOption.setRebateByBill(Boolean.valueOf(getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.OPTION_REBATE_SINGLE)));

      configOption.setReceiptBankRequired(Boolean.valueOf(getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.OPTION_RECEIPT_BANK_REQUIRED)));

      configOption.setExtinvSystemRequired(Boolean.valueOf(getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.EXTINVSYSTEM_ENABLED)));

      configOption.setAccObjectEnbaled(Boolean.valueOf(getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.OPTION_ACCOBJECT_ENABLED)));

      configOption.setByDepositEnabled(Boolean.valueOf(getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.OPTION_BYDEPOSIT_ENABLED)));

      configOption.setGenZeroStatement(Boolean.valueOf(getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.OPTION_GENZEROSTATEMENT)));

      configOption.setReceiptEqualsUnpayed(Boolean.valueOf(getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.OPTION_RECEIPT_EQUALS_UNPAYED)));

      String scaleString = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_SCALE);
      configOption.setScale(StringUtil.isNullOrBlank(scaleString) ? 2 : Integer
          .valueOf(scaleString));

      String roundingModeString = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_ROUNDING_MODE);
      configOption
          .setRoundingMode(StringUtil.isNullOrBlank(roundingModeString) ? RoundingMode.HALF_UP
              .toString() : roundingModeString);

      String statementProcessDefinitionKey = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_STATEMENTDEFAULTBPM);
      configOption.setStatementDefaultBPMKey(statementProcessDefinitionKey);
      BProcessDefinition statementDef = getProcessDefinition(statementProcessDefinitionKey);
      configOption.setStatementDefaultBPM(statementDef == null ? null : statementDef.getName());

      String paymentNoticeProcessDefinitionKey = getOptionService().getOption(
          AccOptions.OPTION_PATH, AccOptions.OPTION_PAYMENTNOTICDEFAULTBPM);
      configOption.setPaymentNoticeDefaultBPMKey(paymentNoticeProcessDefinitionKey);
      BProcessDefinition paymentNoticeDef = getProcessDefinition(paymentNoticeProcessDefinitionKey);
      configOption.setPaymentNoticeDefaultBPM(paymentNoticeDef == null ? null : paymentNoticeDef
          .getName());
      String receiptOverdueDefault = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_OVERDUE_DEFAULT);
      configOption
          .setReceiptOverdueDefaults(receiptOverdueDefault == null ? BReceiptOverdueDefault.calcValue
              : BReceiptOverdueDefault.valueOf(receiptOverdueDefault));

      String settleNoRuleDay = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_SETTLENORULEDAY);
      if (StringUtil.isNullOrBlank(settleNoRuleDay) == false
          && "null".equals(settleNoRuleDay) == false) {
        configOption.setSettleNoRuleDay(Integer.valueOf(settleNoRuleDay));
      }

      String receiptPaymentType = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_PAYMENTTYPE);
      String receiptPaymentTypeLimit = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_PAYMENTTYPE_LIMITS);
      configOption.setReceiptPaymentType(getPaymentTypeByCode(receiptPaymentType));
      configOption.setReceiptPaymentTypeLimits(receiptPaymentTypeLimit == null ? BigDecimal.ZERO
          : new BigDecimal(receiptPaymentTypeLimit));

      String defalutReceiptPaymentType = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_DEFAULT_RECEIPT_PAYMENTTYPE);
      configOption.setDefalutReceiptPaymentType(defalutReceiptPaymentType);

      String defalutInvoiceType = getOptionService().getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_DEFAULT_INVOICETYPE);
      configOption.setDefalutInvoiceType(defalutInvoiceType);

      return configOption;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDefaultOption getDefaultOption() throws Exception {
    try {
      return DefaultOptionLoader.getInstance().getDefaultOption();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void setConfigOption(BConfigOption configOption) throws Exception {
    try {
      BeanOperateContext operateCtx = ConvertHelper.getOperateContext(getSessionUser());
      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_MONTH_DAYS,
          String.valueOf(configOption.getMonthDays()), operateCtx);
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_INCOMPLETEMONTH_REALDAYS,
          String.valueOf(configOption.isIncompleteMonthByRealDays()), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_REBATE_SINGLE,
          String.valueOf(configOption.isRebateByBill()), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_GENZEROSTATEMENT,
          String.valueOf(configOption.isGenZeroStatement()), operateCtx);
      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE,
          String.valueOf(configOption.getScale()), operateCtx);
      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_ROUNDING_MODE,
          configOption.getRoundingMode(), operateCtx);
      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_STATEMENTDEFAULTBPM,
          configOption.getStatementDefaultBPMKey(), operateCtx);
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_PAYMENTNOTICDEFAULTBPM, configOption.getPaymentNoticeDefaultBPMKey(),
          operateCtx);
      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SETTLENORULEDAY,
          String.valueOf(configOption.getSettleNoRuleDay()), operateCtx);
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_OVERDUE_DEFAULT,
          configOption.getReceiptOverdueDefaults().name(), operateCtx);
      getOptionService().setOption(
          AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_PAYMENTTYPE,
          configOption.getReceiptPaymentType() == null ? null : configOption
              .getReceiptPaymentType().getCode(), operateCtx);
      getOptionService().setOption(
          AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_PAYMENTTYPE_LIMITS,
          configOption.getReceiptPaymentTypeLimits() == null ? BigDecimal.ZERO.toString()
              : configOption.getReceiptPaymentTypeLimits().toString(), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_RECEIPT_BANK_REQUIRED,
          String.valueOf(configOption.isReceiptBankRequired()), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.EXTINVSYSTEM_ENABLED,
          String.valueOf(configOption.isExtinvSystemRequired()), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_ACCOBJECT_ENABLED,
          String.valueOf(configOption.isAccObjectEnbaled()), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_BYDEPOSIT_ENABLED,
          String.valueOf(configOption.isByDepositEnabled()), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_DEFAULT_RECEIPT_PAYMENTTYPE,
          configOption.getDefalutReceiptPaymentType(), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_DEFAULT_INVOICETYPE,
          configOption.getDefalutInvoiceType(), operateCtx);

      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_EQUALS_UNPAYED,
          String.valueOf(configOption.isReceiptEqualsUnpayed()), operateCtx);

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void setDefaultOption(BDefaultOption defaultOption) throws Exception {
    try {
      BeanOperateContext operateCtx = ConvertHelper.getOperateContext(getSessionUser());
      getOptionService().setOption(AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_PAYMENT,
          defaultOption.getPaymentType() == null ? null : defaultOption.getPaymentType().getUuid(),
          operateCtx);
      getOptionService().setOption(
          AccOptions.DEFAULT_PATH,
          AccOptions.DEFAULT_TAXRATE,
          defaultOption.getTaxRate() == null ? null : JsonUtil
              .objectToJson(convertTaxRate(defaultOption.getTaxRate())), operateCtx);
      getOptionService().setOption(
          AccOptions.DEFAULT_PATH,
          AccOptions.DEFAULT_PRERECEIVE_SUBJECT,
          defaultOption.getPreReceiveSubject() == null ? null : defaultOption
              .getPreReceiveSubject().getUuid(), operateCtx);
      getOptionService().setOption(
          AccOptions.DEFAULT_PATH,
          AccOptions.DEFAULT_PREPAY_SUBJECT,
          defaultOption.getPrePaySubject() == null ? null : defaultOption.getPrePaySubject()
              .getUuid(), operateCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void saveStoreMonthDaysOptions(List<BStoreMonthDaysOption> options) throws Exception {

    BeanOperateContext operateCtx = ConvertHelper.getOperateContext(getSessionUser());

    /** 获取默认的财务月天数 */
    String monthDaysString = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_MONTH_DAYS);
    if (StringUtil.isNullOrBlank(monthDaysString)) {
      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_MONTH_DAYS,
          String.valueOf(BigDecimal.ZERO), operateCtx);
    }

    /** 先把原值设置为null，如果项目过多会有性能问题，可以优化. */
    List<BStoreMonthDaysOption> oldOptions = getStoreMonthDaysOptions();
    for (BStoreMonthDaysOption oldOption : oldOptions) {
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_MONTH_DAYS + "/" + oldOption.getStore().getUuid(), null, operateCtx);
    }

    /** 保存新的配置值 */
    for (BStoreMonthDaysOption option : options) {
      if (option.isActualMonthDays()) {
        option.setMonthDays(BigDecimal.ZERO);
      }
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_MONTH_DAYS + "/" + option.getStore().getUuid(),
          String.valueOf(option.getMonthDays()), operateCtx);
    }

  }

  @Override
  public List<BStoreMonthDaysOption> getStoreMonthDaysOptions() throws Exception {
    List<BUCN> stores = queryAccountUnits();

    if (stores == null || stores.isEmpty()) {
      return new ArrayList<BStoreMonthDaysOption>();
    }

    List<BStoreMonthDaysOption> bizOptions = new ArrayList<BStoreMonthDaysOption>();

    List<AccountSettleOption> options = getOptionService().getAccountSettleOptions(
        getUuids(stores), AccountSettleOption.FETCH_MONTHDAYS);
    for (AccountSettleOption option : options) {
      BUCN store = getBUCN(stores, option.getStoreUuid());
      if (store == null || option.getMonthDays() == null) {
        continue;
      }

      BStoreMonthDaysOption bizOption = new BStoreMonthDaysOption();
      bizOption.setMonthDays(option.getMonthDays());
      if (BigDecimal.ZERO.compareTo(bizOption.getMonthDays()) == 0) {
        bizOption.setActualMonthDays(true);
      }
      bizOption.setStore(store);

      bizOptions.add(bizOption);
    }

    return bizOptions;
  }

  @Override
  public void saveRebateByBillOption(List<BRebateByBillOption> options) throws Exception {
    BeanOperateContext operateCtx = ConvertHelper.getOperateContext(getSessionUser());

    /** 获取默认的返款单是否按笔收款 */
    String rebateByBillStr = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_REBATE_SINGLE);
    if (StringUtil.isNullOrBlank(rebateByBillStr)) {
      // 默认为false
      getOptionService().setOption(AccOptions.OPTION_PATH, AccOptions.OPTION_REBATE_SINGLE,
          String.valueOf(false), operateCtx);
    }

    /** 先把原值设置为null，如果项目过多会有性能问题，可以优化. */
    List<BRebateByBillOption> oldOptions = getRebateByBillOptions();
    for (BRebateByBillOption oldOption : oldOptions) {
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_REBATE_SINGLE + "/" + oldOption.getStore().getUuid(), null, operateCtx);
    }

    /** 保存新的配置值 */
    for (BRebateByBillOption option : options) {
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_REBATE_SINGLE + "/" + option.getStore().getUuid(),
          String.valueOf(option.isRebateByBill()), operateCtx);
    }
  }

  @Override
  public List<BRebateByBillOption> getRebateByBillOptions() throws Exception {
    List<BUCN> stores = queryAccountUnits();
    List<BRebateByBillOption> bizOptions = new ArrayList<BRebateByBillOption>();

    if (stores == null || stores.isEmpty()) {
      return bizOptions;// 至少返回一个空集合
    }

    List<AccountSettleOption> options = getOptionService().getAccountSettleOptions(
        getUuids(stores), AccountSettleOption.FETCH_REBATEBYBILL);
    for (AccountSettleOption option : options) {
      BUCN store = getBUCN(stores, option.getStoreUuid());
      if (store == null || option.getRebateByBill() == null) {
        continue;
      }
      BRebateByBillOption bizOption = new BRebateByBillOption();
      bizOption.setRebateByBill(option.getRebateByBill());
      bizOption.setStore(store);

      bizOptions.add(bizOption);
    }
    return bizOptions;
  }

  @Override
  public List<BIncompleteMonthByRealDaysOption> getIncompleteMonthByRealDaysOptions()
      throws Exception {
    List<BUCN> stores = queryAccountUnits();

    if (stores == null || stores.isEmpty()) {
      return new ArrayList<BIncompleteMonthByRealDaysOption>();
    }

    List<BIncompleteMonthByRealDaysOption> bizOptions = new ArrayList<BIncompleteMonthByRealDaysOption>();

    List<AccountSettleOption> options = getOptionService().getAccountSettleOptions(
        getUuids(stores), AccountSettleOption.FETCH_INCOMPLETEMONTHBYREALDAYS);
    for (AccountSettleOption option : options) {
      BUCN store = getBUCN(stores, option.getStoreUuid());
      if (store == null || option.getIncompleteMonthByRealDays() == null) {
        continue;
      }

      BIncompleteMonthByRealDaysOption bizOption = new BIncompleteMonthByRealDaysOption();
      bizOption.setIncompleteMonthByRealDays(option.getIncompleteMonthByRealDays());
      bizOption.setStore(store);

      bizOptions.add(bizOption);
    }

    return bizOptions;
  }

  @Override
  public void saveIncompleteMonthByRealDaysOption(List<BIncompleteMonthByRealDaysOption> options)
      throws Exception {
    BeanOperateContext operateCtx = ConvertHelper.getOperateContext(getSessionUser());

    /** 获取默认的非整月月末是否按实际天数计算 */
    String incompleteMonthByRealDaysStr = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_INCOMPLETEMONTH_REALDAYS);
    if (StringUtil.isNullOrBlank(incompleteMonthByRealDaysStr)) {
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_INCOMPLETEMONTH_REALDAYS, String.valueOf(false), operateCtx);
    }

    /** 先把原值设置为null，如果项目过多会有性能问题，可以优化. */
    List<BIncompleteMonthByRealDaysOption> oldOptions = getIncompleteMonthByRealDaysOptions();
    for (BIncompleteMonthByRealDaysOption oldOption : oldOptions) {
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_INCOMPLETEMONTH_REALDAYS + "/" + oldOption.getStore().getUuid(), null,
          operateCtx);
    }

    /** 保存新的配置值 */
    for (BIncompleteMonthByRealDaysOption option : options) {
      getOptionService().setOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_INCOMPLETEMONTH_REALDAYS + "/" + option.getStore().getUuid(),
          String.valueOf(option.isIncompleteMonthByRealDays()), operateCtx);
    }

  }

  @Override
  public BUCN getSubjectByCode(String code) throws Exception {
    try {
      if (StringUtil.isNullOrBlank(code))
        return null;
      Subject subject = getSubjectService().getByCode(code);
      if (subject == null)
        return null;
      BUCN result = new BUCN();
      result.setUuid(subject.getUuid());
      result.setCode(subject.getCode());
      result.setName(subject.getName());
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<BUCN> querySubjects(CodeNameFilter filter) throws Exception {
    try {

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.getConditions().add(buildCondition(Subjects.CONDITION_STATE_EQUALS, Boolean.TRUE));
      if (StringUtil.isNullOrBlank(filter.getCode()) == false) {
        queryDef.getConditions().add(
            buildCondition(Subjects.CONDITION_CODE_STARTWITH, filter.getCode()));
      }

      if (StringUtil.isNullOrBlank(filter.getName()) == false) {
        queryDef.getConditions()
            .add(buildCondition(Subjects.CONDITION_NAME_LIKE, filter.getName()));
      }

      if (((SubjectFilter) filter).getSubjectType() != null) {
        queryDef.getConditions().add(
            buildCondition(Subjects.CONDITION_SUBJECT_TYPE_EQUALS,
                ((SubjectFilter) filter).getSubjectType()));
      }

      if (((SubjectFilter) filter).getDirectionType() != 0) {
        queryDef.getConditions().add(
            buildCondition(Subjects.CONDITION_DIRECTION_EQUALS,
                ((SubjectFilter) filter).getDirectionType()));
      }

      String orderField = Subjects.ORDER_BY_CODE;
      QueryOrderDirection dir = QueryOrderDirection.asc;
      if (filter.getOrders().isEmpty() == false) {
        if (ObjectUtil.equals("code", filter.getOrders().get(0).getFieldName()))
          orderField = Subjects.ORDER_BY_CODE;
        else if (ObjectUtil.equals("name", filter.getOrders().get(0).getFieldName()))
          orderField = Subjects.ORDER_BY_NAME;

        dir = QueryOrderDirection.valueOf(filter.getOrders().get(0).getDir().name());
      }

      queryDef.getOrders().clear();
      queryDef.getOrders().add(QueryDefinitionUtil.createQueryOrder(orderField, dir));
      queryDef.setPage(filter.getPage());
      queryDef.setPageSize(filter.getPageSize());
      QueryResult<Subject> qr = getSubjectService().query(queryDef);

      RPageData<BUCN> result = new RPageData<BUCN>();
      RPageDataUtil.copyPagingInfo(qr, result);

      List<BUCN> subjects = new ArrayList<BUCN>();
      for (Subject record : qr.getRecords()) {
        BUCN subject = new BUCN();
        subject.setUuid(record.getUuid());
        subject.setCode(record.getCode());
        subject.setName(record.getName());
        subjects.add(subject);
      }
      result.setValues(subjects);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BUCN getPaymentTypeByCode(String code) throws Exception {
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
  public RPageData<BUCN> queryPaymentTypes(CodeNameFilter filter) throws Exception {
    try {

      List<BUCN> paymentTypes = new ArrayList<BUCN>();
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addFlecsCondition(PaymentTypes.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
      List<PaymentType> records = getPaymentTypeService().query(def).getRecords();
      
      for (PaymentType record : records) {
        if (StringUtil.isNullOrBlank(filter.getCode()) == false
            && record.getCode().toLowerCase().startsWith(filter.getCode().trim().toLowerCase()) == false) {
          continue;
        }

        if (StringUtil.isNullOrBlank(filter.getName()) == false
            && record.getName().contains(filter.getName()) == false) {
          continue;
        }
        BUCN paymentType = new BUCN();
        paymentType.setUuid(record.getUuid());
        paymentType.setCode(record.getCode());
        paymentType.setName(record.getName());
        paymentTypes.add(paymentType);
      }

      if (!filter.getOrders().isEmpty())
        sortBUCNList(paymentTypes, filter.getOrders().get(0));

      return RPageDataUtil.flip(paymentTypes, filter.getPage(), filter.getPageSize());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /**
   * 构造查询条件。
   * 
   * @param operation
   * @param parameters
   * @return 查询条件。
   */
  public static QueryCondition buildCondition(String operation, Object... parameters) {
    QueryCondition condition = new QueryCondition();
    condition.setOperation(operation);
    List<Object> params = new ArrayList<Object>();
    for (Object parameter : parameters) {
      if (parameter == null)
        continue;
      if (parameter instanceof String)
        parameter = ((String) parameter).trim();
      params.add(parameter);
    }
    condition.setParameters(params);
    if (params.isEmpty())
      condition.getParameters().add("");
    return condition;
  }

  @Override
  public List<BProcessDefinition> queryProcessDefinition(String keyPrefix) throws Exception {
    try {
      if (StringUtil.isNullOrBlank(keyPrefix)) {
        return new ArrayList<BProcessDefinition>();
      }
      List<BProcessDefinition> result = new ArrayList<BProcessDefinition>();
      List<Definition> defs = getProcessService().getDefinitionsByKeyPrefix(keyPrefix);
      for (Definition def : defs) {
        result.add(new BProcessDefinition(def.getDefinitionKey(), def.getName()));
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BProcessDefinition getProcessDefinition(String key) throws ClientBizException {
    if (StringUtil.isNullOrBlank(key))
      return null;
    try {
      Definition def = getProcessService().getDefinitionByKey(key);
      if (def == null)
        return null;

      return new BProcessDefinition(def.getDefinitionKey(), def.getName());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private Map<String, String> getInvoiceTypes() {
    Map<String, String> mapInvoiceTypes = new HashMap<String, String>();
    try {
      for (BInvoiceType type : InvoiceTypeUtils.getAll()) {
        mapInvoiceTypes.put(type.getCode(), type.getName());
      }
      return mapInvoiceTypes;
    } catch (Exception e) {
      return null;
    }
  }

  private void sortBUCNList(List<BUCN> paymentTypes, final Order order) {
    if (paymentTypes == null || paymentTypes.isEmpty() || order == null)
      return;

    Collections.sort(paymentTypes, new Comparator<BUCN>() {
      @Override
      public int compare(BUCN o1, BUCN o2) {
        int dir = OrderDir.asc.compareTo(order.getDir()) == 0 ? 1 : -1;
        if (ObjectUtil.equals("code", order.getFieldName())) {
          return dir * o1.getCode().compareTo(o2.getCode());
        } else if (ObjectUtil.equals("name", order.getFieldName())) {
          return dir * o1.getName().compareTo(o2.getName());
        }
        return 0;
      }
    });
  }

  /** 将税率B对象转化成接口对象 */
  private TaxRate convertTaxRate(BTaxRate source) {
    if (source == null)
      return null;

    TaxRate t = new TaxRate();
    t.setName(source.getName());
    if (source.getTaxType() != null)
      t.setTaxType(TaxType.valueOf(source.getTaxType().name()));

    if (source.getRate() != null)
      t.setRate(new BigDecimal(source.getRate().toString()));
    return t;
  }

  private List<BUCN> queryAccountUnits() throws ClientBizException {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      // 受组织限制
      List<String> orgIds = getUserOrgService().getUserPermOrgIds(getSessionUser().getId());
      if (orgIds.isEmpty()) {
        return new ArrayList<BUCN>();
      }
      queryDef.addCondition(Stores.CONDITION_ORG_ID_IN, orgIds.toArray());

      QueryResult<MStore> qr = getStoreService().query(
          new FlecsQueryDefinitionConverter().convert(queryDef));

      return ArrayListConverter.newConverter(UCNBizConverter.getInstance())
          .convert(qr.getRecords());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BUCN> getPaymentTypes() throws Exception {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addFlecsCondition(PaymentTypes.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
      List<PaymentType> paymentTypes = getPaymentTypeService().query(def).getRecords();
       
      if (paymentTypes == null || paymentTypes.isEmpty())
        return new ArrayList<BUCN>();

      List<BUCN> payments = new ArrayList<BUCN>();
      for (PaymentType paymentType : paymentTypes) {
        BUCN result = new BUCN(paymentType.getUuid(), paymentType.getCode(), paymentType.getName());
        payments.add(result);
      }

      return payments;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private List<String> getUuids(List<BUCN> stores) {
    List<String> uuids = new ArrayList<String>();
    if (stores == null) {
      return uuids;
    }
    for (BUCN store : stores) {
      uuids.add(store.getUuid());
    }

    return uuids;

  }

  private BUCN getBUCN(List<BUCN> ucns, String uuid) {
    if (ucns == null || uuid == null) {
      return null;
    }

    for (BUCN ucn : ucns) {
      if (uuid.equals(ucn.getUuid())) {
        return ucn;
      }
    }
    return null;
  }

  private static M3UserOrgService getUserOrgService() {
    return M3ServiceFactory.getBean(M3UserOrgService.DEFAULT_CONTEXT_ID, M3UserOrgService.class);
  }

  private RSStoreService getStoreService() {
    return M3ServiceFactory.getService(RSStoreService.class);
  }

  private PaymentTypeService getPaymentTypeService() throws ClientBizException {
    return M3ServiceFactory.getService(PaymentTypeService.class);
  }

  private SubjectService getSubjectService() throws ClientBizException {
    return M3ServiceFactory.getService(SubjectService.class);
  }

  private ProcessService getProcessService() {
    return M3ServiceFactory.getService(ProcessService.class);
  }

}