package com.example.sunqi.securityking.bean;

import android.graphics.Bitmap;

/**
 * Created by sshunsun on 2017/5/8.
 */
public class NotifyData {
    private String title;
    private int notify_id;
    private String content;
    private long LONG;
    private Bitmap icon;

    public NotifyData() {

    }

    public NotifyData(String title, int notify_id, String content, long LONG, Bitmap icon) {
        this.title = title;
        this.notify_id = notify_id;
        this.content = content;
        this.LONG = LONG;
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotify_id(int notify_id) {
        this.notify_id = notify_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLONG(long LONG) {
        this.LONG = LONG;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getTitle() {

        return title;
    }

    public int getNotify_id() {
        return notify_id;
    }

    public String getContent() {
        return content;
    }

    public long getLONG() {
        return LONG;
    }

    public Bitmap getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "NotifyData{" +
                "title='" + title + '\'' +
                ", notify_id=" + notify_id +
                ", content='" + content + '\'' +
                ", LONG=" + LONG +
                '}';
    }
}
