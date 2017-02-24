package com.zxms;

import android.content.Intent;
import android.os.Bundle;

import com.zxms.imgselector.presenter.PhotoPresenter;
import com.zxms.imgselector.view.ImageLookActivity;
import com.zxms.imgselector.view.PhotoPickupActivity;
import com.zxms.imgselector.widget.SimpleGrid;
import com.zxms.utils.Constants;
import com.zxms.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicSelectorActivity extends BaseActivity {

    @BindView(R.id.img_grid)
    SimpleGrid mImgGrid;
    private PhotoPresenter mPhotoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_selector);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mPhotoPresenter = new PhotoPresenter(this,"feedback");
        mPhotoPresenter.initView(mImgGrid);
        mImgGrid.setMaxItemPerRow(4);
        mImgGrid.setItemMarginHor(7f);
        mImgGrid.setItemMarginVer(7f);
        mPhotoPresenter.updateImgGrid();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoPickupActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (PhotoPickupActivity.getSelectedImgPros(data) != null) {
                    mPhotoPresenter.pickPhotoResult(data);
                }
            }
        } else if (requestCode == ImageLookActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    mPhotoPresenter.lookImageResult(data);
                }
            }
        }
    }

    @Override
    public void setStatusBarColor() {
        StatusBarUtil.setColor(this,getResources().getColor(Constants.mColor),0);
    }
}
