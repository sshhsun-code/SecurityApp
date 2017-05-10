package com.example.sunqi.securityking.service;

import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.example.sunqi.securityking.bean.NotifyData;
import com.example.sunqi.securityking.utils.RedpacketAppsManager;

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
        tryPostRedPacketMsg(notification);
    }

    @Override
    public void onNotificationRemoved(NotificationMoniter service, StatusBarNotification notification) {
        super.onNotificationRemoved(service, notification);
    }


    private void tryPostRedPacketMsg(StatusBarNotification notification) {
        try {

            if(filterPackage(notification))
            {
                return;
            }

            String pkgName = notification.getPackageName();
            NotifyData data = new NotifyData(notification);
            data.setPkgName(pkgName);
            RedPacketBackgroundService.get().sendRedPakcetRecvMsg(data);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private boolean filterPackage(StatusBarNotification sbn)
    {
        String pkgName = sbn.getPackageName();
        if (TextUtils.isEmpty(pkgName)) {
            return true;
        }

        if(!RedpacketAppsManager.isRedpacketApp(pkgName))
        {
            return true;
        }

        return false;
    }
}
