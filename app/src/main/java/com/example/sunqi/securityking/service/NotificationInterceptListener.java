package com.example.sunqi.securityking.service;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.service.notification.StatusBarNotification;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.dataprovider.ASpProvider;
import com.example.sunqi.securityking.dataprovider.NotifyDataProcessor;
import com.example.sunqi.securityking.global.GlobalPref;

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
        if (isShouldNotify(notification)) {
            NotifyDataProcessor.addNotifyData(notification, mcontext);
            service.cancelNotification(notification);
        }
    }

    @Override
    public void onNotificationRemoved(NotificationMoniter service, StatusBarNotification notification) {
        super.onNotificationRemoved(service, notification);
    }

    private boolean isShouldNotify(StatusBarNotification notification) {
        if (!getNotifySpSwitch()) {
            return false;
        }

        if(NotificationMoniter.isWhiteApp(notification.getPackageName())) {
            return false;
        }

        return true;
    }

    private boolean getNotifySpSwitch() {
        ContentResolver resolver = SecurityApplication.getInstance().getContentResolver();
        Cursor cursor = resolver.query(ASpProvider.Content_URL,null,GlobalPref.SECURITY_KEY_SWITCH_NOTIFY,null,null,null);
        if (cursor == null) {
            return false;
        } else {
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex("value"));
            return Boolean.parseBoolean(result);
        }
    }
}
