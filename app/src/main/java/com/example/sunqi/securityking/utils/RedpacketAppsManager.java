package com.example.sunqi.securityking.utils;


public class RedpacketAppsManager {
    public static final String QQPkg = "com.tencent.mobileqq";
    public static final String WebChatPkg = "com.tencent.mm";
    public static final String WebChatRedpacket = "[微信红包]";

    private static final String[] Apps = {WebChatPkg , QQPkg};

    public static boolean isRedpacketApp(String packageName) {
        for (int i = 0; i < Apps.length; ++i) {
            if (Apps[i].equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRedpacketNotification(String content) {
        if (content.isEmpty()) {
            return false;
        }

        for (int i = 0; i < Apps.length; ++i) {
            if (content.contains(WebChatRedpacket)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getApps() {
        return Apps;
    }
}
