package com.zxms.imgselector.presenter;

import android.graphics.Bitmap;
import android.support.v4.util.Pair;

import com.zxms.imgselector.util.MediaUtils;
import com.zxms.imgselector.view.IPhotoPickupView;

import java.util.ArrayList;
import java.util.HashSet;

public interface IPhotoPickupPresenter {

    void onCreate();

    void onDestroy();

    void setCallback(IPhotoPickupView cb);

    Bitmap getThumbnailBitmap(Object reqKey, MediaUtils.ImageProperty imgPro);

    Bitmap getLargeBitmap(Object reqKey, MediaUtils.ImageProperty imgPro, int targetWidth, int targetHeight);

    void refresh(HashSet<String> selImgIds);

    ArrayList<MediaUtils.ImageProperty> getImgProperties(String bunketName);

    ArrayList<Pair<String, ArrayList<MediaUtils.ImageProperty>>> getImgProsGroup();

    boolean isDefBunketName(String bunketName);

    void cancelThumbnailBitmapGetting(Object reqKey, MediaUtils.ImageProperty imgPro);

    void cancelLargeBitmapGetting(Object reqKey, MediaUtils.ImageProperty imgPro);
}
