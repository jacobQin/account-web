/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	BaseWidgetConstants.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月11日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.base.client;

/**
 * 提供Widget使用的相关常量
 * @author chenganbang
 */
public class BaseWidgetConstants {
  /** 通用排序字段定义 */
  public static final String ORDER_BY_FIELD_CODE = "code";
  public static final String ORDER_BY_FIELD_NAME = "name";

  /** 资料部件 */
  public static final String KEY_FETCH_PARTS = "fetchParts";
  /** 过滤条件参数 */
  public static final String KEY_FILTER_STATE = "state";
  /** 过滤条件参数: uuid在...中 */
  public static final String KEY_FILTER_UUID_IN = "uuid in";

  /** 过滤条件参数: 上级在...中 ，主要用于类别 */
  public static final String KEY_FILTER_PARENT_IN = "parent in";
  /** 过滤条件参数: 级别是... ，主要用于类别 */
  public static final String KEY_FILTER_LEVLES = "level =";

  /** 查询条件：category */
  public static final String KEY_FILTER_CATEGORY = "category";

  /** 查询条件 合同编号 */
  public static final String KEY_FILTER_SERIALNUMBER_STARTS_WITH = "serialNumber";
  /** 查询条件 签约编号 */
  public static final String KEY_FILTER_SIGNNUMBER_STARTS_WITH = "signNumber";
  /** 查询条件 商户等于 */
  public static final String KEY_FILTER_TENANT_EQUALS = "tenant";
  /** 查询条件 项目(uuid)等于 */
  public static final String KEY_FILTER_STORE_EQUALS = "store";
  /** 查询条件 店招类似于 */
  public static final String KEY_FILTER_SIGNBOARD_LIKE = "signboard";
  /** 查询条件 状态等于 */
  public static final String KEY_FILTER_STATE_EQUALS = "state";
  /** 查询条件 合作方式等于 */
  public static final String KEY_FILTER_COOPMODE_EQUALS = "coopMode";
  /** 查询条件 核算楼层等于 */
  public static final String KEY_FILTER_FLOOR_EQUALS = "floor";
  /** 查询条件 科目类型等于 */
  public static final String KEY_FILTER_SUBJECT_TYPE = "subjectType";
  /** 查询条件 科目用途等于 */
  public static final String KEY_FILTER_SUBJECT_USAGES = "usages";
  /** POS机模式 */
  public static final String CONDITION_POSMODE_EQUALS = "posMode =";
  /** 查询条件(String)：合同类型等于。 */
  public static final String CONDITION_CONTRACT_TYPE_EQUALS = "contractType =";

  public static final String KEY_FIELD_BEGINTIME_MAX = "beginTime_max";
  public static final String KEY_FIELD_BEGINTIME_MIN = "beginTime_min";
  public static final String KEY_FIELD_ENDTIME_MAX = "endTime_max";
  public static final String KEY_FIELD_ENDTIME_MIN = "endTime_min";

  /** 参数值：使用中 */
  public static final String VALUE_USING = "using";
  /** 参数值：已删除 */
  public static final String VALUE_DELETED = "deleted";

  /** 参数值：启用 */
  public static final String VALUE_ENABLED = "enabled";

  /** 收银机查询时用来限制是否限制是否在收银机组内 */
  public static final String IS_IN_POSGROUP = "isPosGroup";
  /** 查询条件 收银机组(uuid)等于 */
  public static final String KEY_FILTER_POSGROUP_EQUALS = "posGroup";
}
