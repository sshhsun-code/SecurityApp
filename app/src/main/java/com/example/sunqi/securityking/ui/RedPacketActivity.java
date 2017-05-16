package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.global.GlobalPref;

/**
 * Created by sshunsun on 2017/5/14.
 */
public class RedPacketActivity extends Activity {
    private GlobalPref globalPref = GlobalPref.getInstance();
    private Context mcontext = SecurityApplication.getInstance().getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.activity_redpacket);
        initData();
        initView();
        iniTitle();
    }

    private void iniTitle() {

    }

    private void initView() {

    }

    private void initData() {

    }

    private void setStatusBarTranslate() {
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
