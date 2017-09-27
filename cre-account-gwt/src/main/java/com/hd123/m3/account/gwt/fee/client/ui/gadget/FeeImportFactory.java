/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	FeeImportFactory.java
 * 模块说明：	
 * 修改历史：
 * 2015-9-17 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui.gadget;

import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.commons.gwt.widget.client.biz.BImpParams;
import com.hd123.m3.commons.gwt.widget.impex.client.dialog.gadget.ImpContentFactory;
import com.hd123.m3.commons.gwt.widget.impex.client.dialog.gadget.ImpResultPanel;
import com.hd123.m3.commons.gwt.widget.impex.client.dialog.gadget.ImpStartPanel;
import com.hd123.m3.commons.gwt.widget.impex.client.dialog.gadget.job.JobResultPanel;
import com.hd123.rumba.commons.gwt.mini.client.util.Pair;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMessagePanel;

/**
 * 
 * 
 * @author liuguilin
 * 
 */
public class FeeImportFactory extends ImpContentFactory {

  private BImpParams importParams = new BImpParams();

  private FeeImportStartPanel startPanel;
  private FeeImpResultPanel resultPanel;

  @Override
  public ImpStartPanel createImpStartPanel() {
    if (startPanel == null) {
      startPanel = new FeeImportStartPanel(importParams);
    }
    return startPanel;
  }

  @Override
  public JobResultPanel createResultPanel() {
    if (resultPanel == null) {
      resultPanel = new FeeImpResultPanel();
    }
    return resultPanel;
  }

  public void refreshPanel() {
    startPanel.refreshPanel();
  }

  public String getPermGroupId() {
    return startPanel.getPermGroupId();
  }

  public void setPermGroupId(String permGroupId) {
    startPanel.setPermGroupId(permGroupId);
  }

  public String getPermGroupTitle() {
    return startPanel.getPermGroupTitle();
  }

  public void hideResultPanel() {
    if (resultPanel != null)
      resultPanel.hide();
  }

  private class FeeImpResultPanel extends ImpResultPanel {
    public FeeImpResultPanel() {
      super();
    }
  }

  private class FeeImportStartPanel extends ImpStartPanel {

    private RCaptionBox box;
    private FlexTable table;
    private RComboBox<String> permField;
    private RMessagePanel msgPanel;

    public FeeImportStartPanel(BImpParams params) {
      super(params);
    }

    @Override
    protected Widget drawOtherParams() {
      VerticalPanel panel = new VerticalPanel();
      panel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
      panel.setWidth("100%");

      msgPanel = new RMessagePanel();
      panel.add(msgPanel);

      table = new FlexTable();
      table.setWidth("100%");
      table.getColumnFormatter().setWidth(0, "100%");

      permField = new RComboBox<String>(FeeMessages.M.permGroup());
      permField.setEditable(false);
      permField.setMaxDropdownRowCount(10);
      permField.setWidth("60%");
      permField.removeNullOption();
      table.setWidget(0, 0, permField);

      box = new RCaptionBox();
      box.setWidth("100%");
      box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
      box.setCaption(FeeMessages.M.permGroup());
      box.add(table);

      panel.add(box);

      return panel;
    }

    public String getPermGroupId() {
      return permField.getValue();
    }

    public void setPermGroupId(String permGroupId) {
      permField.setValue(permGroupId);
    }

    public String getPermGroupTitle() {
      return permField.getText();
    }

    @Override
    protected boolean validateBeforeImport() {
      msgPanel.clearMessages();
      boolean permEnable = EPFee.getInstance().isPermEnabled();
      if (permEnable) {
        if (permField.getValue() == null) {
          msgPanel.clearMessages();
          msgPanel.putErrorMessage("授权组：不能为空。", permField);
        }
        return permField.getValue() != null;
      } else {
        return true;
      }
    }

    @Override
    public void onShow() {
      super.onShow();
      msgPanel.clearMessages();
      refreshPanel();
    }

    public void refreshPanel() {
      permField.clearValue();
      permField.clearOptions();
      boolean permEnable = EPFee.getInstance().isPermEnabled();
      box.setVisible(permEnable);
      if (box.isVisible()) {
        List<Pair<String, String>> userGroups = EPFee.getInstance().getUserGroupList();
        if (userGroups != null) {
          for (Pair<String, String> group : userGroups) {
            permField.addOption(group.getLeft(), group.getRight());
          }
          if (userGroups.size() > 0) {
            permField.setValue(userGroups.get(0).getLeft());
          }
        }
      }
    }
  }

}
