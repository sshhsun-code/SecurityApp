package com.example.sunqi.securityking.service;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.util.Log;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.NotifyData;
import com.example.sunqi.securityking.dataprovider.ASpProvider;
import com.example.sunqi.securityking.global.GlobalPref;
import com.example.sunqi.securityking.ui.RedpacketSettingActivity;
import com.example.sunqi.securityking.utils.Commons;

/**
 * Created by sunqi on 2017/5/10.
 */

public class RedPacketInterceptor {
    private static String TAG = RedPacketInterceptor.class.getSimpleName();

    private static RedPacketInterceptor mInstance = null;
    private Context mContext;

    public synchronized static RedPacketInterceptor getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new RedPacketInterceptor();
        }
        return mInstance;
    }

    private RedPacketInterceptor()
    {
        mContext = SecurityApplication.getInstance();

    }

    public synchronized void executeWebChatGroupRedPacketRecv(final NotifyData notifyData)
    {

        if (!parsedWebChatGroupRedPacket(notifyData, false)) {
            return;
        }

        //是否需要点亮屏幕
        RedPacketBackgroundService.get().tryMakeScreenOn();

        handlePlayTips();

        if (tryOpenRedpacketPage(notifyData)) {

        } else {
            RedPacketBackgroundService.get().sendRedPacketSysNotify(notifyData);
        }
        GlobalPref.getInstance().addSecurityNumRedpacket();//拦截到的红包总数进行更新
        RedpacketSettingActivity.sendRefreshBroadcast();

    }

    /**
     * 快速抢红包【高级功能】 是否开启，开启直接打开红包微信界面
     * @param notifyData
     * @return true 打开， false：不打开
     */
    private boolean tryOpenRedpacketPage(NotifyData notifyData)
    {
        return doOpenRedpacketPage(notifyData);
    }

    private boolean doOpenRedpacketPage(NotifyData notifyData) {
        try{
            //成功执行跳转微信，系统默认就会清空通知栏里面的通知信息。（消息不进入通知面板）
            PendingIntent intent = notifyData.getPendingIntent();
            if (getAutoOpenSwitch()) {
                intent.send();
            }

            //收回系统的通知面板；
            Commons.collapseNotificationsPanel(SecurityApplication.getInstance());

            RedPacketBackgroundService.get().cancelRedPacketSysNotify(notifyData.getPkgName()
                    /*RedPacketNotificationListener.WECHAT_PKG_NAME*/,notifyData.getNotify_id());

            return true;

        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean getAutoOpenSwitch() {
        ContentResolver resolver = SecurityApplication.getInstance().getContentResolver();
        Cursor cursor = resolver.query(ASpProvider.Content_URL,null,GlobalPref.SECURITY_SWITCH_AUTOOPEN_REDPACKET,null,null,null);
        if (cursor == null) {
            return false;
        } else {
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex("value"));
            return Boolean.parseBoolean(result);
        }
    }

    private void handlePlayTips() {
        if (null == mContext) {
            Log.d(TAG, "Context is null.");
            return;
        }
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        switch (mAudioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL: {
                playRedPacketMusic();
            }
            break;
            case AudioManager.RINGER_MODE_SILENT:
                break;
            case AudioManager.RINGER_MODE_VIBRATE: {
                RedPacketBackgroundService.get().sendVibrateMsg(false);
            }
            break;
        }
    }

    /**
     * 播放红包来了的声音提醒
     */
    private void playRedPacketMusic()
    {
        RedPacketBackgroundService.get().playRedPacketMusic();

    }

    private boolean parsedWebChatGroupRedPacket(NotifyData notifyData, boolean b) {
        return true;
    }
}
