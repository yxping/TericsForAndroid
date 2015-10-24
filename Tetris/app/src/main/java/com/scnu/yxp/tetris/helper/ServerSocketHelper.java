package com.scnu.yxp.tetris.helper;

import android.util.Log;

import com.scnu.yxp.tetris.logic.Bean;
import com.scnu.yxp.tetris.logic.Constants;
import com.scnu.yxp.tetris.ui.GameView;
import com.scnu.yxp.tetris.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by yxp on 2015/10/22.
 */
public class ServerSocketHelper {
    private ServerSocket serverSocket;
    private ArrayList<Socket> socketList = new ArrayList<Socket>();
    private Thread listenThread, syncThread;
    private boolean isAlive = true;
    private GameView gameView;
    private int MAX_CONNECTED = 5;
    private int curConnectedNum = 0;

    public ServerSocketHelper(GameView gameView, int port) {
        this.gameView = gameView;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void listen(){
        if(serverSocket == null) return;
        listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isAlive && curConnectedNum < MAX_CONNECTED){
                    Socket tmp = null;
                    try {
                        Log.i("server start", "true");
                        tmp = serverSocket.accept();
                        curConnectedNum++;
                        Log.i("client", tmp.getRemoteSocketAddress()+"is connected");
                        socketList.add(tmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        listenThread.start();
        syncThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isAlive){
                    sendSyncGame();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        syncThread.start();
    }

    private void sendSyncGame() {
        for (Socket socket : socketList) {
            Bean bean = new Bean();
            bean.setBlocks(gameView.getBlockArray());
            bean.setCurComponent(gameView.getCurComponent());
            bean.setGoal(gameView.getGoal());
            bean.setLevel(Constants.LEVEL);
            bean.setNextComponent(gameView.getNextComponent());
            bean.setIsGameOver(gameView.isGameOver());
            bean.setIsPause(gameView.isPause());
            OutputStream os = null;
            ByteArrayOutputStream bos = null;
            try {
                os = socket.getOutputStream();
                bos = new ByteArrayOutputStream();
                ObjectOutputStream tmp = new ObjectOutputStream(bos);
                tmp.writeObject(bean);
                os.write(Util.intToByteArray(bos.toByteArray().length));
                os.write(bos.toByteArray());
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeAll() {
        isAlive = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Socket socket : socketList) {
                    OutputStream os = null;
                    try {
                        os = socket.getOutputStream();
                        String string = new String("quit");
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream tmp = new ObjectOutputStream(bos);
                        tmp.writeObject(string);
                        for (int i = 0; i < 2; i++) {
                            os.write(Util.intToByteArray(bos.toByteArray().length));
                            os.write(bos.toByteArray());
                            os.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    serverSocket.close();
                    Log.i("server", "closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
