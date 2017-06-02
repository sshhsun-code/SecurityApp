package com.example.sunqi.securityking.dataprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;

public class ASpProvider extends ContentProvider {

	public static final String AUTHORITY = "com.sunqi.provider";

	public static final String SP_NAME_STRING = "GlobalConfig";
	public static final Uri Content_URL = Uri.parse("content://" + AUTHORITY + "/" + SP_NAME_STRING);

	public static final int SP = 1;

	static UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, SP_NAME_STRING, SP);// 访问SharedPreference

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.i(getClass().getSimpleName(), "query");

		switch (sUriMatcher.match(uri)) {
		case SP:
			SharedPreferences sp = getSharedPreference(SP_NAME_STRING);
				//两列 数据
			MatrixCursor cursor = new MatrixCursor(new String[] { "key", "value" });
			Map<String, ?> map = sp.getAll();
			if (map == null || map.size() == 0) {
				Log.i(getClass().getSimpleName(), "map 为空");
				return null;
			}
			if (selection != null) {
				Log.i(getClass().getSimpleName(), "查询 单条");
				//统一以String 类型返回
				if (map.containsKey(selection)) {
					cursor.addRow(new String[] { selection, String.valueOf(map.get(selection)) });
				} else {
					return null;
				}
			} else {
				Log.i(getClass().getSimpleName(), "查询 全部");
				Iterator<String> iterator = map.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					cursor.addRow(new String[] { key, String.valueOf(map.get(key)) });
				}
			}
			return cursor;

		default:
			break;
		}
		return null;
	}

	private SharedPreferences getSharedPreference(String spName) {
		return getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
	}

	private void saveData(String type, Editor editor, String key, String value) {
		Log.i(getClass().getSimpleName(), "type="+type);
		if (int.class.getSimpleName().equals(type)) {
			editor.putInt(key, Integer.parseInt(value)).commit();
		} else if (Boolean.class.getSimpleName().equals(type)) {
			editor.putBoolean(key, Boolean.parseBoolean(value)).commit();
		} else {
			Log.e(getClass().getSimpleName(), "未定义保存类型=" + type);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case SP:
			saveData(selection, getSharedPreference(SP_NAME_STRING).edit(), values.getAsString("key"), values.getAsString("value"));
			break;

		default:
			break;
		}
		return 0;
	}
}
