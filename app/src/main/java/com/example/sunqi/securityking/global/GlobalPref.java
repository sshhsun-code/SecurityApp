package com.example.sunqi.securityking.global;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.dataprovider.ASpProvider;

/**
 * Created by sshunsun on 2017/5/6.
 */
public class GlobalPref {
    private static SharedPreferences sharedPerences = null;
    private static SharedPreferences.Editor editor = null;
    private static GlobalPref instance = null;

    public static final String SECURITY_KEY_SWITCH_NOTIFY = "key_switch_notify";
    public static final String SECURITY_KEY_SWITCH_APPLOCK = "key_switch_applock";
    public static final String SECURITY_KEY_SWITCH_REDPACKET = "key_switch_redpacket";
    public static final String SECURITY_KEY_IS_FIRST_IN = "key_is_first_in";


    public static final String SECURITY_SWITCH_WEIXIN_REDPACKET = "key_switch_weixin_redpacket";
    public static final String SECURITY_SWITCH_QQ_REDPACKET = "key_switch_qq_redpacket";
    public static final String SECURITY_SWITCH_AUTOOPEN_REDPACKET = "key_switch_autoopen_redpacket";
    public static final String SECURITY_NUM_REDPACKET = "key_num_redpacket";

    public static final String SECURITY_HAS_ENTER_AUTOSTART = "key_has_enter_auto_start";

    protected GlobalPref() {
    }

    public static GlobalPref getInstance(Context context) {
        if (instance == null && context != null) {
            synchronized (GlobalPref.class) {
                instance = new GlobalPref();
                initShared(context, "GlobalConfig");
            }
        }
        return instance;
    }

    public static GlobalPref getInstance() {
       return getInstance(SecurityApplication.getInstance());
    }

    private static void initShared(Context ctx, String sharedname) {
        sharedPerences = ctx.getSharedPreferences(sharedname, Context.MODE_PRIVATE);
        editor = sharedPerences.edit();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public void putInteger(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return sharedPerences.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        return sharedPerences.getString(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return sharedPerences.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPerences.getBoolean(key, defValue);
    }

    public int getInteger(String key, int defValue) {
        return sharedPerences.getInt(key, defValue);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public void setSecuritySwitchWeixinRedpacket(boolean hasEnterAutostart) {
        putBoolean(SECURITY_SWITCH_WEIXIN_REDPACKET,hasEnterAutostart);
        savaContentPrvd(SECURITY_SWITCH_WEIXIN_REDPACKET,hasEnterAutostart+"",Boolean.class.getSimpleName());
    }

    private void savaContentPrvd(String key,String value,String type){
        ContentValues values=new ContentValues();
        values.put("key", key);
        values.put("value", value);
        SecurityApplication.getInstance().getContentResolver().update(ASpProvider.Content_URL, values, type, null);
    }

    public boolean getSecuritySwitchWeixinRedpacket() {
        return getBoolean(SECURITY_SWITCH_WEIXIN_REDPACKET, false);
    }

    public void setSecurityKeyIsFirstIn(boolean IsFirstIn) {
        putBoolean(SECURITY_KEY_IS_FIRST_IN,IsFirstIn);
    }

    public boolean getSecurityKeyIsFirstIn() {
        return getBoolean(SECURITY_KEY_IS_FIRST_IN, true);
    }

    public void setSecuritySwitchAutoopenRedpacket(boolean hasEnterAutostart) {
        putBoolean(SECURITY_SWITCH_AUTOOPEN_REDPACKET,hasEnterAutostart);
        savaContentPrvd(SECURITY_SWITCH_AUTOOPEN_REDPACKET,hasEnterAutostart+"",Boolean.class.getSimpleName());
    }

    public boolean getSecuritySwitchAutoopenRedpacket() {
        return getBoolean(SECURITY_SWITCH_AUTOOPEN_REDPACKET, false);
    }

    public void setSecuritySwitchQqRedpacket(boolean hasEnterAutostart) {
        putBoolean(SECURITY_SWITCH_QQ_REDPACKET,hasEnterAutostart);
    }

    public boolean getSecuritySwitchQqRedpacket() {
        return getBoolean(SECURITY_SWITCH_QQ_REDPACKET, false);
    }

    public void addSecurityNumRedpacket() {
        int num = getInteger(SECURITY_NUM_REDPACKET, 0);
        putInteger(SECURITY_NUM_REDPACKET,num + 1);
    }

    public int getSecurityNumRedpacket() {
        return getInteger(SECURITY_NUM_REDPACKET, 0);
    }

    public void setSecurityHasEnterAutostart(boolean hasEnterAutostart) {
        putBoolean(SECURITY_HAS_ENTER_AUTOSTART,hasEnterAutostart);
    }

    public boolean getSecurityHasEnterAutostart() {
        return getBoolean(SECURITY_HAS_ENTER_AUTOSTART, false);
    }
}
