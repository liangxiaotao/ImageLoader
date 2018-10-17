package com.taotao7.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

public interface ImageCache {
    void put(String url,Bitmap bitmap);
    Bitmap get(String url);

}
