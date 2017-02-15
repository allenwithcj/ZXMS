package com.zxms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zxms.utils.ActivityControl;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActivityControl.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityControl.removeActivity(this);
    }
}
