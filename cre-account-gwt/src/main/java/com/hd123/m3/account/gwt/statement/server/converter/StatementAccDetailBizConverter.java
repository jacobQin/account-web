/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	StatementAccDetailBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2015-3-18 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.server.converter;

import java.util.ArrayList;
import java.util.Map;

import com.hd123.m3.account.commons.StringPairGroup;
import com.hd123.m3.account.gwt.commons.server.StringPairGroupBizConverter;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccDetail;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccFreeDetail;
import com.hd123.m3.account.service.acc.AccountDetail;
import com.hd123.m3.account.service.acc.AccountFreeDetail;
import com.hd123.m3.account.service.acc.AccountSourceType;
import com.hd123.m3.account.service.acc.AccountTermDetail;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.rpc.StringPair;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author huangjunxian
 * 
 */
public class StatementAccDetailBizConverter implements
    Converter<AccountDetail, BStatementAccDetail> {

  private static StatementAccDetailBizConverter instance;

  public static StatementAccDetailBizConverter getInstance() {
    if (instance == null) {
      instance = new StatementAccDetailBizConverter();
    }
    return instance;
  }

  private Map<String, AccountSourceType> typeMap;

  public Map<String, AccountSourceType> getTypeMap() {
    return typeMap;
  }

  public void setTypeMap(Map<String, AccountSourceType> typeMap) {
    this.typeMap = typeMap;
  }

  @Override
  public BStatementAccDetail convert(AccountDetail source) throws ConversionException {
    if (source == null)
      return null;
    BStatementAccDetail target = new BStatementAccDetail();
    target.setCaption(source.getCaption());
    target.setDateRange(new BDateRange(source.getAcc1().getBeginTime(), source.getAcc1()
        .getEndTime()));
    target.setTotal(source.getTotal() == null ? null : source.getTotal().getTotal());
    target.setFreeTotal(source.getFreeTotal() == null ? null : source.getFreeTotal().getTotal());
    if (source.getDetail() != null) {
      for (StringPair pair : source.getDetail().getDetails()) {
        target.getDetails().add(pair.getKey() + "：" + pair.getValue());
      }
      for (StringPairGroup group : source.getDetail().getGroups()) {
        target.getGroups().add(StringPairGroupBizConverter.getInstance().convert(group));
      }
      for (AccountTermDetail child : source.getDetail().getChilds()) {
        BStatementAccDetail detail = new BStatementAccDetail();
        detail.setCaption(child.getCaption());
        detail.setTotal(child.getTotal());
        target.getChilds().add(detail);
      }
    }
    if (source.getFreeDetails() != null) {
      target.setFreeDetails(new ArrayList<BStatementAccFreeDetail>());
      for (AccountFreeDetail freeDetail : source.getFreeDetails()) {
        BStatementAccFreeDetail bt = new BStatementAccFreeDetail();
        bt.setFreeRange(new BDateRange(freeDetail.getFreeRange().getBeginDate(), freeDetail
            .getFreeRange().getEndDate()));
        bt.setRate(freeDetail.getRate());
        bt.setTotal(freeDetail.getTotal().getTotal());
        target.getFreeDetails().add(bt);
      }
    }
    if (typeMap != null) {
      AccountSourceType type = typeMap.get(source.getAcc1().getId());
      target.setAccSrcType(type == null ? null : type.name());
    }
    return target;
  }

}
