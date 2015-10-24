package com.scnu.yxp.tetris.ui;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.scnu.yxp.tetris.R;
import com.scnu.yxp.tetris.base.BaseDialogView;

/**
 * Created by yxp on 2015/10/21.
 */
public class ResultDialogView extends BaseDialogView{
    private Button restartBtn, backToMenuBtn;
    private TextView resultText;
    public ResultDialogView(Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.view_result_dialog);
        restartBtn = (Button) window.findViewById(R.id.result_restart_btn);
        backToMenuBtn = (Button) window.findViewById(R.id.result_back_to_menu_btn);
        resultText = (TextView) window.findViewById(R.id.result_text);
        // 设置点击外面不消失
        dialog.setCanceledOnTouchOutside(false);
        setListener();
        if (!isClickable) {
            restartBtn.setClickable(isClickable);
            backToMenuBtn.setClickable(isClickable);
        }
    }

    public void setListener(){
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) listener.onClick("restart");
            }
        });
        backToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)  listener.onClick("backToMenu");
            }
        });
    }

    public void setResult(String result){
        resultText.setText(result);
    }

}
