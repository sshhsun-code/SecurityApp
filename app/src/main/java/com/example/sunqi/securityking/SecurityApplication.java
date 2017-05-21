package com.example.sunqi.securityking;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.sunqi.securityking.utils.AppIconUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sshunsun on 2017/4/23.
 */
public class SecurityApplication extends Application {
    private static SecurityApplication application;
    private static SharedPreferences  notify_read_sp;
    private static HashSet<String> showApps;
    private static final String  NOTIFY_SETTING = "notify_setting";
    public static ArrayList<String> installApps = new ArrayList<>();
    private static PackageManager manager;
//    private GlobalPref globalPref = GlobalPref.getInstance();

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
                initData();
                AppIconUtils.initBitmaps();
            }
        }).start();
    }

    private void initData() {
        manager = this.getApplicationContext().getPackageManager();
        List<ApplicationInfo> list = manager.getInstalledApplications(0);
        for (ApplicationInfo info :list) {
            installApps.add(info.loadLabel(manager).toString());
        }
    }

    public static SecurityApplication getInstance() {
        if (application == null) {
            application = new SecurityApplication();
        }

        return application;
    }

}
