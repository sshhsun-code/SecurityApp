package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.VirusApp;
import com.example.sunqi.securityking.bean.VirusShowBean;
import com.example.sunqi.securityking.customview.RadarScanView;
import com.example.sunqi.securityking.dataprovider.VirusScanDataProcessor;
import com.example.sunqi.securityking.xmlparser.PullParseService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.sunqi.securityking.R.drawable.manager;

/**
 * Created by sunqi on 2017/5/16.
 */

public class ScanVirusActicity extends Activity implements VirusScanDataProcessor.ScanListener{

    private ArrayList<String> apps = new ArrayList<>();
    private List<VirusShowBean> mVirusShowBeanList = new ArrayList<>();

    private RadarScanView racarscanview;
    private TextView scan_state;
    private TextView app_name;

    private Handler mhandler;
    private HandlerThread scanThread;
    private Handler scanHandler;

    private boolean isScanning = true;

    private static int index = 0;

    private boolean hasVirusApp = false;

    private static final int SCAN_FINISHED = 1;
    private static final int SCANING = 2;
    private static final int MSG_UPDATE_INFO = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_virus);
        initData();
        initView();
    }

    private void initView() {
        racarscanview = (RadarScanView) findViewById(R.id.racarscanview);
        scan_state = (TextView) findViewById(R.id.scan_state);
        app_name = (TextView) findViewById(R.id.app_name);
    }

    @Override
    public void onDataFinished(List<VirusShowBean> virusShowBeanList) {
        if (virusShowBeanList == null || virusShowBeanList.isEmpty()) {
            hasVirusApp = false;
        } else {
            hasVirusApp = true;
            mVirusShowBeanList = virusShowBeanList;
        }
    }

    private void getToListActicity() {
        racarscanview.stopScan();
        app_name.setVisibility(View.GONE);
        scan_state.setText(getString(hasVirusApp ? R.string.scan_finished_has_virus:R.string.scan_finished_no_virus));
        scan_state.setTextColor(getResources().getColor(hasVirusApp ? R.color.cn_rank_protect_bg_red : R.color.safepay_app_startup_bg));
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasVirusApp) {

                }
            }
        },2000);
        try {
            InputStream inputStream = getAssets().open("virus.xml");
            PullParseService.getVirusApps(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        apps = SecurityApplication.installApps;
        VirusScanDataProcessor.setScanListener(this);
        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == SCAN_FINISHED) {
                    getToListActicity();
                }
            }
        };

        scanThread = new HandlerThread("Scan_Thread");
        scanThread.start();
        scanHandler = new Handler(scanThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                checkForUpdate();
                if (isScanning)
                {
                    scanHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 70);
                }
            }
        };
    }


    private void checkForUpdate() {
        if (index < apps.size() - 1) {
            try {
                Thread.sleep(70);
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("AntiVirus", index + ":"+apps.get(index));
                        app_name.setText(apps.get(index ++));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            isScanning = true;
        } else {
            isScanning = false;
            mhandler.sendEmptyMessage(SCAN_FINISHED);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //开始查询
        isScanning = true;
        scanHandler.sendEmptyMessage(MSG_UPDATE_INFO);
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            List<ResolveInfo> resolveInfolist = this.getPackageManager().queryIntentActivities(intent, 0);
            InputStream inputStream = getAssets().open("virus.xml");
            List<VirusApp> apps = PullParseService.getVirusApps(inputStream);
            VirusScanDataProcessor.requestVirusAppData(resolveInfolist, apps, this.getPackageManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //停止查询
        isScanning = false;
        scanHandler.removeMessages(MSG_UPDATE_INFO);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        scanThread.quit();

        index = 0;
    }
}
