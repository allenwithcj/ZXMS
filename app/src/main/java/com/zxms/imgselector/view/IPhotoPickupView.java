package com.zxms.imgselector.view;

import android.graphics.Bitmap;

import com.zxms.imgselector.util.MediaUtils;

import java.util.HashMap;

public interface IPhotoPickupView {
     void onRefreshComplete(String defBunketName, HashMap<String, MediaUtils.ImageProperty> selResult);
     void onThumbnailBitmapReady(Object reqKey, MediaUtils.ImageProperty imgPro, Bitmap bm);
     void onLargeBitmapReady(Object reqKey, MediaUtils.ImageProperty imgPro, Bitmap bm);
}
