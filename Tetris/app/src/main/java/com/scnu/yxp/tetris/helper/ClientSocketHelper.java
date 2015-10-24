package com.scnu.yxp.tetris.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.scnu.yxp.tetris.util.Util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by yxp on 2015/10/22.
 */
public class ClientSocketHelper {
    private Socket socket;
    private Thread acceptMsgThread;
    private SocketRunnableTask socketRunnableTask;
    private boolean isAlive = true;
    private Context context;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public ClientSocketHelper(Context context) {
        this.context = context;
    }

    public void connect(final String ip, final int port) {
        acceptMsgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress(ip, port), 5000);
                    socketRunnableTask.runTask();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        acceptMsgThread.start();
    }

    // 从socket中获取对象
    public void getMsgFromSocket(){
        InputStream is = null;
        int length = Integer.MAX_VALUE;
        boolean isData = false;
        while (isAlive) {
            try {
                is = socket.getInputStream();
                if(is.available() > 4 && !isData){
                    byte[] tmp = new byte[4];
                    is.read(tmp, 0, 4);
                    length = Util.byteArrayToInt(tmp);
                    isData = true;
                }

                if(is.available() >= length && isData){
                    byte[] tmp = new byte[length];
                    is.read(tmp, 0, length);
                    ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    Object o = ois.readObject();
                    socketRunnableTask.acceptMsgObject(o);
                    isData = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        isAlive = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocketRunnableTask(SocketRunnableTask socketRunnableTask){
        this.socketRunnableTask = socketRunnableTask;
    }

    public interface SocketRunnableTask {
        void runTask();
        void acceptMsgObject(Object o);
    }

}
