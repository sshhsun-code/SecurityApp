package com.example.sunqi.securityking.service;

import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.NotifyData;
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

    /**
     * 若是红包消息，执行红包来的相关后续行为:
     * 点亮屏幕，
     *
     * 注意： 这个是一个非常耗时的行为，必须非UI异步执行
     * @param notifyData
     */
    public synchronized void executeWebChatGroupRedPacketRecv(final NotifyData notifyData)
    {

        if (!parsedWebChatGroupRedPacket(notifyData, false)) {
            return;
        }

        //是否需要点亮屏幕
        RedPacketBackgroundService.get().tryMakeScreenOn();

        handlePlayTips();

        boolean isRemindMsgOnly = true;


        if (tryOpenRedpacketPage(notifyData)) {
            isRemindMsgOnly = false;

        }
        else
        {
            RedPacketBackgroundService.get().sendRedPacketSysNotify(notifyData);
        }

        RedpacketSettingActivity.sendRefreshBroadcast();

    }

    /**
     * 快速抢红包【高级功能】 是否开启，开启直接打开红包微信界面
     * @param notifyData
     * @return true 打开， false：不打开
     */
    private boolean tryOpenRedpacketPage(NotifyData notifyData)
    {
//        if(!RedpacketSettingManager.getInstance().isFastObtain()) {
//            return false;
//        }
//
//        // 横屏状态下，不自动打开应用
//        if (DimenUtils.isOrientationLandscape(MobileDubaApplication.getInstance())) {
//            return false;
//        }
//
//        //锁屏状态下，不自动打开微信
//        if(RedPacketBackgroundService.get().isInKeyguard())
//        {
//            DebugMode.Log(TAG, "Not Open wechat Now into keyguard!! ");
//            return false;
//        }

        return doOpenRedpacketPage(notifyData);

    }

    private boolean doOpenRedpacketPage(NotifyData notifyData) {
        try{
            //成功执行跳转微信，系统默认就会清空通知栏里面的通知信息。（消息不进入通知面板）
            PendingIntent intent = notifyData.getPendingIntent();
            intent.send();

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
