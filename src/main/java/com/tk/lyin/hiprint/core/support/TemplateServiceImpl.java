package com.tk.lyin.hiprint.core.support;

import com.microsoft.playwright.*;
import com.tk.lyin.hiprint.admin.vo.TemplateType;
import com.tk.lyin.hiprint.cache.AttachementCache;
import com.tk.lyin.hiprint.cache.TemplateCache;
import com.tk.lyin.hiprint.config.ServerConfig;
import com.tk.lyin.hiprint.core.TemplateService;
import com.tk.lyin.hiprint.core.exception.BaseException;
import com.tk.lyin.hiprint.dto.AttachmentVo;
import com.tk.lyin.hiprint.dto.Template;
import com.tk.lyin.hiprint.dto.TemplateVo;
import com.tk.lyin.hiprint.utils.string.RandomUtils;
import com.tk.lyin.hiprint.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.dromara.pdf.pdfbox.core.base.Document;
import org.dromara.pdf.pdfbox.core.ext.convertor.html.HtmlConvertor;
import org.dromara.pdf.pdfbox.core.ext.convertor.html.PageLoadState;
import org.dromara.pdf.pdfbox.handler.PdfHandler;
import org.dromara.pdf.pdfbox.util.ImageUtil;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {
    private final ServerConfig config;

    @SneakyThrows
    @Override
    public TemplateVo loadTemplate(Template template, String type) {

        String id = RandomUtils.generate(3);
        String url = template.getBaseUrl();
        String baseUrl = url + "/?id=" + id;
        TemplateCache.getInstance().set(id, template);
        TemplateVo templateVo = new TemplateVo();
        String active = config.getActive();
        AttachementCache attachementCache = AttachementCache.getInstance();

        HtmlConvertor convertor = PdfHandler.getDocumentConvertor().getHtmlConvertor();
        convertor.setPageState(PageLoadState.NETWORKIDLE);
        String fileName;
        String filePath;

        if (StringUtils.isEmpty(active)) {
            throw new BaseException(10010, "hiprint.active 没有配置，请配置");
        }
        // 没有目录创建
        FileUtils.forceMkdir(new File(active));

        // 转换pdf
        if (StringUtils.equals(type, TemplateType.PDF.getType())) {
            fileName = id + ".pdf";
            filePath = active + fileName;
            AttachmentVo vo = new AttachmentVo();

            Document document = convertor.toPdf(baseUrl);
            // 保存并关闭
            document.saveAndClose(filePath);
            //生成缓存
            vo.setId(id);
            vo.setFileName(fileName);
            vo.setFilePath(filePath);
            vo.setFileType("application/pdf");
            attachementCache.set(id, vo);

            templateVo.setDownloadUrl(url + "/api/template/download?id=" + id);
            templateVo.setViewUrl(url + "/api/template/view?id=" + id);

        }

        // 转换成图片
        if (StringUtils.equals(type, TemplateType.IMAGE.getType())) {
            fileName = id + ".png";
            filePath = active + fileName;
            BufferedImage bufferedImage = convertor.toImage(baseUrl);
            ImageUtil.write(bufferedImage, Files.newOutputStream(Paths.get(filePath)));
            AttachmentVo vo = new AttachmentVo();
            //生成缓存
            vo.setId(id);
            vo.setFileName(fileName);
            vo.setFilePath(filePath);
            vo.setFileType("image/png");
            attachementCache.set(id, vo);

            templateVo.setDownloadUrl(url + "/api/template/download?id=" + id);
            templateVo.setViewUrl(url + "/api/template/view?id=" + id);
        }

        if (StringUtils.equals(type, TemplateType.HTML.getType())) {
            String domId = template.getDomId();
            if (StringUtils.isEmpty(domId)) {
                throw new BaseException(10021, "template domId not is empty");
            }
            templateVo.setHtml(convertor.toHtml(baseUrl, domId));
        }
        return templateVo;
    }

}


