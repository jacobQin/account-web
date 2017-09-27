/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2010，所有权利保留。
 * 
 * 项目名：	M3
 * 文件名：	BSubjectConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */
package com.hd123.m3.account.gwt.subject.server;

import java.util.ArrayList;

import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.subject.client.biz.BSubject;
import com.hd123.m3.account.gwt.subject.client.biz.BSubjectStoreTaxRate;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectStoreTaxRate;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.server.converter.UCNBizConverter;
import com.hd123.rumba.commons.gwt.entity.server.BStandardEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author cRazy
 * 
 */
public class SubjectBizConverter extends BStandardEntityConverter<Subject, BSubject> {

  private static SubjectBizConverter instance = null;

  public static SubjectBizConverter getInstance() {
    if (instance == null)
      instance = new SubjectBizConverter();
    return instance;
  }

  private SubjectBizConverter() {
    super();
  }

  @Override
  public BSubject convert(Subject source) throws ConversionException {
    if (source == null)
      return null;

    BSubject target = new BSubject();
    inject(source, target);

    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setEnabled(source.isEnabled());
    target.setCustomType(source.getCustomType());
    target
        .setDirection(DirectionType.payment.getDirectionValue() != source.getDirection() ? DirectionType.receipt
            .getDirectionValue() : DirectionType.payment.getDirectionValue());
    target.setType(source.getType() == null ? null : source.getType().name());
    target.setTaxRate(TaxRateBizConverter.getInstance().convert(source.getTaxRate()));
    target.setRemark(source.getRemark());

    target.setUsages(new ArrayList<String>());
    target.getUsages().addAll(source.getUsages());
    for (SubjectStoreTaxRate storeTaxRate : source.getStoreTaxRates()) {
      if(storeTaxRate.getStore()!=null){
        target.getStoreTaxRates().add(convertStoreTaxRate(storeTaxRate));
      }
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