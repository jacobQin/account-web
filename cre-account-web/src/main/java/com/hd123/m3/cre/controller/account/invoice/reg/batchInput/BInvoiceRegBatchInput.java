/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	BInvoiceRegBatchInput.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月7日 - renjingzhan - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg.batchInput;

import java.util.List;
import com.hd123.rumba.commons.biz.entity.Entity;

/**
 * @author renjingzhan
 *
 */
public class BInvoiceRegBatchInput extends Entity {

  private static final long serialVersionUID = -4250692877241331840L;

  private List<BIvcRegLine> regLines;

  private String remark;

  public List<BIvcRegLine> getRegLines() {
    return regLines;
  }

  public void setRegLines(List<BIvcRegLine> regLines) {
    this.regLines = regLines;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
