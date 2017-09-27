/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeService.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QBatchStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.filter.BatchFilter;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.filter.StatementFilter;
import com.hd123.m3.commons.gwt.base.client.biz.HasPerm;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 同步接口
 * 
 * @author zhuhairui
 * 
 */
@RemoteServiceRelativePath("account/paymentnotice")
public interface PaymentNoticeService extends M3ModuleService, M3ModuleNaviService {

  /** 收付款通知单默认流程Key */
  public static final String KEY_DEFAULTBPMKEY = "_defaultBpmKey";

  /** 持久化单元 */
  public static final String PU_ACCOUNT_PAYMENTNOTICE = "account_paymentnotice";

  public static class Locator {
    private static PaymentNoticeServiceAsync service = null;

    public static PaymentNoticeServiceAsync getService() {
      if (service == null)
        service = GWT.create(PaymentNoticeService.class);
      return service;
    }
  }

  /**
   * 查询符合条件的收付款通知单列表。
   * 
   * @param conditions
   *          查询条件。
   * @param pageSort
   *          分页和排序条件。
   * @return 收付款通知单的分页数据。
   * @throws Exception
   */
  public RPageData<BPaymentNotice> query(List<Condition> conditions, PageSort pageSort)
      throws Exception;

  /**
   * 创建收付款通知单。
   * 
   * @param accountCenterUuid
   *          结算中心uuid，not null。
   * @return 收付款通知单。
   * @throws Exception
   */
  public BPaymentNotice create() throws Exception;

  /**
   * 查询账单。
   * 
   * @param filter
   *          账单筛选条件。
   * @param statementUuids
   *          账单标识集合，过滤掉重复的账单。
   * @return 账单的分页数据。
   * @throws Exception
   */
  public RPageData<QStatement> queryStatement(StatementFilter filter, List<String> statementUuids)
      throws Exception;

  /**
   * 加载收付款通知单。
   * 
   * @param uuid
   *          收付款通知单标识。
   * @return 收付款通知单。
   * @throws Exception
   */
  public BPaymentNotice load(String uuid) throws Exception;

  /**
   * 根据收付款通知单号加载收付款通知单。
   * 
   * @param billNumber
   *          收付款通知单号。not null。
   * @return 收付款通知单。
   * @throws Exception
   */
  public BPaymentNotice loadByNumber(String billNumber) throws Exception;

  /**
   * 删除付款通知单。
   * 
   * @param uuid
   *          付款通知单标识。not null。
   * @param version
   *          付款通知单版本。
   * @throws Exception
   */
  public void delete(String uuid, long version) throws Exception;

  /**
   * 收付款通知单生效
   * 
   * @param uuid
   *          收付款通知单uuid。not null。
   * @param latestComment
   *          审核说明。
   * @param oca
   *          版本号。
   * @throws Exception
   */
  public void effect(String uuid, String latestComment, long oca) throws Exception;

  /**
   * 作废付款通知单。
   * 
   * @param uuid
   *          付款通知单标识。not null。
   * @param version
   *          付款通知单版本。not null。
   * @param comment
   *          作废说明。not null。
   * @throws Exception
   */
  public void abort(String uuid, long version, String comment) throws Exception;

  /**
   * 保存收付款通知单。
   * 
   * @param notice
   *          收付款通知单B对象，not null。
   * @param task
   *          BPM任务上下文。
   * @return 收付款通知单标识。
   * @throws Exception
   */
  public BPaymentNotice save(BPaymentNotice notice, BProcessContext task) throws Exception;

  /**
   * 根据对方单位批量生成收付款通知单
   * 
   * @param accUnitAndCounterpart
   *          结算单位与对方单位
   * @param isSpligBill
   *          是否根据合同拆单
   * @param perm
   *          包含授权
   * @param proDef
   *          流程定义
   * @return 生成的通知单号列表
   * 
   * @throws Exception
   */
  public List<String> generateBill(QBatchStatement accUnitAndCounterpart, boolean isSpligBill,
      HasPerm perm, String defKey, BTaskContext task) throws Exception;

  /**
   * 执行BPM任务。
   * 
   * @param outgoing
   *          操作定义。not null。
   * @param entity
   *          收付款通知单。not null。
   * @param task
   *          BPM任务上下文。
   * @param saveBeforeAction
   *          执行操作前是否保存。
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BPaymentNotice entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

  /**
   * 查询存在未发收付款通知单的账单的商户（按项目分组）
   * 
   * @param filter
   *          查询条件。
   * @return
   * @throws ClientBizException
   */
  public RPageData<QBatchStatement> queryBatchStatement(BatchFilter filter)
      throws ClientBizException;

  /**
   * 查询流程定义。
   * 
   * @param filter
   *          流程定义的筛选条件。
   * @return 流程定义的分页数据。
   * @throws Exception
   */
  public List<BProcessDefinition> queryProcessDefinition() throws Exception;

  /**
   * 获取合作方式
   * 
   * @return 合作方式列表
   * @throws ClientBizException
   */
  List<String> getCoopModes() throws ClientBizException;
}
