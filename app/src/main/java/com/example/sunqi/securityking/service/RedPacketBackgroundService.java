package com.example.sunqi.securityking.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.TextUtils;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.NotifyData;

/**
 * Created by sunqi on 2017/5/10.
 */

public class RedPacketBackgroundService extends HandlerThread {


    private static final String TAG = RedPacketBackgroundService.class.getSimpleName();
    private static RedPacketBackgroundService sInstance;

    private PowerManager.WakeLock  mWakeLock = null;

    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock kl = null;
    private Handler mHandler;
    private Context mContext;

    private SoundPool mSoundPool;
    private int mSoundId;
    private boolean mSouldLoadFlag = false;
    private int mStreamType = AudioManager.STREAM_SYSTEM;
    //最大音量
    private int mMaxVolume = 0;
    //当前音量
    private int mCurrentVolume = 0;

    private String mSoundFilePath;

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
        loadSound(getCurrentSoundResourceID());
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
    private String getCurrentSoundResourceID()
    {
        return String.valueOf(R.raw.redpacketcome);
    }


    private void loadSound(String soundPath)
    {
        if (mSoundPool == null) {

            // 实现soundPool对象
            mSoundPool = new SoundPool(1, mStreamType, 0); //分别对应声音池数量，AudioManager.STREAM_MUSIC 和 0

            //使用soundPool加载声音，该操作位异步操作，如果资源很大，需要一定的时间
            if (soundPath.startsWith("/")) {
                mSoundId = mSoundPool.load(soundPath, 1);
            } else {
                mSoundId = mSoundPool.load(SecurityApplication.getInstance(), Integer.valueOf(soundPath), 1);
            }

            //为声音池设定加载完成监听事件
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    mSouldLoadFlag = true;    //  表示加载完成
                }
            });
            mSoundFilePath = soundPath;
        }
    }

    private void releaseSound()
    {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
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

            if (pm.isScreenOn()) {
                return;
            }
            mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, this.getClass().getCanonicalName());
        }
            mWakeLock.acquire();

            km = (KeyguardManager) SecurityApplication.getInstance().getSystemService(Context.KEYGUARD_SERVICE);
            kl = km.newKeyguardLock("unLock");

            kl.disableKeyguard();

            mHandler.sendEmptyMessageDelayed(MSG_SCREEN_WAKE_OFF, 10 * 1000);

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
            if (kl != null) {
                kl.reenableKeyguard();
            }


            km = null;
            kl = null;
            mWakeLock = null;

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
        try {
            AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

            if(mSoundPool != null && mAudioManager != null)
            {
                if(mMaxVolume == 0)
                {
                    mMaxVolume = mAudioManager.getStreamMaxVolume(mStreamType);
                }
                mCurrentVolume = mAudioManager.getStreamVolume(mStreamType);

                /**
                 * 静音状态下，不进行播放声音的操作。
                 */
                if(mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL || 0 == mCurrentVolume)
                {
                    return;
                }

                //非锁屏 + 非静音 -> 红包声音音量使用系统当前音量
                int playVolume = /*isInKeyguard() ? mMaxVolume : */mCurrentVolume;

                float volume = (mMaxVolume == 0) ? 1f : playVolume / (1f * mMaxVolume);

//                DebugMode.logw(TAG, "stream volume, max/current=" + mMaxVolume + "/" + mCurrentVolume+" playVolume:"+playVolume);
                mAudioManager.setStreamVolume(mStreamType, playVolume, 0);

                mSoundPool.play(mSoundId, volume, volume, 1, 0, 1.0f);

                if(playVolume != mCurrentVolume)
                {
                    //恢复当前音量
                    mAudioManager.setStreamVolume(mStreamType, mCurrentVolume, 0);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
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
