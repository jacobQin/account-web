/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CommonConstants.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.common.client;

/**
 * 提供Common使用的相关常量
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class CommonConstants {
  /** 通用排序字段定义 */
  public static final String FIELD_INVOICE_CODE = "invoiceCode";
  public static final String FIELD_INVOICE_NUMBER = "invoiceNumber";
  public static final String FIELD_INVOICE_STATE = "state";
  public static final String FIELD_INVOICE_STORE = "store";
  public static final String FIELD_INVOICE_TYPE = "invoiceType";

  /** 票据类型：取值参见“SORT_”开始的常量 */
  public static final String FIELD_INVOICE_SORT = "invoiceSort";

  /** 使用方式，取值参见{@link UseType}定义的常量 */
  public static final String FIELD_USETYPE = "useType";

  /** 票据类型取值：发票 */
  public static final String SORT_INVOICE = "invoice";
  /** 票据类型取值：收据 */
  public static final String SORT_RECEIPT = "receipt";

  /** 查询过滤条件(ArrayList<String>) 发票号码不在...中 */
  public static final String FIELD_NUMBERS_NOT_IN = "numbersNotIn";

  public static class UseType {
    public static String NORMAL = "0";
    public static String BALANCED = "1";
    public static String BALANCING = "2";
    public static String EXCHANGED = "3";
    public static String BALANCED_EXCHANGED = "4";
    public static String EXCHANGING = "5";
  }
  
}
