package com.scnu.yxp.tetris.base;

import com.scnu.yxp.tetris.logic.Constants;

import java.io.Serializable;

/**
 * Created by yxp on 2015/10/20.
 */
public abstract class Component implements BaseActionInterface, Serializable{

    private static final long serialVersionUID = -69209086357997950L;
    public Block[] blocks;
    public int curMode = 0;

    public Component(int bitmapIndex, int mode){
        blocks = new Block[4];
    }

    // 变换形式
    public void switchMode(){
        switch (curMode) {
            case 0:
                for (int i = 1; i < 4; i++) {
                    int tmpX = blocks[i].getX();
                    int tmpY = blocks[i].getY();
                    blocks[i].setY(blocks[0].getY() + blocks[0].getX() - tmpX);
                    blocks[i].setX(blocks[0].getX() - blocks[0].getY() + tmpY);
                }
                curMode = 1;
                break;
            case 1:
                for (int i = 1; i < 4; i++) {
                    int tmpX = blocks[i].getX();
                    int tmpY = blocks[i].getY();
                    blocks[i].setX(blocks[0].getX() - blocks[0].getY() + tmpY);
                    blocks[i].setY(blocks[0].getY() + blocks[0].getX() - tmpX);
                }
                curMode = 2;
                break;
            case 2:
                for (int i = 1; i < 4; i++) {
                    int tmpX = blocks[i].getX();
                    int tmpY = blocks[i].getY();
                    blocks[i].setY(blocks[0].getY() + blocks[0].getX() - tmpX);
                    blocks[i].setX(blocks[0].getX() - blocks[0].getY() + tmpY);
                }
                curMode = 3;
                break;
            case 3:
                for (int i = 1; i < 4; i++) {
                    int tmpX = blocks[i].getX();
                    int tmpY = blocks[i].getY();
                    blocks[i].setX(blocks[0].getX() - blocks[0].getY() + tmpY);
                    blocks[i].setY(blocks[0].getY() + blocks[0].getX() - tmpX);
                }
                curMode = 0;
                break;
            default:
                break;
        }
    }

    // 变换前先进行检测
    public void switchMode(Block[][] arrays){
        // 检测边界, 不可变换则不变换
        if(blocks[0].getX() + 1 >= Constants.WIDTH_NUM || blocks[0].getX() - 1 < 0) return;
        if(blocks[0].getY() + 1 >= Constants.HEIGHT_NUM) return;
        int tmp = curMode;
        switchMode();
        for(int i = 0; i < blocks.length; i++) {
            if (blocks[i].getX() >= 0 && blocks[i].getY() >= 0 && blocks[i].getX() < Constants.WIDTH_NUM
                    && blocks[i].getY() < Constants.HEIGHT_NUM && arrays[blocks[i].getY()][blocks[i].getX()] != null) {
                while (curMode != tmp) {
                    switchMode();
                }
            }
        }

    }

    public void moveLeft(){
        for(int i = 0; i < blocks.length; i++){
            blocks[i].moveLeft();
        }
    }

    public void moveRight(){
        for(int i = 0; i < blocks.length; i++){
            blocks[i].moveRight();
        }
    }

    public void moveDown(){
        for(int i = 0; i < blocks.length; i++){
            blocks[i].moveDown();
        }
    }
}
