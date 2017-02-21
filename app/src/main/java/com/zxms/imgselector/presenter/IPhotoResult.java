package com.zxms.imgselector.presenter;


import com.zxms.imgselector.util.MediaUtils;

public interface IPhotoResult {
    void showUploadView(String url, MediaUtils.ImageProperty imageProperty);

    void showUploadFailureView(MediaUtils.ImageProperty imageProperty);


}
