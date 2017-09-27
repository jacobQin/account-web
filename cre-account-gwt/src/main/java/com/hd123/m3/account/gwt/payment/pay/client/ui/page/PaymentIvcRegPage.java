/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PaymentIvcRegPage.java
 * 模块说明：	
 * 修改历史：
 * 2015-9-7 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.ivc.common.client.StartNumberBrowserBox;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BStockState;
import com.hd123.m3.account.gwt.payment.commons.client.CommonMessages;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLogger;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.account.gwt.payment.pay.client.rpc.PaymentService;
import com.hd123.m3.account.gwt.payment.pay.intf.client.PaymentUrlParams.IvcReg;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RContainerValidator;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewDateField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author liuguilin
 * 
 */
public class PaymentIvcRegPage extends BaseContentPage implements IvcReg {

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

  private StringFieldDef ivcCodeViewDef = new StringFieldDef("ivcCodeView",
      PaymentMessages.M.ivcCode(), true, 0, 32);
  private StringFieldDef ivcNumberBrowserDef = new StringFieldDef("ivcNumberBrowser",
      PaymentMessages.M.ivcNumber(), true, 0, 32);
  private StringFieldDef ivcTypeViewDef = new StringFieldDef("ivcTypeView",
      PaymentMessages.M.ivcType(), true, 0, 20);

  private static PaymentIvcRegPage instance;
  private boolean checkEmpty;

  public static PaymentIvcRegPage getInstance() {
    if (instance == null)
      instance = new PaymentIvcRegPage();
    return instance;
  }

  public PaymentIvcRegPage() {
    super();
    drawToolbar();
    drawSelf();
  }

  private EPPayment ep = EPPayment.getInstance();
  private BPayment bill;
  private VerticalPanel root;

  private RAction saveAction;
  private RAction saveRegiterAction;
  private RAction cancelAction;

  private RViewStringField billNumberField;
  private RViewStringField storeField;
  private RViewStringField counterpartField;
  private RViewDateField paymentDateField;

  private RForm invoiceForm;
  private Map<String, String> ivcTypeMap;
  private RComboBox<String> ivcTypeBox;
  private RDateBox ivcDateBox;
  private RTextBox ivcCodeBox;
  private RTextBox ivcNumberBox;
  private RViewStringField ivcTypeViewBox;
  private RViewStringField ivcCodeViewBox;
  private StartNumberBrowserBox ivcNumberBrowserBox;

  private EditGrid<BPaymentAccountLine> editGrid;
  private CellRendererFactory rendererFactory;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef ivcCodeCol;
  private RGridColumnDef ivcNumberCol;
  private RGridColumnDef ivcTotalCol;
  private RGridColumnDef ivcDateCol;
  private RGridColumnDef ivcTypeCol;
  private RGridColumnDef ivcCodeViewCol;
  private RGridColumnDef ivcNumberBrowserCol;
  private RGridColumnDef ivcTypeViewCol;

  private HTML resultTotalHtml;
  protected List<Message> messages = new ArrayList<Message>();

  private RTextArea remarkField;

  private BInvoiceRegConfig ivcRegConfig;
  private BigDecimal lowTotal;
  private BigDecimal highTotal;
  private BigDecimal lowTax;
  private BigDecimal highTax;

  private Handler_click clickHandler = new Handler_click();
  private Handler_change changeHandler = new Handler_change();

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, clickHandler);
    getToolbar().add(new RToolbarButton(saveAction));

    saveRegiterAction = new RAction(RActionFacade.SAVE, clickHandler);
    saveRegiterAction.setCaption(PaymentMessages.M.saveRegiter());
    getToolbar().add(new RToolbarButton(saveRegiterAction));

    cancelAction = new RAction(RActionFacade.CANCEL, clickHandler);
    getToolbar().add(new RToolbarButton(cancelAction));
  }

  private void drawSelf() {
    root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    root.add(drawGeneral());
    root.add(drawRegiste());
    root.add(drawRemark());
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
    mvp.add(0, drawInvoiceInfo());

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PaymentMessages.M.generalInfo());
    box.setWidth("100%");
    box.setEditing(true);
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

  private Widget drawInvoiceInfo() {
    VerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(5);

    invoiceForm = new RForm(1);
    invoiceForm.setWidth("100%");

    ivcTypeBox = new RComboBox<String>();
    ivcTypeBox.setCaption(PaymentMessages.M.ivcType());
    ivcTypeBox.setMaxDropdownRowCount(5);
    ivcTypeBox.setEditable(false);
    ivcTypeBox.setNullOptionText(PaymentMessages.M.nullOption());
    ivcTypeBox.addChangeHandler(changeHandler);
    invoiceForm.addField(ivcTypeBox);

    ivcTypeViewBox = new RViewStringField();
    ivcTypeViewBox.setCaption(PaymentMessages.M.ivcType());
    invoiceForm.addField(ivcTypeViewBox);

    ivcDateBox = new RDateBox(PaymentMessages.M.ivcDate());
    ivcDateBox.addChangeHandler(changeHandler);
    invoiceForm.addField(ivcDateBox);

    ivcCodeBox = new RTextBox(ivcCodeDef);
    ivcCodeBox.addChangeHandler(changeHandler);
    invoiceForm.addField(ivcCodeBox);

    ivcNumberBox = new RTextBox(ivcNumberDef);
    ivcNumberBox.addChangeHandler(changeHandler);
    invoiceForm.addField(ivcNumberBox);

    ivcCodeViewBox = new RViewStringField(ivcCodeViewDef);
    invoiceForm.add(ivcCodeViewBox);

    ivcNumberBrowserBox = new StartNumberBrowserBox(ivcNumberBrowserDef.getCaption(),
        BStockState.received.name(), true);
    ivcNumberBrowserBox.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        bill.setIvcNumber(ivcNumberBrowserBox.getValue());
        BInvoiceStock stock = ivcNumberBrowserBox.getRawValue();
        if (stock != null) {
          bill.setIvcCode(stock.getInvoiceCode());
          bill.setIvcType(stock.getInvoiceType());
          ivcCodeViewBox.setValue(stock.getInvoiceCode());
          ivcTypeViewBox.setValue(ivcTypeMap.get(stock.getInvoiceType()));
        } else {
          bill.setIvcCode(null);
          bill.setIvcType(null);
          ivcCodeViewBox.setValue(null);
          ivcTypeViewBox.setValue(ivcTypeMap.get(null));
        }
        for (BPaymentAccountLine line : bill.getAccountLines()) {
          line.setIvcNumber(bill.getIvcNumber());
          line.setIvcCode(bill.getIvcCode());
          line.setIvcType(bill.getIvcType());
        }
        editGrid.refresh();

      }
    });
    invoiceForm.add(ivcNumberBrowserBox);

    panel.add(invoiceForm);

    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.setCaption(PaymentMessages.M.invoiceInfo());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(panel);
    return box;
  }

  private Widget drawRegiste() {
    editGrid = new EditGrid<BPaymentAccountLine>();
    editGrid.setWidth("100%");
    editGrid.setShowCurrentRow(true);
    editGrid.setHoverRow(true);

    GridDataProvider provider = new GridDataProvider();
    editGrid.setProvider(provider);
    editGrid.setDataProducer(provider);
    resultTotalHtml = new HTML(PaymentMessages.M.resultTotal(0));

    rendererFactory = new CellRendererFactory();
    lineNumberCol = new RGridColumnDef();
    lineNumberCol.setCaption(PPaymentAccountLineDef.constants.lineNumber());
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    editGrid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentAccountLineDef.acc1_subject);
    subjectCol.setWidth("150px");
    editGrid.addColumnDef(subjectCol);

    dateRangeCol = new RGridColumnDef(dateRangeDef);
    dateRangeCol.setWidth("200px");
    editGrid.addColumnDef(dateRangeCol);

    ivcCodeCol = new RGridColumnDef(ivcCodeDef);
    ivcCodeCol.setRendererFactory(rendererFactory);
    ivcCodeCol.setWidth("100px");
    editGrid.addColumnDef(ivcCodeCol);

    ivcNumberCol = new RGridColumnDef(ivcNumberDef);
    ivcNumberCol.setRendererFactory(rendererFactory);
    ivcNumberCol.setWidth("100px");
    editGrid.addColumnDef(ivcNumberCol);

    ivcCodeViewCol = new RGridColumnDef(ivcCodeDef);
    ivcCodeViewCol.setWidth("100px");
    editGrid.addColumnDef(ivcCodeViewCol);

    ivcNumberBrowserCol = new RGridColumnDef(ivcNumberBrowserDef);
    ivcNumberBrowserCol.setRendererFactory(rendererFactory);
    ivcNumberBrowserCol.setWidth("100px");
    editGrid.addColumnDef(ivcNumberBrowserCol);

    ivcTotalCol = new RGridColumnDef(ivcTotalDef);
    ivcTotalCol.setRendererFactory(rendererFactory);
    ivcTotalCol.setWidth("100px");
    editGrid.addColumnDef(ivcTotalCol);

    ivcDateCol = new RGridColumnDef(ivcDateDef);
    ivcDateCol.setRendererFactory(rendererFactory);
    ivcDateCol.setWidth("150px");
    editGrid.addColumnDef(ivcDateCol);

    ivcTypeCol = new RGridColumnDef(ivcTypeDef);
    ivcTypeCol.setRendererFactory(rendererFactory);
    ivcTypeCol.setWidth("100px");
    editGrid.addColumnDef(ivcTypeCol);

    ivcTypeViewCol = new RGridColumnDef(ivcTypeViewDef);
    ivcTypeViewCol.setWidth("100px");
    editGrid.addColumnDef(ivcTypeViewCol);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PaymentMessages.M.registeInfo());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().addButton(resultTotalHtml);
    box.setContent(editGrid);
    return box;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PPaymentDef.remark);
    remarkField.setSelectAllOnFocus(true);
    remarkField.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        bill.setIvcRemark(remarkField.getValue());
      }
    });
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PPaymentDef.constants.remark());
    box.setEditing(true);
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
    final String uuid = params.getUrlRef().get(PaymentIvcRegPage.PN_UUID);
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
          initInvoiceType();
          refresh();
          ivcTypeBox.setFocus(true);
        }
      }
    });
    refreshTitle();
    loadIvcRegConfig();
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

  private void initInvoiceType() {
    ivcTypeMap = ep.getInvoiceTypeMap();
    ivcTypeBox.clearOptions();
    for (String key : ivcTypeMap.keySet()) {
      ivcTypeBox.addOption(key, ivcTypeMap.get(key));
    }
  }

  private void loadIvcRegConfig() {
    ivcRegConfig = ep.getInvoiceRegConfig();
    lowTotal = ivcRegConfig.getTotalDiffLo() == null ? BigDecimal.ZERO : ivcRegConfig
        .getTotalDiffLo();
    highTotal = ivcRegConfig.getTotalDiffHi() == null ? BigDecimal.ZERO : ivcRegConfig
        .getTotalDiffHi();
    lowTax = ivcRegConfig.getTaxDiffLo() == null ? BigDecimal.ZERO : ivcRegConfig.getTaxDiffLo();
    highTax = ivcRegConfig.getTaxDiffHi() == null ? BigDecimal.ZERO : ivcRegConfig.getTaxDiffHi();
  }

  private void refresh() {
    assert bill != null;

    refreshBasicInfo();
    refreshInvoiceInfo();
    refreshRegisteInfo();
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

  private void refreshInvoiceInfo() {
    ivcTypeBox.setValue(bill.getIvcType());
    ivcTypeViewBox.setValue(ivcTypeMap.get(bill.getIvcType()));
    ivcDateBox.setValue(bill.getIvcDate());
    ivcCodeBox.setValue(bill.getIvcCode());
    ivcNumberBox.setValue(bill.getIvcNumber());
    ivcCodeViewBox.setValue(bill.getIvcCode());
    ivcNumberBrowserBox.setValue(bill.getIvcNumber());
    ivcNumberBrowserBox.setStoreLimit(bill.getAccountUnit());

    ivcTypeBox.setVisible(!ep.getConfig().isUseInvoiceStock());
    ivcTypeViewBox.setVisible(ep.getConfig().isUseInvoiceStock());
    ivcCodeBox.setVisible(!ep.getConfig().isUseInvoiceStock());
    ivcNumberBox.setVisible(!ep.getConfig().isUseInvoiceStock());
    ivcCodeViewBox.setVisible(ep.getConfig().isUseInvoiceStock());
    ivcNumberBrowserBox.setVisible(ep.getConfig().isUseInvoiceStock());
    invoiceForm.clearValidResults();
    invoiceForm.rebuild();

  }

  private void refreshRegisteInfo() {
    ivcCodeCol.setVisible(!ep.getConfig().isUseInvoiceStock());
    ivcNumberCol.setVisible(!ep.getConfig().isUseInvoiceStock());
    ivcTypeCol.setVisible(!ep.getConfig().isUseInvoiceStock());
    ivcCodeViewCol.setVisible(ep.getConfig().isUseInvoiceStock());
    ivcNumberBrowserCol.setVisible(ep.getConfig().isUseInvoiceStock());
    ivcTypeViewCol.setVisible(ep.getConfig().isUseInvoiceStock());
    editGrid.setDefaultDataRowCount(0);
    editGrid.setValues(bill.getAccountLines());
    if (bill != null && bill.getAccountLines().isEmpty() == false) {
      editGrid.setCurrentRow(0);
    }
    editGrid.rebuild();
    editGrid.clearValidResults();
  }

  private void refreshRemark() {
    remarkField.setValue(bill.getIvcRemark());
  }

  private void clearValue() {
    billNumberField.clearValue();
    storeField.clearValue();
    counterpartField.clearValue();
    paymentDateField.clearValue();
    ivcTypeBox.clearValue();
    ivcDateBox.clearValue();
    ivcCodeBox.clearValue();
    ivcNumberBox.clearValue();
    ivcCodeViewBox.clearValue();
    ivcNumberBrowserBox.clearValue();
    ivcNumberBrowserBox.clearConditions();
    remarkField.clearValue();
    bill = null;
    editGrid.refresh();
  }

  private class Handler_change implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource().equals(ivcTypeBox)) {
        bill.setIvcType(ivcTypeBox.getValue());
        for (BPaymentAccountLine line : bill.getAccountLines()) {
          line.setIvcType(bill.getIvcType());
        }
        editGrid.refresh();
      } else if (event.getSource().equals(ivcDateBox)) {
        bill.setIvcDate(ivcDateBox.getValue());
        for (BPaymentAccountLine line : bill.getAccountLines()) {
          line.setIvcDate(bill.getIvcDate());
        }
        editGrid.refresh();
      } else if (event.getSource().equals(ivcCodeBox)) {
        bill.setIvcCode(ivcCodeBox.getValue());
        for (BPaymentAccountLine line : bill.getAccountLines()) {
          line.setIvcCode(bill.getIvcCode());
        }
        editGrid.refresh();
      } else if (event.getSource().equals(ivcNumberBox)) {
        bill.setIvcNumber(ivcNumberBox.getValue());
        for (BPaymentAccountLine line : bill.getAccountLines()) {
          line.setIvcNumber(bill.getIvcNumber());
        }
        editGrid.refresh();
      }
    }
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(saveAction)) {
        doSave();
      } else if (event.getSource().equals(saveRegiterAction)) {
        doSaveRegiter();
      } else if (event.getSource().equals(cancelAction)) {
        doCancel();
      }
    }
  }

  private void doSaveRegiter() {
    GWTUtil.blurActiveElement();
    if (validate() == false) {
      return;
    }

    for (BPaymentAccountLine line : bill.getAccountLines()) {
      if (line.getTotal() == null || line.getTotal().getTotal() == null) {
        line.setIvcCode(null);
        line.setIvcDate(null);
        line.setIvcNumber(null);
        line.setIvcType(null);
      }
    }

    RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.registe()));
    PaymentService.Locator.getService().registe(bill, new AsyncCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        RLoadingDialog.hide();
        // 记录日志
        BPaymentLogger.getInstance().log(PaymentMessages.M.registe(), bill);
        Message msg = Message.info(PaymentMessages.M.actionSuccess(PaymentMessages.M.payment(),
            PaymentMessages.M.registe()));
        ep.jumpToViewPage(bill.getUuid(), msg);
      }

      @Override
      public void onFailure(Throwable caught) {
        RLoadingDialog.hide();
        String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.registe(),
            PaymentMessages.M.payment());
        RMsgBox.showError(msg, caught);
      }
    });
  }

  private void doSave() {
    GWTUtil.blurActiveElement();
    if (validate() == false) {
      return;
    }

    for (BPaymentAccountLine line : bill.getAccountLines()) {
      if (line.getTotal() == null || line.getTotal().getTotal() == null) {
        line.setIvcCode(null);
        line.setIvcDate(null);
        line.setIvcNumber(null);
        line.setIvcType(null);
      }
    }

    RLoadingDialog.show(PaymentMessages.M.actionDoing(PaymentMessages.M.registe()));
    PaymentService.Locator.getService().saveInvoice(bill, new AsyncCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        RLoadingDialog.hide();
        // 记录日志
        BPaymentLogger.getInstance().log(PaymentMessages.M.registe(), bill);
        Message msg = Message.info(PaymentMessages.M.actionSuccess(PaymentMessages.M.payment(),
            PaymentMessages.M.saveInvoice()));
        ep.jumpToViewPage(bill.getUuid(), msg);
      }

      @Override
      public void onFailure(Throwable caught) {
        RLoadingDialog.hide();
        String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.registe(),
            PaymentMessages.M.payment());
        RMsgBox.showError(msg, caught);
      }
    });
  }

  private void clearValidResults() {
    messages.clear();
    getMessagePanel().clearMessages();
    RContainerValidator.clearValidResults(root);
  }

  private boolean validate() {
    clearValidResults();
    boolean invoiceFormValid = invoiceForm.validate();
    checkEmpty = true;
    boolean gridValid = editGrid.validate();
    checkEmpty = false;
    boolean remarkValid = remarkField.validate();
    boolean isValid = invoiceFormValid && gridValid && remarkValid;
    if (!isValid) {
      getMessagePanel().putMessages(getInvalidMessages());
    }
    return isValid;
  }

  public List<Message> getInvalidMessages() {
    messages.addAll(RContainerValidator.getInvalidMessages(root));
    return messages;
  }

  private void doCancel() {
    RMsgBox.showConfirm(
        PaymentMessages.M.actionComfirm(PaymentMessages.M.cancel(), PaymentMessages.M.registe()),
        new RMsgBox.ConfirmCallback() {
          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed) {
              RHistory.back();
            }
          }
        });
  }

  private class GridDataProvider implements RGridDataProvider,
      EditGridDataProduce<BPaymentAccountLine> {

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
      } else if (col == (ivcCodeViewCol.getIndex())) {
        return rowData.getIvcCode();
      } else if (col == (ivcNumberBrowserCol.getIndex())) {
        return rowData.getIvcNumber();
      } else if (col == (ivcTotalCol.getIndex())) {
        return rowData.getIvcTotal() == null || rowData.getIvcTotal().getTotal() == null ? null
            : rowData.getIvcTotal().getTotal();
      } else if (col == (ivcDateCol.getIndex())) {
        return rowData.getIvcDate();
      } else if (col == (ivcTypeViewCol.getIndex())) {
        return rowData.getIvcType() == null ? null : ivcTypeMap.get(rowData.getIvcType());
      }
      return null;
    }

    @Override
    public BPaymentAccountLine create() {
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

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      String fieldName = colDef.getName();
      BPaymentAccountLine value = bill.getAccountLines().get(row);
      if (fieldName.equals(ivcCodeCol.getName())) {
        return new IvcCodeRenderer(editGrid, colDef, row, col, value);
      } else if (fieldName.equals(ivcNumberCol.getName())) {
        return new IvcNumberRenderer(editGrid, colDef, row, col, value);
      } else if (fieldName.equals(ivcTotalCol.getName())) {
        return new IvcTotalRenderer(editGrid, colDef, row, col, data);
      } else if (fieldName.equals(ivcDateCol.getName())) {
        return new IvcDateRenderer(editGrid, colDef, row, col, data);
      } else if (fieldName.equals(ivcTypeCol.getName())) {
        return new IvcTypeRenderer(editGrid, colDef, row, col, data);
      } else if (fieldName.equals(ivcNumberBrowserCol.getName())) {
        return new IvcNumberBrowserRenderer(editGrid, colDef, row, col, data);
      }
      return null;
    }
  }

  private class IvcCodeRenderer extends EditGridCellWidgetRenderer {
    private RTextBox ivcCodeField;

    public IvcCodeRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected RTextBox drawField(RGridColumnDef colDef, Object data) {
      ivcCodeField = new RTextBox(ivcCodeDef);
      ivcCodeField.setSelectAllOnFocus(true);
      ivcCodeField.addChangeHandler(new IvcCodeChange(getRow()));
      ivcCodeField.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BPaymentAccountLine line = bill.getAccountLines().get(getRow());
          if (checkEmpty && line.getIvcTotal() != null && line.getIvcTotal().getTotal() != null
              && StringUtil.isNullOrBlank(ivcCodeField.getValue())) {
            return Message.error(PaymentMessages.M.notNullField(PaymentMessages.M.ivcCode()));
          }
          return null;
        }
      });
      return ivcCodeField;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      ivcCodeField.setValue(detail.getIvcCode());
    }
  }

  private class IvcNumberRenderer extends EditGridCellWidgetRenderer {
    private RTextBox ivcNumberField;

    public IvcNumberRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected RTextBox drawField(RGridColumnDef colDef, Object data) {
      ivcNumberField = new RTextBox(ivcNumberDef);
      ivcNumberField.setSelectAllOnFocus(true);
      ivcNumberField.addChangeHandler(new IvcNumberChange(getRow()));
      ivcNumberField.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BPaymentAccountLine line = bill.getAccountLines().get(getRow());
          if (checkEmpty && line.getIvcTotal() != null && line.getIvcTotal().getTotal() != null
              && StringUtil.isNullOrBlank(ivcNumberField.getValue())) {
            return Message.error(PaymentMessages.M.notNullField(PaymentMessages.M.ivcNumber()));
          } else {
            return null;
          }
        }
      });
      return ivcNumberField;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      ivcNumberField.setValue(detail.getIvcNumber());
    }
  }

  private class IvcTotalRenderer extends EditGridCellWidgetRenderer {
    private RNumberBox ivcTotalField;

    public IvcTotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected RNumberBox drawField(RGridColumnDef colDef, Object data) {
      ivcTotalField = new RNumberBox(ivcTotalDef);
      ivcTotalField.setSelectAllOnFocus(true);
      ivcTotalField.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
      ivcTotalField.setFormat(M3Format.fmt_money);
      ivcTotalField.addChangeHandler(new IvcTotalChange(getRow()));
      ivcTotalField.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BPaymentAccountLine line = bill.getAccountLines().get(getRow());
          if (ivcTotalField.getValueAsBigDecimal() != null) {
            BigDecimal total = ivcTotalField.getValueAsBigDecimal();
            if (total.compareTo(BigDecimal.ZERO) == 0) {
              return Message.error(PaymentMessages.M.ivcTotalNoZero());
            }
            if (total.multiply(line.getTotal().getTotal()).compareTo(BigDecimal.ZERO) < 0) {
              return Message.error(PaymentMessages.M.ivcTotalDirectionError());
            }
            BigDecimal tempTotal = ivcTotalField.getValueAsBigDecimal().subtract(
                line.getTotal().getTotal());
            if (tempTotal.compareTo(lowTotal) < 0 || tempTotal.compareTo(highTotal) > 0) {
              return Message.error(PaymentMessages.M.noValidTotalRange(M3Format.fmt_money
                  .format(tempTotal.doubleValue())));
            }
            BigDecimal tempTax = BTaxCalculator.tax(ivcTotalField.getValueAsBigDecimal(),
                line.getAcc1().getTaxRate(), ep.getScale(), ep.getRoundingMode()).subtract(
                line.getTotal().getTax());
            if (ep.getConfig().isShowTax()) {
              if (tempTax.compareTo(lowTax) < 0 || tempTax.compareTo(highTax) > 0) {
                return Message.error(PaymentMessages.M.noValidTaxRange(M3Format.fmt_money
                    .format(tempTax.doubleValue())));
              }
            }
          }
          return null;

        }
      });
      return ivcTotalField;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      ivcTotalField.setValue(detail.getIvcTotal() == null ? null : detail.getIvcTotal().getTotal());
    }
  }

  private class IvcDateRenderer extends EditGridCellWidgetRenderer {
    private RDateBox ivcDateField;

    public IvcDateRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected RDateBox drawField(RGridColumnDef colDef, Object data) {
      ivcDateField = new RDateBox();
      ivcDateField.addChangeHandler(new IvcDateChange(getRow()));
      ivcDateField.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BPaymentAccountLine line = bill.getAccountLines().get(getRow());
          if (checkEmpty && line.getIvcTotal() != null && line.getIvcTotal().getTotal() != null
              && ivcDateField.getValue() == null) {
            return Message.error(PaymentMessages.M.notNullField(PaymentMessages.M.ivcDate()));
          }
          return null;
        }
      });
      return ivcDateField;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      ivcDateField.setValue(detail.getIvcDate());
    }
  }

  private class IvcTypeRenderer extends EditGridCellWidgetRenderer {
    private RComboBox<String> ivcTypeField;

    public IvcTypeRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected RComboBox<String> drawField(RGridColumnDef colDef, Object data) {
      ivcTypeField = new RComboBox<String>();
      ivcTypeField.setEditable(false);
      ivcTypeField.setNullOptionText(PaymentMessages.M.nullOption());
      for (String key : ivcTypeMap.keySet()) {
        ivcTypeField.addOption(key, ivcTypeMap.get(key));
      }
      ivcTypeField.addChangeHandler(new IvcTypeChange(getRow()));
      ivcTypeField.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BPaymentAccountLine line = bill.getAccountLines().get(getRow());
          if (checkEmpty && line.getIvcTotal() != null && line.getIvcTotal().getTotal() != null
              && ivcTypeField.getValue() == null) {
            return Message.error(PaymentMessages.M.notNullField(PaymentMessages.M.ivcType()));
          }
          return null;
        }
      });
      return ivcTypeField;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      ivcTypeField.setValue(detail.getIvcType());
    }
  }

  private class IvcNumberBrowserRenderer extends EditGridCellWidgetRenderer {
    private StartNumberBrowserBox ivcNumberBrowserBox;

    public IvcNumberBrowserRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col,
        Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected StartNumberBrowserBox drawField(RGridColumnDef colDef, Object data) {
      ivcNumberBrowserBox = new StartNumberBrowserBox(ivcNumberDef.getCaption(),
          BStockState.received.name(), true);
      ivcNumberBrowserBox.setStoreLimit(bill.getAccountUnit());
      ivcNumberBrowserBox.addValueChangeHandler(new IvcNumberValueChange(getRow()));
      ivcNumberBrowserBox.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BPaymentAccountLine line = bill.getAccountLines().get(getRow());
          if (checkEmpty && line.getIvcTotal() != null && line.getIvcTotal().getTotal() != null
              && StringUtil.isNullOrBlank(ivcNumberBrowserBox.getValue())) {
            return Message.error(PaymentMessages.M.notNullField(PaymentMessages.M.ivcNumber()));
          } else {
            return null;
          }
        }
      });
      return ivcNumberBrowserBox;
    }

    @Override
    public void setValue(Object value) {
      BPaymentAccountLine detail = bill.getAccountLines().get(getRow());
      ivcNumberBrowserBox.setValue(detail.getIvcNumber());
    }
  }

  private class IvcCodeChange implements ChangeHandler {

    private int row;

    public IvcCodeChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentAccountLine value = bill.getAccountLines().get(row);
      if (value == null) {
        return;
      }
      RTextBox box = (RTextBox) event.getSource();
      value.setIvcCode(box.getValue());
    }
  }

  private class IvcNumberChange implements ChangeHandler {

    private int row;

    public IvcNumberChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentAccountLine value = bill.getAccountLines().get(row);
      if (value == null) {
        return;
      }
      RTextBox box = (RTextBox) event.getSource();
      value.setIvcNumber(box.getValue());
    }

  }

  private class IvcTotalChange implements ChangeHandler {

    private int row;

    public IvcTotalChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentAccountLine value = bill.getAccountLines().get(row);
      if (value == null) {
        return;
      }
      RNumberBox box = (RNumberBox) event.getSource();
      BigDecimal total = box.getValueAsBigDecimal();
      if (box.isValid()) {
        value.setIvcTotal(new BTotal(total, BTaxCalculator.tax(total, value.getAcc1().getTaxRate(),
            ep.getScale(), ep.getRoundingMode())));
      }
    }
  }

  private class IvcDateChange implements ChangeHandler {

    private int row;

    public IvcDateChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentAccountLine value = bill.getAccountLines().get(row);
      RDateBox box = (RDateBox) event.getSource();
      value.setIvcDate(box.getValue());
    }

  }

  private class IvcTypeChange implements ChangeHandler {

    private int row;

    public IvcTypeChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentAccountLine value = bill.getAccountLines().get(row);
      RComboBox<String> box = (RComboBox<String>) event.getSource();
      value.setIvcType(box.getValue());
    }
  }

  private class IvcNumberValueChange implements ValueChangeHandler {
    private int row;

    public IvcNumberValueChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onValueChange(ValueChangeEvent event) {
      BPaymentAccountLine value = bill.getAccountLines().get(row);
      StartNumberBrowserBox box = (StartNumberBrowserBox) event.getSource();
      value.setIvcNumber(box.getValue());
      BInvoiceStock stock = box.getRawValue();
      if (stock != null) {
        value.setIvcCode(stock.getInvoiceCode());
        value.setIvcType(stock.getInvoiceType());
      } else {
        value.setIvcCode(null);
        value.setIvcType(null);
      }
      editGrid.refreshRow(row);
    }
  }
}
