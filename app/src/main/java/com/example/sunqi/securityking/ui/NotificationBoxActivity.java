package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.bean.NotifyData;
import com.example.sunqi.securityking.dataprovider.NotifyDataProcessor;
import com.example.sunqi.securityking.global.Constant;

import java.util.ArrayList;

/**
 * 通知栏信息拦截Activity
 * Created by sshunsun on 2017/4/30.
 */
public class NotificationBoxActivity extends Activity implements View.OnClickListener{

    private static Context mcontext;
    private static ArrayList<NotifyData> datalist;
    private String uriStr = Constant.URI.NOTIFY_DATA_URI;
    private NotifyAdapter mAdapter;

    private ListView mNotifyListView;
    NorifyDataObserver norifyDataObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.notify_box_layout);
        initData();
        initView();
        iniTitle();
    }

    private void initView() {
        mNotifyListView = (ListView) findViewById(R.id.notify_list);
        mAdapter = new NotifyAdapter(datalist ,mcontext);
        mNotifyListView.setAdapter(mAdapter);
    }

    private void initData() {
        mcontext = this;
        datalist = new ArrayList<>();
        Uri uri = Uri.parse(uriStr);
        getContentResolver().registerContentObserver(uri, true, norifyDataObserver);
        norifyDataObserver = new NorifyDataObserver();
    }

    private void iniTitle() {

    }

    @Override
    public void onClick(View v) {

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

    private static void refreshData() {
        datalist = NotifyDataProcessor.getAllNotifyData(mcontext);
    }

    static class NorifyDataObserver extends ContentObserver {
        public NorifyDataObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            refreshData();
        }
    }

    private class NotifyAdapter extends BaseAdapter {

        ArrayList<NotifyData> appList;
        Context mContext;

        public NotifyAdapter(ArrayList<NotifyData> appList, Context mContext) {
            this.appList = appList;
            this.mContext = mContext;
        }
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}
