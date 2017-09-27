/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-common
 * 文件名：	StringPairGroupItemBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-7-24 - huangjunxian- 创建。
 */
package com.hd123.m3.cre.controller.account.statement.convert;

import com.hd123.m3.account.commons.StringPairGroupItem;
import com.hd123.m3.cre.controller.account.statement.model.BStringPairGroupItem;
import com.hd123.rumba.commons.collection.CollectionUtil;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.commons.rpc.StringPair;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * StringPairGroupItem->BStringPairGroupItem
 * 
 * @author chenganbang
 * 
 */
public class StringPairGroupItemBizConverter implements
    Converter<StringPairGroupItem, BStringPairGroupItem> {
  private static StringPairGroupItemBizConverter instance;

  public static StringPairGroupItemBizConverter getInstance() {
    if (instance == null)
      instance = new StringPairGroupItemBizConverter();
    return instance;
  }

  @Override
  public BStringPairGroupItem convert(StringPairGroupItem source) throws ConversionException {
    if (source == null)
      return null;

    BStringPairGroupItem target = new BStringPairGroupItem();
    if (StringUtil.isNullOrBlank(source.getKeys()) == false) {
      target.setKeys(CollectionUtil.toList(source.getKeys(), ','));
    }
    for (StringPair pair : source.getPairs()) {
      target.getPairs().put(pair.getKey(), pair.getValue());
      target.getDetails().add(pair.getKey() + "：" + pair.getValue());
    }
    return target;
  }

}
