package com.zxms.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hp on 2017/1/16.
 */
public class MyViewPager extends ViewPager {

    private boolean mSwiped = true; // 是否可滑动

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwiped(boolean swiped) {
        mSwiped = swiped;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mSwiped) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((mSwiped)) {
            return super.onTouchEvent(event);
        } else {
            return true;
        }
    }
}
