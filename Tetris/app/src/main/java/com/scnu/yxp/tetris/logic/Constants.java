package com.scnu.yxp.tetris.logic;

/**
 * Created by yxp on 2015/10/20.
 */
public class Constants {

    // 多少毫秒下落一次
    public static int[] SPEED = {400, 250, 100};
    public static int LEVEL = 0;
    public static int MAX_LEVEL = 2;
    public static int[] LEVEL_GOAL = {5, 8, 12};

    // 界面的长宽
    public static int WIDTH_NUM = 16;
    public static int HEIGHT_NUM = 22;

    // 屏幕长宽
    public static int WIDTH;
    public static int HEIGHT;

    // 屏幕分辨率
    public static float DENSITY;
    public static float TEXT_SIZE_RATE;

    // 偏移量
    public static int shiftX;
    public static int shiftY;

    // socket端口
    public static int PORT = 23333;
    public static int SHARE_PORT = 55555;
}
