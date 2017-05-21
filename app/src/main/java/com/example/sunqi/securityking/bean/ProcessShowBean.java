package com.example.sunqi.securityking.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by sunqi on 2017/5/19.
 */

public class ProcessShowBean {
    private String pakName; //应用包名
    private String appName; //应用名称
    private Drawable appIcon; //应用图标
    private int size;   //进程占用空间大小

    public ProcessShowBean(String pakName, String appName, Drawable appIcon, int size) {
        this.pakName = pakName;
        this.appName = appName;
        this.appIcon = appIcon;
        this.size = size;
    }

    public ProcessShowBean() {
    }

    public String getPakName() {
        return pakName;
    }

    public void setPakName(String pakName) {
        this.pakName = pakName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
