package com.example.sunqi.securityking.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by popfisher on 2017/3/10.
 */

public class NotifyServiceUtil {
    private static final String NOTI_ENABLED_LISTENERS = "enabled_notification_listeners";
    private static final String TAG = "NotifyServiceUtil";

    public static boolean isEnable(Context context) {
        boolean isEnable = false;
        String packageName = context.getPackageName();
        String flat = Settings.Secure.getString(context.getContentResolver(), NOTI_ENABLED_LISTENERS);

        if (flat != null) {
            isEnable = flat.contains(packageName);
        }
        return isEnable;
    }
}
