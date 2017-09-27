package com.hd123.m3.cre.controller.account.common.cache;

public class CacheBuilder {

  /**
   * 设置缓存名称。
   * 
   * @param name
   *          缓存名称。
   */
  public CacheBuilder name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 设置缓存大小。
   * 
   * @param size
   *          缓存大小，必须大于0。
   */
  public CacheBuilder size(int size) {
    this.size = size;
    return this;
  }

  /**
   * 设置超时时间（单位：秒），<0表示永远不超时。
   * 
   * @param timeout
   *          超时时间。
   */
  public CacheBuilder timeout(long timeout) {
    this.timeout = timeout;
    return this;
  }

  public Cache build() {
    Cache c = new Cache(size);
    c.timeout = timeout;
    c.name = name;
    return c;
  }

  private int size = 1000;
  private long timeout = -1;
  private String name = "default";
}
