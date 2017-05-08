package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
import com.example.sunqi.securityking.bean.NotifyAppInfo;
import com.example.sunqi.securityking.dataprovider.NotifyDataProcessor;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.service.NotificationMoniter;
import com.example.sunqi.securityking.utils.NotifyServiceUtil;

import java.util.ArrayList;

/**
 * Created by sshunsun on 2017/4/23.
 */
public class NotifySettingActivity extends Activity implements View.OnClickListener, NotifyDataProcessor.onDataListener{

    private ListView mListView;

    private ArrayList<NotifyAppInfo> baseAppInfos;
    private AppDataAdapter mAdapter;
    private Handler mHandler;



    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private static final int HANDLER_GET_APP_DATA = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.notify_settting_layout);
        initData();
        initView();
        Log.e("log","test log");
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
        mListView = (ListView) findViewById(R.id.app_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(NotifySettingActivity.this,baseAppInfos.get(i).getPakName(),Toast.LENGTH_SHORT);
//                baseAppInfos.get(i).setAppState(Constant.State.NOTIFY_SHOW);
//                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initData() {
        processAppData();
        boolean isNotifyRead = isNotifyReadEnable();
        if (!isNotifyRead) {
            showConfirmDialog();
        }
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HANDLER_GET_APP_DATA:

                        break;
                }
            }
        };
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage("通知读取权限受限，请开启相应权限")
                .setTitle("权限修复")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openNotificationAccess();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        })
                .create().show();
    }


    private void openNotificationAccess() {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    private void showNotifyFunNotificaion() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationBoxActivity.class), 0);
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncBuilder = new NotificationCompat.Builder(this);
        ncBuilder.setContentTitle("My Notification");
        ncBuilder.setContentText("Notification Listener Service Example");
        ncBuilder.setTicker("Notification Listener Service Example");
        ncBuilder.setSmallIcon(R.mipmap.ic_launcher);
        ncBuilder.setAutoCancel(false);
        ncBuilder.setContentIntent(pendingIntent);
        manager.notify((int) System.currentTimeMillis(), ncBuilder.build());
    }

    private void processAppData() {
        mHandler = new Handler();
        NotifyDataProcessor.setDataListener(this);
    }

    private boolean isNotifyReadEnable() {
        return NotifyServiceUtil.isEnable(this);
    }

    @Override
    public void onDataFinished(ArrayList<NotifyAppInfo> list) {
        baseAppInfos = list;
        mAdapter = new AppDataAdapter(list, getApplicationContext());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotifyDataProcessor.getNotifyAppSettingData(NotifySettingActivity.this);

        if (NotifyServiceUtil.isEnable(this)) {
            try {
                toggleNotificationListenerService(this);
                if(NotificationMoniter.mInstance == null) {
                    Intent intent = new Intent(this, NotificationMoniter.class);
                    startService(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            showNotifyFunNotificaion();
        }
    }


    /**
     * 重新注册，触发系统重新bind，防止重启监听进程后不生效
     * @param context
     */
    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(context, com.example.sunqi.securityking.service.NotificationMoniter.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(context, com.example.sunqi.securityking.service.NotificationMoniter.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class AppDataAdapter extends BaseAdapter {
        ArrayList<NotifyAppInfo> appList;
        Context mContext;
        SwitchClickListener listener;

        public AppDataAdapter(ArrayList<NotifyAppInfo> list, Context context) {
            this.appList = list;
            this.mContext = context;
        }

        @Override
        public boolean hasStableIds() {
            return false;
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
            final NotifyAppInfo info = appList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.notify_setting_ex_parent_item,null);
            }
            if (info != null) {
                ((ImageView)convertView.findViewById(R.id.app_item_icon)).setBackgroundDrawable(info.getAppIcon());
                ((ImageView)convertView.findViewById(R.id.app_switch)).setBackgroundDrawable(info.getAppState() == 0 ?getDrawable(R.drawable.switch_off):getDrawable(R.drawable.switch_on_1));
                ((TextView)convertView.findViewById(R.id.app_item_text)).setText(info.getAppName());
            }
            convertView.findViewById(R.id.app_switch).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getAppState() != Constant.State.NOTIFY_SHOW) {
                        info.setAppState(Constant.State.NOTIFY_SHOW);
                        notifyDataSetChanged();
                        NotifyDataProcessor.addUnMonitoredApp(info.getPakName(),mContext);
                    }
                }
            });
            return convertView;
        }
    }

    private  interface SwitchClickListener {
        void onClick(int position, int switch_state);
    }
}
