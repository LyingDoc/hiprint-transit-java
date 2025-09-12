package com.tk.lyin.hiprint.admin;

import com.tk.lyin.hiprint.admin.vo.R;

public class BaseController {
    protected R success(Object data) {
        return R.success(data);
    }

    protected R success() {
        return R.success(null);
    }
}
