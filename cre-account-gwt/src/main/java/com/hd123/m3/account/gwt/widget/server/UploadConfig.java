/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ec-web
 * 文件名：	UploadDefineFactory.java
 * 模块说明：	
 * 修改历史：
 * 2014-10-16 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.widget.server;

/**
 * 上传相关参数配置
 * 
 * @author huangjunxian
 * 
 */
public class UploadConfig {

  private static UploadConfig instance;

  public static UploadConfig getInstance() {
    if (instance == null)
      instance = new UploadConfig();
    return instance;
  }

  private String folderRoot;
  private String downloadSuffix;

  /** 上传文件根目录 */
  public String getFolderRoot() {
    return folderRoot;
  }

  public void setFolderRoot(String folderRoot) {
    this.folderRoot = folderRoot;
  }

  /** 模板下载文件后缀 */
  public String getDownloadSuffix() {
    return downloadSuffix;
  }

  public void setDownloadSuffix(String downloadSuffix) {
    this.downloadSuffix = downloadSuffix;
  }

}
