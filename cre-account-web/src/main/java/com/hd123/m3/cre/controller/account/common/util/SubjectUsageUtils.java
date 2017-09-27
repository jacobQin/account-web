/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	SubjectUsageUtils.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月26日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectType;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.rpc.M3ServiceFactory;

/**
 * 科目用途工具类
 * 
 * @author LiBin
 *
 */
public class SubjectUsageUtils {
  private static final List<SubjectUsage> usages = new ArrayList<SubjectUsage>();
  private static final Map<String, SubjectUsage> map = new HashMap<String, SubjectUsage>();

  private static boolean inited = false;

  private static void init() throws Exception {
    if (inited) {
      return;
    }
    usages.clear();
    map.clear();
    try {
      List<SubjectUsage> list = M3ServiceFactory.getService(SubjectService.class).getAllUsages();
      for (SubjectUsage u : list) {
        usages.add(u);
        map.put(u.getCode(), u);
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
  public static SubjectUsage getSubjectUsage(String code) throws Exception {
    init();
    SubjectUsage u = map.get(code);
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
  public static List<SubjectUsage> getSubjectUsage(UsageType type) throws Exception {
    if (type == null) {
      return new ArrayList<SubjectUsage>();
    }
    init();
    List<SubjectUsage> list = new ArrayList<SubjectUsage>();
    for (SubjectUsage u : usages) {
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
  public static List<SubjectUsage> getAll() throws Exception {
    init();
    List<SubjectUsage> list = new ArrayList<SubjectUsage>();
    for (SubjectUsage u : usages) {
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
  public static List<SubjectUsage> getBySubjectType(SubjectType type) throws Exception {
    init();
    List<SubjectUsage> list = new ArrayList<SubjectUsage>();
    if (type == null)
      return list;
    for (SubjectUsage u : usages) {
      if (type.equals(u.getType()))
        list.add(u.clone());
    }
    return list;
  }

}
