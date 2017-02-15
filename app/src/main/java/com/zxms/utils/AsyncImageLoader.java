package com.zxms.utils;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

/**
 * 异步加载图片
 * Created by hp on 2017/1/17.
 */
public class AsyncImageLoader {
    // 软引用，使用内存做临时缓存 （程序退出，或内存不够则清除软引用）
    private HashMap<String, SoftReference<Drawable>> imageCache;

    public AsyncImageLoader() {
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }

    /**
     * 定义回调接口
     */
    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable, String imageUrl);
    }


    /**
     * 创建子线程加载图片
     * 子线程加载完图片交给handler处理（子线程不能更新ui，而handler处在主线程，可以更新ui）
     * handler又交给imageCallback，imageCallback须要自己来实现，在这里可以对回调参数进行处理
     *
     * @param imageUrl       ：须要加载的图片url
     * @param imageCallback：
     * @return
     */
    public Drawable loadDrawable(final String imageUrl,
                                 final ImageCallback imageCallback) {

        //如果缓存中存在图片  ，则首先使用缓存
        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            Drawable drawable = softReference.get();
            if (drawable != null) {
                imageCallback.imageLoaded(drawable, imageUrl);//执行回调
                return drawable;
            }
        }

        /**
         * 在主线程里执行回调，更新视图
         */
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
            }
        };


        /**
         * 创建子线程访问网络并加载图片 ，把结果交给handler处理
         */
        new Thread() {
            @Override
            public void run() {
                Drawable drawable = loadImageFromUrl(imageUrl);
                // 下载完的图片放到缓存里
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();

        return null;
    }


    /**
     * 下载图片  （注意HttpClient 和httpUrlConnection的区别）
     */
    public Drawable loadImageFromUrl(String imgUrl) {

        try {
            URL url = new URL(imgUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream ins = conn.getInputStream();
                Drawable d = Drawable.createFromStream(ins, "src");
                return d;
            } else {
                return null;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //清除缓存
    public void clearCache() {

        if (this.imageCache.size() > 0) {

            this.imageCache.clear();
        }

    }
}
