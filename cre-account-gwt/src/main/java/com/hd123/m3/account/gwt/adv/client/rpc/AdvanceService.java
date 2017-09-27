/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceService.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceData;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceLog;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceFilter;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceLogFilter;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.ModuleService;

/**
 * 同步接口
 * 
 * @author zhuhairui
 * 
 */
@RemoteServiceRelativePath("account/advance")
public interface AdvanceService extends ModuleService {

  /** 持久化单元 */
  public static final String PU_ACCOUNT_FEE = "account_advance";
  /** 键值：单据类型 */
  public static final String KEY_BILLTYPES = "billTypes";
  /** 键值: 是否启用结算组 */
  public static final String KEY_USESETTLEGROUP = "useSettleGroup";

  public static class Locator {
    private static AdvanceServiceAsync service = null;

    public static AdvanceServiceAsync getService() {
      if (service == null)
        service = GWT.create(AdvanceService.class);
      return service;
    }
  }

  /**
   * 根据商户查找预存款信息。
   * 
   * @param counterpartUnitUuid
   *          商户标识。not null。
   * @return 预存款信息。
   * @throws Exception
   */
  public BAdvanceData getAdvanceDataByCounterpartUnitUuid(String counterpartUnitUuid)
      throws Exception;

  /**
   * 查找存在预存款余额的商户（搜索页面列表）。
   * 
   * @param filter
   *          筛选条件。not null。
   * @return 商户分页数据。
   * @throws Exception
   */
  public RPageData<BCounterpart> queryCounterpartUnit(AdvanceFilter filter) throws Exception;

  /**
   * 查询预存款日志。
   * 
   * @param filter
   *          预存款日志筛选条件。not null。
   * @return 预存款日志分页数据。
   * @throws Exception
   */
  public RPageData<BAdvanceLog> queryLog(AdvanceLogFilter filter) throws Exception;

  /**
   * 根据项目查找预存款信息。
   * 
   * @param accountUnitUuid
   *          项目标识。not null。
   * @return 预存款信息。
   * @throws Exception
   */
  public BAdvanceData getAdvanceDataByAccountUnitUuid(String accountUnitUuid) throws Exception;

}
