package com.example.sunqi.securityking.dataprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sunqi.securityking.db.AppDataBaseHelper;

/**
 * Created by sshunsun on 2017/4/25.
 */
public class MyContentProvider extends ContentProvider {
    private AppDataBaseHelper helper;
    public static final int APP_NOTIFY = 1;
    public static final int APP_LOCK = 2;
    private UriMatcher matcher;

    @Override
    public boolean onCreate() {
        helper = new AppDataBaseHelper(getContext());
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("app.security.sunqi", "app_notify", APP_NOTIFY);
        matcher.addURI("app.security.sunqi", "app_lock", APP_LOCK);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code = matcher.match(uri);
        Cursor cursor = null;
        if (code == APP_NOTIFY) {
            cursor = helper.getReadableDatabase().query("app_notify",
                    new String[]{"_id","packname"},
            selection,selectionArgs,null,null,sortOrder);
        } else if (code == APP_LOCK){
            cursor = helper.getReadableDatabase().query("app_lock",
                    new String[]{"_id","packname"},
                    selection,selectionArgs,null,null,sortOrder);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String type = "";
        switch (matcher.match(uri)) {
            case APP_NOTIFY:
                type = "vnd.android.cursor.dir/app_notify";
                break;
            case APP_LOCK:
                type = "vnd.android.cursor.dir/app_lock";
                break;
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = matcher.match(uri);
        long id = 0;
        if (code == APP_NOTIFY) {
            id = helper.getWritableDatabase().insert("app_notify", null, values);
        } else if (code == APP_LOCK){
            id = helper.getWritableDatabase().insert("app_lock", null, values);
        }
        Log.e("insert","id:"+id);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = matcher.match(uri);
        if (code == APP_NOTIFY) {
            helper.getWritableDatabase().delete("app_notify", "packname = ?", selectionArgs);
        } else if (code == APP_LOCK){
            helper.getWritableDatabase().delete("app_lock", "packname = ?", selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
