package com.example.sunqi.securityking.service;

import android.content.Intent;
import android.service.notification.StatusBarNotification;

/**
 * Created by sunqi on 2017/5/8.
 */

public class NotificationListener {

    public void onCreate(NotificationMoniter service){}
    public void onDestroy(NotificationMoniter service){}

    public void onStartCommand(NotificationMoniter service, Intent intent){}

    public void onNotificationPosted(NotificationMoniter service, StatusBarNotification notification){}
    public void onNotificationRemoved(NotificationMoniter service, StatusBarNotification notification){}
}
