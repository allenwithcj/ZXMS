package com.zxms.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxms.R;
import com.zxms.model.Module;
import com.zxms.model.ModuleContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/1/14.
 * 模块
 */
public class ModuleFragment extends Fragment {
    private String[] titles = {"资讯", "视听"};
    private String[] moduleName1 = {"新闻", "专题"};
    private int[] moduleImg1 = {R.drawable.module_icon_news, R.drawable.module_icon_topic};
    private String[] moduleName2 = {"直播", "点播", "4G影院"};
    private int[] moduleImg2 = {R.drawable.module_icon_live, R.drawable.module_icon_vod, R.drawable.module_icon_4g};
    private RecyclerView recyclerView;
    private MyMoudleAdapter adapter;
    private MyMoudleItemAdapter itemAdapter;
    private List<Module> moduleList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_module, container, false);
        initDate(view);
        initView(view);
        return view;
    }

    /**
     * 加载数据
     *
     * @param view
     */
    private void initDate(View view) {
        List<ModuleContent> mod1 = new ArrayList<ModuleContent>();
        List<ModuleContent> mod2 = new ArrayList<ModuleContent>();
        List<List<ModuleContent>> list = new ArrayList<List<ModuleContent>>();
        for (int i = 0; i < moduleName1.length; i++) {
            ModuleContent m1 = new ModuleContent(moduleName1[i], moduleImg1[i]);
            mod1.add(m1);
        }
        for (int j = 0; j < moduleName2.length; j++) {
            ModuleContent m2 = new ModuleContent(moduleName2[j], moduleImg2[j]);
            mod2.add(m2);
        }
        list.add(mod1);
        list.add(mod2);
        moduleList = new ArrayList<Module>();
        for (int x = 0; x < titles.length; x++) {
            Module module = new Module();
            module.setTitle(titles[x]);
            module.setModuleContents(list.get(x));
            moduleList.add(module);
        }
    }


    /**
     * 初始化组件
     *
     * @param view
     */
    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new MyMoudleAdapter(moduleList);
        recyclerView.setAdapter(adapter);
    }

    public class MyMoudleAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Module> moduleList;

        public MyMoudleAdapter(List<Module> moduleList) {
            this.moduleList = moduleList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_module_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.position = position;
            final Module module = moduleList.get(position);
            holder.module_title.setText(module.getTitle());
            itemAdapter = new MyMoudleItemAdapter(getActivity());
            holder.gridView.setAdapter(itemAdapter);
            itemAdapter.addAll(module.getModuleContents());
            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(),module.getModuleContents().get(position).getName(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return moduleList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView module_title;
        private GridView gridView;
        private int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            module_title = (TextView) itemView.findViewById(R.id.module_title);
            gridView = (GridView) itemView.findViewById(R.id.gridView);
        }

    }

    public class MyMoudleItemAdapter extends ArrayAdapter<ModuleContent> {

        public MyMoudleItemAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            ModuleContent moduleContent = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_module_content_item, null);
                holder = new ViewHolder();
                holder.my_icon = (ImageView) convertView.findViewById(R.id.my_icon);
                holder.my_name = (TextView) convertView.findViewById(R.id.my_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.my_icon.setBackgroundDrawable(getResources().getDrawable(moduleContent.getImg()));
            holder.my_name.setText(moduleContent.getName());
            return convertView;
        }
        //        public MyMoudleItemAdapter(List<ModuleContent> date) {
//            this.date = date;
//        }
//
//        @Override
//        public MyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_module_content_item,null);
//            MyItemViewHolder myItemViewHolder = new MyItemViewHolder(view);
//            return myItemViewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(MyItemViewHolder holder, int position) {
//            holder.position = position;
//            ModuleContent moduleContent = date.get(position);
//            holder.my_name.setText(moduleContent.getName());
//            holder.my_icon.setBackgroundDrawable(getResources().getDrawable(moduleContent.getImg()));
//        }
//
//        @Override
//        public int getItemCount() {
//            return date.size();
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return super.getItemViewType(position);
//        }
    }

    private class ViewHolder {
        private TextView my_name;
        private ImageView my_icon;
    }
//    private class MyItemViewHolder extends RecyclerView.ViewHolder{
//        private TextView my_name;
//        private ImageView my_icon;
//        private int position;
//
//        public MyItemViewHolder(final View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            my_name = (TextView)itemView.findViewById(R.id.my_name);
//            my_icon = (ImageView)itemView.findViewById(R.id.my_icon);
//        }
//    }

}
