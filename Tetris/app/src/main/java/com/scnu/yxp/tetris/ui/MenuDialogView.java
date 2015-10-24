package com.scnu.yxp.tetris.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.scnu.yxp.tetris.R;
import com.scnu.yxp.tetris.base.BaseDialogView;

/**
 * Created by yxp on 2015/10/21.
 */
public class MenuDialogView extends BaseDialogView {
    private Button resumeBtn, backToMenuBtn, restartBtn, shareBtn, witnessBtn;
    public MenuDialogView(Context context) {
        super(context);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener){
        if(dialog != null){
            dialog.setOnDismissListener(listener);
        }
    }

    public void show(){
        super.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.view_dialog);
        resumeBtn = (Button) window.findViewById(R.id.resume_btn);
        backToMenuBtn = (Button) window.findViewById(R.id.back_to_menu_btn);
        restartBtn = (Button) window.findViewById(R.id.restart_btn);
        shareBtn = (Button) window.findViewById(R.id.share_btn);
        witnessBtn = (Button) window.findViewById(R.id.start_witness_btn);
        setListener();
        if(!isClickable){
            resumeBtn.setClickable(isClickable);
            backToMenuBtn.setClickable(isClickable);
            shareBtn.setClickable(isClickable);
            restartBtn.setClickable(isClickable);
            witnessBtn.setClickable(isClickable);
            dialog.setCanceledOnTouchOutside(isClickable);
        }
    }

    private void setListener(){
        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        backToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) listener.onClick("backToMenu");
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) listener.onClick("share");
            }
        });
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClick("restart");
            }
        });
        witnessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClick("witness");
            }
        });
    }

}
