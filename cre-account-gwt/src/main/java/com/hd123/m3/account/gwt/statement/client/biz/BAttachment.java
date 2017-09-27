/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BAttachment.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月5日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.biz;

import com.hd123.rumba.commons.gwt.entity.client.BEntity;
import com.hd123.rumba.commons.gwt.entity.client.BOperateInfo;

/**
 * 附件|B对象。
 * 
 * @author zhr
 * 
 */
public class BAttachment extends BEntity {
  private static final long serialVersionUID = -6161206650580911462L;

  private String id;
  private String name;
  private String fileType;
  private long size;
  private String url;
  private BOperateInfo createInfo;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public BOperateInfo getCreateInfo() {
    return createInfo;
  }

  public void setCreateInfo(BOperateInfo createInfo) {
    this.createInfo = createInfo;
  }

}
