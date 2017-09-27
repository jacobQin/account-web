/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	EmailSender.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月10日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.email;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.hd123.m3.cre.controller.account.statement.email.config.EmailConfig;

/**
 * @author mengyinkun
 * 
 */
@Component
public class EmailSender {

  @Autowired
  @Qualifier("cre-mail.javaMailSender")
  private JavaMailSender javaMailSender;

  public void sendEmail(String srcPath, String aName, EmailConfig config,
      List<InternetAddress> users) throws EmailException, MessagingException {

    MimeMessage message = javaMailSender.createMimeMessage();

    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
    messageHelper.setFrom(((JavaMailSenderImpl) javaMailSender).getUsername());

    InternetAddress[] senders = getSenders(users);

    messageHelper.setTo(senders);
    messageHelper.setSubject(config.getSubjectMsg());
    messageHelper.setText(config.getMsg(), true);
    messageHelper.addAttachment(aName, new File(srcPath));
    javaMailSender.send(message);

  }

  /**
   * 转换集合，得到联系人列表
   * 
   * @param users
   * @return
   */
  private InternetAddress[] getSenders(List<InternetAddress> users) {
    InternetAddress[] senders = new InternetAddress[users.size()];
    for (int i = 0; i < senders.length; i++) {
      senders[i] = users.get(i);
    }
    return senders;
  }
}
