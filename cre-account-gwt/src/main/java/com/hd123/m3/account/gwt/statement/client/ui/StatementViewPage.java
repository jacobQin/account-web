/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： StatementViewPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.perm.PayInvoiceRegPermDef;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.perm.RecInvoiceRegPermDef;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.perm.AccountDefrayalPermDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccRange;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLogger;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementEntityLoader;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementServiceAgent;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.AttachmentViewGadget;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.StatementAccInfoViewBox;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.dd.CStatementType;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.m3.account.gwt.statement.intf.client.dd.StatementTypeDef;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmViewPage;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.DefaultNavigateHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.EntityNavigator;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RViewDateField;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarMenuButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * 查看页面
 * 
 * @author huangjunxian
 * 
 */
public class StatementViewPage extends BaseBpmViewPage implements StatementUrlParams.View {

  private EPStatement ep = EPStatement.getInstance(EPStatement.class);

  private static StatementViewPage instance;

  public static StatementViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new StatementViewPage();
    return instance;
  }

  public StatementViewPage() throws ClientBizException {
    super();
    try {
      entityLoader = new StatementEntityLoader();
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(CommonsMessages.M.cannotCreatePage("StatementViewPage"), e);
    }
  }

  private BStatement entity;
  private StatementEntityLoader entityLoader;

  private Handler_action actionHandler = new Handler_action();
  // 工具栏
  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private RToolbarMenuButton moreButton;
  private RPopupMenu moreMenu;
  private RAction payIvcAction;
  private RAction payAction;
  private RAction receiptIvcAction;
  private RAction receiptAction;
  private RAction adjAction;
  private RAction subjectAction;

  private EntityNavigator navigator;
  private PrintButton printButton;

  // 基本信息
  private RForm basicForm;
  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private RViewStringField counterPartField;
  private DispatchLinkField contractNumberField;
  private RViewStringField contractNameField;
  private RViewStringField typeField;

  // 状态与操作
  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField commentField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RViewStringField settleStateField;
  private RHyperlink moreInfoField;

  private PermGroupViewField permGroupField;

  private RCaptionBox referenceBox;
  private RForm referenceForm;
  private RViewNumberField saleTotalField;
  // 账单信息
  private RForm statementForm;
  private RViewStringField settleNoField;
  private RViewDateField accountTimeField;
  private RViewDateField receiptAccDateField;

  // 应付合计
  private RForm payTotalForm;
  private RViewNumberField payTotalField;
  private RViewNumberField payTaxField;
  private RViewNumberField payedField;
  private RViewNumberField ivcPayTotalField;
  private RViewNumberField ivcPayTaxField;
  private RViewNumberField ivcPayedField;

  // 应收合计
  private RForm receiptTotalForm;
  private RViewNumberField receiptTotalField;
  private RViewNumberField receiptTaxField;
  private RViewNumberField receiptedField;
  private RViewNumberField ivcRecTotalField;
  private RViewNumberField ivcRecTaxField;
  private RViewNumberField ivcReceiptedField;

  // 账款信息
  private StatementAccInfoViewBox grid;

  // 附件
  private AttachmentViewGadget attachementGadget;

  // 说明
  private RCaptionBox remarkBox;
  private RTextArea remarkField;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, actionHandler);
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, actionHandler);
    getToolbar().add(new RToolbarButton(deleteAction));
    getToolbar().addSeparator();

    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(StatementMessages.M.effect(), actionHandler);
    getToolbar().add(new RToolbarButton(effectAction));

    abortAction = new RAction(StatementMessages.M.abort(), actionHandler);
    getToolbar().add(new RToolbarButton(abortAction));

    getToolbar().addSeparator();

    moreMenu = new RPopupMenu();

    payIvcAction = new RAction(StatementMessages.M.payIvc(), actionHandler);
    moreMenu.addItem(new RMenuItem(payIvcAction));

    payAction = new RAction(StatementMessages.M.pay(), actionHandler);
    moreMenu.addItem(new RMenuItem(payAction));

    receiptIvcAction = new RAction(StatementMessages.M.receiptIvc(), actionHandler);
    moreMenu.addItem(new RMenuItem(receiptIvcAction));

    receiptAction = new RAction(StatementMessages.M.receipt(), actionHandler);
    moreMenu.addItem(new RMenuItem(receiptAction));

    moreMenu.addSeparator();

    adjAction = new RAction(StatementMessages.M.adjust(), actionHandler);
    moreMenu.addItem(new RMenuItem(adjAction));

    getToolbar().addSeparator();

    subjectAction = new RAction(StatementMessages.M.subjectInfo(), actionHandler);
    moreMenu.addItem(new RMenuItem(subjectAction));

    moreButton = new RToolbarMenuButton(StatementMessages.M.operate(), moreMenu);
    getToolbar().add(moreButton);

    // 导航
    drawNaviButtons();
    // 打印
    drawPrintButton();
  }

  private void drawNaviButtons() {
    navigator = new EntityNavigator(getEP().getModuleService());
    navigator.setNavigateHandler(new DefaultNavigateHandler(getEP(), getStartNode(), getEP()
        .getUrlBizKey()));
    getToolbar().addToRight(navigator);
  }

  private void drawPrintButton() {
    getToolbar().addToRight(new RToolbarSeparator());
    printButton = new PrintButton(getEP().getPrintTemplate(), getEP().getCurrentUser().getId());
    getToolbar().addToRight(printButton);
    // 刷新模板
    printButton.refresh();
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawGeneral());
    panel.add(drawAccountTotal());
    panel.add(drawAccountInfo());
    panel.add(drawAttachementPanle());
    panel.add(drawRemark());
  }

  private Widget drawGeneral() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(8);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawBasic());
    mvp.add(0, drawStatement());
    mvp.add(1, drawOperateInfo());

    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    mvp.add(1, drawReference());

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.generalInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_STANDARD);
    box.setContent(vp);
    return box;
  }

  private Widget drawBasic() {
    basicForm = new RForm(1);
    basicForm.setWidth("100%");

    billNumberField = new RViewStringField(PStatementDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    basicForm.addField(billNumberField);

    accountUnitField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    basicForm.addField(accountUnitField);

    counterPartField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    basicForm.addField(counterPartField);

    contractNumberField = new DispatchLinkField(PStatementDef.constants.contract_code());
    contractNumberField.setLinkKey(GRes.R.dispatch_key());
    basicForm.addField(contractNumberField);

    contractNameField = new RViewStringField(PStatementDef.constants.contract_name());
    basicForm.addField(contractNameField);

    typeField = new RViewStringField(PStatementDef.type);
    basicForm.addField(typeField);

    settleNoField = new RViewStringField(PStatementDef.settleNo);
    basicForm.addField(settleNoField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.basicInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(basicForm);
    return box;
  }

  private Widget drawOperateInfo() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PStatementDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    settleStateField = new RViewStringField(PStatementDef.settleState);
    settleStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(settleStateField);

    commentField = new RViewStringField(StatementMessages.M.reason());
    operateForm.addField(commentField);

    createInfoField = new RSimpleOperateInfoField(PStatementDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PStatementDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    moreInfoField = new RHyperlink(StatementMessages.M.moreInfo());
    moreInfoField.addClickHandler(actionHandler);
    operateForm.addField(moreInfoField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.operateInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(operateForm);
    return box;
  }

  private Widget drawReference() {
    referenceForm = new RForm(1);
    referenceForm.setWidth("100%");

    saleTotalField = new RViewNumberField(StatementMessages.M.saleTotal());
    saleTotalField.setFormat(M3Format.fmt_money);
    referenceForm.addField(saleTotalField);

    referenceBox = new RCaptionBox();
    referenceBox.setCaption(StatementMessages.M.reference());
    referenceBox.setWidth("100%");
    referenceBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    referenceBox.setContent(referenceForm);
    return referenceBox;
  }

  private Widget drawStatement() {
    statementForm = new RForm(1);
    statementForm.setWidth("100%");

    accountTimeField = new RViewDateField(PStatementDef.accountTime);
    accountTimeField.setFormat(M3Format.fmt_yMd);

    receiptAccDateField = new RViewDateField(PStatementDef.receiptAccDate);
    receiptAccDateField.setFormat(M3Format.fmt_yMd);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.statementInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(statementForm);
    return box;
  }

  private Widget drawAccountTotal() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(8);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawPayTotal());
    mvp.add(1, drawReceiptTotal());

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.accountTotalInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_STANDARD);
    box.setContent(vp);
    return box;
  }

  private Widget drawPayTotal() {
    payTotalForm = new RForm(1);
    payTotalForm.setWidth("100%");

    payTotalField = new RViewNumberField();
    payTotalField.setFormat(M3Format.fmt_money);
    payTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    payTaxField = new RViewNumberField();
    payTaxField.setFormat(M3Format.fmt_money);
    payTaxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField payCombinField = new RCombinedField() {
      {
        setCaption(StatementMessages.M.field_payTotal());
        addField(payTotalField, 0.5f);
        addField(payTaxField, 0.5f);
      }
    };
    payTotalForm.addField(payCombinField);

    payedField = new RViewNumberField(PStatementDef.payed);
    payedField.setFormat(M3Format.fmt_money);
    payedField.setWidth("49.5%");
    payedField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    payTotalForm.addField(payedField);

    ivcPayTotalField = new RViewNumberField();
    ivcPayTotalField.setFormat(M3Format.fmt_money);
    ivcPayTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    ivcPayTaxField = new RViewNumberField();
    ivcPayTaxField.setFormat(M3Format.fmt_money);
    ivcPayTaxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField ivcPayCombinField = new RCombinedField() {
      {
        setCaption(StatementMessages.M.field_ivcPayTotal());
        addField(ivcPayTotalField, 0.5f);
        addField(ivcPayTaxField, 0.5f);
      }
    };
    payTotalForm.addField(ivcPayCombinField);

    ivcPayedField = new RViewNumberField(PStatementDef.ivcPayed);
    ivcPayedField.setFormat(M3Format.fmt_money);
    ivcPayedField.setWidth("49.5%");
    ivcPayedField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    payTotalForm.addField(ivcPayedField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.payTotalInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(payTotalForm);
    return box;
  }

  private Widget drawReceiptTotal() {
    receiptTotalForm = new RForm(1);
    receiptTotalForm.setWidth("100%");

    receiptTotalField = new RViewNumberField();
    receiptTotalField.setFormat(M3Format.fmt_money);
    receiptTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    receiptTaxField = new RViewNumberField();
    receiptTaxField.setFormat(M3Format.fmt_money);
    receiptTaxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField receiptCombinField = new RCombinedField() {
      {
        setCaption(StatementMessages.M.field_receiptTotal());
        addField(receiptTotalField, 0.5f);
        addField(receiptTaxField, 0.5f);
      }
    };
    receiptTotalForm.addField(receiptCombinField);

    receiptedField = new RViewNumberField(PStatementDef.receipted);
    receiptedField.setFormat(M3Format.fmt_money);
    receiptedField.setWidth("49.5%");
    receiptedField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    receiptTotalForm.addField(receiptedField);

    ivcRecTotalField = new RViewNumberField();
    ivcRecTotalField.setFormat(M3Format.fmt_money);
    ivcRecTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    ivcRecTaxField = new RViewNumberField();
    ivcRecTaxField.setFormat(M3Format.fmt_money);
    ivcRecTaxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    RCombinedField ivcRecCombinField = new RCombinedField() {
      {
        setCaption(StatementMessages.M.field_ivcReceiptTotal());
        addField(ivcRecTotalField, 0.5f);
        addField(ivcRecTaxField, 0.5f);
      }
    };
    receiptTotalForm.addField(ivcRecCombinField);

    ivcReceiptedField = new RViewNumberField(PStatementDef.ivcReceipted);
    ivcReceiptedField.setFormat(M3Format.fmt_money);
    ivcReceiptedField.setWidth("49.5%");
    ivcReceiptedField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    receiptTotalForm.addField(ivcReceiptedField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementMessages.M.receiptTotalInfo());
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(receiptTotalForm);
    return box;
  }

  private Widget drawAccountInfo() {
    grid = new StatementAccInfoViewBox();
    return grid;
  }

  private Widget drawAttachementPanle() {
    attachementGadget = new AttachmentViewGadget();

    RCaptionBox box = new RCaptionBox();
    box.getCaptionBar().setShowCollapse(true);
    box.setContentSpacing(0);
    box.setCaption(StatementMessages.M.attachement());
    box.setContent(attachementGadget);
    box.setWidth("100%");

    return box;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PStatementDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    remarkBox = new RCaptionBox();
    remarkBox.setCaption(StatementMessages.M.remark());
    remarkBox.setStyleName(RCaptionBox.STYLENAME_STANDARD);
    remarkBox.getCaptionBar().setShowCollapse(true);
    remarkBox.setContent(remarkField);
    remarkBox.setWidth("100%");

    return remarkBox;
  }

  @Override
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    entityLoader.decodeParams(params, new Command() {

      @Override
      public void execute() {
        entity = entityLoader.getEntity();
        refreshEntity();
        refreshBpm(entity);
        refreshToolbar();
      }
    });
  }

  private void refreshToolbar() {
    getEP().appendSearchBox();
    navigator.start(getEP().getObjectName(), entity.getUuid());
    refreshPrint();
    // 去除导航器消失时多余的分隔符
    getToolbar().minimizeSeparators();
  }

  private void refreshEntity() {
    refreshPrint();
    refreshGeneral();
    refreshAccTotal();
    refreshAccInfo();
    refreshAttachment();
    refreshRemark();
  }

  private void refreshPrint() {
    if (printButton == null) {
      return;
    }
    if (entity != null) {
      printButton.clearParameters();
      printButton.setEnabled(getEP().isPermitted(BAction.PRINT.getKey()));
      Map<String, String> map = new HashMap<String, String>();
      map.put(PrintingTemplate.KEY_UUID, entity.getUuid());
      map.put(PrintingTemplate.KEY_BILLNUMBER, entity.getBillNumber());
      printButton.addPrintObject(map);
      if (entity.getStore() != null
          && StringUtil.isNullOrBlank(entity.getStore().getUuid()) == false) {
        printButton.setStoreCodes(entity.getStore().getCode());
      }
    }
  }

  private void refreshGeneral() {
    // 基本信息
    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit().toFriendlyStr());
    counterPartField.setValue(entity.getCounterpart() == null ? null : entity.getCounterpart()
        .toFriendlyStr(ep.getCounterpartTypeMap()));
    if (entity.getContract() == null) {
      contractNumberField.clearValue();
    } else {
      if (entity.getCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getCounterpart()
              .getCounterpartType())) {
        contractNumberField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity
            .getContract().getCode());
      } else {
        contractNumberField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getContract()
            .getCode());
      }
    }
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());
    typeField.setValue(CStatementType.patch.equals(entity.getType()) ? StatementTypeDef.constants
        .patch() : StatementTypeDef.constants.normal());

    // 状态与操作
    bizStateField.setValue(PStatementDef.bizState.getEnumCaption(entity.getBizState()));
    commentField.setVisible(StringUtil.isNullOrBlank(entity.getBpmMessage()) == false);
    if (commentField.isVisible())
      commentField.setValue(entity.getBpmMessage());
    createInfoField.setValue(entity.getCreateInfo());
    settleStateField.setValue(PStatementDef.settleState.getEnumCaption(entity.getSettleState()));
    lastModifyInfoField.setValue(entity.getLastModifyInfo());
    operateForm.rebuild();

    // 账单
    refreshStatement();
    // 参考信息
    refreshReference();

    permGroupField.refresh(ep.isPermEnabled(), entity);

    basicForm.rebuild();
  }

  private void refreshStatement() {
    statementForm.clear();
    // 动态展示账期
    for (BStatementAccRange range : entity.getRanges()) {
      RViewStringField field = new RViewStringField(
          range.getCaption() == null ? StatementMessages.M.accountRange() : range.getCaption());
      field.setValue(EPStatement.buildDateRangeStr(range.getDateRange()));
      statementForm.addField(field);
    }
    statementForm.addField(accountTimeField);
    statementForm.addField(receiptAccDateField);
    statementForm.rebuild();

    settleNoField.setValue(entity.getSettleNo());
    accountTimeField.setValue(entity.getAccountTime());
    receiptAccDateField.setValue(entity.getReceiptAccDate());
  }

  private void refreshReference() {
    referenceBox.setVisible(entity.isShowRefer());
    if (referenceBox.isVisible()) {
      saleTotalField.setValue(entity.getSaleTotal() == null ? null : entity.getSaleTotal()
          .doubleValue());
    }
  }

  private void refreshAccTotal() {
    // 应付合计
    if (entity.getPayTotal() != null) {
      payTotalField.setValue(entity.getPayTotal().getTotal());
      payTaxField.setValue(entity.getPayTotal().getTax());
    } else {
      payTotalField.clearValue();
      payTaxField.clearValue();
    }
    payedField.setValue(entity.getPayed());
    if (entity.getIvcPayTotal() != null) {
      ivcPayTotalField.setValue(entity.getIvcPayTotal().getTotal());
      ivcPayTaxField.setValue(entity.getIvcPayTotal().getTax());
    } else {
      ivcPayTotalField.clearValue();
      ivcPayTaxField.clearValue();
    }
    ivcPayedField.setValue(entity.getIvcPayed());

    // 应收合计
    if (entity.getReceiptTotal() != null) {
      receiptTotalField.setValue(entity.getReceiptTotal().getTotal());
      receiptTaxField.setValue(entity.getReceiptTotal().getTax());
    } else {
      receiptTotalField.clearValue();
      receiptTaxField.clearValue();
    }
    receiptedField.setValue(entity.getReceipted());
    if (entity.getIvcReceiptTotal() != null) {
      ivcRecTotalField.setValue(entity.getIvcReceiptTotal().getTotal());
      ivcRecTaxField.setValue(entity.getIvcReceiptTotal().getTax());
    } else {
      ivcRecTotalField.clearValue();
      ivcRecTaxField.clearValue();
    }
    ivcReceiptedField.setValue(entity.getIvcReceipted());
  }

  private void refreshAccInfo() {
    grid.setValue(entity);
  }

  private void refreshAttachment() {
    attachementGadget.setValue(entity.getAttachs());
  }

  private void refreshRemark() {
    remarkField.setValue(entity.getRemark());
  }

  private boolean checkIn() {
    if (ep.isProcessMode()) {
      return true;
    }
    if (ep.isPermitted(StatementPermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {

  }

  /*********************************************************/
  private class Handler_action implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == editAction) {
        doEdit();
      } else if (event.getSource() == deleteAction) {
        doConfirmDeleteOne();
      } else if (event.getSource() == effectAction) {
        doConfirmEffectOne();
      } else if (event.getSource() == abortAction) {
        doConfirmAbortOne();
      } else if (event.getSource() == payIvcAction) {
        ep.doPaymentIvc(entity);
      } else if (event.getSource() == payAction) {
        ep.doPayment(entity);
      } else if (event.getSource() == receiptIvcAction) {
        ep.doReceiptIvc(entity);
      } else if (event.getSource() == receiptAction) {
        ep.doReceipt(entity);
      } else if (event.getSource() == adjAction) {
        ep.doAdjust(entity);
      } else if (event.getSource() == subjectAction) {
        ep.doSubjectInfo(entity);
      } else if (event.getSource() == moreInfoField) {
        doViewLog();
      }
    }
  }

  private void doEdit() {
    JumpParameters params = null;
    if (CStatementType.patch.equals(entity.getType())) {
      params = new JumpParameters(StatementEditPatchPage.START_NODE);
    } else {
      params = new JumpParameters(StatementEditPage.START_NODE);
    }
    params.getUrlRef().set(StatementEditPatchPage.PN_UUID, entity.getUuid());
    ep.jump(params);
  }

  private void doConfirmDeleteOne() {
    getMessagePanel().clearMessages();
    if (CStatementType.normal.equals(entity.getType())) {
      StatementService.Locator.getService().validateBeforeAction(entity.getUuid(),
          BBizActions.DELETE, new RBAsyncCallback2<String>() {

            @Override
            public void onException(Throwable caught) {
              String msg = StatementMessages.M.actionFailed(StatementMessages.M.delete(),
                  StatementUrlParams.MODULE_CAPTION);
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(String result) {
              String msg = (result == null ? "" : result)
                  + StatementMessages.M.actionComfirm2(StatementMessages.M.delete(),
                      StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
              RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

                @Override
                public void onClosed(boolean confirmed) {
                  if (confirmed == false)
                    return;
                  doDeleteOne();
                }
              });
            }
          });
    } else {
      String msg = StatementMessages.M.actionComfirm2(StatementMessages.M.delete(),
          StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doDeleteOne();
        }
      });
    }

  }

  private void doDeleteOne() {
    RLoadingDialog.show(StatementMessages.M.actionDoing(StatementMessages.M.delete()));
    StatementService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.delete(),
                StatementUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BStatementLogger.getInstance().log(StatementMessages.M.delete(), entity);

            JumpParameters params = StatementSearchPage.getInstance().getLastParams() != null ? StatementSearchPage
                .getInstance().getLastParams() : new JumpParameters(StatementSearchPage.START_NODE);
            params.getMessages().add(
                Message.info(StatementMessages.M.onSuccess(StatementMessages.M.delete(),
                    StatementUrlParams.MODULE_CAPTION, entity.getBillNumber())));
            ep.jump(params);
          }
        });
  }

  private void doConfirmEffectOne() {
    getMessagePanel().clearMessages();
    String msg = StatementMessages.M.actionComfirm2(StatementMessages.M.effect(),
        StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

      @Override
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffectOne();
      }
    });
  }

  private void doEffectOne() {
    RLoadingDialog.show(StatementMessages.M.actionDoing(StatementMessages.M.effect()));
    StatementService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.effect(),
                StatementUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BStatementLogger.getInstance().log(StatementMessages.M.effect(), entity);

            String msg = StatementMessages.M.onSuccess(StatementMessages.M.effect(),
                StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
            ep.jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private void doConfirmAbortOne() {
    getMessagePanel().clearMessages();
    if (CStatementType.normal.equals(entity.getType())) {
      StatementService.Locator.getService().validateBeforeAction(entity.getUuid(),
          BBizActions.ABORT, new RBAsyncCallback2<String>() {

            @Override
            public void onException(Throwable caught) {
              String msg = StatementMessages.M.actionFailed(StatementMessages.M.abort(),
                  StatementUrlParams.MODULE_CAPTION);
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(String result) {
              String msg = (result == null ? "" : result)
                  + StatementMessages.M.actionComfirm2(StatementMessages.M.abort(),
                      StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
              RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

                @Override
                public void onClosed(boolean confirmed) {
                  if (confirmed == false)
                    return;
                  doAbort();
                }
              });
            }
          });
    } else {
      String msg = StatementMessages.M.actionComfirm2(StatementMessages.M.abort(),
          ep.getModuleCaption(), entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doAbort();
        }
      });
    }
  }

  private void doAbort() {
    RLoadingDialog.show(StatementMessages.M.actionDoing(StatementMessages.M.abort()));
    StatementService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.abort(),
                ep.getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BStatementLogger.getInstance().log(StatementMessages.M.abort(), entity);

            String msg = StatementMessages.M.onSuccess(StatementMessages.M.abort(),
                StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
            ep.jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private void doViewLog() {
    JumpParameters params = new JumpParameters(StatementUrlParams.Log.START_NODE);
    params.getUrlRef().set(StatementUrlParams.Log.PN_UUID, entity.getUuid());
    ep.jump(params);
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected EPStatement getEP() {
    return EPStatement.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    boolean canDelete = ep.isPermitted(StatementPermDef.DELETE);
    boolean canEdit = ep.isPermitted(StatementPermDef.UPDATE);
    boolean canEffect = ep.isPermitted(StatementPermDef.EFFECT);
    boolean canAbort = ep.isPermitted(StatementPermDef.ABORT);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    boolean isProprietor = BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getCounterpart()
        .getCounterpartType());

    editAction.setVisible(isInEffect && canEdit);
    deleteAction.setVisible(isInEffect && canDelete);
    effectAction.setVisible(isInEffect && canEffect);
    abortAction.setVisible(isEffect && canAbort);
    // 其它按钮
    adjAction.setVisible(isInEffect && CStatementType.normal.equals(entity.getType())
        && ep.isPermitted(StatementAdjustPermDef.CREATE));
    payAction.setVisible(isEffect && ep.isPermitted(PaymentPermDef.CREATE));
    receiptAction.setVisible(isEffect && ep.isPermitted(ReceiptPermDef.CREATE));
    payIvcAction.setVisible(isEffect && ep.isPermitted(PayInvoiceRegPermDef.CREATE)
        && (isProprietor == false));
    receiptIvcAction.setVisible(isEffect && ep.isPermitted(RecInvoiceRegPermDef.CREATE)
        && (isProprietor == false));
    subjectAction.setVisible(ep.isPermitted(AccountDefrayalPermDef.READ) && isEffect);

    boolean visible = false;
    for (Widget item : moreMenu.getItems()) {
      if (item.isVisible()) {
        visible = true;
        break;
      }
    }
    moreButton.setVisible(visible);
    getToolbar().rebuild();
  }

  @Override
  protected List<Widget> getRelateWidgets() {
    List<Widget> list = new ArrayList<Widget>();
    list.add(editAction);
    list.add(deleteAction);
    list.add(effectAction);
    list.add(abortAction);
    list.add(moreButton);
    return list;
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      StatementServiceAgent.executeTask(operation, entity, processCtx, false, this);
    }
  }
}
