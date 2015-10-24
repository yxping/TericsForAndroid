package com.scnu.yxp.tetris;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.scnu.yxp.tetris.base.BaseActivity;

/**
 * Created by yxp on 2015/10/21.
 */
public class GuideActivity extends BaseActivity {
    static boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isDestroy) {
                    Intent intent = new Intent(GuideActivity.this, HomeActivity.class);
                    startActivity(intent);
                    GuideActivity.this.finish();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDestroy = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
