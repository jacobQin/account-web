/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	SourceBillDispatchLinkField.java
 * 模块说明：	
 * 修改历史：
 * 2016年1月27日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * 作为可编辑表格列渲染字段
 * 
 * @author LiBin
 *
 */
public class SourceBillDispatchLinkField extends DispatchLinkField implements RValidatable{

  @Override
  public void clearValidResults() {
    //Do Nothing
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public List<Message> getInvalidMessages() {
    return new ArrayList<Message>();
  }

  @Override
  public boolean validate() {
    return true;
  }
  
}