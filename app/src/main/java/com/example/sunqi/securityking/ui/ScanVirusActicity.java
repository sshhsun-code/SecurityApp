package com.example.sunqi.securityking.ui;

import android.app.Activity;
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
import com.example.sunqi.securityking.customview.RadarScanView;

import java.util.ArrayList;

/**
 * Created by sunqi on 2017/5/16.
 */

public class ScanVirusActicity extends Activity {

    private ArrayList<String> apps = new ArrayList<>();

    private RadarScanView racarscanview;
    private TextView scan_state;
    private TextView app_name;
    private Button start_scan;

    private Handler mhandler;
    private HandlerThread scanThread;
    private Handler scanHandler;

    private boolean isScanning = true;

    private static int index = 0;

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


    private void getToListActicity() {
        racarscanview.stopScan();
        app_name.setVisibility(View.GONE);
        scan_state.setText(getString(R.string.scan_finished));
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //到扫描结果页
            }
        },2000);
    }

    private void initData() {
        apps = SecurityApplication.installApps;
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
