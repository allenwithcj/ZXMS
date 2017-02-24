package com.zxms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxms.R;
import com.zxms.fragment.test.Fragment01;
import com.zxms.fragment.test.Fragment02;
import com.zxms.fragment.test.Fragment03;
import com.zxms.fragment.test.Fragment04;
import com.zxms.fragment.test.Fragment05;
import com.zxms.fragment.test.Fragment06;
import com.zxms.fragment.test.Fragment07;
import com.zxms.fragment.test.Fragment08;
import com.zxms.fragment.test.Fragment09;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hp on 2017/1/14.
 */
public class NewsFragment extends Fragment {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.fragment_tab_item)
    TabLayout mFragmentTabItem;
    private String[] titles = {"头条", "无锡", "约慧", "生活", "视频", "天下", "微闻", "新华社", "博报"};
    private View view;
    private Unbinder unbinder;
    private List<Fragment> fragmentList;
    private List<String> listTitles;
    private MyFragmentPagerAdapter adapter;
    private Fragment fragment01,fragment02,fragment03,fragment04,fragment05,
            fragment06,fragment07,fragment08,fragment09;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        initFragments();
        return view;
    }

    private void initFragments() {
        fragment01 = new Fragment01();
        fragment02 = new Fragment02();
        fragment03 = new Fragment03();
        fragment04 = new Fragment04();
        fragment05 = new Fragment05();
        fragment06 = new Fragment06();
        fragment07 = new Fragment07();
        fragment08 = new Fragment08();
        fragment09 = new Fragment09();

        fragmentList = new ArrayList<>();
        fragmentList.add(fragment01);
        fragmentList.add(fragment02);
        fragmentList.add(fragment03);
        fragmentList.add(fragment04);
        fragmentList.add(fragment05);
        fragmentList.add(fragment06);
        fragmentList.add(fragment07);
        fragmentList.add(fragment08);
        fragmentList.add(fragment09);

        listTitles = Arrays.asList(titles);
        //设置滑动
        mFragmentTabItem.setTabMode(TabLayout.MODE_SCROLLABLE);
        for(String title : titles){
            mFragmentTabItem.addTab(mFragmentTabItem.newTab().setText(title));
        }
        adapter = new MyFragmentPagerAdapter(getActivity()
                .getSupportFragmentManager(),fragmentList,listTitles);
        mViewPager.setAdapter(adapter);
        mFragmentTabItem.setupWithViewPager(mViewPager);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragmentList;
        private List<String> listTitles;


        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> listTitles) {
            super(fm);
            this.fragmentList = fragmentList;
            this.listTitles = listTitles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listTitles.get(position % listTitles.size());
        }
    }
}
