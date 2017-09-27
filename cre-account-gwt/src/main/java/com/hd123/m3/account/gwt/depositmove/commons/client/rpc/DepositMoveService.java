/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositMoveSerivce.java
 * 模块说明：	
 * 修改历史：
 *  2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.filter.AdvanceContractFilter;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.filter.AdvanceSubjectFilter;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 预存款转移单|同步接口
 * 
 * @author zhuhairui
 * 
 */
@RemoteServiceRelativePath("account/depositMove")
public interface DepositMoveService extends M3ModuleService {
  public static class Locator {
    private static DepositMoveServiceAsync service = null;

    public static DepositMoveServiceAsync getService() {
      if (service == null)
        service = GWT.create(DepositMoveService.class);
      return service;
    }
  }

  /**
   * 获取科目列表。
   * 
   * @param filter
   *          科目查询筛选条件 not null
   * @return 科目列表
   * @throws ClientBizException
   */
  public RPageData<BUCN> querySubject(AdvanceSubjectFilter filter) throws ClientBizException;

  /**
   * 通过代码获取科目。
   * 
   * @param code
   *          科目代码 not null
   * @param direction
   *          收付方向
   * @param accountUnitUuid
   *          项目uuid
   * @param counterpartUuid
   *          对方单位uuid
   * @param contractUuid
   *          合同uuid
   * @param isQueryAdvance
   *          是否从预存款账户中取科目
   * @return 科目
   * @throws ClientBizException
   */
  public BUCN getSubjectByCode(String code, int direction, String accountUnitUuid,
      String counterpartUuid, String contractUuid, boolean isQueryAdvance)
      throws ClientBizException;

  /**
   * 获取合同列表。
   * 
   * @param filter
   *          合同查询筛选条件 not null
   * @return 合同列表
   * @throws ClientBizException
   */
  public RPageData<BAdvanceContract> queryContract(AdvanceContractFilter filter)
      throws ClientBizException;

  /**
   * 通过合同编号获取合同。
   * 
   * @param billNumber
   *          合同编号 not null
   * @param direction
   *          收付方向
   * @param isQueryAdvance
   *          是否从预存款账户中获合同
   * @return 合同
   * @throws ClientBizException
   */
  public BAdvanceContract getContractByBillNumber(String billNumber, int direction,
      String accountUnituuid, boolean isQueryAdvance) throws ClientBizException;
}
