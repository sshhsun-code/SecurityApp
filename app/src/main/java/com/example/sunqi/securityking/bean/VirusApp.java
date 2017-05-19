package com.example.sunqi.securityking.bean;

/**
 * Created by sunqi on 2017/5/19.
 */

public class VirusApp {
    private String pkgName;
    private String appName;
    private int type;

    public VirusApp(String pkgName, String appName, int type) {
        this.pkgName = pkgName;
        this.appName = appName;
        this.type = type;
    }

    public VirusApp() {

    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VirusApp{" +
                "pkgName='" + pkgName + '\'' +
                ", appName='" + appName + '\'' +
                ", type=" + type +
                '}';
    }
}
