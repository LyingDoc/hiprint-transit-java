package com.tk.lyin.hiprint.admin.ctrl;

import com.tk.lyin.hiprint.admin.vo.R;
import com.tk.lyin.hiprint.cache.AttachementCache;
import com.tk.lyin.hiprint.cache.TemplateCache;
import com.tk.lyin.hiprint.core.TemplateService;
import com.tk.lyin.hiprint.core.exception.BaseException;
import com.tk.lyin.hiprint.core.provider.AttachmentProvider;
import com.tk.lyin.hiprint.dto.AttachmentVo;
import com.tk.lyin.hiprint.dto.Template;
import com.tk.lyin.hiprint.dto.TemplateVo;
import com.tk.lyin.hiprint.utils.gson.GsonUtils;
import com.tk.lyin.hiprint.utils.io.IOUtils;
import com.tk.lyin.hiprint.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/template")
public class TemplateApiCtrl extends BaseCtrl {
    private final TemplateService templateService;

    @GetMapping("/get")
    public R getTemplate(HttpServletRequest request, @RequestParam String id) {
        Template template = TemplateCache.getInstance().get(id);
        if (template == null) {
            return error("模板不存在");
        }
        return success(template);
    }


    @PostMapping("/load")
    public R template(HttpServletRequest request, @RequestParam String type) {
        Template template = GsonUtils.wrapDataToEntity(request, Template.class);
        // 默认为 pdf
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        template.setBaseUrl(baseUrl);
        TemplateVo vo = templateService.loadTemplate(template, type);
        return success(vo);
    }

    @SneakyThrows
    @RequestMapping(value = {"/view"}, method = {RequestMethod.GET}, params = {"id"})
    public void view(@RequestParam String id, HttpServletResponse response) throws IOException {
        Assert.hasText(id, "附件ID不能为空！");
        AttachmentVo vo = AttachementCache.getInstance().get(id);
        Assert.notNull(vo, "ID为[" + id + "]的附件不存在或者已经被删除!");
        String path = vo.getFilePath();
        File file = new File(path);
        Assert.isTrue(file.exists(), String.format("文件%s(ID:%s,路径:%s)已经不存在，附件无法查看！", vo.getFileName(), id, path));
        String contentType = vo.getFileType();
        String disposition = "attachment;filename=" + StringUtils.encodeByUTF8(vo.getFileName());
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-disposition", "inline;filename=" + disposition);
        response.setContentType(contentType);
        FileInputStream fileInputStream = new FileInputStream(file);

        IOUtils.copy(fileInputStream, response.getOutputStream());
        fileInputStream.close();

    }

    @RequestMapping(value = {"/download"}, method = {RequestMethod.GET}, params = {"id"})
    public void download(@RequestParam String id, final HttpServletResponse response) {
        Assert.hasText(id, "附件ID不能为空！");
        AttachmentVo vo = AttachementCache.getInstance().get(id);
        if (ObjectUtils.isEmpty(vo)) {
            throw new BaseException(10001, "ID为[" + id + "]的附件不存在或者已经被删除!");
        }

        AttachmentProvider.handle(id, stream -> {
            response.setContentType(vo.getFileType());
            String fileName = vo.getFileName();
            String disposition = null;
            disposition = "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", disposition);
            IOUtils.copy(stream, response.getOutputStream());
        });
    }
}
