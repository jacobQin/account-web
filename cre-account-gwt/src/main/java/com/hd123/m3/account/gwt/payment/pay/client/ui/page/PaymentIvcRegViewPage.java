/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PaymentIvcRegViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月24日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.page;

import java.util.Date;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentService;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams.IvcRegView;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RViewDateField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author lixiaohong
 * 
 */
public class PaymentIvcRegViewPage extends BaseContentPage implements IvcRegView {

  private OtherFieldDef dateRangeDef = new OtherFieldDef("dateRange", CommonMessages.M.dateRange(),
      true);
  private StringFieldDef ivcCodeDef = new StringFieldDef("ivcCode", PaymentMessages.M.ivcCode(),
      true, 0, 32);
  private StringFieldDef ivcNumberDef = new StringFieldDef("ivcNumber",
      PaymentMessages.M.ivcNumber(), true, 0, 32);
  private BigDecimalFieldDef ivcTotalDef = new BigDecimalFieldDef("ivcTotal",
      PaymentMessages.M.ivcTotal(), true, null, true, null, true, 2);
  private DateFieldDef ivcDateDef = new DateFieldDef("ivcDate", PaymentMessages.M.ivcDate());
  private StringFieldDef ivcTypeDef = new StringFieldDef("ivcType", PaymentMessages.M.ivcType(),
      true, 0, 20);

  private static PaymentIvcRegViewPage instance;

  public static PaymentIvcRegViewPage getInstance() {
    if (instance == null)
      instance = new PaymentIvcRegViewPage();
    return instance;
  }

  public PaymentIvcRegViewPage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private EPPayment ep = EPPayment.getInstance();
  private BPayment bill;
  private RAction cancelAction;
  private RToolbarButton cancelButton;

  private RViewStringField billNumberField;
  private RViewStringField storeField;
  private RViewStringField counterpartField;
  private RViewDateField paymentDateField;

  private Map<String, String> ivcTypeMap = ep.getInvoiceTypeMap();

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef ivcCodeCol;
  private RGridColumnDef ivcNumberCol;
  private RGridColumnDef ivcTotalCol;
  private RGridColumnDef ivcDateCol;
  private RGridColumnDef ivcTypeCol;

  private HTML resultTotalHtml;

  private RTextArea remarkField;

  private Handler_click clickHandler = new Handler_click();

  private void drawToolbar() {
    cancelAction = new RAction(PaymentMessages.M.returnButton(), clickHandler);
    cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawGeneral());
    panel.add(drawRegiste());
    panel.add(drawRemark());
  }

  private Widget drawGeneral() {
    VerticalPanel vp = new VerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawBasicInfo());

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PaymentMessages.M.generalInfo());
    box.setWidth("100%");
    box.setContent(vp);
    return box;
  }

  private Widget drawBasicInfo() {
    VerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(5);

    RForm form = new RForm(1);
    form.setWidth("100%");

    billNumberField = new RViewStringField(PaymentMessages.M.paymentNumber());
    billNumberField.addStyleName(RTextStyles.STYLE_BOLD);
    form.addField(billNumberField);

    storeField = new RViewStringField(PaymentMessages.M.store());
    form.addField(storeField);

    counterpartField = new RViewStringField();
    counterpartField
        .setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
    form.addField(counterpartField);

    paymentDateField = new RViewDateField(PaymentMessages.M.paymentDate());
    form.addField(paymentDateField);

    panel.add(form);

    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.setCaption(PaymentMessages.M.basicInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(panel);
    return box;
  }

  private Widget drawRegiste() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    resultTotalHtml = new HTML(PaymentMessages.M.resultTotal(0));

    lineNumberCol = new RGridColumnDef();
    lineNumberCol.setCaption(PPaymentAccountLineDef.constants.lineNumber());
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_subject);
    subjectCol.setWidth("150px");
    grid.addColumnDef(subjectCol);

    dateRangeCol = new RGridColumnDef(dateRangeDef);
    dateRangeCol.setWidth("200px");
    grid.addColumnDef(dateRangeCol);

    ivcCodeCol = new RGridColumnDef(ivcCodeDef);
    ivcCodeCol.setWidth("100px");
    grid.addColumnDef(ivcCodeCol);

    ivcNumberCol = new RGridColumnDef(ivcNumberDef);
    ivcNumberCol.setWidth("100px");
    grid.addColumnDef(ivcNumberCol);

    ivcTotalCol = new RGridColumnDef(ivcTotalDef);
    ivcTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    ivcTotalCol.setWidth("100px");
    grid.addColumnDef(ivcTotalCol);

    ivcDateCol = new RGridColumnDef(ivcDateDef);
    ivcDateCol.setWidth("150px");
    grid.addColumnDef(ivcDateCol);

    ivcTypeCol = new RGridColumnDef(ivcTypeDef);
    ivcTypeCol.setWidth("100px");
    grid.addColumnDef(ivcTypeCol);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PaymentMessages.M.registeInfo());
    box.setWidth("100%");
    box.getCaptionBar().addButton(resultTotalHtml);
    box.setContent(grid);
    return box;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PPaymentDef.remark);
    remarkField.setReadOnly(true);
    remarkField.setSelectAllOnFocus(true);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PPaymentDef.constants.remark());
    box.setWidth("100%");
    box.setContent(remarkField);
    box.getCaptionBar().setShowCollapse(true);
    return box;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    clearValue();
  }

  @Override
  public void onShow(final JumpParameters params) {
    super.onShow(params);
    if (checkIn() == false) {
      return;
    }
    final String uuid = params.getUrlRef().get(PaymentIvcRegViewPage.PN_UUID);
    decodeParams(uuid, new Command() {
      @Override
      public void execute() {
        if (bill == null || bill.getAccountLines().isEmpty()) {
          RMsgBox.show(PaymentMessages.M.noRegisteAccounts(), new RMsgBox.Callback() {
            @Override
            public void onClosed(ButtonConfig clickedButton) {
              ep.jumpToViewPage(uuid);
            }
          });
        } else {
          refresh();
        }
      }
    });
    refreshTitle();
  }

  private void decodeParams(String uuid, final Command cmd) {
    PaymentService.Locator.getService().loadRegiste(uuid, new AsyncCallback<BPayment>() {
      @Override
      public void onFailure(Throwable caught) {
        String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.find(),
            PaymentMessages.M.payment());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BPayment result) {
        bill = result;
        cmd.execute();
      }
    });
  }

  private boolean checkIn() {
    if (ep.isPermitted(PaymentPermDef.IVCREGISTE) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refresh() {
    assert bill != null;
    getToolbar().rebuild();
    refreshBasicInfo();
    grid.refresh();
    refreshRemark();
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(PaymentMessages.M.payment());
    ep.getTitleBar().appendAttributeText(PaymentMessages.M.ivcRegiste());
  }

  private void refreshBasicInfo() {
    billNumberField.setValue(bill.getBillNumber());
    storeField.setValue(bill.getStore().toFriendlyStr());
    counterpartField.setValue(bill.getCounterpart().toFriendlyStr(ep.getCounterpartTypeMap()));
    paymentDateField.setValue(bill.getPaymentDate());
  }

  private void refreshRemark() {
    remarkField.setValue(bill.getIvcRemark());
  }

  private void clearValue() {
    billNumberField.clearValue();
    storeField.clearValue();
    counterpartField.clearValue();
    paymentDateField.clearValue();
    remarkField.clearValue();
    bill = null;
    grid.refresh();
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(cancelAction)) {
        RHistory.back();
      }
    }
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return bill == null || bill.getAccountLines() == null ? 0 : bill.getAccountLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (bill == null || bill.getAccountLines() == null || bill.getAccountLines().isEmpty()
          || row < 0 || row >= bill.getAccountLines().size())
        return null;

      resultTotalHtml.setText(PaymentMessages.M.resultTotal(bill.getAccountLines().size()));

      BPaymentAccountLine rowData = bill.getAccountLines().get(row);
      if (col == (lineNumberCol.getIndex())) {
        return row + 1;
      } else if (col == (subjectCol.getIndex())) {
        return rowData.getAcc1().getSubject().toFriendlyStr();
      } else if (col == (dateRangeCol.getIndex())) {
        return buildDateRange(rowData.getAcc1().getBeginTime(), rowData.getAcc1().getEndTime());
      } else if (col == (ivcCodeCol.getIndex())) {
        return rowData.getIvcCode();
      } else if (col == (ivcNumberCol.getIndex())) {
        return rowData.getIvcNumber();
      } else if (col == (ivcTotalCol.getIndex())) {
        return rowData.getIvcTotal() == null || rowData.getIvcTotal().getTotal() == null ? null
            : (rowData.getIvcTotal().getTotal() == null ? null : M3Format.fmt_money.format(rowData
                .getIvcTotal().getTotal()));
      } else if (col == (ivcDateCol.getIndex())) {
        return rowData.getIvcDate() == null ? null : M3Format.fmt_yMd.format(rowData.getIvcDate());
      } else if (col == (ivcTypeCol.getIndex())) {
        return rowData.getIvcType() == null ? null : ivcTypeMap.get(rowData.getIvcType());
      }
      return null;
    }
  }

  private String buildDateRange(Date beginDate, Date endDate) {
    if (beginDate == null && endDate == null)
      return null;
    StringBuffer sb = new StringBuffer();
    if (beginDate != null)
      sb.append(M3Format.fmt_yMd.format(beginDate));
    sb.append("~");
    if (endDate != null)
      sb.append(M3Format.fmt_yMd.format(endDate));
    return sb.toString();
  }
}
