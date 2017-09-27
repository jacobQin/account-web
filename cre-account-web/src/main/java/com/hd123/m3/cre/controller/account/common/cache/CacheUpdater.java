package com.hd123.m3.cre.controller.account.common.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hd123.bpm.service.util.AppId;

public class CacheUpdater {
  private static final Logger LOG = LoggerFactory.getLogger(CacheUpdater.class);

  private CacheUpdater() {
  }

  public static CacheUpdater get() {
    return INSTANCE;
  }

  public CacheUpdater start(Runnable task) {
    this.updateTasks.add(task);
    start();
    return this;
  }

  private void start() {
    new Timer(this.getClass().getSimpleName(), true).schedule(new TimerTask() {
      @Override
      public void run() {
        for (Runnable task : updateTasks) {
          LOG.debug("更新缓存({})：{}", AppId.get(), task.getClass().getSimpleName());
          try {
            task.run();
          } catch (Exception e) {
            LOG.warn("更新缓存异常。", e);
          }
        }
      }
    }, DELAY, PERIOD);
  }

  private static final long DELAY = 1000L * 60 * 5;
  private static final long PERIOD = 1000L * 60;

  /** 缓存更新任务 */
  private List<Runnable> updateTasks = Collections.synchronizedList(new ArrayList());

  private static final CacheUpdater INSTANCE = new CacheUpdater();
}
