package com.zxms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zxms.R;


/**
 * Created by hp on 2017/1/14.
 * 资讯
 */
public class InformationFragment extends BaseFragment {
    private Button news_btn, picsay_btn, videolisten_btn;
    private FragmentManager fragmentManager;
    private NewsFragment newsFragment;
    private PicSayFragment picSayFragment;
    private VideoListenFragment videoListenFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information, container, false);
        initView(view);
        setTabSelection(0);
        return view;
    }

    private void initView(View view) {
        fragmentManager = getFragmentManager();
        news_btn = (Button) view.findViewById(R.id.news_btn);
        picsay_btn = (Button) view.findViewById(R.id.picsay_btn);
        videolisten_btn = (Button) view.findViewById(R.id.videolisten_btn);

        news_btn.setOnClickListener(this);
        picsay_btn.setOnClickListener(this);
        videolisten_btn.setOnClickListener(this);
    }

    private void setTabSelection(int i) {
        resetBtn();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (i) {
            case 0:
                news_btn.setTextColor(getResources().getColor(R.color.blue));
                news_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_left_choice));
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    fragmentTransaction.add(R.id.information_content, newsFragment);
                } else {
                    fragmentTransaction.show(newsFragment);
                }
                break;
            case 1:
                picsay_btn.setTextColor(getResources().getColor(R.color.blue));
                picsay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_middle_choice));
                if (picSayFragment == null) {
                    picSayFragment = new PicSayFragment();
                    fragmentTransaction.add(R.id.information_content, picSayFragment);
                } else {
                    fragmentTransaction.show(picSayFragment);
                }
                break;
            case 2:
                videolisten_btn.setTextColor(getResources().getColor(R.color.blue));
                videolisten_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_right_choice));
                if (videoListenFragment == null) {
                    videoListenFragment = new VideoListenFragment();
                    fragmentTransaction.add(R.id.information_content, videoListenFragment);
                } else {
                    fragmentTransaction.show(videoListenFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void resetBtn() {
        news_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_left));
        picsay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_middle));
        videolisten_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_right));

        news_btn.setTextColor(getResources().getColor(R.color.white));
        picsay_btn.setTextColor(getResources().getColor(R.color.white));
        videolisten_btn.setTextColor(getResources().getColor(R.color.white));
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (newsFragment != null) {
            fragmentTransaction.hide(newsFragment);
        }
        if (picSayFragment != null) {
            fragmentTransaction.hide(picSayFragment);
        }
        if (videoListenFragment != null) {
            fragmentTransaction.hide(videoListenFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news_btn:
                setTabSelection(0);
                break;
            case R.id.picsay_btn:
                setTabSelection(1);
                break;
            case R.id.videolisten_btn:
                setTabSelection(2);
                break;
        }
    }
}
