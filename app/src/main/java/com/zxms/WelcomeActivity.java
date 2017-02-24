package com.zxms;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zxms.utils.AsyncImageLoader;
import com.zxms.utils.Constants;
import com.zxms.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.adv_img)
    ImageView mAdvImg;
    @BindView(R.id.jump)
    Button mJump;
    @BindView(R.id.view_need_offset)
    CoordinatorLayout mViewNeedOffset;

    private String url = "http://img.hb.aicdn.com/bfa96d9facdde0a37f25ed66e5e0f70a87b6a7505fe0c-sXwrB0_fw658";
    private Context context;
    private AsyncImageLoader asyncImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        context = WelcomeActivity.this;
        asyncImageLoader = new AsyncImageLoader();
        showAdvertImg();

    }

    private void showAdvertImg() {
        asyncImageLoader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                if (imageDrawable != null) {
                    mJump.setVisibility(View.VISIBLE);
                    mAdvImg.setBackgroundDrawable(imageDrawable);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jump();
                        }
                    }, Constants.TIME);
                }else{
                    mJump.setVisibility(View.VISIBLE);
                    mAdvImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome));
                }
            }
        });

    }


    @OnClick({R.id.jump})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump:
                jump();
                break;
        }
    }

    private void jump() {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setStatusBarColor() {
        StatusBarUtil.setTranslucentForImageView(this,0,mViewNeedOffset);
    }
}
