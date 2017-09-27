/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceCommonMessages.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author lixiaohong
 *
 */
public interface InvoiceCommonMessages extends CommonsMessages{
  public static InvoiceCommonMessages M = GWT.create(InvoiceCommonMessages.class);

  @DefaultMessage("发票类型获取错误")
  String invoiceTypeError();
  
  @DefaultMessage("发票明细")
  String invoiceDetail();
  
  @DefaultMessage("共计 {0}  条")
  String totalCount(int count);
  
  @DefaultMessage("添加")
  String append();
  
  @DefaultMessage("在上方插入行")
  String insertBefore();
  
  @DefaultMessage("行号")
  String lineNo();
  
  @DefaultMessage("格式错误：只能是大于等于0的数字")
  String regexMessage();
  
  @DefaultMessage("格式错误：只能是字母或数字")
  String regexNumOrStrMessage();
  
  @DefaultMessage("状态与操作")
  String operateInfo();
  
  @DefaultMessage("生成结束发票号长度超过起始发票长度")
  String overQuantitySize();
  
  @DefaultMessage("第{0}行和第{1}行单据号不能相互包含")
  String contained(int i,int j);
  
  @DefaultMessage("不允许空值;")
  String notNull();
  
  @DefaultMessage("不能小于起始发票号码")
  String endNumberLessError();
  
  @DefaultMessage("与起始发票号码长度不一致")
  String notEqualSize();
  
  @DefaultMessage("应该大于0。")
  String needOne();
  
  @DefaultMessage("应该大于等于0。")
  String realNeedZero();
  
  @DefaultMessage("正在{0}...")
  String actionDoing(String action);
  
  @DefaultMessage("加载")
  String load();
  
  @DefaultMessage("当前员工")
  String currentEmployee();
  
  @DefaultMessage("{0}{1}过程中发生错误。")
  String actionFailed(String action, String entityCaption);
  
  @DefaultMessage("未找到{0}{1}")
  String notFindInvoice(String caption,String invoiceNumber);
  
  @DefaultMessage("号码{0}已存在")
  String numberExists(String number);
  
  @DefaultMessage("发票明细将被清空，是否继续？")
  String confirmClear();
  
  @DefaultMessage("起止号码之差不能大于入库张数最大值{0}")
  String canNotGreaterMax(String intMax);
  
  @DefaultMessage("不能超过最大值{0}")
  String maxNotGreater(String intMax);
  
  @DefaultMessage("不能超过应回收张数{0}")
  String maxNotQty(String qty);
  
  @DefaultMessage("商户")
  String tenant();
}
