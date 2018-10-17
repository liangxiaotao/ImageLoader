package com.taotao7.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache implements ImageCache {
    //图片缓存
    private LruCache<String, Bitmap> mImageCache;

    public MemoryCache() {
        initCache();
    }

    /**
     * 初始化缓存
     */
    private void initCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mImageCache.put(url, bitmap);
    }

    @Override
    public Bitmap get(String url) {
        return mImageCache.get(url);
    }
}
