package com.example.sunqi.securityking.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.global.Constant;

/**
 * 引导权限开启
 * Created by sunqi on 2017/5/17.
 */

public class PermissionGuideUtil {

    public static final String ACTION_NOTI_ACCE = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static final String ACTION_USAGE_ACCESS = "android.settings.USAGE_ACCESS_SETTINGS";

    public static void openSinglePermission(Constant.Permission permission) {
        switch (permission) {
            case AUTO_START:

                break;
            case NOTIFICATION_READ:

                break;
            case USAGE_STATS:

                break;
        }
    }

    public static Intent getNotificationReadIntent() {
        return new Intent(ACTION_NOTI_ACCE);
    }

    public static Intent getAppUsageIntent() {
        return new Intent(ACTION_USAGE_ACCESS);
    }

    public static Intent getAutoStartIntent() {
        Context context = SecurityApplication.getInstance().getApplicationContext();
        Intent intent = new Intent();
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", context.getPackageName());
        return isIntentAvailable(context, intent) ? intent : null;
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        if (intent == null) return false;
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
