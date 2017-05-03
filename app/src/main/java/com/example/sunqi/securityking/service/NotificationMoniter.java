package com.example.sunqi.securityking.service;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.example.sunqi.securityking.dataprovider.NotifyDataProcessor;
import com.example.sunqi.securityking.global.Constant;

import java.util.ArrayList;

/**
 * Created by sshunsun on 2017/4/30.
 */
public class NotificationMoniter extends NotificationListenerService {
    AppObserver appObserver;
    private static ArrayList<String> whiteNameApps;

    private String uriStr = Constant.URI.NOTIFY_APP_URI;
    @Override
    public void onCreate() {
        super.onCreate();
        Uri uri = Uri.parse(uriStr);
        appObserver = new AppObserver();
        whiteNameApps = new ArrayList<>();
        getContentResolver().registerContentObserver(uri,true,appObserver);
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
        super.onDestroy();
        getContentResolver().unregisterContentObserver(appObserver);
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
