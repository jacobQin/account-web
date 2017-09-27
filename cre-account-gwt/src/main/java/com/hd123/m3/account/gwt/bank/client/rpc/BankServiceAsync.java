/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： BankServiceAsync.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * @author chenrizhang
 * 
 */
public interface BankServiceAsync extends ModuleServiceAsync {

  void getAll(AsyncCallback<List<BBank>> callback);

  void create(AsyncCallback<BBank> callback);

  void load(String uuid, AsyncCallback<BBank> asyncCallback);

  void loadByCode(String code, AsyncCallback<BBank> asyncCallback);

  void save(BBank entity, AsyncCallback<BBank> callback);

  public void delete(String uuid, long version, AsyncCallback<Void> callback);
}
