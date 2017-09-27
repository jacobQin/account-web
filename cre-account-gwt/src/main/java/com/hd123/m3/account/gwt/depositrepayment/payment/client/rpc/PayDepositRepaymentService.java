/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PayDepositRepaymentService.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 预付款还款单同步接口
 * 
 * @author chenpeisi
 * 
 */
@RemoteServiceRelativePath("account/payDepositRepayment")
public interface PayDepositRepaymentService extends M3ModuleService, M3ModuleNaviService {

  public static final String PUKEY_UNIT = "account";

  /** 键值：打印模板文件 */
  public static final String KEY_BQFILE = "_BQFile";
  /** 是否启用结算组限制 */
  public static final String KEY_ENABLE_SETTLEGROUP = "enableSettleGroup";
  /** 付款方式 */
  public static final String KEY_PAYMENTTYPE = "paymentType";
  /** 银行资料 */
  public static final String KEY_BANK = "bank";

  public static class Locator {
    private static PayDepositRepaymentServiceAsync service = null;

    public static PayDepositRepaymentServiceAsync getService() {
      if (service == null)
        service = GWT.create(PayDepositRepaymentService.class);
      return service;
    }
  }

  /**
   * 通过单号获取预付款还款单。
   * 
   * @param keyword
   *          单号 not null
   * @return 预付款还款单
   * @throws ClientBizException
   */
  public BDepositRepayment loadByNumber(String keyword) throws ClientBizException;

  /**
   * 通过uuid获取预付款还款单。
   * 
   * @param uuid
   *          标识 not null
   * @return 预付款还款单
   * @throws ClientBizException
   */
  public BDepositRepayment load(String uuid) throws ClientBizException;

  /**
   * 取得预付款还款单列表。
   * 
   * @param conditions
   * @param visibleColumnNames
   * @param pageSort
   *          分页排序参数
   * @return 预付款还款单列表
   * @throws ClientBizException
   */
  public RPageData<BDepositRepayment> query(List<Condition> conditions,
      List<String> visibleColumnNames, PageSort pageSort) throws ClientBizException;

  /**
   * 删除预付款还款单。
   * 
   * @param uuid
   *          标识 not null
   * @param version
   *          版本控制 not null
   * @throws ClientBizException
   */
  public void remove(String uuid, long version) throws ClientBizException;

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
   * 创建预付款还款单。
   * 
   * @return 预付款还款单
   * @throws ClientBizException
   */
  public BDepositRepayment create() throws ClientBizException;

  /**
   * 根据预存款账户创建预付款还款单。
   * 
   * @param advanceUuid
   *          预存款账户标识，not null。
   * @return 预存款账户。
   * @throws ClientBizException
   */
  public BDepositRepayment createByAdvance(String advanceUuid) throws ClientBizException;

  /**
   * 保存预付款还款单。
   * 
   * @param entity
   *          预付款还款单。
   * @param task
   *          BPM任务上下文。
   * @return 保存后的预付款还款单。
   * @throws ClientBizException
   */
  public BDepositRepayment save(BDepositRepayment entity, BProcessContext task)
      throws ClientBizException;

  /**
   * 取得对方结算单位唯一的合同，如果存在多张合同返回null。
   * 
   * @param accountUnitUuid
   *          项目uuid
   * @param counterpartUuid
   *          对方单位uuid
   * @return
   * @throws ClientBizException
   */
  public BContract getUniqueAdvance(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException;

  /**
   * 取得对方单位指定科目的预存款账户余额。
   * 
   * @param accountUnitUuid
   *          结算单位uuid not null
   * @param counterpartUuid
   *          对方单位uuid not null
   * @param subjectUuid
   *          科目uuid not null
   * @param contractUuid
   *          合同uuid
   * @return 预存款账户余额
   * @throws ClientBizException
   */
  public BigDecimal getAdvance(String accountUnitUuid, String counterpartUuid, String subjectUuid,
      String contractUuid) throws ClientBizException;

  /**
   * 执行BPM任务。
   * 
   * @param operation
   *          任务出口。not null。
   * @param entity
   *          预存款还款单。not null。
   * @param task
   *          BPM任务上下文。
   * @param saveBeforeAction
   *          执行操作前是否保存。
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BDepositRepayment entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

  /**
   * 获取支付方式。
   * 
   * @return
   * @throws ClientBizException
   */
  public List<BUCN> getPaymentTypes() throws ClientBizException;

  /**
   * 获取银行资料。
   * 
   * @return
   * @throws ClientBizException
   */
  public List<BBank> getBanks() throws ClientBizException;

  /**
   * 取得当前登录员工。
   * 
   * @param id
   *          用户id not null。
   * @return 登录员工
   * @throws ClientBizException
   */
  public BUCN getCurrentEmployee(String id) throws ClientBizException;

}
