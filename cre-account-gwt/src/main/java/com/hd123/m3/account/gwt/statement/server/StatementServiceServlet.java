/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： StatementServiceServlet.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.server;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.acc.server.Acc1BizConverter;
import com.hd123.m3.account.gwt.acc.server.Acc2BizConverter;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BSourceBill;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.server.TotalBizConverter;
import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.perm.AccountSettlePermDef;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.perm.PayInvoiceRegPermDef;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegPermDef;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.perm.AccountDefrayalPermDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
import com.hd123.m3.account.gwt.statement.client.biz.BAccountSettleLog;
import com.hd123.m3.account.gwt.statement.client.biz.BAttachment;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccDetail;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccRange;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementSumLine;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementAccFilter;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.account.gwt.statement.server.converter.AccountSettleLogBizConverter;
import com.hd123.m3.account.gwt.statement.server.converter.BizStatementConverter;
import com.hd123.m3.account.gwt.statement.server.converter.StatementAccDetailBizConverter;
import com.hd123.m3.account.gwt.statement.server.converter.StatementBizConverter;
import com.hd123.m3.account.gwt.util.server.BillTypeUtils;
import com.hd123.m3.account.gwt.util.server.SubjectUsageUtils;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountDetail;
import com.hd123.m3.account.service.acc.AccountDetails;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.AccountSourceType;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.accounting.AccountingService;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.AccountSettles;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.contract.model.settle.Settlement;
import com.hd123.m3.account.service.statement.SettleState;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementAccountDetail;
import com.hd123.m3.account.service.statement.StatementDesActiveLine;
import com.hd123.m3.account.service.statement.StatementLine;
import com.hd123.m3.account.service.statement.StatementSubjectAccount;
import com.hd123.m3.account.service.statement.StatementType;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.account.service.statement.adjust.StatementAdjust;
import com.hd123.m3.account.service.statement.adjust.StatementAdjustService;
import com.hd123.m3.account.service.statement.adjust.StatementAdjusts;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.date.DateRange;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.mediaservice.service.MediaSFileInfo;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrder;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.entity.client.BOperateInfo;
import com.hd123.rumba.commons.gwt.entity.server.BOperatorConverter;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.util.MediaServiceUtil;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 客户端服务接口实现
 * 
 * @author huangjunxian
 * 
 */
public class StatementServiceServlet extends AccBPMServiceServlet implements StatementService {
  private static final long serialVersionUID = 300100L;

  @Override
  public String getObjectName() {
    return Statements.OBJECT_NAME;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    try {
      LOGGER.info("buildModuleContext start:");
      BBillType billType = BillTypeUtils.getBillTypeByClassName(Statement.class.getName(), null);
      LOGGER.info("getBillTypeByClassName end:");
      if (billType == null)
        throw new Exception(R.R.cannotFindBillType());
      moduleContext.put(KEY_STATEMENT_TYPE, billType.getName());
      moduleContext.put(SCALE, getScale());
      LOGGER.info("getScale end:");
      moduleContext.put(ROUNDING_MODE, getRoundingMode());
      LOGGER.info("getRoundingMode end:");
      moduleContext.put(KEY_COOPMODES, CollectionUtil.toString(getAllEnabledCoopModes()));

      moduleContext.put(KEY_BUILDING_TYPE, CollectionUtil.toString(new ArrayList<String>()));
      LOGGER.info("getAllEnabledCoopModes end:");

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    super.buildModuleContext(moduleContext, codecUtils);
    LOGGER.info("super.buildModuleContext end:");
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    LOGGER.info("===================准备获取账单模块用户权限 start:===================");
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);

    Set<String> resourceIds = new HashSet<String>();
    resourceIds.addAll(StatementPermDef.RESOURCE_STATEMENT_SET);
    resourceIds.addAll(AccountSettlePermDef.RESOURCE_ACCOUNTSETTLE_SET);
    resourceIds.addAll(PayInvoiceRegPermDef.RESOURCE_PAYINVOICEREG_SET);
    resourceIds.add(InvoiceRegPermDef.RESOURCE_KEY);
    resourceIds.addAll(PaymentPermDef.RESOURCE_PAYMENT_SET);
    resourceIds.addAll(ReceiptPermDef.RESOURCE_RECEIPT_SET);
    resourceIds.addAll(StatementAdjustPermDef.RESOURCE_STATEMENTADJUST_SET);
    resourceIds.addAll(AccountDefrayalPermDef.RESOURCE_ACCOUNTDEFRAYAL_SET);

    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        resourceIds);

    LOGGER.info("===================获取账单模块用户结束 =====================");
    return permissions;
  }

  @Override
  public RPageData<BStatement> query(List<Condition> conditions, List<String> visibleColumnNames,
      PageSort pageSort) throws ClientBizException {
    try {

      LOGGER.info("===============开始准备搜索账单=======================");

      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.getConditions().addAll(getPermConditions());
      queryDef.getFlecsConditions().addAll(
          StatementQueryBuilder.getInstance().buildFlecsConditions(conditions));
      queryDef.setOrders(StatementQueryBuilder.getInstance().buildQueryOrders(
          pageSort.getSortOrders()));
      queryDef.setPage(pageSort.getPage());
      queryDef.setPageSize(pageSort.getPageSize());
      queryDef.getFetchParts().add(Statement.FETCH_PART_RANGES);

      LOGGER.info("===============构造搜索条件完成，准备调用服务查询=======================");
      QueryResult<Statement> qr = getStatementService().query(queryDef);
      LOGGER.info("===============调用服务查询账单完毕=======================");
      RPageData<BStatement> result = new RPageData<BStatement>();
      RPageDataUtil.copyPagingInfo(qr, result);
      result.setValues(ConverterUtil.convert(qr.getRecords(), StatementBizConverter.getInstance()));

      // 更新导航数据
      updateNaviEntities(getObjectName(), result.getValues());

      LOGGER.info("===============查询账单后处理完毕，准备将数据返回到界面=======================");
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public RPageData<BAccountSettleLog> queryAccountSettleLog(List<Condition> conditions,
      PageSort pageSort) throws ClientBizException {
    try {

      FlecsQueryDefinition queryDefinition = new FlecsQueryDefinition();

      boolean useContractPermT = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      boolean useContractPermP = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR);
      if (useContractPermT || useContractPermP) {
        // 启用授权组
        List<String> userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups == null || userGroups.isEmpty())
          return new RPageData<BAccountSettleLog>();

        queryDefinition.addCondition(AccountSettles.CONDITION_PERM_CONTRACT_GROUP_ID_IN,
            useContractPermT, useContractPermP, userGroups);
      }
      // 受项目限制
      List<String> list = getUserStores(getSessionUser().getId());
      if (list.isEmpty()) {
        return new RPageData();
      }
      queryDefinition.addCondition(Contracts.CONDITION_ACCOUNT_UNIT_UUID_IN, list.toArray());
      queryDefinition.setPage(pageSort.getPage());
      queryDefinition.setPageSize(pageSort.getPageSize());
      queryDefinition.getFlecsConditions().addAll(
          AccountSettleQueryBuilder.getInstance().buildFlecsConditions(conditions));
      queryDefinition.setOrders(AccountSettleQueryBuilder.getInstance().buildQueryOrders(
          pageSort.getSortOrders()));

      QueryResult<AccountSettle> qr = getContractService().queryAccountSettle(queryDefinition);
      RPageData<BAccountSettleLog> rp = new RPageData<BAccountSettleLog>();
      RPageDataUtil.copyPagingInfo(qr, rp);
      rp.setValues(ConverterUtil.convert(qr.getRecords(),
          AccountSettleLogBizConverter.getInstance()));

      return rp;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BStatement load(String uuid, String startNode) throws ClientBizException {
    try {

      Statement source = getStatementService().get(uuid);
      if (source == null) {
        return null;
      }
      BStatement target = StatementBizConverter.getInstance().convert(source);
      // 授权组验证
      validEntityPerm(target);

      if (shouldDecorate(startNode)) {
        decorate(target, startNode);
      }
      return target;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BStatement loadByNumber(String billNumber, String startNode) throws ClientBizException {
    try {

      Statement source = getStatementService().getByNumber(billNumber, getFetchParts(startNode));
      if (source == null) {
        return null;
      }
      BStatement target = StatementBizConverter.getInstance().convert(source);
      // 授权组验证
      validEntityPerm(target);

      if (shouldDecorate(startNode)) {
        decorate(target, startNode);
      }
      return target;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public void remove(String uuid, long version) throws ClientBizException {
    try {

      getStatementService()
          .remove(uuid, version, ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public void effect(String uuid, long version) throws ClientBizException {
    try {

      getStatementService()
          .effect(uuid, version, ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public void abort(String uuid, long version) throws ClientBizException {
    try {

      getStatementService().abort(uuid, version, ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public RPageData<BStatementLine> queryAccount(StatementAccFilter filter,
      Collection<String> excludedUuids) throws ClientBizException {
    try {

      QueryDefinition queryDef = new QueryDefinition();
      // 默认条件
      if (StringUtil.isNullOrBlank(filter.getStatementUuid()) == false) {
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, filter.getStatementUuid());
        queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_LOCK_UUID);
        queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_EQUALS, Accounts.NONE_BILL_UUID);
      } else {
        queryDef.addCondition(Accounts.CONDITION_CAN_STATEMENT);
      }

      queryDef.addCondition(Accounts.CONDITION_ACTIVE);
      if (excludedUuids.isEmpty() == false) {
        queryDef.addCondition(Accounts.CONDITION_ACCID_NOTIN, excludedUuids.toArray());
      }
      if (StringUtil.isNullOrBlank(filter.getCounterpartUuid()) == false) {
        queryDef.addCondition(Accounts.CONDITION_COUNTERPART_UUID_EQUALS, filter
            .getCounterpartUuid().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getContractUuid()) == false) {
        queryDef.addCondition(Accounts.CONDITION_CONTRACT_ID_EQUALS, filter.getContractUuid()
            .trim());
      }
      // 查询条件
      if (StringUtil.isNullOrBlank(filter.getSourceBillType()) == false) {
        queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_TYPE_EQUALS, filter.getSourceBillType()
            .trim());
      }
      if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
        queryDef.addCondition(Accounts.CONDITION_SOURCEBILL_NUMBER_STARTWITH, filter
            .getSourceBillNumber().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getSubjectCode()) == false) {
        queryDef.addCondition(Accounts.CONDITION_SUBJECT_CODE_LIKE, filter.getSubjectCode().trim());
      }
      if (StringUtil.isNullOrBlank(filter.getSubjectName()) == false) {
        queryDef.addCondition(Accounts.CONDITION_SUBJECT_NAME_LIKE, filter.getSubjectName().trim());
      }
      if (filter.getBeginDate() != null || filter.getEndDate() != null) {
        queryDef.addCondition(Accounts.CONDITION_ACCOUNT_DATE_BETWEEN, filter.getBeginDate(),
            filter.getEndDate());
      }
      // 排序
      if (filter.getOrders() != null && filter.getOrders().isEmpty() == false) {
        for (com.hd123.rumba.gwt.base.client.QueryFilter.Order order : filter.getOrders()) {
          queryDef.getOrders().add(
              new QueryOrder(convertAccountOrderField(order.getFieldName()), OrderDir.asc
                  .equals(order.getDir()) ? QueryOrderDirection.asc : QueryOrderDirection.desc));
        }
      } else {
        queryDef.getOrders()
            .add(new QueryOrder(Accounts.ORDER_BY_SUBJECT, QueryOrderDirection.asc));
      }

      queryDef.setPage(filter.getPage());
      queryDef.setPageSize(filter.getPageSize());

      QueryResult<Account> qr = getAccountService().query(queryDef);
      RPageData<BStatementLine> result = new RPageData<BStatementLine>();
      RPageDataUtil.copyPagingInfo(qr, result);
      for (Account account : qr.getRecords()) {
        BStatementLine line = new BStatementLine();
        line.setUuid(account.getUuid());
        line.setAcc1(Acc1BizConverter.getInstance().convert(account.getAcc1()));
        line.setAcc2(Acc2BizConverter.getInstance().convert(account.getAcc2()));
        line.setTotal(TotalBizConverter.getInstance().convert(account.getTotal()));
        result.getValues().add(line);
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BStatementAccDetail getAccountDetail(String id, boolean active) throws ClientBizException {
    try {

      AccountDetail source = getAccountService().getDetail(id, active);
      return StatementAccDetailBizConverter.getInstance().convert(source);
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
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
  public Map<String, String> getBillTypeMap() throws ClientBizException {
    try {
      return BillTypeUtils.getAllNameCaption();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String executeTask(BOperation operation, BStatement entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");
      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction()) == false
          && saveBeforeAction) {
        String uuid = null;
        if (entity.getUuid() == null) {
          uuid = saveNew(entity);
        } else {
          uuid = saveModify(entity);
        }
        Statement result = getStatementService().get(uuid);
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
  public BAttachment uploadFile(String fileName) throws ClientBizException {
    try {
      MediaSFileInfo mfi = MediaServiceUtil.uploadFile(fileName);
      BAttachment attachment = new BAttachment();
      attachment.setId(mfi.getFileID());
      attachment.setName(mfi.getFileName());
      if (StringUtil.isNullOrBlank(attachment.getName())) {
        attachment.setName(new File(fileName).getName());
      }
      attachment.setSize(mfi.getFileSize());
      if (attachment.getSize() == 0) {
        FileInputStream fis = new FileInputStream(new File(fileName));
        attachment.setSize(fis.available());
        fis.close();
      }
      attachment.setCreateInfo(new BOperateInfo(new Date(), new BOperatorConverter()
          .convert(getSessionUser())));
      attachment.setUrl(MediaServiceUtil.getFileGetUrl(mfi.getFileID(), mfi.getFileName()));
      attachment.setFileType(attachment.getName().substring(attachment.getName().indexOf(".") + 1));
      return attachment;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BStatement create() throws ClientBizException {
    try {
      BStatement statement = new BStatement();
      statement.setType(StatementType.patch.name());
      statement.setSettleState(SettleState.initial.name());
      statement.setBizState(BBizStates.INEFFECT);

      return statement;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public List<BStatementLine> queryLines(StatementAccFilter filter, boolean includeSelf)
      throws ClientBizException {
    QueryDefinition def = new QueryDefinition();
    def.setPage(0);
    def.setPageSize(0);

    if (StringUtil.isNullOrBlank(filter.getStatementUuid()) == false) {
      def.addCondition(Statements.CONDITION_UUID_EQUALS, filter.getStatementUuid());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillType()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SRCBILLTYPE_EQUALS, filter.getSourceBillType());
    }
    if (includeSelf) {
      List<String> billNumbers = new ArrayList<String>();
      billNumbers.add(Contracts.EMPTY_BILL_NUMBER);
      if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
        billNumbers.add(filter.getSourceBillNumber().trim());
      }
      def.addCondition(Statements.LINE_CONDITION_SRCBILLNUMBER_IN, billNumbers.toArray());
    } else if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SRCBILLNUMBER_EQUALS, filter.getSourceBillNumber()
          .trim());
    }
    if (StringUtil.isNullOrBlank(filter.getSubjectUuid()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SUBJECT_EQUALS, filter.getSubjectUuid());
    }
    try {
      List<StatementLine> list = getStatementService().queryLines(def);
      List<BStatementLine> result = new ArrayList<BStatementLine>();
      for (StatementLine sl : list) {
        BStatementLine line = new BStatementLine();
        line.setAcc1(Acc1BizConverter.getInstance().convert(sl.getAcc1()));
        line.setAcc2(Acc2BizConverter.getInstance().convert(sl.getAcc2()));
        line.setAccSrcType(sl.getAccSrcType() == null ? null : sl.getAccSrcType().name());
        line.setTotal(TotalBizConverter.getInstance().convert(sl.getTotal()));
        line.setFreeTotal(TotalBizConverter.getInstance().convert(sl.getFreeTotal()));
        line.setFromStatement(sl.isFromStatement());
        line.setActive(true);
        line.setRemark(sl.getRemark());
        result.add(line);
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public List<BStatementLine> queryDesLines(StatementAccFilter filter, boolean includeSelf)
      throws ClientBizException {
    QueryDefinition def = new QueryDefinition();
    def.setPage(0);
    def.setPageSize(0);

    if (StringUtil.isNullOrBlank(filter.getStatementUuid()) == false) {
      def.addCondition(Statements.CONDITION_UUID_EQUALS, filter.getStatementUuid());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillType()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SRCBILLTYPE_EQUALS, filter.getSourceBillType());
    }
    if (includeSelf) {
      List<String> billNumbers = new ArrayList<String>();
      billNumbers.add(Contracts.EMPTY_BILL_NUMBER);
      if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
        billNumbers.add(filter.getSourceBillNumber().trim());
      }
      def.addCondition(Statements.LINE_CONDITION_SRCBILLNUMBER_IN, billNumbers.toArray());
    } else if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SRCBILLNUMBER_EQUALS, filter.getSourceBillNumber()
          .trim());
    }
    if (StringUtil.isNullOrBlank(filter.getSubjectUuid()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SUBJECT_EQUALS, filter.getSubjectUuid());
    }
    try {
      List<StatementDesActiveLine> list = getStatementService().queryDesLines(def);
      List<BStatementLine> result = new ArrayList<BStatementLine>();
      for (StatementDesActiveLine sl : list) {
        // 忽略系统附加生成的无效账款
        if (sl.isSystemAdded()) {
          continue;
        }
        BStatementLine line = new BStatementLine();
        line.setAcc1(Acc1BizConverter.getInstance().convert(sl.getAcc1()));
        line.setAcc2(Acc2BizConverter.getInstance().convert(sl.getAcc2()));
        line.setAccSrcType(sl.getAccSrcType() == null ? null : sl.getAccSrcType().name());
        line.setSystemAdded(sl.isSystemAdded());
        line.setTotal(TotalBizConverter.getInstance().convert(sl.getTotal()));
        line.setFreeTotal(TotalBizConverter.getInstance().convert(sl.getFreeTotal()));
        line.setActive(false);
        result.add(line);
      }
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public List<BStatementAccDetail> queryAccDetails(StatementAccFilter filter, boolean includeSelf)
      throws ClientBizException {
    QueryDefinition def = new QueryDefinition();
    def.setPage(0);
    def.setPageSize(0);

    if (StringUtil.isNullOrBlank(filter.getStatementUuid()) == false) {
      def.addCondition(Statements.CONDITION_UUID_EQUALS, filter.getStatementUuid());
    }
    if (StringUtil.isNullOrBlank(filter.getSourceBillType()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SRCBILLTYPE_EQUALS, filter.getSourceBillType());
    }
    if (includeSelf) {
      List<String> billNumbers = new ArrayList<String>();
      billNumbers.add(Contracts.EMPTY_BILL_NUMBER);
      if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
        billNumbers.add(filter.getSourceBillNumber().trim());
      }
      def.addCondition(Statements.LINE_CONDITION_SRCBILLNUMBER_IN, billNumbers.toArray());
    } else if (StringUtil.isNullOrBlank(filter.getSourceBillNumber()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SRCBILLNUMBER_EQUALS, filter.getSourceBillNumber()
          .trim());
    }
    if (StringUtil.isNullOrBlank(filter.getSubjectUuid()) == false) {
      def.addCondition(Statements.LINE_CONDITION_SUBJECT_EQUALS, filter.getSubjectUuid());
    }
    try {
      List<StatementAccountDetail> list = getStatementService().queryDetails(def);
      if (list.isEmpty()) {
        return new ArrayList<BStatementAccDetail>();
      }
      List<String> accIds = new ArrayList<String>();
      Map<String, AccountSourceType> typeMap = new HashMap<String, AccountSourceType>();
      for (StatementAccountDetail d : list) {
        accIds.add(d.getAccId());
        typeMap.put(d.getAccId(), d.getAccSrcType());
      }
      List<AccountDetail> details = getAccountService().getDetail(accIds, true,
          AccountDetails.FETCH_DETAIL_CONTENT);

      StatementAccDetailBizConverter.getInstance().setTypeMap(typeMap);
      return ConverterUtil.convert(details, StatementAccDetailBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BUCN getSubjectByCode(String code) {
    if (StringUtil.isNullOrBlank(code))
      return null;
    Subject src = getSubjectService().getByCode(code.trim());
    return UCNBizConverter.getInstance().convert(src);
  }

  @Override
  public BStatement save(BStatement entity, BProcessContext task) throws ClientBizException {
    try {
      // 先判断流程权限
      doBeforeExecute(entity, task);

      String uuid = null;
      if (entity.getUuid() == null) {
        uuid = saveNew(entity);
      } else {
        uuid = saveModify(entity);
      }

      Statement result = getStatementService().get(uuid);

      doAfterSave(result, task);

      return StatementBizConverter.getInstance().convert(result);
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  public BContract getOnlyOneContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException {
    try {
      return getSingleContract(accountUnitUuid, counterpartUuid, counterpartType);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String validateBeforeAction(String uuid, String action) {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }
    FlecsQueryDefinition def = new FlecsQueryDefinition();
    def.addCondition(StatementAdjusts.CONDITION_STATEMENT_EQUALS, uuid);
    def.addCondition(StatementAdjusts.CONDITION_BIZSTATE_IN, BizStates.INEFFECT, BizStates.EFFECT);
    def.setPageSize(1);
    QueryResult<StatementAdjust> qr = getAdjustService().query(def);
    if (qr.getRecords().isEmpty()) {
      return null;
    } else {
      if (BBizActions.DELETE.equals(action)) {
        return R.R.removeValidateMsg();
      } else {
        return R.R.abortValidateMsg();
      }
    }
  }

  @Override
  public void cleanSettle(String uuid) throws ClientBizException {
    try {
      getAccountingService().cleanEmptySettle(uuid);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public Boolean isMaxSettled(String accountSettleUuid) {
    return getStatementService().isSettleMax(accountSettleUuid);
  }

  /**********************************************/
  private String saveNew(BStatement entity) throws ClientBizException {
    try {

      // 当前操作时间
      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      preHandlerLines(entity, operCtx);
      Statement target = BizStatementConverter.getInstance().convert(entity);
      return getStatementService().save(target, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  private String saveModify(BStatement entity) throws ClientBizException {
    try {

      // 当前操作时间
      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      preHandlerLines(entity, operCtx);
      Statement statement = BizStatementConverter.getInstance().convert(entity);
      return getStatementService().save(statement,
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  private void preHandlerLines(BStatement entity, BeanOperateContext operCtx) {
    if (StatementType.patch.name().equals(entity.getType()) == false) {
      for (BStatementLine line : entity.getLines()) {
        line.getAcc2().setLocker(BAccount.NONE_LOCKER);
      }
      return;
    }
    // 注意此处将 Acc1中的SourceBill赋值
    BillType statementBillType = getBillTypeService().getByClassName(Statement.class.getName());
    BSourceBill sourceBill = new BSourceBill(BAccount.NONE_BILL_UUID, BAccount.NONE_BILL_NUMBER,
        statementBillType.getName());

    // 为账单周期accountRange赋值
    Date beginDate = null;
    Date endDate = null;
    for (BStatementLine line : entity.getLines()) {
      if (line.isValid() == false)
        continue;
      line.setFreeTotal(BTotal.zero());
      repairAcc1(entity, line, sourceBill);
      repairAcc2(entity, line);
      if (beginDate != null) {
        beginDate = DateRange.min(beginDate, line.getAcc1().getBeginTime());
      } else {
        beginDate = line.getAcc1().getBeginTime();
      }
      if (endDate != null) {
        endDate = DateRange.max(endDate, line.getAcc1().getEndTime());
      } else {
        endDate = line.getAcc1().getEndTime();
      }
    }
    entity.setAccountTime(operCtx.getTime());
    entity.getRanges().clear();
    BStatementAccRange range = new BStatementAccRange();
    range.setDateRange(new BDateRange(beginDate, endDate));
    entity.getRanges().add(range);
    // 付款免租金额、收款免租金额=0
    entity.setFreePayTotal(BTotal.zero());
    entity.setFreeReceiptTotal(BTotal.zero());
  }

  private void repairAcc1(BStatement entity, BStatementLine line, BSourceBill sourceBill) {
    line.getAcc1().setSourceBill(sourceBill);
    line.getAcc1().setAccountUnit(entity.getAccountUnit());
    line.getAcc1().setContract(entity.getContract());
    line.getAcc1().setCounterpart(entity.getCounterpart());
  }

  private void repairAcc2(BStatement entity, BStatementLine line) {
    // 锁定者
    line.getAcc2().setLocker(BAccount.NONE_LOCKER);
    // 收付款信息
    line.getAcc2().setPayment(BAccount.NONE_BILL);
    // 账单信息
    line.getAcc2().setStatement(BAccount.NONE_BILL);
  }

  @Override
  protected Map<String, Object> getBpmVariables(Object obj, BTaskContext ctx) {
    Map<String, Object> variables = super.getBpmVariables(obj, ctx);
    if (obj instanceof Statement) {
      Statement bill = (Statement) obj;
      variables.put(
          BpmConstants.KEY_BILLCAPTION,
          CodecUtils.encode(MessageFormat.format(R.R.billCaption(), bill.getBillNumber(),
              bill.getContract() == null ? "" : bill.getContract().getCode())));
    }
    return variables;
  }

  private String[] getFetchParts(String startNode) {
    if (StatementUrlParams.AccEdit.START_NODE.equals(startNode)) {
      return new String[] {
        Statement.FETCH_PART_LINES };
    } else if (StatementUrlParams.EditPatch.START_NODE.equals(startNode)
        || StatementUrlParams.Edit.START_NODE.equals(startNode)
        || StatementUrlParams.Create.START_NODE.equals(startNode)
        || StatementUrlParams.View.START_NODE.equals(startNode)) {
      return new String[] {
          Statement.FETCH_PART_LINES, Statement.FETCH_PART_DESACTIVELINES,
          Statement.FETCH_PART_ATTACH, Statement.FETCH_PART_RANGES };
    } else {
      return new String[0];
    }
  }

  private boolean shouldDecorate(String startNode) {
    if (StatementUrlParams.View.START_NODE.equals(startNode)
        || StatementUrlParams.Create.START_NODE.equals(startNode)
        || StatementUrlParams.Edit.START_NODE.equals(startNode)) {
      return true;
    }
    return false;
  }

  private void decorate(BStatement entity, String startNode) throws Exception {
    if (StatementUrlParams.Edit.START_NODE.equals(startNode) == false) {
      aggregate(entity);
    }
    decorateSettlement(entity);
  }

  private void decorateSettlement(BStatement entity) throws Exception {
    // 正常账单，周期账款类型为提成或销售返款才需要显示销售额
    if (StatementType.normal.name().equals(entity.getType())) {
      Set<String> settlementIds = new HashSet<String>();
      for (BStatementAccRange line : entity.getRanges()) {
        if (StringUtil.isNullOrBlank(line.getSettlementId())) {
          continue;
        }
        settlementIds.add(line.getSettlementId());
      }
      if (settlementIds.isEmpty()) {
        return;
      }
      List<Settlement> settlements = getContractService().getSettlements(settlementIds);
      if (settlements.isEmpty()) {
        return;
      }
      Set<String> accountTypes = new HashSet<String>();
      for (Settlement s : settlements) {
        accountTypes.addAll(s.getDef().getAccountTypes());
      }
      List<BSubjectUsage> list = SubjectUsageUtils.getAll();
      for (BSubjectUsage usage : list) {
        if (accountTypes.contains(usage.getCode()) == false) {
          continue;
        }
        if (UsageType.saleDeduct.name().equals(usage.getUsageType().name())
            || UsageType.salePayment.name().equals(usage.getUsageType().name())) {
          entity.setShowRefer(true);
          break;
        }
      }
    }
  }

  private void aggregate(BStatement entity) {
    Collection<List> group = group(entity.getLines(), new Comparator<BStatementLine>() {

      @Override
      public int compare(BStatementLine o1, BStatementLine o2) {
        if (ObjectUtil.equals(o1.getAcc1().getSubject(), o2.getAcc1().getSubject())
            && o1.getAcc1().getDirection() == o2.getAcc1().getDirection())
          return 0;
        else
          return 1;
      }
    });
    List<StatementSubjectAccount> accounts = getStatementService().getStatementAccount(
        entity.getUuid());
    for (List<BStatementLine> list : group) {
      BStatementLine fline = list.get(0);
      if (fline.getAcc1() == null || fline.getAcc1().getSubject() == null)
        continue;
      BStatementSumLine sl = new BStatementSumLine();
      sl.setSubjectUuid(fline.getAcc1().getSubject().getUuid());
      sl.setSubjectCode(fline.getAcc1().getSubject().getCode());
      sl.setSubjectName(fline.getAcc1().getSubject().getName());
      sl.setDirection(fline.getAcc1().getDirection());
      StatementSubjectAccount account = findAccount(accounts, sl.getSubjectUuid(),
          sl.getDirection());
      if (account != null) {
        sl.setSettled(account.getSettled() == null ? null : account.getSettled().getTotal());
        sl.setInvoiced(account.getInvoiced() == null ? null : account.getInvoiced()
            .add(account.getInvoiceAdj()).getTotal());
      }

      BTotal total = BTotal.zero();
      BigDecimal invoiceTotal = BigDecimal.ZERO;
      for (BStatementLine l : list) {
        total = total.add(l.getTotal());
        if (l.getInvoice() && l.getTotal() != null && l.getTotal().getTotal() != null) {
          invoiceTotal = invoiceTotal.add(l.getTotal().getTotal());
        }
      }
      sl.setTotal(total);
      sl.setNeedInvoice(invoiceTotal);
      entity.getSumLines().add(sl);
    }

    // 排序-科目代码升序
    Collections.sort(entity.getSumLines(), new Comparator<BStatementSumLine>() {

      @Override
      public int compare(BStatementSumLine o1, BStatementSumLine o2) {
        if (o1.getSubjectCode() == null || o2.getSubjectCode() == null)
          return 0;
        return o1.getSubjectCode().compareTo(o2.getSubjectCode());
      }
    });
  }

  private StatementSubjectAccount findAccount(List<StatementSubjectAccount> accounts,
      String subjectUuid, int direction) {
    if (accounts == null || accounts.isEmpty())
      return null;

    for (StatementSubjectAccount acc : accounts) {
      if (ObjectUtil.equals(subjectUuid, acc.getSubject()) && direction == acc.getDirection())
        return acc;
    }

    return null;
  }

  protected static Collection group(Collection elements, Comparator c) {
    List<List> result = new ArrayList<List>();
    for (Object element : elements) {
      boolean found = false;
      for (List<Object> group : result) {
        if (c.compare(group.get(0), element) == 0) {
          group.add(element);
          found = true;
          break;
        }
      }
      if (!found) {
        List<Object> group = new ArrayList<Object>();
        group.add(element);
        result.add(group);
      }
    }
    return result;

  }

  private String convertAccountOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (StatementUrlParams.AccountFilter.FIELD_SUBJECT.equals(fieldName))
      return Accounts.ORDER_BY_SUBJECT;
    else if (StatementUrlParams.AccountFilter.FIELD_DIRECTION.equals(fieldName))
      return Accounts.ORDER_BY_DIRECTION;
    else if (StatementUrlParams.AccountFilter.FIELD_TOTAL.equals(fieldName))
      return Accounts.ORDER_BY_TOTAL;
    else if (StatementUrlParams.AccountFilter.FIELD_TAX.equals(fieldName))
      return Accounts.ORDER_BY_TAX;
    else if (StatementUrlParams.AccountFilter.FIELD_DATERANGE.equals(fieldName))
      return Accounts.ORDER_BY_BEGINTIME;
    else if (StatementUrlParams.AccountFilter.FIELD_SOURCEBILLTYPE.equals(fieldName))
      return Accounts.ORDER_BY_SOURCEBILLTYPE;
    else if (StatementUrlParams.AccountFilter.FIELD_SOURCEBILLNUMBER.equals(fieldName))
      return Accounts.ORDER_BY_SOURCEBILLNUMBER;
    return null;
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

  private com.hd123.m3.account.service.statement.StatementService getStatementService() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.statement.StatementService.class);
  }

  private AccountService getAccountService() {
    return M3ServiceFactory.getService(AccountService.class);
  }

  private AccountingService getAccountingService() {
    return M3ServiceFactory.getService(AccountingService.class);
  }

  private BillTypeService getBillTypeService() {
    return M3ServiceFactory.getService(BillTypeService.class);
  }

  private SubjectService getSubjectService() {
    return M3ServiceFactory.getService(SubjectService.class);
  }

  private StatementAdjustService getAdjustService() {
    return M3ServiceFactory.getService(StatementAdjustService.class);
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("账单")
    String moduleCaption();

    @DefaultStringValue("账单：{0},合同编号：{1}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("获取单据类型出错：")
    String findBillTypeError();

    @DefaultStringValue("找不到账单的单据类型配置。")
    String cannotFindBillType();

    @DefaultStringValue("指定的\"账单\"不存在。")
    String nullObject();

    @DefaultStringValue("：")
    String caption_separator();

    @DefaultStringValue("{0},{1}不是有效的数据，或者缺少授权组权限。")
    String accountUnitAndCounterpart();

    @DefaultStringValue("{0}不是有效的数据，或者缺少授权组权限。")
    String lackOfAuthorizatioGroups();

    @DefaultStringValue("指定要删除的账单，存在对应有效的账单调账单，删除账单将影响账单调整单。")
    String removeValidateMsg();

    @DefaultStringValue("指定要作废的账单，存在对应有效的账单调账单，作废账单将影响账单调整单。")
    String abortValidateMsg();
  }

}
