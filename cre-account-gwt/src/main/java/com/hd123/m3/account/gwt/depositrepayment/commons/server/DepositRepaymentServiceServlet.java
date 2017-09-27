/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	DepositRepaymentServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-19 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.server;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hd123.m3.account.gwt.base.server.AccRemoteServiceServlet;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.rpc.DepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.filter.AdvanceSubjectFilter;
import com.hd123.m3.account.service.adv.Advance;
import com.hd123.m3.account.service.adv.AdvanceService;
import com.hd123.m3.account.service.adv.Advances;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author zhuhairui
 * 
 */
public class DepositRepaymentServiceServlet extends AccRemoteServiceServlet implements
    DepositRepaymentService {

  private static final long serialVersionUID = -3398654722304769102L;
  private static Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);

  @Override
  public String getObjectName() {
    return DepositRepayment.class.getSimpleName();
  }

  @Override
  public RPageData<BUCN> querySubject(AdvanceSubjectFilter filter) throws ClientBizException {
    try {
      
      QueryDefinition queryDef = new QueryDefinition();
      queryDef.addCondition(Advances.CONDITION_TOTAL_LAGGER, BigDecimal.ZERO);
      
      queryDef
          .addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, filter.getAccountUnitUuid());

      queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, filter.getDirectionType());

      queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, filter.getCounterpartUuid());

      queryDef.addCondition(Advances.CONDITION_BILL_ID_EQUALS,
          filter.getContractUuid() == null ? Advances.NONE_BILL_UUID : filter.getContractUuid());

      if (StringUtil.isNullOrBlank(filter.getCode()) == false)
        queryDef.addCondition(Advances.CONDITION_SUBJECT_CODE_START_WITH, filter.getCode());

      if (StringUtil.isNullOrBlank(filter.getName()) == false)
        queryDef.addCondition(Advances.CONDITION_SUBJECT_NAME_LIKE, filter.getName());

      QueryResult<Advance> qr = getAdvanceService().query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return null;

      List<BUCN> list = new ArrayList<BUCN>();

      List<String> subjectCodes = new ArrayList<String>();
      for (Advance adv : qr.getRecords()) {
        if (subjectCodes.contains(adv.getSubject().getCode()))
          continue;
        else {
          subjectCodes.add(adv.getSubject().getCode());
          list.add(new BUCN(adv.getSubject().getUuid(), adv.getSubject().getCode(), adv
              .getSubject().getName()));
        }
      }
      RPageData<BUCN> result = RPageDataUtil.flip(list, filter);

      if (filter.getOrders().isEmpty() == false)
        result.setValues(sortBUCNList(result.getValues(), filter.getOrders().get(0)));

      return result;

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BUCN getSubjetByCode(String code, int direction, String accountUnitUuid,
      String counterpartUuid, String contractUuid) throws ClientBizException {
    try {
      

      QueryDefinition queryDef = new FlecsQueryDefinition();

      queryDef.addCondition(Advances.CONDITION_TOTAL_LAGGER, BigDecimal.ZERO);
      
      queryDef.addCondition(Advances.CONDITION_DIRECTION_EQUALS, direction);

      queryDef.addCondition(Advances.CONDITION_SUBJECT_CODE_EQUALS, code);

      queryDef.addCondition(Advances.CONDITION_ACCOUNTUNIT_UUID_EQUALS, accountUnitUuid);

      queryDef.addCondition(Advances.CONDITION_COUNTERPAR_UUID_EQUALS, counterpartUuid);

      queryDef.addCondition(Advances.CONDITION_BILL_ID_EQUALS,
          contractUuid == null ? Advances.NONE_BILL_UUID : contractUuid);

      QueryResult<Advance> qr = getAdvanceService().query(queryDef);

      if (qr == null || qr.getRecords().isEmpty())
        return null;

      return UCNBizConverter.getInstance().convert(qr.getRecords().get(0).getSubject());

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /** 内存排序 */
  private List<BUCN> sortBUCNList(List<BUCN> values, Order order) {
    final String sortField = order.getFieldName();
    final OrderDir sortDir = order.getDir();
    if (sortField == null)
      return values;
    List<BUCN> result = new ArrayList<BUCN>(values);
    Collections.sort(result, new Comparator<BUCN>() {
      public int compare(BUCN o1, BUCN o2) {
        Comparable c1, c2;
        if (sortField.equals(AccountCpntsContants.ORDER_BY_FIELD_CODE)) {
          c1 = o1.getCode();
          c2 = o2.getCode();
        } else if (sortField.equals(AccountCpntsContants.ORDER_BY_FIELD_NAME)) {
          c1 = o1.getName();
          c2 = o2.getName();
        } else {
          c1 = null;
          c2 = null;
        }
        int compare = 0;
        if (c1 != null && c2 != null)
          compare = cmp.compare(c1, c2);
        else if (c1 == null)
          compare = -1;
        else
          compare = 1;
        if (sortDir == OrderDir.desc)
          compare *= -1;
        return compare;
      }
    });
    return result;
  }

  private AdvanceService getAdvanceService()  {
    return M3ServiceFactory.getService(AdvanceService.class);
  }
}
