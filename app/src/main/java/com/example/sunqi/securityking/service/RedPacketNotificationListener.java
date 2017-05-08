package com.example.sunqi.securityking.service;

import android.content.Intent;
import android.service.notification.StatusBarNotification;

/**
 * Created by sunqi on 2017/5/8.
 */

public class RedPacketNotificationListener extends NotificationListener {
    @Override
    public void onCreate(NotificationMoniter service) {
        super.onCreate(service);
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
    }

    @Override
    public void onNotificationRemoved(NotificationMoniter service, StatusBarNotification notification) {
        super.onNotificationRemoved(service, notification);
    }
}
