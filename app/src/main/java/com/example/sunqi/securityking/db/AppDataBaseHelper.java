package com.example.sunqi.securityking.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用于存储在防通知打扰，应用锁中哪些应用应该有效
 * Created by sshunsun on 2017/4/24.
 */
public class AppDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app.db";//数据库名称
    private static final int SCHEMA_VERSION = 1;//版本号

    public AppDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, SCHEMA_VERSION);
    }

    public AppDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE app_notify (_id INTEGER PRIMARY KEY AUTOINCREMENT, packname TEXT);");
        db.execSQL("CREATE TABLE app_lock (_id INTEGER PRIMARY KEY AUTOINCREMENT, packname TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
