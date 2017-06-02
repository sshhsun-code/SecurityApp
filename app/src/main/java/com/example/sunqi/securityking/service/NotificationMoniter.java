package com.example.sunqi.securityking.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.dataprovider.NotifyDataProcessor;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.ui.KeepForegroundActivity;
import com.example.sunqi.securityking.ui.NotificationBoxActivity;

import java.util.ArrayList;

/**
 * Created by sshunsun on 2017/4/30.
 */
public class NotificationMoniter extends NotificationListenerService {

    private static ArrayList<Class<? extends NotificationListener>> sListenerClass = new ArrayList<>();
    AppObserver appObserver;
    public static ArrayList<String> whiteNameApps = new ArrayList<>();
    public static NotificationMoniter mInstance;

    private String uriStr = Constant.URI.NOTIFY_APP_URI;

    static {
        sListenerClass.add(RedPacketNotificationListener.class);
        sListenerClass.add(NotificationInterceptListener.class);
    }

    private ArrayList<NotificationListener> mListeners = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = NotificationMoniter.this;
        for (int i = 0; i < sListenerClass.size(); i++) {
            Class<? extends NotificationListener> cls = sListenerClass.get(i);
            NotificationListener instance;
            try {
                instance = cls.newInstance();
                instance.onCreate(this);
                mListeners.add(instance);
            } catch (Exception e) {
                Log.e("NotificationMoniter", "Unable to create instance for: " + cls);

            }
        }
        initData();
        initListener();
        showNotifyFunNotificaion();
    }

    private void initListener() {
        Uri uri = Uri.parse(uriStr);
        getContentResolver().registerContentObserver(uri,true,appObserver);
    }

    private void initData() {
        whiteNameApps = NotifyDataProcessor.getUnMonitoredApp(getApplicationContext());
        appObserver = new AppObserver();
        whiteNameApps = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(this, KeepForegroundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        for (NotificationListener listener : mListeners) {
            if (listener != null) {
                listener.onNotificationPosted(this, sbn);
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        for (NotificationListener listener : mListeners) {
            if (listener != null) {
                listener.onNotificationRemoved(this, sbn);
            }
        }

    }

    @Override
    public void onDestroy() {
        stopForeground(false);
        mInstance = null;
        for (NotificationListener listener : mListeners) {
            if (listener != null) {
                listener.onDestroy(this);
            }
        }
        mListeners.clear();
        getContentResolver().unregisterContentObserver(appObserver);
        super.onDestroy();
    }

    public void cancelNotification(StatusBarNotification notification) {
        if (Build.VERSION.SDK_INT < 21) {
            cancelNotification(notification.getPackageName(), notification.getTag(), notification.getId());
        } else {
            cancelNotification(notification.getKey());
        }
    }

    public void cancelNotification(String packageName, String tag, int id, String key){
        if (Build.VERSION.SDK_INT < 21) {
            cancelNotification(packageName, tag, id);
        } else {
            cancelNotification(key);
        }
    }

    private void refreshWhiteName() {
        whiteNameApps = NotifyDataProcessor.getUnMonitoredApp(getApplicationContext());
    }

    private void showNotifyFunNotificaion() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationBoxActivity.class), 0);
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncBuilder = new NotificationCompat.Builder(this);
        ncBuilder.setContentTitle("安全卫士");
        ncBuilder.setContentText("通知收纳盒，点击查看拦截的通知");
        ncBuilder.setTicker("通知收纳盒，点击查看拦截的通知");
        ncBuilder.setSmallIcon(R.mipmap.ic_launcher);
        ncBuilder.setAutoCancel(false);
        ncBuilder.setContentIntent(pendingIntent);
        ncBuilder.setPriority(100);
        ncBuilder.setOngoing(true);
        startForeground((int) System.currentTimeMillis(), ncBuilder.build());
    }

    public static boolean isWhiteApp(String pkgName) {
        return whiteNameApps.contains(pkgName);
    }


    private class AppObserver extends ContentObserver{
        public AppObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            refreshWhiteName();
        }
    }


}
