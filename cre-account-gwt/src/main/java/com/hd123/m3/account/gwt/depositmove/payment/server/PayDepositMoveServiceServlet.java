/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositMoveServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.server;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.server.BCounterpartConverter;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.commons.server.converter.BizDepositMoveConverter;
import com.hd123.m3.account.gwt.depositmove.commons.server.converter.DepositMoveBizConverter;
import com.hd123.m3.account.gwt.depositmove.payment.client.rpc.PayDepositMoveService;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.perm.PayDepositMovePermDef;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.depositmove.DepositMove;
import com.hd123.m3.account.service.depositmove.DepositMoveService;
import com.hd123.m3.account.service.depositmove.DepositMoves;
import com.hd123.m3.commons.biz.BpmOperateContext;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositMoveServiceServlet extends AccBPMServiceServlet implements
    PayDepositMoveService {
  private static final long serialVersionUID = -6126674725813743058L;

  @Override
  public String getObjectName() {
    return DepositMoves.OBJECT_NAME_PAY;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        PayDepositMovePermDef.RESOURCE_PAYDEPOSITMOVE_SET);
    return permissions;
  }

  @Override
  public RPageData<BDepositMove> query(List<Condition> conditions, List<String> visibleColumnNames,
      PageSort pageSort) throws ClientBizException {
    try {

      FlecsQueryDefinition queryDefinition = buildQueryDefinition(conditions, pageSort);
      if (queryDefinition == null)
        return new RPageData<BDepositMove>();

      QueryResult<DepositMove> queryResult = getDepositMoveService().query(queryDefinition);

      RPageData<BDepositMove> result = new RPageData<BDepositMove>();
      RPageDataUtil.copyPagingInfo(queryResult, result);
      result.setValues(ConverterUtil.convert(queryResult.getRecords(),
          DepositMoveBizConverter.getInstance()));

      // 更新导航数据
      updateNaviEntities(getObjectName(), result.getValues());
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositMove load(String uuid) throws ClientBizException {
    try {

      DepositMove source = getDepositMoveService().get(uuid);

      BDepositMove result = DepositMoveBizConverter.getInstance().convert(source);
      if (validateEntity(result) == false)
        return null;

      result.setOutBalance(getBalance(result.getAccountUnit().getUuid(), result.getOutCounterpart()
          .getUuid(), result.getOutSubject().getUuid(), result.getOutContract() == null ? null
          : result.getOutContract().getUuid()));

      result.setInBalance(getBalance(result.getAccountUnit().getUuid(), result.getInCounterpart()
          .getUuid(), result.getInSubject().getUuid(), result.getInContract() == null ? null
          : result.getInContract().getUuid()));
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositMove loadByNumber(String billNumber) throws ClientBizException {
    try {

      DepositMove source = getDepositMoveService().get(billNumber);

      BDepositMove result = DepositMoveBizConverter.getInstance().convert(source);
      if (validateEntity(result) == false)
        return null;

      result.setOutBalance(getBalance(result.getAccountUnit().getUuid(), result.getOutCounterpart()
          .getUuid(), result.getOutSubject().getUuid(), result.getOutContract() == null ? null
          : result.getOutContract().getUuid()));

      result.setInBalance(getBalance(result.getAccountUnit().getUuid(), result.getInCounterpart()
          .getUuid(), result.getInSubject().getUuid(), result.getInContract() == null ? null
          : result.getInContract().getUuid()));

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositMove create() throws ClientBizException {
    try {
      DepositMove source = getDepositMoveService().create(Direction.PAYMENT);

      BDepositMove move = DepositMoveBizConverter.getInstance().convert(source);

      return move;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositMove createByAdvance(String advanceUuid) throws ClientBizException {
    try {

      DepositMove source = getDepositMoveService().create(advanceUuid);
      BDepositMove move = DepositMoveBizConverter.getInstance().convert(source);

      move.setOutBalance(getBalance(move.getAccountUnit().getUuid(), move.getOutCounterpart()
          .getUuid(), move.getOutSubject().getUuid(), move.getOutContract() == null ? null : move
          .getOutContract().getUuid()));

      return move;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BDepositMove save(BDepositMove entity, BProcessContext task) throws ClientBizException {
    try {

      // 先判断流程权限
      doBeforeExecute(entity, task);

      DepositMove target = null;
      if (entity.getUuid() == null)
        target = getDepositMoveService().create(Direction.PAYMENT);
      else
        target = getDepositMoveService().get(entity.getUuid());
      write(entity, target);

      String uuid = getDepositMoveService().save(target,
          ConvertHelper.getOperateContext(getSessionUser()));
      DepositMove result = getDepositMoveService().get(uuid);

      doAfterSave(result, task);

      return DepositMoveBizConverter.getInstance().convert(result);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void remove(String uuid, long version) throws ClientBizException {
    try {

      getDepositMoveService().remove(uuid, version,
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BAdvanceContract getUniqueContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType, boolean isGetAdvance) throws ClientBizException {
    try {

      BAdvanceContract result;
      if (isGetAdvance)
        result = getContractFromAdvance(accountUnitUuid, counterpartUuid, counterpartType);
      else
        result = getContractFromContract(accountUnitUuid, counterpartUuid, counterpartType);

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BigDecimal getBalance(String accountUnitUuid, String counterpartUuid, String subjectUuid,
      String contractUuid) throws ClientBizException {
    try {

      if (contractUuid == null)
        contractUuid = Advances.NONE_BILL_UUID;

      Advance adv = getAdvanceService().get(accountUnitUuid, counterpartUuid, contractUuid,
          subjectUuid);

      if (adv == null)
        return BigDecimal.ZERO;

      return adv.getTotal();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void effect(String uuid, String comment, long oca) throws Exception {
    try {

      BpmOperateContext coperCtx = new BpmOperateContext(getSessionUser(), comment);
      getDepositMoveService().effect(uuid, oca, coperCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void abort(String uuid, String comment, long oca) throws Exception {
    try {

      BpmOperateContext coperCtx = new BpmOperateContext(getSessionUser(), comment);
      getDepositMoveService().abort(uuid, oca, coperCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String executeTask(BOperation operation, BDepositMove entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");
      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (saveBeforeAction
          && !BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction())) {

        String uuid = getDepositMoveService().save(
            BizDepositMoveConverter.getInstance().convert(entity),
            ConvertHelper.getOperateContext(getSessionUser()));

        DepositMove result = getDepositMoveService().get(uuid);
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
    if (obj instanceof DepositMove) {
      DepositMove bill = (DepositMove) obj;
      variables.put(
          BpmConstants.KEY_BILLCAPTION,
          CodecUtils.encode(MessageFormat.format(R.R.billCaption(), bill.getBillNumber(),
              bill.getOutContract() == null ? "" : bill.getOutContract().getCode())));
    }
    return variables;
  }

  private BAdvanceContract getContractFromAdvance(String accountUnitUuid, String counterpartUuid,
      String counterpartType) {
    // 受项目限制
    List<String> stores = getUserStores(getSessionUser().getId());
    if (stores.contains(accountUnitUuid) == false) {
      return null;
    }
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnitUuid.trim());
    queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, Direction.PAYMENT);
    queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpartUuid.trim());

    QueryResult<Advance> qr = getAdvanceService().query(queryDef);

    if (qr.getRecords() == null)
      return null;

    List<String> advanceUuids = new ArrayList<String>();
    List<Advance> advances = new ArrayList<Advance>();
    for (Advance advacne : qr.getRecords()) {
      if (advacne.getBill().getUuid().equals(Advances.NONE_BILL_NUMBER))
        continue;
      if (advanceUuids.contains(advacne.getBill().getUuid()))
        continue;
      else {
        advanceUuids.add(advacne.getBill().getUuid());
        advances.add(advacne);
      }
    }
    if (advances.size() != 1)
      return null;

    BAdvanceContract result = new BAdvanceContract();
    result.setAccountUnit(UCNBizConverter.getInstance().convert(advances.get(0).getAccountUnit()));
    result.setContract(UCNBizConverter.getInstance().convert(advances.get(0).getBill()));
    result.setCounterpart(BCounterpartConverter.getInstance().convert(
        advances.get(0).getCounterpart(), advances.get(0).getCounterpartType()));

    return result;
  }

  private BAdvanceContract getContractFromContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) {

    BContract contract = getSingleContract(accountUnitUuid, counterpartUuid, counterpartType);
    if (contract == null) {
      return null;
    }

    BAdvanceContract result = new BAdvanceContract();
    result.setContract(new BUCN(contract.getUuid(), contract.getBillNumber(), contract.getTitle()));
    result.setCounterpart(contract.getCounterpart());

    return result;
  }

  private boolean validateEntity(BDepositMove source) throws Exception {
    if (source == null)
      return false;
    if (source.getDirection() == Direction.RECEIPT)
      return false;

    validEntityPerm(source);
    return true;
  }

  private void write(BDepositMove source, DepositMove target) {
    assert source != null;
    assert target != null;

    target.setPermGroupId(source.getPermGroupId());
    target.setPermGroupTitle(source.getPermGroupTitle());
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setAmount(source.getAmount());
    target.setDirection(Direction.PAYMENT);
    target.setInContract(BizUCNConverter.getInstance().convert(source.getInContract()));
    target.setInCounterpart(BizUCNConverter.getInstance().convert(source.getInCounterpart()));
    target.setInCounterpartType(source.getInCounterpart() == null ? null : source
        .getInCounterpart().getCounterpartType());
    target.setInSubject(BizUCNConverter.getInstance().convert(source.getInSubject()));
    target.setOutContract(BizUCNConverter.getInstance().convert(source.getOutContract()));
    target.setOutCounterpart(BizUCNConverter.getInstance().convert(source.getOutCounterpart()));
    target.setOutCounterpartType(source.getOutCounterpart() == null ? null : source
        .getOutCounterpart().getCounterpartType());
    target.setOutSubject(BizUCNConverter.getInstance().convert(source.getOutSubject()));
    target.setRemark(source.getRemark());
    target.setVersion(source.getVersion());
    target.setAccountDate(source.getAccountDate());
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
        PayDepositMoveQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(PayDepositMoveQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    queryDef.addCondition(DepositMoves.CONDITION_DIRECTION_EQUALS, Direction.PAYMENT);
    queryDef.getConditions().addAll(getPermConditions());
    return queryDef;
  }

  private DepositMoveService getDepositMoveService() {
    return M3ServiceFactory.getService(DepositMoveService.class);
  }

  private AdvanceService getAdvanceService() {
    return M3ServiceFactory.getService(AdvanceService.class);
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("预付款转移单")
    String moduleCaption();

    @DefaultStringValue("预付款转移单：{0},合同编号：{1}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("获取单据类型出错：")
    String findBillTypeError();

    @DefaultStringValue("指定的\"预付款转移单\"不存在。")
    String nullObject();
  }
}
