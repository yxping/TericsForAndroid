package com.scnu.yxp.tetris.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scnu.yxp.tetris.R;
import com.scnu.yxp.tetris.base.BaseDialogView;
import com.scnu.yxp.tetris.base.Block;
import com.scnu.yxp.tetris.base.Component;
import com.scnu.yxp.tetris.factory.ComponentFactory;
import com.scnu.yxp.tetris.helper.ClientSocketHelper;
import com.scnu.yxp.tetris.helper.SaveAndReadHelper;
import com.scnu.yxp.tetris.helper.ServerSocketHelper;
import com.scnu.yxp.tetris.logic.Bean;
import com.scnu.yxp.tetris.logic.Constants;
import com.scnu.yxp.tetris.logic.Rule;
import com.scnu.yxp.tetris.util.Util;

import java.util.Objects;

/**
 * 游戏进行的界面
 * Created by user on 2015/10/20.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{

    private SurfaceHolder mHolder;
    private GameThread mThread;
    private boolean isDestroy = false;
    private Block[][] blockArray;
    private Context context;
    private Component curComponent;
    private Component nextComponent;
    private boolean isGameOver = false;
    private GestureDetector mGesture;
    private boolean isPause = false;

    private int goal = 0;
    private Bitmap bg;
    private Bitmap[] blockImg;
    private int[] resId = new int[]{R.drawable.block0, R.drawable.block1, R.drawable.block2,
            R.drawable.block3, R.drawable.block4, R.drawable.block5, R.drawable.block6, R.drawable.block7};
    private Bitmap pauseBtn;

    // next component展示位置中心
    private int nextComDst = 1;
    private MenuDialogView menuDialogView;
    private ResultDialogView resultDialogView;
    private QRDialogView qrDialogView;

    // 观战模式
    private boolean isWitnessBattle = false;
    private boolean isAllowWitness = false;
    private ServerSocketHelper serverSocketHelper;
    private ClientSocketHelper clientSocketHelper;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x121) {
                resultDialogView.show();
                resultDialogView.setResult("遊戲結束");
            }else if (msg.what == 0x122) {
                resultDialogView.show();
                resultDialogView.setResult("您獲勝了");
            }else if (msg.what == 0x123) {
                menuDialogView.show();
            }else if (msg.what == 0x124) {
                Toast.makeText(context, "玩家已经退出游戏", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public GameView(Context context) {
        this(context, false, false);
    }

    public GameView(Context context, Bean bean) {
        this(context, false, false);
        blockArray = bean.getBlocks();
        curComponent = bean.getCurComponent();
        nextComponent = bean.getNextComponent();
        Constants.LEVEL = bean.getLevel();
        goal = bean.getGoal();
    }
    /**
     *
     * @param context
     * @param hasData 是否有已有的数据
     */
    public GameView(Context context, Boolean hasData, Boolean isWitnessBattle){
        super(context);
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        this.context = context;

        menuDialogView = new MenuDialogView(context);
        resultDialogView = new ResultDialogView(context);
        qrDialogView = new QRDialogView(context);
        setMenuListener();
        setResultDialogListener();
        setQrDialogListener();

        if(isWitnessBattle){
            this.isWitnessBattle = true;
            final EditText textView = new EditText(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200));
            new AlertDialog.Builder(context)
                    .setTitle("目标ip地址")
                    .setView(textView)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!textView.getText().toString().equals(""))
                                startConnectServer(textView.getText().toString());
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
//            startConnectServer("172.18.45.12");
            menuDialogView.setBtnClickable(false);
            resultDialogView.setBtnClickable(false);
        }else{
            mThread = new GameThread(context, mHolder);
            mGesture = new GestureDetector(context, new GestureListener());
            this.setOnTouchListener(this);
            this.setLongClickable(true);
            this.setFocusable(true);
        }

        if (hasData) {
            readDataAndInit();
        } else {
            blockArray = new Block[Constants.HEIGHT_NUM][Constants.WIDTH_NUM];
            curComponent = ComponentFactory.createComponent();
            nextComponent = ComponentFactory.createComponent();
        }

        initResource();
    }

    // 连接服务器
    public void startConnectServer(String ip) {
        clientSocketHelper = new ClientSocketHelper(context);
        clientSocketHelper.setSocketRunnableTask(new ClientSocketHelper.SocketRunnableTask() {
            @Override
            public void runTask() {
                clientSocketHelper.getMsgFromSocket();
            }

            @Override
            public void acceptMsgObject(Object o) {
                if(o instanceof String){
                    String msg = (String)o;
                    if (msg.equals("quit")) {
                        handler.sendEmptyMessage(0x124);
                        finish();
                    }
                }else if (o instanceof Bean) {
                    Bean bean = (Bean)o;
                    Log.e("accept msg from server", bean.getGoal() + "分");
                    curComponent = bean.getCurComponent();
                    nextComponent = bean.getNextComponent();
                    blockArray = bean.getBlocks();
                    Constants.LEVEL = bean.getLevel();
                    goal = bean.getGoal();
                    if (bean.isGameOver()) {
                        if (Constants.LEVEL == Constants.MAX_LEVEL && goal >= Constants.LEVEL_GOAL[Constants.LEVEL]) {
                            if (!resultDialogView.isShowing()) {
                                handler.sendEmptyMessage(0x122);
                            }
                        } else {
                            if (!resultDialogView.isShowing()) {
                                handler.sendEmptyMessage(0x121);
                                return;
                            }
                        }
                        return;
                    } else {
                        if (resultDialogView.isShowing()) {
                            resultDialogView.dismiss();
                        }
                    }
                    if (bean.isPause()) {
                        if (!menuDialogView.isShowing()) {
                            handler.sendEmptyMessage(0x123);
                        }
                        return;
                    } else {
                        if (menuDialogView.isShowing()) {
                            menuDialogView.dismiss();
                        }
                    }
                    draw(false);
                }
            }

        });
        clientSocketHelper.connect(ip, Constants.PORT);
    }

    // 读取数据并进行初始化
    public void readDataAndInit(){
        Bean bean = SaveAndReadHelper.readObjectDataFromFile(context);
        blockArray = bean.getBlocks();
        curComponent = bean.getCurComponent();
        nextComponent = bean.getNextComponent();
        Constants.LEVEL = bean.getLevel();
        goal = bean.getGoal();
    }

    // 资源初始化
    public void initResource(){
        bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.courtbg);
        pauseBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause_btn);
        blockImg = new Bitmap[resId.length];
        for(int i = 0; i < resId.length; i++){
            blockImg[i] = BitmapFactory.decodeResource(context.getResources(), resId[i]);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDestroy = false;
        if(!isWitnessBattle) mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDestroy = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return mGesture.onTouchEvent(event);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 左滑
            if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 20) {
                if(!Rule.checkSideBorder(0, curComponent, blockArray)) {
                    curComponent.moveLeft();
                    draw(false);
                }
            // 右滑
            } else if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 20) {
                if(!Rule.checkSideBorder(1, curComponent, blockArray)) {
                    curComponent.moveRight();
                    draw(false);
                }
            // 上滑
            } else if (e1.getY() - e2.getY() > 50 && Math.abs(velocityY) > 20) {
                curComponent.switchMode(blockArray);
                draw(false);
            // 下滑
            } else if (e2.getY() - e1.getY() > 50 && Math.abs(velocityY) > 20) {
                draw(true);
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(e.getX() > Constants.WIDTH - (int)(pauseBtn.getWidth() / 2) - 10 && e.getX() < Constants.WIDTH - 10
                    && e.getY() > 10 && e.getY() < (int)(pauseBtn.getHeight() / 2) + 10){
                pause();
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    // 暂停
    public void pause(){
        isPause = true;
        menuDialogView.show();
    }

    // 返回
    public void resume(){
        if(isWitnessBattle) return;
        mThread = new GameThread(context, mHolder);
        mThread.start();
        isPause = false;
    }

    // 重新开始
    public void restart(){
        isGameOver = false;
        isPause = false;
        mThread = new GameThread(context, mHolder);
        blockArray = new Block[Constants.HEIGHT_NUM][Constants.WIDTH_NUM];
        curComponent = ComponentFactory.createComponent();
        nextComponent = ComponentFactory.createComponent();
        Constants.LEVEL = 0;
        goal = 0;
    }

    public void setQrDialogListener(){
        qrDialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                qrDialogView.closeServerSocket();
            }
        });
    }

    public void setMenuListener(){
        menuDialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resume();
            }
        });
        menuDialogView.setBackClickMessageListener(new BaseDialogView.BackClickMessageListener() {
            @Override
            public void onClick(String msg) {
                if (msg.equals("share")) {
                    qrDialogView.show();
                    qrDialogView.startServerSocket(GameView.this);
                    return;
                } else if (msg.equals("restart")) {
                    restart();
                } else if (msg.equals("backToMenu")) {
                    finish();
                } else if (msg.equals("witness")) {
                    allowWitness();
                }
                menuDialogView.dismiss();
            }
        });
    }

    public void setResultDialogListener(){
        resultDialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resume();
            }
        });
        resultDialogView.setBackClickMessageListener(new BaseDialogView.BackClickMessageListener() {
            @Override
            public void onClick(String msg) {
                resultDialogView.dismiss();
                if (msg.equals("restart")) {
                    restart();
                } else if (msg.equals("backToMenu")) {
                    finish();
                }
            }
        });
    }

    // 开启实时观战，作为服务器
    public void allowWitness(){
        isAllowWitness = true;
        if(serverSocketHelper == null) serverSocketHelper = new ServerSocketHelper(this, Constants.PORT);
        Toast.makeText(context, "允许其他玩家观战", Toast.LENGTH_SHORT).show();
        serverSocketHelper.listen();
    }

    // 结束游戏
    public void finish(){
        menuDialogView.dismiss();
        resultDialogView.dismiss();
        isGameOver = true;
        isPause = true;
        isDestroy = true;
        if (isAllowWitness) {
            serverSocketHelper.closeAll();
        }
        if(!isWitnessBattle){
            saveData();
        }else{
            clientSocketHelper.close();
        }
        ((Activity) context).finish();
    }

    // 保存游戏
    public void saveData(){
        Bean bean = new Bean();
        bean.setBlocks(blockArray);
        bean.setCurComponent(curComponent);
        bean.setGoal(goal);
        bean.setLevel(Constants.LEVEL);
        bean.setNextComponent(nextComponent);
        SaveAndReadHelper.saveObjectDataToFile(context, bean);
    }

    // 更新组合
    public void switchComponent(){
        curComponent = nextComponent;
        nextComponent = ComponentFactory.createComponent();
    }

    // 结束游戏
    public void gameOver(){
        isGameOver = true;
        // 展示结果
        handler.sendEmptyMessage(0x121);
    }

    // 胜利
    public void win(){
        isGameOver = true;
        handler.sendEmptyMessage(0x122);
    }

    public void draw(boolean isRun){
        long a = System.currentTimeMillis();
        Canvas c = null;
        try{
            c = mHolder.lockCanvas();
            if (isRun) {
                curComponent.moveDown();
            }
            c.drawColor(Color.BLACK);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            // 绘制背景
            c.drawBitmap(bg, null, new Rect(0, 0, Constants.WIDTH, Constants.HEIGHT), paint);

            // 绘制数组中的方块
            for(int i = 0; i < Constants.HEIGHT_NUM; i++){
                for(int j = 0; j < Constants.WIDTH_NUM; j++){
                    if(blockArray[i][j] != null) {
                        c.drawBitmap(blockImg[blockArray[i][j].getMBitmap()], null,
                                new Rect(blockArray[i][j].getStartX(), blockArray[i][j].getStartY(),
                                        blockArray[i][j].getStartX() + Block.LENGTH, blockArray[i][j].getStartY() + Block.LENGTH), paint);
                    }
                }
            }
            // 绘制当前的component
            for(int i = 0; i < curComponent.blocks.length; i++) {
                c.drawBitmap(blockImg[curComponent.blocks[i].getMBitmap()], null,
                        new Rect(curComponent.blocks[i].getStartX(), curComponent.blocks[i].getStartY(),
                                curComponent.blocks[i].getStartX() + Block.LENGTH, curComponent.blocks[i].getStartY() + Block.LENGTH), paint);
            }
            // 绘制下一个component
            if(nextComponent != null){
                int x, y;
                for(int i = 0; i < nextComponent.blocks.length; i++) {
                    x = (nextComponent.blocks[i].getX() - nextComponent.blocks[0].getX() + nextComDst) * Block.LENGTH;
                    y = (nextComponent.blocks[i].getY() - nextComponent.blocks[0].getY() + nextComDst) * Block.LENGTH;
                    c.drawBitmap(blockImg[nextComponent.blocks[i].getMBitmap()], null,
                            new Rect(x + 40, y + 10, x + Block.LENGTH + 40, y + Block.LENGTH + 10), paint);
                }
            }
            // 边界检测
            if(!isWitnessBattle) {
                Rule.checkBottomBorder(curComponent, blockArray, this);
            }
//            checkBottomBorder();
            paint.setTextSize(25 * Constants.TEXT_SIZE_RATE);
            // 绘制分数
            c.drawText("Level  " + (Constants.LEVEL + 1), Constants.WIDTH / 2 - 30, 25 * Constants.TEXT_SIZE_RATE, paint);
            c.drawText("得分： " + goal + " 分", Constants.WIDTH / 2 - 50, 55 * Constants.TEXT_SIZE_RATE, paint);
            c.drawText("next", 5, 30 * Constants.TEXT_SIZE_RATE, paint);
            // 绘制暂停按钮
            c.drawBitmap(pauseBtn, null, new Rect(Constants.WIDTH - (int)(pauseBtn.getWidth() / 2) - 10, 10,
                    Constants.WIDTH - 10, (int)(pauseBtn.getHeight() / 2) + 10), paint);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(c != null && mHolder != null){
                mHolder.unlockCanvasAndPost(c);
            }
        }
        long b = System.currentTimeMillis();
//        Log.e("time use", ":"+(b - a));
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public Block[][] getBlockArray() {
        return blockArray;
    }

    public Component getCurComponent() {
        return curComponent;
    }

    public Component getNextComponent() {
        return nextComponent;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isPause() {
        return isPause;
    }

    // 游戏循环线程
    class GameThread extends Thread{
        private SurfaceHolder holder;

        public GameThread(Context context, SurfaceHolder holder){
            this.holder = holder;

        }

        @Override
        public void run() {
            int count = 0;
            while(!isDestroy && !isPause && !isGameOver) {
                draw(true);
                try {
                    Thread.sleep(Constants.SPEED[Constants.LEVEL]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
