package com.example.sunqi.securityking.utils;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

import com.example.sunqi.securityking.SecurityApplication;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 查看应用使用权限判断
 */

public class UsageStatsUtil {

    public static boolean isTargetOSVer() {
        return Build.VERSION.SDK_INT >= 21;
    }

    /**
     * Used for Low power / AppLock screen / AppLock Main
     * @return
     */
    public static boolean shouldShowPromoptForPermission() {
        return !(!isTargetUserForPermission() || isUsagePermEnabled());
    }

    public static boolean isUsagePermEnabled() {
        boolean enabled = isAppUsagePermissionGranted(SecurityApplication.getInstance())
                && isAppUsagePermissionGrantedExt(SecurityApplication.getInstance());
        return enabled;
    }

    public static boolean isTargetUserForPermission() {
        boolean user = isTargetOSVer();
        boolean launchable = isUsageAccessSettingLaunchable(SecurityApplication.getInstance());

        return user && launchable;
    }

    private static final int STATE_NONE = 0;
    private static final int STATE_LAUNCHABLE = 1;
    private static final int STATE_NONLAUNCHABLE = 2;
    private static int sIsLaunchable = STATE_NONE;
    public static boolean isUsageAccessSettingLaunchable(Context context) {
        if (STATE_NONE != sIsLaunchable)
            return STATE_LAUNCHABLE == sIsLaunchable;
        try {
            Intent i = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            List<ResolveInfo> info = context.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
            sIsLaunchable = !info.isEmpty() ? STATE_LAUNCHABLE : STATE_NONLAUNCHABLE;
            return STATE_LAUNCHABLE == sIsLaunchable;
        } catch(Exception e) {
            return false;
        }
    }

    public static boolean isAppUsagePermissionGrantedExt(Context context) {
        if (isTargetOSVer()) {
            boolean granted = false;
            final long now = System.currentTimeMillis();
            UsageStatsManager sageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");// Context.USAGE_STATS_SERVICE);
            GregorianCalendar startDate = new GregorianCalendar(1970, 1, 1);
            GregorianCalendar endDate = new GregorianCalendar(2100, 1, 1);
            final List<UsageStats> events = sageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startDate.getTimeInMillis(), endDate.getTimeInMillis());
            granted = null != events ? events != Collections.EMPTY_LIST : false;
            return granted;
        } else {
            return false;
        }
    }

    private static boolean problematicDeviceWasGranted = false;

    public static boolean isAppUsagePermissionGranted(Context context) {
        if (context == null) {
            return false;
        }

        if (isTargetOSVer()) {
            boolean granted = false;
            if (isProblemDevice()) { 
                final long now = System.currentTimeMillis();
                UsageStatsManager sageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");// Context.USAGE_STATS_SERVICE);
                List<UsageStats> events = sageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - SystemClock.uptimeMillis(), now);
                granted = null != events ? events != Collections.EMPTY_LIST : false;
                if (granted) {
                    problematicDeviceWasGranted = true;
                } else if (problematicDeviceWasGranted) {
                    GregorianCalendar startDate = new GregorianCalendar(1970, 1, 1);
                    GregorianCalendar endDate = new GregorianCalendar(2100, 1, 1);
                    events = sageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startDate.getTimeInMillis(), endDate.getTimeInMillis());
                    granted = null != events ? events != Collections.EMPTY_LIST : false;
                    if (!granted) {
                        problematicDeviceWasGranted = false;
                    }
                }
            } else {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                granted = AppOpsManager.MODE_ALLOWED == appOpsMgr.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
            }
            return granted;
        } else {
            return false;
        }
    }

    public static final int DEVICE_NONE = 0;
    private static final int DEVICE_NO_PROMBLEM = 1;
    private static final int DEVICE_PROBLEM = 2;
    private static int mIsProblemDevice = DEVICE_NONE;
    public static boolean isProblemDevice() {
        return true;
    }

}
