package com.scnu.yxp.tetris.logic;

import com.scnu.yxp.tetris.base.Block;
import com.scnu.yxp.tetris.base.Component;
import com.scnu.yxp.tetris.ui.GameView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by yxp on 2015/10/20.
 * 游戏检测逻辑，包括左右移动位置检测，底边碰撞添加检测，数组溢出检测，
 */
public class Rule {

    // 检查底边碰撞
    public static void checkBottomBorder(Component curComponent, Block[][] blockArray, GameView gameView) {
        // 检查是否在数组底边
        for (int i = 0; i < curComponent.blocks.length; i++) {
            if (curComponent.blocks[i].getY() >= Constants.HEIGHT_NUM - 1) {
                // 添加进数组中
                addToBlocks(curComponent, blockArray, gameView);
                return;
            }
        }
        // 检查数组中是否有东西阻挡
        for (int i = 0; i < curComponent.blocks.length; i++) {
            Block b = curComponent.blocks[i];
            if (b.getY() + 1 < Constants.HEIGHT_NUM && b.getX() >= 0 && b.getY() + 1 > -1 && b.getX() < Constants.WIDTH_NUM
                    && blockArray[b.getY() + 1][b.getX()] != null) {
                // 添加进数组中
                addToBlocks(curComponent, blockArray, gameView);
                return;
            }
        }
    }

    // 无阻碍 -1， 左边阻碍 0， 右边阻碍 1
    public static boolean checkSideBorder(int target, Component curComponent, Block[][] blockArray) {
        // 检查是否在数组边缘
        for (int i = 0; i < curComponent.blocks.length; i++) {
            if(target == 0 && curComponent.blocks[i].getX() == 0){
                return true;
            }else if( target == 1 && curComponent.blocks[i].getX() == Constants.WIDTH_NUM - 1){
                return true;
            }
        }
        // 检查数组中是否有东西阻挡在左右
        for (int i = 0; i < curComponent.blocks.length; i++) {
            Block b = curComponent.blocks[i];
            if (target == 0 && b.getX() + 1 > -1 && b.getY() > -1 && b.getX() - 1 > -1
                    && blockArray[b.getY()][b.getX() - 1] != null) {
                return true;
            } else if (target == 1 && b.getX() - 1 < Constants.WIDTH_NUM && b.getY() > -1 && b.getX() + 1 > -1
                    && blockArray[b.getY()][b.getX() + 1] != null) {
                return true;
            }
        }
        return false;
    }

    // 添加进数组中
    public static void addToBlocks(Component curComponent, Block[][] blockArray, GameView gameView){
        for (int i = 0; i < curComponent.blocks.length; i++){
            Block b = curComponent.blocks[i];
            if(b.getX() < Constants.WIDTH_NUM && b.getX() >= 0 && b.getY() >= 0 && b.getY() < Constants.HEIGHT_NUM)
                blockArray[b.getY()][b.getX()] = curComponent.blocks[i];
        }
        // 消除行
        deleteLines(curComponent, blockArray, gameView);
        // 检测数组是否满了，满了则结束游戏
        checkFull(curComponent, gameView);
        gameView.switchComponent();
    }

    // 删除多行
    public static void deleteLines(Component curComponent, Block[][] blockArray, GameView gameView){
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < curComponent.blocks.length; i++){
            Block b = curComponent.blocks[i];
            map.put(b.getY(), b.getY());
            if(b.getX() < Constants.WIDTH_NUM && b.getX() >= 0 && b.getY() >= 0 && b.getY() < Constants.HEIGHT_NUM)
                blockArray[b.getY()][b.getX()] = curComponent.blocks[i];
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(Integer i : map.keySet()) {
            list.add(i);
        }
        Collections.sort(list);
        for (Integer i : list) {
            if(i < 0 || i >= Constants.HEIGHT_NUM) continue;
            boolean isFull = true;
            for (int j = 0; j < Constants.WIDTH_NUM; j++) {
                if(blockArray[i][j] == null){
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                deleteLine(i, blockArray, gameView);
            }
        }
    }

    // 删除一行
    public static void deleteLine(int line, Block[][] blockArray, GameView gameView){
        if(line < 0 || line >= Constants.HEIGHT_NUM) return;
        for (int i = line; i >= 0; i--) {
            for (int j = 0; j < Constants.WIDTH_NUM; j++) {
                if (i == 0){
                    blockArray[i][j] = null;
                }else{
                    blockArray[i][j] = blockArray[i - 1][j];
                    if(blockArray[i][j] != null) {
                        blockArray[i][j].moveDownOneStep();
                    }
                }
            }
        }
        gameView.setGoal(gameView.getGoal()+1);
        // 是否满足升级或获胜的条件
        if(gameView.getGoal() >= Constants.LEVEL_GOAL[Constants.LEVEL]){
            Constants.LEVEL++;
            if(Constants.LEVEL > Constants.MAX_LEVEL){
                gameView.win();
                Constants.LEVEL = 0;
            }
        }
    }

    // 判断数组是否满了
    public static void checkFull(Component curComponent, GameView gameView) {
        for (int i = 0; i < curComponent.blocks.length; i++) {
            Block b = curComponent.blocks[i];
            if(b.getY() <= 0){
                gameView.gameOver();
                break;
            }
        }
    }

}





// 检查底边碰撞
//    public void checkBottomBorder(){
//        // 检查是否在数组底边
//        for (int i = 0; i < curComponent.blocks.length; i++){
//            if(curComponent.blocks[i].getY() >= Constants.HEIGHT_NUM - 1){
//                // 添加进数组中
//                addToBlocks();
//                return;
//            }
//        }
//        // 检查数组中是否有东西阻挡
//        for (int i = 0; i < curComponent.blocks.length; i++){
//            Block b = curComponent.blocks[i];
//            if(b.getY() + 1 < Constants.HEIGHT_NUM && b.getX() >= 0 && b.getY() + 1 > -1 && b.getX() < Constants.WIDTH_NUM
//                    && blockArray[b.getY() + 1][b.getX()] != null){
//                // 添加进数组中
//                addToBlocks();
//                return;
//            }
//        }
//    }

// 无阻碍 -1， 左边阻碍 0， 右边阻碍 1
//    public boolean checkSideBorder(int target) {
//        // 检查是否在数组边缘
//        for (int i = 0; i < curComponent.blocks.length; i++) {
//            if(target == 0 && curComponent.blocks[i].getX() == 0){
//                return true;
//            }else if( target == 1 && curComponent.blocks[i].getX() == Constants.WIDTH_NUM - 1){
//                return true;
//            }
//        }
//        // 检查数组中是否有东西阻挡在左右
//        for (int i = 0; i < curComponent.blocks.length; i++) {
//            Block b = curComponent.blocks[i];
//            if (target == 0 && b.getX() + 1 > -1 && b.getY() > -1 && b.getX() - 1 > -1
//                    && blockArray[b.getY()][b.getX() - 1] != null) {
//                return true;
//            } else if (target == 1 && b.getX() - 1 < Constants.WIDTH_NUM && b.getY() > -1 && b.getX() + 1 > -1
//                    && blockArray[b.getY()][b.getX() + 1] != null) {
//                return true;
//            }
//        }
//        return false;
//    }

// 添加进数组中
//    public void addToBlocks(){
//        for (int i = 0; i < curComponent.blocks.length; i++){
//            Block b = curComponent.blocks[i];
//            if(b.getX() < Constants.WIDTH_NUM && b.getX() >= 0 && b.getY() >= 0 && b.getY() < Constants.HEIGHT_NUM)
//                blockArray[b.getY()][b.getX()] = curComponent.blocks[i];
//        }
//        // 消除行
//        deleteLines();
//        // 检测数组是否满了，满了则结束游戏
//        checkFull();
//        curComponent = nextComponent;
//        nextComponent = ComponentFactory.createComponent();
//    }

// 删除多行
//    public void deleteLines(){
//        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
//        for (int i = 0; i < curComponent.blocks.length; i++){
//            Block b = curComponent.blocks[i];
//            map.put(b.getY(), b.getY());
//            if(b.getX() < Constants.WIDTH_NUM && b.getX() >= 0 && b.getY() >= 0 && b.getY() < Constants.HEIGHT_NUM)
//                blockArray[b.getY()][b.getX()] = curComponent.blocks[i];
//        }
//        for(Integer i : map.keySet()) {
//            if(i < 0 || i >= Constants.HEIGHT_NUM) continue;
//            boolean isFull = true;
//            for (int j = 0; j < Constants.WIDTH_NUM; j++) {
//                if(blockArray[i][j] == null){
//                    isFull = false;
//                    break;
//                }
//            }
//            if (isFull) {
//                deleteLine(i);
//            }
//        }
//    }

// 删除一行
//    public void deleteLine(int line){
//        if(line < 0 || line >= Constants.HEIGHT_NUM) return;
//        for (int i = line; i >= 0; i--) {
//            for (int j = 0; j < Constants.WIDTH_NUM; j++) {
//                if (i == 0){
//                    blockArray[i][j] = null;
//                }else{
//                    blockArray[i][j] = blockArray[i - 1][j];
//                    if(blockArray[i][j] != null) {
//                        blockArray[i][j].moveDownOneStep();
//                    }
//                }
//            }
//        }
//        goal++;
//        // 是否满足升级或获胜的条件
//        if(goal >= Constants.LEVEL_GOAL[Constants.LEVEL]){
//            Constants.LEVEL++;
//            if(Constants.LEVEL > Constants.MAX_LEVEL){
//                win();
//                Constants.LEVEL = 0;
//            }
//        }
//    }

// 判断数组是否满了
//    public void checkFull() {
//        for (int i = 0; i < curComponent.blocks.length; i++) {
//            Block b = curComponent.blocks[i];
//            if(b.getY() <= 0){
//                gameOver();
//                break;
//            }
//        }
//    }
