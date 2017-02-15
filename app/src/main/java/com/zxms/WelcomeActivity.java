package com.zxms;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.zxms.utils.AsyncImageLoader;


public class WelcomeActivity extends BaseActivity {
    private String url = "http://img.taopic.com/uploads/allimg/100208/6-10020Q646340.jpg";
    private Context context;
    private ImageView adv_img;
    private ProgressBar progressBar;
    private int max = 100;
    private int value = 0;
    private Handler mHandler;
    private AsyncImageLoader asyncImageLoader;
    private Button jump;
    private LinearLayout adv_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = WelcomeActivity.this;
        asyncImageLoader = new AsyncImageLoader();
        mHandler = new Handler();
        initView();

    }

    private void initView() {
        adv_img = (ImageView)findViewById(R.id.adv_img);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        adv_layout = (LinearLayout)findViewById(R.id.adv_layout);
        jump = (Button)findViewById(R.id.jump);


        asyncImageLoader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                if(imageDrawable != null){
                    adv_layout.setVisibility(View.VISIBLE);
                    jump.setVisibility(View.VISIBLE);
                    adv_img.setBackgroundDrawable(imageDrawable);
                }
            }
        });



        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump();
                mHandler.removeCallbacks(runnable);
            }
        });

        mHandler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            value = value + 20;
            if(value == max){
                mHandler.removeCallbacks(runnable);
                handler.sendEmptyMessage(0);
                jump();
            }else{
                handler.sendEmptyMessage(0);
            }
            mHandler.postDelayed(runnable,1000);
        }
    };

    private void jump() {
        Intent intent = new Intent();
        intent.setClass(context,MainActivity.class);
        startActivity(intent);
        finish();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    progressBar.setProgress(value);

                    break;
            }
        }
    };
}
