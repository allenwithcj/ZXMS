package com.zxms;

import android.content.Context;
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


public class MainActivity extends BaseActivity{
    private LinearLayout homePage_layout, information_layout, moudle_layout, find_layout, my_layout;
    private ImageView homePage_img, information_img, moudle_img, find_img, my_img;
    private TextView homePage_txt, information_txt, moudle_txt, find_txt, my_txt;
    private Context context;
    private FragmentManager fragmentManager;
    private HomePageFragment homePageFragment;
    private InformationFragment informationFragment;
    private ModuleFragment moudleFragment;
    private FindFragment findFragment;
    private MyFragment myFragment;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        initView();
        setTabSelection(0);
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        homePage_layout = (LinearLayout) findViewById(R.id.homePage_layout);
        information_layout = (LinearLayout) findViewById(R.id.information_layout);
        moudle_layout = (LinearLayout) findViewById(R.id.moudle_layout);
        find_layout = (LinearLayout) findViewById(R.id.find_layout);
        my_layout = (LinearLayout) findViewById(R.id.my_layout);

        homePage_img = (ImageView) findViewById(R.id.homePage_img);
        information_img = (ImageView) findViewById(R.id.information_img);
        moudle_img = (ImageView) findViewById(R.id.moudle_img);
        find_img = (ImageView) findViewById(R.id.find_img);
        my_img = (ImageView) findViewById(R.id.my_img);

        homePage_txt = (TextView) findViewById(R.id.homePage_txt);
        information_txt = (TextView) findViewById(R.id.information_txt);
        moudle_txt = (TextView) findViewById(R.id.moudle_txt);
        find_txt = (TextView) findViewById(R.id.find_txt);
        my_txt = (TextView) findViewById(R.id.my_txt);

        homePage_layout.setOnClickListener(this);
        information_layout.setOnClickListener(this);
        moudle_layout.setOnClickListener(this);
        find_layout.setOnClickListener(this);
        my_layout.setOnClickListener(this);

    }

    private void setTabSelection(int i) {
        resetBtn();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (i) {
            case 0:
                homePage_txt.setTextColor(this.getResources().getColor(R.color.blue));
                homePage_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.home_active));
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                    fragmentTransaction.add(R.id.id_content, homePageFragment);
                } else {
                    fragmentTransaction.show(homePageFragment);
                }
                break;
            case 1:
                information_txt.setTextColor(this.getResources().getColor(R.color.blue));
                information_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.news_active));
                if (informationFragment == null) {
                    informationFragment = new InformationFragment();
                    fragmentTransaction.add(R.id.id_content, informationFragment);
                } else {
                    fragmentTransaction.show(informationFragment);
                }
                break;
            case 2:
                moudle_txt.setTextColor(this.getResources().getColor(R.color.blue));
                moudle_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.life_active));
                if (moudleFragment == null) {
                    moudleFragment = new ModuleFragment();
                    fragmentTransaction.add(R.id.id_content, moudleFragment);
                } else {
                    fragmentTransaction.show(moudleFragment);
                }
                break;
            case 3:
                find_txt.setTextColor(this.getResources().getColor(R.color.blue));
                find_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.find_active));
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    fragmentTransaction.add(R.id.id_content, findFragment);
                } else {
                    fragmentTransaction.show(findFragment);
                }
                break;
            case 4:
                my_txt.setTextColor(this.getResources().getColor(R.color.blue));
                my_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.my_active));
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
        homePage_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.home));
        homePage_txt.setTextColor(this.getResources().getColor(R.color.black));

        information_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.news));
        information_txt.setTextColor(this.getResources().getColor(R.color.black));

        moudle_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.life));
        moudle_txt.setTextColor(this.getResources().getColor(R.color.black));

        find_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.find));
        find_txt.setTextColor(this.getResources().getColor(R.color.black));

        my_img.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.my));
        my_txt.setTextColor(this.getResources().getColor(R.color.black));

    }

    /**
     * 隐藏fragment
     *
     * @param fragmentTransaction
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(context,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Constants.isLogin = false;
                ActivityControl.finishAll();
            }
        }
        return  false;
    }

}