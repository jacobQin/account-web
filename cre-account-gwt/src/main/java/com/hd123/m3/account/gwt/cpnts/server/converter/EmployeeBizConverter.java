/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	EmployeeBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-22 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.converter;

import com.hd123.ia.authen.service.contact.Phone;
import com.hd123.ia.authen.service.user.User;
import com.hd123.m3.account.gwt.cpnts.client.biz.BEmployee;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * @author chenrizhang
 * 
 */
public class EmployeeBizConverter implements Converter<User, BEmployee> {
  private static EmployeeBizConverter instance;

  public static EmployeeBizConverter getInstance() {
    if (instance == null) {
      instance = new EmployeeBizConverter();
    }
    return instance;
  }

  private EmployeeBizConverter() {
  }

  @Override
  public BEmployee convert(User source) throws ConversionException {
    if (source == null)
      return null;

    BEmployee target = new BEmployee();
    target.setUuid(source.getUuid());
    target.setCode(source.getId());
    target.setName(source.getFullName());
    target.setPhone(source.getPhone(Phone.USAGE_HOME));
    target.setMobile(source.getPhone(Phone.USAGE_MOBILE));
    target.setEmail(source.getEmail());

    return target;
  }
}
