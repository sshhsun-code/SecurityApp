package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.ProcessShowBean;
import com.example.sunqi.securityking.dataprovider.ProcessDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 进程加速Activity
 * Created by sunqi on 2017/5/17.
 */

public class ProcessActivity extends Activity implements ProcessDataProvider.DataListener{
    private ListView process_listview;

    private View process_top;
    private ImageView normal_title_back;
    private TextView normal_title_text;
    private ImageView title_items;

    private List<ProcessShowBean> mprocessShowBeanList = new ArrayList<>();

    private ProcessAdapter adapter;
    private ActivityManager activityManager;

    private int clearSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.activity_process);
        activityManager = (ActivityManager) SecurityApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        ProcessDataProvider.setDataListener(this);
        initView();
    }

    private void setStatusBarTranslate() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initView() {
        process_listview = (ListView) findViewById(R.id.process_listview);
        process_top = findViewById(R.id.process_top);
        process_top.setBackgroundResource(R.color.cn_rank_protect_bg_yellow);
        normal_title_back = (ImageView) process_top.findViewById(R.id.normal_title_back);
        normal_title_text = (TextView) process_top.findViewById(R.id.normal_title_text);
        title_items = (ImageView) process_top.findViewById(R.id.title_items);
        normal_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        normal_title_text.setText("后台进程");
        title_items.setVisibility(View.GONE);
        process_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pkgName = mprocessShowBeanList.get(position).getPakName();
                int size = mprocessShowBeanList.get(position).getSize();
                clearSize += size;
                activityManager.killBackgroundProcesses(pkgName);
                mprocessShowBeanList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProcessDataProvider.requestBackUpProcessData(SecurityApplication.getInstance());
    }

    @Override
    public void onDataFinished(List<ProcessShowBean> processShowBeanList) {
        this.mprocessShowBeanList = processShowBeanList;
        if (mprocessShowBeanList != null && !mprocessShowBeanList.isEmpty()) {
            adapter = new ProcessAdapter(mprocessShowBeanList);
            process_listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ProcessActivity.this, "后台进程查询失败...",Toast.LENGTH_LONG).show();
        }
    }

    static class ProcessAdapter extends BaseAdapter {
        private List<ProcessShowBean> processShowBeanList;

        public ProcessAdapter(List<ProcessShowBean> list) {
            this.processShowBeanList = list;
        }

        @Override
        public int getCount() {
            return processShowBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return processShowBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProcessShowBean ProcessShowBean = processShowBeanList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(SecurityApplication.getInstance()).inflate(R.layout.process_app_item, null);
            }

            ((ImageView)convertView.findViewById(R.id.process_item_icon)).setImageDrawable(ProcessShowBean.getAppIcon());
            ((TextView)convertView.findViewById(R.id.process_item_name)).setText(ProcessShowBean.getAppName());
            ((TextView)convertView.findViewById(R.id.process_item_size)).setText(ProcessShowBean.getSize()+"MB");
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(SecurityApplication.getInstance(), "释放内存"+clearSize+"MB",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
