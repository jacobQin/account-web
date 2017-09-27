/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	StoreMonthDaysOptionsDialog.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月6日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui.settle;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.m3.account.gwt.option.client.BStoreMonthDaysOption;
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

/**
 * 项目财务月天数配置项编辑对话框
 * 
 * @author LiBin
 * 
 */
public class StoreMonthDaysOptionsDialog extends RDialog implements ClickHandler, RActionHandler {

  private List<BStoreMonthDaysOption> value;

  private RHyperlink addLink;
  private RForm mainBody;
  private AccountUnitBrowserDialog storeDialog;
  private RButton okButton;

  public StoreMonthDaysOptionsDialog() {
    setCaptionHTML(OptionMessages.M.monthDays());
    setWidth("520px");
    setHeight("500px");
    setAutoHideEnabled(false);

    ButtonConfig buttonOk = new ButtonConfig(OptionMessages.M.ok(), false);
    setButtons(new ButtonConfig[] {
        buttonOk, RDialog.BUTTON_CANCEL });

    okButton = getButton(buttonOk);
    okButton.addClickHandler(this);
    okButton.setText(OptionMessages.M.ok());

    drawBody();
  }

  @Override
  public void center() {
    super.center();
    loadMonthDaysOptions();
  }

  private void drawBody() {
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

  /* 页面隐藏时调用 */
  public void onHide() {
    if(storeDialog !=null){
      storeDialog.clearConditions();
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
    } else if (event.getSource() == okButton) {
      saveMonthDaysOptions();
    }
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName().equals(OptionConstants.ACTION_REMOVE_LINE)) {
      if (event.getParameters() != null && event.getParameters().isEmpty() == false) {
        Object obj = event.getParameters().get(0);
        if (obj instanceof BStoreMonthDaysOption) {
          BStoreMonthDaysOption option = (BStoreMonthDaysOption) obj;
          removeOption(option);
          refreshShow();
        }
      }
    }

  }

  private void removeOption(BStoreMonthDaysOption option) {
    if (value == null) {
      return;
    }

    for (BStoreMonthDaysOption op : value) {
      if (op.getStore().equals(option.getStore())) {
        value.remove(op);
        break;
      }
    }
  }

  private void loadMonthDaysOptions() {
    RLoadingDialog.show();
    OptionService.Locator.getService().getStoreMonthDaysOptions(
        new AsyncCallback<List<BStoreMonthDaysOption>>() {
          @Override
          public void onSuccess(List<BStoreMonthDaysOption> result) {
            RLoadingDialog.hide();
            value = result;
            refreshShow();
          }

          @Override
          public void onFailure(Throwable caught) {
            RLoadingDialog.hide();
            RMsgBox.showErrorAndBack(OptionMessages.M.getStoreMonthDaysError(), caught);
          }
        });
  }

  private void saveMonthDaysOptions() {
    RLoadingDialog.show();
    OptionService.Locator.getService().saveStoreMonthDaysOptions(value, new AsyncCallback<Void>() {

      @Override
      public void onSuccess(Void result) {
        RLoadingDialog.hide();
        hide();
      }

      @Override
      public void onFailure(Throwable caught) {
        RLoadingDialog.hide();
        RMsgBox.showErrorAndBack(OptionMessages.M.saveStoreMonthDaysError(), caught);
      }
    });
  }

  /** 刷新显示 */
  private void refreshShow() {
    mainBody.clear();
    if (value == null) {
      return;
    }

    for (BStoreMonthDaysOption option : value) {
      StoreMonthDaysField field = new StoreMonthDaysField(option);
      field.addActionHandler(this);
      mainBody.addField(field);
    }
    mainBody.rebuild();
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
      for (BStoreMonthDaysOption option : value) {
        if (store.equals(option.getStore())) {
          stores.remove(index);
        }
      }
    }
  }

  /** 添加项目选项 */
  private void addOptions(List<BUCN> stores) {
    for (BUCN store : stores) {
      BStoreMonthDaysOption option = new BStoreMonthDaysOption();
      option.setStore(store);
      value.add(option);
    }
  }

}
