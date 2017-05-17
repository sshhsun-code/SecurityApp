package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.global.GlobalPref;

/**
 * 红包提醒的相关设置
 * Created by sshunsun on 2017/5/14.
 */
public class RedPacketActivity extends Activity implements View.OnClickListener{
    private GlobalPref globalPref = GlobalPref.getInstance();
    private Context mcontext = SecurityApplication.getInstance().getApplicationContext();
    private View includeView;
    private ImageView normal_title_back;
    private ImageView title_items;
    private TextView normal_title_text;
    private TextView record_count;
    private TextView redpacket_permission;
    private ImageView weixin_switch;
    private ImageView qq_switch;
    private ImageView auto_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.activity_redpacket);
        initData();
        iniTitleAndView();
    }

    private void iniTitleAndView() {
        includeView = findViewById(R.id.redpacket_top);
        normal_title_back = (ImageView) includeView.findViewById(R.id.normal_title_back);
        title_items = (ImageView) includeView.findViewById(R.id.title_items);
        title_items.setImageResource(R.drawable.share);
        normal_title_text = (TextView) includeView.findViewById(R.id.normal_title_text);
        normal_title_text.setText(getString(R.string.repacket_notify));
        record_count = (TextView) findViewById(R.id.record_count);
        redpacket_permission = (TextView) findViewById(R.id.redpacket_permission);
        weixin_switch = (ImageView) findViewById(R.id.weixin_switch);
        qq_switch = (ImageView) findViewById(R.id.qq_switch);
        auto_switch = (ImageView) findViewById(R.id.auto_switch);
        normal_title_back.setOnClickListener(this);
        title_items.setOnClickListener(this);
        redpacket_permission.setOnClickListener(this);
        weixin_switch.setOnClickListener(this);
        qq_switch.setOnClickListener(this);
        auto_switch.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_title_back:
                break;
            case R.id.title_items:
                break;
            case R.id.redpacket_permission:
                break;
            case R.id.weixin_switch:
                break;
            case R.id.qq_switch:
                break;
            case R.id.auto_switch:
                break;
        }
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
