package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

    private static int i = 0;

    private static final int SCAN_FINISHED = 1;
    private static final int SCANING = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_virus);
        initData();
        initView();
    }

    private void initView() {
        racarscanview = (RadarScanView) findViewById(R.id.racarscanview);
        racarscanview.stopScan();
        scan_state = (TextView) findViewById(R.id.scan_state);
        app_name = (TextView) findViewById(R.id.app_name);
        start_scan = (Button) findViewById(R.id.start_scan);
        start_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
    }

    private void startScan() {
        racarscanview.startScan();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Message message = mhandler.obtainMessage(SCANING);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        if (!apps.isEmpty()) {
//            for (String app: apps) {
//                app_name.setText(app);
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        mhandler.sendEmptyMessage(SCAN_FINISHED);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!apps.isEmpty()) {
//            for (String app: apps) {
//                app_name.setText(app);
//            }
//        }
//        mhandler.sendEmptyMessage(SCAN_FINISHED);
    }
}
