package com.example.sunqi.securityking.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用于存储在防通知打扰中的数据信息
 * Created by sshunsun on 2017/4/24.
 */
public class NotifyInfoDataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notify_info.db";//数据库名称
    private static final int SCHEMA_VERSION = 1;//版本号

    public NotifyInfoDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, SCHEMA_VERSION);
    }

    public NotifyInfoDataHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE app_notify_info (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, when LONG);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
