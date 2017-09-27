/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayService.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.client.rpc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositTotal;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author chenpeisi
 * 
 */
@RemoteServiceRelativePath("account/payDeposit")
public interface PayDepositService extends M3ModuleService, M3ModuleNaviService {

  public static class Locator {
    private static PayDepositServiceAsync service = null;

    public static PayDepositServiceAsync getService() {
      if (service == null)
        service = GWT.create(PayDepositService.class);
      return service;
    }
  }

  /**
   * 查询预存款单。
   * 
   * @param condition
   * @param visibleColumnNames
   * @param pageSort
   * @return
   * @throws ClientBizException
   */
  public RPageData<BDeposit> query(List<Condition> condition, List<String> visibleColumnNames,
      PageSort pageSort) throws ClientBizException;

  /**
   * 通过uuid获取预付款单。
   * 
   * @param uuid
   * @return
   * @throws ClientBizException
   */
  public BDeposit load(String uuid) throws ClientBizException;

  /**
   * 通过单号获取预付款单。
   * 
   * @param billNumber
   * @return
   * @throws ClientBizException
   */
  public BDeposit loadByNumber(String billNumber) throws ClientBizException;

  /**
   * 删除预付款单。
   * 
   * @param uuid
   * @param oca
   * @throws ClientBizException
   */
  public void remove(String uuid, long oca) throws ClientBizException;

  /**
   * 创建预付款单。
   * 
   * @return
   * @throws ClientBizException
   */
  public BDeposit create() throws ClientBizException;

  /**
   * 根据预存款账户创建预付款单。
   * 
   * @param advanceUuid
   *          预存款账户标识，not null。
   * @return 预付款单。
   * @throws ClientBizException
   */
  public BDeposit createByAdvance(String advanceUuid) throws ClientBizException;

  /**
   * 生效预付款单。
   * 
   * @param uuid
   *          预付款单标识uuid not null。
   * @param version
   *          版本控制 oca not null。
   * @throws ClientBizException
   */
  public void effect(String uuid, long version) throws ClientBizException;
  
  /**
   * 作废预付款单。
   * 
   * @param uuid
   *          预付款单标识uuid not null。
   * @param version
   *          版本控制 oca not null。
   * @throws ClientBizException
   */
  public void abort(String uuid, long version) throws ClientBizException;

  /**
   * 保存预付款单。
   * 
   * @param pay
   *          预存款。
   * @param task
   *          BPM任务上下文。
   * @return 预存款。
   * @throws ClientBizException
   */
  public BDeposit save(BDeposit pay, BProcessContext task) throws ClientBizException;

  /**
   * 获取账户余额。
   * 
   * @param accountUnitUuid
   *          结算单位Uuid not null。
   * @param counterpartUuid
   *          对方单位uuid not null
   * @param subjectUuid
   *          科目uuid not null
   * @param contractUuid
   *          合同uuid
   * @return 账户余额
   * @throws ClientBizException
   */
  public BDepositTotal getAdvance(String accountUnitUuid, String counterpartUuid,
      String subjectUuid, String contractUuid) throws ClientBizException;

  /**
   * 根据商户uuid取其仅有的一份合同。如果商户只有一份有效合同，那么返回该合同，否则均返回空。
   * 
   * @param accountUnitUuid
   * @param counterpartUuid
   * @return
   * @throws ClientBizException
   */
  public BContract getContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException;

  /**
   * 执行BPM任务。
   * 
   * @param operation
   *          任务出口。not null。
   * @param entity
   *          预付款单。not null。
   * @param task
   *          BPM任务上下文。
   * @param saveBeforeAction
   *          在执行操作前是否保存。
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BDeposit entity, BTaskContext task,
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

  /**
   * 批量获取账户余额。
   * 
   * @param accountUnitUuid
   *          结算单位uud not null。
   * @param counterpartUuid
   *          对方单位uuid not null。
   * @param subjectUuids
   *          科目uuid列表 not null。
   * @param contractUuid
   *          合同uuid
   * @return key为subjectuuid，value为对应账户余额。
   * @throws ClientBizException
   */
  public Map<String, BDepositTotal> getAdvances(String accountUnitUuid, String counterpartUuid,
      Collection<String> subjectUuids, String contractUuid) throws ClientBizException;

  /**
   * 从合同导入预存款明细。
   * 
   * @param contractUuid
   *          合同uuid,not null。
   * @return
   * @throws ClientBizException
   */
  public List<BDepositLine> importFromContract(String contractUuid, String accountUnitUuid,
      String counterpartUuid) throws ClientBizException;

}
