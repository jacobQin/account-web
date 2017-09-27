/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BSubjectConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-23 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectStoreTaxRate;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectStoreTaxRate;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.gwt.base.client.BUCN;

/**
 * @author subinzhu
 * 
 */
public class SubjectBizConverter implements Converter<Subject, BSubject> {
  private static SubjectBizConverter instance;

  public static SubjectBizConverter getInstance() {
    if (instance == null)
      instance = new SubjectBizConverter();
    return instance;
  }

  @Override
  public BSubject convert(Subject source) {
    if (source == null)
      return null;

    BSubject target = new BSubject();
    BUCN subject = new BUCN();
    subject.setUuid(source.getUuid());
    subject.setCode(source.getCode());
    subject.setName(source.getName());
    target.setSubject(subject);
    target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
    target.setDirection(source.getDirection());
    target.setSubjectType(source.getType().toString());
    for (SubjectStoreTaxRate storeTaxRate : source.getStoreTaxRates()) {
      if (storeTaxRate.getStore() == null) {
        continue;
      }
      target.getStoreTaxRates().add(convertStoreTaxRate(storeTaxRate));
    }
    return target;
  }

  private BSubjectStoreTaxRate convertStoreTaxRate(SubjectStoreTaxRate source) {
    if (source == null)
      return null;
    BSubjectStoreTaxRate target = new BSubjectStoreTaxRate();
    target.setRemark(source.getRemark());
    target.setStore(UCNBizConverter.getInstance().convert(source.getStore()));
    target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
    return target;
  }

}
