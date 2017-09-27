/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeService.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author liuguilin
 * 
 */
@RemoteServiceRelativePath("account/internalFee")
public interface InternalFeeService extends M3ModuleService, M3ModuleNaviService {

  /** 计算精度,小数位数 */
  public static final String SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String ROUNDING_MODE = "roundingMode";
  
  public static class Locator {
    private static InternalFeeServiceAsync service = null;

    public static InternalFeeServiceAsync getService() {
      if (service == null)
        service = GWT.create(InternalFeeService.class);
      return service;
    }
  }

  /**
   * 查询符合条件的内部费用单列表
   * 
   * @param filter
   *          查询条件
   * @return 返回符合条件的内部费用单列表
   * @throws EXception
   */
  public RPageData<BInternalFee> query(List<Condition> conditions, PageSort pageSort)
      throws Exception;

  /**
   * 根据uuid获取费用单
   * 
   * @param uuid
   *          指定内部费用单的uuid。not null。
   * @return 返回内部费用单
   * @throws Exception
   */
  public BInternalFee load(String uuid) throws Exception;

  /**
   * 根据单号获取内部费用单
   * 
   * @param number
   *          指定内部费用单的单号。not null。
   * @return 返回内部费用单
   * @throws Exception
   */
  public BInternalFee loadByNumber(String number) throws Exception;

  /**
   * 新建内部费用单
   * 
   * @return
   * @throws Exception
   */
  public BInternalFee create() throws Exception;

  /**
   * 保存内部费用单
   * 
   * @param bill
   * @param task
   * @return
   * @throws Exception
   */
  public BInternalFee save(BInternalFee bill, BProcessContext task) throws Exception;

  /**
   * 删除内部费用单
   * 
   * @param uuid
   * @param version
   * @throws Exception
   */
  public void delete(String uuid, long version) throws Exception;

  /**
   * 内部费用单生效
   * 
   * @param uuid
   *          内部 费用单uuid。not null。
   * @param latestComment
   *          审核说明。
   * @param version
   *          版本号。
   * @throws Exception
   */
  public void effect(String uuid, String comment, long version) throws Exception;

  /**
   * 作废内部费用单
   * 
   * @param uuid
   *          内部费用单uuid。not null。
   * @param latestComment
   *          作废说明。
   * @param version
   * @throws Exception
   */
  public void abort(String uuid, String comment, long version) throws Exception;

  /**
   * 执行BPM任务。
   * 
   * @param outgoing
   *          操作定义
   * @param entity
   *          内部费用单
   * @param task
   *          操作任务上下文
   * @param saveBeforeAction
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BInternalFee entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

  /**
   * 查询符合条件的供应商列表
   * 
   * @param filter
   *          查询条件
   * @param using
   *          是否是使用中状态
   * @param freezed
   *          是否是已冻结状态
   * @return 返回符合条件的供应商列表
   * @throws EXception
   */
  public RPageData<BUCN> queryVendors(CodeNameFilter filter, Boolean state, Boolean freezed)
      throws ClientBizException;

  /**
   * 获取指定代码的供应商
   * 
   * @param code
   *          供应商代码
   * @param using
   *          是否是使用中状态
   * @param freezed
   *          是否是已冻结状态
   * @return
   * @throws ClientBizException
   */
  public BUCN getVendorByCode(String code, Boolean state, Boolean freezed)
      throws ClientBizException;
}
