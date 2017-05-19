package com.example.sunqi.securityking.utils;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sshunsun on 2017/5/14.
 */
public class AppIconUtils {

    private static Map<String, Bitmap> appIcons = new HashMap<>();

    public static void initBitmaps() {
        appIcons.clear();
        PackageManager manager = SecurityApplication.getInstance().getPackageManager();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> resolveInfolist = manager.queryIntentActivities(intent, 0);
        if (resolveInfolist != null || resolveInfolist.isEmpty()) {
            for (ResolveInfo info : resolveInfolist) {
                appIcons.put(info.activityInfo.packageName, drawableToBitamp(info.activityInfo.loadIcon(manager)));
            }
        }
    }

    private static Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap getIcon(String pkgName) {
        if (appIcons.containsKey(pkgName)) {
            return appIcons.get(pkgName);
        }
        return drawableToBitamp(SecurityApplication.getInstance().getDrawable(R.drawable.default_icon));
    }

    public static boolean isInstallApp(String pkgName) {
        return appIcons.containsKey(pkgName);
    }
}
