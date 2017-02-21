package com.zxms.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.zxms.ClipImageActivity;
import com.zxms.LoginActivity;
import com.zxms.R;
import com.zxms.model.Defaultcontent;
import com.zxms.model.My;
import com.zxms.utils.AsyncImageLoader;
import com.zxms.utils.Constants;
import com.zxms.utils.Tools;
import com.zxms.view.ActionSheetDialog;
import com.zxms.view.CircleImageView;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by hp on 2017/1/14.
 * 我的
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MyFragment";
    private static final int REQUEST_CAPTURE = 100;//请求相机
    private static final int REQUEST_PICK = 101;//请求相册
    private static final int REQUEST_CROP_PHOTO = 102;//请求截图
    @BindView(R.id.set)
    ImageView mSet;
    @BindView(R.id.user_img)
    CircleImageView mUserImg;
    @BindView(R.id.goLogin_btn)
    Button mGoLoginBtn;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.message)
    ImageView mMessage;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private String[] names = {"我的收藏", "积分查询", "今日任务", "我的评论", "我的报料",
            "我的互助", "商城订单", "积分兑换单", "我的银行", "我的打车", "我的医院"};
    private int[] imgs = {R.drawable.mine_unlogin_favor, R.drawable.mine_unlogin_jifen, R.drawable.mine_unlogin_task, R.drawable.mine_unlogin_comment,
            R.drawable.mine_unlogin_submit, R.drawable.mine_unlogin_help, R.drawable.mine_unlogin_order, R.drawable.mine_unlogin_duihuan, R.drawable.mine_unlogin_bank,
            R.drawable.mine_unlogin_taxi, R.drawable.mine_unlogin_hospital};
    private MyAdapter adapter;
    private LinkedList<My> myalls;
    private AsyncImageLoader asyncImageLoader;
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private LoginReciver loginReciver;
    private File tempFile;
    private View view;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        asyncImageLoader = new AsyncImageLoader();
        mShareListener = new CustomShareListener();
        loginReciver = new LoginReciver();
        //广播注册
        getActivity().registerReceiver(loginReciver, new IntentFilter(Constants.THIRDPARTYLOGIN));
        initDate();
        initView(view);
        return view;
    }

    /**
     * 控件初始化
     */
    private void initView(View view) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new MyAdapter(myalls);
        mRecyclerView.setAdapter(adapter);

        mSet.setOnClickListener(this);
        mUserImg.setOnClickListener(this);
        mGoLoginBtn.setOnClickListener(this);

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

    /**
     * 宫格数据数据初始化
     */
    private void initDate() {
        myalls = new LinkedList<My>();
        for (int i = 0; i < names.length; i++) {
            My my = new My();
            my.setImg(imgs[i]);
            my.setName(names[i]);
            myalls.add(my);
        }
    }


    /**
     * 登录：判断是否已经登录
     * 1.登录成功，点击头像替换头像
     * 2.没有登录，点击则跳转到登录界面
     */
    private void goLogin() {
        if (!Constants.isLogin) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_right, R.anim.out_left);
        } else {
            new ActionSheetDialog(getActivity()).builder()
                    .setTitle("头像")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("相册选取", null, new ActionSheetDialog.OnSheetItemClickListener() {

                        @Override
                        public void onClick(int which) {
                            // 相册中选择
                            //权限判断
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                //申请READ_EXTERNAL_STORAGE权限
//                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                            } else {
                                //跳转到调用系统图库
                                gotoPhoto();
                            }
                        }
                    })
                    .addSheetItem("相机拍照", null, new ActionSheetDialog.OnSheetItemClickListener() {

                        @Override
                        public void onClick(int which) {
                            gotoCarema();
                        }
                    }).show();
        }
    }

    @OnClick({R.id.set, R.id.user_img, R.id.goLogin_btn})
    public void onClick(View v) {
        switch (v.getId()) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    /**
     * 分享和收藏监听
     */
    private class CustomShareListener implements UMShareListener {

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

    /**
     * 监听第三方登录成功后获取用户信息：头像和昵称
     */
    UMAuthListener authListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (map != null) {
                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_LONG).show();
                Constants.isLogin = true;
                mGoLoginBtn.setVisibility(View.GONE);
                mName.setVisibility(View.VISIBLE);
                mName.setText(map.get("name"));
                Constants.userName = mName.getText().toString();
                tempFile = new File(Tools.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                        Constants.userName + ".jpg");
                asyncImageLoader.loadDrawable(map.get("iconurl"), new AsyncImageLoader.ImageCallback() {
                    @Override
                    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                        mUserImg.setImageDrawable(imageDrawable);
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

    /**
     * 接收第三方登录后发送的广播
     */
    class LoginReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.THIRDPARTYLOGIN) {
                SHARE_MEDIA share_media = (SHARE_MEDIA) intent.getSerializableExtra("share_media");
                UMShareAPI.get(getActivity()).getPlatformInfo(getActivity(), share_media, authListener);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAPTURE: //调用系统相机返回
                    if (data != null) {
                        gotoClipActivity(Uri.fromFile(tempFile));
                    }
                    break;
                case REQUEST_PICK:  //调用系统相册返回
                    if (data != null) {
                        Uri uri = data.getData();
                        gotoClipActivity(uri);
                    }
                    break;
                case REQUEST_CROP_PHOTO:  //剪切图片返回
                    if (data != null) {
                        final Uri uri = data.getData();
                        if (uri == null) {
                            return;
                        }
                        String cropImagePath = getRealFilePathFromUri(getActivity(), uri);
                        Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                        mUserImg.setImageBitmap(bitMap);
                        //此处后面可以将bitMap转为二进制上传后台网络
                        //......
                    }

                    break;
            }
        }

    }

    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }

    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < 24) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", tempFile));
        }
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAPTURE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(getActivity()).release();
        getActivity().unregisterReceiver(loginReciver);
    }

}
