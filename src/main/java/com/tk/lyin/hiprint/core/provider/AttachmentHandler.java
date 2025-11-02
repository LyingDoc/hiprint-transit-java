package com.tk.lyin.hiprint.core.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public interface AttachmentHandler {
    void handle(InputStream var1) throws IOException;
}
