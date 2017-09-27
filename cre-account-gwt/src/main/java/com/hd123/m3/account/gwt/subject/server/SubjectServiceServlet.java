/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： SubjectServiceImpl.java
 * 模块说明：
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */
package com.hd123.m3.account.gwt.subject.server;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.subject.client.biz.BSubject;
import com.hd123.m3.account.gwt.subject.client.biz.BSubjectStoreTaxRate;
import com.hd123.m3.account.gwt.subject.client.rpc.SubjectService;
import com.hd123.m3.account.gwt.subject.intf.client.perm.SubjectPermDef;
import com.hd123.m3.account.gwt.util.server.SubjectUsageUtils;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectStoreTaxRate;
import com.hd123.m3.account.service.subject.SubjectType;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author cRazy
 * 
 */
public class SubjectServiceServlet extends AccRemoteServiceServlet implements SubjectService {
  private static final long serialVersionUID = 300200L;
  private static SubjectQueryBuilder queryBuilder = SubjectQueryBuilder.getInstance();

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        SubjectPermDef.RESOURCE_SUBJECT_SET);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
  }

  @Override
  public String getObjectName() {
    return Subject.class.getSimpleName();
  }

  // 业务方法
  @Override
  public RPageData<BSubject> query(List<Condition> conditions, PageSort pageSort)
      throws ClientBizException {
    try {

      FlecsQueryDefinition def = new FlecsQueryDefinition();

      def.setFlecsConditions(queryBuilder.buildFlecsConditions(conditions));
      def.setOrders(queryBuilder.buildQueryOrders(pageSort.getSortOrders()));
      def.setPage(pageSort.getPage());
      def.setPageSize(pageSort.getPageSize());

      QueryResult<Subject> queryResult = getSubjectService().query(def, Subject.PART_WHOLE);

      RPageData<BSubject> result = new RPageData<BSubject>();
      RPageDataUtil.copyPagingInfo(queryResult, result);
      result.setValues(ConverterUtil.convert(queryResult.getRecords(),
          SubjectBizConverter.getInstance()));
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BSubject create() throws ClientBizException {
    try {
      isPermitted(SubjectPermDef.CREATE);

      Subject entity = new Subject();
      entity.setEnabled(true);
      return SubjectBizConverter.getInstance().convert(entity);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BSubject load(String uuid) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(uuid))
        return null;

      Subject entity = getSubjectService().get(uuid, Subject.PART_WHOLE);
      BSubject reuslt = SubjectBizConverter.getInstance().convert(entity);
      return reuslt;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BSubject loadByCode(String code) throws ClientBizException {
    try {
      if (StringUtil.isNullOrBlank(code))
        return null;

      Subject entity = getSubjectService().getByCode(code, Subject.PART_WHOLE);
      BSubject reuslt = SubjectBizConverter.getInstance().convert(entity);
      return reuslt;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BSubject save(BSubject entity) throws ClientBizException {
    try {

      isPermitted(StringUtil.isNullOrBlank(entity.getUuid()) ? SubjectPermDef.CREATE
          : SubjectPermDef.UPDATE);

      Subject target = null;
      if (StringUtil.isNullOrBlank(entity.getUuid())) {
        target = new Subject();
      } else {
        target = getSubjectService().get(entity.getUuid());
      }

      write(entity, target);
      String uuid = getSubjectService().save(target,
          ConvertHelper.getOperateContext(getSessionUser()));
      return load(uuid);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void enable(String uuid, long version, boolean enable) throws ClientBizException {
    try {

      if (enable) {
        isPermitted(SubjectPermDef.ENABLE);
        getSubjectService()
            .enable(uuid, version, ConvertHelper.getOperateContext(getSessionUser()));
      } else {
        isPermitted(SubjectPermDef.DISABLE);
        getSubjectService().disable(uuid, version,
            ConvertHelper.getOperateContext(getSessionUser()));
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private void write(BSubject source, Subject target) {
    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setVersion(source.getVersion());
    target.setCustomType(source.getCustomType());
    target.setEnabled(source.isEnabled());
    target.setDirection(source.getDirection());
    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));
    target.setUsages(source.getUsages());
    for (BSubjectStoreTaxRate storeTaxRate : source.getStoreTaxRates()) {
      if (storeTaxRate.getStore() == null) {
        continue;
      }
      target.getStoreTaxRates().add(convertStoreTaxRate(storeTaxRate));
    }
    target.setType(source.getType() == null ? null : SubjectType.valueOf(source.getType()));
    target.setRemark(source.getRemark());
  }

  private SubjectStoreTaxRate convertStoreTaxRate(BSubjectStoreTaxRate source) {
    if (source == null)
      return null;
    SubjectStoreTaxRate target = new SubjectStoreTaxRate();
    target.setRemark(source.getRemark());
    target.setStore(BizUCNConverter.getInstance().convert(source.getStore()));
    target.setTaxRate(BizTaxRateConverter.getInstance().convert(source.getTaxRate()));
    return target;
  }

  // EJB接口类
  private com.hd123.m3.account.service.subject.SubjectService getSubjectService()
      throws NamingException {
    return M3ServiceFactory.getService(com.hd123.m3.account.service.subject.SubjectService.class);
  }

  @Override
  public List<BSubjectUsage> getUsages() throws ClientBizException {
    try {
      return SubjectUsageUtils.getAll();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }
}
