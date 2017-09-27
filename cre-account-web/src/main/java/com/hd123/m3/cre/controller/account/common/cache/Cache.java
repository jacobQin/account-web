/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	bpm-api
 * 文件名：	Cache.java
 * 模块说明：	
 * 修改历史：
 * 2015-1-14 - zhangyanbo - 创建。
 */
package com.hd123.m3.cre.controller.account.common.cache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cache<T> {

  private static final Logger LOG = LoggerFactory.getLogger(Cache.class);

  /**
   * 构造缓存。
   * 
   * @param size
   *          缓存大小，必须大于0
   */
  public Cache(int size) {
    this.map = Collections.synchronizedMap(new LRUMap(size));
    this.maxSize = size;
  }

  /**
   * 从缓存中取值。
   * 
   * @param key
   *          键名，not null
   * @return 值
   */
  public T get(String key) {
    total++;
    SoftReference<CachedObject> obj = map.get(key);
    if (obj == null)
      return null;
    CachedObject cachedobj = obj.get();
    if (cachedobj == null) {
      map.remove(key);
      return null;
    }

    // 如果超时，直接返回
    if (timeout > 0 && System.currentTimeMillis() > cachedobj.getTimeToLive() + timeout * 1000) {
      map.remove(key);
      LOG.debug("刷新缓存：{}, {}", name, key);
      return null;
    }

    hit++;
    return (T) cachedobj.getObj();
  }

  /**
   * 从缓存中取值。
   * 
   * @param key
   *          键名，not null
   * @return 值
   */
  public T get(CacheKey key) {
    return get(key.toString());
  }

  /**
   * 移除key。
   * 
   * @param key
   */
  public void remove(String key) {
    if (key == null)
      return;
    map.remove(key);
  }

  /**
   * 清空缓存。
   */
  public void clear() {
    map.clear();
  }

  /** 调用次数 */
  public long getTotal() {
    return total;
  }

  /** 命中次数 */
  public long getHit() {
    return hit;
  }

  /** 缓存名 */
  public String getName() {
    return name;
  }

  /** 缓存大小 */
  public long size() {
    return map != null ? map.size() : 0L;
  }

  /** 最大容量 */
  public int getMaxSize() {
    return maxSize;
  }

  /**
   * 放入缓存。
   * 
   * @param key
   *          键名，not null
   * @param value
   *          值
   */
  public void put(String key, T value) {
    map.put(key, new SoftReference(new CachedObject(value)));
  }

  /**
   * 放入缓存。
   * 
   * @param key
   *          键名，not null
   * @param value
   *          值
   */
  public void put(CacheKey key, T value) {
    put(key.toString(), value);
  }

  /**
   * 从缓存中取值，最多只有一个值，不需要指定key。
   * 
   * @return 值
   */
  public T get() {
    return get(KEY_NONE);
  }

  /**
   * 放入缓存，有且只有一个值，不需要指定key。
   * 
   * @param value
   *          值
   */
  public void put(T value) {
    put(KEY_NONE, value);
  }

  class CachedObject {
    CachedObject(Object obj) {
      this.obj = obj;
      this.timeToLive = System.currentTimeMillis();
    }

    private Object obj;
    private long timeToLive;

    /** 对象 */
    public Object getObj() {
      return obj;
    }

    public void setObj(Object obj) {
      this.obj = obj;
    }

    /** 存活时间 */
    public long getTimeToLive() {
      return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
      this.timeToLive = timeToLive;
    }
  }

  protected long timeout = -1;
  protected String name = "default";
  protected int maxSize = 0;

  // 命中率计数
  protected long total = 0L; // 取次数
  protected long hit = 0L; // 命中次数

  private Map<String, SoftReference<CachedObject>> map;

  private static final String KEY_NONE = "-";
}
