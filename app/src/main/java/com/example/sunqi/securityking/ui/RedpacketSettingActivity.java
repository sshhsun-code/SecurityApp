package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.NotifyAppInfo;
import com.example.sunqi.securityking.dataprovider.NotifyDataProcessor;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.service.NotificationMoniter;
import com.example.sunqi.securityking.utils.NotifyServiceUtil;

import java.util.ArrayList;

/**
 * 红包设置页面
 * Created by sshunsun on 2017/5/8
 */
public class RedpacketSettingActivity extends Activity implements View.OnClickListener {

    public static final String BROADCAST_ACTION_REFRESH_REDPACKET_SETTING_UI = "broadcast_action_refresh_redpacket_setting_ui";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onClick(View view) {

    }

    public static void sendRefreshBroadcast() {
        SecurityApplication.getInstance().sendBroadcast(new Intent(
                RedpacketSettingActivity.BROADCAST_ACTION_REFRESH_REDPACKET_SETTING_UI));
    }
}