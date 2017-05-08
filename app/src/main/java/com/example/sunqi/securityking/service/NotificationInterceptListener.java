package com.example.sunqi.securityking.service;

import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;

import com.example.sunqi.securityking.dataprovider.NotifyDataProcessor;

/**
 * Created by sunqi on 2017/5/8.
 */

public class NotificationInterceptListener extends NotificationListener {
    private Context mcontext;

    @Override
    public void onCreate(NotificationMoniter service) {
        super.onCreate(service);
        mcontext = service.getApplication();
    }

    @Override
    public void onDestroy(NotificationMoniter service) {
        super.onDestroy(service);
    }

    @Override
    public void onStartCommand(NotificationMoniter service, Intent intent) {
        super.onStartCommand(service, intent);
    }

    @Override
    public void onNotificationPosted(NotificationMoniter service, StatusBarNotification notification) {
        super.onNotificationPosted(service, notification);
        NotifyDataProcessor.addNotifyData(notification,mcontext);
    }

    @Override
    public void onNotificationRemoved(NotificationMoniter service, StatusBarNotification notification) {
        super.onNotificationRemoved(service, notification);
    }
}
