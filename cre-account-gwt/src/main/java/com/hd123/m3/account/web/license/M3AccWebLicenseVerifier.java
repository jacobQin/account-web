/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web
 * 文件名：	LeaseWebLicenseVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2014年1月17日 - suizhe - 创建。
 */
package com.hd123.m3.account.web.license;

import com.hd123.license.License;
import com.hd123.m3.account.about.M3AccountLicense;
import com.hd123.rumba.commons.jar.ApplicationAbout;
import com.hd123.rumba.webframe.license.LicenseResult;
import com.hd123.rumba.webframe.license.LicenseVerifier;

/**
 * @author suizhe
 *
 */
public class M3AccWebLicenseVerifier implements LicenseVerifier {

  @Override
  public LicenseResult verify(ApplicationAbout about) {
    try {
      License license = M3AccountLicense.getInstance();
      if (license == null) {
        return LicenseResult.break_();
      }

      if(license.isValidComponent(M3AccountLicense.COMPONENT_WEB))
        return LicenseResult.pass();
      else
        return LicenseResult.break_();
    } catch (Exception e) {
      return LicenseResult.break_();
    } 
  }

}
