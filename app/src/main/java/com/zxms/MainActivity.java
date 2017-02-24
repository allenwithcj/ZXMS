package com.zxms;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxms.fragment.FindFragment;
import com.zxms.fragment.HomePageFragment;
import com.zxms.fragment.InformationFragment;
import com.zxms.fragment.ModuleFragment;
import com.zxms.fragment.MyFragment;
import com.zxms.utils.ActivityControl;
import com.zxms.utils.Constants;
import com.zxms.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.homePage_img)
    ImageView mHomePageImg;
    @BindView(R.id.homePage_txt)
    TextView mHomePageTxt;
    @BindView(R.id.homePage_layout)
    LinearLayout mHomePageLayout;
    @BindView(R.id.information_img)
    ImageView mInformationImg;
    @BindView(R.id.information_txt)
    TextView mInformationTxt;
    @BindView(R.id.information_layout)
    LinearLayout mInformationLayout;
    @BindView(R.id.moudle_img)
    ImageView mMoudleImg;
    @BindView(R.id.moudle_txt)
    TextView mMoudleTxt;
    @BindView(R.id.moudle_layout)
    LinearLayout mMoudleLayout;
    @BindView(R.id.find_img)
    ImageView mFindImg;
    @BindView(R.id.find_txt)
    TextView mFindTxt;
    @BindView(R.id.find_layout)
    LinearLayout mFindLayout;
    @BindView(R.id.my_img)
    ImageView mMyImg;
    @BindView(R.id.my_txt)
    TextView mMyTxt;
    @BindView(R.id.my_layout)
    LinearLayout mMyLayout;

    private Context context;
    private FragmentManager fragmentManager;
    private HomePageFragment homePageFragment;
    private InformationFragment informationFragment;
    private ModuleFragment moudleFragment;
    private FindFragment findFragment;
    private MyFragment myFragment;
    private long exitTime = 0;
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = MainActivity.this;
        getPersimmions();
        setTabSelection(0);
    }

    private void setTabSelection(int i) {
        fragmentManager = getSupportFragmentManager();
        resetBtn();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (i) {
            case 0:
                mHomePageTxt.setTextColor(this.getResources().getColor(R.color.blue));
                mHomePageImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.home_active));
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                    fragmentTransaction.add(R.id.id_content, homePageFragment);
                } else {
                    fragmentTransaction.show(homePageFragment);
                }
                break;
            case 1:
                mInformationTxt.setTextColor(this.getResources().getColor(R.color.blue));
                mInformationImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.news_active));
                if (informationFragment == null) {
                    informationFragment = new InformationFragment();
                    fragmentTransaction.add(R.id.id_content, informationFragment);
                } else {
                    fragmentTransaction.show(informationFragment);
                }
                break;
            case 2:
                mMoudleTxt.setTextColor(this.getResources().getColor(R.color.blue));
                mMoudleImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.life_active));
                if (moudleFragment == null) {
                    moudleFragment = new ModuleFragment();
                    fragmentTransaction.add(R.id.id_content, moudleFragment);
                } else {
                    fragmentTransaction.show(moudleFragment);
                }
                break;
            case 3:
                mFindTxt.setTextColor(this.getResources().getColor(R.color.blue));
                mFindImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.find_active));
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    fragmentTransaction.add(R.id.id_content, findFragment);
                } else {
                    fragmentTransaction.show(findFragment);
                }
                break;
            case 4:
                mMyTxt.setTextColor(this.getResources().getColor(R.color.blue));
                mMyImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.my_active));
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    fragmentTransaction.add(R.id.id_content, myFragment);
                } else {
                    fragmentTransaction.show(myFragment);
                }
                break;

        }
        fragmentTransaction.commit();
    }

    /**
     * 重置icon属性
     */
    private void resetBtn() {
        mHomePageImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.home));
        mHomePageTxt.setTextColor(this.getResources().getColor(R.color.black));

        mInformationImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.news));
        mInformationTxt.setTextColor(this.getResources().getColor(R.color.black));

        mMoudleImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.life));
        mMoudleTxt.setTextColor(this.getResources().getColor(R.color.black));

        mFindImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.find));
        mFindTxt.setTextColor(this.getResources().getColor(R.color.black));

        mMyImg.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.my));
        mMyTxt.setTextColor(this.getResources().getColor(R.color.black));

    }

    /**
     * 隐藏fragment
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (homePageFragment != null) {
            fragmentTransaction.hide(homePageFragment);
        }

        if (informationFragment != null) {
            fragmentTransaction.hide(informationFragment);
        }

        if (moudleFragment != null) {
            fragmentTransaction.hide(moudleFragment);
        }

        if (findFragment != null) {
            fragmentTransaction.hide(findFragment);
        }

        if (myFragment != null) {
            fragmentTransaction.hide(myFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Constants.isLogin = false;
                ActivityControl.finishAll();
            }
        }
        return false;
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            //开启摄像头权限
            if (addPermission(permissions, Manifest.permission.CAMERA)) {
                permissionInfo += "Manifest.permission.CAMERA Deny \n";
            }
            //外部读取存储权限
            if (addPermission(permissions, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.READ_EXTERNAL_STORAGE Deny \n";
            }
            //读取wifi状态
            if (addPermission(permissions, Manifest.permission.ACCESS_WIFI_STATE)) {
                permissionInfo += "Manifest.permission.ACCESS_WIFI_STATE Deny \n";
            }


            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case SDK_PERMISSION_REQUEST:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setAction(Constants.LOCATION);
                    sendBroadcast(intent);
                }
                break;
        }

    }

    @OnClick({R.id.homePage_layout,R.id.information_layout,R.id.moudle_layout,R.id.find_layout,R.id.my_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homePage_layout:
                setTabSelection(0);
                break;
            case R.id.information_layout:
                setTabSelection(1);
                break;
            case R.id.moudle_layout:
                setTabSelection(2);
                break;
            case R.id.find_layout:
                setTabSelection(3);
                break;
            case R.id.my_layout:
                setTabSelection(4);
                break;
        }
    }

    @Override
    public void setStatusBarColor() {
        StatusBarUtil.setColor(this,getResources().getColor(Constants.mColor),0);
    }
}