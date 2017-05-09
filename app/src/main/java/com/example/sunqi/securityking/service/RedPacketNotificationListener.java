package com.example.sunqi.securityking.service;

import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

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

            Log.e("RedPacketListener", "tryPostRedPacketMsg: parser notification begin " + pkgName);

//            NotificationParsedHelper notificationParsedHelper = new NotificationParsedHelper(sbn);
//            ExpandDetailInfo detailInfo = notificationParsedHelper.getTextNotificationInfo();
//            detailInfo.setPackageName(pkgName);


            Log.e("RedPacketListener", "tryPostRedPacketMsg: parser notification end" + pkgName);


//            RedPacketBackgroundService.get().sendRedPakcetRecvMsg(detailInfo);


            Log.e("RedPacketListener", "tryPostRedPacketMsg: sendRedPakcetRecvMsg end" + pkgName);


        }catch (Exception e)
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
