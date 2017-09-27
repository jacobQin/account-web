/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	BStatementAdjustValidator.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-21 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustLineDef;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.client.validation.LineLocator;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;

/**
 * @author zhuhairui
 * 
 */
public class BStatementAdjustValidator implements RValidatable {

  public BStatementAdjustValidator(HasFocusables owner, BStatementAdjust statementAdjust) {
    super();
    this.statementAdjust = statementAdjust;
    this.owner = owner;
  }

  private List<Message> messages = new ArrayList<Message>();
  private BStatementAdjust statementAdjust;
  private HasFocusables owner;

  @Override
  public void clearValidResults() {
    messages.clear();
  }

  @Override
  public boolean isValid() {
    return messages.isEmpty();
  }

  @Override
  public List<Message> getInvalidMessages() {
    return messages;
  }

  @Override
  public boolean validate() {
    messages.clear();

    boolean noRecord = true;
    for (int i = 0; i < statementAdjust.getLines().size(); i++) {
      BStatementAdjustLine line = statementAdjust.getLines().get(i);
      if (line.getSubject() == null)
        continue;

      noRecord = false;

      for (int j = i + 1; j < statementAdjust.getLines().size(); j++) {
        if (line.equalsKey(statementAdjust.getLines().get(j))) {
          Message message = Message.error(StatementAdjustMessages.M.lineNumber(i + 1)
              + StatementAdjustMessages.M.and() + StatementAdjustMessages.M.lineNumber(j + 1)
              + StatementAdjustMessages.M.repeat());
          messages.add(message);
        }
      }

      boolean noTotal = line.getTotal().getTotal() == null
          || line.getTotal().getTotal().compareTo(BigDecimal.ZERO) == 0;
      if (noTotal) {
        String msg = StatementAdjustMessages.M.belongToline(i + 1)
            + PStatementAdjustLineDef.constants.amount_total() + StatementAdjustMessages.M.cannot()
            + StatementAdjustMessages.M.beBblank() + StatementAdjustMessages.M.or()
            + StatementAdjustMessages.M.valueEquals("0") + "。";
        Message message = Message.error(msg, getLocator(i, BStatementAdjustLine.FN_TOTAL));
        messages.add(message);
      }

      if (line.getBeginDate() != null && line.getEndDate() != null) {
        if (line.getBeginDate().after(line.getEndDate())) {
          String msg = StatementAdjustMessages.M.belongToline(i + 1)
              + PStatementAdjustLineDef.constants.dateRange_beginDate()
              + StatementAdjustMessages.M.cannot()
              + StatementAdjustMessages.M.greaterThan(PStatementAdjustLineDef.constants
                  .dateRange_endDate()) + "。";
          Message message = Message.error(msg, getLocator(i, BStatementAdjustLine.FN_ENDDATE));
          messages.add(message);
        }
      }

      if (line.getTotal() != null && line.getTotal().getTotal() != null) {
        if (line.getTotal().getTotal().compareTo(BoundaryValue.BIGDECIMAL_MAXVALUE_S2) == 1) {
          Message message = Message.error(
              StatementAdjustMessages.M.belongToline(i + 1)
                  + PStatementAdjustLineDef.constants.amount_total()
                  + StatementAdjustMessages.M.cannot()
                  + StatementAdjustMessages.M.greaterThan(String.valueOf(BoundaryValue.BIGDECIMAL_MAXVALUE_S2
                      .doubleValue())) + "。", getLocator(i, BStatementAdjustLine.FN_TOTAL));
          messages.add(message);
        }
      }

      if (line.getTaxRate() == null) {
        Message message = Message.error(StatementAdjustMessages.M.belongToline(i + 1)
            + PStatementAdjustLineDef.constants.taxRate() + StatementAdjustMessages.M.cannot()
            + StatementAdjustMessages.M.beBblank() + "。",
            getLocator(i, BStatementAdjustLine.FN_TAXRATE));
        messages.add(message);
      }

      if (line.getRemark() != null && line.getRemark().length() > 127) {
        Message message = Message.error(StatementAdjustMessages.M.belongToline(i + 1)
            + StatementAdjustMessages.M.remarkIllegal(),
            getLocator(i, BStatementAdjustLine.FN_REMARK));
        messages.add(message);
      }
      if (line.getAccountDate() == null) {
        Message message = Message.error(StatementAdjustMessages.M.belongToline(i + 1)
            + StatementAdjustMessages.M.accountDateIsNotNull(),
            getLocator(i, BStatementAdjustLine.FN_ACCOUNTDATE));
        messages.add(message);
      } else {
        boolean include = false;
        StringBuilder sb = new StringBuilder();
        for (BDateRange dr : statementAdjust.getAccountRanges()) {
          if (sb.length() > 0) {
            sb.append(",");
          }
          sb.append(M3Format.fmt_yMd.format(dr.getBeginDate()) + "~"
              + M3Format.fmt_yMd.format(dr.getEndDate()));
          if (dr.include(line.getAccountDate())) {
            include = true;
            break;
          }
        }
        if (include == false) {
          Message message = Message.error(StatementAdjustMessages.M.belongToline(i + 1)
              + StatementAdjustMessages.M.accountDateRange(sb.toString()),
              getLocator(i, BStatementAdjustLine.FN_ACCOUNTDATE));
          messages.add(message);
        }
      }
    }

    if (noRecord) {
      Message message = Message.error(StatementAdjustMessages.M.detailLines()
          + StatementAdjustMessages.M.cannot() + StatementAdjustMessages.M.beBblank() + "。",
          getLocator(0, BStatementAdjustLine.FN_STATEMENTSUBJECT));
      messages.add(message);
    }

    return isValid();
  }

  private LineLocator getLocator(int lineIndex, String field) {
    Focusable f = owner != null ? owner.getFocusable(field) : null;
    return new LineLocator(lineIndex, f);
  }

}
