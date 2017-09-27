/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AbstractPaymentController.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月8日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.CounterpartType;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.account.service.payment.PaymentService;
import com.hd123.m3.account.service.payment.PaymentTotal;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.flow.BizFlow;
import com.hd123.m3.commons.biz.flow.BizState;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.cre.controller.account.common.AccountCommonComponent;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.model.SUser;
import com.hd123.m3.cre.controller.account.common.util.CommonUtil;
import com.hd123.m3.cre.controller.account.payment.common.PaymentQueryBuilder;
import com.hd123.m3.cre.controller.account.payment.converter.BizPaymentConverter;
import com.hd123.m3.cre.controller.account.payment.converter.PaymentBizConverter;
import com.hd123.m3.cre.controller.account.payment.model.BPayment;
import com.hd123.m3.cre.controller.account.payment.model.BPaymentAccountLine;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.util.DateUtil;

/**
 * 收付款单控制器基类
 * 
 * @author LiBin
 *
 */
public abstract class AbstractPaymentController extends BizFlowModuleController {

  /** 计算精度,小数位数 */
  protected static final String KEY_SCALE = "scale";
  /** 计算精度,舍入算法 */
  protected static final String KEY_ROUNDING_MODE = "roundingMode";
  /** 键值：单据类型 */
  protected static final String KEY_BILLTYPES = "billTypes";

  @Autowired
  protected PaymentService paymentService;
  @Autowired
  protected AccountOptionComponent optionComponent;
  @Autowired
  protected AccountCommonComponent accountCommonComponent;

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(PaymentQueryBuilder.class);
  }

  @Override
  protected String getServiceBeanId() {
    return PaymentService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put(KEY_BILLTYPES, JsonUtil.objectToJson(CommonUtil.getBillTypes()));
    moduleContext.put(KEY_SCALE, optionComponent.getScale());
    moduleContext.put(KEY_ROUNDING_MODE, optionComponent.getRoundingMode());
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    BPayment entity = JsonUtil.jsonToObject(json, BPayment.class);
    entity.doBeforeSave();

    entity = doBeforeSave(entity);

    Payment payment = BizPaymentConverter.getInstance().convert(entity);
    String uuid = paymentService.save(payment, new BeanOperateContext(getSessionUser()));

    return uuid;
  }

  /** 根据收款单标识（单号或者uuid）加载收款单，包括明细信息 */
  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody BPayment load(@RequestParam("uuid") String id) throws M3ServiceException {
    Payment payment = paymentService.getByUuid(id, Payment.PART_WHOLE);
    BPayment biz = PaymentBizConverter.getInstance().convert(payment);

    doAfterLoad(biz);
    return biz;
  }

  /** 新建收款单 */
  @RequestMapping(value = "create", method = RequestMethod.GET)
  public @ResponseBody BPayment create() throws Exception {
    try {
      BPayment payment = new BPayment();
      initPayment(payment);

      return payment;
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
  }

  @RequestMapping(value = "byAccountUuids", method = RequestMethod.POST)
  public @ResponseBody BPayment createByAccountUuids(@RequestBody List<String> accountUuids)
      throws Exception {
    try {

      Payment payment = paymentService.createByAccounts(accountUuids, getDirection());

      BPayment bPayment = PaymentBizConverter.getInstance().convert(payment);

      doAfterCreate(bPayment);

      return bPayment;
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
  }

  @RequestMapping(value = "byAccIds", method = RequestMethod.POST)
  public @ResponseBody BPayment createByAccountIds(@RequestBody List<String> accountIds)
      throws Exception {
    try {

      Payment payment = paymentService.createByAccountIds(accountIds, getDirection());

      BPayment bPayment = PaymentBizConverter.getInstance().convert(payment);

      initPayment(bPayment);

      doAfterCreate(bPayment);

      return bPayment;
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
  }

  @RequestMapping(value = "bySrcBills", method = RequestMethod.POST)
  public @ResponseBody BPayment createBySrcBills(@RequestBody List<String> srcBills)
      throws Exception {
    try {

      Payment payment = paymentService.createBySourceBills(srcBills, getDirection());

      BPayment bPayment = PaymentBizConverter.getInstance().convert(payment);

      initPayment(bPayment);

      doAfterCreate(bPayment);

      return bPayment;
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
  }

  @RequestMapping(value = "byStatements", method = RequestMethod.POST)
  public @ResponseBody BPayment createByStatements(@RequestBody List<String> statements)
      throws Exception {
    try {

      Payment payment = paymentService.createByStatements(statements, getDirection());

      BPayment bPayment = PaymentBizConverter.getInstance().convert(payment);

      initPayment(bPayment);

      doAfterCreate(bPayment);

      return bPayment;
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
  }

  protected void buildSummary(SummaryQueryResult result, QueryFilter queryFilter) {
    FlecsQueryDefinition queryDef = getQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    PaymentTotal total = paymentService.queryTotal(queryDef);
    result.getSummary().put("unpayedTotal", total.getUnpayedTotal());
    result.getSummary().put("total", total.getTotal());
    result.getSummary().put("leftTotal", total.getUnpayedTotal().subtract(total.getTotal()));
    super.buildSummary(result, queryFilter);
  }

  protected BPayment doBeforeSave(BPayment entity) {
    return entity;
  }

  protected void doAfterLoad(BPayment payment) throws M3ServiceException {
    // Do Nothing
  }

  /** 返回方向，收款单为1，付款单为-1 */
  protected abstract int getDirection();

  /** 默认收款方式 */
  protected abstract PaymentDefrayalType getDefaultPaymentDefrayalType();

  /** 创建后处理 */
  protected void doAfterCreate(BPayment payment) throws M3ServiceException {
    initPayment(payment);
    negateLineTotal(payment);
    processOverdues(payment);
    setDefaultBank(payment);
  };

  protected void setDefaultBank(BPayment payment) {
    // Do Nothing
  }

  private void initPayment(BPayment payment) {
    payment.setDirection(getDirection());
    payment.setBizState(BizStates.INEFFECT);
    payment.setCounterpartType(CounterpartType.TENANT);
    payment.setPaymentDate(DateUtil.datePart(new Date()));
    payment.setIncomeDate(DateUtil.datePart(new Date()));
    payment.setDealer(getSUser());

    payment.setDepositSubject(UCN.newInstance(optionComponent.getDefaultPreSubject()));
    payment.setDefrayalType(getDefaultPaymentDefrayalType());
  }

  /** 收款选择方向为付的账款时，金额需要取反 */
  private void negateLineTotal(BPayment payment) {
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      if (getDirection() != line.getAcc1().getDirection()) {
        line.setOriginTotal(line.getOriginTotal().negate());
        line.setTotal(line.getTotal().negate());
        line.setUnpayedTotal(line.getUnpayedTotal().negate());
      }
    }
  }

  /** 针对收款单，将为付账款的滞纳金设置为0。 */
  private void processOverdues(BPayment payment) {
    for (BPaymentAccountLine line : payment.getAccountLines()) {
      if (Direction.PAYMENT == line.getAcc1().getDirection()) {
        line.getOverdues().clear();
        line.getOverdueTerms().clear();
        line.getOverdueTotal().setTotal(BigDecimal.ZERO);
      }
    }
  }

  private SUser getSUser() {
    IsOperator operator = getSessionUser();
    return accountCommonComponent.getSUser(operator.getId());
  }

}
