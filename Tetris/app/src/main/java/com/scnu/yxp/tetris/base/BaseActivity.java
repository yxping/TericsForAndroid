package com.scnu.yxp.tetris.base;

import android.app.Activity;
import android.os.Bundle;

import com.scnu.yxp.tetris.base.ActivityManager;

/**
 * Created by yxp on 2015/10/20.
 */
public class BaseActivity extends Activity {
    protected ActivityManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        am = ActivityManager.getInstance();
        am.addActivity(this);
    }

}
