package com.example.sunqi.securityking.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.util.SparseArrayCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by kingsoft on 2015/8/11.
 */
public class Commons {

    private Commons() {
        throw new AssertionError();
    }


    public static Intent getAppIntentWithPackageName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageinfo = null;
        try {
            packageinfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return null;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        List<ResolveInfo> list = packageManager.queryIntentActivities(resolveIntent, 0);

        if (list != null && !list.isEmpty()) {
            ResolveInfo resolveInfo = list.get(0);
            if (resolveInfo != null) {
                String pn = resolveInfo.activityInfo.packageName;
                String className = resolveInfo.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(pn, className);
                intent.setComponent(cn);
                return intent;
            }
        }
        return null;
    }

    // 必须都使用此方法打开外部activity,避免外部activity不存在而造成崩溃，
    public static boolean startActivity(Context context, Intent intent) {
        boolean bResult = true;
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            bResult = false;
        } catch (Exception e) {
            e.printStackTrace();
            bResult = false;
        }
        return bResult;
    }

    public static boolean startActivity(Context context, Intent intent, boolean useSlideInAnimation) {
        boolean bResult = true;
        bResult = startActivity(context, intent);
        if (useSlideInAnimation) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                //动画
            }
        }
        return bResult;
    }

    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 拉下系统通知栏
    public static void expandNotificationsPanel(Context applicitionContext) {
        try {
            Object service = applicitionContext.getSystemService("statusbar");
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
            Method expand = null;
            if (service != null) {
                if (android.os.Build.VERSION.SDK_INT <= 16) {
                    expand = statusbarManager.getMethod("expand");
                } else {
                    expand = statusbarManager.getMethod("expandNotificationsPanel");
                }
                expand.setAccessible(true);
                expand.invoke(service);
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    // 收回系统通知栏
    public static void collapseNotificationsPanel(Context applicitionContext) {
        try {
            Object service = applicitionContext.getSystemService("statusbar");
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
            Method collapsePanels = null;
            if (service != null) {
                if (android.os.Build.VERSION.SDK_INT <= 16) {
                    collapsePanels = statusbarManager.getMethod("collapsePanels");
                } else {
                    collapsePanels = statusbarManager.getMethod("collapsePanels");
                }
                collapsePanels.setAccessible(true);
                collapsePanels.invoke(service);
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

//    public synchronized static void pullNotificationPanel() {
//        boolean isPull = NotificationInterceptPref.getInstance().isPullNotificationPanel();
//        if (isPull) {
//            int workMode = NotificationInterceptSetting.getWorkingMode();
//            if (workMode == NotificationInterceptSetting.WORKING_MODE_NOTIFICATION_BOX) {
//                if (MiuiHelper.isMiui() || EMuiHelper.isEMUI3() || EMuiHelper.isEMUI2()) {
//                    NotificationInterceptPermanentService.sendBroadCastNoPermanentNotify();
//                }
//                ks.cm.antivirus.notification.intercept.utils.Commons.expandNotificationsPanel();
//            }
//        }
//        NotificationInterceptPref.getInstance().setIsPullNotificationPanel(true);
//    }

    private static boolean sFeaturesInitialized = false;
    private static FeatureInfo[] sFeaturesList = null;

    /**
     * 系统是否支持某个特性
     *
     * @param context the context
     * @param feature 特性名
     * @return true -> 支持 false -> 不支持
     */
    public static synchronized boolean isFeatureAvailable(Context context, String feature) {
        if (!sFeaturesInitialized && sFeaturesList == null) {
            PackageManager packageManager;
            try {
                packageManager = context.getPackageManager();
            } catch (RuntimeException e) {
                return false;
            }
            if (packageManager == null) {
                return false;
            }

            try {
                sFeaturesList = packageManager.getSystemAvailableFeatures();
            } catch (RuntimeException e) {
                return false;
            }
            sFeaturesInitialized = true;
        }

        if (sFeaturesList == null) {
            return false;
        }

        for (FeatureInfo f : sFeaturesList) {
            if (f.name != null && f.name.equals(feature)) {
                return true;
            }
        }
        return false;
    }


    /**
     * @return
     */
    public static boolean isSDKAbove21() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable == null) {
            return bitmap;
        }
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            try {
                int w = drawable.getIntrinsicWidth();
                int h = drawable.getIntrinsicHeight();
                Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
                bitmap = Bitmap.createBitmap(w, h, config);
                //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, w, h);
                drawable.draw(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static boolean checkHasIntent(Context context, Intent intent) {
        if (intent == null || context == null) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (null == resolveInfos)
            return false;

        return 0 < resolveInfos.size();
    }

    public static boolean inSameDay(Date date1, Date Date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(Date2);
        int year2 = calendar.get(Calendar.YEAR);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);

        return (year1 == year2) && (day1 == day2);
    }

    public static ComponentName getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null && runningTaskInfos.size() > 0) {
            ActivityManager.RunningTaskInfo info = runningTaskInfos.get(0);
            if (info == null) {
                return null;
            }
            ComponentName component = info.topActivity;
            return component;
        } else {
            return null;
        }
    }

    public static String getDateTimeString(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return c.getTime().toString();
    }

//    public static final int TIPS_PANEL_CALL_FROM_ANIM_END = 0;
//    public static final int TIPS_PANEL_CALL_FROM_BOXVIEW_INIT = 1;

//    public static boolean isTipsPanelShowValid(int from) {
//        if (!NotificationInterceptPref.getInstance().getIsFrom5EntryToPanel()) {
//            // getIsFrom5EntryToPanel()为true表示面板的从1、开屏引导  2、首页固定入口
//            // 3、菜单栏入口  4、风险页入口 5、信息流入口 进入。
//            return false;
//        }
//        if (from == TIPS_PANEL_CALL_FROM_ANIM_END) {
//            NotificationInterceptPref.getInstance().setIsFrom5EntryToPanel(false);
//        }
//        if (!NotificationInterceptPref.getInstance().getIsFirstShowSceneGuideCard()) {
//            return false;
//        }
//        return Commons.getIsOpenSceneGuide1PanelCloudSwitch();
//    }

    public static <C> List<C> asList(SparseArrayCompat<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }
}
