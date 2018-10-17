package com.taotao7.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    //获取CPU的数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //线程池，线程数量为CPU数量加1
    private ExecutorService mExecutor = Executors.newFixedThreadPool(CPU_COUNT + 1);
    private Bitmap mBitmap;
    private ImageCache mImageCache;

    private static ImageLoader mImageLoader;

    private ImageLoader() {
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
        if (mImageCache == null){
            mImageCache = new MemoryCache();
        }
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
            //设置网络连接超时时间为3秒
            connection.setConnectTimeout(3000);
            //设置请求方法
            connection.setRequestMethod("GET");
            //打开输入流
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void setImageCache(ImageCache mImageCache){
        this.mImageCache = mImageCache;
    }
}
