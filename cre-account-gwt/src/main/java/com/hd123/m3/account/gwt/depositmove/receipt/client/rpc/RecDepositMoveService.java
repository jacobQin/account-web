/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositMoveService.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.receipt.client.rpc;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author zhuhairui
 * 
 */
@RemoteServiceRelativePath("account/recDepositMove")
public interface RecDepositMoveService extends M3ModuleService, M3ModuleNaviService {
  public static final String PUKEY_UNIT = "account_recDepositMove";

  /** 键值：打印模板文件 */
  public static final String KEY_BQFILE = "_BQFile";

  public static class Locator {
    private static RecDepositMoveServiceAsync service = null;

    public static RecDepositMoveServiceAsync getService() {
      if (service == null)
        service = GWT.create(RecDepositMoveService.class);
      return service;
    }
  }

  /**
   * 获取预付款转移单列表。
   * 
   * @param conditions
   * @param visibleColumnNames
   * @param pageSort
   *          分页信息。
   * @return 预付款列表。
   * @throws ClientBizException
   */
  public RPageData<BDepositMove> query(List<Condition> conditions, List<String> visibleColumnNames,
      PageSort pageSort) throws ClientBizException;

  /**
   * 通过uuid加载预付款转移单。
   * 
   * @param uuid
   *          预付款转移单uuid。 not null。
   * @return 预付款转移单。
   * @throws ClientBizException
   */
  public BDepositMove load(String uuid) throws ClientBizException;

  /**
   * 通过单号加载预付款转移单。
   * 
   * @param billNumber
   *          预付款转移单单号。 not null。
   * @return 预付款转移单。
   * @throws ClientBizException
   */
  public BDepositMove loadByNumber(String billNumber) throws ClientBizException;

  /**
   * 创建预付款转移单。
   * 
   * @return 预付款转移单。
   * @throws ClientBizException
   */
  public BDepositMove create() throws ClientBizException;

  /**
   * 根据预存款账户创建预存款转移单。
   * 
   * @param advanceUuid
   *          预存款账户标识，not null。
   * @return 预存款转移单。
   * @throws ClientBizException
   */
  public BDepositMove createByAdvance(String advanceUuid) throws ClientBizException;

  /**
   * 保存预付款转移单。
   * 
   * @param entity
   *          预付款转移单。
   * @param task
   *          BPM任务上下文。
   * @return 预付款转移单。
   * @throws ClientBizException
   */
  public BDepositMove save(BDepositMove entity, BProcessContext task) throws ClientBizException;

  /**
   * 删除预付款转移单。
   * 
   * @param uuid
   *          指定删除的预付款转移单uuid。 not null。
   * @param version
   *          版本控制 not null。
   * @throws ClientBizException
   */
  public void remove(String uuid, long version) throws ClientBizException;

  /**
   * 获取唯一的合同，该合同从预存款账户里面查询。如果存在多个或者没有预存款账户均返回null，否则返回预存款账户的合同信息。
   * 
   * @param counterpartUuid
   *          结算单位uuid not null。
   * @param counterpartUuid
   *          对方单位uuid not null。
   * @param isGetAdvance
   *          是否从预存款账户取得合同。
   * @return 预存款账户合同。
   * @throws ClientBizException
   */
  public BAdvanceContract getUniqueContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType, boolean isGetAdvance) throws ClientBizException;

  /**
   * 获取预存款账户。
   * 
   * @param accountUnitUuid
   *          结算单位uuid not null。
   * @param counterpartUuid
   *          对方单位uuid not null。
   * @param subjectUuid
   *          科目uuid not null。
   * @param contractUuid
   *          合同uuid。
   * @return 账户余额。
   * @throws ClientBizException
   */
  public BigDecimal getBalance(String accountUnitUuid, String counterpartUuid, String subjectUuid,
      String contractUuid) throws ClientBizException;

  /**
   * 生效预付款还款单。
   * 
   * @param uuid
   *          标识 not null。
   * @param comment
   *          生效说明。
   * @param oca
   *          版本号。
   * @throws Exception
   */
  public void effect(String uuid, String comment, long oca) throws Exception;
  
  /**
   * 作废预付款还款单。
   * 
   * @param uuid
   *          标识 not null。
   * @param comment
   *          作废说明。
   * @param oca
   *          版本号。
   * @throws Exception
   */
  public void abort(String uuid, String comment, long oca) throws Exception;

  /**
   * 执行BPM任务。
   * 
   * @param operation
   *          任务出口。not null。
   * @param entity
   *          预存款转移单。not null。
   * @param task
   *          BPM任务上下文。
   * @param saveBeforeAction
   *          执行操作前是否保存。
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BDepositMove entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

}
