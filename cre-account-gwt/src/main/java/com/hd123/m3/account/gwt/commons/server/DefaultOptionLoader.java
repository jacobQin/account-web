/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	DefaultOptionLoader.java
 * 模块说明：	
 * 修改历史：
 * 2016年2月18日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.commons.server;

import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * 默认配置加载工具类.
 * 
 * @author LiBin
 * 
 */
public class DefaultOptionLoader {

  private static DefaultOptionLoader instance = null;

  public static DefaultOptionLoader getInstance() {
    if (instance == null) {
      instance = new DefaultOptionLoader();
    }
    return instance;
  }

  private DefaultOptionLoader() {
    // Do Nothing
  }

  /** 获得默认配置 */
  public BDefaultOption getDefaultOption() throws Exception {
    try {
      BDefaultOption defaultOption = new BDefaultOption();
      defaultOption.setPaymentType(getPaymentType(getOptionService().getOption(
          AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_PAYMENT)));
      TaxRate taxRate = JsonUtil.jsonToObject(
          getOptionService().getOption(AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_TAXRATE),
          TaxRate.class);
      defaultOption.setTaxRate(TaxRateBizConverter.getInstance().convert(taxRate));
      defaultOption.setPreReceiveSubject(getSubject(getOptionService().getOption(
          AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_PRERECEIVE_SUBJECT)));
      defaultOption.setPrePaySubject(getSubject(getOptionService().getOption(
          AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_PREPAY_SUBJECT)));

      return defaultOption;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /** 获得默认配置的预存款科目 */
  public BUCN getPreReceiveSubject() throws ClientBizException {
    return getSubject(getOptionService().getOption(AccOptions.DEFAULT_PATH,
        AccOptions.DEFAULT_PRERECEIVE_SUBJECT));
  }

  /** 获得默认配置的预付款科目 */
  public BUCN getPrePaySubject() throws ClientBizException {
    return getSubject(getOptionService().getOption(AccOptions.DEFAULT_PATH,
        AccOptions.DEFAULT_PREPAY_SUBJECT));
  }

  private BUCN getPaymentType(String uuid) throws ClientBizException {
    if (StringUtil.isNullOrBlank(uuid))
      return null;

    PaymentType paymentType = getPaymentTypeService().get(uuid);
    if (paymentType == null)
      return null;
    BUCN result = new BUCN();
    result.setUuid(paymentType.getUuid());
    result.setCode(paymentType.getCode());
    result.setName(paymentType.getName());

    return result;
  }

  private BUCN getSubject(String uuid) throws ClientBizException {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }

    Subject subject = getSubjectService().get(uuid);
    if (subject == null) {
      return null;
    }

    BUCN result = new BUCN();
    result.setUuid(subject.getUuid());
    result.setCode(subject.getCode());
    result.setName(subject.getName());

    return result;
  }

  private AccountOptionService getOptionService() {
    return M3ServiceFactory.getService(AccountOptionService.class);
  }

  private PaymentTypeService getPaymentTypeService() throws ClientBizException {
    return M3ServiceFactory.getService(PaymentTypeService.class);
  }

  private SubjectService getSubjectService() throws ClientBizException {
    return M3ServiceFactory.getService(SubjectService.class);
  }

}
