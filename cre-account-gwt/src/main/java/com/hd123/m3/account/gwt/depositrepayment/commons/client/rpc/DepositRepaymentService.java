/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositMoveSerivce.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-22 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.filter.AdvanceSubjectFilter;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 预存款还款单|同步接口
 * 
 * @author chenpeisi
 * 
 */
@RemoteServiceRelativePath("account/depositRepayment")
public interface DepositRepaymentService extends M3ModuleService {
  public static class Locator {
    private static DepositRepaymentServiceAsync service = null;

    public static DepositRepaymentServiceAsync getService() {
      if (service == null)
        service = GWT.create(DepositRepaymentService.class);
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
   * @param counterpartUuid
   *          对方单位uuid
   * @param contractUuid
   *          合同uuid
   * @return 科目
   * @throws ClientBizException
   */
  public BUCN getSubjetByCode(String code, int direction, String accountUnitUuid,
      String counterpartUuid, String contractUuid) throws ClientBizException;

}
