/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BillTypeUtils.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.util.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.server.BillTypeBizConverter;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 单据类型工具类。
 * 
 * @author chenpeisi
 * 
 */
public class BillTypeUtils {
  private static final List<BBillType> types = new ArrayList<BBillType>();
  private static final Map<String, BBillType> map = new HashMap<String, BBillType>();

  private static boolean inited = false;

  private static void init() throws Exception {
    if (inited)
      return;

    try {
      List<BillType> list = getBillTypeService().getAllTypes();
      for (BillType t : list) {
        BBillType type = BillTypeBizConverter.getInstance().convert(t);
        types.add(type);
        map.put(type.getName(), type);
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
  public static BBillType getBillTypeByClassName(String className, Integer direction)
      throws Exception {
    init();
    BillType source = null;
    if (direction == null)
      source = getBillTypeService().getByClassName(className);
    else
      source = getBillTypeService().getByClassName(className, direction);

    return BillTypeBizConverter.getInstance().convert(source);
  }

  /**
   * 根据名称获取单据类型。
   * 
   * @param name
   *          单据类型代码
   * @return 单据类型
   * @throws Exception
   */
  public static BBillType getBillTypeByName(String name) throws Exception {
    init();
    BBillType type = map.get(name);
    return type == null ? null : type.clone();
  }

  /**
   * 获取所有单据类型。
   * 
   * @return 单据类型列表
   * @throws Exception
   */
  public static List<BBillType> getAll() throws Exception {
    init();
    List<BBillType> list = new ArrayList<BBillType>();
    for (BBillType type : types) {
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
  public static List<BBillType> getAllWithout(BBillType... billTypes) throws Exception {
    init();
    List<BBillType> list = new ArrayList<BBillType>();
    for (BBillType type : types) {
      if (containType(type, billTypes) == false) {
        list.add(type.clone());
      }
    }
    return list;
  }

  private static boolean containType(BBillType type, BBillType... billTypes) throws Exception {
    if (billTypes == null || billTypes.length <= 0)
      return false;
    for (BBillType t : billTypes) {
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
    for (BBillType type : types) {
      result.put(type.getName(), type.getCaption());
    }
    return result;
  }

  private static BillTypeService getBillTypeService() {
    return M3ServiceFactory.getService(BillTypeService.class);
  }
}
