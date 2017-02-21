package com.zxms;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.zxms.utils.AsyncImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.adv_img)
    ImageView mAdvImg;
    @BindView(R.id.jump)
    Button mJump;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.adv_layout)
    LinearLayout mAdvLayout;

    private String url = "http://img.taopic.com/uploads/allimg/100208/6-10020Q646340.jpg";
    private Context context;
    private int max = 100;
    private int value = 0;
    private Handler mHandler;
    private AsyncImageLoader asyncImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        context = WelcomeActivity.this;
        asyncImageLoader = new AsyncImageLoader();
        mHandler = new Handler();
        showAdvertImg();

    }

    private void showAdvertImg() {
        asyncImageLoader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                if (imageDrawable != null) {
                    mAdvLayout.setVisibility(View.VISIBLE);
                    mJump.setVisibility(View.VISIBLE);
                    mAdvImg.setBackgroundDrawable(imageDrawable);
                    mHandler.post(runnable);
                }
            }
        });

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            value = value + 20;
            if (value == max) {
                mHandler.removeCallbacks(runnable);
                handler.sendEmptyMessage(0);
                jump();
            } else {
                handler.sendEmptyMessage(0);
            }
            mHandler.postDelayed(runnable, 1000);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mProgressBar.setProgress(value);
                    break;
            }
        }
    };

    @OnClick({R.id.jump})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump:
                jump();
                mHandler.removeCallbacks(runnable);
                break;
        }
    }

    private void jump() {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
