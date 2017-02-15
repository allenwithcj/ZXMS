package com.zxms.view;

import android.view.View;

import java.util.List;

/**
 * Created by hp on 2017/1/16.
 */
public abstract class MyBaseAdapter {
    private HorizontalScrollMenu mHorizontalScrollMenu;

    public abstract List<String> getMenuItems();

    public abstract List<View> getContentViews();

    public abstract void onPageChanged(int position, boolean visitStatus);

    public void setHorizontalScrollMenu(HorizontalScrollMenu horizontalScrollMenu) {
        mHorizontalScrollMenu = horizontalScrollMenu;
    }

    public void notifyDataSetChanged() {
        mHorizontalScrollMenu.notifyDataSetChanged(this);
    }
}
