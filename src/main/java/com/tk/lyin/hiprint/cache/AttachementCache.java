package com.tk.lyin.hiprint.cache;

import com.google.common.cache.*;
import com.tk.lyin.hiprint.dto.AttachmentVo;

public class AttachementCache {
    private AttachementCache() {
    }

    private final static AttachementCache attachementCache = new AttachementCache();


    private final Cache<String, AttachmentVo> cache = CacheBuilder.newBuilder()
            .build();

    public static AttachementCache getInstance() {
        return attachementCache;
    }

    public void set(String key, AttachmentVo vo) {
        cache.put(key, vo);
    }

    public AttachmentVo get(String key) {
        return cache.getIfPresent(key);
    }

    public void remove(String key) {
        cache.invalidate(key);
    }

    public void clear() {
        cache.invalidateAll();
    }
}
