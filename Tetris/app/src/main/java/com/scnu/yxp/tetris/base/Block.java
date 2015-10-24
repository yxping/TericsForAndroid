package com.scnu.yxp.tetris.base;

import android.graphics.Bitmap;

import com.scnu.yxp.tetris.base.BaseActionInterface;
import com.scnu.yxp.tetris.logic.Constants;

import java.io.Serializable;

/**
 * Created by yxp on 2015/10/20.
 */
public class Block implements BaseActionInterface,Serializable{
    private static final long serialVersionUID = 6781837224810271562L;
    // 方块图片序号
    private int mBitmapIndex;
    // 坐标信息
    private int x;
    private int y;
    // 方格的大小
    public static int LENGTH;

    public Block(int mBitmapIndex, int x, int y){
        this.mBitmapIndex = mBitmapIndex;
        this.x = x;
        this.y = y;
    }

    public int getMBitmap() {
        return mBitmapIndex;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStartX(){
        return x * LENGTH + Constants.shiftX;
    }

    public int getStartY(){
        return y * LENGTH + Constants.shiftY;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveDown(){
        y++;
    }

    public void moveRight(){
        x++;
    }

    public void moveLeft(){
        x--;
    }

    public void moveDownOneStep(){
        y++;
    }
}
