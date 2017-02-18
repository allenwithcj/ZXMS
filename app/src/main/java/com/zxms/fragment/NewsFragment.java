package com.zxms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxms.R;
import com.zxms.view.HorizontalScrollMenu;
import com.zxms.view.MyBaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hp on 2017/1/14.
 */
public class NewsFragment extends BaseFragment {
    private String[] titles = {"头条", "无锡", "约慧", "生活", "视频", "天下", "微闻", "新华社", "博报"};
    private HorizontalScrollMenu horizontalScrollMenu;
    private MenuAdapter menuAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        horizontalScrollMenu = (HorizontalScrollMenu) view.findViewById(R.id.horizontal);
        horizontalScrollMenu.setSwiped(true);
        menuAdapter = new MenuAdapter();
        horizontalScrollMenu.setAdapter(menuAdapter);
    }

    class MenuAdapter extends MyBaseAdapter {

        @Override
        public List<String> getMenuItems() {
            return Arrays.asList(titles);
        }

        @Override
        public List<View> getContentViews() {
            List<View> views = new ArrayList<View>();
            for (String str : titles) {
                View v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.activity_horizontaal_scroll_menu_item, null);
                TextView tv = (TextView) v.findViewById(R.id.title_txt);
                tv.setText(str);
                views.add(v);
            }
            return views;

        }

        @Override
        public void onPageChanged(int position, boolean visitStatus) {

        }

    }


}
