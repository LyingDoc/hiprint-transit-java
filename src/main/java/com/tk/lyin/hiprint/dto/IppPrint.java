package com.tk.lyin.hiprint.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class IppPrint {
    private String url;
    private String action;
    private Opt opt;
    private Map<String, Object> message;

    // 打印机参数： {version,uri,charset,language}
    @Getter
    @Setter
    private static class Opt {
        private String version;
        private String uri;
        private String charset;
        private String language;
    }

}
