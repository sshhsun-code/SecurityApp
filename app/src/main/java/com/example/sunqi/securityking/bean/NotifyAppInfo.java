package com.example.sunqi.securityking.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by sshunsun on 2017/4/24.
 */
public class NotifyAppInfo extends BaseAppInfo {

    private String pakName; //应用包名
    private String appName; //应用名称
    private Drawable appIcon; //应用图标
    private int appState;   //应用的状态：禁止通知，收起通知，显示通知

    public NotifyAppInfo() {

    }

    public NotifyAppInfo(String pakName, String appName, Drawable appIcon, int appState) {
        this.pakName = pakName;
        this.appName = appName;
        this.appIcon = appIcon;
        this.appState = appState;
    }

    public String getPakName() {
        return pakName;
    }

    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public int getAppState() {
        return appState;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setPakName(String pakName) {
        this.pakName = pakName;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setAppState(int appState) {
        this.appState = appState;
    }
}
