package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.bean.VirusApp;
import com.example.sunqi.securityking.bean.VirusShowBean;
import com.example.sunqi.securityking.customview.RadarScanView;
import com.example.sunqi.securityking.dataprovider.VirusScanDataProcessor;
import com.example.sunqi.securityking.xmlparser.PullParseService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunqi on 2017/5/16.
 */

public class ScanVirusActicity extends Activity implements VirusScanDataProcessor.ScanListener, View.OnClickListener{

    private ArrayList<String> apps = new ArrayList<>();
    private List<VirusShowBean> mVirusShowBeanList = new ArrayList<>();

    private RadarScanView racarscanview;
    private TextView scan_state;
    private TextView app_name;
    private TextView normal_title_text;
    private View scan_title;
    private RelativeLayout scan_view;
    private RelativeLayout result_view;
    private RelativeLayout normal_title_layout;
    private ImageView normal_title_back;
    private ImageView title_items;
    private ListView result_listview;

    private Handler mhandler;
    private HandlerThread scanThread;
    private Handler scanHandler;
    private VirusAdapter adapter;

    private boolean isScanning = true;

    private static int index = 0;

    private boolean hasVirusApp = false;

    private static final int SCAN_FINISHED = 1;
    private static final int SCANING = 2;
    private static final int MSG_UPDATE_INFO = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.activity_scan_virus);
        initData();
        initView();
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

    private void initView() {
        scan_title = findViewById(R.id.scan_title);
        normal_title_text = (TextView) scan_title.findViewById(R.id.normal_title_text);
        normal_title_text.setText("病毒扫描");
        scan_view = (RelativeLayout) findViewById(R.id.scan_view);
        scan_view.setVisibility(View.VISIBLE);
        result_view = (RelativeLayout) findViewById(R.id.result_view);
        result_view.setVisibility(View.GONE);
        scan_title.setBackgroundResource(R.color.cn_rank_protect_bg_blue);
        normal_title_back = (ImageView) scan_title.findViewById(R.id.normal_title_back);
        normal_title_back.setOnClickListener(this);
        title_items = (ImageView) scan_title.findViewById(R.id.title_items);
        title_items.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_title_back:
                if (scan_view.getVisibility() == View.GONE) {
                    finish();
                }
                break;
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
                    scan_view.setVisibility(View.GONE);
                    result_view.setVisibility(View.VISIBLE);
                    findViewById(R.id.total_layout).setBackgroundColor(Color.WHITE);
                    scan_title.setBackgroundResource(R.color.cn_rank_protect_bg_red);
                    result_listview = (ListView) findViewById(R.id.result_listview);
                    adapter = new VirusAdapter(mVirusShowBeanList);
                    result_listview.setAdapter(adapter);
                    result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String pkgName = mVirusShowBeanList.get(position).getPakName();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:" + pkgName));
                            startActivity(intent);
                        }
                    });
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
    public void onBackPressed() {
        super.onBackPressed();
        if (scan_view.getVisibility() == View.GONE) {
            finish();
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

    static class VirusAdapter extends BaseAdapter {
        private List<VirusShowBean> virusApps;

        public VirusAdapter(List<VirusShowBean> virusApps) {
            this.virusApps = virusApps;
        }

        @Override
        public int getCount() {
            return virusApps.size();
        }

        @Override
        public Object getItem(int position) {
            return virusApps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VirusShowBean virusShowBean = virusApps.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(SecurityApplication.getInstance()).inflate(R.layout.virus_app_item, null);
            }

            ((ImageView)convertView.findViewById(R.id.virus_item_icon)).setImageDrawable(virusShowBean.getAppIcon());
            ((TextView)convertView.findViewById(R.id.virus_item_name)).setText(virusShowBean.getAppName());
            ((TextView)convertView.findViewById(R.id.virus_item_type)).setText(getVirusType(virusShowBean.getType()));
            return convertView;
        }

        private String getVirusType(int type) {
            String typeString = "";
            switch (type) {
                case 1:
                    typeString = "含有高危病毒";
                    break;
                case 2:
                    typeString = "该应用频繁后台操作";
                    break;
            }
            return typeString;
        }
    }
}
