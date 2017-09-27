/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BillTypeUtils.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月27日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 单据类型工具类
 * 
 * @author LiBin
 *
 */
public class BillTypeUtils {
  private static final List<BillType> types = new ArrayList<BillType>();
  private static final Map<String, BillType> map = new HashMap<String, BillType>();

  private static boolean inited = false;

  private static void init() throws Exception {
    if (inited)
      return;

    try {
      List<BillType> list = getBillTypeService().getAllTypes();
      for (BillType t : list) {
        types.add(t);
        map.put(t.getName(), t);
      }
      inited = true;
    } catch (Exception e) {
      Logger.getLogger(BillTypeUtils.class).error("BillTypeUtils init error", e);
      throw new Exception(e.getMessage());
    }
  }

  /**
   * 根据类型收付方向取得单据类型
   * 
   * @param className
   *          单据类名
   * @param direction
   *          收付方向,允许为空，为空表示不考虑收付方向。
   * @return 单据类型
   * @throws Exception
   */
  public static BillType getBillTypeByClassName(String className, Integer direction)
      throws Exception {
    init();
    BillType source = null;
    if (direction == null) {
      source = getBillTypeService().getByClassName(className);
    } else {
      source = getBillTypeService().getByClassName(className, direction);
    }
    
    return source;
  }

  /**
   * 根据名称获取单据类型。
   * 
   * @param name
   *          单据类型代码
   * @return 单据类型
   * @throws Exception
   */
  public static BillType getBillTypeByName(String name) throws Exception {
    init();
    BillType type = map.get(name);
    return type == null ? null : type.clone();
  }

  /**
   * 获取所有单据类型。
   * 
   * @return 单据类型列表
   * @throws Exception
   */
  public static List<BillType> getAll() throws Exception {
    init();
    List<BillType> list = new ArrayList<BillType>();
    for (BillType type : types) {
      list.add(type.clone());
    }
    return list;
  }

  /**
   * 取出不包含指定类型的单据类型
   * 
   * @param billTypes
   *          不包含的单据类型列表（必须指定className和direction）
   * @return 单据类型列表
   * @throws Exception
   */
  public static List<BillType> getAllWithout(BillType... billTypes) throws Exception {
    init();
    List<BillType> list = new ArrayList<BillType>();
    for (BillType type : types) {
      if (containType(type, billTypes) == false) {
        list.add(type.clone());
      }
    }
    return list;
  }

  private static boolean containType(BillType type, BillType... billTypes) throws Exception {
    if (billTypes == null || billTypes.length <= 0)
      return false;
    for (BillType t : billTypes) {
      if (StringUtil.isNullOrBlank(t.getClassName()))
        continue;
      if (t.getClassName().equals(type.getClassName()) && t.getDirection() == type.getDirection())
        return true;
    }
    return false;
  }

  /**
   * 获取所有单据类型的名称(key)-标题(value)map
   * 
   * @return 名称标题map
   * @throws Exception
   */
  public static Map<String, String> getAllNameCaption() throws Exception {
    init();
    Map<String, String> result = new HashMap<String, String>();
    for (BillType type : types) {
      result.put(type.getName(), type.getCaption());
    }
    return result;
  }

  private static BillTypeService getBillTypeService() {
    return M3ServiceFactory.getService(BillTypeService.class);
  }
}
