/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： SubjectServiceAsync.java
 * 模块说明：
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */
package com.hd123.m3.account.gwt.subject.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.subject.client.biz.BSubject;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * @author cRazy
 * 
 */
public interface SubjectServiceAsync extends ModuleServiceAsync {

  // 业务方法
  void query(List<Condition> conditions, PageSort pageSort,
      AsyncCallback<RPageData<BSubject>> callback);

  void create(AsyncCallback<BSubject> callback);

  void load(String uuid, AsyncCallback<BSubject> callback);

  void loadByCode(String code, AsyncCallback<BSubject> callback);

  void save(BSubject entity, AsyncCallback<BSubject> callback);

  public void enable(String uuid, long version, boolean enable, AsyncCallback<Void> callback);

  public void getUsages(AsyncCallback<List<BSubjectUsage>> callback);
}
