/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccSettleResultPanel.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.ui.gadget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.contract.accountsettle.client.AccountSettleMessages;
import com.hd123.m3.account.gwt.contract.accountsettle.client.EPAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BAccountSettleGroup;
import com.hd123.m3.account.gwt.contract.accountsettle.client.rpc.AccountSettleService;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.AccountSettlePage;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.rumba.gwt.util.client.GWTUtil;
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
 * 出账结果
 * 
 * @author huangjunxian
 * 
 */
public class AccExecuteResultPanel extends Composite implements RRoadmapStepPanel, RValidatable {

  private AccountSettlePage page;
  private EPAccountSettle ep = EPAccountSettle.getInstance();

  private RVerticalPanel monitorPanel;

  public AccExecuteResultPanel(AccountSettlePage page) {
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
    box.setCaption(AccountSettleMessages.M.select_result());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    box.setContent(scrollPanel);

    rootPanel.add(box);

    initWidget(rootPanel);
  }

  public void disablePreButton() {
    RButton preButton = AccountSettlePage.getRoadMapPreButton(page.getRoadMapPanel());
    if (preButton == null)
      return;

    preButton.setEnabled(false);
    for (int index = 1; index <= page.getRoadMapPanel().getRoadmap().getStepCount() - 1; index++)
      page.getRoadMapPanel().getRoadmap().getStep(index).setState(RRoadmapStepState.DISABLED);
  }

  public void enablePreButton() {
    RButton preButton = AccountSettlePage.getRoadMapPreButton(page.getRoadMapPanel());
    if (preButton == null)
      return;

    preButton.setEnabled(true);
    for (int index = 1; index <= page.getRoadMapPanel().getRoadmap().getStepCount() - 1; index++)
      page.getRoadMapPanel().getRoadmap().getStep(index).setState(RRoadmapStepState.ENABLED);
  }

  public void refreshNextButton() {
    RButton nextButton = AccountSettlePage.getRoadMapNextButton(page.getRoadMapPanel());
    if (nextButton == null)
      return;

    nextButton.setText(AccountSettleMessages.M.viewStatement());
    nextButton.setEnabled(true);
    HandlerRegistration handlerReg = nextButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent arg0) {
        GwtUrl url = StatementUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, StatementUrlParams.Search.START_NODE);
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
        } catch (Exception e) {
          String msg = AccountSettleMessages.M.cannotNavigate(url.toString());
          RMsgBox.showError(msg, e);
        }

      }
    });
    page.removeRoadMapNextButtonHandlers();
    page.getRoadMapNextButtonHandlerRegs().add(handlerReg);
  }

  @Override
  public void onShow() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    ep.getTitleBar().appendAttributeText(AccountSettleMessages.M.execute());

    page.removeRoadMapNextButtonHandlers();
    disablePreButton();
    doExecute();
  }

  @Override
  public void onHide() {
    clear();
  }

  public void clear() {
    monitorPanel.clear();
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
    return false;
  }

  /**************************************************/
  private String calculateId;

  private void doExecute() {
    List<BAccountSettle> records = page.getRecords();
    if (records == null || records.isEmpty()) {
      RMsgBox.showError(AccountSettleMessages.M.msg_emptyRecords());
      return;
    }

    // 分组处理出账记录
    List<BAccountSettleGroup> list = handlerRecords(records);
    // 获取本次执行标识
    GWTUtil.enableSynchronousRPC();
    AccountSettleService.Locator.getService().getCalculateId(new RBAsyncCallback2<String>() {

      @Override
      public void onException(Throwable caught) {
        calculateId = null;
      }

      @Override
      public void onSuccess(String result) {
        calculateId = result;
      }
    });

    CalculateBatchProcesser processer = new CalculateBatchProcesser(list);
    processer.start();
  }

  private List<BAccountSettleGroup> handlerRecords(List<BAccountSettle> records) {
    if (records == null || records.isEmpty()) {
      return new ArrayList<BAccountSettleGroup>();
    }
    // 按合同+开单日分组
    Collection<List<BAccountSettle>> group = group(records, new Comparator<BAccountSettle>() {

      @Override
      public int compare(BAccountSettle o1, BAccountSettle o2) {
        if (o1.getContractUuid().equals(o2.getContractUuid())
            && o1.getAccountTime().compareTo(o2.getAccountTime()) == 0) {
          return 0;
        } else {
          return 1;
        }
      }
    });
    // 对每一个分组按起始日期升序排序
    List<BAccountSettleGroup> result = new ArrayList<BAccountSettleGroup>();
    for (List<BAccountSettle> list : group) {
      Collections.sort(list, new Comparator<BAccountSettle>() {

        @Override
        public int compare(BAccountSettle o1, BAccountSettle o2) {
          if (o1.getBeginDate().compareTo(o2.getBeginDate()) == 0) {
            return o1.getEndDate().compareTo(o2.getEndDate());
          } else {
            return o1.getBeginDate().compareTo(o2.getBeginDate());
          }
        }
      });
      BAccountSettleGroup asg = new BAccountSettleGroup();
      asg.setContractUuid(list.get(0).getContractUuid());
      asg.setSettles(list);
      result.add(asg);
    }
    // 对分组按开单日期升序排序
    Collections.sort(result, new Comparator<BAccountSettleGroup>() {

      @Override
      public int compare(BAccountSettleGroup ol1, BAccountSettleGroup ol2) {
        BAccountSettle o1 = ol1.getSettles().get(0);
        BAccountSettle o2 = ol2.getSettles().get(0);
        if (o1.getAccountTime().compareTo(o2.getAccountTime()) == 0) {
          return o1.getBeginDate().compareTo(o2.getBeginDate());
        } else {
          return o1.getAccountTime().compareTo(o2.getAccountTime());
        }
      }
    });

    return result;
  }

  /** 批处理回调接口 */
  private interface CalcCallback {
    void execute(BAccountSettleGroup record, BatchProcesser processer, AsyncCallback callback);
  }

  private class Calculator implements CalcCallback {

    @Override
    public void execute(BAccountSettleGroup record, BatchProcesser processer,
        final AsyncCallback callback) {
      AccountSettleService.Locator.getService().calculate(calculateId, record.getSettles(),
          page.getStatementProcesserKey(), page.getPermGroupId(), page.getPermGroupTitle(),
          new RBAsyncCallback2<String>() {

            @Override
            public void onException(Throwable caught) {
              callback.onFailure(caught);
            }

            @Override
            public void onSuccess(final String result) {
              if (result != null && !AccountSettleMessages.M.empty_billNumber().equals(result)) {
                final RViewStringField captionField = new RViewStringField();
                captionField.setValue(AccountSettleMessages.M.statement_caption());
                final HTML spaceHtml = new HTML();
                spaceHtml.setWidth("10px");
                final RHyperlinkField billNumberField = new RHyperlinkField();
                billNumberField.addClickHandler(new ClickHandler() {

                  @Override
                  public void onClick(ClickEvent event) {
                    GwtUrl url = StatementUrlParams.ENTRY_URL;
                    url.getQuery().set(JumpParameters.PN_START, StatementUrlParams.View.START_NODE);
                    url.getQuery().set(StatementUrlParams.View.PN_BILLNUMBER, result);
                    try {
                      RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
                    } catch (Exception e) {
                      String msg = AccountSettleMessages.M.cannotNavigate(url.toString());
                      RMsgBox.showError(msg, e);
                    }
                  }
                });
                billNumberField.setValue(result);
                RCombinedFlowField field = new RCombinedFlowField() {
                  {
                    addField(captionField);
                    addField(spaceHtml);
                    addField(billNumberField);
                  }
                };
                monitorPanel.add(field);
              } else {
                monitorPanel.add(new HTML(AccountSettleMessages.M.statement_empty()));
              }
              callback.onSuccess(null);
            }
          });
    }

  }

  private class CalculateBatchProcesser extends BatchProcesser<BAccountSettleGroup> {

    private CalcCallback callback;

    public CalculateBatchProcesser(List<BAccountSettleGroup> records) {
      setProgressPanel(new RProgressDialog());
      setRecords(records.toArray(new BAccountSettleGroup[] {}));
      callback = new Calculator();
    }

    @Override
    public void process(final BAccountSettleGroup record, int index, final BatchProcesser processer) {
      if (record == null) {
        getReport().reportSuccess();
        next();
        return;
      }

      String message = AccountSettleMessages.M.msg_execute_ing(
          ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()),
          record.toCounterpartStr(), AccountSettleMessages.M.settle_name(), record.toSettleName(),
          AccountSettleMessages.M.contract_number(), record.toContractNumber());
      getProgressPanel().setMessage(message);
      monitorPanel.add(new HTML(message));
      assert callback != null;
      callback.execute(record, processer, new RBAsyncCallback2<Void>() {

        @Override
        public void onException(final Throwable caught) {
          processer.getReport().reportFailure();
          final String msg = AccountSettleMessages.M.msg_execute_error(
              ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()),
              record.toCounterpartStr(), AccountSettleMessages.M.settle_name(),
              record.toSettleName(), AccountSettleMessages.M.contract_number(),
              record.toContractNumber());
          monitorPanel.add(new HTML(msg));
          RMsgBox.showError(msg + AccountSettleMessages.M.msg_continue(), caught,
              RMsgBox.BUTTONS_OKCANCEL, RMsgBox.BUTTON_CANCEL, new RMsgBox.Callback() {
                public void onClosed(ButtonConfig clickedButton) {
                  if (RMsgBox.BUTTON_CANCEL.equals(clickedButton)) {
                    processer.abort(msg, caught);
                    refreshNextButton();
                  } else
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
      sb.append(AccountSettleMessages.M.msg_finish(interrupted ? AccountSettleMessages.M
          .msg_userInterrupt() : AccountSettleMessages.M.execute()));
      sb.append("<br>" + getReport().getHTML());
      RMsgBox.show(sb.toString(), interrupted ? RMsgBox.ICON_WARN : RMsgBox.ICON_INFO);
      monitorPanel.add(new HTML(AccountSettleMessages.M.msg_finish(AccountSettleMessages.M
          .execute())));
      enablePreButton();
      refreshNextButton();
    }

    @Override
    public void onAborted(String message, Throwable caught) {
      HTML html = new HTML(AccountSettleMessages.M.interrupt());
      monitorPanel.add(html);
    }
  }

  /**
   * 将集合中的元素进行分组。
   * 
   * @param elements
   *          元素
   * @param c
   *          比较两个元素是否在一个集合中
   * @return 分组后的集合，其中的每个元素也是集合。
   */
  public static Collection group(Collection elements, Comparator c) {
    List<List> result = new ArrayList<List>();
    for (Object element : elements) {
      boolean found = false;
      for (List<Object> group : result) {
        if (c.compare(group.get(0), element) == 0) {
          group.add(element);
          found = true;
          break;
        }
      }
      if (!found) {
        List<Object> group = new ArrayList<Object>();
        group.add(element);
        result.add(group);
      }
    }
    return result;
  }

}
