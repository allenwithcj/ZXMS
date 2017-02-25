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
import com.zxms.utils.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.cancel_btn)
    ImageView mCancelBtn;
    @BindView(R.id.sina_login_layout)
    RelativeLayout mSinaLoginLayout;
    @BindView(R.id.tencent_login_layout)
    RelativeLayout mTencentLoginLayout;
    @BindView(R.id.wechat_login_layout)
    RelativeLayout mWechatLoginLayout;
    @BindView(R.id.QQ_login_layout)
    RelativeLayout mQQLoginLayout;
    @BindView(R.id.phone_login_layout)
    RelativeLayout mPhoneLoginLayout;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = LoginActivity.this;
    }

    UMAuthListener authListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
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
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
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

    @OnClick({R.id.cancel_btn, R.id.sina_login_layout, R.id.tencent_login_layout, R.id.wechat_login_layout, R.id.QQ_login_layout, R.id.phone_login_layout})
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

    @Override
    public void setStatusBarColor() {
        StatusBarUtil.setColor(this,getResources().getColor(Constants.COLOR),0);
    }
}