package com.example.sunqi.securityking.permission.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.global.GlobalPref;
import com.example.sunqi.securityking.permission.PermissionGuideUtil;

/**
 * Created by sunqi on 2017/5/18.
 */

public class PermissionTutotialRoutingActivity extends Activity{

    public static final String TASK = "task";
    public static final String EXTRA_ACTION = "action";
    public static final String ACTION_KILL_GUIDE_TUTORIAL_BASE_ACTIVITY_TASK = "killself";
    public static final int TASK_TO_GUIDE_NOTIFICATION_READ = Constant.Task.TASK_TO_GUIDE_NOTIFICATION_READ;
    public static final int TASK_TO_GUIDE_APP_USAGE = Constant.Task.TASK_TO_GUIDE_APP_USAGE;
    public static final int TASK_TO_GUIDE_AUTO_STRAT = Constant.Task.TASK_TO_GUIDE_AUTO_STRAT;
    private static final int SHOW_TUTORIAL_ACTIVITY = 1;
    private int mTask = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TUTORIAL_ACTIVITY:
                    launchPermissionSettingTutorial(mTask);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        dealWithIntent(getIntent());
    }



    private void setStatusBarTranslate(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        dealWithIntent(intent);
    }

    private void dealWithIntent(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            String action = getIntent().getStringExtra(EXTRA_ACTION);

            if (!TextUtils.isEmpty(action) && action.equalsIgnoreCase(ACTION_KILL_GUIDE_TUTORIAL_BASE_ACTIVITY_TASK)) {
                finishSelf();
            } else {
                mTask = getIntent().getIntExtra(TASK, 0);
                doTask(mTask);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int task = getIntent().getIntExtra(TASK, 0);
//        if (task == TutorialUtils.FROM_RISK_SAFE_RESULT_PAGE) {
//            if (FloatWindowsPermissionHelper.isAlertWindowPermissionOn(getBaseContext())) {
//                GuideReactor.getInstance().doRiskSafeResultPageTip();
//            }
//        } else if (!setFlag()) {
//            // 可以准确判断权限开启状态的情况，如果执行到这里，说明是用户按back键返回的情况，应对终止引导
//            TutorialUtils.sendGuideFinishBroadcast();
//
//            // 补刀逻辑
////            GuidePermissionSwitchChecker.getInstance().stopChecker();
//        }
        setFlag();
        finishSelf();
    }

    public static void killTask(Context context) {
        try {
            Intent i = new Intent();
            i.putExtra(EXTRA_ACTION, ACTION_KILL_GUIDE_TUTORIAL_BASE_ACTIVITY_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setClass(context, PermissionTutotialRoutingActivity.class);
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishSelf() {
        finish();
        mHandler.removeMessages(SHOW_TUTORIAL_ACTIVITY);
    }

    private void doTask(int task) {
        if (launchPermissionSettingPage(task)) {
            int delayMillis = 1500;
            mHandler.sendEmptyMessageDelayed(SHOW_TUTORIAL_ACTIVITY, delayMillis);
        }
    }

    private boolean launchPermissionSettingPage(int task) {
        Intent intent;
        if(task == TASK_TO_GUIDE_NOTIFICATION_READ) {
            intent = PermissionGuideUtil.getNotificationReadIntent();
        } else if (task == TASK_TO_GUIDE_APP_USAGE) {
            intent = PermissionGuideUtil.getAppUsageIntent();
        } else if (task == TASK_TO_GUIDE_AUTO_STRAT) {
            intent = PermissionGuideUtil.getAutoStartIntent();
        } else {
            return false;
        }

        if (intent != null) {
            this.startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setFlag() {
        if (mTask == TASK_TO_GUIDE_AUTO_STRAT) {
            GlobalPref.getInstance().setSecurityHasEnterAutostart(true);
        }
    }

    private void launchPermissionSettingTutorial(int task) {
        Intent intent;
        if(task == TASK_TO_GUIDE_NOTIFICATION_READ) {
            intent = new Intent(SecurityApplication.getInstance(), TutorialActivity.class);
            intent.putExtra(TutorialActivity.FROM, task);
        } else if (task == TASK_TO_GUIDE_APP_USAGE) {
            intent = new Intent(SecurityApplication.getInstance(), TutorialActivity.class);
            intent.putExtra(TutorialActivity.FROM, task);
        } else if (task == TASK_TO_GUIDE_AUTO_STRAT) {
            intent = new Intent(SecurityApplication.getInstance(), Flyme6AutoSetupPermissionTutorialActivity.class);
            intent.putExtra(TutorialActivity.FROM, task);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent = null;
        }
        if (intent != null) {
            this.startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
