package com.hd123.m3.account.gwt.cpnts.client.ui.position;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * @author chenrizhang
 * 
 */
public class PositionUCNBox extends RUCNBox<SPosition> implements RUCNQueryByCodeCommand {
  private PositionFilter filter = new PositionFilter();

  private boolean storeLimit = false;
  private PositionBrowserDialog dialog;

  public PositionUCNBox(String... states) {
    super();

    dialog = new PositionBrowserDialog();
    setBrowser(dialog);

    refreshTypeCaption();
    setStates(states);

    setQueryByCodeCommand(this);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    getBrowser().setCaption(CommonsMessages.M.seleteData(caption));
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    clearConditions();
  }

  @Override
  public void query(String code, AsyncCallback callback) {
    if (storeLimit && StringUtil.isNullOrBlank(filter.getStoreUuid())) {
      callback.onSuccess(null);
    } else {
      filter.setCode(code);
      AccountCpntsService.Locator.getService().getPositionByCode(filter, callback);
    }
  }

  /**
   * 设置项目限制，当值为ture时，必须给出指定的项目uuid。否则返回空。
   * 
   */
  public void setStoreLimit(boolean storeLimit) {
    this.storeLimit = storeLimit;
    dialog.setStoreLimit(storeLimit);
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
    dialog.setStoreUuid(storeUuid);
  }

  public void setStates(String... states) {
    filter.setStates(states == null ? new ArrayList<String>() : Arrays.asList(states));
    dialog.setStates(states);
  }
  
  public void setShowState(boolean showState){
    dialog.setShowState(showState);
  }

  public void refreshPositionSubTypes(boolean includeSubType) {
    dialog.refreshPositionSubTypes(includeSubType);
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
    filter.setPositionType(positionType);
    dialog.setPositionType(positionType);
    refreshTypeCaption();
  }

  private void refreshTypeCaption() {
    String caption = filter.getPositionType() == null ? M.defualtCaption() : filter
        .getPositionType().toFriendlyStr();
    setCaption(caption);
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("位置")
    String defualtCaption();

  }
}
