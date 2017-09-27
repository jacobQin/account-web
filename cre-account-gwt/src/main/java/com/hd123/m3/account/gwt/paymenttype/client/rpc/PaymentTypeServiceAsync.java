/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.paymenttype.client.biz.BPaymentType;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * 付款方式|异步接口
 * 
 * @author zhuhairui
 * 
 */
public interface PaymentTypeServiceAsync extends ModuleServiceAsync {

  public void create(AsyncCallback<BPaymentType> callback);

  public void getAll(AsyncCallback<List<BPaymentType>> callback);

  public void getAllValid(AsyncCallback<List<BPaymentType>> callback) throws Exception;

  public void save(BPaymentType entity, AsyncCallback<BPaymentType> callback);

  public void enable(String uuid, long oca, AsyncCallback<BPaymentType> callback);

  public void disable(String uuid, long oca, AsyncCallback<BPaymentType> callback);

  public void load(String uuid, AsyncCallback<BPaymentType> callback);

  public void loadByCode(String code, AsyncCallback<BPaymentType> callback);

}
