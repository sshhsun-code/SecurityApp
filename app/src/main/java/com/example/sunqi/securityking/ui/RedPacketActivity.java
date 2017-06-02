package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.sunqi.securityking.dataprovider.ASpProvider;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.global.GlobalPref;
import com.example.sunqi.securityking.permission.PermissionManager;
import com.example.sunqi.securityking.permission.ui.PermissionTutotialRoutingActivity;

/**
 * 红包提醒的相关设置
 * Created by sshunsun on 2017/5/14.
 */
public class RedPacketActivity extends Activity implements View.OnClickListener{
    private GlobalPref globalPref = GlobalPref.getInstance();
    private Context mcontext = SecurityApplication.getInstance().getApplicationContext();
    private boolean autoOpen = false;
    private boolean weiXinOpen = false;
    private boolean QQOpen = false;
    private View includeView;
    private ImageView normal_title_back;
    private ImageView title_items;
    private TextView normal_title_text;
    private TextView record_count;
    private TextView redpacket_permission;
    private ImageView weixin_switch;
    private ImageView qq_switch;
    private ImageView auto_switch;
    private String apkPath = Constant.PATH.APKURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.activity_redpacket);
        initData();
        initPref();
        iniTitleAndView();
    }

    private void initPref() {
        if (globalPref.getSecurityKeyIsFirstIn()) {
        globalPref.setSecuritySwitchWeixinRedpacket(true);
        globalPref.setSecuritySwitchQqRedpacket(false);
        globalPref.setSecuritySwitchAutoopenRedpacket(true);
        }
    }

    private void iniTitleAndView() {
        includeView = findViewById(R.id.redpacket_top);
        normal_title_back = (ImageView) includeView.findViewById(R.id.normal_title_back);
        title_items = (ImageView) includeView.findViewById(R.id.title_items);
        title_items.setImageResource(R.drawable.share);
        normal_title_text = (TextView) includeView.findViewById(R.id.normal_title_text);
        normal_title_text.setText(getString(R.string.repacket_notify));
        record_count = (TextView) findViewById(R.id.record_count);
        record_count.setText(getWeixinRedpacketNum()+"");
        redpacket_permission = (TextView) findViewById(R.id.redpacket_permission);
        weixin_switch = (ImageView) findViewById(R.id.weixin_switch);
        weixin_switch.setImageResource(globalPref.getSecuritySwitchWeixinRedpacket() ? R.drawable.switch_open : R.drawable.switch_close);
        qq_switch = (ImageView) findViewById(R.id.qq_switch);
        qq_switch.setImageResource(globalPref.getSecuritySwitchQqRedpacket() ? R.drawable.switch_open : R.drawable.switch_close);
        auto_switch = (ImageView) findViewById(R.id.auto_switch);
        auto_switch.setImageResource(globalPref.getSecuritySwitchAutoopenRedpacket() ? R.drawable.switch_open_1 : R.drawable.switch_close);
        normal_title_back.setOnClickListener(this);
        title_items.setOnClickListener(this);
        redpacket_permission.setOnClickListener(this);
        weixin_switch.setOnClickListener(this);
        qq_switch.setOnClickListener(this);
        auto_switch.setOnClickListener(this);
    }

    private int getWeixinRedpacketNum() {
        ContentResolver resolver = SecurityApplication.getInstance().getContentResolver();
        Cursor cursor = resolver.query(ASpProvider.Content_URL,null,GlobalPref.SECURITY_NUM_REDPACKET,null,null,null);
        if (cursor == null) {
            return 0;
        } else {
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex("value"));
            return Integer.parseInt(result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniTitleAndView();
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_title_back:
                finish();
                break;
            case R.id.title_items:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "应用分享");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi~,我发现一款红包提醒神器，赶紧下载哦♥♥" + apkPath);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.redpacket_permission:
                PermissionManager.GuidePermission(PermissionTutotialRoutingActivity.TASK_TO_GUIDE_NOTIFICATION_READ);
                break;
            case R.id.weixin_switch:
                if (globalPref.getSecuritySwitchWeixinRedpacket()) {
                    weixin_switch.setImageResource(R.drawable.switch_close);
                    globalPref.setSecuritySwitchWeixinRedpacket(false);
                } else {
                    weixin_switch.setImageResource(R.drawable.switch_open);
                    globalPref.setSecuritySwitchWeixinRedpacket(true);
                }
                break;
            case R.id.qq_switch:
                if (globalPref.getSecuritySwitchQqRedpacket()) {
                    qq_switch.setImageResource(R.drawable.switch_close);
                    globalPref.setSecuritySwitchQqRedpacket(false);
                } else {
                    qq_switch.setImageResource(R.drawable.switch_open);
                    globalPref.setSecuritySwitchQqRedpacket(true);
                }
                break;
            case R.id.auto_switch:
                if (globalPref.getSecuritySwitchAutoopenRedpacket()) {
                    auto_switch.setImageResource(R.drawable.switch_close);
                    globalPref.setSecuritySwitchAutoopenRedpacket(false);
                } else {
                    auto_switch.setImageResource(R.drawable.switch_open_1);
                    globalPref.setSecuritySwitchAutoopenRedpacket(true);
                }
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
