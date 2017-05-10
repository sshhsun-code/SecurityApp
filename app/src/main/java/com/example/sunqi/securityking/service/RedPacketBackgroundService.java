package com.example.sunqi.securityking.service;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.NotifyData;

/**
 * Created by sunqi on 2017/5/10.
 */

public class RedPacketBackgroundService extends HandlerThread {


    private static final String TAG = RedPacketBackgroundService.class.getSimpleName();
    private static RedPacketBackgroundService sInstance;

    private PowerManager.WakeLock  mWakeLock = null;

    private Handler mHandler;
    private Context mContext;

    /**
     * 一个可能是红包消息发送过来
     */
    private static final int MSG_RED_PACKET_RECV = 0;

    /**
     * 红包来啦播放声音
     */
    public static final int MSG_RED_PACKET_COME_MUSIC_PLAY = 4;

    /**
     * 红包设置相关改变消息
     */
    public static final int MSG_SETTING_OPEN_REMIND = 5;

    /**
     * 是否需要点亮屏幕相关
     */
    public static final int MSG_SCREEN_WAKE_ON = 6;
    private static final int MSG_SCREEN_WAKE_OFF = 7;

    /**
     * 进行震动
     */
    public static final int MSG_VIBRATE_PLAY = 8;

    /**
     * 发送红包通知到系统通知栏中
     */
    private static final int MSG_SEND_RED_PACKET_SYS_NOTIFY = 12;




    public RedPacketBackgroundService() {
        super("RedPacketBackgroundService-Thread", android.os.Process.THREAD_PRIORITY_BACKGROUND);

        mContext = SecurityApplication.getInstance();
    }

    public static RedPacketBackgroundService get() {
        synchronized (RedPacketBackgroundService.class) {
            ensureThreadLocked();
            return sInstance;
        }
    }

    private static synchronized void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new RedPacketBackgroundService();
            sInstance.start();
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        mHandler = new MyHandler(this.getLooper());
    }


    public void sendRedPakcetRecvMsg(Object data)
    {
        Message m = mHandler.obtainMessage(MSG_RED_PACKET_RECV, data);
        mHandler.sendMessageAtFrontOfQueue(m);
    }

    /**
     * 处理红包来的消息做的事情
     */
    private void handleRedPacketRecv(Object obj)
    {
        RedPacketInterceptor.getInstance().executeWebChatGroupRedPacketRecv((NotifyData)obj);
    }

    /**
     * 点亮屏幕
     */
    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);

            if(pm.isScreenOn ())
            {
                return;
            }
            mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, this.getClass().getCanonicalName());
            mWakeLock.acquire();

            mHandler.sendEmptyMessageDelayed(MSG_SCREEN_WAKE_OFF, 10 * 1000);

        }

    }

    /**
     * 播放红包来了的声音提醒
     */
    public void playRedPacketMusic()
    {
        mHandler.removeMessages(MSG_RED_PACKET_COME_MUSIC_PLAY);
        mHandler.sendEmptyMessageDelayed(MSG_RED_PACKET_COME_MUSIC_PLAY, 50);
    }

    public void sendVibrateMsg(boolean isCancel)
    {
        Message m = mHandler.obtainMessage(MSG_VIBRATE_PLAY, isCancel);
        mHandler.sendMessage(m);
    }

    public void tryMakeScreenOn()
    {
        mHandler.sendEmptyMessage(MSG_SCREEN_WAKE_ON);

    }

    private void doVirate(boolean isCancel)
    {
        try {
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);

            if(isCancel)
            {
                vibrator.cancel();

            }else {
//
//                if(!isDoVirate())
//                {
//                    return;
//                }

                long[] pattern = {0, 3000, 10};
                vibrator.vibrate(pattern, -1);
            }


        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 去掉屏幕锁，以省电；
     */
    private void releaseWakeLock() {
        if (mWakeLock !=null && mWakeLock.isHeld()) {

            mWakeLock.release();
            mWakeLock =null;

        }
    }

    /**
     * 发送红包消息到系统通知栏
     */
    public void sendRedPacketSysNotify(Object data)
    {
        Message m = mHandler.obtainMessage(MSG_SEND_RED_PACKET_SYS_NOTIFY, data);
        mHandler.sendMessage(m);
    }

    private void handleRedPacketSystemNotify(Object obj) {
    }

    private void handlePlayMusic()
    {

    }

    public void cancelRedPacketSysNotify(String pkgName, int id) {

    }

    private class MyHandler extends Handler {
        MyHandler() {

        }

        MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_RED_PACKET_RECV:
                    handleRedPacketRecv(msg.obj);
                    break;
                case MSG_SCREEN_WAKE_OFF:
                    releaseWakeLock();
                    break;
                case MSG_SCREEN_WAKE_ON:
                    acquireWakeLock();
                    break;
                case MSG_RED_PACKET_COME_MUSIC_PLAY:
                    handlePlayMusic();
                    break;
                case MSG_VIBRATE_PLAY:
                    doVirate(false);
                    break;
                case MSG_SEND_RED_PACKET_SYS_NOTIFY:
                    handleRedPacketSystemNotify(msg.obj);
                    break;
            }
        }
    }


}
