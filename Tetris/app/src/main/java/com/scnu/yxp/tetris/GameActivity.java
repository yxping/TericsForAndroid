package com.scnu.yxp.tetris;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import com.scnu.yxp.tetris.base.BaseActivity;
import com.scnu.yxp.tetris.logic.Bean;
import com.scnu.yxp.tetris.logic.Constants;
import com.scnu.yxp.tetris.base.Block;
import com.scnu.yxp.tetris.ui.GameView;

/**
 * 游戏界面
 * Created by yxp on 2015/10/20.
 */
public class GameActivity extends BaseActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        String bean = getIntent().getStringExtra("bean");
        if (bean != null && bean.equals("true")) {
            gameView = new GameView(this, (Bean) getIntent().getSerializableExtra("object"));
        } else {
            gameView = new GameView(this, Boolean.valueOf(getIntent().getStringExtra("hasData")),
                    Boolean.valueOf(getIntent().getStringExtra("isWitness")));
        }
        setContentView(gameView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameView.finish();
    }

    // 初始化对应的常量信息
    public void init(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.WIDTH = dm.widthPixels;
        Constants.HEIGHT = dm.heightPixels;
        Constants.DENSITY = dm.density;
        Constants.TEXT_SIZE_RATE = Constants.DENSITY / 2 + 0.3F;
        Block.LENGTH = Constants.WIDTH / Constants.WIDTH_NUM;
        Constants.shiftX = ( Constants.WIDTH - Block.LENGTH * Constants.WIDTH_NUM ) / 2;
        Constants.shiftY = Constants.HEIGHT - Block.LENGTH * (Constants.HEIGHT_NUM + 1);
        if (Constants.shiftY < 0) {
            Constants.shiftY = 0;
        }
    }
}
