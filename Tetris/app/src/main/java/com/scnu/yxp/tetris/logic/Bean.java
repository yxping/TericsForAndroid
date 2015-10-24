package com.scnu.yxp.tetris.logic;

import com.scnu.yxp.tetris.base.Block;
import com.scnu.yxp.tetris.base.Component;

import java.io.Serializable;

/**
 * 用来保存相应的数据
 * Created by yxp on 2015/10/21.
 */
public class Bean implements Serializable{

    private static final long serialVersionUID = -2969688336489003681L;
    private int goal;
    private int level;
    private Block[][] blocks;
    private Component curComponent;
    private Component nextComponent;
    private boolean isPause;
    private boolean isGameOver;

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public void setCurComponent(Component curComponent) {
        this.curComponent = curComponent;
    }

    public void setNextComponent(Component nextComponent) {
        this.nextComponent = nextComponent;
    }

    public int getGoal() {
        return goal;
    }

    public int getLevel() {
        return level;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public Component getCurComponent() {
        return curComponent;
    }

    public Component getNextComponent() {
        return nextComponent;
    }

    public boolean isPause() {
        return isPause;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }
}
