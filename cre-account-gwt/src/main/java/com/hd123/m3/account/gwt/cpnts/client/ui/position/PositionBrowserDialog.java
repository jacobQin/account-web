/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	PositionBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-5-10 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.mres.client.dd.rt.CBasicState;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author chenrizhang
 * 
 */
public class PositionBrowserDialog extends RBrowserDialog<SPosition> {
  private RGridColumnDef codeCol;
  private RGridColumnDef nameCol;
  private RGridColumnDef ownerCol;

  private boolean storeLimit = false;
  private List<String> states = new ArrayList<String>();
  private PositionFilter filter = new PositionFilter();

  private PositionSubType positionType;

  private PageDataProvider provider;
  private boolean showState = false;

  public PositionBrowserDialog(String... states) {
    super();
    drawSelf();
    refreshTypeCaption();
    setStates(states);
  }

  private void drawSelf() {
    codeCol = new RGridColumnDef();
    codeCol.setName(PositionFilter.ORDER_BY_FIELD_CODE);
    codeCol.setCaption(M.code());
    codeCol.setSortable(true);
    nameCol = new RGridColumnDef();
    nameCol.setCaption(M.name());
    nameCol.setName(PositionFilter.ORDER_BY_FIELD_NAME);
    nameCol.setSortable(true);
    ownerCol = new RGridColumnDef();
    ownerCol.setCaption(M.owner());
    ownerCol.setName(PositionFilter.ORDER_BY_FIELD_STORE);
    ownerCol.setSortable(true);
    setColumnDefs(codeCol, nameCol, ownerCol);
    getGrid().setAllColumnsOverflowEllipsis(true);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
  }

  /**
   * 设置项目限制，当值为ture时，必须给出指定的项目uuid。否则返回空。<br>
   * 默认为false。
   * 
   */
  public void setStoreLimit(boolean storeLimit) {
    this.storeLimit = storeLimit;
    ownerCol.setVisible(storeLimit == false);
    getGrid().rebuild();
    provider.filterRebuild();
  }

  /**
   * 设置铺位所属项目。<br>
   * 调用该方法会自动将项目限制置为true。
   * 
   * @param storeUuid
   */
  public void setStoreUuid(String storeUuid) {
    setStoreLimit(true);
    filter.setStoreUuid(storeUuid);
  }

  /**
   * 设置铺位类型，会导致铺位类型的搜索条件隐藏。
   * 
   * @param positionType
   */
  public void setPositionType(BPositionType positionType) {
    setPositionType(positionType == null ? null : new PositionSubType(positionType.name()));
  }

  /**
   * 设置铺位类型，会导致铺位类型的搜索条件隐藏。
   * 
   * @param positionType
   */
  public void setPositionType(PositionSubType positionType) {
    this.positionType = positionType;
    refreshTypeCaption();
    provider.filterRebuild();
  }

  public void setStates(String... states) {
    this.states = states == null ? new ArrayList<String>() : Arrays.asList(states);
    filter.setStates(this.states);
    provider.filterRebuild();
  }

  private void refreshTypeCaption() {
    String caption = positionType == null ? M.defualtCaption() : positionType.toFriendlyStr();
    setCaption(CommonsMessages.M.seleteData(caption));
  }

  public void refreshPositionSubTypes(boolean includeSubType) {
    provider.refreshPositionSubTypes(includeSubType);
  }

  private class PageDataProvider implements RPageDataProvider<SPosition>, RFilterCallback {
    private RForm form;
    private RTextBox codeField;
    private RTextBox nameField;
    private RComboBox<PositionSubType> positionTypeField;
    private RComboBox<String> stateField;
    private boolean includeSubType;

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<SPosition>> callback) {
      if (storeLimit && StringUtil.isNullOrBlank(filter.getStoreUuid())) {
        callback.onSuccess(new RPageData<SPosition>());
        return;
      }
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(codeCol.getName(), OrderDir.asc);
      }

      AccountCpntsService.Locator.getService().queryPositions(filter, callback);
    }

    @Override
    public Object getData(int row, int col, SPosition rowData, List<SPosition> pageData) {
      if (col == codeCol.getIndex()) {
        return rowData.getCode();
      }
      if (col == nameCol.getIndex()) {
        return rowData.getName();
      }
      if (col == ownerCol.getIndex()) {
        return BUCN.toFriendlyStr(rowData.getStore(), null);
      }
      return null;
    }

    public void filterRebuild() {
      if (positionTypeField != null) {
        positionTypeField.setVisible(positionType == null);
      }
      if (form != null) {
        form.rebuild();
      }
    }

    @Override
    public void buildConditions(RForm form) {
      this.form = form;
      form.setWidth("400px");

      codeField = new RTextBox(M.code());
      form.addField(codeField);

      nameField = new RTextBox(M.name());
      form.addField(nameField);

      positionTypeField = new RComboBox<PositionSubType>(M.positionType());
      positionTypeField.setRequired(false);
      positionTypeField.setEditable(false);
      positionTypeField.setNullOptionText(CommonsMessages.M.all());
      refreshPositionSubTypes(includeSubType);
      form.addField(positionTypeField);
      
      stateField = new RComboBox<String>(M.state());
      stateField.setRequired(false);
      stateField.setEditable(false);
      /*stateField.addOption(CBasicState.using, BasicStateDef.constants.using());
      stateField.addOption(CBasicState.deleted, BasicStateDef.constants.deleted());*/
      stateField.addOption(BPositionState.using.name(), BPositionState.using.getCaption());
      stateField.addOption(BPositionState.deleted.name(), BPositionState.deleted.getCaption());
      stateField.addOption(BPositionState.splitted.name(), BPositionState.splitted.getCaption());
      stateField.addOption(BPositionState.merged.name(), BPositionState.merged.getCaption());
      stateField.setNullOptionText(CommonsMessages.M.all());
      stateField.setValue(CBasicState.using);
      stateField.setVisible(showState);
      form.addField(stateField);
      filterRebuild();
    }

    private void refreshPositionSubTypes(boolean includeSubType) {
      this.includeSubType = includeSubType;
      if (form == null)
        return;
      if (includeSubType == false) {
        positionTypeField.addOption(new PositionSubType(BPositionType.shoppe.name()),
            BPositionType.shoppe.getCaption());
        positionTypeField.addOption(new PositionSubType(BPositionType.booth.name()),
            BPositionType.booth.getCaption());
        positionTypeField.addOption(new PositionSubType(BPositionType.adPlace.name()),
            BPositionType.adPlace.getCaption());
        return;
      }

      AccountCpntsService.Locator.getService().getPositionSubTyoes(
          new RBAsyncCallback2<Map<String, List<String>>>() {

            @Override
            public void onException(Throwable caught) {
              RMsgBox.showError(caught);
            }

            @Override
            public void onSuccess(Map<String, List<String>> positionSubTyoes) {

              positionTypeField.addOption(new PositionSubType(BPositionType.shoppe.name()),
                  BPositionType.shoppe.getCaption());
              if (positionSubTyoes.containsKey(BPositionType.shoppe.name())) {
                for (String subType : positionSubTyoes.get(BPositionType.shoppe.name())) {
                  PositionSubType positionSubType = new PositionSubType(
                      BPositionType.shoppe.name(), subType);
                  positionTypeField.addOption(positionSubType, positionSubType.toFriendlyStr());
                }
              }
              positionTypeField.addOption(new PositionSubType(BPositionType.booth.name()),
                  BPositionType.booth.getCaption());
              if (positionSubTyoes.containsKey(BPositionType.booth.name())) {
                for (String subType : positionSubTyoes.get(BPositionType.booth.name())) {
                  PositionSubType positionSubType = new PositionSubType(BPositionType.booth.name(),
                      subType);
                  positionTypeField.addOption(positionSubType, positionSubType.toFriendlyStr());
                }
              }
              positionTypeField.addOption(new PositionSubType(BPositionType.adPlace.name()),
                  BPositionType.adPlace.getCaption());
              if (positionSubTyoes.containsKey(BPositionType.adPlace.name())) {
                for (String subType : positionSubTyoes.get(BPositionType.adPlace.name())) {
                  PositionSubType positionSubType = new PositionSubType(BPositionType.adPlace
                      .name(), subType);
                  positionTypeField.addOption(positionSubType, positionSubType.toFriendlyStr());
                }
              }
            }
          });
    }

    @Override
    public void clearConditions() {
      if (codeField != null) {
        codeField.clearValue();
      }
      if (nameField != null) {
        nameField.clearValue();
      }
      if (positionTypeField != null) {
        positionTypeField.clearValue();
      }
      if (stateField != null && stateField.isVisible()) {
        stateField.clearValue();
      }
    }

    @Override
    public void onQuery() {
      if (codeField != null) {
        filter.setCode(codeField.getValue());
      }
      if (nameField != null) {
        filter.setName(nameField.getValue());
      }
      if (positionType != null) {
        filter.setPositionType(positionType);
      } else if (positionTypeField != null && positionTypeField.isVisible()) {
        filter.setPositionType(positionTypeField.getValue());
      }
      if (stateField != null && stateField.isVisible()) {
        List<String> value = new ArrayList<String>();
        if (stateField.getValue() != null) {
          value.add(stateField.getValue());
        }
        filter.setStates(value);
      }
    }
  }
  
  public void setShowState(boolean showState) {
    this.showState = showState;
  }
  
  @Override
  public void show() {
    if (provider.stateField != null) {
      provider.stateField.setVisible(showState);
    }
    super.show();
  }
  
  @Override
  public void center() {
    if (provider.stateField != null) {
      provider.stateField.setVisible(showState);
    }
    super.center();
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("位置")
    String defualtCaption();

    @DefaultMessage("代码")
    String code();

    @DefaultMessage("名称")
    String name();

    @DefaultMessage("所属项目")
    String owner();

    @DefaultMessage("位置类型")
    String positionType();

    @DefaultMessage("状态")
    String state();
  }
}
