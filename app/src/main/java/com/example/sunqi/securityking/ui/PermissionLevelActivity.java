package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.permission.PermissionManager;
import com.example.sunqi.securityking.permission.ui.PermissionTutotialRoutingActivity;

/**
 * Created by sunqi on 2017/5/17.
 */

public class PermissionLevelActivity extends Activity implements View.OnClickListener{
    private TextView rank_protect_main_button;
    private RelativeLayout rank_protect_monitor_card;
    private RelativeLayout rank_protect_antiharass_card;
    private RelativeLayout rank_protect_applock_card;
    private TextView permission_level_main_title;

    private ImageView level_applock_card_switch_ok;
    private ImageView levle_antiharass_card_switch_ok;
    private ImageView level_monitor_card_switch_ok;
    private TextView  level_monitor_card_switch_not_ok;
    private TextView  level_applock_card_switch_not_ok;
    private TextView  levle_antiharass_card_switch_not_ok;
    private TextView  levle_protect_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.activity_permission_layoutt);
        initView();
    }


    private void initView() {
        rank_protect_main_button = (TextView) findViewById(R.id.rank_protect_main_button);
        rank_protect_main_button.setOnClickListener(this);
        rank_protect_main_button.setVisibility(View.GONE);
        rank_protect_monitor_card = (RelativeLayout) findViewById(R.id.rank_protect_monitor_card);
        rank_protect_antiharass_card = (RelativeLayout) findViewById(R.id.rank_protect_antiharass_card);
        rank_protect_applock_card = (RelativeLayout) findViewById(R.id.rank_protect_applock_card);
        permission_level_main_title = (TextView) findViewById(R.id.permission_level_main_title);
        level_applock_card_switch_ok = (ImageView) findViewById(R.id.level_applock_card_switch_ok);
        levle_antiharass_card_switch_ok = (ImageView) findViewById(R.id.levle_antiharass_card_switch_ok);
        level_monitor_card_switch_ok = (ImageView) findViewById(R.id.level_monitor_card_switch_ok);
        level_monitor_card_switch_not_ok = (TextView) findViewById(R.id.level_monitor_card_switch_not_ok);
        level_applock_card_switch_not_ok = (TextView) findViewById(R.id.level_applock_card_switch_not_ok);
        levle_antiharass_card_switch_not_ok = (TextView) findViewById(R.id.levle_antiharass_card_switch_not_ok);
        levle_protect_back = (TextView) findViewById(R.id.levle_protect_back);
        rank_protect_applock_card.setOnClickListener(this);
        rank_protect_antiharass_card.setOnClickListener(this);
        rank_protect_monitor_card.setOnClickListener(this);
        levle_protect_back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    private void refreshUI() {
        refreshBackGround();
        refreshTitle();
        refreshCard();
    }

    private void refreshBackGround() {
        findViewById(R.id.level_text_layout).setBackgroundResource(getBgColorId());
    }



    private int getBgColorId() {
        return R.color.notification_background_color_blue;
//        Constant.ProtectLevel level = PermissionManager.getLevel();
//        if (level == Constant.ProtectLevel.HIGH) {
//            return R.color.cn_rank_protect_bg_blue;
//        } else if (level == Constant.ProtectLevel.MID) {
//            return R.color.cn_rank_protect_bg_yellow;
//        } else {
//            return R.color.cn_rank_protect_bg_red;
//        }
    }


    private void refreshCard() {
        refreshCardView(level_monitor_card_switch_ok, level_monitor_card_switch_not_ok,PermissionManager.checkPermission(Constant.Permission.AUTO_SETUP));
        refreshCardView(levle_antiharass_card_switch_ok, levle_antiharass_card_switch_not_ok,PermissionManager.checkPermission(Constant.Permission.NOTIFICATION_READ));
        refreshCardView(level_applock_card_switch_ok, level_applock_card_switch_not_ok,PermissionManager.checkPermission(Constant.Permission.USAGE_STATS));
    }

    private void refreshCardView(View viewOk, View viewNotOk, boolean isOpen) {
        if (isOpen) {
            viewOk.setVisibility(View.VISIBLE);
            viewNotOk.setVisibility(View.GONE);
        } else {
            viewOk.setVisibility(View.GONE);
            viewNotOk.setVisibility(View.VISIBLE);
        }
    }

    private void refreshTitle() {
        Constant.Level level = PermissionManager.getLevel();
        if (level == Constant.Level.HIGH) {
            permission_level_main_title.setText("极高");
        } else if (level == Constant.Level.MID) {
            permission_level_main_title.setText("待提升");
        } else {
            permission_level_main_title.setText("极低");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rank_protect_main_button:
                PermissionManager.GuidePermission(PermissionTutotialRoutingActivity.TASK_TO_GUIDE_NOTIFICATION_READ);
                break;
            case R.id.rank_protect_applock_card:
                if (PermissionManager.checkPermission(Constant.Permission.USAGE_STATS)) {
                    return;
                }
                PermissionManager.GuidePermission(PermissionTutotialRoutingActivity.TASK_TO_GUIDE_APP_USAGE);
                break;
            case R.id.rank_protect_antiharass_card:
                if (PermissionManager.checkPermission(Constant.Permission.NOTIFICATION_READ)) {
                    return;
                }
                PermissionManager.GuidePermission(PermissionTutotialRoutingActivity.TASK_TO_GUIDE_NOTIFICATION_READ);
                break;
            case R.id.rank_protect_monitor_card:
                if (PermissionManager.checkPermission(Constant.Permission.AUTO_SETUP)) {
                    return;
                }
                PermissionManager.GuidePermission(PermissionTutotialRoutingActivity.TASK_TO_GUIDE_AUTO_STRAT);
                break;
            case R.id.levle_protect_back:
                finish();
                break;
        }
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
}
