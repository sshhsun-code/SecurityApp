package com.example.sunqi.securityking.dataprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.sunqi.securityking.db.NotifyInfoDataHelper;

/**
 * Created by sshunsun on 2017/4/25.
 */
public class NotifyDataContentProvider extends ContentProvider {
    private NotifyInfoDataHelper helper;
    public static final int DATA_NOTIFY_INFO = 1;
    private UriMatcher matcher;

    @Override
    public boolean onCreate() {
        helper = new NotifyInfoDataHelper(getContext());
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("notify.security.sunqi", "data_notify", DATA_NOTIFY_INFO);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code = matcher.match(uri);
        Cursor cursor = null;
        if (code == DATA_NOTIFY_INFO) {
            cursor = helper.getReadableDatabase().query("notify_info_data",
                    new String[]{"_id","title","notify_id","content","when","icon","packname"},
            selection,selectionArgs,null,null,sortOrder);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String type = "";
        switch (matcher.match(uri)) {
            case DATA_NOTIFY_INFO:
                type = "vnd.android.cursor.dir/notify_info_data";
                break;
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = matcher.match(uri);
        long id = 0;
        if (code == DATA_NOTIFY_INFO) {
            id = helper.getWritableDatabase().insert("notify_info_data", null, values);
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = matcher.match(uri);
        if (code == DATA_NOTIFY_INFO) {
            helper.getWritableDatabase().delete("notify_info_data", selection, selectionArgs);
        } 
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
