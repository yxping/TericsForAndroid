package com.scnu.yxp.tetris.component;

import com.scnu.yxp.tetris.base.Block;
import com.scnu.yxp.tetris.base.Component;
import com.scnu.yxp.tetris.logic.Constants;

/**
 * Created by yxp on 2015/10/20.
 */
public class LComponent extends Component {

    public LComponent(int resId, int mode) {
        super(resId, mode);
        int mid = Constants.WIDTH_NUM / 2;
        blocks[0] = new Block(resId, mid, 0);// 中心点
        blocks[1] = new Block(resId, mid - 1, 0);
        blocks[2] = new Block(resId, mid + 1, 0);
        blocks[3] = new Block(resId, mid + 1, -1);
        while(curMode != mode){
            switchMode();
        }
    }

    public void switchMode() {
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
}
