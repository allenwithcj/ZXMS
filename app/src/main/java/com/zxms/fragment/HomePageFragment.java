package com.zxms.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxms.CityActivity;
import com.zxms.MyCaptureActivity;
import com.zxms.R;
import com.zxms.model.HomePageMenu;
import com.zxms.utils.AsyncImageLoader;
import com.zxms.utils.Tools;
import com.zxms.zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/1/14.
 * 首页
 */
public class HomePageFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "HomePageFragment";
    public static final int LOCAL_CITY = 1001;
    private ViewPager viewPager_adv;
    private ViewPager viewPager_menu;
    private MyAdvViewPagerAdapter advAdapter;
    private MyMenuViewPagerAdapter menuAdapter;
    private String[] url = {"http://pic.qiantucdn.com/58pic/17/94/38/55b08aabda62f_1024.jpg",
            "http://img.zcool.cn/community/01f5ce56e112ef6ac72531cb37bec4.png@900w_1l_2o_100sh.jpg",
            "http://img.zcool.cn/community/01517657ad89500000012e7e98f5c7.jpg@900w_1l_2o_100sh.jpg"};
    private String[] name = {"新闻", "直播", "商城", "公交", "列车", "地铁", "报料", "专题", "自行车", "微博圈", "航班", "景点", "更多"};
    private int[] img = {R.drawable.customize_icon_news,
            R.drawable.customize_icon_live,
            R.drawable.customize_icon_huigou,
            R.drawable.customize_icon_keyun,
            R.drawable.customize_icon_lieche,
            R.drawable.customize_icon_metro,
            R.drawable.customize_icon_contribute,
            R.drawable.customize_icon_topic,
            R.drawable.customize_icon_bicycle,
            R.drawable.customize_icon_weibo,
            R.drawable.customize_icon_hangban,
            R.drawable.customize_icon_travel,
            R.drawable.customize_icon_life};
    private ImageView[] indicator_adv_imgs = new ImageView[url.length];
    private ImageView[] indicator_menu_imgs;
    private List<View> advViewList;
    private List<View> menuViewList;
    private List<HomePageMenu> homePageMenuList;
    private View adItemView;
    private View menuItemView;
    private ImageView menu_add_btn;
    private int totalPage;
    private int pageSize = 10;//页面显示的个数
    private MyGridViewAdapter gridAdapter;
    private boolean isOpenClick = true;
    private RotateAnimation rotate_right,rotate_left;
    private LinearLayout shake_layout,rebelion_layout,scan_layout,search_layout;
    private PopupWindow popupWindow;
    private SwipeRefreshLayout swipeLayout;
    private RelativeLayout title_layout;
    private View popuView;
    public static final int REQUEST_CAMERA = 101;//照相机权限
    public static final int REQUEST_CODE = 102;//选择相册
    private TextView city;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager_adv.setCurrentItem(viewPager_adv.getCurrentItem()+1);
            mHandler.sendEmptyMessageDelayed(0,2000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_homepage, container, false);
        initDateAdv();
        initMenuDate();
        initView(view);
        initIndicator_adv(view);
        initIndicator_menu(view);
        return view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int currentItem = viewPager_adv.getCurrentItem();
                currentItem++;
                viewPager_adv.setCurrentItem(currentItem, false);
            }
        }
    };


    /**
     * 加载广告页面数据
     */
    private void initDateAdv() {
        advViewList = new ArrayList<View>();
        for (int x = 0; x < url.length; x++) {
            adItemView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_homepage_adv_item, null);
            advViewList.add(adItemView);
        }
    }

    /**
     * 加载菜单页面数据
     */
    private void initMenuDate() {
        homePageMenuList = new ArrayList<HomePageMenu>();
        for (int i = 0; i < name.length; i++) {
            HomePageMenu homePageMenu = new HomePageMenu();
            homePageMenu.setName(name[i]);
            homePageMenu.setImg(img[i]);
            homePageMenuList.add(homePageMenu);
        }
        menuViewList = new ArrayList<View>();
        totalPage = (int) Math.ceil(homePageMenuList.size() * 1.0 / pageSize);

        for (int j = 0; j < totalPage; j++) {
            menuItemView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_homepage_menu_item, null);
            GridView gridView = (GridView) menuItemView.findViewById(R.id.gridView);
            gridAdapter = new MyGridViewAdapter(homePageMenuList, j, pageSize);
            gridView.setAdapter(gridAdapter);
            menuViewList.add(gridView);

            final int finalJ = j;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(parent.getContext(), homePageMenuList.get(position + pageSize * finalJ).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initView(final View view) {
        indicator_menu_imgs = new ImageView[totalPage];
        rotate_right = new RotateAnimation(0f,45f,Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate_left = new RotateAnimation(45f,0f,Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f) ;

        viewPager_adv = (ViewPager) view.findViewById(R.id.view_pager_adv);
        viewPager_menu = (ViewPager) view.findViewById(R.id.view_pager_menu);
        menu_add_btn = (ImageView) view.findViewById(R.id.menu_add_btn);
        city = (TextView) view.findViewById(R.id.city);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        title_layout = (RelativeLayout) view.findViewById(R.id.title_layout);

        advAdapter = new MyAdvViewPagerAdapter(advViewList);
        viewPager_adv.setAdapter(advAdapter);
        viewPager_adv.setCurrentItem(1000);

        menuAdapter = new MyMenuViewPagerAdapter(menuViewList);
        viewPager_menu.setAdapter(menuAdapter);
        viewPager_menu.setCurrentItem(0);

        menu_add_btn.setOnClickListener(this);
        viewPager_adv.setOnPageChangeListener(new MyPageChangeListener(0));
        viewPager_menu.setOnPageChangeListener(new MyPageChangeListener(1));

        //初始化popview
        popuView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_popmenu,null);
        int title_height = title_layout.getLayoutParams().height;
        popuView.setAlpha(0.9f);//设置透明度
        int screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        int statusBarHeight = Tools.getStatusBarHeight(getActivity());
        popupWindow = new PopupWindow(popuView,ViewGroup.LayoutParams.MATCH_PARENT,
                screenHeight - title_height - statusBarHeight);
        popupWindow.setFocusable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        shake_layout = (LinearLayout) popuView.findViewById(R.id.shake_layout);
        rebelion_layout = (LinearLayout) popuView.findViewById(R.id.rebelion_layout);
        scan_layout = (LinearLayout) popuView.findViewById(R.id.scan_layout);
        search_layout = (LinearLayout) popuView.findViewById(R.id.search_layout);

        shake_layout.setOnClickListener(this);
        rebelion_layout.setOnClickListener(this);
        scan_layout.setOnClickListener(this);
        search_layout.setOnClickListener(this);
        city.setOnClickListener(this);

        popuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() != R.id.shake_layout || v.getId() != R.id.rebelion_layout || v.getId() != R.id.rebelion_layout || v.getId() != R.id.scan_layout ||
                        v.getId() != R.id.search_layout){
                    dismissPopuView();
                }
                return false;
            }
        });
    }

    private void initIndicator_adv(View view) {
        View v = view.findViewById(R.id.indicator_adv);
        for (int i = 0; i < url.length; i++) {
            ImageView img = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.setMargins(3, 10, 3, 10);
            img.setLayoutParams(layoutParams);
            indicator_adv_imgs[i] = img;
            if (i == 0) {
                indicator_adv_imgs[i].setBackgroundResource(R.drawable.indicator_select);
            } else {
                indicator_adv_imgs[i].setBackgroundResource(R.drawable.indicator_no_select);
            }
            ((ViewGroup) v).addView(indicator_adv_imgs[i]);
        }
    }

    private void initIndicator_menu(View view) {
        View v = view.findViewById(R.id.indicator_menu);
        for (int i = 0; i < totalPage; i++) {
            ImageView img = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.setMargins(3, 10, 3, 10);
            img.setLayoutParams(layoutParams);
            indicator_menu_imgs[i] = img;
            if (i == 0) {
                indicator_menu_imgs[i].setBackgroundResource(R.drawable.indicator_select_menu);
            } else {
                indicator_menu_imgs[i].setBackgroundResource(R.drawable.indicator_no_select_menu);
            }
            ((ViewGroup) v).addView(indicator_menu_imgs[i]);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }else if(requestCode == LOCAL_CITY){
            if(null != data){
                city.setText(data.getExtras().getString("city"));
            }

        }
    }

    private void startRotateAnimation(RotateAnimation animation, ImageView menu_add_btn){
        animation.setDuration(500);
        animation.setFillAfter(true);
        menu_add_btn.startAnimation(animation);
    }

    private void showPopuView() {
        isOpenClick = false;
        startRotateAnimation(rotate_right,menu_add_btn);
        popupWindow.setAnimationStyle(R.style.popView_animation_style);
        popupWindow.showAtLocation(swipeLayout,Gravity.BOTTOM,0,0);

    }

    private void dismissPopuView() {
        isOpenClick = true;
        startRotateAnimation(rotate_left,menu_add_btn);
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    @Override
    public void onRefresh() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city:
                Intent intent = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(intent,LOCAL_CITY);
                getActivity().overridePendingTransition(R.anim.in_right,R.anim.out_left);
                break;
            case R.id.menu_add_btn:
                if(isOpenClick){
                    showPopuView();
                }else{
                    dismissPopuView();
                }
                break;
            case R.id.scan_layout:
                dismissPopuView();
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请CAMERA权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA);
                } else {
                    gotoCarema();
                }
                break;
        }
    }

    public class MyAdvViewPagerAdapter extends PagerAdapter {
        private List<View> viewList;
        private AsyncImageLoader asyncImageLoader;

        public MyAdvViewPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
            asyncImageLoader = new AsyncImageLoader();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }


        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
              asyncImageLoader.loadDrawable(url[position % viewList.size()], new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    View view = viewList.get(position % viewList.size());
                    ImageView imageView = (ImageView) view.findViewById(R.id.adv_img);
                    imageView.setBackgroundDrawable(imageDrawable);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "页面" + position % viewList.size(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    view.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if(event.getAction() == MotionEvent.ACTION_DOWN){
                                mHandler.removeMessages(0);
                            }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                                mHandler.removeMessages(0);
                            }else if(event.getAction() == MotionEvent.ACTION_UP){
                                mHandler.sendEmptyMessageDelayed(0,2000);
                            }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                                mHandler.sendEmptyMessageDelayed(0,2000);
                            }
                            return false;
                        }
                    });

                }
            });
            container.removeView(viewList.get(position % viewList.size()));
            container.addView(viewList.get(position % viewList.size()));

            return viewList.get(position % viewList.size());
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int index;

        public MyPageChangeListener(int i) {
            this.index = i;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (index) {
                case 0:
                    for (int s = 0; s < indicator_adv_imgs.length; s++) {
                        indicator_adv_imgs[s].setBackgroundResource(R.drawable.indicator_no_select);
                    }
                    indicator_adv_imgs[position % advViewList.size()].setBackgroundResource(R.drawable.indicator_select);
                    break;
                case 1:
                    for (int s = 0; s < indicator_menu_imgs.length; s++) {
                        indicator_menu_imgs[s].setBackgroundResource(R.drawable.indicator_no_select_menu);
                    }
                    indicator_menu_imgs[position].setBackgroundResource(R.drawable.indicator_select_menu);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public class MyMenuViewPagerAdapter extends PagerAdapter {
        private List<View> menuViewList;

        public MyMenuViewPagerAdapter(List<View> menuViewList) {
            this.menuViewList = menuViewList;
        }

        @Override
        public int getCount() {
            return menuViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(menuViewList.get(position));
            return menuViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(menuViewList.get(position));
        }
    }

    public class MyGridViewAdapter extends BaseAdapter {
        private List<HomePageMenu> homePageMenuList;
        private int index;
        private int pageSzie;

        public MyGridViewAdapter(List<HomePageMenu> homePageMenuList, int index, int pageSize) {
            this.homePageMenuList = homePageMenuList;
            this.index = index;
            this.pageSzie = pageSize;
        }

        @Override
        public int getCount() {
            return homePageMenuList.size() > (index + 1) * pageSzie ?
                    pageSize : (homePageMenuList.size() - index * pageSzie);
        }

        @Override
        public Object getItem(int position) {
            return homePageMenuList.get(position + pageSzie * index);
        }

        @Override
        public long getItemId(int position) {
            return position + pageSzie * index;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int pos = position + pageSzie * index;
            HomePageMenu homePageMenu = homePageMenuList.get(pos);
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_menu_gridview_item, null);
                holder = new ViewHolder();
                holder.menu_icon = (ImageView) convertView.findViewById(R.id.menu_icon);
                holder.menu_name = (TextView) convertView.findViewById(R.id.menu_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.menu_icon.setBackgroundDrawable(getResources().getDrawable(homePageMenu.getImg()));
            holder.menu_name.setText(homePageMenu.getName());
            return convertView;
        }
    }

    class ViewHolder {
        private ImageView menu_icon;
        private TextView menu_name;
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeMessages(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(0,2000);
    }

    /**
     * 外部存储权限申请返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCarema();
            } else {
                // Permission Denied
            }
        }
}
    private void gotoCarema() {
        Intent intent = new Intent(getActivity(), MyCaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


}