/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BStringPairGroup.java
 * 模块说明：	
 * 修改历史：
 * 2014-7-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangjunxian
 * 
 */
public class BStringPairGroup implements Serializable {
  private static final long serialVersionUID = -5243427597604498582L;

  private boolean showDetail;
  private String caption;
  private List<String> mainKeys = new ArrayList<String>();
  private List<BStringPairGroupItem> items = new ArrayList<BStringPairGroupItem>();

  /** 数据标题 */
  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  /** 表格显示的标题 */
  public List<String> getMainKeys() {
    return mainKeys;
  }

  public void setMainKeys(List<String> mainKeys) {
    this.mainKeys = mainKeys;
  }

  /** 表格数据明细 */
  public List<BStringPairGroupItem> getItems() {
    return items;
  }

  public void setItems(List<BStringPairGroupItem> items) {
    this.items = items;
  }

  /** 是否需要显示明细,当mainKeys和所有item的keys相等时为false，否则为true */
  public boolean isShowDetail() {
    return showDetail;
  }

  public void setShowDetail(boolean showDetail) {
    this.showDetail = showDetail;
  }

}
