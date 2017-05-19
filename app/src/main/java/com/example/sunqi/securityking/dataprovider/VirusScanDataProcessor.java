package com.example.sunqi.securityking.dataprovider;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.example.sunqi.securityking.bean.VirusApp;
import com.example.sunqi.securityking.bean.VirusShowBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunqi on 2017/5/19.
 */

public class VirusScanDataProcessor {

    private static ScanListener scanListener = null;

    public static void requestVirusAppData(List<ResolveInfo> installList, List<VirusApp> virusAppList, PackageManager manager) {
        List<VirusShowBean> result = new ArrayList<>();
        VirusShowBean virusShowBean;
        int index, index2;
        for (index = 0; index < installList.size(); index++) {
            for (index2 = 0; index2 < virusAppList.size(); index2++) {
                if (installList.get(index).activityInfo.packageName.equals(virusAppList.get(index2).getPkgName())) { //匹配
                    //添加到匹配集合中
                    virusShowBean = new VirusShowBean();
                    virusShowBean.setAppName(installList.get(index).activityInfo.loadLabel(manager).toString());
                    virusShowBean.setPakName(installList.get(index).activityInfo.packageName);
                    virusShowBean.setAppIcon(installList.get(index).activityInfo.loadIcon(manager));
                    virusShowBean.setType(virusAppList.get(index2).getType());
                    virusAppList.remove(index2);
                    break;
                }
            }
            if (virusAppList.size() == 0) {
                if (scanListener != null) {
                    scanListener.onDataFinished(result);
                }
                break;
            }
        }
    }

    public static void setScanListener(ScanListener mScanListener) {
        scanListener = mScanListener;
    }


    public interface ScanListener {
        void onDataFinished(List<VirusShowBean> virusShowBeanList);
    }
}
