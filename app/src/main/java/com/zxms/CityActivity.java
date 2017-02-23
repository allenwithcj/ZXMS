package com.zxms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.zxms.application.MyApplication;
import com.zxms.baidu.service.LocationService;
import com.zxms.fragment.HomePageFragment;
import com.zxms.model.City;
import com.zxms.utils.Constants;
import com.zxms.utils.PinyinComparator;
import com.zxms.utils.PinyinUtils;
import com.zxms.view.EditTextWithDel;
import com.zxms.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.cancel_btn)
    ImageView mCancelBtn;
    @BindView(R.id.et_search)
    EditTextWithDel mEtSearch;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.dialog)
    TextView mDialog;
    @BindView(R.id.sidrbar)
    SideBar mSidrbar;

    private List<City> cities;
    private MySortAdapter adapter;
    private Context context;
    private Button btn_city_name;
    private RecyclerView gv_hot_city;
    private MyHotCityAdapter hotAdapter;
    private LocationService locationService;
    private TextView choice_city_name;
    private String locationCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        context = CityActivity.this;
        initDate();
        onClick();
    }


    private void initDate() {
        mSidrbar.setTextView(mDialog);
        cities = sortCities(getResources().getStringArray(R.array.provinces));
        Collections.sort(cities, new PinyinComparator());
        LinearLayoutManager linear = new LinearLayoutManager(this);
        linear.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linear);
        adapter = new MySortAdapter(cities);
        mRecyclerView.setAdapter(adapter);
        //添加headview
        setHeaderView(mRecyclerView);
    }

    private void onClick() {
        mSidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    llm.scrollToPositionWithOffset(position + 1, 0);//将指定的position滑动到距离上面第0个的位置，也就是顶部
                }
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //过滤输入的城市刷新列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setHeaderView(RecyclerView recyclerView) {
        View headView = LayoutInflater.from(context).inflate(R.layout.activity_city_headview, recyclerView, false);
        adapter.setHeaderView(headView);
        btn_city_name = (Button) headView.findViewById(R.id.btn_city_name);
        gv_hot_city = (RecyclerView) headView.findViewById(R.id.gv_hot_city);
        choice_city_name = (TextView) headView.findViewById(R.id.choice_city_name);
        btn_city_name.setOnClickListener(this);

        locationCity = Constants.locationCity;
        if (!"".equals(locationCity) || !TextUtils.isEmpty(locationCity)) {
            choice_city_name.setText(locationCity);
        }
        GridLayoutManager gdm = new GridLayoutManager(this, 3);
        gv_hot_city.setLayoutManager(gdm);
        String[] datas = getResources().getStringArray(R.array.city);
        ArrayList<String> cityList = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            cityList.add(datas[i]);
        }
        hotAdapter = new MyHotCityAdapter(cityList);
        gv_hot_city.setAdapter(hotAdapter);
    }

    @OnClick({R.id.cancel_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                finish();
                overridePendingTransition(R.anim.in_left, R.anim.out_right);
                break;
            case R.id.btn_city_name:
                String city = btn_city_name.getText().toString();
                if (!city.equals(getString(R.string.localing))) {
                    choiceCity(city);
                }
                break;
        }
    }


    class MySortAdapter extends RecyclerView.Adapter<MySortAdapter.MyViewHolder> implements SectionIndexer {
        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_catagory, tv_city_name;
            private int position;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position != 0) {
                            choiceCity(cities.get(position - 1).getName());
                        }
                    }
                });

                tv_catagory = (TextView) itemView.findViewById(R.id.tv_catagory);
                tv_city_name = (TextView) itemView.findViewById(R.id.tv_city_name);
            }
        }

        private static final int TYPE_HEAD = 0;//headView
        private static final int TYPE_FOOTER = 1;//footer
        private static final int TYPE_NORMAL = 2;//normal
        private List<City> cities;
        private View headView;

        public MySortAdapter(List<City> cities) {
            this.cities = cities;
        }

        public View getHeadView() {
            return headView;
        }

        public void setHeadView(View headView) {
            this.headView = headView;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEAD;
            }
            return TYPE_NORMAL;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (headView != null && viewType == TYPE_HEAD) {
                return new MyViewHolder(headView);
            }
            View view = LayoutInflater.from(context).inflate(R.layout.activity_city_item, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.position = position;
            if (getItemViewType(position) == TYPE_NORMAL) {
                if (holder instanceof MyViewHolder) {
                    City city = cities.get(position - 1);
                    holder.tv_catagory.setText(city.getLetter());
                    int section = getSectionForPosition(position - 1);
                    if (position - 1 == getPositionForSection(section)) {
                        holder.tv_catagory.setVisibility(View.VISIBLE);
                        holder.tv_catagory.setText(city.getLetter());
                    } else {
                        holder.tv_catagory.setVisibility(View.GONE);
                    }
                    holder.tv_city_name.setText(city.getName());
                    return;
                }
                return;
            } else if (getItemViewType(position) == TYPE_HEAD) {
                return;
            } else {
                return;
            }


        }

        @Override
        public int getItemCount() {
            if (headView != null) {
                return cities.size() + 1;
            } else {
                return cities.size();
            }
        }

        @Override
        public Object[] getSections() {
            return new Object[0];
        }

        @Override
        public int getPositionForSection(int position) {
            for (int i = 0; i < cities.size(); i++) {
                String sortStr = cities.get(i).getLetter();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == position) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            return cities.get(position).getLetter().charAt(0);
        }

        public void updateListView(List<City> mSortList) {
            this.cities = mSortList;
            notifyDataSetChanged();
        }

        public void setHeaderView(View headView) {
            this.headView = headView;
            notifyItemInserted(0);
        }
    }

    private void choiceCity(String city) {
        if (!TextUtils.isEmpty(city) || !"".equals(city)) {
            Constants.locationCity = city;
            Intent intent = new Intent();
            intent.putExtra("city", city);
            setResult(HomePageFragment.LOCAL_CITY, intent);
        }
        finish();
        overridePendingTransition(R.anim.in_left, R.anim.out_right);
    }


    /**
     * 根据输入框中的值来过滤数据并更新recyclerView
     */
    public void filterData(String filterStr) {
        List<City> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = cities;
        } else {
            mSortList.clear();
            for (City sortModel : cities) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 ||
                        PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    /**
     * 城市数据排序
     */
    private List<City> sortCities(String[] date) {
        List<City> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            City sortModel = new City();
            sortModel.setName(date[i]);
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setLetter(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        mSidrbar.setIndexText(indexString);
        return mSortList;
    }

    class MyHotCityAdapter extends RecyclerView.Adapter<MyHotCityAdapter.MyHotViewHolder> {

        class MyHotViewHolder extends RecyclerView.ViewHolder {
            private Button tv_city;
            private int position;

            public MyHotViewHolder(View itemView) {
                super(itemView);
                tv_city = (Button) itemView.findViewById(R.id.tv_city);
                tv_city.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choiceCity(cityList.get(position));
                    }
                });
            }
        }

        private List<String> cityList;

        public MyHotCityAdapter(ArrayList<String> cityList) {
            this.cityList = cityList;
        }

        @Override
        public MyHotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_hotcity_item, parent, false);
            return new MyHotViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHotViewHolder holder, int position) {
            holder.position = position;
            String hot_city = cityList.get(position);
            holder.tv_city.setText(hot_city);
        }

        @Override
        public int getItemCount() {
            return cityList.size();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        locationService = MyApplication.getInstances().locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                btn_city_name.setText(location.getCity());
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
