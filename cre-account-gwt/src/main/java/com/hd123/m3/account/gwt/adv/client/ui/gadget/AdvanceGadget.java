/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.ui.gadget;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.adv.client.EPAdvance;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvance;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceLogFilter;
import com.hd123.m3.account.gwt.adv.client.ui.AdvanceLogPage;
import com.hd123.m3.account.gwt.adv.intf.client.AdvanceMessages;
import com.hd123.m3.account.gwt.adv.intf.client.AdvanceUrlParams;
import com.hd123.m3.account.gwt.adv.intf.client.dd.PAdvanceDef;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.PayDepositUrlParams;
import com.hd123.m3.account.gwt.deposit.payment.intf.client.perm.PayDepositPermDef;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.RecDepositUrlParams;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.perm.RecDepositPermDef;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.PayDepositMoveUrlParams;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.perm.PayDepositMovePermDef;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.perm.RecDepositMovePermDef;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.PayDepositRepaymentUrlParams;
import com.hd123.m3.account.gwt.depositrepayment.payment.intf.client.perm.PayDepositRepaymentPermDef;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.RecDepositRepaymentUrlParams;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.perm.RecDepositRepaymentPermDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RGridSummaryProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuSeparator;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author zhuhairui
 * 
 */
public class AdvanceGadget extends RCaptionBox {

  private EPAdvance ep = EPAdvance.getInstance();

  public void refresh() {
    grid.refresh();
  }

  public void setAdvancesforProvider(List<BAdvance> advances) {
    provider.setAdvances(advances);
  }

  public AdvanceGadget(String caption, boolean isRec) {
    super();
    this.isRec = isRec;

    setCaption(caption);
    setContentSpacing(0);
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);
    setContent(drawContentPanel());

  }

  private boolean isRec = true;

  private RGrid grid;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef contractBillNumCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef totalCol;
  private GridDataProvider provider = new GridDataProvider();

  private RMenuItem depositItem;
  private RMenuItem depositRepaymentItem;
  private RMenuItem depositMoveItem;

  private Widget drawContentPanel() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    panel.add(drawGrid());
    return panel;
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(false);
    grid.addClickHandler(new Handler_grid());
    grid.setCustomSummaryProvider(new SummaryProvider());
    grid.setProvider(provider);

    subjectCodeCol = new RGridColumnDef(PAdvanceDef.subject_code);
    subjectCodeCol.setWidth("100px");
    subjectCodeCol.setRendererFactory(new RHotItemRendererFactory(drawLineMenu(),
        new HyperlinkRendererFactory("60px")));
    subjectCodeCol.setSummaryMethod(RGridColumnDef.SUMMARY_METHOD_CUSTOM);
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PAdvanceDef.subject_name);
    subjectNameCol.setWidth("130px");
    grid.addColumnDef(subjectNameCol);

    accountUnitCol = new RGridColumnDef(PAdvanceDef.accountUnit);
    accountUnitCol.setWidth("130px");
    grid.addColumnDef(accountUnitCol);

    counterpartCol = new RGridColumnDef(PAdvanceDef.counterpart);
    counterpartCol.setWidth("140px");
    grid.addColumnDef(counterpartCol);

    contractBillNumCol = new RGridColumnDef(PAdvanceDef.bill_code);
    contractBillNumCol.setWidth("120px");
    grid.addColumnDef(contractBillNumCol);

    contractNameCol = new RGridColumnDef(PAdvanceDef.bill_name);
    contractNameCol.setWidth("100px");
    grid.addColumnDef(contractNameCol);

    totalCol = new RGridColumnDef(PAdvanceDef.total);
    totalCol.setWidth("100px");
    totalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setSummaryMethod(RGridColumnDef.SUMMARY_METHOD_SUM);
    grid.addColumnDef(totalCol);

    grid.setAllColumnsOverflowEllipsis(true);
    grid.rebuild();

    return grid;
  }

  private RPopupMenu drawLineMenu() {
    RPopupMenu lineMenu = new RPopupMenu();
    lineMenu.addOpenHandler(new Handler_lineMenu());

    depositItem = new RMenuItem(new RActionFacade(AdvanceMessages.M.advanceItem()),
        new Handler_depositItem());
    depositItem.setHotKey(null);
    lineMenu.addItem(depositItem);

    depositRepaymentItem = new RMenuItem(new RActionFacade(AdvanceMessages.M.withdrawalItem()),
        new Handler_depositRepaymentItem());
    depositRepaymentItem.setHotKey(null);
    lineMenu.addItem(depositRepaymentItem);

    depositMoveItem = new RMenuItem(new RActionFacade(AdvanceMessages.M.moveItem()),
        new Handler_depositMoveItem());
    depositMoveItem.setHotKey(null);
    lineMenu.addItem(depositMoveItem);

    lineMenu.addItem(new RMenuSeparator());

    RMenuItem viewLogItem = new RMenuItem(new RActionFacade(AdvanceMessages.M.viewLog()),
        new Handler_viewLogItem());
    viewLogItem.setHotKey(null);
    lineMenu.addItem(viewLogItem);

    return lineMenu;
  }

  private class Handler_lineMenu implements OpenHandler<RPopupMenu> {
    @Override
    public void onOpen(OpenEvent<RPopupMenu> event) {
      if (isRec) {
        depositItem.setEnabled(ep.isPermitted(RecDepositPermDef.CREATE));
        depositRepaymentItem.setEnabled(ep.isPermitted(RecDepositRepaymentPermDef.CREATE));
        depositMoveItem.setEnabled(ep.isPermitted(RecDepositMovePermDef.CREATE));
      } else {
        depositItem.setEnabled(ep.isPermitted(PayDepositPermDef.CREATE));
        depositRepaymentItem.setEnabled(ep.isPermitted(PayDepositRepaymentPermDef.CREATE));
        depositMoveItem.setEnabled(ep.isPermitted(PayDepositMovePermDef.CREATE));
      }
    }
  }

  private class SummaryProvider implements RGridSummaryProvider {
    public String getSummary(int col) {
      if (col == 0)
        return AdvanceMessages.M.summary();
      else
        return "";
    }
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      RGridColumnDef colDef = cell.getColumnDef();
      int row = cell.getRow();

      if (colDef.equals(subjectCodeCol)) {
        doClickCode(row);
      }
    }
  }

  private void doClickCode(int row) {
    String subjectCode = (String) provider.getData(row, subjectCodeCol.getIndex());
    GwtUrl url = SubjectUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
    url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_CODE, subjectCode);
    try {
      RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
    } catch (Exception e) {
      String msg = AdvanceMessages.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
      RMsgBox.showError(msg, e);
    }
  }

  private class Handler_depositItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BAdvance advance = provider.getAdvances().get(hotItem.getGridRow());

      GwtUrl url;
      if (isRec) {
        url = RecDepositUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, RecDepositUrlParams.Create.START_NODE);
        url.getQuery().set(RecDepositUrlParams.Create.PN_ADVANCE_UUID, advance.getUuid());
      } else {
        url = PayDepositUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, PayDepositUrlParams.Create.START_NODE);
        url.getQuery().set(PayDepositUrlParams.Create.PN_ADVANCE_UUID, advance.getUuid());
      }

      try {
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
      } catch (Exception e) {
        String msg = "无法导航到指定模块(" + url + ")。";
        RMsgBox.showError(msg, e);
      }
    }
  }

  private class Handler_depositRepaymentItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BAdvance advance = provider.getAdvances().get(hotItem.getGridRow());
      GwtUrl url;
      if (isRec) {
        url = RecDepositRepaymentUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, RecDepositRepaymentUrlParams.Create.START_NODE);
        url.getQuery().set(RecDepositRepaymentUrlParams.Create.PN_ADVANCE_UUID, advance.getUuid());
      } else {
        url = PayDepositRepaymentUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, PayDepositRepaymentUrlParams.Create.START_NODE);
        url.getQuery().set(PayDepositRepaymentUrlParams.Create.PN_ADVANCE_UUID, advance.getUuid());
      }
      try {
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
      } catch (Exception e) {
        String msg = "无法导航到指定模块(" + url + ")。";
        RMsgBox.showError(msg, e);
      }
    }
  }

  private class Handler_depositMoveItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BAdvance advance = provider.getAdvances().get(hotItem.getGridRow());
      GwtUrl url;
      if (isRec) {
        url = RecDepositMoveUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, RecDepositMoveUrlParams.Create.START_NODE);
        url.getQuery().set(RecDepositMoveUrlParams.Create.PN_ADVANCE_UUID, advance.getUuid());
      } else {
        url = PayDepositMoveUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, PayDepositMoveUrlParams.Create.START_NODE);
        url.getQuery().set(PayDepositMoveUrlParams.Create.PN_ADVANCE_UUID, advance.getUuid());
      }
      try {
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
      } catch (Exception e) {
        String msg = "无法导航到指定模块(" + url + ")。";
        RMsgBox.showError(msg, e);
      }
    }
  }

  private class Handler_viewLogItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      JumpParameters params = new JumpParameters(AdvanceLogPage.START_NODE);

      // 项目代码
      String accountUnitCodeName = (String) grid.getProvider().getData(hotItem.getGridRow(),
          accountUnitCol.getIndex());
      if (accountUnitCodeName != null) {
        int acc = accountUnitCodeName.indexOf("]");
        String accountUnitCode = accountUnitCodeName.substring(1, acc);
        params.getUrlRef().set(AdvanceUrlParams.LogFilter.KEY_ACCOUNTUNIT, accountUnitCode);
      }

      // 商户代码
      BAdvance bAdvance = provider.getAdvances().get(hotItem.getGridRow());
      BCounterpart counterpartUnit = bAdvance.getCounterpart();
      params.getUrlRef().set(AdvanceUrlParams.LogFilter.KEY_COUNTERPARTUNIT,
          counterpartUnit.getCode());
      params.getUrlRef().set(AdvanceUrlParams.LogFilter.KEY_COUNTERPARTTYPE,
          counterpartUnit.getCounterpartType());

      // 科目代码
      String subjectCode = (String) grid.getProvider().getData(hotItem.getGridRow(),
          subjectCodeCol.getIndex());
      params.getUrlRef().set(AdvanceUrlParams.LogFilter.KEY_SUBJECT, subjectCode);
      params.getUrlRef().set(AdvanceUrlParams.Log.KEY_IS_REC, isRec ? "true" : "false");
      new AdvanceLogFilter().encodeUrlParams(params.getUrlRef());
      EPAdvance.getInstance().jump(params);
    }
  }

  private class GridDataProvider implements RGridDataProvider {

    private List<BAdvance> advances;

    public List<BAdvance> getAdvances() {
      return advances;
    }

    public void setAdvances(List<BAdvance> advances) {
      this.advances = advances;
    }

    @Override
    public int getRowCount() {
      return advances == null ? 0 : advances.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (advances == null)
        return null;

      BAdvance rowData = advances.get(row);
      if (col == subjectCodeCol.getIndex())
        return rowData.getSubject() == null ? null : rowData.getSubject().getCode();
      else if (col == subjectNameCol.getIndex())
        return rowData.getSubject() == null ? null : rowData.getSubject().getName();
      else if (col == accountUnitCol.getIndex())
        return rowData.getAccountUnit() == null ? null : rowData.getAccountUnit().getNameCode();
      else if (col == contractBillNumCol.getIndex())
        return rowData.getContract() == null ? null : rowData.getContract().getCode();
      else if (col == contractNameCol.getIndex())
        return rowData.getContract() == null ? null : rowData.getContract().getName();
      else if (col == totalCol.getIndex())
        return rowData.getTotal() == null ? Double.valueOf(0) : Double.valueOf(rowData.getTotal()
            .doubleValue());
      else if (col == counterpartCol.getIndex())
        return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
            ep.getCounterpartTypeMap());
      return null;
    }
  }

}
