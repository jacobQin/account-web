/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.server;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.RoundingMode;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.commons.server.BizBillConverter;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.commons.server.SourceBillBizConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.server.converter.ContractBizConverter;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatement;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatementLine;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustService;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementBrowseFilter;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
import com.hd123.m3.account.gwt.statement.adjust.server.converter.BizStatementAdjustConverter;
import com.hd123.m3.account.gwt.statement.adjust.server.converter.StatementAdjustBizConverter;
import com.hd123.m3.account.gwt.util.server.BillTypeUtils;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractState;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementAccRange;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.StatementType;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.account.service.statement.adjust.StatementAdjust;
import com.hd123.m3.account.service.statement.adjust.StatementAdjustLine;
import com.hd123.m3.account.service.statement.adjust.StatementAdjusts;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.date.DateRange;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.service.counterpart.Counterparts;
import com.hd123.m3.investment.service.tenant.tenant.Tenants;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustServiceServlet extends AccBPMServiceServlet implements
    StatementAdjustService {
  private static final long serialVersionUID = 3299189670247690246L;

  @Override
  public String getObjectName() {
    return StatementAdjusts.OBJECT_NAME;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    try {
      moduleContext.put(KEY_BILLTYPES, CollectionUtil.toString(BillTypeUtils.getAllNameCaption()));
      moduleContext.put(SCALE, getScale());
      moduleContext.put(ROUNDING_MODE, getRoundingMode());
    } catch (Exception e) {
      Logger.getLogger(StatementAdjustServiceServlet.class).error("buildModuleContext error", e);
    }
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    if (isHost()) {
      Set<BPermission> permissions = new HashSet<BPermission>();
      permissions.add(StatementAdjustPermDef.READ);
      permissions.add(StatementAdjustPermDef.CREATE);
      permissions.add(StatementAdjustPermDef.UPDATE);
      permissions.add(StatementAdjustPermDef.DELETE);
      permissions.add(StatementAdjustPermDef.EFFECT);
      permissions.add(StatementAdjustPermDef.ABORT);
      return permissions;
    }
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        StatementAdjustPermDef.RESOURCE_STATEMENTADJUST_SET);
    return permissions;
  }

  @Override
  public RPageData<BStatementAdjust> query(List<Condition> conditions,
      List<String> visibleColumnNames, PageSort pageSort) throws Exception {
    try {

      FlecsQueryDefinition def = buildQueryDefinition(conditions, pageSort);
      if (def == null)
        return new RPageData<BStatementAdjust>();

      QueryResult<StatementAdjust> qr = getStatementAdjustService().query(def);

      RPageData<BStatementAdjust> pd = new RPageData<BStatementAdjust>();
      RPageDataUtil.copyPagingInfo(qr, pd);
      pd.setValues(ConverterUtil.convert(qr.getRecords(), StatementAdjustBizConverter.getInstance()));

      // 更新导航数据
      updateNaviEntities(getObjectName(), pd.getValues());
      return pd;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public RPageData<QStatement> queryStatement(StatementBrowseFilter filter) throws Exception {
    try {

      String statementNum = filter.getStatementNum();
      String settleNo = filter.getSettleNo();
      String counterPart = filter.getCounterpart();
      String counterPartType = filter.getCounterpartType();
      String contractNum = filter.getContractNum();
      String accountUnit = filter.getAccountUnit();

      FlecsQueryDefinition definition = new FlecsQueryDefinition();
      definition.getFetchParts().add(Statement.FETCH_PART_RANGES);

      // 启用账单授权
      boolean useStatementPerm = isPermEnabled(Statements.OBJECT_NAME);
      // 启用商户授权
      boolean useCounterpartPerm = isPermEnabled(Tenants.OBJECT_NAME_MERCHANT);

      List<String> userGroups = new ArrayList<String>();
      if (useStatementPerm || useCounterpartPerm) {
        userGroups = getUserGroups(getSessionUser().getId());

        if (userGroups.isEmpty()) {
          return new RPageData<QStatement>();
        }
      }

      // 启用账单授权
      if (useStatementPerm) {
        definition.addCondition(Statements.CONDITION_PERM_GROUP_ID_IN, userGroups.toArray());
      }

      // 受项目限制
      List<String> list = getUserStores(getSessionUser().getId());
      if (list.isEmpty()) {
        return new RPageData<QStatement>();
      }
      definition.addCondition(Statements.CONDITION_ACCOUNTUNIT_IN, list.toArray());

      definition.addCondition(Statements.CONDITION_BIZSTATE_EQUALS, BizStates.INEFFECT);
      definition.addCondition(Statements.CONDITION_TYPE_EQUALS, StatementType.normal);

      if (StringUtil.isNullOrEmpty(accountUnit) == false) {
        definition.addCondition(Statements.CONDITION_ACCOUNTUNITCODENAME_LIKE, accountUnit.trim());
      }

      if (StringUtil.isNullOrEmpty(statementNum) == false) {
        definition.addCondition(Statements.CONDITION_BILLNUMBER_STARTWITH, statementNum.trim());
      }

      if (StringUtil.isNullOrEmpty(settleNo) == false) {
        definition.addCondition(Statements.CONDITION_SETTLENO_EQUALS, settleNo.trim());
      }

      if (StringUtil.isNullOrEmpty(counterPart) == false) {
        definition.addCondition(Statements.CONDITION_COUNTERPARTCODESTARTNAMELIKE,
            counterPart.trim());
      }

      if (StringUtil.isNullOrEmpty(counterPartType) == false) {
        definition.addCondition(Statements.CONDITION_COUNTERPARTYPE_EQUALS, counterPartType);
      }

      if (StringUtil.isNullOrEmpty(contractNum) == false) {
        definition
            .addCondition(Statements.CONDITION_CONTRACT_BILLNUM_STARTWITH, contractNum.trim());
      }

      List<QueryOrder> orders = new ArrayList<QueryOrder>();

      if (filter.getOrders() != null && filter.getOrders().size() > 0) {
        for (com.hd123.rumba.gwt.base.client.QueryFilter.Order order : filter.getOrders()) {
          String orderField = null;
          if (ObjectUtil.equals(QStatement.FIELD_BILLNUMBER, order.getFieldName())) {
            orderField = Statements.ORDER_BY_BILL_NUMBER;
          } else if (ObjectUtil.equals(QStatement.FIELD_SETTLENO, order.getFieldName())) {
            orderField = Statements.ORDER_BY_SETTLE;
          } else if (ObjectUtil.equals(QStatement.FIELD_COUNTERPART, order.getFieldName())) {
            orderField = Statements.ORDER_BY_COUNTERPART_CODE;
          } else if (ObjectUtil.equals(QStatement.FIELD_CONTRACTNUMBER, order.getFieldName())) {
            orderField = Statements.ORDER_BY_CONTRACTNUMBER;
          }
          QueryOrderDirection dir = OrderDir.asc.equals(order.getDir()) ? QueryOrderDirection.asc
              : QueryOrderDirection.desc;
          orders.add(new QueryOrder(orderField, dir));
        }
      } else {
        String orderField = Statements.ORDER_BY_BILL_NUMBER;
        QueryOrderDirection dir = QueryOrderDirection.asc;
        orders.add(new QueryOrder(orderField, dir));
      }

      definition.setOrders(orders);
      definition.setPage(filter.getPage());
      definition.setPageSize(filter.getPageSize());
      QueryResult<Statement> queryResult = getStatementService().query(definition);
      if (queryResult == null)
        return null;
      RPageData<QStatement> rp = new RPageData<QStatement>();
      RPageDataUtil.copyPagingInfo(queryResult, rp);
      List<QStatement> result = new ArrayList<QStatement>();
      for (Statement source : queryResult.getRecords()) {
        QStatement target = new QStatement();
        target.setStatement(new BBill(source.getUuid(), source.getBillNumber()));
        target.setSettleNo(source.getSettleNo());
        if (source.getCounterpart() != null)
          target.setCounterpart(BCounterpartConverter.getInstance().convert(
              source.getCounterpart(), source.getCounterpartType()));
        if (source.getAccountUnit() != null)
          target.setAccountUnit(UCNBizConverter.getInstance().convert(source.getAccountUnit()));
        target.setContract(UCNBizConverter.getInstance().convert(source.getContract()));
        for (StatementAccRange range : source.getRanges()) {
          target.getAccountRanges()
              .add(
                  new BDateRange(range.getDateRange().getBeginDate(), range.getDateRange()
                      .getEndDate()));
        }
        result.add(target);
      }
      rp.setValues(result);
      return rp;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public QStatement queryStatementByBillNum(String billNumber) throws Exception {

    FlecsQueryDefinition definition = new FlecsQueryDefinition();

    // 启用账单授权
    boolean useStatementPerm = isPermEnabled(Statements.OBJECT_NAME);
    List<String> userGroups = new ArrayList<String>();
    if (useStatementPerm) {
      userGroups = getUserGroups(getSessionUser().getId());

      if (userGroups == null || userGroups.isEmpty()) {
        return null;
      }
    }

    // 启用账单授权
    if (useStatementPerm) {
      definition.addCondition(Statements.CONDITION_PERM_GROUP_ID_IN, userGroups.toArray());
    }

    definition.addCondition(Statements.CONDITION_BIZSTATE_EQUALS, BizStates.INEFFECT);
    // 过滤补录单
    definition.addCondition(Statements.CONDITION_TYPE_EQUALS, StatementType.normal);
    definition.addCondition(Statements.CONDITION_BILLNUMBER_STARTWITH, billNumber.trim());
    definition.getFetchParts().add(Statement.FETCH_PART_RANGES);

    QueryResult<Statement> queryResult = getStatementService().query(definition);
    if (queryResult == null || queryResult.getRecords().isEmpty())
      return null;

    Statement statement = queryResult.getRecords().get(0);
    if (statement == null)
      return null;
    // 受项目限制
    List<String> list = getUserStores(getSessionUser().getId());
    if (list.contains(statement.getAccountUnit().getUuid()) == false) {
      return null;
    }

    QStatement target = new QStatement();

    target.setStatement(new BBill(statement.getUuid(), statement.getBillNumber()));
    target.setSettleNo(statement.getSettleNo());
    if (statement.getCounterpart() != null)
      target.setCounterpart(BCounterpartConverter.getInstance().convert(statement.getCounterpart(),
          statement.getCounterpartType()));
    if (statement.getAccountUnit() != null)
      target.setAccountUnit(UCNBizConverter.getInstance().convert(statement.getAccountUnit()));
    target.setContract(UCNBizConverter.getInstance().convert(statement.getContract()));
    for (StatementAccRange range : statement.getRanges()) {
      target.getAccountRanges().add(
          new BDateRange(range.getDateRange().getBeginDate(), range.getDateRange().getEndDate()));
    }
    return target;
  }

  @Override
  public BStatementAdjust create() throws Exception {
    try {
      StatementAdjust statementAdjust = getStatementAdjustService().create();
      return StatementAdjustBizConverter.getInstance().convert(statementAdjust);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BStatementAdjust createByStatement(String statementUuid) throws ClientBizException {
    try {
      StatementAdjust statementAdjust = getStatementAdjustService()
          .createByStatement(statementUuid);
      BStatementAdjust result = StatementAdjustBizConverter.getInstance().convert(statementAdjust);

      QStatement statment = queryStatementByBillNum(statementAdjust.getStatement().getBillNumber());
      result.setAccountRanges(statment.getAccountRanges());
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BStatementAdjust save(BStatementAdjust entity, BProcessContext task) throws Exception {
    try {

      // 先判断流程权限
      doBeforeExecute(entity, task);

      StatementAdjust statementAdjust = null;
      if (entity.getUuid() == null) {
        statementAdjust = getStatementAdjustService().createByStatement(
            entity.getStatement().getBillUuid());
      } else {
        statementAdjust = getStatementAdjustService().get(entity.getUuid());
        if (statementAdjust == null)
          throw new M3ServiceException("找不到指定的{0}，可能已被删除。", "账单调整单");
      }

      write(entity, statementAdjust);
      String uuid = getStatementAdjustService().save(statementAdjust,
          ConvertHelper.getOperateContext(getSessionUser()));

      StatementAdjust source = getStatementAdjustService().get(uuid);
      doAfterSave(source, task);

      return StatementAdjustBizConverter.getInstance().convert(source);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BStatementAdjust load(String uuid, String scence) throws Exception {
    try {

      StatementAdjust source = getStatementAdjustService().get(uuid);
      if (source == null)
        return null;

      BStatementAdjust statementAdjust = StatementAdjustBizConverter.getInstance().convert(source);
      validEntityPerm(statementAdjust);

      FlecsQueryDefinition definition = new FlecsQueryDefinition();
      definition
          .addCondition(Statements.CONDITION_UUID_EQUALS, source.getStatement().getBillUuid());
      definition.getFetchParts().add(Statement.FETCH_PART_RANGES);
      List<Statement> statements = getStatementService().query(definition).getRecords();
      if (statements.isEmpty() == false) {
        for (StatementAccRange range : statements.get(0).getRanges()) {
          statementAdjust.getAccountRanges()
              .add(
                  new BDateRange(range.getDateRange().getBeginDate(), range.getDateRange()
                      .getEndDate()));
        }
      }

      return statementAdjust;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BStatementAdjust loadByNumber(String billNumber, String scence) throws Exception {
    try {

      StatementAdjust source = getStatementAdjustService().get(billNumber);
      if (source == null)
        return null;

      BStatementAdjust statementAdjust = StatementAdjustBizConverter.getInstance().convert(source);
      validEntityPerm(statementAdjust);

      FlecsQueryDefinition definition = new FlecsQueryDefinition();
      definition
          .addCondition(Statements.CONDITION_UUID_EQUALS, source.getStatement().getBillUuid());
      definition.getFetchParts().add(Statement.FETCH_PART_RANGES);
      List<Statement> statements = getStatementService().query(definition).getRecords();
      if (statements.isEmpty() == false) {
        for (StatementAccRange range : statements.get(0).getRanges()) {
          statementAdjust.getAccountRanges()
              .add(
                  new BDateRange(range.getDateRange().getBeginDate(), range.getDateRange()
                      .getEndDate()));
        }
      }

      return statementAdjust;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void delete(String uuid, long version) throws Exception {
    try {

      getStatementAdjustService().remove(uuid, version,
          ConvertHelper.getOperateContext(getSessionUser()));

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void abort(String uuid, long version, boolean effectStatement, String comment)
      throws Exception {
    try {

      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(StatementAdjusts.PROPERTY_MESSAGE, comment);

      getStatementAdjustService().abort(uuid, version, effectStatement, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BContract getOnlyOneContractByCounterpart(String counterpartUuid, String counterpartType)
      throws Exception {
    try {

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

      queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING, ContractState.using);
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS, counterpartUuid);

      // 对方单位uuid已确定，不需要指定合同类型。
      boolean useContractPerm = permEnabled(Counterparts.MODULE_PROPRIETOR.equals(counterpartType) ? com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR
          : com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      if (useContractPerm) {
        // 启用授权组
        List<String> userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups == null || userGroups.isEmpty())
          return null;

        queryDef.addCondition(Contracts.CONDITION_PERM_GROUP_ID_IN, userGroups.toArray());
      }

      QueryResult<Contract> result = getContractService().query(queryDef);
      if (result.getRecordCount() == 1) {
        BContract contract = ContractBizConverter.getInstance().convert(result.getRecords().get(0));
        // 受项目限制
        List<String> stores = getUserStores(getSessionUser().getId());
        if (stores.contains(contract.getAccountUnit().getUuid()) == false) {
          return null;
        }
        return contract;
      } else {
        return null;
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public QStatement getStatementByUuid(String uuid) throws Exception {
    try {

      Statement statement = getStatementService().get(uuid);
      if (statement == null)
        return null;

      QStatement result = new QStatement();
      for (StatementLine source : statement.getLines()) {
        QStatementLine target = new QStatementLine();
        target.setSubject(UCNBizConverter.getInstance().convert(
            source.getAcc1() != null ? source.getAcc1().getSubject() : null));
        target.setDirection(source.getAcc1() != null ? source.getAcc1().getDirection() : 0);
        target.setTotal(source.getTotal() != null ? source.getTotal().getTotal() : null);
        target.setTax(source.getTotal() != null ? source.getTotal().getTax() : null);
        target.setBeginDate(source.getAcc1() != null ? source.getAcc1().getBeginTime() : null);
        target.setEndDate(source.getAcc1() != null ? source.getAcc1().getEndTime() : null);

        target.setSourceBill(SourceBillBizConverter.getInstance().convert(
            source.getAcc1().getSourceBill()));
        BBillType billType = BillTypeUtils.getBillTypeByClassName(Statement.class.getName(), 0);
        if (billType == null)
          throw new M3ServiceException(R.R.unRegisterBillType());
        if (Accounts.NONE_BILL_NUMBER.equals(target.getSourceBill().getBillNumber())
            && billType.getName().equals(target.getSourceBill().getBillType())) {
          target.getSourceBill().setBillNumber(statement.getBillNumber());
          target.getSourceBill().setBillUuid(statement.getUuid());
        }

        target.setTaxRate(TaxRateBizConverter.getInstance().convert(
            source.getAcc1() != null ? source.getAcc1().getTaxRate() : null));
        target.setRemark(source.getAcc1() != null ? source.getAcc1().getRemark() : null);
        target
            .setInvoice(source.getAcc2() != null ? source.getAcc2().getInvoice() != null ? !Accounts.DISABLE_BILL_UUID
                .equals(source.getAcc2().getInvoice().getBillUuid()) : true
                : true);
        target.setAccId(source.getAccId());
        target.setAccountDate(source.getAcc1() != null ? source.getAcc1().getAccountDate() : null);
        result.getLines().add(target);
      }

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BBillType getBillType() throws ClientBizException {
    try {
      return BillTypeUtils.getBillTypeByClassName(StatementAdjust.class.getName(), 0);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void effect(String uuid, String comment, long oca) throws Exception {
    try {

      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(StatementAdjusts.PROPERTY_MESSAGE, comment);

      getStatementAdjustService().effect(uuid, oca, true, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String executeTask(BOperation operation, BStatementAdjust entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");

      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction()) == false
          && saveBeforeAction) {
        String uuid = getStatementAdjustService().save(
            new BizStatementAdjustConverter().convert(entity),
            ConvertHelper.getOperateContext(getSessionUser()));
        StatementAdjust result = getStatementAdjustService().get(uuid);
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
  public String validateAccountUnitAndCounterpartAndContractByStatement(QStatement statement)
      throws ClientBizException {
    try {
      if (statement == null)
        return null;

      List<String> userGroups = getUserGroups(getSessionUser().getId());
      BContract contract = validateContract(statement.getContract() == null ? null : statement
          .getContract().getUuid(), userGroups);
      if (contract == null)
        return R.R.contract();
      return null;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("未注册的单据类型账单。")
    public String unRegisterBillType();

    @DefaultStringValue("edit")
    public String editScence();

    @DefaultStringValue("账单调整单")
    String moduleCaption();

    @DefaultStringValue("账单调整单：{1}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("{0},{1},合同不是有效的数据，或者缺少授权组权限。")
    String accountUnitAndCounterpartAndContract();

    @DefaultStringValue("{0},{1}不是有效的数据，或者缺少授权组权限。")
    String accountUnitAndCounterpart();

    @DefaultStringValue("{0},合同不是有效的数据，或者缺少授权组权限。")
    String lackOfAuthorizatioGroupsAndContract();

    @DefaultStringValue("{0}不是有效的数据，或者缺少授权组权限。")
    String lackOfAuthorizatioGroups();

    @DefaultStringValue("合同不是有效的数据，或者缺少授权组权限。")
    String contract();

    @DefaultStringValue("指定的\"账单调整单\"不存在。")
    String nullObject();
  }

  private BContract validateContract(String contractBillUuid, List<String> userGroups)
      throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(contractBillUuid))
        return null;
      Contract contract = getContractService().get(contractBillUuid);
      if (contract == null)
        return null;
      else if (!ContractState.using.equals(contract.getState()))
        return null;
      // 启用合同授权
      boolean useContractPerm = Counterparts.MODULE_PROPRIETOR
          .equals(contract.getCounterpartType()) ? permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR)
          : permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      if (useContractPerm) {
        if (userGroups.isEmpty()) {
          return null;
        }
        if (userGroups.contains(contract.getPermGroupId()) == false) {
          return null;
        }
      }

      return contract == null ? null : ContractBizConverter.getInstance().convert(contract);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  protected Map<String, Object> getBpmVariables(Object obj, BTaskContext ctx) {
    Map<String, Object> variables = super.getBpmVariables(obj, ctx);
    if (obj instanceof StatementAdjust) {
      StatementAdjust bill = (StatementAdjust) obj;
      variables.put(BpmConstants.KEY_BILLCAPTION, CodecUtils.encode(MessageFormat.format(
          R.R.billCaption(), bill.getBillNumber(), bill.getContract().getCode())));
    }
    return variables;
  }

  private void write(BStatementAdjust source, StatementAdjust target) throws Exception {
    assert source != null;
    assert target != null;

    target.setPermGroupId(source.getPermGroupId());
    target.setPermGroupTitle(source.getPermGroupTitle());
    target.setUuid(source.getUuid());
    target.setVersion(source.getVersion());
    target.setBillNumber(source.getBillNumber());
    target.setSettleNo(source.getSettleNo());
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setContract(new UCN(source.getBcontract().getUuid(), source.getBcontract()
        .getBillNumber(), source.getBcontract().getTitle()));
    target.setStatement(BizBillConverter.getInstance().convert(source.getStatement()));
    target.setReceiptTotal(BizTotalConverter.getInstance().convert(
        new BTotal(source.getReceiptTotal(), source.getReceiptTax())));
    target.setPaymentTotal(BizTotalConverter.getInstance().convert(
        new BTotal(source.getPaymentTotal(), source.getPaymentTax())));
    target.setBizState(source.getBizState());
    target.setRemark(source.getRemark());

    target.getLines().clear();

    for (int i = 0; i < source.getLines().size(); i++) {
      BStatementAdjustLine line = source.getLines().get(i);
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        continue;
      target.getLines().add(writeLine(line));
    }
  }

  private StatementAdjustLine writeLine(BStatementAdjustLine source) throws Exception {
    assert source != null;
    StatementAdjustLine target = new StatementAdjustLine();
    target.setSubject(BizUCNConverter.getInstance().convert(source.getSubject()));
    target.setAmount(BizTotalConverter.getInstance().convert(
        new BTotal(source.getTotal().getTotal(), source.getTotal().getTax())));
    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));
    target.setDirection(source.getDirection());
    target.setInvoice(source.isInvoice());
    target.setDateRange(new DateRange(source.getBeginDate(), source.getEndDate()));
    target.setRemark(source.getRemark());
    target.setSourceAccountId(source.getSourceAccountId());
    target.setAccountDate(source.getAccountDate());
    target.setLastPayDate(source.getLastPayDate());

    return target;
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
        StatementAdjustQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(StatementAdjustQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() throws ClientBizException {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.getConditions().addAll(getPermConditions());

    return queryDef;
  }

  private String getScale() {
    String scale = getOptionService().getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE);
    return scale == null ? "2" : scale;
  }

  private String getRoundingMode() {
    String roundingMode = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_ROUNDING_MODE);
    return roundingMode == null ? RoundingMode.HALF_UP.name() : roundingMode;
  }

  private com.hd123.m3.account.service.statement.adjust.StatementAdjustService getStatementAdjustService()
      throws NamingException {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.statement.adjust.StatementAdjustService.class);
  }

  private StatementService getStatementService() throws NamingException {
    return M3ServiceFactory.getService(StatementService.class);
  }

}
