package com.scnu.yxp.tetris;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.scnu.yxp.tetris.base.BaseActivity;
import com.scnu.yxp.tetris.helper.ClientSocketHelper;
import com.scnu.yxp.tetris.logic.Bean;
import com.scnu.yxp.tetris.logic.Constants;
import com.zxing.activity.CaptureActivity;


/**
 * Created by yxp on 2015/10/20.
 * 游戏主界面展示
 */
public class HomeActivity extends BaseActivity {
    private Button newBtn, goonBtn, qrBtn, lookBtn, quitBtn;
    private ClientSocketHelper clientSocketHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        newBtn = (Button) findViewById(R.id.new_btn);
        goonBtn = (Button) findViewById(R.id.goon_btn);
        qrBtn = (Button) findViewById(R.id.qr_btn);
        lookBtn = (Button) findViewById(R.id.look_btn);
        quitBtn = (Button) findViewById(R.id.quit_btn);
        setListener();
    }

    public void setListener(){
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hasdata", "false");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        goonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hasData", "true");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isWitness", "true");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(HomeActivity.this,
                        CaptureActivity.class);
                startActivityForResult(openCameraIntent, 200);
            }
        });
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            goonGamePlay(result);
        }
    }

    public void goonGamePlay(String ip) {
        clientSocketHelper = new ClientSocketHelper(this);
        clientSocketHelper.setSocketRunnableTask(new ClientSocketHelper.SocketRunnableTask() {
            @Override
            public void runTask() {
                clientSocketHelper.getMsgFromSocket();
            }

            @Override
            public void acceptMsgObject(Object o) {
                if (o instanceof Bean) {
                    Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("bean", "true");
                    bundle.putSerializable("object", (Bean) o);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    clientSocketHelper.close();
                }
            }

        });
        clientSocketHelper.connect(ip, Constants.SHARE_PORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(clientSocketHelper != null) clientSocketHelper.close();
    }

}
