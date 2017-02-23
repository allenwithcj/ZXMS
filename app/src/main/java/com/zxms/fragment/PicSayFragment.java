package com.zxms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxms.R;
import com.zxms.model.PictureSay;
import com.zxms.utils.AsyncImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hp on 2017/1/14.
 */
public class PicSayFragment extends Fragment {
    private String[] picUrl = {"http://cms-bucket.nosdn.127.net/15159dc4065c4d079af6c2eec713177e20170221004640.png?imageView&thumbnail=550x0",
     "http://cms-bucket.nosdn.127.net/dbe3f6546ed8449c8276bb0c84eb5e8520170223121831.png",
     "http://cms-bucket.nosdn.127.net/catchpic/d/d4/d4147072826a292162e9b8ed7fdc5686.jpg?imageView&thumbnail=550x0"};
    private String[] picContent = {"打鸡血，曾风靡中国的养生法","这一代95后更爱国？多数相信社会公平宜居","习近平如何向世界讲中国故事?"};
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private View view;
    private Unbinder unbinder;
    private List<PictureSay> pictureSays;
    private AsyncImageLoader asyncImageLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_picsay, container, false);
        unbinder = ButterKnife.bind(this, view);
        asyncImageLoader = new AsyncImageLoader();
        initDate();
        return view;
    }

    private void initDate() {
        pictureSays = new ArrayList<>();
        for(int i = 0;i<picUrl.length;i++){
            PictureSay ps = new PictureSay();
            ps.setUrl(picUrl[i]);
            ps.setContent(picContent[i]);
            pictureSays.add(ps);
        }
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sm);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
