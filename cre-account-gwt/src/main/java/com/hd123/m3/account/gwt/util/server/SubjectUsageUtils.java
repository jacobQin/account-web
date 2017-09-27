/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	SubjectUsageUtils.java
 * 模块说明：	
 * 修改历史：
 * 2013-8-15 - suizhe - 创建。
 */
package com.hd123.m3.account.gwt.util.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.cpnts.client.biz.BUsageType;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.rpc.M3ServiceFactory;

/**
 * 科目用途工具类
 * 
 * @author suizhe
 * 
 */
public class SubjectUsageUtils {
  private static final List<BSubjectUsage> usages = new ArrayList<BSubjectUsage>();
  private static final Map<String, BSubjectUsage> map = new HashMap<String, BSubjectUsage>();

  private static boolean inited = false;

  private static void init() throws Exception {
    if (inited)
      return;
    usages.clear();
    map.clear();
    try {
      List<SubjectUsage> list = M3ServiceFactory.getService(SubjectService.class).getAllUsages();
      for (SubjectUsage u : list) {
        BSubjectUsage bu = new BSubjectUsage();
        bu.setCode(u.getCode());
        bu.setName(u.getName());
        bu.setType(BSubjectType.valueOf(u.getType().name()));
        bu.setUsageType(BUsageType.valueOf(u.getUsageType().name()));
        usages.add(bu);
        map.put(bu.getCode(), bu);
      }
      inited = true;
    } catch (Exception e) {
      Logger.getLogger(SubjectUsageUtils.class).error("SubjectUsageUtils init error", e);
      throw new Exception(e.getMessage());
    }

  }

  /**
   * 根据代码获取科目用途
   * 
   * @param code
   *          用途代码
   * @return 用途
   * @throws Exception
   */
  public static BSubjectUsage getSubjectUsage(String code) throws Exception {
    init();
    BSubjectUsage u = map.get(code);
    return u == null ? null : u.clone();
  }

  /**
   * 根据用途类型获取科目用途
   * 
   * @param type
   *          用途类型
   * @return 用途列表
   * @throws Exception
   */
  public static List<BSubjectUsage> getSubjectUsage(UsageType type) throws Exception {
    if (type == null) {
      return new ArrayList<BSubjectUsage>();
    }
    init();
    List<BSubjectUsage> list = new ArrayList<BSubjectUsage>();
    for (BSubjectUsage u : usages) {
      if (type.name().equals(u.getUsageType().name())) {
        list.add(u.clone());
      }
    }
    return list;
  }

  /**
   * 获取所有科目用途
   * 
   * @return 科目用途列表
   * @throws Exception
   */
  public static List<BSubjectUsage> getAll() throws Exception {
    init();
    List<BSubjectUsage> list = new ArrayList<BSubjectUsage>();
    for (BSubjectUsage u : usages) {
      list.add(u.clone());
    }
    return list;
  }

  /**
   * 获取指定类型的科目用途
   * 
   * @param type
   *          类型
   * @return 用途列表
   * @throws Exception
   */
  public static List<BSubjectUsage> getBySubjectType(BSubjectType type) throws Exception {
    init();
    List<BSubjectUsage> list = new ArrayList<BSubjectUsage>();
    if (type == null)
      return list;
    for (BSubjectUsage u : usages) {
      if (type.equals(u.getType()))
        list.add(u.clone());
    }
    return list;
  }

}
