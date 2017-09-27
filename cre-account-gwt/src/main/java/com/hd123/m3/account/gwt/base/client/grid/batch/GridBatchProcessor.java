/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	emposale-web-main-w
 * 文件名：	GridBatchProcessor.java
 * 模块说明：	
 * 修改历史：
 * 2013-8-20 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.base.client.grid.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.rumba.commons.gwt.mini.client.Assert;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.RPCExceptionDecoder;
import com.hd123.rumba.gwt.util.client.batchprocess.BatchProcesser;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RProgressDialog;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author chenpeisi
 * 
 */
public class GridBatchProcessor<T extends Serializable> {

  private List<T> forEntitys;
  private String entityCaption;
  private RGrid grid;

  /**
   * 取得批量处理器对象。
   * 
   * @param gird
   *          表格,not null。
   * @param entityCaption
   *          处理对象标题。
   * @return
   * @throws ClientBizException
   */
  public GridBatchProcessor(List<T> forEntitys, RGrid gird, String entityCaption)
      throws ClientBizException {
    Assert.assertArgumentNotNull(gird, "pagingGrid");

    this.forEntitys = forEntitys;
    this.entityCaption = entityCaption;
    this.grid = gird;
  }

  /**
   * 批量操作。
   * 
   * @param actionText
   *          操作名。not null。
   * @param array
   *          处理数据数组。not null。
   *          <p>
   *          用于数据转换，使用者可以传入new T[]{}即可。
   * @param callback
   *          回调接口。not null。
   */
  public void batchProcess(String actionText, T[] array, GridBatchProcessCallback<T> callback) {
    if (array == null)
      return;
    if (callback == null)
      return;
    doBatchProcess(actionText, array, callback);
  }

  private void doBatchProcess(final String actionText, final T[] array,
      final GridBatchProcessCallback callback) {
    assert grid != null;
    assert array != null;
    assert callback != null;

    final List<T> entitys = new ArrayList<T>();

    for (Integer row : grid.getSelections()) {
      entitys.add(forEntitys.get(row.intValue()));
    }
    if (entitys.isEmpty()) {
      RMsgBox.showError(GridBatchProcessorRes.R.pleaseChoose() + entityCaption + "。");
      return;
    }

    String msg = GridBatchProcessorRes.R.whether() + actionText + GridBatchProcessorRes.R.current()
        + entityCaption + "（ " + GridBatchProcessorRes.R.total() + entitys.size()
        + GridBatchProcessorRes.R.lines() + " ）？";
    RMsgBox.show(msg, RMsgBox.ICON_QUESTION, RMsgBox.BUTTONS_OKCANCEL, RMsgBox.BUTTON_OK,
        new RMsgBox.Callback() {
          public void onClosed(ButtonConfig clickedButton) {
            if (RMsgBox.BUTTON_OK.equals(clickedButton)) {
              EntityBatchProcesser processer = new EntityBatchProcesser(entitys, array);
              processer.setActionText(actionText);
              processer.setCallback(callback);
              processer.start();
            }
          }
        });
  }

  /** 批量处理器对象 */
  private class EntityBatchProcesser extends BatchProcesser<T> {

    public EntityBatchProcesser(List<T> records, T[] array) {
      setProgressPanel(new RProgressDialog());
      setRecords(records.toArray(array));
    }

    private String actionText = GridBatchProcessorRes.R.unknownOperation();
    private GridBatchProcessCallback<T> callback;

    public void setActionText(String actionText) {
      this.actionText = actionText;
    }

    public void setCallback(GridBatchProcessCallback<T> callback) {
      this.callback = callback;
    }

    @Override
    public void onAborted(String message, Throwable caught) {
      StringBuilder sb = new StringBuilder();
      if (message != null)
        sb.append(message);
      if (caught != null) {
        if (sb.length() != 0)
          sb.append("：");
        sb.append(RPCExceptionDecoder.decode(caught).getMessage());
      }
      if (sb.length() != 0)
        sb.append("<br>");
      sb.append(getReport().getHTML());
      RMsgBox.showError(sb.toString());

      assert callback != null;
      callback.onAborted(grid, message, caught);
    }

    @Override
    public void onOver(boolean interrupted) {
      StringBuilder sb = new StringBuilder();
      sb.append(interrupted ? GridBatchProcessorRes.R.interrupt() : GridBatchProcessorRes.R.batch()
          + actionText + GridBatchProcessorRes.R.completely());
      sb.append("<br>" + getReport().getHTML());
      RMsgBox.show(sb.toString(), interrupted ? RMsgBox.ICON_WARN : RMsgBox.ICON_INFO);

      assert callback != null;
      callback.onOver(grid, interrupted);
    }

    @Override
    public void process(final T entity, int index, final BatchProcesser processer) {
      if (entity == null) {
        getReport().reportSuccess();
        next();
        return;
      }
      getProgressPanel().setMessage(
          "正在" + actionText + entityCaption + "“" + entity.toString() + "”...");

      assert callback != null;
      callback.execute(entity, processer, new RBAsyncCallback2() {
        @Override
        public void onException(final Throwable caught) {
          processer.getReport().reportFailure();
          final String msg = actionText + entityCaption + "“" + entity.toString() + "”过程中发生错误。";
          final Throwable throwable = caught instanceof ClientBizException ? ((ClientBizException) caught)
              .getCause() : caught;
          RMsgBox.showError(msg + "请确认是否继续？", throwable, RMsgBox.BUTTONS_OKCANCEL,
              RMsgBox.BUTTON_CANCEL, new RMsgBox.Callback() {
                public void onClosed(ButtonConfig clickedButton) {
                  if (RMsgBox.BUTTON_CANCEL.equals(clickedButton))
                    processer.abort(msg, throwable);
                  else
                    processer.next();
                }
              });
        }

        @Override
        public void onSuccess(Object result) {
          processer.getReport().reportSuccess();
          processer.next();
        }
      });
    }
  }

  public interface GridBatchProcessorRes extends ConstantsWithLookup {
    public static GridBatchProcessorRes R = GWT.create(GridBatchProcessorRes.class);

    @DefaultStringValue("请先选择")
    String pleaseChoose();

    @DefaultStringValue("是否")
    String whether();

    @DefaultStringValue("当前选中的")
    String current();

    @DefaultStringValue("共计")
    String total();

    @DefaultStringValue("行")
    String lines();

    @DefaultStringValue("未知操作")
    String unknownOperation();

    @DefaultStringValue("用户中断。")
    String interrupt();

    @DefaultStringValue("批量")
    String batch();

    @DefaultStringValue("完成。")
    String completely();

  }
}
