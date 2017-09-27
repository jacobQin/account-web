/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StringPairGroupGrid.java
 * 模块说明：	
 * 修改历史：
 * 2014-7-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.hd123.m3.account.gwt.commons.client.biz.BStringPairGroup;
import com.hd123.m3.account.gwt.commons.client.biz.BStringPairGroupItem;
import com.hd123.rumba.commons.gwt.event.client.ActionEvent;
import com.hd123.rumba.commons.gwt.event.client.ActionHandler;
import com.hd123.rumba.commons.gwt.event.client.HasActionHandlers;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;

/**
 * @author huangjunxian
 * 
 */
public class StringPairGroupLineGrid extends Composite implements HasValue<BStringPairGroup>,
    HasActionHandlers, ActionHandler {

  public StringPairGroupLineGrid() {
    super();
    drawSelf();
  }

  private BStringPairGroup value;

  private RGrid grid;
  private int currentRow = 0;

  private void drawSelf() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setHoverRow(true);
    grid.setShowCurrentRow(true);
    grid.addHighlightHandler(new GridHighlightHandler());
    grid.setProvider(new GridDataProvider());

    initWidget(grid);
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return value == null || value.getItems() == null ? 0 : value.getItems().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (value == null || value.getItems() == null || value.getItems().isEmpty())
        return null;

      BStringPairGroupItem item = value.getItems().get(row);
      if (item == null)
        return null;
      String key = value.getMainKeys().get(col);
      return item.getValue(key);
    }

  }

  private class GridHighlightHandler implements HighlightHandler<Point> {

    @Override
    public void onHighlight(HighlightEvent<Point> event) {
      GWTUtil.blurActiveElement();

      final int row = event.getHighlighted().getY();

      CommandQueue.offer(new LocalCommand() {
        public void onCall(CommandQueue queue) {
          currentRow = row;
          doFireChangeLine();
          queue.goon();
        }
      });
      CommandQueue.awake();
    }

  }

  private void doFireChangeLine() {
    ActionEvent.fire(StringPairGroupLineGrid.this, ActionName.CHANGE_LINE, new Integer(currentRow));
  }

  public static class ActionName {
    /** 换行事件 */
    public static final String CHANGE_LINE = "change_line";
    /** 上一行 */
    public static final String PREVLINE = "prevLine";
    /** 下一行 */
    public static final String NEXTLINE = "nextLine";
    /** 变值 */
    public static final String CHANGE_VALUE = "change_value";
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<BStringPairGroup> arg0) {
    return null;
  }

  @Override
  public BStringPairGroup getValue() {
    return value;
  }

  @Override
  public void setValue(BStringPairGroup value) {
    setValue(value, false);
  }

  @Override
  public void setValue(BStringPairGroup value, boolean fireEvent) {
    this.value = value;
    currentRow = 0;
    refresh();
  }

  private void refresh() {
    grid.clearColumnDefs();
    if (value == null) {
      grid.rebuild();
      return;
    }
    for (String caption : value.getMainKeys()) {
      RGridColumnDef colDef = new RGridColumnDef(caption);
      colDef.setWidth("90px");
      colDef.setSortable(false);
      colDef.setOverflowEllipsis(true);
      grid.addColumnDef(colDef);
    }
    grid.rebuild();
    grid.setCurrentRow(currentRow);
    doFireChangeLine();
  }

  @Override
  public void onAction(final ActionEvent event) {
    CommandQueue.offer(new LocalCommand() {

      @Override
      public void onCall(CommandQueue queue) {
        if (event.getAction() == ActionName.PREVLINE) {
          currentRow = currentRow == 0 ? currentRow : --currentRow;
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          ActionEvent.fire(StringPairGroupLineGrid.this, ActionName.CHANGE_LINE, new Integer(
              currentRow));
        } else if (event.getAction() == ActionName.NEXTLINE) {
          currentRow = currentRow >= value.getItems().size() - 1 ? currentRow : ++currentRow;
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          ActionEvent.fire(StringPairGroupLineGrid.this, ActionName.CHANGE_LINE, new Integer(
              currentRow));
        } else if (event.getAction() == ActionName.CHANGE_LINE) {
          currentRow = ((Integer) event.getParameter()).intValue();
          grid.setCurrentRow(currentRow);
          grid.refreshRow(currentRow);
          ActionEvent.fire(StringPairGroupLineGrid.this, ActionName.CHANGE_LINE, new Integer(
              currentRow));
        } else if (event.getAction() == ActionName.CHANGE_VALUE) {
          int row = ((Integer) event.getParameter()).intValue();
          grid.refreshRow(row);
        }
        queue.goon();
      }
    });
    CommandQueue.awake();
  }

  @Override
  public HandlerRegistration addActionHandler(ActionHandler handler) {
    return addHandler(handler, ActionEvent.getType());
  }

}
