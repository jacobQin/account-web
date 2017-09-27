/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： BankService.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.webframe.gwt.base.client.ModuleService;

/**
 * @author chenrizhang
 * 
 */
@RemoteServiceRelativePath("account/bank")
public interface BankService extends ModuleService {

  public static final String PUKEY_UNIT = "account";

  public static class Locator {
    private static BankServiceAsync service = null;

    public static BankServiceAsync getService() {
      if (service == null)
        service = GWT.create(BankService.class);
      return service;
    }
  }

  public List<BBank> getAll() throws ClientBizException;

  public BBank create() throws ClientBizException;

  public BBank load(String uuid) throws ClientBizException;

  public BBank loadByCode(String code) throws ClientBizException;

  public BBank save(BBank entity) throws ClientBizException;

  public void delete(String uuid, long version) throws ClientBizException;
}
