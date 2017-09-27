/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	RebateBill2Controller.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月24日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.rebate2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.option.AccountSettleOption;
import com.hd123.m3.account.service.rebate2.RebateBill;
import com.hd123.m3.account.service.rebate2.RebateBillService;
import com.hd123.m3.account.service.rebate2.RebateBills;
import com.hd123.m3.account.service.rebate2.RebateLine;
import com.hd123.m3.account.service.rebate2.SalesPayment;
import com.hd123.m3.account.service.report.product.SalesReceiver;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.rebate.RebateBillQueryBuilder;
import com.hd123.m3.sales.service.moonstar.order2.Order;
import com.hd123.m3.sales.service.moonstar.order2.OrderService;
import com.hd123.m3.sales.service.moonstar.order2.buy.Buy;
import com.hd123.m3.sales.service.moonstar.order2.buy.BuyPayment;
import com.hd123.m3.sales.service.moonstar.order2.buy.OrderBuyService;
import com.hd123.m3.sales.service.paytype.PaymentTypeService;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author chenganbang
 *
 */
@Controller
@RequestMapping("account/rebate2/*")
public class RebateBill2Controller extends BizFlowModuleController {

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
  private OrderService orderService;
  @Autowired
  private OrderBuyService orderBuyService;

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
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(json)) {
      return null;
    }
    RebateBill entity = JsonUtil.jsonToObject(json, RebateBill.class);
    int index = 1;
    for (RebateLine line : entity.getLines()) {
      line.setLineNumber(index++);
    }
    return rebateBillService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  @RequestMapping("load")
  @ResponseBody
  public RebateBill load(@RequestParam String uuid) throws Exception {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }
    RebateBill entity = rebateBillService.get(uuid);
    validEntityPerm(entity);
    return entity;
  }

  @RequestMapping("queryLines")
  @ResponseBody
  public QueryResult<RebateLine> queryLines(@RequestBody QueryFilter filter) throws Exception {
    QueryResult<RebateLine> result = new QueryResult<RebateLine>();
    if (filter == null) {
      return result;
    }
    FlecsQueryDefinition definition = getQueryBuilder().build4Query(filter);
    QueryResult<Order> orders = orderService.query(definition);
    result.setPage(orders.getPage());
    result.setPageSize(orders.getPageSize());
    result.setPageCount(orders.getPageCount());
    result.setRecordCount(orders.getRecordCount());

    List<String> orderUuids = new ArrayList<String>();
    List<Order> records = orders.getRecords();
    List<RebateLine> lines = new ArrayList<RebateLine>();

    if (records.isEmpty()) {
      return result;
    }

    for (Order order : records) {
      orderUuids.add(order.getUuid());

      RebateLine line = new RebateLine();
      line.setSrcBill(new UCN(order.getUuid(), order.getBillNumber(), order.getBillNumber()));
      line.setSalesDate(order.getPayDate());
      line.setRebateAmount(order.getAccountTotal());
      line.setPoundageAmount(order.getFeeTotal());
      line.setAmount(order.getAccountTotal().subtract(order.getFeeTotal()));

      lines.add(line);
    }

    List<Buy> buys = orderBuyService.getOrderBuys(orderUuids);
    for (RebateLine line : lines) {
      String orderUuid = line.getSrcBill().getUuid();
      Map<String, BuyPayment> paymentMap = new HashMap<String, BuyPayment>();

      for (Buy buy : buys) {
        if (orderUuid.equals(buy.getOrder())) {
          line.getBuys().add(buy.getUuid());
          for (BuyPayment buyPayment : buy.getPayments()) {
            String paymentUuid = buyPayment.getPayment().getUuid();
            if (paymentMap.get(paymentUuid) == null) {
              paymentMap.put(paymentUuid, buyPayment);
            } else {
              BuyPayment payment = paymentMap.get(paymentUuid);
              payment.setTotal((payment.getTotal() == null ? BigDecimal.ZERO : payment.getTotal())
                  .add(buyPayment.getTotal()));
              paymentMap.put(paymentUuid, payment);
            }
          }
        }
      }

      for (String paymentUuid : paymentMap.keySet()) {
        BuyPayment buyPayment = paymentMap.get(paymentUuid);

        SalesPayment payment = new SalesPayment();
        payment.setBank(buyPayment.getBank());
        payment.setPayment(buyPayment.getPayment());
        payment.setReceiver(SalesReceiver.valueOf(buyPayment.getReceiver().name()));
        payment.setTotal(buyPayment.getTotal());

        line.getPayments().add(payment);
      }
    }

    result.setRecords(lines);
    return result;
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
