package com.tk.lyin.hiprint.core;

import com.tk.lyin.hiprint.dto.Template;
import com.tk.lyin.hiprint.dto.TemplateVo;

public interface TemplateService {

    /**
     * 模板载入
     *
     * @param template 模板参数
     * @param type     文件类型
     * @return {@link TemplateVo}返回值描述（节点html，或者文件url）
     */

    TemplateVo loadTemplate(Template template, String type);
}
