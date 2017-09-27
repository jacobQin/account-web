/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.paymenttype.client.biz.BPaymentType;
import com.hd123.m3.account.gwt.paymenttype.client.rpc.PaymentTypeService;
import com.hd123.m3.account.gwt.paymenttype.intf.client.perm.PaymentTypePermDef;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypes;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author zhuhairui
 * 
 */
public class PaymentTypeServiceServlet extends AccRemoteServiceServlet implements
    PaymentTypeService {

  private static final long serialVersionUID = 2364733830022560105L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    if (isHost()) {
      Set<BPermission> permissions = new HashSet<BPermission>();
      permissions.add(PaymentTypePermDef.READ);
      permissions.add(PaymentTypePermDef.CREATE);
      permissions.add(PaymentTypePermDef.UPDATE);
      permissions.add(PaymentTypePermDef.ENABLE);
      permissions.add(PaymentTypePermDef.DISABLE);
      return permissions;
    }
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        PaymentTypePermDef.RESOURCE_PAYMENTTYPE_SET);
    return permissions;
  }

  @Override
  public String getObjectName() {
    return PaymentType.class.getSimpleName();
  }

  @Override
  public BPaymentType create() throws Exception {
    try {
      BPaymentType entity = new BPaymentType();
      entity.setEnabled(Boolean.TRUE);
      return entity;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BPaymentType> getAll() throws Exception {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      List<PaymentType> list = getService().query(def).getRecords();
       
      if (list.isEmpty())
        return new ArrayList<BPaymentType>();
      return ConverterUtil.convert(list, PaymentTypeBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public List<BPaymentType> getAllValid() throws Exception {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addFlecsCondition(PaymentTypes.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
      List<PaymentType> list = getService().query(def).getRecords();
       
      if (list.isEmpty())
        return new ArrayList<BPaymentType>();
      return ConverterUtil.convert(list, PaymentTypeBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPaymentType save(BPaymentType entity) throws Exception {
    try {

      PaymentType paymentType = null;
      if (entity.getUuid() == null) {
        paymentType = new PaymentType();
      } else {
        paymentType = getService().get(entity.getUuid());
        if (paymentType == null)
          throw new M3ServiceException("找不到指定的{0}，可能已被删除", "付款方式");
      }
      write(entity, paymentType);

      String uuid = getService().save(paymentType,
          ConvertHelper.getOperateContext(getSessionUser()));

      return load(uuid);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPaymentType enable(String uuid, long oca) throws Exception {
    try {

      getService().enable(uuid, oca, ConvertHelper.getOperateContext(getSessionUser()));
      return load(uuid);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPaymentType disable(String uuid, long oca) throws Exception {
    try {

      getService().disable(uuid, oca, ConvertHelper.getOperateContext(getSessionUser()));
      return load(uuid);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPaymentType load(String uuid) throws Exception {
    try {

      PaymentType paymentType = getService().get(uuid);
      if (paymentType == null)
        return null;
      return PaymentTypeBizConverter.getInstance().convert(paymentType);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BPaymentType loadByCode(String code) throws Exception {
    try {

      PaymentType paymentType = getService().getByCode(code);
      if (paymentType == null)
        return null;
      return PaymentTypeBizConverter.getInstance().convert(paymentType);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void write(BPaymentType source, PaymentType target) {
    assert source != null;
    assert target != null;

    target.inject(source);
    target.setVersion(source.getVersion());
    target.setUuid(source.getUuid());
    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setEnabled(source.isEnabled());
    target.setRemark(source.getRemark());
  }

  private com.hd123.m3.account.service.paymenttype.PaymentTypeService getService()
      throws NamingException {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.paymenttype.PaymentTypeService.class);
  }

}
