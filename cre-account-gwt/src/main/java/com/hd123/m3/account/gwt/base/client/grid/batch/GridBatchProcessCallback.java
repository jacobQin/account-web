/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	emposale-web-main-w
 * 文件名：	GridBatchProcessCallback.java
 * 模块说明：	
 * 修改历史：
 * 2013-8-20 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.base.client.grid.batch;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.rumba.gwt.util.client.batchprocess.BatchProcesser;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;

/**
 * @author chenpeisi
 * 
 */
public abstract class GridBatchProcessCallback<T extends Serializable> {
  /**
   * 当处理被中断时被调用。
   * <p>
   * 提供默认行为，可以被重写。
   * 
   * @param grid
   *          处理的表格。
   * @param message
   * @param caught
   */
  public void onAborted(RGrid grid, String message, Throwable caught) {
    if (grid == null)
      return;
    grid.refresh();
  }

  /**
   * 当处理结束时被调用。
   * <p>
   * 提供默认行为，可以被重写。
   * 
   * @param grid
   *          处理的表格。
   * @param interrupted
   *          指定是否是被用户手工中断。
   */
  public void onOver(RGrid grid, boolean interrupted) {
    if (grid == null)
      return;
    grid.refresh();
  }

  /**
   * 批处理。
   * 
   * @param entity
   *          处理对象。
   * @param processer
   *          批理处理器。
   * @param callback
   */
  public abstract void execute(T entity, BatchProcesser<T> processer, AsyncCallback callback);
}
