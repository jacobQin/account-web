/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceRegController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.ivc.reg.InvoiceReg;
import com.hd123.m3.account.service.ivc.reg.InvoiceRegService;
import com.hd123.m3.account.service.ivc.reg.InvoiceRegs;
import com.hd123.m3.account.service.ivc.reg.IvcRegLine;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.common.util.CommonUtil;
import com.hd123.m3.cre.controller.account.invoice.reg.config.InvoiceRegOption;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;

/**
 * 收款发票登记单控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/invoice/reg/*")
public class InvoiceRegController extends BizFlowModuleController {

  /** 默认发票类型 */
  public static final String KEY_DEFALUT_INVOICE_TYPE = "defalutInvoiceType";
  public static final String KEY_BILLTYPES = "billTypes";

  /** 选项：包括是否启用发票库存配置、是否启用账款可以多次开票 */
  public static final String OPTION = "option";
  /** 计算精度,小数位数 */
  public static final String SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String ROUNDING_MODE = "roundingMode";
  /** 是否启用外部发票系统 */
  public static final String ENABLED_EXTINVOICESYSTEM = "isEnabledExtInvoiceSystem";

  @Autowired
  private AccountOptionComponent optionComponent;
  @Autowired
  private AccountOptionService accountOptionService;
  @Autowired
  private AccountService accountService;
  @Autowired
  InvoiceRegService invoiceRegService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    Set<String> resourceIds = new HashSet<String>();
    resourceIds.add(AccountPermResourceDef.RESOURCE_RECINVOICEREG);
    permissions.addAll(
        getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(), resourceIds));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put(KEY_BILLTYPES, JsonUtil.objectToJson(CommonUtil.getBillTypes()));
    moduleContext.put(ENABLED_EXTINVOICESYSTEM, optionComponent.isExtinvSystemEnabled());
    moduleContext.put(SCALE, optionComponent.getScale());
    moduleContext.put(ROUNDING_MODE, optionComponent.getRoundingMode());
    moduleContext.put(OPTION, getOption());
    moduleContext.put(KEY_DEFALUT_INVOICE_TYPE, getDefalutInvoiceType());
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    InvoiceReg entity = JsonUtil.jsonToObject(json, InvoiceReg.class);

    return invoiceRegService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  @RequestMapping("openInvoice")
  @ResponseStatus(HttpStatus.OK)
  public void openInvoice(@RequestBody InvoiceReg entity) throws M3ServiceException {
    Assert.assertArgumentNotNull(entity, "entity");
    try {
      invoiceRegService.print(entity.getUuid(), entity.getVersion(),
          new BeanOperateContext(getSessionUser()));
    } catch (M3ServiceException e) {
      LOGGER.error(R.R.openInvoiceFailed(), e);
      throw new M3ServiceException(e.getMessage());
    }
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(InvoiceRegQueryBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected String getServiceBeanId() {
    return InvoiceRegService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return InvoiceRegs.OBJECT_NAME_RECEIPT;
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody InvoiceReg load(@RequestParam("uuid") String uuid)
      throws M3ServiceException {
    InvoiceReg entity = invoiceRegService.get(uuid);
    if (entity == null) {
      return null;
    }
    decorate(entity);
    return entity;
  }

  /**
   * 更新登记单中的本次应开票金额 需要更新的条件：单据是未生效状态，允许多次开票。
   */
  private void decorate(InvoiceReg entity) {
    if (BizStates.INEFFECT.equals(entity.getBizState()) == false) {
      return;
    }
    if (optionComponent.isAllowSplitReg() == false) {
      return;
    }

    Set<String> accIds = new HashSet<String>();
    List<Pair<String, String>> accPairs = new ArrayList<Pair<String, String>>();
    for (IvcRegLine line : entity.getRegLines()) {
      accIds.add(line.getAcc1().getId());
      accPairs.add(new ImmutablePair<String, String>(line.getAcc1().getId(),
          line.getAcc2().getPayment() == null ? null : line.getAcc2().getPayment().getBillUuid()));
    }

    QueryDefinition queryDef = new QueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACCID_IN, accIds.toArray());
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);
    queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, entity.getUuid());
    queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    queryDef.addCondition(Accounts.CONDITION_INVOICE_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    List<Account> accounts = accountService.query(queryDef).getRecords();
    Map<String, Total> map = new HashMap<String, Total>();
    for (Account account : accounts) {
      String key = account.getAcc1().getId() + (account.getAcc2().getPayment() == null ? null
          : account.getAcc2().getPayment().getBillUuid());// 账款id+收款单作为key
      Total total = map.get(key) == null ? Total.zero() : map.get(key);
      map.put(key, total.add(account.getTotal()));
    }

    for (IvcRegLine line : entity.getRegLines()) {
      String key = line.getAcc1().getId() + (line.getAcc2().getPayment() == null ? null
          : line.getAcc2().getPayment().getBillUuid());// 账款id+收款单作为key
      Total total = map.get(key) == null ? Total.zero() : map.get(key);
      line.setUnregTotal(total);
    }
  }

  private InvoiceRegOption getOption() {
    InvoiceRegOption option = new InvoiceRegOption();

    option.setAllowSplitReg(optionComponent.isAllowSplitReg());
    option.setUseInvoiceStock(optionComponent.isUseInvoiceStock());

    return option;
  }

  private String getDefalutInvoiceType() {
    String defalutInvoiceType = optionComponent.getDefalutInvoiceType() == null ? "common"
        : optionComponent.getDefalutInvoiceType();
    return defalutInvoiceType;
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("收款发票登记单")
    public String moduleCaption();

    @DefaultStringValue("开票失败")
    public String openInvoiceFailed();

  }

}
