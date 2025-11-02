package com.tk.lyin.hiprint.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tk.lyin.hiprint.dto.Template;

import java.util.concurrent.TimeUnit;

public class TemplateCache {
    private TemplateCache() {
    }

    private final static TemplateCache templateCache = new TemplateCache();

    private final Cache<String, Template> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build();

    public static TemplateCache getInstance() {
        return templateCache;
    }

    public void set(String key, Template template) {
        cache.put(key, template);
    }

    public Template get(String key) {
        return cache.getIfPresent(key);
    }

    public void remove(String key) {
        cache.invalidate(key);
    }

    public void clear() {
        cache.invalidateAll();
    }
}
