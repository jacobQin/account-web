/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeLineFilter.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.fee.client.biz.BAccSettle;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeTemplate;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.m3.commons.gwt.widget.client.biz.BImpParams;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.quartz.gwt.client.rpc.BJobScheduleHandler;

/**
 * @author subinzhu
 * 
 */
@RemoteServiceRelativePath("account/fee")
public interface FeeService extends M3ModuleService, M3ModuleNaviService {

  /** 文件导出路径 */
  public static final String EXPORTDIR = "exportDir";
  /** 文件后缀名 */
  public static final String EXPSUFFIX = "fee_suffix";
  /** 下载模板文件位置 */
  public static final String DOWANLOAD_URL = "download_url";
  /** 计算精度,小数位数 */
  public static final String SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String ROUNDING_MODE = "roundingMode";
  /** 合作方式 */
  public static final String KEY_COOPMODES = "_value_CoopModes";

  public static class Locator {
    private static FeeServiceAsync service = null;

    public static FeeServiceAsync getService() {
      if (service == null)
        service = GWT.create(FeeService.class);
      return service;
    }
  }

  /**
   * 查询符合条件的费用单列表
   * 
   * @param filter
   *          查询条件
   * @return 返回符合条件的费用单列表
   * @throws EXception
   */
  public RPageData<BFee> query(List<Condition> conditions, PageSort pageSort) throws Exception;

  /**
   * 根据uuid获取费用单
   * 
   * @param uuid
   *          指定费用单的uuid。not null。
   * @return 返回费用单
   * @throws Exception
   */
  public BFee load(String uuid) throws Exception;

  /**
   * 根据单号获取费用单
   * 
   * @param number
   *          指定费用单的单号。not null。
   * @return 返回费用单
   * @throws Exception
   */
  public BFee loadByNumber(String number) throws Exception;

  /**
   * 新建费用单
   * 
   * @return
   * @throws Exception
   */
  public BFee create() throws Exception;

  /**
   * 保存费用单
   * 
   * @param bill
   * @param task
   * @return
   * @throws Exception
   */
  public BFee save(BFee bill, BProcessContext task) throws Exception;

  /**
   * 删除费用单
   * 
   * @param uuid
   * @param oca
   * @throws Exception
   */
  public void delete(String uuid, long oca) throws Exception;

  /**
   * 费用单生效
   * 
   * @param uuid
   *          费用单uuid。not null。
   * @param latestComment
   *          审核说明。
   * @param oca
   *          版本号。
   * @throws Exception
   */
  public void effect(String uuid, String comment, long oca) throws Exception;

  /**
   * 作废费用单
   * 
   * @param uuid
   *          费用单uuid。not null。
   * @param latestComment
   *          作废说明。
   * @param oca
   * @throws Exception
   */
  public void abort(String uuid, String comment, long oca) throws Exception;

  /**
   * 根据商户uuid取其仅有的一份合同。如果商户只有一份有效合同，那么返回该合同，否则返回空。
   * 
   * @param counterpartUuid
   * @return
   * @throws Exception
   */
  public BContract getOnlyOneContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws Exception;

  /**
   * 执行BPM任务。
   * 
   * @param outgoing
   *          操作定义
   * @param entity
   *          费用单
   * @param task
   *          操作任务上下文
   * @param saveBeforeAction
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BFee entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

  /**
   * 获取合同结算周期
   * 
   * @param contractUuid
   *          合同uuid
   * @return
   * @throws ClientBizException
   */
  public BAccSettle getAccSettle(String contractUuid) throws ClientBizException;

  /**
   * 导出费用单
   * 
   * @param template
   *          导出模板
   * @throws ClientBizException
   */
  public String exportFile(BFeeTemplate template) throws ClientBizException;

  /**
   * 导入费用单
   * 
   * @param impParams
   *          导入参数
   * @param permGroupId
   *          授权组id
   * @param permGroupTitle
   *          授权组名称
   * @throws ClientBizException
   */
  public BJobScheduleHandler importFile(BImpParams impParams, String permGroupId,
      String permGroupTitle) throws ClientBizException;
}
