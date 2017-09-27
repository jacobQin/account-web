/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InvoiceRegBatchInputController.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月7日 - renjingzhan - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg.batchInput;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.service.ivc.reg.InvoiceReg;
import com.hd123.m3.account.service.ivc.reg.InvoiceRegService;
import com.hd123.m3.account.service.ivc.reg.IvcRegLine;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.ModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.cre.controller.account.common.AccountOptionComponent;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Entity;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.VersionedEntity;
import com.hd123.rumba.commons.lang.ExceptionUtil;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author renjingzhan
 *
 */
@Controller
@RequestMapping("account/invoice/reg/batchInput/*")
public class InvoiceRegBatchInputController extends ModuleController {

  public static final String KEY_BILLTYPES = "billTypes";
  @Autowired
  private InvoiceRegImportProcessor importProcessor;

  @Autowired
  InvoiceRegService invoiceRegService;
  @Autowired
  private AccountOptionComponent optionComponent;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    Set<String> resourceIds = new HashSet<String>();
    resourceIds.add(AccountPermResourceDef.RESOURCE_RECINVOICEREG);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        resourceIds));
    return permissions;
  }

  @RequestMapping(value = "importFromStatement")
  public @ResponseBody BInvoiceRegBatchInput importFileFromStatement(
      @RequestBody Map<String, String> impexParams) throws Exception {
    String filePath = impexParams.get("filePath");

    if (StringUtil.isNullOrBlank(filePath)) {
      throw ExceptionUtil.nullArgument("filePath");
    }

    return importProcessor.importFile(filePath, impexParams.get("type"), getSessionUserId());
  }

  @RequestMapping(value = "importFromPayment")
  public @ResponseBody BInvoiceRegBatchInput importFileFromPayment(
      @RequestBody Map<String, String> impexParams) throws Exception {
    String filePath = impexParams.get("filePath");

    if (StringUtil.isNullOrBlank(filePath)) {
      throw ExceptionUtil.nullArgument("filePath");
    }

    return importProcessor.importFile(filePath, impexParams.get("type"), getSessionUserId());
  }

  @RequestMapping("save")
  @ResponseBody
  public void save(@RequestBody BInvoiceRegBatchInput entity,@RequestParam String state) throws Exception {

    try {
      BeanOperateContext operCtx = new BeanOperateContext(getSessionUser());

      List<BIvcRegLine> bIvcRegLines = entity.getRegLines();
      List<InvoiceReg> invoiceRegs = decorateByBInvoiceRegBatchInput(bIvcRegLines, entity);

      for (InvoiceReg invoiceReg : invoiceRegs) {
        String uuid = invoiceRegService.save(invoiceReg, operCtx);
        InvoiceReg InvoiceEntity=invoiceRegService.get(uuid);
        log( AuditActions.R.create(), uuid);
        if(state.equals("effect")){
          invoiceRegService.changeBizState(uuid, InvoiceEntity.getVersion(), BizStates.EFFECT, operCtx);
          log( AuditActions.R.effect(), uuid);
        }
      }
    } catch (Exception e) {
      throw new M3ServiceException(e.getMessage());
    }
  }

  
  private List<InvoiceReg> decorateByBInvoiceRegBatchInput(List<BIvcRegLine> bIvcRegLines,
      BInvoiceRegBatchInput entity) {
    
    List<InvoiceReg> invoiceRegs = new ArrayList<InvoiceReg>();

    Collection<List> groups = CollectionUtil.group(bIvcRegLines, new Comparator<BIvcRegLine>() {
      @Override
      public int compare(BIvcRegLine o1, BIvcRegLine o2) {
        if (ObjectUtils.equals(o1.getAcc1().getCounterpart(), o2.getAcc1().getCounterpart())
            && ObjectUtils.equals(o1.getAcc1().getAccountUnit(), o2.getAcc1().getAccountUnit())
            && ObjectUtils.equals(o1.getRegDate(), o2.getRegDate())
            && ObjectUtils.equals(o1.getInvoiceType(), o2.getInvoiceType())) {
          return 0;
        } else {
          return 1;
        }
      }
    });
    for (List<BIvcRegLine> group : groups) {
      InvoiceReg result = new InvoiceReg();
      result.setAccountUnit(group.get(0).getAcc1().getAccountUnit());
      result.setCounterpart(group.get(0).getAcc1().getCounterpart());
      result.setCounterpartType(group.get(0).getAcc1().getCounterpartType());
      if (group.get(0).getInvoiceType().equals("普通发票")) {
        result.setInvoiceType("common");
      } else if (group.get(0).getInvoiceType().equals("凭据")) {
        result.setInvoiceType("evidence");
      } else {
        result.setInvoiceType("VAT");
      }
      result.setDirection(Direction.RECEIPT);
      result.setBizState(BizStates.INEFFECT);
      result.setRegDate(group.get(0).getRegDate());
      result.setAllowSplitReg(optionComponent.isAllowSplitReg());
      result.setUseInvoiceStock(false);
      result.setRemark(entity.getRemark());

      BigDecimal invoiceTotal = new BigDecimal(0);
      BigDecimal invoiceTax = new BigDecimal(0);
      BigDecimal accountTotal = new BigDecimal(0);
      BigDecimal accountTax = new BigDecimal(0);
      List<IvcRegLine> ivcRegLines = new ArrayList<IvcRegLine>();
      for (BIvcRegLine bIvcRegLine : group) {
        IvcRegLine ivcRegLine = new IvcRegLine();
        invoiceTotal = invoiceTotal.add(bIvcRegLine.getTotal().getTotal());
        invoiceTax = invoiceTax.add(bIvcRegLine.getTotal().getTax());
        accountTotal = accountTotal.add(bIvcRegLine.getUnregTotal().getTotal());
        accountTax = accountTax.add(bIvcRegLine.getUnregTotal().getTax());
        ivcRegLine.setAcc1(bIvcRegLine.getAcc1());
        ivcRegLine.setAcc2(bIvcRegLine.getAcc2());
        ivcRegLine.setOriginTotal(bIvcRegLine.getOriginTotal());
        ivcRegLine.setUnregTotal(bIvcRegLine.getUnregTotal());
        ivcRegLine.setTotal(bIvcRegLine.getUnregTotal());
        if (bIvcRegLine.getInvoiceType().equals("普通发票")) {
          ivcRegLine.setInvoiceType("common");
        } else if (bIvcRegLine.getInvoiceType().equals("凭据")) {
          ivcRegLine.setInvoiceType("evidence");
        } else {
          ivcRegLine.setInvoiceType("VAT");
        }
        ivcRegLine.setInvoiceCode(bIvcRegLine.getInvoiceCode());
        ivcRegLine.setInvoiceNumber(bIvcRegLine.getInvoiceNumber());
        ivcRegLine.setRemark(bIvcRegLine.getRemark());
        ivcRegLine.setTotalDiff(bIvcRegLine.getTotalDiff());
        ivcRegLine.setTaxDiff(bIvcRegLine.getTaxDiff());
        ivcRegLines.add(ivcRegLine);
      }
      result.setInvoiceTotal(new Total(invoiceTotal, invoiceTax));
      result.setAccountTotal(new Total(accountTotal, accountTax));
      result.setTotalDiff(accountTotal.subtract(invoiceTotal));
      result.setTaxDiff(accountTax.subtract(invoiceTax));
      result.setPrinted(false);
      result.setRegLines(ivcRegLines);
      invoiceRegs.add(result);
    }
    return invoiceRegs;
  }

}
