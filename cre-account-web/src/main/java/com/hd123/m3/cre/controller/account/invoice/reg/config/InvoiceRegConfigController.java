/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceRegConfigController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月16日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.ivc.reg.InvoiceRegs;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.commons.servlet.controllers.BaseController;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * 收款发票登记单设置控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/invoice/reg/config/*")
public class InvoiceRegConfigController extends BaseController{
  
  @Autowired
  AccountOptionService optionService;

  @RequestMapping(value = "saveOption", method = RequestMethod.POST)
  @ResponseBody
  public void saveOption(@RequestBody InvoiceRegOption option) throws ClientBizException {
    try {
      optionService.setOption(InvoiceRegs.OBJECT_NAME_RECEIPT, InvoiceRegs.OPTION_REC_ALLOW_SPLIT_REG,
          String.valueOf(option.isAllowSplitReg()), new BeanOperateContext(getSessionUser()));
      optionService.setOption(InvoiceRegs.OBJECT_NAME_RECEIPT, InvoiceRegs.OPTION_REC_USEINVOICESTOCK,
          String.valueOf(option.isUseInvoiceStock()), new BeanOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }
}
