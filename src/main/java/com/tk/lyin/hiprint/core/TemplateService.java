package com.tk.lyin.hiprint.core;

import com.tk.lyin.hiprint.dto.Template;
import com.tk.lyin.hiprint.dto.TemplateVo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface TemplateService {

    /**
     * 这是一个示例方法。
     *
     * @param template 模板参数
     * @param type     文件类型
     * @return 返回值描述（节点html，或者文件url）
     */
    TemplateVo loadTemplate(Template template, String type);
}
