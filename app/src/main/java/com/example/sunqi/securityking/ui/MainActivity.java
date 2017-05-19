package com.example.sunqi.securityking.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.dataprovider.MainUIDataProcess;
import com.example.sunqi.securityking.global.Constant;
import com.example.sunqi.securityking.permission.PermissionManager;
import com.github.lzyzsd.circleprogress.ArcProgress;

import static com.example.sunqi.securityking.dataprovider.MainUIDataProcess.getInternalMemory;
import static com.example.sunqi.securityking.dataprovider.MainUIDataProcess.getStorge;

public class MainActivity extends Activity implements View.OnClickListener{
    private DrawerLayout drawer_layout;
    private RelativeLayout right_drawer;
    private ImageView title_items;
    private ImageView level_icon_bg;
    private GridView mian_gridView;
    private ArcProgress arc_progress;
    private ArcProgress arc_progress2;
    private RelativeLayout level_layout;
    private TextView defend_level_num;
    private TextView protect_days_num;
    private TextView defend_level_notice;
    private RelativeLayout redpacket_layout;
    private RelativeLayout process_layout;
    private RelativeLayout notify_layout;
    private RelativeLayout virus_layout;
    private RelativeLayout me_layout;
    private GridAdapter madapter;
    private String[] funs = {"进程管理","防通知打扰","红包提醒","病毒检测"};
    private int[] icons = {R.drawable.manager, R.drawable.notify, R.drawable.redpacket, R.drawable.bingdu};
    private Context mcontext;
    private boolean isShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslate();
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShowing) {
            startAnimator();
        }
    }

    /**
     * 主界面的指示图开始变化
     */
    private void startAnimator() {
        ObjectAnimator anim = ObjectAnimator.ofInt(arc_progress, "progress", 0, (int)(100 - getStorge() + 0.5));
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(1500);
        ObjectAnimator anim2 = ObjectAnimator.ofInt(arc_progress2, "progress", 0, (int)(100 - getInternalMemory() + 0.5));
        anim2.setInterpolator(new DecelerateInterpolator());
        anim2.setDuration(1500);
        anim.start();
        anim2.start();
    }

    private void initData() {
        madapter = new GridAdapter(this);
        mcontext = this;
    }

    private void initView() {
        initTitle();
        right_drawer = (RelativeLayout) findViewById(R.id.right_drawer);
        right_drawer.setOnClickListener(this);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_layout.addDrawerListener(listener);
        arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
        arc_progress2 = (ArcProgress) findViewById(R.id.arc_progress2);
        mian_gridView = (GridView) findViewById(R.id.mian_gridView);
        mian_gridView.setAdapter(madapter);
        mian_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(mcontext,"进程加速",Toast.LENGTH_SHORT).show();
                        Intent intent4 = new Intent(mcontext, ProcessActivity.class);
                        mcontext.startActivity(intent4);
                        break;
                    case 1:
                        Toast.makeText(mcontext,"防通知打扰",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mcontext, NotifySettingActivity.class);
                        mcontext.startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(mcontext,"红包提醒",Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(mcontext, RedPacketActivity.class);
                        mcontext.startActivity(intent2);
                        break;
                    case 3:
                        Toast.makeText(mcontext,"病毒检测",Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(mcontext, ScanVirusActicity.class);
                        mcontext.startActivity(intent3);
                        break;
                }
            }
        });
        level_icon_bg = (ImageView) findViewById(R.id.level_icon_bg);
        defend_level_notice = (TextView) findViewById(R.id.defend_level_notice);
        protect_days_num = (TextView) findViewById(R.id.protect_days_num);
        defend_level_num = (TextView) findViewById(R.id.defend_level_num);
        level_layout = (RelativeLayout) findViewById(R.id.level_layout);
        redpacket_layout = (RelativeLayout) findViewById(R.id.redpacket_layout);
        process_layout = (RelativeLayout) findViewById(R.id.process_layout);
        notify_layout = (RelativeLayout) findViewById(R.id.notify_layout);
        virus_layout = (RelativeLayout) findViewById(R.id.virus_layout);
        me_layout = (RelativeLayout) findViewById(R.id.me_layout);
        me_layout.setOnClickListener(this);
        virus_layout.setOnClickListener(this);
        notify_layout.setOnClickListener(this);
        level_layout.setOnClickListener(this);
        redpacket_layout.setOnClickListener(this);
        process_layout.setOnClickListener(this);
        protect_days_num.setText(MainUIDataProcess.getInstallDays() +"");
    }

    private void initTitle() {
        title_items = (ImageView) findViewById(R.id.title_items);
        title_items.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_items:
                drawer_layout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.right_drawer:
                Toast.makeText(MainActivity.this,"点击侧边栏", Toast.LENGTH_LONG).show();
                drawer_layout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.me_layout:
                Intent me_intent = new Intent(MainActivity.this, AboutActivity.class);
                mcontext.startActivity(me_intent);
                drawer_layout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.virus_layout:
                Intent virus_intent = new Intent(MainActivity.this, ScanVirusActicity.class);
                mcontext.startActivity(virus_intent);
                drawer_layout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.notify_layout:
                Intent notify_intent = new Intent(MainActivity.this, NotifySettingActivity.class);
                mcontext.startActivity(notify_intent);
                drawer_layout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.level_layout:
                Intent level_intent = new Intent(MainActivity.this, PermissionLevelActivity.class);
                mcontext.startActivity(level_intent);
                drawer_layout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.redpacket_layout:
                Intent redpacket_intent = new Intent(MainActivity.this, RedPacketActivity.class);
                mcontext.startActivity(redpacket_intent);
                drawer_layout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.process_layout:
                Intent process_intent = new Intent(MainActivity.this, ProcessActivity.class);
                mcontext.startActivity(process_intent);
                drawer_layout.closeDrawer(Gravity.RIGHT);
                break;
        }
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            isShowing = false;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            isShowing = true;
            startAnimator();
            getInternalMemory();
            getStorge();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            if(newState == DrawerLayout.STATE_DRAGGING) {
                refreshDrawer();
            }
        }
    };

    private void refreshDrawer() {
        Constant.Level level = PermissionManager.getLevel();
        protect_days_num.setText(MainUIDataProcess.getInstallDays() +"");
        if (level == Constant.Level.HIGH) {
            levelUP(true);
        } else {
            levelUP(false);
        }
    }

    private void levelUP(boolean high) {
        level_layout.setBackgroundResource(high ? R.color.cn_rank_protect_bg_blue : R.color.cn_rank_protect_bg_red);
        level_icon_bg.setImageResource(high ? R.drawable.high : R.drawable.low);
        defend_level_num.setText(high ? "极高" : "极低");
        defend_level_notice.setText(high ? "你已获得完整的安全防护" : "你有未开权限待开启");
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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private TextView fun_text;
        private ImageView fun_image;


        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return funs.length;
        }

        @Override
        public Object getItem(int position) {
            return funs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fun_grid_item, null);
            }
            fun_text = (TextView) convertView.findViewById(R.id.fun_item_text);
            fun_image = (ImageView) convertView.findViewById(R.id.fun_item_icon);
            if (fun_text != null) {
                fun_text.setText(funs[position]);
            }
            if (fun_image != null) {
                fun_image.setImageResource(icons[position]);
            }
            return convertView;
        }
    }
}
