package com.zxms.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zxms.LoginActivity;
import com.zxms.R;
import com.zxms.model.Defaultcontent;
import com.zxms.model.My;
import com.zxms.utils.AsyncImageLoader;
import com.zxms.utils.Constants;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by hp on 2017/1/14.
 * 我的
 */
public class MyFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "MyFragment";
    private String[] names = {"我的收藏", "积分查询", "今日任务", "我的评论", "我的报料",
            "我的互助", "商城订单", "积分兑换单", "我的银行", "我的打车", "我的医院"};
    private int[] imgs = {R.drawable.mine_unlogin_favor, R.drawable.mine_unlogin_jifen, R.drawable.mine_unlogin_task, R.drawable.mine_unlogin_comment,
            R.drawable.mine_unlogin_submit, R.drawable.mine_unlogin_help, R.drawable.mine_unlogin_order, R.drawable.mine_unlogin_duihuan, R.drawable.mine_unlogin_bank,
            R.drawable.mine_unlogin_taxi, R.drawable.mine_unlogin_hospital};
    private ImageView set, user_img;
    private Button goLogin_btn;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private LinkedList<My> myalls;
    private TextView name;
    private AsyncImageLoader asyncImageLoader;
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private LoginReciver loginReciver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my, container, false);
        asyncImageLoader = new AsyncImageLoader();
        mShareListener = new CustomShareListener();
        loginReciver = new LoginReciver();
        getActivity().registerReceiver(loginReciver, new IntentFilter(Constants.THIRDPARTYLOGIN));
        initDate();
        initView(view);
        return view;
    }

    private void initView(View view) {
        set = (ImageView) view.findViewById(R.id.set);
        user_img = (ImageView) view.findViewById(R.id.user_img);
        goLogin_btn = (Button) view.findViewById(R.id.goLogin_btn);
        name = (TextView) view.findViewById(R.id.name);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new MyAdapter(myalls);
        recyclerView.setAdapter(adapter);

        set.setOnClickListener(this);
        user_img.setOnClickListener(this);
        goLogin_btn.setOnClickListener(this);

        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(getActivity()).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.MORE)
                .addButton("umeng_sharebutton_copy", "umeng_sharebutton_copy", "umeng_socialize_copy", "umeng_socialize_copy")
                .addButton("umeng_sharebutton_copyurl", "umeng_sharebutton_copyurl", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copy")) {
                            Toast.makeText(getActivity(), "复制文本按钮", Toast.LENGTH_LONG).show();
                        } else if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            Toast.makeText(getActivity(), "复制链接按钮", Toast.LENGTH_LONG).show();
                        } else {
                            new ShareAction(getActivity()).withText(Defaultcontent.text + "来自友盟自定义分享面板")
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });
    }


    private void initDate() {
        myalls = new LinkedList<My>();
        for (int i = 0; i < names.length; i++) {
            My my = new My();
            my.setImg(imgs[i]);
            my.setName(names[i]);
            myalls.add(my);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_img:
                goLogin();
                break;

            case R.id.goLogin_btn:
                goLogin();
                break;

            case R.id.set:
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                mShareAction.open(config);
                break;
        }

    }

    private void goLogin() {
        if(!Constants.isLogin){
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_right,R.anim.out_left);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private LinkedList<My> myalls = null;

        public MyAdapter(LinkedList<My> myalls) {
            this.myalls = myalls;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_item, null);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.position = position;
            My my = myalls.get(position);
            int drawable = my.getImg();
            holder.my_icon.setBackgroundDrawable(getResources().getDrawable(drawable));
            holder.my_name.setText(my.getName());
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return myalls.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView my_icon;
        private TextView my_name;
        private int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), myalls.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            my_icon = (ImageView) itemView.findViewById(R.id.my_icon);
            my_name = (TextView) itemView.findViewById(R.id.my_name);
        }
    }

    private class CustomShareListener implements UMShareListener{

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(getActivity(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE) {
                    Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE) {
                Toast.makeText(getActivity(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    UMAuthListener authListener = new UMAuthListener(){

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.d("authListener","查询用户信息监听:"+map);
            if(map != null){
                goLogin_btn.setVisibility(View.GONE);
                name.setVisibility(View.VISIBLE);
                name.setText(map.get("name"));
                asyncImageLoader.loadDrawable(map.get("iconurl"), new AsyncImageLoader.ImageCallback() {
                    @Override
                    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                        user_img.setImageDrawable(imageDrawable);
                    }
                });

            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };


    class LoginReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LoginReciver","===收到广播===");
            if(intent.getAction() == Constants.THIRDPARTYLOGIN){
                SHARE_MEDIA share_media = (SHARE_MEDIA)intent.getSerializableExtra("share_media");
                UMShareAPI.get(getActivity()).getPlatformInfo(getActivity(),share_media,authListener);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(getActivity()).release();
        getActivity().unregisterReceiver(loginReciver);
    }
}
