//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.core.provider;


import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.tk.lyin.hiprint.cache.AttachementCache;
import com.tk.lyin.hiprint.config.ServerConfig;
import com.tk.lyin.hiprint.core.exception.BaseException;
import com.tk.lyin.hiprint.core.system.SystemContainer;
import com.tk.lyin.hiprint.dto.AttachmentVo;
import com.tk.lyin.hiprint.utils.string.StringUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

public class AttachmentProvider {

    @SneakyThrows
    public static void handle(String attachmentId, AttachmentHandler handler) {
        Assert.hasText(attachmentId, "获取附件流进行处理时,附件ID不能为空!");
        Assert.notNull(handler, "获取附件流进行处理时,AttachmentHandler不能为空!");
        File file = getFile(attachmentId);
        if (file == null) {
            throw new FileNotFoundException(String.format("ID为[%s]的附件不存在!", attachmentId));
        }
        InputStream var5 = Files.newInputStream(file.toPath());
        handler.handle(var5);
        IOUtils.closeQuietly(var5);

    }


    public static File getFile(String attachmentId) {
        AttachmentVo attachmentVo= AttachementCache.getInstance().get(attachmentId);
        String fileName = attachmentVo.getFileName();
        Assert.hasText(fileName, "文件名不能为空!");
        ServerConfig config = SystemContainer.getBean(ServerConfig.class);
        Assert.notNull(config, String.format("附件配置实例载入失败!%s", ServerConfig.class.getName()));
        String path = config.getActive();
        if (StringUtils.isEmpty(path)) {
            throw new BaseException(10010, "hiprint.active 没有配置，请配置");
        }
        File file = new File(path + "/" + fileName);
        return file.exists() ? file : null;

    }
}
