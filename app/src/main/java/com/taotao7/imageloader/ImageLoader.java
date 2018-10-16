package com.taotao7.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.chrono.MinguoChronology;
import java.util.IllegalFormatCodePointException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    //获取CPU的数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //图片缓存
    private LruCache<String, Bitmap> mImageCache;
    //线程池，线程数量为CPU数量加1
    private ExecutorService mExecutor = Executors.newFixedThreadPool(CPU_COUNT + 1);
    private Bitmap mBitmap;

    private static ImageLoader mImageLoader;

    private ImageLoader() {
        initCache();
    }

    public static ImageLoader newInstance(){
        if (mImageLoader == null){
            synchronized (ImageLoader.class){
                if (mImageLoader == null){
                    mImageLoader = new ImageLoader();
                }
            }
        }
        return mImageLoader;
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

    /**
     * 图片加载
     *
     * @param url       图片的url
     * @param imageView 显示图片的ImageView
     */
    public void displayImage(final String url, final ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            if (imageView != null) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                new Thread("ImageView is NUll");
            }
            return;
        }
        imageView.setTag(url);
        mBitmap = mImageCache.get(url);
        if (mBitmap != null) {
            imageView.setImageBitmap(mBitmap);
        } else {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    mBitmap = downloadImage(url);
                    if (mBitmap == null) {
                        return;
                    }
                    if (imageView.getTag().equals(url)) {
                        imageView.setImageBitmap(mBitmap);
                    }
                    mImageCache.put(url, mBitmap);
                }
            });
        }
    }

    /**
     * 图片下载
     *
     * @param imageUrl 图片的url
     * @return
     */
    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
