package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.global.GlobalPref;

/**
 * Created by sshunsun on 2017/5/14.
 */
public class RedPacketActivity extends Activity {
    private GlobalPref globalPref = GlobalPref.getInstance();
    private Context mcontext = SecurityApplication.getInstance().getApplicationContext();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_redpacket);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
