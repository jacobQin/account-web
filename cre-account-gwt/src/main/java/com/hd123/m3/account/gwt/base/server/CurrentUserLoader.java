/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	CurrentUserLoader.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-11 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.base.server;

import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.gwt.flecs.prefs.server.OperatorLoader;
import com.hd123.rumba.webframe.session.Session;

/**
 * 当前操作人装载器。
 * 
 * @author huangjunxian
 * 
 */
public class CurrentUserLoader implements OperatorLoader {

  @Override
  public IsOperator getCurrentOperator() {
    return Session.getInstance().getCurrentUser();
  }

}
