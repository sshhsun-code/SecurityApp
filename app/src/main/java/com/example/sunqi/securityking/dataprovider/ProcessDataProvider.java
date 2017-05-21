package com.example.sunqi.securityking.dataprovider;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.text.TextUtils;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.ProcessShowBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理进程加速相关数据和操作
 * Created by sshunsun on 2017/5/20.
 */
public class ProcessDataProvider {

    private static final int SYSTEM_REF_APP = 1;
    private static final int USER_APP = 2;
    private static final int UNKNOW_APP = 3;
    private static List<ProcessShowBean> processShowBeanList = new ArrayList<>();
    private static List<Integer> pidList = new ArrayList<>();
    private static DataListener mlistener;
    private static Context mcontext = SecurityApplication.getInstance();

    @TargetApi(21)
    public static void requestBackUpProcessData(Context context) {
        processShowBeanList.clear();
        pidList.clear();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(Integer.MAX_VALUE)) {

            int pid = rsi.pid;
            if (pidList.contains(pid)) {
                continue;
            } else {
                pidList.add(pid);
            }
            String pkgName = rsi.service.getPackageName();
            if (checkAppType(pkgName) != USER_APP || TextUtils.isEmpty(pkgName)) {
                continue;
            }
            Debug.MemoryInfo[] infos = am.getProcessMemoryInfo(new int[]{pid});
            int memory = infos[0].getTotalPss() / 1024;
            ProcessShowBean bean = getProcessShowBean(pkgName, memory);
            if (bean != null) {
                processShowBeanList.add(bean);
            }
        }
        if (mlistener != null) {
            mlistener.onDataFinished(processShowBeanList);
        }
    }

    private static int checkAppType(String pname) {
        try {
            PackageInfo pInfo = mcontext.getPackageManager().getPackageInfo(pname, 0);
            // 是系统软件或者是系统软件更新
            if (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) {
                return SYSTEM_REF_APP;
            } else {
                return USER_APP;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return UNKNOW_APP;
    }

    private static ProcessShowBean getProcessShowBean(String pkgName, int Size) {
        try {
            ProcessShowBean processShowBean = new ProcessShowBean();
            PackageInfo pInfo = mcontext.getPackageManager().getPackageInfo(pkgName, 0);
            processShowBean.setAppIcon(pInfo.applicationInfo.loadIcon(mcontext.getPackageManager()));
            processShowBean.setPakName(pkgName);
            processShowBean.setAppName(pInfo.applicationInfo.loadLabel(mcontext.getPackageManager()).toString());
            processShowBean.setSize(Size);
            return processShowBean;
            } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
            }
    }

    public static boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public static boolean isUserApp(PackageInfo pInfo) {
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }

    public static void setDataListener(DataListener listener) {
        mlistener = listener;
    }

    public interface DataListener{
        void onDataFinished(List<ProcessShowBean> processShowBeanList);
    }
}
