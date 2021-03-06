package com.example.sunqi.securityking.bean;

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

/**
 * Created by sshunsun on 2017/5/8.
 */
public class NotifyData {
    private String pkgName;
    private String title;
    private int notify_id;
    private String content;
    private long when;
    private Bitmap icon;
    private PendingIntent pendingIntent;

    public NotifyData() {

    }

    public NotifyData(StatusBarNotification notification) {
        this.pkgName = notification.getPackageName();
        Bundle bundle = notification.getNotification().extras;
        this.title = bundle.getString(Notification.EXTRA_TITLE);
        this.content = bundle.getString(Notification.EXTRA_TEXT);
        this.when = bundle.getLong(Notification.EXTRA_SHOW_WHEN);
        this.icon = bundle.getParcelable(Notification.EXTRA_LARGE_ICON);
        this.notify_id = notification.getId();
        this.pendingIntent = notification.getNotification().contentIntent;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(int notify_id) {
        this.notify_id = notify_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }
}
