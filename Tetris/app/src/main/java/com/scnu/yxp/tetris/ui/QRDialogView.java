package com.scnu.yxp.tetris.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.scnu.yxp.tetris.R;
import com.scnu.yxp.tetris.base.BaseDialogView;
import com.scnu.yxp.tetris.logic.Bean;
import com.scnu.yxp.tetris.logic.Constants;
import com.scnu.yxp.tetris.util.QRCoderProductUtil;
import com.scnu.yxp.tetris.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yxp on 2015/10/22.
 */
public class QRDialogView extends BaseDialogView {
    private ImageView img;
    private ServerSocket serverSocket;
    private boolean isAlive = true;

    public QRDialogView(Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.view_qr_dialog);
        img = (ImageView) window.findViewById(R.id.qr_code_img);

        Bitmap bmp = QRCoderProductUtil.create2DCoderBitmap(getIp(), 250, 250);
        Log.i("ip", getIp());
        img.setImageBitmap(bmp);
        if (!isClickable) {
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    private String getIp(){
        WifiManager wm=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        //检查Wifi状态
        if(!wm.isWifiEnabled())
            return "";
        WifiInfo wi=wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd=wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        String ip=intToIp(ipAdd);
        return ip;
    }
    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    public void startServerSocket(final GameView gameView){
        isAlive = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(Constants.SHARE_PORT);
                    while(isAlive) {
                        Log.i("start server", "successful");
                        Socket socket = serverSocket.accept();
                        Bean bean = new Bean();
                        bean.setBlocks(gameView.getBlockArray());
                        bean.setCurComponent(gameView.getCurComponent());
                        bean.setGoal(gameView.getGoal());
                        bean.setLevel(Constants.LEVEL);
                        bean.setNextComponent(gameView.getNextComponent());
                        bean.setIsGameOver(gameView.isGameOver());
                        bean.setIsPause(gameView.isPause());
                        OutputStream os = socket.getOutputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream tmp = new ObjectOutputStream(bos);
                        tmp.writeObject(bean);
                        for (int i = 0; i < 3; i++) {
                            os.write(Util.intToByteArray(bos.toByteArray().length));
                            os.write(bos.toByteArray());
                            os.flush();
                        }
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void closeServerSocket(){
        isAlive = false;
        try {
            serverSocket.close();
            Log.e("server close", "successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBtnClickable(boolean clickable) {
        super.setBtnClickable(clickable);
    }
}
