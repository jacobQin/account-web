/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： CashDefrayalLineGadget.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.ContractComboBox;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.SubjectComboBox;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDepositDefrayalDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 按总额收款时，付款信息中扣预存款行编辑控件
 * 
 * @author subinzhu
 * 
 */
public class DepositDefrayalLineEditGadget extends Composite implements
        HasRActionHandlers, RValidatable, HasFocusables, Focusable {

    public static final String WIDTH_DETAIL_COL0 = "200px";
    public static final String WIDTH_DETAIL_COL1 = "300px";
    public static final String WIDTH_DETAIL_COL2 = "100px";
    public static final String WIDTH_DETAIL_COL3 = "100px";
    public static final String WIDTH_DETAIL_COL4 = "100px";
    public static final String WIDTH_DETAIL_COL5 = "20px";
    public static final String WIDTH_DETAIL_COL6 = "20px";

    private BPayment bill;
    private BPaymentDepositDefrayal value = new BPaymentDepositDefrayal();

    private FlexTable root;
    private SubjectComboBox subjectField;
    private ContractComboBox contractField;
    private RViewNumberField remainTotalField;
    private RNumberBox totalField;
    private RTextBox remarkField;
    private RToolbarButton addButton;
    private RToolbarButton removeButton;

    private Handler_valueChange valueChangeHandler = new Handler_valueChange();

    public BPayment getBill() {
        return bill;
    }

    public void setBill(BPayment bill) {
        this.bill = bill;
    }

    public DepositDefrayalLineEditGadget() {
        root = new FlexTable();
        root.setCellPadding(0);
        root.setCellSpacing(2);

        subjectField = new SubjectComboBox();
        subjectField.addValueChangeHandler(valueChangeHandler);
        root.setWidget(0, 0, subjectField);
        root.getColumnFormatter().setWidth(0, WIDTH_DETAIL_COL0);

        contractField = new ContractComboBox();
        contractField.setEditable(false);
        contractField.addValueChangeHandler(valueChangeHandler);
        root.setWidget(0, 1, contractField);
        root.getColumnFormatter().setWidth(1, WIDTH_DETAIL_COL1);

        remainTotalField = new RViewNumberField();
        remainTotalField.setFormat(GWTFormat.fmt_money);
        root.setWidget(0, 2, remainTotalField);
        root.getColumnFormatter().setWidth(2, WIDTH_DETAIL_COL2);

        totalField = new RNumberBox();
        totalField.setSelectAllOnFocus(true);
        totalField.setScale(2);
        totalField.setFormat(GWTFormat.fmt_money);
        totalField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
        totalField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
        totalField.addValueChangeHandler(valueChangeHandler);
        totalField.setValidator(new RValidator() {
            @Override
            public Message validate(Widget sender, String value1) {
                if (totalField.getValueAsBigDecimal() != null
                        && totalField.getValueAsBigDecimal().compareTo(
                                BigDecimal.ZERO) < 0) {
                    return Message.error(ReceiptMessages.M.cannotLessThan(
                            PPaymentDepositDefrayalDef.constants.total(), "0"));
                } else if (totalField.getValueAsBigDecimal() != null
                        && totalField.getValueAsBigDecimal().compareTo(
                                value.getRemainTotal()) > 0) {
                    return Message.error(ReceiptMessages.M.cannotMoreThan(
                            PPaymentDepositDefrayalDef.constants.total(),
                            ReceiptMessages.M.remainTotal()
                                    + value.getRemainTotal()));
                } else {
                    return null;
                }
            }
        });
        root.setWidget(0, 3, totalField);
        root.getColumnFormatter().setWidth(3, WIDTH_DETAIL_COL3);

        remarkField = new RTextBox(PPaymentDepositDefrayalDef.remark);
        remarkField.setCaption("");
        remarkField.addValueChangeHandler(valueChangeHandler);
        root.setWidget(0, 4, remarkField);
        root.getColumnFormatter().setWidth(4, WIDTH_DETAIL_COL4);

        addButton = new RToolbarButton(RActionFacade.ADD_ITEM,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent arg0) {
                        GWTUtil.blurActiveElement();
                        RActionEvent
                                .fire(DepositDefrayalLineEditGadget.this,
                                        ActionName.ACTION_DEPOSITDEFRAYALLINE_ADDDETAIL,
                                        value.getLineNumber());
                    }
                });
        addButton.setShowText(false);
        root.setWidget(0, 5, addButton);
        root.getColumnFormatter().setWidth(5, WIDTH_DETAIL_COL5);

        removeButton = new RToolbarButton(RActionFacade.REMOVE_ITEM,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent arg0) {
                        GWTUtil.blurActiveElement();
                        RActionEvent
                                .fire(DepositDefrayalLineEditGadget.this,
                                        ActionName.ACTION_DEPOSITDEFRAYALLINE_REMOVEDETAIL,
                                        value.getLineNumber());
                    }
                });
        removeButton.setShowText(false);
        root.setWidget(0, 6, removeButton);
        root.getColumnFormatter().setWidth(6, WIDTH_DETAIL_COL6);

        initWidget(root);
    }

    public BPaymentDepositDefrayal getValue() {
        return value;
    }

    public void setValue(BPaymentDepositDefrayal value) {
        setValue(value, false);
    }

    public void setValue(BPaymentDepositDefrayal value, boolean fireEvent) {
        this.value = value;

        if (value.getSubject() == null) {
            if (subjectField.getOptions().size() > 0) {
                value.setSubject(subjectField.getOptions().getValue(0));
            }
        } else {
            if (subjectField.getOptions().getByValue(value.getSubject()) == null) {
                if (subjectField.getOptions().size() > 0) {
                    value.setSubject(subjectField.getOptions().getValue(0));
                } else {
                    value.setSubject(null);
                }
            }
        }
        subjectField.setValue(value.getSubject(), fireEvent);

        contractField.setValue(value.getContract(), fireEvent);

        if (value.getRemainTotal() == null)
            value.setRemainTotal(BigDecimal.ZERO);
        remainTotalField.setValue(value.getRemainTotal());

        totalField.setValue(value.getTotal() == null ? null : value.getTotal()
                .doubleValue(), fireEvent);
        remarkField.setValue(value.getRemark(), fireEvent);

        if (bill.getCounterpart() != null
                && !StringUtil.isNullOrBlank(bill.getCounterpart().getUuid()))
            getRemainTotal();

        clearValidResults();
    }

    @Override
    public HandlerRegistration addActionHandler(RActionHandler handler) {
        return addHandler(handler, RActionEvent.getType());
    }

    @Override
    public void clearValidResults() {
        subjectField.clearValidResults();
        contractField.clearValidResults();
        totalField.clearValidResults();
        remarkField.clearValidResults();
    }

    @Override
    public boolean isValid() {
        boolean isValid = subjectField.isValid();
        isValid &= contractField.isValid();
        isValid &= totalField.isValid();
        isValid &= remarkField.isValid();
        return isValid;
    }

    @Override
    public List<Message> getInvalidMessages() {
        List<Message> messages = new ArrayList<Message>();

        messages.addAll(subjectField.getInvalidMessages());
        messages.addAll(contractField.getInvalidMessages());
        messages.addAll(totalField.getInvalidMessages());
        messages.addAll(remarkField.getInvalidMessages());

        return messages;
    }

    @Override
    public boolean validate() {
        boolean valid = subjectField.validate();
        valid &= contractField.validate();
        valid &= totalField.validate();
        valid &= remarkField.validate();
        return valid;
    }

    public void focusOnFirstField() {
        subjectField.setFocus(true);
    }

    private class Handler_valueChange implements ValueChangeHandler {
        @Override
        public void onValueChange(ValueChangeEvent event) {
            if (value == null)
                return;

            if (event.getSource() == subjectField) {
                value.setSubject(subjectField.getValue());
                getRemainTotal();
            } else if (event.getSource() == contractField) {
                value.setContract(contractField.getValue());
                getRemainTotal();
            } else if (event.getSource() == totalField) {
                value.setTotal(totalField.getValueAsBigDecimal() == null ? BigDecimal.ZERO
                        : totalField.getValueAsBigDecimal());
            } else if (event.getSource() == remarkField) {
                value.setRemark(remarkField.getValue());
            }
            RActionEvent.fire(DepositDefrayalLineEditGadget.this,
                    ActionName.ACTION_DEPOSITDEFRAYALLINE_TOTALCHANGE);
        }
    }

    /**
     * 获取科目余额
     */
    public void getRemainTotal() {
        if (bill.getAccountUnit() == null
                || bill.getCounterpart() == null
                || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())
                || value.getSubject() == null
                || (value.getContract() == null || StringUtil
                        .isNullOrBlank(value.getContract().getUuid()))) {
            value.setRemainTotal(BigDecimal.ZERO);
            remainTotalField.setValue(BigDecimal.ZERO);
            totalField.setValue(value.getTotal());
            totalField.validate();
            return;
        }

        RLoadingDialog.show(ReceiptMessages.M.loading2(ReceiptMessages.M
                .depositRecSubject() + ReceiptMessages.M.remainTotal()));
        GWTUtil.enableSynchronousRPC();
        PaymentCommonsService.Locator
                .getService()
                .getDepositSubjectRemainTotal(
                        bill.getAccountUnit().getUuid(),
                        bill.getCounterpart().getUuid(),
                        (value.getContract() == null || StringUtil
                                .isNullOrBlank(value.getContract().getUuid())) ? "-"
                                : value.getContract().getUuid(),
                        value.getSubject().getUuid(),
                        new RBAsyncCallback2<BigDecimal>() {
                            @Override
                            public void onException(Throwable caught) {
                                RLoadingDialog.hide();
                                String msg = ReceiptMessages.M.actionFailed(
                                        ReceiptMessages.M.load(),
                                        ReceiptMessages.M.depositRecSubject()
                                                + ReceiptMessages.M
                                                        .remainTotal());
                                RMsgBox.showErrorAndBack(msg, caught);
                            }

                            @Override
                            public void onSuccess(BigDecimal result) {
                                RLoadingDialog.hide();
                                value.setRemainTotal(result);
                                remainTotalField.setValue(result);
                                totalField.setValue(value.getTotal());
                                totalField.validate();
                            }
                        });
    }

    public static class ActionName {
        /** 改变值 */
        public static final String ACTION_DEPOSITDEFRAYALLINE_TOTALCHANGE = "DEPOSITDEFRAYALLINE_change";
        /** 增加一行 */
        public static final String ACTION_DEPOSITDEFRAYALLINE_ADDDETAIL = "DEPOSITDEFRAYALLINE_ADD_DETAIL";
        /** 删除一行 */
        public static final String ACTION_DEPOSITDEFRAYALLINE_REMOVEDETAIL = "DEPOSITDEFRAYALLINE_REMOVE_DETAIL";
    }

    /**
     * 添加重复错误消息
     */
    public void addRepeatError(String msg) {
        subjectField.addErrorMessage(msg);
    }

    @Override
    public Focusable getFocusable(String field) {
        if (BPaymentDepositDefrayal.FN_DEPOSITDEFRAYALTOTAL.equals(field))
            return totalField;
        return null;
    }

    public void refreshSubjects(List<BUCN> subjects) {
        subjectField.refreshOptions(subjects);
        setDefaultSubject();
    }

    public void refreshContracts(List<BUCN> contracts) {
        contractField.refreshOptions(contracts);
        contractField.removeNullOption();
    }

    /**
     * 设置默认科目
     */
    private void setDefaultSubject() {
        if (subjectField.getOptions().getByValue(
                EPReceipt.getInstance().getDefaultOption().getPrePaySubject()) != null) {
            value.setSubject(EPReceipt.getInstance().getDefaultOption()
                    .getPrePaySubject());
        } else {
            if (subjectField.getOptions().size() > 0) {
                value.setSubject(subjectField.getOptions().getValue(0));
            } else {
                value.setSubject(null);
            }
        }
        subjectField.setValue(value.getSubject(), true);

        if (bill.getCounterpart() != null
                && !StringUtil.isNullOrBlank(bill.getCounterpart().getUuid()))
            getRemainTotal();
    }

    /**
     * 刷新预存款科目余额
     */
    public void refreshRemainTotal() {
        remainTotalField.setValue(value.getRemainTotal());
    }

    @Override
    public int getTabIndex() {
        return subjectField.getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        subjectField.setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        subjectField.setFocus(focused);
    }

    @Override
    public void setTabIndex(int index) {
        subjectField.setTabIndex(index);
    }
}
