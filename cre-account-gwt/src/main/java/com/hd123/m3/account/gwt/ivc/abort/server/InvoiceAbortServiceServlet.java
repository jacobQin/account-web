/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceAbortServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.ivc.abort.client.EPInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.client.biz.BInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.client.rpc.InvoiceAbortService;
import com.hd123.m3.account.gwt.ivc.abort.server.converter.BizInvoiceAbortConverter;
import com.hd123.m3.account.gwt.ivc.abort.server.converter.InvoiceAbortBizConverter;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.service.ivc.abort.InvoiceAbort;
import com.hd123.m3.account.service.ivc.abort.InvoiceAborts;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.bpm.server.M3BpmService2Servlet;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.server.RPageDataConverter;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 发票作废单|客户端服务。
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class InvoiceAbortServiceServlet extends M3BpmService2Servlet<BInvoiceAbort> implements
    InvoiceAbortService {
  private static final long serialVersionUID = -8223429971295631698L;

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    moduleContext.put(INVOICE_TYPE, CollectionUtil.toString(getInvoiceTypes()));
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    Set<BPermission> permissions = new HashSet<BPermission>();
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getId(),
        Arrays.asList(EPInvoiceAbort.RESOURCE_KEY)));
    return permissions;
  }

  public Map<String, String> getInvoiceTypes() {
    Map<String, String> mapInvoiceTypes = new HashMap<String, String>();
    try {
      for (BInvoiceType type : InvoiceTypeUtils.getAll()) {
        mapInvoiceTypes.put(type.getCode(), type.getName());
      }
      return mapInvoiceTypes;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public BUCN getCurrentEmployee(String id) throws ClientBizException {
    try {

      User user = getUserService().get(id);
      return new BUCN(user.getUuid(), user.getId(), user.getFullName());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  protected String doSave(BInvoiceAbort entity) throws Exception {
    try {
      entity.setSort(InvoiceTypeUtils.getInvoiceTypeByCode(entity.getInvoiceType()).getSort());
      removeEmptyLines(entity);
      return getInvoiceAbortService().save(BizInvoiceAbortConverter.getInstance().convert(entity),
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  /** 去除空明细行 */
  private void removeEmptyLines(BInvoiceAbort entity) {
    for (int i = entity.getAbortLines().size() - 1; i >= 0; i--) {
      if (entity.getAbortLines().get(i).isEmpty()) {
        entity.getAbortLines().remove(i);
      }
    }
  }

  @Override
  protected BInvoiceAbort doGet(String id) throws Exception {
    Assert.assertArgumentNotNull(id, "id");
    try {
      InvoiceAbort bill = getInvoiceAbortService().get(id);
      BInvoiceAbort bBill = InvoiceAbortBizConverter.getInstance().convert(bill);
      return bBill;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected void doChangeBizState(String uuid, long version, String state) throws Exception {
    getInvoiceAbortService().changeBizState(uuid, version, state,
        ConvertHelper.getOperateContext(getSessionUser()));
  }

  @Override
  protected String getServiceBeanId() {
    return com.hd123.m3.account.service.ivc.abort.InvoiceAbortService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected RPageData<BInvoiceAbort> doQuery(FlecsQueryDef definition) throws Exception {
    try {
      
      FlecsQueryDefinition queryDef = buildQueryDefinition(definition,
          InvoiceAbortQueryBuilder.getInstance());
      QueryResult<InvoiceAbort> qr = getInvoiceAbortService().query(queryDef);
      return RPageDataConverter.convert(qr, InvoiceAbortBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected String getObjectCaption() {
    return R.R.billCaption();
  }

  @Override
  public String getObjectName() {
    return InvoiceAborts.OBJECT_NAME;
  }

  private com.hd123.m3.account.service.ivc.abort.InvoiceAbortService getInvoiceAbortService() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.ivc.abort.InvoiceAbortService.class);
  }

  protected UserService getUserService() {
    return M3ServiceFactory.getBean(UserService.DEFAULT_CONTEXT_ID, UserService.class);
  }

  public static interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("发票作废单")
    String billCaption();
  }
}
