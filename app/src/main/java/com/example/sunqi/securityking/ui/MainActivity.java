package com.example.sunqi.securityking.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunqi.securityking.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

public class MainActivity extends Activity {
    private GridView mian_gridView;
    private ArcProgress arc_progress;
    private ArcProgress arc_progress2;
    private GridAdapter madapter;
    private String[] funs = {"进程管理","防通知打扰","红包提醒","病毒检测"};
    private int[] icons = {R.drawable.manager, R.drawable.notify, R.drawable.redpacket, R.drawable.bingdu};
    private Context mcontext;


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
        startAnimator(56, 77);
    }

    /**
     * 主界面的指示图开始变化
     * @param progress_1 存储空间百分比
     * @param progress_2 内存百分比
     */
    private void startAnimator(int progress_1, int progress_2) {
        ObjectAnimator anim = ObjectAnimator.ofInt(arc_progress, "progress", 0, progress_1);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(1500);
        ObjectAnimator anim2 = ObjectAnimator.ofInt(arc_progress2, "progress", 0, progress_2);
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
        arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
        arc_progress2 = (ArcProgress) findViewById(R.id.arc_progress2);
        mian_gridView = (GridView) findViewById(R.id.mian_gridView);
        mian_gridView.setAdapter(madapter);
        mian_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
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
                }
            }
        });
    }

    private void initTitle() {

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
