package com.example.sunqi.securityking.service;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.sunqi.securityking.dataprovider.NotifyDataProcessor;
import com.example.sunqi.securityking.global.Constant;

import java.util.ArrayList;

/**
 * Created by sshunsun on 2017/4/30.
 */
public class NotificationMoniter extends NotificationListenerService {

    private static ArrayList<Class<? extends NotificationListener>> sListenerClass = new ArrayList<>();
    AppObserver appObserver;
    private static ArrayList<String> whiteNameApps;

    private String uriStr = Constant.URI.NOTIFY_APP_URI;

    static {
        sListenerClass.add(RedPacketNotificationListener.class);
        sListenerClass.add(NotificationInterceptListener.class);
    }

    private ArrayList<NotificationListener> mListeners = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
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
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onDestroy() {
        for (NotificationListener listener : mListeners) {
            if (listener != null) {
                listener.onDestroy(this);
            }
        }
        mListeners.clear();
        super.onDestroy();
        super.onDestroy();
        getContentResolver().unregisterContentObserver(appObserver);
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
