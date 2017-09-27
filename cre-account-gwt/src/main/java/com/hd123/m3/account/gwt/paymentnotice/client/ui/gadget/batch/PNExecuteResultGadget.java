/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PNExecuteResultGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-26 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui.gadget.batch;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QBatchStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeService;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeBatchPage;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams;
import com.hd123.rumba.gwt.util.client.batchprocess.BatchProcesser;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.dialog.RProgressDialog;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RHyperlinkField;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepPanel;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepState;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 生成结果
 * 
 * @author huangjunxian
 * 
 */
public class PNExecuteResultGadget extends Composite implements RRoadmapStepPanel, RValidatable {

  private PaymentNoticeBatchPage page;
  private EPPaymentNotice ep = EPPaymentNotice.getInstance();

  private RVerticalPanel monitorPanel;

  public PNExecuteResultGadget(PaymentNoticeBatchPage page) {
    super();
    this.page = page;

    drawMain();
  }

  private void drawMain() {
    RVerticalPanel rootPanel = new RVerticalPanel();
    rootPanel.setWidth("100%");
    rootPanel.setSpacing(0);

    monitorPanel = new RVerticalPanel();
    monitorPanel.setSpacing(8);
    ScrollPanel scrollPanel = new ScrollPanel(monitorPanel);
    scrollPanel.setHeight("400px");

    RCaptionBox box = new RCaptionBox();
    box.setContentSpacing(0);
    box.setCaption(PaymentNoticeMessages.M.batch_generate(ep.getModuleCaption()));
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    box.setContent(scrollPanel);

    rootPanel.add(box);

    initWidget(rootPanel);
  }

  @Override
  public void onShow() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(PaymentNoticeMessages.M.batch_caption(ep.getModuleCaption()));
    ep.getTitleBar().appendAttributeText(PaymentNoticeMessages.M.batch_execute());

    page.removeRoadMapNextButtonHandlers();
    disablePreButton();
    doExecute();
  }

  public void disablePreButton() {
    RButton preButton = PaymentNoticeBatchPage.getRoadMapPreButton(page.getRoadMapPanel());
    if (preButton == null)
      return;

    preButton.setEnabled(false);
    for (int index = 1; index <= page.getRoadMapPanel().getRoadmap().getStepCount() - 1; index++)
      page.getRoadMapPanel().getRoadmap().getStep(index).setState(RRoadmapStepState.DISABLED);
  }

  public void enablePreButton() {
    RButton preButton = PaymentNoticeBatchPage.getRoadMapPreButton(page.getRoadMapPanel());
    if (preButton == null)
      return;

    preButton.setEnabled(true);
    for (int index = 1; index <= page.getRoadMapPanel().getRoadmap().getStepCount() - 1; index++)
      page.getRoadMapPanel().getRoadmap().getStep(index).setState(RRoadmapStepState.ENABLED);
  }

  public void refreshNextButton() {
    RButton nextButton = PaymentNoticeBatchPage.getRoadMapNextButton(page.getRoadMapPanel());
    if (nextButton == null)
      return;

    nextButton.setText(PaymentNoticeMessages.M.batch_viewBill());
    nextButton.setEnabled(true);
    HandlerRegistration handlerReg = nextButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent arg0) {
        JumpParameters params = new JumpParameters(PaymentNoticeUrlParams.Search.START_NODE);
        ep.jump(params);
      }
    });
    page.getRoadMapNextButtonHandlerRegs().add(handlerReg);
  }

  @Override
  public void onHide() {
    monitorPanel.clear();
  }

  /********************************************/
  private void doExecute() {
    List<QBatchStatement> records = page.getCounterparts();
    if (records == null || records.isEmpty()) {
      RMsgBox.show(PaymentNoticeMessages.M.batch_msg_emptyCounterparts(ep.getFieldCaption(
          GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart())));
      return;
    }

    GenerateBatchProcesser processer = new GenerateBatchProcesser(records);
    processer.start();
  }

  private interface GenerateCallback {
    void execute(QBatchStatement record, BatchProcesser processer, AsyncCallback callback);
  }

  private class Generator implements GenerateCallback {

    @Override
    public void execute(QBatchStatement record, BatchProcesser processer,
        final AsyncCallback callback) {
      PaymentNoticeService.Locator.getService().generateBill(record, page.isSplitBill(),
          page.getPerm(), page.getProDefinition(), ep.getProcessCtx().getTask(),
          new RBAsyncCallback2<List<String>>() {

            @Override
            public void onException(Throwable caught) {
              callback.onFailure(caught);
            }

            @Override
            public void onSuccess(List<String> result) {
              doShowResult(result);
              callback.onSuccess(null);
            }
          });

    }

  }

  private void doShowResult(List<String> result) {
    if (result == null || result.isEmpty()) {
      monitorPanel.add(new HTML(PaymentNoticeMessages.M.batch_emptyBill()));
      return;
    }

    for (final String billNumber : result) {
      final RViewStringField captionField = new RViewStringField();
      captionField.setValue(PaymentNoticeMessages.M.batch_billNumber_caption());
      final HTML spaceHtml = new HTML();
      spaceHtml.setWidth("10px");
      final RHyperlinkField billNumberField = new RHyperlinkField();
      billNumberField.setValue(billNumber);
      billNumberField.addClickHandler(new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
          GwtUrl url = PaymentNoticeUrlParams.ENTRY_URL;
          url.getQuery().set(JumpParameters.PN_START, PaymentNoticeUrlParams.View.START_NODE);
          url.getQuery().set(PaymentNoticeUrlParams.View.PN_ENTITY_BILLNUMBER, billNumber);
          try {
            RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
          } catch (Exception e) {
            String msg = PaymentNoticeMessages.M.cannotNavigate(url.toString());
            RMsgBox.showError(msg, e);
          }
        }
      });
      RCombinedFlowField field = new RCombinedFlowField() {
        {
          addField(captionField);
          addField(spaceHtml);
          addField(billNumberField);
        }
      };
      monitorPanel.add(field);
    }
  }

  private class GenerateBatchProcesser extends BatchProcesser<QBatchStatement> {

    private GenerateCallback callback;

    public GenerateBatchProcesser(List<QBatchStatement> records) {
      setProgressPanel(new RProgressDialog());
      setRecords(records.toArray(new QBatchStatement[] {}));
      callback = new Generator();
    }

    @Override
    public void process(final QBatchStatement record, int index, final BatchProcesser processer) {
      if (record == null) {
        getReport().reportSkipped();
        next();
        return;
      }
      String message = PaymentNoticeMessages.M.batch_generating(ep.getFieldCaption(
          GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()), record.getCounterpart().getNameCode(),
          ep.getModuleCaption());
      getProgressPanel().setMessage(message);
      monitorPanel.add(new HTML(message));
      callback.execute(record, processer, new RBAsyncCallback2<Void>() {

        @Override
        public void onException(final Throwable t) {
          processer.getReport().reportFailure();
          final String msg = PaymentNoticeMessages.M.batch_generating_error(ep.getFieldCaption(
              GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()), record.getCounterpart()
              .getNameCode(), ep.getModuleCaption());
          monitorPanel.add(new HTML(msg));
          RMsgBox.showError(msg + PaymentNoticeMessages.M.batch_continue(), t,
              RMsgBox.BUTTONS_OKCANCEL, RMsgBox.BUTTON_CANCEL, new RMsgBox.Callback() {

                @Override
                public void onClosed(ButtonConfig clickedButton) {
                  if (RMsgBox.BUTTON_CANCEL.equals(clickedButton))
                    processer.abort(msg, t);
                  else
                    processer.next();
                }
              });

        }

        @Override
        public void onSuccess(Void result) {
          processer.getReport().reportSuccess();
          processer.next();
        }
      });
    }

    @Override
    public void onOver(boolean interrupted) {
      StringBuffer sb = new StringBuffer();
      sb.append(PaymentNoticeMessages.M.batch_msg_finish(interrupted ? PaymentNoticeMessages.M
          .batch_msg_userInterrupt() : PaymentNoticeMessages.M.batch_execute()));
      sb.append("<br>" + getReport().getHTML());
      RMsgBox.show(sb.toString(), interrupted ? RMsgBox.ICON_WARN : RMsgBox.ICON_INFO);
      monitorPanel.add(new HTML(PaymentNoticeMessages.M.batch_msg_finish(PaymentNoticeMessages.M
          .batch_execute())));
      enablePreButton();
      refreshNextButton();
    }

    @Override
    public void onAborted(String message, Throwable caught) {
      HTML html = new HTML(PaymentNoticeMessages.M.batch_interrupt());
      monitorPanel.add(html);
    }

  }

  @Override
  public void clearValidResults() {

  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public List<Message> getInvalidMessages() {
    return Collections.emptyList();
  }

  @Override
  public boolean validate() {
    return page.getCounterparts() != null && page.getCounterparts().isEmpty() == false;
  }

}
