/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	SubjectPageDataProvider.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui.gadget.dataprovider;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.option.client.rpc.OptionService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author zhuhairui
 * 
 */
public class SubjectPageDataProvider extends PageDataProvider {

  @Override
  protected void queryData(CodeNameFilter tilter, AsyncCallback<RPageData<BUCN>> callback) {
    OptionService.Locator.getService().querySubjects(filter, callback);
  }
}
