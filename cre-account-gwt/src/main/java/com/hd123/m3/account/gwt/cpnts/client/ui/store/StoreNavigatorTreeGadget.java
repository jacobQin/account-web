/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-web
 * 文件名：	StoreTreeGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016-1-5 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.store;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.widget.client.ui.loading.LoadingField;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RHorizontalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 搜索页面项目树
 * 
 * @author chenrizhang
 * 
 */
public class StoreNavigatorTreeGadget extends Composite implements HasRActionHandlers {

  /** 未选中项目时，当前取值。避免报空 */
  public static final String DEFAULT_SELECTED = "-";
  /** Action Name：选择项目 */
  public static final String ACTION_SELECT_STORE = "_select_store";

  public StoreNavigatorTreeGadget() {
    super();
    drawSelf();
    initial();
  }

  private Tree tree;

  private List<BStoreNavigator> navigators;// 加载完成之后才实例化
  private String current = DEFAULT_SELECTED;

  private boolean selectFirst;// 因为加载项目导航列表是延时的，所以如果外部调用选择第一个方法时，需要进行标记。

  private void drawSelf() {
    tree = new Tree(StoreNavigatorResources.INSTANCE);

    RSimplePanel sp = new RSimplePanel();
    sp.add(tree);
    sp.setPadding(5);
    sp.setWidth("95%");
    sp.getElement().getStyle().setProperty("maxHeight", "680px");
    sp.getElement().getStyle().setOverflowY(Overflow.AUTO);

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.add(sp);

    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.setCaption("项目导航");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(panel);
    initWidget(box);
  }

  private void initial() {
    current = null;
    tree.clear();
    tree.addItem(new LoadingNode());

    AccountCpntsService.Locator.getService().loadStoreNavigator(
        new AsyncCallback<List<BStoreNavigator>>() {

          @Override
          public void onSuccess(List<BStoreNavigator> result) {
            setValue(result);
            if (selectFirst) {
              selectFirst();
            }
          }

          @Override
          public void onFailure(Throwable caught) {
            RMsgBox.showError(caught);
          }
        });
  }

  public void setValue(List<BStoreNavigator> values) {
    navigators = values != null ? values : new ArrayList<BStoreNavigator>();
    tree.clear();

    for (BStoreNavigator store : values) {
      StoreNode node = new StoreNode();
      tree.addItem(node);
      node.setValue(store);
    }
    if (current != null) {
      setCurrent(current);
    }
  }

  public String getSelected() {
    return current;
  }

  /** 选中第一个，并发出action事件 */
  public void selectFirst() {
    String oldSelected = current;
    if (navigators == null) {
      selectFirst = true;
      return;
    }
    if (navigators.isEmpty()) {
      selectFirst = false;
    } else {
      selectFirst = false;
      setCurrent(navigators.get(0).getUuid());
    }
    if (ObjectUtil.equals(oldSelected, current) == false) {
      RActionEvent.fire(StoreNavigatorTreeGadget.this, ACTION_SELECT_STORE, current);
    }
  }

  /**
   * 设置当前选中
   */
  public void setCurrent(String storeUuid) {
    if (storeUuid == null) {
      return;
    }
    this.current = storeUuid;
    for (int i = 0; i < tree.getItemCount(); i++) {
      TreeItem treeItem = tree.getItem(i);
      if (treeItem instanceof StoreNode) {
        StoreNode node = (StoreNode) treeItem;
        node.setCurrent(storeUuid);
      }
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  private class LoadingNode extends TreeItem {
    private LoadingField field;

    public LoadingNode() {
      super();
      field = new LoadingField();
      setWidget(field);
    }
  }

  private class StoreNode extends TreeItem {

    private BStoreNavigator value;
    private Image icon;
    private RHTMLField itemHtml;

    public StoreNode() {
      super();
      RHorizontalPanel panel = new RHorizontalPanel();
      panel.setWidth("100%");
      panel.setSpacing(1);
      setWidget(panel);

      icon = new Image();
      icon.setResource(StoreNavigatorResources.INSTANCE.store());
      panel.add(icon);
      panel.setCellWidth(icon, "18px");

      itemHtml = new RHTMLField();
      itemHtml.getElement().getStyle().setCursor(Cursor.POINTER);
      itemHtml.setWidth("120px");
      itemHtml.setOverflowEllipsis(true);

      itemHtml.addClickHandler(new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
          current = value.getUuid();
          RActionEvent.fire(StoreNavigatorTreeGadget.this, ACTION_SELECT_STORE, value);
        }
      });

      panel.add(itemHtml);
    }

    public StoreNode(BStoreNavigator values) {
      this();
      setValue(values);
    }

    public void setValue(BStoreNavigator value) {
      this.value = value;
      itemHtml.setHTML(value.getName());

      if (value.getChilds() == null)
        return;

      for (BStoreNavigator child : value.getChilds()) {
        StoreNode node = new StoreNode(child);
        addItem(node);
      }
    }

    public void setCurrent(String storeUuid) {
      for (int i = 0; i < this.getChildCount(); i++) {
        TreeItem treeItem = getChild(i);
        if (treeItem instanceof StoreNode) {
          StoreNode node = (StoreNode) treeItem;
          node.setCurrent(storeUuid);
        }
      }
      itemHtml.getElement().getStyle().clearBackgroundColor();
      if (value.getUuid().equals(storeUuid)) {
        itemHtml.getElement().getStyle().setBackgroundColor("#99CCFF");
        refreshOpenState(this);
      }
    }

    /** 让当前被选中节点打开 */
    private void refreshOpenState(TreeItem item) {
      if (item != null) {
        item.setState(true);
        if (item.getParentItem() != null) {
          refreshOpenState(item.getParentItem());
        }
      }
    }
  }
}
