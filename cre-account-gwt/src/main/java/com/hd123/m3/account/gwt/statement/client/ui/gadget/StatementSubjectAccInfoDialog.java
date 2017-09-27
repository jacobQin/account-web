/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementSubjectViewDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-13 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.BStringPairGroup;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccDetail;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccFreeDetail;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 科目账款结算情况
 * 
 * @author huangjunxian
 * 
 */
public class StatementSubjectAccInfoDialog extends RDialog {
  private static StatementSubjectAccInfoDialog instance = null;

  public static StatementSubjectAccInfoDialog getInstance() {
    if (instance == null)
      instance = new StatementSubjectAccInfoDialog();
    return instance;
  }

  private static final String DEFAULT_WIDTH = "500px";
  private static final String DEFAULT_HEIGHT = "350px";

  private BStatementAccDetail detail;
  private String billNumber;
  private BStatementLine line;

  public StatementSubjectAccInfoDialog() {
    super();
    setCaptionText(StatementMessages.M.subject_accountInfo());
    setWidth(DEFAULT_WIDTH);
    setHeight(DEFAULT_HEIGHT);
    setWorkingAreaPadding(2);

    setWidget(drawSelf());
    setButtons(new ButtonConfig[] {
      BUTTON_CLOSE });
  }

  private ScrollPanel scrollPanel;
  private RVerticalPanel root;

  private Widget drawSelf() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");

    root = new RVerticalPanel();
    root.setSpacing(5);
    root.setWidth("100%");

    scrollPanel = new ScrollPanel(root);
    scrollPanel.setAlwaysShowScrollBars(false);
    scrollPanel.ensureVisible(root);
    scrollPanel.setWidth("100%");
    scrollPanel.setHeight(DEFAULT_HEIGHT);

    panel.add(scrollPanel);

    return panel;
  }

  private Widget drawBlank() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("90%");
    panel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

    RSimplePanel blankPanel = RSimplePanel.decorateBorder(panel, "");
    blankPanel.setPaddingBottom(130);
    blankPanel.setPaddingTop(130);

    HTML html = new HTML(StatementMessages.M.acc_notExists());
    html.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    panel.add(html);
    return blankPanel;
  }

  public void show(String billNumber, BStatementLine line) {
    this.billNumber = billNumber;
    this.line = line;
    refreshTitle();
    GWTUtil.enableSynchronousRPC();
    if (line.getAcc1() != null && StringUtil.isNullOrBlank(line.getAcc1().getId()) == false) {
      doLoad(new Command() {

        @Override
        public void execute() {
          refresh();
        }
      });
    } else {
      refreshBlank();
    }
    super.center();
  }

  private void refreshTitle() {
    String subjectStr = line == null || line.getAcc1() == null
        || line.getAcc1().getSubject() == null ? "" : line.getAcc1().getSubject().toFriendlyStr();
    setCaptionText(StatementMessages.M.subject_accInfoCaption((billNumber == null ? ""
        : (billNumber + "-")) + subjectStr));
  }

  private void refreshBlank() {
    root.clear();
    root.add(drawBlank());
  }

  private void refresh() {
    root.clear();

    if (detail == null) {
      root.add(drawBlank());
      return;
    }
    // 账款信息
    HTML accCaptionField = new HTML(StatementMessages.M.acc_caption(detail.getCaption()));
    root.add(accCaptionField);
    HTML accTotalField = new HTML(StatementMessages.M.acc_total(buildTotalStr(detail.getTotal())));
    root.add(accTotalField);
    HTML dateRangeField = new HTML(StatementMessages.M.acc_dateRange(buildDateRange(detail
        .getDateRange())));
    root.add(dateRangeField);
    if (detail.getFreeDetails().isEmpty() == false) {
      for (BStatementAccFreeDetail freeDetail : detail.getFreeDetails()) {
        root.add(new HTML(StatementMessages.M.acc_freeTotalDetail(
            freeDetail.getFreeRange().format(M3Format.fmt_yMd),
            M3Format.fmt_percent.format(freeDetail.getRate()),
            M3Format.fmt_money.format(freeDetail.getTotal()))));
      }
      if (detail.getFreeDetails().size() > 1) {
        root.add(new HTML(StatementMessages.M.acc_freeTotal(buildTotalStr(detail.getFreeTotal()))));
      }
    }
    // 明细信息
    for (String str : detail.getDetails()) {
      HTML html = new HTML(str);
      root.add(html);
    }
    // 子项信息
    for (BStatementAccDetail child : detail.getChilds()) {
      HTML html = new HTML(StatementMessages.M.pair_value(child.getCaption(),
          buildTotalStr(child.getTotal())));
      root.add(html);
    }

    for (BStringPairGroup group : detail.getGroups()) {
      StringPairGroupGadget groupGadget = new StringPairGroupGadget();
      groupGadget.setValue(group);
      root.add(groupGadget);
    }
  }

  private String buildTotalStr(BigDecimal value) {
    if (value == null)
      return M3Format.fmt_money.format(0);
    return M3Format.fmt_money.format(value.doubleValue());
  }

  private String buildDateRange(BDateRange dateRange) {
    if (dateRange == null)
      return "";
    Date beginDate = dateRange.getBeginDate();
    Date endDate = dateRange.getEndDate();
    if (beginDate == null && endDate == null)
      return "";
    StringBuffer sb = new StringBuffer();
    if (beginDate != null)
      sb.append(M3Format.fmt_yMd.format(beginDate));
    sb.append(StatementMessages.M.to());
    if (endDate != null)
      sb.append(M3Format.fmt_yMd.format(endDate));
    return sb.toString();
  }

  private void doLoad(final Command callback) {
    RLoadingDialog.show();
    StatementService.Locator.getService().getAccountDetail(line.getAcc1().getId(), line.isActive(),
        new RBAsyncCallback2<BStatementAccDetail>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.find(),
                StatementMessages.M.subject_accountInfo());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(BStatementAccDetail result) {
            RLoadingDialog.hide();
            detail = result;
            callback.execute();
          }
        });
  }

  @Override
  public void hide() {
    root.clear();
    super.hide();
  }
}
