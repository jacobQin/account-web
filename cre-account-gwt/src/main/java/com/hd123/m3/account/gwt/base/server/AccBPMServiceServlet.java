/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	AccBPMServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015-2-26 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.base.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.commons.AccConstants;
import com.hd123.m3.account.gwt.base.client.AccSessions;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.server.converter.ContractBizConverter;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.base.client.M3Sessions;
import com.hd123.m3.commons.gwt.bpm.server.M3BpmServiceServlet;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.service.counterpart.Counterparts;
import com.hd123.m3.mdata.service.typetag.TypeTag;
import com.hd123.m3.mdata.service.typetag.TypeTagService;
import com.hd123.m3.mdata.service.typetag.TypeTags;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;

/**
 * 账务单据基础servlet
 * 
 * @author huangjunxian
 * 
 */
public abstract class AccBPMServiceServlet extends M3BpmServiceServlet {
  private static final long serialVersionUID = -6875685913872475842L;

  /** IA 用户 */
  private UserService userService;
  private AccountOptionService optionService;
  private TypeTagService typeTagService;
  private Map<String, String> captionMap;

  /** 是否启用授权组 */
  protected boolean isPermEnabled(String objectName) {
    if (objectName == null) {
      return true;
    }
    return getPermOptionService().isPermEnabled(objectName);
  }

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
      String counterpartType) {
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
    boolean useContractPerm = permEnabled(Counterparts.MODULE_PROPRIETOR.equals(counterpartType)
        ? com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR
        : com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
    if (useContractPerm) {
      // 启用授权组
      List<String> userGroups = getUserGroups(getSessionUser().getId());
      if (userGroups == null || userGroups.isEmpty())
        return null;
      queryDef.addCondition(Basices.CONDITION_PERM_GROUP_ID_IN, userGroups.toArray());
    }
    QueryResult<Contract> result = getContractService().query(queryDef);
    return result.getRecordCount() == 1
        ? ContractBizConverter.getInstance().convert(result.getRecords().get(0)) : null;
  }

  protected ContractService getContractService() {
    try {
      return M3ServiceFactory.getService(ContractService.class);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
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

  /**
   * 获取可用合作伙伴列表
   * 
   * @return 合作方式列表
   * @throws NamingException
   * @throws M3ServiceException
   */
  public List<String> getAllEnabledCoopModes() {
    try {
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.addFlecsCondition(TypeTags.FIELD_CATEGORY, TypeTags.OPERATOR_EQUALS,
          "investment.coopMode");
      queryDef.addFlecsCondition(TypeTags.FIELD_SUBTYPE, TypeTags.OPERATOR_EQUALS, "tenant");
      List<TypeTag> values = getTypeTagService().query(queryDef).getRecords();

      List<String> results = new ArrayList<String>();
      for (TypeTag coopType : values) {
        results.add(coopType.getName());
      }
      return results;
    } catch (Exception e) {
      return new ArrayList<String>();
    }
  }

  protected UserService getUserService() {
    if (userService == null) {
      userService = M3ServiceFactory.getBean(UserService.DEFAULT_CONTEXT_ID, UserService.class);
    }
    return userService;
  }

  protected AccountOptionService getOptionService() {
    if (optionService == null) {
      optionService = M3ServiceFactory.getService(AccountOptionService.class);
    }
    return optionService;
  }

  protected TypeTagService getTypeTagService() {
    return M3ServiceFactory.getService(TypeTagService.DEFAULT_CONTEXT_ID, TypeTagService.class);
  }
}
