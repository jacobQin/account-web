package com.hd123.m3.cre.controller.account.common.cache;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hd123.bpm.service.util.AppId;

public class CacheMonitor {
  private static final Logger LOG = LoggerFactory.getLogger(CacheMonitor.class);

  private CacheMonitor() {
  }

  public static CacheMonitor get() {
    return INSTANCE;
  }

  public CacheMonitor monitor(Cache... caches) {
    for (Cache c : caches)
      list.add(c);
    if (list.isEmpty() == false)
      start();
    return this;
  }

  public void clear(String name) {
    Cache c = getCache(name);
    if (c != null)
      c.clear();
  }

  public void clearAll() {
    for (Cache c : list)
      c.clear();
  }

  public Map<String, Map<String, Object>> listAll() {
    Map<String, Map<String, Object>> result = new HashMap();
    for (Cache c : list) {
      Map<String, Object> map = new HashMap();
      map.put("size", c.size());
      map.put("maxSize", c.getMaxSize());
      map.put("total", c.getTotal());
      map.put("hit", c.getHit());
      map.put("hitRate", getRate(c.getHit(), c.getTotal()));
      map.put("useRate", getRate(c.size(), c.getMaxSize()));
      result.put(c.getName(), map);
    }
    return result;
  }

  public Map<String, Object> list(String name) {
    Cache c = getCache(name);
    Map<String, Object> result = new HashMap();
    if (c != null) {
      result.put("size", c.size());
      result.put("maxSize", c.getMaxSize());
      result.put("total", c.getTotal());
      result.put("hit", c.getHit());
      result.put("hitRate", getRate(c.getHit(), c.getTotal()));
      result.put("useRate", getRate(c.size(), c.getMaxSize()));
    }
    return result;
  }

  private Cache getCache(String name) {
    for (Cache c : list)
      if (ObjectUtils.equals(name, c.getName()))
        return c;
    return null;
  }

  private synchronized void start() {
    if (started)
      return;

    new Timer(this.getClass().getSimpleName(), true).scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        try {
          printHit();
        } catch (Exception e) {
        }
      }
    }, DELAY, PERIOD);
    started = true;
  }

  private void printHit() {
    StringBuilder sb = new StringBuilder();
    sb.append("缓存情况(" + AppId.get() + ")：");
    for (Cache c : list) {
      sb.append("\r\n  ").append(
          MessageFormat.format("{0}，大小：{1}，使用率：{2}，调用：{3}，命中：{4}，命中率：{5}", c.getName(), c.size(),
              getRate(c.size(), c.getMaxSize()), c.getTotal(), c.getHit(),
              getRate(c.getHit(), c.getTotal())));
    }
    LOG.info(sb.toString());
  }

  private String getRate(long hit, long total) {
    if (total == 0) {
      return "NaN";
    } else {
      return (hit * 100 / total) + "%";
    }
  }

  private List<Cache> list = new ArrayList();
  private static final long DELAY = 1000L * 60 * 10;
  private static final long PERIOD = 1000L * 60 * 10;

  private static boolean started = false;
  private static final CacheMonitor INSTANCE = new CacheMonitor();
}
