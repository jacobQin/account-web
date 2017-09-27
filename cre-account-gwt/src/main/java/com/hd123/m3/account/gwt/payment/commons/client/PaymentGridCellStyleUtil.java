/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PaymentGridCellStyleUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月7日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client;

import java.util.List;

import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.misc.HasTextStyle;

/**
 * 收付款单单元格样式设置工具类。
 * 
 * @author LiBin
 *
 */
public class PaymentGridCellStyleUtil {
  
  /** 如果账款明细含有全部为收就返回收(1),全部为付返回付(-1)，否则就返回0。 */
  public static int getAccsDirection(List<BAccount> accounts) {
    boolean isReceipt = false;
    boolean isPayment = false;
    for (BAccount acc : accounts) {
      if (DirectionType.receipt.getDirectionValue() == acc.getAcc1().getDirection()) {
        isReceipt = true;
      } else {
        isPayment = true;
      }
    }

    if ((isReceipt ^ isPayment) == false) {
      return 0;
    }

    if (isReceipt) {
      return DirectionType.receipt.getDirectionValue();
    }

    return DirectionType.payment.getDirectionValue();
  }

  /**
   * 设置表格单元格样式
   * 
   * @param cell
   *          单元格信息。
   * @param direction
   *          收付方向：“1”存收，“-1”存付，“0”有收有付
   * @param type
   *          收付款类型。
   */
  public static void refreshCellStye(RGridCellInfo cell, int direction, DirectionType type) {
    if (cell.getCol() == 0) {
      return;
    }
    if ((DirectionType.payment.getDirectionValue() == direction && DirectionType.receipt
        .equals(type))
        || (DirectionType.receipt.getDirectionValue() == direction && DirectionType.payment
            .equals(type))) {
      cell.getGrid().getCellFormatter()
          .addStyleName(cell.getRow(), cell.getCol(), PaymentCommonsConstants.STYLENAME_REDBOLD);
      cell.getGrid().getCellFormatter()
          .addStyleName(cell.getRow(), cell.getCol(), PaymentCommonsConstants.STYLENAME_REDBOLD);
      if (cell.getWidget() instanceof HasTextStyle)
        ((HasTextStyle) cell.getWidget())
            .addTextStyleName(PaymentCommonsConstants.STYLENAME_REDBOLD);
    } else {
      cell.getGrid().getCellFormatter()
          .removeStyleName(cell.getRow(), cell.getCol(), PaymentCommonsConstants.STYLENAME_REDBOLD);
      if (cell.getWidget() instanceof HasTextStyle)
        ((HasTextStyle) cell.getWidget())
            .removeTextStyleName(PaymentCommonsConstants.STYLENAME_REDBOLD);
    }
  }

}
