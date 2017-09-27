/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebatebillOptionsDialog.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月18日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui.settle;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.m3.account.gwt.option.client.BRebateByBillOption;
import com.hd123.m3.account.gwt.option.client.OptionMessages;
import com.hd123.m3.account.gwt.option.client.rpc.OptionService;
import com.hd123.m3.account.gwt.option.client.ui.OptionConstants;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RHorizontalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 返款单是否按笔返款浏览框
 * @author chenganbang
 */
public class RebateByBillOptionsDialog extends RDialog implements ClickHandler, RActionHandler{
  private List<BRebateByBillOption> value;
  
  private RButton okBtn;//确定按钮
  private RForm mainBody;
  private AccountUnitBrowserDialog storeDialog;
  private RHyperlink addLink;
  
  public RebateByBillOptionsDialog(){
    setCaptionHTML(OptionMessages.M.rebateByBill());
    setWidth("520px");
    setHeight("500px");
    setAutoHideEnabled(false);
    
    ButtonConfig buttonOk = new ButtonConfig(OptionMessages.M.ok(), false);
    setButtons(new ButtonConfig[] {
        buttonOk, RDialog.BUTTON_CANCEL });
    
    okBtn = getButton(buttonOk);
    okBtn.addClickHandler(this);
    okBtn.setText(OptionMessages.M.ok());
    
    drawSelf();
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName().equals(OptionConstants.ACTION_REMOVE_LINE)) {
      if (event.getParameters() != null && event.getParameters().isEmpty() == false) {
        Object obj = event.getParameters().get(0);
        if (obj instanceof BRebateByBillOption) {
          BRebateByBillOption option = (BRebateByBillOption) obj;
          removeOption(option);
          refreshShow();
        }
      }
    }
  }

  /**
   * @param option
   */
  private void removeOption(BRebateByBillOption option) {
    if (value == null) {
      return;
    }

    for (BRebateByBillOption op : value) {
      if (op.getStore().equals(option.getStore())) {
        value.remove(op);
        break;
      }
    }
  }

  @Override
  public void onClick(ClickEvent event) {
    if (event.getSource() == addLink) {
      if (storeDialog == null) {
        storeDialog = new AccountUnitBrowserDialog(true, false);
        storeDialog.setSingleSelection(false);
        storeDialog.addValueChangeHandler(new StoreDialogValueChangeHandler());
      }
      storeDialog.center();
    } else if (event.getSource() == okBtn) {
      saveRebateByBillOptions();
    }
  }
  
  /**
   * 保存配置
   */
  private void saveRebateByBillOptions() {
    RLoadingDialog.show();
    OptionService.Locator.getService().saveRebateByBillOption(value, new RBAsyncCallback2<Void>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        RMsgBox.showErrorAndBack(OptionMessages.M.saveRebateByBillOptionsError(), caught);
      }

      @Override
      public void onSuccess(Void result) {
        RLoadingDialog.hide();
        hide();
      }
    });
  }

  /**
   * 绘制浏览框
   */
  private void drawSelf(){
    mainBody = new RForm(1);
    mainBody.setCellSpacing(4);
    mainBody.setLabelWidth(0.05f);
    
    ScrollPanel bodyPanel = new ScrollPanel();
    bodyPanel.setHeight("400px");
    bodyPanel.add(mainBody);
    
    addLink = new RHyperlink(OptionMessages.M.addStore());
    addLink.addClickHandler(this);
    
    RHorizontalPanel hp = new RHorizontalPanel();

    HTML space = new HTML("");
    space.setWidth("400px");

    hp.add(space);
    hp.add(addLink);

    VerticalPanel root = new VerticalPanel();
    root.setSpacing(4);
    root.add(bodyPanel);
    root.add(hp);

    setWidget(root);
  }

  /**
   * 页面隐藏时调用
   */
  public void onHide(){
    if (storeDialog != null) {
      storeDialog.clearConditions();
    }
  }
  
  @Override
  public void center() {
    super.center();
    loadRebateByBillOptions();
  }
  
  /**
   * 加载配置
   */
  private void loadRebateByBillOptions() {
    RLoadingDialog.show();
    OptionService.Locator.getService().getRebateByBillOptions(new RBAsyncCallback2<List<BRebateByBillOption>>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        RMsgBox.showErrorAndBack(OptionMessages.M.getRebateByBillOptionsError(), caught);
      }

      @Override
      public void onSuccess(List<BRebateByBillOption> result) {
        RLoadingDialog.hide();
        value = result;
        refreshShow();
      }
      
    });
  }

  private class StoreDialogValueChangeHandler implements ValueChangeHandler<List<BUCN>> {
    @Override
    public void onValueChange(ValueChangeEvent<List<BUCN>> event) {
      List<BUCN> stores = event.getValue();
      removeReatOptions(stores);
      if (stores.isEmpty()) {
        return;
      }

      addOptions(stores);
      refreshShow();
    }
  }
  
  /** 去除重复配置项 */
  private void removeReatOptions(List<BUCN> stores) {
    if (value == null) {
      return;
    }
    for (int index = stores.size() - 1; index >= 0; index--) {
      BUCN store = stores.get(index);
      for (BRebateByBillOption option : value) {
        if (store.equals(option.getStore())) {
          stores.remove(index);
        }
      }
    }
  }
  
  /** 添加项目选项 */
  private void addOptions(List<BUCN> stores) {
    for (BUCN store : stores) {
      BRebateByBillOption option = new BRebateByBillOption();
      option.setStore(store);
      value.add(option);
    }
  }
  
  /** 刷新显示 */
  private void refreshShow() {
    mainBody.clear();
    if (value == null) {
      return;
    }

    for (BRebateByBillOption option : value) {
      RebateByBillField field = new RebateByBillField(option);
      field.addActionHandler(this);
      mainBody.addField(field);
    }
    mainBody.rebuild();
  }
}
