package com.example.sunqi.securityking.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.global.GlobalPref;
import com.example.sunqi.securityking.permission.ui.PermissionTutotialRoutingActivity;
import com.example.sunqi.securityking.utils.NotifyServiceUtil;
import com.example.sunqi.securityking.utils.UsageStatsUtil;

import java.util.List;

/**
 * 检测权限开启状态，防护等级
 * Created by sunqi on 2017/5/17.
 */

public class PermissionManager {

    private static Context mcontext = SecurityApplication.getInstance();

    public static boolean checkPermission(Constant.Permission permission) {
        boolean result = true;
        switch (permission) {
            case AUTO_START:
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
        return result;
    }

    public static Constant.Level getLevel() {
        if(checkPermission(Constant.Permission.AUTO_START)
                && checkPermission(Constant.Permission.NOTIFICATION_READ)
                && checkPermission(Constant.Permission.USAGE_STATS)) {
            return Constant.Level.HIGH;
        }
        return Constant.Level.LOW;
    }

    public static void GuidePermission(int task) {
        Intent intent = new Intent(SecurityApplication.getInstance(), PermissionTutotialRoutingActivity.class);
        intent.putExtra(PermissionTutotialRoutingActivity.TASK, task);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mcontext.startActivity(intent);
    }

    public static void GuidePermission(List<Constant.Permission> permissionList) {

    }
}
