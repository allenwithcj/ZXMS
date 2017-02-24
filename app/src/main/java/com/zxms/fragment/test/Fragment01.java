package com.zxms.fragment.test;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxms.R;
import com.zxms.model.PictureSay;
import com.zxms.utils.AsyncImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hp on 2017/2/24.
 */

public class Fragment01 extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private View view;
    private Unbinder unbinder;
    private String[] picUrl = {"http://pic.qiantucdn.com/58pic/17/94/38/55b08aabda62f_1024.jpg",
            "http://img.zcool.cn/community/01f5ce56e112ef6ac72531cb37bec4.png@900w_1l_2o_100sh.jpg",
            "http://img.zcool.cn/community/01517657ad89500000012e7e98f5c7.jpg@900w_1l_2o_100sh.jpg"};
    private String[] picContent = {"打鸡血，曾风靡中国的养生法","这一代95后更爱国？多数相信社会公平宜居","习近平如何向世界讲中国故事?"};
    private List<PictureSay> pictureSayList;
    private MyFragmentAdapter adapter;
    private AsyncImageLoader asyncImageLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment01, container, false);
        unbinder = ButterKnife.bind(this, view);
        asyncImageLoader = new AsyncImageLoader();
        initDate();
        return view;
    }

    private void initDate() {
        pictureSayList = new ArrayList<>();
        for(int i = 0;i<picUrl.length;i++){
            PictureSay ps = new PictureSay();
            ps.setUrl(picUrl[i]);
            ps.setContent(picContent[i]);
            pictureSayList.add(ps);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyFragmentAdapter(pictureSayList);
        mRecyclerView.setAdapter(adapter);
    }

    class MyFragmentAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<PictureSay> pictureSayList;
        public MyFragmentAdapter(List<PictureSay> pictureSayList) {
            this.pictureSayList = pictureSayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_picsay_item,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            PictureSay say = pictureSayList.get(position);
            holder.pic_content.setText(say.getContent());
            asyncImageLoader.loadDrawable(say.getUrl(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    holder.pic_img.setBackgroundDrawable(imageDrawable);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pictureSayList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView pic_img;
        private TextView pic_content;
        public MyViewHolder(View itemView) {
            super(itemView);
            pic_img = (ImageView) itemView.findViewById(R.id.pic_img);
            pic_content = (TextView) itemView.findViewById(R.id.pic_content);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
