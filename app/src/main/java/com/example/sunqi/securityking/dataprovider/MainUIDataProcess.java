package com.example.sunqi.securityking.dataprovider;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.format.Formatter;
import android.util.Log;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.utils.FileSizeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 提供主界面中内存占比,存储空间占比,以及安装时间(保护时间)
 * Created by sunqi on 2017/5/19.
 */

public class MainUIDataProcess {

    public static double getStorge() {
        return (((double)FileSizeUtil.getAvailableInternalMemorySize()
                /FileSizeUtil.getTotalInternalMemorySize())
                * 100);
    }

    public static double getInternalMemory() {
        return (((double)FileSizeUtil.getAvailableMemory(SecurityApplication.getInstance())
                /FileSizeUtil.getTotalMemorySize(SecurityApplication.getInstance()))
                * 100);
    }

    public static int getInstallDays() {
        long install = getInstallTime(SecurityApplication.getInstance());
        long now = System.currentTimeMillis();
        int days =  (int)((now - install) / (1000 * 60 * 60 * 24));
        return days + 1;
    }

    @SuppressLint("NewApi") // SDK >= 9
    private static long getInstallTime(Context context) {
        if(Build.VERSION.SDK_INT < 9) {
            return 0;
        }
        PackageManager pm = SecurityApplication.getInstance().getApplicationContext().getPackageManager();
        long nowTS = System.currentTimeMillis();
        long installTime = 0;
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            installTime = pi.firstInstallTime;
            if(installTime <= 0 || installTime > nowTS) {
                ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
                String appFile = info.sourceDir;
                installTime = new File(appFile).lastModified();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            installTime = 0;
        }
        return installTime;
    }

    private static String getAvailMemory() {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) SecurityApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(SecurityApplication.getInstance(), mi.availMem);// 将获取的内存大小规格化
    }

    private static String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[0]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(SecurityApplication.getInstance(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }


}
