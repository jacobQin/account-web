/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： BankServiceImpl.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.m3.account.gwt.bank.client.rpc.BankService;
import com.hd123.m3.account.gwt.bank.intf.client.perm.BankPermDef;
import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.service.bank.Bank;
import com.hd123.m3.account.service.bank.Banks;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author chenrizhang
 * 
 */
public class BankServiceServlet extends AccRemoteServiceServlet implements BankService {
  private static final long serialVersionUID = 300200L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        BankPermDef.RESOURCE_BANK_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
  }

  @Override
  public String getObjectName() {
    return Bank.class.getSimpleName();
  }

  // 业务方法
  @Override
  public List<BBank> getAll() throws ClientBizException {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addFlecsCondition(Banks.FIELD_ENABLED, Basices.OPERATOR_EQUALS, true);
      List<Bank> banks = getBankService().query(def).getRecords();
      List<String> storeUuids = getUserStores(getSessionUser().getId());
      List<Bank> list = new ArrayList<Bank>();
      for (Bank bank : banks) {
        if (storeUuids.contains(bank.getStore().getUuid())) {
          list.add(bank);
        }
      }
      return ConverterUtil.convert(list, BankBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BBank create() throws ClientBizException {
    try {
      isPermitted(BankPermDef.CREATE);
      Bank bank = new Bank();
      return BankBizConverter.getInstance().convert(bank);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BBank load(String uuid) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(uuid))
        return null;

      Bank entity = getBankService().get(uuid);
      BBank reuslt = BankBizConverter.getInstance().convert(entity);
      return reuslt;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BBank loadByCode(String code) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(code))
        return null;

      Bank entity = getBankService().getByCode(code);
      BBank reuslt = BankBizConverter.getInstance().convert(entity);
      return reuslt;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BBank save(BBank entity) throws ClientBizException {
    try {
      isPermitted(StringUtil.isNullOrBlank(entity.getUuid()) ? BankPermDef.CREATE
          : BankPermDef.UPDATE);

      Bank target = null;
      if (StringUtil.isNullOrBlank(entity.getUuid())) {
        target = new Bank();
      } else {
        target = getBankService().get(entity.getUuid());
      }

      write(entity, target);
      target.setEnabled(true);
      String uuid = getBankService()
          .save(target, ConvertHelper.getOperateContext(getSessionUser()));
      return load(uuid);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void delete(String uuid, long version) throws ClientBizException {
    try {
      isPermitted(BankPermDef.DELETE);

      getBankService().remove(uuid, version, ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void write(BBank source, Bank target) {
    target.setVersion(source.getVersion());
    target.setStore(BizUCNConverter.getInstance().convert(source.getStore()));
    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setAccount(source.getAccount());
    target.setAddress(source.getAddress());
    target.setBank(source.getBank());
    target.setRemark(source.getRemark());
  }

  // EJB接口类
  private com.hd123.m3.account.service.bank.BankService getBankService() throws NamingException {
    return M3ServiceFactory.getService(com.hd123.m3.account.service.bank.BankService.class);
  }
}
