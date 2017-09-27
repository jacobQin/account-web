/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AccountDefrayalServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.biz.BAccountDefrayal;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.rpc.AccountDefrayalService;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.perm.AccountDefrayalPermDef;
import com.hd123.m3.account.gwt.util.server.BillTypeUtils;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.deposit.Deposit;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.account.service.freeze.Freeze;
import com.hd123.m3.account.service.invoice.InvoiceReg;
import com.hd123.m3.account.service.paymentnotice.PaymentNotice;
import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayal;
import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayals;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author zhuhairui
 * 
 */
public class AccountDefrayalServiceServlet extends AccRemoteServiceServlet implements
    AccountDefrayalService {

  private static final long serialVersionUID = -2988547491306930692L;

  @Override
  public String getObjectName() {
    return AccountDefrayal.class.getSimpleName();
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    if (isHost()) {
      Set<BPermission> permissions = new HashSet<BPermission>();
      permissions.add(AccountDefrayalPermDef.READ);
      return permissions;
    }
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    return helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        AccountDefrayalPermDef.RESOURCE_ACCOUNTDEFRAYAL_SET);
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    try {
      moduleContext.put(KEY_BILLTYPES, getBillTypes());
    } catch (Exception e) {
      Logger.getLogger(AccountDefrayalServiceServlet.class).error("buildModuleContext error", e);
    }
  }

  private String getBillTypes() throws Exception {
    Map<String, String> result = new HashMap<String, String>();

    // 排除掉的单据类型
    BBillType freeze = new BBillType();// 账款冻结单
    freeze.setClassName(Freeze.class.getName());
    freeze.setDirection(0);
    BBillType payDeposit = new BBillType();// 预付款单
    payDeposit.setClassName(Deposit.class.getName());
    payDeposit.setDirection(Direction.PAYMENT);
    BBillType receiptDeposit = new BBillType();// 预存款单
    receiptDeposit.setClassName(Deposit.class.getName());
    receiptDeposit.setDirection(Direction.RECEIPT);
    BBillType payDepositRepayment = new BBillType();// 预付款还款单
    payDepositRepayment.setClassName(DepositRepayment.class.getName());
    payDepositRepayment.setDirection(Direction.PAYMENT);
    BBillType receiptDepositRepayment = new BBillType();// 预存款还款单
    receiptDepositRepayment.setClassName(DepositRepayment.class.getName());
    receiptDepositRepayment.setDirection(Direction.RECEIPT);
    BBillType payDepositMove = new BBillType();// 预付款转移单
    payDepositMove.setClassName(DepositMove.class.getName());
    payDepositMove.setDirection(Direction.PAYMENT);
    BBillType receiptDepositMove = new BBillType();// 预存款转移单
    receiptDepositMove.setClassName(DepositMove.class.getName());
    receiptDepositMove.setDirection(Direction.RECEIPT);
    BBillType notice = new BBillType();// 收付款通知单
    notice.setClassName(PaymentNotice.class.getName());
    notice.setDirection(0);
    BBillType receiptInvoice = new BBillType();// 收款发票登记单
    receiptInvoice.setClassName(InvoiceReg.class.getName());
    receiptInvoice.setDirection(Direction.RECEIPT);

    BBillType payInvoice = new BBillType();// 付款发票登记单
    payInvoice.setClassName(InvoiceReg.class.getName());
    payInvoice.setDirection(Direction.PAYMENT);

    List<BBillType> billTypes = BillTypeUtils.getAllWithout(freeze, payDeposit, receiptDeposit,
        payDepositMove, receiptDepositMove, payDepositRepayment, receiptDepositRepayment, notice,
        payInvoice, receiptInvoice);

    for (BBillType billType : billTypes) {
      result.put(billType.getName(), billType.getCaption());
    }
    return CollectionUtil.toString(result);
  }

  @Override
  public RPageData<BAccountDefrayal> query(List<Condition> conditions,
      List<String> visibleColumnNames, PageSort pageSort) throws Exception {
    try {

      FlecsQueryDefinition def = buildQueryDefinition(conditions, pageSort);
      if (def == null)
        return new RPageData<BAccountDefrayal>();
      // 受项目限制
      List<String> list = getUserStores(getSessionUser().getId());
      if (list.isEmpty()) {
        return new RPageData();
      }
      def.addCondition(AccountDefrayals.CONDITION_ACCOUNTUNIT_IN, list.toArray());

      QueryResult<AccountDefrayal> queryResult = getAccountDefrayalService().query(def);
      RPageData<BAccountDefrayal> result = new RPageData<BAccountDefrayal>();
      RPageDataUtil.copyPagingInfo(queryResult, result);
      result.setValues(ConverterUtil.convert(queryResult.getRecords(),
          AccountDefrayalBizConverter.getInstance()));

      // 查账单
      Map<String, List<BAccountDefrayal>> defrayals = new HashMap<String, List<BAccountDefrayal>>();
      for (BAccountDefrayal defrayal : result.getValues()) {
        if (Accounts.NONE_BILL_UUID.equals(defrayal.getStatement().getBillUuid()))
          continue;
        List<BAccountDefrayal> defrayalList = defrayals.get(defrayal.getStatement().getBillUuid());
        if (defrayalList == null) {
          defrayalList = new ArrayList<BAccountDefrayal>();
          defrayals.put(defrayal.getStatement().getBillUuid(), defrayalList);
        }
        defrayalList.add(defrayal);
      }

      if (defrayals.isEmpty())
        return result;

      FlecsQueryDefinition statementQueryDef = new FlecsQueryDefinition();
      statementQueryDef.addCondition(Statements.CONDITION_UUID_IN, defrayals.keySet().toArray());
      List<Statement> statements = getStatementService().query(statementQueryDef).getRecords();
      for (Statement statement : statements) {
        List<BAccountDefrayal> defrayalList = defrayals.get(statement.getUuid());
        for (BAccountDefrayal defrayal : defrayalList) {
          defrayal.setSettleNo(statement.getSettleNo());
        }
      }

      return result;

    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions, PageSort pageSort) {
    FlecsQueryDefinition queryDef = buildQueryDefinition(conditions, pageSort.getSortOrders());
    if (queryDef == null)
      return null;

    queryDef.setPage(pageSort.getPage());
    queryDef.setPageSize(pageSort.getPageSize());
    return queryDef;
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions,
      List<Order> sortOrders) {
    FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();
    if (queryDef == null)
      return null;

    queryDef.getFlecsConditions().addAll(
        AccountDefrayalQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(AccountDefrayalQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    boolean useContractPermT = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
    boolean useContractPermP = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR);
    if (useContractPermT || useContractPermP) {
      // 启用授权组
      List<String> userGroups = getUserGroups(getSessionUser().getId());
      if (userGroups == null || userGroups.isEmpty())
        return null;
      queryDef.addCondition(AccountDefrayals.CONDITION_PERM_CONTRACT_GROUP_ID_IN, useContractPermT,
          useContractPermP, userGroups);
    }

    return queryDef;
  }

  private com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayalService getAccountDefrayalService() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayalService.class);
  }

  private StatementService getStatementService() {
    return M3ServiceFactory.getService(StatementService.class);
  }

}
