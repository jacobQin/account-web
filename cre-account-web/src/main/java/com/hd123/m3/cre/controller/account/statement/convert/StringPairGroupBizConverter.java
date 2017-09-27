/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-common
 * 文件名：	StringPairGroupBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-7-24 - huangjunxian- 创建。
 */
package com.hd123.m3.cre.controller.account.statement.convert;

import org.apache.commons.lang3.ObjectUtils;

import com.hd123.m3.account.commons.StringPairGroup;
import com.hd123.m3.account.commons.StringPairGroupItem;
import com.hd123.m3.cre.controller.account.statement.model.BStringPairGroup;
import com.hd123.rumba.commons.collection.CollectionUtil;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;
import com.hd123.rumba.commons.util.converter.ConverterUtil;

/**
 * StringPairGroup->BStringPairGroup
 * 
 * @author chenganbang
 * 
 */
public class StringPairGroupBizConverter implements Converter<StringPairGroup, BStringPairGroup> {

  private static StringPairGroupBizConverter instance;

  public static StringPairGroupBizConverter getInstance() {
    if (instance == null)
      instance = new StringPairGroupBizConverter();
    return instance;
  }

  @Override
  public BStringPairGroup convert(StringPairGroup source) throws ConversionException {
    if (source == null)
      return null;

    BStringPairGroup target = new BStringPairGroup();
    if (StringUtil.isNullOrBlank(source.getMainKeys()) == false) {
      target.setMainKeys(CollectionUtil.toList(source.getMainKeys(), ','));
    }
    target.setCaption(source.getCaption());
    target.getItems().addAll(
        ConverterUtil.convert(source.getItems(), StringPairGroupItemBizConverter.getInstance()));
    boolean showDetail = false;
    for (StringPairGroupItem item : source.getItems()) {
      if (ObjectUtils.equals(source.getMainKeys(), item.getKeys()) == false) {
        showDetail = true;
        break;
      }
    }
    target.setShowDetail(showDetail);
    return target;
  }

}
