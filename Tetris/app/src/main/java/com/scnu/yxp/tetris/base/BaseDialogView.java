package com.scnu.yxp.tetris.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by yxp on 2015/10/21.
 */
public class BaseDialogView {
    protected AlertDialog dialog;
    protected BackClickMessageListener listener;
    protected Context context;
    protected boolean isClickable = true;

    public BaseDialogView(Context context){
        dialog = new AlertDialog.Builder(context).create();
        this.context = context;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener){
        if(dialog != null){
            dialog.setOnDismissListener(listener);
        }
    }

    public void setBtnClickable(boolean clickable) {
        isClickable = clickable;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public void setBackClickMessageListener(BackClickMessageListener listener){
        this.listener = listener;
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public interface BackClickMessageListener {
        // 返回点击的事件名称
        void onClick(String msg);
    }

}
