/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	PaymentTypeController.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月11日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.paymenttype.reductionratio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatio;
import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioBill;
import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioBillService;
import com.hd123.m3.account.service.paymenttype.reductionratio.PaymentTypeReductionRatioBills;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author mengyinkun
 * 
 */
@Controller
@RequestMapping("account/ptreductionRatio/*")
public class PaymentTypeReductionRatioBillController extends BizFlowModuleController {
 
  @Autowired
  private PaymentTypeReductionRatioBillService ratioBillService;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        AccountPermResourceDef.RESOURCE_PAYMENTREDUCTIONRATIO));
    return permissions;
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (StringUtil.isNullOrBlank(json))
      throw new M3ServiceException(R.R.entityIsNull());
    BPaymentTypeReductionRatioBill ratioBill = JsonUtil.jsonToObject(json,
        BPaymentTypeReductionRatioBill.class);
    List<PaymentTypeReductionRatio> lines = ratioBill.getDetails();

    for (int i = lines.size() - 1; i >= 0; i--) {
      if (lines.get(i).getPaymentType() == null) {
        lines.remove(lines.get(i));
      }
    }
    ratioBill.setDetails(lines);
    lines = calcRatio(lines);
    PaymentTypeReductionRatioBill entity = PaymentTypeReductionRatioBillConverter.getInstance()
        .convert(ratioBill);
    try {
      return ratioBillService.save(entity, new BeanOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  @RequestMapping("save")
  public @ResponseBody
  PaymentTypeReductionRatioBill save(@RequestBody
  PaymentTypeReductionRatioBill entity, @RequestParam(value = "effect", required = false)
  boolean effect) throws M3ServiceException {
    calcRatio(entity.getLines());
    String uuid = ratioBillService.save(entity, new BeanOperateContext(getSessionUser()));
    if (effect == true) {
      ratioBillService.changeBizState(uuid, -1, BizStates.EFFECT, new BeanOperateContext(
          getSessionUser()));
    }
    PaymentTypeReductionRatioBill target = ratioBillService.get(uuid);
    log(StringUtil.isNullOrBlank(entity.getUuid()) ? AuditActions.R.create()
        : AuditActions.R.modify(), target);
    return target;
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(PaymentTypeReductionRatioBillBuilder.class);
  }

  @Override
  protected String getObjectCaption() {
    return R.R.ObjectCaption();
  }

  @Override
  protected String getServiceBeanId() {
    return PaymentTypeReductionRatioBillService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected String getObjectName() {
    return PaymentTypeReductionRatioBills.OBJECT_NAME_PAY;
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody
  BPaymentTypeReductionRatioBill load(@RequestParam("uuid")
  String uuid) throws Exception {
    PaymentTypeReductionRatioBill entity = ratioBillService.get(uuid);
    if (entity != null && entity.getLines().size() > 0) {
      for (PaymentTypeReductionRatio paymentTypeReductionRatio : entity.getLines()) {
        paymentTypeReductionRatio.setRatio(paymentTypeReductionRatio.getRatio().multiply(
            new BigDecimal(100)));
      }
    }
    validEntityPerm(entity);
    BPaymentTypeReductionRatioBill target = BPaymentTypeReductionRatioBillConverter.getInstance()
        .convert(entity);
    return target;
  }

  private List<PaymentTypeReductionRatio> calcRatio(List<PaymentTypeReductionRatio> list) {
    for (PaymentTypeReductionRatio paymentTypeReductionRatio : list) {
      paymentTypeReductionRatio.setRatio(paymentTypeReductionRatio.getRatio().divide(
          new BigDecimal(100)));
    }
    return list;

  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("付款方式提成比例")
    String ObjectCaption();

    @DefaultStringValue("指定的对象不能为空")
    String entityIsNull();
  }

}
