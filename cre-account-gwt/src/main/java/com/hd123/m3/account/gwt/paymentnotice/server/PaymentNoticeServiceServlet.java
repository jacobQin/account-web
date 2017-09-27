/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.server;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.bpm.service.execution.Definition;
import com.hd123.bpm.service.execution.ProcessService;
import com.hd123.bpm.service.execution.StartOperation;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.server.ProcessClient;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNoticeLine;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QBatchStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeService;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.filter.BatchFilter;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.filter.StatementFilter;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.perm.PaymentNoticePermDef;
import com.hd123.m3.account.gwt.util.server.CollectionUtil;
import com.hd123.m3.account.gwt.util.server.SubjectUsageUtils;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.paymentnotice.PaymentNotice;
import com.hd123.m3.account.service.paymentnotice.PaymentNotices;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.BpmOperateContext;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.base.client.biz.HasPerm;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.mdata.service.typetag.TypeTag;
import com.hd123.m3.mdata.service.typetag.TypeTags;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.HasUCN;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 客户端服务接口实现
 * 
 * @author zhuhairui
 * 
 */
public class PaymentNoticeServiceServlet extends AccBPMServiceServlet
    implements PaymentNoticeService {

  private static final long serialVersionUID = -6661173450258406540L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        PaymentNoticePermDef.RESOURCE_PAYMENTNOTICE_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    // String defaultBPMKey =
    // getOptionService().getOption(AccOptions.OPTION_PATH,
    // AccOptions.OPTION_PAYMENTNOTICDEFAULTBPM);
    // moduleContext.put(PaymentNoticeService.KEY_DEFAULTBPMKEY, defaultBPMKey);
  }

  @Override
  public RPageData<BPaymentNotice> query(List<Condition> conditions, PageSort pageSort)
      throws Exception {
    try {

      FlecsQueryDefinition def = buildQueryDefinition(conditions, pageSort);
      if (def == null)
        return new RPageData<BPaymentNotice>();

      QueryResult<PaymentNotice> qr = getPaymentNoticeService().query(def);
      RPageData<BPaymentNotice> rp = new RPageData<BPaymentNotice>();
      RPageDataUtil.copyPagingInfo(qr, rp);
      rp.setValues(ConverterUtil.convert(qr.getRecords(), PaymentNoticeBizConverter.getInstance()));

      // 更新导航数据
      updateNaviEntities(getObjectName(), rp.getValues());
      return rp;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPaymentNotice create() throws Exception {
    try {
      BPaymentNotice paymentNotice = new BPaymentNotice();
      paymentNotice.setBizState(BizStates.INEFFECT);
      return paymentNotice;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
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

    queryDef.getFlecsConditions()
        .addAll(PaymentNoticeQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(PaymentNoticeQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() throws ClientBizException {
    FlecsQueryDefinition queryDefinition = new FlecsQueryDefinition();
    queryDefinition.getConditions().addAll(getPermConditions());
    return queryDefinition;
  }

  @Override
  public BPaymentNotice load(String uuid) throws Exception {
    try {

      PaymentNotice source = getPaymentNoticeService().get(uuid, PaymentNotices.Fetch_PART_LINES);
      if (source == null)
        return null;

      BPaymentNotice result = PaymentNoticeBizConverter.getInstance().convert(source);
      validEntityPerm(result);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPaymentNotice loadByNumber(String billNumber) throws Exception {
    try {

      PaymentNotice source = getPaymentNoticeService().getByNumber(billNumber,
          PaymentNotices.Fetch_PART_LINES);
      if (source == null)
        return null;

      BPaymentNotice result = PaymentNoticeBizConverter.getInstance().convert(source);
      validEntityPerm(result);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void delete(String uuid, long version) throws Exception {
    try {

      getPaymentNoticeService().remove(uuid, version,
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void effect(String uuid, String latestComment, long oca) throws Exception {
    try {

      BpmOperateContext operCtx = new BpmOperateContext(getSessionUser(), latestComment);
      getPaymentNoticeService().effect(uuid, oca, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public void abort(String uuid, long version, String comment) throws Exception {
    try {

      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(PaymentNotices.PROPERTY_MESSAGE, comment);
      getPaymentNoticeService().abort(uuid, version, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPaymentNotice save(BPaymentNotice notice, BProcessContext task) throws Exception {
    try {

      // 先判断流程权限
      doBeforeExecute(notice, task);

      PaymentNotice target = BizPaymentNoticeConverter.getInstance().convert(notice);
      String uuid = getPaymentNoticeService().save(target,
          new BeanOperateContext(getSessionUser()));
      target = getPaymentNoticeService().get(uuid);

      doAfterSave(target, task);

      return PaymentNoticeBizConverter.getInstance().convert(target);
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }

  }

  @Override
  public RPageData<QStatement> queryStatement(StatementFilter filter, List<String> statementUuids)
      throws Exception {
    try {

      FlecsQueryDefinition definition = new FlecsQueryDefinition();
      if (isPermEnabled(Statements.OBJECT_NAME)) {
        // 启用账单授权
        List<String> userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups == null || userGroups.isEmpty()) {
          return new RPageData<QStatement>();
        }
        definition.addCondition(Statements.CONDITION_PERM_GROUP_ID_IN, userGroups.toArray());
      }
      if (!StringUtil.isNullOrBlank(filter.getAccountUnitUuid())) {
        definition.addCondition(Statements.CONDITION_ACCOUNTUNIT_EQUALS,
            filter.getAccountUnitUuid());
      } else {
        // 受项目限制
        List<String> list = getUserStores(getSessionUser().getId());
        if (list.isEmpty()) {
          return new RPageData();
        }
        definition.addCondition(Statements.CONDITION_ACCOUNTUNIT_IN, list.toArray());
      }

      if (!StringUtil.isNullOrBlank(filter.getCounterPartUuid())) {
        definition.addCondition(Statements.CONDITION_COUNTERPAR_EQUALS,
            filter.getCounterPartUuid());
      }

      if (!StringUtil.isNullOrBlank(filter.getCounterpartType())) {
        definition.addCondition(Statements.CONDITION_COUNTERPARTYPE_EQUALS,
            filter.getCounterpartType());
      }

      definition.addCondition(Statements.CONDITION_BIZSTATE_EQUALS, BizStates.EFFECT);
      definition.addCondition(Statements.CONDITION_PAYMENTNOTICE, filter.getNoticeUuid());

      if (!StringUtil.isNullOrBlank(filter.getBillNumber())) {
        definition.addCondition(Statements.CONDITION_BILLNUMBER_STARTWITH, filter.getBillNumber());
      }

      if (!StringUtil.isNullOrBlank(filter.getSettleNo())) {
        definition.addCondition(Statements.CONDITION_SETTLENO_EQUALS, filter.getSettleNo());
      }

      if (!StringUtil.isNullOrBlank(filter.getContractNum())) {
        definition.addCondition(Statements.CONDITION_CONTRACT_BILLNUM_STARTWITH,
            filter.getContractNum());
      }

      if (statementUuids != null && statementUuids.isEmpty() == false) {
        definition.addCondition(Statements.CONDITION_UUID_NOTIN, statementUuids.toArray());
      }

      if (!StringUtil.isNullOrBlank(filter.getContractTitle())) {
        definition.addCondition(Statements.CONDITION_CONTRACT_TITLE_LIKE,
            filter.getContractTitle());
      }

      String orderField = Statements.ORDER_BY_BILL_NUMBER;
      QueryOrderDirection dir = QueryOrderDirection.asc;
      List<com.hd123.rumba.gwt.base.client.QueryFilter.Order> orders = filter.getOrders();
      if (orders != null && orders.isEmpty() == false) {
        for (com.hd123.rumba.gwt.base.client.QueryFilter.Order order : orders) {
          if (QStatement.FIELD_BILLNUMBER.equals(order.getFieldName())) {
            orderField = Statements.ORDER_BY_BILL_NUMBER;
          } else if (QStatement.FIELD_SETTLENO.equals(order.getFieldName())) {
            orderField = Statements.ORDER_BY_SETTLE;
          } else if (QStatement.FIELD_COUNTERPART.equals(order.getFieldName())) {
            orderField = Statements.ORDER_BY_COUNTERPART_CODE;
          } else if (QStatement.FIELD_CONTRACTNUM.equals(order.getFieldName())) {
            orderField = Statements.ORDER_BY_CONTRACTNUMBER;
          }
          dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
        }
      } else {
        orderField = Statements.ORDER_BY_BILL_NUMBER;
        dir = QueryOrderDirection.desc;
      }

      definition.getOrders().add(new QueryOrder(orderField, dir));
      definition.setPage(filter.getPage());
      definition.setPageSize(filter.getPageSize());

      QueryResult<Statement> qr = getStatementService().query(definition);
      if (qr == null || qr.getRecords().isEmpty())
        return new RPageData<QStatement>();

      RPageData<QStatement> pd = new RPageData<QStatement>();
      RPageDataUtil.copyPagingInfo(qr, pd);
      List<QStatement> list = new ArrayList<QStatement>();
      Set<String> contractUuids = new HashSet<String>();
      for (Statement source : qr.getRecords()) {
        QStatement target = new QStatement();
        target.setStatement(new BBill(source.getUuid(), source.getBillNumber()));
        target.setSettleNo(source.getSettleNo());
        target.setCounterpart(UCNBizConverter.getInstance().convert(source.getCounterpart()));
        target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
        target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
        target.setReceiptTotal(source.getReceiptTotal() != null
            ? source.getReceiptTotal().getTotal() : BigDecimal.ZERO);
        target.setReceiptTax(
            source.getReceiptTotal() != null ? source.getReceiptTotal().getTax() : BigDecimal.ZERO);
        target.setReceiptIvcTotal(source.getIvcReceiptTotal() != null
            ? source.getIvcReceiptTotal().getTotal() : BigDecimal.ZERO);
        target.setReceiptIvcTax(source.getIvcReceiptTotal() != null
            ? source.getIvcReceiptTotal().getTax() : BigDecimal.ZERO);
        target.setPaymentTotal(
            source.getPayTotal() != null ? source.getPayTotal().getTotal() : BigDecimal.ZERO);
        target.setPaymentTax(
            source.getPayTotal() != null ? source.getPayTotal().getTax() : BigDecimal.ZERO);
        target.setPaymentIvcTotal(
            source.getIvcPayTotal() != null ? source.getIvcPayTotal().getTotal() : BigDecimal.ZERO);
        target.setPaymentIvcTax(
            source.getIvcPayTotal() != null ? source.getIvcPayTotal().getTax() : BigDecimal.ZERO);
        target.setRemark(source.getRemark());
        target.setAccountTypeCode(source.getAccountType());
        list.add(target);
        contractUuids.add(target.getContract().getUuid());
      }
      fetchContract4QStatement(contractUuids, list);
      fetchAccountTypeName(list);
      pd.setValues(list);
      return pd;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void fetchContract4QStatement(Collection<String> contractUuids,
      Collection<QStatement> statement) throws ClientBizException {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.addCondition(Contracts.CONDITION_ID_IN, contractUuids.toArray());

      QueryResult<Contract> qr = getContractService().query(queryDef);
      Map<String, Contract> map = new HashMap<String, Contract>();
      for (Contract c : qr.getRecords()) {
        map.put(c.getUuid(), c);
      }

      for (QStatement qStatement : statement) {
        Contract contract = map.get(qStatement.getContract().getUuid());
        if (contract == null)
          continue;
        if (qStatement.getContract().getUuid().equals(contract.getUuid())) {
          qStatement.getContract().setName(contract.getTitle());
        }
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void fetchAccountTypeName(Collection<QStatement> statement) throws ClientBizException {
    try {
      for (QStatement qStatement : statement) {
        if (qStatement.getAccountTypeCode() == null)
          continue;
        String[] accountTypes = qStatement.getAccountTypeCode().split(",");
        List<String> accountTypeNames = new ArrayList<String>();
        for (String accountType : accountTypes) {
          BSubjectUsage usage = SubjectUsageUtils.getSubjectUsage(accountType);
          if (usage == null)
            continue;
          accountTypeNames.add(usage.getName());
        }
        qStatement.setAccountTypeName(
            com.hd123.rumba.gwt.base.client.util.CollectionUtil.toString(accountTypeNames, ','));
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<QBatchStatement> queryBatchStatement(BatchFilter filter)
      throws ClientBizException {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      String accountUnitUuid = filter.getAccountUnitUuid();
      if (StringUtil.isNullOrBlank(accountUnitUuid) == false) {
        queryDef.addCondition(Statements.CONDITION_ACCOUNTUNIT_EQUALS, accountUnitUuid);
      } else {
        // 受项目限制
        List<String> list = getUserStores(getSessionUser().getId());
        if (list.isEmpty()) {
          return new RPageData();
        }
        queryDef.addCondition(Statements.CONDITION_ACCOUNTUNIT_IN, list.toArray());
      }

      String counterpartUuid = filter.getCounterpartUuid();
      if (StringUtil.isNullOrBlank(counterpartUuid) == false) {
        queryDef.addCondition(Statements.CONDITION_COUNTERPAR_EQUALS, counterpartUuid);
      }

      String counterpartType = filter.getCounterpartType();
      if (StringUtil.isNullOrBlank(counterpartType) == false) {
        queryDef.addCondition(Statements.CONDITION_COUNTERPARTYPE_EQUALS, counterpartType);
      }

      String positionType = filter.getPositionType();
      if (StringUtil.isNullOrBlank(positionType) == false) {
        queryDef.addCondition(Statements.CONDITION_CONTRACT_POSITION_TYPE_EQUALS, positionType);
      }

      String positionUuid = filter.getPositionUuid();
      if (StringUtil.isNullOrBlank(positionUuid) == false) {
        queryDef.addCondition(Statements.CONDITION_CONTRACT_POSITION_UUID_EQUALS, positionUuid);
      }

      String contractTitle = filter.getContractTitle();
      if (StringUtil.isNullOrBlank(contractTitle) == false) {
        queryDef.addCondition(Statements.CONDITION_CONTRACT_TITLE_LIKE, contractTitle);
      }

      String floor = filter.getFloor();
      if (StringUtil.isNullOrBlank(floor) == false) {
        queryDef.addCondition(Statements.CONDITION_CONTRACT_FLOOR_UUID_EQUALS, floor);
      }

      String coopMode = filter.getCoopMode();
      if (StringUtil.isNullOrBlank(coopMode) == false) {
        queryDef.addCondition(Statements.CONDITION_CONTRACT_COOPMODE_EQUALS, coopMode);
      }

      queryDef.addCondition(Statements.CONDITION_PAYMENTNOTICE);

      List<QueryOrder> orders = new ArrayList<QueryOrder>();

      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (com.hd123.rumba.gwt.base.client.QueryFilter.Order order : filter.getOrders()) {
          String orderField = null;
          if (ObjectUtil.equals(PaymentNoticeUrlParams.ORDER_BY_COUNTERPART_CODE,
              order.getFieldName())) {
            orderField = Statements.ORDER_BY_COUNTERPART_CODE;
          } else if (ObjectUtil.equals(PaymentNoticeUrlParams.ORDER_BY_ACCOUNTUNIT_CODE,
              order.getFieldName())) {
            orderField = Statements.ORDER_BY_ACCOUNTUNIT_CODE;
          } else if (ObjectUtil.equals(PaymentNoticeUrlParams.ORDER_BY_CONTRACTBILLNUMBER,
              order.getFieldName())) {
            orderField = Statements.ORDER_BY_CONTRACTNUMBER;
          }

          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      }

      queryDef.setOrders(orders);
      queryDef.setPage(0);
      queryDef.setPageSize(0);

      QueryResult<Statement> qr = getStatementService().query(queryDef);

      RPageData<QBatchStatement> pd = new RPageData<QBatchStatement>();
      RPageDataUtil.copyPagingInfo(qr, pd);

      List<QBatchStatement> list = new ArrayList<QBatchStatement>();
      Set<String> contractUuids = new HashSet<String>();
      Set<String> keys = new HashSet<String>();
      for (Statement statement : qr.getRecords()) {
        String uuid = statement.getAccountUnit().getUuid() + statement.getCounterpart().getUuid();

        if (keys.contains(uuid))
          continue;
        keys.add(uuid);

        QBatchStatement qBatchStatement = new QBatchStatement();
        qBatchStatement
            .setAccountUnit(UCNBizConverter.getInstance().convert(statement.getAccountUnit()));
        qBatchStatement.setCounterpart(BCounterpartConverter.getInstance()
            .convert(statement.getCounterpart(), statement.getCounterpartType()));
        qBatchStatement.setContractUuid(statement.getContract().getUuid());
        qBatchStatement.setContractBillNumber(statement.getContract().getCode());
        list.add(qBatchStatement);
        contractUuids.add(statement.getContract().getUuid());
      }

      fetchContractQBatchStatement(contractUuids, list);
      pd.setValues(list);
      return pd;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void fetchContractQBatchStatement(Collection<String> contractUuids,
      Collection<QBatchStatement> batchStatement) throws ClientBizException {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      queryDef.addCondition(Contracts.CONDITION_ID_IN, contractUuids.toArray());

      QueryResult<Contract> qr = getContractService().query(queryDef);
      Map<String, Contract> map = new HashMap<String, Contract>();
      for (Contract c : qr.getRecords()) {
        map.put(c.getUuid(), c);
      }

      for (QBatchStatement qBatchStatement : batchStatement) {
        Contract contract = map.get(qBatchStatement.getContractUuid());
        if (contract == null)
          continue;
        if (qBatchStatement.getContractUuid().equals(contract.getUuid())) {
          qBatchStatement.setContractBillNumber(contract.getBillNumber());
          qBatchStatement.setContractTitle(contract.getTitle());
          qBatchStatement.setFloor(UCNBizConverter.getInstance().convert(contract.getFloor()));
          qBatchStatement.setCoopMode(contract.getCoopMode());
        }
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<String> generateBill(QBatchStatement accUnitAndCoupart, boolean isSpligBill,
      HasPerm perm, String defKey, BTaskContext task) throws Exception {
    try {

      StatementFilter filter = new StatementFilter();
      filter.setCounterPartUuid(accUnitAndCoupart.getCounterpart().getUuid());
      filter.setAccountUnitUuid(accUnitAndCoupart.getAccountUnit().getUuid());
      RPageData<QStatement> rp = queryStatement(filter, null);
      if (rp.getValues().isEmpty())
        return new ArrayList<String>();

      List<String> result = new ArrayList<String>();
      if (isSpligBill == false) {
        BPaymentNotice entity = createByStatements(rp.getValues(), accUnitAndCoupart);
        entity.setRemark(R.R.batch_remark());
        if (perm != null) {
          entity.setPermGroupId(perm.getPermGroupId());
          entity.setPermGroupTitle(perm.getPermGroupTitle());
        }

        result.add(generate(entity, defKey, task));
      } else {
        // 按照合同分组
        Collection<List> group = CollectionUtil.group(rp.getValues(), new Comparator<QStatement>() {

          @Override
          public int compare(QStatement o1, QStatement o2) {
            if (ObjectUtil.equals(o1.getContract(), o2.getContract()))
              return 0;
            else
              return 1;
          }
        });
        // 生成每个分组的通知单
        for (List<QStatement> list : group) {
          BPaymentNotice entity = createByStatements(list, accUnitAndCoupart);
          entity.setRemark(R.R.batch_remark());
          if (perm != null) {
            entity.setPermGroupId(perm.getPermGroupId());
            entity.setPermGroupTitle(perm.getPermGroupTitle());
          }
          result.add(generate(entity, defKey, task));
        }
        // 重新按照单号升序排列
        Collections.sort(result, new Comparator<String>() {

          @Override
          public int compare(String o1, String o2) {
            return o1.compareTo(o2);
          }

        });
      }

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  private BPaymentNotice createByStatements(List<QStatement> statements,
      QBatchStatement accUnitAndCounterpart) throws Exception {
    if (statements == null || statements.isEmpty())
      return null;

    BPaymentNotice entity = create();
    entity.setAccountUnit(accUnitAndCounterpart.getAccountUnit());
    entity.setCounterpart(accUnitAndCounterpart.getCounterpart());
    int i = 1;
    for (QStatement statement : statements) {
      BPaymentNoticeLine line = createLineByStatement(statement);
      line.setLineNumber(i);
      entity.getLines().add(line);
      i++;
    }
    entity.aggregate();
    return entity;
  }

  private BPaymentNoticeLine createLineByStatement(QStatement source) {
    BPaymentNoticeLine target = new BPaymentNoticeLine();
    target.setAccountUnit(source.getAccountUnit());
    target.setStatement(source.getStatement());
    target.setContract(source.getContract());
    target.setReceiptTotal(source.getReceiptTotal());
    target.setReceiptTax(source.getReceiptTax());
    target.setReceiptIvcTotal(source.getReceiptIvcTotal());
    target.setReceiptIvcTax(source.getReceiptIvcTax());
    target.setPaymentTotal(source.getPaymentTotal());
    target.setPaymentTax(source.getPaymentTax());
    target.setPaymentIvcTotal(source.getPaymentIvcTotal());
    target.setPaymentIvcTax(source.getPaymentIvcTax());
    target.setRemark(source.getRemark());

    return target;
  }

  private String generate(BPaymentNotice entity, String defKey, BTaskContext task)
      throws Exception {
    // 生成单据
    PaymentNotice target = BizPaymentNoticeConverter.getInstance().convert(entity);
    String uuid = getPaymentNoticeService().save(target, new BeanOperateContext(getSessionUser()));
    PaymentNotice result = getPaymentNoticeService().get(uuid);
    if (StringUtil.isNullOrBlank(defKey)) {
      return result.getBillNumber();
    }
    // 保存流程变量
    Map<String, Object> variables = new HashMap<String, Object>();
    variables = getBpmVariables(result, variables);
    variables.put(BpmConstants.KEY_BILLCAPTION,
        CodecUtils.encode(MessageFormat.format(R.R.billCaption(), result.getBillNumber(),
            result.getAccountUnit() == null ? null : result.getAccountUnit().toString(),
            result.getCounterpart() == null ? null : result.getCounterpart().toString())));
    // 发起流程
    StartOperation operation = new StartOperation();
    operation.setVariables(variables);
    operation.setBusinessKey(uuid);
    getProcessClient().start(defKey, operation);
    return result.getBillNumber();
  }

  protected ProcessClient getProcessClient() {
    return ProcessClient.getInstance(getAppCtx(), getSessionUser());
  }

  @Override
  public String executeTask(BOperation operation, BPaymentNotice entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");

      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction()) == false
          && saveBeforeAction) {
        String uuid = getPaymentNoticeService().save(
            new BizPaymentNoticeConverter().convert(entity),
            new BeanOperateContext(getSessionUser()));
        PaymentNotice result = getPaymentNoticeService().get(uuid);
        doExecuteTask(result, operation, task);
        return result.getUuid();
      } else {
        doExecuteTask(operation, task);
        return entity == null ? null : entity.getUuid();
      }
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected Map<String, Object> getBpmVariables(Object obj, BTaskContext ctx) {
    Map<String, Object> variables = super.getBpmVariables(obj, ctx);
    if (obj instanceof PaymentNotice) {
      PaymentNotice bill = (PaymentNotice) obj;
      variables.put(BpmConstants.KEY_BILLCAPTION,
          CodecUtils.encode(MessageFormat.format(R.R.billCaption(), bill.getBillNumber(),
              toUCNStr(bill.getAccountUnit()), toUCNStr(bill.getCounterpart()))));
    }
    return variables;
  }

  @Override
  public List<BProcessDefinition> queryProcessDefinition() throws Exception {
    try {
      List<Definition> defs = getProcessService()
          .getDefinitionsByKeyPrefix(PaymentNotices.OBJECT_NAME);
      if (defs == null || defs.isEmpty()) {
        return new ArrayList<BProcessDefinition>();
      }
      Set<String> keys = new HashSet<String>();
      for (Definition def : defs) {
        keys.add(def.getDefinitionKey());
      }
      // 过滤无发齐全的流程
      Collection<String> canStartList = getProcessService().canStartProcesses(keys,
          getSessionUser().getId());
      List<BProcessDefinition> result = new ArrayList<BProcessDefinition>();
      for (Definition def : defs) {
        if (canStartList.contains(def.getDefinitionKey()) == false) {
          continue;
        }
        result.add(new BProcessDefinition(def.getDefinitionKey(), def.getName()));
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<String> getCoopModes() throws ClientBizException {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.addFlecsCondition(TypeTags.FIELD_CATEGORY, TypeTags.OPERATOR_EQUALS,
          "investment.coopMode");
      queryDef.addFlecsCondition(TypeTags.FIELD_SUBTYPE, TypeTags.OPERATOR_EQUALS, "tenant");
      List<TypeTag> values = getTypeTagService().query(queryDef).getRecords();

      List<String> results = new ArrayList<String>();
      for (TypeTag coopType : values) {
        results.add(coopType.getName());
      }
      return results;
    } catch (Exception e) {
      return new ArrayList<String>();
    }
  }

  private String toUCNStr(HasUCN ucn) {
    if (ucn == null) {
      return "";
    } else {
      return "[" + ucn.getCode() + "]" + ucn.getName();
    }
  }

  private com.hd123.m3.account.service.paymentnotice.PaymentNoticeService getPaymentNoticeService() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.paymentnotice.PaymentNoticeService.class);
  }

  private StatementService getStatementService() {
    return M3ServiceFactory.getService(StatementService.class);
  }

  protected interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("收付款通知单")
    public String moduleCaption();

    @DefaultStringValue("收付款通知单：{0},商户：{1},对方单位：{2}")
    public String billCaption();

    @DefaultStringValue("通过批量生成的单据。")
    public String batch_remark();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    public String nullTask();

    @DefaultStringValue("收付款通知单")
    public String paymentNotice();

    @DefaultStringValue("指定的\"收付款通知单\"不存在。")
    String nullObject();

    @DefaultStringValue("找不到指定流程实例({0})所属的任务。")
    String noFoundTask();
  }

  private ProcessService getProcessService() {
    return M3ServiceFactory.getService(ProcessService.class);
  }

  @Override
  public String getObjectName() {
    return PaymentNotices.OBJECT_NAME;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

}
