/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	OperateContextUtil.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-17 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.util.server;

import java.util.Date;

import com.hd123.m3.account.commons.CommentOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.Operator;

/**
 * @author chenrizhang
 * 
 */
public class OperateContextUtil {

  public static CommentOperateContext buildCommentOperateCtx(IsOperator operator, String comment) {
    CommentOperateContext operateCtx = new CommentOperateContext();

    operateCtx.setTime(new Date());
    operateCtx.setOperator(new Operator());
    operateCtx.getOperator().setNamespace(operator.getNamespace());
    operateCtx.getOperator().setId(operator.getId());
    operateCtx.getOperator().setFullName(operator.getFullName());
    operateCtx.setComment(comment);

    return operateCtx;
  }
}
