package com.example.sunqi.securityking;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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
        initApplication();
    }

    private void initApplication() {

    }

    public static SecurityApplication getInstance() {
        if (application == null) {
            application = new SecurityApplication();
        }

        return application;
    }

    public static Context getAppContext() {
        application = getInstance();
        return application.getApplicationContext();
    }

}
