package com.example.sunqi.securityking.permission;

import android.os.Build;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.global.GlobalPref;
import com.example.sunqi.securityking.utils.NotifyServiceUtil;
import com.example.sunqi.securityking.utils.UsageStatsUtil;

/**
 * 检测权限开启状态，防护等级
 * Created by sunqi on 2017/5/17.
 */

public class PermissionManager {


    public static boolean checkPermission(Constant.Permission permission) {
        boolean result = true;
        switch (permission) {
            case AUTO_SETUP:
                result = GlobalPref.getInstance().getBoolean(GlobalPref.SECURITY_HAS_ENTER_AUTOSTART, false);
                break;
            case NOTIFICATION_READ:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    result = NotifyServiceUtil.isEnable(SecurityApplication.getInstance());
                }
                break;
            case USAGE_STATS:
                result = !UsageStatsUtil.shouldShowPromoptForPermission();
                break;
        }
        return false;
    }

    public static Constant.ProtectLevel getLevel() {
        if(checkPermission(Constant.Permission.AUTO_SETUP)
                && checkPermission(Constant.Permission.NOTIFICATION_READ)
                && checkPermission(Constant.Permission.USAGE_STATS)) {
            return Constant.ProtectLevel.HIGH;
        }
        return Constant.ProtectLevel.LOW;
    }
}
