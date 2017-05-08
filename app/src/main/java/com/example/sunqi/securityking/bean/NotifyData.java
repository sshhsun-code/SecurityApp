package com.example.sunqi.securityking.bean;

import android.app.PendingIntent;
import android.graphics.Bitmap;

/**
 * Created by sshunsun on 2017/5/8.
 */
public class NotifyData {
    private String pkgName;
    private String title;
    private int notify_id;
    private String content;
    private long LONG;
    private Bitmap icon;
    private PendingIntent pendingIntent;

    public NotifyData() {

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

    public long getLONG() {
        return LONG;
    }

    public void setLONG(long LONG) {
        this.LONG = LONG;
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
