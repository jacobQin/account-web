/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FreezeService.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-3 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.client.ui.filter.AccountFilter;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 同步接口
 * 
 * @author zhuhairui
 * 
 */
@RemoteServiceRelativePath("account/freeze")
public interface FreezeService extends M3ModuleService, M3ModuleNaviService {
  /** 持久化单元 */
  public static final String PU_ACCOUNT_FREEZE = "account_freeze";
  /** 键值：单据类型 */
  public static final String KEY_BILLTYPES = "billTypes";
  /** 键值：打印模板文件 */
  public static final String KEY_BQFILE = "_BQFile";

  public static class Locator {
    private static FreezeServiceAsync service = null;

    public static FreezeServiceAsync getService() {
      if (service == null)
        service = GWT.create(FreezeService.class);
      return service;
    }
  }

  /**
   * 查询符合条件的账款冻结单列表。
   * 
   * @param conditions
   *          查询条件。
   * @param pageSort
   *          分页和排序条件。
   * @return 账款冻结单的分页数据。
   * @throws Exception
   */
  public RPageData<BFreeze> query(List<Condition> conditions, PageSort pageSort) throws Exception;

  /**
   * 加载账款冻结单。
   * 
   * @param uuid
   *          账款冻结单标识，not null。
   * @return 账款冻结单。
   * @throws Exception
   */
  public BFreeze load(String uuid) throws Exception;

  /**
   * 根据账款冻结单单号加载账款冻结单。
   * 
   * @param billNumber
   *          账款冻结单单号，not null。
   * @return 账款冻结单。
   * @throws Exception
   */
  public BFreeze loadByNumber(String billNumber) throws Exception;

  /**
   * 解冻账款冻结单。
   * 
   * @param uuid
   *          账款冻结单标识，not null。
   * @param version
   *          账款冻结单版本，not null。
   * @param unfreezeReason
   *          解冻原因。
   * @throws Exception
   */
  public void unfreeze(String uuid, long version, String unfreezeReason) throws Exception;

  /**
   * 新建账款冻结单。
   * 
   * @param accountUnitUuid
   *          结算单位uuid，not null。
   * @return 账款冻结单。
   * @throws Exception
   */
  public BFreeze create() throws Exception;

  /**
   * 保存账款冻结单，即冻结账款。
   * 
   * @param accountUuids
   *          账款uuid列表，not null。
   * @param freeze
   *          账款冻结单，not null。
   * @param freezeReason
   *          冻结原因，not null。
   * @return 账款冻结单。
   * @throws Exception
   */
  public BFreeze freeze(List<String> accountUuids, BFreeze freeze, String freezeReason)
      throws Exception;

  /**
   * 查询账款。
   * 
   * @param filter
   *          账款过滤器。
   * @param accUuids
   *          账款的标识列表。
   * @return 账款的分页数据。
   * @throws Exception
   */
  public RPageData<BAccount> queryAccount(AccountFilter filter, List<String> accUuids)
      throws Exception;

}
