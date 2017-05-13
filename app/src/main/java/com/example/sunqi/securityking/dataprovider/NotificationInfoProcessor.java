package com.example.sunqi.securityking.dataprovider;

import android.content.Context;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.NotifyData;

import java.util.ArrayList;

/**
 * Created by sunqi on 2017/5/14.
 */
public class NotificationInfoProcessor {

    private static onDataListener onDataListener;
    private static ArrayList<NotifyData> dataList = new ArrayList<>();
    private static Context mcontext = SecurityApplication.getInstance();

    static {
        init();
    }

    private static void init() {

    }

    public static void setDataListener(onDataListener listener) {
        onDataListener = listener;
    }

    public static void requsetNotifyData() {
        dataList.clear();
        dataList = getNotifyDataFromDB();
        if (onDataListener != null) {
            if (dataList.isEmpty()) {
                onDataListener.onDataNone();
            } else {
                onDataListener.onDataFinished(dataList);
            }
        }
    }

    /**
     * 删除数据库中的一条记录
     * @param data
     */
    public static void removeNotifyData(NotifyData data) {
        dataList.clear();
        NotifyDataProcessor.removeNotifyData(data.getNotify_id());
    }

    /**
     * 删除数据库中所有的数据
     */
    public static void removeAllNotifyData() {
        NotifyDataProcessor.removeAllNotifyData();
    }

    private static ArrayList<NotifyData> getNotifyDataFromDB() {
        ArrayList<NotifyData> result = NotifyDataProcessor.getAllNotifyData();
        return result;
    }

    private static ArrayList<NotifyData> getNotifiyDataFromCache() {
        return null;
    }

    public interface onDataListener {

        void onDataFinished(ArrayList<NotifyData> applist);

        void onDataNone();
    }
}
