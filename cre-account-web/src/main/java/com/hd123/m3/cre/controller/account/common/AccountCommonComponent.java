/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AccountComponent.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月7日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.cre.controller.account.common.model.SUser;

/**
 * 账务系统通用组件。
 * 
 * @author LiBin
 *
 */
@Component
public class AccountCommonComponent {
  
  @Autowired
  UserService userSrvice;
  
  /***
   * 根据用于id取得指定的用户
   * 
   * @param userId
   *          用户id，传入null将返回null。
   * @return 找不到将返回null。
   */
  public SUser getSUser(String userId) {
    if (userId == null) {
      return null;
    }

    User user = userSrvice.get(userId);

    if (user == null) {
      return null;
    }

    SUser suser = new SUser();
    suser.setUuid(user.getUuid());
    suser.setCode(user.getLoginName());
    suser.setName(user.getFullName());
    suser.setId(user.getLoginName());
    suser.setNamespace(user.getNamespace());
    return suser;
  }


}
