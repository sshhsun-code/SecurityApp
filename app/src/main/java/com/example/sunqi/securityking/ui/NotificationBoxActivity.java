package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.bean.NotifyData;
import com.example.sunqi.securityking.dataprovider.NotificationInfoProcessor;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.utils.Commons;
import com.example.sunqi.securityking.utils.TimeUtils;

import java.util.ArrayList;

/**
 * 通知栏信息拦截Activity
 * Created by sshunsun on 2017/4/30.
 */
public class NotificationBoxActivity extends Activity implements View.OnClickListener , NotificationInfoProcessor.onDataListener{

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
        if (!datalist.isEmpty()) {
            mAdapter = new NotifyAdapter(datalist ,mcontext);
            mNotifyListView.setAdapter(mAdapter);
        }
        mNotifyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotifyData data = datalist.get(position);
                NotificationInfoProcessor.removeNotifyData(data); //从数据库中删除
                datalist.remove(position);
                mAdapter.notifyDataSetChanged();
                handleIntent(data);
            }
        });
    }

    @Override
    public void onDataFinished(ArrayList<NotifyData> Datalist) {
        datalist = Datalist;
        mAdapter = new NotifyAdapter(datalist ,mcontext);
        mNotifyListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataNone() {

    }

    private void handleIntent(NotifyData data) {
        PendingIntent pi = data.getPendingIntent();
        try {
            if (pi != null) {
                pi.send();
            } else {
                tryAllWithIntent(mcontext, data.getPkgName());
            }
        } catch (PendingIntent.CanceledException exception) {
            exception.printStackTrace();
            tryAllWithIntent(mcontext, data.getPkgName());
        }
    }

    private void tryAllWithIntent(Context context, String pkgName) {
        sendIntentByPackageName(pkgName, context);
    }

    private void sendIntentByPackageName(String packageName, Context context){
        try {
            if (packageName != null) {
                Intent intent = Commons.getAppIntentWithPackageName(context, packageName);
                if (intent != null) {
                    Commons.startActivity(context, intent);
                    // 如果这里也打开失败，那么就无解了
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        mcontext = this;
        datalist = new ArrayList<>();
        Uri uri = Uri.parse(uriStr);
        NotificationInfoProcessor.setDataListener(this);
        norifyDataObserver = new NorifyDataObserver();
        getContentResolver().registerContentObserver(uri, true, norifyDataObserver);
    }

    private void iniTitle() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationInfoProcessor.requsetNotifyData(); //请求拦截到的通知消息
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

    static class NorifyDataObserver extends ContentObserver {
        public NorifyDataObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
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
            return appList.size();
        }

        @Override
        public Object getItem(int position) {
            return appList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NotifyData data = datalist.get(position);
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.notify_data_item,null);
            }
            ViewHolder viewHolder = new ViewHolder(convertView);
            if (data.getIcon() != null) {
                viewHolder.icon.setImageBitmap(data.getIcon());
            }
            viewHolder.title.setText(data.getTitle());
            viewHolder.content.setText(data.getContent());
            viewHolder.time.setText(TimeUtils.getTimeStr(data.getWhen()));
            return convertView;
        }

        private class ViewHolder {
            public ImageView icon;
            public TextView title;
            public TextView content;
            public TextView time;

            public ViewHolder(View convertView) {
                icon = (ImageView) convertView.findViewById(R.id.notify_icon);
                title = (TextView) convertView.findViewById(R.id.notify_title);
                content = (TextView) convertView.findViewById(R.id.notify_content);
                time = (TextView) convertView.findViewById(R.id.notify_when);
            }
        }
    }


}
