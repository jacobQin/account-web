/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	ProcessDefinitionController.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月16日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.settle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.bpm.service.definition.ProcessDefinition;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.controllers.BaseController;
import com.hd123.rumba.commons.biz.query.QueryResult;

/**
 * @author mengyinkun
 * 
 */
@Controller
@RequestMapping("account/process/*")
public class ProcessDefinitionController extends BaseController {
  @Autowired
  private CommonComponent commonComponent;

  // 账单流程
  @RequestMapping("queryProcess")
  public @ResponseBody QueryResult<ProcessDefinition> queryProcess(@RequestBody QueryFilter filter) {
    return commonComponent.queryProcess(filter);
  }
}
