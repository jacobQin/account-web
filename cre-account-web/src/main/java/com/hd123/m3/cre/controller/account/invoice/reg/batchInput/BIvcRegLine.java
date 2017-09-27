/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BIvcRegLine.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月7日 - renjingzhan - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg.batchInput;

import java.util.Date;

import com.hd123.m3.account.service.ivc.reg.IvcRegLine;

/**
 * @author renjingzhan
 *
 */
public class BIvcRegLine extends IvcRegLine {

  private static final long serialVersionUID = -4300835637507632157L;

  private Date regDate;

  public Date getRegDate() {
    return regDate;
  }

  public void setRegDate(Date regDate) {
    this.regDate = regDate;
  }

}
