package com.example.sunqi.securityking.ui;

import android.app.Activity;
import android.os.Bundle;

import com.example.sunqi.securityking.R;
import com.example.sunqi.securityking.SecurityApplication;
import com.example.sunqi.securityking.dataprovider.ProcessDataProvider;

/**
 * 进程加速Activity
 * Created by sunqi on 2017/5/17.
 */

public class ProcessActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        ProcessDataProvider.requestBackUpProcessData(SecurityApplication.getInstance());
    }
}
