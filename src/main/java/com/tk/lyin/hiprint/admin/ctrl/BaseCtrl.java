package com.tk.lyin.hiprint.admin.ctrl;

import com.tk.lyin.hiprint.admin.vo.R;

public class BaseCtrl {
    protected R success(Object data) {
        return R.success(data);
    }

    protected R success() {
        return R.success(null);
    }
    protected R error(String msg) {
        return R.error(msg);
    }
}
