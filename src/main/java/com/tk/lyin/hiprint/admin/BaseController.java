package com.tk.lyin.hiprint.admin;

import com.tk.lyin.hiprint.admin.vo.R;

public class BaseController {
    protected <T> R<T> success(T data) {
        return R.success(data);
    }

    protected <T> R<T> success() {
        return R.success(null);
    }
}
