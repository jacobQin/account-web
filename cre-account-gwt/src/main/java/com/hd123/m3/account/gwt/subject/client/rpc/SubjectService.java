/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： SubjectService.java
 * 模块说明：
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */
package com.hd123.m3.account.gwt.subject.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.subject.client.biz.BSubject;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.ModuleService;

/**
 * @author cRazy
 * 
 */
@RemoteServiceRelativePath("account/subject")
public interface SubjectService extends ModuleService {

  public static final String KEY_USAGES = "_key_usages";

  public static class Locator {
    private static SubjectServiceAsync service = null;

    public static SubjectServiceAsync getService() {
      if (service == null)
        service = GWT.create(SubjectService.class);
      return service;
    }
  }

  // 业务方法
  public RPageData<BSubject> query(List<Condition> conditions, PageSort pageSort)
      throws ClientBizException;

  public BSubject create() throws ClientBizException;

  public BSubject load(String uuid) throws ClientBizException;

  public BSubject loadByCode(String code) throws ClientBizException;

  public BSubject save(BSubject entity) throws ClientBizException;

  public void enable(String uuid, long version, boolean enable) throws ClientBizException;

  public List<BSubjectUsage> getUsages() throws ClientBizException;
}
