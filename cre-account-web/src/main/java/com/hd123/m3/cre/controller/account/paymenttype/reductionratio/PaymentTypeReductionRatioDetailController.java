/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	PaymentTypeReductionRatioDetailController.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月15日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype.reductionratio;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatio;
import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioBillService;
import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioBills;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.controllers.module.ModuleController;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryResult;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author mengyinkun
 * 
 */
@Controller
@RequestMapping("account/ptreductionRatioDtl/*")
public class PaymentTypeReductionRatioDetailController extends ModuleController {
  /** 默认项目 */
  private static final String KEY_DEFAULT_STORE = "defaultStore";
  @Autowired
  private PaymentTypeReductionRatioBillService ratioBillService;

  /** 构建模块操作上下文 */
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);

    List<UCN> stores = getUserStoreNames(getSessionUser().getId());
    Collections.sort(stores, new Comparator<UCN>() {

      @Override
      public int compare(UCN o1, UCN o2) {
        return (o1.getCode().compareTo(o2.getCode()));
      }
    });
    if (stores.size() > 0) {
      moduleContext.put(KEY_DEFAULT_STORE, stores.get(0));
    }
  }

  @RequestMapping("query")
  public @ResponseBody
  QueryResult<PaymentTypeReductionRatio> getLines(@RequestBody
  QueryFilter filter) {
    if (filter == null)
      return new QueryResult<PaymentTypeReductionRatio>();
    FlecsQueryDefinition queryDef = getQueryBuilder().build(filter,
        PaymentTypeReductionRatioBills.OBJECT_NAME_PAY, getSessionUserId());
    QueryResult<PaymentTypeReductionRatio> target = ratioBillService
        .queryReductionRatioes(queryDef);
    return target;

  }

  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(PaymentTypeReductionRatioDetailBuilder.class);
  }

}
