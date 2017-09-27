/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountSettleService.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.ModuleService;

/**
 * @author huangjunxian
 * 
 */
@RemoteServiceRelativePath("account/contract/accountsettle")
public interface AccountSettleService extends ModuleService {

  /** 账单默认流程Key */
  public static final String KEY_DEFAULTBPMKEY = "_defaultBpmKey";
  
  /** 楼宇类型Key */
  public static final String KEY_BUILDING_TYPE = "buildingType";

  public static class Locator {
    private static AccountSettleServiceAsync service = null;

    public static AccountSettleServiceAsync getService() {
      if (service == null)
        service = GWT.create(AccountSettleService.class);
      return service;
    }
  }

  /**
   * 查询出账记录
   * 
   * @param filter
   *          查询条件
   * @return 符合条件的有效出账记录列表
   * @throws ClientBizException
   */
  RPageData<BAccountSettle> query(AccountSettleFilter filter) throws ClientBizException;

  /**
   * 出账
   * 
   * @param accountSettle
   *          出账记录
   * @param processDefKey
   *          账单流程定义Key
   * @param permGroupId
   *          授权组id，允许为空
   * @param permGroupTitle
   *          授权组标题，允许为空
   * @return 账单单号
   * @throws ClientBizException
   */
  String calculate(String calculateId, List<BAccountSettle> accountSettles, String processDefKey,
      String permGroupId, String permGroupTitle) throws ClientBizException;

  /**
   * 查询流程定义。
   * 
   * @param filter
   *          流程定义的筛选条件。
   * @return 流程定义的分页数据。
   * @throws Exception
   */
  List<BProcessDefinition> queryProcessDefinition() throws ClientBizException;

  /**
   * 根据商户uuid取其仅有的一份合同。如果商户只有一份有效合同，那么返回该合同，否则返回空。
   * 
   * @param counterpartUuid
   * @return
   * @throws Exception
   */
  BContract getOnlyOneContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException;

  /**
   * 获取合作方式
   * 
   * @return
   * @throws ClientBizException
   */
  List<String> getCoopModes() throws ClientBizException;

  /**
   * 获取执行id
   * 
   * @return
   * @throws ClientBizException
   */
  String getCalculateId() throws ClientBizException;
}
