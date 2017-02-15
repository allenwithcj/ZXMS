package com.zxms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zxms.utils.Constants;

import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageView cancel_btn;
    private RelativeLayout sina_login_layout, tencent_login_layout, wechat_login_layout,
            QQ_login_layout, phone_login_layout;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        initView();
    }

    private void initView() {
        cancel_btn = (ImageView) findViewById(R.id.cancel_btn);
        sina_login_layout = (RelativeLayout) findViewById(R.id.sina_login_layout);
        tencent_login_layout = (RelativeLayout) findViewById(R.id.tencent_login_layout);
        wechat_login_layout = (RelativeLayout) findViewById(R.id.wechat_login_layout);
        QQ_login_layout = (RelativeLayout) findViewById(R.id.QQ_login_layout);
        phone_login_layout = (RelativeLayout) findViewById(R.id.phone_login_layout);

        cancel_btn.setOnClickListener(this);
        sina_login_layout.setOnClickListener(this);
        tencent_login_layout.setOnClickListener(this);
        wechat_login_layout.setOnClickListener(this);
        QQ_login_layout.setOnClickListener(this);
        phone_login_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sina_login_layout:
                UMShareAPI.get(context).doOauthVerify(this, SHARE_MEDIA.SINA, authListener);
                break;
            case R.id.tencent_login_layout:
                UMShareAPI.get(context).doOauthVerify(this, SHARE_MEDIA.TENCENT, authListener);
                break;
            case R.id.wechat_login_layout:
                UMShareAPI.get(context).doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);
                break;
            case R.id.QQ_login_layout:
                UMShareAPI.get(context).doOauthVerify(this, SHARE_MEDIA.QQ, authListener);
                break;
            case R.id.cancel_btn:
                finish();
                overridePendingTransition(R.anim.in_left, R.anim.out_right);
                break;
        }
    }

    UMAuthListener authListener = new UMAuthListener(){

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Toast.makeText(context, "登录成功", Toast.LENGTH_LONG).show();
            //发送广播
            Constants.isLogin = true;
            sendBroadCast(share_media);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(context, "登录失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(context, "用户取消", Toast.LENGTH_LONG).show();
        }
    };

    private void sendBroadCast(SHARE_MEDIA share_media) {
        Intent intent = new Intent();
        intent.setAction(Constants.THIRDPARTYLOGIN);
        intent.putExtra("share_media", share_media);
        sendBroadcast(intent);
        finish();
        overridePendingTransition(R.anim.in_right,R.anim.out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

}