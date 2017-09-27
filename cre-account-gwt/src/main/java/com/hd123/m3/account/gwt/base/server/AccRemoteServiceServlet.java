/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	AccountRemoteServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-16 - suizhe - 创建。
 */
package com.hd123.m3.account.gwt.base.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.ia.authen.service.organization.OrganizationService;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.ia.author.service.receiptor.ReceiptorGroupService;
import com.hd123.ia.author.service.receiptor.ReceiptorService;
import com.hd123.m3.account.commons.AccConstants;
import com.hd123.m3.account.gwt.base.client.AccSessions;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.server.converter.ContractBizConverter;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.base.client.M3Sessions;
import com.hd123.m3.commons.gwt.base.server.M3RemoteServiceServlet;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.service.counterpart.Counterparts;
import com.hd123.rumba.commons.biz.entity.OperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;

/**
 * 账务基础servlet
 * 
 * @author suizhe
 * 
 */
public abstract class AccRemoteServiceServlet extends M3RemoteServiceServlet {
  private static final long serialVersionUID = -8244753440193173918L;

  /** IA 用户服务 */
  private ReceiptorService receiptorService;
  /** IA 用户组服务 */
  private ReceiptorGroupService receiptorGroupService;
  /** IA 组织服务 */
  private OrganizationService orgService;
  /** IA 用户 */
  private UserService userService;
  private AccountOptionService optionService;

  private Map<String, String> captionMap;

  @Override
  public String getFormName() {
    return AccConstants.FORM_CAPTIONS;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    Map<String, String> map = getOptionService().getCounterpartType();
    if (map != null && !map.isEmpty()) {
      moduleContext.put(AccSessions.KEY_COUNTERPART_TYPE, CollectionUtil.toString(map));
    }
  }

  public BContract getSingleContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(accountUnitUuid) || StringUtil.isNullOrBlank(counterpartUuid)
          || StringUtil.isNullOrBlank(counterpartType))
        return null;
      // 受项目限制
      List<String> stores = getUserStores(getSessionUser().getId());
      if (stores.contains(accountUnitUuid) == false) {
        return null;
      }
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING);
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_EQUALS, accountUnitUuid.trim());
      queryDef.addCondition(Contracts.CONDITION_COUNTERPART_UUID_EQUALS, counterpartUuid.trim());
      queryDef.setPage(0);
      queryDef.setPageSize(1);

      // 对方单位uuid已确定，不需要指定合同类型。
      boolean useContractPerm = permEnabled(Counterparts.MODULE_PROPRIETOR.equals(counterpartType) ? com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR
          : com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      if (useContractPerm) {
        // 启用授权组
        List<String> userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups == null || userGroups.isEmpty())
          return null;
        queryDef.addCondition(Basices.CONDITION_PERM_GROUP_ID_IN, userGroups.toArray());
      }
      QueryResult<Contract> result = getContractService().query(queryDef);
      return result.getRecordCount() == 1 ? ContractBizConverter.getInstance().convert(
          result.getRecords().get(0)) : null;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  protected ContractService getContractService() {
    try {
      return M3ServiceFactory.getService(ContractService.class);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  protected ReceiptorService getReceiptorService() {
    if (receiptorService == null) {
      receiptorService = M3ServiceFactory.getBean(ReceiptorService.DEFAULT_CONTEXT_ID,
          ReceiptorService.class);
    }
    return receiptorService;
  }

  protected ReceiptorGroupService getReceiptorGroupService() {
    if (receiptorGroupService == null)
      receiptorGroupService = M3ServiceFactory.getBean(ReceiptorGroupService.DEFAULT_CONTEXT_ID,
          ReceiptorGroupService.class);
    return receiptorGroupService;
  }

  protected OrganizationService getOrganizationService() {
    if (orgService == null)
      orgService = M3ServiceFactory.getBean(OrganizationService.DEFAULT_CONTEXT_ID,
          OrganizationService.class);
    return orgService;
  }

  protected UserService getUserService() {
    if (userService == null) {
      userService = M3ServiceFactory.getBean(UserService.DEFAULT_CONTEXT_ID, UserService.class);
    }
    return userService;
  }

  public OperateContext getOperateCtx() {
    OperateContext operateCtx = new OperateContext();
    Operator operator = getSessionUser();
    operateCtx.setOperator(operator);
    return operateCtx;
  }

  /**
   * 是否启用授权组
   * 
   * @param objectName
   *          模块授权组配置ID
   * @return
   */
  public boolean permEnabled(String objectName) {
    if (objectName == null)
      return false;
    return getPermOptionService().isPermEnabled(objectName);
  }

  /**
   * 取字段标题
   * 
   * @param field
   *          字段名
   * @param defaultCaption
   *          默认标题
   * @return 标题
   */
  public String getFieldCaption(String field, String defaultCaption) {
    if (field == null)
      return defaultCaption;

    if (captionMap == null) {
      String str = getModuleContext().get(M3Sessions.KEY_FORM_CAPTION);
      if (str == null)
        captionMap = new HashMap<String, String>();
      else
        captionMap = CollectionUtil.toMap(str);
    }
    if (captionMap == null)
      return defaultCaption;

    String caption = captionMap.get(field);
    return caption == null ? defaultCaption : caption;
  }

  protected AccountOptionService getOptionService() {
    if (optionService == null) {
      optionService = M3ServiceFactory.getService(AccountOptionService.class);
    }
    return optionService;
  }
}
