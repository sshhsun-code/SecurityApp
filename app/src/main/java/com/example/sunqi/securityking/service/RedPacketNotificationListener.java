package com.example.sunqi.securityking.service;

import android.app.Notification;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.NotifyData;
import com.example.sunqi.securityking.dataprovider.ASpProvider;
import com.example.sunqi.securityking.global.GlobalPref;
import com.example.sunqi.securityking.utils.RedpacketAppsManager;

/**
 * Created by sunqi on 2017/5/8.
 */

public class RedPacketNotificationListener extends NotificationListener {

    GlobalPref globalPref = GlobalPref.getInstance();
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
            RedPacketInterceptor.getAutoOpenSwitch();
            if (!getWeixinRedpacketSwitch()) { //开关控制
                return;
            }

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


    /**
     * 过滤收到的通知，只有是红包的时候才执行后续操作
     * @param sbn
     * @return
     */
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

        String content = sbn.getNotification().extras.getString(Notification.EXTRA_TEXT);

        if(!RedpacketAppsManager.isRedpacketNotification(content))
        {
            return true;
        }

        return false;
    }

    private boolean getWeixinRedpacketSwitch() {
        ContentResolver resolver = SecurityApplication.getInstance().getContentResolver();
        Cursor cursor = resolver.query(ASpProvider.Content_URL,null,GlobalPref.SECURITY_SWITCH_WEIXIN_REDPACKET,null,null,null);
        if (cursor == null) {
            return false;
        } else {
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex("value"));
            return Boolean.parseBoolean(result);
        }
    }
}
