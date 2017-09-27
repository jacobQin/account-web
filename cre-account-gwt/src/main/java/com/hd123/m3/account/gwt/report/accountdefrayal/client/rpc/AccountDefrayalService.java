/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountDefRayalService.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-9 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.biz.BAccountDefrayal;
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
@RemoteServiceRelativePath("account/report/accountdefrayal")
public interface AccountDefrayalService extends M3ModuleService {

  /** 键值：单据类型 */
  public static final String KEY_BILLTYPES = "billTypes";
  /** 键值: 是否启用对账组 */
  public static final String KEY_USEACCGROUP = "useAccGroup";

  public static class Locator {
    private static AccountDefrayalServiceAsync service = null;

    public static AccountDefrayalServiceAsync getService() {
      if (service == null)
        service = GWT.create(AccountDefrayalService.class);
      return service;
    }
  }

  /**
   * 按搜索条件查找科目收付情况。
   * 
   * @param filter
   *          来源单据筛选条件。
   * @param conditions
   *          搜索条件，not null。
   * @param visibleColumnNames
   *          显示的结果列集合。
   * @param pageSort
   *          分页和排序条件。
   * @return 科目收付情况的分页数据。
   * @throws Exception
   */
  public RPageData<BAccountDefrayal> query(List<Condition> conditions,
      List<String> visibleColumnNames, PageSort pageSort) throws Exception;

}
