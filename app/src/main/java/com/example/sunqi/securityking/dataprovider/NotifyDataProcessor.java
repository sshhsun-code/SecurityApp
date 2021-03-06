package com.example.sunqi.securityking.dataprovider;

import android.app.Notification;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.NotifyAppInfo;
import com.example.sunqi.securityking.bean.NotifyData;
import com.example.sunqi.securityking.global.Constant;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshunsun on 2017/4/24.
 */
public class NotifyDataProcessor {
    private static onDataListener dataListener;
    private static ArrayList<NotifyAppInfo> applist = new ArrayList<>();
    private static ArrayList<String> whiteAppList = new ArrayList<>();
    private static String APP_URI = Constant.URI.NOTIFY_APP_URI;
    private static String NOTIFY_URI = Constant.URI.NOTIFY_DATA_URI;
    private static Context mconext = SecurityApplication.getInstance();

    public static void setDataListener(onDataListener listener) {
        dataListener = listener;
    }

    public static void getNotifyAppSettingData(Context context) {
        ArrayList<NotifyAppInfo> whiteList = new ArrayList<>();
        initWhiteList(context);
        applist.clear();
        NotifyAppInfo appInfo;
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> resolveInfolist = manager.queryIntentActivities(intent, 0);
        if (resolveInfolist != null || resolveInfolist.isEmpty()) {
            for (ResolveInfo info : resolveInfolist) {
                appInfo = new NotifyAppInfo();
                appInfo.setAppName(info.activityInfo.loadLabel(manager).toString());
                appInfo.setPakName(info.activityInfo.packageName);
                appInfo.setAppIcon(info.activityInfo.loadIcon(manager));
                appInfo.setAppState(Constant.State.NOTIFY_MANAGE);
                if (whiteAppList.contains(appInfo.getPakName())) {
                    appInfo.setAppState(Constant.State.NOTIFY_SHOW);
                    whiteList.add(appInfo);
                } else {
                    applist.add(appInfo);
                }
            }
        }
        applist.addAll(0,whiteList);
        if (dataListener != null) {
            dataListener.onDataFinished(applist);
        }
    }

    private static void initWhiteList(Context context) {
        whiteAppList.clear();
        whiteAppList = getUnMonitoredApp(context);
    }

    public static void addUnMonitoredApp(String packName, Context context) {
        Uri uri = Uri.parse(APP_URI);
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("packname", packName);
        resolver.insert(uri,values);
        resolver.notifyChange(uri,null);
        Cursor cursor = resolver.query(uri,null,null,null,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.e("package",cursor.getString(cursor.getColumnIndex("packname")));
        }
        Log.e("package","=============================================");
        cursor.close();
    }

    public static void removeUnMonitoredApp(String packName, Context context) {
        Uri uri = Uri.parse(APP_URI);
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("packname", packName);
        resolver.delete(uri,null,new String[]{packName});
        resolver.notifyChange(uri,null);
    }

    public static ArrayList<String> getUnMonitoredApp(Context context) {
        Uri uri = Uri.parse(APP_URI);
        ArrayList<String> apps = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri,null,null,null,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            apps.add(cursor.getString(cursor.getColumnIndex("packname")));
        }
        return apps;
    }


    /**
     * 向ContentProvider中添加数据
     * @param notification
     * @param context
     */
    public static void addNotifyData(StatusBarNotification notification, Context context){
        Uri uri = Uri.parse(NOTIFY_URI);
        ContentResolver resolver = context.getContentResolver();
        Bundle extras = notification.getNotification().extras;
        ContentValues values = new ContentValues();
        values.put("title", extras.getString(Notification.EXTRA_TITLE));
        values.put("notify_id", notification.getId());
        values.put("content", extras.getCharSequence(Notification.EXTRA_TEXT).toString());
        values.put("time", notification.getPostTime());
        values.put("icon", getImageBuff((Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON)));
        values.put("packname", notification.getPackageName());
        resolver.insert(uri,values);
        resolver.notifyChange(uri,null);
    }

    public static ArrayList<NotifyData> getAllNotifyData() {
        ArrayList<NotifyData> datalist = new ArrayList<>();
        Uri uri = Uri.parse(NOTIFY_URI);
        ContentResolver resolver = mconext.getContentResolver();
        Cursor cursor = resolver.query(uri,null,null,null,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            NotifyData data = new NotifyData();
            data.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            data.setNotify_id(cursor.getInt(cursor.getColumnIndex("notify_id")));
            data.setContent(cursor.getString(cursor.getColumnIndex("content")));
            data.setWhen(cursor.getLong(cursor.getColumnIndex("time")));
            data.setIcon(getBitmapFromDB(cursor.getBlob(cursor.getColumnIndex("icon"))));
            data.setPkgName(cursor.getString(cursor.getColumnIndex("packname")));
            datalist.add(data);
        }
        cursor.close();
        return datalist;
    }

    private static Bitmap getBitmapFromDB(byte[] iconData) {
        if (iconData == null) {
            return null;
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
        if (bmp == null) {
            return null;
        }
        return bmp;
    }

    public static void removeNotifyData(int notify_id) {
        Uri uri = Uri.parse(NOTIFY_URI);
        ContentResolver resolver = mconext.getContentResolver();
        resolver.delete(uri,"notify_id = ?", new String[]{notify_id+""});
    }

    public static void removeAllNotifyData() {
        Uri uri = Uri.parse(NOTIFY_URI);
        ContentResolver resolver = mconext.getContentResolver();
        resolver.delete(uri,null,null);
    }

    private static byte[] getImageBuff(Bitmap bitmap) {
        if(bitmap == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }



    private static int checkAppState(String packageName) {
        return Constant.State.NOTIFY_MANAGE;
    }

    public interface onDataListener {
        void onDataFinished(ArrayList<NotifyAppInfo> applist);
    }
}
