package com.example.sunqi.securityking;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.sunqi.securityking.utils.AppIconUtils;

import java.util.HashSet;

/**
 * Created by sshunsun on 2017/4/23.
 */
public class SecurityApplication extends Application {
    private static SecurityApplication application;
    private static SharedPreferences  notify_read_sp;
    private static HashSet<String> showApps;
    private static final String  NOTIFY_SETTING = "notify_setting";



    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initApplication();
    }

    private void initApplication() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppIconUtils.initBitmaps();
            }
        }).start();
    }

    public static SecurityApplication getInstance() {
        if (application == null) {
            application = new SecurityApplication();
        }

        return application;
    }

}
