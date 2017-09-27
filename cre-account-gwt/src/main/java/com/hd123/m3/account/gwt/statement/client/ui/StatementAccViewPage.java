/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAccViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.acc.client.BAccountSourceType;
import com.hd123.m3.account.gwt.commons.client.biz.BStringPairGroup;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccDetail;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementAccFilter;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementEntityLoader;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.SourceBillTypeBox;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.StatementCardPanel;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.StatementLineBox;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.StringPairGroupGadget;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementLineDef;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.account.gwt.subject.intf.client.dd.CSubjectType;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * 账款查看页面
 * 
 * @author huangjunxian
 * 
 */
public class StatementAccViewPage extends BaseContentPage implements StatementUrlParams.AccView {

  private static StatementAccViewPage instance;

  public static StatementAccViewPage getInstance() {
    if (instance == null)
      instance = new StatementAccViewPage();
    return instance;
  }

  public StatementAccViewPage() {
    super();
    entityLoader = new StatementEntityLoader();
    drawToolbar();
    drawSelf();
  }

  private EPStatement ep = EPStatement.getInstance();

  private BStatement entity;
  private List<BStatementLine> activeLines;
  private List<BStatementLine> desActiveLines;

  private StatementEntityLoader entityLoader;
  private StatementAccFilter filter = new StatementAccFilter();
  private Handler_action actionHandler = new Handler_action();

  private RAction backAction;
  private StatementCardPanel cardPanel;

  private RForm filterForm;
  private SourceBillTypeBox srcBillTypeField;
  private RTextBox srcBillNumberField;
  private SubjectUCNBox subjectField;
  private RAction searchAction;
  private RAction clearAction;

  private StatementLineBox activeBox;
  private StatementLineBox desActiveBox;
  private RCaptionBox amountLimitBox;
  private RCaptionBox maxSubjectBox;
  private RCaptionBox liqudateBox;

  private void drawToolbar() {
    backAction = new RAction(RActionFacade.BACK_TO_VIEW, actionHandler);
    RToolbarButton backButton = new RToolbarButton(backAction);
    getToolbar().add(backButton);
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(8);
    initWidget(vp);

    cardPanel = new StatementCardPanel();
    vp.add(cardPanel);

    vp.add(drawFilter());

    activeBox = new StatementLineBox();
    activeBox.getCaptionBar().setShowCollapse(true);
    RHTMLField htmlField = new RHTMLField();
    htmlField.addTextStyleName(RTextStyles.STYLE_GRAY);
    htmlField.setText(StatementMessages.M.accInfo_remark());
    activeBox.getCaptionBar().addButton(htmlField);
    vp.add(activeBox);

    desActiveBox = new StatementLineBox();
    desActiveBox.getCaptionBar().setShowCollapse(true);
    desActiveBox.setCaption(StatementMessages.M.desActiveAcc());
    vp.add(desActiveBox);

    amountLimitBox = new RCaptionBox();
    amountLimitBox.setCaption(StatementMessages.M.amountLimit());
    amountLimitBox.setWidth("100%");
    amountLimitBox.getCaptionBar().setShowCollapse(true);
    vp.add(amountLimitBox);

    maxSubjectBox = new RCaptionBox();
    maxSubjectBox.setCaption(StatementMessages.M.max_subject_info());
    maxSubjectBox.setWidth("100%");
    maxSubjectBox.getCaptionBar().setShowCollapse(true);
    vp.add(maxSubjectBox);

    liqudateBox = new RCaptionBox();
    liqudateBox.setCaption(StatementMessages.M.liquidate_info());
    liqudateBox.setWidth("100%");
    liqudateBox.getCaptionBar().setShowCollapse(true);
    vp.add(liqudateBox);
  }

  private Widget drawFilter() {
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(5);

    filterForm = new RForm(2);
    filterForm.setWidth("100%");

    srcBillTypeField = new SourceBillTypeBox();
    srcBillTypeField.setNullOptionText(StatementMessages.M.all());
    srcBillTypeField.setFieldCaption(StatementMessages.M
        .captionEquals(PStatementLineDef.acc1_sourceBill_billType.getCaption()));
    filterForm.addField(srcBillTypeField);

    srcBillNumberField = new RTextBox();
    srcBillNumberField.setFieldCaption(StatementMessages.M
        .captionEquals(PStatementLineDef.acc1_sourceBill_billNumber.getCaption()));
    filterForm.addField(srcBillNumberField);

    subjectField = new SubjectUCNBox(CSubjectType.credit);
    subjectField.setFieldCaption(StatementMessages.M.captionEquals(PStatementLineDef.acc1_subject
        .getCaption()));
    filterForm.addField(subjectField);
    root.add(filterForm);

    HorizontalPanel hl = new HorizontalPanel();
    hl.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    hl.setSpacing(8);

    searchAction = new RAction(RActionFacade.SEARCH, actionHandler);
    hl.add(new RButton(searchAction));

    clearAction = new RAction(RActionFacade.CLEAR, actionHandler);
    hl.add(new RButton(clearAction));
    root.add(hl);

    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.setContent(root);
    box.getCaptionBar().setShowCollapse(true);
    box.setCaption(StatementMessages.M.search_caption());
    return box;
  }

  private boolean checkIn() {
    if (ep.isProcessMode()) {
      return true;
    }
    if (ep.isPermitted(StatementPermDef.UPDATE) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    ep.getTitleBar().appendAttributeText(entity.getBillNumber());
    ep.getTitleBar().appendAttributeText(StatementMessages.M.account_detail(), "-");
  }

  private void refresh() {
    assert entity != null;

    cardPanel.setValue(entity);

    refreshLines();
  }

  private void refreshLines() {
    // 有效账款
    GWTUtil.enableSynchronousRPC();
    StatementService.Locator.getService().queryLines(filter, isIncludeSelf(),
        new RBAsyncCallback2<List<BStatementLine>>() {

          @Override
          public void onException(Throwable caught) {
            activeLines = null;
            activeBox.setValue(entity.getBillNumber(), null);
          }

          @Override
          public void onSuccess(List<BStatementLine> result) {
            activeLines = result;
            if (activeLines.isEmpty()) {
              activeBox.setVisible(false);
            } else {
              activeBox.setVisible(true);
              activeBox.setValue(entity.getBillNumber(), activeLines);
            }
          }
        });

    // 无效账款
    GWTUtil.enableSynchronousRPC();
    StatementService.Locator.getService().queryDesLines(filter, isIncludeSelf(),
        new RBAsyncCallback2<List<BStatementLine>>() {

          @Override
          public void onException(Throwable caught) {
            desActiveLines = null;
            desActiveBox.setValue(entity.getBillNumber(), null);
          }

          @Override
          public void onSuccess(List<BStatementLine> result) {
            desActiveLines = result;
            if (desActiveLines.isEmpty()) {
              desActiveBox.setVisible(false);
            } else {
              desActiveBox.setVisible(true);
              desActiveBox.setValue(entity.getBillNumber(), desActiveLines);
            }
          }
        });
    // 账款明细
    GWTUtil.enableSynchronousRPC();
    StatementService.Locator.getService().queryAccDetails(filter, isIncludeSelf(),
        new RBAsyncCallback2<List<BStatementAccDetail>>() {

          @Override
          public void onException(Throwable caught) {
            amountLimitBox.clear();
            maxSubjectBox.clear();
            liqudateBox.clear();
          }

          @Override
          public void onSuccess(List<BStatementAccDetail> result) {
            refreshAccDetails(result);
          }
        });
  }

  private void refreshAccDetails(List<BStatementAccDetail> result) {
    if (result == null || result.isEmpty()) {
      amountLimitBox.clear();
      maxSubjectBox.clear();
      liqudateBox.clear();
      amountLimitBox.setVisible(false);
      maxSubjectBox.setVisible(false);
      liqudateBox.setVisible(false);
    } else {
      List<BStatementAccDetail> amountLimits = new ArrayList<BStatementAccDetail>();
      List<BStatementAccDetail> maxSubjects = new ArrayList<BStatementAccDetail>();
      List<BStatementAccDetail> liquidates = new ArrayList<BStatementAccDetail>();
      for (BStatementAccDetail d : result) {
        if (BAccountSourceType.amountLimit.equals(d.getAccSrcType())) {
          amountLimits.add(d);
        } else if (BAccountSourceType.maxSubject.equals(d.getAccSrcType())) {
          maxSubjects.add(d);
        } else if (BAccountSourceType.liquidate.equals(d.getAccSrcType())) {
          liquidates.add(d);
        }
      }
      // 刷新账款金额限制
      refresh(amountLimitBox, amountLimits);
      // 刷新最大值
      refresh(maxSubjectBox, maxSubjects);
      // 刷新平衡清算
      refresh(liqudateBox, liquidates);
    }
  }

  private void refresh(RCaptionBox box, List<BStatementAccDetail> details) {
    if (details.isEmpty()) {
      box.clear();
      box.setVisible(false);
    } else {
      box.setVisible(true);
      RMultiVerticalPanel panel = new RMultiVerticalPanel(2);
      panel.setSpacing(5);
      panel.setColumnWidth(0, "50%");
      panel.setColumnWidth(1, "50%");
      box.setContent(panel);

      for (int i = 0; i < details.size(); i++) {
        drawDetail(panel, details.get(i), i % 2);
      }
    }
  }

  private void drawDetail(RMultiVerticalPanel rootPanel, BStatementAccDetail detail, int column) {
    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    rootPanel.add(column, box);

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(5);
    box.setContent(panel);

    // 账款信息
    box.setCaption(detail.getCaption());
    if (BigDecimal.ZERO.compareTo(detail.getTotal()) != 0) {
      HTML accTotalField = new HTML(StatementMessages.M.acc_total(buildTotalStr(detail.getTotal())));
      panel.add(accTotalField);
    }
    // 明细信息
    for (String str : detail.getDetails()) {
      HTML html = new HTML(str);
      panel.add(html);
    }
    // 子项信息
    for (BStatementAccDetail child : detail.getChilds()) {
      HTML html = new HTML(StatementMessages.M.pair_value(child.getCaption(),
          buildTotalStr(child.getTotal())));
      panel.add(html);
    }

    for (BStringPairGroup group : detail.getGroups()) {
      StringPairGroupGadget groupGadget = new StringPairGroupGadget();
      groupGadget.setValue(group);
      panel.add(groupGadget);
    }
  }

  private String buildTotalStr(BigDecimal value) {
    if (value == null)
      return M3Format.fmt_money.format(0);
    return M3Format.fmt_money.format(value.doubleValue());
  }

  private boolean isIncludeSelf() {
    if (filter.getSourceBillType() != null)
      return false;
    if (entity.getBillNumber().equals(filter.getSourceBillNumber()) == false)
      return false;
    return true;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (checkIn() == false)
      return;

    decodeParams(params);

    entityLoader.decodeParams(params, new Command() {

      @Override
      public void execute() {
        entity = entityLoader.getEntity();

        refreshTitle();
        refreshFilter();
        refresh();
      }
    });
  }

  private void decodeParams(JumpParameters params) {
    String subjectUuid = params.getUrlRef().get(KEY_SUBJECT_UUID);
    String subjectCode = params.getUrlRef().get(KEY_SUBJECT_CODE);
    String subjectName = params.getUrlRef().get(KEY_SUBJECT_NAME);
    String uuid = params.getUrlRef().get(PN_UUID);

    filter.setStatementUuid(uuid);
    if (StringUtil.isNullOrBlank(subjectUuid) == false) {
      filter.setSubjectUuid(subjectUuid);
      filter.setSubjectCode(subjectCode);
      filter.setSubjectName(subjectName);
    } else if (StringUtil.isNullOrBlank(subjectCode) == false) {
      GWTUtil.enableSynchronousRPC();
      StatementService.Locator.getService().getSubjectByCode(subjectCode,
          new RBAsyncCallback2<BUCN>() {

            @Override
            public void onException(Throwable caught) {
              filter.setSubject(null);
            }

            @Override
            public void onSuccess(BUCN result) {
              if (result != null) {
                filter.setSubjectUuid(result.getUuid());
                filter.setSubjectCode(result.getCode());
                filter.setSubjectName(result.getName());
              } else {
                filter.setSubject(null);
              }
            }
          });
    } else {
      filter.setSubject(null);
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  private class Handler_action implements ClickHandler {
    public void onClick(ClickEvent event) {
      if (event.getSource() == backAction) {
        ep.jumpToViewPage(entity.getUuid());
      } else if (event.getSource() == searchAction) {
        if (validate()) {
          readFilter();
          refreshLines();
        }
      } else if (event.getSource() == clearAction) {
        clearFilter();
      }
    }
  }

  private boolean validate() {
    boolean isValid = srcBillTypeField.validate();
    isValid &= srcBillNumberField.validate();
    isValid &= subjectField.validate();
    return isValid;
  }

  private void refreshFilter() {
    srcBillTypeField.setValue(filter.getSourceBillType());
    srcBillNumberField.setValue(filter.getSourceBillNumber());
    subjectField.setValue(filter.getSubject());
  }

  private void readFilter() {
    filter.setSourceBillType(srcBillTypeField.getValue());
    if (StringUtil.isNullOrBlank(srcBillNumberField.getValue()) == false
        && srcBillNumberField.getValue().trim().equals(entity.getBillNumber())
        && ep.getModuleContext().get(StatementService.KEY_STATEMENT_TYPE)
            .equals(srcBillTypeField.getValue())) {
      // 本账单
      filter.setSourceBillNumber(StatementMessages.M.self_billNumber());
    } else {
      filter.setSourceBillNumber(srcBillNumberField.getValue());
    }
    filter.setSubject(subjectField.getValue());
  }

  private void clearFilter() {
    srcBillTypeField.clearValue();
    srcBillNumberField.clearValue();
    subjectField.clearValue();

    filter.setSourceBillNumber(null);
    filter.setSourceBillType(null);
    filter.setSubject(null);
  }
}
