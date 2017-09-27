/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AttachmentBizConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月6日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.server.converter;

import com.hd123.m3.account.gwt.statement.client.biz.BAttachment;
import com.hd123.m3.commons.biz.attach.Attachment;
import com.hd123.m3.commons.gwt.util.server.converter.OperateInfoBizConverter;
import com.hd123.rumba.commons.gwt.entity.server.BEntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.util.MediaServiceUtil;

/**
 * 附件转换器
 * <p>
 * Attachment->BAttachment
 * 
 * @author zhr
 * 
 */
public class AttachmentBizConverter extends BEntityConverter<Attachment, BAttachment> {
  private static AttachmentBizConverter instance = null;

  public static AttachmentBizConverter getInstnce() {
    if (instance == null)
      instance = new AttachmentBizConverter();
    return instance;
  }

  @Override
  public BAttachment convert(Attachment source) throws ConversionException {
    if (source == null)
      return null;

    BAttachment target = new BAttachment();
    target.setUuid(source.getUuid());
    target.setId(source.getId());
    target.setName(source.getName());
    target.setFileType(source.getFileType());
    target.setSize(source.getSize());
    target.setUrl(MediaServiceUtil.getFileGetUrl(source.getId()));
    target.setCreateInfo(OperateInfoBizConverter.getInstance().convert(source.getCreateInfo()));
    return target;
  }

}
