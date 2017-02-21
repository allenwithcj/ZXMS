package com.zxms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zxms.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by hp on 2017/1/14.
 * 资讯
 */
public class InformationFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.news_btn)
    Button mNewsBtn;
    @BindView(R.id.picsay_btn)
    Button mPicsayBtn;
    @BindView(R.id.videolisten_btn)
    Button mVideolistenBtn;
    private FragmentManager fragmentManager;
    private NewsFragment newsFragment;
    private PicSayFragment picSayFragment;
    private VideoListenFragment videoListenFragment;
    private View view;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information, container, false);
        unbinder = ButterKnife.bind(this, view);
        setTabSelection(0);
        return view;
    }


    private void setTabSelection(int i) {
        fragmentManager = getFragmentManager();
        resetBtn();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (i) {
            case 0:
                mNewsBtn.setTextColor(getResources().getColor(R.color.blue));
                mNewsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_left_choice));
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    fragmentTransaction.add(R.id.information_content, newsFragment);
                } else {
                    fragmentTransaction.show(newsFragment);
                }
                break;
            case 1:
                mPicsayBtn.setTextColor(getResources().getColor(R.color.blue));
                mPicsayBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_middle_choice));
                if (picSayFragment == null) {
                    picSayFragment = new PicSayFragment();
                    fragmentTransaction.add(R.id.information_content, picSayFragment);
                } else {
                    fragmentTransaction.show(picSayFragment);
                }
                break;
            case 2:
                mVideolistenBtn.setTextColor(getResources().getColor(R.color.blue));
                mVideolistenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_right_choice));
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
        mNewsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_left));
        mPicsayBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_middle));
        mVideolistenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_button_right));

        mNewsBtn.setTextColor(getResources().getColor(R.color.white));
        mPicsayBtn.setTextColor(getResources().getColor(R.color.white));
        mVideolistenBtn.setTextColor(getResources().getColor(R.color.white));
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

    @OnClick({R.id.news_btn, R.id.picsay_btn, R.id.videolisten_btn})
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
