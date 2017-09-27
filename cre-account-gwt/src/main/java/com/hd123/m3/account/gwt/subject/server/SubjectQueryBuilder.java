/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名：M3
 * 文件名： SubjectViewPage.java
 * 模块说明：
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */
package com.hd123.m3.account.gwt.subject.server;

import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.commons.gwt.util.server.FlecsConditionDecoderImpl;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author cRazy
 * 
 */
public class SubjectQueryBuilder extends FlecsConditionDecoderImpl {
  private static SubjectQueryBuilder instance = null;

  public static SubjectQueryBuilder getInstance() {
    if (instance == null)
      instance = new SubjectQueryBuilder();
    return instance;
  }

  private SubjectQueryBuilder() {
    super();
  }

  @Override
  protected String getDefaultOrderField() {
    return Subjects.FIELD_CODE;
  }

  @Override
  protected String convertQueryField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (SubjectUrlParams.Search.FIELD_CODE.equals(fieldName.trim())) {
      return Subjects.FIELD_CODE;
    } else if (SubjectUrlParams.Search.FIELD_NAME.equals(fieldName.trim())) {
      return Subjects.FIELD_NAME;
    } else if (SubjectUrlParams.Search.FIELD_STATE.equals(fieldName.trim())) {
      return Subjects.FIELD_STATE;
    } else if (SubjectUrlParams.Search.FIELD_USAGE.equals(fieldName.trim())) {
      return Subjects.FIELD_USAGE;
    } else if (SubjectUrlParams.Search.FIELD_DIRECTION.equals(fieldName.trim())) {
      return Subjects.FIELD_DIRECTION;
    } else if (SubjectUrlParams.Search.FIELD_CUSTOMTYPE.equals(fieldName.trim())) {
      return Subjects.FIELD_CUSTOMTYPE;
    } else if (SubjectUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return Subjects.FIELD_CREATOR_CODE;
    } else if (SubjectUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return Subjects.FIELD_CREATOR_NAME;
    } else if (SubjectUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return Subjects.FIELD_CREATED;
    } else if (SubjectUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim())) {
      return Subjects.FIELD_LAST_MODIFIER_CODE;
    } else if (SubjectUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName.trim())) {
      return Subjects.FIELD_LAST_MODIFIER_NAME;
    } else if (SubjectUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return Subjects.FIELD_LAST_MODIFY_TIME;
    }
    return null;
  }

  @Override
  protected String convertOrderField(String fieldName) {
    if (StringUtil.isNullOrBlank(fieldName))
      return null;

    if (SubjectUrlParams.Search.FIELD_CODE.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_CODE;
    } else if (SubjectUrlParams.Search.FIELD_NAME.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_NAME;
    } else if (SubjectUrlParams.Search.FIELD_STATE.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_STATE;
    } else if (SubjectUrlParams.Search.FIELD_DIRECTION.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_DIRECTION;
    } else if (SubjectUrlParams.Search.FIELD_CUSTOMTYPE.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_CUSTOMTYPE;
    } else if (SubjectUrlParams.Search.FIELD_CREATE_INFO_OPER_ID.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_CREATOR_CODE;
    } else if (SubjectUrlParams.Search.FIELD_CREATE_INFO_OPER_NAME.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_CREATOR_NAME;
    } else if (SubjectUrlParams.Search.FIELD_CREATE_INFO_TIME.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_CREATE_TIME;
    } else if (SubjectUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_ID.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_LAST_MODIFIER_CODE;
    } else if (SubjectUrlParams.Search.FIELD_LASTMODIFY_INFO_OPER_NAME.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_LAST_MODIFIER_NAME;
    } else if (SubjectUrlParams.Search.FIELD_LASTMODIFY_INFO_TIME.equals(fieldName.trim())) {
      return Subjects.ORDER_BY_LAST_MODIFY_TIME;
    }
    return null;
  }

}