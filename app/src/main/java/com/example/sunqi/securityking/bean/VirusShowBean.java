package com.example.sunqi.securityking.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by sunqi on 2017/5/19.
 */

public class VirusShowBean {
    private String pakName; //应用包名
    private String appName; //应用名称
    private Drawable appIcon; //应用图标
    private int type;   //病毒类型

    public VirusShowBean(String pakName, String appName, Drawable appIcon, int type) {
        this.pakName = pakName;
        this.appName = appName;
        this.appIcon = appIcon;
        this.type = type;
    }

    public VirusShowBean() {

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
