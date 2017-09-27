/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	EmailConfig.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月10日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.email.config;

import java.io.Serializable;

/**
 * 与邮件发送有关的变量配置
 * 
 * @author mengyinkun
 * 
 */
public class EmailConfig implements Serializable {
  private static final long serialVersionUID = 1L;
  private String subjectMsg = "账单欠款信息";
  private String msg = "<html><body><a href='www.baidu.com'>百度一下</a>为了加强团队建设，为了弘扬公司文化，特定于晚上6点整在联航路1588号餐厅聚餐！</body></html>";

  public EmailConfig() {
  }

  /**
   * 邮件主题
   * 
   * @return
   */
  public String getSubjectMsg() {
    return subjectMsg;
  }

  public void setSubjectMsg(String subjectMsg) {
    this.subjectMsg = subjectMsg;
  }

  /**
   * 邮件正文内容
   * 
   * @return
   */
  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

}
