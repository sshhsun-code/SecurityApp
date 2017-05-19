package com.example.sunqi.securityking.dataprovider;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

import com.example.sunqi.securityking.SecurityApplication;

/**
 * 管理进程加速相关数据和操作
 * Created by sshunsun on 2017/5/20.
 */
public class ProcessDataProvider {

    private static Context mcontext = SecurityApplication.getInstance();

    @TargetApi(21)
    public static void requestBackUpProcessData(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(Integer.MAX_VALUE)) {
            int pid = rsi.pid;
            String pkgName = rsi.service.getPackageName();
            Debug.MemoryInfo[] infos = am.getProcessMemoryInfo(new int[]{pid});
            int memory = infos[0].getTotalPss() / 1024;
        }

//        UsageStatsManager mUsageStatsManager =
//                (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
//        long endTime = System.currentTimeMillis();
//        long beginTime = endTime - 1000*60;
//        List<UsageStats> stats = mUsageStatsManager.
//                queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
//        for (UsageStats info : stats) {
//            info.getPackageName();
//           
//        }
    }

    public interface DataListener{

    }
}
