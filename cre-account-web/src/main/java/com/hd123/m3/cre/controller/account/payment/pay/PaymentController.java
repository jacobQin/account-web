/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	PaymentController.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月14日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.payment.pay;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.payment.AbstractPaymentController;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;

/**
 * 付款单控制器
 * 
 * @author LiBin
 *
 */
@Controller
@RequestMapping("account/payment/pay/*")
public class PaymentController extends AbstractPaymentController {
  /** 键值：付款单配置项 */
  private static final String KEY_OPTION = "payOption";

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    Set<String> resourceIds = new HashSet<String>();
    resourceIds.add(AccountPermResourceDef.RESOURCE_PAYMENT);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        resourceIds));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put(KEY_OPTION, getOpion());
  }

  @Override
  public SummaryQueryResult query(@RequestBody QueryFilter queryFilter) {
    if (queryFilter == null) {
      return new SummaryQueryResult();
    }

    queryFilter.getFilter().put(Payments.CONDITION_DIRECTION_EQUALS, Direction.PAYMENT);

    return super.query(queryFilter);

  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected String getObjectName() {
    return Payments.OBJECT_NAME_PAY;
  }

  @Override
  protected int getDirection() {
    return Direction.PAYMENT;
  }

  @Override
  protected PaymentDefrayalType getDefaultPaymentDefrayalType() {
    return PaymentDefrayalType.bill;
  }

  private PaymentOption getOpion() {
    PaymentOption option = new PaymentOption();
    PaymentType paymentType = optionComponent.getDefaltPaymentType();
    if (paymentType != null) {
      option.setPaymentType(new UCN(paymentType.getUuid(), paymentType.getCode(), paymentType
          .getName()));
    }

    Subject subject = optionComponent.getDefaultPreSubject();
    if (subject != null) {
      option.setDepositSubject(new UCN(subject.getUuid(), subject.getCode(), subject.getName()));
    }
    return option;
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("付款单")
    public String moduleCaption();

  }

}
